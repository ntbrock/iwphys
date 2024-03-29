/**
 * Interactive Web Physics 6 - Animator UI Routines, requires Jquery
 * Ryan Steed, Taylor Brockman 2016 - Version 5.0 Initial Port to HTML + SVG
 * Matthew Mims, Taylor Brockman 2018  - Version 5.1 Added Graphing and D3
 * Albert Gong, Nial Mullane, Taylor Brockman 2019 - Version 6.0 Migrated into Play Framework
 */


/**
 * Performs no calculations, but repaints every thing (time, outputs, solids) onto screen from memory at current step.
 * This is used mostly for the backwards step.
 */
function repaintStep(step) {
  var vars = varsAtStep[step];
  if ( vars == undefined ) {
    throw "No previous calculations available at step: " + step;
  } else {

   updateTimeDisplay(vars.t);

/*
   console.log("iwp6-animator:21> repaintStep("+step+"), parsedAnimation: " , parsedAnimation);

   console.log("iwp6-animator:21> repaintStep("+step+"), outputs: " , parsedAnimation.outputs());

   console.log("iwp6-animator:21> repaintStep("+step+"), solids: " , parsedAnimation.solids() );

   console.log("iwp6-animator:21> repaintStep("+step+"), floatingTexts: " , parsedAnimation.floatingTexts() );

   console.log("iwp6-animator:21> repaintStep("+step+"), vars: " , vars );
*/


   parsedAnimation.outputs().forEach( function( output, index ) {
      updateUserFormOutputDouble(output, vars[output.name]);
   });

   parsedAnimation.solids().forEach( function( solid, index ) {
      updateSolidSvgPathAndShape(solid, vars[solid.name])
   });

   parsedAnimation.floatingTexts().forEach( function( floatingText, index ) {
      updateFloatingTextSvgPathAndShape(floatingText, vars[floatingText.name]);
   });


  }
}




//--------------------------------------------------------------------------------
// DOM Manipulation

function setAuthorName(username) {
  $("#authorUsername").html( username );
}


function renderProblemFromMemory() {
  // Render from memory into page
  $("#itime").html( time.start.toString() );
  $("#itime_change").val(time.change.toString());
  $("#itime_start").val(time.start.toString());
  $("#itime_stop").val(time.stop.toString());
  $("#description").html( description.text );

  // 2018Mar23 Units are not editable.
  $("#iwindow_xmin").val( animationWindow.xmin );
  $("#iwindow_xmax").val( animationWindow.xmax );
  $("#iwindow_xgrid").val( animationWindow.xgrid );
  $("#iwindow_xunit").html( animationWindow.xunit );
  $("#iwindow_ymax").val( animationWindow.ymax );
  $("#iwindow_ymin").val( animationWindow.ymin );
  $("#iwindow_ygrid").val( animationWindow.ygrid );
  $("#iwindow_yunit").html( animationWindow.yunit );
  
  	
   // $("#graphWindow").html( graphWindow );
  inputTitle = 0;
  outputTitle = 0;

  $.each(htmlInputs, function(index, input) {
    if ( !input.includes("display:none;")  ) {
      inputTitle = 1;
    }
  })
  $.each(htmlOutputs, function(index, output) {
    if ( !output.includes("display:none;") ) {
      outputTitle = 1;
    }
  })

  //$("#inputTable").append("<tr><th colspan='2'>Inputs</th></tr>");
  $.each(htmlInputs, function( index, input ) {
    $("#inputTable").append(input);
  })

  $.each(htmlOutputs, function( index, output ) {
    $("#outputTable").append(output);
  })
  $("#inputTable").append("<tr style='height:auto'></tr>")
  $("#outputTable").append("<tr style='height:auto'></tr>")

  // 2018Feb01 - Keep the title + description, just hide all inputs.
  if (!inputTitle) {
    $("#inputTable").css('display','none');
    $("#outputTable").removeClass("iwp-input-output-half-width-table");
    $("#outputTable").addClass("iwp-input-output-full-width-table");
  }
  if (!outputTitle) {
    $("#outputTable").css('display','none');
    $("#inputTable").removeClass("iwp-input-output-half-width-table");
    $("#inputTable").addClass("iwp-input-output-full-width-table");
  }

  $("#inputTable").css("height", $("#outputTable").css('height'));
  $("#outputTable").css("height", $("#inputTable").css('height'));



  fitText("#inputTable");
  /* Debugging 07 Oct 2016 Ryan Steed
  //$("#outputTable").append("<tr><th colspan='2'>Outputs</th></tr>"+htmlOutputs);
  */
  //Moved to addSolidsToCanvas, 8 Aug 2016
  //$("#solids").html( solids.join(" ") );
  
  renderCanvas();
  addSolidsToCanvas(svgSolids);
  addSolidsToCanvas(svgObjects);
};

//21 Sep 2016 Ryan Steed
//Auto-adjust font-size so that font size fits the table.
function fitText(input) {
    var HeightDiv = $("#tabTables").height();
    var WidthDiv = $("#tabTables").width();
    var toFit = $(input);
    // console.log("input: ", input);
    var HeightTable = toFit.height();
    var WidthTable = toFit.width();
    if (HeightTable > HeightDiv) {
        var FontSizeTable = parseInt(toFit.css("font-size"), 10);
        while (HeightTable > HeightDiv && FontSizeTable > 10) {
            FontSizeTable--;
            toFit.css("font-size", FontSizeTable);
            HeightTable = toFit.height();
        }
    //console.log("text fitted");
    }
    if (WidthTable > WidthDiv) {
        var FontSizeTable = parseInt(toFit.css("font-size"), 10);
        while (WidthTable > WidthDiv && FontSizeTable > 10) {
            FontSizeTable--;
            toFit.css("font-size", FontSizeTable);
            WidthTable = toFit.width();
        }
    }
    toFit.css("width", WidthDiv);
  };

function addSolidsToCanvas(solids) {

  for (i in solids) {
    $("#canvas").append(solids[i]);
  }

  //$("#canvas").append(solids);
  //Blitting effect
  $("#canvasDiv").html($("#canvasDiv").html());
}

function renderCanvas() {
  var c = $("#canvas")
  var g = $("#gridlines")
  // Parse viewbox attributes from canvas to override defaults.
  var canvasBoxAttrs = c[0].getAttribute("viewBox").split(" ");
  canvasBox = { minX: parseFloat(canvasBoxAttrs[0]), minY: parseFloat(canvasBoxAttrs[1]), maxX: parseFloat(canvasBoxAttrs[2]), maxY: parseFloat(canvasBoxAttrs[3]) };
  // To Render the window is that we start at the Xmin, and draw full vertial lines,
  // increment by xgrid,
  // stopping at xmax
  // Add X gridlines -- TODO CONVERT TO A FOR LOOP

  //Update window settings with user form data
  $.each(animationWindow, function(index, val) {
    animationWindow[index] = queryUserFormWindowDouble(index);
  })
  $(".gridline").remove();


  var xGridlines = (animationWindow.xmax - animationWindow.xmin)/animationWindow.xgrid;
  for ( var interval = 1; interval < xGridlines; interval ++ ) {
    var xGridPosition = (interval - xGridlines/2)*animationWindow.xgrid;
    //console.log("whatItShouldBe: "+xCanvas(xGridPosition*animationWindow.xgrid)+", coordinates: "+coordinates);
    g.append( "<path class='gridline' d='M " + xCanvasGridlines(xGridPosition) + " 0 V 1000' stroke='lightgray' fill='transparent'/>" )
    g.append( "<path class='gridline' d='M " + xCanvas(0) + " 0 V 1000' stroke='black' fill='transparent'/>" )
    };

  // Add Y gridlines
  var yGridlines = (animationWindow.ymax - animationWindow.ymin)/animationWindow.ygrid;
  //Debugging 7 Aug 2016
  //console.log("yGridlines: "+yGridlines);
  for ( var interval = 1; interval <= yGridlines-1; interval ++ ) {
    var yGridPosition = (interval - yGridlines/2)*animationWindow.ygrid;
    g.append( "<path class='gridline' d='M 0 " + yCanvasGridlines(yGridPosition) + " H 1000' stroke='lightgray' fill='transparent'/>" )
    g.append( "<path class='gridline' d='M 0 " + yCanvas(0) + " H 1000' stroke='black' fill='transparent'/>" )
  };



  // Blitting / Double buffering approach
  // redraw a single time!
  // http://stackoverflow.com/questions/3642035/jquerys-append-not-working-with-svg-element
  $("#canvasDiv").html($("#canvasDiv").html());


};

function queryTimeStepInputDouble() {
  time.change = parseFloat($("#itime_change").val());
  return time.change;
}
function queryTimeStartInputDouble() {
  time.start = parseFloat($("#itime_start").val());
  return time.start;
}
function queryTimeStopInputDouble() {
  time.stop = parseFloat($("#itime_stop").val());
  return time.stop;
}
function queryUserFormInputDouble(input) {

	var readValue = $("#" + input.name).val();
	var doubleValue = parseFloat(readValue);
	//console.log("queryUserDefinedInput: for input: ", input, " readValue: ", readValue,  "  doubleValue: ", doubleValue );
	// TODO if readValue doesn't make sense, then default back to input.initialValue;

	return doubleValue;
}
function queryUserFormWindowDouble(index) {
  var readValue = $("#iwindow_" + index).val();
  var doubleValue = parseFloat(readValue);
  //console.log("queryUserDefinedInput: for input: ", input, " readValue: ", readValue,  "  doubleValue: ", doubleValue );
  // TODO if readValue doesn't make sense, then default back to input.initialValue;

  return doubleValue;
}

function updateUserFormOutputDouble(output, newValue) {
  if ( typeof(output) != "undefined" ) {
  	var readValue = $("#" + output.name).val(newValue.toPrecision(5));
  }
}

function updateTimeDisplay(t) {

  var timeToDisplay = t.toPrecision(5);
  if ( timeToDisplay == 0 ) {
    timeToDisplay = t; // Handle very small numbers.
  }
	$("#itime").html(timeToDisplay);
  //console.log("t = "+t);
}

function updateSolidSvgPathAndShape(solid, pathAndShape) {

  // console.log("iwp5:1550> updateSolidSvgPathAndShape: solid : " + solid.name + "  solid: " , solid )

  var svgSolid = $("#solid_" + solid.name);
  var svgTrail = $("#solid_" + solid.name + "_trail")

	//console.log("updateSolidSvgPathAndShape: ", solid, svgSolid, pathAndShape);
	// translate from math to visual.
   /* pathAndShape: { height: 1, width: 1, x: 9, xdisp: 9, y: 0, ydisp: 0 }*/

  var shapeType = solid.shape.shapeType;

  if (shapeType == "circle") {
    svgSolid.attr("cx", xCanvas(pathAndShape.x))
		.attr("cy", yCanvas(pathAndShape.y))
		.attr("rx", xWidth(pathAndShape.width/2))
		.attr("ry", yHeight(pathAndShape.height/2));
    //console.log("rx: ", pathAndShape.width)
  }
  else if (shapeType == "rectangle") {
    svgSolid.attr("width", xWidth(pathAndShape.width))
		.attr("height", yHeight(pathAndShape.height))
    .attr("x", xCanvas(pathAndShape.x - pathAndShape.width / 2))
    .attr("y", yCanvas(pathAndShape.y + pathAndShape.height / 2));
 }
 else if (shapeType == "line") {

    var x1 = xCanvas(pathAndShape.x)
    var x2 = xCanvas(pathAndShape.x + pathAndShape.width)
    var y1 = yCanvas(pathAndShape.y)
    var y2 = yCanvas(pathAndShape.y + pathAndShape.height)

    // console.log("iwp6-animator:295> Line draw for : " + solid.name + " x1: " + x1 + " x2: " + x2 + " y1: " + y1 + " y2: " + y2)

    // DEBUGGING RYAN STEED 21 SEP 2016
    /*console.log("y1: ", yCanvas(pathAndShape.y));
    console.log("y2: ", pathAndShape.y + pathAndShape.height);*/
    svgSolid.attr("x1", x1)
    .attr("x2", x2)
    .attr("y1", y1)
    .attr("y2", y2);
  }
  else if (shapeType == "vector") {


    //http://stackoverflow.com/questions/10316180/how-to-calculate-the-coordinates-of-a-arrowhead-based-on-the-arrow
    var x1 = xCanvas(pathAndShape.x)
    var x2 = xCanvas(pathAndShape.x + pathAndShape.width)
    var y1 = yCanvas(pathAndShape.y)
    var y2 = yCanvas(pathAndShape.y + pathAndShape.height)

    // 2022Apr28 Debugging animation/winters-ncssm-2009/mirror-concave-ray-tracing-02.iwp
    /*
    if ( solid.name === "ray_1b") {
        console.log("iwp6-animator:335> Vector draw for : " + solid.name + " pathAndShape.x: ", pathAndShape.x );
        console.log("iwp6-animator:335> Vector draw for : " + solid.name + " pathAndShape.y: ", pathAndShape.y );
        console.log("iwp6-animator:335> Vector draw for : " + solid.name + " pathAndShape.width: ", pathAndShape.width );
        console.log("iwp6-animator:335> Vector draw for : " + solid.name + " pathAndShape.height: ", pathAndShape.height );
    }
    */

    // console.log("iwp6-animator:319> Vector draw for : " + solid.name + " x1: " + x1 + " x2: " + x2 + " y1: " + y1 + " y2: " + y2)

    var point1 = "" + x1 + "," + y1 + " "
    var point2 = "" + x2 + "," + y2 + " "
    var dx = x1 - x2
    var dy = y1 - y2
    var norm = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2))
    var udx = dx / norm
    var udy = dy / norm
    var ax = udx * Math.sqrt(3) / 2 - udy * 1 / 2
    var ay = udx * 1 / 2 + udy * Math.sqrt(3) / 2
    var bx = udx * Math.sqrt(3) / 2 + udy * 1 / 2
    var by = - udx * 1 / 2 + udy * Math.sqrt(3) / 2
    var arrow1 = "" + (x2 + 30 * ax) + "," + (y2 + 30 * ay) + " "
    var arrow2 = "" + (x2 + 30 * bx) + "," + (y2 + 30 * by) + " "

    // 2022Apr28 Debugging animation/winters-ncssm-2009/mirror-concave-ray-tracing-02.iwp
    /*
    if ( solid.name === "ray_1b") {
        console.log("iwp6-animator:335> Vector draw for : " + solid.name + " setting points to: ", (point1+point2+arrow1+point2+arrow2) )
        console.log("iwp6-animator:335> Vector draw for : " + solid.name + " ; point1: ", point1 );
        console.log("iwp6-animator:335> Vector draw for : " + solid.name + " ; point2: ", point2 );
        console.log("iwp6-animator:335> Vector draw for : " + solid.name + " ; arrow1: ", arrow1 );
        console.log("iwp6-animator:335> Vector draw for : " + solid.name + " ; arrow2: ", arrow2 );

    }
    */

    svgSolid.attr("points",point1+point2+arrow1+point2+arrow2)
  }
  else if (shapeType == "polygon") {

    // console.log("iwp5:1581> Redrawing Polygon: " + solid.name + " has Error? " + solid.calculationError );
    var points = pathAndShape.points
    pointsAttr = ""
    $.each( pathAndShape.points, function( index, i ) {
      pointsAttr += xCanvas(points[index].x+pathAndShape.x)+","+yCanvas(points[index].y+pathAndShape.y)+" "
      console.log("animator.js:329> Polygon i: " + i + " points: " , pointsAttr)
    });
    svgSolid.attr("points", pointsAttr)
  }
  else if (shapeType == "Bitmap" || shapeType == "bitmap") {

    console.log("iwp5:1720> Bitmap type! solid: " , solid,  "  pathAndShape: " , pathAndShape )
		//TODO: try changing animation from bitmap to rectangle to see calculation differences
    var angle = pathAndShape.angle*-180/Math.PI
    var xTran = xCanvas(pathAndShape.x)//+xWidth(pathAndShape.width*2)/2
    var yTran = yCanvas(pathAndShape.y)//+yHeight(pathAndShape.height*2)/2
		var xTran2 = -xWidth(pathAndShape.width*2)/2
		var yTran2 = -yHeight(pathAndShape.height*2)/2

    var domId = "#solid_"+solid.name; //3-20-20 removed .toLowerCase()
    var solidSvg = $(domId);

    // console.log("iwp5:1423> Bitmap type,  id: ", domId, " xTran: ", xTran, " yTran: ", yTran, "  Angle: ", pathAndShape.angle, "  solidSvg: ", solidSvg);


    solidSvg.attr("x",xCanvas(pathAndShape.x))
    .attr("y",yCanvas(pathAndShape.y))
    .attr("preserveAspectRatio","none")
    .attr("width",xWidth(pathAndShape.width*2))
    .attr("height",yHeight(pathAndShape.height*2))
    if (angle) {
      svgSolid.attr("transform","rotate("+angle+" "+xTran+" "+yTran+") translate("+xTran2+" "+yTran2+")");
    } else {
			svgSolid.attr("transform","translate("+xTran2+" "+yTran2+")");
		}



  }
  else {
  	//Debugging 25 Jan 2017
    //throw "Object in problem";
    console.log("!! Unidentified shape:550> solid = ", solid);
    return;
  };

  //console.log("solid: ",solid)
  //console.log("currentStep:",currentStep)
  if (solid.shape.drawTrails) {
    var points = []
    var pointsAttr = ""
    for (i in varsAtStep) {

      // 2018Mar01 - Limit rendering up to current step
      if ( i <= currentStep ) {
          // console.log("iwp5.js:1437> Building Trail Points: solid.name: " , solid.name, " i: ", i, "  currentStep: " , currentStep, " currentState: " , currentState );
          var currentState = varsAtStep[i][solid.name]
          //console.log("currentState",currentState)
          pair = {x: currentState.x, y: currentState.y }
          points.push(pair)
      }
    }
    $.each( points, function( index, i ) {
      pointsAttr += xCanvas(points[index].x)+","+yCanvas(points[index].y)+" "
      // console.log("iwp5.js:1443> Drawing Trail Points attr: ", pointsAttr );

    });
    svgTrail.attr("points", pointsAttr)
  }


/*for rectangle
		x, y
for line
		lineData -> linear interpolation*/
}

// 2020Feb21 Refactor

function updateFloatingTextSvgPathAndShape(text, pathAndShape) {

  var svgText = $("#text_" + text.name);

  // 2020Feb21 - Changed Structure of Floatingtext to a new object type with no shape.
  if (text.objectType == "floatingText" ||  text.shape.type == "edu.ncssm.iwp.objects.floatingtext.DObject_FloatingText") {

    // console.log("iwp5:1698> Floating Text Calculation: name: ", text.name, "  pathAndShape: ", pathAndShape );

    var safeText = text.text
    if ( text.text == null || text.text instanceof Object ) { safeText = ""; }

    var safeUnits = text.units
    if ( text.units == null || text.units instanceof Object ) { safeUnits = ""; }

    var newLabel = safeText

    if ( text.showValue ) {
        // console.log("iwp5:1411> Printing Decimal for : ", incomingNumber );
        var formatted = printDecimal( pathAndShape.value , 2 )
        //var formatted = pathAndShape.objectValue
        newLabel = safeText + " " + formatted + " " + safeUnits
    }

    var x = xCanvas(pathAndShape.x)
    var y = yCanvas(pathAndShape.y)

    // console.log("iwp5:1638> Floating Text Animation, x: " + x +  " y: " + y + " moving svgText: " , svgText)
    svgText.attr("x",x).attr("y",y).html(newLabel)

  } else {
    console.log("iwp6-animator:434> updateFloatingTextSvgPathAndShape: Warning, text was not objectType='floatingText': " , text);
  }

}







//Click handles.
function handleStartClick() {
	handleGoClick();
}

var buttonIds  = { startStop: "startStopButton", back: "backButton", forward: "forwardButton", reset: "resetButton" }
var stepTrigger;

//Restarts motion.
function handleGoClick() {
	stepTrigger = setInterval("handleStep()", 1 / time.fps * 1000);

	setStepDirection(1);

	document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleStopClick()");
	$("#startStopIcon").attr("class", "fa fa-pause fa-lg");
}
//Stops motion.
function handleStopClick() {
//Stop move and time functions.
	clearInterval(stepTrigger);
	setStepDirection(0);
	document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleGoClick()");
	$("#startStopIcon").attr("class", "fa fa-play fa-lg");
};
//Resets simulation.
function handleResetClick() {
  renderCanvas();
  graphSetWindowFromAnimation("pos", graphWindow);	
  graphSetWindowFromAnimation("vel", graphWindow);
  graphSetWindowFromAnimation("accel", graphWindow);
  updateTimeDisplay(0);
  handleStopClick();
	var vars0 = masterResetSteps();
  graphResetZero(0, vars = vars0, solids = parsedAnimation.solids(), "pos", graphWindow );
  graphResetZero(0, vars = vars0, solids = parsedAnimation.solids(), "vel", graphWindow );
  graphResetZero(0, vars = vars0, solids = parsedAnimation.solids(), "accel", graphWindow );

	//document.getElementById(buttonIds.startStop).setAttribute("class", "Start");
	document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleStartClick()");
}

function handleBackClick() {
	clearInterval(stepTrigger);
	stepBackwardAndPause();
	document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleGoClick()");
	$("#startStopIcon").attr("class", "fa fa-play fa-lg");
};
function handleForwardClick() {
	clearInterval(stepTrigger);
	stepForwardAndPause();
	document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleGoClick()");
	$("#startStopIcon").attr("class", "fa fa-play fa-lg");
};

function handleInputChange() {
  $("*").change( function () {
                  handleResetClick();
                });
}
handleInputChange();
