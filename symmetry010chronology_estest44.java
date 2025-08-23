package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the range of fields in {@link Symmetry010Chronology}.
 *
 * Note: The original test class name "Symmetry010Chronology_ESTestTest44"
 * suggests it was auto-generated. A more conventional name would be
 * "Symmetry010ChronologyTest".
 */
public class Symmetry010Chronology_ESTestTest44 {

    /**
     * Verifies that the valid range for the DAY_OF_YEAR field is correctly defined.
     * <p>
     * The Symmetry010 calendar has 364 days in a standard year and 371 in a leap year.
     * The range should therefore be from 1 to 364-371.
     */
    @Test
    public void range_forDayOfYear_returnsCorrectFixedRange() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        // A standard year has 364 days; a leap year has 371.
        ValueRange expectedRange = ValueRange.of(1, 364, 371);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.DAY_OF_YEAR);

        // Assert
        assertEquals("The range for DAY_OF_YEAR should be [1, 364/371]", expectedRange, actualRange);
    }
}