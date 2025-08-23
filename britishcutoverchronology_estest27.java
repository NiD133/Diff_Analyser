package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the range of fields in {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that the range for the YEAR_OF_ERA field is correct.
     * The BritishCutoverChronology supports years from 1 to 999,999 for both the BC and AD eras.
     */
    @Test
    public void range_forYearOfEra_returnsCorrectRange() {
        // Arrange: Define the expected value range for the YEAR_OF_ERA field.
        // This range is documented in the BritishCutoverChronology class.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        ValueRange expectedRange = ValueRange.of(1, 999_999);

        // Act: Get the actual range from the chronology.
        ValueRange actualRange = chronology.range(ChronoField.YEAR_OF_ERA);

        // Assert: Verify that the actual range matches the expected range.
        assertEquals("The range for YEAR_OF_ERA should be from 1 to 999,999.", expectedRange, actualRange);
    }
}