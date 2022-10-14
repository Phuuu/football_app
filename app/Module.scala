import com.google.inject.AbstractModule
import com.google.inject.name.Names
import models.Stadium
import services.{AsyncPlayerService, MemoryStadiumService, MongoDBPlayerService, StadiumService}

import scala.collection.mutable.ListBuffer

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[StadiumService]).to(classOf[MemoryStadiumService]).in(classOf[javax.inject.Singleton])
    bind(classOf[ListBuffer[_]]).toInstance(ListBuffer.empty[Stadium])
//    bind(classOf[TeamService]).to(classOf[MemoryTeamService])
    bind(classOf[AsyncPlayerService]).to(classOf[MongoDBPlayerService])
  }


}
