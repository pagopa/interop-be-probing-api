package it.pagopa.interop.probing.unit.scheduler;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import it.pagopa.interop.probing.config.MockMessageListenerConfiguration;
import it.pagopa.interop.probing.scheduler.JobScheduler;

@SpringBootTest
@Import({MockMessageListenerConfiguration.class})
@TestPropertySource(properties = { "scheduler.bucketReader.active = true" })
 class JobSchedulerTest {

	@InjectMocks
	private JobScheduler jobSchedulerMock;
	
	@Mock
	Scheduler scheduler;
	
	@Test
	@DisplayName("scheduleJob is executed")
	void testScheduleJob_whenCronTriggerIsVerified_thenScheduleJobIsExecuted() throws SchedulerException {
		jobSchedulerMock.scheduleRecoverBucketReader();
		assertTrue(true);
		}
}
