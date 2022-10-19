package services

import models.{Stadium, Team}
import org.mongodb.scala.{Document, MongoClient, MongoDatabase}
import org.mongodb.scala.model.Filters.equal

import javax.inject.Inject
import scala.concurrent.Future

class MongoDBTeamService @Inject()(myCompanyDatabase: MongoDatabase) extends AsyncTeamService {
  val teamCollection = myCompanyDatabase.getCollection("team")

  override def create(team: Team): Unit = {
    val newTeam = teamToADocument(team)

    teamCollection
      .insertOne(newTeam)
      .map(r => r.getInsertedId.asInt64().longValue())
      .head()
  }

  override def update(team: Team): Future[Option[Team]] = {
    val namedSomething: Document = Document(
      "_id" -> team.id,
      "name" -> team.name,
      "stadium" -> team.stadiumId.id
    )
    teamCollection.findOneAndUpdate(equal("_id", team.id), namedSomething).map(a => documentToATeam(a)).toSingle().headOption()
  }

  override def findById(id: Long): Future[Option[Team]] = {
    teamCollection.find(equal("_id", id)).map(a => documentToATeam(a)).toSingle().headOption()
  }

  override def findAll(): Future[List[Team]] = {
    teamCollection.find().map(x => documentToATeam(x)).foldLeft(List.empty[Team])((list, team) => team :: list).head()
  }

  override def findByName(name: String): Future[Option[Team]] = {
    teamCollection.find(equal("name", name)).map(a => documentToATeam(a))
  }.toSingle().headOption()

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

  def teamToADocument(x: Team): Document = {
    Document(
      "_id" -> x.id,
      "name" -> x.name,
      "stadium" -> x.stadiumId.name,
    )
  }
}

