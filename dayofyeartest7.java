package org.threeten.extra;

import org.junit.Test;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import static org.junit.Assert.assertSame;

/**
 * This test suite focuses on the {@link DayOfYear#adjustInto(Temporal)} method.
 */
public class DayOfYear_ESTestTest7 {

    /**
     * Tests that {@link DayOfYear#adjustInto(Temporal)} returns the same object instance
     * if the provided {@link Temporal} already has the same day-of-year.
     *
     * The {@code adjustInto} method is expected to be a no-op in this scenario,
     * avoiding the creation of a new object for performance reasons.
     */
    @Test
    public void adjustInto_whenDayOfYearIsUnchanged_shouldReturnSameInstance() {
        // Arrange: Create a DayOfYear and a LocalDate that already matches that day-of-year.
        final int dayValue = 45;
        final int year = 2024; // A leap year, ensuring day 45 is valid.
        DayOfYear dayOfYear = DayOfYear.of(dayValue);
        LocalDate initialDate = LocalDate.ofYearDay(year, dayValue);

        // Act: Adjust the LocalDate using the DayOfYear instance.
        Temporal adjustedDate = dayOfYear.adjustInto(initialDate);

        // Assert: The returned Temporal should be the exact same instance as the initial LocalDate,
        // as no adjustment was necessary.
        assertSame("adjustInto should return the same instance if the day-of-year is already correct.",
                initialDate, adjustedDate);
    }
}