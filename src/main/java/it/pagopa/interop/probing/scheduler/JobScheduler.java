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
	private static final String INTEROP_JOB_GROUP_KEY = "interop-job-group";

	private static final String BUCKET_READER_JOB_KEY = "BUCKET_READER_JOB";

	/** The Constant RETRY_INITIAL_INTERVAL_SECS_KEY. */
	private static final String RETRY_INITIAL_INTERVAL_SECS_KEY = "RETRY_INITIAL_INTERVAL_SECS";

	/** The scheduler. */
	private final Scheduler scheduler;

	/** The cron expression notify. */
	@Value("${scheduler.bucketReader.daily-minutes}")
	private int dailyMin;

	/** The daily hour. */
	@Value("${scheduler.bucketReader.daily-hours}")
	private int dailyHour;

	/** The is active notify job. */
	@Value("${scheduler.bucketReader.active}")
	private boolean isActiveNotifyJob;

	/** The retry interval secs. */
	@Value("${scheduler.bucketReader.retry-initial-interval}")
	private int retryIntervalSecs;

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
		JobKey jobKey = JobKey.jobKey(BUCKET_READER_JOB_KEY, INTEROP_JOB_GROUP_KEY);
		scheduleJob(jobKey, BucketReaderJob.class);
	}

	/**
	 * Schedule job.
	 *
	 * @param jobKey   the job key
	 * @param jobClass the job class
	 */
	private void scheduleJob(JobKey jobKey, Class<? extends Job> jobClass) {
		try {
			for (Trigger trigger : scheduler.getTriggersOfJob(jobKey)) {
				scheduler.unscheduleJob(trigger.getKey());
			}
			JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobKey)
					.usingJobData(RETRY_INITIAL_INTERVAL_SECS_KEY, retryIntervalSecs).build();
//			Trigger trigger = TriggerBuilder.newTrigger()
//					.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(dailyHour, dailyMin)).build();
			/* TEST */
			Trigger trigger = TriggerBuilder.newTrigger().withSchedule(
					CronScheduleBuilder.cronSchedule("0 /1 * ? * *").inTimeZone(TimeZone.getTimeZone("Europe/Rome")))
					.build();

			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
