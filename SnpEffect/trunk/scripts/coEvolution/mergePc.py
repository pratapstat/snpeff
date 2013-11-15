#!/usr/bin/env python

#------------------------------------------------------------------------------
#
# Add PC values to coEvolution.txt file
# Those values are used as covariates in a logistic regression model
# See coEvolution.r
#
# Note: We need age as covariate (TFAM file, column after phenotype )
#
#
#														Pablo Cingolani 2013
#------------------------------------------------------------------------------

import sys

debug = False

#------------------------------------------------------------------------------
# Print a header line
#------------------------------------------------------------------------------
def printHeaderLine(title, values):
	outLine = ""

	# First columns are just 'title'
	for i in range(headerColId):
			outLine += title + "\t"

	# No p-value on header
	outLine += "1.0\t1.0\t1.0\t1.0"

	# Add values on the following columns
	for i in range(headerCol, len(header)):
			id = header[i]
			outLine += "\t{}".format( values.get(id, "-1") )

	print outLine

#------------------------------------------------------------------------------
# Main
#------------------------------------------------------------------------------

#---
# Parse command line arguments
#---
if len(sys.argv) != 4 :
	print "Usage: {} pcaFile.txt pheno.age.tfam coEvolution.txt".format(sys.argv[0])
	sys.exit(1)

pcaFile = sys.argv[1]
tfamFile = sys.argv[2]
coEvFile = sys.argv[3]

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
# Read TFAM
#---
print >> sys.stderr, "Reading TFAM (with age) file: " + tfamFile
sex = {}
pheno = {}
age = {}
with open(tfamFile) as f:
	for line in f:
		f = line.rstrip().split('\t')
		id, lsex, lpheno, lage = f[1], f[4], f[5], f[6]
		sex[ id ] = lsex
		pheno[ id ] = lpheno
		age[ id ] = lage
		if debug : print "Line\tID: {}\tSex: {}\tPheno: {}".format(id, sex, pheno)

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

		# Show PC information
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

		# Show sex information
		printHeaderLine( "sex", sex)
		printHeaderLine( "age", age)

f.close()

