#!/bin/sh

rm -rf PerlProblemServer-09.25.04
./build.pl
mv dest PerlProblemServer-09.25.04
tar zcvf PerlProblemServer-09.25.04.tgz PerlProblemServer-09.25.04/
scp PerlProblemServer-09.25.04.tgz root@iwp2.ncssm.edu:/var/www/iwp2.ncssm.edu/

