/**
 * Return true if a is equal to b within a pct tolerance, example = 0.01
 *
 * TODO - Refactor this function in a common location to be used by other tests.
 *
 *
 *
 * @param a
 * @param b
 * @param pct
 */

function equalWithinPercentError(a, b, pct = 0.0000001 ) {

    // zero case
    if ( a == 0 && b == 0 ) { return true; }
    // inverse case
    else if ( a == -b ) {
        console.log(`test-animation-calc-rk4:23> Return false for special case of a == -b  ( ${a} == - ${b} )`)
        return false;
    }
    else {
        let diff = ( a - b )
        let error = Math.abs ( diff / ((a+b) / 2) );

        let equalWithin = error <= pct
        if (!equalWithin) {
            let breaker17 = true
        }

        return equalWithin;
    }
}

module.exports = {
    equalWithinPercentError: equalWithinPercentError
}