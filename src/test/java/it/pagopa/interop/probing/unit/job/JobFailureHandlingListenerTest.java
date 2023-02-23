package it.pagopa.interop.probing.unit.job;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import it.pagopa.interop.probing.config.MockMessageListenerConfiguration;
import it.pagopa.interop.probing.listener.JobFailureHandlingListener;

@SpringBootTest
@Import({MockMessageListenerConfiguration.class})
@TestPropertySource(properties = { "scheduler.bucketReader.active = true" })
class JobFailureHandlingListenerTest {

	@InjectMocks
	JobFailureHandlingListener mockJobFailureHandlingListener;
	
	@Mock
	JobExecutionContext context;

	private static final String JOB_NAME = "testJob";
	private static final double DEFAULT_RETRY_INTERVAL_MULTIPLIER = 2.0;

	@Test
	@DisplayName("job is rescheduled")
		void testJobWasExecuted_whenGivenValidJobExecutionContextAndJobExecutionException_thenJobIsRescheduled() throws SchedulerException {
			Scheduler scheduler = mock(Scheduler.class);
			Trigger trigger = mock(Trigger.class);
			JobDataMap map = mock(JobDataMap.class);
			JobKey jobKey = new JobKey(JOB_NAME); 
			JobDetail jobDetail = mock(JobDetail.class); 
			Mockito.when(jobDetail.getKey()).thenReturn(jobKey); 
			Mockito.when(context.getJobDetail()).thenReturn(jobDetail);
			when(context.getTrigger()).thenReturn(trigger);
			when(context.getTrigger().getKey()).thenReturn(new TriggerKey("testTrigger"));
			when(context.getMergedJobDataMap()).thenReturn(map);
			when(context.getMergedJobDataMap().get(Mockito.anyString())).thenReturn(0, 0);
			when(context.getMergedJobDataMap().computeIfAbsent(Mockito.anyString(), Mockito.any())).thenReturn(DEFAULT_RETRY_INTERVAL_MULTIPLIER);
			
			when(context.getScheduler()).thenReturn(scheduler);
			when(scheduler.rescheduleJob(any(TriggerKey.class), any(Trigger.class))).thenReturn(null);
			JobExecutionException jobException = new JobExecutionException(new SchedulerException());
			
			mockJobFailureHandlingListener.jobWasExecuted(context, jobException);
			
			verify(context, times(1)).getJobDetail();
			verify(context, times(1)).getScheduler();
			verify(scheduler, times(1)).rescheduleJob(any(TriggerKey.class), any(Trigger.class));
			verify(context, times(1)).getScheduler();
			verify(scheduler, times(1)).rescheduleJob(any(TriggerKey.class), any(Trigger.class));
		}
}
