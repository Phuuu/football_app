package services

import com.fasterxml.jackson.module.scala.deser.overrides.MutableList
import models.{Player, Position}

import javax.inject.Inject
import scala.collection.mutable.ListBuffer
import scala.util.Try

class MemoryPlayerService @Inject()(val players:ListBuffer[Player]) extends PlayerService {
  override def create(player: Player): Unit = players += player

  override def update(player: Player): Try[Player] = {
    Try(players.find(t => t.id == player.id).head)
      .map(t => {
        players.filterInPlace(t => t.id != player.id)
          .addOne(player);
        t
      })
  }
  override def findById(id: Long): Option[Player] = players.find(t => t.id == id)

  override def findAll(): List[Player] = players.toList

  override def findByFirstName(firstName: String): List[Player] = players.filter(p => p.firstName == firstName).toList

  override def findByLastName(lastName: String): List[Player] = players.filter(p => p.lastName == lastName).toList

  override def findByPosition(position: Position): List[Player] = players.filter(p => p.position == position).toList
}
