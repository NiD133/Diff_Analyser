package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the range of fields in {@link Symmetry010Chronology}.
 */
public class Symmetry010ChronologyRangeTest {

    @Test
    public void range_forEraField_returnsCorrectRange() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        // The Symmetry010 calendar system uses the same eras as the ISO calendar:
        // BCE (0) and CE (1).
        ValueRange expectedRange = ValueRange.of(0, 1);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.ERA);

        // Assert
        assertEquals("The range for the ERA field should be 0 to 1.", expectedRange, actualRange);
    }
}