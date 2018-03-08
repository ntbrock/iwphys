package controllers

import java.time.LocalDateTime
import java.util
import javax.inject._

import edu.ncssm.iwp.math.designers.MCalculator_Abstract_subDesigner
import edu.ncssm.iwp.math.{MCalculator, MCalculator_Parametric, MDataHistory, MVariables}
import edu.ncssm.iwp.objects.DObject_Solid
import edu.ncssm.iwp.problemdb.{DProblemManager, DProblemState}
import play.api._
import play.api.libs.json.{JsNumber, JsObject, JsString, Json}
import play.api.mvc._


@Singleton
class DProblemController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def index() = Action { implicit request: Request[AnyContent] =>

    // Tests to see if we can initialize old java code.
    val dpm = new DProblemManager()

    val dp = dpm.getEmptyProblem

    val o = new DObject_Solid()


    val calc = new MCalculator_Parametric("1+1+tbwashere")

    // Idea of how to manually build an Animation (aka problem)
    o.name = "Solid"
    o.setCalculatorX( calc )
    o.setCalculatorY( calc )


    val state = new DProblemState(dp)

    val vars = state.vars()

    vars.setAtCurrentTick("tbwashere", 1.0)
    vars.setAtCurrentTick("delta_t", 1.0)


    val calcResult = calc.calculate(vars);

    val js = JsObject(Seq(
      "calcResult"->JsNumber(calcResult),
      "now"-> JsString(LocalDateTime.now.toString)
    ))


    Ok(Json.stringify(js)).as("application/json")
  }
}
