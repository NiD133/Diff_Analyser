package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.*;

/**
 * Test suite for the Minutes class functionality.
 * Tests cover creation, arithmetic operations, conversions, and edge cases.
 */
public class MinutesTest {

    // ========== Factory Method Tests ==========
    
    @Test
    public void createMinutes_withPositiveValue_shouldReturnCorrectMinutes() {
        Minutes minutes = Minutes.minutes(5);
        assertEquals(5, minutes.getMinutes());
    }
    
    @Test
    public void createMinutes_withZero_shouldReturnZeroMinutes() {
        Minutes minutes = Minutes.minutes(0);
        assertEquals(0, minutes.getMinutes());
    }
    
    @Test
    public void createMinutes_withNegativeValue_shouldReturnNegativeMinutes() {
        Minutes minutes = Minutes.minutes(-10);
        assertEquals(-10, minutes.getMinutes());
    }
    
    @Test
    public void createMinutes_withMaxValue_shouldReturnMaxMinutes() {
        Minutes minutes = Minutes.minutes(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, minutes.getMinutes());
    }
    
    @Test
    public void createMinutes_withMinValue_shouldReturnMinMinutes() {
        Minutes minutes = Minutes.minutes(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, minutes.getMinutes());
    }

    // ========== Constants Tests ==========
    
    @Test
    public void constants_shouldHaveCorrectValues() {
        assertEquals(0, Minutes.ZERO.getMinutes());
        assertEquals(1, Minutes.ONE.getMinutes());
        assertEquals(2, Minutes.TWO.getMinutes());
        assertEquals(3, Minutes.THREE.getMinutes());
        assertEquals(Integer.MAX_VALUE, Minutes.MAX_VALUE.getMinutes());
        assertEquals(Integer.MIN_VALUE, Minutes.MIN_VALUE.getMinutes());
    }

    // ========== Parsing Tests ==========
    
    @Test
    public void parseMinutes_withNull_shouldReturnZero() {
        Minutes minutes = Minutes.parseMinutes(null);
        assertEquals(0, minutes.getMinutes());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void parseMinutes_withInvalidFormat_shouldThrowException() {
        Minutes.parseMinutes("invalid");
    }

    // ========== Between Methods Tests ==========
    
    @Test
    public void minutesBetween_withSameInstants_shouldReturnZero() {
        Instant now = Instant.now();
        Minutes minutes = Minutes.minutesBetween(now, now);
        assertEquals(0, minutes.getMinutes());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void minutesBetween_withNullInstants_shouldThrowException() {
        Minutes.minutesBetween((ReadableInstant) null, (ReadableInstant) null);
    }
    
    @Test
    public void minutesBetween_withSamePartials_shouldReturnZero() {
        DateTimeFieldType[] fields = {DateTimeFieldType.year()};
        int[] values = new int[1];
        Partial partial = new Partial(null, fields, values);
        
        Minutes minutes = Minutes.minutesBetween(partial, partial);
        assertEquals(0, minutes.getMinutes());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void minutesBetween_withNullPartials_shouldThrowException() {
        Minutes.minutesBetween((ReadablePartial) null, (ReadablePartial) null);
    }
    
    @Test
    public void minutesIn_withNullInterval_shouldReturnZero() {
        Minutes minutes = Minutes.minutesIn(null);
        assertEquals(0, minutes.getMinutes());
    }

    // ========== Conversion Tests ==========
    
    @Test
    public void standardMinutesIn_withNullPeriod_shouldReturnZero() {
        Minutes minutes = Minutes.standardMinutesIn(null);
        assertEquals(0, minutes.getMinutes());
    }
    
    @Test
    public void standardMinutesIn_withDays_shouldConvertCorrectly() {
        Days threeDays = Days.THREE;
        Minutes minutes = Minutes.standardMinutesIn(threeDays);
        assertEquals(4320, minutes.getMinutes()); // 3 days * 24 hours * 60 minutes
    }
    
    @Test
    public void toStandardSeconds_withOneMinute_shouldReturn60Seconds() {
        Minutes oneMinute = Minutes.ONE;
        Seconds seconds = oneMinute.toStandardSeconds();
        assertEquals(60, seconds.getSeconds());
    }
    
    @Test
    public void toStandardSeconds_withZero_shouldReturnZeroSeconds() {
        Minutes zero = Minutes.ZERO;
        Seconds seconds = zero.toStandardSeconds();
        assertEquals(0, seconds.getSeconds());
    }
    
    @Test(expected = ArithmeticException.class)
    public void toStandardSeconds_withMinValue_shouldThrowOverflowException() {
        Minutes.MIN_VALUE.toStandardSeconds();
    }
    
    @Test
    public void toStandardHours_withZero_shouldReturnZeroHours() {
        Hours hours = Minutes.ZERO.toStandardHours();
        assertEquals(0, hours.getHours());
    }
    
    @Test
    public void toStandardHours_with1431Minutes_shouldReturn23Hours() {
        Minutes minutes = Minutes.minutes(1431);
        Hours hours = minutes.toStandardHours();
        assertEquals(23, hours.getHours());
    }
    
    @Test
    public void toStandardDays_withMaxValue_shouldConvertCorrectly() {
        Days days = Minutes.MAX_VALUE.toStandardDays();
        assertEquals(1491308, days.getDays());
    }
    
    @Test
    public void toStandardWeeks_withMaxValue_shouldConvertCorrectly() {
        Weeks weeks = Minutes.MAX_VALUE.toStandardWeeks();
        assertEquals(213044, weeks.getWeeks());
    }
    
    @Test
    public void toStandardWeeks_withOneMinute_shouldReturnZeroWeeks() {
        Weeks weeks = Minutes.ONE.toStandardWeeks();
        assertEquals(0, weeks.getWeeks());
    }
    
    @Test
    public void toStandardDuration_withOneMinute_shouldReturn60000Millis() {
        Duration duration = Minutes.ONE.toStandardDuration();
        assertEquals(60000L, duration.getMillis());
    }

    // ========== Arithmetic Operations Tests ==========
    
    @Test
    public void plus_withPositiveMinutes_shouldAddCorrectly() {
        Minutes two = Minutes.TWO;
        Minutes result = two.plus(3);
        assertEquals(5, result.getMinutes());
    }
    
    @Test
    public void plus_withNegativeMinutes_shouldSubtractCorrectly() {
        Minutes two = Minutes.TWO;
        Minutes result = two.plus(-2);
        assertEquals(0, result.getMinutes());
    }
    
    @Test
    public void plus_withNullMinutes_shouldReturnSameInstance() {
        Minutes zero = Minutes.ZERO;
        Minutes result = zero.plus((Minutes) null);
        assertSame(zero, result);
    }
    
    @Test
    public void plus_withZero_shouldReturnSameInstance() {
        Minutes maxValue = Minutes.MAX_VALUE;
        Minutes result = maxValue.plus(0);
        assertEquals(Integer.MAX_VALUE, result.getMinutes());
    }
    
    @Test(expected = ArithmeticException.class)
    public void plus_withOverflow_shouldThrowException() {
        Minutes.MAX_VALUE.plus(Minutes.MAX_VALUE);
    }
    
    @Test(expected = ArithmeticException.class)
    public void plus_withIntegerOverflow_shouldThrowException() {
        Minutes.TWO.plus(Integer.MAX_VALUE);
    }
    
    @Test
    public void minus_withPositiveMinutes_shouldSubtractCorrectly() {
        Minutes one = Minutes.ONE;
        Minutes result = one.minus(192);
        assertEquals(-191, result.getMinutes());
    }
    
    @Test
    public void minus_withNegativeMinutes_shouldAddCorrectly() {
        Minutes one = Minutes.ONE;
        Minutes result = one.minus(-241);
        assertEquals(242, result.getMinutes());
    }
    
    @Test
    public void minus_withNullMinutes_shouldReturnSameInstance() {
        Minutes zero = Minutes.ZERO;
        Minutes result = zero.minus((Minutes) null);
        assertSame(zero, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void minus_withMinValueFromItself_shouldThrowException() {
        Minutes.MIN_VALUE.minus(Minutes.MIN_VALUE);
    }
    
    @Test(expected = ArithmeticException.class)
    public void minus_withOverflow_shouldThrowException() {
        Minutes.MAX_VALUE.minus(-2550);
    }
    
    @Test
    public void multipliedBy_withPositiveScalar_shouldMultiplyCorrectly() {
        Minutes minutes = Minutes.minutes(1431);
        Minutes result = minutes.multipliedBy(3012);
        assertEquals(4310172, result.getMinutes());
    }
    
    @Test
    public void multipliedBy_withNegativeScalar_shouldReturnNegativeResult() {
        Minutes one = Minutes.ONE;
        Minutes result = one.multipliedBy(-5447);
        assertEquals(-5447, result.getMinutes());
    }
    
    @Test
    public void multipliedBy_withZero_shouldReturnZero() {
        Minutes minutes = Minutes.minutes(100);
        Minutes result = minutes.multipliedBy(0);
        assertEquals(0, result.getMinutes());
    }
    
    @Test(expected = ArithmeticException.class)
    public void multipliedBy_withOverflow_shouldThrowException() {
        Minutes.MAX_VALUE.multipliedBy(-4433);
    }
    
    @Test
    public void dividedBy_withPositiveDivisor_shouldDivideCorrectly() {
        Minutes three = Minutes.THREE;
        Minutes result = three.dividedBy(2);
        assertEquals(1, result.getMinutes()); // Integer division: 3/2 = 1
    }
    
    @Test
    public void dividedBy_withOne_shouldReturnSameValue() {
        Minutes zero = Minutes.ZERO;
        Minutes result = zero.dividedBy(1);
        assertSame(zero, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void dividedBy_withZero_shouldThrowException() {
        Minutes.ZERO.dividedBy(0);
    }
    
    @Test
    public void negated_withPositiveValue_shouldReturnNegative() {
        Minutes three = Minutes.THREE;
        Minutes result = three.negated();
        assertEquals(-3, result.getMinutes());
    }
    
    @Test
    public void negated_withZero_shouldReturnZero() {
        Minutes zero = Minutes.ZERO;
        Minutes result = zero.negated();
        assertEquals(0, result.getMinutes());
    }
    
    @Test(expected = ArithmeticException.class)
    public void negated_withMinValue_shouldThrowException() {
        Minutes.MIN_VALUE.negated();
    }

    // ========== Comparison Tests ==========
    
    @Test
    public void isGreaterThan_withSmallerValue_shouldReturnTrue() {
        Minutes two = Minutes.TWO;
        Minutes zero = Minutes.ZERO;
        assertTrue(two.isGreaterThan(zero));
    }
    
    @Test
    public void isGreaterThan_withLargerValue_shouldReturnFalse() {
        Minutes two = Minutes.TWO;
        Minutes three = Minutes.THREE;
        assertFalse(two.isGreaterThan(three));
    }
    
    @Test
    public void isGreaterThan_withSameValue_shouldReturnFalse() {
        Minutes one = Minutes.ONE;
        assertFalse(one.isGreaterThan(one));
    }
    
    @Test
    public void isGreaterThan_withNull_shouldTreatAsZero() {
        Minutes positive = Minutes.minutes(1431);
        Minutes negative = Minutes.MIN_VALUE;
        
        assertTrue(positive.isGreaterThan(null));
        assertFalse(negative.isGreaterThan(null));
    }
    
    @Test
    public void isLessThan_withLargerValue_shouldReturnTrue() {
        Minutes two = Minutes.TWO;
        Minutes thirtyOne = Minutes.minutes(31);
        assertTrue(two.isLessThan(thirtyOne));
    }
    
    @Test
    public void isLessThan_withSmallerValue_shouldReturnFalse() {
        Minutes maxValue = Minutes.MAX_VALUE;
        Minutes two = Minutes.TWO;
        assertFalse(maxValue.isLessThan(two));
    }
    
    @Test
    public void isLessThan_withSameValue_shouldReturnFalse() {
        Minutes thirtyOne = Minutes.minutes(31);
        assertFalse(thirtyOne.isLessThan(thirtyOne));
    }
    
    @Test
    public void isLessThan_withNull_shouldTreatAsZero() {
        Minutes positive = Minutes.ONE;
        Minutes negative = Minutes.MIN_VALUE;
        
        assertFalse(positive.isLessThan(null));
        assertTrue(negative.isLessThan(null));
    }

    // ========== Metadata Tests ==========
    
    @Test
    public void getFieldType_shouldReturnMinutesFieldType() {
        DurationFieldType fieldType = Minutes.MIN_VALUE.getFieldType();
        assertEquals("minutes", fieldType.toString());
    }
    
    @Test
    public void getPeriodType_shouldReturnCorrectType() {
        PeriodType periodType = Minutes.MAX_VALUE.getPeriodType();
        assertEquals(1, periodType.size());
    }
    
    @Test
    public void size_shouldReturnOne() {
        assertEquals(1, Minutes.ZERO.size());
    }
    
    @Test
    public void toString_shouldReturnISO8601Format() {
        String result = Minutes.MAX_VALUE.toString();
        assertEquals("PT2147483647M", result);
    }

    // ========== Edge Cases and Error Conditions ==========
    
    @Test
    public void conversionFromSeconds_shouldWorkCorrectly() {
        Seconds maxSeconds = Seconds.MAX_VALUE;
        Minutes minutes = maxSeconds.toStandardMinutes();
        assertEquals(35791394, minutes.getMinutes());
        
        Seconds minSeconds = Seconds.MIN_VALUE;
        Minutes minutesFromMin = minSeconds.toStandardMinutes();
        Seconds backToSeconds = minutesFromMin.toStandardSeconds();
        assertEquals(-35791394, minutesFromMin.getMinutes());
        assertEquals(-2147483640, backToSeconds.getSeconds());
    }
    
    @Test
    public void complexArithmeticOperations_shouldWorkCorrectly() {
        Minutes maxValue = Minutes.MAX_VALUE;
        Minutes divided = maxValue.dividedBy(-82);
        Minutes doubled = divided.plus(divided);
        
        assertEquals(-26188824, divided.getMinutes());
        assertEquals(-52377648, doubled.getMinutes());
    }
}