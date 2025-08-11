/*
 *  Copyright 2001-2010 Stephen Colebourne
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.joda.time;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link Weeks} class.
 */
public class WeeksTest {

    private static final long WEEKS_IN_MILLIS = 7L * 24L * 60L * 60L * 1000L;

    //-----------------------------------------------------------------------
    // Factory methods
    //-----------------------------------------------------------------------

    @Test
    public void testFactory_weeks_int() {
        assertEquals(0, Weeks.weeks(0).getWeeks());
        assertEquals(1, Weeks.weeks(1).getWeeks());
        assertEquals(Integer.MAX_VALUE, Weeks.weeks(Integer.MAX_VALUE).getWeeks());
        
        // Test cached constants
        assertSame(Weeks.ZERO, Weeks.weeks(0));
        assertSame(Weeks.ONE, Weeks.weeks(1));
        assertSame(Weeks.TWO, Weeks.weeks(2));
        assertSame(Weeks.THREE, Weeks.weeks(3));
    }

    @Test
    public void testFactory_weeksBetween_instants() {
        Instant start = new Instant(0L);
        Instant end = new Instant(3 * WEEKS_IN_MILLIS);

        assertEquals(3, Weeks.weeksBetween(start, end).getWeeks());
        assertEquals(0, Weeks.weeksBetween(start, start).getWeeks());
        assertEquals(-3, Weeks.weeksBetween(end, start).getWeeks());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactory_weeksBetween_nullInstants_throwsException() {
        Weeks.weeksBetween(null, new Instant());
    }

    @Test
    public void testFactory_weeksIn_interval() {
        Interval interval = new Interval(0L, 3 * WEEKS_IN_MILLIS);
        assertEquals(3, Weeks.weeksIn(interval).getWeeks());
        assertEquals(0, Weeks.weeksIn(null).getWeeks());
    }

    @Test
    public void testFactory_standardWeeksIn_period() {
        assertEquals(0, Weeks.standardWeeksIn(null).getWeeks());
        assertEquals(2, Weeks.standardWeeksIn(Days.days(15)).getWeeks()); // 15 days is 2 full weeks
        assertEquals(1, Weeks.standardWeeksIn(Hours.hours(168)).getWeeks()); // 168 hours is 1 week
    }

    @Test
    public void testFactory_parseWeeks() {
        assertEquals(0, Weeks.parseWeeks(null).getWeeks());
        assertEquals(5, Weeks.parseWeeks("P5W").getWeeks());
        assertEquals(-3, Weeks.parseWeeks("P-3W").getWeeks());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFactory_parseWeeks_invalidFormat() {
        // "P5D" is invalid because it contains a non-week field.
        Weeks.parseWeeks("P5D");
    }

    //-----------------------------------------------------------------------
    // Getters
    //-----------------------------------------------------------------------

    @Test
    public void testGetters() {
        Weeks weeks = Weeks.THREE;
        assertEquals(3, weeks.getWeeks());
        assertEquals(DurationFieldType.weeks(), weeks.getFieldType());
        assertEquals(PeriodType.weeks(), weeks.getPeriodType());
    }

    //-----------------------------------------------------------------------
    // Converters
    //-----------------------------------------------------------------------

    @Test
    public void testToStandardDays() {
        assertEquals(21, Weeks.THREE.toStandardDays().getDays());
        assertEquals(-14, Weeks.weeks(-2).toStandardDays().getDays());
    }

    @Test(expected = ArithmeticException.class)
    public void testToStandardDays_overflow() {
        Weeks.MAX_VALUE.toStandardDays();
    }

    @Test
    public void testToStandardHours() {
        assertEquals(168, Weeks.ONE.toStandardHours().getHours()); // 1 * 7 * 24
        assertEquals(-336, Weeks.TWO.negated().toStandardHours().getHours());
    }

    @Test(expected = ArithmeticException.class)
    public void testToStandardHours_overflow() {
        Weeks.MAX_VALUE.toStandardHours();
    }

    @Test
    public void testToStandardMinutes() {
        assertEquals(10080, Weeks.ONE.toStandardMinutes().getMinutes()); // 1 * 7 * 24 * 60
        assertEquals(-20160, Weeks.TWO.negated().toStandardMinutes().getMinutes());
    }

    @Test(expected = ArithmeticException.class)
    public void testToStandardMinutes_overflow() {
        Weeks.MAX_VALUE.toStandardMinutes();
    }

    @Test
    public void testToStandardSeconds() {
        assertEquals(604800, Weeks.ONE.toStandardSeconds().getSeconds()); // 1 * 7 * 24 * 60 * 60
        assertEquals(-1209600, Weeks.TWO.negated().toStandardSeconds().getSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void testToStandardSeconds_overflow() {
        Weeks.MIN_VALUE.toStandardSeconds();
    }

    @Test
    public void testToStandardDuration() {
        assertEquals(2 * WEEKS_IN_MILLIS, Weeks.TWO.toStandardDuration().getMillis());
        assertEquals((long) Integer.MIN_VALUE * WEEKS_IN_MILLIS, Weeks.MIN_VALUE.toStandardDuration().getMillis());
    }

    //-----------------------------------------------------------------------
    // Arithmetic
    //-----------------------------------------------------------------------

    @Test
    public void testPlus() {
        assertEquals(3, Weeks.ONE.plus(2).getWeeks());
        assertEquals(3, Weeks.ONE.plus(Weeks.TWO).getWeeks());
        assertEquals(1, Weeks.ONE.plus((Weeks) null).getWeeks()); // null is treated as zero
        assertEquals(-1, Weeks.MAX_VALUE.plus(Weeks.MIN_VALUE).getWeeks());
    }

    @Test(expected = ArithmeticException.class)
    public void testPlus_overflow() {
        Weeks.MAX_VALUE.plus(1);
    }

    @Test
    public void testMinus() {
        assertEquals(-1, Weeks.ONE.minus(2).getWeeks());
        assertEquals(-1, Weeks.ONE.minus(Weeks.TWO).getWeeks());
        assertEquals(1, Weeks.ONE.minus((Weeks) null).getWeeks()); // null is treated as zero
    }

    @Test(expected = ArithmeticException.class)
    public void testMinus_overflow() {
        Weeks.MIN_VALUE.minus(1);
    }

    @Test(expected = ArithmeticException.class)
    public void testMinus_negationOverflow() {
        // Fails because minus(Weeks) is implemented as plus(negated(other))
        // and negating MIN_VALUE causes an overflow.
        Weeks.ONE.minus(Weeks.MIN_VALUE);
    }

    @Test
    public void testMultipliedBy() {
        assertEquals(6, Weeks.TWO.multipliedBy(3).getWeeks());
        assertEquals(-6, Weeks.TWO.multipliedBy(-3).getWeeks());
    }

    @Test(expected = ArithmeticException.class)
    public void testMultipliedBy_overflow() {
        Weeks.MAX_VALUE.multipliedBy(2);
    }

    @Test
    public void testDividedBy() {
        assertEquals(2, Weeks.weeks(4).dividedBy(2).getWeeks());
        assertEquals(1, Weeks.weeks(3).dividedBy(2).getWeeks()); // integer division
    }

    @Test(expected = ArithmeticException.class)
    public void testDividedBy_zero() {
        Weeks.ONE.dividedBy(0);
    }

    @Test
    public void testNegated() {
        assertEquals(-1, Weeks.ONE.negated().getWeeks());
        assertEquals(0, Weeks.ZERO.negated().getWeeks());
    }

    @Test(expected = ArithmeticException.class)
    public void testNegated_overflow() {
        Weeks.MIN_VALUE.negated();
    }

    //-----------------------------------------------------------------------
    // Comparisons
    //-----------------------------------------------------------------------

    @Test
    public void testIsGreaterThan() {
        assertTrue(Weeks.TWO.isGreaterThan(Weeks.ONE));
        assertFalse(Weeks.ONE.isGreaterThan(Weeks.ONE));
        assertFalse(Weeks.ONE.isGreaterThan(Weeks.TWO));
        assertTrue(Weeks.ONE.isGreaterThan(null)); // null is treated as zero
    }

    @Test
    public void testIsLessThan() {
        assertTrue(Weeks.ONE.isLessThan(Weeks.TWO));
        assertFalse(Weeks.ONE.isLessThan(Weeks.ONE));
        assertFalse(Weeks.TWO.isLessThan(Weeks.ONE));
        assertTrue(Weeks.MIN_VALUE.isLessThan(null)); // null is treated as zero
    }

    //-----------------------------------------------------------------------
    // Object methods
    //-----------------------------------------------------------------------

    @Test
    public void testToString() {
        assertEquals("P1W", Weeks.ONE.toString());
        assertEquals("P0W", Weeks.ZERO.toString());
        assertEquals("P-2W", Weeks.weeks(-2).toString());
        assertEquals("P" + Integer.MAX_VALUE + "W", Weeks.MAX_VALUE.toString());
    }
}