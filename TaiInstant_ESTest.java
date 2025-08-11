package org.threeten.extra.scale;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class TaiInstant_ESTest extends TaiInstant_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testTaiInstantConversionToUtcInstant() throws Throwable {
        Instant mockInstant = MockInstant.now();
        TaiInstant taiInstant = TaiInstant.of(mockInstant);
        UtcInstant utcInstant = UtcInstant.of(taiInstant);
        UtcInstant modifiedUtcInstant = utcInstant.withModifiedJulianDay(0L);
        TaiInstant modifiedTaiInstant = TaiInstant.of(modifiedUtcInstant);

        assertEquals(73281320000000L, modifiedUtcInstant.getNanoOfDay());
        assertEquals(1771100516L, taiInstant.getTaiSeconds());
        assertEquals(modifiedTaiInstant.hashCode(), modifiedTaiInstant.hashCode());
    }

    @Test(timeout = 4000)
    public void testTaiInstantEqualityWithDifferentNano() throws Throwable {
        TaiInstant taiInstant1 = TaiInstant.ofTaiSeconds(37L, 37L);
        TaiInstant taiInstant2 = TaiInstant.ofTaiSeconds(37L, 3503L);

        assertFalse(taiInstant1.equals(taiInstant2));
        assertEquals(37L, taiInstant2.getTaiSeconds());
        assertEquals(3503, taiInstant2.getNano());
    }

    @Test(timeout = 4000)
    public void testTaiInstantInequalityWithDifferentUtcInstant() throws Throwable {
        Instant mockInstant = MockInstant.now();
        TaiInstant taiInstant = TaiInstant.of(mockInstant);
        UtcInstant utcInstant = UtcInstant.of(taiInstant);
        UtcInstant modifiedUtcInstant = utcInstant.withModifiedJulianDay(0L);
        TaiInstant modifiedTaiInstant = TaiInstant.of(modifiedUtcInstant);

        assertFalse(taiInstant.equals(modifiedTaiInstant));
        assertEquals(73281320000000L, modifiedUtcInstant.getNanoOfDay());
    }

    @Test(timeout = 4000)
    public void testTaiInstantMinusDuration() throws Throwable {
        Instant mockInstant = MockInstant.now();
        TaiInstant taiInstant = TaiInstant.of(mockInstant);
        Duration duration = Duration.ofHours(-3507L);
        TaiInstant resultTaiInstant = taiInstant.minus(duration);

        assertEquals(320000000, resultTaiInstant.getNano());
        assertEquals(1783725716L, resultTaiInstant.getTaiSeconds());
        assertFalse(resultTaiInstant.isBefore(taiInstant));
    }

    @Test(timeout = 4000)
    public void testTaiInstantWithTaiSeconds() throws Throwable {
        Instant mockInstant = MockInstant.now();
        TaiInstant taiInstant = TaiInstant.of(mockInstant);
        TaiInstant modifiedTaiInstant = taiInstant.withTaiSeconds(1000000000L);

        assertEquals(320000000, taiInstant.getNano());
        assertEquals(1000000000L, modifiedTaiInstant.getTaiSeconds());
    }

    @Test(timeout = 4000)
    public void testTaiInstantDurationUntilSelf() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(0L, 0L);
        Duration duration = taiInstant.durationUntil(taiInstant);

        assertEquals(Duration.ZERO, duration);
        assertEquals(0, taiInstant.getNano());
    }

    @Test(timeout = 4000)
    public void testTaiInstantMinusSmallDuration() throws Throwable {
        Instant mockInstant = MockInstant.ofEpochSecond(36204L, -1L);
        TaiInstant taiInstant = TaiInstant.of(mockInstant);
        Duration duration = Duration.ofMillis(-1L);
        TaiInstant resultTaiInstant = taiInstant.minus(duration);

        assertEquals(378727414L, resultTaiInstant.getTaiSeconds());
        assertEquals(999999, resultTaiInstant.getNano());
    }

    @Test(timeout = 4000)
    public void testTaiInstantPlusDuration() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(1000L, 1000L);
        Duration duration = Duration.ofSeconds(1000L, 1000L);
        TaiInstant resultTaiInstant = taiInstant.plus(duration);

        assertEquals(2000L, resultTaiInstant.getTaiSeconds());
        assertEquals(2000, resultTaiInstant.getNano());
    }

    @Test(timeout = 4000)
    public void testTaiInstantWithInvalidNano() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(50L, 50L);
        try {
            taiInstant.withNano(1000000000);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.threeten.extra.scale.TaiInstant", e);
        }
    }

    @Test(timeout = 4000)
    public void testTaiInstantWithValidNano() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(0L, 0L);
        TaiInstant resultTaiInstant = taiInstant.withNano(91);

        assertEquals(0L, resultTaiInstant.getTaiSeconds());
        assertEquals(91, resultTaiInstant.getNano());
    }

    @Test(timeout = 4000)
    public void testTaiInstantWithNegativeTaiSeconds() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(124934400L, -1909L);
        TaiInstant modifiedTaiInstant = taiInstant.withTaiSeconds(-1909L);
        TaiInstant finalTaiInstant = modifiedTaiInstant.withNano(331);

        assertEquals(-1909L, finalTaiInstant.getTaiSeconds());
        assertEquals(331, finalTaiInstant.getNano());
    }

    @Test(timeout = 4000)
    public void testTaiInstantWithZeroNano() throws Throwable {
        Instant mockInstant = MockInstant.ofEpochSecond(36204L, -1L);
        TaiInstant taiInstant = TaiInstant.of(mockInstant);
        TaiInstant modifiedTaiInstant = taiInstant.withNano(0);

        assertEquals(0, modifiedTaiInstant.getNano());
        assertEquals(378727413L, modifiedTaiInstant.getTaiSeconds());
    }

    @Test(timeout = 4000)
    public void testUtcInstantConversionToTaiInstant() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(0L, 0L);
        TaiInstant taiInstant = TaiInstant.of(utcInstant);
        UtcInstant resultUtcInstant = taiInstant.toUtcInstant();

        assertEquals(0L, resultUtcInstant.getNanoOfDay());
        assertEquals(0, taiInstant.getNano());
    }

    @Test(timeout = 4000)
    public void testUtcInstantConversionWithModifiedJulianDay() throws Throwable {
        Instant mockInstant = MockInstant.now();
        TaiInstant taiInstant = TaiInstant.of(mockInstant);
        UtcInstant utcInstant = UtcInstant.of(taiInstant);
        UtcInstant modifiedUtcInstant = utcInstant.withModifiedJulianDay(0L);
        TaiInstant modifiedTaiInstant = TaiInstant.of(modifiedUtcInstant);
        UtcInstant resultUtcInstant = modifiedTaiInstant.toUtcInstant();

        assertEquals(73281320000000L, modifiedUtcInstant.getNanoOfDay());
        assertTrue(resultUtcInstant.equals(modifiedUtcInstant));
    }

    @Test(timeout = 4000)
    public void testUtcInstantConversionWithNegativeJulianDay() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(774L, 1L);
        UtcInstant modifiedUtcInstant = utcInstant.withModifiedJulianDay(-2L);
        TaiInstant taiInstant = TaiInstant.of(modifiedUtcInstant);
        UtcInstant resultUtcInstant = taiInstant.toUtcInstant();

        assertEquals(1L, resultUtcInstant.getNanoOfDay());
        assertEquals(1, taiInstant.getNano());
    }

    @Test(timeout = 4000)
    public void testTaiInstantPlusZeroDuration() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(0L, 0L);
        Duration duration = Duration.ZERO;
        TaiInstant resultTaiInstant = taiInstant.plus(duration);

        assertEquals(0, resultTaiInstant.getNano());
        assertSame(resultTaiInstant, taiInstant);
    }

    @Test(timeout = 4000)
    public void testTaiInstantPlusNegativeDuration() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(41316L, -1L);
        Duration duration = Duration.ofDays(41317L);
        Duration multipliedDuration = duration.multipliedBy(-187L);
        TaiInstant resultTaiInstant = taiInstant.plus(multipliedDuration);

        assertEquals(-667550464285L, resultTaiInstant.getTaiSeconds());
        assertEquals(999999999, taiInstant.getNano());
    }

    @Test(timeout = 4000)
    public void testTaiInstantConversionToUtcInstantAndBack() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(0L, 0L);
        UtcInstant utcInstant = taiInstant.toUtcInstant();
        TaiInstant resultTaiInstant = TaiInstant.of(utcInstant);

        assertEquals(0, resultTaiInstant.getNano());
    }

    @Test(timeout = 4000)
    public void testTaiInstantMinusLargeDuration() throws Throwable {
        Duration duration = Duration.ofDays(41317L);
        Clock clock = MockClock.systemUTC();
        Instant mockInstant = MockInstant.now(clock);
        Instant subtractedInstant = MockInstant.minus(mockInstant, (TemporalAmount) duration);
        TaiInstant taiInstant = TaiInstant.of(subtractedInstant);

        assertEquals(-1798688309L, taiInstant.getTaiSeconds());
        assertEquals(320000000, taiInstant.getNano());
    }

    @Test(timeout = 4000)
    public void testUtcInstantToInstantConversion() throws Throwable {
        UtcInstant utcInstant = UtcInstant.ofModifiedJulianDay(1000000000L, 1000000000L);
        Instant instant = utcInstant.toInstant();
        TaiInstant taiInstant = TaiInstant.of(instant);

        assertEquals(86396871974438L, taiInstant.getTaiSeconds());
        assertEquals(0, taiInstant.getNano());
    }

    @Test(timeout = 4000)
    public void testTaiInstantMinusZeroDuration() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(0L, 0L);
        Duration duration = Duration.ofSeconds(0L);
        TaiInstant resultTaiInstant = taiInstant.minus(duration);

        assertSame(resultTaiInstant, taiInstant);
        assertEquals(0, resultTaiInstant.getNano());
    }

    @Test(timeout = 4000)
    public void testTaiInstantWithNegativeTaiSecondsAndZeroDuration() throws Throwable {
        Instant mockInstant = MockInstant.now();
        TaiInstant taiInstant = TaiInstant.of(mockInstant);
        Duration duration = Duration.ZERO;
        TaiInstant modifiedTaiInstant = taiInstant.withTaiSeconds(-2L);
        TaiInstant resultTaiInstant = modifiedTaiInstant.minus(duration);

        assertEquals(-2L, resultTaiInstant.getTaiSeconds());
        assertSame(resultTaiInstant, modifiedTaiInstant);
    }

    @Test(timeout = 4000)
    public void testTaiInstantGetTaiSeconds() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(0L, 0L);
        long taiSeconds = taiInstant.getTaiSeconds();

        assertEquals(0L, taiSeconds);
    }

    @Test(timeout = 4000)
    public void testTaiInstantGetTaiSecondsFromInstant() throws Throwable {
        Instant mockInstant = MockInstant.now();
        TaiInstant taiInstant = TaiInstant.of(mockInstant);
        long taiSeconds = taiInstant.getTaiSeconds();

        assertEquals(1771100516L, taiSeconds);
    }

    @Test(timeout = 4000)
    public void testTaiInstantGetTaiSecondsFromModifiedUtcInstant() throws Throwable {
        Instant mockInstant = MockInstant.now();
        TaiInstant taiInstant = TaiInstant.of(mockInstant);
        UtcInstant utcInstant = UtcInstant.of(taiInstant);
        UtcInstant modifiedUtcInstant = utcInstant.withModifiedJulianDay(0L);
        TaiInstant modifiedTaiInstant = TaiInstant.of(modifiedUtcInstant);
        long taiSeconds = modifiedTaiInstant.getTaiSeconds();

        assertEquals(-3127952309L, taiSeconds);
    }

    @Test(timeout = 4000)
    public void testTaiInstantGetNano() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(1L, 0L);
        int nano = taiInstant.getNano();

        assertEquals(0, nano);
    }

    @Test(timeout = 4000)
    public void testTaiInstantGetNanoFromInstant() throws Throwable {
        Instant mockInstant = MockInstant.now();
        TaiInstant taiInstant = TaiInstant.of(mockInstant);
        int nano = taiInstant.getNano();

        assertEquals(320000000, nano);
    }

    @Test(timeout = 4000)
    public void testTaiInstantCompareTo() throws Throwable {
        Instant mockInstant = MockInstant.now();
        TaiInstant taiInstant = TaiInstant.of(mockInstant);
        TaiInstant modifiedTaiInstant = taiInstant.withTaiSeconds(-2L);
        int comparisonResult = modifiedTaiInstant.compareTo(taiInstant);

        assertEquals(-1, comparisonResult);
    }

    @Test(timeout = 4000)
    public void testTaiInstantCompareToSelf() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(0L, 0L);
        int comparisonResult = taiInstant.compareTo(taiInstant);

        assertEquals(0, comparisonResult);
    }

    @Test(timeout = 4000)
    public void testTaiInstantCompareToModifiedUtcInstant() throws Throwable {
        Instant mockInstant = MockInstant.now();
        TaiInstant taiInstant = TaiInstant.of(mockInstant);
        UtcInstant utcInstant = UtcInstant.of(taiInstant);
        UtcInstant modifiedUtcInstant = utcInstant.withModifiedJulianDay(0L);
        TaiInstant modifiedTaiInstant = TaiInstant.of(modifiedUtcInstant);
        int comparisonResult = taiInstant.compareTo(modifiedTaiInstant);

        assertEquals(1, comparisonResult);
    }

    @Test(timeout = 4000)
    public void testTaiInstantEqualityWithDifferentTaiSeconds() throws Throwable {
        TaiInstant taiInstant1 = TaiInstant.ofTaiSeconds(0L, 0L);
        TaiInstant taiInstant2 = taiInstant1.withTaiSeconds(5495L);

        assertFalse(taiInstant1.equals(taiInstant2));
    }

    @Test(timeout = 4000)
    public void testTaiInstantEqualityWithDifferentObject() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(19L, 19L);
        Object otherObject = new Object();

        assertFalse(taiInstant.equals(otherObject));
    }

    @Test(timeout = 4000)
    public void testTaiInstantEqualityWithSelf() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(0L, 0L);

        assertTrue(taiInstant.equals(taiInstant));
    }

    @Test(timeout = 4000)
    public void testTaiInstantIsBefore() throws Throwable {
        TaiInstant taiInstant1 = TaiInstant.ofTaiSeconds(37L, 37L);
        TaiInstant taiInstant2 = TaiInstant.ofTaiSeconds(37L, 3503L);

        assertTrue(taiInstant1.isBefore(taiInstant2));
    }

    @Test(timeout = 4000)
    public void testTaiInstantIsBeforeSelf() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(0L, 0L);

        assertFalse(taiInstant.isBefore(taiInstant));
    }

    @Test(timeout = 4000)
    public void testTaiInstantIsAfter() throws Throwable {
        TaiInstant taiInstant1 = TaiInstant.ofTaiSeconds(37L, 37L);
        TaiInstant taiInstant2 = TaiInstant.ofTaiSeconds(37L, 3503L);

        assertTrue(taiInstant2.isAfter(taiInstant1));
    }

    @Test(timeout = 4000)
    public void testTaiInstantIsAfterWithInvalidNano() throws Throwable {
        TaiInstant taiInstant1 = TaiInstant.ofTaiSeconds(50L, 50L);
        TaiInstant taiInstant2 = TaiInstant.ofTaiSeconds(-1L, 1000000000L);

        assertFalse(taiInstant2.isAfter(taiInstant1));
    }

    @Test(timeout = 4000)
    public void testTaiInstantIsAfterSelf() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(37L, 37L);

        assertFalse(taiInstant.isAfter(taiInstant));
    }

    @Test(timeout = 4000)
    public void testTaiInstantPlusLargeDuration() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(1000000000L, 1000000000L);
        ChronoUnit chronoUnit = ChronoUnit.FOREVER;
        Duration duration = chronoUnit.getDuration();

        try {
            taiInstant.plus(duration);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("java.lang.Math", e);
        }
    }

    @Test(timeout = 4000)
    public void testTaiInstantEqualityWithModifiedNano() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(124934400L, -1909L);
        TaiInstant modifiedTaiInstant = taiInstant.withNano(331);

        assertFalse(taiInstant.equals(modifiedTaiInstant));
    }

    @Test(timeout = 4000)
    public void testTaiInstantWithInvalidNanoHigh() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(-604800L, -604800L);

        try {
            taiInstant.withNano(2147424333);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.threeten.extra.scale.TaiInstant", e);
        }
    }

    @Test(timeout = 4000)
    public void testTaiInstantWithInvalidNanoLow() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(0L, 0L);

        try {
            taiInstant.withNano(-4339);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.threeten.extra.scale.TaiInstant", e);
        }
    }

    @Test(timeout = 4000)
    public void testTaiInstantParseInvalidFormat() throws Throwable {
        try {
            TaiInstant.parse("0.000000000s(TAI)");
            // fail("Expecting exception: IllegalStateException");
            // Unstable assertion
        } catch (IllegalStateException e) {
            verifyException("java.util.regex.Matcher", e);
        }
    }

    @Test(timeout = 4000)
    public void testTaiInstantParseInvalidText() throws Throwable {
        try {
            TaiInstant.parse("0.00000000s(TAI)");
            fail("Expecting exception: DateTimeParseException");
        } catch (DateTimeParseException e) {
            verifyException("org.threeten.extra.scale.TaiInstant", e);
        }
    }

    @Test(timeout = 4000)
    public void testTaiInstantEqualityWithSameTaiSeconds() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(0L, 0L);
        TaiInstant resultTaiInstant = taiInstant.withTaiSeconds(0L);

        assertTrue(taiInstant.equals(resultTaiInstant));
    }

    @Test(timeout = 4000)
    public void testTaiInstantConversionToInstantAndBack() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(0L, 0L);
        Instant instant = taiInstant.toInstant();
        TaiInstant resultTaiInstant = TaiInstant.of(instant);

        assertEquals(0, resultTaiInstant.getNano());
    }

    @Test(timeout = 4000)
    public void testTaiInstantToString() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(1L, 0L);
        String stringRepresentation = taiInstant.toString();

        assertEquals("1.000000000s(TAI)", stringRepresentation);
    }

    @Test(timeout = 4000)
    public void testTaiInstantConversionToUtcInstantAndBack() throws Throwable {
        TaiInstant taiInstant = TaiInstant.ofTaiSeconds(1L, 0L);
        UtcInstant utcInstant = taiInstant.toUtcInstant();
        TaiInstant resultTaiInstant = TaiInstant.of(utcInstant);

        assertEquals(1L, taiInstant.getTaiSeconds());
    }
}