package controllers

import models._
import services.{AsyncPlayerService, PlayerService}
import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.mvc._

import javax.inject._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.hashing.MurmurHash3

case class PlayerData(team: String, position: String, firstName: String, lastName: String)

class PlayerController @Inject()(
  val controllerComponents: ControllerComponents,
  val playerService: AsyncPlayerService
  ) extends BaseController with play.api.i18n.I18nSupport {

  def list() = Action.async { implicit request =>
    val result = playerService.findAll()
    result.map(r => Ok(views.html.player.players(r)))
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
    case "GoalKeeper" => GoalKeeper
    case "RightFullback" => RightFullback
    case "LeftFullback" => LeftFullback
    case "Sweeper" => Sweeper
    case "Striker" => Striker
    case "HoldingMidfielder" => HoldingMidfielder
    case "RightMidfielder" => RightMidfielder
    case "Central" => Central
    case "AttackingMidfielder" => AttackingMidfielder
    case "LeftMidfielder" => LeftMidfielder
    case _ => Central
  }

  def create(): Action[AnyContent] = Action { implicit request =>
    playerForm.bindFromRequest.fold(
      formWithErrors => {
        println("Nay!" + formWithErrors)
        BadRequest(views.html.player.create(formWithErrors))
      },
      playerData => {
        val id = MurmurHash3.stringHash(playerData.firstName)
        val newId = if (id < 0) id * -1 else id
        val newPlayer = models.Player(
          newId,
          Team(playerData.team, Stadium(10L, "Stamford Bridge", "B", 1203)),
          convertPosition(playerData.position),
          playerData.firstName,
          playerData.lastName
        )
        println("Yay!" + newPlayer)
        playerService.create(newPlayer)
        Redirect(routes.PlayerController.show(newId))
      }
    )
  }

  def show(id: Long) = Action { implicit request =>
    Ok("This is placeholder for this player")
  }



}
