#!/usr/bin/perl -w
use strict;

# This will parse a key: value text file w/ comments and turn it into a
# hashref.
# brockman 09.22.03

my $MASTER_CONFIG = "/usr/local/tribble/etc/master.cfg";
my $CATALOG_SINGLETON = masterConfig ( );


sub masterConfig
{
	# this could store a singleton instance of the master config
	# hash, thus saving the file load multiple times per 
	# process instance.
	
	if ( ! $CATALOG_SINGLETON ) {
		# print STDERR "[cfgParser] Parsing Config File\n";
		$CATALOG_SINGLETON = cfgParse ( $MASTER_CONFIG );
	} else {
		# print STDERR "[cfgParser] USING SINGLETON\n";
	}

	return $CATALOG_SINGLETON;
}

sub cfgParse
{
	my ( $filename ) = @_;
	return loadConfig ( $filename );
}


sub loadConfig
{
	my ( $filename ) = @_;

	# print STDERR "[cfgParser.loadConfig] loading: $filename\n";

	my @lines = readLines ( $filename );
	my $vars;

	foreach my $line ( @lines ) {

		if ( ! $line or
			 $line =~ m/^[\#]/ or
			 $line =~ m!^//! ) {
			# blank line 
		} elsif ( $line =~ m/(.*?)[=]\s*(.*)/) {
			# print STDERR "[cfgParser] $1 => $2\n";
			$vars->{$1} = $2;
		}
	}

	return $vars;
}

sub cloneHashRef
{
	my ( $sourceHash ) = @_;
	my %solidHash = %{$sourceHash};

	return \%solidHash;
}


# This will re-save any modifications to the configuration, trying to preserve
# the file structure as well as possible. new additions will be written to the 
# bottom.
# WARNING: This method will mangle the contents of $config. Reload it.
# TODO: Could put in a cloneHash method to preserve it.

sub saveConfig
{
	my ( $filename, $configRef ) = @_;

	my $config = cloneHashRef ( $configRef );

	my @lines = readLines ( $filename );

	# open the file for writing
	if ( ! open ( OUT, ">$filename" ) ) { die "Unable to write: $filename: $!"; }

	foreach my $line ( @lines ) {

		if ( $line =~ m/(.+?)([=]\s*)(.*)/) {

			my $key = $1;
			my $sep = $2;
			my $oldValue = $3;
			my $newValue = $config->{$key};
			if ( ! $newValue ) { $newValue = ''; }

			# print STDERR "[cfgParser] newValue: $newValue  oldValue: $oldValue\n";

			# see if we've updated it in memory.
			if ( $oldValue && $newValue eq $oldValue ) {
				# it's the same, leave it.
			} else {
				# it's a new value, re-update the line
				$line = $key . $sep . $newValue;
			}

			# let's remove the key config the config, so that it's not written
			# as a new variable
			undef $config->{$key};
		}
		
		print OUT $line . "\n";
	}

	# need to write out any new variables
	foreach my $key ( keys %{$config} ) {
		if ( $config->{$key} ) {
			print OUT $key . '='. $config->{$key} . "\n";
		}
	}

	close OUT;

}



# read in all the lines of a file, and return an array containing their data.

sub readLines
{
	my ( $filename ) = @_;

	# read in all the raw lines of the orig config.
	if ( ! open ( CFG, $filename )) { 
		die "Unable to open config file: $filename: $!";
	}

	my @lines;

	while ( <CFG> ) {
		chomp;
		push @lines, $_;
	}

	close CFG;

	return @lines;
}



1;

