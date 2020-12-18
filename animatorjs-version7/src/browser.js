/**
 * Entry Point for webbrowser loads
 */


const $ = require('jquery')

console.log("browser.js:6> Hello World");

const animationIllustrate = require('./iwp7/animation-illustrate')

const inputJson = {
	"objectType": "input",
	"name": "newInput",
	"hidden": false,
	"initialValue": 100,
	"text": "Test",
	"units": "m"
};

animationIllustrate.illustrateInput($, window, inputJson);

