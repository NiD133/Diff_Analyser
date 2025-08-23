package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link BritishCutoverChronology#resolveDate(Map, ResolverStyle)}.
 */
public class BritishCutoverChronologyTest {

    /**
     * Tests that resolveDate correctly creates a date from a map containing only the EPOCH_DAY field.
     */
    @Test
    public void resolveDate_fromEpochDay_returnsCorrectDate() {
        // --- Arrange ---
        // Use the singleton instance as recommended by the class Javadoc.
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;

        // The input is a map containing the EPOCH_DAY field.
        // An epoch day of 763 corresponds to the ISO date 1972-02-04.
        long epochDayValue = 763L;
        Map<TemporalField, Long> fieldValues = Map.of(ChronoField.EPOCH_DAY, epochDayValue);

        // The expected result is a BritishCutoverDate representing 1972-02-04.
        // This date is after the 1752 cutover, so it behaves like a standard Gregorian date.
        BritishCutoverDate expectedDate = BritishCutoverDate.of(1972, 2, 4);

        // --- Act ---
        // Resolve the map of fields to a date using a strict resolver style.
        BritishCutoverDate resolvedDate = chronology.resolveDate(fieldValues, ResolverStyle.STRICT);

        // --- Assert ---
        // The original test only checked for a non-null result.
        // A specific check for correctness is a much stronger and more useful assertion.
        assertEquals(expectedDate, resolvedDate);
    }
}