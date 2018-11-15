package services

import javax.inject.Inject
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.{CodecRegistries, CodecRegistry}
import org.mongodb.scala.MongoClient
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import play.api.Configuration
import ch.rasc.bsoncodec.math.BigDecimalStringCodec
import ch.rasc.bsoncodec.time.{LocalDateDateCodec, LocalDateTimeDateCodec}

import models.IwpAnimation
import org.mongodb.scala._
import play.api.Configuration
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}

import scala.reflect.ClassTag
// Get your learn on:
// http://mongodb.github.io/mongo-scala-driver/2.5/getting-started/quick-tour/


class IwpMongoClient  @Inject() (configuration: Configuration) {

  def requireConfig(path: String) : String = {
    configuration.getOptional[String](path) match {
      case None => throw new RuntimeException(s"CryptoMongoClient: Config missing ${path} in application.conf")
      case Some(e) => e
    }
  }

  lazy val uri: String = requireConfig("iwp.mongodb.uri")

  lazy val mongoClient: MongoClient = {
    MongoClient(uri)
  }



  //===== Codecs for All Identityship models  =====================================================

  // http://www.jannikarndt.de/blog/2017/08/writing_case_classes_to_mongodb_in_scala/
  // https://github.com/ralscha/bsoncodec
  private val javaCodecs = CodecRegistries.fromCodecs(
    new LocalDateTimeDateCodec(),
    new LocalDateDateCodec(),
    new BigDecimalStringCodec())

  // Enable each app to register its own codecs
  private var applicationCodecs =  fromProviders(classOf[IwpAnimation])

  def registerApplicationCodecs(applicationRegistry: CodecRegistry): Unit = {
    applicationCodecs = applicationRegistry
  }


  private def codecRegistry = {
    fromRegistries(applicationCodecs, javaCodecs, DEFAULT_CODEC_REGISTRY)
  }


}
