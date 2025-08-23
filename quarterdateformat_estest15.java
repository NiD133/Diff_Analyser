package org.jfree.chart.axis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.TimeZone;

/**
 * Tests for the constructor of the {@link QuarterDateFormat} class.
 * This focuses on handling invalid arguments.
 */
public class QuarterDateFormatConstructorTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException
     * when a null TimeZone is provided, as this is a required dependency.
     */
    @Test
    public void constructor_withNullTimeZone_shouldThrowIllegalArgumentException() {
        // The constructor under test requires a non-null TimeZone.
        // We expect an IllegalArgumentException if null is passed.
        try {
            new QuarterDateFormat((TimeZone) null);
            fail("Expected an IllegalArgumentException to be thrown for a null TimeZone.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct, confirming the right check failed.
            assertEquals("Null 'zone' argument.", e.getMessage());
        }
    }
}