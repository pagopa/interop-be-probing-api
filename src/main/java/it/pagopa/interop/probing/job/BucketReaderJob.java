/*
 * 
 */
package it.pagopa.interop.probing.job;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
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

	/**
	 * Execute.
	 *
	 * @param context the context
	 * @throws JobExecutionException the job execution exception
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {
			String jobName = context.getJobDetail().getKey().getName();
			log.info(jobName + " started at :" + LocalDateTime.now());
			LocalDateTime start = LocalDateTime.now();
			int zero = 0;
			int calculation = 4815 / 0;
			LocalDateTime end = LocalDateTime.now();
			log.info(jobName + " ended in: " + Duration.between(start, end).getSeconds() + " seconds at: "
					+ Instant.now());
	}

}