package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Symmetry010Chronology} class, focusing on the range of its fields.
 */
// Note: The original test class name and inheritance from the EvoSuite scaffolding
// have been preserved as per the request.
public class Symmetry010Chronology_ESTestTest42 extends Symmetry010Chronology_ESTest_scaffolding {

    /**
     * Tests that the range() method returns the correct value range for the MONTH_OF_YEAR field.
     * The Symmetry010 calendar system consistently has 12 months in a year.
     */
    @Test
    public void range_forMonthOfYear_shouldReturn1to12() {
        // Arrange
        Symmetry010Chronology chronology = Symmetry010Chronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 12);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.MONTH_OF_YEAR);

        // Assert
        assertEquals("The range for MONTH_OF_YEAR should be from 1 to 12.", expectedRange, actualRange);
    }
}