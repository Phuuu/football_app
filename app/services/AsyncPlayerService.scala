package services

import models.{Player, Position}

import scala.concurrent.Future
import scala.util.Try

trait AsyncPlayerService {
  def create(player: Player): Future[Long]

  def update(player: Player): Future[Option[Player]]

  def findById(id: Long): Future[Option[Player]]

  def findAll(): Future[List[Player]]

  def findByFirstName(firstName: String): Future[List[Player]]

  def findByLastName(lastName: String): Future[List[Player]]

  def findByPosition(position: Position): Future[List[Player]]

}
