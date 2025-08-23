package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link IslamicChronology}.
 * This focuses on edge cases for year calculations.
 */
public class IslamicChronologyTest {

    /**
     * Tests that calculateFirstDayOfYearMillis() throws an ArithmeticException
     * if the requested year is greater than the maximum supported year.
     */
    @Test
    public void calculateFirstDayOfYearMillis_shouldThrowExceptionWhenYearIsTooLarge() {
        // Arrange: Create a chronology instance and determine an invalid year
        // that is just beyond the maximum supported limit.
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        int maxYear = chronology.getMaxYear();
        int invalidYear = maxYear + 1;

        // Act & Assert: Call the method and verify that the correct exception is thrown.
        try {
            chronology.calculateFirstDayOfYearMillis(invalidYear);
            fail("Expected an ArithmeticException for a year exceeding the maximum limit.");
        } catch (ArithmeticException e) {
            // Verify that the exception message is informative and correct.
            String expectedMessage = "Year is too large: " + invalidYear + " > " + maxYear;
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}