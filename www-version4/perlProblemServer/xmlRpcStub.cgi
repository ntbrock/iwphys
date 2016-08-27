#!/usr/bin/perl -w -Iinc
#		
# Perl Problem Server xml-rpc API definition file
# brockman 03.19.04
#

require 'inc/xmlrpc_cgi.inc';

use Data::Dumper;

use ConfigParser;
use UserSession;
use UserDatabase;
use ProblemDatabase;
use RpcHelper;

#------------------------------------------------------------------------

my $config = new ConfigParser ('etc/server.cfg');
my $userSession = new UserSession ( $config->{sessionDirectory} );

my $userDatabase = new UserDatabase ( $config->{userDirectory} );
my $problemDatabase = new ProblemDatabase ( $config->{problemDirectory} );

#------------------------------------------------------------------------

my $methodHash = {'pps.authenticate' => \&authenticate,
				  'pps.listFiles' => \&listFiles,
				  'pps.listDirectories' => \&listDirectories,	
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
	
	# TODO: lookup the user record + issue a new authkey + session

	return { authkey => 'STUB_AUTHKEY' };
}

# listFiles ( authkey =>,
#             directory => ) 

sub listFiles
{
	my ( $request ) = @_;

	debug ( 'listFiles', $request );

	my $user = { authkey => RpcHelper::param ( $request, 'authkey' ) };
	my $directory = RpcHelper::param ( $request, 'directory' );

	@files = ('fake1.iwp', 'fake2.iwp', 'fake3.iwp');

	return { authkey => $user->{authkey},
			 files => \@files };
}

# listDirectories ( authkey => ,
#                  directory => )
sub listDirectories
{
	my ( $request ) = @_;
	
	my $user = { authkey => RpcHelper::param ( $request, 'authkey' )};
	my $directory = RpcHelper::param ( $request, 'directory' );

	debug ( 'listDirectories', $request );

	my @files = ('fakedir1', 'fakedir2', 'fakedir3');
	return { authkey => $user->{authkey},
			 files => \@files };

}

# putFile ( data =>,
#           filename => 
sub putFile
{
	my ( $request ) = @_;

	debug ( 'putFile', $request );

	my $user = { authkey => RpcHelper::param ( $request, 'authkey' )};
	my $filename = RpcHelper::param ( $request, 'filename' );
	my $base64Data = RpcHelper::param ( $request, 'data' );

	return { authkey => $user->{authkey} };
}


# getFile ( filename => 
sub getFile
{
	my ( $request ) = @_;

	debug ( 'getFile', $request );

	my $user = { authkey => RpcHelper::param ( $request, 'authkey' )};
	my $filename = RpcHelper::param ( $request, 'filename' );

	return { authkey => $user->{authkey},
			 data => 'TODO: Encode the data' };

}


# deleteFile ( filename => 
# 
sub deleteFile
{
	my ( $request ) = @_;

	debug ( 'deleteFile', $request );

	my $user = { authkey => RpcHelper::param ( $request, 'authkey' )};
	my $filename = RpcHelper::param ( $request, 'filename' );

	return { authkey => $user->{authkey} };
}

#------------------------------------------------------------------------

sub debug
{
	my ( $function, $param ) = @_;
	print STDERR "[debug:stubServer.$function] " . Dumper ( $param );
}
