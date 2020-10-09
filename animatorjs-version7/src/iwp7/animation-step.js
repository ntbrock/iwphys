const animationCalc = require("./animation-calc");
const deepExtend = require('./deepExtend');


// What is a step? An interation that is a multiple of t delta.   Current T =   T0 + step * Tdelta
// Set of the variables in their state at that point.
// The inputs are replicated into each of the steps.
// The object for the step is exactly what is passed into the equation calculation.
// Need to support complex variables in the equation like ball.x


// IWP6 - Nashorn accessibility with some javascript function overloading.
function varsAtStepJson(animation, step) {
    if ( ( typeof step === "string" && step != "" ) ||
        ( typeof step === "number" && step >= 0 ) ) {
        return JSON.stringify(animation.varsAtStep[+step]);
    } else {
        return JSON.stringify(animation.varsAtStep);
    }
}


function resetSteps(animation) {
    animation.step.currentStep = 0;
    animation.step.changeStep = 0;
    animation.step.varsAtStep = [];

    // We also need to clear out previous parameteric displacements used for instant velecioty calculations.
    animation.loop.forEach(function ( object, index) {
        if(object.objectType === 'solid') {
            resetSolidCalculators(object);
        }
    });

    const vars0 = animationCalc.calculateVarsAtStep(animation, 0);

    // 2018Feb01 Graphing Reset hookin
    // 2020Oct09 TODO How to reconnect graphing?
    /*
    if ( typeof graphResetZero === "function" ) {
        graphResetZero(0, vars = vars0, solids = parsedAnimation.solids(), "pos");
        graphResetZero(0, vars = vars0, solids = parsedAnimation.solids(), "vel");
        graphResetZero(0, vars = vars0, solids = parsedAnimation.solids(), "accel");

    }
*/

    archiveVarsAtStep( animation, animation.step.currentStep, vars0 ); // Boot up the environment

    return vars0;
}



// Global Number Formatting routine.
function printDecimal( incomingNumber, incomingPlaces ) {
    // console.log("iwp5:83> printDecimal: ", incomingNumber, " places: " , incomingPlaces );
    return parseFloat(Math.round(incomingNumber * Math.pow(10,incomingPlaces)) / Math.pow(10,incomingPlaces)).toFixed(incomingPlaces);
}

function setStepDirection(animation, newDirection) {
    animation.step.changeStep = +newDirection;
}

function stepForwardAndPause(animation) {
    setStepDirection(animation,1);
    handleStep(animation);
    setStepDirection(animation,0);
}

function stepBackwardAndPause(animation) {
    setStepDirection(animation,-1);
    handleStep(animation);
    setStepDirection(animation,0);
}


// forward and back stops animation


/**
 * Major function - called every time the animation time changes
 */
function handleStep(animation) {

    // console.log("iwp6-calc:173> HandleStep: current: " , currentStep, "  change: ", changeStep );

    // apply the time change.
    let newStep = animation.step.currentStep + animation.step.changeStep;

    // handle time horizons
    if ( newStep < 0 ) {
        animation.step.changeStep = 0;
        newStep = 0;
    }

    // If we are beyond stop time don't do tanything.
    /*
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
    }*/

    //console.log("handleStep:61> newStep: " + newStep)

    if ( newStep !== animation.step.currentStep ) {

        // Back button poerformance - Let's look at the historical array of variables, if we have it, reload
        // to avoid doing a recalcaultion

        let vars = animation.varsAtStep[newStep];
        if ( vars != undefined ) {
            //console.log("[iwp5.js:118] Step " + newStep + " already exist, reloading for history.")
            /*
            // TODO - Hook into repaint at a higher level
            if ( typeof repaintStep === "function") {
                repaintStep(newStep);
            }
*/

        } else {
            // UI rendering is handled by the calculate as a side effect
            vars = animationCalc.calculateVarsAtStep(animation, newStep);
            archiveVarsAtStep( animation, newStep, vars );
        }

        // iwp5.1 - Adding Hook into the graph capabilties
/*
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

*/
    }

    animation.step.currentStep = newStep;

    return newStep;
}


function archiveVarsAtStep( animation, step, vars ) {
    // console.log("Archving vars at step: " + step );
    animation.varsAtStep[step] = deepExtend(animation.varsAtStep[step], vars);
}



/**
 * 2016-Nov-09 - Reset the instantanous velcity calculations on reset
 * 2018Mar23 - Erase all internal state, including intialVelocities inside Euler.
 */
function resetSolidCalculators(solid) {

    // console.log("iwp5:753> resetSolidCalculators: " , solid )

    if ( solid.xpath && solid.xpath.calculator ) {
        solid.xpath.calculator.latestValue = undefined;
        solid.xpath.calculator.currentVelocity = undefined;
        solid.xpath.calculator.initialVelocity = undefined;
        solid.xpath.calculator.currentDisplacement = undefined;
        solid.xpath.calculator.initialDisplacement = undefined;
    }
    if ( solid.ypath && solid.ypath.calculator ) {
        solid.ypath.calculator.latestValue = undefined;
        solid.ypath.calculator.currentVelocity = undefined;
        solid.ypath.calculator.initialVelocity = undefined;
        solid.ypath.calculator.currentDisplacement = undefined;
        solid.ypath.calculator.initialDisplacement = undefined;
    }

    /** IWP5 is not yet using velocity on height + width calcs */
    /*
    if ( solid.xpath && solid.xpath.calculator ) {
        solid.width.calculator.latestValue = null;
    }
    if ( solid.ypath && solid.ypath.calculator ) {
        solid.height.calculator.latestValue = null;
    }
    */
    // BUGBUG - Not concerned about polypath reset yet.
}




module.exports = {
    varsAtStepJson : varsAtStepJson,
    resetSteps : resetSteps,
    setStepDirection : setStepDirection,
    stepForwardAndPause : stepForwardAndPause,
    stepBackwardAndPause : stepBackwardAndPause,
    handleStep : handleStep, // Major function - called every time the animation time changes
    archiveVarsAtStep : archiveVarsAtStep
};

