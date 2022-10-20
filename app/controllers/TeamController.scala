package controllers

import org.mongodb.scala.MongoDatabase
import org.mongodb.scala.model.Aggregates
import play.api.data.Form
import play.api.data.Forms.{longNumber, mapping, text}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.{AsyncStadiumService, AsyncTeamService, TeamStadiumView}

import java.util
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.jdk.CollectionConverters._
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

    def init(): Action[AnyContent] = Action.async { implicit request =>
      stadiumService.findAll().map(x => Ok(views.html.team.create(teamForm, x)))
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

    def show(id: Long): Action[AnyContent] = Action.async { implicit request =>
      val teamCollection = mongoDatabase
        .getCollection("teams")
      val aggregated = teamCollection.aggregate(
        Seq(
          Aggregates
            .lookup("stadiums", "stadium", "_id", "stadiumArray")
        )
      )

      val stageOne = aggregated
        .map(d => {
          val stadiumName = d
            .getList("stadiumArray", classOf[util.Map[_, _]])
            .asScala.head.get("name")
          TeamStadiumView(
            d.getLong("_id"),
            d.getString("name"),
            stadiumName.asInstanceOf[String],
            d.getLong("stadium")
          )
        })
      stageOne.subscribe(t => println(t), t => t.printStackTrace(), () => println("done"))
      val eventualMaybeView = stageOne.toSingle().headOption()
      eventualMaybeView
        .map {
                case Some(teams) => Ok(views.html.team.show(teams))
                case None => NotFound("Team not found")
              }
    }
  }