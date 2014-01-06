package ca.mcgill.mcb.pcingola.snpSift.coEvolution.akka;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * A work queue  
 * 
 * @author pablocingolani
 */
public class WorkQueueCoEvolution {

	int batchSize, showEvery;
	Class<? extends Actor> masterClazz;
	Props masterProps;

	public WorkQueueCoEvolution(int batchSize, int showEvery, Props masterProps) {
		this.batchSize = batchSize;
		this.showEvery = showEvery;
		this.masterProps = masterProps;
	}

	public void run(boolean wait) {
		// Create an Akka system
		ActorSystem workQueue = ActorSystem.create("WorkQueueCoEvolution");

		// Create the master
		ActorRef master;
		if (masterClazz != null) master = workQueue.actorOf(new Props(masterClazz), "masterCoEvolution");
		else master = workQueue.actorOf(masterProps, "masterCoEvolution");

		// Start processing
		master.tell(new StartMasterCoEvolution(batchSize, showEvery));

		// Wait until completion
		if (wait) workQueue.awaitTermination();
	}
}
