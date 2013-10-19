package ca.mcgill.mcb.pcingola.snpSift.coEvolution.akka;

import ca.mcgill.mcb.pcingola.akka.Worker;
import ca.mcgill.mcb.pcingola.snpSift.coEvolution.SnpSiftCmdCoEvolution;

/**
 * Worker agent 
 * 
 * @author pablocingolani
 */
public class WorkerCoEvolution extends Worker<Integer, String> {

	SnpSiftCmdCoEvolution snpSiftCmdCoEvolution; // Used only to show errors

	public WorkerCoEvolution(SnpSiftCmdCoEvolution snpSiftCmdCoEvolution) {
		super();
		this.snpSiftCmdCoEvolution = snpSiftCmdCoEvolution;
	}

	@Override
	public String calculate(Integer idx) {
		String out = "";
		try {
			out = snpSiftCmdCoEvolution.runCoEvolution(idx);
		} catch (Throwable t) {
			t.printStackTrace();
		}

		return out.isEmpty() ? null : out;
	}

}
