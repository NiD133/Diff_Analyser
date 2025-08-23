package org.threeten.extra;

import org.junit.Test;
import java.time.DateTimeException;
import java.time.Month;
import java.time.temporal.TemporalAccessor;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * This test suite focuses on the from(TemporalAccessor) factory method of the DayOfYear class.
 */
public class DayOfYearFromTest {

    /**
     * Tests that creating a DayOfYear from a TemporalAccessor that does not support
     * the DAY_OF_YEAR field throws a DateTimeException.
     *
     * The Month enum is a TemporalAccessor but lacks the necessary day-of-year information,
     * so the conversion is expected to fail.
     */
    @Test
    public void from_unsupportedTemporalAccessor_throwsDateTimeException() {
        // Arrange: A TemporalAccessor that does not contain day-of-year information.
        final TemporalAccessor temporalWithoutDayOfYear = Month.NOVEMBER;
        final String expectedErrorMessage = "Unable to obtain DayOfYear from TemporalAccessor";

        // Act & Assert: Verify that a DateTimeException is thrown with a descriptive message.
        DateTimeException thrown = assertThrows(
                DateTimeException.class,
                () -> DayOfYear.from(temporalWithoutDayOfYear)
        );

        // Assert that the exception message clearly indicates the cause of the failure.
        assertTrue(
                "The exception message should explain why the conversion failed.",
                thrown.getMessage().contains(expectedErrorMessage)
        );
    }
}