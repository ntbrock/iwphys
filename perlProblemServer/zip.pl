#!/usr/bin/perl -w

use strict;

my ( $date ) = @ARGV;
my $dateString = '';
if ( $date ) { $dateString = "-$date" }

system ("./build.pl");
system ("mv dest PerlProblemServer" . $dateString );
system ("tar zcvf PerlProblemServer". $dateString .".tgz PerlProblemServer" . $dateString);

