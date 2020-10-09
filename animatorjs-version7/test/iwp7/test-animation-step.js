
let assert = require('assert');
let _ = require('lodash');

let animationCompiler = require('../../src/iwp7/animation-compile')
let animationPlayer = require('../../src/iwp7/animation-step')

describe('Animation', function () {
    describe('Step', function () {
        it('Handles the empty animation json to advance time', function () {

            let json = require('../animations/empty.json');
            assert.strictEqual(json.objects.length, 4);

            let animation = animationCompiler.compileAnimationFromJson(json);

            assert.strictEqual(animation.time.start, 0);
            assert.strictEqual(animation.time.stop, 100);
            assert.strictEqual(animation.loop.length, 0);

            animationPlayer.resetSteps(animation);

            assert.strictEqual(animation.step.currentStep, 0);


            animationPlayer.stepForwardAndPause(animation);

            const breaker23=true

            assert.strictEqual(animation.step.currentStep, 1);
            assert.strictEqual(animation.varsAtStep[1].t, 0.1);


            animationPlayer.stepForwardAndPause(animation);
            animationPlayer.stepForwardAndPause(animation);
            animationPlayer.stepForwardAndPause(animation);
            animationPlayer.stepForwardAndPause(animation);

            assert.strictEqual(animation.step.currentStep, 5);
            assert.strictEqual(animation.varsAtStep[5].t, 0.5);


        });


    });
});

