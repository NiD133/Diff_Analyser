package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A test suite for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that converting a {@link Weeks} object to {@link Hours} produces the correct result.
     * The test case uses a {@code Weeks} value that is itself derived from converting the
     * maximum possible {@code Seconds} value, verifying the chain of conversions.
     */
    @Test
    public void toStandardHours_convertsCorrectlyFromWeeksDerivedFromMaxSeconds() {
        // Arrange: Define the constants needed for conversion and calculate the expected result.
        final int HOURS_PER_DAY = 24;
        final int DAYS_PER_WEEK = 7;
        final int SECONDS_PER_WEEK = 60 * 60 * HOURS_PER_DAY * DAYS_PER_WEEK;

        // When converting from a smaller unit (seconds) to a larger one (weeks),
        // Joda-Time uses integer division, truncating any remainder.
        int expectedWeeks = Integer.MAX_VALUE / SECONDS_PER_WEEK; // Result is 3550
        int expectedHours = expectedWeeks * HOURS_PER_DAY * DAYS_PER_WEEK; // Result is 596400

        // Start with the maximum number of seconds.
        Seconds maxSeconds = Seconds.MAX_VALUE;

        // Act: Perform the two-step conversion: Seconds -> Weeks -> Hours.
        Weeks weeks = maxSeconds.toStandardWeeks();
        Hours hours = weeks.toStandardHours();

        // Assert: Verify that the final number of hours matches the calculated expectation.
        assertEquals(expectedHours, hours.getHours());
    }
}