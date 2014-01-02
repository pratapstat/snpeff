#!/bin/sh

#---
# Create config file based on individual config files
#---

cat config/snpEff.core.config \
	config/snpEff.dbs.config \
	config/snpEff.test.config \
	config/snpEff.ENSEMBL_64.config \
	config/snpEff.ENSEMBL_65.config \
	config/snpEff.ENSEMBL_66.config \
	config/snpEff.ENSEMBL_67.config \
	config/snpEff.ENSEMBL_68.config \
	config/snpEff.ENSEMBL_69.config \
	config/snpEff.ENSEMBL_70.config \
	config/snpEff.ENSEMBL_71.config \
	config/snpEff.ENSEMBL_72.config \
	config/snpEff.ENSEMBL_73.config \
	config/snpEff.ENSEMBL_74.config \
	config/snpEff.ENSEMBL_BFMPP_18.config \
	config/snpEff.NCBI_bacterial.config \
	> snpEff.config
