package services
import java.io.File

import play.api.Logger


/**
  * 2017-Jan-18 Updated to support ~ filtering to hide files.
  *
  */

trait BoilerplateIO {

  val SUFFIX_IWP_JSON = ".iwp.json"
  val SUFFIX_JSON = ".json"

  val SUFFIX_TXT = ".txt"
  val SUFFIX_HTML = ".html"

  val SUFFIX_PNG = ".png"
  val SUFFIX_JPG = ".jpg"
  val SUFFIX_JPEG = ".jpeg"

  val SUFFIX_JS = ".js"
  val SUFFIX_CSS = ".css"

  val SUFFIX_TTF = ".ttf"
  val SUFFIX_OTF = ".otf"
  val SUFFIX_EOT = ".eot"
  val SUFFIX_WOFF = ".woff"
  val SUFFIX_WOFF2 = ".woff2"

  def getExtension(f: File): String = {
    f.getName.replaceAll("^.*[.]([A-Za-z0-9]+$)","$1")
  }

  def removeExtension(f: File): String = {
    f.getName.replaceAll("[.][A-Za-z0-9]+$","")
  }

  // Boilerplate IO
  def listDirectories(f: File): Array[File] = {
    // Logger.info(s"BoilerplateIo:41> ListDirectories: ${f}  (${f.exists})")
    listVisibleDirectories(f)
  }

  // Support the ~ tilde like fm
  def listVisibleDirectories(f: File): Array[File] = {
    f.listFiles().sorted.filter(_.isDirectory).filter { file =>
      ! ( file.getName.endsWith("~") || file.isHidden )
    }
  }


  // Support the ~ tilde like fm
  def listVisibleFiles(f: File): Array[File] = {
    if ( ! f.isDirectory ) {
      Array()
    } else {
      f.listFiles().sorted.filter(!_.isDirectory).filter { file =>
        !(file.getName.endsWith("~") || file.isHidden)
      }
    }
  }


  def listIwpJsonFiles(f: File): Array[File] = {
    listVisibleFiles(f).sorted.filter(!_.isDirectory).filter { file =>
      file.getName.toLowerCase.endsWith(SUFFIX_IWP_JSON)
    }
  }


  def listJsonFiles(f: File): Array[File] = {
    listVisibleFiles(f).sorted.filter(!_.isDirectory).filter { file =>
      file.getName.toLowerCase.endsWith(SUFFIX_JSON)
    }
  }


  def listTextFiles(f: File): Array[File] = {
    listVisibleFiles(f).sorted.filter(!_.isDirectory).filter { file =>
      file.getName.toLowerCase.endsWith(SUFFIX_TXT) ||
        file.getName.toLowerCase.endsWith(SUFFIX_HTML)
    }
  }

  def listImageFiles(f: File): Array[File] = {
    listVisibleFiles(f).sorted.filter(! _.isDirectory).filter { file =>
      file.getName.toLowerCase.endsWith(SUFFIX_PNG) ||
        file.getName.toLowerCase.endsWith(SUFFIX_JPG) ||
        file.getName.toLowerCase.endsWith(SUFFIX_JPEG)

    }
  }

  def listFontFiles(f: File): Array[File] = {
    listVisibleFiles(f).sorted.filter(! _.isDirectory).filter { file =>

      val lc = file.getName.toLowerCase

      lc.endsWith(SUFFIX_OTF) ||
        lc.endsWith(SUFFIX_EOT) ||
        lc.endsWith(SUFFIX_TTF) ||
        lc.endsWith(SUFFIX_WOFF) ||
        lc.endsWith(SUFFIX_WOFF2)

    }
  }

  def listFilesWithExtension(f: File, ext: String): Array[File] = {
    listVisibleFiles(f).sorted.filter(! _.isDirectory).filter { file =>
      file.getName.toLowerCase.endsWith(ext.toLowerCase)
    }
  }


  def getKnownFileWithExtension(d: File, fileName: String, ext: String): Option[File] = {

    val possibles =
      d.listFiles.filter(! _.isDirectory).filter { file =>
        file.isFile &&
          file.getName.equalsIgnoreCase(s"${fileName}.${ext}")
      }

    if ( possibles.size > 0 ) {
      // Return the first
      Some(possibles(0))
    }  else {
      None
    }
  }


/*
  def getKnownDirectory(d: File, dirName: String): Option[RecipeUri] = {

    val possibles =
      d.listFiles.filter(_.isDirectory).filter { file =>
        file.getName.equalsIgnoreCase(s"${dirName}")
      }

    if ( possibles.size > 0 ) {
      // Return the first
      Some(new RecipeUri(possibles(0)))
    }  else {
      None
    }
  }
*/


  def readFileCompletely(f: File): String = {
    val source = scala.io.Source.fromFile(f)
    val lines = try source.mkString finally source.close()
    lines
  }

  /**
    * 2017.3 Used by Image readers for uploads and ImageDeliveryController
    * @param f
    * @return
    */

  def readFileBytesCompletely(f: File) : Array[Byte] = {
    val source = scala.io.Source.fromFile(f)(scala.io.Codec.ISO8859)
    val byteArray = try  source.map(_.toByte).toArray finally source.close()
    byteArray
  }

}
