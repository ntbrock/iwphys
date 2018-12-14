package controllers

import java.time.LocalDateTime
import java.util

import javax.inject._
import edu.ncssm.iwp.math.designers.MCalculator_Abstract_subDesigner
import edu.ncssm.iwp.math.{MCalculator, MCalculator_Parametric, MDataHistory, MVariables}
import edu.ncssm.iwp.objects.{DObject_Solid, DObject_Time}
import edu.ncssm.iwp.plugin.IWPAnimated
import edu.ncssm.iwp.problemdb.{DProblemManager, DProblemState}
import play.api._
import play.api.libs.json._
import play.api.mvc._
import services.IwpVersion4CalculatorService

import scala.collection.mutable


@Singleton
class DProblemController @Inject()(cc: ControllerComponents,
                                   iwpVersion4CalculatorService: IwpVersion4CalculatorService) extends AbstractController(cc) {



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


    val ps = new DProblemState(dp)

    val vars = ps.vars()

    vars.setAtCurrentTick("tbwashere", 999999.0)
    vars.setAtCurrentTick("delta_t", 1.0)


    val calcResult = calc.calculate(vars)

    val js = JsObject(Seq(
      "calcResult"->JsNumber(calcResult),
      "now"-> JsString(LocalDateTime.now.toString)
    ))


    Ok(Json.stringify(js)).as("application/json")
  }



  def view(path: String) = Action { implicit request =>

    val frames = iwpVersion4CalculatorService.animateToJsonFrames(path)
    Ok(Json.stringify(frames)).as("application/json")

  }
}
