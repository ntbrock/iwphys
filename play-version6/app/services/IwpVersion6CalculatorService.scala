package services

import java.io.FileReader

import javax.inject.Singleton
import javax.script.{Invocable, ScriptEngineManager}
import play.api.Logger
import play.api.libs.json.{JsArray, Json}

import scala.io.Source

@Singleton
class IwpVersion6CalculatorService {


  def spawnEngine : Invocable = {

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

    invokable
  }



  def loadFile(path:String) =
    Source.fromFile(path).getLines.toArray.mkString("\n")

  def animateToJsonFrames(path: String): JsArray = {

    // Load the Javascript file
    val iwpJs = loadFile(path)

    val jse = spawnEngine

    //load animation
    val read = jse.invokeFunction("readAnimationString", iwpJs )

    val play = jse.invokeFunction("playAnimationToEnd", "" )

    // Access frame 0
    val varsAtStep = jse.invokeFunction("varsAtStepJson", "" )

    Json.parse(varsAtStep.toString).as[JsArray]

  }


}


