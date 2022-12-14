import com.google.inject._
import models.Stadium
import org.mongodb.scala.{MongoClient, MongoDatabase}
import play.api.Configuration
import services.{AsyncPlayerService, AsyncStadiumService, AsyncTeamService, MemoryStadiumService, MongoDBPlayerService, MongoDBStadiumService, MongoDBTeamService, StadiumService}

import scala.collection.mutable.ListBuffer

class Module extends AbstractModule {

  override def configure(): Unit = {

    @Provides
    def databaseProvider(configuration: Configuration): MongoDatabase = {
      val username = configuration.get[String]("mongo.username")
      val password = configuration.get[String]("mongo.password")
      val database = configuration.get[String]("mongo.database")
      val url = configuration.get[String]("mongo.host")
      val port = configuration.get[String]("mongo.port")
      val mongoClient: MongoClient = MongoClient(
        s"mongodb://$username:$password@$url:$port"
      )
      mongoClient.getDatabase(database)
    }


    bind(classOf[StadiumService]).to(classOf[MemoryStadiumService]).in(classOf[javax.inject.Singleton])
    bind(classOf[ListBuffer[_]]).toInstance(ListBuffer.empty[Stadium])
//    bind(classOf[TeamService]).to(classOf[MemoryTeamService])
    bind(classOf[AsyncPlayerService]).to(classOf[MongoDBPlayerService])
    bind(classOf[AsyncStadiumService]).to(classOf[MongoDBStadiumService])
    bind(classOf[AsyncTeamService]).to(classOf[MongoDBTeamService])


  }


}
