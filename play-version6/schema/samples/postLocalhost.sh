#!/bin/sh

curl -X POST \
	-d @apparent-depth-6.iwp.json \
	-H "Content-type: application/json" \
	http://localhost:8470/animations/winters-ncssm-2009/apparent-depth-6.iwp

