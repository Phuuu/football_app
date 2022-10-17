package services

import models._
import org.mongodb.scala._
import org.mongodb.scala.model.Filters.{empty, equal}

import scala.util.Try
import scala.concurrent.Future



class MongoDBPlayerService extends AsyncPlayerService {
  val mongoClient: MongoClient = MongoClient("mongodb://mongo-root:mongo-password@localhost:" + 27017)
  val myCompanyDatabase = mongoClient.getDatabase("football_app")
  val playerCollection = myCompanyDatabase.getCollection("players")

  override def create(player: Player): Future[Long] = {
    val document: Document = Document(
      "_id" -> player.id,
      "team" -> player.team.name,
    "position" -> player.position.toString,
    "firstName" -> player.firstName,
    "lastName" -> player.lastName
    )

    playerCollection
      .insertOne(document)
      .map(r => r.getInsertedId.asInt64().longValue())
      .head()
  }

  // todo
  override def update(player: Player):Future[Try[Player]] = ???

  override def findById(id: Long): Future[Option[Player]] = {
    playerCollection
      .find(equal("_id", id))
      .map { d =>
        documentToPlayer(d)
      }
      .toSingle()
      .headOption()
  }

  private def documentToPlayer(x : Document) = {
    Player(
      x.getLong("_id"),
      documentToATeam(x.get("team").map(d => Document(d.asDocument())).get),
      stringToPosition(x.getString("position")),
      x.getString("firstName"),
      x.getString("lastName"),
    )
  }

  private def stringToPosition(string: String): Position = {
    string match {
      case "GoalKeeper" => GoalKeeper
      case "RightFullback" => RightFullback
      case "LeftFullback" => LeftFullback
      case "CenterBack" => CenterBack
      case "Sweeper" => Sweeper
      case "Striker" => Striker
      case "HoldingMidfielder" => HoldingMidfielder
      case "RightMidfielder" => RightFullback
      case "Central" => Central
      case "AttackingMidfielder" => AttackingMidfielder
      case "LeftMidfielder" => LeftFullback
    }
  }

  override def findAll(): Future[List[Player]] = playerCollection.find().map(documentToPlayer).foldLeft(List.empty[Player])((list, player) => player :: list).head()

  override def findByFirstName(firstName: String): Future[List[Player]] = ??? // this neeed doing one again

  override def findByLastName(lastName: String): Future[List[Player]] = ???

  override def findByPosition(position: Position): Future[List[Player]] = ???

  def documentToATeam(x: Document) = {
    Team(x.getLong("_id"), x.getString("name"), documentToAStadium(x.get("team").map(d => Document(d.asDocument())).get))
  }

  def documentToAStadium(x: Document) = {
    Stadium(
      x.getLong("_id"),
      x.getString("name"),
      x.getString("country"),
      x.getInteger("capacity")
    )

  }
}
