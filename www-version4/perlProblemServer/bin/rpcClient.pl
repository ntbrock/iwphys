#!/usr/bin/perl -w

use strict;
use Frontier::Client;
use Data::Dumper;

$|++;

if ( @ARGV < 1 ) {
	print STDERR "Usage: rpcClient.pl [rpcURL]\n";
	exit(1);
}

my $rpcUrl = $ARGV[0];

my $rpcClient = new Frontier::Client ( url => $rpcUrl );

if ( ! $rpcClient ) {
	print STDERR "Error: Unable to connect to: $rpcUrl\n";
	exit(1);
}

# STDIN LOOP

my $lastLine = '';

prompt();
while (<STDIN>) {
	chomp;

	if ( $_ ) { 

		my $commandLine = $_;

		# if the user enters a bang sign, he wants to use the last command
		if ( $commandLine eq '!' ) {
			$commandLine = $lastLine;
			prompt(); print "$commandLine\n";		
		}

		my ( $command, @args ) = split ( / /, $commandLine );
		# do we have to divide up each arg?
		for ( my $c = 0; $c < @args; $c++ ) { 
			my $arg = $args[$c];

			if ( $arg =~ m!^\{(.+)\}! ) { 
				my $hash = {};
				# this argument is a hash
				print STDERR "piece: '$1'\n";

				foreach my $csv ( split (/,/, $1) ) {
					if ( $csv =~ m!(.+?)=>(.+)! ) { 
						$hash->{$1} = $2;
					}
				}
				$args[$c] = $hash;
			}
		}

		print STDERR "Args: " . Dumper ( \@args );

		processCommand ( $command, @args );
		$lastLine = $commandLine;
	}
	prompt();
}

print "\n";


sub processCommand 
{
	my ( $command, @args ) = @_;

	my $rpcReturn;
	my $ret;

	if ( ! @args ) {
		my $ret = eval { $rpcReturn = $rpcClient->call ( $command ); }
	} elsif ( @args <= 1 ) {
		my $ret = eval { $rpcReturn = $rpcClient->call ( $command, $args[0] ); }
	} elsif ( @args == 2 ) {
		my $ret = eval { $rpcReturn = $rpcClient->call ( $command, $args[0], $args[1] ); }
	} else {
		my $ret = eval { $rpcReturn = $rpcClient->call ( $command, @args ); }
	}

	if ( ! $ret && ! $rpcReturn ) {
		print "ERROR: $@\n";
	} else { 
		print "" . ( Dumper $rpcReturn ) . "\n";
	}

}


sub prompt { print "rpc> "; }

