package services

import com.dimafeng.testcontainers.{Container, ForAllTestContainer, MongoDBContainer}
import models.Stadium
import org.mongodb.scala.{Document, MongoClient}
import org.scalatestplus.play.PlaySpec

class MongoDBStadiumServiceTest extends PlaySpec with ForAllTestContainer{

  override def container: MongoDBContainer = new MongoDBContainer()

  "MongoDB Stadium service" must{
    "Create a new stadium" in{
      pending
      container.start()
      val mongoClient: MongoClient = MongoClient(container.container.getConnectionString)
      val footballAppDB = mongoClient.getDatabase("football_app")
      val stadiumCollection = footballAppDB.getCollection("stadium")
      val mongoDBStadiumService = new MongoDBStadiumService(footballAppDB)

      val testing = mongoDBStadiumService.create(Stadium(30L,"Stus Stadium", "United Stuart of Awesomica",5))
      mongoDBStadiumService.findById(30L) mustBe(testing)

    }
  }

}
