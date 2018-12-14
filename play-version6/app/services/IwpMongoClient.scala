package services

import javax.inject.Inject
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.{CodecRegistries, CodecRegistry}
import org.mongodb.scala.MongoClient
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import play.api.Configuration
import ch.rasc.bsoncodec.math.BigDecimalStringCodec
import ch.rasc.bsoncodec.time.{LocalDateDateCodec, LocalDateTimeDateCodec}
import models._
import org.mongodb.scala._
import play.api.Configuration
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import play.api.libs.json.Json

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

  lazy val env: String = requireConfig("iwp.env")


  lazy val url: String = requireConfig("iwp.mongodb.url")

  lazy val mongoClient: MongoClient = { MongoClient(url) }



  //===== Codecs for All Iwp models  =====================================================

  // http://www.jannikarndt.de/blog/2017/08/writing_case_classes_to_mongodb_in_scala/
  // https://github.com/ralscha/bsoncodec
  private val javaCodecs = CodecRegistries.fromCodecs(
    new LocalDateTimeDateCodec(),
    new LocalDateDateCodec(),
    new BigDecimalStringCodec())

  // Enable each app to register its own codecs
  private var applicationCodecs =
    fromProviders(
      classOf[Iwp6Calculator],
      classOf[Iwp6InitiallyOn],
      classOf[Iwp6GraphOptions],
      classOf[Iwp6Path],
      classOf[Iwp6Vectors],
      classOf[Iwp6Length],
      classOf[Iwp6Time],
      classOf[Iwp6Window],
      classOf[Iwp6Color],
      classOf[Iwp6Shape],
      classOf[Iwp6Solid],
      classOf[Iwp6Output],
      classOf[Iwp6Input],
      classOf[Iwp6Description],
      classOf[Iwp6GraphWindow],
      classOf[Iwp6Objects],
      classOf[Iwp6Author],
      classOf[Iwp6Animation]
    )

  def registerApplicationCodecs(applicationRegistry: CodecRegistry): Unit = {
    applicationCodecs = applicationRegistry
  }


  private def codecRegistry = {
    fromRegistries(applicationCodecs, javaCodecs, DEFAULT_CODEC_REGISTRY)
  }

  // ====== Generalized Functions to get any database with any collection
  private def exactCollection[A:ClassTag](databaseName: String, collectionName: String) : MongoCollection[A] = {

    val database = mongoClient.getDatabase(databaseName).withCodecRegistry(codecRegistry)
    database.getCollection[A](collectionName)
  }

  // Grab a collection that's environment specific
  protected def environmentCollection[A:ClassTag]( databasePrefix: String, collectionName: String ) : MongoCollection[A] = {
    exactCollection[A](s"$databasePrefix-$env", collectionName)
  }


  // ====== IWP Bindings =======================

  def animationCollectionNames() : Seq[String] = {
    // Hardcoded for now, don't expose community content yet.
    Seq("winters-ncssm-2009", "iwp-packaged")
  }

  def animationCollection(collectionName: String) : MongoCollection[Iwp6Animation] = {
    environmentCollection("iwp6", collectionName)
  }

}
