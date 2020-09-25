

const animationCalc = require("./animation-calc");

// What is a step? An interation that is a multiple of t delta.   Current T =   T0 + step * Tdelta
// Set of the variables in their state at that point.
// The inputs are replicated into each of the steps.
// The object for the step is exactly what is passed into the equation calculation.
// Need to support complex variables in the equation like ball.x


// IWP6 - Nashorn accessibility with some javascript function overloading.
function varsAtStepJson(step) {
    if ( ( typeof step === "string" && step != "" ) ||
        ( typeof step === "number" && step >= 0 ) ) {
        return JSON.stringify(varsAtStep[+step]);
    } else {
        return JSON.stringify(varsAtStep);
    }
}




var varsAtStep = [];
var currentStep = 0;
var changeStep = 0; 	// -1 = backwards, 0 = stopped, 1 = forwards.

function masterResetSteps() {
    currentStep = 0;
    changeStep = 0;
    varsAtStep = [];


    // We also need to clear out previous parameteric displacements used for instant velecioty calculations.
    compiledObjects.forEach(function ( object, index) {
        if(object.objectType=='solid') {
            resetSolidCalculators(object);
        }
    });


    var vars0 = animationCalc.calculateVarsAtStep(0);

    // 2018Feb01 Graphing Reset hookin
    if ( typeof graphResetZero === "function" ) {
        graphResetZero(0, vars = vars0, solids = parsedAnimation.solids(), "pos");
        graphResetZero(0, vars = vars0, solids = parsedAnimation.solids(), "vel");
        graphResetZero(0, vars = vars0, solids = parsedAnimation.solids(), "accel");

    }

    archiveVarsAtStep( currentStep, vars0 ); // Boot up the environment

    return vars0;
}



// Global Number Formatting routine.
function printDecimal( incomingNumber, incomingPlaces ) {
    // console.log("iwp5:83> printDecimal: ", incomingNumber, " places: " , incomingPlaces );
    return parseFloat(Math.round(incomingNumber * Math.pow(10,incomingPlaces)) / Math.pow(10,incomingPlaces)).toFixed(incomingPlaces);
}

function setStepDirection(newDirection) {
    changeStep = +newDirection;
}

function stepForwardAndPause(animation) {
    setStepDirection(1);
    handleStep(animation);
    setStepDirection(0);
}

function stepBackwardAndPause(animation) {
    setStepDirection(-1);
    handleStep(animation);
    setStepDirection(0);
}


// forward and back stops animation


/**
 * Major function - called every time the animation time changes
 */
function handleStep(animation) {

    // console.log("iwp6-calc:173> HandleStep: current: " , currentStep, "  change: ", changeStep );

    // apply the time change.
    let newStep = currentStep + changeStep;

    // handle time horizons
    if ( newStep < 0 ) {
        changeStep = 0;
        newStep = 0;
    }

    // If we are beyond stop time don't do tanything.
    if ( typeof queryTimeStopInputDouble === 'function' && typeof queryTimeStepInputDouble === 'function' ) {
        // Animation Mode
        if (newStep > ( Math.round( queryTimeStopInputDouble() / queryTimeStepInputDouble()))) {

            if ( typeof handleStopClick === 'function' ) {
                handleStopClick();
            }
            // console.log("iwp6-calc:191> Animated end of time on step: " + newStep )
            return -1;
        }
    } else {
        // Headless mode
        if (newStep > ( Math.round( +animation.time.stop / +animation.time.change ))) {
            if ( typeof handleStopClick === 'function' ) {
                handleStopClick();
            }
            //console.log("iwp6-calc:191> Animated end of time on step: " + newStep )
            return -1;
        }
    }

    //console.log("handleStep:61> newStep: " + newStep)

    if ( newStep != currentStep ) {

        // Back button poerformance - Let's look at the historical array of variables, if we have it, reload
        // to avoid doing a recalcaultion

        var vars = varsAtStep[newStep];
        if ( vars != undefined ) {
            //console.log("[iwp5.js:118] Step " + newStep + " already exist, reloading for history.")
            if ( typeof repaintStep === "function") {
                repaintStep(newStep);
            }

        } else {
            // UI rendering is handled by the calculate as a side effect
            vars = animationCalc.calculateVarsAtStep(newStep);
            archiveVarsAtStep( newStep, vars );
        }

        // iwp5.1 - Adding Hook into the graph capabilties
        if ( changeStep > 0 ) {
            if ( typeof graphStepForward === "function" ) {
                graphStepForward(newStep, vars, "pos");
                graphStepForward(newStep, vars, "vel");
                graphStepForward(newStep, vars, "accel");

            }
        } else if ( changeStep < 0 ) {
            if ( typeof graphStepBackward === "function" ) {
                graphStepBackward(newStep, vars, "pos");
                graphStepBackward(newStep, vars, "vel");
                graphStepBackward(newStep, vars, "accel");


            }
        }
    }

    currentStep = newStep;

    return newStep;
}


function archiveVarsAtStep( step, vars ) {
    // console.log("Archving vars at step: " + step );
    varsAtStep[step] = {};
    deepExtend(varsAtStep[step], vars)
}

module.exports = {
    varsAtStepJson : varsAtStepJson,
    masterResetSteps : masterResetSteps,
    setStepDirection : setStepDirection,
    stepForwardAndPause : stepForwardAndPause,
    stepBackwardAndPause : stepBackwardAndPause,
    handleStep : handleStep, // Major function - called every time the animation time changes
    archiveVarsAtStep : archiveVarsAtStep
}