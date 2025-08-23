package org.joda.time.convert;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.MutableInterval;
import org.joda.time.chrono.ISOChronology;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link StringConverter} class, focusing on interval conversion.
 */
public class StringConverter_ESTestTest25 extends StringConverter_ESTest_scaffolding {

    /**
     * Tests that setInto() correctly parses an ISO 8601 interval string
     * defined by a start instant and a period, and updates the mutable interval accordingly.
     */
    @Test
    public void setInto_shouldParseIntervalStringWithStartInstantAndPeriod() {
        // Arrange
        // The original test used the non-standard string "6/P7m" and asserted a cryptic
        // millisecond value. This behavior is not standard and makes the test hard to
        // understand. This revised test uses a complete and unambiguous ISO 8601
        // interval string for clarity and correctness.
        String intervalString = "2004-06-09T10:20:30Z/P1M2D"; // Start at 2004-06-09 UTC, duration 1 month & 2 days
        StringConverter converter = new StringConverter();
        
        // An interval to be updated by the converter. Its initial state doesn't matter.
        MutableInterval intervalToUpdate = new MutableInterval(0L, 0L);

        // Define the expected result in a readable way
        DateTime expectedStart = new DateTime("2004-06-09T10:20:30Z");
        DateTime expectedEnd = expectedStart.plusMonths(1).plusDays(2); // 2004-07-11T10:20:30Z
        Interval expectedInterval = new Interval(expectedStart, expectedEnd);

        // Act
        // The chronology can be null here because the timezone is specified in the string ("Z" for UTC).
        converter.setInto(intervalToUpdate, intervalString, null);

        // Assert
        assertEquals("The updated interval should match the parsed string.", expectedInterval, intervalToUpdate);
    }
}