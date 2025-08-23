package org.joda.time.format;

import junit.framework.TestCase;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;

/**
 * Tests for the {@link PeriodFormatter#parseInto(org.joda.time.ReadWritablePeriod, String, int)} method.
 * This class focuses on parsing behavior with the standard ISO format.
 */
public class PeriodFormatterParseIntoTest extends TestCase {

    // The formatter under test is immutable and can be a constant.
    // This removes the need for complex setUp/tearDown state management.
    private static final PeriodFormatter ISO_STANDARD_FORMATTER = ISOPeriodFormat.standard();

    /**
     * Tests that a valid ISO 8601 period string is parsed correctly into a MutablePeriod.
     */
    public void testParseInto_validString_parsesCorrectly() {
        // Arrange
        String periodString = "P1Y2M3W4DT5H6M7.008S";
        // Use an immutable Period for the expected value for clarity and safety.
        Period expectedPeriod = new Period(1, 2, 3, 4, 5, 6, 7, 8);
        MutablePeriod actualPeriod = new MutablePeriod();

        // Act
        int parsedLength = ISO_STANDARD_FORMATTER.parseInto(actualPeriod, periodString, 0);

        // Assert
        // Check that the returned position is the length of the parsed string.
        assertEquals("Parsed string length should match input string length", periodString.length(), parsedLength);
        // Check that the period was populated with the correct values.
        assertEquals("Parsed period values should match expected values", expectedPeriod, actualPeriod.toPeriod());
    }

    /**
     * Tests that calling parseInto with a null period object throws an IllegalArgumentException.
     */
    public void testParseInto_nullPeriod_throwsIllegalArgumentException() {
        // Arrange
        String periodString = "P1Y2M3W4DT5H6M7.008S";

        // Act & Assert
        try {
            ISO_STANDARD_FORMATTER.parseInto(null, periodString, 0);
            fail("Expected an IllegalArgumentException to be thrown for a null period object.");
        } catch (IllegalArgumentException ex) {
            // This is the expected outcome.
        }
    }

    /**
     * Tests that parsing an invalid string fails and returns the correct failure position.
     */
    public void testParseInto_invalidString_returnsFailurePosition() {
        // Arrange
        String invalidString = "ABC";
        MutablePeriod period = new MutablePeriod(); // The period object to parse into.

        // Act
        int parseResult = ISO_STANDARD_FORMATTER.parseInto(period, invalidString, 0);

        // Assert
        // For a failed parse, parseInto returns the bitwise complement of the failure position.
        // Failure at the start of the string (position 0) is represented by ~0.
        int expectedFailurePosition = 0;
        assertEquals("Return value should indicate failure at position 0", ~expectedFailurePosition, parseResult);
    }
}