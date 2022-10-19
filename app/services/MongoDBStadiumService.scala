package services
import models.Stadium
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.{Document, MongoClient, MongoDatabase}

import javax.inject.Inject
import scala.concurrent.Future

class MongoDBStadiumService @Inject()(myCompanyDatabase: MongoDatabase) extends AsyncStadiumService {
//  val mongoClient: MongoClient = MongoClient("mongodb://mongo-root:mongo-password@localhost:" + 27017)
//  val myCompanyDatabase = mongoClient.getDatabase("football_app")
  val stadiumCollection = myCompanyDatabase.getCollection("stadium")
  override def create(stadium: Stadium): Unit = {
    val newStadium = stadiumDocument(stadium)
    stadiumCollection.insertOne(newStadium).map(x => x.getInsertedId.asInt64().longValue()).head()
  }

  override def update(id: Long, doc: Document): Future[Stadium] = ???

  override def findById(id: Long): Future[Option[Stadium]] = {
    stadiumCollection.find(equal("_id",id)).map(a => documentStadium(a))
  }.toSingle().headOption()

  override def findAll(): Future[List[Stadium]] = ???

  override def findByCountry(country: String): Future[List[Stadium]] = ???

  override def findByName(name: String): Future[Option[Stadium]] = ???

  def stadiumDocument(x: Stadium): Document = {
    Document(
      "_id" -> x.id,
      "name" -> x.name,
      "country" -> x.country,
      "seats" -> x.seats
    )
  }

  def documentStadium(x: Document) = {
    Stadium(
     x.getLong("_id"),
      x.getString("name"),
      x.getString("country"),
      x.getInteger("seats")
    )
  }


}
