package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.temporal.UnsupportedTemporalTypeException;

/**
 * Tests for the {@link DayOfMonth#range(TemporalField)} method.
 */
public class DayOfMonth_ESTestTest12 extends DayOfMonth_ESTest_scaffolding {

    /**
     * Tests that calling range() with a field that is not supported by DayOfMonth
     * throws an UnsupportedTemporalTypeException.
     */
    @Test(expected = UnsupportedTemporalTypeException.class)
    public void range_whenFieldIsUnsupported_throwsException() {
        // Arrange: Create a DayOfMonth instance. The specific day is not important for this test.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);

        // Act: Attempt to get the value range for an unsupported field like ERA.
        // The DayOfMonth class only supports the DAY_OF_MONTH field.
        dayOfMonth.range(ChronoField.ERA);

        // Assert: The test passes if an UnsupportedTemporalTypeException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}