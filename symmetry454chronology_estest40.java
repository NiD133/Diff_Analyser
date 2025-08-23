package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the range() method of {@link Symmetry454Chronology}.
 */
public class Symmetry454ChronologyRangeTest {

    /**
     * Tests that the range for the YEAR_OF_ERA field is correct.
     * <p>
     * The Symmetry454Chronology supports a year range from -1,000,000 to 1,000,000,
     * which should be returned by the range() method for this field.
     */
    @Test
    public void range_forYearOfEra_returnsCorrectSupportedRange() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        // This expected range is defined by the MAX_YEAR constant in the source class.
        ValueRange expectedRange = ValueRange.of(-1_000_000L, 1_000_000L);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.YEAR_OF_ERA);

        // Assert
        assertEquals("The range for YEAR_OF_ERA should match the chronology's supported year range.", expectedRange, actualRange);
    }
}