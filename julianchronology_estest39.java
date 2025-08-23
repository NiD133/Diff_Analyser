package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link JulianChronology} class.
 */
public class JulianChronologyTest {

    /**
     * Tests that the range() method returns the correct value range for the PROLEPTIC_MONTH field.
     */
    @Test
    public void range_forProlepticMonth_returnsCorrectValueRange() {
        // Arrange
        JulianChronology julianChronology = JulianChronology.INSTANCE;

        // The expected range is derived from the JulianChronology's supported year range,
        // which is -999,998 to 999,999.
        // Minimum proleptic month: -999,998 * 12 = -11,999,976
        // Maximum proleptic month: (999,999 * 12) + 11 (for December) = 11,999,999
        ValueRange expectedRange = ValueRange.of(-11_999_976L, 11_999_999L);

        // Act
        ValueRange actualRange = julianChronology.range(ChronoField.PROLEPTIC_MONTH);

        // Assert
        assertEquals("The range for PROLEPTIC_MONTH should match the documented limits.",
                expectedRange, actualRange);
    }
}