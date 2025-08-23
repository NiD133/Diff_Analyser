package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test case for the {@link InternationalFixedChronology#getLeapYearsBefore(long)} method.
 * This test focuses on verifying the leap year calculation for a large input value.
 */
public class InternationalFixedChronologyGetLeapYearsBeforeTest {

    /**
     * Tests that {@code getLeapYearsBefore} correctly calculates the number of leap years
     * for a very large proleptic year value.
     *
     * The number of leap years before a given year 'y' follows the Gregorian rule:
     * a year is a leap year if it is divisible by 4, unless it is divisible by 100 but not by 400.
     * The formula for the count of leap years before 'y' is:
     * floor((y-1) / 4) - floor((y-1) / 100) + floor((y-1) / 400).
     */
    @Test
    public void shouldCalculateCorrectNumberOfLeapYearsForLargeYear() {
        // Arrange
        long prolepticYear = 365_242_134L;

        // The expected number of leap years is calculated based on the Gregorian leap year rule.
        // For year = 365,242,134:
        //   (365,242,133 / 4)   =  91,310,533
        // - (365,242,133 / 100)  =  -3,652,421
        // + (365,242,133 / 400)  =    +913,105
        // ------------------------------------
        // Total                  =  88,571,217
        long expectedLeapYears = 88_571_217L;

        // Act
        long actualLeapYears = InternationalFixedChronology.getLeapYearsBefore(prolepticYear);

        // Assert
        assertEquals("The number of leap years should be calculated correctly.", expectedLeapYears, actualLeapYears);
    }
}