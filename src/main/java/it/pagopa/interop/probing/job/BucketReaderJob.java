/*
 * 
 */
package it.pagopa.interop.probing.job;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.pagopa.interop.probing.dto.EserviceDTO;
import it.pagopa.interop.probing.producer.ServicesSend;
import it.pagopa.interop.probing.service.BucketService;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class BucketReaderJob.
 */
@Component
@Slf4j
@DisallowConcurrentExecution
public class BucketReaderJob implements Job {

	@Autowired
	BucketService bucketService;

	@Autowired
	ServicesSend producer;

	@Value("${amazon.sqs.end-point.services-queue}")
	private String url;

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
		List<EserviceDTO> listEservice = bucketService.readObject();
		for (EserviceDTO eserviceDTO : listEservice) {
			try {
				producer.sendMessage(eserviceDTO, url);
			} catch (JsonProcessingException e) {
				log.error(e.getMessage());
			}
		}
		LocalDateTime end = LocalDateTime.now();
		log.info(jobName + " ended in: " + Duration.between(start, end).getSeconds() + " seconds at: " + Instant.now());
	}
}