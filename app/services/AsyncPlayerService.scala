package services

import models.Player

import scala.concurrent.Future
import scala.reflect.internal.util.Position
import scala.util.Try

trait AsyncPlayerService {
  def create(player: Player): Future[Unit]

  def update(player: Player): Future[Try[Player]]

  def findById(id: Long): Future[Option[Player]]

  def findAll(): Future[List[Player]]

  def findByFirstName(firstName: String): Future[List[Player]]

  def findByLastName(lastName: String): Future[List[Player]]

  def findByPosition(position: Position): Future[List[Player]]

  override def findByPosition(position: Position): Future[List[Player]]
}
