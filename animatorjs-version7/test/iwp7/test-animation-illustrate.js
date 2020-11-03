
let assert = require('assert');
let _ = require('lodash');
const sinon  = require('sinon');

const fs = require('fs');
const jsdom = require("jsdom");
const { JSDOM } = jsdom;


let animationIllustrate = require('../../src/iwp7/animation-illustrate')

describe('Animation', function () {
    describe('Calculator', function () {
        it('xWidth translate from a coordinate system into canvas position', function () {

            animationIllustrate.setAnimationWindow( {
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

            assert.strictEqual( animationIllustrate.xWidth(0) , 0 );
            assert.strictEqual( animationIllustrate.xWidth(0.5) , 25 );
            assert.strictEqual( animationIllustrate.xWidth(1) , 50 );
            assert.strictEqual( animationIllustrate.xWidth(-1) , -50 );

        });


        it('Load the Empty Animation and Add an Input', function () {

                const rawHtml = fs.readFileSync("./test/html/empty-animation.html",
                    'utf8')

                    const dom = new JSDOM(rawHtml);
                    const { window } = (new JSDOM(rawHtml));
                    const { document } = window.window

                    const $ = require( "jquery" )(window);



                    // Call an Illustrate function, and see an input has been added.




                    const inputJson = {
                            "objectType": "input",
                            "name": "newInput",
                            "hidden": false,
                            "initialValue": 100,
                            "text": "Test",
                            "units": "m"
                        };


                        const inputTableBefore = $("#inputTable").html();
                        const rowsBefore = $("#inputTable tr").length;

                        animationIllustrate.illustrateInput($, window, inputJson);

                        const inputTableAfter = $("#inputTable").html();
                        const rowsAfter = $("#inputTable tr").length;

                        const break65=true

                        assert.strictEqual( rowsBefore , 1 );
                        assert.strictEqual( rowsAfter , 2 ); // Same here... Why does it run, even if this value is changed?

                        const break79 =true;

            const break83=true;
        });
        it('Adds an output with animationIllustrate.illustrateOutput', function () {
            const rawHtml = fs.readFileSync("./test/html/empty-animation.html",
                'utf8')

                const dom = new JSDOM(rawHtml);
                const { window } = (new JSDOM(rawHtml));
                const { document } = window.window

                const $ = require( "jquery" )(window);



                // Call an Illustrate function, and see an input has been added.


                const outputJson = {
                    "objectType": "output",
                    "name": "newOutput",
                    "hidden": false,
                    "units": "m/ss",
                    "text": "Acceleration",
                    "calculator": { attributesProperty: { "type": "parametric" }, "value": "Red.xaccel" }
                };

                const rowsBefore = $("#outputTable tr").length;

                animationIllustrate.illustrateOutput($, window, outputJson);       // Says that htmlOutputs is not defined

                const outputTableAfter = $("#output_newOutput").html();
                const rowsAfter = $("#outputTable tr").length;

                const break118=true

                assert.strictEqual( rowsBefore , 1 );
                assert.strictEqual( rowsAfter , 2 );
        });
        it('Illustrates a solid with illustrateSolid', function () {
            const rawHtml = fs.readFileSync("./test/html/empty-animation.html",
                'utf8')

                const dom = new JSDOM(rawHtml);
                const { window } = (new JSDOM(rawHtml));
                const { document } = window.window

                const $ = require( "jquery" )(window);

                // Set up illustration window

            animationIllustrate.setAnimationWindow( {
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

                // Call an Illustrate function, and see an input has been added.


                const solidJson = {
                    "objectType": "Solid",
                    "name": "newSolid",
                    "hidden": false,
                    "shape": {
                        "shapeType": "circle"
                    },              // Do I need to test all objects?
                    "color": {
                        "red": 10,
                        "blue": 100,
                        "green": 100,
                    }
                };
                animationIllustrate.illustrateSolid($,solidJson);     // svgSolids from 214 of other not defined?

                const solidAfter = $("#canvas ellipse");
                const colorAfter = solidAfter.attr('style');

                const break118=true     ;                    // Why won't it let me break point here? Message about frames

                assert.strictEqual( colorAfter, "fill:rgb(10,100,100)");
        })
        it('Illustrates floating text with illustrateFloatingText', function () {
            const rawHtml = fs.readFileSync("./test/html/empty-animation.html",
                'utf8')

            const dom = new JSDOM(rawHtml);
            const {window} = (new JSDOM(rawHtml));
            const {document} = window.window

            const $ = require("jquery")(window);

            animationIllustrate.setAnimationWindow( {
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

            const textJson = {
                "name": "newText",
                "fontSize": 12,
                "color": {
                    "red": 100,
                    "blue": 10,
                    "green": 10,
                }
            };

            animationIllustrate.illustrateFloatingText($,textJson);

            const textAfter = $("#canvas text");

            const fontAfter = textAfter.attr('font-size')       // Should be 12+15
            const colorAfter = textAfter.attr('style')
            const takeabreak = true;

            assert.strictEqual(fontAfter, "27");
            assert.strictEqual(colorAfter, 'fill:rgb(100,10,10)')
        })
        it('Tests computer reacts as expected to unknown shape for illustrateSolid', function () {
            const rawHtml = fs.readFileSync("./test/html/empty-animation.html",
                'utf8')

            const dom = new JSDOM(rawHtml);
            const {window} = (new JSDOM(rawHtml));
            const {document} = window.window

            const $ = require("jquery")(window);

            let spy = sinon.spy(console, 'log');

            animationIllustrate.setAnimationWindow({
                "xmin": -10,
                "xmax": 10,
                "ymin": -10,
                "ymax": 10,
                "xgrid": 2,
                "ygrid": 2,
                "xunit": "meters",
                "yunit": "meters",
                "objectType": "window"
            });

            const solidJson = {
                "objectType": "Solid",
                "name": "newSolid",
                "hidden": false,
                "shape": {
                    "shapeType": "unknownShape"
                },
                "color": {
                    "red": 10,
                    "blue": 100,
                    "green": 100,
                }
            };
            animationIllustrate.illustrateSolid($,solidJson);

            const breaker = true;

            assert(spy.calledWith("iwp5:821> ERROR: Unrecognized Solid Shape Type: ", "unknownShape"));

            spy.restore;            //  Ensures function is restored; Probably not needed, but wanted to be safe
        });
        it('Ensures a path is created if solid.shape.drawTrails = true', function () {
            const rawHtml = fs.readFileSync("./test/html/empty-animation.html",
                'utf8')

            const dom = new JSDOM(rawHtml);
            const {window} = (new JSDOM(rawHtml));
            const {document} = window.window

            const $ = require("jquery")(window);

            // Set up illustration window

            animationIllustrate.setAnimationWindow({
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

            // Call an Illustrate function, and see an input has been added.


            const solidJson = {
                "objectType": "Solid",
                "name": "newSolid",
                "hidden": false,
                "shape": {
                    "shapeType": "circle",
                    "drawTrails": true
                },              // Do I need to test all objects?
                "color": {
                    "red": 10,
                    "blue": 100,
                    "green": 100,
                }
            };
            animationIllustrate.illustrateSolid($, solidJson);

            const helperHtml = $("#canvas").html();
            const lineAfter = $("#canvas polyline");
            const linecolorAfter = lineAfter.attr('stroke');

            const break118 = true;

            assert.strictEqual(linecolorAfter, "rgb(10,100,100)");
        });
    });
});

/* Questions:
 - Would like to add new modules (see above)
 - Issues w/ polyline
 - Functions missed?
 - What next? ... Gridlines?
*/
