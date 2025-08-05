package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class Minutes_ESTest extends Minutes_ESTest_scaffolding {

    private static final int MAX_INT = Integer.MAX_VALUE;
    private static final int MIN_INT = Integer.MIN_VALUE;

    @Test(timeout = 4000)
    public void testMaxValueIsNotLessThanTwoMinutes() {
        Minutes maxMinutes = Minutes.MAX_VALUE;
        Minutes twoMinutes = Minutes.TWO;
        assertFalse(maxMinutes.isLessThan(twoMinutes));
    }

    @Test(timeout = 4000)
    public void testZeroMinutesIsNotLessThanNull() {
        Minutes zeroMinutes = Minutes.ZERO;
        assertFalse(zeroMinutes.isLessThan(null));
    }

    @Test(timeout = 4000)
    public void testTwoMinutesIsNotGreaterThanThreeMinutes() {
        Minutes twoMinutes = Minutes.TWO;
        Minutes threeMinutes = Minutes.THREE;
        assertFalse(twoMinutes.isGreaterThan(threeMinutes));
    }

    @Test(timeout = 4000)
    public void testMinutesInNullIntervalIsNotGreaterThanNull() {
        Minutes minutesInNullInterval = Minutes.minutesIn(null);
        assertFalse(minutesInNullInterval.isGreaterThan(null));
    }

    @Test(timeout = 4000)
    public void testMaxValueToStandardWeeks() {
        Minutes maxMinutes = Minutes.MAX_VALUE;
        Weeks weeks = maxMinutes.toStandardWeeks();
        assertEquals(213044, weeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testMinValueToStandardWeeks() {
        Minutes minMinutes = Minutes.MIN_VALUE;
        Weeks weeks = minMinutes.toStandardWeeks();
        assertEquals(-213044, weeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testZeroMinutesToStandardSeconds() {
        Minutes zeroMinutes = Minutes.ZERO;
        Seconds seconds = zeroMinutes.toStandardSeconds();
        assertEquals(0, seconds.getSeconds());
    }

    @Test(timeout = 4000)
    public void testOneMinuteToStandardSeconds() {
        Minutes oneMinute = Minutes.ONE;
        Seconds seconds = oneMinute.toStandardSeconds();
        assertEquals(60, seconds.getSeconds());
    }

    @Test(timeout = 4000)
    public void testMinutesToStandardHours() {
        Minutes minutes = Minutes.minutes(1431);
        Hours hours = minutes.toStandardHours();
        assertEquals(1431, minutes.getMinutes());
        assertEquals(23, hours.getHours());
    }

    @Test(timeout = 4000)
    public void testMaxValueToStandardDuration() {
        Minutes maxMinutes = Minutes.MAX_VALUE;
        Duration duration = maxMinutes.toStandardDuration();
        assertEquals(1491308L, duration.getStandardDays());
    }

    @Test(timeout = 4000)
    public void testOneMinuteToStandardDuration() {
        Minutes oneMinute = Minutes.ONE;
        Duration duration = oneMinute.toStandardDuration();
        assertEquals(60000L, duration.getMillis());
    }

    @Test(timeout = 4000)
    public void testMinValueToStandardDuration() {
        Minutes minMinutes = Minutes.MIN_VALUE;
        Duration duration = minMinutes.toStandardDuration();
        assertEquals((long) MIN_INT, duration.getStandardMinutes());
    }

    @Test(timeout = 4000)
    public void testMaxValueToStandardDays() {
        Minutes maxMinutes = Minutes.MAX_VALUE;
        Days days = maxMinutes.toStandardDays();
        assertEquals(1491308, days.getDays());
    }

    @Test(timeout = 4000)
    public void testZeroMinutesPlusNull() {
        Minutes zeroMinutes = Minutes.ZERO;
        Minutes result = zeroMinutes.plus((Minutes) null);
        assertSame(zeroMinutes, result);
    }

    @Test(timeout = 4000)
    public void testMaxValueDividedByNegative() {
        Seconds maxSeconds = Seconds.MAX_VALUE;
        Minutes maxMinutes = maxSeconds.toStandardMinutes();
        Minutes dividedMinutes = maxMinutes.dividedBy(-82);
        Minutes doubledMinutes = dividedMinutes.plus(dividedMinutes);
        assertEquals(-26188824, dividedMinutes.getMinutes());
        assertEquals(-52377648, doubledMinutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testTwoMinutesPlusNegativeTwo() {
        Minutes twoMinutes = Minutes.TWO;
        Minutes result = twoMinutes.plus(-2);
        assertEquals(0, result.getMinutes());
    }

    @Test(timeout = 4000)
    public void testZeroMinutesPlusNegative() {
        Minutes zeroMinutes = Minutes.ZERO;
        Minutes result = zeroMinutes.plus(-2104);
        assertEquals(-2104, result.getMinutes());
    }

    @Test(timeout = 4000)
    public void testMinutesInNullIntervalNegated() {
        Minutes minutesInNullInterval = Minutes.minutesIn(null);
        Minutes negatedMinutes = minutesInNullInterval.negated();
        assertEquals(0, negatedMinutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testThreeMinutesNegated() {
        Minutes threeMinutes = Minutes.THREE;
        Minutes negatedMinutes = threeMinutes.negated();
        assertEquals(-3, negatedMinutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testMinutesInNullIntervalMultipliedBy() {
        Minutes minutesInNullInterval = Minutes.minutesIn(null);
        Minutes multipliedMinutes = minutesInNullInterval.multipliedBy(1995);
        assertEquals(0, multipliedMinutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testMinutesMultipliedBy() {
        Minutes minutes = Minutes.minutes(1431);
        Minutes multipliedMinutes = minutes.multipliedBy(3012);
        assertEquals(1431, minutes.getMinutes());
        assertEquals(4310172, multipliedMinutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testOneMinuteMultipliedByNegative() {
        Minutes oneMinute = Minutes.ONE;
        Minutes multipliedMinutes = oneMinute.multipliedBy(-5447);
        assertEquals(-5447, multipliedMinutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testOneMinusZeroMinutes() {
        Minutes oneMinute = Minutes.ONE;
        Minutes zeroMinutes = Minutes.ZERO;
        Minutes result = oneMinute.minus(zeroMinutes);
        assertEquals(1, result.getMinutes());
    }

    @Test(timeout = 4000)
    public void testTwoMinusThreeMinutes() {
        Minutes twoMinutes = Minutes.TWO;
        Minutes threeMinutes = Minutes.THREE;
        Minutes result = twoMinutes.minus(threeMinutes);
        assertEquals(-1, result.getMinutes());
    }

    @Test(timeout = 4000)
    public void testZeroMinusZeroMinutes() {
        Minutes zeroMinutes = Minutes.ZERO;
        Minutes result = zeroMinutes.minus(0);
        assertEquals(0, result.getMinutes());
    }

    @Test(timeout = 4000)
    public void testOneMinusNegative() {
        Minutes oneMinute = Minutes.ONE;
        Minutes result = oneMinute.minus(-241);
        assertEquals(242, result.getMinutes());
    }

    @Test(timeout = 4000)
    public void testOneMinusLargeNumber() {
        Minutes oneMinute = Minutes.ONE;
        Minutes result = oneMinute.minus(192);
        assertEquals(-191, result.getMinutes());
    }

    @Test(timeout = 4000)
    public void testMinutesInNullInterval() {
        Minutes minutesInNullInterval = Minutes.minutesIn(null);
        assertEquals(0, minutesInNullInterval.getMinutes());
    }

    @Test(timeout = 4000)
    public void testMaxSecondsToStandardMinutes() {
        Seconds maxSeconds = Seconds.MAX_VALUE;
        Minutes minutes = maxSeconds.toStandardMinutes();
        assertEquals(35791394, minutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testThreeMinutesDividedByTwo() {
        Minutes threeMinutes = Minutes.THREE;
        Minutes result = threeMinutes.dividedBy(2);
        assertEquals(1, result.getMinutes());
    }

    @Test(timeout = 4000)
    public void testMaxValueDividedByNegativeAndNegated() {
        Minutes maxMinutes = Minutes.MAX_VALUE;
        Minutes dividedMinutes = maxMinutes.dividedBy(-82);
        dividedMinutes.negated();
        assertEquals(-26188824, dividedMinutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testMinValueToStandardSecondsThrowsException() {
        Minutes minMinutes = Minutes.MIN_VALUE;
        try {
            minMinutes.toStandardSeconds();
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testDaysToStandardMinutes() {
        Days threeDays = Days.THREE;
        Minutes minutes = Minutes.standardMinutesIn(threeDays);
        assertEquals(4320, minutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testMaxValuePlusMaxValueThrowsException() {
        Minutes maxMinutes = Minutes.MAX_VALUE;
        try {
            maxMinutes.plus(maxMinutes);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testMinutesBetweenSamePartial() {
        DateTimeFieldType[] fieldTypeArray = new DateTimeFieldType[1];
        fieldTypeArray[0] = DateTimeFieldType.year();
        int[] intArray = new int[9];
        Partial partial = new Partial(null, fieldTypeArray, intArray);
        Minutes minutes = Minutes.minutesBetween(partial, partial);
        assertEquals(0, minutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testMinutesBetweenNullPartialsThrowsException() {
        try {
            Minutes.minutesBetween(null, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.base.BaseSingleFieldPeriod", e);
        }
    }

    @Test(timeout = 4000)
    public void testMinutesBetweenNullInstantsThrowsException() {
        try {
            Minutes.minutesBetween((ReadableInstant) null, (ReadableInstant) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.base.BaseSingleFieldPeriod", e);
        }
    }

    @Test(timeout = 4000)
    public void testMinValueMinusMinValueThrowsException() {
        Minutes minMinutes = Minutes.MIN_VALUE;
        try {
            minMinutes.minus(minMinutes);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testMinutesInNullIntervalDividedByZeroThrowsException() {
        Minutes minutesInNullInterval = Minutes.minutesIn(null);
        try {
            minutesInNullInterval.dividedBy(0);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.Minutes", e);
        }
    }

    @Test(timeout = 4000)
    public void testTwoMinutesPlusMaxValueThrowsException() {
        Minutes twoMinutes = Minutes.TWO;
        try {
            twoMinutes.plus(MAX_INT);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testMaxValuePlusZero() {
        Minutes maxMinutes = Minutes.MAX_VALUE;
        Minutes result = maxMinutes.plus(0);
        assertEquals(MAX_INT, result.getMinutes());
    }

    @Test(timeout = 4000)
    public void testThreeMinutes() {
        Minutes minutes = Minutes.minutes(3);
        assertEquals(3, minutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testTwoMinutesIsLessThanThirtyOneMinutes() {
        Minutes twoMinutes = Minutes.TWO;
        Minutes thirtyOneMinutes = Minutes.minutes(31);
        assertTrue(twoMinutes.isLessThan(thirtyOneMinutes));
    }

    @Test(timeout = 4000)
    public void testOneMinuteIsNotLessThanNull() {
        Minutes oneMinute = Minutes.ONE;
        assertFalse(oneMinute.isLessThan(null));
    }

    @Test(timeout = 4000)
    public void testMinValueIsLessThanNull() {
        Minutes minMinutes = Minutes.MIN_VALUE;
        assertTrue(minMinutes.isLessThan(null));
    }

    @Test(timeout = 4000)
    public void testThirtyOneMinutesIsNotLessThanItself() {
        Minutes thirtyOneMinutes = Minutes.minutes(31);
        assertFalse(thirtyOneMinutes.isLessThan(thirtyOneMinutes));
    }

    @Test(timeout = 4000)
    public void testTwoMinutesIsGreaterThanZeroMinutes() {
        Minutes twoMinutes = Minutes.TWO;
        Minutes zeroMinutes = Minutes.ZERO;
        assertTrue(twoMinutes.isGreaterThan(zeroMinutes));
    }

    @Test(timeout = 4000)
    public void testMinValueIsNotGreaterThanNull() {
        Minutes minMinutes = Minutes.MIN_VALUE;
        assertFalse(minMinutes.isGreaterThan(null));
    }

    @Test(timeout = 4000)
    public void testMinutesIsGreaterThanNull() {
        Minutes minutes = Minutes.minutes(1431);
        assertTrue(minutes.isGreaterThan(null));
    }

    @Test(timeout = 4000)
    public void testOneMinuteIsNotGreaterThanItself() {
        Minutes oneMinute = Minutes.ONE;
        assertFalse(oneMinute.isGreaterThan(oneMinute));
    }

    @Test(timeout = 4000)
    public void testZeroMinutesDividedByOne() {
        Minutes zeroMinutes = Minutes.ZERO;
        Minutes result = zeroMinutes.dividedBy(1);
        assertSame(zeroMinutes, result);
    }

    @Test(timeout = 4000)
    public void testZeroMinutesMinusNull() {
        Minutes zeroMinutes = Minutes.ZERO;
        Minutes result = zeroMinutes.minus((Minutes) null);
        assertSame(zeroMinutes, result);
    }

    @Test(timeout = 4000)
    public void testOneMinutePlusNull() {
        Minutes oneMinute = Minutes.ONE;
        Minutes result = oneMinute.plus((Minutes) null);
        assertSame(oneMinute, result);
    }

    @Test(timeout = 4000)
    public void testParseNullMinutes() {
        Minutes minutes = Minutes.parseMinutes(null);
        assertEquals(1, minutes.size());
    }

    @Test(timeout = 4000)
    public void testParseInvalidMinutesThrowsException() {
        try {
            Minutes.parseMinutes("UT");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.format.PeriodFormatter", e);
        }
    }

    @Test(timeout = 4000)
    public void testMinutesInNullIntervalToStandardDuration() {
        Minutes minutesInNullInterval = Minutes.minutesIn(null);
        Duration duration = minutesInNullInterval.toStandardDuration();
        Minutes result = duration.toStandardMinutes();
        assertEquals(0, result.getMinutes());
    }

    @Test(timeout = 4000)
    public void testMinutesBetweenNullPartialThrowsException() {
        int[] intArray = new int[1];
        Partial partial = new Partial(null, null, intArray);
        try {
            Minutes.minutesBetween(partial, partial);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.Partial", e);
        }
    }

    @Test(timeout = 4000)
    public void testMaxIntegerMinutes() {
        Minutes minutes = Minutes.minutes(MAX_INT);
        assertEquals(MAX_INT, minutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testTwoMinutes() {
        Minutes minutes = Minutes.minutes(2);
        assertEquals(2, minutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testOneMinute() {
        Minutes minutes = Minutes.minutes(1);
        assertEquals(1, minutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testMinValueToStandardHours() {
        Minutes minMinutes = Minutes.minutes(MIN_INT);
        Hours hours = minMinutes.toStandardHours();
        assertEquals(MIN_INT, minMinutes.getMinutes());
        assertEquals(-35791394, hours.getHours());
    }

    @Test(timeout = 4000)
    public void testMaxValueMultipliedByNegativeThrowsException() {
        Minutes maxMinutes = Minutes.MAX_VALUE;
        try {
            maxMinutes.multipliedBy(-4433);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testMinSecondsToStandardMinutes() {
        Seconds minSeconds = Seconds.MIN_VALUE;
        Minutes minutes = minSeconds.toStandardMinutes();
        Seconds seconds = minutes.toStandardSeconds();
        assertEquals(-35791394, minutes.getMinutes());
        assertEquals(-2147483640, seconds.getSeconds());
    }

    @Test(timeout = 4000)
    public void testMaxValueMinusNegativeThrowsException() {
        Minutes maxMinutes = Minutes.MAX_VALUE;
        try {
            maxMinutes.minus(-2550);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetFieldType() {
        Minutes minMinutes = Minutes.MIN_VALUE;
        DurationFieldType fieldType = minMinutes.getFieldType();
        assertEquals("minutes", fieldType.toString());
    }

    @Test(timeout = 4000)
    public void testZeroMinutesToStandardHours() {
        Minutes zeroMinutes = Minutes.ZERO;
        Hours hours = zeroMinutes.toStandardHours();
        assertEquals(0, hours.getHours());
    }

    @Test(timeout = 4000)
    public void testMaxValueToString() {
        Minutes maxMinutes = Minutes.MAX_VALUE;
        String result = maxMinutes.toString();
        assertEquals("PT2147483647M", result);
    }

    @Test(timeout = 4000)
    public void testStandardMinutesInNullPeriod() {
        Minutes minutes = Minutes.standardMinutesIn(null);
        assertEquals(0, minutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testMinutesBetweenSameInstant() {
        Instant instant = Instant.now();
        Minutes minutes = Minutes.minutesBetween(instant, instant);
        assertEquals(0, minutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testGetMinutesMinValue() {
        Minutes minMinutes = Minutes.MIN_VALUE;
        assertEquals(MIN_INT, minMinutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testGetPeriodType() {
        Minutes maxMinutes = Minutes.MAX_VALUE;
        PeriodType periodType = maxMinutes.getPeriodType();
        assertEquals(1, periodType.size());
    }

    @Test(timeout = 4000)
    public void testZeroMinutesToStandardDays() {
        Minutes zeroMinutes = Minutes.ZERO;
        Days days = zeroMinutes.toStandardDays();
        assertEquals(0, days.getDays());
    }

    @Test(timeout = 4000)
    public void testMinValueNegatedThrowsException() {
        Minutes minMinutes = Minutes.MIN_VALUE;
        try {
            minMinutes.negated();
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testOneMinuteToStandardWeeks() {
        Minutes oneMinute = Minutes.ONE;
        Weeks weeks = oneMinute.toStandardWeeks();
        assertEquals(0, weeks.getWeeks());
    }
}