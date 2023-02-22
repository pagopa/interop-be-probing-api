package it.pagopa.interop.probing.listener;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import it.pagopa.interop.probing.InteropProbingApplication;

@SpringBootTest(classes = InteropProbingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
@TestPropertySource(properties = { "spring.quartz.properties.org.quartz.scheduler.skipUpdateCheck = true" })

 class ExponentialRandomBackoffFixturesTest {
	
	@Test
	@DisplayName("GetNextStartDate is executed")
	void testGetNextStartDate_whenGivenValidTimesRetriedAndInitialIntervalAndMultiplier_thenStartDateIsNotNullAndAfterNow() {
		int timesRetried = 1;
		int initialIntervalSecs = 5;
		double multiplier = 2.0;
		
		Date now = new Date();	
		Date startDate = ExponentialRandomBackoffFixtures.getNextStartDate(timesRetried, initialIntervalSecs, multiplier);
		
		assertNotNull(startDate, "startDate shouldn't be null");
		assertTrue(startDate.after(now), "startDate shouldn't be after actual date");
		
	}

}
