package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link InternationalFixedChronology}.
 */
public class InternationalFixedChronologyTest {

    @Test
    public void dateNow_whenUsingFixedClockInNonLeapYear_returnsDateWithCorrectYearLength() {
        // Arrange: Set up a fixed clock pointing to a specific moment in a non-leap year (2021).
        // The InternationalFixedChronology uses the same leap year rules as the Gregorian calendar.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        Instant instantInNonLeapYear = Instant.parse("2021-07-22T10:15:30.00Z");
        Clock fixedClock = Clock.fixed(instantInNonLeapYear, ZoneOffset.UTC);

        // Act: Obtain the "current" date from the chronology using the fixed clock.
        InternationalFixedDate date = chronology.dateNow(fixedClock);

        // Assert: Verify that the length of the year is 365 days, as expected for a non-leap year.
        int expectedDaysInYear = 365;
        assertEquals(
            "The length of a non-leap year should be 365 days.",
            expectedDaysInYear,
            date.lengthOfYear()
        );
    }
}