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

animationIllustrate.illustrateAnimation($, animation);

// Ready to accept user interaction!
