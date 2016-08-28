/** 
 * Interactive Web Physics 5 - Javascript SVG Animation Implementation
 * Ryan Steed, Taylor Brockman 2016
 */

//-----------------------------------------------------------------------
// Memory Intialization + Globals Section

var time = {};
var description = "";
var iwindow = {};
var graphWindow = {};

var inputs = [];
var outputs = [];
var solids = [];

var htmlInputs = [];
var htmlOutputs = [];
var svgSolids = [];


// What is a step? An interation that is a multiple of t delta.   Current T =   T0 + step * Tdelta
// Set of the variables in their state at that point.
// The inputs are replicated into each of the steps.
// The object for the step is exactly what is passed into the equation calculation.
// Need to support complex variables in the equation like ball.x

var varsAtStep = [];
var currentStep = 0;
var changeStep = 0; 	// -1 = backwards, 0 = stopped, 1 = forwards.

function masterResetSteps() { 
	currentStep = 0;
	changeStep = 0;
	varsAtStep = [];
}



//-----------------------------------------------------------------------
// Pseudocode for Mathematical Animation

// Calculate Initial Variable Set from inputs.

// Calculate at initial step to render displays

// Calculate for each step as time ticks.

// Provide a historical variable set at each tick (for trails, graphing).

// Monkey Patch the Match space to extend in our own functions.
// Converts from degrees to radians. this makes the 'toRadians(..)' function available in the Calculator space.
math.toRadians = function(degrees) { return degrees * Math.PI / 180; };
 // Converts from radians to degrees.
math.toDegrees = function(radians) { return radians * 180 / Math.PI; };

// Likely the are are other physical constants that we'll need to load as well.
varsConstants = { G : -9.8 }


function setStepDirection(newDirection) {
	changeStep = newDirection;
}

function stepForwardAndPause() { 
	setStepDirection(1);
	handleStep();
	setStepDirection(0);
}

function stepBackwardAndPause() { 
	setStepDirection(-1);
	handleStep();
	setStepDirection(0);
}


// forward and back stops animation


/**
 * MAjor function - called every time the animation time changes 
 */
function handleStep() { 

	console.log("handleStep:87> current: " , currentStep, "  change: ", changeStep );
	// apply the time change.
	var newStep = currentStep + changeStep;

	// handle time horizons
	if ( newStep < 0 ) { 
		changeStep = 0;
		newStep = 0;
	} 
	// TODO - end of time.

	console.log("handleStep:61> newStep: " + newStep)

	if ( newStep != currentStep ) { 

		// UI rendering is handled by the calculate as a side effect
		var vars = calculateVarsAtStep(newStep);

		archiveVarsAtStep( newStep, vars );
	}

	currentStep = newStep;

}


function archiveVarsAtStep( step, vars ) {
	varsAtStep[step] = vars
}


function calculateVarsAtStep(step) { 

	// vars should be a map of string to double, including the mathematica / physical constants.
	var vars = { step: step }
	$.extend(vars, varsConstants);

	// Eveything begins with time, populate t.
	vars.t = time.start + step * time.change;
	vars.tDelta = time.change;

	updateTimeDisplay(vars.t);


	/*
	for each input
		query the dom by input_$id to get the value from the user form
	*/
	$.each( inputs, function( index, input ) {
		// next load in variables for all of the inputs.
		vars[input.name] = { value:  queryUserFormInputDouble(input) };
    });

	/* for each solid
		sequence of the solids does matter in the problem file. 
	*/
	$.each( solids, function( index, solid ) {
		/*
		for x, y, h, w , perform the calculator 
		*/

		// TODO - Derive Velocity and Acceleration!

		var x = evaluateCalculator( solid.xpath.calculator, vars )

		var y = evaluateCalculator( solid.ypath.calculator, vars )


		calc = { 
			x: x,
			xdisp: x,
			y: y,
			ydisp: y,
			height: evaluateCalculator( solid.shape.height.calculator, vars ),
			width: evaluateCalculator( solid.shape.width.calculator, vars )
		}

		vars[solid.name] = calc

		//	-> update the DOM with theose new results
		updateSolidSvgPathAndShape(solid, calc)
    });


/*
	for each output perofrm the calculator
*/
	$.each( outputs, function( index, output ) {

		var newValue = evaluateCalculator( output.calculator, vars );
		vars[output.name] = newValue;

		// -> update the DOM with the new reuslts.
		updateUserFormOutputDouble(output, newValue);
    });


	console.log(" calculateVarsAtStep, vars = ", vars);
	vars;
}

// After the problem parse, we want to call :   calculateVarsAtStep(currentStep = 0);


//-----------------------------------------------------------------------
// Parsing Section

// "time": { "start": "0.0", "stop": "5.0", "change": "0.02",  "fps": "25.0" },
function setTime(inTime) { 
   console.log("time :", inTime);
   time = { 
   	start : parseFloat(inTime.start),
   	stop : parseFloat(inTime.stop),
   	change : parseFloat(inTime.change),
   	fps : parseFloat(inTime.fps)
   };
}

// "description": { "text": "A ball is attached to a horizontal spring (not shown) which causes the ball to oscillate about the origin. Run the animation until it stops. Click on Show graph. \n\nWhich graph represents position vs. time?  How do you know?\nWhich graph represents velocity vs. time?  How do you know?\nWhich graph represents acceleration vs. time?  How do you know?\n\nWhat would a graph of net force on the ball vs. time look like?  Why?" 
function setDescription(inDescription) { 
  //console.log("description :", inDescription);
   description = inDescription;
}

// "window": { "xmin": "-10.0", "xmax": "10.0", "ymin": "-10.0", "ymax": "10.0", "xgrid": "2.0", "ygrid": "1.0", "xunit": "meters", "yunit": "meters"
function setWindow(inWindow) { 
  //console.log("window :", inWindow);
  iwindow = inWindow;
}


// "GraphWindow": { "xmin": "0.0", "xmax": "5.0", "ymin": "-50.0", "ymax": "50.0", "xgrid": "0.5", "ygrid": "10.0"
function setGraphWindow(inGraphWindow) {
  //console.log("graphWindow :", inGraphWindow);
  graphWindow = inGraphWindow;
}


function addInput(input) { 
  //console.log("addInput: ", input );
  inputs.push( input );
  // {name: "ar", text: "Amplitude", initialValue: "9.0", units: "m"}
  htmlInputs.push( "<tr id='input_" + input.name + "' class='bottomBorder'><td><label for='"+ input.name +"'>"+ input.text +"</label></td><td><input id='" + input.name + "' type='text' value='" + input.initialValue + "'> " + input.units + "</td></tr>");
}

function addOutput(output) { 
  //console.log("addOutput ", output );

  var compiledOutput = { 
  	name: output.name,
  	text: output.text,
  	units: output.units,
  	calculator:  compileCalculator(output.calculator)
  }

  outputs.push( compiledOutput );
  // { "name": "axr", "text": "Acceleration", "units": "m/ss", "calculator": { "@attributes": { "type": "parametric" }, "value": "Red.xaccel" } }
  htmlOutputs.push( "<tr id='output_" + output.name + "' class='bottomBorder'><td><label for='"+ output.name +"'>"+ output.text +"</label></td><td><input id='" + output.name + "' type='text' value='-999'> " + output.units + "</td></tr>");
}




function addSolid(solid) { 
  //console.log("solid: ", solid );
  // console.log("addSolid width: ", solid.shape.width.calculator.value);

  // In Memory - PreParse Equations with math.js

  var compiledSolid = { 
  	name: solid.name,
  	color: { 
  		red: parseFloat(solid.color.red),
		green: parseFloat(solid.color.green),
  		blue: parseFloat(solid.color.blue),
  	},
  	shape: {
  		type: solid.shape["@attributes"].type,
  		drawTrails: solid.shape["@attributes"].drawTrails,
  		drawVectors: solid.shape["@attributes"].drawVectors,
  		graphOptions: solid.graphOptions,
  		width: { 
  			calculator: compileCalculator(solid.shape.width.calculator)
  		},
  		height: { 
  			calculator: compileCalculator(solid.shape.height.calculator)
  		}
  	},
  	xpath: { 
  		calculator : compileCalculator(solid.xpath.calculator)
  	},
	ypath: { 
  		calculator : compileCalculator(solid.ypath.calculator)
  	}
  };
  solids.push(compiledSolid);


  //HTML 
  if (solid.shape["@attributes"].type == "circle") {
    console.log("it's a circle");
    svgSolids.push( "<ellipse id='solid_" +solid.name+ "' cx='500' cy='500' rx=" +xWidth(solid.shape.width.calculator.value)+ " ry=" +yHeight(solid.shape.height.calculator.value)+ " style='fill:rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")'> " );
  }
  else if (solid.shape["@attributes"].type == "rectangle") {
    console.log("it's a rectangle");
    svgSolids.push( "<rect x='500' y='500' id='solid_" +solid.name+ "' width=" +xWidth(solid.shape.width.calculator.value)+ " height=" +yHeight(solid.shape.height.calculator.value)+ " style='fill:rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")'> " );
  }
  else if (solid.shape["@attributes"].type == "line") {
    console.log("it's a line")
    //solids.push( "<path d='M ...
  }
  else {
    return;
  };
}

//-----------------------------------------------------------------------
// Calculation Section


function compileCalculator(iwpCalculator) { 

	var incomingType = iwpCalculator["@attributes"].type
	if ( incomingType == "parametric" ) {

		/**
			<calculator type="parametric">
					<value>ar.value*cos((kr.value/mr.value)^.5*t+toRadians(pr.value)*1)</value>
				</calculator>
		*/

		var c = { 
			type: "mathjs",
			compiled: math.compile( iwpCalculator.value ),
			equation: iwpCalculator.value
		}
		// console.log("compileCalculator:171> value : ", iwpCalculator.value, " compiledTo: ", c)
		return c;

	} else { 
		console.log("DEBUG ERROR: Only parameteric calculator supported in the August 2016 version, unable to handle: ", incomingType);
		return {};
	}
}


function evaluateCalculator( calculator, vars ) {

	if ( calculator.type == "mathjs" )   {
		/*
			{type : mathjs, compiled: Object}
		*/

		try { 
			var result = calculator.compiled.eval(vars);
			return result;
		} catch ( err ) { 
			console.log("evaluateCalculator:260> Unable to evaluate calculator: ", err, calculator.equation, vars);
			return -1;
		} 

	} else { 
		console.log("DEVELOPER TODO: Unsupproted calculator type : ", calculator);
		return -1;
	}


}




function parseProblemToMemory( problem ) { 

  $("#authorUsername").html( problem.author.username );

  // Time
  setTime(problem.objects.time);

  // Description
  setDescription(problem.objects.description);

  // Window
  setWindow(problem.objects.window);

  // GraphWindow
  setGraphWindow(problem.objects.graphWindow);


  // Inputs - These could be an array OR a single item.
  if ( $.type ( problem.objects.input ) == 'array' ) { 
    $.each( problem.objects.input, function( index, input ) {
      addInput(input);
    });
  } else { 
    addInput(problem.objects.input);
  }

  // Output - These could be an array OR a single item.
  if ( $.type ( problem.objects.output ) == 'array' ) { 
    $.each( problem.objects.output, function( index, output ) {
      addOutput(output);
    });
  } else { 
    addOutput(problem.objects.output);
  }

  // Solids - These could be an array OR a single item.
  if ( $.type ( problem.objects.solid ) == 'array' ) { 
    $.each( problem.objects.solid, function( index, solid ) {
    addSolid(solid);
    });
  } else { 
    addSolid(problem.objects.solid);
  }
}







//--------------------------------------------------------------------------------
// SVG ViewBox Scaling

var canvasBox = { minX: 0, minY: 0, maxX: 1000, maxY: 1000 };
function yCanvas(y) {
  var yDomain = iwindow.ymax - iwindow.ymin;
  var yProportion = y / yDomain;
  var yCorrected = yProportion + 0.5;
  var cDomain = canvasBox.maxY - canvasBox.minY;
  var cProportion = yCorrected * cDomain;
  return cProportion;
};

function xCanvas(x) {
// the proportional entry point in from window.xmin -> window.xmax needs to be interpolated into the 
// propotional exit point between viewbox.minX -> viewbox.maxX 
  var xDomain = iwindow.xmax - iwindow.xmin;
  var xProportion = x / xDomain;

  // xProprotion is a value between -1 -> 1
  // xproprtion + 1 is a value between 0 -> 2

  var xCorrected = xProportion + 0.5;

  //Debugging 29 Jul 2016
  //console.log("json:205: x:  ", x, "  xDomain: ", xDomain,  "  xProportion: ", xProportion, "  xCorrected: ", xCorrected );

  var cDomain = canvasBox.maxX - canvasBox.minX;
  var cProportion = xCorrected * cDomain;

  return cProportion;
};

function xWidth(size) {
  var xDomain = iwindow.xmax - iwindow.xmin;
  var cDomain = canvasBox.maxX - canvasBox.minX;
  var proportion = cDomain/xDomain; 
  return size*proportion;
};

function yHeight(size) {
  var yDomain = iwindow.ymax - iwindow.ymin;
  var cDomain = canvasBox.maxY - canvasBox.minY;
  var proportion = cDomain/yDomain; 
  return size*proportion;
};


//--------------------------------------------------------------------------------
// DOM Manipulation

function renderProblemFromMemory() { 
  // Render from memory into page

  $("#itime").html( time.start.toFixed(2) );
  $("#description").html( description.text );

//Debugging 29 Jul 2016
//console.log("setting xmin val: ", iwindow.xmin );
  $("#iwindow_xmin").val( iwindow.xmin );
  $("#iwindow_xmax").val( iwindow.xmax );
  $("#iwindow_xgrid").val( iwindow.xgrid );
  $("#iwindow_xunit").val( iwindow.xunit );
  $("#iwindow_ymax").val( iwindow.ymax );
  $("#iwindow_ymin").val( iwindow.ymin );
  $("#iwindow_ygrid").val( iwindow.ygrid );
  $("#iwindow_yunit").val( iwindow.yunit );

// GraphWindow is a TODO feature for now.
// $("#graphWindow").html( graphWindow );

  $("#variableTable").append("<tr><th colspan='2'>Inputs</th></tr>"+htmlInputs+"<tr><th colspan='2'>Outputs</th></tr>"+htmlOutputs);
  //Moved to addSolidsToCanvas, 8 Aug 2016
  //$("#solids").html( solids.join(" ") );

  renderCanvasFromMemory();
  addSolidsToCanvas(svgSolids);
};



function addSolidsToCanvas(solids) {
  //console.log("solids: ", solids);
  $("#canvas").append(svgSolids);
  //Blitting effect
  $("#canvasDiv").html($("#canvasDiv").html());
}
function renderCanvasFromMemory() { 
  var c = $("#canvas");
  // Parse viewbox attributes from canvas to override defaults.
  var canvasBoxAttrs = c[0].getAttribute("viewBox").split(" ");
  canvasBox= { minX: parseFloat(canvasBoxAttrs[0]), minY: parseFloat(canvasBoxAttrs[1]), maxX: parseFloat(canvasBoxAttrs[2]), maxY: parseFloat(canvasBoxAttrs[3]) };
  // To Render the window is that we start at the Xmin, and draw full vertial lines, 
  // increment by xgrid,
  // stopping at xmax
  // Add X gridlines -- TODO CONVERT TO A FOR LOOP
  /*Shifted to for loop, 7 Aug 2016:c.append( "<path d='M " + xCanvas(-8) + " 0 V 1000' stroke='lightgray' fill='transparent'/>" );
  c.append( "<path d='M " + xCanvas(-6) + " 0 V 1000' stroke='lightgray' fill='transparent'/>" );
  c.append( "<path d='M " + xCanvas(-4) + " 0 V 1000' stroke='lightgray' fill='transparent'/>" );
  c.append( "<path d='M " + xCanvas(-2) + " 0 V 1000' stroke='lightgray' fill='transparent'/>" );
  c.append( "<path d='M " + xCanvas(0) + " 0 V 1000' stroke='black' fill='transparent'/>" );
  c.append( "<path d='M " + xCanvas(2) + " 0 V 1000' stroke='lightgray' fill='transparent'/>" );
  c.append( "<path d='M " + xCanvas(4) + " 0 V 1000' stroke='lightgray' fill='transparent'/>" );
  c.append( "<path d='M " + xCanvas(6) + " 0 V 1000' stroke='lightgray' fill='transparent'/>" );
  c.append( "<path d='M " + xCanvas(8) + " 0 V 1000' stroke='lightgray' fill='transparent'/>" );
  */
  
  var xGridlines = (iwindow.xmax - iwindow.xmin)/iwindow.xgrid;
  for ( var interval = 1; interval <= xGridlines-1; interval ++ ) {
    var xGridPosition = (interval - xGridlines/2)*iwindow.xgrid;
    var coordinates = xCanvas(xGridPosition*iwindow.xgrid);;
    //console.log("whatItShouldBe: "+xCanvas(xGridPosition*iwindow.xgrid)+", coordinates: "+coordinates);
    if (xGridPosition == 0) {
      c.append( "<path d='M " + xCanvas(xGridPosition) + " 0 V 1000' stroke='black' fill='transparent'/>" );
    }
    else {
      c.append( "<path d='M " + xCanvas(xGridPosition) + " 0 V 1000' stroke='lightgray' fill='transparent'/>" );
    };
  };
  // Add Y gridlines
  var yGridlines = (iwindow.ymax - iwindow.ymin)/iwindow.ygrid;
  //Debugging 7 Aug 2016
  //console.log("yGridlines: "+yGridlines);
  for ( var interval = 1; interval <= yGridlines-1; interval ++ ) {
    var yGridPosition = (interval - yGridlines/2)*iwindow.ygrid;
    if (yGridPosition == 0) {
      c.append( "<path d='M 0 " + yCanvas(yGridPosition) + " H 1000' stroke='black' fill='transparent'/>" );
      //console.log("it's the origin");
    }
    else {
      c.append( "<path d='M 0 " + yCanvas(yGridPosition) + " H 1000' stroke='lightgray' fill='transparent'/>" );
      //console.log("it's not");
    };
  };

  // Blitting / Double buffering approach
  // redraw a single time!
  // http://stackoverflow.com/questions/3642035/jquerys-append-not-working-with-svg-element
  $("#canvasDiv").html($("#canvasDiv").html());
};



function queryUserFormInputDouble(input) {

	var readValue = $("#" + input.name).val();
	var doubleValue = parseFloat(readValue);
	//console.log("queryUserDefinedInput: for input: ", input, " readValue: ", readValue,  "  doubleValue: ", doubleValue );

	// TODO if readValue doesn't make sense, then default back to input.initialValue;
	
	return doubleValue;
}

function updateUserFormOutputDouble(output, newValue) { 
	var readValue = $("#" + output.name).val(newValue);
}

function updateTimeDisplay(t) { 
	$("#itime").val(t.toFixed(2));

}

function updateSolidSvgPathAndShape(solid, pathAndShape) { 
	
	var svgSolid = $("#solid_" + solid.name);

	console.log("updateSolidSvgPathAndShape: ", solid, svgSolid, pathAndShape);

	// translate from math to visual.
	/* pathAndShape: { height: 1, width: 1, x: 9, xdisp: 9, y: 0, ydisp: 0 }*/

if (solid.shape.type == "circle") {
    svgSolid.attr("cx", xCanvas(pathAndShape.x))
		.attr("cy", yCanvas(pathAndShape.y))
		.attr("rx", xWidth(pathAndShape.width))
		.attr("ry", yHeight(pathAndShape.height));
  }
  else if (solid.shape.type == "rectangle") {
  	svgSolid.attr("x", xCanvas(pathAndShape.x))
		.attr("y", yCanvas(pathAndShape.y))
		.attr("width", xWidth(pathAndShape.width))
		.attr("height", yHeight(pathAndShape.height));
 }
  else if (solid.shape.type == "line") {
    console.log("it's a line");
    //line...
  }
  else {
  	console.log("!! Unidentified shape:550> solid = ", solid.shape.type);
    return;
  };


/*for rectangle
		x, y
for line
		lineData -> linear interpolation*/
}


//Hide and reveal windowSettings table.
function windowSettingsOn() {
	$("#iwindow").attr("style", "visibility:visible");
	$("#windowSettings").attr("onclick", "windowSettingsOff()");
};
function windowSettingsOff() {
	$("#iwindow").attr("style", "visibility:hidden");
	$("#windowSettings").attr("onclick", "windowSettingsOn()");
}


function handleStartClick() {
	handleGoClick();
	showReset();
}

var buttonIds  = { startStop: "startStopButton", back: "backButton", forward: "forwardButton", reset: "resetButton" }
var stepTrigger;

//Restarts motion.
function handleGoClick() {
	stepTrigger = setInterval("handleStep()", 100);

	setStepDirection(1);

	document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleStopClick()");
	document.getElementById(buttonIds.startStop).setAttribute("value", "Stop");
}
//Stops motion.					
function handleStopClick() {
//Stop move and time functions.
	clearInterval(stepTrigger);
	setStepDirection(0);
	document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleGoClick()");
	document.getElementById(buttonIds.startStop).setAttribute("value", "Resume");
};
//Resets simulation.
function handleResetClick() {
	handleStopClick();
	masterResetSteps();
	document.getElementById(buttonIds.startStop).setAttribute("value", "Start");
	document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleStartClick()");
}

function handleBackClick() {
	clearInterval(stepTrigger);
	stepBackwardAndPause();
	document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleGoClick()");
	document.getElementById(buttonIds.startStop).setAttribute("value", "Resume");
};
function handleForwardClick() {
	clearInterval(stepTrigger);
	stepForwardAndPause();
	document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleGoClick()");
	document.getElementById(buttonIds.startStop).setAttribute("value", "Resume");
};


function showReset() {
	document.getElementById(buttonIds.reset).style.visibility = "visible";
	document.getElementById(buttonIds.startStop).setAttribute("colspan", "1");
}
function hideReset() {
	document.getElementById(buttonIds.reset).style.visibility = "collapse";
	document.getElementById(buttonIds.startStop).setAttribute("colspan","2");
}

