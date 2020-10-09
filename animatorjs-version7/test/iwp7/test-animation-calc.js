
let assert = require('assert');
let _ = require('lodash');

let animationCalc = require('../../src/iwp7/animation-calc')

describe('Animation', function () {
    describe('Calculator', function () {
        it('Parameteric Identity', function () {

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

        it('Parametric Variable', function () {

            const calculatorJson =
                {
                    "calcType": "parametric",
                    "value": "a"
                };

            const calculatorCompiled = animationCalc.compileCalculator(calculatorJson);
            const calculatorResult = animationCalc.evaluateCalculator( 'test', calculatorCompiled, 0, 1, { a: 5 }, true, "test")

            const breaker38=true

            assert.strictEqual( calculatorResult.value, 5 );


        });


        it('Euler Variable', function () {

            const calculatorJson =
                {
                    "calcType": "MCalculator_Euler",
                    "value": "0",
                    "displacement": "0",
                    "velocity": "1",
                    "acceleration": "2"
                };


            const calculatorCompiled = animationCalc.compileCalculator(calculatorJson);

            const resultVariable = 'ball.x'
            const objectName = 'ball'


            const vars = { delta_t : 0.1 }

            const calculatorResults = []
            calculatorResults[0] = animationCalc.evaluateCalculator( resultVariable, calculatorCompiled, 0, 1, vars, true, objectName)
            const breaker62=true

            // Next time: Jonathan start here: Why on step 1 did we receive NaN?

            calculatorResults[1] = animationCalc.evaluateCalculator( resultVariable, calculatorCompiled, 1, 1, vars, true, objectName)
            const breaker65=true

            calculatorResults[2] = animationCalc.evaluateCalculator( resultVariable, calculatorCompiled, 2, 1, vars, true, objectName)
            const breaker68=true

            calculatorResults[3] = animationCalc.evaluateCalculator( resultVariable, calculatorCompiled, 3, 1,vars, true, objectName)
            const breaker71=true

            assert.strictEqual( calculatorResults[3].step, 3 );
            assert.strictEqual( calculatorResults[3].value, 0.39 );
            assert.strictEqual( calculatorResults[3].displacement, 0.39 );
            assert.strictEqual( calculatorResults[3].velocity, 1.5 );
            assert.strictEqual( calculatorResults[3].acceleration, 2 );

        });


    });
});

