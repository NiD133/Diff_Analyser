package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the range of fields in {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that the valid value range for the ALIGNED_WEEK_OF_MONTH field is correct.
     * In the International Fixed Calendar, every month has exactly 28 days, which is 4 weeks.
     */
    @Test
    public void range_forAlignedWeekOfMonth_returnsCorrectRange() {
        // Arrange
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        // In the International Fixed calendar, each month has exactly 4 weeks (28 days).
        ValueRange expectedRange = ValueRange.of(1, 4);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.ALIGNED_WEEK_OF_MONTH);

        // Assert
        assertEquals("The range for ALIGNED_WEEK_OF_MONTH should be 1-4.", expectedRange, actualRange);
    }
}