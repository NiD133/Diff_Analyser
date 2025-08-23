package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the factory method {@link DayOfMonth#from(TemporalAccessor)}.
 */
class DayOfMonthFromTest {

    @Test
    @DisplayName("DayOfMonth.from() should throw an exception for a temporal that lacks day-of-month information")
    void from_throwsExceptionForTemporalWithoutDayOfMonth() {
        // A LocalTime object is a TemporalAccessor that does not contain any
        // information about the day, month, or year.
        TemporalAccessor temporalWithoutDayOfMonth = LocalTime.NOON;

        // Attempting to create a DayOfMonth from it should fail because the
        // required DAY_OF_MONTH field is not available.
        assertThrows(DateTimeException.class, () -> DayOfMonth.from(temporalWithoutDayOfMonth));
    }
}