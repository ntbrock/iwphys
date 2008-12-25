#!/usr/bin/perl -w
#
# Author: brockman 10.02.03
#  (Based on xmlrpc_cgi.c by Eric Kidd <http://xmlrpc-c.sourceforge.net/>.)
#

use strict;
use Frontier::RPC2;
use Frontier::Daemon;
use CGI::Fast;

#-------------------------------------------------------------------------
# Example Invocation
# 
# sub sumAndDifference {
# 	my ($x, $y) = @_;
# 	return {'sum' => $x + $y, 'difference' => $x - $y,
# 		'x' => $x, 'y' => $y};
# }
#
# my $methodHash = { 'sample.sumAndDifference' => \&sumAndDifference };
#
# xmlrpc_handle ( $methodHash );
# # OR
# xmlrpc_fastCGI_handle ( $methodHash );
#
#-------------------------------------------------------------------------


#==========================================================================
#  FastCGI Support
#==========================================================================
# This method allows stateful, fastCGI access to the xmlrpc receiver.
#
sub xmlrpc_fastCGI_handle
{
	my ( $methods ) = @_;
	# main fastCGI Loop

	while ( FCGI::accept() >= 0 ) 
	{
		xmlrpc_handle ( $methods );
	}
}



#==========================================================================
#  Single-shot CGI Support
#==========================================================================
#  Simple CGI support for Frontier::RPC2. You can copy this into your CGI
#  scripts verbatim, or you can package it into a library.
#  (Based on xmlrpc_cgi.c by Eric Kidd <http://xmlrpc-c.sourceforge.net/>.)

sub xmlrpc_handle
{
	my ($methods) = @_;

	# Get our CGI request information.
	my $method = $ENV{'REQUEST_METHOD'};
	my $type = $ENV{'CONTENT_TYPE'};
	my $length = $ENV{'CONTENT_LENGTH'};
	my $queryString = $ENV{'QUERY_STRING'};

	# print STDERR "[xmlrpc.cgi] METHOD: $method  TYPE: $type   LENGTH: $length\n";
	# print STDERR "[xmlrpc.cgi] QUERY_STRING: $queryString\n";

	# Perform some sanity checks.
	http_error(405, "Method Not Allowed") unless $method eq "POST";

	# this dosen't work w/ FastCGI
	http_error(400, "Incorrect Content Type") unless $type eq "text/xml";

	http_error(411, "Length Required") unless $length > 0;

	# Fetch our body.
	my $body;
	my $count = read STDIN, $body, $length;

	# print STDERR "[xmlrpc_cgi] length: $length  count: $count\n$body\n";

	http_error(400, "Mismatched Content Length") unless $count == $length; 

	# print STDERR "[xmlrpc.cgi] REQUEST_BODY:\n$body\n";

	# Serve our request.
	my $coder = Frontier::RPC2->new;
	send_xml($coder->serve($body, $methods));
}


#----------------------------------------------------------------------------
# Send an HTTP error and exit.

sub http_error ($$) {
	my ($code, $message) = @_;

	print STDERR "[xmlrpc_cgi.http_error] ERROR $code :: $message\n";

	print "Status: $code $message\n";
	print "Content-type: text/html\n";

	print "\n";
	print "<title>$code $message</title>\n";
	print "<h1>$code $message</h1>\n";
	print "<p>Unexpected error processing XML-RPC request.</p>\n";

	die "http_error: $message";
}


# Send an XML document (but don't exit).
sub send_xml {
	my ($xml_string) = @_;
	my $length = length($xml_string);

	# print STDERR "[xmlrpc.cgi] RESPONSE_BODY:\n$xml_string\n";

	# We want precise control over whitespace here.
	print "Status: 200 OK\n";
	print "Content-type: text/xml\n";
	print "Content-length: $length\n";
	print "\n";
	print $xml_string;
}


#==========================================================================
#  Standalone XML-RPC Server: For when apache is not an option
#==========================================================================

sub xmlrpc_standAlone
{
	my ( $methods, $port ) = @_;

	while ( ! ( my $daemon = new Frontier::Daemon ( LocalPort => $port,
													methods => $methods ) ) ) {
		
		# error starting server
		print "Unable to start server on port: $!. sleep(2) then retry.\n";
		sleep (2);
	}
}


1;
