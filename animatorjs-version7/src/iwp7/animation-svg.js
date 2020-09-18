"use strict";

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

// BOOK
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

module.exports.xWidth = xWidth;
module.exports.setAnimationWindow = setAnimationWindow;

true;

