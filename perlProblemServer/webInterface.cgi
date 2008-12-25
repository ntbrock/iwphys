#!/usr/bin/perl -w -Iinc
#--------------------------------------------------------------------

use strict;

use CGI;
use Text::Template;
use Data::Dumper;

use ConfigParser;
use UserSession;
use UserDatabase;
use ProblemDatabase;

require 'inc/SafeUtil.inc';

my $LOCATION = "http://" . $ENV{'HTTP_HOST'} . $ENV{'SCRIPT_NAME'}; # !fcgi
my $scriptBase = $LOCATION;
$scriptBase =~ s!/[^/]+$!!;

# this will elave the 'previous directory' off of the web ui. 2005-Jul-9 brock
my $SHOW_DOT_DIRECTORIES = 0;

#--------------------------------------------------------------------
# config / database loading

my $config = new ConfigParser ('etc/server.cfg');
my $pageTitles = new ConfigParser ('etc/pageTitles.cfg');

my $userDatabase = new UserDatabase ( $config->{userDirectory}, $config->{userProblemDirectory} );
my $problemDatabase = new ProblemDatabase ( $config->{problemDirectory}, $config->{userProblemDirectory} );

#--------------------------------------------------------------------
# CGI Loading

# The user will be dispatched to tmpl/$action.tmpl by default. You
# can change this destination page 

my $query = new CGI;

my $action = $query->param('action');

# browse hack. YOu can browse by just setting the PATH_INFO
# TODO: add in the detection of the .iwp in the pathinfo for animating / editing
my $pathInfo = $ENV{'PATH_INFO'};
if ( $pathInfo ) { $action = 'browse'; }
if ( $pathInfo && $pathInfo =~ m![.]iwp$!i ) { $action = 'problemAnimate'; }


if ( ! $action ) { $action = 'index'; }
my $dispatchTo = $action;
my $redirect = 0;


#--------------------------------------------------------------------
# template variable fill-in

$OUTPUT::action = $action;
$OUTPUT::color->{table} = "#cccccc";
# TODO: use the full LOCATION method
$OUTPUT::self = $LOCATION;
$OUTPUT::selfPath = SafeUtil::extractWebPath ( $LOCATION );
$OUTPUT::rpcHost = $scriptBase . "/problemServer.cgi";

#--------------------------------------------------------------------
# Page Title

setPageTitle ( $action );


#--------------------------------------------------------------------
# USER SESSIONING

# check the cookie + load the user session
my $userSession = new UserSession ( $config->{sessionDataDirectory},
									$config->{sessionLockDirectory} );

my $sessionID = $userSession->getSessionIDFromCookies();
#print STDERR "[webInterface] getSessionID: $sessionID\n";

#### ---- THIS IS JUST FOR SESSION DEBUGGING
#print STDERR "[webInterface] userSession->getAttribute(_session_id) " . $userSession->getAttribute('_session_id') . "\n";

my $accessCount = 0;
if ( $userSession->getAttribute('accessCount') ) {
	$accessCount = $userSession->getAttribute('accessCount');
}
$accessCount++;
$userSession->setAttribute ( 'accessCount', $accessCount );

###-----

my $sessionCopy = $userSession->sessionCopy();
$OUTPUT::session = $sessionCopy;
$OUTPUT::sessionID = $sessionID;

# pull the user object out if it exists.
if ( $sessionCopy->{user} ) { $OUTPUT::user = $sessionCopy->{user}; }

#--------------------------------------------------------------------





# action specific data loads, actions

if ( $action eq "authenticate" ) { 

	my $username = SafeUtil::safeUsername ( $query->param('username') );
	my $password = $query->param('password');

	if ( ! $username || ! $password ) { 

		$OUTPUT::error = "Please enter a username and a password to login";
		dispatchTo ( 'loginPrompt' );

	} else {

		my $user;

		eval {
			$user = $userDatabase->authenticateUser ( $username, $password );

			# establish the session, too
			print STDERR "[webInterface] setting user into session: $user->{username}\n";
			$userSession->setAttribute ('user', $user );
		};
		if ( $@ ) { 
			dispatchTo ( "loginPrompt" );
			$OUTPUT::error = $@;
			
		} else { 

			dispatchTo ( "userHome" );

			# the login page can specify a destination dispatch parameter.
			my $destinationDispatch = $query->param('destinationDispatch');
			if ( $destinationDispatch ) { dispatchTo ( $destinationDispatch ) }

			$redirect++;
			$OUTPUT::user = $user;
		}
	}
}

elsif ( $action eq "logout" ) {
	$userSession->setAttribute ('user', 0);
	$redirect++;
	dispatchTo ( "index" );
}

#--------------------------------------------------------------------

elsif ( $action eq "problemAnimate" ) {

	# we need to do something with filename
	my $filename = $query->param("filename");
	if ( ! $filename ) { $filename = $pathInfo; }
	if ( ! $filename ) { 
		dispatchhTo ( "error" ); $OUTPUT::message = "parameter 'filename' not specified";
	} else {
	    # $OUTPUT::url = $scriptBase . '/etc/problems' . $filename;
	    # brockman tweak 2005-Jul-10. This makes it work. heh. (took out /etc/problems/)
	    $OUTPUT::url = $scriptBase . '/' . $filename;
	    $OUTPUT::filename = $filename;
	}
}

elsif ( $action eq "newUserSubmit" ) {

	# parse the parameters for the submission, and either display a success
	# or error page

	# try to create new user.
	my $user;

	$OUTPUT::username = $query->param('username');
	$OUTPUT::password = $query->param('password');
	$OUTPUT::realName = $query->param('realName');
	$OUTPUT::organization = $query->param('organization');

	print STDERR "[webInterface.newUserSubmit] organization = $OUTPUT::organization\n";

	eval { 
		$user = $userDatabase->newUser ( $query->param('username'), 
										 $query->param('password'), 
										 $query->param('passwordConfirm'), 
										 $query->param('realName'),
										 $query->param('organization') );

		# make his directory
		$problemDatabase->makeUserDirectory ( $user->{username} );
	};
	if ( $@ ) { 
		# error
		$OUTPUT::error = "$@";
		dispatchTo ( "newUser" );
	} else {
		# success
		$OUTPUT::username = $user->{username};
		$OUTPUT::realName = $user->{realName};
	}
	

}

###-------------------------------------------------
### USER*
###-------------------------------------------------

# all the user functions are protected
elsif ( $action =~ m/^user/i ) {

	my $user = $sessionCopy->{user};
	if ( ! $user ) {
		$OUTPUT::error = 'Please Log in to view the protected page';
		$OUTPUT::destinationDispatch = $action;
		dispatchTo ('loginPrompt' );
	} else {


		if ( $action eq "userHome" ) { 
			prepareDirectoryForDisplay ( $config->{userProblemDirectory},
										 $user->{username} );
		}
		elsif ( $action eq "userRename" ) {
			$OUTPUT::fullFilename = $query->param("filename");
			$OUTPUT::filename = SafeUtil::extractFilename ( $query->param("filename") );
		}
		elsif ( $action eq "userRenameSubmit") {

			# the security of a user not being able to delete another users files
			# is handled down in the ProblemDB Library

			my $newFilename = $query->param("newFilename");
			my $filename = $query->param("filename");
			my $fullFilename = $query->param("fullFilename");

			dispatchTo ( "userHome" );
			$redirect++;

			if ( $newFilename eq $filename ) {

				dispatchTo ( "userRename" );
				$OUTPUT::error = "New Filename is same as Old Filename";
				$OUTPUT::fullFilename = $fullFilename;
				$OUTPUT::filename = $filename;

				$redirect = 0;

			} elsif ( ! $newFilename ) { 

				dispatchTo ( "userRename" );
				$OUTPUT::error = "New Filename was blank";
				$OUTPUT::fullFilename = $fullFilename;
				$OUTPUT::filename = $filename;

				$redirect = 0;

			} else { 

				eval { 
					# actually perform the operation here.
					$problemDatabase->renameFile ( $fullFilename, SafeUtil::extractPath ( $fullFilename ) . $newFilename, $user->{username} );
				};
				if ( $@ ) { 
					dispatchTo (  "userRename" );
					$OUTPUT::error = $@;
					$OUTPUT::fullFilename = $fullFilename;
					$OUTPUT::filename = $filename;

					$redirect = 0;
				}
			}

		}
		elsif ( $action eq "userDelete" ) {
			$OUTPUT::fullFilename = $query->param("filename");
			$OUTPUT::filename = SafeUtil::extractFilename ( $query->param("filename") );
		}
		elsif ( $action eq "userDeleteSubmit") {

			# the security of a user not being able to delete another users files
			# is handled down in the ProblemDB Library
			my $filename = $query->param("filename");
			my $fullFilename = $query->param("fullFilename");

			dispatchTo ( "userHome" );
			$redirect++;

			if ( $query->param("YesNo") eq "Yes" ) {

				eval { 
					# actually perform the operation here.
					$problemDatabase->deleteFile ( $fullFilename, $user->{username} );
				};
				if ( $@ ) { 
					dispatchTo ( "userRename" );
					$OUTPUT::error = $@;
					$OUTPUT::fullFilename = $fullFilename;
					$OUTPUT::filename = $filename;

					$redirect = 0;
				}
			}

		}

		# this is protected
		elsif ( $action eq "userProblemDesign" ) {
			
			# we need to do something with filename
			my $filename = $query->param("filename");
			if ( ! $filename ) { $filename = $pathInfo; }
			if ( ! $filename ) { 
				dispatchTo ( "error" ); $OUTPUT::message = "parameter 'filename' not specified";
			} else {

				$filename = $config->{problemDirectory} . '/' . $filename;

				# brockman tweak 2005-Jul-10. This makes it work. heh. (took out /etc/problems/)
				$OUTPUT::url = $scriptBase . '/' . $filename;
				$OUTPUT::filename = $filename;
			}
		}

	}
}


###-------------------------------------------------
### BROWSE
###-------------------------------------------------

elsif ( $action eq "browse" ) {

	# directory browsing

	my $path = $query->param('path');
	if ( ! $path ) { $path = $pathInfo; }
	if ( ! $path ) { $path = '/'; }

	# make sure path has a slash on the beginning and end of it
	$path = SafeUtil::safePath ( $path );
	$OUTPUT::path = $path;

	# -----------------------------------------
	# put a '..' at the top of the subdirectory list, if it's not the top-level dir
	my @subdirs;
	my @files;

	if ( $SHOW_DOT_DIRECTORIES ) { 
		push @subdirs, { filename => 'Root Directory',
						 relativePath => '/' };

		my $previousDir = SafeUtil::previousDirectory ( $path );
	
		push @subdirs, { filename => 'Parent Directory',
						 relativePath => "$previousDir" };
	}

	$OUTPUT::subdirs = \@subdirs;
	$OUTPUT::files = \@files;

	# -----------------------------------------

	prepareDirectoryForDisplay ( $path );
}






#----------------------------------------------------------------------
# template OUTPUT + include routine


if ( $redirect ) {

	my $loc = "$LOCATION?action=$dispatchTo";
	# print STDERR "[webInterface] Redirect to: $loc\n";
 	print "Location: $loc\n\n";
	exit(0);
}

my $templateFile = "tmpl/$dispatchTo.tmpl";
if ( ! -f $templateFile ) { 
	print "Content-type: text/html\n\n";
	print "<b><font color=\"#ff0000\">Unknown Template: $templateFile</font></b>\n";
	exit(0);
}


#remove any informational line numbers from exceptions;
if ( $OUTPUT::error && $config->{hideExceptionLineNumbers} ) { $OUTPUT::error =~ s/at (.+?) line [0-9]+[.]//i; }

my $template = Text::Template->new ( SOURCE => $templateFile,
									 TYPE => 'FILE' );

my $content = $template->fill_in ( PACKAGE => 'OUTPUT' );


if ( $OUTPUT::sessionID ) { 
	# print STDERR "[webInterface] Cookie'ing user with sessionID: $userSession->{sessionID}\n";
	print "Set-Cookie: SESSION_ID=" . $userSession->{sessionID} . "\n";
}
print "Content-type: text/html\n\n";

print $content;


#--------------------------------------------------------------------
# template routines

sub OUTPUT::tmplImport
{
	my ( $templateFile ) = @_;

	my $template = Text::Template->new ( SOURCE => "tmpl/$templateFile",
										 TYPE => 'FILE' );

	my $content = $template->fill_in ( PACKAGE => 'OUTPUT' );
	if ( ! $content ) { 
		die "No content from template: $templateFile: $!";
	} else { 
		return $content;
	}
}


#--------------------------------------------------------------------


sub prepareDirectoryForDisplay
{
	my ( $path, $username ) = @_;

	my @subdirs;
	if ( $OUTPUT::subdirs ) { @subdirs = @{$OUTPUT::subdirs} }

	my @files;
	if ( $OUTPUT::files ) { @files = @{$OUTPUT::files} }
	
	my $fullPath;
	my $content;

	if ( $username ) { 
		( $fullPath, $content ) = $problemDatabase->listUserDirectoryContent ( $username );
	} else {
		( $fullPath, $content ) = $problemDatabase->listDirectoryContent ( $path, $username );
	}

	foreach my $content ( @{$content} ) {

		# print STDERR "Content: " . Dumper ( $content );

		if ( $content->{type} eq "directory" ) {
			# note - this was a conflict. somebody removed it. brock 2005-Jul-9
			$content->{fullPath} = '/' . $fullPath . $content->{filename} . '/';
			push @subdirs, $content;

		} elsif ( $content->{type} eq "problemFile" ) {
			# note - this was a conflict. somebody removed it. brock 2005-Jul-9
			$content->{fullPath} = $fullPath . '/' . $content->{filename};
			push @files, $content;
		}			
	}

	# list the subdirectories
	$OUTPUT::subdirs = \@subdirs;
	$OUTPUT::files = \@files;

}



sub dispatchTo
{
	my ( $dest ) = @_;
	setPageTitle ( $dest );
	$dispatchTo = $dest;
}

sub setPageTitle
{
	my ( $action ) = @_;
	$OUTPUT::title = $pageTitles->{$action} ? $pageTitles->{$action} : $action;
}


#--------------------------------------------------------------------
# WARNING DISABLES
if ( 1 || $OUTPUT::subdirs || $OUTPUT::files || $OUTPUT::password || $OUTPUT::error || $OUTPUT::path ||
	 $OUTPUT::session || $OUTPUT::message || $OUTPUT::fullFilename || $OUTPUT::destinationDispatch ||
	 $OUTPUT::password || $OUTPUT::selfPath ) { };


# this removes the warning satements.
if ( 1 or $OUTPUT::action or $OUTPUT::title or $OUTPUT::filename or 
	 $OUTPUT::self or $OUTPUT::rpcHost or $OUTPUT::url or $OUTPUT::color ) { }



