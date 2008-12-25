#!/usr/bin/perl -w -Iinc
#		
# Perl Problem Server xml-rpc API definition file
# brockman 03.19.04
#
# This is the implementation for the REAL problemServer, not just
# the stub one.
#

require 'inc/xmlrpc_cgi.inc';
require 'inc/SafeUtil.inc';

use strict;

use Data::Dumper;
use MIME::Base64;

use ConfigParser;
use UserSession;
use UserDatabase;
use ProblemDatabase;
use RpcHelper;

#------------------------------------------------------------------------

my $config = new ConfigParser ('etc/server.cfg');

my $userSession = new UserSession ( $config->{sessionDataDirectory},
									$config->{sessionLockDirectory} );

my $userDatabase = new UserDatabase ( $config->{userDirectory} );
my $problemDatabase = new ProblemDatabase ( $config->{problemDirectory} );

my $DEBUG = 0; #lots of printing if this is 1.

#------------------------------------------------------------------------

my $methodHash = {'pps.authenticate' => \&authenticate,
				  'pps.listContent' => \&listContent,
				  'pps.getFile' => \&getFile,
				  'pps.putFile' => \&putFile,
				  'pps.deleteFile' => \&deleteFile };

# I can't get apache to run easily, 

# xmlrpc_fastCGI_handle ( $methodHash );
# xmlrpc_standAlone ( $methodHash, '9999' );
xmlrpc_handle ( $methodHash );

#------------------------------------------------------------------------

# authenticate ( username => ,
#                password => )

sub authenticate 
{
	my ( $request ) = @_;

	debug ( 'authenticate', $request );

	my $username = RpcHelper::param ( $request, 'username' );
	my $password = RpcHelper::param ( $request, 'password' );

	# lookup the user record
	my $user = $userDatabase->authenticateUser ( $username, $password );
	
	# + issue a new authkey + session
	my $authkey = $userSession->createAuthSession ( $user );

	# close the user info into a new hash - it won't go over the
	# xmlrpc line by default b/c it's a hash.
	my $ret = {};
	foreach my $key ( keys %{$user} ) {
		$ret->{$key} = $user->{$key};
	}
	$ret->{authkey} = $authkey;

	print STDERR "[problemServer] authkey: $authkey\n";

	return $ret;
}

# listContent ( authkey =>,
#               directory => ) 

sub listContent
{
	my ( $request ) = @_;

	my $user = $userSession->authRequest ( $request );
	my $directory = RpcHelper::param ( $request, 'directory' );

	print STDERR "[problemServer.listContent] directory: $directory\n";

	# hack around java hard-coded /username@host.com/ request
	if ( $directory =~ m!^[/~][^/]+?@.[^/]+[.]!i ) {
		print STDERR "[problemServer.listContent] Deteched Old-style /user\@host.com/ request\n";
		$directory = '/Users' . $directory;
	}

	debug ( 'listContent', $request );

	my ( $fullPath, $content ) = $problemDatabase->listDirectoryContent ( $directory, $user->{username} );

	return { authkey => $user->{_session_id},
			 files => $content };
}


# there used to be listFiles and listDirectories, but I folded it all
# down into 1 method: listContent.
# brockman 03.19.04



# putFile ( data =>,
#           filename => 
sub putFile
{
	my ( $request ) = @_;

	debug ( 'putFile', $request );

	my $user = $userSession->authRequest ( $request );

	my $filename = RpcHelper::param ( $request, 'filename' );

	# hack around java hard-coded /username@host.com/ request
	if ( $filename =~ m!^[/~][^/]+?@.[^/]+[.]!i ) {
		print STDERR "[problemServer.putFile] Deteched Old-style /user\@host.com/ request\n";
		$filename = '/Users' . $filename;
	}

	my $base64Data = RpcHelper::param ( $request, 'data' );
	my $data = decode_base64 ( $base64Data );

	debug ( 'putFile', "DATA: $data" );
	debug ( 'putFile', "FILENAME: $filename" );

	# This is a new call that takes permissions into account. 2005-Jul-9 brock
	$problemDatabase->writeFile ( $user->{username}, $filename, $data );

	# should be good here.

	return { authkey => $user->{_session_id} };
}




# getFile ( filename => 
sub getFile
{
	my ( $request ) = @_;

	debug ( 'getFile', $request );

	my $user = $userSession->authRequest ( $request );
	my $filename = RpcHelper::param ( $request, 'filename' );

	# hack around java hard-coded /username@host.com/ request
	if ( $filename =~ m!^[/~][^/]+?@.[^/]+[.]!i ) {
		print STDERR "[problemServer.getFile] Deteched Old-style /user\@host.com/ request\n";
		$filename = '/Users' . $filename;
	}


	my $fileInfo = $problemDatabase->loadFile ( $filename );

	debug ('getFile', "File data: $fileInfo->{file_data}" );

	$fileInfo->{data} = encode_base64 ( $fileInfo->{file_data} );

	$fileInfo->{authkey} = $user->{_session_id};

	return $fileInfo;
}


# deleteFile ( filename => 
# 
sub deleteFile
{
	my ( $request ) = @_;

	debug ( 'deleteFile', $request );

	my $user = $userSession->authRequest ( $request );
	my $filename = RpcHelper::param ( $request, 'filename' );

	# make sure the user has permission to do this. 
	$problemDatabase->deleteFile ( $filename, $user->{username} );

	return { authkey => $user->{_session_id} };
}

#------------------------------------------------------------------------

sub debug
{
	my ( $function, $param ) = @_;

	if ( $DEBUG ) { 
		print STDERR "[debug:stubServer.$function] " . Dumper ( $param );
	}
}
