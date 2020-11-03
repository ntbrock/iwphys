/**
 * Interactive Web Physics 7 - Wrapper around mathjs library to add extra defense
 * @param equation
 * @returns {*}
 */

const math = require("mathjs");

function compile( equation ) {

	return math.compile( equation );
}


function evaluate( compiled, vars ) {

	const newValue = compiled.evaluate(vars)
	if ( typeof newValue === "number" ) {
		return newValue;
	} else if ( typeof newValue["value"] === "number" ) {
		return newValue.value;
	} else {
		throw "animation-calc:487> Unable to process compiled, not numeric and doesn't have value property: " + newValue
	}
}




module.exports = {
	compile: compile,
	evaluate : evaluate
}
