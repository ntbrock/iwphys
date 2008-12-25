#!/usr/bin/perl -w 
use strict;

# 2008-Jan-25 Brockman, NCSSM IWP. 
# CGI script to maintain backwards compatibility with iwp2 legacy urls.
# This script just sends a 302 to the new PHP location.

# sanity check
if ( ! $ENV{HTTP_HOST}) { print "Content-type: text/html\n\nError: No Environment HTTP_HOST\n"; exit(1); }
if ( ! $ENV{REQUEST_URI} ) { print "Content-type: text/html\n\nError: No Environment REQUEST_URI\n"; exit(1); }
if ( ! $ENV{SCRIPT_NAME} ) { print "Content-type: text/html\n\nError: No Environment SCRIPT_NAME\n"; exit(1); }

# construct the 302.
my $host = $ENV{HTTP_HOST};
my $uri = $ENV{REQUEST_URI};

# Non 80/443 off port?
my $portNum = $ENV{SERVER_PORT};
my $portPart = '';
if ( $portNum && $portNum ne "80" && $portNum ne "443" ) { $portPart = ':' + $portNum; }

# ssl?
my $sslFlag = $ENV{HTTPS};
my $sslPart = '';
if ( $sslFlag ) { $sslPart = 's'; }

# replace my script name. all orrances of scriptName.cgi are not scriptName.php
my @scriptParts = split(/\//, $ENV{SCRIPT_NAME});
my $scriptName = pop(@scriptParts);
my $phpScriptName = $scriptName;
$phpScriptName =~ s/[.]cgi/.php/;
$uri =~ s/$scriptName/$phpScriptName/;

my $url = 'http' . $sslPart . '://' . $host . $uri;

# print the URL as part of the header.
# print "Content-type: text/html\n\n";

print "Location: $url\n\n";

### 
# finished.
