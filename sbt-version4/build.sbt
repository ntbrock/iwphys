organization := "edu.ncssm"

name := "iwp4-dist-jar"

version := "4.1.3"

scalaVersion := "2.12.3"

packageBin in Compile := file(s"${name.value}_${scalaBinaryVersion.value}.jar")


