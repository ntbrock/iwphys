#!/bin/sh

cp -f dist/iwp.jar doc/
cd doc
appletviewer -J-Djava.security.policy==java.policy designerApplet.html
cd -
