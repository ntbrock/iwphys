package models

case class IwpStepDifferenceSummary( var totalFrames: Int,
                                     var framesWithDifferences: Int,
                                     var totalDifferences: Int,
                                     var framesWithLeftMissing: Int,
                                     var framesWithRightMissing: Int,
                                     var totalLeftMissing: Int,
                                     var totalRightMissing: Int
                                   )
