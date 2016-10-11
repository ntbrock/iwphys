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
  var vars0 = calculateVarsAtStep(0);
  archiveVarsAtStep( currentStep, vars0 ); // Boot up the environment
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
varsConstants = { G : -9.8 , step: function(x) { if ( x > 0 ) { return 1 } else { return 0 } }}


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

	//console.log("handleStep:87> current: " , currentStep, "  change: ", changeStep );
	// apply the time change.
	var newStep = currentStep + changeStep;

	// handle time horizons
	if ( newStep < 0 ) { 
		changeStep = 0;
		newStep = 0;
	} 
	// TODO - end of time.

	//console.log("handleStep:61> newStep: " + newStep)

	if ( newStep != currentStep ) { 

		// UI rendering is handled by the calculate as a side effect
		var vars = calculateVarsAtStep(newStep);

		archiveVarsAtStep( newStep, vars );
	}

	currentStep = newStep;

}


function archiveVarsAtStep( step, vars ) {
  // console.log("Archving vars at step: " + step );
  varsAtStep[step] = {};
	$.extend(varsAtStep[step], vars)
}
  

function calculateOutputAtStep(output, step, vars, verbose) { 
    var newValue = evaluateCalculator( output.name+".out", output.calculator, vars, verbose ).value;
    vars[output.name] = newValue;

    // -> update the DOM with the new reuslts.
    updateUserFormOutputDouble(output, newValue);
}


function calculateSolidAtStep(solid, step, vars, verbose) { 

    // TODO - Derive Velocity and Acceleration!
 

    if ( solid.name == "P" ) { 
      var breaker = 1
    }

    var xComplex = evaluateCalculator( solid.name+".x", solid.xpath.calculator, vars, verbose )
    var x = xComplex.value

    var yComplex = evaluateCalculator( solid.name+".y", solid.ypath.calculator, vars )
    var y = yComplex.value

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
      height: evaluateCalculator( solid.name+".h", solid.shape.height.calculator, vars ).value,
      width: evaluateCalculator( solid.name+".w", solid.shape.width.calculator, vars ).value
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
    updateSolidSvgPathAndShape(solid, calc)

    //console.log("iwp:178: Wrote solid: ", solid.name, " to SVG & vars: ", vars )

}

// This was needed for self-referential euler's animations. like em-ratio-2d.iwp.
var CONFIG_clone_step_from_previous = true;


function calculateVarsAtStep(step) { 

  // vars should be a map of string to double, including the mathematical / physical constants.
  var vars = { step: step }
  $.extend(vars, varsConstants);

  // EXPERIMENTAL: Pull the previous variables in from last step?
  if ( CONFIG_clone_step_from_previous ) { 
  try { 
    $.extend(vars, varsAtStep[step-1])
    //console.log("calculateVarsATStep:195> Cloned vars from past Step-1: " + (step-1) + " => ", JSON.stringify(varsAtStep) )
  } catch(err) { 
    console.log("calculateVarsAtStep:198> Exception when mirring past vars into current vars:", err)
  }

  }

	// Eveything begins with time, populate t.
	vars.t = time.start + step * time.change;
  vars.tDelta = queryTimeStepInputDouble();  
  vars.delta_t = vars.tDelta  
	updateTimeDisplay(vars.t);
	/*
  for each calulation, query dom for time step
	for each input
		query the dom by input_$id to get the value from the user form
	*/
	$.each( inputs, function( index, input ) {
		// next load in variables for all of the inputs.
//		vars[input.name] = { value: queryUserFormInputDouble(input) };
    vars[input.name] = queryUserFormInputDouble(input);
    });


  var failedOutputs = [];
  var failedSolids = [];

  /*
  for each output perofrm the calculator
*/
  $.each( outputs, function( index, output ) {

    try { 
      calculateOutputAtStep(output, step, vars, false );
    } catch ( err ) { 
      failedOutputs.push(output);
    }   
  });


	/* for each solid
		sequence of the solids does matter in the problem file. 
	*/
	$.each( solids, function( index, solid ) {
		/*
		for x, y, h, w , perform the calculator 
		*/

    try { 
     calculateSolidAtStep(solid, step, vars, false );
        //  -> update the DOM with theose new results
  
    } catch ( err ) { 
        //console.log(":231 caught a faailed solid exception: ", err);
      failedSolids.push(solid);
    }

});


  // console.log(":238 failedOutputs: ", failedOutputs);
  // console.log(":239 failedSolids: ", failedSolids);

  var fatalOutputs = [];
  var fatalSolids = [];

  // REPLAY FAILURES
  $.each( failedOutputs, function( index, output ) {

    try { 
      calculateOutputAtStep(output, step, vars, true );
    } catch ( err ) { 
      fatalOutputs.push(output);
    }   
  });

  $.each( failedSolids, function( index, solid ) {  
    try { 
      calculateSolidAtStep(solid, step, vars, true );  
    } catch ( err ) { 
      fatalSolids.push(solid);  
    }
  });

  if ( fatalOutputs.length > 0 ) { 
    console.log(":238 Giving Up on Circular Calc - FATALOutputs: ", fatalOutputs);
  }
  if ( fatalSolids.length > 0 ) { 
    console.log(":239 Giving Up on Circular Calc - FATALSolids: ", fatalSolids);
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
  //console.log("graphWindow :", inGraphWindow);
  graphWindow = inGraphWindow;
}

function addInput(input) { 
  //console.log("addInput: ", input );
  inputs.push( input );
  // {name: "ar", text: "Amplitude", initialValue: "9.0", units: "m"} 
  // 07 Oct 2016 Honoring hidden flag
  var style = "";
  if ( input.hidden == "1" ) { 
    style = "display:none;'"
  }
  htmlInputs.push( "<tr id='input_" + input.name + "' style='" + style + "' class='bottomBorder'><td>"+ input.text +"</td><td><input id='" + input.name + "' type='text' value='" + input.initialValue + "'> " + input.units + "</td></tr>");
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
  var style = ""
  if ( output.hidden == "1" ) { 
    style = "display:none;'"
  }
  htmlOutputs.push( "<tr style='" + style +"' id='output_" + output.name + "' class='bottomBorder'><td>"+ output.text +"</td><td><input id='" + output.name + "' type='text' value='-999'> " + output.units + "</td></tr>");
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

  // Ifthe problem iwp solid has a polygon shape, need to iterate over an initialize each of the calcualtors.
  // hard to do as part of the initialization because it is a dynamic list. 
  // Add points here..? 

  solids.push(compiledSolid);


  //HTML 
  if (solid.shape["@attributes"].type == "circle") {
    //console.log("it's a circle");
    svgSolids.push( "<ellipse id='solid_" +solid.name+ "' cx='500' cy='500' rx=" +xWidth(solid.shape.width.calculator.value)+ " ry=" +yHeight(solid.shape.height.calculator.value)+ " style='fill:rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")'> " );
  }
  else if (solid.shape["@attributes"].type == "rectangle") {
    //console.log("it's a rectangle");
    svgSolids.push( "<rect id='solid_" +solid.name+ "' width='" +30+ "' height='" +30+ "' style='fill:rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")'> " );
  }
  else if (solid.shape["@attributes"].type == "line") {
    //console.log("it's a line");
    svgSolids.push("<line id='solid_" +solid.name+ "' x1='' x2='' y1='' y2='' stroke='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")' stroke-width='2'>");
  }
  else if (solid.shape["@attributes"].type == "polygon") {
    console.log("it's a polygon")
    svgSolids.push("<polyline id='solid_" +solid.name+ "' points='' stroke='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")' stroke-width='2' fill="+solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+">");  
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
			compiled: math.compile( (iwpCalculator.value) ),
			equation: iwpCalculator.value
		}
		// console.log("compileCalculator:171> value : ", iwpCalculator.value, " compiledTo: ", c)
		return c;

	} else if ( incomingType == "euler") {
    
    var c =  { 
      type: "euler-mathjs",
      initialDisplacementCompiled: math.compile( iwpCalculator.displacement ),
      initialVelocityCompiled: math.compile( iwpCalculator.velocity ),
      accelerationCompiled: math.compile( iwpCalculator.acceleration ),
      equation: { 
        acceleration : iwpCalculator.acceleration,
        velocity : iwpCalculator.velocity,
        displacement : iwpCalculator.displacement
      }
    }
    return c;
    /*
        <calculator type="euler">
          <displacement>initxdisp</displacement>
          <velocity>initxvel</velocity>
          <acceleration>-5*t</acceleration>
        </calculator>
      
    */

  }
  else { 
		console.log("DEBUG ERROR: Only parametric and Euler supported in the August 2016 version, unable to handle: ", incomingType);
		return {type:"unsupported", "incomingType": incomingType, "iwpCalculator": iwpCalculator };
	}
}

/**
 * 2016-Sep-23 - Sometimes single variable equations will evalate as object.value, not a numeric
 * Remind Taylor about this if you have questions.
 */

function evaluateCompiledMath( compiled, vars ) { 

  var newValue = compiled.eval(vars)

  if ( $.isNumeric(newValue) ) {
    return newValue;
  } else if ( newValue["value"] != undefined ) { 
     return newValue.value;
  } else { 
     throw("Unable to process compiled, not numeric, doesn't have value property: ", newValue)
  }
}


function evaluateCalculator( resultVariable, calculator, vars, verbose ) {

	if ( calculator.type == "mathjs" )   {
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

			var result = calculator.compiled.eval(vars);


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
		} catch ( err ) { 
      if ( verbose ) { 
			console.log("evaluateCalculator:450> " + resultVariable + "> Unable to evaluate calculator: ", err );
      console.log("evaluateCalculator:450> " + resultVariable + "> Equation: ", calculator.equation );
      console.log("evaluateCalculator:450> " + resultVariable + "> Vars: ", vars);
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




      if ( calculator["initialDisplacement"] == undefined ) { 
        calculator.initialDisplacement = evaluateCompiledMath(calculator.initialDisplacementCompiled, vars)
        calculator.currentDisplacement = calculator.initialDisplacement
        //console.log("iwp5:380> calculating initial displacement to: ", calculator.initialDisplacement )
      }

      if ( calculator["initialVelocity"] == undefined ) { 
        calculator.initialVelocity = evaluateCompiledMath(calculator.initialVelocityCompiled, vars)
        calculator.currentVelocity = calculator.initialVelocity
        //console.log("iwp5:405> calculating initial velocity to: ", calculator.initialVelocity )
      }


      if ( verbose ) { 
        console.log("iwp5:428> BEFORE STEP: ", currentStep, "/", changeStep, "  accelerationCompiled: ", calculator.accelerationCompiled,  "  vars: ", vars )

          }

      var acceleration = 0;
      try { 
        // then calculate the acceleratiion
       acceleration = calculator.accelerationCompiled.eval(vars);
      } catch ( err ) { 
        console.log("evaluateCalculator:580> " + resultVariable + "> Unable to evaluate acceleration, setting to 0.  Calculator: ", err, calculator.equation, "Vars: ", JSON.stringify(vars) );
      }

      if ( currentStep == 0 ) { 
          calculator.currentVelocity = calculator.initialVelocity;
          calculator.currentDisplacement = calculator.initialDisplacement;
      } else if ( changeStep > 0 ) { 
          // Positive direction calcuation
          calculator.currentVelocity += acceleration
          calculator.currentDisplacement += calculator.currentVelocity

      } else if ( changeStep < 0 ) { 
          calculator.currentVelocity -= acceleration
          calculator.currentDisplacement -= calculator.currentVelocity
      } else { 
        // No step direction
      }

if ( true ) { 
      console.log("iwp5:428> " + resultVariable + " at t = " + vars["t"] + "> AFTER STEP: ", currentStep, "/", changeStep, "  d: ", calculator.currentDisplacement, "  v: ", calculator.currentVelocity, " a: ", acceleration );
}

      return { value: calculator.currentDisplacement,
        displacement: calculator.currentDisplacement,
        velocity: calculator.currentVelocity,
        acceleration: acceleration }

      // return displacement.value; 
    } catch ( err ) {
      if ( verbose ) { 
      console.log("evaluateCalculator:375> Unable to evaluate calculator: ", err, calculator.equation, vars);
    }
      throw err;
      //return { value: undefined }; // was -1
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
  setGraphWindow(problem.objects.graphWindow);


  // Inputs - These could be an array OR a single item.
  if ( $.type ( problem.objects.input ) == 'array' ) { 
    $.each( problem.objects.input, function( index, input ) {
      addInput(input);
    });
  } else if ($.type ( problem.objects.output ) == 'item') {
    addInput(problem.objects.input);
  } 
  else { 
    null
  }

  // Output - These could be an array OR a single item.
  if ( $.type ( problem.objects.output ) == 'array' ) { 
    $.each( problem.objects.output, function( index, output ) {
      addOutput(output);
    });
  } else  if ( $.type ( problem.objects.output ) == 'item'){ 
    addOutput(problem.objects.output);
  } else {
    null;
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
  $("#itime_change").val(time.change);
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
  $("#inputTable").append("<tr><th colspan='2'>Inputs</th></tr>");
  $("#outputTable").append("<tr><th colspan='2'>Outputs</th></tr>");
  $.each(htmlInputs, function( index, input ) {
    if (input.hidden == "1") {
      $("#inputTable").append(input);
    }
    else {
      $("#inputTable").append(input);
    }
  })
  $.each(htmlOutputs, function( index, output ) {
    if (output.hidden == "1") {
      $("#outputTable").append(output);

      return;
    }
    else {
      $("#outputTable").append(output);
    }
  })
  
  /* Debugging 07 Oct 2016 Ryan Steed
  //$("#outputTable").append("<tr><th colspan='2'>Outputs</th></tr>"+htmlOutputs);
  */ 
  //Moved to addSolidsToCanvas, 8 Aug 2016
  //$("#solids").html( solids.join(" ") );

  fitText("inputTable");
  renderCanvasFromMemory();
  addSolidsToCanvas(svgSolids);
};

//21 Sep 2016 Ryan Steed
//Auto-adjust font-size so that font size fits the table.
function fitText(input) {
    var HeightDiv = $("#tabTables").height();
    var toFit = $('#'+input);
    // console.log("input: ", input);
    var HeightTable = toFit.height();
    if (HeightTable > HeightDiv) {
        var FontSizeTable = parseInt(toFit.css("font-size"), 10);
        while (HeightTable > HeightDiv && FontSizeTable > 10) {
            FontSizeTable--;
            toFit.css("font-size", FontSizeTable);
            HeightTable = toFit.height();
        }
    //console.log("text fitted");
    }
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

function queryTimeStepInputDouble() {
  time.change = parseFloat($("#itime_change").val());
  return time.change;
}

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
	$("#itime").html(t.toFixed(2));
  //console.log("t = "+t);
}

function updateSolidSvgPathAndShape(solid, pathAndShape) { 
	
	var svgSolid = $("#solid_" + solid.name);

	//console.log("updateSolidSvgPathAndShape: ", solid, svgSolid, pathAndShape);

	// translate from math to visual.
	/* pathAndShape: { height: 1, width: 1, x: 9, xdisp: 9, y: 0, ydisp: 0 }*/

if (solid.shape.type == "circle") {
    svgSolid.attr("cx", xCanvas(pathAndShape.x))
		.attr("cy", yCanvas(pathAndShape.y))
		.attr("rx", xWidth(pathAndShape.width))
		.attr("ry", yHeight(pathAndShape.height));
    //console.log("rx: ", pathAndShape.width)
  }
  else if (solid.shape.type == "rectangle") {
    svgSolid.attr("x", xCanvas(pathAndShape.x - pathAndShape.width / 2))
		.attr("y", yCanvas(pathAndShape.y - pathAndShape.height / 2))
		.attr("width", xWidth(pathAndShape.width))
		.attr("height", yHeight(pathAndShape.height));
 }
  else if (solid.shape.type == "line") {
    // DEBUGGING RYAN STEED 21 SEP 2016
    /*console.log("y1: ", yCanvas(pathAndShape.y));
    console.log("y2: ", pathAndShape.y + pathAndShape.height);*/
    svgSolid.attr("x1", xCanvas(pathAndShape.x))
    .attr("x2", xCanvas(pathAndShape.x + pathAndShape.width))
    .attr("y1", yCanvas(pathAndShape.y))
    .attr("y2", yCanvas(pathAndShape.y + pathAndShape.height));
  }
  else if (solid.shape.type == "polygon") {
    console.log("number of points: ", solid.shape);
    //var points = "" + 
    //svgSolid.attr("points", )
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


//Hide and reveal table tabs.
function windowSettingsOn() {
	$("#iwindow").attr("style", "visibility:visible");
  $("#ws").attr("class", "bottomBorder");
  inputTableOff();
  otherInfoOff();
  outputTableOff();
  fitText("iwindow");
};
function windowSettingsOff() {
	$("#iwindow").attr("style", "visibility:hidden");
  $("#ws").attr("class", "");
}

function otherInfoOn() {
  $("#otherInfo").attr("style", "visibility:visible");
  $("#oib").attr("class", "bottomBorder");
  outputTableOff();
  windowSettingsOff();
  inputTableOff();
  fitText("otherInfo");
};
function otherInfoOff() {
  $("#otherInfo").attr("style", "visibility:hidden");
  $("#oib").attr("class", "");
}

function outputTableOn() {
  $("#outputTable").attr("style", "visibility:visible");
  $("#ot").attr("class", "bottomBorder");
  otherInfoOff();
  windowSettingsOff();
  inputTableOff();
  fitText("outputTable");
};
function outputTableOff() {
  $("#outputTable").attr("style", "visibility:hidden");
  $("#ot").attr("class", "");
}

function inputTableOn() {
  $("#inputTable").attr("style", "visibility:visible");
  $("#it").attr("class", "bottomBorder");
  otherInfoOff();
  windowSettingsOff();
  outputTableOff();
  fitText("inputTable");
};
function inputTableOff() {
  $("#inputTable").attr("style", "visibility:hidden");
  $("#it").attr("class", "");
}

//Click handles.
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
	updateTimeDisplay(0);
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

function handleInputChange() {
  $("*").change( function () {   
                  handleResetClick();
                });
}
handleInputChange();