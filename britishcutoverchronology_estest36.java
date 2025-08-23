package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.temporal.ChronoField;
import java.time.temporal.ValueRange;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the range of fields in {@link BritishCutoverChronology}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that the range for the PROLEPTIC_MONTH field is correctly calculated.
     * <p>
     * The BritishCutoverChronology supports a proleptic year range from -999,998 to 999,999.
     * This test verifies that the proleptic month range correctly reflects this year span.
     */
    @Test
    public void testRangeForProlepticMonth() {
        // Arrange
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        // The proleptic month is defined as (prolepticYear * 12 + month - 1).
        // The supported year range is -999,998 to 999,999.
        // Minimum value: year = -999,998, month = 1 => (-999,998 * 12) = -11,999,976
        // Maximum value: year = 999,999, month = 12 => (999,999 * 12) + 11 = 11,999,999
        long minProlepticMonth = -999_998L * 12L;
        long maxProlepticMonth = 999_999L * 12L + 11L;
        ValueRange expectedRange = ValueRange.of(minProlepticMonth, maxProlepticMonth);

        // Act
        ValueRange actualRange = chronology.range(ChronoField.PROLEPTIC_MONTH);

        // Assert
        assertEquals("The range for PROLEPTIC_MONTH is incorrect.", expectedRange, actualRange);
    }
}