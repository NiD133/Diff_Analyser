package org.threeten.extra.scale;

import static org.junit.Assert.*;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeParseException;

import org.junit.Test;

/**
 * Readable, behavior-focused tests for UtcInstant.
 * 
 * Design goals:
 * - Clear test names describing behavior.
 * - Group related assertions; avoid redundant checks.
 * - Prefer small, realistic inputs over large/random values.
 * - Verify key contracts (immutability, equality, ordering, conversions).
 * - Exercise both happy paths and common error paths.
 */
public class UtcInstantTest {

    // ---------------------------------------------------------------------
    // Factory methods and basic getters
    // ---------------------------------------------------------------------

    @Test
    public void ofModifiedJulianDay_returnsExpectedFields() {
        UtcInstant utc = UtcInstant.ofModifiedJulianDay(0L, 0L);

        assertEquals("MJD should be preserved", 0L, utc.getModifiedJulianDay());
        assertEquals("nanoOfDay should be preserved", 0L, utc.getNanoOfDay());
        assertEquals("toString should be ISO-8601 with Z", "1858-11-17T00:00:00Z", utc.toString());
    }

    @Test
    public void ofInstant_epochRoundTrip() {
        Instant epoch = Instant.EPOCH; // 1970-01-01T00:00:00Z
        UtcInstant utc = UtcInstant.of(epoch);

        assertEquals(40587L, utc.getModifiedJulianDay());
        assertEquals(0L, utc.getNanoOfDay());
        assertEquals(epoch, utc.toInstant());
    }

    @Test
    public void ofTaiInstant_roundTripViaUtc() {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(0L, 123456789L);
        TaiInstant tai = TaiInstant.of(original);
        UtcInstant rehydrated = UtcInstant.of(tai);

        assertEquals(original, rehydrated);
    }

    // ---------------------------------------------------------------------
    // Withers and immutability
    // ---------------------------------------------------------------------

    @Test
    public void withers_doNotMutateOriginal_andReturnAdjustedCopies() {
        UtcInstant original = UtcInstant.ofModifiedJulianDay(-10L, 1000L);

        UtcInstant changedNanos = original.withNanoOfDay(2000L);
        assertEquals(-10L, changedNanos.getModifiedJulianDay());
        assertEquals(2000L, changedNanos.getNanoOfDay());

        UtcInstant changedMjd = original.withModifiedJulianDay(5L);
        assertEquals(5L, changedMjd.getModifiedJulianDay());
        assertEquals(1000L, changedMjd.getNanoOfDay());

        // original unchanged
        assertEquals(-10L, original.getModifiedJulianDay());
        assertEquals(1000L, original.getNanoOfDay());
    }

    // ---------------------------------------------------------------------
    // Equality, hashCode, ordering
    // ---------------------------------------------------------------------

    @Test
    public void equality_reflexive_symmetric_notEqualToOtherTypes() {
        UtcInstant a = UtcInstant.ofModifiedJulianDay(1L, 2L);
        UtcInstant b = UtcInstant.ofModifiedJulianDay(1L, 2L);
        UtcInstant c = UtcInstant.ofModifiedJulianDay(1L, 3L);

        assertTrue(a.equals(a));
        assertTrue(a.equals(b) && b.equals(a));
        assertEquals(a.hashCode(), b.hashCode());
        assertFalse(a.equals(c));
        assertFalse(a.equals("not a UtcInstant"));
    }

    @Test
    public void compareTo_isBefore_isAfter_followTimeLineOrdering() {
        UtcInstant earlier = UtcInstant.ofModifiedJulianDay(100L, 0L);
        UtcInstant laterSameDay = UtcInstant.ofModifiedJulianDay(100L, 1L);
        UtcInstant laterDay = UtcInstant.ofModifiedJulianDay(101L, 0L);

        assertTrue(earlier.compareTo(laterSameDay) < 0);
        assertTrue(laterSameDay.compareTo(earlier) > 0);
        assertEquals(0, laterSameDay.compareTo(laterSameDay));

        assertTrue(earlier.isBefore(laterSameDay));
        assertTrue(laterSameDay.isBefore(laterDay));
        assertTrue(laterDay.isAfter(laterSameDay));
        assertFalse(earlier.isAfter(laterDay));
    }

    // ---------------------------------------------------------------------
    // Parsing and formatting
    // ---------------------------------------------------------------------

    @Test
    public void parse_and_toString_roundTrip() {
        String text = "1858-11-17T00:00:00Z"; // MJD 0 start point
        UtcInstant parsed = UtcInstant.parse(text);
        assertEquals(text, parsed.toString());
        assertEquals(0L, parsed.getModifiedJulianDay());
        assertEquals(0L, parsed.getNanoOfDay());
    }

    @Test
    public void parse_withFractionalNanos() {
        // From original suite: 1958-01-01T00:53:27.000001Z
        UtcInstant utc = UtcInstant.parse("1958-01-01T00:53:27.000001Z");

        assertEquals(36204L, utc.getModifiedJulianDay());
        assertEquals(3_207_000_001_000L, utc.getNanoOfDay());
    }

    @Test(expected = DateTimeParseException.class)
    public void parse_invalidText_throws() {
        UtcInstant.parse("not-a-date");
    }

    @Test(expected = NullPointerException.class)
    public void parse_null_throws() {
        UtcInstant.parse((CharSequence) null);
    }

    // ---------------------------------------------------------------------
    // Arithmetic and duration
    // ---------------------------------------------------------------------

    @Test
    public void plus_minus_durationUntil_areConsistent() {
        UtcInstant start = UtcInstant.parse("1972-01-01T00:00:00Z");
        Duration delta = Duration.ofSeconds(90); // keep it simple and away from leap seconds

        UtcInstant later = start.plus(delta);
        assertEquals("plus should move forward by the duration", start, later.minus(delta));

        Duration computed = start.durationUntil(later);
        assertEquals(delta, computed);
        assertEquals("Adding the durationUntil should yield the other instant", later, start.plus(computed));
    }

    @Test
    public void durationUntil_zeroWhenSameInstant() {
        UtcInstant a = UtcInstant.ofModifiedJulianDay(0L, 0L);
        assertEquals(Duration.ZERO, a.durationUntil(a));
    }

    // ---------------------------------------------------------------------
    // Leap-second checks (simple non-leap case)
    // ---------------------------------------------------------------------

    @Test
    public void isLeapSecond_falseForOrdinarySecond() {
        UtcInstant ordinary = UtcInstant.parse("1970-01-01T00:00:00Z");
        assertFalse(ordinary.isLeapSecond());
    }

    // ---------------------------------------------------------------------
    // Error handling and preconditions
    // ---------------------------------------------------------------------

    @Test(expected = DateTimeException.class)
    public void ofModifiedJulianDay_negativeNanoOfDay_throws() {
        UtcInstant.ofModifiedJulianDay(0L, -1L);
    }

    @Test(expected = DateTimeException.class)
    public void withNanoOfDay_negativeValue_throws() {
        UtcInstant.ofModifiedJulianDay(0L, 0L).withNanoOfDay(-1L);
    }

    @Test(expected = NullPointerException.class)
    public void of_withNullInstant_throws() {
        UtcInstant.of((Instant) null);
    }

    @Test(expected = NullPointerException.class)
    public void of_withNullTaiInstant_throws() {
        UtcInstant.of((TaiInstant) null);
    }

    @Test(expected = NullPointerException.class)
    public void plus_nullDuration_throws() {
        UtcInstant.ofModifiedJulianDay(0L, 0L).plus(null);
    }

    @Test(expected = NullPointerException.class)
    public void minus_nullDuration_throws() {
        UtcInstant.ofModifiedJulianDay(0L, 0L).minus(null);
    }

    @Test(expected = NullPointerException.class)
    public void compareTo_null_throws() {
        UtcInstant.ofModifiedJulianDay(0L, 0L).compareTo(null);
    }

    @Test(expected = NullPointerException.class)
    public void isBefore_null_throws() {
        UtcInstant.ofModifiedJulianDay(0L, 0L).isBefore(null);
    }

    @Test(expected = NullPointerException.class)
    public void isAfter_null_throws() {
        UtcInstant.ofModifiedJulianDay(0L, 0L).isAfter(null);
    }

    @Test(expected = NullPointerException.class)
    public void durationUntil_null_throws() {
        UtcInstant.ofModifiedJulianDay(0L, 0L).durationUntil(null);
    }
}