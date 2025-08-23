package org.joda.time.base;

import org.joda.time.MonthDay;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for the AbstractPartial class, focusing on the toString() method
 * as inherited and implemented by subclasses like MonthDay.
 */
public class AbstractPartial_ESTestTest31 {

    @Test
    public void toString_onMonthDayInstance_returnsISO8601PartialFormat() {
        // Arrange: Create a MonthDay instance for a fixed date (February 14th).
        // Using a fixed date makes the test deterministic and independent of when it is run.
        // The original test used new MonthDay(), which would fail on any day other than Feb 14th.
        MonthDay monthDay = new MonthDay(2, 14); // Represents --02-14
        String expectedString = "--02-14";

        // Act: Call the toString() method.
        String actualString = monthDay.toString();

        // Assert: Verify that the output string matches the standard ISO8601 format for a partial date.
        assertEquals("The toString() method should return the date in ISO8601 partial format (--MM-dd).",
                expectedString, actualString);
    }
}