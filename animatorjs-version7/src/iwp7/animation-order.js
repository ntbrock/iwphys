'use strict';

let _ = require('lodash');
let varsConstants = require('./animation-constants')

/**
 * Interactive Web Physics 7
 * 2020Sep18 Brockman
 *
 * Dynamic Animation Ordering, ported from iwp6-order.js

 * 2019Sep06 New Functions for Object Reordering, port from IWP4.5
 * 2019Sep06 Testing Harness for Port of object odering
 */


/**
 * Return an array of objects that can be filtered with this module
 * 2020Sep18 Brockman
 * @param loop
 * @returns {*}
 */

function findOrderableObjects( objects ) {

    return _.filter( objects, function(o) {
        return (
            o.objectType !== "time" &&
            o.objectType !== "graphWindow" &&
            o.objectType !== "window" &&
            o.objectType !== "description"
        )
    } )
}


//-------------------------------------------------------------------------------------
// Version 6 code

function reorderAnimationObjectsBySymbolicDependencyJsonStringify( unused ) {

    // Perform reodering
    parsedAnimation.loop = reorderAnimationObjectsBySymbolicDependency(parsedAnimation.loop);


    return JSON.stringify(parsedAnimation.loop)
}


/**
 * Actual logic entry point - called from iwp-calc parsing function as well
 */

function reorderAnimationObjectsBySymbolicDependency(loop) {

    // Mutation Side Effect
    loop.forEach ( function( object, index ) {
        object.required = animationObjectRequires(object)
        object.provided = animationObjectProvides(object)
    });

    // Perform reodering
    return animationObjectReorder(loop);
}



/**
 * Special Variables that should not throw a missingVariable exception */
var timeConstants = { "t": true, "delta_t": true }
var functionConstants = {
    "PI": true,
    "PI.value" : true,
    "E": true,
    // From Version 4 MFunctions.java
    "sign": true,
    "exp": true,
    "toRadians": true,
    "toradians": true,
    "toDegrees": true,
    "todegrees": true,
    "step": true,
    "cot": true,
    "sec": true,
    "csc": true,
    "tan": true,
    "sqrt": true,
    "sin": true,
    "cos": true,
    "ln": true,
    "atan": true,
    "asin": true,
    "acos": true,
    "abs": true,
    "modtwo": true,
    "random": true,
    "rand": true,
    "signum": true
};

//------------------------------------------






function equationRequires(eqn) {

    var parts = eqn.split(/[-+*\/ ()^]/);
    // TODO filter out usual suspects, functions, etc.
    // console.log("iwp6-order:80>  Eqn: " + eqn + "   parts : " + parts);

    var keep = new Object();

    parts.forEach( function(part,index) {
        if ( part == "" ) {
        } else if ( varsConstants[part] != undefined ) {
            // is it a contstant?

        } else if ( functionConstants[part] != undefined ) {
            // known match function

        } else if ( timeConstants[part] != undefined  ) {
            // it's time


        } else if ( part[0] == '1' || part[0] == '2' || part[0] == '3' ||
            part[0] == '4' || part[0] == '5' || part[0] == '6' ||
            part[0] == '7' || part[0] == '8' || part[0] == '9' ||
            part[0] == '0' || part[0] == '.' ) {
            // No Numbers

            /*
                    } else if ( part[0] == 'd' || part[0] == 'v' || part[0] == 'a' ||
                                part[0] == 'x' || part[0] == 'y' ) {

                        // No Internals?
            */

        } else {
            keep[part] = true;
        }
    });


    if ( Object.keys(keep).length > 0 ) {
        // console.log("iwp6-order:115>  Eqn: " + eqn + "   Keep : " + JSON.stringify(Object.keys(keep)));
    } else {
        // console.log("iwp6-order:117>  Eqn: " + eqn + "   Keep : Eqn: no requirements!");
    }

    // Dedupe
    return Object.keys(keep);

}


function calculatorRequires(calc) {
    if ( calc.calcType == "parametric" ) {
        return equationRequires(calc.value)

    } else if ( calc.calcType == "MCalculator_Euler") {

        // displacement
        var d = equationRequires(calc.displacement);
        // velocity
        var v = equationRequires(calc.velocity);
        // acceleration
        var a = equationRequires(calc.acceleration);

        //console.log("iwp6-order:82> MCalculator_Euler: d: " + d);
        //console.log("iwp6-order:82> MCalculator_Euler: v: " + v);
        //console.log("iwp6-order:82> MCalculator_Euler: a: " + a);

        return arrayUnique(d.concat(v.concat(a)));

    } else {
        console.log("iwp6-order:41> ERROR Unimplemented calculator type: " + JSON.stringify(calc));
        throw "iwp6-order:41> ERROR Unimplemented calculator type: " + JSON.stringify(calc);
        return [];
    }
}


// What symbols do I requrie?
function animationObjectRequires(object) {
    // console.log("iwp6-order.js 153: calculatorRequires> object.name:" + object.name);

    if ( object.objectType == "input" ) {
        return []

    } else if ( object.objectType == "output" ) {
        return calculatorRequires(object.calculator);
    } else if ( object.objectType == "solid" ) {


        var width = [];
        var height = [];
        var xpath = [];
        var ypath = [];
        var points = [];

        if ( object.shape && object.shape.width && object.shape.width.calculator ) {
            width = calculatorRequires( object.shape.width.calculator );
            //console.log("iwp6-order:101> object.shape.width.calculator: " + JSON.stringify(object.shape.width.calculator) + " width: " + JSON.stringify(width));
        }
        if ( object.shape && object.shape.height && object.shape.height.calculator ) {
            height = calculatorRequires( object.shape.height.calculator );
            //console.log("iwp6-order:106> object.shape.height.calculator: " + JSON.stringify(object.shape.height.calculator) + " height: " + JSON.stringify(height));
        }
        if (object.shape.shapeType == "polygon") {
            // console.log("polygon acquired!", object.shape);
            object.shape.points.forEach( function(p,i) {
                    // console.log("point found:",p);
                    points = points.concat(calculatorRequires(p.xpath.calculator));
                    points = points.concat(calculatorRequires(p.ypath.calculator));
                }
            )
        }
        if ( object.xpath && object.xpath.calculator ) {
            xpath = calculatorRequires( object.xpath.calculator );
            //console.log("iwp6-order:111> object.xpath.calculator: " + JSON.stringify(object.xpath.calculator) + " xpath: " + JSON.stringify(xpath));
        }
        if ( object.ypath && object.ypath.calculator ) {
            ypath = calculatorRequires( object.ypath.calculator );
            //console.log("iwp6-order:115> object.ypath.calculator: " + JSON.stringify(object.ypath.calculator) + " ypath: " + JSON.stringify(ypath));
        }

        //console.log("iwp6-order:94> Solid " + object.name + "  width: " + width);
        //console.log("iwp6-order:94> Solid " + object.name + "  height: " + height);
        //console.log("iwp6-order:94> Solid " + object.name + "  xpath: " + xpath);
        //console.log("iwp6-order:94> Solid " + object.name + "  ypath: " + ypath);

        var all = arrayUnique(points.concat(width.concat( height.concat (xpath.concat (ypath)))));

        //console.log("iwp6-order:94> Solid " + object.name + "  all : " + JSON.stringify(all) );

        return all;

    } else if ( object.objectType == "floatingText" ) {

        // 2020Feb21 Fix for Floating text path dependencies

        var value = [];
        var xpath = [];
        var ypath = [];

        if ( object.value ) {
            // console.log("iwp6-order:208> object.value: " , object.value);
            value = calculatorRequires(object.value);
        }

        if ( object.xpath && object.xpath.calculator ) {
            xpath = calculatorRequires( object.xpath.calculator );
        }

        if ( object.ypath && object.ypath.calculator ) {
            ypath = calculatorRequires( object.ypath.calculator );
        }

        var all = arrayUnique(value.concat(xpath.concat (ypath)));
        // console.log("iwp6-order:221> FloatingText " + object.name + "  all : " + JSON.stringify(all) );
        return all;

    } else {

        throw "animationObjectRequires unsupported object: " + object.objectType;
    }

}



// What symbols do I provide? - This is easy, really my own
function animationObjectProvides(object) {

    var name = object.name;

    if ( object.objectType == "input" ) {
        return [ name, name+".value" ];

    } else if ( object.objectType == "output" ) {
        return [ name, name+".value" ];

    } else if ( object.objectType == "solid" ) {
        return [
            name+".xdisp",
            name+".xpos",
            name+".xvel",
            name+".xaccel",
            name+".ydisp",
            name+".ypos",
            name+".yvel",
            name+".yaccel"
        ];

    } else if ( object.objectType == "floatingText" ) {

        return [
            name,
            name+".value",
            name+".x",
            name+".y"
        ];

    } else {
        throw "animationObjectProvides unsupported object: " + object.objectType;
    }

}



/**
 * Javascript port for Version 3 logic!
 */

function arrayToObject(arr) {
    var obj = {}
    arr.forEach(function(a,i) { if ( obj[a] ) { obj[a]++ } else { obj[a] = 1 } });
    return obj;
}

function arrayUnique(arr) {
    return Object.keys(arrayToObject(arr));
}


function animationObjectReorder(loop) {

    // Not using objects for the End, since we're not trying to catch stray object types here
    // var objectsForTheEnd = [];
    var objectsForTheMiddle = [];
    var orderingCandidates = [];

    var requiresCache = {};
    var providedCache = {};


    loop.forEach(function(object, index) {

        // First Scan - Find each object that requires nothing! and store it and it's provided symbols.
        if (object.required.length == 0 ) {
            objectsForTheMiddle.push(object);
            // Cache it's provides
            object.provided.forEach(function(symbol,index) { if ( providedCache[symbol] ) { providedCache[symbol]++ } else { providedCache[symbol] = 1 } });

        } else {
            // Save it for the next loop
            orderingCandidates.push(object);
        }
    });

    // console.log("iwp6-order:221> ----------------------------------------------------");
    // console.log("iwp6-order:221> Starting reordering, providedCache: " + JSON.stringify(providedCache));

    // Line 467
    var lastCandidateCount = -1;
    var loopCount = 0;

    // Line 470
    while(orderingCandidates.length > 0  && loopCount < 15 ) {

        // Line 472
        var missingVariables = [];
        var badObjects = [];
        var nextOrderingCandidates = [];

        // Line 475
        /*
        console.log("iwp6-order:242> Loop=" + loopCount + "  ==============================================");

        console.log("iwp6-order:243> Loop=" + loopCount + "  objectsForTheMiddle.length: " + objectsForTheMiddle.length );

        objectsForTheMiddle.forEach(function(object,i) {
            console.log("iwp6-order:245> Loop=" + loopCount + "  objectsForTheMiddle["+i+"]: " + object.name + "   provided: " + JSON.stringify(object.provided));
        });

        console.log("iwp6-order:242> Loop=" + loopCount + "  = = = = = = = = =");

        console.log("iwp6-order:243> Loop=" + loopCount + "  orderingCandidates.length: " + orderingCandidates.length );

        orderingCandidates.forEach(function(object,i) {
            console.log("iwp6-order:249> Loop=" + loopCount + "  orderingCandidate["+i+"]: " + object.name + "   required: " + JSON.stringify(object.required));
        });

        console.log("iwp6-order:242> Loop=" + loopCount + "  ==============================================");
        */


        loopCount++;

        // Line 477
        // Loop to find the next candidate.
        orderingCandidates.forEach(function(object,index) {

            // Line 483
            var allRequirementsSatisfied = true;

            var selfProvides = arrayToObject(object.provided);

            // console.log("iwp6-order:192> OrderingCandidate= " + object.name + "  SelfProvides=" + JSON.stringify(selfProvides));

            // Line 486 - Iterate Each requirement
            object.required.forEach(function(symbol,index) {
                // Line 487
                var requirementSatisfied = false;

                // Line 494 -- Much simpler with hashes
                // Line 505 -- Inlined the self-referrential
                if (! providedCache[symbol] && ! selfProvides[symbol] ) {
                    requirementSatisfied = false;
                } else {
                    // Line 499
                    requirementSatisfied = true;
                }

                // Line 519
                if ( ! requirementSatisfied ) {
                    // Line 520
                    allRequirementsSatisfied = false;
                    // Line 524
                    missingVariables.push(symbol);
                    badObjects.push(object.name);
                    // console.log("iwp6-order:220> MISSING VARIABLE: " + symbol + "   from object: " + object.name);
                } else {

                }

            });

            // Line 530
            if ( allRequirementsSatisfied ) {
                objectsForTheMiddle.push(object);
                // Cache it's provides
                object.provided.forEach(function(symbol,index) { if(providedCache[symbol]) { providedCache[symbol]++ } else { providedCache[symbol] = 1 } });
            } else {
                // Line 533
                nextOrderingCandidates.push(object);
            }
        });

        // console.log("iwp6-order:236> orderingCandidates.length=" + orderingCandidates.length + "  nextOrderingCandidates.length=" + nextOrderingCandidates.length);
        // Line 539
        orderingCandidates = nextOrderingCandidates

        // Line 541
        if ( lastCandidateCount == orderingCandidates.length ) {
            throw "Missing Variable or Circular Dependency Detected: missingVariables: " + JSON.stringify(missingVariables) + " badObjects: " + JSON.stringify(badObjects);
        } else {
            lastCandidateCount = orderingCandidates.length
        }

    }

    return objectsForTheMiddle;


    // return loop;
}


/**

 //---------------------------------------------------------------
 // IWP 3 - on initial zero of the problem, re-order the obejcts in the
 // problem based on their variable dependency.
 // 2007-Jan-29 brockman

 public synchronized void reorderProblemObjectsBySymbolicDependency ( )
 throws UnknownVariableException, CircularDependencyException, InvalidEquationException
 {
    	// thiS methodlooks at the IWPCalculated interface
    	// and re-orders the objects based on their dependency.

    	// ALSO TODO: make the Mvariables by default just include the current frame of data.
    	// because now I don't need to look back any further than NOW. cool.
    	IWPLog.debug(this, "Starting to order " + objectsInDrawOrder.size() + " objects");

    	Collection objectsToAnalyze = this.getObjectsForDrawing(); // This adds the full lsit of objects. Internally,
    	// they split up for efficiency.

    	Collection objectsForTheEnd = new ArrayList();
    	Collection objectsForTheMiddle = new ArrayList();
    	Collection orderingCandidates = new ArrayList();

    	Hashtable requiresCache = new Hashtable();
    	Hashtable providesCache = new Hashtable();


    	// This is my attempt at an algorithim to solve the requires provides problem.
    	// I am going to find the objects that are candidates, and iterate over that list,
    	// looking for objects that have had their requirements satisfied, and if they are
    	// satisfied, pull them out of the candidate array, and move them to the end of the
    	// objectsForMiddle.

    	for ( Iterator i = objectsToAnalyze.iterator(); i.hasNext(); ) {
    		Object o = (Object) i.next();
    		if ( ! ( o instanceof IWPCalculated ) ) {
    			//IWPLog.error(this, "ERROR Object Class: " + o.getClass().getName() + " is not a type of IWPCalculated. Should push to end?");
    			objectsForTheEnd.add(o);
    			continue;
    		}

    		IWPCalculated objectCalc = (IWPCalculated) o;

    		// Go ahead and cache the provides - even if it doesnt' depend on anything.
            // The possibilities here are solid.xpos, solid.xdisp, solid.xvel, solid.xaccel, object.value
            // time.curtime, time, start, end, stop, delta.
    		Collection providedSymbols = objectCalc.getProvidedSymbols();
    		if ( providedSymbols != null ) {
    			providesCache.put(objectCalc,providedSymbols);
    		} else {
    			providesCache.put(objectCalc, new ArrayList(0) ); // cache an empty array to avoid nulls below/
    		}

    		// if it has a null or empty requires, go ahead and put it into the objectsForMiddle,
    		// otherwise cache the results
    		Collection requiredSymbols = objectCalc.getRequiredSymbols();
    		//_safePrint("ProvidedSymbols by " + ((DObject)objectCalc).name, providedSymbols);
    		//_safePrint("RequiredSymbols for " + ((DObject)objectCalc).name, requiredSymbols);

    		if ( requiredSymbols == null ||
    			  requiredSymbols.size() == 0 ) {
    			objectsForTheMiddle.add(objectCalc);
    			requiresCache.put(objectCalc, new ArrayList(0) ); // cache an empty array to avoid nulls below.
    		} else {
    			orderingCandidates.add(objectCalc);
    			requiresCache.put(objectCalc, requiredSymbols );
    		}

    	}

    	// Here is where I pull the guys out one by one if their dependenci9es have been
    	// satisfied. INtensive, but we only do once at the beginning of a problem.

    	int lastCandidateCount = -1;
    	int loopCount = 0;

    	while ( orderingCandidates.size() > 0 ) {

    		Collection nextOrderingCandidates = new ArrayList(orderingCandidates.size());
    		Collection missingVariables = new ArrayList(15);

    		IWPLog.debug(this, "orderingCandidates.size: " + orderingCandidates.size() + "  Loop Count: " + loopCount++  );

    		for ( Iterator i = orderingCandidates.iterator(); i.hasNext() ; ) {

    			IWPCalculated calcObject = (IWPCalculated) i.next();


    			// have all of my dependencies been satisfied by what's areadly in objectsForMIddle?
    			boolean allRequirementsSatisfied = true;
    			Collection thisObjectRequires = ((Collection)requiresCache.get(calcObject));

    			for ( Iterator j = thisObjectRequires.iterator(); j.hasNext() && allRequirementsSatisfied; ){
    				boolean requirementSatisfied = false;
    				String requiresSymbol = (String) j.next();

    				for ( Iterator k = objectsForTheMiddle.iterator(); k.hasNext() && ! requirementSatisfied; ) {
    					IWPCalculated alreadyPushedObject = (IWPCalculated) k.next();

    					// does this guy provide the symbol already?
    					for ( Iterator l = ((Collection)providesCache.get(alreadyPushedObject)).iterator();
    						l.hasNext() && ! requirementSatisfied; ){
    						String providedSymbol = (String) l.next();

    						if ( requiresSymbol.equals(providedSymbol) ) {
    							requirementSatisfied = true;
    						}
    					}

    				}

    				// 2007-Jun-04 brockman - Self-dependency smarts.
    				// The object can also reference itself. The implemetnation of this lives down in the calculator.
    				if ( ! requirementSatisfied ) {
    					for ( Iterator k = calcObject.getProvidedSymbols().iterator(); k.hasNext() && ! requirementSatisfied; ) {
    						String providedSymbol = (String) k.next();
    						if ( requiresSymbol.equals(providedSymbol) ) {
    							IWPLog.info(this,"Object self-provided it's own referenced variable: " + requiresSymbol);
    							requirementSatisfied = true;
    						}
    					}
    				}

    				// Ok, we've looked back , and if we found one, then requirementSatisfied became true,.
    				// and we can move the object to the 'good' array here.
    				if ( ! requirementSatisfied ) {
    					allRequirementsSatisfied = false;

    					// Also keep track of the variable + what it's missing so we can report
    					IWPObject object = (IWPObject) calcObject;
    					missingVariables.add(object.getName()+ " requires '" + requiresSymbol + "'");
    				}

    			}


				if ( allRequirementsSatisfied ) {
					objectsForTheMiddle.add(calcObject);
				} else {
					nextOrderingCandidates.add(calcObject);
				}


    		} // outside of teh ordering candidates iterator.

    		orderingCandidates = nextOrderingCandidates;

    		// If we make it through an interation of the loop and don't remove any, then we might have
    		// a circular.
    		if ( lastCandidateCount == orderingCandidates.size() ) {
    			// This could also be an unknown variable exception. I report back the missing names.
    			StringBuffer missingText = new StringBuffer("\n");
    			for ( Iterator m = missingVariables.iterator(); m.hasNext(); ) {
    				missingText.append(m.next().toString());
    				if ( m.hasNext() ) { missingText.append("\n"); }; // yeah, it's cheating - but \n works.
    			}
    			throw new CircularDependencyException("Missing Variable or Circular Dependecy Detected: " + missingText );
    		} else {
    			lastCandidateCount = orderingCandidates.size();
    		}
    	}


    	// Reset the internal object vectorset.

    	Collection orderedObjects = new Vector(objectsToAnalyze.size() );
    	orderedObjects.addAll(objectsForTheMiddle);
    	orderedObjects.addAll(objectsForTheEnd);
    	this.fullOrderedObjectReset(orderedObjects);

    	// Iwp 4.5 - Store the order back into the claculated objects

        int newWorldOrder = 1;
        for ( Iterator i = orderedObjects.iterator(); i.hasNext(); ) {
            Object o = (Object) i.next();
            if (!(o instanceof IWPCalculationOrder)) {
                IWPLog.error(this, "ERROR Object Class: " + o.getClass().getName() + " is not a type of IWPCalculationOrder.");
                objectsForTheEnd.add(o);
                continue;
            }
            IWPCalculationOrder objectCalc = (IWPCalculationOrder) o;
            objectCalc.setCalculatorOrder(newWorldOrder++);
        }

    	IWPLog.debug(this, "Done ordering " + objectsInDrawOrder.size() + " objects");
    }

 **/



module.exports.findOrderableObjects = findOrderableObjects
module.exports.reorderAnimationObjectsBySymbolicDependency = reorderAnimationObjectsBySymbolicDependency

