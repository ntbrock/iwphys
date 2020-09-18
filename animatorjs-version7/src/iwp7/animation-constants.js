'use strict';

/**
 * Interactive Web Physics 7
 * 2020Sep18 Brockman
 *
 * Ported from iwp6-calc.js to a new constants standalone
 *
 * 2018Mar01 Found that monkeypatching mathjs was non-reliable so now including in the vars scope.
 * Converts from degrees to radians. this makes the 'toRadians(..)' function available in the Calculator space.
 * Likely the are are other physical constants that we'll need to load as well.
 */

module.exports = {
    G : -9.8,
    pi : Math.PI,
    step: function(x) { if ( x > 0 ) { return 1 } else { return 0 } },
    toRadians : function(degrees) { return degrees * Math.PI / 180; },
    toDegrees : function(radians) { return radians * 180 / Math.PI; },
    sign: function(input) { if ( input < 0 ) { return -1 } else { return 1 } }
}