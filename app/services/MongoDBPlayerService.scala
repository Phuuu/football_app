package services
import models.{Player,  Position}
import scala.util.Try


import scala.concurrent.Future


class MongoDBPlayerService extends AsyncPlayerService {
  val mongoClient: MongoClient = MongoClient("mongodb://mongo-root:mongo-password@localhost:" + 27017)
  val myCompanyDatabase = mongoClient.getDatabase("football_app")
  val playerCollection = myCompanyDatabase.getCollection("players")

  override def create(player: Player):Future[Unit] = {
    val document: Document = playerToDocument(player)

    playerCollection
      .insertOne(document)
      .map(r => r.getInsertedId.asInt64().longValue())
      .head()
  }

  override def update(player: Player):Future[Try[Player]] = ???

  override def findById(id: Long): Future[Option[Player]] = {
    playerCollection
      .find(equal("_id", id))
      .map { d =>
        documentToStadium(d)
      }
      .toSingle()
      .headOption()
  }


  override def findAll(): Future[List[Player]] = playerCollection

  override def findByFirstName(firstName: String): Future[List[Player]]

  override def findByLastName(lastName: String): Future[List[Player]]

  private def playerToDocument(player: Player) = {
    Document(
      "_id" -> player.id,
      "firstName" -> player.firstName,
      "lastName" -> player.lastName,
      "position" -> player.position,
      "team" -> player.team
    )
  }



}
