package org.threeten.extra;

import org.junit.Test;

import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.UnsupportedTemporalTypeException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for {@link DayOfMonth}.
 */
public class DayOfMonthTest {

    @Test
    public void adjustInto_forUnsupportedTemporal_throwsException() {
        // Arrange: A YearMonth does not support the DAY_OF_MONTH field.
        final DayOfMonth dayOfMonth = DayOfMonth.of(15);
        final YearMonth yearMonth = YearMonth.of(2023, Month.APRIL);

        // Act & Assert: Verify that calling adjustInto with an unsupported temporal type
        // throws UnsupportedTemporalTypeException. The call delegates to
        // yearMonth.with(DAY_OF_MONTH, ...), which is the source of the exception.
        UnsupportedTemporalTypeException thrown = assertThrows(
                UnsupportedTemporalTypeException.class,
                () -> dayOfMonth.adjustInto(yearMonth)
        );

        // Further assert on the exception message for more precise verification.
        assertEquals("Unsupported field: DayOfMonth", thrown.getMessage());
    }
}