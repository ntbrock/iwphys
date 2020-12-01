
let assert = require('assert');
let _ = require('lodash');

let animationCalc = require('../../src/iwp7/animation-calc')


describe('Animation', function () {
    describe('Calculator', function () {
        it('Runge Kutta 4', function () {

            const variableName = 'test'
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

            // Define the expected results
            // Test expected values were extracted from Iwp Version 4 via sbt -jar file
            const expected = [
                { displacement : -10, velocity: 0, acceleration: 5 },
                { displacement : -9.999, velocity: 0.1, acceleration: 0.1 },
                { displacement : -9.996, velocity: 0.2, acceleration: 0.1 },
                { displacement : -9.991, velocity: 0.3, acceleration: 0.1 },
                { displacement : -9.984, velocity: 0.4, acceleration: 0.1 },
                { displacement : -9.997, velocity: 0.5, acceleration: 0.1 }
            ];

            const calculatorResult = [];

            // Build a simple animation loop and check each result line!
            let stepDirection = 1 // Forward!
            const vars = { delta_t : 0.02, t: 0 };

            for ( let step = 0; step < expected.length; step++ ) {

                // Step Iteration 1
                calculatorResult[step] =
                    animationCalc.evaluateCalculator( variableName,
                        calculatorCompiled,
                        step,
                        stepDirection,
                        vars,
                        true);

                let thisResult = calculatorResult[step];

                assert.strictEqual( calculatorResult[step].step, step );
                assert.strictEqual( calculatorResult[step].value, expected[step].displacement );
                assert.strictEqual( calculatorResult[step].displacement, expected[step].displacement );
                assert.strictEqual( calculatorResult[step].velocity, expected[step].velocity );
                assert.strictEqual( calculatorResult[step].acceleration, expected[step].acceleration );

                console.log("test-animation-calc-rk4: Step["+step+"] is equal");
            }


        });

    });
});

