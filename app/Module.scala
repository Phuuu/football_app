import services._
import com.google.inject.AbstractModule
import models.Stadium

import scala.collection.mutable.ListBuffer

class Module extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[StadiumService]).to(classOf[MemoryStadiumService])
    bind(classOf[ListBuffer[_]]).toInstance(ListBuffer.empty[Stadium])
//    bind(classOf[TeamService]).to(classOf[MemoryTeamService])
//    bind(classOf[PlayerService]).to(classOf[MemoryPlayerService])
  }

}
