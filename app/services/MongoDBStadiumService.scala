package services
import models.Stadium
import org.mongodb.scala.model.Aggregates.set
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.{Document, MongoClient, MongoDatabase, model}

import javax.inject.Inject
import scala.concurrent.Future

class MongoDBStadiumService @Inject()(myCompanyDatabase: MongoDatabase) extends AsyncStadiumService {
  val stadiumCollection = myCompanyDatabase.getCollection("stadium")
  override def create(stadium: Stadium): Unit = {
    val newStadium = stadiumDocument(stadium)
    stadiumCollection.insertOne(newStadium).map(x => x.getInsertedId.asInt64().longValue()).head()
  }

  override def update(id: Long, doc: Document): Future[Stadium] = {
    stadiumCollection.findOneAndUpdate(equal("_id", id),
      set(
        model.Field("name", doc("name")),
        model.Field("city", doc("city")),
        model.Field("country", doc("country")),
        model.Field("seats", doc("seats"))
      )
    )
      .map(d => documentStadium(d))
      .toSingle()
      .head()

  }

  override def findById(id: Long): Future[Option[Stadium]] = {
    stadiumCollection.find(equal("_id",id)).map(a => documentStadium(a)).toSingle().headOption()
  }

  override def findAll(): Future[List[Stadium]] = {
    stadiumCollection.find().map(x => documentStadium(x)).foldLeft(List.empty[Stadium])((list, stadium) => stadium :: list).head()
  }

  override def findByCountry(country: String): Future[List[Stadium]] = {
    stadiumCollection.find(equal("country", country))
      .map(x => documentStadium(x))
      .foldLeft(List.empty[Stadium])((list, stadium) => stadium :: list).head()
  }

  override def findByName(name: String): Future[Option[Stadium]] = {
    stadiumCollection.find(equal("name", name)).map(a => documentStadium(a))
  }.toSingle().headOption()

  def stadiumDocument(x: Stadium): Document = {
    Document(
      "_id" -> x.id,
      "name" -> x.name,
      "country" -> x.country,
      "seats" -> x.seats
    )
  }

  def documentStadium(x: Document) = {
    Stadium(
     x.getLong("_id"),
      x.getString("name"),
      x.getString("country"),
      x.getInteger("seats")
    )
  }


}
