// https://mochajs.org/
let animationOrder = require('../../src/iwp7/animation-order');

let assert = require('assert');
let _ = require('lodash');

describe('animationOrder', function () {
    describe('should', function () {


        it('should filter an animation with zero objects', function () {

            let animation = require('../animations/empty.json')
            assert.strictEqual(animation.objects.length, 4);

            let loop = animationOrder.findOrderableObjects(animation.objects);
            assert.strictEqual(loop.length, 0);

            let breaker1618=true
        });


        it('should order an animation with four objects, no changes', function () {

            let animation = require('../animations/simple-two-boxes.json')
            assert.strictEqual(animation.objects.length, 8);

            let loop = animationOrder.findOrderableObjects(animation.objects);
            assert.strictEqual(loop.length, 4);

            assert.strictEqual(loop[0].name, "redBox")
            assert.strictEqual(loop[1].name, "blueBox")
            assert.strictEqual(loop[2].name, "newOutput")
            assert.strictEqual(loop[3].name, "newInput")


            loop = animationOrder.reorderAnimationObjectsBySymbolicDependency( loop );

            // console.log("Loop: " , _.map(loop, (l,i) => "" + i + " = "+  l.name + " : " + l.objectType  ) );

            assert.strictEqual(loop[0].name, "redBox")
            assert.strictEqual(loop[1].name, "blueBox")
            assert.strictEqual(loop[2].name, "newOutput")
            assert.strictEqual(loop[3].name, "newInput")
        });



        it('should order an animation with basic linear dependencies', function () {

            let animation = require('../animations/order-linear-dependency.json')
            assert.strictEqual(animation.objects.length, 9);

            // Original Unordered
            let expected = [ "outputRedBoxY", "redBox", "blueBox", "outputRedBoxX", "newInput" ];
            let loop = animationOrder.findOrderableObjects(animation.objects);

            _.each(expected, (expect, index) => { assert.strictEqual(loop[index].name, expect) })

            // Reordered, Push the outputs to the end
            expected = [ "redBox", "blueBox", "newInput", "outputRedBoxY", "outputRedBoxX" ]
            loop = animationOrder.reorderAnimationObjectsBySymbolicDependency( loop );

            _.each(expected, (expect, index) => { assert.strictEqual(loop[index].name, expect) })

        });
        
    });
});

