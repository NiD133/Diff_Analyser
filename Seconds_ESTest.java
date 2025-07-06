package org.joda.time;

import org.junit.Test;
import static org.junit.Assert.*;

public class SecondsTest {

    @Test
    public void testMinus_basic() {
        Seconds seconds5 = Seconds.seconds(5);
        Seconds seconds2 = Seconds.seconds(2);
        Seconds result = seconds5.minus(seconds2);
        assertEquals(3, result.getSeconds());
    }

    @Test
    public void testMinus_zero() {
        Seconds seconds = Seconds.seconds(5);
        Seconds result = seconds.minus(0);
        assertEquals(5, result.getSeconds());
    }

    @Test
    public void testMinus_negative() {
        Seconds seconds = Seconds.seconds(5);
        Seconds result = seconds.minus(-2);
        assertEquals(7, result.getSeconds());
    }

    @Test
    public void testPlus_basic() {
        Seconds seconds5 = Seconds.seconds(5);
        Seconds seconds2 = Seconds.seconds(2);
        Seconds result = seconds5.plus(seconds2);
        assertEquals(7, result.getSeconds());
    }

    @Test
    public void testPlus_zero() {
        Seconds seconds = Seconds.seconds(5);
        Seconds result = seconds.plus(0);
        assertEquals(5, result.getSeconds());
    }

    @Test
    public void testPlus_negative() {
        Seconds seconds = Seconds.seconds(5);
        Seconds result = seconds.plus(-2);
        assertEquals(3, result.getSeconds());
    }

    @Test
    public void testMultipliedBy() {
        Seconds seconds = Seconds.seconds(5);
        Seconds result = seconds.multipliedBy(3);
        assertEquals(15, result.getSeconds());
    }

    @Test
    public void testDividedBy() {
        Seconds seconds = Seconds.seconds(15);
        Seconds result = seconds.dividedBy(3);
        assertEquals(5, result.getSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void testDividedBy_zero() {
        Seconds seconds = Seconds.seconds(15);
        seconds.dividedBy(0);
    }

    @Test
    public void testNegated() {
        Seconds seconds = Seconds.seconds(5);
        Seconds result = seconds.negated();
        assertEquals(-5, result.getSeconds());
    }

    @Test
    public void testIsGreaterThan() {
        Seconds seconds5 = Seconds.seconds(5);
        Seconds seconds2 = Seconds.seconds(2);
        assertTrue(seconds5.isGreaterThan(seconds2));
        assertFalse(seconds2.isGreaterThan(seconds5));
    }

    @Test
    public void testIsLessThan() {
        Seconds seconds5 = Seconds.seconds(5);
        Seconds seconds2 = Seconds.seconds(2);
        assertTrue(seconds2.isLessThan(seconds5));
        assertFalse(seconds5.isLessThan(seconds2));
    }

    @Test
    public void testEquals() {
        Seconds seconds5a = Seconds.seconds(5);
        Seconds seconds5b = Seconds.seconds(5);
        Seconds seconds2 = Seconds.seconds(2);
        assertEquals(seconds5a, seconds5b);
        assertNotEquals(seconds5a, seconds2);
    }

    @Test
    public void testToString() {
        Seconds seconds = Seconds.seconds(5);
        assertEquals("PT5S", seconds.toString());
    }

    @Test
    public void testToStandardMinutes() {
        Seconds seconds = Seconds.seconds(120);
        Minutes minutes = seconds.toStandardMinutes();
        assertEquals(2, minutes.getMinutes());
    }

    @Test
    public void testToStandardHours() {
        Seconds seconds = Seconds.seconds(7200);
        Hours hours = seconds.toStandardHours();
        assertEquals(2, hours.getHours());
    }

    @Test
    public void testToStandardDays() {
        Seconds seconds = Seconds.seconds(172800);
        Days days = seconds.toStandardDays();
        assertEquals(2, days.getDays());
    }

    @Test
    public void testToStandardWeeks() {
        Seconds seconds = Seconds.seconds(1209600);
        Weeks weeks = seconds.toStandardWeeks();
        assertEquals(2, weeks.getWeeks());
    }

    @Test
    public void testToStandardDuration() {
        Seconds seconds = Seconds.seconds(5);
        Duration duration = seconds.toStandardDuration();
        assertEquals(5000, duration.getMillis());
    }

    @Test
    public void testSecondsBetween_ReadableInstant() {
        MutableDateTime start = new MutableDateTime(2024, 1, 1, 0, 0, 0, 0);
        MutableDateTime end = new MutableDateTime(2024, 1, 1, 0, 0, 10, 0);
        Seconds seconds = Seconds.secondsBetween(start, end);
        assertEquals(10, seconds.getSeconds());
    }

    @Test
    public void testSecondsBetween_ReadablePartial() {
        LocalTime start = new LocalTime(0, 0, 0);
        LocalTime end = new LocalTime(0, 0, 10);
        Seconds seconds = Seconds.secondsBetween(start, end);
        assertEquals(10, seconds.getSeconds());
    }

    @Test
    public void testStandardSecondsIn_ReadablePeriod() {
        Period period = new Period(0, 0, 0, 1, 0, 0, 0, 0); // 1 day
        Seconds seconds = Seconds.standardSecondsIn(period);
        assertEquals(86400, seconds.getSeconds());
    }

    @Test
    public void testParseSeconds() {
        Seconds seconds = Seconds.parseSeconds("PT10S");
        assertEquals(10, seconds.getSeconds());
    }

    @Test
    public void testFactory_seconds() {
        Seconds seconds = Seconds.seconds(10);
        assertEquals(10, seconds.getSeconds());
    }

    @Test
    public void testGetFieldType() {
        Seconds seconds = Seconds.seconds(10);
        DurationFieldType fieldType = seconds.getFieldType();
        assertEquals(DurationFieldType.seconds(), fieldType);
    }

    @Test
    public void testGetPeriodType() {
        Seconds seconds = Seconds.seconds(10);
        PeriodType periodType = seconds.getPeriodType();
        assertEquals(PeriodType.seconds(), periodType);
    }

    @Test
    public void testSecondsIn_ReadableInterval(){
        MutableDateTime start = new MutableDateTime(2024, 1, 1, 0, 0, 0, 0);
        MutableDateTime end = new MutableDateTime(2024, 1, 1, 0, 0, 10, 0);
        Interval interval = new Interval(start, end);
        Seconds seconds = Seconds.secondsIn(interval);
        assertEquals(10, seconds.getSeconds());
    }

    @Test
    public void testGetValue(){
        Seconds seconds = Seconds.seconds(10);
        assertEquals(10, seconds.getSeconds());
    }

    @Test
    public void testMinus_null(){
        Seconds seconds = Seconds.seconds(10);
        Seconds result = seconds.minus((Seconds)null);
        assertEquals(10, result.getSeconds());
    }

    @Test
    public void testPlus_null(){
        Seconds seconds = Seconds.seconds(10);
        Seconds result = seconds.plus((Seconds)null);
        assertEquals(10, result.getSeconds());
    }
}