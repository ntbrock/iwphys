#!/bin/sh

curl -X POST \
	-d @coulombslaw03.iwp.json \
	-H "Content-type: application/json" \
	http://localhost:8470/animations/winters-ncssm-2009/coulombslaw03.iwp

