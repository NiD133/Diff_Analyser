package org.threeten.extra;

import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.Temporal;
import java.time.temporal.UnsupportedTemporalTypeException;
import org.junit.Test;

/**
 * A test suite for the {@link DayOfMonth} class, focusing on the
 * behavior of the {@link DayOfMonth#adjustInto(Temporal)} method.
 */
public class DayOfMonthTest {

    /**
     * Verifies that calling adjustInto() on a temporal type that does not support
     * the DAY_OF_MONTH field, such as {@link YearMonth}, throws an
     * {@link UnsupportedTemporalTypeException}.
     */
    @Test(expected = UnsupportedTemporalTypeException.class)
    public void adjustInto_whenTargetDoesNotSupportDayOfMonth_throwsException() {
        // Arrange: Create a DayOfMonth instance and a target temporal object (YearMonth)
        // that does not support the DAY_OF_MONTH field for adjustments.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);
        YearMonth yearMonth = YearMonth.of(2023, Month.APRIL);

        // Act: Attempt to adjust the YearMonth using the DayOfMonth.
        // This action is expected to throw an UnsupportedTemporalTypeException,
        // which is handled by the @Test(expected=...) annotation.
        dayOfMonth.adjustInto(yearMonth);
    }
}