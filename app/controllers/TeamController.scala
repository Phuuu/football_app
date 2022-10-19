package controllers

import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Aggregates._
import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, text}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.{AsyncStadiumService, AsyncTeamService}

import scala.concurrent.ExecutionContext.Implicits.global
import javax.inject.Inject
import scala.concurrent.Future
import scala.util.hashing.MurmurHash3

case class TeamData(name: String, stadiumId: Long)

  class TeamController @Inject()(
                                  val controllerComponents: ControllerComponents,
                                  val teamService: AsyncTeamService,
                                  val stadiumService: AsyncStadiumService,
                                  mongoDatabase: MongoDatabase
    ) extends BaseController with play.api.i18n.I18nSupport {
    def list() = Action.async { implicit request =>
      val result = teamService.findAll()
      result.map(ls => Ok(views.html.team.teams(ls)))
    }

    def init(): Action[AnyContent] = Action { implicit request =>
      Ok(views.html.team.create(teamForm))
    }

    val teamForm = Form(mapping(
      "name" -> text,
      "stadiumId" -> longNumber
    )
    (TeamData.apply) //Construction
    (TeamData.unapply) //Destructuring
    )

    def create() = Action.async { implicit request =>

      teamForm.bindFromRequest.fold(
        formWithErrors => {
          println("Nay!" + formWithErrors)
          stadiumService
            .findAll()
            .map(xs => BadRequest(views.html.team.create(formWithErrors, xs)))
        },
        teamData => {
          val maybeStadium = stadiumService.findById(teamData.stadiumId)
          val id = MurmurHash3.stringHash(teamData.name)
          maybeStadium
            .map { s =>
              models.Team(
                id,
                teamData.name,
                s match {
                  case Some(stadium) => stadium.id
                }
              )
            }
            .map { t =>
              teamService.create(t)
              Redirect(routes.TeamController.show(t.id))
            }
        }
      )
    }

    def show(id: Long) = Action.async { implicit request =>
      mongoDatabase
        .getCollection("teams")
        .aggregate(
          List(
            lookup("stadiums", "stadium", "_id", "stadiumDetails"),
            out("temp")
          )
        )
        .toSingle()
        .headOption()
        .flatMap {
          case Some(teamInfo) =>
            teamService
              .findById(id)
              .map {
                case Some(team) => Ok(views.html.team.show(team.id, teamInfo))
                case None => NotFound("Team not found")
              }
          case None => Future(NotFound("Team not found"))
        }

    }
  }