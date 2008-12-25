#!/bin/sh

J2SE_HOME=/usr/java/j2sdk1.4.2_03

CP=jars/iwp.jar

PATH=$PATH:$J2SE_HOME/bin

java -classpath $CP:$CLASSPATH edu.ncssm.iwp.test.TEST_MEquation_ParenNegOne "$@"





