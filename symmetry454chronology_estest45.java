package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the range of fields in {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that the range for the DAY_OF_MONTH field is correct.
     * <p>
     * The Symmetry454 calendar has months of either 28 or 35 days.
     * Therefore, the valid range for the day-of-month is from 1 to 35.
     */
    @Test
    public void range_forDayOfMonth_returnsCorrectRange() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        // In the Symmetry454 system, normal months have 28 days (4 weeks) and
        // long months have 35 days (5 weeks).
        ValueRange expectedRange = ValueRange.of(1, 28, 35);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.DAY_OF_MONTH);

        // Assert
        assertEquals("The range for DAY_OF_MONTH should span from 1 to 35.",
                     expectedRange, actualRange);
    }
}