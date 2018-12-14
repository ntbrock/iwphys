package models

import java.io.File

case class Iwp6Collection (file: File,
                           name: String )
{

  def relative(parent: String) = {
    parent + File.separator + name
  }
}
