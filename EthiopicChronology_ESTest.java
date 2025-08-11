package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable tests for EthiopicChronology covering:
 * - era constant
 * - leap-day detection
 * - min/max year constants
 * - epoch approximation
 * - first-day-of-year calculation for a simple case
 * - time-zone behavior (withUTC/withZone)
 * - validation of minDaysInFirstWeek
 * - overflow and range exceptions for extreme instants
 */
public class EthiopicChronologyTest {

    @Test
    public void eraConstant_matchesCE() {
        assertEquals("Ethiopean Era (EE) should match the CE constant",
                DateTimeConstants.CE, EthiopicChronology.EE);
    }

    @Test
    public void isLeapDay_returnsTrueOnlyOn13thMonthDay6() {
        EthiopicChronology c = EthiopicChronology.getInstanceUTC();

        // Find a leap year by checking which year accepts month=13, day=6.
        int leapYear = findLeapYear(c, /*startYear=*/1);

        // 13th month day 6 exists only in leap years -> must be a leap day.
        DateTime leapDay = new DateTime(leapYear, 13, 6, 0, 0, c);
        assertTrue("Expected leap day on Ethiopic " + leapYear + "-13-06",
                c.isLeapDay(leapDay.getMillis()));

        // A normal day (13th month day 5) is never a leap day.
        DateTime normalDay = new DateTime(leapYear, 13, 5, 0, 0, c);
        assertFalse("Expected non-leap day on Ethiopic " + leapYear + "-13-05",
                c.isLeapDay(normalDay.getMillis()));
    }

    @Test
    public void isLeapDay_throwsIllegalArgument_whenBelowSupportedRangeInUTC() {
        EthiopicChronology c = EthiopicChronology.getInstanceUTC();
        try {
            c.isLeapDay(Long.MIN_VALUE);
            fail("Expected IllegalArgumentException for instant below supported minimum");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    @Test
    public void isLeapDay_throwsArithmetic_whenOffsetCausesOverflow() {
        DateTimeZone nonZeroOffset = DateTimeZone.forOffsetHours(1);
        EthiopicChronology c = EthiopicChronology.getInstance(nonZeroOffset);
        try {
            c.isLeapDay(Long.MIN_VALUE); // adding the offset overflows
            fail("Expected ArithmeticException due to time zone offset overflow");
        } catch (ArithmeticException expected) {
            // ok
        }
    }

    @Test
    public void calculateFirstDayOfYearMillis_year1_matchesKnownValue() {
        EthiopicChronology c = EthiopicChronology.getInstanceUTC();
        long firstDayMillis = c.calculateFirstDayOfYearMillis(1);
        // Known value from the chronology implementation/tests
        assertEquals(-61894108800000L, firstDayMillis);
    }

    @Test
    public void minAndMaxYear_matchConstants() {
        EthiopicChronology c = EthiopicChronology.getInstance();
        assertEquals(-292269337, c.getMinYear());
        assertEquals(292272984, c.getMaxYear());
    }

    @Test
    public void approxMillisAtEpochDividedByTwo_matchesConstant() {
        EthiopicChronology c = EthiopicChronology.getInstance();
        assertEquals(30962844000000L, c.getApproxMillisAtEpochDividedByTwo());
    }

    @Test
    public void withUTC_returnsUTCChronology_andNotSameInstanceForNonUTC() {
        EthiopicChronology zoned = EthiopicChronology.getInstance(DateTimeZone.forOffsetHours(3));
        Chronology utc = zoned.withUTC();
        assertNotSame(zoned, utc);
        assertEquals(DateTimeZone.UTC, utc.getZone());
    }

    @Test
    public void withZone_null_returnsSameInstanceForDefaultZone() {
        EthiopicChronology c = EthiopicChronology.getInstance((DateTimeZone) null);
        Chronology same = c.withZone(null);
        assertSame("withZone(null) should return same instance when already in default zone", c, same);
    }

    @Test
    public void getInstance_rejectsInvalidMinDaysInFirstWeek_zero() {
        try {
            EthiopicChronology.getInstance(DateTimeZone.UTC, 0);
            fail("Expected IllegalArgumentException for minDaysInFirstWeek=0");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    @Test
    public void getInstance_rejectsInvalidMinDaysInFirstWeek_aboveSeven() {
        try {
            EthiopicChronology.getInstance(DateTimeZone.UTC, 8);
            fail("Expected IllegalArgumentException for minDaysInFirstWeek=8");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    // This test uses the package-private constructor; keep the test in the same package.
    @Test
    public void constructor_rejectsInvalidMinDaysInFirstWeek() {
        try {
            new EthiopicChronology(null, null, -1);
            fail("Expected IllegalArgumentException for minDaysInFirstWeek=-1");
        } catch (IllegalArgumentException expected) {
            // ok
        }
    }

    // Helper to locate a leap year without relying on the exact leap rule.
    private static int findLeapYear(EthiopicChronology c, int startYearInclusive) {
        for (int y = startYearInclusive; y < startYearInclusive + 8; y++) {
            try {
                // If this doesn't throw, the year has a 6th day in the 13th month => leap year.
                new DateTime(y, 13, 6, 0, 0, c);
                return y;
            } catch (IllegalArgumentException ignore) {
                // not a leap year, try next
            }
        }
        throw new AssertionError("Could not find a leap year within 8 years starting at " + startYearInclusive);
        }
}