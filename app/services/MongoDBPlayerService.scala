package services

import models._
import org.mongodb.scala._
import org.mongodb.scala.model.Filters.{empty, equal}

import javax.inject.Inject
import scala.util.Try
import scala.concurrent.Future



class MongoDBPlayerService @Inject()(myCompanyDatabase: MongoDatabase) extends AsyncPlayerService {
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
  override def update(player: Player):Future[Option[Player]] = {
    playerCollection.findOneAndUpdate(equal("_id", player.id), Document(
      "$set" -> Document(
        "team" -> player.team.name,
        "position" -> player.position.toString,
        "firstName" -> player.firstName,
        "lastName" -> player.lastName
      )
    )).map(x => documentToPlayer(x)).toSingle().headOption()
  }

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
      case _ => Central
    }
  }

  override def findAll(): Future[List[Player]] = playerCollection.find().map(documentToPlayer).foldLeft(List.empty[Player])((list, player) => player :: list).head()

  override def findByFirstName(firstName: String): Future[List[Player]] = {
    playerCollection.find(equal("firstName", firstName))
      .map(x => documentToPlayer(x))
      .foldLeft(List.empty[Player])((list, player) => player :: list).head()
  }

  override def findByLastName(lastName: String): Future[List[Player]] = {
    playerCollection.find(equal("lastName", lastName))
      .map(x => documentToPlayer(x))
      .foldLeft(List.empty[Player])((list, player) => player :: list).head()
  }

  override def findByPosition(position: Position): Future[List[Player]] = {
    playerCollection.find(equal("position", position))
      .map(x => documentToPlayer(x))
      .foldLeft(List.empty[Player])((list, player) => player :: list).head()
  }

  def documentToATeam(x: Document) = {
    Team(x.getLong("_id"), x.getString("name"), x.get("team")
      .map(v => Stadium(v.asInt64().longValue(), "Fake Name", "Fake Country", -5)))
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
