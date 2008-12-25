#!/usr/bin/perl -w

package UserSession;

#----------------------------------------------------------------------
#
# user.inc
# The user toolkit file
# brockman 03.19.04
# updates on 06.12.04
#
# This file uses Apache::Session::File. Code very similar to Tribble.
#
#----------------------------------------------------------------------

use Apache::Session::File;
use Data::Dumper;

use RpcHelper;

my $MAX_RETRIES = 5;

#----------------------------------------------------------------------

sub new
{
	my ( $packageName, $sessionDataDirectory, $sessionLockDirectory ) = @_;
	if ( ! $sessionDataDirectory ) { die "argument sessionDataDirectory undefined"; }
	if ( ! $sessionLockDirectory ) { die "argument sessionLockDirectory undefined"; }
	my $self = { sessionDataDirectory => $sessionDataDirectory,
				 sessionLockDirectory => $sessionLockDirectory };

    return bless $self, $packageName;
}

#----------------------------------------------------------------------

sub getSessionIDFromCookies
{
    my ( $self, $prefix) = @_;
	if ( ! $prefix ) { $prefix = ''; }

    my $sessionID;
    #check to see if we've already got one
    my $cookies = getCookies();

    my $sidCookie = $prefix . "SESSION_ID";

    $sessionID = $cookies->{$sidCookie};

	# print STDERR "[UserSession.getSessionID] from Cookie: $sessionID\n";

    my $hash = getApacheSession( $self, $sessionID, 1);

    return $hash->{_session_id};
}


sub getAttribute
{

    my ($self, $name) = @_;
	# print STDERR "Getting session attribute $name for session $self->{sessionID}\n";

    my $hash = getApacheSession( $self, $self->{sessionID},1);
	# print STDERR "$name is $hash->{$name}\n";

    return $hash->{$name};
}

sub setAttribute
{
	my ($self, $name, $value) = @_;

	# print STDERR "Setting session attribute $name to $value for session $self->{sessionID}\n";
    my $hash = getApacheSession( $self, $self->{sessionID},1);

    $hash->{$name} = $value;
}


sub sessionCopy
{
	my ( $self ) = @_;
	my $hashOut = {};

	# print STDERR "[sessionCopy] self->sessionID: $self->{sessionID}\n";

	my $hashSession = getApacheSession( $self, $self->{sessionID},1);

	# print STDERR "[sessionCopy] Copying session...\n";

	foreach my $key ( keys %{$hashSession} ) {
		$hashOut->{$key} = $hashSession->{$key};
	}

	return $hashOut;
}


#----------------------------------------------------------------------
#
# This method will authorize a request by pulling out the 'authkey'
# parameter automaticallly.

sub authRequest
{
	my ( $self, $requestHash ) = @_;

	my $authkey = RpcHelper::param ( $requestHash, 'authkey' );

	# see if there is a user object associated with this 	
	# authkey in the authkey table


	my $userHash = getApacheSession ( $self, $authkey, $MAX_RETRIES );
	if ( ! $userHash ) { 
		die "Unknown Authkey: '$authkey'";
	}

	# TODO: check and make sure that the expiration time
	# is not too great. If it is, issue a new authkey, update the
	# timestamp, and re-associate the user in the table. 
	# Also, undef the old entry, or put a tamper Flag on it?

	return $userHash;
}

sub createAuthSession
{
	my ( $self, $userInfo ) = @_;

	my $hashRef = getApacheSession ( $self, '', 1 );

	foreach my $key ( keys ( %{$userInfo} ) ) {
		$hashRef->{$key} = $userInfo->{$key};
	}

	return $hashRef->{_session_id};
}



#sub getCookieSession
#{
#    my ( $self, $prefix ) = @_;
#
#	if ( ! $prefix ) { $prefix = ''; }
#
#	my $session;
#    my $sessionID;
#    #check to see if we've already got one
#    my $cookies = getCookies();
#
#    my $sidCookie = $prefix . "SESSION_ID";
#
#    $sessionID = $cookies->{$sidCookie};
#
#	if ( $sessionID ) { 
#		# if sessionID is not defined here, it'll create a new one 
#		# automatically.
#		eval {
#			$session = $self->getApacheSession ( $sessionID );
#		};
#
#		if ( $@ ) { 
#			# there was a problem
#			print STDERR "[UserSession] Unable to get UserSession ($sessionID). Creating new one\n";
#			$session = $self->getApacheSession ();
#		}
#	} else { 
#		print STDERR "UserSession: cookie $sidCookie not present. creating a new session\n";
#		$session = $self->getApacheSession ( );
#		$session->{createDate} = localtime();
#	}
#	return $session;
#}

#-----------------------------------------------------------------------------

sub getApacheSession
{
    my ( $self, $sessionID, $numTries) = @_;

	if ( ! $numTries ) { $numTries = 1; }
    if ( $numTries > $MAX_RETRIES ){
        print STDERR "[UserSession.pm] numTries > 5.  Croaking.\n";
        die "Avoiding infinite apache::session loop.";
    }

    my %hash;

	#print STDERR "[UserSession.getApacheSession] sessionID: '$sessionID'\n";
	#print STDERR "sessionDataDirectory: $self->{sessionDataDirectory}\n";
	#print STDERR "sessionLockDirectory: $self->{sessionLockDirectory}\n";

    eval {
		tie %hash, 'Apache::Session::File', $sessionID,
		{ Directory => $self->{sessionDataDirectory},
		  LockDirectory => $self->{sessionLockDirectory} };
	};

    if($@) {
		print STDERR "[UserSession.getApacheSession] ($numTries) EVAL FAILURE: $@\n";

		return getApacheSession( $self, '', $numTries+1 );
    }

	# print STDERR "[UserSession.getApacheSession] Success: New Session Object: " . Dumper ( \%hash );
	$self->{sessionID} = $hash{_session_id};
    return \%hash;
}



#-----------------------------------------------------------------------------
# Util

my $TOKEN_LENGTH = 12;

sub randomToken
{
	my $length = $TOKEN_LENGTH;

	my @chars=('a'..'z','A'..'Z','0'..'9');
	my $random;

	foreach (1..$length) 
	{
		#rand @chars will generate a random number between 0 and scalar @chars
		$random .= $chars[rand @chars];
	}
	return $random;

}


# make sure that only the valid characteres that are allowed in tokens
# are actually in the token
sub safeToken
{
	my ( $token ) = @_;

	my $safe = $token;
	$safe =~ s/[^a-zA-Z0-9]//g;

	if ( $token ne $safe ) {
		print STDERR "[safeToken] Fixed Token: old: $token  safe: $safe\n";
	} 

	return $safe;
}



# helper method

sub getCookies
{
    my %cookies;
    my $cookieStr = $ENV{'HTTP_COOKIE'};

	if ( ! $cookieStr ) { return {}; }
    my @cookiepairs=split(/;/, $cookieStr);

    foreach my $pair (@cookiepairs) {
		my ($name, $value) = split(/=/, $pair);
		$name =~ s/^ //g;
		$cookies{$name} = $value;
    }

    return \%cookies;
   
}



1;
