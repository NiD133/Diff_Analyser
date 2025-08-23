package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the range of fields in the Symmetry454Chronology.
 */
public class Symmetry454ChronologyRangeTest {

    @Test
    public void range_forSecondOfMinute_returnsStandardTimeRange() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        // The expected range for SECOND_OF_MINUTE is standard across most chronologies.
        ValueRange expectedRange = ValueRange.of(0, 59);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.SECOND_OF_MINUTE);

        // Assert
        assertEquals("The range for SECOND_OF_MINUTE should be the standard 0-59.", expectedRange, actualRange);
    }
}