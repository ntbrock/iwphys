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
        // Logger.info(s"IwpVersion4CalculatorService:45> time: ${time.getTime}")
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

        var varJso = JsObject(Seq.empty)

        val ignoreFields = Seq("Time.deltaTime", "Time.stopTime", "Time.startTime", "Time.curTime", "Time.endTime",
        "startTime", "curTime", "endTime", "stopTime" )


        while ( {
          varKeyi.hasNext
        }) {
          val key = varKeyi.next().asInstanceOf[String]
          val value = vars.get(key)


          // Ignore the core objects like Time.
          if ( ignoreFields.contains(key) ) { }

          else {


            //Logger.info(s"DProblemController:110> ${key} = ${value}")


            //2018Dec14 Apples to Apples Conversion to match iwp5 nested format. Use a rolling Json DeepMerge

            val keyPath = key.split("[.]")

            if (keyPath.size == 2) {

              // Flatten values
              if ( keyPath(1) == "value" ) {
                varJso = varJso.deepMerge(JsObject(Seq(keyPath(0) -> JsNumber(value))))

              } else {
                varJso = varJso.deepMerge(JsObject(Seq(keyPath(0) -> JsObject(Seq(keyPath(1) -> JsNumber(value))))))
              }



            } else if (keyPath.size == 1) {
              varJso = varJso.deepMerge(JsObject(Seq(keyPath(0) -> JsNumber(value))))

            } else {
              throw new RuntimeException(s"IwpVersion4Calculator:89> Keypath of length ${keyPath.size} > 2 not supported by Taylor in 2018 for key : ${key}")
            }

          }
        }

        val jsonFrame = varJso // JsObject(varMap.toMap.map { case (k, v) => k -> JsNumber(v) })


        frames.enqueue(jsonFrame)

        // Logger.info(s"IwpVersion4CalculatorService:124> Time: ${time.getTime}  VarMap: ${varMap.toMap}")
        ps.tickStepForward()

      }


      // Logger.info(s"IwpVersion4CalculatorService:130> Ok, Complete")

      JsArray(frames)
    }

}
