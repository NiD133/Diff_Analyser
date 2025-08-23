package org.joda.time.format;

import static org.junit.Assert.assertEquals;

import org.joda.time.Period;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the parsing functionality of PeriodFormatter.
 * This focuses on the standard ISO 8601 format.
 */
public class PeriodFormatterParseTest {

    private PeriodFormatter formatter;

    @Before
    public void setUp() {
        // Initialize the formatter for the standard ISO8601 period format.
        // This format is locale-independent, so no complex setup is needed.
        formatter = ISOPeriodFormat.standard();
    }

    @Test
    public void parsePeriod_shouldCorrectlyParseValidISOString() {
        // Arrange
        // The ISO string "P1Y2M3W4DT5H6M7.008S" represents a period of:
        // 1 Year, 2 Months, 3 Weeks, 4 Days,
        // 5 Hours, 6 Minutes, 7 Seconds, and 8 Milliseconds.
        String validIsoPeriodString = "P1Y2M3W4DT5H6M7.008S";
        Period expectedPeriod = new Period(1, 2, 3, 4, 5, 6, 7, 8);

        // Act
        Period actualPeriod = formatter.parsePeriod(validIsoPeriodString);

        // Assert
        assertEquals(expectedPeriod, actualPeriod);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parsePeriod_shouldThrowExceptionForInvalidString() {
        // Arrange
        String invalidString = "ABC";

        // Act: This action is expected to throw an IllegalArgumentException.
        formatter.parsePeriod(invalidString);

        // Assert: The test fails if no exception (or the wrong one) is thrown,
        // which is handled declaratively by the @Test annotation.
    }
}