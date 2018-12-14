/**
 * Interactive Web Physics 6 - Pure Javascript implementation animation file loading
 * Ryan Steed, Taylor Brockman 2016 - Version 5.0 Initial Port to HTML + SVG
 * Matthew Mims, Taylor Brockman 2018  - Version 5.1 Added Graphing and D3
 * Albert Gong, Nial Mullane, Taylor Brockman 2019 - Version 6.0 Migrated into Play Framework
 */


function readAnimationObject( animation ) {
    console.log("iwp6-read:1286> Entry point for animation: ", animation );
    if ( typeof animation === "object" ) {
        var a =  parseProblemToMemory( animation );
        masterResetSteps();
        return a;
    } else {
        throw "parseAnimationToMemory:1290> 1st Argument must be Javascript Object, was: "+(typeof animation)
    }
}


function readAnimationString( animationString ) {

    var animation = JSON.parse(animationString);

    console.log("iwp6-read:1286> Entry point for animationString: ", animation );
    if ( typeof animation === "object" ) {
        var a =  parseProblemToMemory( animation );
        masterResetSteps();
        return a;
    } else {
        throw "parseAnimationToMemory:1290> 1st Argument must be Javascript Object, was: "+(typeof animation)
    }
}

true;
