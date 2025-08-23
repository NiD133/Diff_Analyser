package org.threeten.extra.chrono;

import org.junit.Test;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link InternationalFixedChronology} class, focusing on time-dependent operations.
 */
public class InternationalFixedChronologyTest {

    /**
     * Tests that dateNow() correctly determines the length of a non-leap year.
     * The International Fixed Chronology shares its leap year rule with the Gregorian calendar,
     * so 1970 is a non-leap year.
     */
    @Test
    public void dateNow_givenClockInNonLeapYear_returnsDateWithCorrectYearLength() {
        // Arrange: Set up a fixed clock pointing to a moment in 1970, a non-leap year.
        // Using a fixed Clock is the standard, reliable way to test time-sensitive code.
        Instant instantIn1970 = Instant.EPOCH; // Represents 1970-01-01T00:00:00Z
        Clock fixedClock = Clock.fixed(instantIn1970, ZoneId.of("UTC"));
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;

        // Act: Get the current date from the chronology using our fixed clock.
        // We test the dateNow(Clock) overload directly for better test isolation.
        InternationalFixedDate currentDate = chronology.dateNow(fixedClock);

        // Assert: Verify that the length of the year for the obtained date is 365 days.
        assertEquals("A non-leap year should have 365 days", 365, currentDate.lengthOfYear());
    }
}