package Services

import models.{Stadium, Team}
import org.scalatestplus.play.PlaySpec

import scala.collection.mutable.ListBuffer
import scala.util.Failure

class MemoryTeamServiceTest extends PlaySpec{
  "Memory Teamservis" must{
    "return the list I provide it" in {
      val memoryTeamService = new MemoryTeamService(ListBuffer.empty)
      val team = new Team(12L,"Arsenal", new Stadium(3434,"London","UK", 400))
      memoryTeamService.create(team)
      memoryTeamService.findAll().size mustBe(1)
    }
    "find a team by Id" in{
      val memoryTeamService = new MemoryTeamService(ListBuffer.empty)
      val arsenal = Team(12L, "Arsenal", Stadium(3434,"London","UK", 400))
      val chelsea = Team(10L, "Chelsea", Stadium(3535,"London","UK", 450))
      memoryTeamService.create(arsenal)
      memoryTeamService.create(chelsea)
      val result = memoryTeamService.findByName("Arsenal")
      result mustBe (Some(arsenal))

    }
    "find a team by Id" in{
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
