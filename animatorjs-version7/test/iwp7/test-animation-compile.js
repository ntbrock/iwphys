
let assert = require('assert');
let _ = require('lodash');

let animationCompiler = require('../../src/iwp7/animation-compile')

describe('Animation', function () {
    describe('Parsing', function () {
        it('Handles the empty animation json', function () {

            let json = require('../animations/empty.json')
            assert.strictEqual(json.objects.length, 4);

            let animation = animationCompiler.compileAnimationFromJson(json);

            assert.strictEqual(animation.time.start, 0);
            assert.strictEqual(animation.time.stop, 100);
            assert.strictEqual(animation.loop.length, 0);

        });

        it('Handles a simple animation json', function () {

            let json = require('../animations/simple-two-boxes.json');
            assert.strictEqual(json.objects.length, 8);

            let animation = animationCompiler.compileAnimationFromJson(json);

            let breaker29=true

            assert.strictEqual(animation.time.start, 0);
            assert.strictEqual(animation.time.stop, 100);
            assert.strictEqual(animation.loop.length, 4);


        });

    });
});

