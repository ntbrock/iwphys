package models

case class IwpStepDifferenceSummary(path: String,
                                    objectNames: Set[String],
                                    legacyObjectNames: Boolean,
                                    var totalFrames: Int,
                                    var framesWithDifferences: Int,
                                    var totalDifferences: Int,
                                    var framesWithLeftMissing: Int,
                                    var framesWithRightMissing: Int,
                                    var totalLeftMissing: Int,
                                    var totalRightMissing: Int
                                   ) {

  def csvHeader = Seq("result","viewUrl", "validateUrl", "path","legacyObjectNames", "objectCount", "objectNames",  "totalFrames","framesWithDifferences", "totalDifferences",
    "framesWithLeftMissing","framesWithRightMissing","totalLeftMissing","totalRightMissing")
    .map{ value => "\"" + value + "\"" }

  def csvValues = Seq("Complete", viewUrl, diffUrl, path, legacyObjectNames, objectNames.size, objectNames.toSeq.sorted.mkString(" "), totalFrames,framesWithDifferences,totalDifferences,
    framesWithLeftMissing,framesWithRightMissing,totalLeftMissing,totalRightMissing)
    .map{ value => "\"" + value + "\"" }



  def viewUrl = {
    s"https://iwp6.iwphys.org/animation/${path.replaceAllLiterally("../animations/", "")}"
  }

  def diffUrl = {
    s"https://iwp6.iwphys.org/validation/calculation/${path.replaceAllLiterally("../animations/", "")}"
  }


}
