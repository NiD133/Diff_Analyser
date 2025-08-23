package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the range() method in {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyRangeTest {

    @Test
    public void range_forAlignedWeekOfYear_returnsCorrectRange() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        // The Symmetry454Chronology does not override the range for ALIGNED_WEEK_OF_YEAR.
        // Therefore, it should fall back to the default range defined in ChronoField, which is 1-53.
        ValueRange expectedRange = ValueRange.of(1, 53);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.ALIGNED_WEEK_OF_YEAR);

        // Assert
        assertEquals("The range for ALIGNED_WEEK_OF_YEAR should be the default from ChronoField.", expectedRange, actualRange);
    }
}