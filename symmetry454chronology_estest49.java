package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import org.junit.Test;

/**
 * Tests for the range of fields in {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyRangeTest {

    @Test
    public void range_forAlignedDayOfWeekInMonth_returnsCorrectStandardRange() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        // The ALIGNED_DAY_OF_WEEK_IN_MONTH field should always have a range of 1 to 7.
        ValueRange expectedRange = ValueRange.of(1, 7);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH);

        // Assert
        assertEquals("The range for ALIGNED_DAY_OF_WEEK_IN_MONTH should be 1-7.", expectedRange, actualRange);
    }
}