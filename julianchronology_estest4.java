package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link JulianChronology#resolveDate(Map, ResolverStyle)}.
 */
// The original test class name is kept for context.
public class JulianChronology_ESTestTest4 extends JulianChronology_ESTest_scaffolding {

    @Test
    public void resolveDate_fromEpochDay_returnsCorrectDate() {
        // Arrange
        JulianChronology julianChronology = JulianChronology.INSTANCE;
        long epochDayValue = 3L; // Corresponds to 1970-01-04 in the ISO calendar.
        Map<TemporalField, Long> fieldValues = Map.of(ChronoField.EPOCH_DAY, epochDayValue);

        // The expected date is 1969-12-22 in the Julian calendar.
        // We create it directly from the epoch day for a robust and clear comparison.
        JulianDate expectedDate = julianChronology.dateEpochDay(epochDayValue);

        // Act
        // Resolve the map of fields to a JulianDate.
        JulianDate resolvedDate = julianChronology.resolveDate(fieldValues, ResolverStyle.LENIENT);

        // Assert
        assertEquals(expectedDate, resolvedDate);
    }
}