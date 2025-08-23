package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.chrono.JapaneseDate;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DayOfMonth#adjustInto(Temporal)}.
 */
class DayOfMonthAdjustIntoTest {

    private static final DayOfMonth DAY_OF_MONTH_12 = DayOfMonth.of(12);

    @Test
    void adjustInto_withNonIsoChronology_throwsDateTimeException() {
        // The adjustInto() method is specified to work only with the ISO calendar system.
        // Attempting to adjust a date from a different chronology, like JapaneseDate,
        // should fail with a DateTimeException.
        assertThrows(DateTimeException.class, () ->
                DAY_OF_MONTH_12.adjustInto(JapaneseDate.now()));
    }
}