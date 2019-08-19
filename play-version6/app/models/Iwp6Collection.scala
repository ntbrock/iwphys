package models

import java.io.File

import play.api.Logger


trait Iwp6Collection {

  def name : String

  def parent : Option[Iwp6Collection]

  def encoded : String
}


/**
  * Note that the Filesystem Collections always use / as their separator, like WWW Uri's
  * Windows specific backslashes are replaced in at a lower service level.
  * @param directory
  * @param root
  */
case class Iwp6FilesystemCollection (directory: File, root: File) extends Iwp6Collection
{

  val SEPARATOR = "/"

  def name : String = directory.getName

  def encoded : String = {
    directory.getCanonicalPath.replace( root.getCanonicalPath, "" ).replaceAll("\\\\", SEPARATOR).stripPrefix(SEPARATOR)
  }

  def parent : Option[Iwp6Collection] = {

    Logger.info(s"Iwp6Collection:30> encoded: ${encoded}")

    val parts = encoded.split(SEPARATOR).toSeq

    // Logger.info(s"Iwp6Collection:30> Parent Parts: ${parts}")

      if ( parts.size > 1 ) {

        val oneLessPart = parts.dropRight(1).mkString(File.separator)

        val newFile = new File(root + File.separator + oneLessPart)

        // Logger.info(s"Iwp6Collection:30> New File: ${newFile}  (${newFile.exists})")


        if ( newFile.exists() && newFile.isDirectory ) {
          Some( Iwp6FilesystemCollection(newFile, root))
        } else {
          None
        }

      } else {
        None
      }




  }

  override def toString = encoded
}


case class Iwp6MongoCollection (collectionName: String) {

  def name = collectionName

  def encoded = collectionName

  def parent = None
}