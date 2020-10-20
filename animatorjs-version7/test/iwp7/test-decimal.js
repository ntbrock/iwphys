
let assert = require('assert');
let _ = require('lodash');
let math = require('mathjs')
let animationCalc = require('../../src/iwp7/animation-calc')

describe('Animation', function () {
    describe('Decimal', function () {
        it('Test Multiplication', function () {
            const x=math.bignumber('2.5e25');
            const y=math.bignumber('3.5e35');
            const z=x.times(y);
            console.log(z);
            assert.strictEqual(z.toString(), math.bignumber('8.75e+60').toString());
            assert.strictEqual(z.equals(math.bignumber('8.75e+60')), true);
        });

    });
});

