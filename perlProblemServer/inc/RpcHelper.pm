#!/usr/bin/perl -w
use strict;

package RpcHelper;

# Check a parameter hash for a key and return it if it exists, or die 
# with a 'missing parameter' error message.

sub param
{
	my ( $hash, $key ) = @_;

	my $value = $hash->{$key};

	if ( ! $value ) { 
		die "Missing parameter: '$key'";
	} else {
		return $value;
	}

}



1;
