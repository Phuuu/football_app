import akka.parboiled2.RuleTrace.NotPredicate.Base
import play.api.mvc.{BaseController, ControllerComponents}
import services.AsyncTeamService

import javax.inject.Inject

case class TeamData(teamID: Long, name: String, stadium: String)

  class TeamController @Inject()(
      val controllerComponents: ControllerComponents,
      val teamServices: AsyncTeamService
    ) extends BaseController with play.api.i18n.I18nSupport {
    def list() = Action.async { implicit request =>
      val result = teamServices.findAll()
      result.map(ls => Ok(views.html.team.teams(ls)))
    }
  }