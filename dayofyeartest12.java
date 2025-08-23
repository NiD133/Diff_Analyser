package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link DayOfYear#from(TemporalAccessor)} factory method.
 */
class DayOfYearTest {

    /**
     * Tests that DayOfYear.from() throws a DateTimeException when the provided
     * TemporalAccessor does not contain sufficient information to derive a day-of-year.
     */
    @Test
    void from_shouldThrowExceptionForTemporalWithoutDayOfYearInfo() {
        // Arrange: A LocalTime is a temporal object that lacks any date-based fields,
        // including DAY_OF_YEAR.
        TemporalAccessor temporalWithoutDayOfYear = LocalTime.NOON;

        // Act & Assert: Attempting to create a DayOfYear from this temporal should fail,
        // as documented by the from() method.
        assertThrows(DateTimeException.class, () -> DayOfYear.from(temporalWithoutDayOfYear));
    }
}