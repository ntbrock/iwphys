#!/bin/sh

#updated for svn

chmod +x bin/uploadNightlyToIwphys.sh
chmod +x bin/writeBuildVersionJava.sh

echo "IWP: Performing nightly build."

svn update && ./bin/writeBuildVersionJava.sh && ant clean && ant dist && ./bin/uploadNightlyToIwphys.sh

# cvs -q update -dP && ./bin/writeBuildVersionJava.sh && ant clean && ant dist && ./bin/uploadNightlyToIwphys.sh

cat ./src/edu/ncssm/iwp/util/buildversion/BuildVersion.java



