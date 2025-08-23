package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import org.junit.Test;

/**
 * Unit tests for {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    @Test
    public void range_forYearField_returnsCorrectRange() {
        // Arrange
        // The YEAR field represents the proleptic year.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        // The expected range for the proleptic year is defined as a constant in the chronology.
        // Minimum: -999,998, Maximum: 999,999.
        ValueRange expectedRange = ValueRange.of(-999_998, 999_999);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.YEAR);

        // Assert
        assertEquals("The range for the YEAR field should match the documented proleptic year range",
                expectedRange, actualRange);
    }
}