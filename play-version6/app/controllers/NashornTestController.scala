package controllers

import java.io.FileReader

import javax.inject._
import models.Iwp5Animation
import org.mongodb.scala.model.Filters._
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._
import services.IwpMongoClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import javax.script.{Invocable, ScriptEngine, ScriptEngineManager}

/**
  * This controller is Taylor's first integration demo to Nashhorn Javascript engine
  * Tutorial: https://winterbe.com/posts/2014/04/05/java8-nashorn-tutorial/
 */
@Singleton
class NashornTestController @Inject()(cc: ControllerComponents, mongo: IwpMongoClient) extends AbstractController(cc) {



  def nashornTest1_eval() = Action { implicit request: Request[AnyContent] =>


    val engine = new ScriptEngineManager().getEngineByName("nashorn")

    val js = "1+1;"

    val result = engine.eval(js)

    Ok(s"Nashorn Test 1: result: ${result}")

  }


  // Read + Execute from a file.

  def nashornTest2_file() = Action { implicit request: Request[AnyContent] =>

    val engine = new ScriptEngineManager().getEngineByName("nashorn")

    val fr = new FileReader("public/javascripts/test/nashorn/nashornTest2.js")

    val eval = engine.eval(fr)


    val invokable = engine.asInstanceOf[Invocable]

    Logger.info(s"NashornTestController:51> invokable: ${invokable}")
    val r = invokable.invokeFunction("nashhornTest2js", "argument")

    Ok(s"Nashorn Test 2: result: ${eval},  function call to nashhornTest2js: ${r} ")

  }





  // Math.js

  def nashornTest3_loadMathJs() = Action { implicit request: Request[AnyContent] =>

    val engine = new ScriptEngineManager().getEngineByName("nashorn")


    val fr1 = new FileReader("public/javascripts/math.min.js")
    val eval1 = engine.eval(fr1)

    val fr2 = new FileReader("public/javascripts/test/nashorn/nashornTest3.js")
    val eval2 = engine.eval(fr2)


    val invokable = engine.asInstanceOf[Invocable]

    Logger.info(s"NashornTestController:51> invokable: ${invokable}")

    val r = invokable.invokeFunction("sqrt", "5")


    //val r = None
    Ok(s"Nashorn Test 4: Math.js result: ${eval2},  function call to math.sqrt: ${r} ")

  }


  // Execute the iwp5, and jquery animator javascript

  def nashornTest4_loadIwpAnimation() = Action { implicit request: Request[AnyContent] =>

    val engine = new ScriptEngineManager().getEngineByName("nashorn")

    // Jquery 3 does not work in Nashorn: Cannot read property "createElement" from undefined in <eval> at line number 2]
    // val jqFilereader = new FileReader("public/javascripts/jquery-3.2.1.min.js")
    // val jqEval = engine.eval(jqFilereader)

    val mathJsReader = new FileReader("public/javascripts/math.min.js")
    val mathJsEval = engine.eval(mathJsReader)

    val iwpCalcReader = new FileReader("public/javascripts/iwp/iwp6-calc.js")
    val iwpCalcEval = engine.eval(iwpCalcReader)

    val iwpReadReader = new FileReader("public/javascripts/iwp/iwp6-read.js")
    val iwpReadEval = engine.eval(iwpReadReader)


    val invokable = engine.asInstanceOf[Invocable]


    // https://www.iwphys.org/xtoj.php/unit-test-2017/TEST_euler.iwp
    val unitTestAnimation = "{\"author\":{\"username\":{},\"name\":{},\"organization\":{},\"email\":{}},\"objects\":{\"time\":{\"start\":\"0.0\",\"stop\":\"100.0\",\"change\":\"0.02\",\"fps\":\"15.0\"},\"GraphWindow\":{\"xmin\":\"-10.0\",\"xmax\":\"10.0\",\"ymin\":\"-10.0\",\"ymax\":\"10.0\",\"xgrid\":\"1.0\",\"ygrid\":\"1.0\"},\"window\":{\"xmin\":\"-10.0\",\"xmax\":\"10.0\",\"ymin\":\"-10.0\",\"ymax\":\"10.0\",\"xgrid\":\"1.0\",\"ygrid\":\"1.0\",\"xunit\":\"m\",\"yunit\":\"m\",\"showAllDataAvailable\":\"false\",\"drawGridNumbers\":\"true\"},\"description\":{\"text\":\"Very simple test for Euler calculations. It's important to remember that the Initial Displacement and Velocity can be dynamic equations and \\npossibly more complicated than just fixed numbers. This means they would be based on inputs values, which is illustrated here.\"},\"input\":[{\"name\":\"initxdisp\",\"text\":\"Initial X Displacement\",\"initialValue\":\"-10.0\",\"units\":\"m\"},{\"name\":\"initxvel\",\"text\":\"Initial X Velocity\",\"initialValue\":\"5.0\",\"units\":\"m\"}],\"solid\":{\"name\":\"EulerMover\",\"shape\":{\"@attributes\":{\"type\":\"circle\",\"drawTrails\":\"false\",\"drawVectors\":\"false\"},\"vectors\":{\"@attributes\":{\"yAccel\":\"false\",\"Vel\":\"false\",\"xAccel\":\"false\",\"yVel\":\"false\",\"xVel\":\"false\",\"Accel\":\"false\"}},\"width\":{\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"1\"}},\"height\":{\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"1\"}},\"angle\":{\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"0\"}},\"graphOptions\":{\"@attributes\":{\"graphVisible\":\"false\"},\"initiallyOn\":{\"@attributes\":{\"yAccel\":\"false\",\"yVel\":\"false\",\"xAccel\":\"false\",\"xVel\":\"false\",\"yPos\":\"false\",\"xPos\":\"false\"}}}},\"color\":{\"red\":\"255\",\"green\":\"0\",\"blue\":\"0\"},\"xpath\":{\"calculator\":{\"@attributes\":{\"type\":\"euler\"},\"displacement\":\"initxdisp\",\"velocity\":\"initxvel\",\"acceleration\":\"-5*t\"}},\"ypath\":{\"calculator\":{\"@attributes\":{\"type\":\"euler\"},\"displacement\":\"-10\",\"velocity\":\"0\",\"acceleration\":\"5\"}}},\"output\":[{\"name\":\"EulerXDispOutput\",\"text\":\"X Disp\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.xdisp\"}},{\"name\":\"EulerXVelOutput\",\"text\":\"X Vel\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.xvel\"}},{\"name\":\"EulerXAccelOutput\",\"text\":\"X Accel\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.xaccel\"}},{\"name\":\"EulerYVelOutput\",\"text\":\"Y Vel\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.yvel\"}},{\"name\":\"EulerYDispOutput\",\"text\":\"Y Disp\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.ydisp\"}},{\"name\":\"EulerYAccelOutput\",\"text\":\"Y Accel\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.yaccel\"}}]}}"

    Logger.info(s"NashornTestController:51> invokable: ${invokable}")
    val r = invokable.invokeFunction("readAnimationString", unitTestAnimation )

    Ok(s"Nashorn Test 4: calcEval: ${iwpCalcEval},  readEval: ${iwpReadEval}  function call to readAnimationObject: ${r} ")

  }






  // Bring some calculations out!

  def nashornTest5_calcIwpSteps() = Action { implicit request: Request[AnyContent] =>

    val engine = new ScriptEngineManager().getEngineByName("nashorn")

    // Jquery 3 does not work in Nashorn: Cannot read property "createElement" from undefined in <eval> at line number 2]
    // val jqFilereader = new FileReader("public/javascripts/jquery-3.2.1.min.js")
    // val jqEval = engine.eval(jqFilereader)

    val mathJsReader = new FileReader("public/javascripts/math.min.js")
    val mathJsEval = engine.eval(mathJsReader)

    val iwpCalcReader = new FileReader("public/javascripts/iwp/iwp6-calc.js")
    val iwpCalcEval = engine.eval(iwpCalcReader)

    val iwpReadReader = new FileReader("public/javascripts/iwp/iwp6-read.js")
    val iwpReadEval = engine.eval(iwpReadReader)


    val invokable = engine.asInstanceOf[Invocable]

    // https://www.iwphys.org/xtoj.php/unit-test-2017/TEST_euler.iwp
    val unitTestAnimation = "{\"author\":{\"username\":{},\"name\":{},\"organization\":{},\"email\":{}},\"objects\":{\"time\":{\"start\":\"0.0\",\"stop\":\"100.0\",\"change\":\"0.02\",\"fps\":\"15.0\"},\"GraphWindow\":{\"xmin\":\"-10.0\",\"xmax\":\"10.0\",\"ymin\":\"-10.0\",\"ymax\":\"10.0\",\"xgrid\":\"1.0\",\"ygrid\":\"1.0\"},\"window\":{\"xmin\":\"-10.0\",\"xmax\":\"10.0\",\"ymin\":\"-10.0\",\"ymax\":\"10.0\",\"xgrid\":\"1.0\",\"ygrid\":\"1.0\",\"xunit\":\"m\",\"yunit\":\"m\",\"showAllDataAvailable\":\"false\",\"drawGridNumbers\":\"true\"},\"description\":{\"text\":\"Very simple test for Euler calculations. It's important to remember that the Initial Displacement and Velocity can be dynamic equations and \\npossibly more complicated than just fixed numbers. This means they would be based on inputs values, which is illustrated here.\"},\"input\":[{\"name\":\"initxdisp\",\"text\":\"Initial X Displacement\",\"initialValue\":\"-10.0\",\"units\":\"m\"},{\"name\":\"initxvel\",\"text\":\"Initial X Velocity\",\"initialValue\":\"5.0\",\"units\":\"m\"}],\"solid\":{\"name\":\"EulerMover\",\"shape\":{\"@attributes\":{\"type\":\"circle\",\"drawTrails\":\"false\",\"drawVectors\":\"false\"},\"vectors\":{\"@attributes\":{\"yAccel\":\"false\",\"Vel\":\"false\",\"xAccel\":\"false\",\"yVel\":\"false\",\"xVel\":\"false\",\"Accel\":\"false\"}},\"width\":{\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"1\"}},\"height\":{\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"1\"}},\"angle\":{\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"0\"}},\"graphOptions\":{\"@attributes\":{\"graphVisible\":\"false\"},\"initiallyOn\":{\"@attributes\":{\"yAccel\":\"false\",\"yVel\":\"false\",\"xAccel\":\"false\",\"xVel\":\"false\",\"yPos\":\"false\",\"xPos\":\"false\"}}}},\"color\":{\"red\":\"255\",\"green\":\"0\",\"blue\":\"0\"},\"xpath\":{\"calculator\":{\"@attributes\":{\"type\":\"euler\"},\"displacement\":\"initxdisp\",\"velocity\":\"initxvel\",\"acceleration\":\"-5*t\"}},\"ypath\":{\"calculator\":{\"@attributes\":{\"type\":\"euler\"},\"displacement\":\"-10\",\"velocity\":\"0\",\"acceleration\":\"5\"}}},\"output\":[{\"name\":\"EulerXDispOutput\",\"text\":\"X Disp\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.xdisp\"}},{\"name\":\"EulerXVelOutput\",\"text\":\"X Vel\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.xvel\"}},{\"name\":\"EulerXAccelOutput\",\"text\":\"X Accel\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.xaccel\"}},{\"name\":\"EulerYVelOutput\",\"text\":\"Y Vel\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.yvel\"}},{\"name\":\"EulerYDispOutput\",\"text\":\"Y Disp\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.ydisp\"}},{\"name\":\"EulerYAccelOutput\",\"text\":\"Y Accel\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.yaccel\"}}]}}"

    Logger.info(s"NashornTestController:51> invokable: ${invokable}")

    //load animation
    val r = invokable.invokeFunction("readAnimationString", unitTestAnimation )


    // reset to zero



    Ok(s"Nashorn Test 5:  function call to readAnimationObject: ${r} ")

  }


}
