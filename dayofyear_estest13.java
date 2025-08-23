package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class is derived from an auto-generated one.
 * The class name and inheritance are kept to match the original structure.
 */
public class DayOfYear_ESTestTest13 extends DayOfYear_ESTest_scaffolding {

    /**
     * Verifies that calling get() with an unsupported TemporalField throws an exception.
     * The DayOfYear class is designed to support only the DAY_OF_YEAR field.
     * Accessing any other field, such as ERA, should fail.
     */
    @Test
    public void get_withUnsupportedField_throwsException() {
        // Arrange: Create a DayOfYear instance and select an unsupported field.
        // The specific day (e.g., 150) is arbitrary and does not affect the outcome.
        DayOfYear dayOfYear = DayOfYear.of(150);
        ChronoField unsupportedField = ChronoField.ERA;

        // Act & Assert: Attempt to get the value for the unsupported field and
        // verify that the correct exception is thrown with a specific message.
        try {
            dayOfYear.get(unsupportedField);
            fail("Expected UnsupportedTemporalTypeException was not thrown.");
        } catch (UnsupportedTemporalTypeException e) {
            // This is the expected outcome.
            // Check the exception message to ensure the failure is for the correct reason.
            assertEquals("Unsupported field: Era", e.getMessage());
        }
    }
}