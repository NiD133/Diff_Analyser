package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the range of supported fields in {@link Symmetry010Chronology}.
 */
public class Symmetry010ChronologyTest {

    /**
     * Verifies that the range for the YEAR_OF_ERA field is correctly defined.
     * <p>
     * According to the chronology's implementation, the year of era should range
     * from 1 to 1,000,000, inclusive.
     */
    @Test
    public void range_forYearOfEra_returnsCorrectRange() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 1_000_000L);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.YEAR_OF_ERA);

        // Assert
        assertEquals("The range for YEAR_OF_ERA is incorrect.", expectedRange, actualRange);
    }
}