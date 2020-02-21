package models

import play.api.libs.json.JsObject


case class IwpOrderingRequiresProvides(name: String,
                                       requires: Seq[String],
                                       provides: Seq[String],
                                       jso: JsObject )

case class IwpObjectOrderingDiff(order: Int,
                                 v4: IwpOrderingRequiresProvides,
                                 v6: IwpOrderingRequiresProvides,
                                 nameEqual: Boolean,
                                 requiresEqual: Boolean,
                                 providesEqual: Boolean )
