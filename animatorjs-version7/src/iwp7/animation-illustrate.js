"use strict";
// Tasking:
// TODO: Replace all dom selector string rrefs with values form teh selectors object (#canvas is done!)
// BUGBUG - For now, animationWindow is a global in this module, TODO would be refactor to pass by argument


//-------------------------------------------------------------
// Binding - These are all the Id's available in the site/HTML
const selectors = {
    canvas: "#canvas",
    canvasDiv: "#canvasDiv",
    gridlines: "#gridlines",
    iwpSaveScreenshot: "#iwp-save-screenshot",
    time: "#time",
    backButton: "#backButton",
    startStopButton: "#startStopButton",
    forwardButton: "#forwardButton",
    resetButton: "#resetButton"
}


//--------------------------------------------------------------------------------
// SVG!
// Blitting / Double buffering approach
// redraw a single time!
// http://stackoverflow.com/questions/3642035/jquerys-append-not-working-with-svg-element
function redrawSvgDom($) {
    $(selectors.canvasDiv).html($(selectors.canvasDiv).html());
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



// Example of migrating away from the Global object, passing window as parameter.
function illustrateCanvasGridlines($, window ) {

    const canvas = $(selectors.canvas)
    const gridLines = $(selectors.gridlines)


    // Parse viewbox attributes from canvas to override defaults.
    const canvasBoxAttrs = canvas[0].getAttribute("viewBox").split(" ");
    canvasBox = { minX: parseFloat(canvasBoxAttrs[0]), minY: parseFloat(canvasBoxAttrs[1]), maxX: parseFloat(canvasBoxAttrs[2]), maxY: parseFloat(canvasBoxAttrs[3]) };
    // To Render the window is that we start at the Xmin, and draw full vertial lines,
    // increment by xgrid,
    // stopping at xmax
    // Add X gridlines -- TODO CONVERT TO A FOR LOOP

    //Update window settings with user form data
    // TODO - Need to query the UI for window ranges
    /*$.each(window, function(index, val) {
        window[index] = queryUserFormWindowDouble(index);
    })
    $(".gridline").remove();
    */

    const xGridlines = (window.xmax - window.xmin)/window.xgrid;
    for ( let interval = 1; interval < xGridlines; interval ++ ) {
        const xGridPosition = (interval - xGridlines/2)*window.xgrid;
        //console.log("whatItShouldBe: "+xCanvas(xGridPosition*window.xgrid)+", coordinates: "+coordinates);
        gridLines.append( "<path class='gridline' d='M " + xCanvasGridlines(xGridPosition) + " 0 V 1000' stroke='lightgray' fill='transparent'/>" )
        gridLines.append( "<path class='gridline' d='M " + xCanvas(0) + " 0 V 1000' stroke='black' fill='transparent'/>" )
    }

    // Add Y gridlines
    const yGridlines = (window.ymax - window.ymin)/window.ygrid;
    for ( let interval = 1; interval <= yGridlines-1; interval ++ ) {
        const yGridPosition = (interval - yGridlines/2)*window.ygrid;
        gridLines.append( "<path class='gridline' d='M 0 " + yCanvasGridlines(yGridPosition) + " H 1000' stroke='lightgray' fill='transparent'/>" )
        gridLines.append( "<path class='gridline' d='M 0 " + yCanvas(0) + " H 1000' stroke='black' fill='transparent'/>" )
    }
}



/* the proportional entry point in from window.xmin -> window.xmax needs to be interpolated into the
// propotional exit point between viewbox.minX -> viewbox.maxX
*/

// BUGBUG - For now, animationWindow is a global in this module, TODO would be refactor to pass by argument
let animationWindow; // For now, make it available in side this one module
function setAnimationWindow(aw) {
    animationWindow = aw;
}


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
/*
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

*/


//---------------------------------------------------------------------
// Inbound Illustrator function from animation-parsing

function illustrateAnimation(animation) {

    const originalLoopOrder = animation.loop;

    // Helper Functions that run filters.
    originalLoopOrder.forEach( function(object, index) {
        if ( object.objectType == 'input' ) {
            illustrateInput(compileInput(object));
        } else if ( object.objectType == 'output' ) {
            illustrateOutput(compileOutput(object));
        } else if ( object.objectType == 'solid' ) {
            illustrateSolid(compileSolid(object));
        } else if ( object.objectType == 'floatingText' ) {
            illustrateFloatingText(compileFloatingText(object));
            /* 2020Feb21 Dropping Generic Object Support
                } else if ( object.objectType == 'object' ) {
                    // addObject(rawAnimation.objects.object);
            */
        } else {
            throw "Animation parseAnimationToMemory unrecognized Object Type: " + object.objectType;
        }
    } );

}

function illustrateInput($, input) {
    var style = "";
    if ( input.hidden == "1" ) {
        style = "display:none;"
    }

    // 2018Mar01 Fix for empty unit labels
    var unitLabel = "";
    if ( typeof input.units ==="string" ) { unitLabel = input.units; }

    const html =
        "<tr id='input_" + input.name + "' style='" + style + "' class='iwp-input-row'><td class='iwp-input-label'>"+ input.text +"</td><td class='iwp-input-value'><input style='width:60px;' id='" + input.name + "' type='text' value='" + input.initialValue + "'> " + unitLabel + "</td></tr>"

    // Now write directly into the dom!
    $("#inputTable").append(html);

}


function illustrateOutput($, output) {
    // { "name": "axr", "text": "Acceleration", "units": "m/ss", "calculator": { attributesProperty: { "type": "parametric" }, "value": "Red.xaccel" } }
    var style = ""
    if ( output.hidden == "1" ) {
        style = "display:none;'"
    }
    var unitLabelOutput = "";
    if ( typeof output.units === "string" ) { unitLabelOutput = output.units; }

    const html = "<tr style='" + style +"vertical-align:top;' id='output_" + output.name + "' class='iwp-output-row'><td class='iwp-output-label'>"+ output.text +"</td><td class='iwp-output-value'><input id='" + output.name + "' type='text' value='-999' disabled style='width:80px;'> " + unitLabelOutput + "</td></tr>";
    $("#outputTable").append(html);

}


function illustrateFloatingText($,object) {
    const canvas = $(selectors.canvas)

    // Calculators haven't been calcualted yet, so we just place the text on origin at 0,0 and it's moved with first redraw.
    let xOrigin = xCanvas(0);
    let yOrigin = yCanvas(0);

    canvas.append( "<text id='text_" +object.name+ "' x='" + xOrigin + "' y='"+ yOrigin +"' font-size='"+(parseFloat(object.fontSize)+15)+"' fill='rgb(" +object.color.red+ "," +object.color.green+ "," +object.color.blue+ ")'>"+object.text+"</text>" );
};



/**
 * 2020Feb21 - Illustrate Functions build out the SVG elements, typically with
 * placeholder widths, heights that are updated on the first repaint.
 * Colors and other styling properties work well here.
 */
function illustrateSolid($, solid) {

    const canvas = $(selectors.canvas)

    let xOrigin = xCanvas(0);
    let yOrigin = yCanvas(0);

    // $("svg#canvas.iwp-animation-canvas")     save html to var, then link.append(html)

    //HTML
    if (solid.shape.shapeType === "circle") {            //Searched up what svgSolids.push did, basically added solid to array that was then appended to $("#canvas")
        // console.log("iwp6-calc:858> it's a circle: ", solid.shape.width );
        // Initialization Fix, put to the origin, this is updated later
        canvas.append( "<ellipse id='solid_" +solid.name+ "' cx='500' cy='500' rx=" +xWidth(0)+ " ry=" +yHeight(0)+ " fill='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")'> " );
        redrawSvgDom($);
    }
    else if (solid.shape.shapeType === "rectangle") {
        //console.log("it's a rectangle");
        canvas.append( "<rect id='solid_" +solid.name+ "' width='" +30+ "' height='" +30+ "' fill='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")'> " );
    }
    else if (solid.shape.shapeType === "line") {
        // console.log("iwp6-calc:858> It's a line, solid.shape: " , solid.shape);
        // Initialization Fix, put into the origin
        canvas.append("<line id='solid_" +solid.name+ "' x1='"+xOrigin+"' x2='"+xOrigin+"' y1='"+yOrigin+"' y2='"+yOrigin+"' stroke='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")' stroke-width='2'>");
    }
    else if (solid.shape.shapeType === "vector") {
        canvas.append("<polyline id='solid_" +solid.name+ "' points='' stroke='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")' stroke-width='2' fill='none'>");
    }
    else if (solid.shape.shapeType === "polygon") {
        //console.log("it's a polygon:", solid.name);
        canvas.append("<polyline id='solid_" +solid.name+ "' points='' stroke='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")' stroke-width='2' fill='rgb("+solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+")'>");
    }
    else if (solid.shape.shapeType === "Bitmap"||solid.shape.shapeType === "bitmap") {
        //svgSolids.push("<image  x='0' y='0' width='' height='' src='"+solid.fileUri+"'><title>"+solid.name+"</title></image>");

        console.log("iwp6-calc:848> Bitmap support, shape.file= " , solid.shape )

        // 2018Mar01 Brockman - Refactoring the bitmap code here.
        // https://stackoverflow.com/questions/10261731/can-not-add-image-inside-svg-via-jquery-image-tag-becomes-img

        let id = "solid_"+solid.name;

        let img = document.createElementNS('http://www.w3.org/2000/svg','image');
        img.setAttributeNS(null,'id',id)
        img.setAttributeNS('http://www.w3.org/1999/xlink','href','/assets'+solid.shape.file.image);
        img.setAttributeNS(null, 'visibility', 'visible');

        canvas.append(img);
    }



    else {

        console.log("iwp5:821> ERROR: Unrecognized Solid Shape Type: ", solid.shape.shapeType)
        return;
    }

    /** 2019Apr30 Initial Trail is very empty, but filled in with each animation step in iwp6-animator.js */
    if (solid.shape.drawTrails === true ) {
        canvas.append("<polyline id='solid_" +solid.name+ "_trail' points='0,0 0,0' stroke='rgb(" +solid.color.red+ "," +solid.color.green+ "," +solid.color.blue+ ")' stroke-width='1' fill='none'></polyline>");
    }
}



// Taylor Migrated this out of Animation Parsing
function renderGraphWindowRanges(animation) {

    const graphWindow = animation.graphWindow

    if ( typeof $ === "function" ) {

        $("#" + s + "graph_xmin").val( graphWindow[s].xmin );
        $("#" + s + "graph_xmax").val( graphWindow[s].xmax );
        $("#" + s + "graph_xgrid").val( graphWindow[s].xgrid );
        $("#" + s + "graph_ymax").val( graphWindow[s].ymax );
        $("#" + s + "graph_ymin").val( graphWindow[s].ymin );
        $("#" + s + "graph_ygrid").val( graphWindow[s].ygrid );
    }
}


function illustrateAnimation($, animation ) {

    setAnimationWindow( animation.window )
    illustrateCanvasGridlines($, animation.window );

    // TODO : Illustrate Animation Name

    animation.loop.forEach(function(obj) {
        if ( obj === undefined || obj.objectType === undefined ) {
            throw Error("Illustrate: undefined object or objectType in parent animation: " + animation );

        } else if ( obj.objectType === "solid" ) {
            illustrateSolid($, obj);
        } else if ( obj.objectType === "input") {
            illustrateInput($, obj);
        } else if ( obj.objectType === "output") {
            illustrateOutput($, obj);
        } else {
            throw Error("Illustrate: unknown object type to illustrate: " + obj.objectType)
        }
        console.log("animation-illustrator:374> To Draw : " , obj );
    });

    // Finally, redraw the DOM only one time at end.
    redrawSvgDom($);
}


module.exports = {
    xWidth : xWidth,
    setAnimationWindow : setAnimationWindow,
    illustrateInput : illustrateInput,
    illustrateOutput : illustrateOutput,
    illustrateSolid : illustrateSolid,
    illustrateFloatingText : illustrateFloatingText,
    illustrateCanvasGridlines: illustrateCanvasGridlines,
    illustrateAnimation: illustrateAnimation
}
