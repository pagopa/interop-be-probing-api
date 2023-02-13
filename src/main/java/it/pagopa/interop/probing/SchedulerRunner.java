/*
 * 
 */
package it.pagopa.interop.probing;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import it.pagopa.interop.probing.scheduler.JobScheduler;

/**
 * The Class SchedulerRunner.
 */
@Component
public class SchedulerRunner implements ApplicationRunner {

	/** The job scheduler. */
	private final JobScheduler jobScheduler;

	/**
	 * Instantiates a new scheduler runner.
	 *
	 * @param jobScheduler the job scheduler
	 */
	public SchedulerRunner(JobScheduler jobScheduler) {
		this.jobScheduler = jobScheduler;
	}

	/**
	 * Run.
	 *
	 * @param args the args
	 * @throws Exception the exception
	 */
	@Override
    public void run(ApplicationArguments args) throws Exception {
       jobScheduler.startBucketReaderJob();
    }
}
