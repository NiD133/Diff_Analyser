package org.threeten.extra;

import org.junit.Test;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.UnsupportedTemporalTypeException;

/**
 * Tests for {@link DayOfYear}.
 */
public class DayOfYearTest {

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void adjustInto_shouldThrowException_whenTargetDoesNotSupportDayOfYearField() {
        // A YearMonth instance does not have a DAY_OF_YEAR field.
        // The adjustInto() method is equivalent to calling temporal.with(DAY_OF_YEAR, value).
        // This test verifies that attempting to adjust a YearMonth throws the expected exception.

        // Arrange
        DayOfYear dayOfYear150 = DayOfYear.of(150);
        YearMonth june2023 = YearMonth.of(2023, Month.JUNE);

        // Act
        dayOfYear150.adjustInto(june2023); // This call is expected to throw.
    }
}