package org.threeten.extra.chrono;

import org.junit.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;

import static org.junit.Assert.assertEquals;

// The original test class structure is maintained, but unused imports have been removed for clarity.
public class InternationalFixedChronology_ESTestTest13 extends InternationalFixedChronology_ESTest_scaffolding {

    /**
     * Tests that dateNow() correctly determines the date properties at the end of a non-leap year.
     * <p>
     * The International Fixed Calendar has 13 months. In a non-leap year, 12 months have 28 days,
     * and the final month has 29 days, totaling 365 days. This test verifies this structure
     * by checking the date corresponding to the last day of 1969.
     */
    @Test
    public void dateNow_atEndOfNonLeapYear_returnsCorrectDateProperties() {
        // Arrange: Set up a fixed clock for the last day of 1969 (a non-leap year).
        // The original test used System.setCurrentTimeMillis(-633L), which corresponds to
        // 1969-12-31. Using a fixed Clock with an explicit Instant is clearer and more robust.
        Instant lastDayOf1969 = Instant.parse("1969-12-31T12:00:00Z");
        Clock fixedClock = Clock.fixed(lastDayOf1969, ZoneOffset.UTC);
        InternationalFixedChronology ifc = InternationalFixedChronology.INSTANCE;

        // Act: Obtain the "current" date from the chronology using the fixed clock.
        // The original test called dateNow(ZoneId), which relies on the system clock.
        // Calling dateNow(Clock) makes the time dependency explicit and the test more reliable.
        InternationalFixedDate date = ifc.dateNow(fixedClock);

        // Assert: Verify the properties of the resulting date.
        // A non-leap year like 1969 should have 365 days.
        assertEquals("A non-leap year should have 365 days", 365, date.lengthOfYear());

        // The 365th day falls in the 13th month, which has 29 days in a non-leap year.
        assertEquals("The month should be the 13th month", 13, date.get(ChronoField.MONTH_OF_YEAR));
        assertEquals("The day should be the 29th of the month", 29, date.get(ChronoField.DAY_OF_MONTH));
        assertEquals("The last month of a non-leap year should have 29 days", 29, date.lengthOfMonth());
    }
}