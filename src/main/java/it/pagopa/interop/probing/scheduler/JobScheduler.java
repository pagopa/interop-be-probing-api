/*
 * 
 */
package it.pagopa.interop.probing.scheduler;

import java.util.TimeZone;

import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.pagopa.interop.probing.job.BucketReaderJob;

/**
 * The Class JobScheduler.
 */
@Service
public class JobScheduler {

	/** The Constant INTEROP_JOB_GROUP. */
	private static final String INTEROP_JOB_GROUP = "interop-job-group";
	
	/** The scheduler. */
	private final Scheduler scheduler;

	/** The cron expression notify. */
	@Value("${scheduler.bucketReader.cron-expression}")
	private String cronExpressionNotify;
	
	/** The is active notify job. */
	@Value("${scheduler.bucketReader.active}")
	private boolean isActiveNotifyJob;

	/**
	 * Instantiates a new job scheduler.
	 *
	 * @param scheduler the scheduler
	 */
	public JobScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * Start bucket reader job.
	 *
	 * @throws SchedulerException the scheduler exception
	 */
	public void startBucketReaderJob() throws SchedulerException {
		if (isActiveNotifyJob)
			scheduleRecoverBucketReader();
	}

	/**
	 * Schedule recover bucket reader.
	 *
	 * @throws SchedulerException the scheduler exception
	 */
	public void scheduleRecoverBucketReader() throws SchedulerException {
		JobKey jobKey = JobKey.jobKey("ProbingBucketReader", INTEROP_JOB_GROUP);
		scheduleJob(jobKey, cronExpressionNotify, BucketReaderJob.class);
	}

	/**
	 * Schedule job.
	 *
	 * @param jobKey the job key
	 * @param cronExpression the cron expression
	 * @param jobClass the job class
	 * @throws SchedulerException the scheduler exception
	 */
	private void scheduleJob(JobKey jobKey, String cronExpression, Class<? extends Job> jobClass)
			throws SchedulerException {
		for (Trigger trigger : scheduler.getTriggersOfJob(jobKey)) {
			scheduler.unscheduleJob(trigger.getKey());
		}

		JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobKey).build();

		Trigger trigger = TriggerBuilder.newTrigger().withSchedule(
				CronScheduleBuilder.cronSchedule(cronExpression).inTimeZone(TimeZone.getTimeZone("Europe/Rome")))
				.build();

		scheduler.scheduleJob(job, trigger);
	}

}
