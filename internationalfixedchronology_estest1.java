package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    /**
     * The International Fixed Chronology shares its leap year rule with the Gregorian calendar.
     * This test verifies that a year divisible by 100 but not by 400 is correctly
     * identified as a non-leap year. This rule applies to proleptic years as well.
     */
    @Test
    public void isLeapYear_whenYearIsDivisibleBy100ButNot400_returnsFalse() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        // A year like 1900 is a well-known example of a century that is not a leap year.
        // The original test used a large negative number, so we use a simpler, positive
        // example to make the intent clear.
        long nonLeapCenturyYear = 1900L;

        // Act
        boolean isLeap = chronology.isLeapYear(nonLeapCenturyYear);

        // Assert
        assertFalse("A year divisible by 100 but not 400 should not be a leap year.", isLeap);
    }
}