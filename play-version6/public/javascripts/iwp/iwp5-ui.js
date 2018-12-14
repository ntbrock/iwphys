/** 
 * Interactive Web Physics 5 - Javascript SVG Animation Implementation
 * Ryan Steed, Taylor Brockman 2016 - Version 5.0 Initial Port to HTML + SVG
 * Matthew Mims, Taylor Brockman 2018  - Version 5.1 Added Graphing and D3
 */


//Hide and reveal table tabs.


function animationTabOn() {
  $("#animationTab").show();
  $("#it").attr("class", "bottomBorder");
  timeTabOff();
  iwindowTabOff();
  graphTabOff();
  fitText("#animationTab");
};
function animationTabOff() {
  $("#animationTab").hide();
  $("#it").attr("class", "");
}

function graphTabOn() {
  $("#graphTab").show();
  $("#gt").attr("class", "bottomBorder");
  animationTabOff();
  timeTabOff();
  iwindowTabOff();
  fitText("#graphTab");
};
function graphTabOff() {
  $("#graphTab").hide();
  $("#gt").attr("class", "");
}

function iwindowTabOn() {
  $("#iwindowTab").show();
  $("#ws").attr("class", "bottomBorder");
  animationTabOff();
  timeTabOff();
  graphTabOff();
  fitText("#iwindowTab");
};
function iwindowTabOff() {
  $("#iwindowTab").hide();
  $("#ws").attr("class", "");
}


function timeTabOn() {
  $("#timeTab").show();
  $("#oib").attr("class", "bottomBorder");
  iwindowTabOff();
  animationTabOff();
  graphTabOff();
  fitText("#timeTab");
};
function timeTabOff() {
  $("#timeTab").hide();
  $("#oib").attr("class", "");
}
