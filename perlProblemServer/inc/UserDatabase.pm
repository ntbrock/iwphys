#!/usr/bin/perl -w
#
# UserDatabase.pm
#
# This file is the interface into the user database for the 
# web service + the xml-rpc service. It has simple load, save,
# and meta functions.
#
# brockman 03.19.04
#
#-------------------------------------------------------------------------

use strict;

package UserDatabase;

#-------------------------------------------------------------------------


sub new 
{
	# TODO: we could create a 'user database' handle that requires
	# a username passed into it at creation time. Then, every file
	# that we loaded or saved, we could check the user permissions 
	# basqed on the soft settings in the XML files themselves.

    my ( $packageName, $baseDirectory, $userProblemDirectory, $username ) = @_;

	if ( ! $baseDirectory ) { die "Unspecified baseDirectory"; }

	my $self = { baseDirectory => $baseDirectory,
				 userProblemDirectory => $userProblemDirectory };

    return bless $self, $packageName;
}

#-------------------------------------------------------------------------

sub listUsers
{
	my ( $self ) = @_;

	# do the complete file listing for everything in this sub
	# dir. How do we differentiaate if it's a file or a subdir?

	my @users;

	my $directory = $self->{baseDirectory};

	if ( ! opendir ( DIR, $directory ) ) { die "Unable to read dir: $directory"; }
	foreach ( my $file = readdir ( DIR ) ) { 
		debug ( 'listDirectoryContent. file: $file' );

		# if it's a directory....

		if ( $file =~ m/[.]user$/i ) {
			push @users, $self->loadFile ( $file );
		}
	}

	return @users;
}

sub loadUser
{
	my ( $self, $username ) = @_;

	# what file is this user?
	my $filename = $self->{baseDirectory} . "/" . $username . ".user";
	# $filename = s!//!/!g;

	print STDERR "[UserDatabase.loadUser] username: $username  filename: $filename\n";

	return $self->loadFile ( $filename );
}

#---------------------------------------------------------------------------------------------
# brockman + sweeney 06.12.04
sub newUser
{
	my ( $self, $username, $password, $passwordConfirm, $realName, $organization ) = @_;
	
	if ( $username ne SafeUtil::safeUsername ( $username ) ) { 
		die "Invalid Email Address. Use plain characters and ensure a @ and domain";
	}

	if ( ! $password || ! $passwordConfirm ) {
		die "Password was blank";
	}

	if ( $password ne $passwordConfirm ) { 
		die "Passwords do not match";
	}

	# what file is this user?
	my $filename = SafeUtil::safeFilename ( $self->{baseDirectory} . "/" . $username . ".user" );

	# check for existing user files.
	eval {
		my $existingUser = loadUser ( $self, $username );
	};
	if ( ! $@ ) { die "User already exists"; }

	print STDERR "[UserDatabase.newUser] CREATING NEW USER FILE\n";

	my $user = { username => $username,
				 password => $password,
				 realName => $realName,
				 organization => $organization};

	$self->writeFile ( $filename, $user );

	# create the user's problem directory back up a layer in the stack. see webInterface.cgi
	

	# just reload the file for sanity.
	return $self->loadFile ( $filename );
}




sub authenticateUser
{
	my ( $self, $username, $password ) = @_;

	my $user = $self->loadUser ( $username );

	if ( ! passwordValidate ( $user->{password}, $password ) ) {
		die "Invalid Password for user: $username";
	}

	return $user;
}

sub passwordValidate
{
	my ( $known, $unknown ) = @_;

	print STDERR "[UserDatabase.passwordValidate] known: $known,  unknown: $unknown\n";

	return ( $known eq $unknown );
}


#-------------------------------------------------------------------------

sub writeFile
{
	my ( $self, $filename, $content ) = @_;
	
	# TODO: check permissions
	# this is weird. 2005-July-09. brockman

	eval { &ConfigParser::saveConfig ( $filename, $content ); };

	if ( $@ ) {
		die "Unable to save file: '$filename': $@";
	}
}

sub loadFile
{
	my ( $self, $filename ) = @_;
	
	my $user;

	eval { $user = new ConfigParser ( $filename ); }; 

	if ( $@ ) {
		die "Unknown user: $filename (no data file): $@";
	}
	
	return $user;
}

sub deleteFile
{
	my ( $self, $filename ) = @_;

	cmd ( "rm -f $filename" );
}

#-------------------------------------------------------------------------

sub cmd
{
	my ( $cmd ) = @_;

	print STDERR "UserDatabase.CMD> $cmd\n";
	system ( $cmd );
}


sub debug
{
	my ( $function, $param ) = @_;
	print STDERR "[debug:UserDatabase.$function] " . Dumper ( $param );
}


1;

