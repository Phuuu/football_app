package services

import models.Team

trait TeamService {
  def create(team: Team)
  def update(team: Team)
  def findById(id: Long): Option[Team]
  def findAll(): List[Team]
  def findByName(name: String): Option[Team]
}
