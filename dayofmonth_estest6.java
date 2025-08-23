package org.threeten.extra;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the factory methods of {@link DayOfMonth}.
 */
public class DayOfMonthTest {

    /**
     * Tests that DayOfMonth.now(Clock) correctly retrieves the day of the month
     * from the provided clock.
     *
     * This improved test uses the overload of `now()` that accepts a `Clock`,
     * which is the standard, testable way to handle time-dependent logic.
     * It makes the dependency on time explicit and the test fully deterministic.
     */
    @Test
    public void now_withFixedClock_returnsCorrespondingDayOfMonth() {
        // Arrange: Create a clock fixed to a specific date (February 14th, 2021).
        // The exact year and month are not important, only the day of the month.
        final int expectedDay = 14;
        Instant fixedInstant = LocalDate.of(2021, Month.FEBRUARY, expectedDay)
                                        .atStartOfDay(ZoneOffset.UTC)
                                        .toInstant();
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        // Act: Obtain a DayOfMonth instance using the fixed clock.
        DayOfMonth dayOfMonth = DayOfMonth.now(fixedClock);

        // Assert: The obtained DayOfMonth should have the value from the fixed clock's date.
        assertEquals(expectedDay, dayOfMonth.getValue());
    }
}