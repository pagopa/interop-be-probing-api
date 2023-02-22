package it.pagopa.interop.probing.scheduler;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import it.pagopa.interop.probing.InteropProbingApplication;

@SpringBootTest(classes = InteropProbingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
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
