#!/bin/sh

J2SE_HOME=/usr/java/j2sdk1.4.1_02

CP=jars/iwp.jar

PATH=$PATH:$J2SE_HOME/bin

$J2SE_HOME/bin/java -classpath $CP:$CLASSPATH edu.ncssm.iwp.bin.IWP_Designer "$@"





