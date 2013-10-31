
if( ! exists('d') ) {
	fileName <- "coEvolution.txt"

	# Read data
	cat('Reading data', fileName, '\n')
	d <- read.table(fileName, sep="\t", header=TRUE)

	# Read principal components
	pca <- read.table('t2d.mds', sep='\t', header=TRUE)
}

minCol <- 8
maxCol <- length(d[1,])
maxRow <- length(d[,1])

phenoCol <- minCol:maxCol
rows <- 2:maxRow

pheno <- as.vector( d[1,phenoCol] )
genotypes <- as.matrix( d[rows,phenoCol] )
pvalues <- as.numeric(as.character(d[,7]))


keep <- ( pheno >= 0 )

for( idx in 2:maxRow ) {

	# Prepare data
	gt <- as.vector( genotypes[idx,] )
	keep <- ( pheno >= 0 ) & (gt >= 0)
	ph <- pheno[keep]
	gt <- factor( gt[keep] )

	# Logistic regression
	logReg <- glm( ph ~ gt, family=binomial)
	anovaLr <- anova(logReg, test="Chisq")
	pvalue <- anovaLr$"Pr(>Chi)"[2]

	# Show results
	pvalueOri <- pvalues[idx]
	id1 <- paste( d[idx,1] )
	id2 <- paste( d[idx,4] )
	gene1 <- paste( d[idx,2] )
	gene2 <- paste( d[idx,5] )
	ratio <- pvalue / pvalueOri

	if( pvalue < pvalueOri ) {
		cat( paste("=>", idx, pvalue, pvalueOri, ratio, id1, gene1, id2, gene2, sep="\t") , '\n')
	} else {
		cat( paste("  ", idx, pvalue, pvalueOri, ratio, id1, gene1, id2, gene2, sep="\t") , '\n')
	}
}

