package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.DateTimeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains improved tests for the {@link BritishCutoverChronology}.
 * The original test was automatically generated and has been refactored for clarity.
 */
public class BritishCutoverChronologyTest { // Renamed for clarity

    /**
     * Tests that calling eraOf() with an integer value that does not
     * correspond to a valid era (BC=0, AD=1) throws a DateTimeException.
     */
    @Test
    public void eraOf_withInvalidValue_shouldThrowDateTimeException() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        int invalidEraValue = 4;
        String expectedMessage = "Invalid era: " + invalidEraValue;

        // Act & Assert
        try {
            chronology.eraOf(invalidEraValue);
            fail("A DateTimeException was expected but not thrown.");
        } catch (DateTimeException ex) {
            assertEquals("The exception message should detail the invalid era value.",
                    expectedMessage, ex.getMessage());
        }
    }
}