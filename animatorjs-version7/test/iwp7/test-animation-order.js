// https://mochajs.org/
let animationOrder = require('../../src/iwp7/animation-order');
let assert = require('assert');
let _ = require('lodash');

describe('animationOrder', function () {
    describe('should', function () {
        it('should re-order a animation with zero objects', function () {
            console.log("running animation test...")

            let empty = require('../animations/empty.json')

            assert.strictEqual(empty.objects.length, 4);

            let loop = _.filter( empty.objects, function(o) {
                return o.objectType !== "time" && o.objectType !== "graphWindow" && o.objectType !== "window" && o.objectType !== "description"
            } );

            let result = animationOrder.reorderAnimationObjectsBySymbolicDependency( loop );

            console.log("Result: " , result);

            // assert.equal([1, 2, 3].indexOf(4), -1);

        });
    });
});

