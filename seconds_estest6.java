package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that converting from Seconds to Minutes correctly truncates the result
     * when the total number of seconds is not an exact multiple of 60.
     * For example, 1575 seconds is 26.25 minutes, which should be truncated to 26 minutes.
     */
    @Test
    public void toStandardMinutes_shouldTruncatePartialMinutes() {
        // Arrange
        final int totalSeconds = 1575; // Equivalent to 26 minutes and 15 seconds
        final Seconds seconds = Seconds.seconds(totalSeconds);
        final int expectedMinutes = 26;

        // Act
        final Minutes actualMinutes = seconds.toStandardMinutes();

        // Assert
        assertEquals("Conversion to minutes should truncate, not round, the result.",
                expectedMinutes, actualMinutes.getMinutes());
    }
}