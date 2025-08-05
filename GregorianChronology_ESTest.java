package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.TimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.AssembledChronology;
import org.joda.time.chrono.GregorianChronology;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class GregorianChronology_ESTest extends GregorianChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYearMillisForYear20() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
        long firstDayMillis = gregorianChronology.calculateFirstDayOfYearMillis(20);
        assertEquals(-61536067200000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testAssembleFields() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstance();
        AssembledChronology.Fields fields = new AssembledChronology.Fields();
        gregorianChronology.assemble(fields);
    }

    @Test(timeout = 4000)
    public void testIsLeapYearForYear0() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        GregorianChronology gregorianChronology = GregorianChronology.getInstance(defaultZone);
        assertTrue(gregorianChronology.isLeapYear(0));
    }

    @Test(timeout = 4000)
    public void testIsLeapYearForYearMinus900() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstance();
        assertFalse(gregorianChronology.isLeapYear(-900));
    }

    @Test(timeout = 4000)
    public void testGetAverageMillisPerYearDividedByTwo() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstance();
        long averageMillis = gregorianChronology.getAverageMillisPerYearDividedByTwo();
        assertEquals(15778476000L, averageMillis);
    }

    @Test(timeout = 4000)
    public void testGetApproxMillisAtEpochDividedByTwo() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstance();
        long approxMillis = gregorianChronology.getApproxMillisAtEpochDividedByTwo();
        assertEquals(31083597720000L, approxMillis);
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYearMillisForYear1970() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstance();
        long firstDayMillis = gregorianChronology.calculateFirstDayOfYearMillis(1970);
        assertEquals(0L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testGetDaysInYearMonthWithInvalidYearMonth() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
        try {
            gregorianChronology.getDaysInYearMonth(-2764, -2764);
            fail("Expecting exception: ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            verifyException("org.joda.time.chrono.BasicGJChronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testAssembleWithNullFields() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstance();
        try {
            gregorianChronology.assemble(null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.chrono.BasicChronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testLocalDateTimeToDateTimeWithOffset() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
        LocalDateTime localDateTime = new LocalDateTime(199L, gregorianChronology);
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(-3631);
        DateTime dateTime = localDateTime.toDateTime(offsetZone);
        assertEquals(3830L, dateTime.getMillis());
    }

    @Test(timeout = 4000)
    public void testGetInstanceWithInvalidMinDaysInFirstWeek() {
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(531);
        try {
            GregorianChronology.getInstance(offsetZone, 531);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.chrono.GregorianChronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetInstanceWithNegativeMinDaysInFirstWeek() {
        try {
            GregorianChronology.getInstance(null, -3203);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.chrono.GregorianChronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetAverageMillisPerMonth() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
        long averageMillis = gregorianChronology.getAverageMillisPerMonth();
        assertEquals(2629746000L, averageMillis);
    }

    @Test(timeout = 4000)
    public void testGetMaxYear() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstance();
        int maxYear = gregorianChronology.getMaxYear();
        assertEquals(292278993, maxYear);
    }

    @Test(timeout = 4000)
    public void testWithUTC() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        GregorianChronology gregorianChronology = GregorianChronology.getInstance(defaultZone, 7);
        Chronology utcChronology = gregorianChronology.withUTC();
        assertNotSame(utcChronology, gregorianChronology);
    }

    @Test(timeout = 4000)
    public void testGetAverageMillisPerYear() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstance();
        long averageMillis = gregorianChronology.getAverageMillisPerYear();
        assertEquals(31556952000L, averageMillis);
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYearMillisForNegativeYear() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
        long firstDayMillis = gregorianChronology.calculateFirstDayOfYearMillis(-292275054);
        assertEquals(-9223372017043200000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYearMillisForYear3000() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstance();
        long firstDayMillis = gregorianChronology.calculateFirstDayOfYearMillis(3000);
        assertEquals(32503680000000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYearMillisForYear0() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstance();
        long firstDayMillis = gregorianChronology.calculateFirstDayOfYearMillis(0);
        assertEquals(-62167219200000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testWithZoneUTC() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstance();
        DateTimeZone utcZone = DateTimeZone.UTC;
        Chronology utcChronology = gregorianChronology.withZone(utcZone);
        assertSame(gregorianChronology, utcChronology);
    }

    @Test(timeout = 4000)
    public void testWithZoneNull() {
        GregorianChronology gregorianChronology = GregorianChronology.getInstanceUTC();
        Chronology chronologyWithNullZone = gregorianChronology.withZone(null);
        assertSame(gregorianChronology, chronologyWithNullZone);
    }

    @Test(timeout = 4000)
    public void testGetMinYear() {
        TimeZone timeZone = TimeZone.getTimeZone("tySc3*yvDsM");
        DateTimeZone dateTimeZone = DateTimeZone.forTimeZone(timeZone);
        GregorianChronology gregorianChronology = GregorianChronology.getInstance(dateTimeZone, 1);
        int minYear = gregorianChronology.getMinYear();
        assertEquals(-292275054, minYear);
    }
}