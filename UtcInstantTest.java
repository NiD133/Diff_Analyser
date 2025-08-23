package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Tests for UtcInstant.
 *
 * Notes on terminology used in this test:
 * - MJD refers to Modified Julian Day (days since 1858-11-17).
 * - Leap day refers to a date that includes a leap second (e.g., 1972-12-31).
 * - nano-of-day includes any leap second (i.e., it may go up to 86401s worth of nanos).
 */
public class TestUtcInstant {

    // Known MJD values around the first leap second (1972-12-31)
    private static final long MJD_1972_12_30 = 41681;
    private static final long MJD_1972_12_31_LEAP = 41682; // leap second occurs at 23:59:60
    private static final long MJD_1973_01_01 = 41683;
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    // Time constants
    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private static UtcInstant utc(long mjd, long nanoOfDay) {
        return UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
    }

    private static void assertMjdAndNanos(UtcInstant actual, long expectedMjd, long expectedNanos) {
        assertEquals(expectedMjd, actual.getModifiedJulianDay(), "MJD mismatch");
        assertEquals(expectedNanos, actual.getNanoOfDay(), "nano-of-day mismatch");
    }

    // ---------------------------------------------------------------------
    // Basic type interface checks
    // ---------------------------------------------------------------------

    @Test
    public void test_interfaces() {
        assertTrue(Serializable.class.isAssignableFrom(UtcInstant.class));
        assertTrue(Comparable.class.isAssignableFrom(UtcInstant.class));
    }

    // ---------------------------------------------------------------------
    // Serialization
    // ---------------------------------------------------------------------

    @Test
    public void test_serialization_roundTrip() throws Exception {
        UtcInstant original = utc(2, 3);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(original);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
            assertEquals(original, ois.readObject());
        }
    }

    // ---------------------------------------------------------------------
    // ofModifiedJulianDay(long,long)
    // ---------------------------------------------------------------------

    @Test
    @DisplayName("ofModifiedJulianDay: basic valid values")
    public void ofModifiedJulianDay_validValues() {
        for (long mjd = -2; mjd <= 2; mjd++) {
            for (int nanos = 0; nanos < 10; nanos++) {
                UtcInstant t = utc(mjd, nanos);
                assertMjdAndNanos(t, mjd, nanos);
                assertFalse(t.isLeapSecond());
            }
        }
    }

    @Test
    public void ofModifiedJulianDay_normalDay_lastNanos() {
        UtcInstant t = utc(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1);
        assertMjdAndNanos(t, MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1);
        assertFalse(t.isLeapSecond());
        assertEquals("1972-12-31T23:59:59.999999999Z", t.toString());
    }

    @Test
    public void ofModifiedJulianDay_leapDay_firstLeapSecond() {
        UtcInstant t = utc(MJD_1972_12_31_LEAP, NANOS_PER_DAY);
        assertMjdAndNanos(t, MJD_1972_12_31_LEAP, NANOS_PER_DAY);
        assertTrue(t.isLeapSecond());
        assertEquals("1972-12-31T23:59:60Z", t.toString());
    }

    @Test
    public void ofModifiedJulianDay_leapDay_lastNanos() {
        UtcInstant t = utc(MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1);
        assertMjdAndNanos(t, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1);
        assertTrue(t.isLeapSecond());
        assertEquals("1972-12-31T23:59:60.999999999Z", t.toString());
    }

    @Test
    public void ofModifiedJulianDay_invalid_negativeNanos() {
        assertThrows(DateTimeException.class, () -> utc(MJD_1973_01_01, -1));
    }

    @Test
    public void ofModifiedJulianDay_invalid_tooManyNanos_onNormalDay() {
        assertThrows(DateTimeException.class, () -> utc(MJD_1973_01_01, NANOS_PER_DAY));
    }

    @Test
    public void ofModifiedJulianDay_invalid_tooManyNanos_onLeapDay() {
        assertThrows(DateTimeException.class, () -> utc(MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY));
    }

    // ---------------------------------------------------------------------
    // of(Instant)
    // ---------------------------------------------------------------------

    @Test
    public void ofInstant_basic() {
        // Epoch 1970-01-01T00:00:00Z + 2ns
        UtcInstant test = UtcInstant.of(Instant.ofEpochSecond(0, 2));
        assertMjdAndNanos(test, 40587, 2);
    }

    @Test
    public void ofInstant_null() {
        assertThrows(NullPointerException.class, () -> UtcInstant.of((Instant) null));
    }

    // ---------------------------------------------------------------------
    // of(TaiInstant)
    // ---------------------------------------------------------------------

    @Test
    public void ofTaiInstant_roundTripEquivalence() {
        // Sample a broad range of values. The offset of 10s exists around 1972-01-01.
        for (int days = -1000; days < 1000; days++) {
            for (int secOfDay = 0; secOfDay < 10; secOfDay++) {
                UtcInstant expected = utc(36204 + days, secOfDay * NANOS_PER_SEC + 2L);
                TaiInstant tai = TaiInstant.ofTaiSeconds(days * SECS_PER_DAY + secOfDay + 10, 2);
                assertEquals(expected, UtcInstant.of(tai));
            }
        }
    }

    @Test
    public void ofTaiInstant_null() {
        assertThrows(NullPointerException.class, () -> UtcInstant.of((TaiInstant) null));
    }

    // ---------------------------------------------------------------------
    // parse(CharSequence)
    // ---------------------------------------------------------------------

    @Test
    public void parse_validIsoWithLeapSecond() {
        assertEquals(utc(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC), UtcInstant.parse("1972-12-31T23:59:59Z"));
        assertEquals(utc(MJD_1972_12_31_LEAP, NANOS_PER_DAY), UtcInstant.parse("1972-12-31T23:59:60Z"));
    }

    static Stream<String> data_badParse() {
        return Stream.of(
            "",
            "A",
            "2012-13-01T00:00:00Z" // invalid month
        );
    }

    @ParameterizedTest
    @MethodSource("data_badParse")
    public void parse_invalidStrings(String str) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(str));
    }

    @Test
    public void parse_invalidLeapSecondOnNonLeapDay() {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse("1972-11-11T23:59:60Z"));
    }

    @Test
    public void parse_null() {
        assertThrows(NullPointerException.class, () -> UtcInstant.parse((String) null));
    }

    // ---------------------------------------------------------------------
    // withModifiedJulianDay()
    // ---------------------------------------------------------------------

    static Stream<org.junit.jupiter.params.provider.Arguments> data_withModifiedJulianDay() {
        return Stream.of(
            arguments(0L, 12345L, 1L, 1L, 12345L),
            arguments(0L, 12345L, -1L, -1L, 12345L),
            arguments(7L, 12345L, 2L, 2L, 12345L),
            arguments(7L, 12345L, -2L, -2L, 12345L),
            arguments(-99L, 12345L, 3L, 3L, 12345L),
            arguments(-99L, 12345L, -3L, -3L, 12345L),

            // nano-of-day must remain valid for the target day:
            arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30, null, null),  // invalid
            arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
            arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01, null, null),  // invalid
            arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("data_withModifiedJulianDay")
    public void withModifiedJulianDay_cases(long mjd, long nanos, long newMjd, Long expectedMjd, Long expectedNanos) {
        UtcInstant base = utc(mjd, nanos);
        if (expectedMjd != null) {
            UtcInstant result = base.withModifiedJulianDay(newMjd);
            assertMjdAndNanos(result, expectedMjd, expectedNanos);
        } else {
            assertThrows(DateTimeException.class, () -> base.withModifiedJulianDay(newMjd));
        }
    }

    // ---------------------------------------------------------------------
    // withNanoOfDay()
    // ---------------------------------------------------------------------

    static Stream<org.junit.jupiter.params.provider.Arguments> data_withNanoOfDay() {
        return Stream.of(
            arguments(0L, 12345L, 1L, 0L, 1L),
            arguments(0L, 12345L, -1L, null, null),
            arguments(7L, 12345L, 2L, 7L, 2L),
            arguments(-99L, 12345L, 3L, -99L, 3L),

            arguments(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
            arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1),
            arguments(MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1973_01_01, NANOS_PER_DAY - 1),

            // setting to leap second on non-leap days should fail
            arguments(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY, null, null),
            arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
            arguments(MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_DAY, null, null),

            // last valid nanos on leap day
            arguments(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, null, null),
            arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1),
            arguments(MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, null, null),

            // beyond last valid nanos on leap day
            arguments(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY, null, null),
            arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY, null, null),
            arguments(MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY, null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("data_withNanoOfDay")
    public void withNanoOfDay_cases(long mjd, long nanos, long newNanoOfDay, Long expectedMjd, Long expectedNanos) {
        UtcInstant base = utc(mjd, nanos);
        if (expectedMjd != null) {
            UtcInstant result = base.withNanoOfDay(newNanoOfDay);
            assertMjdAndNanos(result, expectedMjd, expectedNanos);
        } else {
            assertThrows(DateTimeException.class, () -> base.withNanoOfDay(newNanoOfDay));
        }
    }

    // ---------------------------------------------------------------------
    // plus(Duration)
    // ---------------------------------------------------------------------

    static Stream<org.junit.jupiter.params.provider.Arguments> data_plus() {
        // Arguments: mjd, nanos, plusSeconds, plusNanos, expectedMjd, expectedNanos
        return Stream.of(
            arguments(0, 0,  -2 * SECS_PER_DAY, 5, -2, 5),
            arguments(0, 0,  -1 * SECS_PER_DAY, 1, -1, 1),
            arguments(0, 0,  -1 * SECS_PER_DAY, 0, -1, 0),
            arguments(0, 0,  0,        -2, -1,  NANOS_PER_DAY - 2),
            arguments(0, 0,  0,        -1, -1,  NANOS_PER_DAY - 1),
            arguments(0, 0,  0,         0,  0,  0),
            arguments(0, 0,  0,         1,  0,  1),
            arguments(0, 0,  0,         2,  0,  2),
            arguments(0, 0,  1,         0,  0,  1 * NANOS_PER_SEC),
            arguments(0, 0,  2,         0,  0,  2 * NANOS_PER_SEC),
            arguments(0, 0,  3, 333_333_333,  0,  3 * NANOS_PER_SEC + 333_333_333),
            arguments(0, 0,  1 * SECS_PER_DAY, 0,  1, 0),
            arguments(0, 0,  1 * SECS_PER_DAY, 1,  1, 1),
            arguments(0, 0,  2 * SECS_PER_DAY, 5,  2, 5),

            arguments(1, 0,  -2 * SECS_PER_DAY, 5, -1, 5),
            arguments(1, 0,  -1 * SECS_PER_DAY, 1, 0, 1),
            arguments(1, 0,  -1 * SECS_PER_DAY, 0, 0, 0),
            arguments(1, 0,  0,        -2,  0,  NANOS_PER_DAY - 2),
            arguments(1, 0,  0,        -1,  0,  NANOS_PER_DAY - 1),
            arguments(1, 0,  0,         0,  1,  0),
            arguments(1, 0,  0,         1,  1,  1),
            arguments(1, 0,  0,         2,  1,  2),
            arguments(1, 0,  1,         0,  1,  1 * NANOS_PER_SEC),
            arguments(1, 0,  2,         0,  1,  2 * NANOS_PER_SEC),
            arguments(1, 0,  3, 333_333_333,  1,  3 * NANOS_PER_SEC + 333_333_333),
            arguments(1, 0,  1 * SECS_PER_DAY, 0,  2, 0),
            arguments(1, 0,  1 * SECS_PER_DAY, 1,  2, 1),
            arguments(1, 0,  2 * SECS_PER_DAY, 5,  3, 5)
        );
    }

    @ParameterizedTest
    @MethodSource("data_plus")
    public void plus_duration(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant result = utc(mjd, nanos).plus(Duration.ofSeconds(plusSeconds, plusNanos));
        assertMjdAndNanos(result, expectedMjd, expectedNanos);
    }

    @Test
    public void plus_overflowTooBig() {
        UtcInstant i = utc(Long.MAX_VALUE, NANOS_PER_DAY - 1);
        assertThrows(ArithmeticException.class, () -> i.plus(Duration.ofNanos(1)));
    }

    @Test
    public void plus_overflowTooSmall() {
        UtcInstant i = utc(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> i.plus(Duration.ofNanos(-1)));
    }

    // ---------------------------------------------------------------------
    // minus(Duration)
    // ---------------------------------------------------------------------

    static Stream<org.junit.jupiter.params.provider.Arguments> data_minus() {
        // Arguments: mjd, nanos, minusSeconds, minusNanos, expectedMjd, expectedNanos
        return Stream.of(
            arguments(0, 0,  2 * SECS_PER_DAY, -5, -2, 5),
            arguments(0, 0,  1 * SECS_PER_DAY, -1, -1, 1),
            arguments(0, 0,  1 * SECS_PER_DAY, 0, -1, 0),
            arguments(0, 0,  0,          2, -1,  NANOS_PER_DAY - 2),
            arguments(0, 0,  0,          1, -1,  NANOS_PER_DAY - 1),
            arguments(0, 0,  0,          0,  0,  0),
            arguments(0, 0,  0,         -1,  0,  1),
            arguments(0, 0,  0,         -2,  0,  2),
            arguments(0, 0,  -1,         0,  0,  1 * NANOS_PER_SEC),
            arguments(0, 0,  -2,         0,  0,  2 * NANOS_PER_SEC),
            arguments(0, 0,  -3, -333_333_333,  0,  3 * NANOS_PER_SEC + 333_333_333),
            arguments(0, 0,  -1 * SECS_PER_DAY, 0,  1, 0),
            arguments(0, 0,  -1 * SECS_PER_DAY, -1,  1, 1),
            arguments(0, 0,  -2 * SECS_PER_DAY, -5,  2, 5),

            arguments(1, 0,  2 * SECS_PER_DAY, -5, -1, 5),
            arguments(1, 0,  1 * SECS_PER_DAY, -1, 0, 1),
            arguments(1, 0,  1 * SECS_PER_DAY, 0, 0, 0),
            arguments(1, 0,  0,          2,  0,  NANOS_PER_DAY - 2),
            arguments(1, 0,  0,          1,  0,  NANOS_PER_DAY - 1),
            arguments(1, 0,  0,          0,  1,  0),
            arguments(1, 0,  0,         -1,  1,  1),
            arguments(1, 0,  0,         -2,  1,  2),
            arguments(1, 0,  -1,         0,  1,  1 * NANOS_PER_SEC),
            arguments(1, 0,  -2,         0,  1,  2 * NANOS_PER_SEC),
            arguments(1, 0,  -3, -333_333_333,  1,  3 * NANOS_PER_SEC + 333_333_333),
            arguments(1, 0,  -1 * SECS_PER_DAY, 0,  2, 0),
            arguments(1, 0,  -1 * SECS_PER_DAY, -1,  2, 1),
            arguments(1, 0,  -2 * SECS_PER_DAY, -5,  3, 5)
        );
    }

    @ParameterizedTest
    @MethodSource("data_minus")
    public void minus_duration(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant result = utc(mjd, nanos).minus(Duration.ofSeconds(minusSeconds, minusNanos));
        assertMjdAndNanos(result, expectedMjd, expectedNanos);
    }

    @Test
    public void minus_overflowTooSmall() {
        UtcInstant i = utc(Long.MIN_VALUE, 0);
        assertThrows(ArithmeticException.class, () -> i.minus(Duration.ofNanos(1)));
    }

    @Test
    public void minus_overflowTooBig() {
        UtcInstant i = utc(Long.MAX_VALUE, NANOS_PER_DAY - 1);
        assertThrows(ArithmeticException.class, () -> i.minus(Duration.ofNanos(-1)));
    }

    // ---------------------------------------------------------------------
    // durationUntil()
    // ---------------------------------------------------------------------

    @Test
    public void durationUntil_oneDayNoLeap() {
        UtcInstant utc1 = utc(MJD_1972_12_30, 0);
        UtcInstant utc2 = utc(MJD_1972_12_31_LEAP, 0);
        Duration test = utc1.durationUntil(utc2);
        assertEquals(86400, test.getSeconds());
        assertEquals(0, test.getNano());
    }

    @Test
    public void durationUntil_oneDayLeap() {
        UtcInstant utc1 = utc(MJD_1972_12_31_LEAP, 0);
        UtcInstant utc2 = utc(MJD_1973_01_01, 0);
        Duration test = utc1.durationUntil(utc2);
        assertEquals(86401, test.getSeconds());
        assertEquals(0, test.getNano());
    }

    @Test
    public void durationUntil_oneDayLeap_negative() {
        UtcInstant utc1 = utc(MJD_1973_01_01, 0);
        UtcInstant utc2 = utc(MJD_1972_12_31_LEAP, 0);
        Duration test = utc1.durationUntil(utc2);
        assertEquals(-86401, test.getSeconds());
        assertEquals(0, test.getNano());
    }

    // ---------------------------------------------------------------------
    // toTaiInstant()
    // ---------------------------------------------------------------------

    @Test
    public void toTaiInstant_roundTripEquivalence() {
        for (int days = -1000; days < 1000; days++) {
            for (int secOfDay = 0; secOfDay < 10; secOfDay++) {
                UtcInstant utc = utc(36204 + days, secOfDay * NANOS_PER_SEC + 2L);
                TaiInstant tai = utc.toTaiInstant();
                assertEquals(days * SECS_PER_DAY + secOfDay + 10, tai.getTaiSeconds());
                assertEquals(2, tai.getNano());
            }
        }
    }

    @Test
    public void toTaiInstant_maxInvalid() {
        UtcInstant utc = utc(Long.MAX_VALUE, 0);
        assertThrows(ArithmeticException.class, utc::toTaiInstant);
    }

    // ---------------------------------------------------------------------
    // toInstant()
    // ---------------------------------------------------------------------

    @Test
    public void toInstant_basic() {
        for (int days = -1000; days < 1000; days++) {
            for (int secOfDay = 0; secOfDay < 10; secOfDay++) {
                Instant expected = Instant.ofEpochSecond(315532800L + days * SECS_PER_DAY + secOfDay).plusNanos(2);
                UtcInstant test = utc(44239 + days, secOfDay * NANOS_PER_SEC + 2);
                assertEquals(expected, test.toInstant());
            }
        }
    }

    // ---------------------------------------------------------------------
    // compareTo(), isBefore(), isAfter()
    // ---------------------------------------------------------------------

    @Test
    public void comparisons_totalOrdering() {
        doTest_comparisons_UtcInstant(
            utc(-2L, 0),
            utc(-2L, NANOS_PER_DAY - 2),
            utc(-2L, NANOS_PER_DAY - 1),
            utc(-1L, 0),
            utc(-1L, 1),
            utc(-1L, NANOS_PER_DAY - 2),
            utc(-1L, NANOS_PER_DAY - 1),
            utc(0L, 0),
            utc(0L, 1),
            utc(0L, 2),
            utc(0L, NANOS_PER_DAY - 1),
            utc(1L, 0),
            utc(2L, 0)
        );
    }

    private void doTest_comparisons_UtcInstant(UtcInstant... instants) {
        for (int i = 0; i < instants.length; i++) {
            UtcInstant a = instants[i];
            for (int j = 0; j < instants.length; j++) {
                UtcInstant b = instants[j];
                if (i < j) {
                    assertEquals(-1, a.compareTo(b));
                    assertNotEquals(a, b);
                    assertTrue(a.isBefore(b));
                    assertFalse(a.isAfter(b));
                } else if (i > j) {
                    assertEquals(1, a.compareTo(b));
                    assertNotEquals(a, b);
                    assertFalse(a.isBefore(b));
                    assertTrue(a.isAfter(b));
                } else {
                    assertEquals(0, a.compareTo(b));
                    assertEquals(a, b);
                    assertFalse(a.isBefore(b));
                    assertFalse(a.isAfter(b));
                }
            }
        }
    }

    @Test
    public void compareTo_null() {
        UtcInstant a = utc(0L, 0);
        assertThrows(NullPointerException.class, () -> a.compareTo(null));
    }

    @Test
    @SuppressWarnings({"unchecked", "rawtypes"})
    public void compareTo_nonUtcInstant() {
        Comparable c = utc(0L, 2);
        assertThrows(ClassCastException.class, () -> c.compareTo(new Object()));
    }

    // ---------------------------------------------------------------------
    // equals() / hashCode()
    // ---------------------------------------------------------------------

    @Test
    public void equals_and_hashCode() {
        new EqualsTester()
            .addEqualityGroup(utc(5L, 20), utc(5L, 20))
            .addEqualityGroup(utc(5L, 30), utc(5L, 30))
            .addEqualityGroup(utc(6L, 20), utc(6L, 20))
            .testEquals();
    }

    // ---------------------------------------------------------------------
    // toString() and parse round-trip
    // ---------------------------------------------------------------------

    static Stream<org.junit.jupiter.params.provider.Arguments> data_toString() {
        return Stream.of(
            arguments(40587, 0, "1970-01-01T00:00:00Z"),
            arguments(40588, 1, "1970-01-02T00:00:00.000000001Z"),
            arguments(40588, 999, "1970-01-02T00:00:00.000000999Z"),
            arguments(40588, 1000, "1970-01-02T00:00:00.000001Z"),
            arguments(40588, 999000, "1970-01-02T00:00:00.000999Z"),
            arguments(40588, 1_000_000, "1970-01-02T00:00:00.001Z"),
            arguments(40618, 999_999_999, "1970-02-01T00:00:00.999999999Z"),
            arguments(40619, 1_000_000_000, "1970-02-02T00:00:01Z"),
            arguments(40620, 60L * 1_000_000_000L, "1970-02-03T00:01:00Z"),
            arguments(40621, 60L * 60L * 1_000_000_000L, "1970-02-04T01:00:00Z"),
            arguments(MJD_1972_12_31_LEAP, 24L * 60L * 60L * 1_000_000_000L - 1_000_000_000L, "1972-12-31T23:59:59Z"),
            arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
            arguments(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z")
        );
    }

    @ParameterizedTest
    @MethodSource("data_toString")
    public void toString_formatsCorrectly(long mjd, long nod, String expected) {
        assertEquals(expected, utc(mjd, nod).toString());
    }

    @ParameterizedTest
    @MethodSource("data_toString")
    public void toString_parse_roundTrip(long mjd, long nod, String str) {
        assertEquals(utc(mjd, nod), UtcInstant.parse(str));
    }
}