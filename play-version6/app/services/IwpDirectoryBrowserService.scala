package services

import java.io.File

import javax.inject.Inject
import models.{Iwp6Animation, Iwp6Collection}
import play.api.{Configuration, Logger}
import play.api.libs.json.Json

class IwpDirectoryBrowserService @Inject()(configuration: Configuration) extends BoilerplateIO {


  val rootO = configuration.getOptional[String]("iwp.filesystem.root")


  def topCollections : Seq[Iwp6Collection] = {

    findFolders("")
  }

  def findFolders(path: String) : Seq[Iwp6Collection] = {

    rootO match {
      case None => throw new RuntimeException("Configuration iwp.filesystem.root not found")
      case Some(root) =>

        val pathFile = new File(root  + File.separator + path)
        listDirectories(pathFile).map { f => Iwp6Collection( f, f.getName ) }
    }

  }



  def findAnimations(path: String) : Seq[Iwp6Animation] = {

    rootO match {
      case None => throw new RuntimeException("Configuration iwp.filesystem.root not found")
      case Some(root) =>

        val pathFile = new File(root  + File.separator + path)

        val files = listIwpJsonFiles(pathFile)

        val out = files.map { file =>

          val jsonString = readFileCompletely(file)

          val json = Json.parse(jsonString)

          val obj = Json.fromJson[Iwp6Animation](json).asOpt

          if ( obj.isEmpty ) {
            Logger.warn(s"IwpDirectoryBrowserService:41> File did not parse: ${file}")
          }

          obj
        }

        out.toSeq.flatten

    }
  }


}
