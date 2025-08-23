package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for CopticChronology.
 * These tests avoid EvoSuite-specific scaffolding and magic numbers where possible,
 * use explicit time zones, and include comments that explain the intent.
 */
public class CopticChronologyTest {

    @Test
    public void getInstanceUTC_returnsSingletonAndStaysUTC() {
        CopticChronology utc = CopticChronology.getInstanceUTC();

        // Same singleton for explicit UTC
        assertSame(utc, CopticChronology.getInstance(DateTimeZone.UTC));

        // withUTC on a UTC instance is a no-op (same reference)
        assertSame(utc, utc.withUTC());

        // withZone(UTC) on a UTC instance is also a no-op (same reference)
        assertSame(utc, utc.withZone(DateTimeZone.UTC));
    }

    @Test
    public void getInstance_returnsCachedInstancePerZoneAndMinDays() {
        DateTimeZone zone = DateTimeZone.forOffsetHours(3);

        // Cached per zone
        CopticChronology a = CopticChronology.getInstance(zone);
        CopticChronology b = CopticChronology.getInstance(zone);
        assertSame(a, b);

        // Different minDaysInFirstWeek yields a different instance in the same zone
        CopticChronology min1 = CopticChronology.getInstance(zone, 1);
        CopticChronology min7 = CopticChronology.getInstance(zone, 7);
        assertNotSame(min1, min7);
        assertEquals(CopticChronology.getInstance(zone, 1), min1);
        assertEquals(CopticChronology.getInstance(zone, 7), min7);
    }

    @Test
    public void withZone_returnsSameWhenZoneUnchanged_andNewWhenChanged() {
        DateTimeZone zone = DateTimeZone.forOffsetHoursMinutes(5, 30);
        CopticChronology inZone = CopticChronology.getInstance(zone);

        // withZone for the same zone returns the identical instance
        assertSame(inZone, inZone.withZone(zone));

        // Switching zones yields an instance in the target zone
        Chronology moved = inZone.withZone(DateTimeZone.UTC);
        assertSame(CopticChronology.getInstanceUTC(), moved);
    }

    @Test
    public void withUTC_convertsFromNonUtcToUtcSingleton() {
        DateTimeZone zone = DateTimeZone.forOffsetHours(-4);
        CopticChronology inZone = CopticChronology.getInstance(zone);

        Chronology utc = inZone.withUTC();
        assertSame(CopticChronology.getInstanceUTC(), utc);
    }

    @Test
    public void constants_minAndMaxYear_areExposed() {
        CopticChronology utc = CopticChronology.getInstanceUTC();
        assertEquals(-292269337, utc.getMinYear());
        assertEquals(292272708, utc.getMaxYear());
    }

    @Test
    public void eraConstant_isCE() {
        assertEquals(DateTimeConstants.CE, CopticChronology.AM);
    }

    @Test
    public void calculateFirstDayOfYearMillis_isStableForKnownYears_andMonotonic() {
        CopticChronology utc = CopticChronology.getInstanceUTC();

        // Year 1 start in milliseconds relative to the ISO epoch (documentation-friendly known value)
        long startYear1 = utc.calculateFirstDayOfYearMillis(1);
        assertEquals(-53184211200000L, startYear1);

        // Starts of consecutive years must strictly increase
        long startYear2 = utc.calculateFirstDayOfYearMillis(2);
        assertTrue("Year 2 start should be after Year 1 start", startYear2 > startYear1);
    }

    @Test
    public void getInstance_validatesMinDaysInFirstWeek() {
        DateTimeZone zone = DateTimeZone.forOffsetHours(1);

        // Valid bounds
        assertNotNull(CopticChronology.getInstance(zone, 1));
        assertNotNull(CopticChronology.getInstance(zone, 7));

        // Below lower bound
        assertThrows(IllegalArgumentException.class, () -> CopticChronology.getInstance(zone, 0));

        // Above upper bound
        assertThrows(IllegalArgumentException.class, () -> CopticChronology.getInstance(zone, 8));
    }

    @Test
    public void isLeapDay_trueOn6thDayOf13thMonthInLeapYears_falseOtherwise() {
        CopticChronology coptic = CopticChronology.getInstanceUTC();

        // Pick a year and move forward until we find a leap year (max 3 steps since leap every 4 years)
        int year = 1736;
        Integer leapYear = null;
        for (int y = year; y < year + 4; y++) {
            try {
                // Day 6 of month 13 only exists in leap years
                new DateTime(y, 13, 6, 0, 0, 0, 0, coptic);
                leapYear = y;
                break;
            } catch (IllegalArgumentException ignored) {
                // Not a leap year, try next
            }
        }
        assertNotNull("Failed to find a leap year in a span of 4 years", leapYear);

        DateTime leapDay = new DateTime(leapYear, 13, 6, 0, 0, 0, 0, coptic);
        assertTrue("Expected 13-06 to be a leap day in leap years", coptic.isLeapDay(leapDay.getMillis()));

        // Any 13-05 exists and should never be a leap day
        DateTime nonLeapDay = new DateTime(leapYear, 13, 5, 0, 0, 0, 0, coptic);
        assertFalse("13-05 should never be a leap day", coptic.isLeapDay(nonLeapDay.getMillis()));
    }

    @Test
    public void isLeapDay_throwsForInstantsBeforeSupportedRange() {
        CopticChronology utc = CopticChronology.getInstanceUTC();

        // Compute the minimum supported instant as the start of year 1 (inclusive)
        long firstSupported = utc.calculateFirstDayOfYearMillis(1);

        // One millisecond before the supported range must throw
        assertThrows(IllegalArgumentException.class, () -> utc.isLeapDay(firstSupported - 1));
    }
}