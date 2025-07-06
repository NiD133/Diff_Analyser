package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.*;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class Seconds_ESTest extends Seconds_ESTest_scaffolding {

    private static final int MAX_SECONDS = Integer.MAX_VALUE;
    private static final int MIN_SECONDS = Integer.MIN_VALUE;
    private static final int TWO_MINUTES_IN_SECONDS = 120;
    private static final int NEGATIVE_SECONDS = -1354;
    private static final int NEGATIVE_MULTIPLIER = -2309;
    private static final int POSITIVE_MULTIPLIER = 2119;
    private static final int HOURS_IN_1500_MINUTES = 25;
    private static final int SECONDS_IN_SIX_DAYS = 518400;
    private static final int SECONDS_IN_ONE_DAY = 86400;

    // Test conversion from Minutes to Seconds and comparison
    @Test(timeout = 4000)
    public void testMinutesToSecondsConversion() throws Throwable {
        Minutes minutes = Minutes.TWO;
        Seconds seconds = minutes.toStandardSeconds();
        Seconds maxMinusSeconds = seconds.MAX_VALUE.minus(seconds);
        boolean isLessThan = maxMinusSeconds.isLessThan(seconds);

        assertEquals(2147483527, maxMinusSeconds.getSeconds());
        assertFalse(isLessThan);
        assertEquals(TWO_MINUTES_IN_SECONDS, seconds.getSeconds());
    }

    // Test isLessThan with null
    @Test(timeout = 4000)
    public void testIsLessThanWithNull() throws Throwable {
        Seconds seconds = Seconds.ZERO;
        boolean isLessThan = seconds.isLessThan(null);
        assertFalse(isLessThan);
    }

    // Test isGreaterThan with null
    @Test(timeout = 4000)
    public void testIsGreaterThanWithNull() throws Throwable {
        Seconds seconds = Seconds.ZERO;
        boolean isGreaterThan = seconds.isGreaterThan(null);
        assertFalse(isGreaterThan);
    }

    // Test conversion from negative seconds to weeks
    @Test(timeout = 4000)
    public void testNegativeSecondsToWeeks() throws Throwable {
        Seconds seconds = Seconds.seconds(NEGATIVE_SECONDS);
        Weeks weeks = seconds.MAX_VALUE.toStandardWeeks();
        assertEquals(NEGATIVE_SECONDS, seconds.getSeconds());
        assertEquals(3550, weeks.getWeeks());
    }

    // Test conversion from MIN_VALUE seconds to weeks
    @Test(timeout = 4000)
    public void testMinValueSecondsToWeeks() throws Throwable {
        Seconds seconds = Seconds.seconds(MIN_SECONDS);
        Weeks weeks = seconds.toStandardWeeks();
        assertEquals(-3550, weeks.getWeeks());
        assertEquals(MIN_SECONDS, seconds.getSeconds());
    }

    // Test conversion from Seconds to Minutes
    @Test(timeout = 4000)
    public void testSecondsToMinutesConversion() throws Throwable {
        Minutes minutes = Minutes.TWO;
        Seconds seconds = minutes.toStandardSeconds();
        Minutes convertedMinutes = seconds.toStandardMinutes();
        assertEquals(2, convertedMinutes.getMinutes());
        assertEquals(TWO_MINUTES_IN_SECONDS, seconds.getSeconds());
    }

    // Test conversion from Seconds to Hours
    @Test(timeout = 4000)
    public void testSecondsToHoursConversion() throws Throwable {
        Seconds seconds = Seconds.ONE;
        Hours hours = seconds.toStandardHours();
        assertEquals(0, hours.getHours());
    }

    // Test conversion from Duration to Hours
    @Test(timeout = 4000)
    public void testDurationToHoursConversion() throws Throwable {
        Duration duration = Duration.standardMinutes(1500L);
        Minutes minutes = duration.toStandardMinutes();
        Seconds seconds = minutes.toStandardSeconds();
        Hours hours = seconds.toStandardHours();
        assertEquals(90000, seconds.getSeconds());
        assertEquals(HOURS_IN_1500_MINUTES, hours.getHours());
    }

    // Test conversion from Seconds to Duration
    @Test(timeout = 4000)
    public void testSecondsToDurationConversion() throws Throwable {
        Seconds seconds = Seconds.ZERO;
        Duration duration = seconds.toStandardDuration();
        assertEquals(0L, duration.getStandardSeconds());
    }

    // Test conversion from MIN_VALUE Seconds to Duration
    @Test(timeout = 4000)
    public void testMinValueSecondsToDuration() throws Throwable {
        Seconds seconds = Seconds.MIN_VALUE;
        Duration duration = seconds.toStandardDuration();
        assertEquals((long) MIN_SECONDS, duration.getStandardSeconds());
    }

    // Test conversion from Seconds to Days
    @Test(timeout = 4000)
    public void testSecondsToDaysConversion() throws Throwable {
        Seconds seconds = Seconds.ZERO;
        Days days = seconds.toStandardDays();
        assertEquals(0, days.getDays());
    }

    // Test conversion from MIN_VALUE Seconds to Days
    @Test(timeout = 4000)
    public void testMinValueSecondsToDays() throws Throwable {
        Minutes minutes = Minutes.TWO;
        Seconds seconds = minutes.toStandardSeconds();
        Days days = seconds.MIN_VALUE.toStandardDays();
        assertEquals(TWO_MINUTES_IN_SECONDS, seconds.getSeconds());
        assertEquals(-24855, days.getDays());
    }

    // Test standardSecondsIn with null
    @Test(timeout = 4000)
    public void testStandardSecondsInWithNull() throws Throwable {
        Seconds seconds = Seconds.standardSecondsIn(null);
        assertEquals(0, seconds.getSeconds());
    }

    // Test addition of MIN_VALUE Seconds with ZERO
    @Test(timeout = 4000)
    public void testMinValuePlusZero() throws Throwable {
        Seconds seconds = Seconds.ZERO;
        Seconds result = seconds.MIN_VALUE.plus(seconds);
        assertEquals(MIN_SECONDS, result.getSeconds());
    }

    // Test addition of Seconds
    @Test(timeout = 4000)
    public void testSecondsAddition() throws Throwable {
        Seconds seconds = Seconds.ZERO;
        Seconds result = seconds.plus(2519);
        assertEquals(2519, result.getSeconds());
    }

    // Test negation of Seconds
    @Test(timeout = 4000)
    public void testSecondsNegation() throws Throwable {
        Seconds seconds = Seconds.ZERO;
        Seconds result = seconds.negated();
        assertEquals(0, result.getSeconds());
    }

    // Test negation of negative Seconds
    @Test(timeout = 4000)
    public void testNegativeSecondsNegation() throws Throwable {
        Seconds seconds = Seconds.ZERO;
        Seconds result = seconds.plus(-1);
        result.negated();
        assertEquals(-1, result.getSeconds());
    }

    // Test conversion from Days to Seconds
    @Test(timeout = 4000)
    public void testDaysToSecondsConversion() throws Throwable {
        Days days = Days.SIX;
        Seconds seconds = days.toStandardSeconds();
        seconds.negated();
        assertEquals(SECONDS_IN_SIX_DAYS, seconds.getSeconds());
    }

    // Test multiplication of Seconds
    @Test(timeout = 4000)
    public void testSecondsMultiplication() throws Throwable {
        Seconds seconds = Seconds.ZERO;
        Seconds result = seconds.multipliedBy(3);
        assertEquals(0, result.getSeconds());
    }

    // Test multiplication of Seconds with positive multiplier
    @Test(timeout = 4000)
    public void testSecondsMultiplicationWithPositiveMultiplier() throws Throwable {
        Seconds seconds = Seconds.TWO;
        Seconds result = seconds.multipliedBy(POSITIVE_MULTIPLIER);
        assertEquals(4238, result.getSeconds());
    }

    // Test multiplication of Seconds with negative multiplier
    @Test(timeout = 4000)
    public void testSecondsMultiplicationWithNegativeMultiplier() throws Throwable {
        Seconds seconds = Seconds.seconds(NEGATIVE_SECONDS);
        Seconds result = seconds.TWO.multipliedBy(NEGATIVE_MULTIPLIER);
        boolean isGreaterThan = result.isGreaterThan(seconds);
        assertEquals(-4618, result.getSeconds());
        assertFalse(isGreaterThan);
    }

    // Test subtraction of Seconds
    @Test(timeout = 4000)
    public void testSecondsSubtraction() throws Throwable {
        Seconds seconds = Seconds.ONE;
        Seconds result = Seconds.seconds(1078);
        Seconds difference = seconds.minus(result);
        assertEquals(-1077, difference.getSeconds());
        assertEquals(1078, result.getSeconds());
    }

    // Test subtraction of zero Seconds
    @Test(timeout = 4000)
    public void testZeroSecondsSubtraction() throws Throwable {
        Seconds seconds = Seconds.ZERO;
        Seconds result = seconds.minus(0);
        assertEquals(0, result.getSeconds());
    }

    // Test subtraction of negative Seconds
    @Test(timeout = 4000)
    public void testNegativeSecondsSubtraction() throws Throwable {
        Seconds seconds = Seconds.ONE;
        Seconds result = seconds.minus(-1);
        assertEquals(2, result.getSeconds());
    }

    // Test getSeconds method
    @Test(timeout = 4000)
    public void testGetSeconds() throws Throwable {
        Seconds seconds = Seconds.ZERO;
        int result = seconds.getSeconds();
        assertEquals(0, result);
    }

    // Test getSeconds method with MIN_VALUE
    @Test(timeout = 4000)
    public void testGetSecondsWithMinValue() throws Throwable {
        Seconds seconds = Seconds.MIN_VALUE;
        int result = seconds.getSeconds();
        assertEquals(MIN_SECONDS, result);
    }

    // Test division of Seconds
    @Test(timeout = 4000)
    public void testSecondsDivision() throws Throwable {
        Seconds seconds = Seconds.ONE;
        Seconds result = seconds.plus(-1);
        Seconds divided = result.dividedBy(1);
        assertEquals(0, divided.getSeconds());
    }

    // Test exception when dividing by zero
    @Test(timeout = 4000)
    public void testDivisionByZeroException() throws Throwable {
        Seconds seconds = Seconds.TWO;
        try {
            seconds.dividedBy(0);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.Seconds", e);
        }
    }

    // Test exception when adding to MAX_VALUE
    @Test(timeout = 4000)
    public void testAdditionOverflowException() throws Throwable {
        Seconds seconds = Seconds.MAX_VALUE;
        try {
            seconds.plus(4088);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    // Test exception when negating MIN_VALUE
    @Test(timeout = 4000)
    public void testNegationOverflowException() throws Throwable {
        Seconds seconds = Seconds.MIN_VALUE;
        try {
            seconds.negated();
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    // Test exception when multiplying MAX_VALUE
    @Test(timeout = 4000)
    public void testMultiplicationOverflowException() throws Throwable {
        Seconds seconds = Seconds.MAX_VALUE;
        try {
            seconds.multipliedBy(725);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    // Test conversion from Seconds to Weeks
    @Test(timeout = 4000)
    public void testSecondsToWeeksConversion() throws Throwable {
        Seconds seconds = Seconds.THREE;
        Weeks weeks = seconds.toStandardWeeks();
        assertEquals(0, weeks.getWeeks());
    }

    // Test getPeriodType method
    @Test(timeout = 4000)
    public void testGetPeriodType() throws Throwable {
        Seconds seconds = Seconds.THREE;
        PeriodType periodType = seconds.getPeriodType();
        assertEquals("Seconds", periodType.getName());
    }

    // Test getFieldType method
    @Test(timeout = 4000)
    public void testGetFieldType() throws Throwable {
        Seconds seconds = Seconds.MIN_VALUE;
        DurationFieldType fieldType = seconds.getFieldType();
        assertEquals("seconds", fieldType.toString());
    }

    // Test conversion from Seconds to Duration
    @Test(timeout = 4000)
    public void testSecondsToStandardDuration() throws Throwable {
        Seconds seconds = Seconds.ONE;
        Duration duration = seconds.toStandardDuration();
        assertEquals(1000L, duration.getMillis());
    }

    // Test standardSecondsIn with Days
    @Test(timeout = 4000)
    public void testStandardSecondsInDays() throws Throwable {
        Days days = Days.ONE;
        Seconds seconds = Seconds.standardSecondsIn(days);
        assertEquals(SECONDS_IN_ONE_DAY, seconds.getSeconds());
    }
}