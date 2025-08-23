package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Symmetry454Chronology} class.
 * This class focuses on testing the range of supported date-time fields.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that the valid range for the DAY_OF_WEEK field is 1 to 7,
     * consistent with the ISO standard week.
     */
    @Test
    public void rangeForDayOfWeek_returnsCorrectRange() {
        // Arrange: Get the singleton instance of the chronology.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        
        // Act: Get the supported range for the DAY_OF_WEEK field.
        ValueRange actualRange = chronology.range(ChronoField.DAY_OF_WEEK);
        
        // Assert: Verify that the range is from 1 (Monday) to 7 (Sunday).
        ValueRange expectedRange = ValueRange.of(1, 7);
        assertEquals("The range for DAY_OF_WEEK should be 1-7", expectedRange, actualRange);
    }
}