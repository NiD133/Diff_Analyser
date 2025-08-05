package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * A comprehensive test suite for the {@link Seconds} class, focusing on clarity and maintainability.
 */
public class SecondsTest {

    // --- Factory Methods ---

    @Test
    public void factory_seconds_returnsCachedConstants() {
        assertSame(Seconds.ZERO, Seconds.seconds(0));
        assertSame(Seconds.ONE, Seconds.seconds(1));
        assertSame(Seconds.TWO, Seconds.seconds(2));
        assertSame(Seconds.THREE, Seconds.seconds(3));
        assertSame(Seconds.MAX_VALUE, Seconds.seconds(Integer.MAX_VALUE));
        assertSame(Seconds.MIN_VALUE, Seconds.seconds(Integer.MIN_VALUE));
    }

    @Test
    public void factory_seconds_returnsNewInstanceForOtherValues() {
        int value = 123;
        Seconds seconds = Seconds.seconds(value);
        assertEquals(value, seconds.getSeconds());
    }

    @Test
    public void factory_secondsBetween_instants_calculatesDifference() {
        Instant start = new Instant(1000L); // 1 second after epoch
        Instant end = new Instant(5000L);   // 5 seconds after epoch

        assertEquals(4, Seconds.secondsBetween(start, end).getSeconds());
        assertEquals(-4, Seconds.secondsBetween(end, start).getSeconds());
        assertEquals(0, Seconds.secondsBetween(start, start).getSeconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void factory_secondsBetween_instants_throwsExceptionForNull() {
        Seconds.secondsBetween(new Instant(), null);
    }

    @Test
    public void factory_secondsIn_calculatesSecondsFromInterval() {
        Instant start = new Instant(1000L);
        Instant end = new Instant(11000L); // 10 seconds duration
        ReadableInterval interval = new Interval(start, end);

        assertEquals(10, Seconds.secondsIn(interval).getSeconds());
    }

    @Test
    public void factory_secondsIn_withNullIntervalReturnsZero() {
        assertEquals(Seconds.ZERO, Seconds.secondsIn(null));
    }

    @Test
    public void factory_standardSecondsIn_convertsPeriodToSeconds() {
        // 1 day = 86400s, 1 hour = 3600s, 1 min = 60s, 1s = 1s
        Period period = new Period(0, 0, 0, 1, 1, 1, 1, 0);
        Seconds result = Seconds.standardSecondsIn(period);
        assertEquals(86400 + 3600 + 60 + 1, result.getSeconds());
    }

    @Test
    public void factory_standardSecondsIn_withNullPeriodReturnsZero() {
        assertEquals(Seconds.ZERO, Seconds.standardSecondsIn(null));
    }

    @Test
    public void factory_parseSeconds_parsesIsoFormatString() {
        assertEquals(Seconds.seconds(12), Seconds.parseSeconds("PT12S"));
        assertEquals(Seconds.seconds(-12), Seconds.parseSeconds("PT-12S"));
        assertEquals(Seconds.ZERO, Seconds.parseSeconds("PT0S"));
    }

    @Test
    public void factory_parseSeconds_withNullStringReturnsZero() {
        assertEquals(Seconds.ZERO, Seconds.parseSeconds(null));
    }

    // --- Getters and Properties ---

    @Test
    public void getSeconds_returnsTheNumberOfSeconds() {
        assertEquals(0, Seconds.ZERO.getSeconds());
        assertEquals(123, Seconds.seconds(123).getSeconds());
        assertEquals(-456, Seconds.seconds(-456).getSeconds());
    }

    @Test
    public void getFieldType_returnsSeconds() {
        assertEquals(DateTimeFieldType.seconds(), Seconds.ONE.getFieldType());
    }

    @Test
    public void getPeriodType_returnsSecondsPeriodType() {
        assertEquals(PeriodType.seconds(), Seconds.ONE.getPeriodType());
    }

    // --- Comparison Methods ---

    @Test
    public void isGreaterThan_shouldReturnCorrectValue() {
        Seconds ten = Seconds.seconds(10);
        Seconds five = Seconds.seconds(5);
        Seconds minusFive = Seconds.seconds(-5);

        assertTrue(ten.isGreaterThan(five));
        assertFalse(five.isGreaterThan(ten));
        assertFalse(ten.isGreaterThan(ten));

        // A null is treated as zero seconds
        assertTrue(ten.isGreaterThan(null));
        assertFalse(minusFive.isGreaterThan(null));
        assertFalse(Seconds.ZERO.isGreaterThan(null));
    }

    @Test
    public void isLessThan_shouldReturnCorrectValue() {
        Seconds ten = Seconds.seconds(10);
        Seconds five = Seconds.seconds(5);
        Seconds minusFive = Seconds.seconds(-5);

        assertTrue(five.isLessThan(ten));
        assertFalse(ten.isLessThan(five));
        assertFalse(five.isLessThan(five));

        // A null is treated as zero seconds
        assertTrue(minusFive.isLessThan(null));
        assertFalse(ten.isLessThan(null));
        assertFalse(Seconds.ZERO.isLessThan(null));
    }

    // --- Arithmetic Operations ---

    @Test
    public void plus_addsValueCorrectly() {
        Seconds ten = Seconds.seconds(10);
        assertEquals(15, ten.plus(5).getSeconds());
        assertEquals(5, ten.plus(-5).getSeconds());
        assertEquals(15, ten.plus(Seconds.seconds(5)).getSeconds());
        assertEquals(10, ten.plus((Seconds) null).getSeconds()); // null is treated as zero
    }

    @Test
    public void minus_subtractsValueCorrectly() {
        Seconds ten = Seconds.seconds(10);
        assertEquals(5, ten.minus(5).getSeconds());
        assertEquals(15, ten.minus(-5).getSeconds());
        assertEquals(5, ten.minus(Seconds.seconds(5)).getSeconds());
        assertEquals(10, ten.minus((Seconds) null).getSeconds()); // null is treated as zero
    }

    @Test
    public void multipliedBy_multipliesValueCorrectly() {
        Seconds ten = Seconds.seconds(10);
        assertEquals(30, ten.multipliedBy(3).getSeconds());
        assertEquals(-20, ten.multipliedBy(-2).getSeconds());
        assertEquals(0, ten.multipliedBy(0).getSeconds());
    }

    @Test
    public void dividedBy_dividesValueCorrectly() {
        Seconds ten = Seconds.seconds(10);
        assertEquals(5, ten.dividedBy(2).getSeconds());
        assertEquals(3, ten.dividedBy(3).getSeconds()); // Integer division
        assertEquals(-5, ten.dividedBy(-2).getSeconds());
    }

    @Test
    public void negated_returnsSecondsWithOppositeSign() {
        assertEquals(-10, Seconds.seconds(10).negated().getSeconds());
        assertEquals(10, Seconds.seconds(-10).negated().getSeconds());
        assertEquals(0, Seconds.ZERO.negated().getSeconds());
    }

    // --- Conversion Methods ---

    @Test
    public void toStandardWeeks_convertsCorrectly() {
        // 1 week = 7 * 24 * 60 * 60 = 604800 seconds
        assertEquals(1, Seconds.seconds(604800).toStandardWeeks().getWeeks());
        assertEquals(0, Seconds.seconds(604799).toStandardWeeks().getWeeks());
        assertEquals(-1, Seconds.seconds(-604800).toStandardWeeks().getWeeks());
    }

    @Test
    public void toStandardDays_convertsCorrectly() {
        // 1 day = 24 * 60 * 60 = 86400 seconds
        assertEquals(1, Seconds.seconds(86400).toStandardDays().getDays());
        assertEquals(0, Seconds.seconds(86399).toStandardDays().getDays());
        assertEquals(-1, Seconds.seconds(-86400).toStandardDays().getDays());
    }

    @Test
    public void toStandardHours_convertsCorrectly() {
        // 1 hour = 60 * 60 = 3600 seconds
        assertEquals(1, Seconds.seconds(3600).toStandardHours().getHours());
        assertEquals(0, Seconds.seconds(3599).toStandardHours().getHours());
        assertEquals(-1, Seconds.seconds(-3600).toStandardHours().getHours());
    }

    @Test
    public void toStandardMinutes_convertsCorrectly() {
        // 1 minute = 60 seconds
        assertEquals(1, Seconds.seconds(60).toStandardMinutes().getMinutes());
        assertEquals(0, Seconds.seconds(59).toStandardMinutes().getMinutes());
        assertEquals(-1, Seconds.seconds(-60).toStandardMinutes().getMinutes());
    }

    @Test
    public void toStandardDuration_convertsCorrectly() {
        assertEquals(5000L, Seconds.seconds(5).toStandardDuration().getMillis());
        assertEquals(-5000L, Seconds.seconds(-5).toStandardDuration().getMillis());
        assertEquals(0L, Seconds.ZERO.toStandardDuration().getMillis());
    }

    @Test
    public void conversionToLargerUnits_mayLosePrecision() {
        // toStandard... methods use integer division, which can lose precision.
        Seconds original = Seconds.seconds(86399); // 1 day minus 1 second
        Days days = original.toStandardDays();
        assertEquals(0, days.getDays());

        // Converting back to seconds shows the loss of precision
        Seconds convertedBack = days.toStandardSeconds();
        assertEquals(0, convertedBack.getSeconds());
        assertTrue(original.isGreaterThan(convertedBack));
    }

    // --- String Representation ---

    @Test
    public void toString_returnsIso8601Format() {
        assertEquals("PT0S", Seconds.ZERO.toString());
        assertEquals("PT5S", Seconds.seconds(5).toString());
        assertEquals("PT-5S", Seconds.seconds(-5).toString());
        assertEquals("PT" + Integer.MAX_VALUE + "S", Seconds.MAX_VALUE.toString());
        assertEquals("PT" + Integer.MIN_VALUE + "S", Seconds.MIN_VALUE.toString());
    }

    // --- Exception Handling ---

    @Test(expected = IllegalArgumentException.class)
    public void factory_parseSeconds_withInvalidFormatThrowsException() {
        Seconds.parseSeconds("12S"); // Missing "PT" prefix
    }

    @Test(expected = IllegalArgumentException.class)
    public void factory_parseSeconds_withNonSecondComponentThrowsException() {
        // The format is valid ISO, but contains non-second components.
        Seconds.parseSeconds("P1DT12S");
    }

    @Test(expected = ArithmeticException.class)
    public void plus_throwsExceptionOnIntegerOverflow() {
        Seconds.MAX_VALUE.plus(1);
    }

    @Test(expected = ArithmeticException.class)
    public void minus_throwsExceptionOnIntegerOverflow() {
        Seconds.MIN_VALUE.minus(1);
    }

    @Test(expected = ArithmeticException.class)
    public void negated_throwsExceptionWhenNegatingMinValue() {
        Seconds.MIN_VALUE.negated();
    }

    @Test(expected = ArithmeticException.class)
    public void multipliedBy_throwsExceptionOnIntegerOverflow() {
        Seconds.seconds(Integer.MAX_VALUE / 2 + 1).multipliedBy(2);
    }

    @Test(expected = ArithmeticException.class)
    public void dividedBy_throwsExceptionWhenDividingByZero() {
        Seconds.ONE.dividedBy(0);
    }
}