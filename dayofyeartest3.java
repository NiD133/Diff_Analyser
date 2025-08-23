package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

/**
 * Test class for the now() methods of DayOfYear.
 */
public class DayOfYear_NowTest {

    private static final ZoneId ZONE_PARIS = ZoneId.of("Europe/Paris");

    /**
     * The original test for DayOfYear.now() was flaky because it used the system clock.
     * It could fail if the day changed between the call to `LocalDate.now()` and `DayOfYear.now()`.
     *
     * This improved test uses a fixed clock to provide a consistent and predictable "current"
     * instant. This makes the test deterministic, reliable, and easier to understand.
     */
    @Test
    public void now_withFixedClock_returnsCorrectDayOfYear() {
        // Arrange: Set up a fixed clock for a specific date.
        // February 20th is the 51st day of the year 2007.
        LocalDate date = LocalDate.of(2007, 2, 20);
        Instant instant = date.atStartOfDay(ZONE_PARIS).toInstant();
        Clock fixedClock = Clock.fixed(instant, ZONE_PARIS);
        int expectedDayOfYear = 51;

        // Act: Call the method under test with the fixed clock.
        DayOfYear actualDayOfYear = DayOfYear.now(fixedClock);

        // Assert: Verify the result is correct for the date specified in the clock.
        assertEquals(expectedDayOfYear, actualDayOfYear.getValue());
    }
}