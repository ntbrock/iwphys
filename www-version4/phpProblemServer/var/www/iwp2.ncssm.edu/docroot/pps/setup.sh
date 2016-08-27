#!/bin/sh

echo "Setting permissions so that suexec likes the webInterface.cgi script"

chmod g-w .

chmod g-w ./webInterface.cgi

