package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Symmetry010Chronology} class, focusing on its range calculation.
 */
public class Symmetry010ChronologyRangeTest {

    /**
     * Tests that the range() method returns the correct, standard range for a field
     * that it does not specifically override.
     *
     * The ALIGNED_DAY_OF_WEEK_IN_YEAR field is not handled explicitly by Symmetry010Chronology,
     * so the implementation should delegate to the field's default range, which is 1 to 7.
     */
    @Test
    public void range_forAlignedDayOfWeekInYear_shouldReturnDefaultFieldRange() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 7);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR);

        // Assert
        assertEquals("The range for ALIGNED_DAY_OF_WEEK_IN_YEAR should be the standard 1-7.", expectedRange, actualRange);
    }
}