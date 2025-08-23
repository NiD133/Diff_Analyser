package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the Symmetry454Chronology class.
 */
public class Symmetry454ChronologyTest {

    /**
     * Tests that the range() method returns the correct, supported range for the YEAR field.
     * According to the Symmetry454Chronology implementation, the supported year range
     * is from -1,000,000 to 1,000,000.
     */
    @Test
    public void range_whenQueriedForYearField_returnsSupportedYearRange() {
        // Arrange
        Symmetry454Chronology chronology = Symmetry454Chronology.INSTANCE;
        ValueRange expectedYearRange = ValueRange.of(-1_000_000L, 1_000_000L);

        // Act
        ValueRange actualYearRange = chronology.range(ChronoField.YEAR);

        // Assert
        assertEquals("The range for the YEAR field should match the defined supported range.",
                expectedYearRange, actualYearRange);
    }
}