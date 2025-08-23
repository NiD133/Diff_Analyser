package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link UtcInstant} class.
 */
public class UtcInstantTest {

    // A standard day has 86,400 seconds, and each second has 1,000,000,000 nanoseconds.
    private static final long NANOS_IN_STANDARD_DAY = 86_400L * 1_000_000_000L;

    /**
     * Tests that adding a small negative duration to an instant at the start of a day
     * correctly rolls back the time to the end of the previous day.
     */
    @Test
    public void plus_withNegativeDuration_shouldRollBackAcrossDayBoundary() {
        // Arrange: Create an instant at the exact start of a day (midnight).
        long initialModifiedJulianDay = -571L;
        UtcInstant startOfDay = UtcInstant.ofModifiedJulianDay(initialModifiedJulianDay, 0L);

        // Arrange: Define a small duration to subtract, causing a day boundary to be crossed.
        long nanosToSubtract = 1606L;
        Duration smallNegativeDuration = Duration.ofNanos(-nanosToSubtract);

        // Act: Add the negative duration to the instant.
        UtcInstant result = startOfDay.plus(smallNegativeDuration);

        // Assert: The resulting instant should be on the previous day.
        long expectedModifiedJulianDay = initialModifiedJulianDay - 1;
        assertEquals(
            "The Modified Julian Day should roll back by one",
            expectedModifiedJulianDay,
            result.getModifiedJulianDay()
        );

        // Assert: The nano-of-day should correspond to the time just before midnight on the previous day.
        // This assumes the previous day is a standard 86400-second day.
        long expectedNanoOfDay = NANOS_IN_STANDARD_DAY - nanosToSubtract;
        assertEquals(
            "The nano-of-day should be at the end of the previous day",
            expectedNanoOfDay,
            result.getNanoOfDay()
        );
    }
}