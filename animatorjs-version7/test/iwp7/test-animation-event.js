
let assert = require('assert');
let _ = require('lodash');
let moment = require('moment');

let animationCalc = require('../../src/iwp7/animation-calc')

let AnimationPlayer = require('../../src/iwp7/animation-player')


describe('Animation', function () {
    describe('Events', function () {
        it('StepCalculated Event', function () {

            const breaker11=true


            const player = new AnimationPlayer();
            player.on("stepCalculated", function(e) {

                const recv = moment();

                const diff = e.on.valueOf() - recv.valueOf();

                console.log("Received stepCalculated! e: " , e, diff );
            })

            // User Presses Play in the UI:
            // playButton.on("click", function() { player.animate(); }
            const animateReturn = player.animate();
            //assert.strictEqual( calculatorResult.value, 1 );

            console.log("Animate() function retured back to unit test. animateReturn:" , animateReturn);

            // Technique - Waiting 10 seconds at end of a test for events to resolve
            return new Promise(resolve => setTimeout(resolve, 1000));
        });

    });
});

