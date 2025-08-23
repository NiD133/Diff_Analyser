package org.joda.time.chrono;

import junit.framework.TestCase;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Tests the isLeap() behavior of the GregorianChronology for both leap and non-leap years.
 */
public class GregorianChronologyLeapYearTest extends TestCase {

    // Using UTC to ensure tests are independent of the default system time zone.
    private static final Chronology GREGORIAN_UTC = GregorianChronology.getInstance(DateTimeZone.UTC);

    /**
     * Tests that for a date on a leap day (Feb 29) in a leap year,
     * the various isLeap() methods correctly return true.
     */
    public void testIsLeap_whenDateIsInLeapYear_returnsTrue() {
        // Arrange: 2012 is a known leap year (divisible by 4 but not by 100).
        final int LEAP_YEAR = 2012;
        DateTime leapDay = new DateTime(LEAP_YEAR, 2, 29, 10, 20, GREGORIAN_UTC);

        // Assert
        assertTrue("Year property should report it is a leap year", leapDay.year().isLeap());
        assertTrue("Month property should report it is a leap month", leapDay.monthOfYear().isLeap());
        assertTrue("Day of month property should report it is a leap day", leapDay.dayOfMonth().isLeap());
        assertTrue("Day of year property should report it is a leap day", leapDay.dayOfYear().isLeap());
    }

    /**
     * Tests that for a date in a non-leap year, the various isLeap() methods
     * correctly return false.
     */
    public void testIsLeap_whenDateIsInNonLeapYear_returnsFalse() {
        // Arrange: 2011 is a known non-leap year (not divisible by 4).
        final int NON_LEAP_YEAR = 2011;
        DateTime nonLeapDay = new DateTime(NON_LEAP_YEAR, 2, 28, 10, 20, GREGORIAN_UTC);

        // Assert
        assertFalse("Year property should report it is not a leap year", nonLeapDay.year().isLeap());
        assertFalse("Month property should report it is not a leap month", nonLeapDay.monthOfYear().isLeap());
        assertFalse("Day of month property should report it is not a leap day", nonLeapDay.dayOfMonth().isLeap());
        assertFalse("Day of year property should report it is not a leap day", nonLeapDay.dayOfYear().isLeap());
    }
}