#!/bin/sh

head -1 2019Sep12_bulkValidate_Raw.csv > 2019Sep12_bulkValidate_Clean.csv
cat 2019Sep12_bulkValidate_Raw.csv |grep -v framesWithDifferences >> 2019Sep12_bulkValidate_Clean.csv

