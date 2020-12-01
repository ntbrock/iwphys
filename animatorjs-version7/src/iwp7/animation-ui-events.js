
let animationIllustrate = require('animation-illustrate');
let animationPlayer = require('animation-player')

let $ = require('jquery');


function bindAllClickHandlers( window ) {

    // replacement for <div onclick="handleBackClick()" id="backButton">
    // Player Controls
    $("#backButton").on("click", handleBackClick() );
    $("#startStopButton").on("click", handleStartClick() );
    $("#forwardButton").on("click", handleForwardClick() );
    $("#resetButton").on("click", handleResetClick() );

    // Tabbed Interface Controls
    $("#animationTabButton").on("click", animationTabOn() );
    $("#graphTabButton").on("click", graphTabOn() );
    $("#timeTabButton").on("click", timeTabOn() );
    $("#windowTabButton").on("click", windowTabOn() );

}


var buttonIds  = { startStop: "startStopButton", back: "backButton", forward: "forwardButton", reset: "resetButton" }
var stepTrigger;

//Restarts motion.
function handleGoClick() {
    stepTrigger = setInterval("handleStep()", 1 / time.fps * 1000);

    setStepDirection(1);

    document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleStopClick()");
    $("#startStopIcon").attr("class", "fa fa-pause fa-lg");
}
//Stops motion.
function handleStopClick() {
//Stop move and time functions.
    clearInterval(stepTrigger);
    setStepDirection(0);
    document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleGoClick()");
    $("#startStopIcon").attr("class", "fa fa-play fa-lg");
};
//Resets simulation.
function handleResetClick() {
    renderCanvas();
    graphSetWindowFromAnimation("pos", graphWindow);
    graphSetWindowFromAnimation("vel", graphWindow);
    graphSetWindowFromAnimation("accel", graphWindow);
    updateTimeDisplay(0);
    handleStopClick();
    var vars0 = masterResetSteps();
    graphResetZero(0, vars = vars0, solids = parsedAnimation.solids(), "pos", graphWindow );
    graphResetZero(0, vars = vars0, solids = parsedAnimation.solids(), "vel", graphWindow );
    graphResetZero(0, vars = vars0, solids = parsedAnimation.solids(), "accel", graphWindow );

    //document.getElementById(buttonIds.startStop).setAttribute("class", "Start");
    document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleStartClick()");
}


function handleBackClick() {
    clearInterval(stepTrigger);
    stepBackwardAndPause();
    document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleGoClick()");
    $("#startStopIcon").attr("class", "fa fa-play fa-lg");
}

function handleForwardClick() {
    clearInterval(stepTrigger);
    stepForwardAndPause();
    document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleGoClick()");
    $("#startStopIcon").attr("class", "fa fa-play fa-lg");
}

function handleInputChange() {
    $("*").change( function () {
        handleResetClick();
    });
}

function handleStartClick() {
    handleGoClick();
}



module.exports = {
    bindAllClickHandlers : bindAllClickHandlers
}
