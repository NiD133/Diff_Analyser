package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link InternationalFixedChronology} class, focusing on its leap year logic.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that a century year is not a leap year if it is not divisible by 400.
     * <p>
     * The International Fixed Chronology shares its leap year rules with the Gregorian calendar.
     * According to this rule, a year like 100 is not a leap year because, although it is
     * divisible by 4, it is a century year that is not divisible by 400.
     */
    @Test
    public void isLeapYear_shouldReturnFalse_forCenturyNotDivisibleBy400() {
        // Arrange: The InternationalFixedChronology uses a singleton instance.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;

        // Act & Assert: Verify that the year 100 is correctly identified as a non-leap year.
        assertFalse("Year 100 should not be a leap year", chronology.isLeapYear(100L));
    }
}