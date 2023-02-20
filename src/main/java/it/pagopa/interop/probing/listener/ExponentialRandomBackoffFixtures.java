package it.pagopa.interop.probing.listener;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.commons.lang3.RandomUtils;

public class ExponentialRandomBackoffFixtures {

	public static Date getNextStartDate(int timesRetried, int initialIntervalSecs, double multiplier) {
		double minValue = initialIntervalSecs * Math.pow(multiplier, timesRetried - 1D);
		double maxValue = minValue * multiplier;
		Duration duration = Duration.ofMillis((long) (RandomUtils.nextDouble(minValue, maxValue) * 1000));
		LocalDateTime nextDateTime = LocalDateTime.now().plus(duration);
		return Date.from(nextDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	private ExponentialRandomBackoffFixtures() {
	}

}