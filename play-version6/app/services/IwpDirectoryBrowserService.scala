package services

import java.io.File

import javax.inject.Inject
import models.Iwp6Animation
import play.api.{Configuration, Logger}
import play.api.libs.json.Json

class IwpDirectoryBrowserService @Inject()(configuration: Configuration) extends BoilerplateIO {


  val rootO = configuration.getOptional[String]("iwp.filesystem.root")


  def topCollections : Seq[String] = {

    findFolders("")
  }

  def findFolders(path: String) : Seq[String] = {

    rootO match {
      case None => throw new RuntimeException("Configuration iwp.filesystem.root not found")
      case Some(root) =>

        val pathFile = new File(root  + File.separator + path)
        listDirectories(pathFile).map {_.getName}
    }

  }



  def findAnimations(path: String) : Seq[Iwp6Animation] = {

    rootO match {
      case None => throw new RuntimeException("Configuration iwp.filesystem.root not found")
      case Some(root) =>

        val pathFile = new File(root  + File.separator + path)

        val files = listIwpJsonFiles(pathFile)

        files.map { file =>

          val jsonString = readFileCompletely(file)

          val json = Json.parse(jsonString)

          val obj = Json.fromJson[Iwp6Animation](json).asOpt

          if ( obj.isEmpty ) {
            Logger.warn(s"IwpDirectoryBrowserService:41> File did not parse: ${file}")
          }

          obj
        }.flatten

    }
  }


}
