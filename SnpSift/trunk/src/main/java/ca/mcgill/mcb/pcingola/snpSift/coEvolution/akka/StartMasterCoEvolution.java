package ca.mcgill.mcb.pcingola.snpSift.coEvolution.akka;

import ca.mcgill.mcb.pcingola.akka.msg.StartMaster;

/**
 * A message telling master process to start calculating
 * 
 * @author pablocingolani
 */
public class StartMasterCoEvolution extends StartMaster {

	public StartMasterCoEvolution(int batchSize, int showEvery) {
		super(batchSize, showEvery);
	}
}
