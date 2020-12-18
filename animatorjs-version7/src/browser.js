/**
 * Entry Point for webbrowser loads
 */

if ( window === undefined ) {
	throw Error("iwp browser.js designed to eb invoked from a browser with a 'window' global")
}

const $ = require('jquery')

console.log("browser.js:6> Hello World");

const animationIllustrate = require('./iwp7/animation-illustrate')



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

