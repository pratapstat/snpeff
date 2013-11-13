#!/usr/bin/env python

#------------------------------------------------------------------------------
#
# Add PC values to coEvolution.txt file
# Those values are used as covariates in a logistic regression model
# See coEvolution.r
#
#------------------------------------------------------------------------------

import sys

debug = False

#---
# Parse command line arguments
#---
if len(sys.argv) != 3 :
	print "Usage: {} pcaFile.txt coEvolution.txt".format(sys.argv[0])
	sys.exit(1)

pcaFile = sys.argv[1]
coEvFile = sys.argv[2]

#---
# Read PCA file
#---
print >> sys.stderr, "Reading PCA file: " + pcaFile
pcas = {}
maxPc = 0
with open(pcaFile) as f:
	for line in f:
		line = line.rstrip()
		id = line.split('\t')[0]
		pc = line.split('\t')[1:]
		maxPc = max(maxPc, len(pc))
		if debug : print "Line\tID: {}\tPCAs: {}".format(id, pc)
		if id : pcas[id] = pc


#---
# Read coEvolution file
#---
headerCol = 10
headerColId = 6
header = {}

print >> sys.stderr, "Reading coEvolution file: " + coEvFile

if coEvFile == "-":
	f = sys.stdin
else:
	f = open(coEvFile)

for line in f.readlines():
	line = line.rstrip()
	print line

	# First line? Read and parse header
	if not header : 
		header = line.split('\t')
		for pcnum in range(maxPc):
			# Show 'PC' label
			outLine = ""
			for i in range(headerColId):
				outLine += "PC{}\t".format(pcnum+1)

			# pvalue column
			outLine += "1.0\t1.0\t1.0\t1.0"

			# Show PC values
			for i in range(headerCol, len(header)):
				id = header[i]
				pc = 0
				pcs = pcas.get(id,[])
				if pcs : pc = pcs[pcnum]
				outLine += "\t{}".format(pc)

			print outLine

f.close()

