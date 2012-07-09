package ca.mcgill.mcb.pcingola.snpSift.caseControl;

import akka.actor.Actor;
import akka.actor.Props;
import akka.actor.UntypedActorFactory;
import ca.mcgill.mcb.pcingola.akka.vcf.MasterVcf;

/**
 * Master agent for Case-Control command
 * 
 * @author pablocingolani
 */
public class MasterVcfCaseControl extends MasterVcf<String> {

	public MasterVcfCaseControl(int numWorkers, final String groups, final Boolean homozygousCase, final Boolean homozygousControl) {
		super(new Props( //
				// Create a factory
				new UntypedActorFactory() {

					@Override
					public Actor create() {
						return new WorkerVcfCaseControl(groups, homozygousCase, homozygousControl);
					}

				}) //
				, numWorkers);
	}
}
