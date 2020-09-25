// https://mochajs.org/
let assert = require('assert');

describe('Promise', function () {
    describe('investigation', function () {
        it('should illuminate our understanding of mocha testing promises', function () {

            // Answer for this = return the promise from your unit test!

            const p1 = new Promise( function(resolve) {
                let answer = 0;
                for ( let i = 0; i <= 1000; i++ ) { answer = i; }
                console.log("p1: ", answer);

                resolve(answer);
            });

            const p2 = new Promise( function(resolve) {
                let answer = 0;
                for ( let i = 0; i >= -1000; i-- ) { answer = i; }
                console.log("p2: ", answer);

                resolve(-answer);
            });


            // Demo - of how to make sure both promises are resolved to assert teh results.
            const p3 = p1.then(function(a) {
                p2.then(function (b) {

                    console.log("p1: ", a, "  p2: ", b);
                    assert.strictEqual(a, b);

                });
            });

            console.log("Let's get going!");

            let breaker39=true

            // The third combined promise is what we're returning up to the Mocha Framework!
            return p3;

        });
    });
});

