package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the valid value ranges in {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that the range for the DAY_OF_YEAR field is correct.
     * A standard Symmetry454 year has 364 days, while a leap year has 371.
     */
    @Test
    public void rangeForDayOfYear_shouldReturnCorrectRange() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 364, 371);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.DAY_OF_YEAR);

        // Assert
        assertEquals("The range for DAY_OF_YEAR should be 1-364/371", expectedRange, actualRange);
    }
}