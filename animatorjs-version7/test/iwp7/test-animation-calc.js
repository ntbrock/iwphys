
let assert = require('assert');
let _ = require('lodash');

let animationCalc = require('../../src/iwp7/animation-calc')

describe('Animation', function () {
    describe('Calculator', function () {
        it('xWidth translate from a coordinate system into canvas position', function () {

            animationCalc.setAnimationWindow( {
                "xmin": -10,
                "xmax": 10,
                "ymin": -10,
                "ymax": 10,
                "xgrid": 2,
                "ygrid": 2,
                "xunit": "meters",
                "yunit": "meters",
                "objectType": "window"
            })

            assert.strictEqual( animationCalc.xWidth(0) , 0 );
            assert.strictEqual( animationCalc.xWidth(0.5) , 25 );
            assert.strictEqual( animationCalc.xWidth(1) , 50 );
            assert.strictEqual( animationCalc.xWidth(-1) , -50 );

        });
    });
});

