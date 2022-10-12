package Services

import models.{Stadium, Team}
import org.scalatestplus.play.PlaySpec

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
      pending
      val memoryTeamService = new MemoryTeamService(ListBuffer.empty)
      val arsenal = Team(12L, "Arsenal", Stadium(3434,"London","UK", 400))
      val chelsea = Team(10L, "Chelsea", Stadium(3535,"London","UK", 450))
      memoryTeamService.create(arsenal)
      memoryTeamService.create(chelsea)
      val result = memoryTeamService.findByName("Arsenal")
      result mustBe (Some(arsenal))

    }
    "some todo two" in{
      pending
      val memoryTeamService = new MemoryTeamService(ListBuffer.empty)
      val arsenal = Team(12L, "Arsenal", Stadium(3434,"London","UK", 400))
      val chelsea = Team(10L, "Chelsea", Stadium(3535,"London","UK", 450))
      memoryTeamService.create(arsenal)
      memoryTeamService.create(chelsea)
      val maybeTeam = memoryTeamService.findById(12L).get
      val updateNew = arsenal.copy(stadium = Stadium(3535,"London","UK", 450))
      val result = memoryTeamService.update(updateNew)
      result mustBe Failure(new NoSuchElementException("Team is not is the list "))

    }
  }


}
