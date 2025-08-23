package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    /**
     * Tests that converting a {@link Weeks} object to a standard duration
     * results in the correct number of milliseconds.
     */
    @Test
    public void toStandardDuration_forTwoWeeks_convertsToCorrectMilliseconds() {
        // Arrange
        Weeks twoWeeks = Weeks.TWO;
        // A standard week contains 604,800,000 milliseconds (7 * 24 * 60 * 60 * 1000).
        // Using a constant makes the expected value's origin clear and less error-prone.
        final long expectedMillis = 2L * DateTimeConstants.MILLIS_PER_WEEK;

        // Act
        Duration duration = twoWeeks.toStandardDuration();

        // Assert
        assertEquals("The duration for two weeks should be the correct number of milliseconds.",
                     expectedMillis, duration.getMillis());
    }
}