#!/usr/bin/env python

"""
  Select GeneSets from MSigDb using expression data from GTEx


  Pablo Cingolani 2013
"""

import sys

# Debug mode?
debug = True

# Value threshold
minValue = float("-inf")
maxValue = float("inf")

#------------------------------------------------------------------------------
# Load MSigDb file
#------------------------------------------------------------------------------
def loadMsigDb(msigFile):
	geneSet = {}
	for line in open(msigFile) :
		fields = line.rstrip().split("\t")
		geneSetName = fields[0]
		geneSet[ geneSetName ] = fields[2:]
		if debug : print geneSetName, " => ", geneSet[ geneSetName ]
	return geneSet

#------------------------------------------------------------------------------
# Process normalized GTEx file
#------------------------------------------------------------------------------
def readGtex(gtexFile, minValCount, minValue, maxValue):
	column = []
	countOk = 0
	countNo = 0
	gtexGenes = {}
	for line in open(gtexFile) :
		fields = line.rstrip().split("\t")

		if not column : 
			# Read header and add all column numbers that we are looking for
			for i in range(len(fields)):
				if fields[i] in ids:
					column.append(i)

			# Sanity check
			if len(column) != len(ids):
				print >> sys.stderr, "Some IDs were not found: \n\t", ids.keys(), "\n\t", [fields[i] for c in column]
				sys.exit(1)
			else:
				print >> sys.stderr, "OK, All required IDs found."

			# We require at least these number of values
			print >> sys.stderr, "Filter:\n\tMinimum number of values: {}\n\tMinimum value: {}\n\tMaximum value: {}\n".format(minValCount, minValue, maxValue)

		else :
			geneId, geneName = fields[0], fields[1]

			# Collect values for requested IDs
			vals = []
			for idx in column :
				val = fields[idx]
				if val != "NA":
					 v = float(val)
					 if (v >= minValue) and (v <= maxValue): vals.append( v )

			# Show results
			if len(vals) >= minValCount:
				avg = reduce(lambda x,y : x+y, vals) / len(vals)
				countOk += 1
				gtexGenes[geneName] = 1
				if debug: print "OK\t{}\t{}\t{} / {}\t{}\t{}".format(geneId, geneName, len(vals), len(column), avg, vals)
			else:
				countNo += 1
				gtexGenes[geneName] = 0
				if debug: print "NO\t{}\t{}\t{} / {}\t{}".format(geneId, geneName, len(vals), len(column), vals)

	print >> sys.stderr, "\tNumber of genes that passed  : {}\n\tNumber of genes filtered out : {}\n".format( countOk, countNo)
	return gtexGenes

#------------------------------------------------------------------------------
# Main
#------------------------------------------------------------------------------

#---
# Command line parameters
#---
if len(sys.argv) < 4 :
	print >> sys.stderr, "Usage: " + sys.argv[0] + " msigDb.gmt gtex_normalized.txt gtexExperimentId_1,gtexExperimentId_2,...,gtexExperimentId_N minCount minValue maxValue"
	sys.exit(1)

msigFile = sys.argv[1]
gtexFile = sys.argv[2]
gtexExperimentIds = sys.argv[3]
minValCount = int( sys.argv[4] ) if len(sys.argv) > 4 else 1
minValue = float( sys.argv[5] ) if len(sys.argv) > 5 else float("-inf")
maxValue = float( sys.argv[6] ) if len(sys.argv) > 6 else float("inf")

# Create a hash of IDs
ids = dict( (id,1) for id in gtexExperimentIds.split(",") )

# Read files
geneSets = loadMsigDb(msigFile)
gtexGenes = readGtex(gtexFile, minValCount, minValue, maxValue)

# Create new gene sets
for gsName in geneSets:
	# Add only genes that have a non-zero value in 'gtexGenes'
	out = "{}\t{}".format(gsName, gsName)
	geneSet = geneSets[gsName]
	count = 0 
	for gene in geneSet:
		if gtexGenes.get(gene,0):
			out += "\t" + gene
			count += 1

	# Show results
	if debug: print "{} / {}\t{}".format( count, len(geneSet), out)
	elif count > 0: print out
