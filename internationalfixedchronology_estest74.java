package org.threeten.extra.chrono;

import static org.junit.Assert.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.Test;

/**
 * Tests for {@link InternationalFixedChronology#dateNow(Clock)}.
 */
// The test class name and inheritance are kept from the original EvoSuite-generated code.
public class InternationalFixedChronology_ESTestTest74 extends InternationalFixedChronology_ESTest_scaffolding {

    /**
     * Tests that dateNow() correctly determines the length of a non-leap year.
     */
    @Test
    public void dateNow_withClockInNonLeapYear_returnsDateWithCorrectLengthOfYear() {
        // Arrange: Set up a fixed clock for a specific moment in a non-leap year (2023).
        // This makes the test deterministic and independent of the actual system time.
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;
        Instant instantInNonLeapYear = Instant.parse("2023-06-15T10:00:00Z");
        Clock fixedClock = Clock.fixed(instantInNonLeapYear, ZoneId.of("UTC"));

        // Act: Obtain the "current" date from the chronology using the fixed clock.
        InternationalFixedDate currentDate = chronology.dateNow(fixedClock);

        // Assert: Verify that the length of the year for the resulting date is 365,
        // as 2023 is not a leap year.
        assertEquals("Length of year for a non-leap year should be 365", 365, currentDate.lengthOfYear());
    }
}