@echo on

set J2SE_HOME=c:\jdk1.3.1_02

set CP=jars\iwp.jar
set CP=%CP%;c:\java\xerces-1.4.4\xerces.jar

set PATH=%PATH%;%J2SE_HOME%/bin

java -classpath %CP%;%CLASSPATH% edu.ncssm.iwp.bin.IWP_Animator %1 %2 %3 %4






