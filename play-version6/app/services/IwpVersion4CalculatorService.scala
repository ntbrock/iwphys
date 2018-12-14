package services

import edu.ncssm.iwp.objects.DObject_Time
import edu.ncssm.iwp.plugin.IWPAnimated
import edu.ncssm.iwp.problemdb.{DProblemManager, DProblemState}
import javax.inject.Singleton
import play.api.Logger
import play.api.libs.json.{JsArray, JsNumber, JsObject, Json}

import scala.collection.mutable

@Singleton
class IwpVersion4CalculatorService {


  // Tests to see if we can initialize old java code.
  val dpm = new DProblemManager()

  def animateToJsonFrames(path: String): JsArray = {

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


      val frames = mutable.Queue[JsObject]()
      val maxFrame = 1000

      while (time.getTime < time.getStopTime && frames.size < maxFrame) {
        Logger.info(s"DProblemController:86> time: ${time.getTime}")
        time.tick(ps)
        val t = time.getTime

        // Now tick everything else in teh animation

        val i = p.getObjectsForTicking.iterator
        while ( {
          i.hasNext
        }) {
          val o = i.next.asInstanceOf[Any]
          if (o.isInstanceOf[IWPAnimated] && !o.isInstanceOf[DObject_Time]) { // don't do time twice.
            val animated = o.asInstanceOf[IWPAnimated]
            animated.tick(ps)
          }
        }


        // Render the current variables as a map

        val varMap = new mutable.HashMap[String, Double]


        val vars = ps.vars.getCurrentTickVars
        val varKeyi = vars.iterator()
        while ( {
          varKeyi.hasNext
        }) {
          val key = varKeyi.next().asInstanceOf[String]
          val value = vars.get(key)

          varMap.put(key, value)
          //Logger.info(s"DProblemController:110> ${key} = ${value}")
        }

        val jsonFrame = JsObject(varMap.toMap.map { case (k, v) => k -> JsNumber(v) })


        frames.enqueue(jsonFrame)

        Logger.info(s"DProblemConroller:88> Time: ${time.getTime}  VarMap: ${varMap.toMap}")
        ps.tickStepForward()

      }


      Logger.info(s"DProblemController:130> Ok, Complete")

      JsArray(frames)
    }

}
