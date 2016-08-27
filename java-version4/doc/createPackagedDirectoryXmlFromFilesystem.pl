#!/usr/bin/perl -w 
#
# 2007-Jul-27 brockman - Perl script that scans the current working directory
# and writes the directory.xml for the packaged problems utility in iwp3.

use strict;
use Data::Dumper;

my @files = split(/\n/,`find . |grep [.]iwp`);

my $struct = {};


print "Scanning the current working directory for .iwp files...\n";

foreach my $file ( @files ) {
	# print STDERR "File> $file\n";

	$file =~ s!^./!!; # unix find adds ./
	if ($file =~ m!^(.+?)/([^/]+)$!) {
		my $dir = $1;
		my $filepart = $2;
		my $description = "";

		# print STDERR "Dir> $dir  FilePart> $filepart\n";
		
		# read the desc

		if ( ! open ( PROB, $file ) ) { 
			die "Unable to open file: $file: $!";
		}
		# xml fileread hack  to get the descirption
		my $inDesc = 0;
		while ( <PROB> ) { 

			if ( m!<description>! ) { $inDesc = 1; }
			if ( m!</description>! ) { $inDesc = 0; }

			if ( $inDesc ) { 
				if ( m!<text>(.+)!msx ) { $description = $1; }
			}

		}
		close(PROB);

		$description =~ s![<][^>]+[>]!!g; # remove all extra tags.
		chomp $description; #remove trailing newlines
		chomp $description; #remove trailing newlines

		$struct->{$dir} = [] if ! $struct->{$dir};

		push(@{$struct->{$dir}}, { filepart => $filepart,
								   filename => $file,
								   description => $description,
								   dirname => $dir } );
	}


}

#------- now, build the output

my $dirCount = 0;
my $probCount = 0;

# print STDERR Dumper($struct);

if ( ! open XML, ">directory.xml" ) { 
	die "Unable to write: directory.xml: $!";
}

print XML <<EOT
<?xml version="1.0"?>
<directory>
\t<title>Packaged Problems for IWP</title>

EOT
	;

foreach my $dir ( keys(%{$struct}) ) { 

	print "Dir> $dir\n";

	my $dirCopy = $dir;
	print XML "\t<category>\n";
	print XML "\t\t<name>$dirCopy</name>\n";

	foreach my $problem (@{$struct->{$dir}}) { 

		print XML "\t\t<problemLink>\n";
		print XML "\t\t\t<filename>/packagedProblems/$problem->{filename}</filename>\n";
		print XML "\t\t\t<summary>$problem->{description}</summary>\n";
		print XML "\t\t</problemLink>\n";

		$probCount++;
	}

	$dirCount++;

	print XML "\t</category>\n";
}

print XML <<EOT
</directory>

EOT
	;


print "Wrote $dirCount directories and $probCount problems to: directory.xml\n";

print "Xmlwf (xml well formed?) output:\n";
print `xmlwf directory.xml`;

