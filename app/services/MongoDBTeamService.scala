package services

import models.{Stadium, Team}
import org.mongodb.scala.{Document, MongoClient}
import org.mongodb.scala.model.Filters.equal

import scala.concurrent.Future

class MongoDBTeamService extends AsyncTeamService {
  val mongoClient: MongoClient = MongoClient("mongodb://mongo-root:mongo-password@localhost:" + 27017)
  val myCompanyDatabase = mongoClient.getDatabase("football_app")
  val teamCollection = myCompanyDatabase.getCollection("team")

  override def create(team: Team): Unit = {
    val document: Document = Document(
      "_id" -> team.id,
      "name" -> team.name,
      "stadium" -> team.stadium.toString
    )

    teamCollection
      .insertOne(document)
      .map(r => r.getInsertedId.asInt64().longValue())
      .head()
  }

  override def update(team: Team): Future[Option[Team]] = {
    val namedSomething: Document = Document(
      "_id" -> team.id,
      "name" -> team.name,
      "stadium" -> team.stadium.id
    )
    teamCollection.findOneAndUpdate(equal("_id", team.id), namedSomething).map(a => documentToATeam(a)).toSingle().headOption()
  }


  override def findById(id: Long): Future[Option[Team]] = ???

  override def findAll(): Future[List[Team]] = ???

  override def findByName(name: String): Future[Option[Team]] = ???

  def documentToATeam(x: Document) = {
    Team(
      x.getLong("_id"),
      x.getString("name"),
      documentToAStadium(x.get("stadium").map(x => Document(x.asDocument())).get)
      )
  }

  def documentToAStadium(x: Document) = {
    Stadium(
      x.getLong("_id"),
      x.getString("name"),
      x.getString("country"),
      x.getInteger("seats")
    )
  }

  def teamToADocument(x: Document) = {

  }
}

