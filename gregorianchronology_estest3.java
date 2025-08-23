package org.joda.time.chrono;

import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link GregorianChronology} class, focusing on leap year calculations.
 */
public class GregorianChronology_ESTestTest3 extends GregorianChronology_ESTest_scaffolding {

    /**
     * Tests that the proleptic Gregorian calendar correctly identifies year 0 as a leap year.
     * <p>
     * According to the Gregorian rules, a year is a leap year if it is divisible by 4,
     * unless it is divisible by 100 but not by 400. Year 0 is divisible by 400,
     * and therefore it is a leap year.
     */
    @Test
    public void isLeapYear_shouldReturnTrue_forYearZero() {
        // Arrange
        // The isLeapYear calculation is independent of the time zone, so we can use a fixed
        // zone like UTC to make the test more deterministic.
        GregorianChronology gregorianChronology = GregorianChronology.getInstance(DateTimeZone.UTC);
        int year = 0;

        // Act
        boolean isLeap = gregorianChronology.isLeapYear(year);

        // Assert
        assertTrue("Year 0 should be identified as a leap year.", isLeap);
    }
}