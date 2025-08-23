package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the range of fields in {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyRangeTest {

    /**
     * Verifies that the range for the ALIGNED_WEEK_OF_YEAR field is correct.
     * <p>
     * The BritishCutoverChronology has a specific range for this field to accommodate
     * the shortened cutover year of 1752. This test ensures that the defined
     * constant range is returned.
     */
    @Test
    public void range_forAlignedWeekOfYear_returnsCorrectRange() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        // The expected range is defined in the source class as ValueRange.of(1, 51, 53).
        // This means the smallest possible maximum is 51 and the largest is 53.
        ValueRange expectedRange = ValueRange.of(1, 51, 53);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.ALIGNED_WEEK_OF_YEAR);

        // Assert
        assertEquals("The range for ALIGNED_WEEK_OF_YEAR should be consistent", expectedRange, actualRange);
    }
}