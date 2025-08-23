package org.threeten.extra;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

public class DayOfMonth_ESTestTest46 {

    /**
     * Tests that DayOfMonth.now(clock) correctly retrieves the day from a fixed clock.
     *
     * <p>This test uses a fixed clock to ensure it is deterministic and independent of the
     * actual system time. This makes the test reliable, repeatable, and easier to understand.
     */
    @Test
    public void testNowWithFixedClock() {
        // Arrange: Create a fixed clock set to a specific date (February 14th, 2024).
        Instant fixedInstant = Instant.parse("2024-02-14T10:15:30.00Z");
        Clock fixedClock = Clock.fixed(fixedInstant, ZoneOffset.UTC);

        // Act: Obtain the DayOfMonth using the fixed clock.
        DayOfMonth dayOfMonth = DayOfMonth.now(fixedClock);

        // Assert: Verify that the DayOfMonth object represents the 14th day.
        assertEquals(14, dayOfMonth.getValue());
    }
}