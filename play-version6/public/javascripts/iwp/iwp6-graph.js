
/**
 * Interactive Web Physics 5 - Javascript SVG Animation Implementation
 * Ryan Steed, Taylor Brockman 2016 - Version 5.0 Initial Port to HTML + SVG
 * Matthew Mims, Taylor Brockman 2018  - Version 5.1 Added Graphing and D3
 */

$(function() {
	graphInit("pos");
	graphInit("vel");
	graphInit("accel");
})

// Global container, re-populated on initialization with every object, the plotting series
// are then derived from here as well
var iwpGraphObjects = {}
iwpGraphObjects["pos"] = {}
iwpGraphObjects["vel"] = {}
iwpGraphObjects["accel"] = {}

var graphMeasures = ['xPos', 'yPos', 'xVel', 'yVel', 'xAccel', 'yAccel']

var graphXScale = d3.scaleLinear()
		.domain([-10, 1])
		.range([-100, 100]);

var graphYScale = d3.scaleLinear()
		.domain([-100, 100])
		.range([-100, 100]);

/*var graphXScale = null

var graphYScale = null

function scaleAxes(graphParameters) {
	graphXScale = d3.scaleLinear()
	    .domain([graphParameters.xmax, graphParameters.xmin])
	    .range([-100, 100]);

	graphYScale = d3.scaleLinear()
	    .domain([graphParameters.ymax, graphParameters.ymin])
	    .range([100, -100]);
}*/

var path1 = d3.path();
var path2 = d3.path();
var path3 = d3.path();

var visualPath = null;

//var xGrid = d3.axisTop(graphXScale).ticks(10).tickSize(1000);
//var yGrid = d3.axisRight(graphYScale).ticks(10).tickSize(1000);
//var xAxis = d3.axisBottom(graphXScale).ticks(10).tickSize(0);
//var yAxis = d3.axisLeft(graphYScale).ticks(10).tickSize(0);

var xGrid = null
var yGrid = null
var xAxis = null
var yAxis = null



function graphInit(s) {
	console.log("iwp6-graph59> ", s);
	var svg = d3.select('#' + s + '-graph');

	// Step 0 Add Graph Titles
	if(svg != null) {
		var title = "";
		var xShift = 150;
		if(s == "pos") {
			title = "Position vs. Time";
		}
		else if(s == "vel") {
			title = "Velocity vs. Time";
		}
		else if(s ==  "accel") {
			title = "Acceleration vs. Time";
			xShift = 140;
		}
		svg.append("text").attr("x", xShift).attr("y", -85).attr("style", "font-size : 15;").text(title);
	}

	// Step 1 Build Grid And Axes
	// console.log("iwp5-graph.js:38> Building Grid + Axes for svg: " , svg);
	if ( xGrid != null ) {
		xGrid(svg.append("g").classed("iwp-graph-grid", true).attr("transform", "translate(0, 300)"));
	}
	if ( yGrid != null ) {
		yGrid(svg.append("g").classed("iwp-graph-grid", true).attr("transform", "translate(-100,0)"));
	}
	if ( xAxis != null ) {
		xAxis(svg.append("g").classed("iwp-graph-axis",true));
		svg.append("text").attr("x", 390).attr("y", 20).attr("style", "font-size : 12;").classed("iwp-graph-axis-label", "true").text("Time (s)");
	}

	// 2019Oct25 - Render the Yaxis on the left side of the graph, always
	var yAxisCoordinate = 0;
	if ( graphWindow[s] && graphWindow[s].xmin ) {
		yAxisCoordinate = graphWindow[s].xmin;
	}
    // console.log("iwp6-graph:62> graphInit - Alignment of the YAxis: graphXScale(0) : " , graphXScale(0),  "  graphXScale(yAxisCoordinate) : " , graphXScale(yAxisCoordinate),   "  yAxisCoordinate: " , yAxisCoordinate , " graphWindow: " , graphWindow ) ;


	if ( yAxis != null ) {
		yAxis(svg.append("g").classed("iwp-graph-axis",true).attr("transform", "translate("+ graphXScale(yAxisCoordinate) +", 0)"));
	}

	

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


function queryUserFormGraphDouble(s, index) {
  var readValue = $("#" + s + "graph_" + index).val();
  var doubleValue = parseFloat(readValue);

  //console.log("iwp6-graph.js line 105 ", "#graph_" + index, doubleValue);
  
  //console.log("queryUserDefinedInput: for input: ", input, " readValue: ", readValue,  "  doubleValue: ", doubleValue );
  // TODO if readValue doesn't make sense, then default back to input.initialValue;

  return doubleValue;
}


function graphSetWindowFromAnimation(s, graphWindow) {
        updateGraph(s, graphWindow);
	//graphWindow.xmax = 1.0;
	
    	graphXScale = d3.scaleLinear()
				.domain([graphWindow[s].xmax, graphWindow[s].xmin])
				.range([500, -100]);
				// 2019Nov12 Fix to reverse Graph Y axis
	graphYScale = d3.scaleLinear()
				.domain([graphWindow[s].ymin, graphWindow[s].ymax])
				.range([100, -100]);

	var xTicks = (graphWindow[s].xmax - graphWindow[s].xmin) / graphWindow[s].xgrid
	var yTicks = (graphWindow[s].ymax - graphWindow[s].ymin) / graphWindow[s].ygrid
	console.log("iwp6-graph:140> yTicks: ", yTicks);
	xGrid = d3.axisTop(graphXScale).ticks(xTicks).tickSize(1000);
	yGrid = d3.axisRight(graphYScale).ticks(yTicks).tickSize(1000);
	xAxis = d3.axisBottom(graphXScale).ticks(xTicks).tickSize(0);
	yAxis = d3.axisRight(graphYScale).ticks(yTicks).tickSize(0);

	graphInit(s);
        //$("#graphTab").html($("#graphTab").html());
       
}

function updateGraph(s, graphWindow) {
	var gr = $("#" + s + "-graph");
	//var graphBoxAttrs = gr[0].getAttribute("viewBox").split(" ");
	//graphBox = { gminX: parseFloat(graphBoxAttrs[0]), gminY: parseFloat(graphBoxAttrs[1]), gmaxX: parseFloat(graphBoxAttrs[2]), gmaxY: parseFloat(graphBoxAttrs[3]) };
  //	console.log("graph min/max x/y vals: ", graphBoxAttrs[0], graphBoxAttrs[1], graphBoxAttrs[2], graphBoxAttrs[3]);
	
    	//console.log("iwp6-graph:98> graphSetWindow: " , graphWindow )
	//console.log("Graph window value: " , graphWindow[index]);
        //Update graph settings with user form data 
	$.each(graphWindow[s], function(index, val) {
	  graphWindow[s][index] = queryUserFormGraphDouble(s, index);
	  // console.log("Graph window value: " , graphWindow[index]);
	})

	// Clear out graph contents.
	gr.empty();

}

function graphResetZero(step, vars, solids, s, graphWindow) {
    	var svg = d3.select('#' + s + '-graph');
	// console.log("step: ", step);
	// console.log("iwp-graph:101> graphResetZero, vars: ", vars)
	// console.log("iwp-graph:102> graphResetZero, solids: ", solids)
	svg.select(".iwp-graph-object").remove();
	// 2018Oct25 Gray-out graph button if nothing to be graphed
	graphAny = false;
	for (var i = 0; i < parsedAnimation.solids().length; i++) {
		if (solids[i].shape.graphOptions.graphVisible) {
			graphAny = true;
            		break;
		}
	}
	// console.log("iwp-graph:138>", graphAny);
	if (!graphAny) {
		 document.getElementById("gt").style.color = "#bfbfbf";
		 document.getElementById("gt").onclick = null;
	}

	// Step 2 - Populate Memory for each object that's graphable, plus all of it's visibility
	$.each(parsedAnimation.solids(),function(index, solid) {


		var graphOptions = solid.shape.graphOptions
	
		var visible = graphOptions.graphVisible == true

		// 2018Oct19 Ported a piece of IWP4 logic
		if ( visible ) {

			if ( graphOptions.initiallyOn.xPos == "false" &&
				 graphOptions.initiallyOn.yPos == "false" &&
				 graphOptions.initiallyOn.xVel == "false" &&
				 graphOptions.initiallyOn.yVel == "false" &&
				 graphOptions.initiallyOn.xAccel == "false" &&
				 graphOptions.initiallyOn.yAccel == "false" ) {

				//console.log("iwp-graph:149> Because none of the graphoption are initially, on, we're hiding the object");
				visible = false
			}
		}


		if ( visible ) {
			//console.log("iwp-graph:216> graphResetZero, solid: ", solid);
			//console.log("iwp-graph:217> graphResetZero, solid graphOptions: ", graphOptions)
						
			iwpGraphObjects[s][solid.name] = { solid: solid, 
			graphOptions: graphOptions,
			color: solid.color, 
			visible: true,
			paths: {
				xPos: d3.path(),
				yPos: d3.path(),
				xVel: d3.path(),
				yVel: d3.path(),
				xAccel: d3.path(),
				yAccel: d3.path()
			},
			pathsVisible: {
				xPos: graphOptions.initiallyOn != null && graphOptions.initiallyOn.xPos === true,
				yPos: graphOptions.initiallyOn != null && graphOptions.initiallyOn.yPos === true,
				xVel: graphOptions.initiallyOn != null && graphOptions.initiallyOn.xVel === true,
				yVel: graphOptions.initiallyOn != null && graphOptions.initiallyOn.yVel === true,
				xAccel: graphOptions.initiallyOn != null && graphOptions.initiallyOn.xAccel === true,
				yAccel: graphOptions.initiallyOn != null && graphOptions.initiallyOn.yAccel === true
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
		}
	})


	// Step 3 Add each graph path to the svg and associate stroke color.
	// Hang onto the svg Memory refernces for good luck.

	$.each(iwpGraphObjects[s],function(name, graphObject) {

		//console.log("iwp5graph:256> Reset: name: ", name, "  graphObject: ", graphObject)
		//var vb = console.log("graphWindow vals", )
		var g = svg.append("g").classed("iwp-graph-object", true).attr("iwp-solid-name",name)
 	
		var stroke = "stroke: rgba("+graphObject.color.red+","+graphObject.color.green+","+graphObject.color.blue+",1);"
		var hide = "display: none;"
		if(s == 'pos') {
		graphObject.pathsSvg.xPos =
			g.append('path').attr("iwp-measure", "xPos").attr("style", stroke+(graphObject.pathsVisible.xPos ? '' : hide)).attr("d", graphObject.paths.xPos)
	
		graphObject.pathsSvg.yPos =
			g.append('path').attr("iwp-measure", "yPos").attr("style", stroke+(graphObject.pathsVisible.yPos ? '' : hide)).attr("d", graphObject.paths.yPos)
		}
		if(s == 'vel') {
		graphObject.pathsSvg.xVel =
			g.append('path').attr("iwp-measure", "xVel").attr("style", stroke+(graphObject.pathsVisible.xVel ? '' : hide)).attr("d", graphObject.paths.xVel)		
		
		graphObject.pathsSvg.yVel =
			g.append('path').attr("iwp-measure", "yVel").attr("style", stroke+(graphObject.pathsVisible.yVel ? '' : hide)).attr("d", graphObject.paths.yVel)
		}
		if(s == 'accel') {
		graphObject.pathsSvg.xAccel =
			g.append('path').attr("iwp-measure", "xAccel").attr("style", stroke+(graphObject.pathsVisible.xAccel ? '' : hide))
			.attr("d", graphObject.paths.xAccel).attr("stroke-linecap", "round")

		graphObject.pathsSvg.yAccel =
			g.append('path').attr("iwp-measure", "yAccel").attr("style", stroke+(graphObject.pathsVisible.yAccel ? '' : hide))
			.attr("d", graphObject.paths.yAccel).attr("stroke-linecap", "round")
		}
	});


	// Step 4- Dynamically add buttons for graph toggle on / off



	$(".iwp-graph-controls").html("")


	/** Build the control buttons */
	$(".iwp-graph-controls").append("<div class='iwp-graph-control-buttons'></div>")
	$.each(graphMeasures, function(i, measure) {
		$(".iwp-graph-control-buttons").append("<button onclick='graphMeasureClick(this);' iwp-measure='"+measure+"'>" +measure+"</button>")
	});


	/** Build the legend */
	$(".iwp-graph-controls").append("<div class='iwp-graph-control-legend'></div>")
	$.each(iwpGraphObjects[s],function(name, graphObject) {

		// console.log("iwp5-graph:153> add buttons for: "+ name + "   visible? " + graphObject.visible)

		if ( graphObject.visible ) {
			console.log("iwp6-graph:309> graphObject visible:", name);
			$(".iwp-graph-control-legend").append("<div iwp-solid-name="+name+"><span class='iwp-graph-legend-square'></span> &nbsp; <label>" +name+"</label></div>")
			// Add the color style
			$("div[iwp-solid-name='"+name+"'] .iwp-graph-legend-square").css("background-color", rgbColor(graphObject.color));

		}

		// Which series are intially turned on?
		$.each(graphMeasures, function(i, measure) {

			if ( graphObject.pathsVisible[measure] ) {

				// console.log("iwp5-graph:195> This is visible: graphObject: ", graphObject, "measure", measure)

				$(".iwp-graph-control-buttons button[iwp-measure='"+measure+"']").addClass("active")

			}
		});


	});
	$(".iwp-graph-controls").append("</div>")
	//graphSetWindowFromAnimation(s, graphWindow);
	// console.log("iwp5graph:94> Initialized all Graph Objects: ", iwpGraphObjects)

}


function rgbColor(o) {
	return "rgb("+o.red+","+o.green+","+o.blue+")"
}


function graphMeasureClick(button) {

	var dom = $(button);

	var solidName = dom.attr("iwp-solid-name");
	var measure = dom.attr("iwp-measure");
    	var active = dom.attr("class"); 
        
    	
	console.log("graphMeasureClick:191> I was just clicked, toggle this: " + solidName + " " + measure )
	console.log("graphMeasureClick:191> I was just clicked, my state is: " + active)
	

    // Step 1 - Make inactive and hide path if active
    if(active == "active") {
        $(".iwp-graph-control-buttons button[iwp-measure='"+measure+"']").removeClass("active")
        $("g.iwp-graph-object path[iwp-measure='"+measure+"']").hide();
    }
    // Step 2 - Make active and add graph if inactive
    else {
        $("g.iwp-graph-object path[iwp-measure='" + measure +"']").toggle();
        $(".iwp-graph-control-buttons button[iwp-measure='"+measure+"']").addClass("active")
    }


	return false; // Do not submit the form or refresh the page.
}




/**
 * Performs no calculations, but repaints every thing (time, outputs, solids) onto screen from memory at current step.
 */
function graphStepForward(step, vars, s, graphWindow) {

	var svg = d3.select('#' + s + '-graph');
	var lastStep = varsAtStep[step-1]

	//var vars = varsAtStep[step];
	if ( vars == undefined ) {
		console.log("graphStepForward:217> Warning: Vars undefined at step: " + step);
	} else {
		// console.log("iwp5-graph:221> graphStepForward: ", step)
		// console.log("iwp5-graph:223> thisStep Vars: ", vars)
		// console.log("iwp5-graph:223> lastStep Vars: ", lastStep)

		// During each loop, iterate over all the solids that are graphable, and update paths based
		// on incoming vars at step.

		// 2019Oct25 Updated Acceleration to be a higher frequency pulse.

		$.each(iwpGraphObjects[s],function(name, graphObject) {
			// console.log("iwp5graph:176> GraphStep: name: ", name, "  graphObject: ", graphObject)
			// console.log("iwp5graph:308> vars: ", vars)
			var paths = graphObject.paths
			var pathsSvg = graphObject.pathsSvg
			$.each(graphMeasures, function(i, measure) {
				var graphThisStep = false

				// console.log("iwp6-graph.js line 394: vars.t, vars.delta_t: ", vars.t, vars.delta_t);
				if(s=='pos' && (measure == 'xPos' || measure == 'yPos')) {
					graphThisStep = true
				}
				if(s=='vel' && (measure == 'xVel' || measure == 'yVel')) {
					graphThisStep = true
				}
				if(s=='accel' && (measure == 'xAccel' || measure == 'yAccel')) {
					graphThisStep = true
				}
				if ((measure == 'xVel' || measure == 'yVel' || measure == 'xAccel' || measure == 'yAccel') && vars.t < 2*vars.delta_t) {
					graphThisStep = false
				}
		
		/*	
				//create dashed line effect for velocity values
				if ((measure == 'xVel' || measure == 'yVel') && graphThisStep)  {
					var spacing = 
					var lcMeasure = measure.toLowerCase()
					var oneThirdTime = lastStep.t + (vars.t - lastStep.t) / 3
					var oneThirdMeasure = lastStep[name][lcMeasure] + (vars[name][lcMeasure] - lastStep[name][lcMeasure] ) / 3
              				paths[measure].moveTo (
                        			graphXScale(lastStep.t),
                    			        graphYScale(lastStep[name][lcMeasure])
		                        )
					

					//draw first part
					paths[measure].lineTo(
						graphXScale(oneThirdTime),
						graphYScale(oneThirdMeasure)
					)
					//create gap
					oneThirdTime += (vars.t - lastStep.t) / 3
					oneThirdMeasure += (vars[name][lcMeasure] - lastStep[name][lcMeasure]) / 3
					paths[measure].moveTo(
						graphXScale(oneThirdTime),
						graphYScale(oneThirdMeasure)
					)
					oneThirdTime += (vars.t - lastStep.t) / 3
					oneThirdMeasure += (vars[name][lcMeasure] - lastStep[name][lcMeasure]) / 3
					
					//draw second part
					paths[measure].lineTo(
						graphXScale(oneThirdTime),
						graphYScale(oneThirdMeasure)
					)
					pathsSvg[measure].attr("d", paths[measure])

				}
					
				//create dotted line effect for acceleration values
				else if (measure == 'xAccel' || measure == 'yAccel') {
				
					// Calculate the linear midpoint
					var lcMeasure = measure.toLowerCase()
					var midTime = lastStep.t + ( vars.t - lastStep.t ) / 2
					var midMeasure = lastStep[name][lcMeasure] + ( vars[name][lcMeasure] - lastStep[name][lcMeasure] ) / 2
				
                    paths[measure].moveTo (
                        graphXScale(lastStep.t),
                        graphYScale(lastStep[name][lcMeasure])
                    )

                    paths[measure].lineTo (
                        graphXScale(midTime),
                        graphYScale(midMeasure)
                    )

                    pathsSvg[measure].attr("d", paths[measure])


				} else {
*/
					if (graphThisStep) {
						var lcMeasure = measure.toLowerCase()
						paths[measure].moveTo (
							graphXScale(lastStep.t),
							graphYScale(lastStep[name][lcMeasure])
						)

						paths[measure].lineTo (
							graphXScale(vars.t),
							graphYScale(vars[name][lcMeasure])
						)

						if(pathsSvg[measure] != null) {
							pathsSvg[measure].attr("d", paths[measure])
						}
						else {
							console.log("iwp6-graph.js:495> No Paths exist for measure ", measure);
						}
						//console.log("pathsSvg[measure].getTotalLength()", paths[measure].getTotalLength());
					}

				      	
				

			});
		});

	}
}



function graphStepBackward(step, vars, s) {
	var svg = d3.select('#' + s + '-graph');

	// console.log("iwp5-graph:513> graphStepBackward: ", step)

	$.each(iwpGraphObjects[s],function(name, graphObject) {

		// paths = graphObject.paths
		// pathsSvg = graphObject.pathsSvg

		$.each(graphMeasures, function(i, measure) {

			// console.log("iwp5-graph.graphStepBackward:522> paths[" + measure + "] = ", paths[measure])


			// Rebuild the memory path from the beginning of time.
			// This is like the opening scene of the 5th element

			var lastStep = null;
			for( rebuildStep = 0 ; rebuildStep <= step; rebuildStep++ ) {
				var lcMeasure = measure.toLowerCase();
				var vars = varsAtStep[rebuildStep];


				if ( rebuildStep == 0 ) {
					// Erase everything
					graphObject.paths[measure] = d3.path()
					// Move to Zero Time, it's not necessarily the origin
					graphObject.paths[measure].moveTo (
						graphXScale(vars.t),
						graphYScale(vars[name][lcMeasure])
					)

				} else {
					var graphThisStep = false


					// console.log("iwp6-graph.js line 394: vars.t, vars.delta_t: ", vars.t, vars.delta_t);
					if(s=='pos' && (measure == 'xPos' || measure == 'yPos')) {
						graphThisStep = true
					}
					if(s=='vel' && (measure == 'xVel' || measure == 'yVel')) {
						graphThisStep = true
					}
					if(s=='accel' && (measure == 'xAccel' || measure == 'yAccel')) {
						graphThisStep = true
					}
					if ((measure == 'xVel' || measure == 'yVel' || measure == 'xAccel' || measure == 'yAccel') && vars.t < 2*vars.delta_t) {
						graphThisStep = false
					}

					// console.log("iwp6-graph:546> s: " , s,  "  measure: " , measure, "   graphThisStep: " , graphThisStep);


					if (graphThisStep) {
						// Move to a real point in time.
						graphObject.paths[measure].moveTo (
							graphXScale(lastStep.t),
							graphYScale(lastStep[name][lcMeasure])
						)

						graphObject.paths[measure].lineTo (
							graphXScale(vars.t),
							graphYScale(vars[name][lcMeasure])
						)
					}
				}

				lastStep = vars;
			}


			// Repaint screen w/ new reconstructed path
			// console.log("iwp5-graph:583> Replacing d on : ", graphObject.pathsSvg[measure] )
			if(graphObject.pathsSvg[measure] != null) {
				graphObject.pathsSvg[measure].attr("d", graphObject.paths[measure])
			}
			else {
				console.log("iwp6-graph.js:588> No Paths exist for measure ", measure);
			}
				
		});
	});



}
