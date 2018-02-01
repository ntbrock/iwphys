/**
 * Interactive Web Physics 5 - Javascript SVG Animation Implementation
 * Ryan Steed, Taylor Brockman 2016 - Version 5.0 Initial Port to HTML + SVG
 * Matthew Mims, Taylor Brockman 2018  - Version 5.1 Added Graphing and D3
 */

$(function() {
	graphInit();
})

// Global container, re-populated on initialization with every object, the plotting series 
// are then derived from here as well
var iwpGraphObjects = {}
var iwpGraphSeries = {}


var graphXScale = d3.scaleLinear()
    .domain([-10, 10])
    .range([-100, 100]);

var graphYScale = d3.scaleLinear()
    .domain([-10, 10])
    .range([100, -100]);

var path1 = d3.path();
var path2 = d3.path();
var path3 = d3.path();

var visualPath = null;

var xGrid = d3.axisTop(graphXScale).ticks(10).tickSize(1000);
var yGrid = d3.axisRight(graphYScale).ticks(10).tickSize(1000);
var xAxis = d3.axisBottom(graphXScale).ticks(10).tickSize(0);
var yAxis = d3.axisLeft(graphYScale).ticks(10).tickSize(0);

function graphInit() {

	var svg = d3.select('#graph');


	// Step 1 Build Grid And Axes
	console.log("iwp5-graph.js:38> Building Grid + Axes for svg: " , svg);

	xGrid(svg.append("g").classed("iwp-graph-grid", true).attr("transform", "translate(0, 100)"));
	yGrid(svg.append("g").classed("iwp-graph-grid", true).attr("transform", "translate(-100, 0)"));
	
	xAxis(svg.append("g").classed("iwp-graph-axis",true));
	yAxis(svg.append("g").classed("iwp-graph-axis",true));

	

	/*
	visualPath1 = svg.append('path')
		.classed("iwp-graph-line-red", true)
		.attr("d", path1 )


	path1.moveTo(0, 0)
	visualPath2 = svg.append('path')
								   .classed("iwp-graph-line-green", true)
									 .attr("d", path2 )
	path2.moveTo(0, 0)
	visualPath3 = svg.append('path')
								   .classed("iwp-graph-line-blue", true)
									 .attr("d", path3 )
	path3.moveTo(0, 0)
	*/
}




function graphResetZero(step, vars, solids ) { 
	var svg = d3.select('#graph');

	console.log("iwp-graph:69> graphResetZero, vars: ", vars)

	// Step 2 - Populate Memory for each object that's graphable, plus all of it's visibility
	$.each(solids,function(index, solid) { 
		//console.log("iwp-graph:69> graphResetZero, solid: ", solid)	
		var graphOptions = solid.shape.graphOptions
		//console.log("iwp-graph:69> graphResetZero, solid graphOptions: ", graphOptions)	

		iwpGraphObjects[solid.name] = { solid: solid,
			graphOptions: graphOptions,
			color: solid.color,
			objectVisible: graphOptions.graphVisible == "true",
			paths: {
				xPos: d3.path(),
				yPos: d3.path(),
				xVel: d3.path(),
				yVel: d3.path(),
				xAccel: d3.path(),
				yAccel: d3.path()
			},
			pathsVisible: {
				xPos: graphOptions.initiallyOn != null && graphOptions.initiallyOn.xPos == "true",
				yPos: graphOptions.initiallyOn != null && graphOptions.initiallyOn.yPos == "true",
				xVel: graphOptions.initiallyOn != null && graphOptions.initiallyOn.xVel == "true",
				yVel: graphOptions.initiallyOn != null && graphOptions.initiallyOn.yVel == "true",
				xAccel: graphOptions.initiallyOn != null && graphOptions.initiallyOn.xAccel == "true",
				yAccel: graphOptions.initiallyOn != null && graphOptions.initiallyOn.yAccel == "true"
			},
			pathsSvg: { 
				xPos: null,
				yPos: null,
				xVel: null,
				yVel: null,
				xAccel: null,
				yAccel: null
			}
		}
	})


	// Step 3 Add each graph path to the svg and associate stroke color.
	// Hang onto the svg Memory refernces for good luck.

	$.each(iwpGraphObjects,function(name, graphObject) { 

		console.log("iwp5graph:115> Reset: name: ", name, "  graphObject: ", graphObject)

		var g = svg.append("g").classed("iwp-graph-object", true)

		var stroke = "stroke: rgba("+graphObject.color.red+","+graphObject.color.green+","+graphObject.color.blue+",1);"

		graphObject.pathsSvg.xPos =
			g.append('path').classed('iwp-graph-object-xpos',true).attr("style", stroke).attr("d", graphObject.paths.xPos)

		graphObject.pathsSvg.yPos =
			g.append('path').classed('iwp-graph-object-ypos',true).attr("style", stroke).attr("d", graphObject.paths.yPos)

		graphObject.pathsSvg.xVel =
			g.append('path').classed('iwp-graph-object-xvel',true).attr("style", stroke).attr("d", graphObject.paths.xVel)

		graphObject.pathsSvg.yVel =
			g.append('path').classed('iwp-graph-object-yvel',true).attr("style", stroke).attr("d", graphObject.paths.yVel)

		graphObject.pathsSvg.xAccel =
			g.append('path').classed('iwp-graph-object-xaccel',true).attr("style", stroke).attr("d", graphObject.paths.xAccel)
		
		graphObject.pathsSvg.yAccel =
			g.append('path').classed('iwp-graph-object-yaccel',true).attr("style", stroke).attr("d", graphObject.paths.yAccel)

	});


/*


					visualPath1 = svg.append('path')
		.classed("iwp-graph-line-red", true)
		.attr("d", path1 )


			} }*/




	console.log("iwp5graph:94> Initialized all Graph Objects: ", iwpGraphObjects)

}



/**
 * Performs no calculations, but repaints every thing (time, outputs, solids) onto screen from memory at current step.
 */
function graphStep(step, vars) {

	var svg = d3.select('#graph');
	var lastStep = varsAtStep[step-1]

	//var vars = varsAtStep[step];
	if ( vars == undefined ) {
		throw "No previous calculations available at step: " + step;
	}


	console.log("iwp5-graph:48> currentStep: ", vars)
	console.log("iwp5-graph:49> lastStep: ", lastStep)

	// During each loop, iterate over all the solids that are graphable, and update paths based
	// on incoming vars at step.



	$.each(iwpGraphObjects,function(name, graphObject) { 

		console.log("iwp5graph:176> GraphStep: name: ", name, "  graphObject: ", graphObject)

		var g = svg.append("g").classed("iwp-graph-object", true)

		var stroke = "stroke: rgba("+graphObject.color.red+","+graphObject.color.green+","+graphObject.color.blue+",1);"

		g.append('path').classed('iwp-graph-object-xpos',true).attr("style", stroke).attr("d", graphObject.paths.xPos)
		g.append('path').classed('iwp-graph-object-ypos',true).attr("style", stroke).attr("d", graphObject.paths.yPos)

		g.append('path').classed('iwp-graph-object-xvel',true).attr("style", stroke).attr("d", graphObject.paths.xVel)
		g.append('path').classed('iwp-graph-object-yvel',true).attr("style", stroke).attr("d", graphObject.paths.yVel)

		g.append('path').classed('iwp-graph-object-xaccel',true).attr("style", stroke).attr("d", graphObject.paths.xAccel)
		g.append('path').classed('iwp-graph-object-yaccel',true).attr("style", stroke).attr("d", graphObject.paths.yAccel)

	});




}


	/*
	path1.moveTo(graphXScale(lastStep.t), graphYScale(lastStep.object.ypos))
	path1.lineTo(graphXScale(vars.t), graphYScale(vars.object.ypos))
	visualPath1.attr("d", path1)
	path2.moveTo(graphXScale(lastStep.t), graphYScale(lastStep.object.yvel))
	path2.lineTo(graphXScale(vars.t), graphYScale(vars.object.yvel))
	visualPath2.attr("d", path2)
	path3.moveTo(graphXScale(lastStep.t), graphYScale(lastStep.object.yaccel))
	path3.lineTo(graphXScale(vars.t), graphYScale(vars.object.yaccel))
	visualPath3.attr("d", path3)
	*/

/*
   $.each( outputs, function( index, output ) {
      updateUserFormOutputDouble(output, vars[output.name]);
   });

   $.each( solids, function( index, solid ) {
      updateSolidSvgPathAndShape(solid, vars[solid.name])
   });
*/
