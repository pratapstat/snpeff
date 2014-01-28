#!/bin/sh

cd $HOME/workspace/SnpSift/
java -Xmx4g -cp SniSift.jar ca.mcgill.mcb.pcingola.snpSift.testCases.TestSuiteAll 2>&1 | tee testSuiteAll.snpsift.txt

cd $HOME/workspace/SnpEff/
java -Xmx4g -cp snpEff.jar ca.mcgill.mcb.pcingola.snpEffect.testCases.TestSuiteAll 2>&1 | tee testSuiteAll.snpeff.txt

