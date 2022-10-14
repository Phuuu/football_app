package services

import com.fasterxml.jackson.module.scala.deser.overrides.MutableList
import models.Stadium

import javax.inject.{Inject, Named}
import scala.collection.mutable.ListBuffer
import scala.util.Try

class MemoryStadiumService @Inject()(stadiums: ListBuffer[Stadium]) extends StadiumService {
  override def create(stadium: Stadium): Unit = stadiums += stadium

  override def update(stadium: Stadium): Try[Stadium] = {
    Try(stadiums.find(t => t.id == stadium.id).head)
      .map(t => {
        stadiums.filterInPlace(t => t.id != stadium.id)
          .addOne(stadium);
        t
      })
  }


  override def findById(id: Long): Option[Stadium] = stadiums.find(s => s.id == id)

  override def findAll(): List[Stadium] = stadiums.toList

  override def findByCountry(country: String): List[Stadium] = stadiums.filter(s => s.country == country).toList

  override def findByName(name: String): Option[Stadium] = stadiums.find(s => s.name == name)

}
