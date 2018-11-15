name := """iwp6-play"""
organization := "edu.ncssm.iwp"

version := "6.0.1-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test

// 2018May07 - Depend on iwp4_1_3 Java library, sbt publishLocal git/iwphys/sbt-version4
libraryDependencies += "edu.ncssm" %% "iwp4-dist-jar" % "4.1.3"


// 2018Nov15 - Mongo for animation storage
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.4.1"

libraryDependencies += "ch.rasc" % "bsoncodec" % "1.0.1",


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "edu.ncssm.iwp.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "edu.ncssm.iwp.binders._"
