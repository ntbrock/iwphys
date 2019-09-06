/**
 * 2019Sep06 New Functions for Object Reordering, port from IWP4.5
 */

/**
 * 2019Sep06 Testing Harness for Port of object odering
 */
function reorderAnimationObjectsBySymbolicDependencyJsonStringify( unused ) {

	// New routine for calculting object dependency ordering


	parsedAnimation.loop.forEach ( function( object, index ) {

	  // console.log("iwp6-order:15> object.calculator: " + object.calculator)

      object.required = animationObjectRequires(object)
      object.provided = animationObjectProvides(object)

    });


	return JSON.stringify(parsedAnimation.loop)
}



function equationRequires(eqn) {

	var parts = eqn.split(/[-+*\/ ()^]/);
	// TODO filter out usual suspects, functions, etc.

	var keep = new Object();

	parts.forEach( function(part,index) {
		if ( part == "" ) {
		} else if ( varsConstants[part] != undefined ) {

		} else if ( part[0] == '1' || part[0] == '2' || part[0] == '3' ||
					part[0] == '4' || part[0] == '5' || part[0] == '6' ||
					part[0] == '7' || part[0] == '8' || part[0] == '9' ||
        		    part[0] == '0' ) {
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



	console.log("iwp6-order:43>  Eqn: " + eqn + "   Keep : " + JSON.stringify(Object.keys(keep)));

	// Dedupe
	return Object.keys(keep);

}


function calculatorRequires(calc) {

	if ( calc.calcType == "parametric" ) {
		return equationRequires(calc.value)

	} else {
		console.log("iwp6-order:41> Unimplemented calculator type: " + JSON.stringify(calc));
		return ["TODO"];
	}
}


// What symbols do I requrie?
function animationObjectRequires(object) {


	if ( object.objectType == "input" ) {
		return []

	} else if ( object.objectType == "output" ) {
        return calculatorRequires(object.calculator)

	} else if ( object.objectType == "solid" ) {
            return [ "TODO" ]

	} else {
		return []
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

	} else {
		return []
	}


}



true;

