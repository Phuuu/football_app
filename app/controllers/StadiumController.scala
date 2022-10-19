package controllers

import models.Stadium
import services.{AsyncStadiumService, StadiumService}

import javax.inject._
import play.api._
import play.api.data.Form
import play.api.data.Forms.{mapping, number, text}
import play.api.mvc._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.util.hashing.MurmurHash3

case class StadiumData(name: String, city: String, country: String, seats: Int)

class StadiumController @Inject()(
    val controllerComponents: ControllerComponents,
    val stadiumService: AsyncStadiumService
  ) extends BaseController with play.api.i18n.I18nSupport {
  def list() = Action.async { implicit request =>
    val result = stadiumService.findAll()
    result.map(ls => Ok(views.html.stadium.stadiums(ls)))

  }

  val stadiumForm = Form(mapping(
    "name" -> text,
    "city" -> text,
    "country" -> text,
    "seats" -> number
  )
  (StadiumData.apply) //Construction
  (StadiumData.unapply) //Destructuring
  )
  def init(): Action[AnyContent] = Action{ implicit request =>
    Ok(views.html.stadium.create(stadiumForm))
  }

  def create() = Action { implicit request =>
    stadiumForm.bindFromRequest.fold(
      formWithErrors => {
        println("Nay!" + formWithErrors)
        BadRequest(views.html.stadium.create(formWithErrors))
      },
      stadiumData => {
        val id = MurmurHash3.stringHash(stadiumData.name)
        val newId = if (id < 0) id * -1 else id
        val newStadium = models.Stadium(
          newId,
          stadiumData.name,
          stadiumData.city,
          stadiumData.seats
        )
        println("Yay!" + newStadium)
        stadiumService.create(newStadium)
        Redirect(routes.StadiumController.show(newId))
      }
    )
  }

  def show(id: Long) = Action { implicit request =>
    Ok("This is placeholder for this stadium")
  }


}
