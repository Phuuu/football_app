package Services

import com.fasterxml.jackson.module.scala.deser.overrides.MutableList
import models.Stadium

class MemoryStadiumService(stadium: MutableList[Stadium]) extends StadiumService {
  override def create(stadium: Stadium): Unit = ???

  override def update(stadium: Stadium): Unit = ???

  override def findById(id: Long): Unit = ???

  override def findAll(): Unit = ???

  override def findByCountry(firstName: String): Unit = ???
}
