package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.chrono.JapaneseDate;
import java.time.temporal.TemporalAccessor;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DayOfMonth}.
 */
public class DayOfMonthTest {

    /**
     * Tests that {@code DayOfMonth.from()} can extract the day from a
     * {@code TemporalAccessor} that uses a non-ISO calendar system.
     */
    @Test
    public void from_extractsDayFromNonIsoChronology() {
        // Arrange: Create a date in a non-ISO calendar (Japanese)
        // The underlying implementation converts this to an ISO LocalDate first.
        LocalDate isoDate = LocalDate.of(2012, 6, 23);
        TemporalAccessor nonIsoDate = JapaneseDate.from(isoDate);
        int expectedDay = 23;

        // Act: Create a DayOfMonth from the non-ISO date
        DayOfMonth actualDayOfMonth = DayOfMonth.from(nonIsoDate);

        // Assert: The extracted day-of-month should match the original
        assertEquals(expectedDay, actualDayOfMonth.getValue());
    }
}