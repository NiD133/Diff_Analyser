package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link Symmetry010Chronology}.
 */
public class Symmetry010ChronologyTest {

    /**
     * Tests that the range() method returns the correct value range for the YEAR field.
     * The Symmetry010 chronology supports a year range from -1,000,000 to 1,000,000.
     */
    @Test
    public void testRangeForYearField() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        // The expected range is defined by the chronology's constants:
        // static final ValueRange YEAR_RANGE = ValueRange.of(-MAX_YEAR, MAX_YEAR);
        // where MAX_YEAR = 1_000_000L;
        ValueRange expectedRange = ValueRange.of(-1_000_000L, 1_000_000L);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.YEAR);

        // Assert
        assertEquals("The range for the YEAR field should match the chronology's specification.",
                expectedRange, actualRange);
    }
}