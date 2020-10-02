
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

            const promise =
                fs.readFile("./test/html/empty-animation.html",
                    'utf8',
                    function (err,rawHtml) {

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
                        assert.strictEqual( rowsAfter , 2 );

                    });

            return promise; // so that mocha executes it!
        });

    });
});



