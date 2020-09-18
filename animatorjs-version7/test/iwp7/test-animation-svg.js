
let assert = require('assert');
let _ = require('lodash');

let animationSvg = require('../../src/iwp7/animation-svg')

describe('Animation', function () {
    describe('Calculator', function () {
        it('xWidth translate from a coordinate system into canvas position', function () {

            animationSvg.setAnimationWindow( {
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

            assert.strictEqual( animationSvg.xWidth(0) , 0 );
            assert.strictEqual( animationSvg.xWidth(0.5) , 25 );
            assert.strictEqual( animationSvg.xWidth(1) , 50 );
            assert.strictEqual( animationSvg.xWidth(-1) , -50 );

        });
    });
});

