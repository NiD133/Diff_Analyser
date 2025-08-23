package org.joda.time.chrono;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A test class focused on the understandability of testing the
 * {@link IslamicChronology#setYear(long, int)} method.
 */
public class IslamicChronologySetYearTest {

    /**
     * Tests that setting the year on the first day of the Islamic epoch (year 1)
     * correctly returns the first day of the target year.
     */
    @Test
    public void setYear_onFirstDayOfEpoch_returnsFirstDayOfTargetYear() {
        // Arrange
        // Use a fixed time zone (UTC) to ensure the test is deterministic and not
        // dependent on the system's default time zone.
        DateTimeZone utcZone = DateTimeZone.UTC;
        IslamicChronology islamicChronology = IslamicChronology.getInstance(utcZone);

        // The Islamic calendar epoch starts at year 1, month 1, day 1.
        // This represents the instant -42246144000000L from the original test.
        DateTime startOfIslamicEpoch = new DateTime(1, 1, 1, 0, 0, islamicChronology);

        int targetYear = 1900;

        // The expected result is the same date and time (month, day, time-of-day),
        // but in the target year. This corresponds to 15651100800000L.
        DateTime expectedDate = new DateTime(targetYear, 1, 1, 0, 0, islamicChronology);

        // Act
        // Change the year of the 'startOfIslamicEpoch' instant to the 'targetYear'.
        long resultMillis = islamicChronology.setYear(startOfIslamicEpoch.getMillis(), targetYear);

        // Assert
        // The resulting millisecond instant should match the expected date's instant.
        assertEquals(expectedDate.getMillis(), resultMillis);
    }
}