#!/bin/sh

#J2SE_HOME=/usr/java/j2sdk1.3.1
J2SE_HOME=/usr/java/j2sdk1.4.1_02


CP=jars/iwp.jar:/usr/java/xerces-1_4_3/xerces.jar
CP=$CP:/usr/java/cos-05Nov2002/lib/cos.jar

PATH=$PATH:$J2SE_HOME/bin

java -classpath $CP:$CLASSPATH edu.ncssm.iwp.bin.IWP_ProblemServer "$@"

