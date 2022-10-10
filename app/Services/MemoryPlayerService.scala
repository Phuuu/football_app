package Services

import com.fasterxml.jackson.module.scala.deser.overrides.MutableList
import models.Player

import scala.reflect.internal.util.Position

class MemoryPlayerService(players:MutableList[Player]) extends PlayerService {
  override def create(player: Player): Unit = ???

  override def update(player: Player): Unit = ???

  override def findById(id: Long): Unit = ???

  override def findAll(): Unit = ???

  override def findByFirstName(firstName: String): Unit = ???

  override def findByLastName(lastName: String): Unit = ???

  override def findByPosition(position: Position): Unit = ???
}
