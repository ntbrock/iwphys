package services

import java.io.File

import javax.inject.Inject
import models.{Iwp6Animation, Iwp6Collection}
import org.mongodb.scala.model.Filters.equal
import play.api.{Configuration, Logger}
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{AnyContent, Request}

import scala.util.{Failure, Success, Try}

class IwpDirectoryBrowserService @Inject()(configuration: Configuration) extends BoilerplateIO {


  val rootO = configuration.getOptional[String]("iwp.filesystem.root")


  def topCollections: Seq[Iwp6Collection] = {

    findFolders("")
  }

  def findFolders(collection: String): Seq[Iwp6Collection] = {

    rootO match {
      case None => throw new RuntimeException("Configuration iwp.filesystem.root not found")
      case Some(root) =>

        val pathFile = new File(root + File.separator + collection)
        listDirectories(pathFile).map { f => Iwp6Collection(f, f.getName) }
    }

  }


  def findAnimations(collection: String): Seq[Iwp6Animation] = {

    rootO match {
      case None => throw new RuntimeException("Configuration iwp.filesystem.root not found")
      case Some(root) =>

        val pathFile = new File(root + File.separator + collection)

        val files = listIwpJsonFiles(pathFile)

        val out = files.map { file => Iwp6Animation.fromFile(file) }

        out.toSeq.map(_.toOption).flatten

    }
  }


  def getAnimation(collection: String, filename: String) : Try[Iwp6Animation] = {

    Try(
      rootO match {
        case None => throw new RuntimeException("Configuration iwp.filesystem.root not found")
        case Some(root) =>

          val pathFile = new File(root + File.separator + collection + File.separator + filename + ".json")

          Iwp6Animation.fromFile(pathFile) match {
            case Failure(x) => throw x
            case Success(s) => s
          }
      }
    )
  }

}
