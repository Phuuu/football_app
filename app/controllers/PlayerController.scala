package controllers

import models._
import services.PlayerService
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.mvc._


import javax.inject._

case class PlayerData(team: String, position: String, firstName: String, lastName: String)

class PlayerController @Inject()(
  val controllerComponents: ControllerComponents,
//  val playerService: PlayerService
  ) extends BaseController with play.api.i18n.I18nSupport {

  def list() = Action { implicit request =>
    Ok("Well done!, You cracked it!")
  }

  val playerForm = Form(mapping(
    "team" -> text,
    "position" -> text,
    "firstName" -> text,
    "lastName" -> text
  )
  (PlayerData.apply) //Construction
  (PlayerData.unapply) //Destructuring
  )

  def init(): Action[AnyContent] = Action { implicit request =>
    Ok(views.html.player.create(playerForm))
  }
  def convertPosition(s:String): Position = s match {
    case "CenterBack" => CenterBack
  }

  def create(): Action[AnyContent] = Action { implicit request =>
    playerForm.bindFromRequest.fold(
      formWithErrors => {
        println("Nay!" + formWithErrors)
        BadRequest(views.html.player.create(formWithErrors))
      },
      playerData => {
        val newPlayer = models.Player(
          Team(playerData.team, Stadium(10L, "Stamford Bridge", "B", 1203)),
          convertPosition(playerData.position),
          playerData.firstName,
          playerData.lastName
        )
        println("Yay!" + newPlayer)
//        playerService.create(newPlayer)
        Redirect(routes.StadiumController.show(10))
      }
    )
  }
}
