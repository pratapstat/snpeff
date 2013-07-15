package ca.mcgill.mcb.pcingola.chipSeq.macs;

import ca.mcgill.mcb.pcingola.interval.Marker;

/**
 * Peak information from MACS (XLS file)
 * 
 * @author pablocingolani
 */
@SuppressWarnings("serial")
public class PeakXlsInfo extends Marker {

	int tags;
	double q, fold, fdr;

	public PeakXlsInfo(Marker parent, int start, int end, String id, int tags, double q, double fold, double fdr) {
		super(parent, start, end, 1, id);
		this.tags = tags;
		this.q = q;
		this.fold = fold;
		this.fdr = fdr;
	}

	public double getFdr() {
		return fdr;
	}

	public double getFold() {
		return fold;
	}

	public double getQ() {
		return q;
	}

	public int getTags() {
		return tags;
	}

	@Override
	public String toString() {
		return getChromosomeName() + "\t" + start + "\t" + end //
				+ "\t" + ((id != null) && (id.length() > 0) ? id : "") //
				+ "\t" + tags //
				+ "\t" + q //
				+ "\t" + fold //
				+ "\t" + fdr //
		;
	}

}
