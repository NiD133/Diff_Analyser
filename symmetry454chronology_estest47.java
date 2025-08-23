package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the range of fields in {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyRangeTest {

    /**
     * Verifies the valid range for the ALIGNED_WEEK_OF_MONTH field.
     * <p>
     * The Symmetry454 calendar has months of either 4 weeks (28 days) or 5 weeks (35 days).
     * This test ensures that the chronology correctly reports this specific range.
     */
    @Test
    public void rangeForAlignedWeekOfMonth_isCorrect() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        // The expected range for weeks in a month is 1 to 5, with the shortest
        // "long" month having 4 weeks.
        ValueRange expectedRange = ValueRange.of(1, 4, 5);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.ALIGNED_WEEK_OF_MONTH);

        // Assert
        assertEquals(
                "The range for ALIGNED_WEEK_OF_MONTH should be 1-4/5 weeks.",
                expectedRange,
                actualRange);
    }
}