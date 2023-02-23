package it.pagopa.interop.probing.unit.listener;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import it.pagopa.interop.probing.config.MockMessageListenerConfiguration;
import it.pagopa.interop.probing.listener.ExponentialRandomBackoffFixtures;

@SpringBootTest
@Import({MockMessageListenerConfiguration.class})
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
