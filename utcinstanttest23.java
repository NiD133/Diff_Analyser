package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertAll;
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
 * Tests for modification, arithmetic, and formatting of {@link UtcInstant}.
 */
class UtcInstantModificationAndArithmeticTest {

    // A non-leap day in 1972
    private static final long MJD_1972_12_30 = 41681;
    // A leap day in 1972, which had 86401 seconds.
    private static final long MJD_1972_12_31_LEAP = 41682;
    // The day after the 1972 leap day.
    private static final long MJD_1973_01_01 = 41683;
    // A leap day in 1973.
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 86400L;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    // Nanos in a day that has a positive leap second.
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    @ParameterizedTest
    @ValueSource(strings = {"", "A", "2012-13-01T00:00:00Z"})
    void parse_withInvalidFormat_throwsDateTimeException(String invalidText) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidText));
    }

    @Nested
    @DisplayName("withModifiedJulianDay()")
    class WithModifiedJulianDay {

        static Stream<Arguments> provider_validChanges() {
            return Stream.of(
                Arguments.of("Simple forward change", UtcInstant.ofModifiedJulianDay(0L, 12345L), 1L, UtcInstant.ofModifiedJulianDay(1L, 12345L)),
                Arguments.of("Simple backward change", UtcInstant.ofModifiedJulianDay(0L, 12345L), -1L, UtcInstant.ofModifiedJulianDay(-1L, 12345L)),
                Arguments.of("Change to a leap day with valid nano-of-day", UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY), MJD_1972_12_31_LEAP, UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY)),
                Arguments.of("Change from one leap day to another", UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY), MJD_1973_12_31_LEAP, UtcInstant.ofModifiedJulianDay(MJD_1973_12_31_LEAP, NANOS_PER_DAY))
            );
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("provider_validChanges")
        void givenValidNewDay_returnsUpdatedInstant(String description, UtcInstant initial, long newMjd, UtcInstant expected) {
            UtcInstant result = initial.withModifiedJulianDay(newMjd);
            assertEquals(expected, result);
        }

        static Stream<Arguments> provider_invalidChanges() {
            // Create an instant during the leap second of a leap day.
            UtcInstant instantInLeapSecond = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY);
            return Stream.of(
                // This nano-of-day is invalid for the target non-leap days.
                Arguments.of("From leap day to non-leap day before", instantInLeapSecond, MJD_1972_12_30),
                Arguments.of("From leap day to non-leap day after", instantInLeapSecond, MJD_1973_01_01)
            );
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("provider_invalidChanges")
        void givenInvalidNewDay_throwsDateTimeException(String description, UtcInstant initial, long newMjd) {
            assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjd));
        }
    }

    @Nested
    @DisplayName("withNanoOfDay()")
    class WithNanoOfDay {

        static Stream<Arguments> provider_validChanges() {
            return Stream.of(
                Arguments.of("Simple change", 0L, 12345L, 1L, 0L, 1L),
                Arguments.of("On non-leap day, up to max nanos", MJD_1972_12_30, 0L, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
                Arguments.of("On leap day, up to standard max nanos", MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1),
                Arguments.of("On leap day, to leap second start", MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
                Arguments.of("On leap day, up to leap day max nanos", MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1)
            );
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("provider_validChanges")
        void givenValidNanoOfDay_returnsUpdatedInstant(String desc, long mjd, long nanos, long newNanoOfDay, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            UtcInstant result = initial.withNanoOfDay(newNanoOfDay);
            assertAll(
                () -> assertEquals(expectedMjd, result.getModifiedJulianDay(), "MJD should match"),
                () -> assertEquals(expectedNanos, result.getNanoOfDay(), "Nano-of-day should match")
            );
        }

        static Stream<Arguments> provider_invalidChanges() {
            return Stream.of(
                Arguments.of("Negative nano-of-day", 0L, 12345L, -1L),
                Arguments.of("On non-leap day, nano-of-day too large", MJD_1972_12_30, 0L, NANOS_PER_DAY),
                Arguments.of("On non-leap day, nano-of-day for leap day", MJD_1972_12_30, 0L, NANOS_PER_LEAP_DAY - 1),
                Arguments.of("On leap day, nano-of-day too large", MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY)
            );
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("provider_invalidChanges")
        void givenInvalidNanoOfDay_throwsDateTimeException(String desc, long mjd, long nanos, long newNanoOfDay) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(newNanoOfDay));
        }
    }

    static Stream<Arguments> provider_plus() {
        return Stream.of(
            // --- Add zero ---
            Arguments.of(1L, 0L, Duration.ZERO, 1L, 0L),
            // --- Add positive duration ---
            Arguments.of(0L, 0L, Duration.ofNanos(1), 0L, 1L),
            Arguments.of(0L, 0L, Duration.ofSeconds(1), 0L, NANOS_PER_SEC),
            Arguments.of(0L, 0L, Duration.ofSeconds(3, 333_333_333), 0L, 3 * NANOS_PER_SEC + 333_333_333),
            Arguments.of(0L, 0L, Duration.ofSeconds(SECS_PER_DAY), 1L, 0L),
            Arguments.of(1L, 0L, Duration.ofSeconds(SECS_PER_DAY, 1), 2L, 1L),
            // --- Add negative duration ---
            Arguments.of(0L, 0L, Duration.ofNanos(-1), -1L, NANOS_PER_DAY - 1),
            Arguments.of(0L, 0L, Duration.ofSeconds(-1), -1L, NANOS_PER_DAY - NANOS_PER_SEC),
            Arguments.of(1L, 0L, Duration.ofSeconds(-SECS_PER_DAY), 0L, 0L),
            Arguments.of(0L, 0L, Duration.ofSeconds(-2 * SECS_PER_DAY, 5), -2L, 5L)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_plus")
    void plus_withVariousDurations_calculatesCorrectly(long mjd, long nanos, Duration durationToAdd, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.plus(durationToAdd);
        assertAll(
            () -> assertEquals(expectedMjd, result.getModifiedJulianDay(), "MJD"),
            () -> assertEquals(expectedNanos, result.getNanoOfDay(), "Nano of day")
        );
    }

    static Stream<Arguments> provider_minus() {
        return Stream.of(
            // --- Subtract zero ---
            Arguments.of(1L, 0L, Duration.ZERO, 1L, 0L),
            // --- Subtract positive duration ---
            Arguments.of(0L, 0L, Duration.ofNanos(1), -1L, NANOS_PER_DAY - 1),
            Arguments.of(0L, 0L, Duration.ofSeconds(1), -1L, NANOS_PER_DAY - NANOS_PER_SEC),
            Arguments.of(1L, 0L, Duration.ofSeconds(SECS_PER_DAY), 0L, 0L),
            Arguments.of(0L, 0L, Duration.ofSeconds(2 * SECS_PER_DAY, -5), -2L, 5L),
            // --- Subtract negative duration ---
            Arguments.of(0L, 0L, Duration.ofNanos(-1), 0L, 1L),
            Arguments.of(0L, 0L, Duration.ofSeconds(-1), 0L, NANOS_PER_SEC),
            Arguments.of(0L, 0L, Duration.ofSeconds(-SECS_PER_DAY), 1L, 0L),
            Arguments.of(1L, 0L, Duration.ofSeconds(-SECS_PER_DAY, -1), 2L, 1L)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_minus")
    void minus_withVariousDurations_calculatesCorrectly(long mjd, long nanos, Duration durationToSubtract, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.minus(durationToSubtract);
        assertAll(
            () -> assertEquals(expectedMjd, result.getModifiedJulianDay(), "MJD"),
            () -> assertEquals(expectedNanos, result.getNanoOfDay(), "Nano of day")
        );
    }

    static Stream<Arguments> provider_toString() {
        return Stream.of(
            Arguments.of(40587, 0, "1970-01-01T00:00:00Z"),
            Arguments.of(40588, 1, "1970-01-02T00:00:00.000000001Z"),
            Arguments.of(40588, 999, "1970-01-02T00:00:00.000000999Z"),
            Arguments.of(40588, 1000, "1970-01-02T00:00:00.000001Z"),
            Arguments.of(40588, 999000, "1970-01-02T00:00:00.000999Z"),
            Arguments.of(40588, 1000000, "1970-01-02T00:00:00.001Z"),
            Arguments.of(40618, 999999999, "1970-02-01T00:00:00.999999999Z"),
            Arguments.of(40619, 1000000000, "1970-02-02T00:00:01Z"),
            Arguments.of(40620, 60L * 1000000000L, "1970-02-03T00:01:00Z"),
            Arguments.of(40621, 60L * 60L * 1000000000L, "1970-02-04T01:00:00Z"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
            Arguments.of(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z")
        );
    }

    @ParameterizedTest
    @MethodSource("provider_toString")
    void toString_returnsIso8601FormattedString(long mjd, long nod, String expected) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nod);
        assertEquals(expected, instant.toString());
    }

    @ParameterizedTest
    @MethodSource("provider_toString")
    void parse_canParseStringFromToString(long mjd, long nod, String str) {
        UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nod);
        assertEquals(expected, UtcInstant.parse(str));
    }

    @Test
    void durationUntil_acrossLeapDay_isCorrect() {
        // The day after the 1972 leap day
        UtcInstant instantAfterLeap = UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, 0);
        // The start of the 1972 leap day
        UtcInstant instantOnLeapDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);

        // The duration from the day after the leap day to the leap day itself.
        // This should be negative and account for the extra leap second.
        Duration duration = instantAfterLeap.durationUntil(instantOnLeapDay);

        assertAll(
            () -> assertEquals(-86401, duration.getSeconds(), "Duration in seconds should include leap second"),
            () -> assertEquals(0, duration.getNano(), "Nano part of duration should be zero")
        );
    }
}