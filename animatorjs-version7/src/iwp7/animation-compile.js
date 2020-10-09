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



function compileInput(inputJson) {
    inputJson.objectType = 'input'

    return deepExtend(inputJson, {});
    // {name: "ar", text: "Amplitude", initialValue: "9.0", units: "m"}
    // 07 Oct 2016 Honoring hidden flag
}

function compileOutput(outputJson) {

    const compiledOutput = {
        objectType: 'output',
        name: outputJson.name,
        text: outputJson.text,
        units: outputJson.units,
        calculator: animationCalc.compileCalculator(outputJson.calculator),
        hidden: outputJson.hidden //Hidden flag still needed - be careful about cutting off attributes here.
    }

    return compiledOutput;
}



function compileSolid(solidJson) {

    // In Memory - PreParse Equations with math.js

    const compiledSolid = {
        objectType: 'solid',
        name: solidJson.name,
        color: {
            red: parseFloat(solidJson.color.red),
            green: parseFloat(solidJson.color.green),
            blue: parseFloat(solidJson.color.blue),
        },
        shape: {
            shapeType: solidJson.shape.shapeType,
            points: solidJson.shape.points, // 2020Jan31 Re-plumbing V4 features
            file: solidJson.shape.file, // 2020Jan31 Re-plumbing V4 features
            drawTrails: solidJson.shape.drawTrails,
            drawVectors: solidJson.shape.drawVectors,
            graphOptions:
                deepExtend( solidJson.shape.graphOptions,
                    { initiallyOn: solidJson.shape.graphOptions.initiallyOn } ),
            width: {
                calculator: animationCalc.compileCalculator(solidJson.shape.width.calculator)
            },
            height: {
                calculator: animationCalc.compileCalculator(solidJson.shape.height.calculator)
            }
        },
        xpath: {
            calculator : animationCalc.compileCalculator(solidJson.xpath.calculator)
        },
        ypath: {
            calculator : animationCalc.compileCalculator(solidJson.ypath.calculator)
        }
    };


    // If the animation iwp solid has a polygon shape, need to iterate over an initialize each of the calcualtors.
    // hard to do as part of the initialization because it is a dynamic list.
    // Add points here..?
    if ( compiledSolid.shape.shapeType == "polygon" ) {
        compiledSolid["points"] = []

        if ( ! solidJson.shape.points ) {
            console.log("iwp6-calc:766: The solid's polygon shape had no points! solid: ", solid);
        } else {
            // 2020Jan31 - Mapped to new structure w/o intermediate point
            solidJson.shape.points.forEach ( function( i, index ) {
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
        compiledSolid.fileUri = "../../images/"+solidJson.shape.file.image.split("/images/")[1]
        // console.log("fileUri:",compiledSolid.fileUri)
    }

    // 2019Jan18 Promoted Angle processing to a more common location
    if ( typeof solidJson.shape.angle !== "undefined" ) {
        compiledSolid.shape.angle = {calculator: animationCalc.compileCalculator(solidJson.shape.angle.calculator)}
    }

    return compiledSolid;
}


// 2020Feb07 Added formal support for floating text
function compileFloatingText(objectJson) {

    // console.log("compileFloatingText:904> object: " , object);

    return {
        objectType: 'floatingText',
        name: objectJson.name,
        shape: {
            shapeType: objectJson.class,
            height: 1,
            width: 1
        },
        text: objectJson.text,
        units: objectJson.units,
        value: {
            calculator: animationCalc.compileCalculator(objectJson.value)
        },
        fontSize: objectJson.fontSize,
        showValue: ( objectJson.showValue === true || false ),
        color: {
            red: parseFloat(objectJson.color.red),
            green: parseFloat(objectJson.color.green),
            blue: parseFloat(objectJson.color.blue),
        },
        xpath: {
            calculator : animationCalc.compileCalculator(objectJson.xpath.calculator)
        },
        ypath: {
            calculator : animationCalc.compileCalculator(objectJson.ypath.calculator)
        }
    }

}


// "time": { "start": "0.0", "stop": "5.0", "change": "0.02",  "fps": "25.0" },
function compileTime(timeJson) {
    //console.log("time :", inTime);
    return {
        start : parseFloat(timeJson.start),
        stop : parseFloat(timeJson.stop),
        change : parseFloat(timeJson.change),
        fps : parseFloat(timeJson.fps)
    };
}

// "description": { "text": "A ball is attached to a horizontal spring (not shown) which causes the ball to oscillate about the origin. Run the animation until it stops. Click on Show graph. \n\nWhich graph represents position vs. time?  How do you know?\nWhich graph represents velocity vs. time?  How do you know?\nWhich graph represents acceleration vs. time?  How do you know?\n\nWhat would a graph of net force on the ball vs. time look like?  Why?"
// No Real Operation
function compileDescription(descriptionJson) {
    //console.log("description :", inDescription);
    // Global
    return deepExtend(descriptionJson, {});
}


// "window": { "xmin": "-10.0", "xmax": "10.0", "ymin": "-10.0", "ymax": "10.0", "xgrid": "2.0", "ygrid": "1.0", "xunit": "meters", "yunit": "meters"
// No Real Operation
function compileWindow(windowJson) {
    //console.log("window :", inWindow);
    // Global
    return deepExtend(windowJson, {});
}



// "GraphWindow": { "xmin": "0.0", "xmax": "5.0", "ymin": "-50.0", "ymax": "50.0", "xgrid": "0.5", "ygrid": "10.0"
function compileGraphWindow(graphWindowJson) {

    return deepExtend(graphWindowJson, {});

    /*

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
    */


}


/**
 * Important entry point!
 * Formerly: Parse Animation to Memeory
 * 2020Oct09 Rewrite Version 7 Animator in node
 * 2018Dec14 Converted to pure Js, the $.xxType interface is different than typeof, requires Array.isArray
 *
 * ! Illustration (the step of populating values into HTML) is now done elsewhere
 *
 * After the animation parse, outer layer must call :  masterResetSteps()  or   calculateVarsAtStep(currentStep = 0);
 * The new iwp6-read does this.
 */


function compileAnimationFromJson( animationJson ) {

    let animation = { loop: [] };

    let preCompileLoop = [];

    animation.author = animationJson.author;

    animationJson.objects.forEach( function( objectJson, index ) {

        // console.log("iwp6-calc:1451> parseAnimationToMemory> Iterator: " + JSON.stringify(object) );

        if ( objectJson.objectType === "time" ) {
            animation.time = compileTime(objectJson);

        } else if ( objectJson.objectType === "graphWindow" ) {
            animation.graphWindow = compileGraphWindow(objectJson);

        }  else if ( objectJson.objectType === "window" ) {
            animation.window = compileWindow(objectJson);

        } else if ( objectJson.objectType === "description" ) {
            animation.description = compileDescription(objectJson)

        } else if ( objectJson.objectType === "input" ) {
            preCompileLoop.push(objectJson);

        } else if ( objectJson.objectType === "output" ) {
            preCompileLoop.push(objectJson);

        } else if ( objectJson.objectType === "solid" ) {
            preCompileLoop.push(objectJson);

        } else if ( objectJson.objectType === "floatingText" ) {
            preCompileLoop.push(objectJson);

        } else {
            throw "Calculation parseAnimationToMemory unrecognized Object Type: " + object.objectType;
        }

    });



    // 2019Sep06 Reordering of the Animatin Objects based on IWP3 Logic Port.
    // Important to note that the implementation I did iterates over Jsons, not the compiled objects

    animationOrder.reorderAnimationObjectsBySymbolicDependency(preCompileLoop).forEach( function(objectJson) {

        if (objectJson.objectType === "input") {
            animation.loop.push(compileInput(objectJson));

        } else if (objectJson.objectType === "output") {
            animation.loop.push(compileOutput(objectJson));

        } else if (objectJson.objectType === "solid") {
            animation.loop.push(compileSolid(objectJson));

        } else if (objectJson.objectType === "floatingText") {
            animation.loop.push(compileFloatingText(objectJson));

        } else {
            throw "Animation loop contained an unexpected ObjectType: " + objectJson.objectType;
        }
    });

    // This animation object is 'almost' an object oriented class!
    decorateAnimationFunctions(animation);


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

    // Success!
    animation.compiled = true;
    return animation;
}


/**
 * Decrorate some helper functions onto the animation 'object'
 * This is almost like a class
 */
function decorateAnimationFunctions(animation) {

    if ( ! animation ) { throw "Refusing to proceed with decoration since animation argument is null" }

    animation.solids = function() {
        let out = [];
        animation.loop.forEach( function( object, index ) {
            if ( object.objectType == 'solid' ) { out.push(object); }
        });
        return out;
    };

    /**
     2020Feb21 Disabled Generic Objects*/
    /*
      animation.objects = function() {
          var out = [];
          animation.loop.forEach( function( object, index ) {
              if ( object.objectType == 'objects' ) { out.push(object); }
          });
          return out;
      };
    */

    animation.inputs = function() {
        let out = [];
        animation.loop.forEach( function( object, index ) {
            if ( object.objectType == 'input' ) { out.push(object); }
        });
        return out;
    };

    animation.outputs = function() {
        let out = [];
        animation.loop.forEach( function( object, index ) {
            if ( object.objectType == 'output' ) { out.push(object); }
        });
        return out;
    };

    animation.floatingTexts = function() {
        let out = [];
        animation.loop.forEach( function( object, index ) {
            if ( object.objectType == 'floatingText' ) { out.push(object); }
        });
        return out;
    };

    return animation;

}

//---------------------------------------------------------------------------

module.exports =  {
    compileAnimationFromJson: compileAnimationFromJson
};







/**
 * Deactivated in Version 7 - Our only generic is FloatingText
 function compileObject(object) {

    //2018Oct12 - Detect the WaveBox
    if ( object.objectType == "edu.ncssm.iwp.objects.grapher.DObject_Grapher" ) {
        alert("This Animation Contains a GraphBox, Not yet implented in IWP5");
        return;
    }

    if ( ! Array.isArray(animation.compiledObjects)) {
        throw "Animation.compiledObjects not initialized as array, is: " + animation.compiledObjects;
    }

    const compiledObject = {
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
    return compiledObject;

}*/