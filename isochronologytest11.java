package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.IllegalFieldValueException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for ISOChronology focusing on its behavior at the maximum supported year.
 */
public class ISOChronologyMaxYearTest {

    @Test
    public void testHandlingOfMaximumYear() {
        // Arrange: Set up constants and dates for the maximum year in UTC.
        final Chronology chrono = ISOChronology.getInstanceUTC();
        final int maxYear = chrono.year().getMaximumValue();

        final DateTime startOfMaxYear = new DateTime(maxYear, 1, 1, 0, 0, 0, 0, chrono);
        final DateTime endOfMaxYear = new DateTime(maxYear, 12, 31, 23, 59, 59, 999, chrono);

        // Assert: Verify basic properties of the max year dates.
        assertTrue("Millis for start of max year should be positive", startOfMaxYear.getMillis() > 0);
        assertTrue("End of max year should be after the start", endOfMaxYear.getMillis() > startOfMaxYear.getMillis());
        assertEquals("Year of the start date should be the max year", maxYear, startOfMaxYear.getYear());
        assertEquals("Year of the end date should be the max year", maxYear, endOfMaxYear.getYear());

        // Assert: Verify the duration of the maximum year.
        long fullYearDuration = (startOfMaxYear.year().isLeap() ? 366L : 365L) * DateTimeConstants.MILLIS_PER_DAY;
        // The duration is from the first millisecond of the year to the last, so it's one millisecond less than a full year.
        long expectedDurationBetweenStartAndEnd = fullYearDuration - 1;
        long actualDuration = endOfMaxYear.getMillis() - startOfMaxYear.getMillis();
        assertEquals("Duration of the max year is incorrect", expectedDurationBetweenStartAndEnd, actualDuration);

        // Assert: Verify that parsing ISO strings for the max year works correctly.
        assertEquals("Parsing start of max year from string", startOfMaxYear, new DateTime(maxYear + "-01-01T00:00:00.000Z", chrono));
        assertEquals("Parsing end of max year from string", endOfMaxYear, new DateTime(maxYear + "-12-31T23:59:59.999Z", chrono));

        // Assert: Adding one year to a date in the maximum year should fail.
        try {
            startOfMaxYear.plusYears(1);
            fail("Adding a year to the start of the max year should have thrown an exception.");
        } catch (IllegalFieldValueException e) {
            // Expected behavior
        }

        try {
            endOfMaxYear.plusYears(1);
            fail("Adding a year to the end of the max year should have thrown an exception.");
        } catch (IllegalFieldValueException e) {
            // Expected behavior
        }

        // Assert: Check an implementation detail for handling instants beyond the max year.
        // The year field should return maxYear + 1 for a millisecond value of Long.MAX_VALUE,
        // which demonstrates how the chronology handles overflow.
        assertEquals("Year for Long.MAX_VALUE should be maxYear + 1", maxYear + 1, chrono.year().get(Long.MAX_VALUE));
    }
}