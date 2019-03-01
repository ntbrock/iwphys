package services

import java.io.File
import java.net.URLDecoder

import edu.ncssm.iwp.problemdb.{DProblem, DProblemXMLParser}
import javax.inject.Inject
import models.{Iwp6Animation, Iwp6Collection, Iwp6FilesystemCollection}
import org.mongodb.scala.model.Filters.equal
import play.api.{Configuration, Logger}
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{AnyContent, Request}

import scala.util.{Failure, Success, Try}

class IwpFilesystemBrowserService @Inject()(configuration: Configuration) extends BoilerplateIO {


  val rootPathStringO = configuration.getOptional[String]("iwp.filesystem.root")

  def rootO: Option[File] = {
    rootPathStringO match {
      case None => None
      case Some(rootPath) =>

        val f = new File(rootPath)
        if (f.exists() && f.isDirectory) {
          Some(f)
        } else {
          None
        }
    }
  }


  def topCollections(): Seq[Iwp6Collection] = {
    findCollections(topCollection)
  }


  def getCollection(collectionEncoded: String): Option[Iwp6FilesystemCollection] = {

    rootO match {
      case None => throw new RuntimeException("Configuration iwp.filesystem.root not found")
      case Some(root) =>

        val decoded = URLDecoder.decode(collectionEncoded, "UTF-8")

        if ( decoded.startsWith("/") ) {
          throw new RuntimeException("Collection names starting with slash not permitted")
        }

        if ( decoded.contains("..") ) {
          throw new RuntimeException("Collection names with custom directory paths not permitted")
        }


        // Logger.info(s"IwpFilesystemBrowser:56> Decoded: ${decoded}")

        val pathFile = new File(root + File.separator + decoded)

        // Logger.info(s"IwpFilesystemBrowser:56> Pathfile: ${pathFile}   exists: ${pathFile.exists}")

        if (pathFile.exists) {
          Some( Iwp6FilesystemCollection(pathFile, root))
        } else {

          None
        }
    }
  }



  def topCollection : Iwp6Collection = {

    rootO match {
      case None => throw new RuntimeException("Configuration iwp.filesystem.root not found")
      case Some(root) =>
        Iwp6FilesystemCollection(root, root)
    }
  }



  def findCollections(collection: Iwp6Collection): Seq[Iwp6FilesystemCollection] = {

    rootO match {
      case None => throw new RuntimeException("Configuration iwp.filesystem.root not found")
      case Some(root) =>

        val pathFile = new File(root + File.separator + collection.encoded)
        //Logger.info(s"IwpDirectoryBrowserService:31> Scanning: ${pathFile}")

        listDirectories(pathFile).map { f => Iwp6FilesystemCollection(f, root) }
    }

  }


  def findAnimations(collection: Iwp6FilesystemCollection): Seq[Iwp6Animation] = {

    rootO match {
      case None => throw new RuntimeException("Configuration iwp.filesystem.root not found")
      case Some(root) =>

        val files = listIwpJsonFiles(collection.directory)

        val out = files.map { file => Iwp6Animation.fromFile(file) }

        out.toSeq.map(_.toOption).flatten

    }
  }


  def getAnimation(collection: Iwp6Collection, filename: String) : Try[Iwp6Animation] = {

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

  /**
    * IWP4 Adaptattion
    * @param collection
    * @param filename
    * @return
    */

  def getVersion4Problem(collection: Iwp6Collection, filename: String) : Try[DProblem] = {

    Try(
      rootO match {
        case None => throw new RuntimeException("Configuration iwp.filesystem.root not found")
        case Some(root) =>

          val pathFile = new File(root + File.separator + collection + File.separator + filename )// NOTE! These are .iwp files, not Json

          val xmlString = readFileCompletely(pathFile)

          val problem = DProblemXMLParser.load(xmlString)
          problem.filename = filename

          problem
      }
    )
  }


}
