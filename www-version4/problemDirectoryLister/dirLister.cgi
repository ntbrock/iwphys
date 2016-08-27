#!/usr/bin/perl -w

use strict;
use CGI;


print "Content-type: text/html\n\n";
print "<b>dirLister.cgi</b><br>\n";

opendir ( DIR, "./problems" );


print "<table border=\"1\" cellpadding=\"3\" cellspacing=\"0\">\n";
print "<tr bgcolor=\"#cccccc\"><th><b>Filename</b></th><th><b>Designer</b></th><th><b>Student</b></th></tr>\n";

while ( my $filename = readdir DIR ) {

	if ( $filename =~ m/^[.]/i ) { next; }
	
	my $designLink = "launch.cgi?problem=problems/$filename&mode=design";
	my $studentLink = "launch.cgi?problem=problems/$filename&mode=animate";

	print "<tr><td>$filename</td><td align=\"center\"><a href=\"$designLink\">Design</a></td><td align=\"center\"><a href=\"$studentLink\">Animate</a></td></tr>\n";
}

closedir DIR;


print "</table>\n";
