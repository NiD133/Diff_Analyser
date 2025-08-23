package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests the range() method of {@link Symmetry010Chronology}.
 */
public class Symmetry010ChronologyRangeTest {

    /**
     * Verifies the valid value range for the ALIGNED_WEEK_OF_MONTH field.
     *
     * <p>The Symmetry010 calendar system has months that are composed of a varying
     * number of weeks. According to its implementation, the smallest maximum number
     * of weeks in a month is 4, and the largest maximum is 5. This test confirms
     * that the chronology correctly reports this range.
     */
    @Test
    public void range_forAlignedWeekOfMonth_returnsCorrectRange() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        // The expected range is 1 to 4 for shorter months and 1 to 5 for longer ones.
        ValueRange expectedRange = ValueRange.of(1, 4, 5);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.ALIGNED_WEEK_OF_MONTH);

        // Assert
        assertEquals(
                "The range for ALIGNED_WEEK_OF_MONTH should be 1-4 for some months and 1-5 for others.",
                expectedRange,
                actualRange
        );
    }
}