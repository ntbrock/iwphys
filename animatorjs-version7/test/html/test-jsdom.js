
let assert = require('assert');
fs = require('fs');

const jsdom = require("jsdom");
const { JSDOM } = jsdom;

describe('Html', function () {
    describe('Dom', function () {
        it('Be Readable from a static file', function () {

            // TODO find a way to initalize unti test reading in a static html file.
            // const ce3 = require();

            const promise =
            fs.readFile("./test/html/collision-elastic-3.html",
                'utf8',
                function (err,rawHtml) {

                    const dom = new JSDOM(rawHtml);

                    // console.log(dom.window.document.querySelector("p").textContent); // "Hello world"

                    const { document } = (new JSDOM(rawHtml)).window;

                    const h1s = document.querySelector("nav").innerHTML;

                    const timeTab = document.getElementById("timeTab");
                        // Synonum for $("#

                    // Great research area - could we bring jquery module into node?

                    // How would I ask the dom if a certain elements is visble on the page or not?
                    // How do you do: timeTab.isVisible??

                    let breaker1700=true

                });
        });
    });
});

