/**
 * Interactive Web Physics Version 7
 * 2020Dec18 Burgess , Brockman
 * Entry Point for webbrowser loads, bundled via browserify, dev env : bin/beefyListen.sh
 *
 */

// D-Fense, require window global object
if ( window === undefined ) {
	throw Error("iwp browser.js designed to be invoked from a browser with a 'window' global")
}

const $ = require('jquery')

const animationIllustrate = require('./iwp7/animation-illustrate')
const animationPlayer = require('./iwp7/animation-player')
const animationCompile = require('./iwp7/animation-compile')


// Metacode: read simple Animation Json  (Todo , dynamic from page / rest endpoint )

const testJson = require('./animations/simple-two-boxes.json')
const animation = animationCompile.compileAnimationFromJson( testJson );

console.log("Test Animation: " , animation);



animationIllustrate.setAnimationWindow( {
	"xmin": -10,
	"xmax": 10,
	"ymin": -10,
	"ymax": 10,
	"xgrid": 2,
	"ygrid": 2,
	"xunit": "meters",
	"yunit": "meters",
	"objectType": "window"
});

animationIllustrate.illustrateCanvasGridlines($);



const inputJson = {
	"objectType": "input",
	"name": "newInput",
	"hidden": false,
	"initialValue": 100,
	"text": "Test",
	"units": "m"
};

animationIllustrate.illustrateInput($, window, inputJson);




const solidJson = {
	"objectType": "Solid",
	"name": "newSolid",
	"hidden": false,
	"shape": {
		"shapeType": "circle"
	},
	"color": {
		"red": 10,
		"blue": 100,
		"green": 100,
	}
};
animationIllustrate.illustrateSolid($,solidJson);
// svgSolids from 214 of other not defined?

