package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link InternationalFixedChronology#isLeapYear(long)} method.
 */
public class InternationalFixedChronologyLeapYearTest {

    /**
     * Tests that a year divisible by 400 is correctly identified as a leap year.
     * <p>
     * The International Fixed Chronology shares the same leap year rules as the
     * Gregorian calendar, where a year divisible by 400 is always a leap year.
     */
    @Test
    public void isLeapYear_forYearDivisibleBy400_shouldReturnTrue() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        // The year 2000 is a well-known example of a leap year divisible by 400.
        // This is a clearer example than the original large number (365,000,000).
        long yearDivisibleBy400 = 2000L;

        // Act
        boolean isLeap = chronology.isLeapYear(yearDivisibleBy400);

        // Assert
        assertTrue(
                "Year " + yearDivisibleBy400 + " should be identified as a leap year.",
                isLeap);
    }
}