package ca.mcgill.mcb.pcingola.snpSift.caseControl;

import ca.mcgill.mcb.pcingola.akka.vcfStr.WorkerVcfStr;
import ca.mcgill.mcb.pcingola.vcf.VcfEntry;

/**
 * Worker agent for Case-Control command
 * 
 * @author pablocingolani
 */
public class WorkerVcfCaseControl extends WorkerVcfStr {

	VcfCaseControl vcfCaseControl;

	public WorkerVcfCaseControl(String groups, Boolean homozygousCase, Boolean homozygousControl) {
		super();
		vcfCaseControl = new VcfCaseControl(groups, homozygousCase, homozygousControl);
	}

	@Override
	public String calculate(VcfEntry vcfEntry) {
		if (vcfEntry == null) return null;
		vcfCaseControl.addInfo(vcfEntry);
		return vcfEntry.toString();
	}

}
