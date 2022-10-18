package services
import models.Stadium
import org.mongodb.scala.{Document, MongoClient}

import scala.concurrent.Future

class MongoDBStadiumService extends AsyncStadiumService {
  val mongoClient: MongoClient = MongoClient("mongodb://mongo-root:mongo-password@localhost:" + 27017)
  val myCompanyDatabase = mongoClient.getDatabase("football_app")
  val stadiumCollection = myCompanyDatabase.getCollection("stadium")
  override def create(stadium: Stadium): Unit = {
    val newStadium = stadiumDocument(stadium)
    stadiumCollection.insertOne(newStadium).map(x => x.getInsertedId)
  }

  override def update(id: Long, doc: Document): Future[Stadium] = ???

  override def findById(id: Long): Future[Option[Stadium]] = ???

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
}
