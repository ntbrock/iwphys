package services

import java.io.{File, FileReader}

import controllers.AnimationFilesystemController
import javax.inject.{Inject, Singleton}
import javax.script.{Invocable, ScriptEngineManager}
import models.{Iwp6Animation, Iwp6FilesystemCollection}
import play.api.Logger
import play.api.libs.json.{JsArray, Json}

import scala.io.Source
import scala.util.{Failure, Success}

@Singleton
class IwpVersion6CalculatorService @Inject() ( animationFilesystem: IwpFilesystemBrowserService ) {


  def spawnEngine : Invocable = {

    val engine = new ScriptEngineManager().getEngineByName("nashorn")

    // Jquery 3 does not work in Nashorn: Cannot read property "createElement" from undefined in <eval> at line number 2]
    // val jqFilereader = new FileReader("public/javascripts/jquery-3.2.1.min.js")
    // val jqEval = engine.eval(jqFilereader)

    val mathJsReader = new FileReader("public/javascripts/math.min.js")
    val mathJsEval = engine.eval(mathJsReader)


    engine.eval("var CONFIG_attributesPropertyOverride = '@attributes';")

    val iwpCalcReader = new FileReader("public/javascripts/iwp/iwp6-calc.js")
    val iwpCalcEval = engine.eval(iwpCalcReader)

    val iwpReadReader = new FileReader("public/javascripts/iwp/iwp6-read.js")
    val iwpReadEval = engine.eval(iwpReadReader)


    val invokable = engine.asInstanceOf[Invocable]

    invokable
  }



  def loadFile(collection:String, filename: String) = {

    val root = animationFilesystem.rootO.getOrElse {
      throw new RuntimeException("Root animation not found in configuration")

    }

    Logger.info(s"IwpVersion6CalculatorService:54> loadFile: collection: ${collection}  filename: ${filename}")

    val breaker = 1
    animationFilesystem.getAnimation(Iwp6FilesystemCollection( new File(root + File.separator + collection), root), filename)

    // Use the new converter
    // Iwp6Animation.fromFile(new File(path))

  }


  def animateToJsonFrames(collection:String, path: String): JsArray = {

    // Load the Javascript file
    loadFile(collection, path) match {

      case Failure(x) => throw x
      case Success(iwpAnimation) =>

        val iwpJs = iwpAnimation.toJsonString


        val jse = spawnEngine

        //load animation
        val read = jse.invokeFunction("readAnimationString", iwpJs)

        val play = jse.invokeFunction("playAnimationToEnd", "")

        // Access frame 0
        val varsAtStep = jse.invokeFunction("varsAtStepJson", "")

        Json.parse(varsAtStep.toString).as[JsArray]

    }
  }


}


