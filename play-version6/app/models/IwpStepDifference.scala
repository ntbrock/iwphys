package models


case class IwpStepDifference ( step: Int,
                               notEqual: Map[String, (BigDecimal, BigDecimal)],
                               equal: Map[String, BigDecimal],
                               leftEmpty: Boolean,
                               leftMissing: Seq[String],
                               rightEmpty: Boolean,
                               rightMissing: Seq[String],
                             )

