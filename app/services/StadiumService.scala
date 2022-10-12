package services

import models.Stadium

import scala.util.Try

trait StadiumService {
  def create(stadium:Stadium)

  def update(stadium:Stadium): Try[Stadium]

  def findById(id:Long): Option[Stadium]

  def findAll(): List[Stadium]

  def findByCountry(country:String): List[Stadium]

  def findByName(name: String): Option[Stadium]

}
