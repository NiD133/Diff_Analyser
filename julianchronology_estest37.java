package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link JulianChronology} class.
 */
public class JulianChronologyTest {

    /**
     * Verifies that the range() method returns the correct and expected
     * value range for the YEAR_OF_ERA field in the Julian chronology.
     */
    @Test
    public void shouldReturnCorrectRangeForYearOfEra() {
        // Arrange: Define the expected range for YEAR_OF_ERA.
        // According to the JulianChronology's definition, this is 1 to 999,999.
        ValueRange expectedRange = ValueRange.of(1, 999_999);
        JulianChronology julianChronology = JulianChronology.INSTANCE;

        // Act: Get the actual range from the chronology for the specified field.
        ValueRange actualRange = julianChronology.range(ChronoField.YEAR_OF_ERA);

        // Assert: Verify that the actual range matches the expected range.
        assertEquals("The range for YEAR_OF_ERA should be from 1 to 999,999.", expectedRange, actualRange);
    }
}