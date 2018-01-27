/**
 * Interactive Web Physics 5 - Javascript SVG Animation Implementation
 * Ryan Steed, Taylor Brockman 2016 - Version 5.0 Initial Port to HTML + SVG
 * Matthew Mims, Taylor Brockman 2018  - Version 5.1 Added Graphing and D3
 */

$(function() {
	graphInit();
})



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
var xAxis = d3.axisBottom(graphXScale).ticks(10);
var yAxis = d3.axisLeft(graphYScale).ticks(10);

function graphInit() {

  var svg = d3.select('#graph');
  console.log("iwp5-graph.js:15 found svg: " , svg);
	xGrid(svg.append("g").classed("grid", true).attr("transform", "translate(0, 100)"));
	yGrid(svg.append("g").classed("grid", true).attr("transform", "translate(-100, 0)"));
	xAxis(svg.append("g").classed("iwp-graph-x-axis",true));
	yAxis(svg.append("g").classed("iwp-graph-y-axis",true));
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

}

/**
 * Performs no calculations, but repaints every thing (time, outputs, solids) onto screen from memory at current step.
 */
function graphStep(step, vars) {

	var lastStep = varsAtStep[step-1]
  //var vars = varsAtStep[step];
  if ( vars == undefined ) {
    throw "No previous calculations available at step: " + step;
  } else {


	var svg = d3.select('#graph');
	console.log("iwp5-graph:48> currentStep: ", vars)
	console.log("iwp5-graph:49> lastStep: ", lastStep)
	path1.moveTo(graphXScale(lastStep.t), graphYScale(lastStep.object.ypos))
	path1.lineTo(graphXScale(vars.t), graphYScale(vars.object.ypos))
	visualPath1.attr("d", path1)
	path2.moveTo(graphXScale(lastStep.t), graphYScale(lastStep.object.yvel))
	path2.lineTo(graphXScale(vars.t), graphYScale(vars.object.yvel))
	visualPath2.attr("d", path2)
	path3.moveTo(graphXScale(lastStep.t), graphYScale(lastStep.object.yaccel))
	path3.lineTo(graphXScale(vars.t), graphYScale(vars.object.yaccel))
	visualPath3.attr("d", path3)
   /*svg.append('circle')
//      .classed("iwp-axis-line", true)
      .attr("cx", graphXScale(vars.t))
      .attr("cy", graphYScale(vars.y))
      .attr("r", graphXScale(0.1))
	*/

/*
   $.each( outputs, function( index, output ) {
      updateUserFormOutputDouble(output, vars[output.name]);
   });

   $.each( solids, function( index, solid ) {
      updateSolidSvgPathAndShape(solid, vars[solid.name])
   });
*/

  }
}
