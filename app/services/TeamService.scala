package services

import models.Team

import scala.util.Try

trait TeamService {
  def create(team: Team): Unit
  def update(team: Team): Try[Team]
  def findById(id: Long): Option[Team]
  def findAll(): List[Team]
  def findByName(name: String): Option[Team]
}
