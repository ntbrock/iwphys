
let assert = require('assert');
fs = require('fs');

const { Parser } = require('htmlparser2');
const { DomHandler } = require("domhandler");

describe('Html', function () {
    describe('Dom', function () {
        it('Be Readable from a static file', function () {

            // TODO find a way to initalize unti test reading in a static html file.
            // const ce3 = require();

            const promise =
            fs.readFile("./test/html/collision-elastic-3.html",
                'utf8',
                function (err,rawHtml) {

                    let breaker1700=true

                    // https://github.com/fb55/domhandler
                    // let rawHtml = "<h1>Hello World</h1>"

                    const handler = new DomHandler(function (error, dom) {
                        if (error) {
                            // Handle error
                        } else {
                            // Parsing completed, do something
                            console.log(dom);
                        }
                    });

                    const parser = new Parser(handler);
                    parser.write(rawHtml);
                    parser.end();

                    const breaker1710=true

                });
        });
    });
});

