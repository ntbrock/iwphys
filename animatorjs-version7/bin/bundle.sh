#!/bin/sh

npx browserify src/browser.js -o bundle.js

# npx browserify src/index.js --debug | npx exorcist bundle.map.js > bundle.js

