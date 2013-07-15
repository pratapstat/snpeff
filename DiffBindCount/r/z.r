
library(DiffBind)

#-------------------------------------------------------------------------------
# References http://www.bioconductor.org/packages/2.11/bioc/html/DiffBind.html
#-------------------------------------------------------------------------------

source('analyze.R')
source('contrast.R')
source('core.R')
source('counts.R')
source('DBA.R')
source('helper.R')
source('io.R')
source('overLapper.R')
source('parallel.R')
source('utils.R')

#-------------------------------------------------------------------------------
# Main
#-------------------------------------------------------------------------------

tamoxifen = dba(sampleSheet="tamoxifen.csv")
tamoxifen = dba.count(tamoxifen)
#tamoxifen = dba.contrast(tamoxifen, categories=DBA_CONDITION)
#tamoxifen = dba.analyze(tamoxifen)
#tamoxifen.DB = dba.report(tamoxifen)

