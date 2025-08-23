package org.joda.time;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, behavior-oriented tests for org.joda.time.Seconds.
 *
 * Focus:
 * - Factory methods and constants
 * - Conversions (weeks/days/hours/minutes/duration)
 * - Arithmetic (plus/minus/multiply/divide/negate) including overflow guards
 * - Comparisons (including null semantics)
 * - Parsing/formatting
 * - “Between” and “In” helpers
 */
public class SecondsReadableTest {

    // ---------------------------------------------------------------------
    // Factory methods and constants
    // ---------------------------------------------------------------------

    @Test
    public void factory_seconds_returnsExactValue() {
        assertEquals(0, Seconds.seconds(0).getSeconds());
        assertEquals(1, Seconds.seconds(1).getSeconds());
        assertEquals(2, Seconds.seconds(2).getSeconds());
        assertEquals(3, Seconds.seconds(3).getSeconds());
        assertEquals(Integer.MAX_VALUE, Seconds.seconds(Integer.MAX_VALUE).getSeconds());
        assertEquals(Integer.MIN_VALUE, Seconds.seconds(Integer.MIN_VALUE).getSeconds());
    }

    @Test
    public void constants_haveExpectedValues() {
        assertEquals(0, Seconds.ZERO.getSeconds());
        assertEquals(1, Seconds.ONE.getSeconds());
        assertEquals(2, Seconds.TWO.getSeconds());
        assertEquals(3, Seconds.THREE.getSeconds());
        assertEquals(Integer.MAX_VALUE, Seconds.MAX_VALUE.getSeconds());
        assertEquals(Integer.MIN_VALUE, Seconds.MIN_VALUE.getSeconds());
    }

    // ---------------------------------------------------------------------
    // Parsing and formatting
    // ---------------------------------------------------------------------

    @Test
    public void toString_minValue_usesISO8601() {
        assertEquals("PT-2147483648S", Seconds.MIN_VALUE.toString());
    }

    @Test
    public void parseSeconds_nullReturnsZero() {
        assertEquals(0, Seconds.parseSeconds(null).getSeconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseSeconds_invalidFormat_throws() {
        Seconds.parseSeconds("");
    }

    // ---------------------------------------------------------------------
    // Conversions to other periods
    // ---------------------------------------------------------------------

    @Test
    public void toStandardMinutes_hours_days_weeks_forTypicalValues() {
        Seconds s = Seconds.seconds(1575);
        assertEquals(26, s.toStandardMinutes().getMinutes()); // 26m 15s

        s = Seconds.seconds(690_562_340);
        assertEquals(191_822, s.toStandardHours().getHours());

        assertEquals(0, Seconds.ZERO.toStandardHours().getHours());
        assertEquals(0, Seconds.TWO.toStandardDays().getDays());
        assertEquals(0, Seconds.TWO.toStandardWeeks().getWeeks());
    }

    @Test
    public void toStandardSeconds_fromDaysAndWeeks_roundTripsWhenExact() {
        Days fiveDays = Days.FIVE;
        Seconds s = fiveDays.toStandardSeconds();
        assertEquals(5 * 24 * 60 * 60, s.getSeconds());
        assertEquals(5, s.toStandardDays().getDays());

        Weeks twoWeeks = Weeks.TWO;
        Days fourteenDays = twoWeeks.toStandardDays();
        Seconds secondsInTwoWeeks = fourteenDays.toStandardSeconds();
        assertEquals(14 * 24 * 60 * 60, secondsInTwoWeeks.getSeconds());
    }

    @Test
    public void minValue_toStandardWeeks_andBackToSeconds_isMultipleOfAWeek() {
        Weeks weeksFromMin = Seconds.MIN_VALUE.toStandardWeeks();
        assertEquals(-3550, weeksFromMin.getWeeks()); // floor division of negative seconds
        Seconds secondsFromWeeks = weeksFromMin.toStandardSeconds();
        assertEquals(-3_550 * 7 * 24 * 60 * 60, secondsFromWeeks.getSeconds());
    }

    // ---------------------------------------------------------------------
    // Conversion to duration
    // ---------------------------------------------------------------------

    @Test
    public void toStandardDuration_zero_min_max() {
        assertEquals(0L, Seconds.ZERO.toStandardDuration().getMillis());
        assertEquals(-2_147_483_648_000L, Seconds.MIN_VALUE.toStandardDuration().getMillis());

        Duration dMax = Seconds.MAX_VALUE.toStandardDuration();
        assertEquals(2_147_483_647_000L, dMax.getMillis());
        assertEquals(24_855L, dMax.getStandardDays()); // sanity check
    }

    // ---------------------------------------------------------------------
    // Arithmetic: plus / minus
    // ---------------------------------------------------------------------

    @Test
    public void plus_minus_withInt() {
        Seconds base = Seconds.seconds(3_528_316_96); // arbitrary large positive
        assertEquals(base.getSeconds() + 3058, base.plus(3058).getSeconds());
        assertEquals(base.getSeconds() - 3058, base.minus(3058).getSeconds());
        assertEquals(0, Seconds.seconds(0).minus(0).getSeconds());
        assertEquals(-1, Days.ZERO.toStandardSeconds().minus(1).getSeconds());
    }

    @Test
    public void plus_minus_withSecondsArgument_andNull() {
        Seconds s = Seconds.seconds(10);
        assertEquals(20, s.plus(s).getSeconds());
        assertEquals(0, Seconds.TWO.minus(Seconds.TWO).getSeconds());

        // null treated as ZERO
        assertEquals(2, Seconds.TWO.plus((Seconds) null).getSeconds());
        assertEquals(2, Seconds.TWO.minus((Seconds) null).getSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void plus_overflow_throws() {
        Seconds.MAX_VALUE.plus(Seconds.MAX_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void plus_withInt_overflow_throws() {
        Seconds.MIN_VALUE.plus(-674);
    }

    @Test(expected = ArithmeticException.class)
    public void minus_withInt_overflow_throws() {
        Seconds.THREE.minus(-2_147_483_646); // 3 + 2_147_483_646
    }

    // ---------------------------------------------------------------------
    // Arithmetic: multiply / divide / negate
    // ---------------------------------------------------------------------

    @Test
    public void multipliedBy_basicCases() {
        assertEquals(0, Seconds.seconds(1810).multipliedBy(0).getSeconds());
        assertEquals(811_188, Seconds.seconds(1566).multipliedBy(518).getSeconds());
        assertEquals(-701, Seconds.ONE.multipliedBy(-701).getSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void multipliedBy_overflow_throws() {
        Seconds.MIN_VALUE.multipliedBy(-674);
    }

    @Test
    public void dividedBy_basicCases() {
        assertEquals(1, Seconds.seconds(1804).dividedBy(1804).getSeconds());
        assertEquals(-596_523, Seconds.MIN_VALUE.dividedBy(3600).getSeconds()); // floor towards zero
    }

    @Test(expected = ArithmeticException.class)
    public void dividedBy_zero_throws() {
        Seconds.MAX_VALUE.dividedBy(0);
    }

    @Test
    public void negated_regularCase() {
        assertEquals(-3, Seconds.THREE.negated().getSeconds());
        // Ensure original instance not mutated
        assertEquals(3, Seconds.THREE.getSeconds());
    }

    @Test(expected = ArithmeticException.class)
    public void negated_minValue_throws() {
        Seconds.MIN_VALUE.negated();
    }

    // ---------------------------------------------------------------------
    // Comparisons
    // ---------------------------------------------------------------------

    @Test
    public void comparisons_withNull_meanZero() {
        assertTrue(Seconds.ONE.isGreaterThan(null));
        assertFalse(Seconds.seconds(-1300).isGreaterThan(null));
        assertFalse(Seconds.MAX_VALUE.isLessThan(null));
        assertTrue(Seconds.seconds(-1300).isLessThan(null));
    }

    @Test
    public void comparisons_withSelf_andAnother() {
        assertFalse(Seconds.ONE.isGreaterThan(Seconds.ONE));
        assertFalse(Seconds.ZERO.isLessThan(Seconds.ZERO));
        assertTrue(Seconds.seconds(2).isGreaterThan(Seconds.MIN_VALUE));
        assertTrue(Days.ZERO.toStandardSeconds().isLessThan(Seconds.THREE));
    }

    // ---------------------------------------------------------------------
    // Between and In helpers
    // ---------------------------------------------------------------------

    @Test
    public void secondsBetween_sameInstant_isZero() {
        Instant now = new Instant();
        Seconds between = Seconds.secondsBetween(now, now);
        assertEquals(0, between.getSeconds());
    }

    @Test(expected = IllegalArgumentException.class)
    public void secondsBetween_nullInstants_throws() {
        Seconds.secondsBetween((ReadableInstant) null, (ReadableInstant) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void secondsBetween_nullPartials_throws() {
        Seconds.secondsBetween((ReadablePartial) null, (ReadablePartial) null);
    }

    @Test
    public void secondsIn_nullInterval_isZero_andNonNullInterval() {
        assertEquals(0, Seconds.secondsIn((ReadableInterval) null).getSeconds());

        Instant start = new Instant(0L);
        Instant end = start.plus(3000L);
        Interval interval = new Interval(start, end);
        assertEquals(3, Seconds.secondsIn(interval).getSeconds());
    }

    @Test
    public void standardSecondsIn_nullPeriod_isZero_andFromSecondsIsIdentity() {
        assertEquals(0, Seconds.standardSecondsIn((ReadablePeriod) null).getSeconds());
        Seconds s = Seconds.seconds(42);
        assertEquals(42, Seconds.standardSecondsIn(s).getSeconds());
    }

    // ---------------------------------------------------------------------
    // Getters and types
    // ---------------------------------------------------------------------

    @Test
    public void getters_fieldType_periodType_andValue() {
        Seconds s = Seconds.seconds(2);
        assertEquals(2, s.getSeconds());
        assertEquals(1, s.getPeriodType().size());
        assertEquals(DurationFieldType.seconds(), s.getFieldType());
    }
}