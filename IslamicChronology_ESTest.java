package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.AssembledChronology.Fields;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.tz.UTCProvider;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class IslamicChronology_ESTest extends IslamicChronology_ESTest_scaffolding {

    private static final long MILLIS_1 = 918518400000L;
    private static final long MILLIS_2 = 604800L;
    private static final long MILLIS_3 = 2602780732800000L;
    private static final long MILLIS_4 = -42246144000000L;
    private static final long MILLIS_5 = 2602045900800000L;

    @Test(timeout = 4000)
    public void testLeapYearPatternTypeEquality() throws Throwable {
        IslamicChronology.LeapYearPatternType leapYearPatternType = IslamicChronology.LEAP_YEAR_15_BASED;
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        boolean isEqual = islamicChronology.LEAP_YEAR_HABASH_AL_HASIB.equals(leapYearPatternType);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testLeapYearPatternTypeIsLeapYear() throws Throwable {
        IslamicChronology.LeapYearPatternType leapYearPatternType = new IslamicChronology.LeapYearPatternType(-1861, -276);
        boolean isLeapYear = leapYearPatternType.isLeapYear(-1861);
        assertFalse(isLeapYear);
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYearMillis() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        long firstDayMillis = islamicChronology.calculateFirstDayOfYearMillis(-292269337);
        assertEquals(-8948534433609600000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testGetYearMonthDayMillis() throws Throwable {
        IslamicChronology.LeapYearPatternType leapYearPatternType = IslamicChronology.LEAP_YEAR_15_BASED;
        IslamicChronology islamicChronology = new IslamicChronology(null, null, leapYearPatternType);
        long yearMonthDayMillis = islamicChronology.getYearMonthDayMillis(292271022, -1, 273);
        assertEquals(8948501182656000000L, yearMonthDayMillis);
    }

    @Test(timeout = 4000)
    public void testGetDaysInMonthMax() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        int daysInMonth = islamicChronology.getDaysInMonthMax(-238);
        assertEquals(29, daysInMonth);
    }

    @Test(timeout = 4000)
    public void testGetDaysInYearMonth() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int daysInYearMonth = islamicChronology.getDaysInYearMonth(220, -4026);
        assertEquals(29, daysInYearMonth);
    }

    @Test(timeout = 4000)
    public void testGetDaysInYearMonthForLeapYear() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int daysInYearMonth = islamicChronology.getDaysInYearMonth(354, 354);
        assertEquals(29, daysInYearMonth);
    }

    @Test(timeout = 4000)
    public void testGetYearDifference() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        long yearDifference = islamicChronology.getYearDifference(-2254L, 55382400010L);
        assertEquals(-2L, yearDifference);
    }

    @Test(timeout = 4000)
    public void testSetYear() throws Throwable {
        DateTimeZone dateTimeZone = DateTimeZone.getDefault();
        IslamicChronology islamicChronology = IslamicChronology.getInstance(dateTimeZone);
        long setYearMillis = islamicChronology.setYear(MILLIS_4, 1900);
        assertEquals(15651100800000L, setYearMillis);
    }

    @Test(timeout = 4000)
    public void testGetDaysInMonthMaxForMillis() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        int daysInMonth = islamicChronology.getDaysInMonthMax(-42215385600000L);
        assertEquals(30, daysInMonth);
    }

    @Test(timeout = 4000)
    public void testChronologyEquality() throws Throwable {
        DateTimeZone dateTimeZone = DateTimeZone.getDefault();
        IslamicChronology islamicChronology = IslamicChronology.getInstance(dateTimeZone);
        Object object = new Object();
        IslamicChronology islamicChronology1 = new IslamicChronology(islamicChronology, object, islamicChronology.LEAP_YEAR_15_BASED);
        boolean isEqual = islamicChronology.equals(islamicChronology1);
        assertFalse(islamicChronology1.equals(islamicChronology));
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testAssembleFields() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        Fields fields = new Fields();
        islamicChronology.assemble(fields);
        assertEquals(1, IslamicChronology.AH);
    }

    @Test(timeout = 4000)
    public void testIsLeapYear() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        boolean isLeapYear = islamicChronology.isLeapYear(1);
        assertFalse(isLeapYear);
    }

    @Test(timeout = 4000)
    public void testGetYearDifferenceForMillis() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        long yearDifference = islamicChronology.getYearDifference(MILLIS_1, MILLIS_2);
        assertEquals(29L, yearDifference);
    }

    @Test(timeout = 4000)
    public void testGetYear() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int year = islamicChronology.getYear(-69139612803234L);
        assertEquals(-839, year);
    }

    @Test(timeout = 4000)
    public void testGetTotalMillisByYearMonth() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        long totalMillis = islamicChronology.getTotalMillisByYearMonth(1, 1);
        assertEquals(0L, totalMillis);
    }

    @Test(timeout = 4000)
    public void testGetTotalMillisByYearMonthForNegativeYear() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        long totalMillis = islamicChronology.getTotalMillisByYearMonth(-182, 690562340);
        assertEquals(1760105289686400000L, totalMillis);
    }

    @Test(timeout = 4000)
    public void testGetTotalMillisByYearMonthForNegativeMonth() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        long totalMillis = islamicChronology.getTotalMillisByYearMonth(1, -1291);
        assertEquals(-3293049600000L, totalMillis);
    }

    @Test(timeout = 4000)
    public void testGetMonthOfYear() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int monthOfYear = islamicChronology.getMonthOfYear(0L, -2992);
        assertEquals(52360, monthOfYear);
    }

    @Test(timeout = 4000)
    public void testGetMonthOfYearForLargeYear() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        int monthOfYear = islamicChronology.getMonthOfYear(1518L, 292271022);
        assertEquals(-16657633, monthOfYear);
    }

    @Test(timeout = 4000)
    public void testGetLeapYearPatternType() throws Throwable {
        UTCProvider utcProvider = new UTCProvider();
        GJChronology gjChronology = GJChronology.getInstance(null);
        IslamicChronology islamicChronology = new IslamicChronology(gjChronology, utcProvider, null);
        IslamicChronology.LeapYearPatternType leapYearPatternType = islamicChronology.getLeapYearPatternType();
        assertNull(leapYearPatternType);
    }

    @Test(timeout = 4000)
    public void testGetDayOfMonth() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int dayOfMonth = islamicChronology.getDayOfMonth(-108654998400000L);
        assertEquals(0, dayOfMonth);
    }

    @Test(timeout = 4000)
    public void testGetDayOfMonthForNegativeMillis() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int dayOfMonth = islamicChronology.getDayOfMonth(-83855174400000L);
        assertEquals(-2, dayOfMonth);
    }

    @Test(timeout = 4000)
    public void testGetAverageMillisPerMonth() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        long averageMillisPerMonth = islamicChronology.getAverageMillisPerMonth();
        assertEquals(2551440384L, averageMillisPerMonth);
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYearMillisForPositiveYear() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        long firstDayMillis = islamicChronology.calculateFirstDayOfYearMillis(1576);
        assertEquals(5700585600000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testSetYearWithNullLeapYearPatternType() throws Throwable {
        CopticChronology copticChronology = CopticChronology.getInstanceUTC();
        IslamicChronology islamicChronology = new IslamicChronology(copticChronology, copticChronology, null);
        try {
            islamicChronology.setYear(380L, 1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testHashCodeWithNullLeapYearPatternType() throws Throwable {
        ISOChronology isoChronology = ISOChronology.getInstanceUTC();
        IslamicChronology islamicChronology = new IslamicChronology(isoChronology, isoChronology, null);
        try {
            islamicChronology.hashCode();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetYearWithNullLeapYearPatternType() throws Throwable {
        IslamicChronology islamicChronology = new IslamicChronology(null, null, null);
        try {
            islamicChronology.getYear(1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetMonthOfYearWithNullLeapYearPatternType() throws Throwable {
        ISOChronology isoChronology = ISOChronology.getInstanceUTC();
        IslamicChronology islamicChronology = new IslamicChronology(isoChronology, isoChronology, null);
        try {
            islamicChronology.getMonthOfYear(MILLIS_1, 1138);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetDayOfMonthWithNullLeapYearPatternType() throws Throwable {
        CopticChronology copticChronology = CopticChronology.getInstance();
        IslamicChronology islamicChronology = new IslamicChronology(copticChronology, copticChronology, null);
        try {
            islamicChronology.getDayOfMonth(1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYearMillisWithNullLeapYearPatternType() throws Throwable {
        GJChronology gjChronology = GJChronology.getInstanceUTC();
        Object object = new Object();
        IslamicChronology islamicChronology = new IslamicChronology(gjChronology, object, null);
        try {
            islamicChronology.calculateFirstDayOfYearMillis(1593);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYearMillisForLargeYear() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        try {
            islamicChronology.calculateFirstDayOfYearMillis(292272984);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetInstanceWithNullLeapYearPatternType() throws Throwable {
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetHours(12);
        try {
            IslamicChronology.getInstance(dateTimeZone, null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testWithUTC() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        Chronology chronology = islamicChronology.withUTC();
        assertSame(islamicChronology, chronology);
    }

    @Test(timeout = 4000)
    public void testGetLeapYearPatternTypeNotNull() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        IslamicChronology.LeapYearPatternType leapYearPatternType = islamicChronology.getLeapYearPatternType();
        assertNotNull(leapYearPatternType);
    }

    @Test(timeout = 4000)
    public void testLeapYearPatternTypeEqualityForDifferentTypes() throws Throwable {
        IslamicChronology.LeapYearPatternType leapYearPatternType = IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB;
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        boolean isEqual = islamicChronology.LEAP_YEAR_INDIAN.equals(leapYearPatternType);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testLeapYearPatternTypeEqualityForSameType() throws Throwable {
        IslamicChronology.LeapYearPatternType leapYearPatternType = IslamicChronology.LEAP_YEAR_15_BASED;
        boolean isEqual = leapYearPatternType.equals(leapYearPatternType);
        assertTrue(isEqual);
    }

    @Test(timeout = 4000)
    public void testLeapYearPatternTypeEqualityForDifferentObject() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        boolean isEqual = islamicChronology.LEAP_YEAR_16_BASED.equals(islamicChronology);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testGetDaysInMonthMaxForPositiveMonth() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        int daysInMonth = islamicChronology.getDaysInMonthMax(1);
        assertEquals(30, daysInMonth);
    }

    @Test(timeout = 4000)
    public void testGetDaysInMonthMaxForMonth12() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        int daysInMonth = islamicChronology.getDaysInMonthMax(12);
        assertEquals(30, daysInMonth);
    }

    @Test(timeout = 4000)
    public void testGetDaysInMonthMaxForLargeMonth() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int daysInMonth = islamicChronology.getDaysInMonthMax(100);
        assertEquals(29, daysInMonth);
    }

    @Test(timeout = 4000)
    public void testGetDaysInMonthMaxForMillis() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int daysInMonth = islamicChronology.getDaysInMonthMax(MILLIS_3);
        assertEquals(30, daysInMonth);
    }

    @Test(timeout = 4000)
    public void testGetDaysInYearMonthForLeapYear() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        int daysInYearMonth = islamicChronology.getDaysInYearMonth(12, 12);
        assertEquals(29, daysInYearMonth);
    }

    @Test(timeout = 4000)
    public void testGetDaysInYearForLeapYear() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int daysInYear = islamicChronology.getDaysInYear(-188);
        assertEquals(355, daysInYear);
    }

    @Test(timeout = 4000)
    public void testGetDaysInYearForNonLeapYear() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int daysInYear = islamicChronology.getDaysInYear(1);
        assertEquals(354, daysInYear);
    }

    @Test(timeout = 4000)
    public void testGetDayOfMonthForMillis() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int dayOfMonth = islamicChronology.getDayOfMonth(MILLIS_3);
        assertEquals(30, dayOfMonth);
    }

    @Test(timeout = 4000)
    public void testGetYearDifferenceForSameMillis() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        long yearDifference = islamicChronology.getYearDifference(-2966L, -2966L);
        assertEquals(0L, yearDifference);
    }

    @Test(timeout = 4000)
    public void testSetYearForLargeMillis() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        long setYearMillis = islamicChronology.setYear(MILLIS_5, 46);
        assertEquals(-41113267200000L, setYearMillis);
    }

    @Test(timeout = 4000)
    public void testSetYearForLargeYear() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        long setYearMillis = islamicChronology.setYear(MILLIS_3, 86400);
        assertEquals(2602811318400000L, setYearMillis);
    }

    @Test(timeout = 4000)
    public void testSetYearForSmallMillis() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        long setYearMillis = islamicChronology.setYear(1, 1);
        assertEquals(-42496790399999L, setYearMillis);
    }

    @Test(timeout = 4000)
    public void testGetDayOfMonthForLeapYearPatternType() throws Throwable {
        IslamicChronology.LeapYearPatternType leapYearPatternType = new IslamicChronology.LeapYearPatternType(8171, 8171);
        IslamicChronology islamicChronology = new IslamicChronology(null, null, leapYearPatternType);
        int dayOfMonth = islamicChronology.getDayOfMonth(8171);
        assertEquals(19, dayOfMonth);
    }

    @Test(timeout = 4000)
    public void testChronologyEqualityForDifferentTimeZones() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(1);
        IslamicChronology islamicChronology1 = IslamicChronology.getInstance(dateTimeZone);
        boolean isEqual = islamicChronology.equals(islamicChronology1);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testChronologyEqualityForDifferentLeapYearPatternTypes() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(1);
        IslamicChronology islamicChronology1 = IslamicChronology.getInstance(dateTimeZone, islamicChronology.LEAP_YEAR_INDIAN);
        boolean isEqual = islamicChronology.equals(islamicChronology1);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testChronologyEqualityForDifferentObjects() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        Object object = new Object();
        boolean isEqual = islamicChronology.equals(object);
        assertFalse(isEqual);
    }

    @Test(timeout = 4000)
    public void testChronologyEqualityForSameInstance() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        boolean isEqual = islamicChronology.equals(islamicChronology);
        assertTrue(isEqual);
    }

    @Test(timeout = 4000)
    public void testChronologyEqualityForSameLeapYearPatternType() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        IslamicChronology islamicChronology1 = new IslamicChronology(islamicChronology, islamicChronology, islamicChronology.LEAP_YEAR_16_BASED);
        boolean isEqual = islamicChronology.equals(islamicChronology1);
        assertTrue(isEqual);
    }

    @Test(timeout = 4000)
    public void testWithZone() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetHoursMinutes(1, 1);
        Chronology chronology = islamicChronology.withZone(dateTimeZone);
        assertNotSame(chronology, islamicChronology);
    }

    @Test(timeout = 4000)
    public void testWithZoneNull() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        IslamicChronology islamicChronology1 = (IslamicChronology) islamicChronology.withZone(null);
        assertEquals(1, IslamicChronology.AH);
    }

    @Test(timeout = 4000)
    public void testGetInstanceWithInvalidLeapYearPatternType() throws Throwable {
        IslamicChronology.LeapYearPatternType leapYearPatternType = new IslamicChronology.LeapYearPatternType(4497, 4497);
        try {
            IslamicChronology.getInstance(null, leapYearPatternType);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetDaysInYearMax() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        int daysInYearMax = islamicChronology.getDaysInYearMax();
        assertEquals(355, daysInYearMax);
    }

    @Test(timeout = 4000)
    public void testGetMinYear() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        int minYear = islamicChronology.getMinYear();
        assertEquals(1, minYear);
    }

    @Test(timeout = 4000)
    public void testGetDaysInYearWithNullLeapYearPatternType() throws Throwable {
        IslamicChronology islamicChronology = new IslamicChronology(null, null, null);
        try {
            islamicChronology.getDaysInYear(1);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testGetApproxMillisAtEpochDividedByTwo() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        long approxMillis = islamicChronology.getApproxMillisAtEpochDividedByTwo();
        assertEquals(21260793600000L, approxMillis);
    }

    @Test(timeout = 4000)
    public void testGetMaxYear() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        int maxYear = islamicChronology.getMaxYear();
        assertEquals(292271022, maxYear);
    }

    @Test(timeout = 4000)
    public void testGetAverageMillisPerYearDividedByTwo() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstanceUTC();
        long averageMillis = islamicChronology.getAverageMillisPerYearDividedByTwo();
        assertEquals(15308640144L, averageMillis);
    }

    @Test(timeout = 4000)
    public void testGetAverageMillisPerYear() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        long averageMillis = islamicChronology.getAverageMillisPerYear();
        assertEquals(30617280288L, averageMillis);
    }

    @Test(timeout = 4000)
    public void testGetYearForNegativeMillis() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance(null);
        int year = islamicChronology.getYear(-2099L);
        assertEquals(1389, year);
    }

    @Test(timeout = 4000)
    public void testHashCode() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        islamicChronology.hashCode();
    }

    @Test(timeout = 4000)
    public void testGetDaysInMonthMaxDefault() throws Throwable {
        IslamicChronology islamicChronology = IslamicChronology.getInstance();
        int daysInMonthMax = islamicChronology.getDaysInMonthMax();
        assertEquals(30, daysInMonthMax);
    }
}