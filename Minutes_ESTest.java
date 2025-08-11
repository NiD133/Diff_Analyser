package org.joda.time;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, intention-revealing tests for org.joda.time.Minutes.
 * These tests focus on behavior, edge cases, and clear naming.
 */
public class MinutesTest {

    // Factory methods and parsing

    @Test
    public void minutes_factory_returnsExpectedValue() {
        assertEquals(0, Minutes.minutes(0).getMinutes());
        assertEquals(1, Minutes.minutes(1).getMinutes());
        assertEquals(2, Minutes.minutes(2).getMinutes());
        assertEquals(Integer.MAX_VALUE, Minutes.minutes(Integer.MAX_VALUE).getMinutes());
        assertEquals(Integer.MIN_VALUE, Minutes.minutes(Integer.MIN_VALUE).getMinutes());
    }

    @Test
    public void parseMinutes_validString() {
        assertEquals(15, Minutes.parseMinutes("PT15M").getMinutes());
        assertEquals(-3, Minutes.parseMinutes("PT-3M").getMinutes());
    }

    @Test
    public void parseMinutes_nullYieldsZero() {
        assertEquals(Minutes.ZERO, Minutes.parseMinutes(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseMinutes_invalidFormatThrows() {
        Minutes.parseMinutes("UT");
    }

    // Basic properties

    @Test
    public void toString_returnsISOFormat() {
        assertEquals("PT3M", Minutes.THREE.toString());
        assertEquals("PT2147483647M", Minutes.MAX_VALUE.toString());
    }

    @Test
    public void fieldType_and_periodType() {
        assertEquals("minutes", Minutes.ONE.getFieldType().toString());
        assertEquals(1, Minutes.ONE.getPeriodType().size());
    }

    @Test
    public void getMinutes_returnsUnderlyingValue() {
        assertEquals(0, Minutes.ZERO.getMinutes());
        assertEquals(Integer.MIN_VALUE, Minutes.MIN_VALUE.getMinutes());
        assertEquals(Integer.MAX_VALUE, Minutes.MAX_VALUE.getMinutes());
    }

    // Arithmetic: plus / minus (int)

    @Test
    public void plusInt_addsMinutes() {
        assertEquals(3, Minutes.ONE.plus(2).getMinutes());
        assertEquals(0, Minutes.TWO.plus(-2).getMinutes());
    }

    @Test
    public void minusInt_subtractsMinutes() {
        assertEquals(-1, Minutes.TWO.minus(3).getMinutes());
        assertEquals(242, Minutes.ONE.minus(-241).getMinutes());
        assertEquals(0, Minutes.ZERO.minus(0).getMinutes());
    }

    @Test(expected = ArithmeticException.class)
    public void plusInt_overflowThrows() {
        Minutes.TWO.plus(Integer.MAX_VALUE);
    }

    // Arithmetic: plus / minus (Minutes)

    @Test
    public void plusMinutes_handlesNullAsZero() {
        assertSame(Minutes.ZERO, Minutes.ZERO.plus((Minutes) null));
        assertSame(Minutes.ONE, Minutes.ONE.plus((Minutes) null));
    }

    @Test
    public void minusMinutes_handlesNullAsZero() {
        assertSame(Minutes.ZERO, Minutes.ZERO.minus((Minutes) null));
        assertSame(Minutes.ONE, Minutes.ONE.minus((Minutes) null));
    }

    @Test
    public void plusMinusMinutes_works() {
        assertEquals(-1, Minutes.TWO.minus(Minutes.THREE).getMinutes());
        assertEquals(2, Minutes.ONE.plus(Minutes.ONE).getMinutes());
    }

    @Test(expected = ArithmeticException.class)
    public void plusMinutes_overflowThrows() {
        Minutes.MAX_VALUE.plus(Minutes.MAX_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void minusMinutes_negatingMinValueThrows() {
        Minutes.MIN_VALUE.minus(Minutes.MIN_VALUE);
    }

    // MultipliedBy / DividedBy / Negated

    @Test
    public void multipliedBy_scalesValue() {
        assertEquals(0, Minutes.minutes(1431).multipliedBy(0).getMinutes());
        assertEquals(4310172, Minutes.minutes(1431).multipliedBy(3012).getMinutes());
        assertEquals(-5447, Minutes.ONE.multipliedBy(-5447).getMinutes());
    }

    @Test(expected = ArithmeticException.class)
    public void multipliedBy_overflowThrows() {
        Minutes.MAX_VALUE.multipliedBy(-4433);
    }

    @Test
    public void dividedBy_integerDivision() {
        assertEquals(1, Minutes.THREE.dividedBy(2).getMinutes());
        assertEquals(-1, Minutes.minutes(-3).dividedBy(2).getMinutes());
        assertSame(Minutes.ZERO, Minutes.ZERO.dividedBy(1));
    }

    @Test(expected = ArithmeticException.class)
    public void dividedBy_byZeroThrows() {
        Minutes.ONE.dividedBy(0);
    }

    @Test
    public void negated_invertsSign() {
        assertEquals(0, Minutes.ZERO.negated().getMinutes());
        assertEquals(-3, Minutes.THREE.negated().getMinutes());
    }

    @Test(expected = ArithmeticException.class)
    public void negated_minValueThrows() {
        Minutes.MIN_VALUE.negated();
    }

    // Comparisons

    @Test
    public void isGreaterThan_comparesWithNullAsZero() {
        assertTrue(Minutes.TWO.isGreaterThan(Minutes.ZERO));
        assertTrue(Minutes.minutes(1431).isGreaterThan(null));
        assertFalse(Minutes.MIN_VALUE.isGreaterThan(null));
        assertFalse(Minutes.ONE.isGreaterThan(Minutes.ONE));
    }

    @Test
    public void isLessThan_comparesWithNullAsZero() {
        assertTrue(Minutes.TWO.isLessThan(Minutes.minutes(31)));
        assertFalse(Minutes.minutes(31).isLessThan(Minutes.minutes(31)));
        assertFalse(Minutes.ZERO.isLessThan(null));
        assertTrue(Minutes.MIN_VALUE.isLessThan(null));
        assertFalse(Minutes.ONE.isLessThan(null));
        assertFalse(Minutes.MAX_VALUE.isLessThan(Minutes.TWO));
    }

    // Conversions to other periods

    @Test
    public void toStandardSeconds_basicAndZero() {
        assertEquals(60, Minutes.ONE.toStandardSeconds().getSeconds());
        assertEquals(0, Minutes.ZERO.toStandardSeconds().getSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void toStandardSeconds_minValueOverflows() {
        Minutes.MIN_VALUE.toStandardSeconds();
    }

    @Test
    public void toStandardHours_basicAndMinValue() {
        assertEquals(23, Minutes.minutes(1431).toStandardHours().getHours());
        assertEquals(Integer.MIN_VALUE / 60, Minutes.MIN_VALUE.toStandardHours().getHours());
        assertEquals(0, Minutes.ZERO.toStandardHours().getHours());
    }

    @Test
    public void toStandardDays_basicAndZero() {
        assertEquals(0, Minutes.ZERO.toStandardDays().getDays());
        int expectedDaysFromMax = Minutes.MAX_VALUE.getMinutes() / (60 * 24);
        assertEquals(expectedDaysFromMax, Minutes.MAX_VALUE.toStandardDays().getDays());
    }

    @Test
    public void toStandardWeeks_basicAndEdges() {
        assertEquals(0, Minutes.ONE.toStandardWeeks().getWeeks());
        int expectedWeeksFromMax = Minutes.MAX_VALUE.getMinutes() / (60 * 24 * 7);
        assertEquals(expectedWeeksFromMax, Minutes.MAX_VALUE.toStandardWeeks().getWeeks());
        int expectedWeeksFromMin = Minutes.MIN_VALUE.getMinutes() / (60 * 24 * 7);
        assertEquals(expectedWeeksFromMin, Minutes.MIN_VALUE.toStandardWeeks().getWeeks());
    }

    @Test
    public void toStandardDuration_andBack() {
        assertEquals(60000L, Minutes.ONE.toStandardDuration().getMillis());
        Minutes roundTripped = Minutes.ZERO.toStandardDuration().toStandardMinutes();
        assertEquals(0, roundTripped.getMinutes());
    }

    // Standard minutes in period / interval

    @Test
    public void standardMinutesIn_periodNullReturnsZero() {
        assertEquals(0, Minutes.standardMinutesIn((ReadablePeriod) null).getMinutes());
    }

    @Test
    public void standardMinutesIn_days() {
        assertEquals(4320, Minutes.standardMinutesIn(Days.THREE).getMinutes());
    }

    @Test
    public void minutesIn_intervalNullReturnsZero() {
        assertEquals(0, Minutes.minutesIn((ReadableInterval) null).getMinutes());
    }

    // Between instants and partials

    @Test
    public void minutesBetween_instants_zeroAndPositive() {
        Instant t0 = new Instant(0);
        Instant t2m = new Instant(2 * 60 * 1000L);
        assertEquals(0, Minutes.minutesBetween(t0, t0).getMinutes());
        assertEquals(2, Minutes.minutesBetween(t0, t2m).getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void minutesBetween_instants_nullThrows() {
        Minutes.minutesBetween((ReadableInstant) null, (ReadableInstant) null);
    }

    @Test
    public void minutesBetween_partials_zeroAndPositive() {
        LocalTime ten = new LocalTime(10, 0);
        LocalTime tenPlus3 = new LocalTime(10, 3);
        assertEquals(0, Minutes.minutesBetween(ten, ten).getMinutes());
        assertEquals(3, Minutes.minutesBetween(ten, tenPlus3).getMinutes());
    }

    @Test(expected = IllegalArgumentException.class)
    public void minutesBetween_partials_nullThrows() {
        Minutes.minutesBetween((ReadablePartial) null, (ReadablePartial) null);
    }

    // Cross-type conversions round-trip sanity

    @Test
    public void secondsToMinutes_backToSeconds() {
        Seconds minSeconds = Seconds.MIN_VALUE;
        Minutes mins = minSeconds.toStandardMinutes();
        Seconds back = mins.toStandardSeconds();
        assertEquals(minSeconds.getSeconds() / 60, mins.getMinutes());
        assertEquals(mins.getMinutes() * 60, back.getSeconds());
    }
}