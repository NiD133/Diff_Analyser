package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.temporal.UnsupportedTemporalTypeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test class contains the refactored test case.
 * While the original class name "DayOfMonth_ESTestTest35" is kept to match the input,
 * in a real-world scenario, it would be renamed to something more meaningful, like "DayOfMonthTest".
 */
public class DayOfMonth_ESTestTest35 extends DayOfMonth_ESTest_scaffolding {

    /**
     * Tests that calling getLong() with a field not supported by DayOfMonth
     * throws an UnsupportedTemporalTypeException.
     */
    @Test
    public void getLong_whenFieldIsUnsupported_throwsException() {
        // Arrange: Create a DayOfMonth instance and an unsupported field.
        // DayOfMonth is a simple value object, so any specific day will suffice.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);
        TemporalField unsupportedField = ChronoField.INSTANT_SECONDS;

        // Act & Assert: Attempt to get the value for the unsupported field
        // and verify that the correct exception is thrown with the expected message.
        try {
            dayOfMonth.getLong(unsupportedField);
            fail("Expected an UnsupportedTemporalTypeException to be thrown.");
        } catch (UnsupportedTemporalTypeException e) {
            // Verify the exception message is informative and correct.
            assertEquals("Unsupported field: InstantSeconds", e.getMessage());
        }
    }
}