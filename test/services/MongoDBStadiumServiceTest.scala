package services

import com.dimafeng.testcontainers.{Container, ForAllTestContainer, MongoDBContainer}
import org.mongodb.scala.{Document, MongoClient}
import org.scalatestplus.play.PlaySpec

class MongoDBStadiumServiceTest extends PlaySpec with ForAllTestContainer{

  override def container: MongoDBContainer = new MongoDBContainer()

  "MongoDB Stadium service" must{
    "Create a new stadium" in{
      pending
      val mongoClient: MongoClient = MongoClient(container.container.getConnectionString)
      val footballAppDB = mongoClient.getDatabase("football_app")
      val stadiumCollection = footballAppDB.getCollection("stadium")

      val document = Document("_id" -> 30L, "name" -> "Stus Stadium", "country" -> "United Stuart of Awesomica", "seats" -> 5)
      stadiumCollection.insertOne(document).subscribe(x => println(x), y => y.printStackTrace(), () => println("Done!"))
    }
  }
}
