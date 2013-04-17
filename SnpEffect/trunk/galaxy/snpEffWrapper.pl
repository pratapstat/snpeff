#!/usr/bin/env perl

#-------------------------------------------------------------------------------
# Galaxy wrapper for SnpEff
#
#																Pablo Cingolani
#-------------------------------------------------------------------------------

# Debug mode?
$debug = 1;

# Get command's dir (full path provided by Galaxy)
$dir = `dirname $0`;
$dir =~ tr/\r\n//d;

# Basic SnpEff command
$command = $ARGV[0];	# Which snpEff's command do we want to execute?
@snpEffCmd = ("java", "-Xmx4g", "-jar", "$dir/snpEff.jar", "$command", "-c", "$dir/snpEff.config");

# Add all command line options
for( $i=1 ; $i < $#ARGV ; $i++ ) {
	$snpEffCmd[$#snpEffCmd] = $ARGV[$i];
}

# Execute
exec @snpEffCmd;
