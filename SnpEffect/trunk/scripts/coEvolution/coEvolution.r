
#--------------------------------------------------------------------------------
#
# Logistic regression of coEvolutionary model
#
#															Pablo Cingolani 2013
#--------------------------------------------------------------------------------

library(epicalc)

#---
# Parse command line args
#---

args <- commandArgs(trailingOnly = TRUE)

if( length(args) > 0 ) {
	# Parse command line arguments
    fileName <- args[1]
    PC <- args[2]
    cat('# Parameters:\n')
    cat('# \tInput file:', fileName, '\n')
    cat('# \tNumber of principal components:', PC, '\n')
} else {
	# Default parameters
	fileName <- "coEvolution.pc.txt"
	PC <- 3
}

#---
# Load data
#---
if( ! exists('d') ) {

	# Read data
	cat('# Reading data', fileName, '\n')
	d <- read.table(fileName, sep="\t", header=TRUE)
}

#---
# Parse the data file
#---
minCol <- 11					# Columns 1-10 are [chr1, pos1, gene1, chr2, pos2, gene2, pllelic, pDominant, pRecessive, pCodominant]
minRow <- 12					# Rows 1-11 are: [PC_1 ... PC_10, phenotypes ]
maxCol <- length(d[1,])
maxRow <- length(d[,1])

# Extract phenotypes
phenoRow <- minRow - 1			# Phenotypes are in the last row before genotypes start
phenoCol <- minCol:maxCol
pheno <- as.numeric( d[phenoRow,phenoCol] )

cat('# Phenotypes:\n')
cat('#     Cases    :', sum(pheno == 1), '\n' )
cat('#     Controls :', sum(pheno == 0), '\n' )
cat('#     Missing  :', sum(pheno == -1), '\n' )
cat('#     Max      :', max(pheno), '\n' )
cat('#     Min      :', min(pheno), '\n' )

# Extract principal components (used as covariates)
pcRows <- 1:(phenoRow-1)
pcCols <- minCol:maxCol
pcs <-  data.matrix( d[pcRows,pcCols] )

# Extract genotypes
genoCols <- minCol:maxCol
genoRows <- minRow:maxRow
genotypes <- data.matrix( d[genoRows, genoCols] )

# Extract p_values
pvalueCol <- (minCol-4):(minCol-1) 			# Last columns before genotypes has p_values
pvaluesMat <- data.matrix( d[genoRows, pvalueCol] )
pvalues <- apply(pvaluesMat, 1, min)		# Min pvalue in each row
cat('# \n# p-values:\n')
cat('#     Min :', min(pvalues), '\n' )
cat('#     Max :', max(pvalues), '\n' )

#---
# Analyze each row (calcualte logistic regression)
#---

cat('\nLogistic regression:\n')
keep <- ( pheno >= 0 )	# No pheotype? Don't use the sample

rowsToAnalyze <- minRow:(minRow+1)
rowsToAnalyze <- 1:(dim(genotypes)[1])
for( idx in rowsToAnalyze ) {

	# Prepare data
	gt <- as.numeric( genotypes[idx,] )
	keep <- ( pheno >= 0 ) & (gt >= 0)	# No pheotype? No genotype? Don't use the sample
	ph <- pheno[keep]
	#gt <- factor( gt[keep] )
	gt <- gt[keep]
	pc <- pcs[,keep]

	#---
	# Logistic regression
	#---
<<<<<<< .mine
	if( PC == 10 ) {
		# PC: First 10 components
		lr0 <- glm( ph ~ gt + pc[1,] + pc[2,] + pc[3,] + pc[4,] + pc[5,] + pc[6,] + pc[7,] + pc[8,] + pc[9,] + pc[10,] , family=binomial)		# Full model, takes into account genotypes and PCs
		lr1 <- glm( ph ~      pc[1,] + pc[2,] + pc[3,] + pc[4,] + pc[5,] + pc[6,] + pc[7,] + pc[8,] + pc[9,] + pc[10,] , family=binomial)		# Reduced model: only PCs, no genotypes
	} else if ( PC == 3 ) {
		# PC: First 3 components
		lr0 <- glm( ph ~ gt + pc[1,] + pc[2,] + pc[3,], family=binomial)	# Full model, takes into account genotypes and PCs
		lr1 <- glm( ph ~      pc[1,] + pc[2,] + pc[3,], family=binomial)	# Reduced model: only PCs, no genotypes
	}

	# Likelyhood ratio test
	lrt <- lrtest(lr0, lr1)
	pvalue <- lrt$p.value	# p-value from likelihood ration test
=======
	# PC: First 10 components
	lr0 <- glm( ph ~ gt + pc[1,] + pc[2,] + pc[3,] + pc[4,] + pc[5,] + pc[6,] + pc[7,] + pc[8,] + pc[9,] + pc[10,] , family=binomial)		# Full model, takes into account genotypes and PCs
	lr1 <- glm( ph ~      pc[1,] + pc[2,] + pc[3,] + pc[4,] + pc[5,] + pc[6,] + pc[7,] + pc[8,] + pc[9,] + pc[10,] , family=binomial)		# Reduced model: only PCs, no genotypes

	# PC: First 3 components
	#lr0 <- glm( ph ~ gt + pc[1,] + pc[2,] + pc[3,], family=binomial)			# Full model, takes into account genotypes and PCs
	#lr1 <- glm( ph ~      pc[1,] + pc[2,] + pc[3,], family=binomial)			# Reduced model: only PCs, no genotypes

	# Likelyhood ratio test
	lrt <- lrtest(lr0, lr1)												# Likeliehood ratio test
	pvalue <- lrt$p.value												# p-value from likelihood ration test
>>>>>>> .r1055

	# Show results
	pvalueOri <- pvalues[idx]

	didx <- idx + minRow - 1
	pos1 <- paste( d[didx,1] )
	gene1 <- paste( d[didx,2] )
	id1 <- paste( d[didx,3] )
	pos2 <- paste( d[didx,4] )
	gene2 <- paste( d[didx,5] )
	id2 <- paste( d[didx,6] )
	ratio <- pvalue / pvalueOri

	if( pvalue < pvalueOri ) {
		cat( paste("=>", idx, pvalue, pvalueOri, ratio, pos1, gene1, id1, pos2, gene2, id2, sep="\t") , '\n')
	} else {
		cat( paste("  ", idx, pvalue, pvalueOri, ratio, pos1, gene1, id1, pos2, gene2, id2, sep="\t") , '\n')
	}
}

