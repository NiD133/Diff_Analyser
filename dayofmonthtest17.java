package org.threeten.extra;

import static org.junit.Assert.*;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.temporal.UnsupportedTemporalTypeException;
import org.junit.Test;

/**
 * This test suite focuses on verifying the behavior of the {@link DayOfMonth#get(TemporalField)} method.
 */
public class DayOfMonth_ESTestTest17 {

    /**
     * Tests that calling get() with a TemporalField that is not supported by DayOfMonth
     * throws an UnsupportedTemporalTypeException.
     *
     * The DayOfMonth class is designed to represent only the day part of a date and does not
     * contain information about time, such as hours or minutes.
     */
    @Test(timeout = 4000, expected = UnsupportedTemporalTypeException.class)
    public void testGet_whenFieldIsUnsupported_throwsException() {
        // Arrange: Define a day of the month and an unsupported temporal field.
        // DayOfMonth does not support time-based fields like CLOCK_HOUR_OF_DAY.
        final DayOfMonth dayOfMonth = DayOfMonth.of(21);
        final TemporalField unsupportedField = ChronoField.CLOCK_HOUR_OF_DAY;

        // Act: Attempt to retrieve the value for the unsupported field.
        // This action is expected to throw an UnsupportedTemporalTypeException.
        dayOfMonth.get(unsupportedField);

        // Assert: The test passes if the expected exception is thrown, which is
        // handled by the `expected` parameter in the @Test annotation.
    }
}