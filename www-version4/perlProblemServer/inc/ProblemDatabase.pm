#!/usr/bin/perl -w
#
# ProblemDatabase.pm
#
# This file is the interface into the problem database for the 
# web service + the xml-rpc service. It has simple load, save,
# and meta functions.
#
# brockman 03.19.04
#
#-------------------------------------------------------------------------

use strict;

package ProblemDatabase;

use Data::Dumper;
use POSIX qw(strftime);

# require 'inc/util.inc';
#-------------------------------------------------------------------------

my $TOP_LEVEL_USERS_DIR = "Users";
my $TOP_LEVEL_EXAMPLES_DIR = "Sample Problems";

my $ALPHA_FILE_ORDERING = 1; #brockman 2005-Jul-10
my $GUARANTEE_EXTENSION = ".iwp"; #whenever a file is written or moved.

my $EXPERIMENTAL_MY_PROBLEMS = 0; # This is the 'my Problems' directory.

my $SHOW_VIRTUALS_IN_ROOT = 0; # This turns off the /Users and /Examples
# in the root folder, so they're not doubled up.
my $SHOW_REALDIRS_IN_ROOT = 1; # turn this on to show the real folders 
# in the root directory.

sub new 
{
    my ( $packageName, $unixDirectory, $userProblemDirectory ) = @_;

	if ( ! $unixDirectory ) { die "Unspecified unixDirectory"; }

	my $self = { unixDirectory => $unixDirectory,
				 userProblemDirectory => $userProblemDirectory };
    return bless $self, $packageName;
}

#-------------------------------------------------------------------------

sub makeUserDirectory
{
	my ( $self, $username ) = @_;

	my $directory = SafeUtil::safeFilename ( $self->{unixDirectory} . '/' . $self->{userProblemDirectory} . '/' . $username );

	debug ("[ProblemDatabase.makeUserDirectory] Making Directory: $directory\n");
 	system("mkdir -p \"$directory\"");

	# copy in the samplee problems
	system("cp -f \"etc/problems/Sample\ Problems/Example\ DVAT\ Target.iwp\" \"$directory\"" );

}



sub listUserDirectoryContent
{
	my ( $self, $username ) = @_;

	return _listDirectoryContent ( $self, $self->{unixDirectory},
								   $self->{userProblemDirectory} . '/' . $username, $username );
}


sub listDirectoryContent
{
	my ( $self, $localDirectory, $username ) = @_;

	return _listDirectoryContent ( $self, $self->{unixDirectory}, $localDirectory, $username );
}


sub _listDirectoryContent
{
	my ( $self, $baseDir, $localDirectory, $username ) = @_;

	# print STDERR "[ProblemDatabase._listDirectoryContent] baseDir: $baseDir  localDirectory: $localDirectory\n";

	# do the complete file listing for everything in this sub
	# dir. How do we differentiaate if it's a file or a subdir?

	my @files;

	# magic directories.
	# not quite working yet. hrm. how do I do this.....

	if ( $SHOW_VIRTUALS_IN_ROOT == 1 || $localDirectory ne "/" ) { 

		if ( $EXPERIMENTAL_MY_PROBLEMS ) { 
			push @files, { type => 'directory',
						   filename => '/My Problems',
						   relativePath => '/My Problems/',
						   sortOrder => 1 };
		}

		push @files, { type => 'directory',
					   filename => '/Sample Problems',
					   relativePath => '/Sample Problems/',
					   sortOrder => 2 };

		push @files, { type => 'directory',
					   filename => '/Users',
					   relativePath => '/Users/',
					   sortOrder => 3 };
		
	}


	my $directory = $baseDir . '/' . $localDirectory;
	$directory =~ s!/[/]+!/!g; # subout unsightly 2x slashes.
	$directory =~ s!/$!!; # take of ending slash

	if ( ! opendir ( DIR, $directory ) ) { die "Unable to read dir: $directory"; }

	# debug ( 'listDirectoryContent', "Reading dir: $directory" );

	while ( my $file = readdir ( DIR ) ) { 

		my $filename = $directory . '/' . $file;


		# print STDERR "[_listDirectoryContent] File: $file  Filename: $filename\n";

		# skip over dot prepended filenames. hidden.
		if ( $file =~ m/^[.]/i ) { next; }

		if ( -d $filename ) { 

			if ( ( $localDirectory eq '/' && $SHOW_REALDIRS_IN_ROOT == 1 ) ||
				 $localDirectory ne '/' ) { 

				push @files, { type => 'directory', filename => $file,
							   relativePath => relativePath ( $self, $filename ) . '/' };
			}
				
		} elsif ( $file =~ m/$GUARANTEE_EXTENSION$/i ) { 
			push @files, $self->loadMetaInfo ( $filename, $file );
		}
	}

	closedir ( DIR );


	# sort the entries by type then filename.
	if ( $ALPHA_FILE_ORDERING ) { 
		@files = sort sort_filesByTypeThenAlpha @files;
	}

	$localDirectory =~ s!/[/]+!/!g; # subout unsightly 2x slashes.
	$localDirectory =~ s!/$!!; # take of ending slash

	return  $localDirectory, \@files;
}


# brockman 2005-Jul-09. This affects both web + applet.
sub sort_filesByTypeThenAlpha
{
	#print STDERR "sort_filesByTypeThenAlpha\n";
	#print STDERR Dumper ( $a );
	#print STDERR Dumper ( $b );

	if ( $a->{type} eq $b->{type} ) { 
		if ( $a->{type} eq "directory" ) {
			# order directories by sort order, then alpha.
			if ( $a->{sortOrder} == 0 && $b->{sortOrder} > 0 ) { 
				return 1;
			} elsif ( $a->{sortOrder} > 0 && $b->{sortOrder} == 0 ) { 
				return -1;
			} elsif ( $a->{sortOrder} > 0 && $b->{sortOrder} > 0 ) { 
				# if sort order defined, use that.
				return $a->{sortOrder} <=> $b->{sortOrder};
			} else { 
				# if no sort order defined, order by alpha
				return $a->{filename} cmp $b->{filename};
			}

		} else { 
			# order 
			return $a->{filename} cmp $b->{filename};
		}
	} else {
		return $a->{type} cmp $b->{type};
	}
}

#-------------------------------------------------------------------------

sub writeFile
{
	my ( $self, $username, $file, $content ) = @_;

	
	# brockman 2005-Jul-10. I'm refactoring / abstracting the way that
	# permissions work. Get rid of username matching inside of the file. 
	# if ( $fileInfo->{username} ne $username ) { die "User does not match username of file"; }
	# go with a hottie new, abstracted interface.
	if ( ! userOwnsFile ( $username, $file ) ) { 
		die "$username does not have appropriate permission to write to the file: $file";
	}


	my $filename = SafeUtil::safeFilename ( $self->{unixDirectory} . '/' . $file );
	$filename = SafeUtil::guaranteeExtension ( $filename, $GUARANTEE_EXTENSION );

	if ( ! open ( OUTPUT, ">$filename" ) ) { die "Unable to write: $filename: $!"; }
	print OUTPUT $content;
	close OUTPUT;

	# success.
}


#
# File: The path put in here shoud be relative to the top of the 
# problems directory, not fully qualified. The system base path is
# tacked on by this script.

sub loadFile
{
	my ( $self, $file ) = @_;

	# calculate the full filename
	# files got absolute all of a sudden

	# no permissions check here, as we should be able to read everybody's.

	my $filename = $self->{unixDirectory} . '/' . $file;
	# my $filename = $file;
	$filename =~ s!/[/]+!/!g; # subout unsightly 2x slashes.
	$filename =~ s!/$!!; # take of ending slash


	my $fileContent = '';
	my $fileInfo = $self->loadMetaInfo ( $filename, $file, \$fileContent );

	$fileInfo->{file_data} = $fileContent;
	return $fileInfo;
}


sub renameFile
{
	my ( $self, $oldFilename, $newFilename, $username ) = @_;
	# print STDERR "[ProblemDatabase.renameFile] TODO\n";

	my $fileInfo = loadFile ( $self, $oldFilename );

	# brockman 2005-Jul-10. I'm refactoring / abstracting the way that
	# permissions work. Get rid of username matching inside of the file. 
	# if ( $fileInfo->{username} ne $username ) { die "User does not match username of file"; }
	# go with a hottie new, abstracted interface.
	if ( ! userOwnsFile ( $username, $oldFilename ) ) { 
		die "$username does not have appropriate permission to delete the file: $oldFilename";
	}

	if ( ! userOwnsFile ( $username, $newFilename ) ) { 
		die "$username does not have appropriate permission to create the file: $oldFilename";
	}


	$newFilename = SafeUtil::safeFilename ( $newFilename );

	# This was changed, but I think this is the way it shuld be. brock 2005-jul-9
	#my $full_oldFilename = $oldFilename;
	#my $full_newFilename = $newFilename;
	my $full_oldFilename = $self->{unixDirectory} . '/' . $oldFilename;
	my $full_newFilename = $self->{unixDirectory} . '/' . $newFilename;


	# postfix an .iwp on the file if it's not there.
	$full_newFilename = SafeUtil::guaranteeExtension ( $full_newFilename, $GUARANTEE_EXTENSION );

	print STDERR "[ProblemDatabase.renameFile] full_old: $full_oldFilename, full_new: $full_newFilename\n";

	system("mv \"$full_oldFilename\" \"$full_newFilename\"");
}


sub deleteFile
{
	my ( $self, $oldFilename, $username ) = @_;

	my $fileInfo = loadFile ( $self, $oldFilename );

	# brockman 2005-Jul-10. I'm refactoring / abstracting the way that
	# permissions work. Get rid of username matching inside of the file. 
	# if ( $fileInfo->{username} ne $username ) { die "User does not match username of file"; }
	# go with a hottie new, abstracted interface.
	if ( ! userOwnsFile ( $username, $oldFilename ) ) { 
		die "$username does not have appropriate permission to delete the file: $oldFilename";
	}

	# This is needed. brockman 2005-Jul-01
	my $full_oldFilename = $self->{unixDirectory} . '/' . $oldFilename;

	# dont' really delete, just move to /tmp. 
	my $filePart = SafeUtil::extractFilename ( $oldFilename );
	my $tmpFile = "/tmp/IWP_DELETED_" . $username . "_" . $filePart;

	system ("mv \"$full_oldFilename\" \"$tmpFile\"");
}



sub loadMetaInfo
{
	my ( $self, $fullFilename, $partFilename, $fileContentRef ) = @_;

	my $ret = { type => 'problemFile',
				filename => $partFilename,
				fullFilename => $fullFilename,
				relativePath => relativePath ( $self, $fullFilename ),
				lastModified => '',
				username => '',
				description => 'TODO' };

	debug ( 'extractMetaInfo', "parsing: $fullFilename" );

	# smash all the content onto 1 line.
	my $content = '';
	if ( ! open ( FILE, $fullFilename ) ) { die "Unable to read: $fullFilename"; }
	while (<FILE>) { 
		chomp;
		$content .= ' ' . $_;

		# we can also capture the file content in-line, whcih makes
		# it efficient to load files.
		if ( $fileContentRef ) { ${$fileContentRef} .= $_ . "\n"; }
	}
	close FILE;
	# study $content; I don't know if this necessarily speed up the
	# regular expressions.


	# get out the last modified

	my ($dev,$ino,$mode,$nlink,$uid,$gid,$rdev,$size, $atime,$mtime,$ctime,$blksize,$blocks)
		= stat($fullFilename);

	# $ret->{lastModified} = localtime ( $mtime );
	$ret->{lastModified} = strftime "%a %b %e %H:%M:%S %Y", localtime ( $mtime );

	# parse the username field
	if ( $content =~ m!<username>([^<>]+)</username>!mig ) { 
		$ret->{username} = $1;
	}

	# and the description field
	if ( $content =~ m!<description>\s*<text>([^<>]+)</text>\s*</description>!mig ) { 
		$ret->{description} = $1;
	}



	return $ret;
}




sub debug
{
	my ( $function, $param ) = @_;
	# print STDERR "[debug:ProblemDatabase.$function] " . Dumper ( $param );
}



sub relativePath
{

	my ( $self, $path ) = @_;

   	$path =~ s!$self->{unixDirectory}!!i;
	return $path;
}



#----------------------------------------------------------------
# NEW: permissions abstraction.
#
# Ownership check.
# brockman 2005-Jul-10
#

sub userOwnsFile
{
	my ( $username, $oldFilename ) = @_;

	# for now, we're guaranteed the path names of:
	# /User/email@company.com/.*

	if ( $oldFilename =~ m!^/$TOP_LEVEL_USERS_DIR/$username/! ) {
		return 1;
	} else { 
		return 0;
	}

}



1;

