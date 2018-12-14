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


//-----------------------------------------------------------------------
// Memory Intialization + Globals Section

var time = {};
var description = "";
var iwindow = {};
var graphWindow = {};

var inputs = [];
var outputs = [];
var solids = [];
var objects = [];
var texts = [];

var htmlInputs = [];
var htmlOutputs = [];
var svgSolids = [];
var svgObjects = [];


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


  // We also need to clear out previous parameteric displacements used for instant velecioty calculations.
  solids.forEach(function ( solid, index) {
    resetSolidCalculators(solid);
  });


  var vars0 = calculateVarsAtStep(0);

  // 2018Feb01 Graphing Reset hookin
  if ( typeof graphResetZero === "function" ) {
    graphResetZero(0, vars = vars0, solids = solids );
  }

  archiveVarsAtStep( currentStep, vars0 ); // Boot up the environment

  return vars0;
}



//-----------------------------------------------------------------------
// Pseudocode for Mathematical Animation

// Calculate Initial Variable Set from inputs.

// Calculate at initial step to render displays

// Calculate for each step as time ticks.

// Provide a historical variable set at each tick (for trails, graphing).

// 2018Mar01 Found that monkeypatching mathjs was non-reliable so now including in the vars scope.
// Converts from degrees to radians. this makes the 'toRadians(..)' function available in the Calculator space.
// Likely the are are other physical constants that we'll need to load as well.
varsConstants = {
 G : -9.8,
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
 * Major function - called every time the animation time changes
 */
function handleStep() {

	console.log("handleStep:113> current: " , currentStep, "  change: ", changeStep );
	// apply the time change.
	var newStep = currentStep + changeStep;

	// handle time horizons
	if ( newStep < 0 ) {
		changeStep = 0;
		newStep = 0;
	}
	// TODO - end of time.
  /*time = {
    start : parseFloat(inTime.start),
    stop : parseFloat(inTime.stop),
    change : parseFloat(inTime.change),
    fps : parseFloat(inTime.fps)
   };*/
  //console.log("stop step", queryTimeStopInputDouble() / queryTimeStepInputDouble())
  //console.log('current step', newStep)
  if (newStep > ( Math.round( queryTimeStopInputDouble() / queryTimeStepInputDouble()))) {
    handleStopClick()
    return;
  }
	//console.log("handleStep:61> newStep: " + newStep)

	if ( newStep != currentStep ) {

    // Back button poerformance - Let's look at the historical array of variables, if we have it, reload
    // to avoid doing a recalcaultion

    var vars = varsAtStep[newStep];
    if ( vars != undefined ) {
      //console.log("[iwp5.js:118] Step " + newStep + " already exist, reloading for history.")
      repaintStep(newStep);
    } else {
  		// UI rendering is handled by the calculate as a side effect
	 	  vars = calculateVarsAtStep(newStep);
  		archiveVarsAtStep( newStep, vars );
    }

    // iwp5.1 - Adding Hook into the graph capabilties
    if ( changeStep > 0 ) {
      graphStepForward(newStep, vars);
    } else if ( changeStep < 0 ) {
      graphStepBackward(newStep, vars);
    }

	}

	currentStep = newStep;

}


function archiveVarsAtStep( step, vars ) {
  // console.log("Archving vars at step: " + step );
  varsAtStep[step] = {};
  deepExtend(varsAtStep[step], vars)
}


function calculateOutputAtStep(output, step, vars, verbose) {
    var newValue = evaluateCalculator( output.name+".out", output.calculator, step, vars, verbose, output.name ).value;
    vars[output.name] = newValue;
    updateUserFormOutputDouble(output, newValue);
    return newValue
}


function calculateSolidAtStep(solid, step, vars, verbose) {

    var xComplex = evaluateCalculator( solid.name+".x", solid.xpath.calculator, step, vars, verbose, solid.name )
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


 // Handle Complex return types from Euler Calculator
     var calcY = {
      y: y,
      ydisp: y,
      ypos: y,
      yvel: 0,  // fold in w/ terinary
      yaccel: 0
    }


    if ( yComplex.velocity != undefined ) {
      calcY.yvel = yComplex.velocity
    }
    if ( yComplex.acceleration != undefined ) {
      calcY.yaccel = yComplex.acceleration
    }

  if ( xComplex.acceleration == null ) {
      //console.log("[iwp5.js:201> Recaclualting X because acceleration was null!")
    vars[solid.name] = deepExtend(calcX,calcY);
     xComplex = evaluateCalculator( solid.name+".x", solid.xpath.calculator, step, vars, verbose, solid.name )
     x = xComplex.value
  }
  if ( yComplex.acceleration == null ) {
      //console.log("[iwp5.js:201> Recaclualting y because acceleration was null!")
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


    if ( solid.shape.type == "polygon" ) {

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

    if ( solid.shape.angle ) {
      calc.angle = evaluateCalculator( solid.name+".a", solid.shape.angle.calculator, step, vars, verbose, solid.name ).value
      // console.log("iwp5:277> Evaluated calculator for Solid.shape.angle: ", solid.shape.angle, " to :", calc.angle  )
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
    vars[solid.name] = calc

    // WARNING: updating svg display deep inside the calc route.
    // console.log("iwp5:314> Warning: Invoking updateSolidSvgPathAndShape deep in recalc: ", solid.name);

    updateSolidSvgPathAndShape(solid, calc)


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
  updateTextSvgPathAndShape(text, {x: x, y: y, value: v})

}



// RENDERING steps:
//  updateTimeDisplay(vars.t);
//  updateUserFormOutputDouble(output, newValue);
//  updateSolidSvgPathAndShape(solid, calc)

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

   outputs.forEach( function( output, index ) {
      updateUserFormOutputDouble(output, vars[output.name]);
   });

   // le.log("iwp5:347> Invoking updateSolidSvgPathAndShape from repaintStep, solids: ", solids );

   solids.forEach( function( solid, index ) {
      updateSolidSvgPathAndShape(solid, vars[solid.name])
   });

   // console.log("iwp5:347> Invoking updateSolidSvgPathAndShape from repaintStep, objects: ", objects );

   objects.forEach( function( object, index ) {
      updateSolidSvgPathAndShape(object, vars[object.name])
   });


  }
}





var CONFIG_throw_solid_calculation_exceptions = false;

function calculateVarsAtStep(step) {

  // vars should be a map of string to double, including the mathematical / physical constants.
  var vars = { step: step}
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

  inputs.forEach( function( input, index ) {
		// next load in variables for all of the inputs.
    //vars[input.name] = { value: queryUserFormInputDouble(input) };
    vars[input.name] = queryUserFormInputDouble(input);

    //console.log("iwp5:410> calculateVarsAtStep("+step+")  Assigned Input: " , input.name, "  A value of: " ,vars[input.name] )

    //vars[input.name]["value"] = vars[input.name];
  });


  //-----------------------------------
  // Make the first pass thru outputs

  outputs.forEach( function( output, index ) {
    try {
      var newValue = calculateOutputAtStep(output, step, vars, false );
      //console.log("newValue",newValue)
      if ( isNaN(newValue) ) {
        throw "not a number"
      };
      if ( !isFinite(newValue) ) {
        throw "not finite"
      }
      vars[output.name] = newValue;
      // console.log("iwp5:409> First Ouputs Pass, added new variable: "+ output.name + " = " + vars[output.name])
    } catch ( err ) {

      console.log("iwp5:409> First Outputs Pass, ERROR Calculating: "+ output.name + " > " + err );
      //console.log("Failed Output:", output)
      //failedOutputs.push(output);
      //console.log("FailedOutputs",failedOutputs)
    }
  });




  //-----------------------------------------------------------
  // EULER INITIALIZATION
  // 2018Mar22 - Euler's Self-referential Fix, we need to pull velocities and displacements for
  // every Solid w/ Euler's and pre-write them into the vars space.
  solids.forEach(function(solid, index) {

    // Initialize if necessary
    if ( vars[solid.name] == null ) { vars[solid.name] = {} }

    if ( solid.xpath.calculator.type == "euler-mathjs" ) {

      var calculator = solid.xpath.calculator
      // Initialization -- If currentDisplacement or currentVelocity is empty!
      if (step == 0 || calculator.currentDisplacement == null) {
        // console.log("iwp5:409> X Euler Pre-calc for solid: " + solid.name + " solid.xpath.calc: " , solid.xpath.calculator)
        calculator.initialDisplacement = evaluateCompiledMath(calculator.initialDisplacementCompiled, vars)
        calculator.currentDisplacement = calculator.initialDisplacement
      }
      if (step == 0 || calculator.currentVelocity == null) {
        calculator.initialVelocity = evaluateCompiledMath(calculator.initialVelocityCompiled, vars)
        calculator.currentVelocity = calculator.initialVelocity;
      }
      vars[solid.name].x = solid.xpath.calculator.currentDisplacement
      vars[solid.name].xpos = solid.xpath.calculator.currentDisplacement
      vars[solid.name].xdisp = solid.xpath.calculator.currentDisplacement
      vars[solid.name].xvel = solid.xpath.calculator.currentVelocity
    }

    if ( solid.ypath.calculator.type == "euler-mathjs" ) {
      var calculator = solid.ypath.calculator
      // Initialization -- If currentDisplacement or currentVelocity is empty!
      if (step == 0 || calculator.currentDisplacement == null) {
        //console.log("iwp5:426> Y Euler Pre-calc for solid: " + solid.name + " solid.ypath.calc: " , solid.ypath.calculator)
        calculator.initialDisplacement = evaluateCompiledMath(calculator.initialDisplacementCompiled, vars)
        calculator.currentDisplacement = calculator.initialDisplacement
      }
      if (step == 0 || calculator.currentVelocity == null) {
        calculator.initialVelocity = evaluateCompiledMath(calculator.initialVelocityCompiled, vars)
        calculator.currentVelocity = calculator.initialVelocity;
      }
      vars[solid.name].y = solid.ypath.calculator.currentDisplacement
      vars[solid.name].ypos = solid.ypath.calculator.currentDisplacement
      vars[solid.name].ydisp = solid.ypath.calculator.currentDisplacement
      vars[solid.name].yvel = solid.ypath.calculator.currentVelocity
    }

  });




  //2018Mar14 - ordering by calculation order, if exists
  var unorderedSolids = [];
  var unorderedOutputs = [];
  var unorderedTexts = [];

  var orderedObjects = [];
  outputs.forEach(function(output, index) {
    if (output.calculationOrder != null) {
      orderedObjects[output.calculationOrder] = output;
    } else {
      unorderedOutputs.push(output);
    }
  });
  solids.forEach(function(solid, index) {
    if (solid.calculationOrder != null) {
      orderedObjects[solid.calculationOrder] = solid;
    } else {
      unorderedSolids.push(solid);
    }
  });
  texts.forEach(function(text, index) {


    if (text.calculationOrder != null) {
      orderedObjects[text.calculationOrder] = text;
    } else {
      // console.log("iwp5:496> Adding Unordered Text: " + text.name, text )
      unorderedTexts.push(text);
    }
  });
  //console.log("iwp5:434> orderedObjects: ", orderedObjects);
  //console.log("iwp5:434> unorderedSolids: ", unorderedSolids);
  //console.log("iwp5:434> unorderedOutputs: ", unorderedOutputs);


  var failedOutputs = [];
  var failedSolids = [];
  var failedTexts = [];


  /**
   * 2018Mar15 Perform the animation calculationOrder First
   */

  var calculationsPerformed = 0;

  orderedObjects.forEach( function(object, index) {

    if(object != null ) { // orderedObjects can be sparseArray

    // console.log("iwp5:515> [Calculations Peformed " + calculationsPerformed + "] Calculating Object: " + object.name )

    if(object.objectType=="solid") {
      try {

        // console.log("iwp5:494> [Calculations Peformed " + calculationsPerformed + "] Calculating Solid: " + object.name )
        calculationsPerformed++
        calculateSolidAtStep(object, step, vars, true );

      } catch ( err ) {
        console.log("iwp5:457> ERROR calculationOrder solid: " + object.name + " err: ", err )
        failedOutputs.push(object);
      }
    } else if(object.objectType=="output") {
      try {

        var newValue = calculateOutputAtStep(object, step, vars, false );
        if ( isNaN(newValue) ) { throw "not a number" };
        if ( !isFinite(newValue) ) { throw "not finite" }
        vars[object.name] = newValue;

        // console.log("iwp5:528> [Calculations Peformed " + calculationsPerformed + "] Calculated Output: " + object.name + " New Value: " + newValue )
        calculationsPerformed++


      } catch ( err ) {
        console.log("iwp5:466> failed calculationOrder err: " , err, " Output:", object)
        failedOutputs.push(object);
      }
    } else if ( object.objectType=="object" ) {

      console.log("iwp5:548> TODO Animate the text: object.name: " + object.name, object )


    } else {
      throw "Unknown objectType: " + object.objectType;
    }
    }
  });


  /*
  * Legacy path to support 4_1_2 animations with no CalculationOrder in iwp file.
  for each output perform the calculator
  */

  unorderedSolids.forEach( function( solid, index ) {
    /*
    for x, y, h, w , perform the calculator
    */
    try {
     calculateSolidAtStep(solid, step, vars, true );
        //  -> update the DOM with theose new results
    } catch ( err ) {
        //console.log(":231 caught a faailed solid exception: ", err);
      if ( CONFIG_throw_solid_calculation_exceptions ) {
      throw err;
    }

    failedSolids.push(solid);
  }});




  unorderedOutputs.forEach( function( output, index ) {
    try {
      var newValue = calculateOutputAtStep(output, step, vars, false );
      //console.log("newValue",newValue)
      if ( isNaN(newValue) ) {
        throw "not a number"
      };
      if ( !isFinite(newValue) ) {
        throw "not finite"
      }
      vars[output.name] = newValue;
      //console.log("added new variable:",output.name,vars[output.name])
    } catch ( err ) {
      //console.log("Failed Output:", output)
      failedOutputs.push(output);
      //console.log("FailedOutputs",failedOutputs)
    }
  });



  // console.log(":238 failedOutputs: ", failedOutputs);
  // console.log(":239 failedSolids: ", failedSolids);

  var fatalOutputs = failedOutputs;
  var fatalSolids = failedSolids;
  var fatalReplayAttemptsRemaining = 3;

  while ( fatalReplayAttemptsRemaining > 0 ) {
    fatalReplayAttemptsRemaining -= 1;
    var replayOutputs = fatalOutputs;
    var replaySolids = fatalSolids;

    fatalOutputs = [];
    fatalSolids = [];

    // REPLAY FAILURES within a resonable maximum number of attempts.
     replayOutputs.forEach( function( output, index ) {
      //console.log("Trying again",output)
      try {
        var newValue = calculateOutputAtStep(output, step, vars, false );
        vars[output.name] = newValue
      } catch ( err ) {
        fatalOutputs.push(output);
      }
    });
    replaySolids.forEach(function( solid, index ) {
      try {
       calculateSolidAtStep(solid, step, vars, true );
      } catch ( err ) {
        console.log(err)
       fatalSolids.push(solid);
      }
    if (fatalReplayAttemptsRemaining == 1) {
      CONFIG_throw_acceleration_calculation_exceptions = false;
    }
   });

  }

  if ( fatalOutputs.length > 0 ) {
    console.log("iwp5:673> ERROR Giving Up on Recursive Circular Calc - FATALOutputs: ", fatalOutputs);
    fatalOutputs.forEach( function( output, index ) {
      output.calculationError = step;
    });
  }
  if ( fatalSolids.length > 0 ) {
    console.log("iwp5:679> ERROR Giving Up on Recursive Circular Calc - FATALSolids: ", fatalSolids);
    fatalSolids.forEach( function( solid, index ) {
      solid.calculationError = step;
    });
  }


  // FINALLY, Text calculation at very end
  unorderedTexts.forEach( function( text, index ) {

    try {
     calculateTextAtStep(text, step, vars, true );
        //  -> update the DOM with theose new results
    } catch ( err ) {
        //console.log(":231 caught a faailed solid exception: ", err);
      if ( CONFIG_throw_solid_calculation_exceptions ) {
      throw err;
    }

    failedTexts.push(text);
  }});

  if ( failedTexts.length > 0 ) {
    console.log("iwp5:685> ERROR Giving Up on Recursive Circular Calc - FAILEDTexts: ", failedTexts);
    failedTexts.forEach( function( text, index ) {
      text.calculationError = step;
    });
  }

	//console.log(" calculateVarsAtStep, vars = ", vars);


  //FIX - Must use the return keyword
  return vars;
}

// After the problem parse, we want to call :   calculateVarsAtStep(currentStep = 0);


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
  // console.log("iwp5:553> graphWindow :", inGraphWindow);
  graphWindow = inGraphWindow;

  // Hook into new iwp5-graph to redraw axes.
  graphSetWindowFromAnimation(graphWindow);

}

function addInput(input) {
  input.objectType = 'input'

  // 2018MAr15 Some re-processed problems have calculation order to fix circular dependency
  if ( input["@attributes"] != null && input["@attributes"]["calculationOrder"] != null  ) {
    input.calculationOrder = +input["@attributes"]["calculationOrder"];
    //console.log("iwp5:586> Imported iwp450 calculation order: ", input.calculationOrder )
  }

  //console.log("addInput: ", input );
  inputs.push( input );
  // {name: "ar", text: "Amplitude", initialValue: "9.0", units: "m"}
  // 07 Oct 2016 Honoring hidden flag
  var style = "";
  if ( input.hidden == "1" ) {
    style = "display:none;"
  }

    // 2018Mar01 Fix for empty unit labels
  var unitLabel = "";
  if ( typeof input.units ==="string" ) { unitLabel = input.units; }

  htmlInputs.push( "<tr id='input_" + input.name + "' style='" + style + "' class='iwp-input-row'><td class='iwp-input-label'>"+ input.text +"</td><td class='iwp-input-value'><input id='" + input.name + "' type='text' value='" + input.initialValue + "'> " + unitLabel + "</td></tr>")
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

  // 2018MAr15 Some re-processed problems have calculation order to fix circular dependency
  if ( output["@attributes"] != null && output["@attributes"]["calculationOrder"] != null  ) {
    compiledOutput.calculationOrder = +output["@attributes"]["calculationOrder"];
    // console.log("iwp5:678> Imported iwp450 calculation order: ", compiledOutput.calculationOrder )
  }

  outputs.push( compiledOutput );
  // { "name": "axr", "text": "Acceleration", "units": "m/ss", "calculator": { "@attributes": { "type": "parametric" }, "value": "Red.xaccel" } }
  var style = ""
  if ( output.hidden == "1" ) {
    style = "display:none;'"
  }
  var unitLabelOutput = "";
  if ( typeof output.units === "string" ) { unitLabelOutput = output.units; }

  htmlOutputs.push( "<tr style='" + style +"' id='output_" + output.name + "' class='iwp-output-row'><td class='iwp-output-label'>"+ output.text +"</td><td class='iwp-output-value'><input id='" + output.name + "' type='text' value='-999' disabled> " + unitLabelOutput + "</td></tr>");
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
  // console.log("addSolid width: ", solid.shape.width.calculator.value);

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
  		type: solid.shape["@attributes"].type,
  		drawTrails: solid.shape["@attributes"].drawTrails,
  		drawVectors: solid.shape["@attributes"].drawVectors,
  		graphOptions:
        Object.assign( solid.shape.graphOptions["@attributes"],
                      { initiallyOn: solid.shape.graphOptions.initiallyOn["@attributes"] } ),
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

  // 2018Mar15 Some re-processed problems have calculation order to fix circular dependency
  if ( solid["@attributes"] != null && solid["@attributes"]["calculationOrder"] != null  ) {
    compiledSolid.calculationOrder = +solid["@attributes"]["calculationOrder"];
    // console.log("iwp5:678> Imported iwp450 calculation order: ", compiledSolid.calculationOrder )
  }


  // If the problem iwp solid has a polygon shape, need to iterate over an initialize each of the calcualtors.
  // hard to do as part of the initialization because it is a dynamic list.
  // Add points here..?
  if ( compiledSolid.shape.type == "polygon" ) {
    compiledSolid["points"] = []
    solid.shape.points.point.forEach ( function( i, index ) {
      var point = {
        xpath: {calculator: compileCalculator(i.xpath.calculator),},
        ypath: {calculator: compileCalculator(i.ypath.calculator),},
      }
    compiledSolid.points.push(point)
    });
    // console.log("iwp5:834> Compiled polygon: ",compiledSolid)
  }

  if ( compiledSolid.shape.type == "Bitmap") {
    // console.log("iwp5:649> Solid is a Bitmap type, building angle: " , solid.shape.angle )
    if (solid.shape.angle) {


      compiledSolid.shape.angle = {calculator: compileCalculator(solid.shape.angle.calculator)}
    }
    compiledSolid.fileUri = "../../images/"+solid.shape.file["@attributes"].image.split("/images/")[1]
    // console.log("fileUri:",compiledSolid.fileUri)
  }

  solids.push(compiledSolid);


  //HTML
  if (compiledSolid.shape.type == "circle") {
    //console.log("it's a circle");
    svgSolids.push( "<ellipse id='solid_" +solid.name+ "' cx='500' cy='500' rx=" +xWidth(solid.shape.width.calculator.value)+ " ry=" +yHeight(solid.shape.height.calculator.value)+ " style='fill:rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")'> " );
  }
  else if (compiledSolid.shape.type == "rectangle") {
    //console.log("it's a rectangle");
    svgSolids.push( "<rect id='solid_" +solid.name+ "' width='" +30+ "' height='" +30+ "' style='fill:rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")'> " );
  }
  else if (compiledSolid.shape.type == "line") {
    //console.log("it's a line")
    svgSolids.push("<line id='solid_" +solid.name+ "' x1='' x2='' y1='' y2='' stroke='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")' stroke-width='2'>");
  }
  else if (compiledSolid.shape.type == "vector") {
    svgSolids.push("<polyline id='solid_" +solid.name+ "' points='' stroke='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")' stroke-width='2' fill='none'>");
  }
  else if (compiledSolid.shape.type == "polygon") {
    //console.log("it's a polygon:", solid.name);
    svgSolids.push("<polyline id='solid_" +solid.name+ "' points='' stroke='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")' stroke-width='2' fill='rgb("+solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+")'>");
  }
  else if (compiledSolid.shape.type == "Bitmap") {
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

    console.log("iwp5:712> ERROR: Unrecognized Solid Shape Type: ", compiledSolid.shape.type)
    return;
  };
  if (solid.shape["@attributes"].drawTrails == "true") {
    svgSolids.push("<polyline id='solid_" +solid.name+ "_trail' points='20,20 50,50' stroke='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")' stroke-width='1' fill='none'></polyline>");
  }
}

function addObject(object) {


  //2018Oct12 - Detect the WaveBox
  if ( object["@attributes"] && object["@attributes"]["class"] == "edu.ncssm.iwp.objects.grapher.DObject_Grapher" ) { 
    alert("This Animation Contains a GraphBox, Not yet implented in IWP5");
    return;
  }
  
  var compiledObject = {
    objectType: 'object',
    name: object.name,
    shape: {
      type: object["@attributes"].class,
      height: 1,
      width: 1
    },
    text: object.text,
    units: object.units,
    showValue: ( object.showValue == "true" || false ),
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


  if (object["@attributes"].class == "edu.ncssm.iwp.objects.floatingtext.DObject_FloatingText") {

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

	var incomingType = iwpCalculator["@attributes"].type
	if ( incomingType == "parametric" ) {

		/*
        <calculator type="parametric">
            <value>ar.value*cos((kr.value/mr.value)^.5*t+toRadians(pr.value)*1)</value>
        </calculator>
		*/

        var e = migrateLegacyEquation(iwpCalculator.value);

		var c = {
			type: "mathjs",
			compiled: math.compile( e ),
			equation: e
		}

		// console.log("iwp5:760> Compiled: ", iwpCalculator.value, " compiledTo: ", c)
        return c;


	} else if ( incomingType == "euler") {
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
          type: "euler-mathjs",
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
		return {type:"unsupported", "incomingType": incomingType, "iwpCalculator": iwpCalculator };
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

  var newValue = compiled.eval(vars)
  if ( $.isNumeric(newValue)) {
    return newValue;
  } else if ( newValue["value"] != undefined ) {
     return newValue.value;
  } else {
     throw("Unable to process compiled, not numeric, doesn't have value property: ", newValue)
  }
}

// Config flag for develoeprs to enable debugging of euler acceleration calculations - have fun!
var CONFIG_throw_acceleration_calculation_exceptions = true;


/** 2018Mar22 Bugfix, use the argument 'calculateStep' instead of reaching out to global */

function evaluateCalculator( resultVariable, calculator, calculateStep, vars, verbose, objectName ) {
  // ResultVariable is solidname.x or solidName.y


  if ( calculator == null ) {
    //console.log("iwp5:688: Warning Null Calculaor for: ", resultVariable)

    return { value: 0 }
  }
	else if ( calculator.type == "mathjs" )   {
		/*
			{type : mathjs, compiled: Object}
		*/
		try {
// Next Step: 2016Sep23 - Figure out why scientific notation is causing the mathjs eqn parsing exception:
/*
evaluateCalculator:364> Unable to evaluate calculator:  Error: Undefined symbol k(…)
iwp5.js:415 evaluateCalculator:364> Equation:  k*(F^(-1.5)+G^(-1.5))
iwp5.js:416 evaluateCalculator:364> Vars:  Object {step: 0, G: -9.8, t: -0.05, tDelta: 0.002, delta_t: 0.002…}
iwp5.js:187 iwp:178: Wrote solid:  Bsum  to vars:  Object {step: 0, G: -9.8, t: -0.05, tDelta: 0.002, delta_t: 0.002…}
*/
//vars.k = 0.000000738528
//vars.F = 0.005125000000000001

      // 2016Oct11 -- Parameteric Calculators need to calculate their own instantaneous velocity.
      // console.log("iwp5:846> Running vars with our custom functions: ", vars, "  to this calculator: " , calculator );

	  var result = calculator.compiled.eval(vars);

      if ( !isFinite(result) ) {
        throw "iwp5:851 Compiled vars are not finite"
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
      if (resultVariable.slice(resultVariable.length-4) == ".out") {
        //console.log("it's an output")
      }
      return { value: result,
        displacement: result,
        velocity: calculator.velocity,
        acceleration: calculator.acceleration };
		}
    catch ( err ) {
      if ( verbose ) {
			console.log("evaluateCalculator:450> ERROR: " + resultVariable + "> Unable to evaluate calculator: ", err );
      console.log("evaluateCalculator:450> ERROR: " + resultVariable + "> Equation: ", calculator.equation );
      console.log("evaluateCalculator:450> ERROR: " + resultVariable + "> Vars: ", vars);
      }
			// return { value: undefined };  // was -1
      throw err;
		}
	} else if ( calculator.type == "euler-mathjs" ) {
    try {
      // 2016-Sep-23 For Euler V5, store the cache of current displacement and velocity IN calculator.
      // An enhancement would be to expose these values out as complex return ttypes of this function,
      // so that they could he historically archived in the variable step array.
      //  IIRC, IWPv4, these were available as xdisp, xvel, xaccell on solids, for example.
      // Today, in IWP5, our return structure out of thi sufnction is a single double value.


      var dt = vars["delta_t"]

      /*
      // This is handled in the master calculate step now  2018Mar22
      if ( calculator["initialDisplacement"] == undefined ) {
        calculator.initialDisplacement = evaluateCompiledMath(calculator.initialDisplacementCompiled, vars)
        calculator.currentDisplacement = calculator.initialDisplacement
        console.log("iwp5:380> calculating initial displacement to: ", calculator.initialDisplacement )
      }

      if ( calculator["initialVelocity"] == undefined ) {
        calculator.initialVelocity = evaluateCompiledMath(calculator.initialVelocityCompiled, vars)
        calculator.currentVelocity = calculator.initialVelocity * dt;
<<<<<<< HEAD
        //console.log("iwp5:405> calculating initial velocity to: ", calculator.initialVelocity )
      }


      // 2018Mar15 Special new code to look backwards
      var previousVars = varsAtStep[currentStep-1];
      if ( previousVars ) {
        // copy everything with this name into current step, which gets all dimensions
        //BOOK
        vars[objectName] = previousVars[objectName]
        // console.log("iwp5:1050> Copied past variable forward: vars["+objectName+"] = ", vars[objectName])
      }


      // 2018Mar15 - Write our displacement values, etc ino current vars.
      // Mimick IWP4 Behavior, set current velocity + displacement into curent vars
      //vars[resultVariable+"disp"] = calculator.currentDisplacement;
      //vars[resultVariable+"pos"] = calculator.currentDisplacement;
      //vars[resultVariable+"vel"] = calculator.currentVelocity;
      // console.log("iwp5:1047> For Step: " + currentStep + " resultVariable: " + resultVariable + " disp: " + calculator.currentDisplacement  + "  vel: " + calculator.currentVelocity);



      if ( verbose ) {
        // console.log("iwp5:661>", resultVariable, "BEFORE STEP: ", currentStep, "/", changeStep, "  accelerationCompiled: ", calculator.accelerationCompiled,  "  vars: ", JSON.stringify(vars) )
      }
      */

      var acceleration = null;
      try {
        // then calculate the acceleratiion
        // 2018Mar22 Fix to only apply the acceleration time component to the change in velocity.

        // console.log("iwp5:1088> Calculating acceleration via calculator: ", calculator, " at calcStep: " + calculateStep + " for vars: ", vars )
        acceleration = calculator.accelerationCompiled.eval(vars);

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
          calculator.currentVelocity = calculator.initialVelocity;
          calculator.currentDisplacement = calculator.initialDisplacement; // no adjustment

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
  }
  else {
    if ( verbose ) {
		console.log("DEVELOPER: Unsupported calculator type : ", calculator);
		throw err;
  }
    //return { value: undefined }; // was -1
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
  setGraphWindow(problem.objects.GraphWindow);


  // Inputs - These could be an array OR a single item.
  if ( $.type ( problem.objects.input ) == 'array' ) {
    problem.objects.input.forEach( function( input, index ) {
      addInput(input);
    });
  } else if ($.type ( problem.objects.input ) == 'item') {
    addInput(problem.objects.input);
  } else if ($.type ( problem.objects.input ) == 'object') {
    addInput(problem.objects.input);

  }


  else {
    console.log("iwp5:954> Unable to handle input: ", $.type ( problem.objects.input ))
  }

  // Output - These could be an array OR a single item.
  if ( $.type ( problem.objects.output ) == 'array' ) {
    problem.objects.output.forEach( function( output, index ) {
      addOutput(output);
    });
  } else  if ( $.type ( problem.objects.output ) == 'item'){
    addOutput(problem.objects.output);
  } else if ($.type ( problem.objects.output ) == 'object') {
    addInput(problem.objects.output);

  } else {
    null;
  }

  // Solids - These could be an array OR a single item.
  if ( typeof problem.objects.solid === 'array' ) {
    problem.objects.solid.forEach( function( solid, index ) {
        addSolid(solid);
    });
  } else if ( problem.objects.solid != null ) {
    // Workaround becaue the php xml to json for single solids, would an object.
      addSolid(problem.objects.solid);
  }





  // Objects - Also detect the floatign texts, which are not relaly solids.
  if ( $.type (problem.objects.object) == 'array' ) {
    problem.objects.object.forEach( function( object, index ) {
      addObject(object);
    });
  } else if ( problem.objects.object != null ) {
     addObject(problem.objects.object);
  }
}







//--------------------------------------------------------------------------------
// SVG ViewBox Scaling

var canvasBox = { minX: 0, minY: 0, maxX: 1000, maxY: 1000 };
function yCanvas(y) {
  var yDomain = iwindow.ymax - iwindow.ymin;
  var sum = iwindow.ymax / yDomain;
  var yProportion = - y / yDomain;
  var yCorrected = yProportion + sum;
  var cDomain = canvasBox.maxY - canvasBox.minY;
  var cProportion = yCorrected * cDomain;
  return cProportion;
};

function xCanvas(x) {
  var xDomain = iwindow.xmax - iwindow.xmin;
  var sum = - iwindow.xmin / xDomain;
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
  var xDomain = iwindow.xmax - iwindow.xmin;
  var xProportion = x / xDomain;
  var xCorrected = xProportion + 0.5;
  var cDomain = canvasBox.maxX - canvasBox.minX;
  var cProportion = xCorrected * cDomain;
  return cProportion;
};
function yCanvasGridlines(y) {
  var yDomain = iwindow.ymax - iwindow.ymin;
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

