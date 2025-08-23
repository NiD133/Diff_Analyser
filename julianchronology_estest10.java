package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link JulianChronology}.
 *
 * <p>This improved test focuses on clarity, determinism, and correctness,
 * replacing a non-deterministic, auto-generated test case.
 */
public class JulianChronologyTest {

    /**
     * Tests that {@code JulianChronology.dateNow(Clock)} correctly determines the date
     * based on the instant and time zone provided by the Clock.
     *
     * <p>The original test was non-deterministic as it relied on the system's current time.
     * By using a {@link Clock#fixed(Instant, ZoneId)}, we provide a stable instant in time,
     * making the test repeatable and its behavior predictable.
     */
    @Test
    public void dateNow_withFixedClock_returnsCorrectDateForThatClock() {
        // --- Arrange ---
        // 1. Define a fixed point in time (Instant).
        // This instant is just after midnight UTC on 2014-02-15.
        Instant fixedInstant = Instant.parse("2014-02-15T01:00:00Z");

        // 2. Define a time zone with a large negative offset. The original test used ZoneOffset.MIN.
        // In this zone (-18:00), the local time for the given instant is still the previous day.
        // Calculation: 2014-02-15 01:00:00Z is 2014-02-14 07:00:00-18:00.
        ZoneId farWestZone = ZoneOffset.MIN;

        // 3. Create a fixed clock that will always return the above instant and zone.
        Clock fixedClock = Clock.fixed(fixedInstant, farWestZone);

        // 4. Determine the expected Julian date.
        // The local date is 2014-02-14 (ISO), which corresponds to 2014-02-01 in the Julian calendar.
        JulianChronology chronology = JulianChronology.INSTANCE;
        JulianDate expectedDate = JulianDate.of(2014, 2, 1);

        // --- Act ---
        // Call the method under test using the fixed clock. The dateNow(Clock) variant
        // is designed for testability, allowing us to inject a controlled time source.
        JulianDate actualDate = chronology.dateNow(fixedClock);

        // --- Assert ---
        // Verify that the result matches the expected date. This is a much more
        // precise and meaningful assertion than the original test's check on the era.
        assertEquals(expectedDate, actualDate);
    }
}