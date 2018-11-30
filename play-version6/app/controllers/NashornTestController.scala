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



  def nashornTest1() = Action { implicit request: Request[AnyContent] =>


    val engine = new ScriptEngineManager().getEngineByName("nashorn")

    val js = "1+1;"

    val result = engine.eval(js)

    Ok(s"Nashorn Test 1: result: ${result}")

  }


  // Read + Execute from a file.

  def nashornTest2() = Action { implicit request: Request[AnyContent] =>

    val engine = new ScriptEngineManager().getEngineByName("nashorn")

    val fr = new FileReader("public/javascripts/test/nashorn/nashornTest2.js")

    val eval = engine.eval(fr)


    val invokable = engine.asInstanceOf[Invocable]

    Logger.info(s"NashornTestController:51> invokable: ${invokable}")
    val r = invokable.invokeFunction("nashhornTest2js", "argument")

    Ok(s"Nashorn Test 2: result: ${eval},  function call to nashhornTest2js: ${r} ")

  }





  // Math.js

  def nashornTest3() = Action { implicit request: Request[AnyContent] =>

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

  def nashornTest4() = Action { implicit request: Request[AnyContent] =>

    val engine = new ScriptEngineManager().getEngineByName("nashorn")


    val jqFilereader = new FileReader("public/javascripts/jquery-3.2.1.min.js")
    val jqEval = engine.eval(jqFilereader)

    val iwpFilereader = new FileReader("public/javascripts/iwp/iwp5.js")
    val iwpEval = engine.eval(iwpFilereader)


    val invokable = engine.asInstanceOf[Invocable]

    Logger.info(s"NashornTestController:51> invokable: ${invokable}")
    val r = invokable.invokeFunction("nashhornTest3js", "argument")

    Ok(s"Nashorn Test 3: result: ${iwpEval},  function call to nashhornTest2js: ${r} ")

  }



}
