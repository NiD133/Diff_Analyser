package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Symmetry010Chronology} class, focusing on its value ranges.
 *
 * Note: The original test class name "Symmetry010Chronology_ESTestTest45" suggests it was
 * automatically generated. This version uses a more conventional name and structure.
 */
public class Symmetry010Chronology_ESTestTest45 {

    /**
     * Tests that the range for the DAY_OF_MONTH field is correctly defined.
     * <p>
     * According to the Symmetry010 calendar rules:
     * <ul>
     *   <li>Standard months have 30 or 31 days.</li>
     *   <li>In a leap year, December has 37 days.</li>
     * </ul>
     * This test verifies that the {@code range()} method returns a {@code ValueRange}
     * that accurately reflects these calendar rules.
     */
    @Test
    public void range_forDayOfMonth_returnsCorrectRange() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        // The expected range for DAY_OF_MONTH reflects that the shortest months have 30 days
        // and the longest month (December in a leap year) has 37 days.
        ValueRange expectedRange = ValueRange.of(1, 30, 37);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.DAY_OF_MONTH);

        // Assert
        assertEquals("The range for DAY_OF_MONTH should be 1-30/37", expectedRange, actualRange);
    }
}