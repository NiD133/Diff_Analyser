package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;

/**
 * Contains unit tests for the DayOfYear class, focusing on exception handling.
 */
public class DayOfYearExceptionTest {

    /**
     * Verifies that the range() method throws an UnsupportedTemporalTypeException
     * when queried with a field that is not supported by DayOfYear.
     *
     * The DayOfYear class represents a day within a year and does not contain
     * time-based information. Therefore, requesting the range for a time-based
     * field like SECOND_OF_MINUTE should fail.
     */
    @Test(expected = UnsupportedTemporalTypeException.class)
    public void range_whenFieldIsUnsupported_throwsException() {
        // Arrange: Create a sample DayOfYear and select a field that it does not support.
        // Using a fixed day (e.g., 150) makes the test deterministic, unlike DayOfYear.now().
        DayOfYear dayOfYear = DayOfYear.of(150);
        ChronoField unsupportedField = ChronoField.SECOND_OF_MINUTE;

        // Act: Attempt to get the value range for the unsupported field.
        // This action is expected to throw the exception specified in the @Test annotation.
        dayOfYear.range(unsupportedField);

        // Assert: The test automatically passes if the expected exception is thrown.
        // No explicit assert statement is needed here.
    }
}