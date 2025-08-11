package org.joda.time;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Readable, intention-revealing tests for org.joda.time.Weeks.
 * 
 * Conventions:
 * - Each test name states the behavior being verified.
 * - AAA (Arrange, Act, Assert) structure inside tests.
 * - Helper method assertWeeks(...) reads like English.
 * - Avoids EvoSuite-specific scaffolding and flaky constructs.
 */
public class WeeksReadableTest {

    private static void assertWeeks(Weeks actual, int expectedWeeks) {
        assertEquals("unexpected week count", expectedWeeks, actual.getWeeks());
    }

    // ---------------------------------------------------------------------
    // Factory methods and constants
    // ---------------------------------------------------------------------

    @Test
    public void weeks_factory_returnsExpectedConstantInstances() {
        assertSame(Weeks.ZERO, Weeks.weeks(0));
        assertSame(Weeks.ONE, Weeks.weeks(1));
        assertSame(Weeks.TWO, Weeks.weeks(2));
        assertSame(Weeks.THREE, Weeks.weeks(3));
        assertSame(Weeks.MAX_VALUE, Weeks.weeks(Integer.MAX_VALUE));
        assertSame(Weeks.MIN_VALUE, Weeks.weeks(Integer.MIN_VALUE));
    }

    @Test
    public void weeksIn_nullInterval_returnsZero() {
        // Act
        Weeks result = Weeks.weeksIn((ReadableInterval) null);
        // Assert
        assertWeeks(result, 0);
    }

    @Test
    public void standardWeeksIn_nullPeriod_returnsZero() {
        // Act
        Weeks result = Weeks.standardWeeksIn((ReadablePeriod) null);
        // Assert
        assertWeeks(result, 0);
    }

    @Test
    public void standardWeeksIn_hoursMinValue_truncatesTowardZero() {
        // Arrange
        Hours hours = Hours.MIN_VALUE; // -2147483648 hours
        // Act
        Weeks result = Weeks.standardWeeksIn(hours);
        // Assert: -2147483648 / (24*7) = -12782640
        assertWeeks(result, -12_782_640);
    }

    // ---------------------------------------------------------------------
    // Between helpers
    // ---------------------------------------------------------------------

    @Test
    public void weeksBetween_sameInstant_isZero() {
        // Arrange
        Instant epoch = Instant.EPOCH;
        // Act
        Weeks result = Weeks.weeksBetween(epoch, epoch);
        // Assert
        assertWeeks(result, 0);
    }

    @Test
    public void weeksBetween_sameLocalDate_isZero() {
        // Arrange
        LocalDate d = new LocalDate(2020, 1, 1);
        // Act
        Weeks result = Weeks.weeksBetween(d, d);
        // Assert
        assertWeeks(result, 0);
    }

    @Test
    public void weeksBetween_nullInstants_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Weeks.weeksBetween((ReadableInstant) null, (ReadableInstant) null));
    }

    @Test
    public void weeksBetween_nullPartials_throws() {
        assertThrows(IllegalArgumentException.class, () ->
                Weeks.weeksBetween((ReadablePartial) null, (ReadablePartial) null));
    }

    // ---------------------------------------------------------------------
    // Parsing and string representation
    // ---------------------------------------------------------------------

    @Test
    public void parseWeeks_null_returnsZero() {
        assertWeeks(Weeks.parseWeeks(null), 0);
    }

    @Test
    public void parseWeeks_invalid_throws() {
        assertThrows(IllegalArgumentException.class, () -> Weeks.parseWeeks(")%X[WS"));
    }

    @Test
    public void toString_oneWeek_isISO8601() {
        assertEquals("P1W", Weeks.ONE.toString());
    }

    // ---------------------------------------------------------------------
    // Accessors and types
    // ---------------------------------------------------------------------

    @Test
    public void getWeeks_returnsUnderlyingValue() {
        assertEquals(3, Weeks.THREE.getWeeks());
        assertEquals(Integer.MIN_VALUE, Weeks.MIN_VALUE.getWeeks());
        assertEquals(Integer.MAX_VALUE, Weeks.MAX_VALUE.getWeeks());
    }

    @Test
    public void getFieldType_isWeeks() {
        assertEquals("weeks", Weeks.THREE.getFieldType().getName());
    }

    @Test
    public void getPeriodType_hasSingleField() {
        assertEquals(1, Weeks.TWO.getPeriodType().size());
    }

    // ---------------------------------------------------------------------
    // Conversions to other time units
    // ---------------------------------------------------------------------

    @Test
    public void toStandardDays_zero_isZero() {
        assertEquals(0, Weeks.ZERO.toStandardDays().getDays());
    }

    @Test
    public void toStandardDays_threeWeeks_is21Days() {
        assertEquals(21, Weeks.THREE.toStandardDays().getDays());
    }

    @Test
    public void toStandardHours_zero_isZero() {
        assertEquals(0, Weeks.ZERO.toStandardHours().getHours());
    }

    @Test
    public void toStandardHours_threeWeeks_is504Hours() {
        assertEquals(504, Weeks.THREE.toStandardHours().getHours());
    }

    @Test
    public void toStandardMinutes_oneWeek_is10080Minutes() {
        assertEquals(10_080, Weeks.ONE.toStandardMinutes().getMinutes());
    }

    @Test
    public void toStandardMinutes_negativeWeeks_isNegativeMinutes() {
        // Arrange
        Weeks w = Weeks.weeks(-3765);
        // Act
        Minutes m = w.toStandardMinutes();
        // Assert
        assertEquals(-37_951_200, m.getMinutes()); // -3765 * 10080
    }

    @Test
    public void toStandardSeconds_oneWeek_is604800Seconds() {
        assertEquals(604_800, Weeks.ONE.toStandardSeconds().getSeconds());
    }

    @Test
    public void toStandardSeconds_zero_isZero() {
        assertEquals(0, Weeks.ZERO.toStandardSeconds().getSeconds());
    }

    @Test
    public void toStandardDuration_twoWeeks_matchesMilliseconds() {
        assertEquals(1_209_600_000L, Weeks.TWO.toStandardDuration().getMillis());
    }

    @Test
    public void toStandardDuration_minValue_hasExpectedStandardDays() {
        // weeks * 7 days; relies on long arithmetic in Duration
        assertEquals(-15_032_385_536L, Weeks.MIN_VALUE.toStandardDuration().getStandardDays());
    }

    // ---------------------------------------------------------------------
    // Conversions from other periods
    // ---------------------------------------------------------------------

    @Test
    public void minutesToWeeks_zeroMinutes_isZeroWeeks() {
        Weeks w = Minutes.ZERO.toStandardWeeks();
        assertWeeks(w, 0);
    }

    @Test
    public void minutesToSecondsToWeeks_truncatesToZero() {
        // Arrange: 3 minutes -> 180 seconds -> 0 weeks
        Seconds s = Minutes.THREE.toStandardSeconds();
        // Act
        Weeks w = s.toStandardWeeks();
        // Assert
        assertWeeks(w, 0);
    }

    @Test
    public void secondsMaxValue_toWeeks_thenToHours() {
        // Arrange
        Weeks w = Seconds.MAX_VALUE.toStandardWeeks(); // floor(2_147_483_647 / 604_800) = 3550
        // Act
        Hours h = w.toStandardHours();
        // Assert
        assertEquals(596_400, h.getHours()); // 3550 * 168
    }

    // ---------------------------------------------------------------------
    // Arithmetic with ints
    // ---------------------------------------------------------------------

    @Test
    public void plus_withPositiveInt_adds() {
        assertWeeks(Weeks.weeks(2).plus(2), 4);
    }

    @Test
    public void minus_withPositiveInt_subtracts() {
        assertWeeks(Weeks.THREE.minus(2), 1);
    }

    @Test
    public void minus_zeroIsNoOp_returnsSameValue() {
        Weeks w = Weeks.weeks(0);
        assertWeeks(w.minus(0), 0);
    }

    @Test
    public void multipliedBy_scalesValue() {
        assertWeeks(Weeks.ONE.multipliedBy(604_800), 604_800);
        assertWeeks(Weeks.THREE.multipliedBy(-617), -1_851);
    }

    @Test
    public void dividedBy_one_returnsSameValue() {
        assertWeeks(Weeks.weeks(317_351_877).dividedBy(1), 317_351_877);
    }

    @Test
    public void dividedBy_negativeOne_negatesValue() {
        assertWeeks(Weeks.MAX_VALUE.dividedBy(-1), -2_147_483_647);
    }

    @Test
    public void negated_zero_isZero() {
        assertWeeks(Weeks.ZERO.negated(), 0);
    }

    // ---------------------------------------------------------------------
    // Arithmetic with Weeks objects
    // ---------------------------------------------------------------------

    @Test
    public void plus_withWeeks_adds() {
        assertWeeks(Weeks.ZERO.plus(Weeks.ZERO), 0);
        assertSame(Weeks.MAX_VALUE, Weeks.MAX_VALUE.plus((Weeks) null)); // null -> identity
    }

    @Test
    public void minus_withWeeks_subtracts() {
        assertWeeks(Weeks.THREE.minus(Weeks.TWO), 1);
        Weeks min = Weeks.MIN_VALUE;
        assertSame(min, min.minus((Weeks) null)); // null -> identity
    }

    // ---------------------------------------------------------------------
    // Comparison helpers
    // ---------------------------------------------------------------------

    @Test
    public void isGreaterThan_and_isLessThan_withNullTreatsNullAsZero() {
        assertTrue(Weeks.THREE.isGreaterThan(null));      // 3 > 0
        assertFalse(Weeks.THREE.isLessThan(null));        // 3 !< 0
        assertTrue(Weeks.MIN_VALUE.isLessThan(null));     // min < 0
        assertFalse(Weeks.MIN_VALUE.isGreaterThan(null)); // min !> 0
        assertFalse(Weeks.ZERO.isLessThan(Weeks.ZERO));   // 0 !< 0
        assertFalse(Weeks.weeks(5).isGreaterThan(Weeks.weeks(5))); // equal -> false
    }

    @Test
    public void isGreaterThan_and_isLessThan_withNonNull() {
        assertTrue(Weeks.weeks(10).isGreaterThan(Weeks.weeks(1)));
        assertFalse(Weeks.weeks(1).isGreaterThan(Weeks.weeks(10)));
        assertTrue(Weeks.weeks(1).isLessThan(Weeks.weeks(2)));
        assertFalse(Weeks.weeks(2).isLessThan(Weeks.weeks(1)));
    }

    // ---------------------------------------------------------------------
    // Overflow and error conditions
    // ---------------------------------------------------------------------

    @Test
    public void dividedBy_zero_throwsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> Weeks.TWO.dividedBy(0));
    }

    @Test
    public void toStandardDays_maxValue_overflowsIntDays() {
        assertThrows(ArithmeticException.class, () -> Weeks.MAX_VALUE.toStandardDays());
    }

    @Test
    public void toStandardHours_minValue_overflowsIntHours() {
        assertThrows(ArithmeticException.class, () -> Weeks.MIN_VALUE.toStandardHours());
    }

    @Test
    public void toStandardMinutes_maxValue_overflowsIntMinutes() {
        assertThrows(ArithmeticException.class, () -> Weeks.MAX_VALUE.toStandardMinutes());
    }

    @Test
    public void toStandardSeconds_minValue_overflowsIntSeconds() {
        assertThrows(ArithmeticException.class, () -> Weeks.MIN_VALUE.toStandardSeconds());
    }

    @Test
    public void multipliedBy_largeProduct_overflowsInt() {
        assertThrows(ArithmeticException.class, () -> Weeks.MAX_VALUE.multipliedBy(7));
    }

    @Test
    public void plus_withWeeks_overflow_throwsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> Weeks.MAX_VALUE.plus(Weeks.MAX_VALUE));
    }

    @Test
    public void minus_withWeeks_causingNegationOverflow_throwsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> Weeks.MIN_VALUE.minus(Weeks.MIN_VALUE));
    }

    @Test
    public void plus_withInt_overflow_throwsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> Weeks.weeks(Integer.MIN_VALUE).plus(Integer.MIN_VALUE));
    }

    @Test
    public void minus_withInt_negativeArgumentCausesOverflow_throwsArithmeticException() {
        assertThrows(ArithmeticException.class, () -> Weeks.MAX_VALUE.minus(-876));
    }

    @Test
    public void negated_minValue_overflows() {
        assertThrows(ArithmeticException.class, () -> Weeks.MIN_VALUE.negated());
    }
}