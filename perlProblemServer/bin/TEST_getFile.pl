#!/usr/bin/perl -w

use strict;
use Frontier::Client;
use Data::Dumper;

$|++;


my ( $rpcUrl, $username, $password, $filename ) = @ARGV;;

if ( ! $rpcUrl ) { 
	print STDERR "Usage: TEST_getFile.pl [rpcUrl] [username] [password] [filename]\n";
	exit(0);
}

my $rpcClient = new Frontier::Client ( url => $rpcUrl );

if ( ! $rpcClient ) {
	print STDERR "Error: Unable to connect to: $rpcUrl\n";
	exit(1);
}


print STDERR "--- pps.authenticate\n";

# authenticate
my $authRet = $rpcClient->call("pps.authenticate", { username=>$username, password=>$password });
print STDERR Dumper ($authRet);
my $authkey = $authRet->{authkey};

print STDERR "--- pps.listContent\n";

# list directory contents
my @items = ( 1 );

foreach my $item ( @items ) {
	my $dirRet = $rpcClient->call("pps.listContent", { authkey=>$authkey, directory=>'/brockman@pinpoint.com/' });
	print STDERR Dumper ($dirRet);
}

print STDERR "--- pps.getFile\n";

# get the file
my $fileRet = $rpcClient->call("pps.getFile", { authkey=>$authkey,
												filename=>$filename } );

print STDERR Dumper ( $fileRet );

