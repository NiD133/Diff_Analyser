package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.*;

/**
 * Test suite for the Joda Time Weeks class.
 * Tests creation, conversion, arithmetic operations, and edge cases.
 */
public class WeeksTest {

    // ========== Creation and Factory Methods ==========
    
    @Test
    public void testCreateWeeksWithPositiveValue() {
        Weeks threeWeeks = Weeks.weeks(3);
        assertEquals(3, threeWeeks.getWeeks());
    }
    
    @Test
    public void testCreateWeeksWithZero() {
        Weeks zeroWeeks = Weeks.weeks(0);
        assertEquals(0, zeroWeeks.getWeeks());
    }
    
    @Test
    public void testCreateWeeksWithNegativeValue() {
        Weeks negativeWeeks = Weeks.weeks(-2820);
        assertEquals(-2820, negativeWeeks.getWeeks());
    }
    
    @Test
    public void testCreateWeeksWithMaxValue() {
        Weeks maxWeeks = Weeks.weeks(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, maxWeeks.getWeeks());
    }
    
    @Test
    public void testCreateWeeksWithMinValue() {
        Weeks minWeeks = Weeks.weeks(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, minWeeks.getWeeks());
    }
    
    @Test
    public void testParseWeeksFromNullString() {
        Weeks parsedWeeks = Weeks.parseWeeks(null);
        assertEquals(0, parsedWeeks.getWeeks());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testParseWeeksFromInvalidString() {
        Weeks.parseWeeks(")%X[WS");
    }
    
    @Test
    public void testWeeksBetweenSameInstant() {
        Instant epoch = Instant.EPOCH;
        Weeks weeksBetween = Weeks.weeksBetween(epoch, epoch);
        assertEquals(0, weeksBetween.getWeeks());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWeeksBetweenNullInstants() {
        Weeks.weeksBetween((ReadableInstant) null, (ReadableInstant) null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testWeeksBetweenNullPartials() {
        Weeks.weeksBetween((ReadablePartial) null, (ReadablePartial) null);
    }
    
    @Test
    public void testWeeksInNullInterval() {
        Weeks weeksInInterval = Weeks.weeksIn(null);
        assertEquals(0, weeksInInterval.getWeeks());
    }
    
    @Test
    public void testStandardWeeksInNullPeriod() {
        Weeks standardWeeks = Weeks.standardWeeksIn(null);
        assertEquals(0, standardWeeks.getWeeks());
    }
    
    @Test
    public void testStandardWeeksFromHours() {
        Hours minValueHours = Hours.MIN_VALUE;
        Weeks weeksFromHours = Weeks.standardWeeksIn(minValueHours);
        assertEquals(-12782640, weeksFromHours.getWeeks());
    }

    // ========== Conversion Methods ==========
    
    @Test
    public void testConvertOneWeekToDays() {
        Weeks oneWeek = Weeks.ONE;
        Days days = oneWeek.toStandardDays();
        assertEquals(7, days.getDays());
    }
    
    @Test
    public void testConvertThreeWeeksToDays() {
        Weeks threeWeeks = Weeks.THREE;
        Days days = threeWeeks.toStandardDays();
        assertEquals(21, days.getDays());
    }
    
    @Test
    public void testConvertZeroWeeksToDays() {
        Weeks zeroWeeks = Weeks.ZERO;
        Days days = zeroWeeks.toStandardDays();
        assertEquals(0, days.getDays());
    }
    
    @Test(expected = ArithmeticException.class)
    public void testConvertMaxWeeksToDaysOverflows() {
        Weeks.MAX_VALUE.toStandardDays();
    }
    
    @Test
    public void testConvertOneWeekToHours() {
        Weeks oneWeek = Weeks.ONE;
        Hours hours = oneWeek.toStandardHours();
        assertEquals(168, hours.getHours()); // 7 days * 24 hours
    }
    
    @Test
    public void testConvertZeroWeeksToHours() {
        Weeks zeroWeeks = Weeks.ZERO;
        Hours hours = zeroWeeks.toStandardHours();
        assertEquals(0, hours.getHours());
    }
    
    @Test(expected = ArithmeticException.class)
    public void testConvertMinWeeksToHoursOverflows() {
        Weeks.MIN_VALUE.toStandardHours();
    }
    
    @Test
    public void testConvertOneWeekToMinutes() {
        Weeks oneWeek = Weeks.ONE;
        Minutes minutes = oneWeek.toStandardMinutes();
        assertEquals(10080, minutes.getMinutes()); // 7 * 24 * 60
    }
    
    @Test
    public void testConvertZeroWeeksToMinutes() {
        Weeks zeroWeeks = Weeks.ZERO;
        Minutes minutes = zeroWeeks.toStandardMinutes();
        assertEquals(0, minutes.getMinutes());
    }
    
    @Test(expected = ArithmeticException.class)
    public void testConvertMaxWeeksToMinutesOverflows() {
        Weeks.MAX_VALUE.toStandardMinutes();
    }
    
    @Test
    public void testConvertOneWeekToSeconds() {
        Weeks oneWeek = Weeks.ONE;
        Seconds seconds = oneWeek.toStandardSeconds();
        assertEquals(604800, seconds.getSeconds()); // 7 * 24 * 60 * 60
    }
    
    @Test
    public void testConvertZeroWeeksToSeconds() {
        Weeks zeroWeeks = Weeks.ZERO;
        Seconds seconds = zeroWeeks.toStandardSeconds();
        assertEquals(0, seconds.getSeconds());
    }
    
    @Test(expected = ArithmeticException.class)
    public void testConvertMinWeeksToSecondsOverflows() {
        Weeks.MIN_VALUE.toStandardSeconds();
    }
    
    @Test
    public void testConvertTwoWeeksToDuration() {
        Weeks twoWeeks = Weeks.TWO;
        Duration duration = twoWeeks.toStandardDuration();
        assertEquals(1209600000L, duration.getMillis()); // 2 weeks in milliseconds
    }
    
    @Test
    public void testConvertMinWeeksToDuration() {
        Weeks minWeeks = Weeks.MIN_VALUE;
        Duration duration = minWeeks.toStandardDuration();
        assertEquals(-15032385536L, duration.getStandardDays());
    }
    
    @Test
    public void testConvertFromMinutesToWeeks() {
        Minutes zeroMinutes = Minutes.ZERO;
        Weeks weeks = zeroMinutes.toStandardWeeks();
        assertEquals(0, weeks.getWeeks());
    }
    
    @Test
    public void testConvertFromSecondsToWeeks() {
        Seconds maxSeconds = Seconds.MAX_VALUE;
        Weeks weeks = maxSeconds.toStandardWeeks();
        assertEquals(35775, weeks.getWeeks());
    }

    // ========== Arithmetic Operations ==========
    
    @Test
    public void testAddWeeksToWeeks() {
        Weeks twoWeeks = Weeks.weeks(2);
        Weeks result = twoWeeks.plus(2);
        assertEquals(4, result.getWeeks());
    }
    
    @Test
    public void testAddZeroWeeks() {
        Weeks threeWeeks = Weeks.THREE;
        Weeks result = threeWeeks.plus(0);
        assertEquals(3, result.getWeeks());
    }
    
    @Test
    public void testAddWeeksObjectToWeeks() {
        Weeks zeroWeeks = Weeks.ZERO;
        Weeks result = zeroWeeks.plus(zeroWeeks);
        assertEquals(0, result.getWeeks());
    }
    
    @Test
    public void testAddNullWeeksObject() {
        Weeks maxWeeks = Weeks.MAX_VALUE;
        Weeks result = maxWeeks.plus((Weeks) null);
        assertEquals(Integer.MAX_VALUE, result.getWeeks());
    }
    
    @Test(expected = ArithmeticException.class)
    public void testAddWeeksOverflow() {
        Weeks maxWeeks = Weeks.MAX_VALUE;
        maxWeeks.plus(maxWeeks);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testAddIntegerOverflow() {
        Weeks minWeeks = Weeks.weeks(Integer.MIN_VALUE);
        minWeeks.plus(Integer.MIN_VALUE);
    }
    
    @Test
    public void testSubtractWeeksFromWeeks() {
        Weeks threeWeeks = Weeks.THREE;
        Weeks twoWeeks = Weeks.TWO;
        Weeks result = threeWeeks.minus(twoWeeks);
        assertEquals(1, result.getWeeks());
    }
    
    @Test
    public void testSubtractZeroWeeks() {
        Weeks zeroWeeks = Weeks.weeks(0);
        Weeks result = zeroWeeks.minus(0);
        assertEquals(0, result.getWeeks());
    }
    
    @Test
    public void testSubtractNullWeeksObject() {
        Weeks minWeeks = Weeks.MIN_VALUE;
        Weeks result = minWeeks.minus((Weeks) null);
        assertSame(minWeeks, result);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSubtractMinValueFromItself() {
        Weeks minWeeks = Weeks.MIN_VALUE;
        minWeeks.minus(minWeeks);
    }
    
    @Test(expected = ArithmeticException.class)
    public void testSubtractNegativeFromMaxValue() {
        Weeks maxWeeks = Weeks.MAX_VALUE;
        maxWeeks.minus(-876);
    }
    
    @Test
    public void testMultiplyWeeksByPositiveNumber() {
        Weeks oneWeek = Weeks.ONE;
        Weeks result = oneWeek.multipliedBy(604800);
        assertEquals(604800, result.getWeeks());
    }
    
    @Test
    public void testMultiplyWeeksByNegativeNumber() {
        Weeks threeWeeks = Weeks.THREE;
        Weeks result = threeWeeks.multipliedBy(-617);
        assertEquals(-1851, result.getWeeks());
    }
    
    @Test
    public void testMultiplyZeroWeeks() {
        Weeks zeroWeeks = Weeks.parseWeeks(null);
        Weeks result = zeroWeeks.multipliedBy(-1060);
        assertEquals(0, result.getWeeks());
    }
    
    @Test(expected = ArithmeticException.class)
    public void testMultiplyOverflow() {
        Weeks maxWeeks = Weeks.MAX_VALUE;
        maxWeeks.multipliedBy(7);
    }
    
    @Test
    public void testDivideWeeksByPositiveNumber() {
        Weeks maxWeeks = Weeks.MAX_VALUE;
        Weeks result = maxWeeks.dividedBy(888);
        assertEquals(2418337, result.getWeeks());
    }
    
    @Test
    public void testDivideWeeksByOne() {
        Weeks zeroWeeks = Weeks.standardWeeksIn(null);
        Weeks result = zeroWeeks.dividedBy(1);
        assertEquals(0, result.getWeeks());
    }
    
    @Test
    public void testDivideWeeksByNegativeOne() {
        Weeks maxWeeks = Weeks.MAX_VALUE;
        Weeks result = maxWeeks.dividedBy(-1);
        assertEquals(-2147483647, result.getWeeks());
    }
    
    @Test(expected = ArithmeticException.class)
    public void testDivideByZero() {
        Weeks twoWeeks = Weeks.TWO;
        twoWeeks.dividedBy(0);
    }
    
    @Test
    public void testNegateZeroWeeks() {
        Weeks zeroWeeks = Weeks.ZERO;
        Weeks result = zeroWeeks.negated();
        assertEquals(0, result.getWeeks());
    }
    
    @Test
    public void testNegateNegativeWeeks() {
        Weeks negativeWeeks = Weeks.weeksIn(null).minus(-2358);
        Weeks result = negativeWeeks.negated();
        assertEquals(-2358, result.getWeeks());
    }
    
    @Test(expected = ArithmeticException.class)
    public void testNegateMinValue() {
        Weeks.MIN_VALUE.negated();
    }

    // ========== Comparison Methods ==========
    
    @Test
    public void testIsGreaterThanWithSmallerValue() {
        Weeks largeWeeks = Weeks.weeks(317351877);
        Weeks minWeeks = Weeks.MIN_VALUE;
        assertTrue(largeWeeks.isGreaterThan(minWeeks));
    }
    
    @Test
    public void testIsGreaterThanWithNull() {
        Weeks threeWeeks = Weeks.THREE;
        assertTrue(threeWeeks.isGreaterThan(null));
    }
    
    @Test
    public void testIsGreaterThanWithSameValue() {
        Weeks weeks = Weeks.weeks(317351877);
        assertFalse(weeks.isGreaterThan(weeks));
    }
    
    @Test
    public void testZeroIsNotGreaterThanNull() {
        Weeks zeroWeeks = Weeks.standardWeeksIn(null);
        assertFalse(zeroWeeks.isGreaterThan(null));
    }
    
    @Test
    public void testIsLessThanWithLargerValue() {
        Weeks zeroWeeks = Weeks.standardWeeksIn(null);
        Weeks maxWeeks = Weeks.MAX_VALUE;
        assertTrue(zeroWeeks.isLessThan(maxWeeks));
    }
    
    @Test
    public void testMinValueIsLessThanNull() {
        Weeks minWeeks = Weeks.MIN_VALUE;
        assertTrue(minWeeks.isLessThan(null));
    }
    
    @Test
    public void testPositiveValueIsNotLessThanNull() {
        Weeks threeWeeks = Weeks.THREE;
        assertFalse(threeWeeks.isLessThan(null));
    }
    
    @Test
    public void testIsLessThanWithSameValue() {
        Weeks zeroWeeks = Weeks.standardWeeksIn(null);
        assertFalse(zeroWeeks.isLessThan(zeroWeeks));
    }

    // ========== Metadata and Utility Methods ==========
    
    @Test
    public void testGetFieldType() {
        Weeks threeWeeks = Weeks.THREE;
        DurationFieldType fieldType = threeWeeks.getFieldType();
        assertEquals("weeks", fieldType.getName());
    }
    
    @Test
    public void testGetPeriodType() {
        Weeks twoWeeks = Weeks.TWO;
        PeriodType periodType = twoWeeks.getPeriodType();
        assertEquals(1, periodType.size());
    }
    
    @Test
    public void testToString() {
        Weeks oneWeek = Weeks.ONE;
        String result = oneWeek.toString();
        assertEquals("P1W", result);
    }
    
    @Test
    public void testSizeProperty() {
        Weeks weeks = Weeks.weeksIn(null);
        assertEquals(1, weeks.size());
    }

    // ========== Edge Cases and Error Conditions ==========
    
    @Test(expected = NullPointerException.class)
    public void testWeeksBetweenPartialWithNullFields() {
        DateTimeFieldType[] fieldTypes = new DateTimeFieldType[9];
        fieldTypes[0] = DateTimeFieldType.dayOfMonth();
        int[] values = new int[0];
        Partial partial = new Partial(null, fieldTypes, values);
        Weeks.weeksBetween(partial, partial);
    }
    
    @Test(expected = NullPointerException.class)
    public void testWeeksBetweenPartialWithAllNullFields() {
        DateTimeFieldType[] fieldTypes = new DateTimeFieldType[9];
        int[] values = new int[0];
        Partial partial = new Partial(null, fieldTypes, values);
        Weeks.weeksBetween(partial, partial);
    }
}