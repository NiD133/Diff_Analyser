package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Symmetry010Chronology#range(ChronoField)} method.
 */
public class Symmetry010ChronologyRangeTest {

    /**
     * Verifies that the range() method returns the correct and expected
     * ValueRange for the EPOCH_DAY field.
     */
    @Test
    public void testRangeForEpochDayReturnsCorrectRange() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        // The expected range is defined as a public constant in the chronology itself.
        ValueRange expectedRange = Symmetry010Chronology.EPOCH_DAY_RANGE;

        // Act
        ValueRange actualRange = chronology.range(ChronoField.EPOCH_DAY);

        // Assert
        // The returned range should be exactly equal to the pre-defined constant.
        assertEquals("The range for EPOCH_DAY should match the defined constant", expectedRange, actualRange);
    }
}