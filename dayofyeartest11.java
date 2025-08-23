package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.temporal.TemporalAccessor;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfYear#from(TemporalAccessor)} factory method.
 */
class DayOfYearFromTest {

    @Test
    void from_shouldExtractDayOfYear_whenTemporalAccessorHasNonIsoChronology() {
        // Arrange: Use a fixed date to ensure the test is deterministic.
        // June 30th, 2012 is the 182nd day of that leap year.
        LocalDate isoDate = LocalDate.of(2012, 6, 30);
        int expectedDayOfYear = 182;
        // A sanity check to ensure our test data is correct.
        assertEquals(expectedDayOfYear, isoDate.getDayOfYear());

        // Create a TemporalAccessor with a non-ISO chronology (Japanese).
        TemporalAccessor nonIsoTemporal = JapaneseDate.from(isoDate);

        // Act: Create a DayOfYear from the non-ISO temporal accessor.
        DayOfYear actualDayOfYear = DayOfYear.from(nonIsoTemporal);

        // Assert: The correct day of the year should be extracted, regardless of chronology.
        assertEquals(expectedDayOfYear, actualDayOfYear.getValue());
    }
}