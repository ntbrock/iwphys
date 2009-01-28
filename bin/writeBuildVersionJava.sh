#!/bin/sh

MAJOR_VERSION="4.0.4 Public Release"
OUT_FILE="./src/edu/ncssm/iwp/util/buildversion/BuildVersion.java"

echo "package edu.ncssm.iwp.util.buildversion;" >$OUT_FILE
echo "public class BuildVersion { public static final String VERSION = \"$MAJOR_VERSION `date "+%Y-%b-%d %T"` `whoami`@`hostname`\"; }" >>$OUT_FILE




