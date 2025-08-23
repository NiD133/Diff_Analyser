package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import org.junit.Test;

/**
 * Tests for the range() method in {@link JulianChronology}.
 */
public class JulianChronologyTest {

    @Test
    public void range_forClockHourOfAmPm_shouldReturnValidRange() {
        // Arrange
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 12);

        // Act
        ValueRange actualRange = julianChronology.range(ChronoField.CLOCK_HOUR_OF_AMPM);

        // Assert
        assertEquals("The range for CLOCK_HOUR_OF_AMPM should be 1-12.", expectedRange, actualRange);
    }
}