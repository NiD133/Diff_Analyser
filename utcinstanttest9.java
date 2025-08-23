package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for the {@link UtcInstant} class, focusing on modification, arithmetic,
 * parsing, and formatting.
 */
@DisplayName("UtcInstant")
class UtcInstantTest {

    // --- Constants for test data clarity ---

    // Modified Julian Day for 1972-12-30 (a non-leap day)
    private static final long MJD_1972_12_30 = 41681;
    // Modified Julian Day for 1972-12-31 (a leap-second day)
    private static final long MJD_1972_12_31_LEAP = 41682;
    // Modified Julian Day for 1973-01-01 (day after a leap-second day)
    private static final long MJD_1973_01_01 = 41683;
    // Modified Julian Day for 1973-12-31 (another leap-second day)
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    // A day with a leap second has one extra second in nanoseconds
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    @Nested
    @DisplayName("factory methods")
    class FactoryTest {

        @ParameterizedTest
        @ValueSource(strings = {"", "A", "2012-13-01T00:00:00Z"})
        void parse_withInvalidString_shouldThrowException(String invalidString) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
        }

        @Test
        void ofModifiedJulianDay_withNanosTooLargeForLeapDay_shouldThrowException() {
            // A leap day can have up to NANOS_PER_LEAP_DAY - 1 nanos.
            assertThrows(DateTimeException.class,
                () -> UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY));
        }
    }

    @Nested
    @DisplayName("withModifiedJulianDay()")
    class WithModifiedJulianDayTest {

        static Stream<Arguments> shouldUpdateModifiedJulianDayArgs() {
            return Stream.of(
                Arguments.of("Positive MJD", 0L, 12345L, 1L, 1L, 12345L),
                Arguments.of("Negative MJD", 0L, 12345L, -1L, -1L, 12345L),
                Arguments.of("Leap day to leap day", MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY),
                Arguments.of("Leap day to same day", MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("shouldUpdateModifiedJulianDayArgs")
        void shouldUpdateModifiedJulianDay(String caseName, long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            UtcInstant result = initial.withModifiedJulianDay(newMjd);

            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        static Stream<Arguments> shouldThrowExceptionForInvalidDayArgs() {
            return Stream.of(
                Arguments.of("From leap day to non-leap day with leap nanos", MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
                Arguments.of("From leap day to another non-leap day with leap nanos", MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("shouldThrowExceptionForInvalidDayArgs")
        void whenNanoOfDayBecomesInvalid_shouldThrowException(String caseName, long initialMjd, long initialNanos, long newMjd) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjd));
        }
    }

    @Nested
    @DisplayName("withNanoOfDay()")
    class WithNanoOfDayTest {

        static Stream<Arguments> shouldUpdateNanoOfDayArgs() {
            return Stream.of(
                Arguments.of("Simple case", 0L, 12345L, 1L, 0L, 1L),
                Arguments.of("End of normal day", MJD_1972_12_30, 0L, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
                Arguments.of("End of leap day", MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1),
                Arguments.of("Leap second on leap day", MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("shouldUpdateNanoOfDayArgs")
        void shouldUpdateNanoOfDay(String caseName, long mjd, long initialNanos, long newNanoOfDay, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
            UtcInstant result = initial.withNanoOfDay(newNanoOfDay);

            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        static Stream<Arguments> shouldThrowExceptionForInvalidNanosArgs() {
            return Stream.of(
                Arguments.of("Negative nano of day", 0L, -1L),
                Arguments.of("Nano of day too large for normal day", MJD_1972_12_30, NANOS_PER_DAY),
                Arguments.of("Nano of day too large for another normal day", MJD_1973_01_01, NANOS_PER_DAY),
                Arguments.of("Nano of day (leap day size) too large for normal day", MJD_1972_12_30, NANOS_PER_LEAP_DAY - 1),
                Arguments.of("Nano of day too large for leap day", MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("shouldThrowExceptionForInvalidNanosArgs")
        void withInvalidValue_shouldThrowException(String caseName, long mjd, long invalidNanoOfDay) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, 0);
            assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(invalidNanoOfDay));
        }
    }

    @Nested
    @DisplayName("arithmetic")
    class ArithmeticTest {

        static Stream<Arguments> plusArgs() {
            // Parameters: initialMjd, initialNanos, plusSeconds, plusNanos, expectedMjd, expectedNanos
            return Stream.of(
                Arguments.of(0, 0, -2 * SECS_PER_DAY, 5, -2, 5),
                Arguments.of(0, 0, -1 * SECS_PER_DAY, 1, -1, 1),
                Arguments.of(0, 0, 0, -1, -1, NANOS_PER_DAY - 1),
                Arguments.of(0, 0, 0, 0, 0, 0),
                Arguments.of(0, 0, 0, 1, 0, 1),
                Arguments.of(0, 0, 1 * SECS_PER_DAY, 1, 1, 1),
                Arguments.of(1, 0, -1 * SECS_PER_DAY, 0, 0, 0),
                Arguments.of(1, 0, 0, -1, 0, NANOS_PER_DAY - 1),
                Arguments.of(1, 0, 0, 1, 1, 1),
                Arguments.of(1, 0, 1 * SECS_PER_DAY, 0, 2, 0)
            );
        }

        @ParameterizedTest
        @MethodSource("plusArgs")
        void plus_shouldCalculateCorrectInstant(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            Duration durationToAdd = Duration.ofSeconds(plusSeconds, plusNanos);
            UtcInstant result = initial.plus(durationToAdd);

            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        static Stream<Arguments> minusArgs() {
            // Parameters: initialMjd, initialNanos, minusSeconds, minusNanos, expectedMjd, expectedNanos
            return Stream.of(
                Arguments.of(0, 0, 2 * SECS_PER_DAY, -5, -2, 5),
                Arguments.of(0, 0, 1 * SECS_PER_DAY, -1, -1, 1),
                Arguments.of(0, 0, 0, 1, -1, NANOS_PER_DAY - 1),
                Arguments.of(0, 0, 0, 0, 0, 0),
                Arguments.of(0, 0, 0, -1, 0, 1),
                Arguments.of(0, 0, -1 * SECS_PER_DAY, -1, 1, 1),
                Arguments.of(1, 0, 1 * SECS_PER_DAY, 0, 0, 0),
                Arguments.of(1, 0, 0, 1, 0, NANOS_PER_DAY - 1),
                Arguments.of(1, 0, 0, -1, 1, 1),
                Arguments.of(1, 0, -1 * SECS_PER_DAY, 0, 2, 0)
            );
        }

        @ParameterizedTest
        @MethodSource("minusArgs")
        void minus_shouldCalculateCorrectInstant(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            Duration durationToSubtract = Duration.ofSeconds(minusSeconds, minusNanos);
            UtcInstant result = initial.minus(durationToSubtract);

            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }
    }

    @Nested
    @DisplayName("formatting and parsing")
    class FormattingAndParsingTest {

        private static final long MJD_1970_01_01 = 40587;
        private static final long MJD_1970_01_02 = 40588;

        static Stream<Arguments> toStringAndParseArgs() {
            return Stream.of(
                Arguments.of("Epoch start", MJD_1970_01_01, 0L, "1970-01-01T00:00:00Z"),
                Arguments.of("One nanosecond after epoch", MJD_1970_01_02, 1L, "1970-01-02T00:00:00.000000001Z"),
                Arguments.of("Microsecond precision", MJD_1970_01_02, 1000L, "1970-01-02T00:00:00.000001Z"),
                Arguments.of("Millisecond precision", MJD_1970_01_02, 1_000_000L, "1970-01-02T00:00:00.001Z"),
                Arguments.of("One second after a leap day", MJD_1973_01_01, 0L, "1973-01-01T00:00:00Z"),
                Arguments.of("During a leap second", MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
                Arguments.of("Just before a leap second", MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z")
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("toStringAndParseArgs")
        void toString_shouldProduceCorrectIsoFormat(String caseName, long mjd, long nod, String expected) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nod);
            assertEquals(expected, instant.toString());
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("toStringAndParseArgs")
        void parse_ofFormattedString_shouldBeReversible(String caseName, long mjd, long nod, String str) {
            UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nod);
            assertEquals(expected, UtcInstant.parse(str));
        }
    }
}