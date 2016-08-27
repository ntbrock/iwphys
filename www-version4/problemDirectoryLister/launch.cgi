#!/usr/bin/perl -w

use strict;
use CGI;

my $version = 'iwp';

#--------------------------------------------------------------

my $query = new CGI;


my $LOCATION = "http://" . $ENV{'HTTP_HOST'} . $ENV{'SCRIPT_NAME'}; # !fcgi
my $self = $LOCATION;

#pop off the script name
my @pathParts = split (/\//, $LOCATION ); pop @pathParts; 
my $path = join ( '/', @pathParts ); $path .= '/';

my $problem = $query->param('problem');
my $mode = $query->param('mode');
my $studentValue = '';
if ( $mode eq 'animate' ) { $studentValue = 'true'; }

#-------------------------------------------------------------
print <<EOT
Content-type: text/html

<b>launch.cgi</b><br>
<applet code="edu.ncssm.iwp.bin.IWP_Applet" archive="$path$version/jars/iwp.jar" height="200" width="200">
<param name="problem" value="$path$problem">
EOT
    ;

    if ( $mode eq 'animate' ) {
	print ("<param name=\"student\" value=\"$studentValue\">");
   }
print <<EOT
</applet>
<br>
<br>

Problem: <a href="$path$problem">$path$problem</a><br>
[ <a href="$self?problem=$problem&mode=$mode">Refresh</a> ]
[ <a href="$path"."dirLister.cgi">Problem Listing</a> ] <br>
<br>

EOT
