package org.threeten.extra.scale;

import org.junit.Test;
import java.time.Duration;
import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link UtcInstant}.
 * This class contains an improved version of an originally auto-generated test.
 */
public class UtcInstantTest {

    /**
     * Tests that subtracting a Duration correctly rolls back the day
     * when the result crosses a midnight boundary into the previous day.
     *
     * <p>This scenario tests subtracting a duration (e.g., a few hours) from an
     * instant that is very close to the start of a day (a few nanoseconds past midnight).
     * The expected result is an instant on the previous day, with the nano-of-day
     * calculated correctly from the start of that previous day.
     */
    @Test
    public void minus_whenDurationCrossesDayBoundary_calculatesCorrectly() {
        // --- Arrange ---
        // An instant just 140 nanoseconds after the start of Modified Julian Day 140.
        long initialMjd = 140L;
        long initialNanoOfDay = 140L;
        UtcInstant startInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanoOfDay);

        // A duration of 140 minutes (2 hours, 20 minutes), which is larger than
        // the elapsed time into the day (140 ns).
        Duration durationToSubtract = Duration.ofMinutes(140);

        // --- Act ---
        UtcInstant resultInstant = startInstant.minus(durationToSubtract);

        // --- Assert ---
        // The subtraction should roll back to the previous day.
        long expectedMjd = initialMjd - 1;

        // The new nano-of-day is calculated by subtracting the "overdraft" from a full day's worth of nanoseconds.
        // Overdraft = (duration in nanos) - (initial nanos into the day)
        // Expected nano-of-day = (nanos in a standard day) - overdraft
        final long NANOS_PER_STANDARD_DAY = 86_400L * 1_000_000_000L;
        long expectedNanoOfDay = NANOS_PER_STANDARD_DAY - (durationToSubtract.toNanos() - initialNanoOfDay);

        assertEquals("Modified Julian Day should roll back by one",
                expectedMjd, resultInstant.getModifiedJulianDay());
        assertEquals("Nano-of-day should be calculated from the start of the previous day",
                expectedNanoOfDay, resultInstant.getNanoOfDay());
    }
}