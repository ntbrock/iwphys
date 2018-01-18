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

var path = d3.path();


function graphInit() { 

  var svg = d3.select('#graph');
  console.log("iwp5-graph.js:15 found svg: " , svg)

}

/** 
 * Performs no calculations, but repaints every thing (time, outputs, solids) onto screen from memory at current step.
 */
function graphStep(step, vars) { 
  

  //var vars = varsAtStep[step];
  if ( vars == undefined ) { 
    throw "No previous calculations available at step: " + step;
  } else { 


	var svg = d3.select('#graph');
	console.log("iwp5-graph:17> regraph step with vars: ", vars)    

   svg.append('circle')
//      .classed("iwp-axis-line", true)
      .attr("cx", graphXScale(vars.t))
      .attr("cy", graphYScale(vars.y))
      .attr("r", graphXScale(0.1))
     

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



