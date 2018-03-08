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
import play.api.libs.json.{JsNumber, JsObject, JsString, Json}
import play.api.mvc._

import scala.collection.mutable


@Singleton
class DProblemController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  // Tests to see if we can initialize old java code.
  val dpm = new DProblemManager()


  def index() = Action { implicit request: Request[AnyContent] =>


    val dp = dpm.getEmptyProblem

    val o = new DObject_Solid()


    val calc = new MCalculator_Parametric("1+1+tbwashere")

    // Idea of how to manually build an Animation (aka problem)
    o.name = "Solid"
    o.setCalculatorX( calc )
    o.setCalculatorY( calc )


    val ps = new DProblemState(dp)

    val vars = ps.vars()

    vars.setAtCurrentTick("tbwashere", 1.0)
    vars.setAtCurrentTick("delta_t", 1.0)


    val calcResult = calc.calculate(vars)

    val js = JsObject(Seq(
      "calcResult"->JsNumber(calcResult),
      "now"-> JsString(LocalDateTime.now.toString)
    ))


    Ok(Json.stringify(js)).as("application/json")
  }


  def view(path: String) = Action { implicit request =>

    val p = dpm.loadFile(path)

    val ps = new DProblemState(p)

    // 2018Mar07 - This logic needs to be ported to IWP5
    p.reorderProblemObjectsBySymbolicDependency()

    // zero all the objects in the problem.

    ps.setTickInProgress(true)
    // This sets the forward direction, previously connect to the play button in swing


// This actually advances the clock
//    ps.tickStepForward()


    val time = p.getTimeObject

    val frames = mutable.Queue[(Double, JsObject)]()

    while (time.getTime < time.getStopTime ) {

      time.tick(ps)
      val t = time.getTime

      // Now tick everything else in teh animation

      val i = p.getObjectsForTicking.iterator
      while ( {
        i.hasNext
      }) {
        val o = i.next.asInstanceOf[Any]
        if (o.isInstanceOf[IWPAnimated] && ! o.isInstanceOf[DObject_Time]  ) { // don't do time twice.
          val animated = o.asInstanceOf[IWPAnimated]
          animated.tick(ps)
        }
      }


      // Render the current variables as a map

      val varMap = new mutable.HashMap[String,Double]


      val vars = ps.vars.getCurrentTickVars
      val varKeyi = vars.iterator()
      while ( {varKeyi.hasNext}) {
        val key = varKeyi.next().asInstanceOf[String]
        val value = vars.get(key)

        varMap.put(key,value)
        //Logger.info(s"DProblemController:110> ${key} = ${value}")
      }

      val jsonMap = JsObject(varMap.toMap.map{ case(k,v) => k -> JsNumber(v) })


      frames.enqueue((t, jsonMap))

      Logger.info(s"DProblemConroller:88> Time: ${time.getTime}  VarMap: ${varMap.toMap}")
      ps.tickStepForward()

    }


    Ok(s"View: ${path}\n\nframes:\n${frames}\n\np : ${p}")

  }


}
