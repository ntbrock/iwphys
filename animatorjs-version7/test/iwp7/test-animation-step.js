
let assert = require('assert');
let _ = require('lodash');

let animationParser = require('../../src/iwp7/animation-parsing')
let animationStep = require('../../src/iwp7/animation-step')

describe('Animation', function () {
    describe('Step', function () {
        it('Handles the empty animation json to advance time', function () {

            let json = require('../animations/empty.json')
            assert.strictEqual(json.objects.length, 4);

            let animation = animationParser.parseAnimationToMemory(json);

            assert.strictEqual(animation.time.start, 0);
            assert.strictEqual(animation.time.stop, 100);
            assert.strictEqual(animation.loop.length, 0);

            animationStep.stepForwardAndPause(animation);


        });


    });
});

