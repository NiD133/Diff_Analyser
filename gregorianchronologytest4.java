package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateTimeZone;
import org.junit.Test;

/**
 * Unit tests for the factory method {@link GregorianChronology#getInstance(DateTimeZone, int)}.
 */
public class GregorianChronology_GetInstanceWithZoneAndMinDaysTest {

    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    @Test
    public void shouldReturnChronologyWithCorrectProperties() {
        // Arrange
        final int minDaysInFirstWeek = 2;

        // Act
        GregorianChronology chrono = GregorianChronology.getInstance(TOKYO, minDaysInFirstWeek);

        // Assert
        assertEquals("The time zone should match the one provided.", TOKYO, chrono.getZone());
        assertEquals("The minimum days in the first week should match the one provided.",
                minDaysInFirstWeek, chrono.getMinimumDaysInFirstWeek());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenMinDaysIsLessThanOne() {
        // The valid range for minimum days in the first week is 1 to 7.
        // This test verifies the lower bound by passing 0.
        final int invalidMinDays = 0;
        GregorianChronology.getInstance(TOKYO, invalidMinDays);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenMinDaysIsGreaterThanSeven() {
        // The valid range for minimum days in the first week is 1 to 7.
        // This test verifies the upper bound by passing 8.
        final int invalidMinDays = 8;
        GregorianChronology.getInstance(TOKYO, invalidMinDays);
    }
}