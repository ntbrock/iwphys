"use strict";

const deepExtend = require('./deepExtend');
const animationCalc = require('./animation-calc');

/**
 * Interactive Web Physics 7
 * 2020Sep18 Brockman
 *
 * Dynamic Animation Ordering, ported from iwp6-order.js

 * 2019Sep06 New Functions for Object Reordering, port from IWP4.5
 * 2019Sep06 Testing Harness for Port of object odering
 */

let animationOrder = require('./animation-order');


//-----------------------------------------------------------------------
// Memory Intialization + Globals Section

var parsedAnimation = null;

var time = {};
var description = "";
var animationWindow = {};
var graphWindow = {};
graphWindow["pos"] = {};
graphWindow["vel"] = {};
graphWindow["accel"] = {};

// IWP6 - Objects are now in a master loop, not separated by type.
var compiledObjects = [];

// User Interface Components, TODO Refactor our
var htmlInputs = [];
var htmlOutputs = [];
var svgSolids = [];
var svgObjects = [];



function compileInput(input) {
    input.objectType = 'input'


    // console.log("iwp6-calc:807> pushingInput: ", JSON.stringify(input) );

    return input;
    // {name: "ar", text: "Amplitude", initialValue: "9.0", units: "m"}
    // 07 Oct 2016 Honoring hidden flag
}


function compileOutput(output) {
    //console.log("addOutput ", output );

    var compiledOutput = {
        objectType: 'output',
        name: output.name,
        text: output.text,
        units: output.units,
        calculator: animationCalc.compileCalculator(output.calculator),
        hidden: output.hidden //Hidden flag still needed - be careful about cutting off attributes here.
    }

    return compiledOutput;
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


function compileSolid(solid) {
    //console.log("solid: ", solid );
    // 2020Jan31 Debug
    if ( solid.shape.shapeType == "polygon") {
        console.log("iwp6-calc:734> addSolid: ", JSON.stringify(solid));
    }
    // In Memory - PreParse Equations with math.js

    var compiledSolid = {
        objectType: 'solid',
        name: solid.name,
        color: {
            red: parseFloat(solid.color.red),
            green: parseFloat(solid.color.green),
            blue: parseFloat(solid.color.blue),
        },
        shape: {
            shapeType: solid.shape.shapeType,
            points: solid.shape.points, // 2020Jan31 Re-plumbing V4 features
            file: solid.shape.file, // 2020Jan31 Re-plumbing V4 features
            drawTrails: solid.shape.drawTrails,
            drawVectors: solid.shape.drawVectors,
            graphOptions:
                deepExtend( solid.shape.graphOptions,
                    { initiallyOn: solid.shape.graphOptions.initiallyOn } ),
            width: {
                calculator: animationCalc.compileCalculator(solid.shape.width.calculator)
            },
            height: {
                calculator: animationCalc.compileCalculator(solid.shape.height.calculator)
            }
        },
        xpath: {
            calculator : animationCalc.compileCalculator(solid.xpath.calculator)
        },
        ypath: {
            calculator : animationCalc.compileCalculator(solid.ypath.calculator)
        }
    };


    // If the animation iwp solid has a polygon shape, need to iterate over an initialize each of the calcualtors.
    // hard to do as part of the initialization because it is a dynamic list.
    // Add points here..?
    if ( compiledSolid.shape.shapeType == "polygon" ) {
        compiledSolid["points"] = []

        if ( ! solid.shape.points ) {
            console.log("iwp6-calc:766: The solid's polygon shape had no points! solid: ", solid);
        } else {
            // 2020Jan31 - Mapped to new structure w/o intermediate point
            solid.shape.points.forEach ( function( i, index ) {
                var point = {
                    xpath: {calculator: animationCalc.compileCalculator(i.xpath.calculator),},
                    ypath: {calculator: animationCalc.compileCalculator(i.ypath.calculator),},
                }
                compiledSolid.points.push(point)
            });
        }
        // console.log("iwp5:834> Compiled polygon: ",compiledSolid)
    }

    if ( compiledSolid.shape.shapeType == "Bitmap") {
        // console.log("iwp5:649> Solid is a Bitmap type, building angle: " , solid.shape.angle )
        compiledSolid.fileUri = "../../images/"+solid.shape.file.image.split("/images/")[1]
        // console.log("fileUri:",compiledSolid.fileUri)
    }

    // 2019Jan18 Promoted Angle processing to a more common location
    if ( typeof solid.shape.angle !== "undefined" ) {
        compiledSolid.shape.angle = {calculator: animationCalc.compileCalculator(solid.shape.angle.calculator)}
    }



    return compiledSolid;

}


// 2020Feb07 Added formal support for floating text
function compileFloatingText(object) {

    // console.log("compileFloatingText:904> object: " , object);

    return {
        objectType: 'floatingText',
        name: object.name,
        shape: {
            shapeType: object.class,
            height: 1,
            width: 1
        },
        text: object.text,
        units: object.units,
        value: {
            calculator: animationCalc.compileCalculator(object.value)
        },
        fontSize: object.fontSize,
        showValue: ( object.showValue === true || false ),
        color: {
            red: parseFloat(object.color.red),
            green: parseFloat(object.color.green),
            blue: parseFloat(object.color.blue),
        },
        xpath: {
            calculator : animationCalc.compileCalculator(object.xpath.calculator)
        },
        ypath: {
            calculator : animationCalc.compileCalculator(object.ypath.calculator)
        }
    }

}


function addObject(object) {

    //2018Oct12 - Detect the WaveBox
    if ( object.objectType == "edu.ncssm.iwp.objects.grapher.DObject_Grapher" ) {
        alert("This Animation Contains a GraphBox, Not yet implented in IWP5");
        return;
    }

    var compiledObject = {
        objectType: 'object',
        name: object.name,
        shape: {
            shapeType: object.class,
            height: 1,
            width: 1
        },
        text: object.text,
        units: object.units,
        showValue: ( object.showValue === true || false ),
        value: {
            calculator: animationCalc.compileCalculator(object.value.calculator)
        },
        fontSize: object.fontSize,
        xpath: {
            calculator : animationCalc.compileCalculator(object.xpath.calculator)
        },
        ypath: {
            calculator : animationCalc.compileCalculator(object.ypath.calculator)
        }
    }
    compiledObjects.push( compiledObject );


}


//-----------------------------------------------------------------------
// Parsing Section

// "time": { "start": "0.0", "stop": "5.0", "change": "0.02",  "fps": "25.0" },
function setTime(inTime) {
    //console.log("time :", inTime);
    time = {
        start : parseFloat(inTime.start),
        stop : parseFloat(inTime.stop),
        change : parseFloat(inTime.change),
        fps : parseFloat(inTime.fps)
    };
}

// "description": { "text": "A ball is attached to a horizontal spring (not shown) which causes the ball to oscillate about the origin. Run the animation until it stops. Click on Show graph. \n\nWhich graph represents position vs. time?  How do you know?\nWhich graph represents velocity vs. time?  How do you know?\nWhich graph represents acceleration vs. time?  How do you know?\n\nWhat would a graph of net force on the ball vs. time look like?  Why?"
// No Real Operation
function setDescription(inDescription) {
    //console.log("description :", inDescription);
    // Global
    description = inDescription;
}

// "window": { "xmin": "-10.0", "xmax": "10.0", "ymin": "-10.0", "ymax": "10.0", "xgrid": "2.0", "ygrid": "1.0", "xunit": "meters", "yunit": "meters"
// No Real Operation
function setWindow(inWindow) {
    //console.log("window :", inWindow);
    // Global
    animationWindow = inWindow;
}

function initializeGraphVars(s, inGraphWindow) {
    graphWindow[s] = inGraphWindow;
    // 2020Feb21 Safety Check for Nashorn mode where Jquery doesn't exisst
    if ( typeof $ === "function" ) {

        $("#" + s + "graph_xmin").val( graphWindow[s].xmin );
        $("#" + s + "graph_xmax").val( graphWindow[s].xmax );
        $("#" + s + "graph_xgrid").val( graphWindow[s].xgrid );
        $("#" + s + "graph_ymax").val( graphWindow[s].ymax );
        $("#" + s + "graph_ymin").val( graphWindow[s].ymin );
        $("#" + s + "graph_ygrid").val( graphWindow[s].ygrid );
    }
}

// "GraphWindow": { "xmin": "0.0", "xmax": "5.0", "ymin": "-50.0", "ymax": "50.0", "xgrid": "0.5", "ygrid": "10.0"
function setGraphWindow(inGraphWindow) {
    // console.log("iwp5:553> graphWindow :", inGraphWindow);
    // Global
    graphWindow["pos"] = inGraphWindow;
    graphWindow["vel"] = inGraphWindow;
    graphWindow["accel"] = inGraphWindow;
    // Hook into new iwp5-graph to redraw axes.
    if ( typeof graphSetWindowFromAnimation === "function" ) {
        graphSetWindowFromAnimation("pos", graphWindow);
        graphSetWindowFromAnimation("vel", graphWindow);
        graphSetWindowFromAnimation("accel", graphWindow);

    } else {
        console.log("iwp6-calc:637> Did not detect the Graphing Library available, graphZetWindowFromAnimation: ", typeof graphSetWindowFromAnimation );
    }

}


/**
 * Important entry point!
 *
 * 2018Dec14 Converted to pure Js, the $.xxType interface is different than typeof, requires Array.isArray
 *
 * After the animation parse, outer layer must call :  masterResetSteps()  or   calculateVarsAtStep(currentStep = 0);
 * The new iwp6-read does this.
 */

function parseAnimationToMemory( rawAnimation ) {

    // console.log("iwp6-calc:1222> parseAnimationToMemory Parsing rawAnimation: ", rawAnimation);

    let animation = { loop: [] };

    rawAnimation.objects.forEach( function( object, index ) {
        // console.log("iwp6-calc:1451> parseAnimationToMemory> Iterator: " + JSON.stringify(object) );

        if ( object.objectType == "time" ) {
            animation.time = object;
        } else if ( object.objectType == "graphWindow" ) {
            animation.graphWindow = object;
        }  else if ( object.objectType == "window" ) {
            animation.window = object;
        } else if ( object.objectType == "description" ) {
            animation.description = object;

        } else if ( object.objectType == "input" ||
            object.objectType == "output" ||
            object.objectType == "solid"  ||
            object.objectType == "floatingText" ||
            object.objectType == "object" ) {

            animation.loop.push(object);

        } else {
            throw "Calculation parseAnimationToMemory unrecognized Object Type: " + object.objectType;
        }

    });


    // D-fence

    if ( typeof animation !== "object" ) {
        throw "Animation Parameter was not an object, was: " + typeof animation
    } else if ( typeof animation.loop !== "object" ) {
        throw "Animation loop attribute was not an array, was: " + typeof animation.loop
    } else if ( typeof animation.time !== "object" ) {
        throw "Animation objects.time attribute was not an object, was: " + typeof animation.time
    } else if ( typeof animation.description !== "object" ) {
        throw "Animation objects.description attribute was not an object, was: " + typeof animation.description
    } else if ( typeof animation.graphWindow !== "object" ) {
        throw "Animation graphWindow attribute was not an object, was: " + typeof animation.graphWindow
    } else if ( typeof animation.window !== "object" ) {
        throw "Animation window attribute was not an object, was: " + typeof animation.window
    }

    // Time
    setTime(animation.time);

    // Description
    setDescription(animation.description);

    // Window
    setWindow(animation.window);

    // GraphWindow

    // console.log("iwp6-calc:1238> Setting GraphWindow: " , animation.graphWindow)
    // console.log("Initializing Graph Vars");
    initializeGraphVars("pos", animation.graphWindow);
    initializeGraphVars("vel", animation.graphWindow);
    initializeGraphVars("accel", animation.graphWindow);
    setGraphWindow(animation.graphWindow);


    animation.author = rawAnimation.author;
    if ( typeof setAuthorName === "function" ) {
        setAuthorName(animation.author.username);
    }

    // console.log("iwp6-calc:1350> Typeof input: " , typeof rawAnimation.objects.input)

    // 2019Sep06 Reordering of the Animatin Objects based on IWP3 Logic Port.
    //console.log("iwp6-calc:1288> Executing animationObject Reordering on CompiledObjects");
    var originalLoopOrder = animation.loop;
    animation.loop = animationOrder.reorderAnimationObjectsBySymbolicDependency(animation.loop);

    animation.loop.forEach( function( object, index ) {

        if ( object.objectType == 'input' ) {
            compiledObjects.push(compileInput(object));
        } else if ( object.objectType == 'output' ) {
            compiledObjects.push(compileOutput(object));
        } else if ( object.objectType == 'solid' ) {
            compiledObjects.push(compileSolid(object));
        } else if ( object.objectType == 'floatingText' ) {
            compiledObjects.push(compileFloatingText(object));
            /* 2020Feb21 Dropping Generic Object Support
                } else if ( object.objectType == 'object' ) {
                    addObject(rawAnimation.objects.object);
            */
        } else {
            throw "Animation parseAnimationToMemory unrecognized Object Type: " + object.objectType;
        }
    } );


    // 2019Apr09 store in global singleton
    parsedAnimation = animation;
    decorateAnimationFunctions();

    return parsedAnimation;
}


/**
 * Decrorate some helper functions onto the animation 'object'
 */
function decorateAnimationFunctions() {

    if ( ! parsedAnimation ) { throw "Refusing to proceed with decoration since parsedAnimation is null" }

    parsedAnimation.solids = function() {
        var out = [];
        parsedAnimation.loop.forEach( function( object, index ) {
            if ( object.objectType == 'solid' ) { out.push(object); }
        });
        return out;
    };

    /**
     2020Feb21 Disabled Generic Objects*/
    /*
      parsedAnimation.objects = function() {
          var out = [];
          parsedAnimation.loop.forEach( function( object, index ) {
              if ( object.objectType == 'objects' ) { out.push(object); }
          });
          return out;
      };
    */

    parsedAnimation.inputs = function() {
        var out = [];
        parsedAnimation.loop.forEach( function( object, index ) {
            if ( object.objectType == 'input' ) { out.push(object); }
        });
        return out;
    };

    parsedAnimation.outputs = function() {
        var out = [];
        parsedAnimation.loop.forEach( function( object, index ) {
            if ( object.objectType == 'output' ) { out.push(object); }
        });
        return out;
    };

    parsedAnimation.floatingTexts = function() {
        var out = [];
        parsedAnimation.loop.forEach( function( object, index ) {
            if ( object.objectType == 'floatingText' ) { out.push(object); }
        });
        return out;
    };

    return parsedAnimation;

}

module.exports =  {
    parseAnimationToMemory: parseAnimationToMemory
};
