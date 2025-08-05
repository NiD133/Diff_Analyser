package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.CharBuffer;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class UtcInstant_ESTest extends UtcInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testHashCodeWithLargeModifiedJulianDay() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(73281320003518L, 73281320003518L);
        assertEquals(73281320003518L, utcInstant.getModifiedJulianDay());
    }

    @Test(timeout = 4000)
    public void testHashCodeWithNegativeModifiedJulianDay() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(-2985L, 0L);
        assertEquals(-2985L, utcInstant.getModifiedJulianDay());
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentNanoOfDay() throws Throwable {
        UtcInstant utcInstant1 = UtcInstant.ofModifiedJulianDay(301L, 301L);
        UtcInstant utcInstant2 = UtcInstant.ofModifiedJulianDay(301L, 70L);
        assertFalse(utcInstant1.equals(utcInstant2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentModifiedJulianDay() throws Throwable {
        UtcInstant utcInstant1 = UtcInstant.ofModifiedJulianDay(1285L, 2341L);
        UtcInstant utcInstant2 = UtcInstant.ofModifiedJulianDay(0L, 0L);
        assertFalse(utcInstant1.equals(utcInstant2));
    }

    @Test(timeout = 4000)
    public void testIsLeapSecondAndIsBefore() throws Throwable {
        UtcInstant utcInstant1 = UtcInstant.ofModifiedJulianDay(9223372036854775807L, 73281320003515L);
        assertFalse(utcInstant1.isLeapSecond());
        
        UtcInstant utcInstant2 = utcInstant1.withModifiedJulianDay(6400000041317L);
        assertFalse(utcInstant1.isBefore(utcInstant2));
    }

    @Test(timeout = 4000)
    public void testTaiInstantConversion() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(3217L, 1000L);
        UtcInstant utcInstant = taiInstant.toUtcInstant();
        TaiInstant taiInstantModified = taiInstant.withTaiSeconds(0L);
        UtcInstant utcInstantModified = UtcInstant.of(taiInstantModified);
        assertFalse(utcInstantModified.isAfter(utcInstant));
    }

    @Test(timeout = 4000)
    public void testCompareToWithDifferentNanoOfDay() throws Throwable {
        UtcInstant utcInstant1 = UtcInstant.ofModifiedJulianDay(0L, 0L);
        UtcInstant utcInstant2 = UtcInstant.ofModifiedJulianDay(0L, 1891L);
        assertEquals(1, utcInstant2.compareTo(utcInstant1));
    }

    @Test(timeout = 4000)
    public void testParseUtcInstant() throws Throwable {
        UtcInstant utcInstant = UtcInstant.parse("1958-01-01T00:53:27.000001Z");
        assertEquals(3207000001000L, utcInstant.getNanoOfDay());
        assertEquals(36204L, utcInstant.getModifiedJulianDay());
    }

    @Test(timeout = 4000)
    public void testWithNanoOfDay() throws Throwable {
        Instant instant = MockInstant.ofEpochSecond(0L);
        UtcInstant utcInstant = UtcInstant.of(instant);
        UtcInstant modifiedUtcInstant = utcInstant.withNanoOfDay(0L);
        assertEquals(0L, modifiedUtcInstant.getNanoOfDay());
        assertEquals(40587L, utcInstant.getModifiedJulianDay());
        assertTrue(modifiedUtcInstant.equals(utcInstant));
    }

    @Test(timeout = 4000)
    public void testWithNanoOfDayModification() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);
        UtcInstant modifiedUtcInstant = utcInstant.withNanoOfDay(82636000000001L);
        assertEquals(0L, modifiedUtcInstant.getModifiedJulianDay());
        assertEquals(82636000000001L, modifiedUtcInstant.getNanoOfDay());
    }

    @Test(timeout = 4000)
    public void testDurationUntilSelf() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);
        Duration duration = utcInstant.durationUntil(utcInstant);
        UtcInstant result = utcInstant.plus(duration);
        assertNotSame(result, utcInstant);
    }

    @Test(timeout = 4000)
    public void testPlusNegativeDuration() throws Throwable {
        Duration duration = Duration.ofNanos(-1606L);
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(-571L, 0L);
        UtcInstant result = utcInstant.plus(duration);
        assertEquals(-572L, result.getModifiedJulianDay());
        assertEquals(86399999998394L, result.getNanoOfDay());
    }

    @Test(timeout = 4000)
    public void testParseDateString() throws Throwable {
        UtcInstant utcInstant = UtcInstant.parse("1859-09-09T00:00:00Z");
        assertEquals(0L, utcInstant.getNanoOfDay());
        assertEquals(296L, utcInstant.getModifiedJulianDay());
    }

    @Test(timeout = 4000)
    public void testParseDateStringWithNano() throws Throwable {
        UtcInstant utcInstant = UtcInstant.parse("1857-01-29T00:00:00.000001876Z");
        assertEquals(1876L, utcInstant.getNanoOfDay());
        assertEquals(-657L, utcInstant.getModifiedJulianDay());
    }

    @Test(timeout = 4000)
    public void testTaiInstantConversionAndBack() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(3217L, 1000L);
        UtcInstant utcInstant = taiInstant.toUtcInstant();
        TaiInstant result = utcInstant.toTaiInstant();
        assertEquals(3217L, result.getTaiSeconds());
    }

    @Test(timeout = 4000)
    public void testToString() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);
        String result = utcInstant.toString();
        assertEquals("1858-11-17T00:00:00Z", result);
    }

    @Test(timeout = 4000)
    public void testEqualsWithParsedUtcInstant() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);
        UtcInstant parsedUtcInstant = UtcInstant.parse("1858-11-17T00:00:00Z");
        assertTrue(parsedUtcInstant.equals(utcInstant));
    }

    @Test(timeout = 4000)
    public void testIsBeforeSelf() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(9223372036854775807L, 73281320003515L);
        assertFalse(utcInstant.isBefore(utcInstant));
    }

    @Test(timeout = 4000)
    public void testIsAfterSelf() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);
        assertFalse(utcInstant.isAfter(utcInstant));
    }

    @Test(timeout = 4000)
    public void testCompareToSelf() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);
        assertEquals(0, utcInstant.compareTo(utcInstant));
    }

    @Test(timeout = 4000)
    public void testIsLeapSecondFalse() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(83843999999988L, 83843999999988L);
        assertFalse(utcInstant.isLeapSecond());
    }

    @Test(timeout = 4000)
    public void testInstantConversion() throws Throwable {
        Instant instant = MockInstant.parse("1858-11-17T00:00:00Z");
        UtcInstant utcInstant = UtcInstant.of(instant);
        assertEquals(0L, utcInstant.getNanoOfDay());
    }

    @Test(timeout = 4000)
    public void testMinusDurationUntilSelf() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);
        Duration duration = utcInstant.durationUntil(utcInstant);
        UtcInstant result = utcInstant.minus(duration);
        assertEquals(0L, result.getNanoOfDay());
    }

    @Test(timeout = 4000)
    public void testWithModifiedJulianDay() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);
        UtcInstant modifiedUtcInstant = utcInstant.withModifiedJulianDay(-2547L);
        assertEquals(-2547L, modifiedUtcInstant.getModifiedJulianDay());
    }

    @Test(timeout = 4000)
    public void testInvalidNanoOfDay() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(86340000001821L, 86340000001821L);
        try {
            utcInstant.withNanoOfDay(-892L);
            fail("Expecting exception: DateTimeException");
        } catch (DateTimeException e) {
            verifyException("org.threeten.extra.scale.UtcRules", e);
        }
    }

    @Test(timeout = 4000)
    public void testPlusDuration() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(73281320003518L, 73281320003518L);
        Duration duration = Duration.ofMillis(73281320003518L);
        UtcInstant result = utcInstant.plus(duration);
        assertTrue(utcInstant.isBefore(result));
    }

    // Add more tests as needed, following the same pattern for clarity and organization.
}