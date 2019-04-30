/**
 * Interactive Web Physics 6 - Pure Javascript implementation animation file loading
 * Ryan Steed, Taylor Brockman 2016 - Version 5.0 Initial Port to HTML + SVG
 * Matthew Mims, Taylor Brockman 2018  - Version 5.1 Added Graphing and D3
 * Albert Gong, Nial Mullane, Taylor Brockman 2019 - Version 6.0 Migrated into Play Framework
 */


 /** Simulating version 4 behavior for comparison purposes */
var MAX_animationSteps = 1000;


function readAnimationObject( animation ) {
    console.log("iwp6-read:10> Entry point for animation: ", animation );
    if ( typeof animation === "object" ) {
        var a =  parseAnimationToMemory( animation );
        masterResetSteps();
        return a;
    } else {
        throw "iwp6-read:16> readAnimationObject 1st Argument must be Javascript Object, was: "+(typeof animation)
    }
}


function readAnimationString( animationString ) {

    var animation = JSON.parse(animationString);

    console.log("iwp6-read:10> Entry point for animationString: ", animation );
    if ( typeof animation === "object" ) {
        var a =  parseAnimationToMemory( animation );
        masterResetSteps();
        return a;
    } else {
        throw "iwp6-read:31> readAnimationObject 1st Argument must be Javascript Object, was: "+(typeof animation)
    }
}


function playAnimationToEnd( animationString ) {

    if ( parsedAnimation == null ) {
        throw "iwp6-read:39> playAnimationToEnd called without an Animation being read yet"
    }

    masterResetSteps();

    setStepDirection(1);

    var loop = true;

    for ( var i = 0; i < MAX_animationSteps-1 && loop; i++ ) {
        var loop = handleStep() >= 0;
        // console.log("iwp6-read:50> Looping after step: " + i );
    }

    return currentStep;
}


true;
