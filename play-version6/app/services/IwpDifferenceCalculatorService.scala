package services

import models.IwpStepDifference
import play.api.libs.json.JsArray


class IwpDifferenceCalculatorService {

  def diff ( left: JsArray, right: JsArray ) : Seq[IwpStepDifference] = {

      val maxSteps = Math.max( left.value.size, right.value.size )

      val diffs = 0.to(maxSteps).map { step =>

        IwpStepDifference(step, Map.empty, Map.empty, false, Seq.empty, false, Seq.empty )
      }

      diffs.toSeq
  }



}
