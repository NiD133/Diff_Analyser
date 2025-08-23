package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the {@link InternationalFixedChronology} class.
 * This specific test was improved for understandability from an auto-generated version.
 */
public class InternationalFixedChronology_ESTestTest8 extends InternationalFixedChronology_ESTest_scaffolding {

    /**
     * Tests the calculation of the number of leap years before a given negative (proleptic) year.
     * The International Fixed Chronology shares its leap year rule with the Gregorian calendar.
     */
    @Test(timeout = 4000)
    public void getLeapYearsBefore_withNegativeYear_calculatesCorrectly() {
        // Arrange
        // The number of leap years before a given 'prolepticYear' is calculated using the
        // Gregorian leap year formula, applied to the year *before* the input year.
        // Formula: count = (y / 4) - (y / 100) + (y / 400), where y = prolepticYear - 1.
        //
        // For an input year of -1610:
        // Let y = -1610 - 1 = -1611
        // count = (-1611 / 4) - (-1611 / 100) + (-1611 / 400)
        //       = (-402)      - (-16)         + (-4)
        //       = -402 + 16 - 4
        //       = -390
        long prolepticYear = -1610L;
        long expectedLeapYearCount = -390L;

        // Act
        long actualLeapYearCount = InternationalFixedChronology.getLeapYearsBefore(prolepticYear);

        // Assert
        assertEquals(expectedLeapYearCount, actualLeapYearCount);
    }
}