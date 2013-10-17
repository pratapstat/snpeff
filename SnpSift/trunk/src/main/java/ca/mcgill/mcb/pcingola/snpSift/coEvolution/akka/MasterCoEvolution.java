package ca.mcgill.mcb.pcingola.snpSift.coEvolution.akka;

import akka.actor.Actor;
import akka.actor.Props;
import akka.actor.UntypedActorFactory;
import ca.mcgill.mcb.pcingola.akka.Master;
import ca.mcgill.mcb.pcingola.snpSift.coEvolution.SnpSiftCmdCoEvolution;

/**
 * Master agent
 * 
 * @author pablocingolani
 */
public class MasterCoEvolution extends Master<Integer, String> {

	SnpSiftCmdCoEvolution snpSiftCmdCoEvolution;
	int idx = 0;

	@SuppressWarnings("serial")
	public MasterCoEvolution(int numWorkers, final SnpSiftCmdCoEvolution snpSiftCmdCoEvolution) {
		super(new Props( //
				// Create a factory
				new UntypedActorFactory() {

					@Override
					public Actor create() {
						return new WorkerCoEvolution(snpSiftCmdCoEvolution);
					}

				}) //
				, numWorkers);

		this.snpSiftCmdCoEvolution = snpSiftCmdCoEvolution;
	}

	@Override
	public boolean hasNext() {
		return idx < snpSiftCmdCoEvolution.getGenotypes().size();
	}

	@Override
	public Integer next() {
		return idx++;
	}
}
