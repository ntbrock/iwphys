package models


case class IwpStepDifference ( step: Int,
                               notEqual: Map[String, (Double,Double)],
                               equal: Map[String, Double],
                               leftEmpty: Boolean,
                               leftMissing: Seq[String],
                               rightEmpty: Boolean,
                               rightMissing: Seq[String],
                             )

