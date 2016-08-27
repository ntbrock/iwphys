#!/usr/bin/perl -w

use strict;

cmd ("./build.pl");
cmd ("cp -r dest/* ~/public_html/pps/");
cmd ("chmod g-w ~/public_html/pps/");

sub cmd
{
	my ( $cmd ) = @_;
	system ("$cmd");
}
