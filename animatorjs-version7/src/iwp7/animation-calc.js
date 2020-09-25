"use strict";
/**
 * Interactive Web Physics 6 - Pure Javascript implementation of calculators and loaders
 * Ryan Steed, Taylor Brockman 2016 - Version 5.0 Initial Port to HTML + SVG
 * Matthew Mims, Taylor Brockman 2018  - Version 5.1 Added Graphing and D3
 * Albert Gong, Nial Mullane, Taylor Brockman 2019 - Version 6.0 Migrated into Play Framework
 * Andy Wang, Benjamin Wu, Taylor Brockmanm 2020 - Version 6.1 Modern Designer and extensive testing
 */

// Version 7
const varsConstants = require("./animation-constants");
const math = require("mathjs");


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

    // console.log("iwp6-calc:251> calculateInputAtStep input: " , input.name,  " ", input, "  step: " , step,  " vars: ", vars );

    // next load in variables for all of the inputs
    // If we're running in calculator only mode, use default values
    if ( typeof queryUserFormInputDouble === "function" ) {
        return queryUserFormInputDouble(input);
    } else {
        // console.log("iwp6-calc:442> Headless mode, setting initial value for: " + input.name + "  value: " + input.initialValue );
        return +input.initialValue;
    }

}

/** BUGBUG - TODE Change this over to have no side effects, all vars writing should happen in parent stack */
function calculateOutputAtStep(output, step, vars, verbose) {

    // console.log("iwp6-calc:267> calculateOutputAtStep: output: " , output.name , " ", output, "  step: " , step , " vars: " , JSON.stringify(vars) );

    var newValue = evaluateCalculator( output.name+".out", output.calculator, step, vars, verbose, output.name ).value;
    vars[output.name] = newValue;

    if ( typeof updateUserFormOutputDouble === 'function' ) {
        updateUserFormOutputDouble(output, newValue);
    }

    // console.log("iwp6-calc:267> calculateOutputAtStep:267> newValue: " + newValue);

    return newValue;
}


/**
 * 2020Feb21 - Floating text now stored in vars as an object
 */
function calculateFloatingTextAtStep(floatingText, step, vars, verbose) {

    // console.log("iwp6-calc:285> calculateFloatingTextAtStep: floatingText: " , floatingText.name , " ", floatingText, "  step: " , step , " vars: " , JSON.stringify(vars) );
    var newValueComplex = evaluateCalculator( floatingText.name+".value", floatingText.value.calculator, step, vars, verbose, floatingText.name );
    var xComplex = evaluateCalculator( floatingText.name+".x", floatingText.xpath.calculator, step, vars, verbose, floatingText.name );
    var yComplex = evaluateCalculator( floatingText.name+".y", floatingText.ypath.calculator, step, vars, verbose, floatingText.name );

    var ft = {
        value: newValueComplex.value,
        x: xComplex.value,
        y: yComplex.value
    };

    // console.log("iwp6-calc:296> calculateFloatingTextAtStep, xComplex: " , xComplex,  "  yComplex: " , yComplex );
    if ( typeof updateFloatingTextSvgPathAndShape === "function" ) {
        // console.log("iwp6-calc:300> Animate the floating text! New value: " , newValueComplex,  " path and shape: ", pathAndShape);
        updateFloatingTextSvgPathAndShape(floatingText, ft );
    }

    return ft;
}



/**
 * 2018Dec14 Note! This is now pass by value, manipulating vars does no good,
 * The caller is responsible for assigning back to vars
 */
function calculateSolidAtStep(solid, step, vars, verbose) {

    // console.log("iwp6-calc:233> calculateSolidAtStep step: " + step + " solid: " + JSON.stringify(solid));

    var xComplex = evaluateCalculator( solid.name+".x", solid.xpath.calculator, step, vars, verbose, solid.name )

    // console.log("iwp6-calc:233> calculateSolidAtStep xComplex: "+ JSON.stringify(xComplex));

    var x = xComplex.value

    //----------------------------------------
    /// 2016-Dec-07 attempt to alleivate circular dependency.
    var calcX = {
        x: x,
        xdisp: x,
        xpos: x,
        xvel: 0,
        xaccel: 0
    }

    // Hande Complex return types from Euler Calculator
    if ( xComplex.velocity != undefined ) {

        calcX.xvel = xComplex.velocity
    }
    if ( xComplex.acceleration != undefined ) {
        calcX.xaccel = xComplex.acceleration
    }

    vars[solid.name] = calcX;
    //----------------------------------------

    var yComplex = evaluateCalculator( solid.name+".y", solid.ypath.calculator, step, vars, verbose, solid.name )
    var y = yComplex.value

    // console.log("iwp6-calc:265> calculateSolidAtStep yComplex: "+ JSON.stringify(yComplex));

    // Handle Complex return types from Euler Calculator
    var calcY = {
        y: y,
        ydisp: y,
        ypos: y,
        yvel: 0,
        yaccel: 0
    }


    if ( yComplex.velocity != undefined ) {
        calcY.yvel = yComplex.velocity
    }
    if ( yComplex.acceleration != undefined ) {
        calcY.yaccel = yComplex.acceleration
    }

    if ( xComplex.acceleration == null ) {
        console.log("iwp6-calc:338> Recaclulating X because acceleration was null!")
        vars[solid.name] = deepExtend(calcX,calcY);
        xComplex = evaluateCalculator( solid.name+".x", solid.xpath.calculator, step, vars, verbose, solid.name )
        x = xComplex.value
    }
    if ( yComplex.acceleration == null ) {
        console.log("iwp6-calc:344> Recaclulating Y because acceleration was null!")
        vars[solid.name] = deepExtend(calcX,calcY);
        yComplex = evaluateCalculator( solid.name+".y", solid.ypath.calculator, step, vars, verbose, solid.name )
        y = yComplex.value
    }


    //-------------------------------------
    // Height and width

    var height = evaluateCalculator( solid.name+".h", solid.shape.height.calculator, step, vars, verbose, solid.name ).value
    var width = evaluateCalculator( solid.name+".w", solid.shape.width.calculator, step, vars, verbose, solid.name ).value

    // console.log("iwp5:259> Solid: " + solid.name + " height + width calculation: width: " , width, " height: ", height)

    //-------------------------------------
    var calc = {
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


    if ( solid.shape.shapeType == "polygon" ) {

        console.log("iwp5:277> Recalculating Polygon: " + solid.name + " has Error? " + solid.calculationError );
        calc["points"] = []
        solid.points.forEach( function( i, index ) {

            console.log("iwp6-calc:387> Recalculating Polygon["+i+"]: X: " + i.xpath  + " Y: " + i.ypath, "  vars: " + JSON.stringify(vars) );

            var x = 0;
            var y = 0;

            try {
                x = evaluateCalculator(solid.name+".xpt", i.xpath.calculator, step, vars, verbose, solid.name ).value;
            } catch(err) {
                console.log("iwp6-calc:394> Recalculating Polygon["+i+"]: ERROR : X: " + i.xpath  + "  vars: " + JSON.stringify(vars) , err);
            }

            try {
                y = evaluateCalculator(solid.name+".ypt", i.ypath.calculator, step, vars, verbose, solid.name ).value;
            } catch(err) {
                console.log("iwp6-calc:401> Recalculating Polygon["+i+"]: ERROR : Y: " + i.xpath  + "  vars: " + JSON.stringify(vars), err );
            }
            point = {
                x: x,
                y: y
            };
            calc["points"].push(point);
        });


        //i.xpath = evaluateCalculator(solid.name+".x"+toString(counter), i.xpath.calculator, vars).value
        //i.ypath = evaluateCalculator(solid.name+".x"+toString(counter), i.ypath.calculator, vars).value
        //console.log("i after",i)
    }


    // console.log("iwp6-calc:400> [" + step + "] Attempting to evaluate solid shape angle for solid.name: " + solid.name + "  shape.angle: " , solid.shape.angle );
    if ( solid.shape.angle ) {
        calc.angle = evaluateCalculator( solid.name+".a", solid.shape.angle.calculator, step, vars, verbose, solid.name ).value
        // console.log("iwp6-calc:401> [" + step + "] Evaluated calculator for solid.name: " + solid.name + " .angle: ", solid.shape.angle, " to :", calc.angle  )
    }

    // For objects with a value beyond x , y, w , h -- This is used for the floatingText Value
    if ( solid.value != null && solid.value.calculator != null ) {
        calc.objectValue = evaluateCalculator( solid.name+".objectValue", solid.value.calculator, step, vars, verbose, solid.name ).value
    }


    // Hande Complex return types from Euler Calculator
    if ( xComplex.velocity != undefined ) {
        calc.xvel = xComplex.velocity
    }
    if ( xComplex.acceleration != undefined ) {
        calc.xaccel = xComplex.acceleration
    }
    if ( yComplex.velocity != undefined ) {
        calc.yvel = yComplex.velocity
    }
    if ( yComplex.acceleration != undefined ) {
        calc.yaccel = yComplex.acceleration
    }

    // console.log("iwp6-calc:373> Completed Solid calculation, writing back to vars: " + solid.name + "   calc: " + JSON.stringify(calc));
    vars[solid.name] = calc

    // WARNING: updating svg display deep inside the calc route.
    // Run in headless mode
    if ( typeof updateSolidSvgPathAndShape === "function" ) {
        updateSolidSvgPathAndShape(solid, calc)
    }

    return calc;
}


// RENDERING steps:
//  updateTimeDisplay(vars.t);
//  updateUserFormOutputDouble(output, newValue);
//  updateSolidSvgPathAndShape(solid, calc)
var CONFIG_throw_solid_calculation_exceptions = false;





/**
 * 2019Apr09 Restructured logic to go by object sequence for IWP6
 */

function calculateVarsAtStep(animation, step) {

    // D-Fence
    if ( animation.compiledObjects == null || animation.compiledObjects.length <= 0 ) {
        throw "calculateVarsAtStep("+step+") Empty compiledObjects array, has animation been initialized?"
    }

    // vars should be a map of string to double, including the mathematical / physical constants.
    var vars = { step: step }
    deepExtend(vars, varsConstants);


    // optional UI
    startTime = null;
    if ( typeof queryTimeStartInputDouble === "function" ) {
        startTime = queryTimeStartInputDouble()
    } else {
        startTime = time.start
    }

    // Everything begins with time, populate t.
    vars.t = startTime + step * time.change;
    vars.tDelta = time.change
    vars.delta_t = vars.tDelta
    vars.tDel = vars.tDelta
    vars.deltaTime = time.change


    if ( typeof updateTimeDisplay === "function" ) {
        updateTimeDisplay(vars.t);
    }

    // Collect user Inputs! These are polled from the DOM every step.
    compiledObjects.forEach( function(object, index) {

        if ( object.objectType == 'input' ) {

            var newValue = calculateInputAtStep(object, step, vars, false );
            vars[object.name] = newValue;

            // console.log("iwp6-calc:521> Input name: " + object.name + "  newValue: "+ newValue );

        } else if ( object.objectType == 'output') {

            var newValue = calculateOutputAtStep(object, step, vars, true );

            if ( isNaN(newValue) ) { throw "not a number" };
            if ( !isFinite(newValue) ) { throw "not finite" };

            vars[object.name] = newValue;

        } else if ( object.objectType == 'solid') {

            var solid = object;

            // Initialize Euler's each loop if necessary
            if ( solid.xpath.calculator.calcType == "euler-mathjs" ) {
                initializeEulerCalculator(solid, step, vars, "x", solid.xpath.calculator)
            }

            if ( solid.ypath.calculator.calcType == "euler-mathjs" ) {
                initializeEulerCalculator(solid, step, vars, "y", solid.ypath.calculator)
            }

            var newValue = calculateSolidAtStep(object, step, vars, true );
            vars[solid.name] = newValue;

            // console.log("iwp6-calc:588> calculateSolidAtStep: " + object.name + "  newValue: " + newValue );

        } else if ( object.objectType == 'floatingText' ) {

            var floatingText = object;

            if ( floatingText.xpath.calculator.calcType == "euler-mathjs" ) {
                initializeEulerCalculator(floatingText, step, vars, "x", solid.xpath.calculator)
            }

            if ( floatingText.ypath.calculator.calcType == "euler-mathjs" ) {
                initializeEulerCalculator(floatingText, step, vars, "y", solid.ypath.calculator)
            }

            vars[floatingText.name] = calculateFloatingTextAtStep(floatingText, step, vars, true );

        } else if ( object.objectType == 'object') {

            throw "iwp6-calc:592> Unrecognized Object type: " + object.objectType;

        } else {
            throw "iwp6-calc:595> Unrecognized Object type: " + object.objectType;
        }

    });

    return vars;
}


function initializeEulerCalculator(solid, step, vars, axis, calculator) {

    // Initialization -- If currentDisplacement or currentVelocity is empty!
    if (step == 0 || calculator.currentDisplacement == null) {
        calculator.initialDisplacement = evaluateCompiledMath(calculator.initialDisplacementCompiled, vars)
        calculator.currentDisplacement = calculator.initialDisplacement
    }
    if (step == 0 || calculator.currentVelocity == null) {
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

function compileCalculator(iwpCalculator) {

    // console.log("iwp6-calc:1161> Attempting to compile calculator: " + JSON.stringify(iwpCalculator));

    var incomingType = iwpCalculator.calcType

    if ( incomingType == "parametric" ) {

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


    } else if ( incomingType == "euler" || incomingType == "MCalculator_Euler") {
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
        throw "iwp6-calc:1134> Unable to process compiled, not numeric and doesn't have value property: " + newValue
    }
}

// Config flag for develoeprs to enable debugging of euler acceleration calculations - have fun!
var CONFIG_throw_acceleration_calculation_exceptions = true;


/** 2018Mar22 Bugfix, use the argument 'calculateStep' instead of reaching out to global */
// ResultVariable is solidname.x or solidName.y

function evaluateCalculator( resultVariable, calculator, calculateStep, vars, verbose, objectName ) {

    if ( calculator == null ) {
        console.log("iwp6-calc:970> Null Calculator for: ", resultVariable)
        return { value: 0 }
    }
    else if ( calculator.calcType == "mathjs" )   {

        try {
            return evaluateParametricCalculator(resultVariable, calculator, calculateStep, vars, verbose, objectName);
        } catch ( err ) {
            console.log("iwp6-calc:991> Exception in evaluateParametricCalculator for " + resultVariable + ": " + err );
            console.log("iwp6-calc:992> Object Detail: Vars: " , vars )
            console.log("iwp6-calc:993> Object Detail: Calculator: " , calculator )
            return { value: 0 }
        }
    } else if ( calculator.calcType == "euler-mathjs" ) {

        try {
            return evaluateEulerCalculator(resultVariable, calculator, calculateStep, vars, verbose, objectName);
        } catch ( err ) {
            console.log("iwp6-calc:999> Exception in evaluateEulerCalculator for " + resultVariable + ": " + err );
            return { value: 0 }
        }

    }
    else {
        throw "iwp6-calc:1264> Unsupported calculator type : " + JSON.stringify(calculator)
    }
}



/**
 * 2019Jan08 For Readability, broke apart Parameteric -vs- Euler implementations
 */

function evaluateParametricCalculator( resultVariable, calculator, calculateStep, vars, verbose, objectName ) {

    try {

        var result = calculator.compiled.evaluate(vars);

        if ( !isFinite(result) ) {
            throw "iwp6-calc:1260> Compiled vars are not finite"
        }

        if ( calculator.latestValue == undefined ) {
            calculator.previousValue = result;
        } else {
            calculator.previousValue = calculator.latestValue;
        }
        calculator.latestValue = result;

        // Instaneous Velocity Calculation.
        calculator.velocity = ( calculator.latestValue - calculator.previousValue ) / vars["delta_t"]
        //console.log("evaluateCalculator:509> " + resultVariable + "> Calculated Velicoty: "  + calculator.velocity + " (previous: " + calculator.previousValue + " latest: "+ calculator.latestValue + ")")

        // Instanteous Acceleration Calcualtor
        if ( calculator.latestVelocity == undefined ) {
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
            console.log("iwp6-calc:1058> evaluateParametricCalculator ERROR: " + resultVariable + "> Exception evaluating calculator: ", err );
            console.log("iwp6-calc:1059> evaluateParametricCalculator ERROR: " + resultVariable + "> Equation: ", calculator.equation );
            console.log("iwp6-calc:1060> evaluateParametricCalculator ERROR: " + resultVariable + "> Vars: ", vars);
            console.log("iwp6-calc:1060> evaluateParametricCalculator ERROR: " + resultVariable + "> Vars.STRINGIFY: ", JSON.stringify(vars));
            console.log("iwp6-calc:1062> evaluateParametricCalculator ERROR: " + resultVariable + "> Vars: t = ", vars.t, "  t1 = " , vars['t1'] );

        }
        throw err;
    }
};




function evaluateEulerCalculator( resultVariable, calculator, calculateStep, vars, verbose, objectName ) {

    try {

        // 2019-Jan-08 Euler Self-referential, fixes for damped-1.iwp to prevent exceptions with new iwp6 javascript.



        // 2016-Sep-23 For Euler V5, store the cache of current displacement and velocity IN calculator.
        // An enhancement would be to expose these values out as complex return ttypes of this function,
        // so that they could he historically archived in the variable step array.
        //  IIRC, IWPv4, these were available as xdisp, xvel, xaccell on solids, for example.
        // Today, in IWP5, our return structure out of thi sufnction is a single double value.


        var dt = vars["delta_t"]
        var acceleration = null;

        // 2019Jan08 Taylor Addressing the Euler self-references, as exemplified in iwp-packaged / Oscillations / damped-1.iwp
        // console.log("iwp6-calc:1317> acceleration resultVariable: ", resultVariable );
        // console.log("iwp6-calc:1317> acceleration calculator: ", calculator );
        // console.log("iwp6-calc:1317> acceleration BEFORE vars: ", vars );
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
            console.log("iwp6-calc:1317> Unknown resultVariable endsWith: " , resultVariable );
        }
        // console.log("iwp6-calc:1346> acceleration AFTER vars: ", vars );




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



        if ( calculateStep == 0 ) {
            // For good measure, recalculate initials just in case other dependencies have changed.
            calculator.initialDisplacement = evaluateCompiledMath(calculator.initialDisplacementCompiled, vars)
            calculator.initialVelocity = evaluateCompiledMath(calculator.initialVelocityCompiled, vars)

            calculator.currentVelocity = calculator.initialVelocity;
            calculator.currentDisplacement = calculator.initialDisplacement; // no adjustment

            //console.log("iwp6-calc:1143> Recalculating Euler Calc Step 0, calculator: " , calculator);
            //var breaker=1;

        } else if ( changeStep > 0 ) {

            if ( acceleration != null ) {
                // Positive direction calcuation
                if ( calculateStep == 1 ) {
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
            acceleration: acceleration }

        // return displacement.value;
    } catch ( err ) {
        if ( verbose ) {
            console.log("evaluateCalculator:375> Unable to evaluate calculator: ", err, calculator.equation, dt);
        }
        throw err;
    }
};


module.exports = {
    evaluateCalculator: evaluateCalculator,
    compileCalculator: compileCalculator,
    calculateVarsAtStep: calculateVarsAtStep
};


