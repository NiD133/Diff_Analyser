package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the range() method in {@link Symmetry010Chronology}.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that the range for the DAY_OF_WEEK field is correct.
     * The day-of-week should always be a value from 1 (Monday) to 7 (Sunday).
     */
    @Test
    public void range_forDayOfWeek_shouldReturn1to7() {
        // Arrange: Define the expected value range for a day of the week.
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 7);

        // Act: Get the actual range for the DAY_OF_WEEK field from the chronology.
        ValueRange actualRange = chronology.range(ChronoField.DAY_OF_WEEK);

        // Assert: Verify that the actual range matches the expected range.
        assertEquals("The range for DAY_OF_WEEK should be 1-7", expectedRange, actualRange);
    }
}