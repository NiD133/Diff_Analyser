package org.threeten.extra.scale;

import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.*;

/**
 * Readable, intention-revealing tests for TaiInstant.
 *
 * This suite focuses on:
 * - Factory behavior and normalization
 * - Basic value semantics (getters, withers, equals/hashCode)
 * - Arithmetic (plus, minus, durationUntil)
 * - Comparison helpers (isBefore/isAfter/compareTo)
 * - Parsing and formatting round-trips
 * - Null and range validation
 *
 * Notes:
 * - Tests avoid environment-dependent conversions (e.g., Instant/UtcInstant)
 *   to keep them deterministic and easy to maintain.
 */
public class TaiInstantTest {

    // Factory and normalization

    @Test
    public void ofTaiSeconds_normalizesNanoseconds_perJavadocExamples() {
        TaiInstant a = TaiInstant.ofTaiSeconds(3, 1);
        TaiInstant b = TaiInstant.ofTaiSeconds(4, -999_999_999);
        TaiInstant c = TaiInstant.ofTaiSeconds(2, 1_000_000_001);

        // All three represent the same instant per the javadoc examples
        assertEquals(a, b);
        assertEquals(a, c);

        // And equal objects must share the same hash code
        assertEquals(a.hashCode(), b.hashCode());
        assertEquals(a.hashCode(), c.hashCode());
    }

    // Getters and "with" methods

    @Test
    public void withNano_replacesNanoWithinRange() {
        TaiInstant base = TaiInstant.ofTaiSeconds(10L, 123);
        TaiInstant changed = base.withNano(456);

        assertEquals(10L, changed.getTaiSeconds());
        assertEquals(456, changed.getNano());
        assertNotEquals(base, changed);
    }

    @Test
    public void withNano_rejectsOutOfRangeValues() {
        TaiInstant base = TaiInstant.ofTaiSeconds(0, 0);

        assertThrows(IllegalArgumentException.class, () -> base.withNano(-1));
        assertThrows(IllegalArgumentException.class, () -> base.withNano(1_000_000_000));
    }

    @Test
    public void withTaiSeconds_changesSecondsOnly() {
        TaiInstant base = TaiInstant.ofTaiSeconds(100, 789);
        TaiInstant changed = base.withTaiSeconds(200);

        assertEquals(200L, changed.getTaiSeconds());
        assertEquals(789, changed.getNano());
        assertNotEquals(base, changed);
    }

    // Arithmetic: plus / minus

    @Test
    public void plus_crossesSecondBoundary() {
        TaiInstant base = TaiInstant.ofTaiSeconds(1, 500_000_000);
        TaiInstant result = base.plus(Duration.ofNanos(600_000_000));

        assertEquals(TaiInstant.ofTaiSeconds(2, 100_000_000), result);
    }

    @Test
    public void minus_crossesSecondBoundary() {
        TaiInstant base = TaiInstant.ofTaiSeconds(1, 500_000_000);
        TaiInstant result = base.minus(Duration.ofNanos(700_000_000));

        // 1.500000000s - 0.700000000s = 0.800000000s
        assertEquals(TaiInstant.ofTaiSeconds(0, 800_000_000), result);
    }

    @Test
    public void plusMinus_zeroDuration_yieldsEqualInstance() {
        TaiInstant base = TaiInstant.ofTaiSeconds(42, 123_456_789);
        Duration zero = Duration.ZERO;

        TaiInstant plusZero = base.plus(zero);
        TaiInstant minusZero = base.minus(zero);

        assertEquals(base, plusZero);
        assertEquals(base, minusZero);
    }

    @Test
    public void plusMinus_nullDuration_throwsNpe() {
        TaiInstant base = TaiInstant.ofTaiSeconds(0, 0);

        assertThrows(NullPointerException.class, () -> base.plus(null));
        assertThrows(NullPointerException.class, () -> base.minus(null));
    }

    // Duration until

    @Test
    public void durationUntil_roundTripWithPlus() {
        TaiInstant start = TaiInstant.ofTaiSeconds(10, 10);
        Duration delta = Duration.ofSeconds(5, 20);

        TaiInstant end = start.plus(delta);

        assertEquals(delta, start.durationUntil(end));
        assertEquals(delta.negated(), end.durationUntil(start));
    }

    @Test
    public void durationUntil_null_throwsNpe() {
        TaiInstant base = TaiInstant.ofTaiSeconds(0, 0);
        assertThrows(NullPointerException.class, () -> base.durationUntil(null));
    }

    // Comparison helpers

    @Test
    public void comparison_isBefore_isAfter_compareTo() {
        TaiInstant smaller = TaiInstant.ofTaiSeconds(1, 2);
        TaiInstant bigger = TaiInstant.ofTaiSeconds(1, 3);

        assertTrue(smaller.isBefore(bigger));
        assertFalse(bigger.isBefore(smaller));

        assertTrue(bigger.isAfter(smaller));
        assertFalse(smaller.isAfter(bigger));

        assertTrue(smaller.compareTo(bigger) < 0);
        assertTrue(bigger.compareTo(smaller) > 0);
        assertEquals(0, smaller.compareTo(TaiInstant.ofTaiSeconds(1, 2)));
    }

    @Test
    public void comparison_nulls_throwNpe() {
        TaiInstant base = TaiInstant.ofTaiSeconds(0, 0);

        assertThrows(NullPointerException.class, () -> base.isBefore(null));
        assertThrows(NullPointerException.class, () -> base.isAfter(null));
        assertThrows(NullPointerException.class, () -> base.compareTo(null));
    }

    // Equality and hashCode

    @Test
    public void equals_and_hashCode() {
        TaiInstant a = TaiInstant.ofTaiSeconds(7, 8);
        TaiInstant b = TaiInstant.ofTaiSeconds(7, 8);
        TaiInstant c = TaiInstant.ofTaiSeconds(7, 9);

        assertEquals(a, a);           // reflexive
        assertEquals(a, b);           // same state
        assertEquals(a.hashCode(), b.hashCode());

        assertNotEquals(a, c);        // different nano
        assertNotEquals(a, new Object());
    }

    // Parsing and formatting

    @Test
    public void toString_and_parse_roundTrip() {
        TaiInstant x = TaiInstant.ofTaiSeconds(1, 0);
        String text = x.toString();

        assertEquals("1.000000000s(TAI)", text);
        assertEquals(x, TaiInstant.parse(text));
    }

    @Test
    public void parse_rejectsInvalidFormat() {
        // wrong number of nano digits
        assertThrows(java.time.format.DateTimeParseException.class,
                () -> TaiInstant.parse("0.00000000s(TAI)"));

        // completely wrong format
        assertThrows(java.lang.IllegalStateException.class,
                () -> TaiInstant.parse("0.000000000s(TAI")); // missing ')'
    }

    @Test
    public void parse_null_throwsNpe() {
        assertThrows(NullPointerException.class, () -> TaiInstant.parse(null));
    }
}