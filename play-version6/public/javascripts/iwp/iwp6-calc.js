/**
 * Interactive Web Physics 6 - Pure Javascript implementation of calculators and loaders
 * Ryan Steed, Taylor Brockman 2016 - Version 5.0 Initial Port to HTML + SVG
 * Matthew Mims, Taylor Brockman 2018  - Version 5.1 Added Graphing and D3
 * Albert Gong, Nial Mullane, Taylor Brockman 2019 - Version 6.0 Migrated into Play Framework
 */


// -----------------------------------------------
// Sugar from http://youmightnotneedjquery.com/

var deepExtend = function(out) {
  out = out || {};

  for (var i = 1; i < arguments.length; i++) {
    var obj = arguments[i];

    if (!obj)
      continue;

    for (var key in obj) {
      if (obj.hasOwnProperty(key)) {
        if (typeof obj[key] === 'object')
          out[key] = deepExtend(out[key], obj[key]);
        else
          out[key] = obj[key];
      }
    }
  }

  return out;
};

// ------------------------------------------------
// Sugar from https://www.n-k.de/riding-the-nashorn/
// Because iwp6-calc can execute in server side nashorn, I have to redeclare console there.

if ( typeof console === "undefined" ) {  // Prevent console redefinition
    var console = {};
    console.debug = print;
    console.log = print;
    console.warn = print;
    console.error = print;
}


//-----------------------------------------------------------------------
// Memory Intialization + Globals Section

var parsedAnimation = null;

var time = {};
var description = "";
var animationWindow = {};
var graphWindow = {};

// IWP6 - Objects are now in a master loop, not separated by type.
var compiledObjects = [];

// User Interface Components, TODO Refactor our
var htmlInputs = [];
var htmlOutputs = [];
var svgSolids = [];
var svgObjects = [];


// What is a step? An interation that is a multiple of t delta.   Current T =   T0 + step * Tdelta
// Set of the variables in their state at that point.
// The inputs are replicated into each of the steps.
// The object for the step is exactly what is passed into the equation calculation.
// Need to support complex variables in the equation like ball.x


// IWP6 - Nashorn accessibility with some javascript function overloading.
function varsAtStepJson(step) {
    if ( ( typeof step === "string" && step != "" ) ||
         ( typeof step === "number" && step >= 0 ) ) {
        return JSON.stringify(varsAtStep[+step]);
    } else {
        return JSON.stringify(varsAtStep);
    }
}




var varsAtStep = [];
var currentStep = 0;
var changeStep = 0; 	// -1 = backwards, 0 = stopped, 1 = forwards.

function masterResetSteps() {
	currentStep = 0;
	changeStep = 0;
	varsAtStep = [];


  // We also need to clear out previous parameteric displacements used for instant velecioty calculations.
  compiledObjects.forEach(function ( object, index) {
    if(object.objectType=='solid') {
        resetSolidCalculators(object);
    }
  });


  var vars0 = calculateVarsAtStep(0);

  // 2018Feb01 Graphing Reset hookin
  if ( typeof graphResetZero === "function" ) {
    graphResetZero(0, vars = vars0, solids = parsedAnimation.solids() );
  }

  archiveVarsAtStep( currentStep, vars0 ); // Boot up the environment

  return vars0;
}



//-----------------------------------------------------------------------
// Pseudocode for Mathematical Animation

// Calculate Initial Variable Set from inputs

// Calculate at initial step to render displays

// Calculate for each step as time ticks.

// Provide a historical variable set at each tick (for trails, graphing).

// 2018Mar01 Found that monkeypatching mathjs was non-reliable so now including in the vars scope.
// Converts from degrees to radians. this makes the 'toRadians(..)' function available in the Calculator space.
// Likely the are are other physical constants that we'll need to load as well.
varsConstants = {
 G : -9.8,
 pi : Math.PI,
 step: function(x) { if ( x > 0 ) { return 1 } else { return 0 } },
 toRadians : function(degrees) { return degrees * Math.PI / 180; },
 toDegrees : function(radians) { return radians * 180 / Math.PI; },
 sign: function(input) { if ( input < 0 ) { return -1 } else { return 1 } }
 }

// Global Number Formatting routine.
function printDecimal( incomingNumber, incomingPlaces ) {
    // console.log("iwp5:83> printDecimal: ", incomingNumber, " places: " , incomingPlaces );
    return parseFloat(Math.round(incomingNumber * Math.pow(10,incomingPlaces)) / Math.pow(10,incomingPlaces)).toFixed(incomingPlaces);
}

function setStepDirection(newDirection) {
	changeStep = +newDirection;
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
 * Major function - called every time the animation time changes
 */
function handleStep() {

	// console.log("iwp6-calc:167> HandleStep: current: " , currentStep, "  change: ", changeStep );
	// apply the time change.
	var newStep = currentStep + changeStep;

	// handle time horizons
	if ( newStep < 0 ) {
		changeStep = 0;
		newStep = 0;
	}

    // If we are beyond stop time don't do tanything.
    if ( typeof queryTimeStopInputDouble === 'function' && typeof queryTimeStepInputDouble === 'function' ) {
        // Animation Mode
        if (newStep > ( Math.round( queryTimeStopInputDouble() / queryTimeStepInputDouble()))) {

            if ( typeof handleStopClick === 'function' ) {
                handleStopClick();
            }
            // console.log("iwp6-calc:191> Animated end of time on step: " + newStep )
            return -1;
        }
    } else {
        // Headless mode
        if (newStep > ( Math.round( +time.stop / +time.change ))) {
            if ( typeof handleStopClick === 'function' ) {
                handleStopClick();
            }
            //console.log("iwp6-calc:191> Animated end of time on step: " + newStep )
            return -1;
        }
    }

	//console.log("handleStep:61> newStep: " + newStep)

    if ( newStep != currentStep ) {

        // Back button poerformance - Let's look at the historical array of variables, if we have it, reload
        // to avoid doing a recalcaultion

        var vars = varsAtStep[newStep];
        if ( vars != undefined ) {
          //console.log("[iwp5.js:118] Step " + newStep + " already exist, reloading for history.")
          if ( typeof repaintStep === "function") {
            repaintStep(newStep);
          }

        } else {
            // UI rendering is handled by the calculate as a side effect
              vars = calculateVarsAtStep(newStep);
            archiveVarsAtStep( newStep, vars );
        }

        // iwp5.1 - Adding Hook into the graph capabilties
        if ( changeStep > 0 ) {
            if ( typeof graphStepForward === "function" ) {
                graphStepForward(newStep, vars);
            }
        } else if ( changeStep < 0 ) {
            if ( typeof graphStepBackward === "function" ) {
                graphStepBackward(newStep, vars);
            }
        }
    }

	currentStep = newStep;

	return newStep;
}


function archiveVarsAtStep( step, vars ) {
  // console.log("Archving vars at step: " + step );
  varsAtStep[step] = {};
  deepExtend(varsAtStep[step], vars)
}


function calculateInputAtStep(input, step, vars, verbose ) {

    // console.log("iwp6-calc:251> calculateInputAtStep input: " , input.name,  " ", input, "  step: " , step,  " vars: ", vars );

	// next load in variables for all of the inputs
	// If we're running in calculator only mode, use default values
    if ( typeof queryUserFormInputDouble === "function" ) {
        return queryUserFormInputDouble(input);
    } else {
        // console.log("iwp6-calc:442> Headless mode, setting initial value for: " + input.name + "  value: " + input.initialValue );
        return +input.initialValue;
    }

}

/** BUGBUG - TODE Change this over to have no side effects, all vars writing should happen in parent stack */
function calculateOutputAtStep(output, step, vars, verbose) {

    // console.log("iwp6-calc:267> calculateOutputAtStep: output: " , output.name , " ", output, "  step: " , step , " vars: " , JSON.stringify(vars) );

    var newValue = evaluateCalculator( output.name+".out", output.calculator, step, vars, verbose, output.name ).value;
    vars[output.name] = newValue;

    if ( typeof updateUserFormOutputDouble === 'function' ) {
        updateUserFormOutputDouble(output, newValue);
    }

    // console.log("iwp6-calc:267> calculateOutputAtStep:267> newValue: " + newValue);

    return newValue;
}


/**
 * 2018Dec14 Note! This is now pass by value, manipulating vars does no good,
 * The caller is responsible for assigning back to vars
 */
function calculateSolidAtStep(solid, step, vars, verbose) {

    // console.log("iwp6-calc:233> calculateSolidAtStep step: " + step + " solid: " + JSON.stringify(solid));

    var xComplex = evaluateCalculator( solid.name+".x", solid.xpath.calculator, step, vars, verbose, solid.name )

    // console.log("iwp6-calc:233> calculateSolidAtStep xComplex: "+ JSON.stringify(xComplex));

    var x = xComplex.value

    //----------------------------------------
    /// 2016-Dec-07 attempt to alleivate circular dependency.
    var calcX = {
        x: x,
        xdisp: x,
        xpos: x,
        xvel: 0,
        xaccel: 0
    }

    // Hande Complex return types from Euler Calculator
    if ( xComplex.velocity != undefined ) {

      calcX.xvel = xComplex.velocity
    }
    if ( xComplex.acceleration != undefined ) {
      calcX.xaccel = xComplex.acceleration
    }

    vars[solid.name] = calcX;
   //----------------------------------------

    var yComplex = evaluateCalculator( solid.name+".y", solid.ypath.calculator, step, vars, verbose, solid.name )
    var y = yComplex.value

    // console.log("iwp6-calc:265> calculateSolidAtStep yComplex: "+ JSON.stringify(yComplex));

    // Handle Complex return types from Euler Calculator
    var calcY = {
        y: y,
        ydisp: y,
        ypos: y,
        yvel: 0,
        yaccel: 0
    }


    if ( yComplex.velocity != undefined ) {
      calcY.yvel = yComplex.velocity
    }
    if ( yComplex.acceleration != undefined ) {
      calcY.yaccel = yComplex.acceleration
    }

    if ( xComplex.acceleration == null ) {
      console.log("iwp6-calc:338> Recaclulating X because acceleration was null!")
      vars[solid.name] = deepExtend(calcX,calcY);
      xComplex = evaluateCalculator( solid.name+".x", solid.xpath.calculator, step, vars, verbose, solid.name )
      x = xComplex.value
    }
    if ( yComplex.acceleration == null ) {
      console.log("iwp6-calc:344> Recaclulating Y because acceleration was null!")
      vars[solid.name] = deepExtend(calcX,calcY);
      yComplex = evaluateCalculator( solid.name+".y", solid.ypath.calculator, step, vars, verbose, solid.name )
      y = yComplex.value
    }


  //-------------------------------------
  // Height and width

  var height = evaluateCalculator( solid.name+".h", solid.shape.height.calculator, step, vars, verbose, solid.name ).value
  var width = evaluateCalculator( solid.name+".w", solid.shape.width.calculator, step, vars, verbose, solid.name ).value

  // console.log("iwp5:259> Solid: " + solid.name + " height + width calculation: width: " , width, " height: ", height)

  //-------------------------------------
    var calc = {
      x: x,
      xdisp: x,
      xpos: x,
      xvel: 0,
      xaccel: 0,
      y: y,
      ydisp: y,
      ypos: y,
      yvel: 0,
      yaccel: 0,
      height: height,
      width: width,
      angle: 0
    }


    if ( solid.shape.shapeType == "polygon" ) {

      // console.log("iwp5:277> Recalculating Polygon: " + solid.name + " has Error? " + solid.calculationError );

      calc["points"] = []
      solid.points.forEach( function( i, index ) {
        point = {
          x: evaluateCalculator(solid.name+".xpt", i.xpath.calculator, step, vars, verbose, solid.name ).value,
          y: evaluateCalculator(solid.name+".ypt", i.ypath.calculator, step, vars, verbose, solid.name ).value
        };
        calc.points.push(point);
      });


        //i.xpath = evaluateCalculator(solid.name+".x"+toString(counter), i.xpath.calculator, vars).value
        //i.ypath = evaluateCalculator(solid.name+".x"+toString(counter), i.ypath.calculator, vars).value
        //console.log("i after",i)
    }


    // console.log("iwp6-calc:400> [" + step + "] Attempting to evaluate solid shape angle for solid.name: " + solid.name + "  shape.angle: " , solid.shape.angle );
    if ( solid.shape.angle ) {
      calc.angle = evaluateCalculator( solid.name+".a", solid.shape.angle.calculator, step, vars, verbose, solid.name ).value
      // console.log("iwp6-calc:401> [" + step + "] Evaluated calculator for solid.name: " + solid.name + " .angle: ", solid.shape.angle, " to :", calc.angle  )
    }

    // For objects with a value beyond x , y, w , h -- This is used for the floatingText Value
    if ( solid.value != null && solid.value.calculator != null ) {
      calc.objectValue = evaluateCalculator( solid.name+".objectValue", solid.value.calculator, step, vars, verbose, solid.name ).value
    }


    // Hande Complex return types from Euler Calculator
    if ( xComplex.velocity != undefined ) {
      calc.xvel = xComplex.velocity
    }
    if ( xComplex.acceleration != undefined ) {
      calc.xaccel = xComplex.acceleration
    }
    if ( yComplex.velocity != undefined ) {
      calc.yvel = yComplex.velocity
    }
    if ( yComplex.acceleration != undefined ) {
      calc.yaccel = yComplex.acceleration
    }

    // console.log("iwp6-calc:373> Completed Solid calculation, writing back to vars: " + solid.name + "   calc: " + JSON.stringify(calc));
    vars[solid.name] = calc

    // WARNING: updating svg display deep inside the calc route.
    // Run in headless mode
    if ( typeof updateSolidSvgPathAndShape === "function" ) {
        updateSolidSvgPathAndShape(solid, calc)
    }

    return calc;
}




function calculateTextAtStep(text, step, vars, verbose) {

  // console.log("iwp5:327> calculateTextAtStep: " + text.name, text )

  var xComplex = evaluateCalculator( text.name+".x", text.xpath.calculator, step, vars, verbose, text.name )
  var x = xComplex.value

  var yComplex = evaluateCalculator( text.name+".y", text.ypath.calculator, step, vars, verbose, text.name )
  var y = yComplex.value

  var vComplex = evaluateCalculator( text.name+".value", text.value.calculator, step, vars, verbose, text.name )
  var v = vComplex.value



  // Dont' need to write texts back to variables.
  // vars[solid.name] = calc

  if ( typeof updateTextSvgPathAndShape === "function" ) {
    updateTextSvgPathAndShape(text, {x: x, y: y, value: v})
  }
}



// RENDERING steps:
//  updateTimeDisplay(vars.t);
//  updateUserFormOutputDouble(output, newValue);
//  updateSolidSvgPathAndShape(solid, calc)




var CONFIG_throw_solid_calculation_exceptions = false;





/**
 * 2019Apr09 Restructured logic to go by object sequence for IWP6
 */

function calculateVarsAtStep(step) {

  // D-Fence
  if ( compiledObjects == null || compiledObjects.length <= 0 ) {
    throw "calculateVarsAtStep("+step+") Empty compiledObjects array, has animation been initialized?"
  }

  // vars should be a map of string to double, including the mathematical / physical constants.
  var vars = { step: step }
  deepExtend(vars, varsConstants);


  // optional UI
  startTime = null;
  if ( typeof queryTimeStartInputDouble === "function" ) {
    startTime = queryTimeStartInputDouble()
  } else {
    startTime = time.start
  }

   // Everything begins with time, populate t.
  vars.t = startTime + step * time.change;
  vars.tDelta = time.change
  vars.delta_t = vars.tDelta
  vars.tDel = vars.tDelta
  vars.deltaTime = time.change


  if ( typeof updateTimeDisplay === "function" ) {
    updateTimeDisplay(vars.t);
  }

  // Collect user Inputs! These are polled from the DOM every step.
  compiledObjects.forEach( function(object, index) {

    // console.log("iwp6-calc:514> CalculationLoop for Object: " + JSON.stringify(object) );

    if ( object.objectType == 'input' ) {

       var newValue = calculateInputAtStep(object, step, vars, false );
       vars[object.name] = newValue;

       // console.log("iwp6-calc:521> Input name: " + object.name + "  newValue: "+ newValue );

    } else if ( object.objectType == 'output') {

// 2019Sep06 Turned on Verbose
        var newValue = calculateOutputAtStep(object, step, vars, true );

        if ( isNaN(newValue) ) { throw "not a number" };
        if ( !isFinite(newValue) ) { throw "not finite" };

        vars[object.name] = newValue;

        // console.log("iwp6-calc:536> Output name: " + object.name + "  newValue: "+ newValue );

    } else if ( object.objectType == 'solid') {

        var solid = object;

        // Initialize Euler's each loop if necessary
        if ( solid.xpath.calculator.calcType == "euler-mathjs" ) {
            initializeEulerCalculator(solid, step, vars, "x", solid.xpath.calculator)
        }

        if ( solid.ypath.calculator.calcType == "euler-mathjs" ) {
            initializeEulerCalculator(solid, step, vars, "y", solid.ypath.calculator)
        }

        var newValue = calculateSolidAtStep(object, step, vars, true );
        vars[solid.name] = newValue;

        // console.log("iwp6-calc:588> calculateSolidAtStep: " + object.name + "  newValue: " + newValue );

    } else if ( object.objectType == 'object') {

        // TODO Animate the Text
        console.log("iwp5:594> TODO Animate the text: object.name: " + object.name, object )

    } else {
        throw "iwp6-calc:599> Unrecognized Object type: " + object.objectType;
    }

  });

  return vars;
}


function initializeEulerCalculator(solid, step, vars, axis, calculator) {

  // Initialization -- If currentDisplacement or currentVelocity is empty!
  if (step == 0 || calculator.currentDisplacement == null) {
    calculator.initialDisplacement = evaluateCompiledMath(calculator.initialDisplacementCompiled, vars)
    calculator.currentDisplacement = calculator.initialDisplacement
  }
  if (step == 0 || calculator.currentVelocity == null) {
    calculator.initialVelocity = evaluateCompiledMath(calculator.initialVelocityCompiled, vars)
    calculator.currentVelocity = calculator.initialVelocity;
  }
  if ( typeof vars[solid.name] === "undefined" ) {
    vars[solid.name] = {}
  }
  vars[solid.name][axis] = calculator.currentDisplacement
  vars[solid.name][axis+"pos"] = calculator.currentDisplacement
  vars[solid.name][axis+"disp"] = calculator.currentDisplacement
  vars[solid.name][axis+"vel"] = calculator.currentVelocity
}



//-----------------------------------------------------------------------
// Parsing Section

// "time": { "start": "0.0", "stop": "5.0", "change": "0.02",  "fps": "25.0" },
function setTime(inTime) {
   //console.log("time :", inTime);
   time = {
   	start : parseFloat(inTime.start),
   	stop : parseFloat(inTime.stop),
   	change : parseFloat(inTime.change),
   	fps : parseFloat(inTime.fps)
   };
}

// "description": { "text": "A ball is attached to a horizontal spring (not shown) which causes the ball to oscillate about the origin. Run the animation until it stops. Click on Show graph. \n\nWhich graph represents position vs. time?  How do you know?\nWhich graph represents velocity vs. time?  How do you know?\nWhich graph represents acceleration vs. time?  How do you know?\n\nWhat would a graph of net force on the ball vs. time look like?  Why?"
// No Real Operation
function setDescription(inDescription) {
  //console.log("description :", inDescription);
  // Global
  description = inDescription;
}

// "window": { "xmin": "-10.0", "xmax": "10.0", "ymin": "-10.0", "ymax": "10.0", "xgrid": "2.0", "ygrid": "1.0", "xunit": "meters", "yunit": "meters"
// No Real Operation
function setWindow(inWindow) {
  //console.log("window :", inWindow);
  // Global
  animationWindow = inWindow;
}

 function initializeGraphVars(inGraphWindow) {
	graphWindow = inGraphWindow;
	$("#graph_xmin").val( graphWindow.xmin );
	$("#graph_xmax").val( graphWindow.xmax );
  	$("#graph_xgrid").val( graphWindow.xgrid );
  	$("#graph_ymax").val( graphWindow.ymax );
  	$("#graph_ymin").val( graphWindow.ymin );
  	$("#graph_ygrid").val( graphWindow.ygrid );
  }
  
// "GraphWindow": { "xmin": "0.0", "xmax": "5.0", "ymin": "-50.0", "ymax": "50.0", "xgrid": "0.5", "ygrid": "10.0"
function setGraphWindow(inGraphWindow) {
  // console.log("iwp5:553> graphWindow :", inGraphWindow);
  // Global
  graphWindow = inGraphWindow;
 // Hook into new iwp5-graph to redraw axes.
  if ( typeof graphSetWindowFromAnimation === "function" ) {
    graphSetWindowFromAnimation(graphWindow);
  } else {
    console.log("iwp6-calc:637> Did not detect the Graphing Library available, graphZetWindowFromAnimation: ", typeof graphSetWindowFromAnimation );
  }

}

function addInput(input) {
  input.objectType = 'input'


  // console.log("iwp6-calc:807> pushingInput: ", JSON.stringify(input) );

  compiledObjects.push( input );
  // {name: "ar", text: "Amplitude", initialValue: "9.0", units: "m"}
  // 07 Oct 2016 Honoring hidden flag
  var style = "";
  if ( input.hidden == "1" ) {
    style = "display:none;"
  }

    // 2018Mar01 Fix for empty unit labels
  var unitLabel = "";
  if ( typeof input.units ==="string" ) { unitLabel = input.units; }

  htmlInputs.push("<tr id='input_" + input.name + "' style='" + style + "' class='iwp-input-row'><td class='iwp-input-label'>"+ input.text +"</td><td class='iwp-input-value'><input style='width:60px;' id='" + input.name + "' type='text' value='" + input.initialValue + "'> " + unitLabel + "</td></tr>")
}

function addOutput(output) {
  //console.log("addOutput ", output );

  var compiledOutput = {
    objectType: 'output',
  	name: output.name,
  	text: output.text,
  	units: output.units,
  	calculator: compileCalculator(output.calculator),
    hidden: output.hidden //Hidden flag still needed - be careful about cutting off attributes here.
  }

  compiledObjects.push( compiledOutput );
  // { "name": "axr", "text": "Acceleration", "units": "m/ss", "calculator": { attributesProperty: { "type": "parametric" }, "value": "Red.xaccel" } }
  var style = ""
  if ( output.hidden == "1" ) {
    style = "display:none;'"
  }
  var unitLabelOutput = "";
  if ( typeof output.units === "string" ) { unitLabelOutput = output.units; }

  htmlOutputs.push( "<tr style='" + style +"vertical-align:top;' id='output_" + output.name + "' class='iwp-output-row'><td class='iwp-output-label'>"+ output.text +"</td><td class='iwp-output-value'><input id='" + output.name + "' type='text' value='-999' disabled style='width:80px;'> " + unitLabelOutput + "</td></tr>");
}

/**
 * 2016-Nov-09 - Reset the instantanous velcity calculations on reset
 * 2018Mar23 - Erase all internal state, including intialVelocities inside Euler.
 */
function resetSolidCalculators(solid) {

  // console.log("iwp5:753> resetSolidCalculators: " , solid )

  if ( solid.xpath && solid.xpath.calculator ) {
      solid.xpath.calculator.latestValue = undefined;
      solid.xpath.calculator.currentVelocity = undefined;
      solid.xpath.calculator.initialVelocity = undefined;
      solid.xpath.calculator.currentDisplacement = undefined;
      solid.xpath.calculator.initialDisplacement = undefined;
  }
  if ( solid.ypath && solid.ypath.calculator ) {
      solid.ypath.calculator.latestValue = undefined;
      solid.ypath.calculator.currentVelocity = undefined;
      solid.ypath.calculator.initialVelocity = undefined;
      solid.ypath.calculator.currentDisplacement = undefined;
      solid.ypath.calculator.initialDisplacement = undefined;
  }

  /** IWP5 is not yet using velocity on height + width calcs */
  /*
  if ( solid.xpath && solid.xpath.calculator ) {
      solid.width.calculator.latestValue = null;
  }
  if ( solid.ypath && solid.ypath.calculator ) {
      solid.height.calculator.latestValue = null;
  }
  */
  // BUGBUG - Not concerned about polypath reset yet.
}


function addSolid(solid) {
  //console.log("solid: ", solid );

  // console.log("iwp6-calc:992> addSolid: ", JSON.stringify(solid));

  // In Memory - PreParse Equations with math.js

  var compiledSolid = {
    objectType: 'solid',
  	name: solid.name,
  	color: {
  		red: parseFloat(solid.color.red),
		green: parseFloat(solid.color.green),
  		blue: parseFloat(solid.color.blue),
  	},
  	shape: {
  		shapeType: solid.shape.shapeType,
  		drawTrails: solid.shape.drawTrails,
  		drawVectors: solid.shape.drawVectors,
  		graphOptions:
        deepExtend( solid.shape.graphOptions,
                      { initiallyOn: solid.shape.graphOptions.initiallyOn } ),
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


  // If the animation iwp solid has a polygon shape, need to iterate over an initialize each of the calcualtors.
  // hard to do as part of the initialization because it is a dynamic list.
  // Add points here..?
  if ( compiledSolid.shape.shapeType == "polygon" ) {
    compiledSolid["points"] = []

    if ( ! solid.shape.points ) {
        console.log("iwp6-calc:766: The solid's polygon shape had no points! solid: ", solid);
    } else {
    solid.shape.points.point.forEach ( function( i, index ) {
      var point = {
        xpath: {calculator: compileCalculator(i.xpath.calculator),},
        ypath: {calculator: compileCalculator(i.ypath.calculator),},
      }
    compiledSolid.points.push(point)
    });
    }
    // console.log("iwp5:834> Compiled polygon: ",compiledSolid)
  }

  if ( compiledSolid.shape.shapeType == "Bitmap") {
    // console.log("iwp5:649> Solid is a Bitmap type, building angle: " , solid.shape.angle )
    compiledSolid.fileUri = "../../images/"+solid.shape.file.image.split("/images/")[1]
    // console.log("fileUri:",compiledSolid.fileUri)
  }

  // 2019Jan18 Promoted Angle processing to a more common location
  if ( typeof solid.shape.angle !== "undefined" ) {
    compiledSolid.shape.angle = {calculator: compileCalculator(solid.shape.angle.calculator)}
  }


  compiledObjects.push(compiledSolid);


  //HTML
  if (compiledSolid.shape.shapeType == "circle") {
    //console.log("it's a circle");
    svgSolids.push( "<ellipse id='solid_" +solid.name+ "' cx='500' cy='500' rx=" +xWidth(solid.shape.width.calculator.value)+ " ry=" +yHeight(solid.shape.height.calculator.value)+ " style='fill:rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")'> " );
  }
  else if (compiledSolid.shape.shapeType == "rectangle") {
    //console.log("it's a rectangle");
    svgSolids.push( "<rect id='solid_" +solid.name+ "' width='" +30+ "' height='" +30+ "' style='fill:rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")'> " );
  }
  else if (compiledSolid.shape.shapeType == "line") {
    //console.log("it's a line")
    svgSolids.push("<line id='solid_" +solid.name+ "' x1='' x2='' y1='' y2='' stroke='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")' stroke-width='2'>");
  }
  else if (compiledSolid.shape.shapeType == "vector") {
    svgSolids.push("<polyline id='solid_" +solid.name+ "' points='' stroke='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")' stroke-width='2' fill='none'>");
  }
  else if (compiledSolid.shape.shapeType == "polygon") {
    //console.log("it's a polygon:", solid.name);
    svgSolids.push("<polyline id='solid_" +solid.name+ "' points='' stroke='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")' stroke-width='2' fill='rgb("+solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+")'>");
  }
  else if (compiledSolid.shape.shapeType == "Bitmap") {
    //svgSolids.push("<image  x='0' y='0' width='' height='' src='"+compiledSolid.fileUri+"'><title>"+solid.name+"</title></image>");

    // 2018Mar01 Brockman - Refactoring the bitmap code here.
    // https://stackoverflow.com/questions/10261731/can-not-add-image-inside-svg-via-jquery-image-tag-becomes-img

    var id = "solid_"+solid.name;

    var img = document.createElementNS('http://www.w3.org/2000/svg','image');
    img.setAttributeNS(null,'id',id)
    img.setAttributeNS('http://www.w3.org/1999/xlink','href',compiledSolid.fileUri);
    img.setAttributeNS(null, 'visibility', 'visible');

    svgSolids.push(img);
  }



  else {

    console.log("iwp5:821> ERROR: Unrecognized Solid Shape Type: ", compiledSolid.shape.shapeType)
    return;
  };

  /** 2019Apr30 Initial Trail is very empty, but filled in with each animation step in iwp6-animator.js */
  if (solid.shape.drawTrails == true ) {
    svgSolids.push("<polyline id='solid_" +solid.name+ "_trail' points='0,0 0,0' stroke='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")' stroke-width='1' fill='none'></polyline>");
  }
}



function addObject(object) {

  //2018Oct12 - Detect the WaveBox
  if ( object.objectType == "edu.ncssm.iwp.objects.grapher.DObject_Grapher" ) {
    alert("This Animation Contains a GraphBox, Not yet implented in IWP5");
    return;
  }
  
  var compiledObject = {
    objectType: 'object',
    name: object.name,
    shape: {
      shapeType: object.class,
      height: 1,
      width: 1
    },
    text: object.text,
    units: object.units,
    showValue: ( object.showValue === true || false ),
    value: {
      calculator: compileCalculator(object.value.calculator)
    },
    fontSize: object.fontSize,
    xpath: {
      calculator : compileCalculator(object.xpath.calculator)
    },
    ypath: {
      calculator : compileCalculator(object.ypath.calculator)
    }
  }
  objects.push( compiledObject );


  if (object.class == "edu.ncssm.iwp.objects.floatingtext.DObject_FloatingText") {

    // console.log("iwp5:933> FloatingText setting x,y: " + object.xpath.calculator.value + ", " + object.ypath.calculator.value + " for object: " , object )
    /// Initilization fix - the calclulators hven't been run yet, so we just place the text on page at 0,0 and it's moveed with first redraw.
    var x = 0; // xCanvas(object.xpath.calculator.value)
    var y = 0; // yCanvas(object.ypath.calculator.value)

    svgObjects.push( "<text id='text_" +object.name+ "' x='" + x + "' y='"+ y +"' font-size='"+(parseFloat(object.fontSize)+15)+"'style='fill:rgb(" +object.color.red+ "," +object.color.green+ "," +object.color.blue+ ")'>"+object.text+"</text>" );

    texts.push( compiledObject );
  }

};
//-----------------------------------------------------------------------
// Calculation Section

function compileCalculator(iwpCalculator) {

    // console.log("iwp6-calc:1161> Attempting to compile calculator: " + JSON.stringify(iwpCalculator));

    var incomingType = iwpCalculator.calcType

	if ( incomingType == "parametric" ) {

		/*
        <calculator type="parametric">
            <value>ar.value*cos((kr.value/mr.value)^.5*t+toRadians(pr.value)*1)</value>
        </calculator>
		*/

        var e = migrateLegacyEquation(iwpCalculator.value);

		var c = {
			calcType: "mathjs",
			compiled: math.compile( e ),
			equation: e
		}

		// console.log("iwp5:760> Compiled: ", iwpCalculator.value, " compiledTo: ", c)
        return c;


	} else if ( incomingType == "euler" || incomingType == "MCalculator_Euler") {
        /*
        <calculator type="euler">
          <displacement>initxdisp</displacement>
          <velocity>initxvel</velocity>
          <acceleration>-5*t</acceleration>
        </calculator>
        */

        var a = migrateLegacyEquation(iwpCalculator.acceleration);
        var v = migrateLegacyEquation(iwpCalculator.velocity);
        var d = migrateLegacyEquation(iwpCalculator.displacement);

        var c =  {
          calcType: "euler-mathjs",
          initialDisplacementCompiled: math.compile( d ),
          initialVelocityCompiled: math.compile( v ),
          accelerationCompiled: math.compile( a ),
          equation: {
            acceleration : a,
            velocity : v,
            displacement : d
          }
        }
        return c;


  }
  else {
		console.log("DEBUG ERROR: Only parametric and Euler supported in the August 2016 version, unable to handle: ", incomingType);
		return {calcType:"unsupported", "incomingType": incomingType, "iwpCalculator": iwpCalculator };
	}
}

function migrateLegacyEquation(toMigrate) {
  return toMigrate.replace(/[.]value/g,"")
}

/**
 * 2016-Sep-23 - Sometimes single variable equations will evalate as object.value, not a numeric
 * Remind Taylor about this if you have questions.
 */

function evaluateCompiledMath( compiled, vars ) {

  var newValue = compiled.evaluate(vars)
  if ( typeof newValue === "number" ) {
    return newValue;
  } else if ( typeof newValue["value"] === "number" ) {
     return newValue.value;
  } else {
     throw "iwp6-calc:1134> Unable to process compiled, not numeric and doesn't have value property: " + newValue
  }
}

// Config flag for develoeprs to enable debugging of euler acceleration calculations - have fun!
var CONFIG_throw_acceleration_calculation_exceptions = true;


/** 2018Mar22 Bugfix, use the argument 'calculateStep' instead of reaching out to global */
// ResultVariable is solidname.x or solidName.y

function evaluateCalculator( resultVariable, calculator, calculateStep, vars, verbose, objectName ) {

    if ( calculator == null ) {
        console.log("iwp6-calc:970> Null Calculator for: ", resultVariable)
        return { value: 0 }
    }
    else if ( calculator.calcType == "mathjs" )   {

        try {
            return evaluateParametricCalculator(resultVariable, calculator, calculateStep, vars, verbose, objectName);
        } catch ( err ) {
            console.log("iwp6-calc:991> Exception in evaluateParametricCalculator for " + resultVariable + ": " + err );
            console.log("iwp6-calc:992> Object Detail: Vars: " , vars )
            console.log("iwp6-calc:993> Object Detail: Calculator: " , calculator )
            return { value: 0 }
        }
    } else if ( calculator.calcType == "euler-mathjs" ) {

        try {
            return evaluateEulerCalculator(resultVariable, calculator, calculateStep, vars, verbose, objectName);
        } catch ( err ) {
            console.log("iwp6-calc:999> Exception in evaluateEulerCalculator for " + resultVariable + ": " + err );
            return { value: 0 }
        }

    }
    else {
        throw "iwp6-calc:1264> Unsupported calculator type : " + JSON.stringify(calculator)
    }
}



/**
 * 2019Jan08 For Readability, broke apart Parameteric -vs- Euler implementations
 */

function evaluateParametricCalculator( resultVariable, calculator, calculateStep, vars, verbose, objectName ) {

    try {


        var result = calculator.compiled.evaluate(vars);

        if ( !isFinite(result) ) {
            throw "iwp6-calc:1260> Compiled vars are not finite"
        }

        if ( calculator.latestValue == undefined ) {
            calculator.previousValue = result;
        } else {
            calculator.previousValue = calculator.latestValue;
        }
        calculator.latestValue = result;

        // Instaneous Velocity Calculation.
        calculator.velocity = ( calculator.latestValue - calculator.previousValue ) / vars["delta_t"]
        //console.log("evaluateCalculator:509> " + resultVariable + "> Calculated Velicoty: "  + calculator.velocity + " (previous: " + calculator.previousValue + " latest: "+ calculator.latestValue + ")")

        // Instanteous Acceleration Calcualtor
        if ( calculator.latestVelocity == undefined ) {
            calculator.previousVelocity = calculator.velocity
        } else {
            calculator.previousVelocity = calculator.latestVelocity;
        }

        calculator.latestVelocity = calculator.velocity;
        calculator.acceleration = ( calculator.latestVelocity - calculator.previousVelocity ) / vars["delta_t"]


        return { value: result,
            displacement: result,
            velocity: calculator.velocity,
            acceleration: calculator.acceleration };
    }
    catch ( err ) {
        if ( verbose ) {
            console.log("iwp6-calc:1058> evaluateParametricCalculator ERROR: " + resultVariable + "> Unable to evaluate calculator: ", err );
            console.log("iwp6-calc:1059> evaluateParametricCalculator ERROR: " + resultVariable + "> Equation: ", calculator.equation );
            console.log("iwp6-calc:1060> evaluateParametricCalculator ERROR: " + resultVariable + "> Vars: ", vars);
            console.log("iwp6-calc:1060> evaluateParametricCalculator ERROR: " + resultVariable + "> Vars.STRINGIFY: ", JSON.stringify(vars));
            console.log("iwp6-calc:1062> evaluateParametricCalculator ERROR: " + resultVariable + "> Vars: t = ", vars.t, "  t1 = " , vars['t1'] );

        }
        throw err;
    }
};




function evaluateEulerCalculator( resultVariable, calculator, calculateStep, vars, verbose, objectName ) {

    try {

    // 2019-Jan-08 Euler Self-referential, fixes for damped-1.iwp to prevent exceptions with new iwp6 javascript.



    // 2016-Sep-23 For Euler V5, store the cache of current displacement and velocity IN calculator.
    // An enhancement would be to expose these values out as complex return ttypes of this function,
    // so that they could he historically archived in the variable step array.
    //  IIRC, IWPv4, these were available as xdisp, xvel, xaccell on solids, for example.
    // Today, in IWP5, our return structure out of thi sufnction is a single double value.


    var dt = vars["delta_t"]
    var acceleration = null;

    // 2019Jan08 Taylor Addressing the Euler self-references, as exemplified in iwp-packaged / Oscillations / damped-1.iwp
    // console.log("iwp6-calc:1317> acceleration resultVariable: ", resultVariable );
    // console.log("iwp6-calc:1317> acceleration calculator: ", calculator );
    // console.log("iwp6-calc:1317> acceleration BEFORE vars: ", vars );
    // Enable the acceleration equations to self-reference our own ypos/yvel

    if ( resultVariable.endsWith(".y") ) {

        if ( typeof vars[ objectName ] !== "object" ) { vars[objectName] = {} }
        vars[ objectName ][ "ypos" ] = calculator.currentDisplacement
        vars[ objectName ][ "yvel" ] = calculator.currentVelocity

    } else if ( resultVariable.endsWith(".x") ) {

        if ( typeof vars[ objectName ] !== "object" ) { vars[objectName] = {} }
        vars[ objectName ][ "xpos" ] = calculator.currentDisplacement
        vars[ objectName ][ "xvel" ] = calculator.currentVelocity

    } else {
        console.log("iwp6-calc:1317> Unknown resultVariable endsWith: " , resultVariable );
    }
    // console.log("iwp6-calc:1346> acceleration AFTER vars: ", vars );




    try {
        // then calculate the acceleration
        // 2018Mar22 Fix to only apply the acceleration time component to the change in velocity.

        // console.log("iwp5:1088> Calculating acceleration via calculator: ", calculator, " at calcStep: " + calculateStep + " for vars: ", vars )
        acceleration = calculator.accelerationCompiled.evaluate(vars);

        if ( !isFinite(acceleration) ) {
            throw "Calculator.accelerationCompiled result is not finite, is: " + acceleration
        }
        if ( isNaN(acceleration) ) {
            throw "Calculator.accelerationCompiled result is NaN"
        }

    } catch ( err ) {
        console.log("evaluateCalculator:1095> ERROR " + resultVariable + "> Unable to evaluate acceleration, setting to 0.  Calculator: ", err, calculator.equation, "Vars: ", JSON.stringify(vars) );
        if ( CONFIG_throw_acceleration_calculation_exceptions ) {
            throw err;
        }
    }



    if ( calculateStep == 0 ) {
		// For good measure, recalculate initials just in case other dependencies have changed.
		calculator.initialDisplacement = evaluateCompiledMath(calculator.initialDisplacementCompiled, vars)
	    calculator.initialVelocity = evaluateCompiledMath(calculator.initialVelocityCompiled, vars)

        calculator.currentVelocity = calculator.initialVelocity;
        calculator.currentDisplacement = calculator.initialDisplacement; // no adjustment

        //console.log("iwp6-calc:1143> Recalculating Euler Calc Step 0, calculator: " , calculator);
		//var breaker=1;

    } else if ( changeStep > 0 ) {

        if ( acceleration != null ) {
            // Positive direction calcuation
            if ( calculateStep == 1 ) {
                // Special first frame consideration
                calculator.currentVelocity += acceleration * dt * 0.5;
            } else {
              calculator.currentVelocity += acceleration * dt;
            }
            calculator.currentDisplacement += calculator.currentVelocity * dt;
        }

    } else if ( changeStep < 0 ) {
        // Negative direction calculation
        if ( acceleration != null ) {
            calculator.currentVelocity -= acceleration * dt;
            calculator.currentDisplacement -= calculator.currentVelocity * dt;
        }
    } else {
        // No step direction
    }


    // console.log("iwp5:1152> evaluateCalculator calculateStep: "+ calculateStep + "  changeStep: " + changeStep + " calculator.currentVelocity: " + calculator.currentVelocity + " calculator.currentDisplacement: " + calculator.currentDisplacement )
    //Return only value if just an output?

    return { step: calculateStep,
        value: calculator.currentDisplacement,
        displacement: calculator.currentDisplacement,
        velocity: calculator.currentVelocity,
        acceleration: acceleration }

    // return displacement.value;
    } catch ( err ) {
        if ( verbose ) {
            console.log("evaluateCalculator:375> Unable to evaluate calculator: ", err, calculator.equation, dt);
        }
        throw err;
    }
};





/**
 * Important entry point!
 *
 * 2018Dec14 Converted to pure Js, the $.xxType interface is different than typeof, requires Array.isArray
 *
 * After the animation parse, outer layer must call :  masterResetSteps()  or   calculateVarsAtStep(currentStep = 0);
 * The new iwp6-read does this.
 */

function parseAnimationToMemory( rawAnimation ) {


    console.log("iwp6-calc:1186> Parsing rawAnimation: ", rawAnimation);

    var animation = { loop: [] };

    // 2019Mar13

     rawAnimation.objects.forEach( function( object, index ) {
        // console.log("iwp6-calc:1451> parseAnimationToMemory> Iterator: " + JSON.stringify(object) );

        if ( object.objectType == "time" ) {
            animation.time = object;
        } else if ( object.objectType == "graphWindow" ) {
            animation.graphWindow = object;
        } else if ( object.objectType == "window" ) {
            animation.window = object;
        } else if ( object.objectType == "description" ) {
            animation.description = object;

        } else if ( object.objectType == "input" || object.objectType == "output" || object.objectType == "solid"  || object.objectType == "object" ) {
            animation.loop.push(object);

        } else {
            throw "Animation parseAnimationToMemory unrecognized Object Type: " + object.objectType;
        }

     });


  // D-fence

  if ( typeof animation !== "object" ) {
    throw "Animation Parameter was not an object, was: " + typeof animation
  } else if ( typeof animation.loop !== "object" ) {
    throw "Animation loop attribute was not an array, was: " + typeof animation.loop
  } else if ( typeof animation.time !== "object" ) {
    throw "Animation objects.time attribute was not an object, was: " + typeof animation.time
  } else if ( typeof animation.description !== "object" ) {
    throw "Animation objects.description attribute was not an object, was: " + typeof animation.description
  } else if ( typeof animation.graphWindow !== "object" ) {
    throw "Animation graphWindow attribute was not an object, was: " + typeof animation.graphWindow
  } else if ( typeof animation.window !== "object" ) {
    throw "Animation window attribute was not an object, was: " + typeof animation.window
  }
  
  // Time
  setTime(animation.time);

  // Description
  setDescription(animation.description);

  // Window
  setWindow(animation.window);

  // GraphWindow

  // console.log("iwp6-calc:1238> Setting GraphWindow: " , animation.graphWindow)
  // console.log("Initializing Graph Vars");
  initializeGraphVars(animation.graphWindow);
  setGraphWindow(animation.graphWindow);


  animation.author = rawAnimation.author;
  if ( typeof setAuthorName === "function" ) {
    setAuthorName(animation.author.username);
  }

  // console.log("iwp6-calc:1350> Typeof input: " , typeof rawAnimation.objects.input)

  // 2019Sep06 Reordering of the Animatin Objects based on IWP3 Logic Port.
  //console.log("iwp6-calc:1288> Executing animationObject Reordering on CompiledObjects");
  animation.loop = reorderAnimationObjectsBySymbolicDependency(animation.loop);

  animation.loop.forEach( function( object, index ) {

    if ( object.objectType == 'input' ) {
        addInput(object);
    } else if ( object.objectType == 'output' ) {
        addOutput(object);
    } else if ( object.objectType == 'solid' ) {
        addSolid(object);
    } else if ( object.objectType == 'object' ) {
        addObject(rawAnimation.objects.object);
    } else {
      throw "Animation parseAnimationToMemory unrecognized Object Type: " + object.objectType;
    }
  } );

  // Helper Functions that run filters.

  // 2019Apr09 store in global singleton

  parsedAnimation = animation;
  decorateAnimationFunctions();

  return parsedAnimation;
}


/**
 * Decrorate some helper functions onto the animation 'object'
 */
function decorateAnimationFunctions() {

  if ( ! parsedAnimation ) { throw "Refusing to proceed with decoration since parsedAnimation is null" }

  parsedAnimation.solids = function() {
      var out = [];
      parsedAnimation.loop.forEach( function( object, index ) {
          if ( object.objectType == 'solid' ) { out.push(object); }
      });
      return out;
  };

  parsedAnimation.objects = function() {
      var out = [];
      parsedAnimation.loop.forEach( function( object, index ) {
          if ( object.objectType == 'objects' ) { out.push(object); }
      });
      return out;
  };


  parsedAnimation.inputs = function() {
      var out = [];
      parsedAnimation.loop.forEach( function( object, index ) {
          if ( object.objectType == 'input' ) { out.push(object); }
      });
      return out;
  };

  parsedAnimation.outputs = function() {
      var out = [];
      parsedAnimation.loop.forEach( function( object, index ) {
          if ( object.objectType == 'output' ) { out.push(object); }
      });
      return out;
  };


  return parsedAnimation;

}

//--------------------------------------------------------------------------------
// SVG ViewBox Scaling

var canvasBox = { minX: 0, minY: 0, maxX: 1000, maxY: 1000 };
function yCanvas(y) {
  var yDomain = animationWindow.ymax - animationWindow.ymin;
  var sum = animationWindow.ymax / yDomain;
  var yProportion = - y / yDomain;
  var yCorrected = yProportion + sum;
  var cDomain = canvasBox.maxY - canvasBox.minY;
  var cProportion = yCorrected * cDomain;
  return cProportion;
};

function xCanvas(x) {
  var xDomain = animationWindow.xmax - animationWindow.xmin;
  var sum = - animationWindow.xmin / xDomain;
  var xProportion = x / xDomain;
  var xCorrected = xProportion + sum;
  var cDomain = canvasBox.maxX - canvasBox.minX;
  var cProportion = xCorrected * cDomain;
  return cProportion;
/* the proportional entry point in from window.xmin -> window.xmax needs to be interpolated into the
// propotional exit point between viewbox.minX -> viewbox.maxX
*/
};
function xCanvasGridlines(x) {
  var xDomain = animationWindow.xmax - animationWindow.xmin;
  var xProportion = x / xDomain;
  var xCorrected = xProportion + 0.5;
  var cDomain = canvasBox.maxX - canvasBox.minX;
  var cProportion = xCorrected * cDomain;
  return cProportion;
};
function yCanvasGridlines(y) {
  var yDomain = animationWindow.ymax - animationWindow.ymin;
  var yProportion = y / yDomain;
  var yCorrected = yProportion + 0.5;
  var cDomain = canvasBox.maxY - canvasBox.minY;
  var cProportion = yCorrected * cDomain;
  return cProportion;
};
/* the proportional entry point in from window.xmin -> window.xmax needs to be interpolated into the
// propotional exit point between viewbox.minX -> viewbox.maxX
*/

function xWidth(size) {
  var xDomain = animationWindow.xmax - animationWindow.xmin;
  var cDomain = canvasBox.maxX - canvasBox.minX;
  var proportion = cDomain/xDomain;
  return size*proportion;
};

function yHeight(size) {
  var yDomain = animationWindow.ymax - animationWindow.ymin;
  var cDomain = canvasBox.maxY - canvasBox.minY;
  var proportion = cDomain/yDomain;
  return size*proportion;
};



//graphBox

var graphBox = { gminX: -100, gminY: -100, gmaxX: 200, gmaxY: 200 };
function ygraph(y) {
  var yDomain = graphWindow.ymax - graphWindow.ymin;
  var sum = graphWindow.ymax / yDomain;
  var yProportion = - y / yDomain;
  var yCorrected = yProportion + sum;
  var gDomain = graphBox.gmaxY - graphBox.gminY;
  var gProportion = yCorrected * gDomain;
  return gProportion;
};

function xgraph(x) {
  var xDomain = graphWindow.xmax - graphWindow.xmin;
  var sum = - graphWindow.xmin / xDomain;
  var xProportion = x / xDomain;
  var xCorrected = xProportion + sum;
  var gDomain = graphBox.gmaxX - graphBox.gminX;
  var gProportion = xCorrected * gDomain;
  return gProportion;
};
function xgraphGridlines(x) {
  var xDomain = graphWindow.xmax - graphWindow.xmin;
  var xProportion = x / xDomain;
  var xCorrected = xProportion + 0.5;
  var gDomain = graphBox.gmaxX - graphBox.gminX;
  var gProportion = xCorrected * gDomain;
  return gProportion;
};
function ygraphGridlines(y) {
  var yDomain = graphWindow.ymax - graphWindow.ymin;
  var yProportion = y / yDomain;
  var yCorrected = yProportion + 0.5;
  var gDomain = graphBox.gmaxY - graphBox.gminY;
  var gProportion = yCorrected * gDomain;
  return gProportion;
};
function gxWidth(size) {
  var xDomain = graphWindow.xmax - graphWindow.xmin;
  var gDomain = graphBox.gmaxX - graphBox.gminX;
  var proportion = gDomain/xDomain;
  return size*proportion;
};

function gyHeight(size) {
  var yDomain = graphWindow.ymax - graphWindow.ymin;
  var gDomain = graphBox.gmaxY - graphBox.gminY;
  var proportion = gDomain/yDomain;
  return size*proportion;
};


true;
