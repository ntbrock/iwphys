#!/usr/bin/perl -w

use strict;

my $dest = 'dest';

cmd ( "mkdir -p $dest" );

cmd ( "cp -fr *.cgi inc bin etc gfx tmpl content $dest");

# TODO: delete CVS directoires from dest/
# cmd ( "find $dest -name CVS -exec rm -rf {} \;" );

# get around suexec
cmd ( "chmod g-w $dest/xmlRpcStub.cgi" );
cmd ( "chmod g-w $dest/problemServer.cgi" );
cmd ( "chmod g-w $dest/webInterface.cgi" );

# nuke all the CVS dirs.
cmd ( "find dest/ -name CVS -exec rm -rf {} \\\; &>/dev/null");
cmd ( "find dest/ -name *\~ -exec rm -rf {} \\\; &>/dev/null");

sub cmd
{
	my ( $cmd ) = @_;
	system ("$cmd");
}
	
