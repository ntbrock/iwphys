"use strict";
/**
 * Interactive Web Physics 6 - Pure Javascript implementation of calculators and loaders
 * Ryan Steed, Taylor Brockman 2016 - Version 5.0 Initial Port to HTML + SVG
 * Matthew Mims, Taylor Brockman 2018  - Version 5.1 Added Graphing and D3
 * Albert Gong, Nial Mullane, Taylor Brockman 2019 - Version 6.0 Migrated into Play Framework
 * Andy Wang, Benjamin Wu, Taylor Brockmanm 2020 - Version 6.1 Modern Designer and extensive testing
 */


// TODO Connect RENDERING steps:
//  updateTimeDisplay(vars.t);
//  updateUserFormOutputDouble(output, newValue);
//  updateSolidSvgPathAndShape(solid, calc)


// Version 7
const varsConstants = require("./animation-constants");
const math = require("mathjs");
const deepExtend = require('./deepExtend');

// ------------------------------------------------
// Sugar from https://www.n-k.de/riding-the-nashorn/
// Because iwp6-calc can execute in server side nashorn, I have to redeclare console there.

/*
// Version 7 now runs in node.js , not Nashorn
if ( typeof console === "undefined" ) {  // Prevent console redefinition
    var console = {};
    console.debug = print;
    console.log = print;
    console.warn = print;
    console.error = print;
}
*/

//-----------------------------------------------------------------------
// Pseudocode for Mathematical Animation

// Calculate Initial Variable Set from inputs

// Calculate at initial step to render displays

// Calculate for each step as time ticks.

// Provide a historical variable set at each tick (for trails, graphing).

// 2018Mar01 Found that monkeypatching mathjs was non-reliable so now including in the vars scope.
// Converts from degrees to radians. this makes the 'toRadians(..)' function available in the Calculator space.
// Likely the are are other physical constants that we'll need to load as well.



function calculateInputAtStep(input, step, vars, verbose ) {

    // console.log("animation-calc:56> calculateInputAtStep input: " , input.name,  " ", input, "  step: " , step,  " vars: ", vars );

    // next load in variables for all of the inputs
    // If we're running in calculator only mode, use default values
    /**
     * TODO Connect the User Inputs!
    if ( typeof queryUserFormInputDouble === "function" ) {
        return queryUserFormInputDouble(input);
    } else {
        // console.log("animation-calc:65> Headless mode, setting initial value for: " + input.name + "  value: " + input.initialValue );
        return +input.initialValue;
    }
     */
    return +input.initialValue;

}

/** BUGBUG - TODE Change this over to have no side effects, all vars writing should happen in parent stack */
function calculateOutputAtStep(output, step, vars, verbose) {

    // console.log("animation-calc:76> calculateOutputAtStep: output: " , output.name , " ", output, "  step: " , step , " vars: " , JSON.stringify(vars) );

    const newValue = evaluateCalculator( output.name+".out", output.calculator, step, vars, verbose, output.name ).value;
    vars[output.name] = newValue;

    if ( typeof updateUserFormOutputDouble === 'function' ) {
        updateUserFormOutputDouble(output, newValue);
    }

    // console.log("animation-calc:85> calculateOutputAtStep:267> newValue: " + newValue);

    return newValue;
}


/**
 * 2020Feb21 - Floating text now stored in vars as an object
 */
function calculateFloatingTextAtStep(floatingText, step, vars, verbose) {

    // console.log("animation-calc:96> calculateFloatingTextAtStep: floatingText: " , floatingText.name , " ", floatingText, "  step: " , step , " vars: " , JSON.stringify(vars) );
    const newValueComplex = evaluateCalculator( floatingText.name+".value", floatingText.value.calculator, step, vars, verbose, floatingText.name );
    const xComplex = evaluateCalculator( floatingText.name+".x", floatingText.xpath.calculator, step, vars, verbose, floatingText.name );
    const yComplex = evaluateCalculator( floatingText.name+".y", floatingText.ypath.calculator, step, vars, verbose, floatingText.name );

    const ft = {
        value: newValueComplex.value,
        x: xComplex.value,
        y: yComplex.value
    };

    // TODO this needs an illustration event hook!
    /*
    // console.log("animation-calc:109> calculateFloatingTextAtStep, xComplex: " , xComplex,  "  yComplex: " , yComplex );
    if ( typeof updateFloatingTextSvgPathAndShape === "function" ) {
        // console.log("animation-calc:111> Animate the floating text! New value: " , newValueComplex,  " path and shape: ", pathAndShape);
        updateFloatingTextSvgPathAndShape(floatingText, ft );
    }
       */
    return ft;
}



/**
 * 2018Dec14 Note! This is now pass by value, manipulating vars does no good,
 * The caller is responsible for assigning back to vars
 */
function calculateSolidAtStep(solid, step, vars, verbose) {

    // console.log("animation-calc:126> calculateSolidAtStep step: " + step + " solid: " + JSON.stringify(solid));

    let xComplex = evaluateCalculator( solid.name+".x", solid.xpath.calculator, step, vars, verbose, solid.name )
    let x = xComplex.value

    //----------------------------------------
    /// 2016-Dec-07 attempt to alleivate circular dependency.
    const calcX = {
        x: x,
        xdisp: x,
        xpos: x,
        xvel: 0,
        xaccel: 0
    }

    // Hande Complex return types from Euler Calculator
    if ( xComplex.velocity !== undefined ) {
        calcX.xvel = xComplex.velocity
    }
    if ( xComplex.acceleration !== undefined ) {
        calcX.xaccel = xComplex.acceleration
    }

    vars[solid.name] = calcX;
    //----------------------------------------

    let yComplex = evaluateCalculator( solid.name+".y", solid.ypath.calculator, step, vars, verbose, solid.name )
    let y = yComplex.value

    // console.log("animation-calc:155> calculateSolidAtStep yComplex: "+ JSON.stringify(yComplex));

    // Handle Complex return types from Euler Calculator
    const calcY = {
        y: y,
        ydisp: y,
        ypos: y,
        yvel: 0,
        yaccel: 0
    }


    if ( yComplex.velocity !== undefined ) {
        calcY.yvel = yComplex.velocity
    }
    if ( yComplex.acceleration !== undefined ) {
        calcY.yaccel = yComplex.acceleration
    }

    if ( xComplex.acceleration == null ) {
        console.log("animation-calc:175> Recaclulating X because acceleration was null!")
        vars[solid.name] = deepExtend(calcX,calcY);
        xComplex = evaluateCalculator( solid.name+".x", solid.xpath.calculator, step, vars, verbose, solid.name )
        x = xComplex.value
    }
    if ( yComplex.acceleration == null ) {
        console.log("animation-calc:181> Recaclulating Y because acceleration was null!")
        vars[solid.name] = deepExtend(calcX,calcY);
        yComplex = evaluateCalculator( solid.name+".y", solid.ypath.calculator, step, vars, verbose, solid.name )
        y = yComplex.value
    }


    //-------------------------------------
    // Height and width

    const height = evaluateCalculator( solid.name+".h", solid.shape.height.calculator, step, vars, verbose, solid.name ).value
    const width = evaluateCalculator( solid.name+".w", solid.shape.width.calculator, step, vars, verbose, solid.name ).value

    // console.log("iwp5:259> Solid: " + solid.name + " height + width calculation: width: " , width, " height: ", height)

    //-------------------------------------
    const calc = {
        x: x,
        xdisp: x,
        xpos: x,
        xvel: 0,
        xaccel: 0,
        y: y,
        ydisp: y,
        ypos: y,
        yvel: 0,
        yaccel: 0,
        height: height,
        width: width,
        angle: 0
    }


    if ( solid.shape.shapeType === "polygon" ) {

        console.log("iwp5:277> Recalculating Polygon: " + solid.name + " has Error? " + solid );
        calc["points"] = []
        solid.points.forEach( function( i, index ) {

            console.log("animation-calc:220> Recalculating Polygon["+i+"]: X: " + i.xpath  + " Y: " + i.ypath, "  vars: " + JSON.stringify(vars) );

            var x = 0;
            var y = 0;

            try {
                x = evaluateCalculator(solid.name+".xpt", i.xpath.calculator, step, vars, verbose, solid.name ).value;
            } catch(err) {
                console.log("animation-calc:228> Recalculating Polygon["+i+"]: ERROR : X: " + i.xpath  + "  vars: " + JSON.stringify(vars) , err);
            }

            try {
                y = evaluateCalculator(solid.name+".ypt", i.ypath.calculator, step, vars, verbose, solid.name ).value;
            } catch(err) {
                console.log("animation-calc:234> Recalculating Polygon["+i+"]: ERROR : Y: " + i.xpath  + "  vars: " + JSON.stringify(vars), err );
            }
            const point = {
                x: x,
                y: y
            };
            calc["points"].push(point);
        });


        //i.xpath = evaluateCalculator(solid.name+".x"+toString(counter), i.xpath.calculator, vars).value
        //i.ypath = evaluateCalculator(solid.name+".x"+toString(counter), i.ypath.calculator, vars).value
        //console.log("i after",i)
    }


    // console.log("animation-calc:250> [" + step + "] Attempting to evaluate solid shape angle for solid.name: " + solid.name + "  shape.angle: " , solid.shape.angle );
    if ( solid.shape.angle ) {
        calc.angle = evaluateCalculator( solid.name+".a", solid.shape.angle.calculator, step, vars, verbose, solid.name ).value
        // console.log("animation-calc:253> [" + step + "] Evaluated calculator for solid.name: " + solid.name + " .angle: ", solid.shape.angle, " to :", calc.angle  )
    }

    // For objects with a value beyond x , y, w , h -- This is used for the floatingText Value
    if ( solid.value != null && solid.value.calculator != null ) {
        calc.objectValue = evaluateCalculator( solid.name+".objectValue", solid.value.calculator, step, vars, verbose, solid.name ).value
    }


    // Hande Complex return types from Euler Calculator
    if ( xComplex.velocity !== undefined ) {
        calc.xvel = xComplex.velocity
    }
    if ( xComplex.acceleration !== undefined ) {
        calc.xaccel = xComplex.acceleration
    }
    if ( yComplex.velocity !== undefined ) {
        calc.yvel = yComplex.velocity
    }
    if ( yComplex.acceleration !== undefined ) {
        calc.yaccel = yComplex.acceleration
    }

    // console.log("animation-calc:276> Completed Solid calculation, writing back to vars: " + solid.name + "   calc: " + JSON.stringify(calc));
    vars[solid.name] = calc

    // WARNING: updating svg display deep inside the calc route.
    // Run in headless mode
    // TODO - Need to figure out eventing to UI
    /*
    if ( typeof updateSolidSvgPathAndShape === "function" ) {
        updateSolidSvgPathAndShape(solid, calc)
    }
*/

    return calc;
}


/**
 * 2019Apr09 Restructured logic to go by object sequence for IWP6
 */

function calculateVarsAtStep(animation, step) {

    const breaker296=true
    // D-Fence
    if ( ! animation.compiled ) {
        throw "calculateVarsAtStep(" + step + ") Animation is not compiled"
    }
    if ( ! Array.isArray(animation.loop) ) {
        throw "calculateVarsAtStep(" + step + ") Animation.loop is not an Array"
    }

    // vars should be a map of string to double, including the mathematical / physical constants.
    let vars = deepExtend({ step: step }, varsConstants);


    // Everything begins with time, populate t.
    vars.t = animation.time.start + step * animation.time.change;
    vars.tDelta = animation.time.change
    vars.delta_t = vars.tDelta
    vars.tDel = vars.tDelta
    vars.deltaTime = animation.time.change


    // Collect user Inputs! These are polled from the DOM every step.
    animation.loop.forEach( function(object, index) {

        if ( object.objectType === 'input' ) {

            vars[object.name] = calculateInputAtStep(object, step, vars, false );
            // console.log("animation-calc:325> Input name: " + object.name + "  newValue: "+ newValue );

        } else if ( object.objectType === 'output') {

            const newValue = calculateOutputAtStep(object, step, vars, true );

            if ( isNaN(newValue) ) { throw "not a number" }
            if ( !isFinite(newValue) ) { throw "not finite" }

            vars[object.name] = newValue;

        } else if ( object.objectType === 'solid') {

            const solid = object; // alias

            // Initialize Euler's each loop if necessary
            if ( solid.xpath.calculator.calcType === "euler-mathjs" ) {
                initializeEulerCalculator(solid, step, vars, "x", solid.xpath.calculator)
            }

            if ( solid.ypath.calculator.calcType === "euler-mathjs" ) {
                initializeEulerCalculator(solid, step, vars, "y", solid.ypath.calculator)
            }

            vars[solid.name] = calculateSolidAtStep(object, step, vars, true );

            // console.log("animation-calc:351> calculateSolidAtStep: " + object.name + "  newValue: " + newValue );

        } else if ( object.objectType === 'floatingText' ) {

            const floatingText = object;

            if ( floatingText.xpath.calculator.calcType === "euler-mathjs" ) {
                initializeEulerCalculator(floatingText, step, vars, "x", solid.xpath.calculator);
            }

            if ( floatingText.ypath.calculator.calcType === "euler-mathjs" ) {
                initializeEulerCalculator(floatingText, step, vars, "y", solid.ypath.calculator);
            }

            vars[floatingText.name] = calculateFloatingTextAtStep(floatingText, step, vars, true );


        } else if ( object.objectType === 'object') {

            throw "animation-calc:370> Unrecognized Object type: " + object.objectType;

        } else {
            throw "animation-calc:373> Unrecognized Object type: " + object.objectType;
        }

    });

    return vars;
}


function initializeEulerCalculator(solid, step, vars, axis, calculator) {

    // Initialization -- If currentDisplacement or currentVelocity is empty!
    if (step === 0 || calculator.currentDisplacement == null) {
        calculator.initialDisplacement = evaluateCompiledMath(calculator.initialDisplacementCompiled, vars)
        calculator.currentDisplacement = calculator.initialDisplacement
    }
    if (step === 0 || calculator.currentVelocity == null) {
        calculator.initialVelocity = evaluateCompiledMath(calculator.initialVelocityCompiled, vars)
        calculator.currentVelocity = calculator.initialVelocity;
    }
    if ( typeof vars[solid.name] === "undefined" ) {
        vars[solid.name] = {}
    }
    vars[solid.name][axis] = calculator.currentDisplacement
    vars[solid.name][axis+"pos"] = calculator.currentDisplacement
    vars[solid.name][axis+"disp"] = calculator.currentDisplacement
    vars[solid.name][axis+"vel"] = calculator.currentVelocity
}



//-----------------------------------------------------------------------
// Calculation Section

//TODO - More descriptive name for this argument - remember it json that we're expecting!
function compileCalculator(iwpCalculator) {

    // console.log("animation-calc:1161> Attempting to compile calculator: " + JSON.stringify(iwpCalculator));

    const breaker421=true

    const incomingType = iwpCalculator.calcType

    if ( incomingType === "parametric" ) {

        /*
        <calculator type="parametric">
            <value>ar.value*cos((kr.value/mr.value)^.5*t+toRadians(pr.value)*1)</value>
        </calculator>
        */

        var e = migrateLegacyEquation(iwpCalculator.value);

        var c = {
            calcType: "mathjs",
            compiled: math.compile( e ),
            equation: e
        }

        // console.log("iwp5:760> Compiled: ", iwpCalculator.value, " compiledTo: ", c)
        return c;


    } else if ( incomingType === "euler" || incomingType === "MCalculator_Euler") {
        /*
        <calculator type="euler">
          <displacement>initxdisp</displacement>
          <velocity>initxvel</velocity>
          <acceleration>-5*t</acceleration>
        </calculator>
        */

        var a = migrateLegacyEquation(iwpCalculator.acceleration);
        var v = migrateLegacyEquation(iwpCalculator.velocity);
        var d = migrateLegacyEquation(iwpCalculator.displacement);

        var c =  {
            calcType: "euler-mathjs",
            initialDisplacementCompiled: math.compile( d ),
            initialVelocityCompiled: math.compile( v ),
            accelerationCompiled: math.compile( a ),
            equation: {
                acceleration : a,
                velocity : v,
                displacement : d
            }
        }
        return c;


    }
    else {
        console.log("DEBUG ERROR: Only parametric and Euler supported in the August 2016 version, unable to handle: ", incomingType);
        return {calcType:"unsupported", "incomingType": incomingType, "iwpCalculator": iwpCalculator };
    }
}

function migrateLegacyEquation(toMigrate) {
    return toMigrate.replace(/[.]value/g,"")
}

/**
 * 2016-Sep-23 - Sometimes single variable equations will evalate as object.value, not a numeric
 * Remind Taylor about this if you have questions.
 */

function evaluateCompiledMath( compiled, vars ) {

    var newValue = compiled.evaluate(vars)
    if ( typeof newValue === "number" ) {
        return newValue;
    } else if ( typeof newValue["value"] === "number" ) {
        return newValue.value;
    } else {
        throw "animation-calc:487> Unable to process compiled, not numeric and doesn't have value property: " + newValue
    }
}

// Config flag for develoeprs to enable debugging of euler acceleration calculations - have fun!
var CONFIG_throw_acceleration_calculation_exceptions = true;


/** 2018Mar22 Bugfix, use the argument 'calculateStep' instead of reaching out to global */
// ResultVariable is solidname.x or solidName.y

function evaluateCalculator( resultVariable, calculator, calculateStep, changeStep, vars, verbose, objectName ) {

    if ( calculator == null ) {
        console.log("animation-calc:499> Null Calculator for: ", resultVariable)
        return { value: 0 }
    }
    else if ( calculator.calcType === "mathjs" )   {

        try {
            return evaluateParametricCalculator(resultVariable, calculator, calculateStep, changeStep, vars, verbose, objectName);
        } catch ( err ) {
            console.log("animation-calc:507> Exception in evaluateParametricCalculator for " + resultVariable + ": " + err );
            console.log("animation-calc:508> Object Detail: Vars: " , vars )
            console.log("animation-calc:509> Object Detail: Calculator: " , calculator )
            return { value: 0 }
        }
    } else if ( calculator.calcType === "euler-mathjs" ) {

        try {
            return evaluateEulerCalculator(resultVariable, calculator, calculateStep, changeStep, vars, verbose, objectName);
        } catch ( err ) {
            console.log("animation-calc:517> Exception in evaluateEulerCalculator for " + resultVariable + ": " + err );
            return { value: 0 }
        }

    }
    else {
        throw "animation-calc:523> Unsupported calculator type : " + JSON.stringify(calculator)
    }
}



/**
 * 2019Jan08 For Readability, broke apart Parameteric -vs- Euler implementations
 *
 * TODO - resultVariable + objectName are only used for debug, consider moving to end of arg list, not used.
 */

function evaluateParametricCalculator( resultVariable, calculator, calculateStep, changeStep, vars, verbose, objectName ) {

    try {

        const result = calculator.compiled.evaluate(vars);

        if ( !isFinite(result) ) {
            throw "animation-calc:544> Compiled vars are not finite"
        }

        if ( calculator.latestValue === undefined ) {
            calculator.previousValue = result;
        } else {
            calculator.previousValue = calculator.latestValue;
        }
        calculator.latestValue = result;

        // Instaneous Velocity Calculation.
        calculator.velocity = ( calculator.latestValue - calculator.previousValue ) / vars["delta_t"]
        //console.log("evaluateCalculator:509> " + resultVariable + "> Calculated Velicoty: "  + calculator.velocity + " (previous: " + calculator.previousValue + " latest: "+ calculator.latestValue + ")")

        // Instanteous Acceleration Calcualtor
        if ( calculator.latestVelocity === undefined ) {
            calculator.previousVelocity = calculator.velocity
        } else {
            calculator.previousVelocity = calculator.latestVelocity;
        }

        calculator.latestVelocity = calculator.velocity;
        calculator.acceleration = ( calculator.latestVelocity - calculator.previousVelocity ) / vars["delta_t"]


        return { value: result,
            displacement: result,
            velocity: calculator.velocity,
            acceleration: calculator.acceleration };
    }
    catch ( err ) {
        if ( verbose ) {
            console.log("animation-calc:576> evaluateParametricCalculator ERROR: " + resultVariable + "> Exception evaluating calculator: ", err );
            console.log("animation-calc:577> evaluateParametricCalculator ERROR: " + resultVariable + "> Equation: ", calculator.equation );
            console.log("animation-calc:578> evaluateParametricCalculator ERROR: " + resultVariable + "> Vars: ", vars);
            console.log("animation-calc:579> evaluateParametricCalculator ERROR: " + resultVariable + "> Vars.STRINGIFY: ", JSON.stringify(vars));
            console.log("animation-calc:580> evaluateParametricCalculator ERROR: " + resultVariable + "> Vars: t = ", vars.t, "  t1 = " , vars['t1'] );

        }
        throw err;
    }
};




function evaluateEulerCalculator( resultVariable, calculator, calculateStep, changeStep, vars, verbose, objectName ) {

    try {

        // 2019-Jan-08 Euler Self-referential, fixes for damped-1.iwp to prevent exceptions with new iwp6 javascript.



        // 2016-Sep-23 For Euler V5, store the cache of current displacement and velocity IN calculator.
        // An enhancement would be to expose these values out as complex return ttypes of this function,
        // so that they could he historically archived in the variable step array.
        //  IIRC, IWPv4, these were available as xdisp, xvel, xaccell on solids, for example.
        // Today, in IWP5, our return structure out of thi sufnction is a single double value.


        const dt = vars["delta_t"]

        if ( ! dt ) {
            throw "No variable 'delta_t' at step: " + calculateStep + " in vars: " + JSON.stringify(vars);
        }

        let acceleration = null;

        // 2019Jan08 Taylor Addressing the Euler self-references, as exemplified in iwp-packaged / Oscillations / damped-1.iwp
        // console.log("animation-calc:614> acceleration resultVariable: ", resultVariable );
        // console.log("animation-calc:615> acceleration calculator: ", calculator );
        // console.log("animation-calc:616> acceleration BEFORE vars: ", vars );
        // Enable the acceleration equations to self-reference our own ypos/yvel

        if ( resultVariable.endsWith(".y") ) {

            if ( typeof vars[ objectName ] !== "object" ) { vars[objectName] = {} }
            vars[ objectName ][ "ypos" ] = calculator.currentDisplacement
            vars[ objectName ][ "ydisp" ] = calculator.currentDisplacement
            vars[ objectName ][ "yvel" ] = calculator.currentVelocity

        } else if ( resultVariable.endsWith(".x") ) {

            if ( typeof vars[ objectName ] !== "object" ) { vars[objectName] = {} }
            vars[ objectName ][ "xpos" ] = calculator.currentDisplacement
            vars[ objectName ][ "xdisp" ] = calculator.currentDisplacement
            vars[ objectName ][ "xvel" ] = calculator.currentVelocity

        } else {
            console.log("animation-calc:634> Unknown resultVariable endsWith: " , resultVariable );
        }
        // console.log("animation-calc:636> acceleration AFTER vars: ", vars );




        try {
            // then calculate the acceleration
            // 2018Mar22 Fix to only apply the acceleration time component to the change in velocity.

            // console.log("iwp5:1088> Calculating acceleration via calculator: ", calculator, " at calcStep: " + calculateStep + " for vars: ", vars )
            acceleration = calculator.accelerationCompiled.evaluate(vars);

            if ( !isFinite(acceleration) ) {
                throw "Calculator.accelerationCompiled result is not finite, is: " + acceleration
            }
            if ( isNaN(acceleration) ) {
                throw "Calculator.accelerationCompiled result is NaN"
            }

        } catch ( err ) {
            console.log("evaluateCalculator:1095> ERROR " + resultVariable + "> Unable to evaluate acceleration, setting to 0.  Calculator: ", err, calculator.equation, "Vars: ", JSON.stringify(vars) );
            if ( CONFIG_throw_acceleration_calculation_exceptions ) {
                throw err;
            }
        }



        if ( calculateStep === 0 ) {
            // For good measure, recalculate initials just in case other dependencies have changed.
            calculator.initialDisplacement = evaluateCompiledMath(calculator.initialDisplacementCompiled, vars)
            calculator.initialVelocity = evaluateCompiledMath(calculator.initialVelocityCompiled, vars)

            calculator.currentVelocity = calculator.initialVelocity;
            calculator.currentDisplacement = calculator.initialDisplacement; // no adjustment

            //console.log("animation-calc:672> Recalculating Euler Calc Step 0, calculator: " , calculator);
            //var breaker=1;

        } else if ( changeStep > 0 ) {

            if ( acceleration != null ) {
                // Positive direction calcuation
                if ( calculateStep === 1 ) {
                    // Special first frame consideration
                    calculator.currentVelocity += acceleration * dt * 0.5;
                } else {
                    calculator.currentVelocity += acceleration * dt;
                }
                calculator.currentDisplacement += calculator.currentVelocity * dt;
            }

        } else if ( changeStep < 0 ) {
            // Negative direction calculation
            if ( acceleration != null ) {
                calculator.currentVelocity -= acceleration * dt;
                calculator.currentDisplacement -= calculator.currentVelocity * dt;
            }
        } else {
            // No step direction
        }


        // console.log("iwp5:1152> evaluateCalculator calculateStep: "+ calculateStep + "  changeStep: " + changeStep + " calculator.currentVelocity: " + calculator.currentVelocity + " calculator.currentDisplacement: " + calculator.currentDisplacement )
        //Return only value if just an output?

        return { step: calculateStep,
            value: calculator.currentDisplacement,
            displacement: calculator.currentDisplacement,
            velocity: calculator.currentVelocity,
            acceleration: acceleration };

        // return displacement.value;
    } catch ( err ) {
        if ( verbose ) {
            console.log("evaluateCalculator:375> Unable to evaluate calculator: ", err, calculator.equation, dt);
        }
        throw err;
    }
}


module.exports = {
    evaluateCalculator: evaluateCalculator,
    compileCalculator: compileCalculator,
    calculateVarsAtStep: calculateVarsAtStep
};


