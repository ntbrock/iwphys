
let assert = require('assert');
let _ = require('lodash');

let animationCalc = require('../../src/iwp7/animation-calc')

describe('Animation', function () {
    describe('Calculator', function () {
        it('Runge Kutta 4', function () {

            const calculatorJson =
                {
                    "calcType" : "MCalculator_RK4",
                    "displacement" : "-10",
                    "velocity" : "0",
                    "acceleration" : "5"
                }


            const calculatorCompiled = animationCalc.compileCalculator(calculatorJson);
            const calculatorResult = animationCalc.evaluateCalculator( 'test', calculatorCompiled, 0, { t: 0 }, true, "test")

            /*
            const breaker17=true

            assert.strictEqual( calculatorResult.value, 1 );
*/

        });

    });
});

