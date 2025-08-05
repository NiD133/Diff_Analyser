package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.AssembledChronology;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.LenientChronology;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class EthiopicChronology_ESTest extends EthiopicChronology_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testIsLeapDayForNonLeapDay() {
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance((DateTimeZone) null);
        boolean isLeapDay = ethiopicChronology.isLeapDay(-56623968000000L);
        assertFalse(isLeapDay);
    }

    @Test(timeout = 4000)
    public void testAssembleFields() {
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstanceUTC();
        AssembledChronology.Fields fields = new AssembledChronology.Fields();
        ethiopicChronology.assemble(fields);
        assertEquals(1, EthiopicChronology.EE);
    }

    @Test(timeout = 4000)
    public void testGetMaxYear() {
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstanceUTC();
        int maxYear = ethiopicChronology.getMaxYear();
        assertEquals(292272984, maxYear);
    }

    @Test(timeout = 4000)
    public void testGetApproxMillisAtEpochDividedByTwo() {
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance();
        long approxMillis = ethiopicChronology.getApproxMillisAtEpochDividedByTwo();
        assertEquals(30962844000000L, approxMillis);
    }

    @Test(timeout = 4000)
    public void testIsLeapDayWithIllegalArgumentException() {
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstanceUTC();
        try {
            ethiopicChronology.isLeapDay(Long.MIN_VALUE);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.chrono.LimitChronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testIsLeapDayWithArithmeticException() {
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(-4246);
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance(dateTimeZone);
        try {
            ethiopicChronology.isLeapDay(Long.MIN_VALUE);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.DateTimeZone", e);
        }
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYearMillisForFutureYear() {
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstanceUTC();
        long firstDayMillis = ethiopicChronology.calculateFirstDayOfYearMillis(15271875);
        assertEquals(481881796790400000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYearMillisForYearOne() {
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance();
        long firstDayMillis = ethiopicChronology.calculateFirstDayOfYearMillis(1);
        assertEquals(-61894108800000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testGetInstanceWithOffsetMillis() {
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(-1962);
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance(dateTimeZone, 1);
        assertEquals(1, EthiopicChronology.EE);
    }

    @Test(timeout = 4000)
    public void testGetInstanceWithInvalidMinDaysInFirstWeek() {
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(1900);
        try {
            EthiopicChronology.getInstance(dateTimeZone, -4898);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.chrono.EthiopicChronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetInstanceWithInvalidMinDaysInFirstWeekNullZone() {
        try {
            EthiopicChronology.getInstance((DateTimeZone) null, 1767);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.chrono.EthiopicChronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithInvalidMinDaysInFirstWeek() {
        try {
            new EthiopicChronology((Chronology) null, (Object) null, -159);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.chrono.BasicChronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testLenientChronologyInstance() {
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(-1962);
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance(dateTimeZone);
        LenientChronology lenientChronology = LenientChronology.getInstance(ethiopicChronology);
        Object param = new Object();
        EthiopicChronology ethiopicChronologyWithLenient = new EthiopicChronology(lenientChronology, param, 1);
        assertFalse(ethiopicChronologyWithLenient.equals(ethiopicChronology));
    }

    @Test(timeout = 4000)
    public void testIsLeapDayForLeapDay() {
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(-833);
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance(dateTimeZone);
        boolean isLeapDay = ethiopicChronology.isLeapDay(3461702400000L);
        assertTrue(isLeapDay);
    }

    @Test(timeout = 4000)
    public void testIsLeapDayForNonLeapDayWithOffset() {
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(-2263);
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance(dateTimeZone);
        boolean isLeapDay = ethiopicChronology.isLeapDay(1209600011L);
        assertFalse(isLeapDay);
    }

    @Test(timeout = 4000)
    public void testWithZoneReturnsSameInstance() {
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance((DateTimeZone) null);
        Chronology chronologyWithZone = ethiopicChronology.withZone((DateTimeZone) null);
        assertSame(ethiopicChronology, chronologyWithZone);
    }

    @Test(timeout = 4000)
    public void testGetMinYear() {
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(-1962);
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance(dateTimeZone);
        int minYear = ethiopicChronology.getMinYear();
        assertEquals(-292269337, minYear);
    }

    @Test(timeout = 4000)
    public void testIsLeapDayForNegativeInstant() {
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(-1962);
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance(dateTimeZone);
        boolean isLeapDay = ethiopicChronology.isLeapDay(-2899);
        assertFalse(isLeapDay);
    }

    @Test(timeout = 4000)
    public void testWithZoneUTC() {
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance();
        DateTimeZone dateTimeZone = DateTimeZone.UTC;
        EthiopicChronology ethiopicChronologyWithZone = (EthiopicChronology) ethiopicChronology.withZone(dateTimeZone);
        assertEquals(1, EthiopicChronology.EE);
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYearMillisForMaxYear() {
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstanceUTC();
        long firstDayMillis = ethiopicChronology.calculateFirstDayOfYearMillis(292272984);
        assertEquals(9223371994233600000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testWithUTC() {
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(-1962);
        EthiopicChronology ethiopicChronology = EthiopicChronology.getInstance(dateTimeZone);
        Chronology chronologyWithUTC = ethiopicChronology.withUTC();
        assertNotSame(chronologyWithUTC, ethiopicChronology);
    }
}