package models

case class IwpStepDifferenceSummary( val path: String,
                                     var totalFrames: Int,
                                     var framesWithDifferences: Int,
                                     var totalDifferences: Int,
                                     var framesWithLeftMissing: Int,
                                     var framesWithRightMissing: Int,
                                     var totalLeftMissing: Int,
                                     var totalRightMissing: Int
                                   ) {

  def csvHeader = Seq("path","totalFrames","framesWithDifferences", "totalDifferences",
    "framesWithLeftMissing","framesWithRightMissing","totalLeftMissing","totalRightMissing")
    .map{ value => "\"" + value + "\"" }

  def csvValues = Seq(path, totalFrames,framesWithDifferences,totalDifferences,
    framesWithLeftMissing,framesWithRightMissing,totalLeftMissing,totalRightMissing)
    .map{ value => "\"" + value + "\"" }

}
