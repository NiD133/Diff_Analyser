package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Symmetry010Chronology class, focusing on field value ranges.
 */
public class Symmetry010ChronologyRangeTest {

    @Test
    public void range_forAlignedWeekOfYear_returnsCorrectRange() {
        // Arrange: The Symmetry010 calendar has 52 weeks in a normal year and 53 in a leap year.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 52, 53);

        // Act: Get the valid range for the ALIGNED_WEEK_OF_YEAR field.
        ValueRange actualRange = chronology.range(ChronoField.ALIGNED_WEEK_OF_YEAR);

        // Assert: The range should reflect that a year can have 52 or 53 weeks.
        assertEquals(
                "The range for ALIGNED_WEEK_OF_YEAR should be 1-52 for normal years and 1-53 for leap years.",
                expectedRange,
                actualRange);
    }
}