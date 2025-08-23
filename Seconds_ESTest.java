package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class Seconds_ESTest extends Seconds_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testHoursToDaysToSecondsConversion() {
        Hours hours = Hours.ONE;
        Days days = hours.toStandardDays();
        Seconds seconds = days.toStandardSeconds();
        assertEquals(0, seconds.getSeconds());
    }

    @Test(timeout = 4000)
    public void testSecondsComparisonWithMinValue() {
        Seconds largeSeconds = Seconds.seconds(352831696);
        Seconds minSeconds = Seconds.MIN_VALUE;
        assertFalse(minSeconds.isGreaterThan(largeSeconds));
    }

    @Test(timeout = 4000)
    public void testSecondsToWeeksConversion() {
        Seconds seconds = Seconds.seconds(352831696);
        Weeks weeks = seconds.toStandardWeeks();
        assertEquals(583, weeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testSecondsToMinutesConversion() {
        Seconds seconds = Seconds.seconds(1575);
        Minutes minutes = seconds.toStandardMinutes();
        assertEquals(26, minutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testZeroSecondsToHoursConversion() {
        Seconds zeroSeconds = Seconds.ZERO;
        Hours hours = zeroSeconds.toStandardHours();
        assertEquals(0, hours.getHours());
    }

    @Test(timeout = 4000)
    public void testSecondsToDurationConversion() {
        Seconds zeroSeconds = Seconds.ZERO;
        Duration duration = zeroSeconds.toStandardDuration();
        assertEquals(0L, duration.getStandardSeconds());
    }

    @Test(timeout = 4000)
    public void testDaysToSecondsAndBackToDays() {
        Days fiveDays = Days.FIVE;
        Seconds seconds = fiveDays.toStandardSeconds();
        Days resultDays = seconds.toStandardDays();
        assertEquals(5, resultDays.getDays());
    }

    @Test(timeout = 4000)
    public void testWeeksToDaysToSeconds() {
        Weeks twoWeeks = Weeks.TWO;
        Days days = twoWeeks.toStandardDays();
        Seconds seconds = days.toStandardSeconds();
        assertEquals(1209600, seconds.getSeconds());
    }

    @Test(timeout = 4000)
    public void testStandardSecondsInNullPeriod() {
        Seconds seconds = Seconds.standardSecondsIn(null);
        assertEquals(0, seconds.getSeconds());
    }

    @Test(timeout = 4000)
    public void testSecondsAddition() {
        Seconds zeroSeconds = Seconds.ZERO;
        Seconds result = zeroSeconds.plus(zeroSeconds);
        assertEquals(0, result.getSeconds());
    }

    @Test(timeout = 4000)
    public void testSecondsMultiplicationByZero() {
        Seconds seconds = Seconds.seconds(1810);
        Seconds result = seconds.multipliedBy(0);
        assertEquals(0, result.getSeconds());
    }

    @Test(timeout = 4000)
    public void testSecondsDivision() {
        Seconds seconds = Seconds.seconds(1804);
        Seconds result = seconds.dividedBy(1804);
        assertEquals(1, result.getSeconds());
    }

    @Test(timeout = 4000)
    public void testSecondsNegation() {
        Seconds seconds = Seconds.THREE;
        Seconds negated = seconds.negated();
        assertEquals(-3, negated.getSeconds());
    }

    @Test(timeout = 4000)
    public void testSecondsToString() {
        Seconds minSeconds = Seconds.MIN_VALUE;
        assertEquals("PT-2147483648S", minSeconds.toString());
    }

    @Test(timeout = 4000)
    public void testSecondsIsGreaterThan() {
        Seconds twoSeconds = Seconds.seconds(2);
        Seconds minSeconds = Seconds.MIN_VALUE;
        assertTrue(twoSeconds.isGreaterThan(minSeconds));
    }

    @Test(timeout = 4000)
    public void testSecondsIsLessThan() {
        Seconds seconds = Seconds.seconds(-1300);
        assertTrue(seconds.isLessThan(null));
    }

    @Test(timeout = 4000)
    public void testSecondsParseNull() {
        Seconds seconds = Seconds.parseSeconds(null);
        assertEquals(1, seconds.size());
    }

    @Test(timeout = 4000)
    public void testSecondsParseEmptyString() {
        try {
            Seconds.parseSeconds("");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception due to invalid format
        }
    }

    @Test(timeout = 4000)
    public void testSecondsBetweenNullInstants() {
        try {
            Seconds.secondsBetween((ReadableInstant) null, (ReadableInstant) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception due to null instants
        }
    }

    @Test(timeout = 4000)
    public void testSecondsOverflowOnAddition() {
        Seconds maxSeconds = Seconds.MAX_VALUE;
        try {
            maxSeconds.plus(maxSeconds);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception due to overflow
        }
    }

    @Test(timeout = 4000)
    public void testSecondsOverflowOnNegation() {
        Seconds minSeconds = Seconds.MIN_VALUE;
        try {
            minSeconds.negated();
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception due to overflow
        }
    }

    @Test(timeout = 4000)
    public void testSecondsOverflowOnMultiplication() {
        Seconds minSeconds = Seconds.MIN_VALUE;
        try {
            minSeconds.multipliedBy(-674);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception due to overflow
        }
    }

    @Test(timeout = 4000)
    public void testSecondsOverflowOnDivisionByZero() {
        Seconds maxSeconds = Seconds.MAX_VALUE;
        try {
            maxSeconds.dividedBy(0);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            // Expected exception due to division by zero
        }
    }
}