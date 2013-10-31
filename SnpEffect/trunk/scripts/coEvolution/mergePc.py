#!/usr/bin/env python

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
headerCol = 6
print >> sys.stderr, "Reading coEvolution file: " + coEvFile
with open(coEvFile) as f:
	header = {}

	for line in f.readlines():
		line = line.rstrip()
		print line

		# First line? Read and parse header
		if not header : 
			header = line.split('\t')
			for pcnum in range(maxPc):
				# Show 'PC' label
				for i in range(headerCol+1):
					print "PC{}".format(pcnum+1),

				# Show PC values
				for i in range(headerCol+1, len(header)):
					id = header[i]
					pc = 0.0
					pcs = pcas.get(id,[])
					if pcs : pc = pcs[pcnum]
					print pc,

				print 



