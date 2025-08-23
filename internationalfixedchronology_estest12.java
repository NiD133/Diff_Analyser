package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.Assert.assertEquals;

/**
 * This test class verifies the behavior of the dateNow() method in InternationalFixedChronology.
 * The original test was auto-generated and has been rewritten for clarity and maintainability.
 */
public class InternationalFixedChronology_ESTestTest12 {

    /**
     * Tests that dateNow() correctly identifies the year length for a date
     * that falls within a non-leap year.
     */
    @Test
    public void dateNowShouldReturnDateWithCorrectYearLengthForNonLeapYear() {
        // Arrange: Set up a fixed clock pointing to a moment in 1970.
        // The International Fixed calendar shares the same leap year rule as the Gregorian calendar,
        // and 1970 is not a leap year.
        final InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        final Instant instantIn1970 = Instant.ofEpochMilli(16); // 1970-01-01T00:00:00.016Z
        final Clock fixedClock = Clock.fixed(instantIn1970, ZoneOffset.UTC);

        // Act: Get the current date from the chronology using the fixed clock.
        InternationalFixedDate currentDate = chronology.dateNow(fixedClock);

        // Assert: Verify that the length of the year for the obtained date is 365 days.
        int expectedYearLength = 365;
        assertEquals(
            "The year 1970 should have 365 days in the International Fixed calendar.",
            expectedYearLength,
            currentDate.lengthOfYear()
        );
    }
}