package org.threeten.extra;

import org.junit.Test;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link DayOfMonth}.
 */
public class DayOfMonthTest {

    /**
     * Tests that DayOfMonth.now(clock) correctly captures the day of the month
     * from a fixed clock, and that getValue() returns it.
     */
    @Test
    public void now_withFixedClock_returnsCorrectDayOfMonth() {
        // Arrange: Create a fixed clock for a predictable date (e.g., July 15th, 2023).
        Instant fixedInstant = Instant.parse("2023-07-15T10:30:00Z");
        Clock clock = Clock.fixed(fixedInstant, ZoneOffset.UTC);
        int expectedDay = 15;

        // Act: Create a DayOfMonth instance using the fixed clock.
        DayOfMonth dayOfMonth = DayOfMonth.now(clock);

        // Assert: The value of the created DayOfMonth should match the day from the clock.
        assertEquals(expectedDay, dayOfMonth.getValue());
    }
}