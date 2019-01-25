package models

import java.io.File


trait Iwp6Collection {

  def name : String

  def encoded : String
}



case class Iwp6FilesystemCollection (directory: File, root: File) extends Iwp6Collection
{

  def name : String = directory.getName

  def encoded : String = {
    directory.getCanonicalPath.replace( root.getCanonicalPath, "" ).stripPrefix("/")
  }

  override def toString = encoded
}


case class Iwp6MongoCollection (collectionName: String) {

  def name = collectionName

  def encoded = collectionName
}