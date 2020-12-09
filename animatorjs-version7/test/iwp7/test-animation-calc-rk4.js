/**
 * TODO - Build an RK4 Reverse Test
 *
 * Upcoming Next - Integration into new version 7 player
 * @type {assert | ((value: any, message?: (string | Error)) => void) | ((value: any, message?: (string | Error)) => asserts value)}
 */

let assert = require('assert');
let _ = require('lodash');

let animationCalc = require('../../src/iwp7/animation-calc')
let {equalWithinPercentError} = require('./test-helper')

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
                { displacement : -9.975, velocity: 0.5, acceleration: 0.1 },
                { displacement : -9.964, velocity: 0.6, acceleration: 0.1 }, // IWP4 Says -9.965, Rounding?
                { displacement : -9.951, velocity: 0.7, acceleration: 0.1 },
                { displacement : -9.936, velocity: 0.8, acceleration: 0.1 }
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

                assert.strictEqual( equalWithinPercentError(calculatorResult[step].step, step ), true);
                assert.strictEqual( equalWithinPercentError(calculatorResult[step].value, expected[step].displacement), true );
                assert.strictEqual( equalWithinPercentError(calculatorResult[step].displacement, expected[step].displacement), true );
                assert.strictEqual( equalWithinPercentError(calculatorResult[step].velocity, expected[step].velocity), true );
                assert.strictEqual( equalWithinPercentError(calculatorResult[step].acceleration, expected[step].acceleration), true );

                console.log("test-animation-calc-rk4: Step["+step+"] is equal");
            }


        });
        it('Runge Kutta 4 Reverse Test', function () {

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
                { displacement : -9.975, velocity: 0.5, acceleration: 0.1 },
                { displacement : -9.964, velocity: 0.6, acceleration: 0.1 }, // IWP4 Says -9.965, Rounding?
                { displacement : -9.951, velocity: 0.7, acceleration: 0.1 },
                { displacement : -9.936, velocity: 0.8, acceleration: 0.1 }
            ];

            const calculatorResult = [];

            // Build a simple animation loop and check each result line!
            let stepDirection = -1 // Forward!
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

                assert.strictEqual( equalWithinPercentError(calculatorResult[step].step, step ), true);
                assert.strictEqual( equalWithinPercentError(calculatorResult[step].value, expected[step].displacement), true );
                assert.strictEqual( equalWithinPercentError(calculatorResult[step].displacement, expected[step].displacement), true );
                assert.strictEqual( equalWithinPercentError(calculatorResult[step].velocity, expected[step].velocity), true );
                assert.strictEqual( equalWithinPercentError(calculatorResult[step].acceleration, expected[step].acceleration), true );

                console.log("test-animation-calc-rk4: Step["+step+"] is equal");
            }


        });

    });
    // it('Runge Kutta 4 Variable Acceleration', function () {
    //
    //     const variableName = 'test'
    //     const calculatorJson =
    //         {
    //             "calcType" : "MCalculator_RK4",
    //             "displacement" : "0",
    //             "velocity" : "0",
    //             "acceleration" : "t"
    //         }
    //
    //     const calculatorCompiled = animationCalc.compileCalculator(calculatorJson);
    //
    //     // Check the compile results first
    //
    //     assert.strictEqual( calculatorCompiled.calcType, "rk4-mathjs" );
    //     assert.strictEqual( calculatorCompiled.equation.acceleration, "t" );
    //     assert.strictEqual( calculatorCompiled.equation.velocity, "0" );
    //     assert.strictEqual( calculatorCompiled.equation.displacement, "0" );
    //
    //     // Define the expected results
    //     // Test expected values were extracted from Iwp Version 4 via sbt -jar file
    //     const expected = [
    //         { displacement : 0, velocity: 0, acceleration: 0 },
    //         { displacement : -9.999, velocity: 0.1, acceleration: 0.1 },
    //         { displacement : -9.996, velocity: 0.2, acceleration: 0.1 },
    //         { displacement : -9.991, velocity: 0.3, acceleration: 0.1 },
    //         { displacement : -9.984, velocity: 0.4, acceleration: 0.1 },
    //         { displacement : -9.975, velocity: 0.5, acceleration: 0.1 },
    //         { displacement : -9.964, velocity: 0.6, acceleration: 0.1 }, // IWP4 Says -9.965, Rounding?
    //         { displacement : -9.951, velocity: 0.7, acceleration: 0.1 },
    //         { displacement : -9.936, velocity: 0.8, acceleration: 0.1 }
    //     ];
    //
    //     const calculatorResult = [];
    //
    //     // Build a simple animation loop and check each result line!
    //     let stepDirection = 1 // Forward!
    //     const vars = { delta_t : 0.02, t: 0 };
    //
    //     for ( let step = 0; step < expected.length; step++ ) {
    //
    //         // Step Iteration 1
    //         calculatorResult[step] =
    //             animationCalc.evaluateCalculator( variableName,
    //                 calculatorCompiled,
    //                 step,
    //                 stepDirection,
    //                 vars,
    //                 true);
    //
    //         let thisResult = calculatorResult[step];
    //
    //         assert.strictEqual( equalWithinPercentError(calculatorResult[step].step, step ), true);
    //         assert.strictEqual( equalWithinPercentError(calculatorResult[step].value, expected[step].displacement), true );
    //         assert.strictEqual( equalWithinPercentError(calculatorResult[step].displacement, expected[step].displacement), true );
    //         assert.strictEqual( equalWithinPercentError(calculatorResult[step].velocity, expected[step].velocity), true );
    //         assert.strictEqual( equalWithinPercentError(calculatorResult[step].acceleration, expected[step].acceleration), true );
    //
    //         console.log("test-animation-calc-rk4: Step["+step+"] is equal");
    //     }
    //
    //
    // });
    it('Equal Numerical Percent', function () {

        let testA = equalWithinPercentError( 1, 1, 0.1 )
        let testB = equalWithinPercentError( 1, 1, 0 )
        let testC = equalWithinPercentError( 0.1, 0.09999999999999999, 0.05 )
        let testD = equalWithinPercentError( 0.1, 0.09999999999999999 )

        let breaker89=true

        assert.strictEqual(true, true)
    });
});

