package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link InternationalFixedChronology} class.
 */
public class InternationalFixedChronologyTest {

    @Test
    public void isLeapYear_returnsFalse_forYearNotDivisibleByFour() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        // The International Fixed Chronology uses the same leap year rule as the Gregorian calendar.
        // A year is not a leap year if it is not divisible by 4.
        long year = 241L;

        // Act
        boolean isLeap = chronology.isLeapYear(year);

        // Assert
        assertFalse("A year that is not divisible by 4 should not be a leap year.", isLeap);
    }
}