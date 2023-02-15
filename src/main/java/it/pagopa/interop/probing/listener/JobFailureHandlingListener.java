/*
 * 
 */
package it.pagopa.interop.probing.listener;

import java.time.LocalDateTime;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/** The Constant log. */
@Slf4j
@Component
public class JobFailureHandlingListener implements JobListener {

	/** The Constant RETRY_NUMBER_KEY. */
	private static final String RETRY_NUMBER_KEY = "RETRY_NUMBER";

	/** The Constant RETRY_INITIAL_INTERVAL_SECS_KEY. */
	private static final String RETRY_INITIAL_INTERVAL_SECS_KEY = "RETRY_INITIAL_INTERVAL_SECS";

	/** The Constant RETRY_INTERVAL_MULTIPLIER_KEY. */
	private static final String RETRY_INTERVAL_MULTIPLIER_KEY = "RETRY_INTERVAL_MULTIPLIER";

	/** The Constant DEFAULT_RETRY_INTERVAL_MULTIPLIER. */
	private static final double DEFAULT_RETRY_INTERVAL_MULTIPLIER = 1.5;

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	@Override
	public String getName() {
		return "FailJobListener";
	}

	/**
	 * Job to be executed.
	 *
	 * @param context the context
	 */
	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
		context.getMergedJobDataMap().merge(RETRY_NUMBER_KEY, 1, (oldValue, initValue) -> ((int) oldValue) + 1);
	}

	/**
	 * Job execution vetoed.
	 *
	 * @param context the context
	 */
	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
	}

	/**
	 * Job was executed.
	 *
	 * @param context      the context
	 * @param jobException the job exception
	 */
	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {

		if (jobException == null) {
			return;
		}
		String jobName = context.getJobDetail().getKey().getName();
		log.info(jobName + " Error in job at : " + LocalDateTime.now());
		log.error(jobName + " " + jobException.getMessage());
		int timesRetried = (int) context.getMergedJobDataMap().get(RETRY_NUMBER_KEY);

		TriggerKey triggerKey = context.getTrigger().getKey();
		int initialIntervalSecs = (int) context.getMergedJobDataMap().get(RETRY_INITIAL_INTERVAL_SECS_KEY);
		double multiplier = (double) context.getMergedJobDataMap().computeIfAbsent(RETRY_INTERVAL_MULTIPLIER_KEY,
				key -> DEFAULT_RETRY_INTERVAL_MULTIPLIER);
		Date newStartTime = ExponentialRandomBackoffFixtures.getNextStartDate(timesRetried, initialIntervalSecs,
				multiplier);
		Trigger newTrigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).startAt(newStartTime)
				.usingJobData(context.getMergedJobDataMap()).build();
		newTrigger.getJobDataMap().put(RETRY_NUMBER_KEY, timesRetried);

		log.info(jobName + " next try at : " + newStartTime);
		try {
			context.getScheduler().rescheduleJob(triggerKey, newTrigger);
		} catch (SchedulerException e) {
			log.error(context.getTrigger().getNextFireTime() + " " + jobException.getMessage());
		}
	}

}