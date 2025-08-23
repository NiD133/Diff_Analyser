package org.joda.time.chrono;

import static org.junit.Assert.assertEquals;

import org.joda.time.DateMidnight;
import org.joda.time.YearMonthDay;
import org.junit.Test;

/**
 * Tests the consistency of maximum field values in ISOChronology.
 */
public class ISOChronologyMaximumValueTest {

    /**
     * This test verifies that the maximum values for year, month, and day fields
     * are consistent between a full datetime object (DateMidnight) and a partial
     * date object (YearMonthDay).
     *
     * It iterates over a date range (1570-1590) that specifically includes the
     * standard Gregorian calendar cutover year of 1582 to ensure that the
     * logic holds true across this historical calendar change.
     */
    @Test
    public void maximumValues_areConsistentBetweenDateMidnightAndYearMonthDay_acrossGregorianCutover() {
        final int startYear = 1570;
        final int endYear = 1590;

        // Start with a DateMidnight instance and iterate day by day
        DateMidnight date = new DateMidnight(startYear, 1, 1);

        while (date.getYear() < endYear) {
            date = date.plusDays(1);
            YearMonthDay ymd = date.toYearMonthDay();

            // For each day, assert that the maximum possible values for each field
            // are identical between the two different time representations.
            // A descriptive failure message helps pinpoint issues faster.
            String failureMessage = "Mismatch on date: " + date;

            assertEquals(failureMessage,
                    date.year().getMaximumValue(),
                    ymd.year().getMaximumValue());

            assertEquals(failureMessage,
                    date.monthOfYear().getMaximumValue(),
                    ymd.monthOfYear().getMaximumValue());

            assertEquals(failureMessage,
                    date.dayOfMonth().getMaximumValue(),
                    ymd.dayOfMonth().getMaximumValue());
        }
    }
}