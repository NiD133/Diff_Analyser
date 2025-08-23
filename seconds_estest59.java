package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Seconds} class.
 */
public class SecondsTest {

    /**
     * Tests that converting a Seconds value that is less than a full day (86,400 seconds)
     * correctly truncates to zero days.
     */
    @Test
    public void toStandardDays_whenSecondsAreLessThanOneDay_returnsZeroDays() {
        // Arrange: A period of 2 seconds is significantly less than a standard day.
        Seconds twoSeconds = Seconds.TWO;

        // Act: Convert the seconds period to standard days.
        Days result = twoSeconds.toStandardDays();

        // Assert: The result should be zero days, as the conversion truncates fractions.
        assertEquals(Days.ZERO, result);
    }
}