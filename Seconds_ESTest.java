package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.*;

/**
 * Test suite for the Joda-Time Seconds class.
 * Tests cover creation, arithmetic operations, conversions, and comparisons.
 */
public class SecondsTest {

    // ========== Factory Method Tests ==========
    
    @Test
    public void seconds_shouldCreateInstanceWithSpecifiedValue() {
        Seconds oneSecond = Seconds.seconds(1);
        Seconds largeValue = Seconds.seconds(352831696);
        
        assertEquals(1, oneSecond.getSeconds());
        assertEquals(352831696, largeValue.getSeconds());
    }
    
    @Test
    public void seconds_shouldCreateInstanceWithMinAndMaxValues() {
        Seconds minValue = Seconds.seconds(Integer.MIN_VALUE);
        Seconds maxValue = Seconds.seconds(Integer.MAX_VALUE);
        
        assertEquals(Integer.MIN_VALUE, minValue.getSeconds());
        assertEquals(Integer.MAX_VALUE, maxValue.getSeconds());
    }

    // ========== Constant Tests ==========
    
    @Test
    public void constants_shouldHaveCorrectValues() {
        assertEquals(0, Seconds.ZERO.getSeconds());
        assertEquals(1, Seconds.ONE.getSeconds());
        assertEquals(2, Seconds.TWO.getSeconds());
        assertEquals(3, Seconds.THREE.getSeconds());
        assertEquals(Integer.MAX_VALUE, Seconds.MAX_VALUE.getSeconds());
        assertEquals(Integer.MIN_VALUE, Seconds.MIN_VALUE.getSeconds());
    }

    // ========== Parsing Tests ==========
    
    @Test
    public void parseSeconds_shouldReturnZeroForNullInput() {
        Seconds result = Seconds.parseSeconds(null);
        assertEquals(0, result.getSeconds());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void parseSeconds_shouldThrowExceptionForEmptyString() {
        Seconds.parseSeconds("");
    }

    // ========== Between/In Factory Methods Tests ==========
    
    @Test
    public void secondsBetween_shouldReturnZeroForSameInstant() {
        Instant now = new Instant();
        Seconds result = Seconds.secondsBetween(now, now);
        assertEquals(0, result.getSeconds());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void secondsBetween_shouldThrowExceptionForNullInstants() {
        Seconds.secondsBetween((ReadableInstant) null, (ReadableInstant) null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void secondsBetween_shouldThrowExceptionForNullPartials() {
        Seconds.secondsBetween((ReadablePartial) null, (ReadablePartial) null);
    }
    
    @Test
    public void secondsIn_shouldReturnZeroForNullInterval() {
        Seconds result = Seconds.secondsIn(null);
        assertEquals(0, result.getSeconds());
    }
    
    @Test
    public void standardSecondsIn_shouldReturnZeroForNullPeriod() {
        Seconds result = Seconds.standardSecondsIn(null);
        assertEquals(0, result.getSeconds());
    }
    
    @Test
    public void standardSecondsIn_shouldConvertFromOtherPeriodTypes() {
        Seconds zeroSeconds = Seconds.ZERO;
        Seconds result = Seconds.standardSecondsIn(zeroSeconds);
        assertEquals(0, result.getSeconds());
    }

    // ========== Arithmetic Operations Tests ==========
    
    @Test
    public void plus_shouldAddSecondsCorrectly() {
        Seconds twoSeconds = Seconds.seconds(2);
        Seconds negativeSeconds = Seconds.seconds(-2530);
        
        Seconds result1 = Seconds.ZERO.plus(twoSeconds);
        Seconds result2 = negativeSeconds.plus(negativeSeconds);
        
        assertEquals(2, result1.getSeconds());
        assertEquals(-5060, result2.getSeconds());
    }
    
    @Test
    public void plus_shouldHandleNullAsZero() {
        Seconds result = Seconds.ONE.plus((Seconds) null);
        assertEquals(1, result.getSeconds());
    }
    
    @Test
    public void plus_shouldAddIntegerSecondsCorrectly() {
        Seconds base = Seconds.seconds(352831696);
        Seconds result = base.plus(3058);
        assertEquals(352834754, result.getSeconds());
    }
    
    @Test(expected = ArithmeticException.class)
    public void plus_shouldThrowExceptionOnOverflow() {
        Seconds.MAX_VALUE.plus(Seconds.MAX_VALUE);
    }
    
    @Test(expected = ArithmeticException.class)
    public void plus_shouldThrowExceptionOnUnderflow() {
        Seconds.MIN_VALUE.plus(-674);
    }
    
    @Test
    public void minus_shouldSubtractSecondsCorrectly() {
        Seconds base = Seconds.seconds(352831696);
        Seconds toSubtract = base.plus(3058);
        
        Seconds result = base.minus(toSubtract);
        assertEquals(-3058, result.getSeconds());
    }
    
    @Test
    public void minus_shouldHandleNullAsZero() {
        Seconds result = Seconds.TWO.minus((Seconds) null);
        assertEquals(2, result.getSeconds());
    }
    
    @Test
    public void minus_shouldSubtractIntegerSecondsCorrectly() {
        Seconds result1 = Seconds.ZERO.minus(0);
        Seconds result2 = Seconds.ZERO.minus(1);
        Seconds result3 = Seconds.MAX_VALUE.minus(352831696);
        
        assertEquals(0, result1.getSeconds());
        assertEquals(-1, result2.getSeconds());
        assertEquals(1794651951, result3.getSeconds());
    }
    
    @Test(expected = ArithmeticException.class)
    public void minus_shouldThrowExceptionWhenSubtractingMinFromMax() {
        Seconds.MAX_VALUE.minus(Seconds.MIN_VALUE);
    }
    
    @Test(expected = ArithmeticException.class)
    public void minus_shouldThrowExceptionOnOverflow() {
        Seconds.THREE.minus(-2147483646);
    }
    
    @Test
    public void multipliedBy_shouldMultiplyCorrectly() {
        Seconds base = Seconds.seconds(1566);
        
        Seconds result1 = base.multipliedBy(518);
        Seconds result2 = base.multipliedBy(0);
        Seconds result3 = Seconds.ONE.multipliedBy(-701);
        
        assertEquals(811188, result1.getSeconds());
        assertEquals(0, result2.getSeconds());
        assertEquals(-701, result3.getSeconds());
    }
    
    @Test(expected = ArithmeticException.class)
    public void multipliedBy_shouldThrowExceptionOnOverflow() {
        Seconds.MIN_VALUE.multipliedBy(-674);
    }
    
    @Test
    public void dividedBy_shouldDivideCorrectly() {
        Seconds base = Seconds.seconds(1804);
        
        Seconds result1 = base.dividedBy(1804);
        Seconds result2 = Seconds.MIN_VALUE.dividedBy(3600);
        
        assertEquals(1, result1.getSeconds());
        assertEquals(-596523, result2.getSeconds());
    }
    
    @Test(expected = ArithmeticException.class)
    public void dividedBy_shouldThrowExceptionWhenDividingByZero() {
        Seconds.MAX_VALUE.dividedBy(0);
    }
    
    @Test
    public void negated_shouldNegateValueCorrectly() {
        Seconds positive = Seconds.THREE;
        Seconds negative = Seconds.seconds(-2530);
        Seconds zero = Seconds.ZERO;
        
        assertEquals(-3, positive.negated().getSeconds());
        assertEquals(2530, negative.negated().getSeconds());
        assertEquals(0, zero.negated().getSeconds());
    }
    
    @Test(expected = ArithmeticException.class)
    public void negated_shouldThrowExceptionForMinValue() {
        Seconds.MIN_VALUE.negated();
    }

    // ========== Comparison Tests ==========
    
    @Test
    public void isGreaterThan_shouldCompareCorrectly() {
        Seconds positive = Seconds.seconds(2);
        Seconds negative = Seconds.seconds(-2147483646);
        
        assertTrue(positive.isGreaterThan(Seconds.MIN_VALUE));
        assertTrue(Seconds.ONE.isGreaterThan(null)); // null treated as zero
        assertFalse(negative.isGreaterThan(null));
        assertFalse(Seconds.ONE.isGreaterThan(Seconds.ONE));
    }
    
    @Test
    public void isLessThan_shouldCompareCorrectly() {
        Seconds zero = Seconds.ZERO;
        Seconds negative = Seconds.seconds(-1300);
        
        assertTrue(zero.isLessThan(Seconds.THREE));
        assertTrue(negative.isLessThan(null)); // null treated as zero
        assertFalse(Seconds.MAX_VALUE.isLessThan(null));
        assertFalse(zero.isLessThan(zero));
    }

    // ========== Conversion Tests ==========
    
    @Test
    public void toStandardWeeks_shouldConvertCorrectly() {
        Seconds largeSeconds = Seconds.seconds(352831696);
        Seconds minValueSeconds = Seconds.MIN_VALUE;
        Seconds smallSeconds = Seconds.TWO;
        
        assertEquals(583, largeSeconds.toStandardWeeks().getWeeks());
        assertEquals(-3550, minValueSeconds.toStandardWeeks().getWeeks());
        assertEquals(0, smallSeconds.toStandardWeeks().getWeeks());
    }
    
    @Test
    public void toStandardDays_shouldConvertCorrectly() {
        Days fiveDays = Days.FIVE;
        Seconds fiveDaysInSeconds = fiveDays.toStandardSeconds();
        
        assertEquals(432000, fiveDaysInSeconds.getSeconds());
        assertEquals(5, fiveDaysInSeconds.toStandardDays().getDays());
        assertEquals(0, Seconds.TWO.toStandardDays().getDays());
    }
    
    @Test
    public void toStandardHours_shouldConvertCorrectly() {
        Seconds largeSeconds = Seconds.seconds(690562340);
        
        assertEquals(0, Seconds.ZERO.toStandardHours().getHours());
        assertEquals(191822, largeSeconds.toStandardHours().getHours());
        assertEquals(-596523, Seconds.MIN_VALUE.toStandardHours().getHours());
    }
    
    @Test
    public void toStandardMinutes_shouldConvertCorrectly() {
        Seconds someSeconds = Seconds.seconds(1575);
        
        assertEquals(26, someSeconds.toStandardMinutes().getMinutes());
        assertEquals(-35791394, Seconds.MIN_VALUE.toStandardMinutes().getMinutes());
        assertEquals(0, Seconds.TWO.toStandardMinutes().getMinutes());
    }
    
    @Test
    public void toStandardDuration_shouldConvertCorrectly() {
        Duration zeroDuration = Seconds.ZERO.toStandardDuration();
        Duration minDuration = Seconds.MIN_VALUE.toStandardDuration();
        Duration maxDuration = Seconds.MAX_VALUE.toStandardDuration();
        
        assertEquals(0L, zeroDuration.getStandardSeconds());
        assertEquals(-2147483648000L, minDuration.getMillis());
        assertEquals(24855L, maxDuration.getStandardDays());
    }

    // ========== Metadata Tests ==========
    
    @Test
    public void getPeriodType_shouldReturnSecondsType() {
        PeriodType periodType = Seconds.ONE.getPeriodType();
        assertEquals(1, periodType.size());
    }
    
    @Test
    public void getFieldType_shouldReturnCorrectType() {
        // This test verifies the field type is accessible
        Seconds.seconds(2).getFieldType();
    }
    
    @Test
    public void toString_shouldFormatCorrectly() {
        String result = Seconds.MIN_VALUE.toString();
        assertEquals("PT-2147483648S", result);
    }

    // ========== Complex Conversion Chain Tests ==========
    
    @Test
    public void conversionChain_shouldMaintainConsistency() {
        // Test: Hours -> Days -> Seconds -> Days
        Hours oneHour = Hours.ONE;
        Days zeroDays = oneHour.toStandardDays(); // 1 hour = 0 days
        Seconds zeroSeconds = zeroDays.toStandardSeconds();
        
        assertEquals(0, zeroSeconds.getSeconds());
    }
    
    @Test
    public void conversionChain_shouldHandleComplexArithmetic() {
        // Test: Weeks -> Days -> Seconds -> arithmetic -> Days
        Weeks twoWeeks = Weeks.TWO;
        Days fourteenDays = twoWeeks.toStandardDays();
        Seconds secondsInTwoWeeks = fourteenDays.toStandardSeconds();
        Seconds afterSubtraction = secondsInTwoWeeks.plus(-2147138048);
        Days finalDays = afterSubtraction.toStandardDays();
        
        assertEquals(1209600, secondsInTwoWeeks.getSeconds());
        assertEquals(-2145928448, afterSubtraction.getSeconds());
        assertEquals(-24837, finalDays.getDays());
    }
    
    @Test
    public void conversionChain_shouldHandleMinValueConversions() {
        Seconds minValue = Seconds.MIN_VALUE;
        Weeks weeksFromMin = minValue.toStandardWeeks();
        Seconds backToSeconds = weeksFromMin.toStandardSeconds();
        
        assertEquals(-3550, weeksFromMin.getWeeks());
        assertEquals(-2147040000, backToSeconds.getSeconds());
        assertFalse(backToSeconds.isLessThan(minValue));
    }
}