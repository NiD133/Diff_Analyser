package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyTest {

    @Test
    public void range_forEpochDay_returnsCorrectRange() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        // The expected range is defined as a package-private constant in the chronology.
        ValueRange expectedRange = Symmetry454Chronology.EPOCH_DAY_RANGE;

        // Act
        ValueRange actualRange = chronology.range(ChronoField.EPOCH_DAY);

        // Assert
        assertEquals("The range for EPOCH_DAY should match the predefined constant.", expectedRange, actualRange);
    }
}