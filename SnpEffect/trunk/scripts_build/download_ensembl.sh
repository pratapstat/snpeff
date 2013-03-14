#!/bin/sh -e

source `dirname $0`/config.sh

#mkdir download
cd download

#---
# Download from ENSEMBL
#---

# # Download GTF files (annotations)
# wget -r -A "*gtf.gz" "ftp://ftp.ensembl.org/pub/release-$ENSEMBL_RELEASE/gtf/"
# 
# # Download FASTA files (reference genomes)
# wget -r -A "*dna.toplevel.fa.gz" "ftp://ftp.ensembl.org/pub/release-$ENSEMBL_RELEASE/fasta/"
# 
# # Download CDS sequences
# wget -r -A "*cdna.all.fa.gz" "ftp://ftp.ensembl.org/pub/release-$ENSEMBL_RELEASE/fasta/"
# 
# # Download PROTEIN sequences
# wget -r -A "*.pep.all.fa.gz" "ftp://ftp.ensembl.org/pub/release-$ENSEMBL_RELEASE/fasta/"

#---
# Create directory structure
#---

# # Move all downloaded file to this directory
# mv `find ftp.ensembl.org -type f` .
# 
# # Gene annotations files
# for gtf in *.gtf.gz
# do
# 	short=`../scripts/file2GenomeName.pl $gtf | cut -f 5`
# 	echo ANNOTATIONS: $short
# 
# 	mkdir -p data/$short
# 	cp $gtf data/$short/genes.gtf.gz
# done
#  
# # Reference genomes files
# mkdir -p data/genomes
# for fasta in *.dna.toplevel.fa.gz
# do
# 	genome=`../scripts/file2GenomeName.pl $fasta | cut -f 5`
# 	echo REFERENCE: $genome
# 
# 	cp $fasta data/genomes/$genome.fa.gz
# done
# 
# # CDS genomes files
# for fasta in *.cdna.all.fa.gz
# do
# 	genome=`../scripts/file2GenomeName.pl $fasta | cut -f 5`
# 	echo CDS: $genome
# 
# 	cp $fasta data/$genome/cds.fa.gz
# done
# 
# # Protein seuqence files
# for pep in *.pep.all.fa.gz
# do
# 	short=`../scripts/file2GenomeName.pl $pep | cut -f 5`
# 	echo PROTEIN: $short
# 
# 	mkdir -p data/$short
# 	cp $pep data/$short/protein.fa.gz
# done

#---
# Config file entries
#---

# for fasta in *.cdna.all.fa.gz
# do
# 	genome=`../scripts/file2GenomeName.pl $fasta | cut -f 4`
# 	short=`../scripts/file2GenomeName.pl $fasta | cut -f 5`
# 
# 	# Individual genome entry
# 	echo -e "$short.genome : $genome"
# 	echo -e "$short.reference : ftp://ftp.ensembl.org/pub/release-$ENSEMBL_RELEASE/gtf/"
# 	echo
# done

# Back to parent dir
cd - > /dev/null

