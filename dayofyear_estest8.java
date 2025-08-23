package org.threeten.extra;

import org.junit.jupiter.api.Test;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.time.temporal.UnsupportedTemporalTypeException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for {@link DayOfYear}.
 */
public class DayOfYearTest {

    /**
     * Tests that calling range() with a field not supported by DayOfYear
     * throws an UnsupportedTemporalTypeException.
     */
    @Test
    public void range_whenFieldIsUnsupported_throwsException() {
        // Arrange: A DayOfYear instance does not support time-based fields.
        DayOfYear dayOfYear = DayOfYear.of(150); // Use a fixed, arbitrary day
        TemporalField unsupportedField = ChronoField.SECOND_OF_MINUTE;

        // Act & Assert: Verify that the expected exception is thrown.
        UnsupportedTemporalTypeException thrown = assertThrows(
            UnsupportedTemporalTypeException.class,
            () -> dayOfYear.range(unsupportedField)
        );

        // Assert: Check if the exception message is correct.
        assertEquals("Unsupported field: SecondOfMinute", thrown.getMessage());
    }
}