
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

            // Check the compile results first

            assert.strictEqual( calculatorCompiled.calcType, "rk4-mathjs" );
            assert.strictEqual( calculatorCompiled.equation.acceleration, "5" );
            assert.strictEqual( calculatorCompiled.equation.velocity, "0" );
            assert.strictEqual( calculatorCompiled.equation.displacement, "-10" );

            const vars = { dt : 0.1, delta_t : 0.1, t: 0 }

            const calculatorResult =
                animationCalc.evaluateCalculator( 'test',
                    calculatorCompiled,
                    0,
                    1,
                    vars,
                    true,
                    "test");

            /*
            const breaker17=true

            assert.strictEqual( calculatorResult.value, 1 );
*/

        });

    });
});

