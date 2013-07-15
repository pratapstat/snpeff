package ca.mcgill.mcb.pcingola.chipSeq;

/***
 * How to download MACS
 * 
 * In MACS download page (http://liulab.dfci.harvard.edu/MACS/Download.html) you have 
 * to solve the following challenge in order to download the software:
 * 
 * 		hfreanzr: znpf
 * 		cnffjbeq: puvcfrd
 * 
 * Which is a simple Caesar cipher. This small program solves the challenge:
 * 
 * 		username: macs
 * 		password: chipseq
 * 
 * @author pcingola
 */
public class MacsPasswdSolve {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		char chars[] = "hfreanzr: znpf\ncnffjbeq: puvcfrd".toCharArray();
		int min = 'a', max = 'z', offset = ('s') - ('f');
		for( int i = 0; i < chars.length; i++ )
			if( Character.isLetter(chars[i]) ) chars[i] = (char) ((chars[i] - min + offset) % (max - min + 1) + min);
		System.out.println(chars);
	}
}
