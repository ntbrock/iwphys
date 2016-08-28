/**
 * 2016-Jul-20 Graphic Layer Initailization to do our own reflection to turn svg cooridnate system into cartesian coordinates.
 */
//Initialize viewbox.
var viewBox = { minX: 0, minY: 0, maxX: 1000, maxY: 1000 };
$(function(){ 
	var viewBoxAttrs = $("#move")[0].getAttribute("viewBox").split(" ");
	viewBox= { minX: parseFloat(viewBoxAttrs[0]), minY: parseFloat(viewBoxAttrs[1]), maxX: parseFloat(viewBoxAttrs[2]), maxY: parseFloat(viewBoxAttrs[3]) };
	//Debugging 20-Jul-2016
	//console.log("Initialized Viewbox: ", viewBox );
});
//Parse motion equations from user input.
var equations = {};
function parseEquationsFromUserInput() { 
	try { 
		equations.ballX = math.compile( $("#ballX").val() );
		$("#ballX").removeClass("input-error");
		$("#ballX").addClass("input-ok");
	} catch (err) { 
		console.log("Unable to compile X equation: ", err);
		//Green or red background color.
		$("#ballX").removeClass("input-ok");
		$("#ballX").addClass("input-error");
	}
	try { 		
		equations.ballY = math.compile( $("#ballY").val() );
		$("#ballY").addClass("input-ok");
		$("#ballY").removeClass("input-error");
	} catch (err) { 
		console.log("Unable to compile Y equation: ", err);
		$("#ballY").addClass("input-error");
		$("#ballY").removeClass("input-ok");
	}
}
// Bind to user change.
$("#ballX").change(function() { parseEquationsFromUserInput(); reset(); });
$("#ballY").change(function() { parseEquationsFromUserInput(); reset(); });
//Parse equations on load.
$(function(){
	//Debugging 20-Jul-2016:
	//console.log("parseEquations: ", equations);
	parseEquationsFromUserInput();
});
	
/**
 * 2016-Jul-20 Concept for creating a associate hash of variable -> value
 */
//Create initial variable array for applet.
var varsInitial = {
	y: 300,
	yi: 300,
	x: 0,
	xi: 0,
	Vx: Math.sqrt(3200),
	Vy: Math.sqrt(3200),
	v: Math.sqrt(Math.pow(Math.sqrt(3200),2)+Math.pow(Math.sqrt(3200),2)),
	Ay: -10,
	Ax: 0,
	theta: Math.PI/4	
};
//Create original variable array for simulation.
var varsOriginal = {};
$.extend(varsOriginal, varsInitial);
// Initialize the memory space for the large array that holds all histroical values
var varsAtStep = [];
// T will always exist in every problem at every step, but must be set based on problem.
var t = 0;
// Tdelta is how much time changes with each animation step.
var tDelta = .2; 
var varsNow = {};
// Run the simulation forward in time, step by step.
function createMemory () {
	// Initialize time - the big bang!
	var stepNow = 0;
	varsAtStep[0] = varsOriginal;
	//Step 0 really is the original!
	varsOriginal.t = t;
	for ( var step = 1; step <= 100; step++ ) { 
	var varsPrevious = varsAtStep[step-1];
	varsNow = {};
	$.extend(varsNow, varsPrevious);
	//console.log("step: ", step, " varsPrevious: ", varsPrevious);
	// Advance time
	varsNow.t = varsPrevious.t + tDelta;
	// Do the calcs here
	varsNow.x = (varsPrevious.Vx+varsPrevious.Ax*varsPrevious.t)*tDelta+varsPrevious.x;	
	varsNow.y = (varsPrevious.Vy+varsPrevious.Ay*varsPrevious.t)*tDelta+varsPrevious.y;
	varsAtStep[step] = varsNow;
	};
};
createMemory();

//Debugging 20 Jul 2016
//console.log("varsOriginal: ", varsOriginal);
//console.log("varsNow: " , varsNow);
//console.log("varsAtStep: " , varsAtStep);
//- END 2016-Jul-20 Brockman -----------------------------------------------------------

//Removed code, 27 Jul 2016: migrated to variable array.
/*var originalY = 300;
var originalX = 0;
var originalV = 80;
var originalAy = -10;
var originalAx = 0;
var originalTheta = Math.PI/4;
var originalTimeStep = 0.05;*/

/* Update to memory 26-Jul-2016
//Cannon angle.
var theta = Math.PI/4;
//Initial position.
var xi = originalX;
var yi = originalY;
//Initial velocity.
var v = originalV;
//Initial acceleration components.
var ax = originalAx;
var ay = originalAy;
*/

// A clock.
var currentIteration = 0;
var t = 0;
function time() {
t += .01; 
};
//Create variables from original array, for use in below equations.
function varsCurrent() {
	// TODO - pull this out of the in memory buffer of varsByStep.
	return { theta: varsNow.theta, xi: varsNow.xi, yi: varsNow.yi, v: varsNow.v, Vx: varsNow.Vx, Vy: varsNow.Vy, ax: varsNow.Ax, ay: varsNow.Ay, t: t };
}
//These are parametric functions governing motion. This would come from the IWP XML file, user defined. 
function x(t) {
	// 2016-Jul-20 - Use the dynamically parsed values 
	var eqn =  equations.ballX;
	var vars = varsCurrent();
	try { 
		var result =  eqn.eval(vars);
		return result;
	} catch ( err ) { 
		console.log("x:135> Unable to evaluate equation: ", eqn, err);
		return 0; 
	}
	// var x = v*t*Math.cos(theta);
	// return x;
};
function y(t) { 
	var eqn = equations.ballY;
	var vars = varsCurrent();
	try {
		var result = eqn.eval(vars);
		return result;
	}
	catch ( err ) { 
		console.log("y:157> Unable to evaluate equation: ", eqn, err);
		return 0;
	}
};
//Translator functions to adjust SVG coordinate system to Cartesian coordinate system.
function yView(y) {
	return viewBox.maxY -1*y;
};
function xView(x) {
	return x;
};
//Function governing projectile movement.
function move() {
	//Move circle.
	d3.selectAll("circle")
		.attr("visibility", "visible")
		.attr("cx", function(d) { return xView(x(t)); } )	  				
		.attr("cy", function(d) { return yView(y(t)); } );
	//Generate object trail. 
	applyTrail();
	//Debugging Code
	/*(function () {
		console.log ("y position = " + d3.selectAll("circle").attr("cy"));
		console.log ("x position = " + d3.selectAll("circle").attr("cx"));
		console.log ("time = " + t)
	}) () ;*/
};	
//Set interval to renew position.
var trigger = null;			
var clock = null;
//Begins motion.
 f

//Debugging Code
/*function check() {
	//console.log ("time = " + t);
	console.log ("x position = " + d3.selectAll("circle").attr("cx"));
	console.log ("y position = " + d3.selectAll("circle").attr("cy"));
}
setInterval(function () { check(); }, 1000);*/

//Where will the projectile land?
/*function impact() {

	var impactPosition = (v*Math.cos(theta)/ay)*(v*Math.sin(theta)+Math.sqrt(Math.pow(v,2)*Math.pow(Math.sin(theta),2)+2*ay*(yi-700)))-10;
	return impactPosition;

};*/

//Update display.
function clockDisplay () {
	var rounded = t.toFixed(2);	
	document.getElementById("digClock").innerHTML = rounded;
};

//Incremental playbar.
var playBarIncrement = .05;
function backTick() {
	stop();
	trigger = setInterval("move()", 1);
	digClock = setInterval("clockDisplay()", 100);
	document.getElementById("startStop").setAttribute("onclick", "go()");
	document.getElementById("startStop").setAttribute("value", "Resume");
	t -= playBarIncrement;
	//console.log("worked");
};
function forwardTick() {
	stop();
	trigger = setInterval("move()", 1);
	digClock = setInterval("clockDisplay()", 100);
	document.getElementById("startStop").setAttribute("onclick", "go()");
	document.getElementById("startStop").setAttribute("value", "Resume");
	t += playBarIncrement;
	//console.log("worked");
};
forwardTickButton = document.getElementById("forwardTick");
backTickButton = document.getElementById("backTick");
//Set arrow tick to keyboard control.
/*function keyboardControl() {
	document.addEventListener('keydown', (event) => {
		const keyName = event.key;
		if (keyName === 'ArrowRight' && $("#input").is(":not(:focus)") && $("#input2").is(":not(:focus)") && $("#input3").is(":not(:focus)") && $("#input4").is(":not(:focus)") && $("#ballX").is(":not(:focus)") && $("#ballY").is(":not(:focus)")) {
		    // not alert when only Control key is pressed.
		    forwardTick();
		}
		else if (keyName === 'ArrowLeft' && $("#input").is(":not(:focus)") && $("#input2").is(":not(:focus)") && $("#input3").is(":not(:focus)") && $("#input4").is(":not(:focus)") && $("#ballX").is(":not(:focus)") && $("#ballY").is(":not(:focus)")) {
			backTick();
		}
		else {
			return;
		}
	}, false);
	window.addEventListener("keydown", function(e) {
	// space and arrow keys
	if([37, 39].indexOf(e.keyCode) > -1 && $("#input").is(":not(:focus)") && $("#input2").is(":not(:focus)") && $("#input3").is(":not(:focus)") && $("#input4").is(":not(:focus)") && $("#ballX").is(":not(:focus)") && $("#ballY").is(":not(:focus)")) {
	 	e.preventDefault();
	}
	}, false);
}
keyboardControl();*/
//Ugh... we need to make an object trail. Plan: create a data set with points and previous points, and apply it as the points atttribute for a polyline.
//https://www.dashingd3js.com/svg-paths-and-d3js2		
var svgContainer = d3.select("svg");
//Sample set of coordinates to create line.
var lineData = [];
function applyTrail() {
	// THIS wil totally break
	// 2016-Jul-20
	//console.log("current iteration: "+currentIteration);
	currentIteration = Math.ceil(t/tDelta);
	lineData = varsAtStep.slice(0, currentIteration); 
	/* Debugging 28 Jul 2016
	console.log("lineData: "+lineData);
	console.log("t: "+t); */
	//console.log("lineFunction: "+lineFunction(lineData));
	//Acesses data in array and extracts coordinates.
	var lineFunction = d3.svg.line()
								.x(function(d) { return xView(d.x); })
								.y(function(d) { return yView(d.y); })
								.interpolate("linear");
	var svgContainer = d3.select("g");
	//Line path.
	var lineGraph = $("#objectTrail")
								.attr("d", lineFunction(lineData))
								.attr("stroke-width", 2);
};
setInterval("applyTrail()", 100);
//Moves ground.
function groundAndPlatform () { 
	if (varsOriginal.y <= 300) {
		var lineData = [ { "x": 0,     "y": yView(varsOriginal.y)},  
		          		 { "x": 1000,  "y": yView(varsOriginal.y)},
		                 { "x": 1000,  "y": 1000}, 
		                 { "x": 0,   "y": 1000} ];
		var lineFunction = d3.svg.line()
		                         .x(function(d) { return d.x; })
		                         .y(function(d) { return d.y; })
		                         .interpolate("linear");
		$("#ground").attr("d", lineFunction(lineData));
		$("#platform").attr("d", "M0 0");
	}
	else if (varsOriginal.y > 300) {
		var lineData = [ { "x": 0,     "y": yView(varsOriginal.y)},  
		          		 { "x": 20,  "y": yView(varsOriginal.y)},
		                 { "x": 20,  "y": 720}, 
		                 { "x": 0,   "y": 720} ];
		var lineFunction = d3.svg.line()
		                         .x(function(d) { return d.x; })
		                         .y(function(d) { return d.y; })
		                         .interpolate("linear");
		$("#platform").attr("d", lineFunction(lineData))
					  .attr("fill", "brown");
	} 
	else {
		return;
	}
};
$("*").change(function () { 
	groundAndPlatform();
});
//Resets values to original parameters.
function resetValues() {
	varsOriginal.yi = varsInitial.yi;
	varsOriginal.y = varsInitial.y;
	varsOriginal.v = varsInitial.v;
	varsOriginal.Ay = varsInitial.Ay;
	varsOriginal.theta = varsInitial.theta;
	$("#input").val(varsInitial.yi);
	$("#input2").val(varsInitial.v);
	$("#input3").val(-varsInitial.Ay);
	$("#input5").val(varsInitial.theta*180/Math.PI);
	/*
	text1.value = originalY;
	text2.value = originalV;
	text3.value = originalAy;
	*/
	reset();
}
// Some elements of code must wait for HTML load to call elements - included below, and tied to html onload event.
function onloadFunction() {
	$("#input").change(function () { 	
							varsOriginal.yi = +$("#input").val();
							varsOriginal.y = +$("#input").val();
							reset();
							/* Debugging 28 Jul 2016
							console.log(varsOriginal.yi);
							console.log(varsAtStep[0]);
							console.log(varsNow.y);
							console.log(varsAtStep[5]);*/
						});

	$("#input2").change(function () { 
							$("#input2").each(function () {
								varsOriginal.v = parseFloat($(this).val());
							});
							varsOriginal.Vx = varsOriginal.v*Math.cos(varsOriginal.theta);
							varsOriginal.Vy = varsOriginal.v*Math.sin(varsOriginal.theta);
							reset();
						});

	$("#input3").change(function () { 
							varsOriginal.Ay = -(+$("#input3").val()); 
							//console.log("varsOriginal: "+varsOriginal);
							reset();
						});

	$("#input4").change(function () { 
							playBarIncrement = +$("#input4").val;
							reset();
						});
	$("#input5").change(function () { 
							$("#input5").each(function () {
								varsOriginal.theta = parseFloat($(this).val());
							});
							varsOriginal.theta = varsOriginal.theta*Math.PI/180;
							reset();
						});

	$("#ballX").change(function() { parseEquationsFromUserInput(); reset(); });
	$("#ballY").change(function() { parseEquationsFromUserInput(); reset(); });
};







