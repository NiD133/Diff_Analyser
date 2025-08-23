package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link DayOfMonth#get(TemporalField)} method.
 */
public class DayOfMonth_ESTestTest17 extends DayOfMonth_ESTest_scaffolding {

    /**
     * Tests that calling get() with a field not supported by DayOfMonth,
     * such as a time-based field, throws an UnsupportedTemporalTypeException.
     */
    @Test
    public void get_whenFieldIsUnsupported_throwsUnsupportedTemporalTypeException() {
        // Arrange: A DayOfMonth instance represents only a day number (1-31) and does not
        // support time-based fields. We use a fixed day for deterministic testing.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);
        ChronoField unsupportedField = ChronoField.CLOCK_HOUR_OF_DAY;

        // Act & Assert: Attempt to get the value for an unsupported field and
        // verify that the correct exception is thrown with the expected message.
        try {
            dayOfMonth.get(unsupportedField);
            fail("Expected an UnsupportedTemporalTypeException to be thrown, but it wasn't.");
        } catch (UnsupportedTemporalTypeException e) {
            // This is the expected outcome. Verify the exception message for correctness.
            assertEquals("Unsupported field: ClockHourOfDay", e.getMessage());
        }
    }
}