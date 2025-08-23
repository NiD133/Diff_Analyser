package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.AssembledChronology;
import org.joda.time.chrono.CopticChronology;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class CopticChronology_ESTest extends CopticChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYearMillis_MaxYear() throws Throwable {
        // Test calculating the first day of the maximum supported year
        CopticChronology copticChronology = CopticChronology.getInstanceUTC();
        long firstDayMillis = copticChronology.calculateFirstDayOfYearMillis(292272708);
        assertEquals(9223371994233600000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testAssembleFields() throws Throwable {
        // Test assembling fields in CopticChronology
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        CopticChronology copticChronology = CopticChronology.getInstance(defaultZone);
        AssembledChronology.Fields fields = new AssembledChronology.Fields();
        copticChronology.assemble(fields);
        assertEquals(1, CopticChronology.AM);
    }

    @Test(timeout = 4000)
    public void testGetApproxMillisAtEpochDividedByTwo() throws Throwable {
        // Test getting approximate milliseconds at epoch divided by two
        CopticChronology copticChronology = CopticChronology.getInstance();
        long approxMillis = copticChronology.getApproxMillisAtEpochDividedByTwo();
        assertEquals(26607895200000L, approxMillis);
    }

    @Test(timeout = 4000)
    public void testIsLeapDay_BelowMinimumInstant() throws Throwable {
        // Test isLeapDay with an instant below the supported minimum
        CopticChronology copticChronology = CopticChronology.getInstance();
        try {
            copticChronology.isLeapDay(-9223372036854775768L);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.chrono.LimitChronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testInvalidMinDaysInFirstWeek() throws Throwable {
        // Test creating CopticChronology with invalid min days in first week
        CopticChronology copticChronology = CopticChronology.getInstanceUTC();
        try {
            new CopticChronology(copticChronology, copticChronology, 634);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.chrono.BasicChronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testChronologyEquality() throws Throwable {
        // Test equality of two CopticChronology instances
        CopticChronology copticChronology = CopticChronology.getInstance();
        CopticChronology anotherChronology = new CopticChronology(copticChronology, copticChronology, 1);
        assertFalse(anotherChronology.equals(copticChronology));
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYearMillis_Year3571() throws Throwable {
        // Test calculating the first day of the year 3571
        CopticChronology copticChronology = CopticChronology.getInstanceUTC();
        long firstDayMillis = copticChronology.calculateFirstDayOfYearMillis(3571);
        assertEquals(59476377600000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYearMillis_Year1() throws Throwable {
        // Test calculating the first day of the year 1
        CopticChronology copticChronology = CopticChronology.getInstanceUTC();
        long firstDayMillis = copticChronology.calculateFirstDayOfYearMillis(1);
        assertEquals(-53184211200000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testInvalidMinDaysInFirstWeek_Negative() throws Throwable {
        // Test creating CopticChronology with negative min days in first week
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(-1686);
        try {
            CopticChronology.getInstance(offsetZone, -1686);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.chrono.CopticChronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetInstanceWithNullZone() throws Throwable {
        // Test getting CopticChronology instance with null time zone
        CopticChronology copticChronology = CopticChronology.getInstance(null, 1);
        assertEquals(1, CopticChronology.AM);
    }

    @Test(timeout = 4000)
    public void testChronologyEqualityWithNullBase() throws Throwable {
        // Test equality of CopticChronology with null base
        CopticChronology copticChronology = CopticChronology.getInstance();
        CopticChronology anotherChronology = new CopticChronology(null, copticChronology, 1);
        assertFalse(anotherChronology.equals(copticChronology));
    }

    @Test(timeout = 4000)
    public void testGetMinYear() throws Throwable {
        // Test getting the minimum supported year
        CopticChronology copticChronology = CopticChronology.getInstanceUTC();
        int minYear = copticChronology.getMinYear();
        assertEquals(-292269337, minYear);
    }

    @Test(timeout = 4000)
    public void testWithUTC() throws Throwable {
        // Test converting CopticChronology to UTC
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(-1686);
        CopticChronology copticChronology = CopticChronology.getInstance(offsetZone, 7);
        CopticChronology utcChronology = (CopticChronology) copticChronology.withUTC();
        assertEquals(1, CopticChronology.AM);
    }

    @Test(timeout = 4000)
    public void testIsLeapDay_NonLeapDay() throws Throwable {
        // Test isLeapDay with a non-leap day instant
        CopticChronology copticChronology = CopticChronology.getInstance();
        boolean isLeapDay = copticChronology.isLeapDay(16965676800000L);
        assertFalse(isLeapDay);
    }

    @Test(timeout = 4000)
    public void testIsLeapDay_MaxInstant() throws Throwable {
        // Test isLeapDay with the maximum supported instant
        CopticChronology copticChronology = CopticChronology.getInstance();
        boolean isLeapDay = copticChronology.isLeapDay(9223371994233600000L);
        assertFalse(isLeapDay);
    }

    @Test(timeout = 4000)
    public void testGetMaxYear() throws Throwable {
        // Test getting the maximum supported year
        CopticChronology copticChronology = CopticChronology.getInstance();
        int maxYear = copticChronology.getMaxYear();
        assertEquals(292272708, maxYear);
    }

    @Test(timeout = 4000)
    public void testWithZone() throws Throwable {
        // Test converting CopticChronology to a specific time zone
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(-1686);
        CopticChronology copticChronology = CopticChronology.getInstance(offsetZone, 7);
        Chronology chronologyWithZone = copticChronology.withZone(offsetZone);
        assertSame(chronologyWithZone, copticChronology);
    }

    @Test(timeout = 4000)
    public void testIsLeapDay_Instant1() throws Throwable {
        // Test isLeapDay with an instant of 1
        CopticChronology copticChronology = CopticChronology.getInstance();
        boolean isLeapDay = copticChronology.isLeapDay(1);
        assertFalse(isLeapDay);
    }

    @Test(timeout = 4000)
    public void testWithZone_NullZone() throws Throwable {
        // Test converting CopticChronology to a null time zone
        CopticChronology copticChronology = CopticChronology.getInstanceUTC();
        Chronology chronologyWithNullZone = copticChronology.withZone(null);
        assertSame(copticChronology, chronologyWithNullZone);
    }
}