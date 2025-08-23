package org.threeten.extra.scale;

import org.junit.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.JulianFields;
import static org.junit.Assert.assertFalse;

/**
 * This test verifies the behavior of the {@link UtcInstant#isLeapSecond()} method.
 *
 * <p>This version has been improved for understandability from an auto-generated test.
 */
public class UtcInstant_ESTestTest57 { // Class name retained from original

    /**
     * Tests that isLeapSecond() returns false for an instant that is known
     * not to be a leap second.
     *
     * <p>The original test used large, arbitrary numbers, making its purpose unclear.
     * This version uses a well-known date and time (noon on January 1st, 2000)
     * to make the test's intent obvious and easily verifiable.
     */
    @Test
    public void isLeapSecond_shouldReturnFalse_forNonLeapSecondInstant() {
        // Arrange: Create an instant for a known date and time that is not a leap second.
        // January 1st, 2000 is a well-known date that did not have a leap second.
        long mjd = LocalDate.of(2000, 1, 1).getLong(JulianFields.MODIFIED_JULIAN_DAY);
        long nanoOfDay = LocalTime.NOON.toNanoOfDay();

        UtcInstant nonLeapSecondInstant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);

        // Act: Check if the instant is considered a leap second.
        boolean isLeap = nonLeapSecondInstant.isLeapSecond();

        // Assert: The result should be false, as the date is not a leap second day.
        assertFalse("isLeapSecond() should return false for a standard instant.", isLeap);
    }
}