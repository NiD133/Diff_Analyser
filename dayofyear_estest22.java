package org.threeten.extra;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link DayOfYear}.
 */
public class DayOfYearTest {

    /**
     * Tests that isSupported() returns false for fields that are not
     * related to the day-of-year, such as time-based fields.
     */
    @Test
    public void isSupported_shouldReturnFalse_forUnsupportedTimeBasedField() {
        // Arrange: Create a DayOfYear instance and select an unsupported field.
        // Using a fixed value like 150 makes the test deterministic, unlike DayOfYear.now().
        DayOfYear dayOfYear = DayOfYear.of(150);
        TemporalField unsupportedField = ChronoField.CLOCK_HOUR_OF_AMPM;

        // Act: Check if the field is supported.
        boolean isSupported = dayOfYear.isSupported(unsupportedField);

        // Assert: The time-based field should not be supported.
        assertFalse(
            "DayOfYear should not support time-based fields like CLOCK_HOUR_OF_AMPM.",
            isSupported
        );
    }
}