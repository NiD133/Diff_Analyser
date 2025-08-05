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
 * Unit tests for the {@link Minutes} class.
 */
public class MinutesTest {

    // Test constants
    private static final Minutes ZERO = Minutes.ZERO;
    private static final Minutes ONE = Minutes.ONE;
    private static final Minutes TWO = Minutes.TWO;
    private static final Minutes THREE = Minutes.THREE;
    private static final Minutes MAX = Minutes.MAX_VALUE;
    private static final Minutes MIN = Minutes.MIN_VALUE;

    // --- Factory Methods ---

    @Test
    public void factory_minutes_returnsCachedInstancesForSmallValues() {
        assertSame(Minutes.minutes(0), ZERO);
        assertSame(Minutes.minutes(1), ONE);
        assertSame(Minutes.minutes(2), TWO);
        assertSame(Minutes.minutes(3), THREE);
        assertSame(Minutes.minutes(Integer.MAX_VALUE), MAX);
        assertSame(Minutes.minutes(Integer.MIN_VALUE), MIN);
    }

    @Test
    public void factory_minutes_returnsNewInstanceForOtherValues() {
        assertEquals(4, Minutes.minutes(4).getMinutes());
        assertEquals(-1, Minutes.minutes(-1).getMinutes());
    }

    @Test
    public void factory_minutesBetween_instants() {
        Instant start = new Instant(1000L * 60 * 5);
        Instant end = new Instant(1000L * 60 * 12);

        assertEquals(7, Minutes.minutesBetween(start, end).getMinutes());
        assertEquals(0, Minutes.minutesBetween(start, start).getMinutes());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void factory_minutesBetween_instants_withNulls_throwsException() {
        Minutes.minutesBetween((ReadableInstant) null, (ReadableInstant) null);
    }

    @Test
    public void factory_minutesBetween_partials() {
        ReadablePartial start = new LocalTime(10, 5);
        ReadablePartial end = new LocalTime(10, 12);

        assertEquals(7, Minutes.minutesBetween(start, end).getMinutes());
        assertEquals(0, Minutes.minutesBetween(start, start).getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void factory_minutesBetween_partials_withNulls_throwsException() {
        Minutes.minutesBetween((ReadablePartial) null, (ReadablePartial) null);
    }

    @Test
    public void factory_minutesIn_withNullInterval_returnsZero() {
        assertEquals(ZERO, Minutes.minutesIn(null));
    }

    @Test
    public void factory_standardMinutesIn_convertsPeriodToMinutes() {
        assertEquals(ZERO, Minutes.standardMinutesIn(null));
        assertEquals(ZERO, Minutes.standardMinutesIn(Days.ZERO));
        assertEquals(4320, Minutes.standardMinutesIn(Days.THREE).getMinutes());
    }

    @Test
    public void factory_parseMinutes_parsesISOFormat() {
        assertEquals(ZERO, Minutes.parseMinutes(null));
        assertEquals(ZERO, Minutes.parseMinutes("PT0M"));
        assertEquals(ONE, Minutes.parseMinutes("PT1M"));
        assertEquals(-3, Minutes.parseMinutes("PT-3M").getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void factory_parseMinutes_withInvalidFormat_throwsException() {
        Minutes.parseMinutes("Invalid-Format");
    }

    // --- Core Methods ---

    @Test
    public void getMinutes_returnsTheNumberOfMinutes() {
        assertEquals(0, ZERO.getMinutes());
        assertEquals(1, ONE.getMinutes());
        assertEquals(Integer.MAX_VALUE, MAX.getMinutes());
        assertEquals(Integer.MIN_VALUE, MIN.getMinutes());
    }

    @Test
    public void getFieldType_returnsMinutes() {
        assertEquals(DurationFieldType.minutes(), ONE.getFieldType());
    }

    @Test
    public void getPeriodType_returnsMinutesType() {
        assertEquals(PeriodType.minutes(), ONE.getPeriodType());
    }

    @Test
    public void toString_returnsISOFormatString() {
        assertEquals("PT0M", ZERO.toString());
        assertEquals("PT1M", ONE.toString());
        assertEquals("PT-1M", Minutes.minutes(-1).toString());
        assertEquals("PT2147483647M", MAX.toString());
    }

    // --- Comparison Methods ---

    @Test
    public void isGreaterThan_returnsTrueForGreaterValues() {
        assertTrue(TWO.isGreaterThan(ONE));
        assertTrue(ONE.isGreaterThan(ZERO));
        assertTrue(ONE.isGreaterThan(null)); // null is treated as zero
    }

    @Test
    public void isGreaterThan_returnsFalseForLesserOrEqualValues() {
        assertFalse(ONE.isGreaterThan(TWO));
        assertFalse(MIN.isGreaterThan(ZERO));
        assertFalse(ONE.isGreaterThan(ONE));
        assertFalse(MIN.isGreaterThan(null)); // null is treated as zero
    }

    @Test
    public void isLessThan_returnsTrueForLesserValues() {
        assertTrue(ONE.isLessThan(TWO));
        assertTrue(MIN.isLessThan(ZERO));
        assertTrue(MIN.isLessThan(null)); // null is treated as zero
    }

    @Test
    public void isLessThan_returnsFalseForGreaterOrEqualValues() {
        assertFalse(TWO.isLessThan(ONE));
        assertFalse(ZERO.isLessThan(MIN));
        assertFalse(ONE.isLessThan(ONE));
        assertFalse(ONE.isLessThan(null)); // null is treated as zero
    }

    // --- Arithmetic Methods ---

    @Test
    public void plus_withInteger_addsMinutes() {
        assertEquals(THREE, ONE.plus(2));
        assertEquals(ZERO, TWO.plus(-2));
        assertEquals(MAX, MAX.plus(0));
    }

    @Test(expected = ArithmeticException.class)
    public void plus_withInteger_whenResultOverflows_throwsException() {
        MAX.plus(1);
    }

    @Test
    public void plus_withMinutes_addsMinutes() {
        assertEquals(THREE, ONE.plus(TWO));
        assertEquals(ONE, THREE.plus(Minutes.minutes(-2)));
        assertEquals(ONE, ONE.plus(ZERO));
        assertEquals(ONE, ONE.plus((Minutes) null)); // null is treated as zero
    }

    @Test(expected = ArithmeticException.class)
    public void plus_withMinutes_whenResultOverflows_throwsException() {
        MAX.plus(ONE);
    }

    @Test
    public void minus_withInteger_subtractsMinutes() {
        assertEquals(ONE, THREE.minus(2));
        assertEquals(ZERO, TWO.minus(2));
        assertEquals(Minutes.minutes(242), ONE.minus(-241));
    }

    @Test(expected = ArithmeticException.class)
    public void minus_withInteger_whenResultOverflows_throwsException() {
        MIN.minus(1);
    }

    @Test
    public void minus_withMinutes_subtractsMinutes() {
        assertEquals(ONE, THREE.minus(TWO));
        assertEquals(Minutes.minutes(-1), TWO.minus(THREE));
        assertEquals(ONE, ONE.minus(ZERO));
        assertEquals(ONE, ONE.minus((Minutes) null)); // null is treated as zero
    }

    @Test(expected = ArithmeticException.class)
    public void minus_withMinutes_whenResultOverflows_throwsException() {
        // MIN - ONE = MIN + (-ONE), which overflows
        MIN.minus(ONE);
    }

    @Test
    public void multipliedBy_calculatesProduct() {
        assertEquals(Minutes.minutes(6), THREE.multipliedBy(2));
        assertEquals(ZERO, MAX.multipliedBy(0));
        assertEquals(Minutes.minutes(-6), THREE.multipliedBy(-2));
    }

    @Test(expected = ArithmeticException.class)
    public void multipliedBy_whenResultOverflows_throwsException() {
        MAX.multipliedBy(2);
    }

    @Test
    public void dividedBy_calculatesQuotient() {
        assertEquals(ONE, THREE.dividedBy(2)); // Integer division
        assertEquals(TWO, TWO.dividedBy(1));
        assertEquals(Minutes.minutes(-1), THREE.dividedBy(-2));
    }

    @Test(expected = ArithmeticException.class)
    public void dividedBy_byZero_throwsException() {
        ONE.dividedBy(0);
    }

    @Test
    public void negated_reversesSign() {
        assertEquals(Minutes.minutes(-1), ONE.negated());
        assertEquals(ONE, Minutes.minutes(-1).negated());
        assertEquals(ZERO, ZERO.negated());
    }

    @Test(expected = ArithmeticException.class)
    public void negated_minValue_throwsException() {
        // Negating Integer.MIN_VALUE causes an overflow
        MIN.negated();
    }

    // --- Conversion Methods ---

    @Test
    public void toStandardWeeks_convertsCorrectly() {
        assertEquals(0, Minutes.minutes(10079).toStandardWeeks().getWeeks()); // 1 week = 10080 min
        assertEquals(1, Minutes.minutes(10080).toStandardWeeks().getWeeks());
        assertEquals(213044, MAX.toStandardWeeks().getWeeks());
        assertEquals(-213044, MIN.toStandardWeeks().getWeeks());
    }

    @Test
    public void toStandardDays_convertsCorrectly() {
        assertEquals(0, Minutes.minutes(1439).toStandardDays().getDays()); // 1 day = 1440 min
        assertEquals(1, Minutes.minutes(1440).toStandardDays().getDays());
        assertEquals(1491308, MAX.toStandardDays().getDays());
    }

    @Test
    public void toStandardHours_convertsCorrectly() {
        assertEquals(0, Minutes.minutes(59).toStandardHours().getHours());
        assertEquals(1, Minutes.minutes(60).toStandardHours().getHours());
        assertEquals(-35791394, MIN.toStandardHours().getHours());
    }

    @Test
    public void toStandardSeconds_convertsCorrectly() {
        assertEquals(0, ZERO.toStandardSeconds().getSeconds());
        assertEquals(60, ONE.toStandardSeconds().getSeconds());
        assertEquals(-120, Minutes.minutes(-2).toStandardSeconds().getSeconds());
    }

    @Test
    public void toStandardSeconds_conversionLosesPrecisionForNonMultiplesOf60() {
        // Seconds.MIN_VALUE is -2147483648
        // toStandardMinutes() truncates: -2147483648 / 60 = -35791394
        Minutes minutes = Seconds.MIN_VALUE.toStandardMinutes();
        assertEquals(-35791394, minutes.getMinutes());

        // toStandardSeconds() multiplies back: -35791394 * 60 = -2147483640
        Seconds seconds = minutes.toStandardSeconds();
        assertEquals(-2147483640, seconds.getSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void toStandardSeconds_whenResultOverflows_throwsException() {
        MAX.toStandardSeconds();
    }

    @Test
    public void toStandardDuration_convertsCorrectly() {
        assertEquals(0L, ZERO.toStandardDuration().getMillis());
        assertEquals(60000L, ONE.toStandardDuration().getMillis());
        assertEquals(-120000L, Minutes.minutes(-2).toStandardDuration().getMillis());
    }
}