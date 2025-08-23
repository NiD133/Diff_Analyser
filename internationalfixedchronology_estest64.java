package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link InternationalFixedChronology#isLeapYear(long)} method.
 */
public class InternationalFixedChronologyTest {

    @Test
    public void isLeapYear_whenYearIsDivisibleBy4_shouldReturnTrue() {
        // The International Fixed Chronology shares the same leap year rules as the Gregorian calendar.
        // A year is a leap year if it is divisible by 4, unless it is a century year not divisible by 400.
        
        // Arrange: Get an instance of the chronology and define a simple leap year to test.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        long year = 4L;

        // Act: Check if the year is a leap year.
        boolean isLeap = chronology.isLeapYear(year);

        // Assert: The result should be true, as year 4 is a leap year.
        assertTrue("Year 4 should be correctly identified as a leap year.", isLeap);
    }
}