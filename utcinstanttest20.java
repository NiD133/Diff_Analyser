package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link UtcInstant}.
 * This class focuses on parsing, modification, and arithmetic operations.
 */
class UtcInstantTest {

    private static final long MJD_1972_12_30 = 41681;
    private static final long MJD_1972_12_31_LEAP = 41682; // A day with a leap second
    private static final long MJD_1973_01_01 = 41683;
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365; // Another leap day

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    @Nested
    @DisplayName("Parse Tests")
    class ParseTests {

        @ParameterizedTest
        @ValueSource(strings = {
            "",                      // empty string
            "A",                     // non-ISO format
            "2012-13-01T00:00:00Z"   // invalid month
        })
        void parse_withInvalidText_throwsException(String text) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(text));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#data_toStringAndParse")
        @DisplayName("parse(validString) creates correct instant")
        void parse_validString_createsCorrectInstant(long mjd, long nod, String text) {
            UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nod);
            assertEquals(expected, UtcInstant.parse(text));
        }
    }

    @Nested
    @DisplayName("withModifiedJulianDay() Tests")
    class WithModifiedJulianDayTests {

        public static Object[][] data_withModifiedJulianDay_valid() {
            // @formatter:off
            return new Object[][] {
                // { initialMjd, initialNanos, newMjd, expectedMjd, expectedNanos }
                // Simple cases with non-leap days
                { 0L, 12345L, 1L, 1L, 12345L },
                { 0L, 12345L, -1L, -1L, 12345L },
                // Change from one leap day to another, preserving nano-of-day
                { MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY },
                // Identity change on a leap day
                { MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY },
            };
            // @formatter:on
        }

        @ParameterizedTest
        @MethodSource("data_withModifiedJulianDay_valid")
        void withModifiedJulianDay_givenValidDay_returnsUpdatedInstant(
                long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            UtcInstant result = initial.withModifiedJulianDay(newMjd);
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        public static Object[][] data_withModifiedJulianDay_invalid() {
            // @formatter:off
            return new Object[][] {
                // { initialMjd, initialNanos, newMjdToSet }
                // From a leap day with 86401 seconds to a normal day (which cannot have that many nanos)
                { MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30 },
                { MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01 },
            };
            // @formatter:on
        }

        @ParameterizedTest
        @MethodSource("data_withModifiedJulianDay_invalid")
        void withModifiedJulianDay_givenInvalidDayForNanos_throwsException(
                long initialMjd, long initialNanos, long newMjd) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjd));
        }
    }

    @Nested
    @DisplayName("withNanoOfDay() Tests")
    class WithNanoOfDayTests {

        public static Object[][] data_withNanoOfDay_valid() {
            // @formatter:off
            return new Object[][] {
                // { mjd, initialNanos, newNanos, expectedMjd, expectedNanos }
                // Simple cases
                { 0L, 12345L, 1L, 0L, 1L },
                { 7L, 12345L, 2L, 7L, 2L },
                // Normal day, max valid nanos
                { MJD_1972_12_30, 0L, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1 },
                // Leap day, nanos valid for normal day
                { MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1 },
                // Leap day, nanos in leap second range
                { MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY },
                // Leap day, max valid nanos
                { MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1 },
            };
            // @formatter:on
        }

        @ParameterizedTest
        @MethodSource("data_withNanoOfDay_valid")
        void withNanoOfDay_givenValidNanos_returnsUpdatedInstant(
                long mjd, long initialNanos, long newNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
            UtcInstant result = initial.withNanoOfDay(newNanos);
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        public static Object[][] data_withNanoOfDay_invalid() {
            // @formatter:off
            return new Object[][] {
                // { mjd, initialNanos, newNanosToSet }
                // Negative nanos
                { 0L, 12345L, -1L },
                // Nanos too large for a normal day
                { MJD_1972_12_30, 0L, NANOS_PER_DAY },
                { MJD_1973_01_01, 0L, NANOS_PER_DAY },
                { MJD_1972_12_30, 0L, NANOS_PER_LEAP_DAY - 1 },
                // Nanos too large for a leap day
                { MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY },
            };
            // @formatter:on
        }

        @ParameterizedTest
        @MethodSource("data_withNanoOfDay_invalid")
        void withNanoOfDay_givenInvalidNanos_throwsException(long mjd, long initialNanos, long newNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
            assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(newNanos));
        }
    }

    @Nested
    @DisplayName("Arithmetic Tests")
    class ArithmeticTests {
        public static Object[][] data_plus() {
            // @formatter:off
            // mjd, nanos, plusSeconds, plusNanos, expectedMjd, expectedNanos
            return new Object[][] {
                { 0, 0, -2 * SECS_PER_DAY, 5, -2, 5 },
                { 0, 0, -1 * SECS_PER_DAY, 1, -1, 1 },
                { 0, 0, -1 * SECS_PER_DAY, 0, -1, 0 },
                { 0, 0, 0, -2, -1, NANOS_PER_DAY - 2 },
                { 0, 0, 0, -1, -1, NANOS_PER_DAY - 1 },
                { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 1, 0, 1 },
                { 0, 0, 1 * SECS_PER_DAY, 0, 1, 0 },
                { 1, 0, -1 * SECS_PER_DAY, 0, 0, 0 },
                { 1, 0, 0, -2, 0, NANOS_PER_DAY - 2 },
                { 1, 0, 0, -1, 0, NANOS_PER_DAY - 1 },
                { 1, 0, 0, 0, 1, 0 },
                { 1, 0, 1 * SECS_PER_DAY, 0, 2, 0 },
            };
            // @formatter:on
        }

        @ParameterizedTest
        @MethodSource("data_plus")
        void plus_withDuration_returnsCorrectInstant(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            UtcInstant result = initial.plus(Duration.ofSeconds(plusSeconds, plusNanos));
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        public static Object[][] data_minus() {
            // @formatter:off
            // mjd, nanos, minusSeconds, minusNanos, expectedMjd, expectedNanos
            return new Object[][] {
                { 0, 0, 2 * SECS_PER_DAY, -5, -2, 5 },
                { 0, 0, 1 * SECS_PER_DAY, -1, -1, 1 },
                { 0, 0, 1 * SECS_PER_DAY, 0, -1, 0 },
                { 0, 0, 0, 2, -1, NANOS_PER_DAY - 2 },
                { 0, 0, 0, 1, -1, NANOS_PER_DAY - 1 },
                { 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, -1, 0, 1 },
                { 0, 0, -1 * SECS_PER_DAY, 0, 1, 0 },
                { 1, 0, 1 * SECS_PER_DAY, 0, 0, 0 },
                { 1, 0, 0, 2, 0, NANOS_PER_DAY - 2 },
                { 1, 0, 0, 1, 0, NANOS_PER_DAY - 1 },
                { 1, 0, 0, 0, 1, 0 },
                { 1, 0, -1 * SECS_PER_DAY, 0, 2, 0 },
            };
            // @formatter:on
        }

        @ParameterizedTest
        @MethodSource("data_minus")
        void minus_withDuration_returnsCorrectInstant(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            UtcInstant result = initial.minus(Duration.ofSeconds(minusSeconds, minusNanos));
            assertEquals(expectedMjd, result.getModifiedJulianDay());
assertEquals(expectedNanos, result.getNanoOfDay());
        }

        @Test
        void minus_whenResultUnderflows_throwsArithmeticException() {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(Long.MIN_VALUE, 0);
            assertThrows(ArithmeticException.class, () -> instant.minus(Duration.ofNanos(1)));
        }

        @Test
        void plus_whenResultOverflows_throwsArithmeticException() {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, NANOS_PER_DAY - 1);
            // This was a 'minus' test with a negative duration, which is equivalent to 'plus'
            assertThrows(ArithmeticException.class, () -> instant.plus(Duration.ofNanos(1)));
        }
    }

    @Nested
    @DisplayName("toString() Tests")
    class ToStringTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#data_toStringAndParse")
        @DisplayName("toString() returns correct ISO-8601 format")
        void toString_returnsIso8601Format(long mjd, long nod, String expected) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nod);
            assertEquals(expected, instant.toString());
        }
    }

    // Data provider shared by toString and parse tests
    public static Object[][] data_toStringAndParse() {
        // @formatter:off
        // mjd, nano-of-day, expected string representation
        return new Object[][] {
            { 40587, 0, "1970-01-01T00:00:00Z" },
            { 40588, 1, "1970-01-02T00:00:00.000000001Z" },
            { 40588, 999, "1970-01-02T00:00:00.000000999Z" },
            { 40588, 1000, "1970-01-02T00:00:00.000001Z" },
            { 40588, 999000, "1970-01-02T00:00:00.000999Z" },
            { 40588, 1000000, "1970-01-02T00:00:00.001Z" },
            { 40618, 999999999, "1970-02-01T00:00:00.999999999Z" },
            { 40619, NANOS_PER_SEC, "1970-02-02T00:00:01Z" },
            { 40620, 60 * NANOS_PER_SEC, "1970-02-03T00:01:00Z" },
            { 40621, 60 * 60 * NANOS_PER_SEC, "1970-02-04T01:00:00Z" },
            // Leap second cases
            { MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z" }, // Before leap second
            { MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z" },                 // During leap second
            { MJD_1973_01_01, 0, "1973-01-01T00:00:00Z" },                                  // After leap second
        };
        // @formatter:on
    }
}