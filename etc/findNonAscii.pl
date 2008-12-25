#!/usr/bin/perl -w

use strict;

foreach my $file ( @ARGV ) { 

	if ( ! open (FILE, $file) ) { die "Unable to open: $file: $!"; }

	while (<FILE>) { 
		chomp;
		if ( m!([^ \t\r\"\'0-9a-z_@\#,.^?()/:;<>=*+-])!i ) {
			print "$1 $file> $_\n";
		}
	}

	close(FILE);
}
		
		
