package services

import models.{Stadium, Team}
import org.scalatestplus.play.PlaySpec
import services.{MemoryStadiumService, MemoryTeamService}

import scala.collection.mutable.ListBuffer
import scala.util.Failure

class MemoryStadiumServiceTest extends PlaySpec{
  "Memory Stadium service" must{
    "return the size of the list after I create the stadium" in {
      val memoryStadiumService = new MemoryStadiumService(ListBuffer.empty)
      val stadium = new Stadium(123L, "Paris", "France", 500)
      memoryStadiumService.create(stadium)
      memoryStadiumService.findAll().size mustBe(1)
    }


    "Some todo" in{
      val memoryTeamService = new MemoryTeamService()
      val arsenal = Team(12L, "Arsenal", 334L)
      val chelsea = Team(10L, "Chelsea", 345L)
      memoryTeamService.create(arsenal)
      memoryTeamService.create(chelsea)
      val result = memoryTeamService.findByName("Arsenal")
      result mustBe (Some(arsenal))

    }

  }


}
