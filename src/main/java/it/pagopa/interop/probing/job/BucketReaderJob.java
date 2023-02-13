/*
 * 
 */
package it.pagopa.interop.probing.job;

import java.time.Duration;
import java.time.Instant;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class BucketReaderJob.
 */
@Component

/** The Constant log. */
@Slf4j
@DisallowConcurrentExecution
public class BucketReaderJob implements Job {

	/** The Constant JOB_LOG_NAME. */
	private static final String JOB_LOG_NAME = "BUCKET_READER_JOB ";
	
	/** The Constant NUM_EXECUTIONS. */
	public static final String NUM_EXECUTIONS = "NUM_EXECUTIONS";
	
	/** The Constant EXECUTION_DELAY. */
	public static final String EXECUTION_DELAY = "EXECUTION_DELAY";

	/** The max execution num. */
	@Value("${scheduler.bucketReader.max-execution}")
	private int maxExecutionNum;

	/**
	 * Execute.
	 *
	 * @param context the context
	 * @throws JobExecutionException the job execution exception
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			log.info(JOB_LOG_NAME + "started at :" + Instant.now());
			Instant start = Instant.now();
			int zero = 0;
			int calculation = 4815 / 2;
			Instant end = Instant.now();
			log.info(JOB_LOG_NAME + "ended in: " + Duration.between(start, end).getSeconds() + " seconds at: "
					+ Instant.now());
		} catch (Exception e) {
			log.error(JOB_LOG_NAME + e.getMessage());
			log.info(JOB_LOG_NAME + "Error in job at : " + Instant.now());

			JobExecutionException jobExecutionException = new JobExecutionException(e, true);
			JobDataMap map = context.getJobDetail().getJobDataMap();

			if (map.containsKey(EXECUTION_DELAY))
				map.put(NUM_EXECUTIONS, map.getInt(NUM_EXECUTIONS) + 1);
			else {
				map.put(NUM_EXECUTIONS, 0);
				map.put(EXECUTION_DELAY, true);
			}

			if (map.getInt(NUM_EXECUTIONS) < maxExecutionNum) {
				log.info(JOB_LOG_NAME + "immediate retry at : " + Instant.now());
				jobExecutionException.refireImmediately();
			} else
				try {
					log.info(JOB_LOG_NAME + "wait 5 minutes for next try : " + Instant.now());
					Thread.sleep(5 * 60 * 1000);
					jobExecutionException.refireImmediately();
				} catch (InterruptedException e1) {
					log.error(e1.getMessage());
					Thread.currentThread().interrupt();
				}

			throw jobExecutionException;
		}
	}

}