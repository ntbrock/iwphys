package services

import models.{IwpStepDifference, IwpStepDifferenceSummary}
import play.api.Logger
import play.api.libs.json.{JsArray, JsNumber, JsObject}

import scala.collection.mutable


class IwpDifferenceCalculatorService {


  // Translate the tiered Solid: { x } into a fully flat space.
  def flatObject ( jso : JsObject ) : Map[String,BigDecimal] = {

    var j2 = JsObject(Seq())

    jso.keys.map { key =>

      val jsv = jso.value(key)
      if ( jsv.isInstanceOf[JsNumber] ) {
        // single level = passs thru
        j2 = j2.deepMerge(JsObject(Seq(key -> jsv )))
      } else if ( jsv.isInstanceOf[JsObject] ) {

        val o2 = jsv.as[JsObject]
        o2.value.map { case(key2, jsv2 ) =>
          if ( jsv2.isInstanceOf[JsNumber] ) {
            j2 = j2.deepMerge(JsObject(Seq(key+"."+key2 -> jsv2 )))
          } else {
            throw new RuntimeException(s"Unexpected Inner condition, was type: ${jsv2}")
          }
        }

      } else {
        throw new RuntimeException(s"Unexpected Data condition, was type: ${jsv}")
      }
    }

    j2.value.map{case(k,v) => k -> v.as[JsNumber].value }.toMap
  }


  def diff ( leftJsa: JsArray, rightJsa: JsArray ) : Seq[IwpStepDifference] = {

      val maxSteps = Math.max( leftJsa.value.size, rightJsa.value.size )

      val diffs = 0.to(maxSteps).map { step =>

        val leftO = leftJsa.value.lift(step).map { r => flatObject(r.as[JsObject]) }
        val rightO = rightJsa.value.lift(step).map { r => flatObject(r.as[JsObject]) }

        val allKeys =
          ( leftO.map { _.keys }.getOrElse(Seq()) ++
            rightO.map { _.keys }.getOrElse(Seq()) ).toSeq.distinct.sorted

        val equal = mutable.Queue[(String, BigDecimal)]()

        val notEqual = mutable.Queue[(String, (BigDecimal,BigDecimal))]()

        val leftMissing = mutable.Queue[String]()

        val rightMissing = mutable.Queue[String]()

        leftO.map { left =>

          rightO.map { right =>

            // Only compare for steps that have both
            allKeys.map { key =>

              val lO = left.get(key)
              val rO = right.get(key)

              if ( lO.isEmpty && rO.isEmpty ) {
                // Defense against the weird. This key should never appear.
                throw new RuntimeException("Unexpected Logic condition 1")

              } else if ( lO.isEmpty && rO.isDefined ) {
                leftMissing.enqueue(key)
              } else if ( lO.isDefined && rO.isEmpty ) {
                rightMissing.enqueue(key)
              } else if ( lO.isDefined && rO.isDefined ) {

                val l = lO.get
                val r = rO.get

                // Surpress the slight java double differences (t,(0.3,0.30000000000000004))
                val tol = 0.00001
                if ( Math.abs((l - r).doubleValue()) < tol ) {

                  equal.enqueue((key, l))
                } else {
                  notEqual.enqueue((key, (l,r)))
                }

              } else {
                throw new RuntimeException("Unexpected Logic condition 2")
              }
            }
          }
        }

        IwpStepDifference(step,
          notEqual.toMap,
          equal.toMap,
          leftO.isEmpty,
          leftMissing,
          rightO.isEmpty,
          rightMissing )
      }

      diffs.toSeq
  }


  def summarize( path: String, diffs: Seq[IwpStepDifference] ) : IwpStepDifferenceSummary = {

    val sum = IwpStepDifferenceSummary(path, 0,0,0,0,0,0,0)

    // Loop over the diffs and begin to calculate.
    diffs.map{ diff =>

      sum.totalFrames = sum.totalFrames + 1

      if ( diff.notEqual.size > 0 ) {
        sum.framesWithDifferences += 1

        sum.totalDifferences += diff.notEqual.size
      }

      if ( diff.leftEmpty ) {
        sum.framesWithLeftMissing += 1
      }

      if ( diff.rightEmpty ) {
        sum.framesWithRightMissing += 1
      }

      sum.totalLeftMissing += diff.leftMissing.size
      sum.totalRightMissing += diff.rightMissing.size
    }

    sum

  }


}
