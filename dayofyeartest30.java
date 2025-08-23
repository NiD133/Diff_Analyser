package org.threeten.extra;

import org.junit.Test;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.Temporal;
import java.time.temporal.UnsupportedTemporalTypeException;

/**
 * This test suite evaluates the behavior of the {@link DayOfYear#adjustInto(Temporal)} method.
 */
public class DayOfYear_ESTestTest30 {

    /**
     * Tests that calling {@code adjustInto} on a temporal type that does not support
     * the DAY_OF_YEAR field throws an {@code UnsupportedTemporalTypeException}.
     * <p>
     * The {@code adjustInto} method is expected to fail when the target temporal object,
     * such as a {@link YearMonth}, cannot be adjusted by a day-of-year value. A
     * {@code YearMonth} lacks the day-of-year field, so any attempt to adjust it
     * should result in an exception. [8, 12] This test confirms that the correct exception
     * is thrown in this scenario.
     */
    @Test(expected = UnsupportedTemporalTypeException.class)
    public void adjustInto_whenTargetUnsupported_shouldThrowException() {
        // Arrange: Create a DayOfYear instance and a YearMonth, which does not support
        // the DAY_OF_YEAR field.
        DayOfYear dayOfYear = DayOfYear.of(150); // An arbitrary day of the year (May 30th in a non-leap year)
        YearMonth yearMonth = YearMonth.of(2023, Month.JUNE); // An arbitrary YearMonth instance

        // Act: Attempt to adjust the YearMonth using the DayOfYear.
        // This action is expected to throw an UnsupportedTemporalTypeException.
        dayOfYear.adjustInto(yearMonth);

        // Assert: The test is successful if the expected exception is thrown.
        // This is handled by the @Test(expected=...) annotation in JUnit 4. [3, 4, 5]
    }
}