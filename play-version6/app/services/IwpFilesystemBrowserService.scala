package services

import java.io.File
import java.net.URLDecoder

import edu.ncssm.iwp.problemdb.{DProblem, DProblemXMLParser}
import javax.inject.Inject
import models.{Iwp6Animation, Iwp6AnimationWithFailure, Iwp6Collection, Iwp6FilesystemCollection}
import org.mongodb.scala.model.Filters.equal
import play.api.{Configuration, Logger}
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{AnyContent, Request}

import scala.collection.mutable
import scala.util.{Failure, Success, Try}



class IwpFilesystemBrowserService @Inject()(configuration: Configuration) extends BoilerplateIO {


  val rootPathStringO = configuration.getOptional[String]("iwp.animations.path")

  def animationDirectory: File = {
    rootPathStringO match {
      case None => throw new RuntimeException("Configuration file missing: iwp.animations.path")
      case Some(rootPath) =>

        val f = new File(rootPath)

        // Logger.info(s"IwpFilesytemBrowserService:28> f: ${f}   f.exists? ${f.exists()}   f.isDirectory: ${f.isDirectory}")

        if (!f.exists()) {
          throw new RuntimeException(s"Configured Animations Path does not exist: iwp.animations.path = ${rootPath}")
        } else if (!f.isDirectory) {
          throw new RuntimeException(s"Configured Animations Path is not a Directory: iwp.animations.path = ${rootPath}")
        } else {
          f
        }
    }
  }


  def topCollections(): Seq[Iwp6Collection] = {
    findCollections(topCollection)
  }


  def getCollection(collectionEncoded: String): Option[Iwp6FilesystemCollection] = {

    val decoded = URLDecoder.decode(collectionEncoded, "UTF-8")

    if (decoded.startsWith("/")) {
      throw new RuntimeException("Collection names starting with slash not permitted")
    }

    if (decoded.contains("..")) {
      throw new RuntimeException("Collection names with custom directory paths not permitted")
    }


    // Logger.info(s"IwpFilesystemBrowser:56> Decoded: ${decoded}")

    val pathFile = new File(animationDirectory + File.separator + decoded)

    // Logger.info(s"IwpFilesystemBrowser:56> Pathfile: ${pathFile}   exists: ${pathFile.exists}")

    if (pathFile.exists) {
      Some(Iwp6FilesystemCollection(pathFile, animationDirectory))
    } else {

      None
    }
  }




  def topCollection : Iwp6Collection = {
    Iwp6FilesystemCollection(animationDirectory, animationDirectory)
  }


  def findCollections(collection: Iwp6Collection): Seq[Iwp6FilesystemCollection] = {

    val pathFile = new File(animationDirectory + File.separator + collection.encoded)
    //Logger.info(s"IwpDirectoryBrowserService:31> Scanning: ${pathFile}")

    listDirectories(pathFile).map { f => Iwp6FilesystemCollection(f, animationDirectory) }

  }




  def findAnimationsWithFailures(collection: Iwp6FilesystemCollection): ( Seq[Iwp6Animation], Seq[Iwp6AnimationWithFailure] ) = {

    val jsonFiles = listIwpJsonFiles(collection.directory)

    val jsons = jsonFiles.map { file => (file, Iwp6Animation.fromJsonFile(file)) }

    val xmlFiles = listIwpXmlFiles(collection.directory)

    val xmls = xmlFiles.map { file => (file, Iwp6Animation.fromXmlFile(file)) }

    val out = jsons ++ xmls


    val successes = mutable.Queue[Iwp6Animation]()
    val failures = mutable.Queue[Iwp6AnimationWithFailure]()

    out.map { case(file, animationT) =>

      animationT match {
        case Success(animation) =>
          successes.enqueue(animation)
        case Failure(x) =>
          failures.enqueue(Iwp6AnimationWithFailure(file.getName, x.getMessage, x))
      }

    }

    ( successes.sortBy(_.filename), failures )
  }




  def getAnimation(collection: Iwp6FilesystemCollection, filename: String) : Try[Iwp6Animation] = {

    // Logger.debug(s"IwpFilesystemBrowserService:128> getAnimation: collection: ${collection.name}  parentO: ${collection.parent} filename: $filename")

    Try {

      val filePath = collection.directory + File.separator + filename

      // First try the Json, and if Fails, fallback to the Xml

      val (jsonFilename, xmlFilenameO) =
        if (filename.endsWith(".json")) {
          // Load Json Only
          (filePath, None)
        } else {
          (filePath + ".json", Some(filePath))
        }

      val jsonAnimationT = Iwp6Animation.fromJsonFile(new File(jsonFilename))


      jsonAnimationT match {
        case Success(jsonAnimation) => jsonAnimation
        case Failure(x) =>

          xmlFilenameO match {
            case Some(xmlFilename) => Iwp6Animation.fromXmlFile(new File(xmlFilename)).get
            case None => throw new RuntimeException(s"No xml file found: ${filePath}")
          }
      }


    }
  }

  /**
    * IWP4 Adaptattion
    * @param collection
    * @param filename
    * @return
    */

  def getVersion4Problem(collection: Iwp6Collection, filename: String) : Try[DProblem] = {

    Try {


      val pathFile = new File(animationDirectory + File.separator + collection + File.separator + filename) // NOTE! These are .iwp files, not Json

      val xmlString = readFileCompletely(pathFile)

      val problem = DProblemXMLParser.load(xmlString)
      problem.filename = filename

      problem
    }

  }


}
