@echo off
set J2SE_HOME=c:\j2sdk1.4.2_05
set ANT_HOME=c:\apache-ant-1.6.2

%J2SE_HOME%/bin/java -classpath %ANT_HOME%\lib\ant-launcher.jar org.apache.tools.ant.launch.Launcher %1 %2 %3 %4 %5 %6