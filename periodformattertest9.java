package org.joda.time.format;

import junit.framework.TestCase;
import org.joda.time.MutablePeriod;

/**
 * Contains tests for parsing strings into MutablePeriod objects using PeriodFormatter.
 * This class focuses on the parseMutablePeriod method.
 */
public class PeriodFormatterParseTest extends TestCase {

    /**
     * Tests that a valid ISO 8601 period string is parsed into the correct MutablePeriod.
     */
    public void testParseMutablePeriod_withValidISOString_parsesCorrectly() {
        // Arrange
        PeriodFormatter formatter = ISOPeriodFormat.standard();
        String isoPeriodString = "P1Y2M3W4DT5H6M7.008S";
        
        // The expected period is 1 year, 2 months, 3 weeks, 4 days, 
        // 5 hours, 6 minutes, 7 seconds, and 8 milliseconds.
        MutablePeriod expectedPeriod = new MutablePeriod(1, 2, 3, 4, 5, 6, 7, 8);

        // Act
        MutablePeriod actualPeriod = formatter.parseMutablePeriod(isoPeriodString);

        // Assert
        assertEquals(expectedPeriod, actualPeriod);
    }

    /**
     * Tests that parsing an invalid, non-period string throws an IllegalArgumentException.
     */
    public void testParseMutablePeriod_withInvalidString_throwsException() {
        // Arrange
        PeriodFormatter formatter = ISOPeriodFormat.standard();
        String invalidString = "This is not a period";

        // Act & Assert
        try {
            formatter.parseMutablePeriod(invalidString);
            fail("Expected an IllegalArgumentException for an invalid format");
        } catch (IllegalArgumentException ex) {
            // This is the expected behavior.
        }
    }
}