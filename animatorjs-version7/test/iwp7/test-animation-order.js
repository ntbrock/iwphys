// https://mochajs.org/
let animationOrder = require('../../src/iwp7/animation-order');
let assert = require('assert');
let _ = require('lodash');

describe('animationOrder', function () {
    describe('should', function () {
        it('should filter an animation with zero objects', function () {
            console.log("running animation test...")

            let animation = require('../animations/empty.json')
            assert.strictEqual(animation.objects.length, 4);

            let loop = animationOrder.findOrderableObjects(animation.objects);
            assert.strictEqual(loop.length, 0);
        });


        it('should filter an animation with two objects', function () {
            console.log("running animation test...")

            let animation = require('../animations/two-boxes.json')
            assert.strictEqual(animation.objects.length, 8);

            let loop = animationOrder.findOrderableObjects(animation.objects);
            assert.strictEqual(loop.length, 4);

            loop = animationOrder.reorderAnimationObjectsBySymbolicDependency( loop );

            // console.log("Loop: " , _.map(loop, (l,i) => "" + i + " = "+  l.name + " : " + l.objectType  ) );

            assert.strictEqual(loop[0].name, "redBox")
            assert.strictEqual(loop[1].name, "blueBox")
            assert.strictEqual(loop[2].name, "newOutput")
            assert.strictEqual(loop[3].name, "newInput")
        });

    });
});

