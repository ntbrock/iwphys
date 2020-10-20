
let assert = require('assert');
let _ = require('lodash');

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

                animationIllustrate.illustrateSolid(solidJson);     // svgSolids from 214 of other not defined?

                const solidAfter = $("#solid_newSolid").html();
                const colorAfter = $("#solid_newSolid").color.red;

                const break118=true                         // Why won't it let me break point here? Message about frames

                assert.strictEqual( colorAfter, 10);
        })
    });
});



