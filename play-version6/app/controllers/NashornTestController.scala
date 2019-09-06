package controllers

import java.io.{File, FileReader}

import javax.inject._
import models.Iwp6Animation
import org.mongodb.scala.model.Filters._
import play.api.Logger
import play.api.libs.json.{JsArray, Json}
import play.api.mvc._
import services.{IwpDifferenceCalculatorService, IwpMongoClient, IwpVersion4CalculatorService, IwpVersion6CalculatorService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import javax.script.{Invocable, ScriptEngine, ScriptEngineManager}

/**
  * This controller is Taylor's first integration demo to Nashhorn Javascript engine
  * Tutorial: https://winterbe.com/posts/2014/04/05/java8-nashorn-tutorial/
 */
@Singleton
class NashornTestController @Inject()(cc: ControllerComponents,
                                      mongo: IwpMongoClient,
                                      iwpVersion4CalculatorService: IwpVersion4CalculatorService,
                                      iwpVersion6CalculatorService: IwpVersion6CalculatorService,
                                      iwpDifferenceCalculatorService: IwpDifferenceCalculatorService
                                     ) extends AbstractController(cc) {



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

  /**
    * 2019Mar13 Pair w/ Niall - Test conversion to new iwp6 'flat' format
    * @return
    */
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

    val iwpOrderReader = new FileReader("public/javascripts/iwp/iwp6-order.js")
    val iwpOrderEval = engine.eval(iwpOrderReader)


    val invokable = engine.asInstanceOf[Invocable]


    // https://www.iwphys.org/xtoj.php/unit-test-2017/TEST_euler.iwp
    // val unitTestAnimation = "{\"author\":{\"username\":{},\"name\":{},\"organization\":{},\"email\":{}},\"objects\":{\"time\":{\"start\":\"0.0\",\"stop\":\"100.0\",\"change\":\"0.02\",\"fps\":\"15.0\"},\"GraphWindow\":{\"xmin\":\"-10.0\",\"xmax\":\"10.0\",\"ymin\":\"-10.0\",\"ymax\":\"10.0\",\"xgrid\":\"1.0\",\"ygrid\":\"1.0\"},\"window\":{\"xmin\":\"-10.0\",\"xmax\":\"10.0\",\"ymin\":\"-10.0\",\"ymax\":\"10.0\",\"xgrid\":\"1.0\",\"ygrid\":\"1.0\",\"xunit\":\"m\",\"yunit\":\"m\",\"showAllDataAvailable\":\"false\",\"drawGridNumbers\":\"true\"},\"description\":{\"text\":\"Very simple test for Euler calculations. It's important to remember that the Initial Displacement and Velocity can be dynamic equations and \\npossibly more complicated than just fixed numbers. This means they would be based on inputs values, which is illustrated here.\"},\"input\":[{\"name\":\"initxdisp\",\"text\":\"Initial X Displacement\",\"initialValue\":\"-10.0\",\"units\":\"m\"},{\"name\":\"initxvel\",\"text\":\"Initial X Velocity\",\"initialValue\":\"5.0\",\"units\":\"m\"}],\"solid\":{\"name\":\"EulerMover\",\"shape\":{\"@attributes\":{\"type\":\"circle\",\"drawTrails\":\"false\",\"drawVectors\":\"false\"},\"vectors\":{\"@attributes\":{\"yAccel\":\"false\",\"Vel\":\"false\",\"xAccel\":\"false\",\"yVel\":\"false\",\"xVel\":\"false\",\"Accel\":\"false\"}},\"width\":{\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"1\"}},\"height\":{\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"1\"}},\"angle\":{\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"0\"}},\"graphOptions\":{\"@attributes\":{\"graphVisible\":\"false\"},\"initiallyOn\":{\"@attributes\":{\"yAccel\":\"false\",\"yVel\":\"false\",\"xAccel\":\"false\",\"xVel\":\"false\",\"yPos\":\"false\",\"xPos\":\"false\"}}}},\"color\":{\"red\":\"255\",\"green\":\"0\",\"blue\":\"0\"},\"xpath\":{\"calculator\":{\"@attributes\":{\"type\":\"euler\"},\"displacement\":\"initxdisp\",\"velocity\":\"initxvel\",\"acceleration\":\"-5*t\"}},\"ypath\":{\"calculator\":{\"@attributes\":{\"type\":\"euler\"},\"displacement\":\"-10\",\"velocity\":\"0\",\"acceleration\":\"5\"}}},\"output\":[{\"name\":\"EulerXDispOutput\",\"text\":\"X Disp\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.xdisp\"}},{\"name\":\"EulerXVelOutput\",\"text\":\"X Vel\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.xvel\"}},{\"name\":\"EulerXAccelOutput\",\"text\":\"X Accel\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.xaccel\"}},{\"name\":\"EulerYVelOutput\",\"text\":\"Y Vel\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.yvel\"}},{\"name\":\"EulerYDispOutput\",\"text\":\"Y Disp\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.ydisp\"}},{\"name\":\"EulerYAccelOutput\",\"text\":\"Y Accel\",\"units\":\"m\",\"calculator\":{\"@attributes\":{\"type\":\"parametric\"},\"value\":\"EulerMover.yaccel\"}}]}}"

    val unitTestAnimation = NashhornTestControllerSampleAnimations.springMotionFlatObjectSequence

    Logger.info(s"NashornTestController:128> invokable: ${invokable}")
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

    val unitTestAnimation = NashhornTestControllerSampleAnimations.springMotionFlatObjectSequence


    Logger.info(s"NashornTestController:51> invokable: ${invokable}")

    //load animation
    val read = invokable.invokeFunction("readAnimationString", unitTestAnimation )

    val play = invokable.invokeFunction("playAnimationToEnd", "" )

    // Access frame 0
    val varsAtStep = invokable.invokeFunction("varsAtStepJson", "" )

    Ok(s"Nashorn Test 5:  function call to varsAtStep(0): ${varsAtStep} ")

  }



  // Bring some calculations out!

  def nashornTest6_compareIwpSteps() = Action { implicit request: Request[AnyContent] =>

    val collection = "animations/unit-test-2017"
    val path = "TEST_euler.iwp"

    val v4 = iwpVersion4CalculatorService.animateToJsonFrames(collection + File.separator + path)

    val v6 = iwpVersion6CalculatorService.animateToJsonFrames(collection, path)

    val diffs = iwpDifferenceCalculatorService.diff( v4, v6 )

    val differenceSummary = iwpDifferenceCalculatorService.summarize( path, diffs )

    Ok(views.html.validation.compareIwpSteps(path, diffs, differenceSummary ))

  }




}




object NashhornTestControllerSampleAnimations {

  val springMotionFlatObjectSequence = """{
"author": {
"email": "",
"name": "",
"organization": "",
"username": "winters@ncssm.edu"
},
"objects": [
{
"start": 0,
"stop": 1,
"change": 0.05,
"fps": 20,
"objectType": "time"
},
{
"xmin": 0,
"xmax": 0.1,
"ymin": -0.1,
"ymax": 0.1,
"xgrid": 0.01,
"ygrid": 0.01,
"objectType": "graphWindow"
},
{
"xmin": -0.1,
"xmax": 0.1,
"ymin": -0.1,
"ymax": 0.1,
"xgrid": 0.02,
"ygrid": 0.02,
"xunit": "meters",
"yunit": "meters",
"objectType": "window"
},
{
"text": "A red ball is connected to a spring which is fixed at the left side of the screen. ",
"objectType": "description"
},
{
"name": "m",
"text": "Mass of ball",
"initialValue": 0.5,
"units": "kg",
"hidden": false,
"objectType": "input"
},
{
"name": "k",
"text": "Spring Constant",
"initialValue": 10,
"units": "N/m",
"hidden": false,
"objectType": "input"
},
{
"name": "h",
"text": "Spring height",
"initialValue": 0.03,
"units": "m",
"hidden": true,
"objectType": "input"
},
{
"name": "a",
"text": "Amplitude",
"initialValue": 0.085,
"units": "m",
"hidden": false,
"objectType": "input"
},
{
"name": "p",
"text": "Phase",
"initialValue": 3.14,
"units": "rad",
"hidden": false,
"objectType": "input"
},
{
"name": "w",
"text": "Angular speed",
"units": "1/s",
"calculator": {
"calcType": "parametric",
"value": "(k/m)^.5"
},
"hidden": true,
"objectType": "output"
},
{
"name": "ball",
"shape": {
"shapeType": "circle",
"vectors": {
"xVel": false,
"yVel": false,
"xAccel": false,
"yAccel": false,
"Vel": false,
"Accel": false
},
"width": {
"calculator": {
"calcType": "parametric",
"value": ".02"
}
},
"height": {
"calculator": {
"calcType": "parametric",
"value": ".02"
}
},
"graphOptions": {
"graphVisible": true,
"initiallyOn": {
"xPos": true,
"xVel": true,
"xAccel": true,
"yPos": false,
"yVel": false,
"yAccel": false
}
},
"isGraphable": true,
"drawTrails": false,
"drawVectors": false
},
"color": {
"red": 255,
"green": 0,
"blue": 0
},
"xpath": {
"calculator": {
"calcType": "parametric",
"value": "a*cos(w*t+p)"
}
},
"ypath": {
"calculator": {
"calcType": "parametric",
"value": "0"
}
},
"objectType": "solid"
},
{
"name": "pitch",
"text": "Spring pitch",
"units": "m",
"calculator": {
"calcType": "parametric",
"value": "(ball.xpos+0.1)/6"
},
"hidden": true,
"objectType": "output"
},
{
"name": "line1",
"shape": {
"shapeType": "line",
"vectors": {
"xVel": false,
"yVel": false,
"xAccel": false,
"yAccel": false,
"Vel": false,
"Accel": false
},
"width": {
"calculator": {
"calcType": "parametric",
"value": "pitch/2"
}
},
"height": {
"calculator": {
"calcType": "parametric",
"value": "h/2"
}
},
"graphOptions": {
"graphVisible": false,
"initiallyOn": {
"xPos": false,
"xVel": false,
"xAccel": false,
"yPos": false,
"yVel": false,
"yAccel": false
}
},
"isGraphable": false,
"drawTrails": false,
"drawVectors": false
},
"color": {
"red": 0,
"green": 0,
"blue": 0
},
"xpath": {
"calculator": {
"calcType": "parametric",
"value": "-.1"
}
},
"ypath": {
"calculator": {
"calcType": "parametric",
"value": "0"
}
},
"objectType": "solid"
},
{
"name": "line2",
"shape": {
"shapeType": "line",
"vectors": {
"xVel": false,
"yVel": false,
"xAccel": false,
"yAccel": false,
"Vel": false,
"Accel": false
},
"width": {
"calculator": {
"calcType": "parametric",
"value": "pitch"
}
},
"height": {
"calculator": {
"calcType": "parametric",
"value": "-h"
}
},
"graphOptions": {
"graphVisible": false,
"initiallyOn": {
"xPos": false,
"xVel": false,
"xAccel": false,
"yPos": false,
"yVel": false,
"yAccel": false
}
},
"isGraphable": false,
"drawTrails": false,
"drawVectors": false
},
"color": {
"red": 0,
"green": 0,
"blue": 0
},
"xpath": {
"calculator": {
"calcType": "parametric",
"value": "-.1+pitch/2"
}
},
"ypath": {
"calculator": {
"calcType": "parametric",
"value": "0+h/2"
}
},
"objectType": "solid"
},
{
"name": "line3",
"shape": {
"shapeType": "line",
"vectors": {
"xVel": false,
"yVel": false,
"xAccel": false,
"yAccel": false,
"Vel": false,
"Accel": false
},
"width": {
"calculator": {
"calcType": "parametric",
"value": "pitch"
}
},
"height": {
"calculator": {
"calcType": "parametric",
"value": "h"
}
},
"graphOptions": {
"graphVisible": false,
"initiallyOn": {
"xPos": false,
"xVel": false,
"xAccel": false,
"yPos": false,
"yVel": false,
"yAccel": false
}
},
"isGraphable": false,
"drawTrails": false,
"drawVectors": false
},
"color": {
"red": 0,
"green": 0,
"blue": 0
},
"xpath": {
"calculator": {
"calcType": "parametric",
"value": "-.1+3*pitch/2"
}
},
"ypath": {
"calculator": {
"calcType": "parametric",
"value": "-h/2"
}
},
"objectType": "solid"
},
{
"name": "line4",
"shape": {
"shapeType": "line",
"vectors": {
"xVel": false,
"yVel": false,
"xAccel": false,
"yAccel": false,
"Vel": false,
"Accel": false
},
"width": {
"calculator": {
"calcType": "parametric",
"value": "pitch"
}
},
"height": {
"calculator": {
"calcType": "parametric",
"value": "-h"
}
},
"graphOptions": {
"graphVisible": false,
"initiallyOn": {
"xPos": false,
"xVel": false,
"xAccel": false,
"yPos": false,
"yVel": false,
"yAccel": false
}
},
"isGraphable": false,
"drawTrails": false,
"drawVectors": false
},
"color": {
"red": 0,
"green": 0,
"blue": 0
},
"xpath": {
"calculator": {
"calcType": "parametric",
"value": "-.1+5*pitch/2"
}
},
"ypath": {
"calculator": {
"calcType": "parametric",
"value": "h/2"
}
},
"objectType": "solid"
},
{
"name": "line5",
"shape": {
"shapeType": "line",
"vectors": {
"xVel": false,
"yVel": false,
"xAccel": false,
"yAccel": false,
"Vel": false,
"Accel": false
},
"width": {
"calculator": {
"calcType": "parametric",
"value": "pitch"
}
},
"height": {
"calculator": {
"calcType": "parametric",
"value": "h"
}
},
"graphOptions": {
"graphVisible": false,
"initiallyOn": {
"xPos": false,
"xVel": false,
"xAccel": false,
"yPos": false,
"yVel": false,
"yAccel": false
}
},
"isGraphable": false,
"drawTrails": false,
"drawVectors": false
},
"color": {
"red": 0,
"green": 0,
"blue": 0
},
"xpath": {
"calculator": {
"calcType": "parametric",
"value": "-.1+7*pitch/2"
}
},
"ypath": {
"calculator": {
"calcType": "parametric",
"value": "-h/2"
}
},
"objectType": "solid"
},
{
"name": "line6",
"shape": {
"shapeType": "line",
"vectors": {
"xVel": false,
"yVel": false,
"xAccel": false,
"yAccel": false,
"Vel": false,
"Accel": false
},
"width": {
"calculator": {
"calcType": "parametric",
"value": "pitch"
}
},
"height": {
"calculator": {
"calcType": "parametric",
"value": "-h"
}
},
"graphOptions": {
"graphVisible": false,
"initiallyOn": {
"xPos": false,
"xVel": false,
"xAccel": false,
"yPos": false,
"yVel": false,
"yAccel": false
}
},
"isGraphable": false,
"drawTrails": false,
"drawVectors": false
},
"color": {
"red": 0,
"green": 0,
"blue": 0
},
"xpath": {
"calculator": {
"calcType": "parametric",
"value": "-.1+9*pitch/2"
}
},
"ypath": {
"calculator": {
"calcType": "parametric",
"value": "h/2"
}
},
"objectType": "solid"
},
{
"name": "line7",
"shape": {
"shapeType": "line",
"vectors": {
"xVel": false,
"yVel": false,
"xAccel": false,
"yAccel": false,
"Vel": false,
"Accel": false
},
"width": {
"calculator": {
"calcType": "parametric",
"value": "pitch/2"
}
},
"height": {
"calculator": {
"calcType": "parametric",
"value": "h/2"
}
},
"graphOptions": {
"graphVisible": false,
"initiallyOn": {
"xPos": false,
"xVel": false,
"xAccel": false,
"yPos": false,
"yVel": false,
"yAccel": false
}
},
"isGraphable": false,
"drawTrails": false,
"drawVectors": false
},
"color": {
"red": 0,
"green": 0,
"blue": 0
},
"xpath": {
"calculator": {
"calcType": "parametric",
"value": "-.1+11*pitch/2"
}
},
"ypath": {
"calculator": {
"calcType": "parametric",
"value": "-h/2"
}
},
"objectType": "solid"
}
]
}"""

}