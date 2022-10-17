package services

import com.fasterxml.jackson.module.scala.deser.overrides.MutableList
import models.Team

import scala.collection.mutable.ListBuffer
import scala.util.Try

class MemoryTeamService extends TeamService {

  val whatYouPlease: ListBuffer[Team] = ListBuffer.empty
  override def create(team: Team): Unit = whatYouPlease += team

  override def update(team: Team): Try[Team] = {
    Try(whatYouPlease.find(n => n.id == team.id).head).map(m => {
      whatYouPlease.filterInPlace(n => n.id != team.id).addOne(team)
      m
    })
  }

  override def findById(id: Long): Option[Team] = whatYouPlease.find(n => n.id == id)

  override def findAll(): List[Team] = whatYouPlease.toList

  override def findByName(name: String): Option[Team] = whatYouPlease.find(n => n.name == name)
}
