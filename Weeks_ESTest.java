package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.*;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class WeeksTest extends Weeks_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testWeeksComparisonWithZeroMinutes() {
        Minutes minutes = Minutes.ZERO;
        Weeks twoWeeks = Weeks.TWO;
        Weeks zeroWeeks = minutes.toStandardWeeks();
        assertFalse(twoWeeks.isLessThan(zeroWeeks));
        assertEquals(0, zeroWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksComparisonWithThreeMinutes() {
        Minutes minutes = Minutes.THREE;
        Seconds seconds = minutes.toStandardSeconds();
        Weeks zeroWeeks = seconds.toStandardWeeks();
        assertFalse(zeroWeeks.isLessThan(null));
        assertEquals(0, zeroWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksComparisonWithNullPeriod() {
        Weeks zeroWeeks = Weeks.standardWeeksIn(null);
        Weeks twoWeeks = Weeks.TWO;
        assertFalse(zeroWeeks.isGreaterThan(twoWeeks));
        assertEquals(0, zeroWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksComparisonWithNullWeeks() {
        Weeks minWeeks = Weeks.MIN_VALUE;
        assertFalse(minWeeks.isGreaterThan(null));
    }

    @Test(timeout = 4000)
    public void testWeeksToStandardSeconds() {
        Weeks negativeWeeks = Weeks.weeks(-2820);
        Seconds seconds = negativeWeeks.toStandardSeconds();
        assertEquals(-1705536000, seconds.getSeconds());
    }

    @Test(timeout = 4000)
    public void testParseWeeksWithNullString() {
        Weeks zeroWeeks = Weeks.parseWeeks(null);
        Minutes minutes = zeroWeeks.toStandardMinutes();
        assertEquals(1, minutes.size());
    }

    @Test(timeout = 4000)
    public void testWeeksToStandardMinutes() {
        Weeks oneWeek = Weeks.ONE;
        Minutes minutes = oneWeek.toStandardMinutes();
        assertEquals(10080, minutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testWeeksMinusLargeNumber() {
        Weeks zeroWeeks = Weeks.weeksIn(null);
        Weeks negativeWeeks = zeroWeeks.minus(3765);
        Minutes minutes = negativeWeeks.toStandardMinutes();
        assertEquals(-37951200, minutes.getMinutes());
    }

    @Test(timeout = 4000)
    public void testWeeksToStandardHours() {
        Weeks zeroWeeks = Weeks.ZERO;
        Hours hours = zeroWeeks.toStandardHours();
        assertEquals(0, hours.getHours());
    }

    @Test(timeout = 4000)
    public void testMaxSecondsToStandardWeeksAndHours() {
        Seconds maxSeconds = Seconds.MAX_VALUE;
        Weeks weeks = maxSeconds.toStandardWeeks();
        Hours hours = weeks.toStandardHours();
        assertEquals(596400, hours.getHours());
    }

    @Test(timeout = 4000)
    public void testWeeksSubtraction() {
        Weeks zeroWeeks = Weeks.ZERO;
        Weeks threeWeeks = Weeks.THREE;
        Weeks negativeWeeks = zeroWeeks.minus(threeWeeks);
        Hours hours = negativeWeeks.toStandardHours();
        assertEquals(-504, hours.getHours());
    }

    @Test(timeout = 4000)
    public void testWeeksToStandardDuration() {
        Weeks zeroWeeks = Weeks.ZERO;
        Duration duration = zeroWeeks.TWO.toStandardDuration();
        assertEquals(1209600000L, duration.getMillis());
    }

    @Test(timeout = 4000)
    public void testWeeksToStandardDays() {
        Weeks zeroWeeks = Weeks.weeksIn(null);
        Days days = zeroWeeks.toStandardDays();
        assertEquals(1, days.size());
    }

    @Test(timeout = 4000)
    public void testWeeksToStandardDaysForThreeWeeks() {
        Weeks zeroWeeks = Weeks.ZERO;
        Days days = zeroWeeks.THREE.toStandardDays();
        assertEquals(21, days.getDays());
    }

    @Test(timeout = 4000)
    public void testWeeksAdditionWithMinValue() {
        Weeks minWeeks = Weeks.MIN_VALUE;
        Weeks resultWeeks = minWeeks.MAX_VALUE.plus(minWeeks);
        assertEquals(-1, resultWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksAddition() {
        Weeks minWeeks = Weeks.MIN_VALUE;
        Weeks resultWeeks = minWeeks.plus(4);
        assertEquals(-2147483644, resultWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksNegation() {
        Weeks zeroWeeks = Weeks.ZERO;
        Weeks negatedWeeks = zeroWeeks.negated();
        assertEquals(0, negatedWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksNegationForNegativeWeeks() {
        Weeks negativeWeeks = Weeks.weeks(-2490);
        negativeWeeks.negated();
        assertEquals(-2490, negativeWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksMultiplicationWithNegative() {
        Weeks zeroWeeks = Weeks.parseWeeks(null);
        Weeks resultWeeks = zeroWeeks.multipliedBy(-1060);
        assertEquals(0, resultWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksMultiplication() {
        Weeks oneWeek = Weeks.ONE;
        Weeks resultWeeks = oneWeek.multipliedBy(604800);
        assertEquals(604800, resultWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksMultiplicationWithNegativeScalar() {
        Weeks threeWeeks = Weeks.THREE;
        Weeks resultWeeks = threeWeeks.multipliedBy(-617);
        assertEquals(-1851, resultWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksSubtractionWithZero() {
        Weeks zeroWeeks = Weeks.ZERO;
        Weeks resultWeeks = zeroWeeks.ZERO.minus(zeroWeeks);
        assertEquals(0, resultWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksSubtractionWithLargeNumber() {
        Weeks largeWeeks = Weeks.weeks(317351877);
        Weeks resultWeeks = largeWeeks.minus(2285);
        assertEquals(317349592, resultWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksFromZeroMinutes() {
        Minutes zeroMinutes = Minutes.ZERO;
        Weeks zeroWeeks = zeroMinutes.toStandardWeeks();
        assertEquals(0, zeroWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksFromMinValue() {
        Weeks minWeeks = Weeks.MIN_VALUE;
        assertEquals(Integer.MIN_VALUE, minWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksDivision() {
        Weeks maxWeeks = Weeks.MAX_VALUE;
        Weeks resultWeeks = maxWeeks.dividedBy(888);
        assertEquals(2418337, resultWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksBetweenWithNullPartial() {
        DateTimeFieldType[] dateTimeFieldTypeArray = new DateTimeFieldType[9];
        DateTimeFieldType dateTimeFieldType = DateTimeFieldType.dayOfMonth();
        dateTimeFieldTypeArray[0] = dateTimeFieldType;
        int[] intArray = new int[0];
        Partial partial = new Partial(null, dateTimeFieldTypeArray, intArray);
        try {
            Weeks.weeksBetween(partial, partial);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.Partial", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksBetweenWithNullPartialThrowsException() {
        try {
            Weeks.weeksBetween((ReadablePartial) null, (ReadablePartial) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.base.BaseSingleFieldPeriod", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksBetweenWithNullInstantThrowsException() {
        try {
            Weeks.weeksBetween((ReadableInstant) null, (ReadableInstant) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.base.BaseSingleFieldPeriod", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksToStandardSecondsThrowsException() {
        Weeks minWeeks = Weeks.MIN_VALUE;
        try {
            minWeeks.toStandardSeconds();
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksFromMinHours() {
        Hours minHours = Hours.MIN_VALUE;
        Weeks weeks = Weeks.standardWeeksIn(minHours);
        assertEquals(-12782640, weeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksAdditionThrowsException() {
        Weeks maxWeeks = Weeks.MAX_VALUE;
        try {
            maxWeeks.plus(maxWeeks);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksSubtractionThrowsException() {
        Weeks minWeeks = Weeks.MIN_VALUE;
        try {
            minWeeks.minus(minWeeks);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksDivisionByZeroThrowsException() {
        Weeks twoWeeks = Weeks.TWO;
        try {
            twoWeeks.dividedBy(0);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.Weeks", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksSubtractionWithZeroWeeks() {
        Weeks zeroWeeks = Weeks.weeks(0);
        Weeks resultWeeks = zeroWeeks.minus(0);
        assertEquals(0, resultWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksFromMaxValue() {
        Weeks maxWeeks = Weeks.weeks(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, maxWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksToStandardSecondsAndWeeks() {
        Weeks zeroWeeks = Weeks.ZERO;
        Seconds seconds = zeroWeeks.ONE.toStandardSeconds();
        Weeks oneWeek = seconds.toStandardWeeks();
        assertEquals(1, oneWeek.getWeeks());
        assertEquals(604800, seconds.getSeconds());
    }

    @Test(timeout = 4000)
    public void testWeeksComparisonWithMaxValue() {
        Weeks zeroWeeks = Weeks.standardWeeksIn(null);
        Weeks maxWeeks = Weeks.MAX_VALUE;
        assertTrue(zeroWeeks.isLessThan(maxWeeks));
        assertEquals(0, zeroWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksComparisonWithNullWeeks() {
        Weeks minWeeks = Weeks.MIN_VALUE;
        assertTrue(minWeeks.isLessThan(null));
    }

    @Test(timeout = 4000)
    public void testWeeksComparisonWithNullWeeksForThreeWeeks() {
        Weeks threeWeeks = Weeks.THREE;
        assertFalse(threeWeeks.isLessThan(null));
    }

    @Test(timeout = 4000)
    public void testWeeksComparisonWithSelf() {
        Weeks zeroWeeks = Weeks.standardWeeksIn(null);
        assertFalse(zeroWeeks.isLessThan(zeroWeeks));
        assertEquals(0, zeroWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksComparisonWithMinValue() {
        Weeks largeWeeks = Weeks.weeks(317351877);
        Weeks minWeeks = Weeks.MIN_VALUE;
        assertTrue(largeWeeks.isGreaterThan(minWeeks));
        assertEquals(317351877, largeWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksComparisonWithNullWeeksForGreaterThan() {
        Weeks threeWeeks = Weeks.THREE;
        assertTrue(threeWeeks.isGreaterThan(null));
    }

    @Test(timeout = 4000)
    public void testWeeksComparisonWithNullWeeksForZeroWeeks() {
        Weeks zeroWeeks = Weeks.standardWeeksIn(null);
        assertFalse(zeroWeeks.isGreaterThan(null));
        assertEquals(0, zeroWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksComparisonWithSelfForGreaterThan() {
        Weeks largeWeeks = Weeks.weeks(317351877);
        assertFalse(largeWeeks.isGreaterThan(largeWeeks));
        assertEquals(317351877, largeWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksDivisionByOne() {
        Weeks zeroWeeks = Weeks.standardWeeksIn(null);
        Weeks resultWeeks = zeroWeeks.dividedBy(1);
        assertEquals(0, resultWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksDivisionByNegativeOne() {
        Weeks maxWeeks = Weeks.MAX_VALUE;
        Weeks resultWeeks = maxWeeks.dividedBy(-1);
        assertEquals(-2147483647, resultWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksSubtractionWithNullWeeks() {
        Weeks minWeeks = Weeks.MIN_VALUE;
        Weeks resultWeeks = minWeeks.minus(null);
        assertSame(minWeeks, resultWeeks);
    }

    @Test(timeout = 4000)
    public void testWeeksAdditionWithNullWeeks() {
        Weeks maxWeeks = Weeks.MAX_VALUE;
        Weeks resultWeeks = maxWeeks.plus(null);
        assertEquals(1, resultWeeks.size());
    }

    @Test(timeout = 4000)
    public void testWeeksAdditionWithZeroWeeks() {
        Weeks zeroWeeks = Weeks.ZERO;
        Weeks resultWeeks = zeroWeeks.plus(zeroWeeks);
        assertEquals(0, resultWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksAdditionWithZero() {
        Weeks zeroWeeks = Weeks.parseWeeks(null);
        Weeks resultWeeks = zeroWeeks.plus(0);
        assertEquals(0, resultWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testParseWeeksWithInvalidStringThrowsException() {
        try {
            Weeks.parseWeeks(")%X[WS");
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.joda.time.format.PeriodFormatter", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksNegationAndDaysConversion() {
        Weeks zeroWeeks = Weeks.weeksIn(null);
        Weeks positiveWeeks = zeroWeeks.minus(-2358);
        Weeks negatedWeeks = positiveWeeks.negated();
        Days days = negatedWeeks.toStandardDays();
        assertEquals(2358, positiveWeeks.getWeeks());
        assertEquals(-16506, days.getDays());
    }

    @Test(timeout = 4000)
    public void testWeeksBetweenWithNullPartialThrowsExceptionAgain() {
        DateTimeFieldType[] dateTimeFieldTypeArray = new DateTimeFieldType[9];
        int[] intArray = new int[0];
        Partial partial = new Partial(null, dateTimeFieldTypeArray, intArray);
        try {
            Weeks.weeksBetween(partial, partial);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.joda.time.Partial", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksCreation() {
        Weeks threeWeeks = Weeks.weeks(3);
        assertEquals(3, threeWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksAdditionWithTwoWeeks() {
        Weeks twoWeeks = Weeks.weeks(2);
        Weeks resultWeeks = twoWeeks.plus(2);
        assertEquals(4, resultWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksSubtractionWithTwoWeeks() {
        Weeks threeWeeks = Weeks.THREE;
        Weeks twoWeeks = Weeks.TWO;
        Weeks resultWeeks = threeWeeks.minus(twoWeeks);
        assertEquals(1, resultWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksAdditionWithMinValueThrowsException() {
        Weeks minWeeks = Weeks.weeks(Integer.MIN_VALUE);
        try {
            minWeeks.plus(Integer.MIN_VALUE);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksToStandardHoursThrowsException() {
        Weeks minWeeks = Weeks.MIN_VALUE;
        try {
            minWeeks.toStandardHours();
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksToString() {
        Weeks oneWeek = Weeks.ONE;
        String string = oneWeek.toString();
        assertEquals("P1W", string);
    }

    @Test(timeout = 4000)
    public void testWeeksToStandardDaysThrowsException() {
        Weeks maxWeeks = Weeks.MAX_VALUE;
        try {
            maxWeeks.toStandardDays();
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksNegationThrowsException() {
        Weeks minWeeks = Weeks.MIN_VALUE;
        try {
            minWeeks.negated();
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksGetFieldType() {
        Weeks threeWeeks = Weeks.THREE;
        DurationFieldType fieldType = threeWeeks.getFieldType();
        assertEquals("weeks", fieldType.getName());
    }

    @Test(timeout = 4000)
    public void testWeeksSubtractionWithNegativeThrowsException() {
        Weeks maxWeeks = Weeks.MAX_VALUE;
        try {
            maxWeeks.minus(-876);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksGetWeeks() {
        Weeks threeWeeks = Weeks.THREE;
        int weeks = threeWeeks.getWeeks();
        assertEquals(3, weeks);
    }

    @Test(timeout = 4000)
    public void testWeeksToStandardSecondsWithNullPeriod() {
        Weeks zeroWeeks = Weeks.standardWeeksIn(null);
        Seconds seconds = zeroWeeks.toStandardSeconds();
        assertEquals(0, seconds.getSeconds());
    }

    @Test(timeout = 4000)
    public void testWeeksGetPeriodType() {
        Weeks twoWeeks = Weeks.TWO;
        PeriodType periodType = twoWeeks.getPeriodType();
        assertEquals(1, periodType.size());
    }

    @Test(timeout = 4000)
    public void testWeeksToStandardMinutesThrowsException() {
        Weeks maxWeeks = Weeks.MAX_VALUE;
        try {
            maxWeeks.toStandardMinutes();
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksMultiplicationThrowsException() {
        Weeks maxWeeks = Weeks.MAX_VALUE;
        try {
            maxWeeks.multipliedBy(7);
            fail("Expecting exception: ArithmeticException");
        } catch (ArithmeticException e) {
            verifyException("org.joda.time.field.FieldUtils", e);
        }
    }

    @Test(timeout = 4000)
    public void testWeeksBetweenWithSameInstant() {
        Instant instant = Instant.EPOCH;
        Weeks zeroWeeks = Weeks.weeksBetween(instant, instant);
        assertEquals(0, zeroWeeks.getWeeks());
    }

    @Test(timeout = 4000)
    public void testWeeksToStandardDurationWithMinValue() {
        Weeks minWeeks = Weeks.MIN_VALUE;
        Duration duration = minWeeks.toStandardDuration();
        assertEquals(-15032385536L, duration.getStandardDays());
    }
}