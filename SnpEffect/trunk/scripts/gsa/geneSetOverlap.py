#!/usr/bin/env python

#------------------------------------------------------------------------------
#
# Overlap between gene sets
#
#------------------------------------------------------------------------------

import sys

# Debug mode?
debug = False

#------------------------------------------------------------------------------
# Load MSigDb file
#------------------------------------------------------------------------------
def loadMsigDb(msigFile):
	geneSet = {}
	for line in open(msigFile) :
		fields = line.rstrip().split("\t")
		geneSetName = fields[0]
		geneSet[ geneSetName ] = set( fields[2:] )
		if debug : print geneSetName, " => ", geneSet[ geneSetName ]
	return geneSet

#------------------------------------------------------------------------------
# Main
#------------------------------------------------------------------------------

#---
# Command line parameters
#---
if len(sys.argv) != 3 :
	print >> sys.stderr, "Usage: " + sys.argv[0] + " msigDb.gmt set.gmt"
	sys.exit(1)

msigFile = sys.argv[1]
setFile = sys.argv[2]

geneSets = loadMsigDb(msigFile)
testSets = loadMsigDb(setFile)

print "{}%\t{}\t{}\t{}\t{}".format("overlap", "count", "len", "gsetName1", "gsetName2")
for gsetName1 in testSets:
	abslen = len(testSets[gsetName1])
	if abslen > 5:
		for gsetName2 in geneSets:
			count = len(testSets[gsetName1] & geneSets[gsetName2])
			if count > 0:
				overlap = (100.0 * count) / abslen
				print "{}%\t{}\t{}\t{}\t{}".format(overlap, count, abslen, gsetName1, gsetName2)




