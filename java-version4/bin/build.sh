#!/bin/sh

# new, correct way.
ant

# old, complicated way.
#J2SE_HOME=/usr/java/j2sdk1.4.1_02/
#ANT_HOME=/usr/java/apache-ant-1.5.2
#CP=${J2SE_HOME}/jre/lib/rt.jar:${J2SE_HOME}/lib/tools.jar
#CP=$CP:$ANT_HOME/dist/lib/ant.jar
#$J2SE_HOME/bin/java -classpath $CP:$CLASSPATH org.apache.tools.ant.Main "$@"




