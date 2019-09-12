package models


case class IwpStepDifference (step: Int,
                              objectNames: Set[String],
                              // Stucture:  Key of Map is variable name as a String
                              // Stucture:  1st BigDecimal is left value.
                              // Stucture:  2nd BigDecimal is right value.
                              notEqual: Map[String, (BigDecimal, BigDecimal)],
                              equal: Map[String, BigDecimal],
                              leftEmpty: Boolean,
                              leftMissing: Seq[String],
                              rightEmpty: Boolean,
                              rightMissing: Seq[String],
                             )

