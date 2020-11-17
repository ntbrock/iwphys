'use strict';

let _ = require('lodash');
let moment = require('moment');

// https://nodejs.org/api/events.html
const EventEmitter = require('events');

class AnimationPlayer extends EventEmitter {

	animate() {
		const klass = this;
		setTimeout(function(){

			for ( let i = 0 ; i < 100 ; i ++ ) {

				const on = moment();

				console.log("Generating stepCalculated: ", i , on.valueOf() );

				klass.emit("stepCalculated", { step : i, on: on } );

				// Proved here that the end of the For block does allow the unit test to recv the event.
			}


		}, 50);
	}

}

// First class export!
module.exports = AnimationPlayer;



