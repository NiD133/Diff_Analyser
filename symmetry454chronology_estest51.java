package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the range of fields in {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyRangeTest {

    @Test
    public void range_forAlignedDayOfWeekInYear_returnsStandardDayOfWeekRange() {
        // Arrange
        // The Symmetry454Chronology is expected to use the standard ISO range for this field.
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 7);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR);

        // Assert
        assertEquals(
            "The range for ALIGNED_DAY_OF_WEEK_IN_YEAR should be the standard 1-7.",
            expectedRange,
            actualRange
        );
    }
}