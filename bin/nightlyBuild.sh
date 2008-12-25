#!/bin/sh

export CVS_RSH=ssh

chmod +x bin/uploadNightlyToIwphys.sh
chmod +x bin/writeBuildVersionJava.sh

echo "IWP: Performing nightly build."

cvs -q update -dP && ./bin/writeBuildVersionJava.sh && ant clean && ant dist && ./bin/uploadNightlyToIwphys.sh

cat ./src/edu/ncssm/iwp/util/buildversion/BuildVersion.java



