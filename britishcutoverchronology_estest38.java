package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the range of fields in {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyRangeTest {

    @Test
    public void range_forAlignedWeekOfMonth_returnsCorrectFixedRange() {
        // Arrange
        // The BritishCutoverChronology defines a specific, fixed range for ALIGNED_WEEK_OF_MONTH.
        // This test verifies that the range() method returns this expected value.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 3, 5);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.ALIGNED_WEEK_OF_MONTH);

        // Assert
        assertEquals("The range for ALIGNED_WEEK_OF_MONTH should be the fixed value defined by the chronology.",
                     expectedRange, actualRange);
    }
}