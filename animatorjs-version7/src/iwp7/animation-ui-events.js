let animationIllustrate = require('animation-illustrate');
let $ = require('jquery');

function handleStartClick() {
    handleGoClick();
};

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
};

function handleForwardClick() {
    clearInterval(stepTrigger);
    stepForwardAndPause();
    document.getElementById(buttonIds.startStop).setAttribute("onclick", "handleGoClick()");
    $("#startStopIcon").attr("class", "fa fa-play fa-lg");
};

function handleInputChange() {
    $("*").change( function () {
        handleResetClick();
    });
}

