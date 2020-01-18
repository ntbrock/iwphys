#!/usr/bin/env bash
# NOTE: Don't call this file directly!

# Authors:            Taylor Brockman <taylor.brockman@gmail.com>
# Revision History:   2015-08-25 - Initial commit

PLAY_USER=iwphys
PLAY_GROUP=iwphys
PLAY_PATH=/opt/iwphys-play

PLAY_PORT=8519
PLAY_CONFIG=/etc/iwphys-play/iwphys-play-application.conf
DAEMON=$PLAY_PATH/bin/iwphys-play
PIDFILE=$PLAY_PATH/RUNNING_PID
