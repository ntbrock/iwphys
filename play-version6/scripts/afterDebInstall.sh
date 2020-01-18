#!/bin/sh
# 2015-Jun-30 Froehlich, fpm --after-install script

export DEB_NAME=iwphys-play

sudo service $DEB_NAME restart --force
