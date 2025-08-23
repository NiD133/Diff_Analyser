package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class, focusing on conversion capabilities.
 */
public class SecondsTest {

    @Test
    public void toStandardDays_shouldConvertSecondsToEquivalentStandardDays() {
        // Arrange: A standard day is defined as exactly 24 hours.
        // We create a Seconds instance representing two standard days.
        Seconds secondsInTwoDays = Seconds.seconds(2 * DateTimeConstants.SECONDS_PER_DAY);
        Days expectedDays = Days.TWO;

        // Act: Convert the Seconds object to standard Days.
        Days actualDays = secondsInTwoDays.toStandardDays();

        // Assert: The result should be exactly two days.
        assertEquals("Conversion from seconds to days did not yield the expected result.",
                expectedDays, actualDays);
    }
}