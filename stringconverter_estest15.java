package org.joda.time.convert;

import org.joda.time.Chronology;
import org.joda.time.chrono.CopticChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class contains tests for the {@link StringConverter}.
 * The original test was auto-generated; this version has been refactored for clarity.
 */
public class StringConverter_ESTestTest15 {

    /**
     * Tests that getInstantMillis() throws an IllegalArgumentException when provided with a string
     * that does not conform to the expected date-time format.
     */
    @Test
    public void getInstantMillis_shouldThrowIllegalArgumentException_forInvalidStringFormat() {
        // Arrange
        final StringConverter converter = StringConverter.INSTANCE;
        final String invalidDateTimeString = " cannot be compared to ";
        final Chronology chronology = CopticChronology.getInstanceUTC();
        final String expectedErrorMessage = "Invalid format: \"" + invalidDateTimeString + "\"";

        // Act & Assert
        try {
            converter.getInstantMillis(invalidDateTimeString, chronology);
            fail("Expected an IllegalArgumentException to be thrown for an invalid format string.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception has the expected message
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}