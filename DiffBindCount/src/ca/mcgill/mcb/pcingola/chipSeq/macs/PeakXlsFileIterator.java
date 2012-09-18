package ca.mcgill.mcb.pcingola.chipSeq.macs;

import java.io.IOException;

import ca.mcgill.mcb.pcingola.fileIterator.MarkerFileIterator;
import ca.mcgill.mcb.pcingola.interval.Chromosome;
import ca.mcgill.mcb.pcingola.interval.Genome;
import ca.mcgill.mcb.pcingola.util.Gpr;

/**
 * Opens a sequence change file and iterates over all intervals in '*peaks.xls' format.
 * See MACS peaks (it's just an interval with a couple more parameters).
 *     
 * @author pcingola
 */
public class PeakXlsFileIterator extends MarkerFileIterator<PeakXlsInfo> {

	protected Genome genome;

	public PeakXlsFileIterator(String fileName) {
		super(fileName, 1);
	}

	@Override
	protected PeakXlsInfo readNext() {
		// Try to read a line
		try {
			while (ready()) {
				line = readLine();

				if (line == null) return null; // End of file?

				// Ignore empty lines and comment lines
				if ((line.length() > 0) && (!line.startsWith("#"))) {
					// Parse line
					String fields[] = line.split("\\s");

					// Is line OK?
					if (fields.length >= 3) {
						String chromosome = fields[0].trim();
						Chromosome chromo = getChromosome(chromosome);
						sanityCheckChromo(chromosome, chromo); // Sanity check

						int start = parsePosition(fields[1]);
						int end = parsePosition(fields[2]);
						int tags = Gpr.parseIntSafe(fields[5]);
						double q = Gpr.parseDoubleSafe(fields[6]);
						double fold = Gpr.parseDoubleSafe(fields[7]);
						double fdr = -1;
						if (fields.length > 8) fdr = Gpr.parseDoubleSafe(fields[8]);

						String id = "line_" + lineNum;

						return new PeakXlsInfo(chromo, start, end, id, tags, q, fold, fdr);
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
}
