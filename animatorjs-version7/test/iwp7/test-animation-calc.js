
let assert = require('assert');
let _ = require('lodash');

let animationCalc = require('../../src/iwp7/animation-calc')

describe('Animation', function () {
    describe('Calculator', function () {
        it('Jonathan Needs to find a function to test!', function () {

            const calculatorJson =
                {
                    "calcType": "parametric",
                    "value": "1"
                };


            const calculatorCompiled = animationCalc.compileCalculator(calculatorJson);
            const calculatorResult = animationCalc.evaluateCalculator( 'test', calculatorCompiled, 0, { t: 0 }, true, "test")

            const breaker17=true

            assert.strictEqual( calculatorResult.value, 1 );


        });
    });
});

