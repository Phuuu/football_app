package Services

import com.fasterxml.jackson.module.scala.deser.overrides.MutableList
import models.Team

class MemoryTeamService(teams: MutableList[Team]) extends TeamService {
  override def create(team: Team): Unit = ???

  override def update(team: Team): Unit = ???

  override def findById(id: Long): Unit = ???

  override def findAll(): Unit = ???

  override def findByName(name: String): Unit = ???
}
