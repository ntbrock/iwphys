#!/bin/sh

#~/brainps/git/idship-cdns-ops/castToPortChecksum.sh iwp-play-version6
# 8470

sbt -J-Xms512m -J-Xmx800m -Dhttp.port=8470 run
