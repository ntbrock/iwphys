#!/bin/sh

#~/brainps/git/idship-cdns-ops/castToPortChecksum.sh iwp-play-version6
# 8470

sbt -mem 2048 -J-Xms512m -J-Xmx1536m -Dhttp.port=8470 run
