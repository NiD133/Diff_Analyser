package org.joda.time.chrono;

import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for IslamicChronology.
 * 
 * Key goals:
 * - Use meaningful test names and comments instead of opaque numbers.
 * - Avoid EvoSuite runner/dependencies and flaky/default-zone behavior.
 * - Prefer testing via public factory methods rather than internal constructors.
 * - Keep assertions tied to documented behavior and constants.
 */
public class IslamicChronologyTest {

    private static final IslamicChronology UTC = IslamicChronology.getInstanceUTC();
    private static final DateTimeZone OFFSET_01_00 = DateTimeZone.forOffsetHours(1);

    // ---------------------------------------------------------------------
    // Constants and singletons
    // ---------------------------------------------------------------------

    @Test
    public void AH_constantMatchesCE() {
        assertEquals("AH should match CE", DateTimeConstants.CE, IslamicChronology.AH);
    }

    @Test
    public void getInstanceUTC_returnsSingleton() {
        assertSame(IslamicChronology.getInstanceUTC(), IslamicChronology.getInstanceUTC());
    }

    @Test
    public void defaultLeapYearPattern_is16Based() {
        assertSame(IslamicChronology.LEAP_YEAR_16_BASED, UTC.getLeapYearPatternType());
    }

    // ---------------------------------------------------------------------
    // Leap year rules
    // ---------------------------------------------------------------------

    @Test
    public void leapYearPattern16Based_examples() {
        // 16-based pattern has leap years in positions: 2,5,7,10,13,16,18,21,24,26,29
        assertTrue(UTC.isLeapYear(2));
        assertTrue(UTC.isLeapYear(16));
        assertFalse(UTC.isLeapYear(17));
        assertFalse(UTC.isLeapYear(30)); // 30 is NOT leap in 16-based
    }

    @Test
    public void habashAlHasibPattern_hasYear30Leap() {
        IslamicChronology habash = IslamicChronology.getInstance(DateTimeZone.UTC,
                IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB);
        assertTrue(habash.isLeapYear(30));
    }

    // ---------------------------------------------------------------------
    // Days in year / month
    // ---------------------------------------------------------------------

    @Test
    public void daysInYear_nonLeapVsLeap() {
        assertEquals(354, UTC.getDaysInYear(1));  // non-leap
        assertEquals(355, UTC.getDaysInYear(2));  // leap
    }

    @Test
    public void daysInYearMonth_regularAndLeapYear() {
        // Months alternate 30/29 with the last month 29 or 30 depending on leap year.
        assertEquals(30, UTC.getDaysInYearMonth(1, 1));   // Muharram (always 30)
        assertEquals(29, UTC.getDaysInYearMonth(1, 2));   // Safar (always 29)
        assertEquals(29, UTC.getDaysInYearMonth(1, 12));  // Dhu al-Hijjah in non-leap
        assertEquals(30, UTC.getDaysInYearMonth(2, 12));  // Dhu al-Hijjah in leap
    }

    @Test
    public void daysInMonthMax_byMonthNumber() {
        assertEquals(30, UTC.getDaysInMonthMax(1));
        assertEquals(29, UTC.getDaysInMonthMax(2));
        assertEquals(30, UTC.getDaysInMonthMax(12)); // max is 30 because of leap years
    }

    @Test
    public void daysInMonthMax_withoutArgs_is30() {
        assertEquals(30, UTC.getDaysInMonthMax());
    }

    @Test
    public void daysInMonthMax_forInstant_startOfYearIs30DayMonth() {
        long startOf1440 = UTC.calculateFirstDayOfYearMillis(1440);
        assertEquals(30, UTC.getDaysInMonthMax(startOf1440));
    }

    // ---------------------------------------------------------------------
    // Year calculations and differences
    // ---------------------------------------------------------------------

    @Test
    public void setYear_preservesDayAndComputesYearDifference() {
        long anyInstant = UTC.calculateFirstDayOfYearMillis(1440); // 1440-01-01 AH, midnight UTC
        int y = UTC.getYear(anyInstant);
        long nextYearSameDay = UTC.setYear(anyInstant, y + 1);

        assertEquals(y, UTC.getYear(anyInstant));
        assertEquals(y + 1, UTC.getYear(nextYearSameDay));

        assertEquals(1L, UTC.getYearDifference(nextYearSameDay, anyInstant));
        assertEquals(-1L, UTC.getYearDifference(anyInstant, nextYearSameDay));
    }

    // ---------------------------------------------------------------------
    // Month/day-of-month from millis
    // ---------------------------------------------------------------------

    @Test
    public void monthAndDayAtStartOfYear() {
        int y = 1440;
        long startOfYear = UTC.calculateFirstDayOfYearMillis(y);

        assertEquals(1, UTC.getMonthOfYear(startOfYear, y));
        assertEquals(1, UTC.getDayOfMonth(startOfYear));

        // Move 35 days forward: should be in month 2 (Safar)
        long plus35Days = startOfYear + 35L * DateTimeConstants.MILLIS_PER_DAY;
        assertEquals(2, UTC.getMonthOfYear(plus35Days, y));
    }

    // ---------------------------------------------------------------------
    // Averages and epoch constants
    // ---------------------------------------------------------------------

    @Test
    public void averageMillis_areStable() {
        assertEquals(30617280288L, UTC.getAverageMillisPerYear());
        assertEquals(15308640144L, UTC.getAverageMillisPerYearDividedByTwo());
        assertEquals(2551440384L, UTC.getAverageMillisPerMonth());
        assertEquals(21260793600000L, UTC.getApproxMillisAtEpochDividedByTwo());
    }

    // ---------------------------------------------------------------------
    // Min/max year and boundary checks
    // ---------------------------------------------------------------------

    @Test
    public void minAndMaxYear() {
        assertEquals(1, UTC.getMinYear()); // Not proleptic
        assertEquals(292271022, UTC.getMaxYear());
        assertEquals(355, UTC.getDaysInYearMax());
    }

    @Test(expected = ArithmeticException.class)
    public void calculateFirstDayOfYearMillis_throwsForYearAboveMax() {
        int tooLarge = UTC.getMaxYear() + 1;
        UTC.calculateFirstDayOfYearMillis(tooLarge);
    }

    // ---------------------------------------------------------------------
    // Instance factories, zones and equality
    // ---------------------------------------------------------------------

    @Test
    public void equalsAndHashCode_considerZoneAndLeapPattern() {
        IslamicChronology utcDefault = IslamicChronology.getInstanceUTC();
        IslamicChronology utcAgain = IslamicChronology.getInstance(DateTimeZone.UTC);
        IslamicChronology utc15 = IslamicChronology.getInstance(DateTimeZone.UTC,
                IslamicChronology.LEAP_YEAR_15_BASED);
        IslamicChronology offset = IslamicChronology.getInstance(OFFSET_01_00);

        // identity and equality
        assertSame(utcDefault, UTC);
        assertTrue(utcDefault.equals(utcAgain));
        assertEquals(utcDefault.hashCode(), utcAgain.hashCode());

        // different leap pattern -> not equal
        assertFalse(utcDefault.equals(utc15));

        // different zone -> not equal
        assertFalse(utcDefault.equals(offset));
    }

    @Test
    public void withZone_changesZoneAndwithUTCReturnsUTC() {
        IslamicChronology offset = UTC.withZone(OFFSET_01_00);
        assertNotSame("Different zone should return a different instance", UTC, offset);
        assertSame("Switching back to UTC should return the UTC singleton", UTC, offset.withUTC());
    }

    @Test(expected = NullPointerException.class)
    public void getInstance_withNullLeapPatternThrows() {
        IslamicChronology.getInstance(DateTimeZone.forOffsetHours(12), null);
    }
}