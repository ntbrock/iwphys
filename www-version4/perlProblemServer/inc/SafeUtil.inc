#!/usr/bin/perl -w

# make the filenames safe.
# brockman + sweeney 06.12.04

package SafeUtil;
use strict;

sub safeFilename
{
	my ( $filename ) = @_;
	$filename =~ s/[^a-z0-9@._\-\/\\]//i;
	return $filename;
}


sub safeUsername
{
	my ( $username ) = @_;
	$username =~ s/[^a-z0-9@._\-]//i;

	# makesure that it has only 1 @
	# make sure that it has at least 1 dot after the at

	# logic not quite complete here, but it'll get us some distance.
	if ( ! ( $username =~ m/@/ ) ) { return ''; }
	if ( ! ( $username =~ m/[.]/ ) ) { return ''; }

	return $username;
}


sub safePath
{
	my ( $path ) = @_;

	if ( ! $path ) { $path = '/'; }
	if ( ! ( $path =~ m!/$! ) ) { $path .= '/' }

	# if ( ! ( $path =~ m!^/! ) ) { $path = '/' . $path }

	return $path;
}


sub previousDirectory
{
	my ( $path ) = @_;

	if ( $path eq '/' ) { return $path; }
	my @parts = split ( /\//, $path );
	pop @parts;

	my $finished = join ('/', @parts );

	return safePath($finished);
}


sub extractFilename 
{
	my ( $fullFilename ) = @_;

	my @parts = split ( /\//, $fullFilename );
	my $filename = pop @parts;

	return safeFilename($filename);
}

sub extractPath
{
	my ( $fullFilename ) = @_;

	my @parts = split ( /\//, $fullFilename );
	pop @parts;
	my $finished = join ('/', @parts );

	return safePath($finished);
}


sub extractWebPath
{
	my ( $fullFilename ) = @_;

	my @parts = split ( /\//, $fullFilename );
	pop @parts;
	my $finished = join ('/', @parts );

	return $finished;
}

sub guaranteeExtension
{
	my ( $filename, $extension ) = @_;

	# if ( ! ($full_newFilename =~ m/[.]iwp$/i) ) { $full_newFilename .= ".iwp"; }

	if ( $filename =~ m!$extension$! ) { 
	} else { 
		$filename = $filename . $extension;
	}

	return $filename;
}

1;

