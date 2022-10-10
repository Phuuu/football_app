package Services

import models.{Player}

import scala.reflect.internal.util.Position

trait PlayerService {
  def create(player: Player)

  def update(player: Player)

  def findById(id: Long)

  def findAll()

  def findByFirstName(firstName: String)

  def findByLastName(lastName: String)

  def findByPosition(position: Position)
}
