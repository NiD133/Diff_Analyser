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

@DisplayName("UtcInstant")
public class UtcInstantTest {

    // A Modified Julian Day for a non-leap day (Dec 30, 1972)
    private static final long MJD_1972_12_30 = 41681;
    // A Modified Julian Day for a leap day (Dec 31, 1972)
    private static final long MJD_1972_12_31_LEAP = 41682;
    // A Modified Julian Day for a non-leap day (Jan 01, 1973)
    private static final long MJD_1973_01_01 = 41683;
    // A Modified Julian Day for a leap day (Dec 31, 1973)
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    // A day with a leap second has one extra second in nanos
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    @Nested
    @DisplayName("parse()")
    class Parse {
        @ParameterizedTest(name = "for input ''{0}'', throws DateTimeException")
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#provideInvalidParseStrings")
        void parse_withInvalidFormat_throwsDateTimeException(String invalidString) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
        }
    }

    public static Stream<String> provideInvalidParseStrings() {
        return Stream.of(
            "",                      // empty string
            "A",                     // not a date
            "2012-13-01T00:00:00Z"   // invalid month
        );
    }

    @Nested
    @DisplayName("withModifiedJulianDay()")
    class WithModifiedJulianDay {
        @ParameterizedTest(name = "from MJD {0}, sets new MJD to {2}, expected MJD {3}")
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#provideValidModifiedJulianDayCases")
        void withModifiedJulianDay_replacesDayCorrectly(
                long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            UtcInstant result = initialInstant.withModifiedJulianDay(newMjd);
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        @ParameterizedTest(name = "from MJD {0} with nanos {1}, setting new MJD to {2} throws exception")
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#provideInvalidModifiedJulianDayCases")
        void withModifiedJulianDay_forInvalidResultingNanoOfDay_throwsException(
                long initialMjd, long initialNanos, long newMjd) {
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            assertThrows(DateTimeException.class, () -> initialInstant.withModifiedJulianDay(newMjd));
        }
    }

    public static Stream<Arguments> provideValidModifiedJulianDayCases() {
        // Arguments: initialMjd, initialNanos, newMjd, expectedMjd, expectedNanos
        return Stream.of(
            // Simple cases where nanoOfDay is small and always valid
            Arguments.of(0L, 12345L, 1L, 1L, 12345L),
            Arguments.of(0L, 12345L, -1L, -1L, 12345L),
            // Cases where nanoOfDay is valid for both the old and new day types (leap/non-leap)
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    public static Stream<Arguments> provideInvalidModifiedJulianDayCases() {
        // Arguments: initialMjd, initialNanos, newMjd
        // These fail because the initial nanoOfDay is valid only for a leap day,
        // but the new day is not a leap day.
        return Stream.of(
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
        );
    }

    @Nested
    @DisplayName("withNanoOfDay()")
    class WithNanoOfDay {
        @ParameterizedTest(name = "on MJD {0}, sets new nano-of-day to {2}, expected {4}")
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#provideValidNanoOfDayCases")
        void withNanoOfDay_replacesNanosCorrectly(
                long mjd, long initialNanos, long newNanoOfDay, long expectedMjd, long expectedNanos) {
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
            UtcInstant result = initialInstant.withNanoOfDay(newNanoOfDay);
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        @ParameterizedTest(name = "on MJD {0}, setting nano-of-day to {2} throws exception")
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#provideInvalidNanoOfDayCases")
        void withNanoOfDay_forInvalidNanoOfDay_throwsException(long mjd, long initialNanos, long newNanoOfDay) {
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
            assertThrows(DateTimeException.class, () -> initialInstant.withNanoOfDay(newNanoOfDay));
        }
    }

    public static Stream<Arguments> provideValidNanoOfDayCases() {
        // Arguments: mjd, initialNanos, newNanoOfDay, expectedMjd, expectedNanos
        return Stream.of(
            // Simple cases
            Arguments.of(0L, 12345L, 1L, 0L, 1L),
            Arguments.of(7L, 12345L, 2L, 7L, 2L),
            // Edge cases for non-leap days
            Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
            // Edge cases for leap days
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1)
        );
    }

    public static Stream<Arguments> provideInvalidNanoOfDayCases() {
        // Arguments: mjd, initialNanos, newNanoOfDay
        return Stream.of(
            // Negative nanoOfDay is always invalid
            Arguments.of(0L, 12345L, -1L),
            // Setting nanoOfDay beyond the valid range for a non-leap day
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY),
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_LEAP_DAY - 1),
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_LEAP_DAY),
            // Setting nanoOfDay beyond the valid range for a leap day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY)
        );
    }

    @Nested
    @DisplayName("plus()")
    class Plus {
        @ParameterizedTest(name = "from MJD {0} nanos {1}, adding {2}s {3}ns results in MJD {4} nanos {5}")
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#plus_testCases")
        void plus_addsDurationAndHandlesRollover(
                long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            UtcInstant result = initial.plus(Duration.ofSeconds(plusSeconds, plusNanos));
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }
    }

    public static Stream<Arguments> plus_testCases() {
        // Arguments: initialMjd, initialNanos, secondsToAdd, nanosToAdd, expectedMjd, expectedNanos
        return Stream.of(
            // --- Additions without day rollover ---
            Arguments.of(0L, 0L, 0L, 1, 0L, 1L),
            Arguments.of(0L, 0L, 1L, 0, 0L, NANOS_PER_SEC),
            // --- Additions causing day rollover ---
            Arguments.of(0L, 0L, SECS_PER_DAY, 0, 1L, 0L),
            Arguments.of(0L, 0L, SECS_PER_DAY, 1, 1L, 1L),
            // --- Subtractions (negative duration) without day rollover ---
            Arguments.of(1L, 1L, 0L, -1, 1L, 0L),
            // --- Subtractions (negative duration) causing day rollover ---
            Arguments.of(0L, 0L, 0L, -1, -1L, NANOS_PER_DAY - 1),
            Arguments.of(0L, 0L, 0L, -2, -1L, NANOS_PER_DAY - 2),
            Arguments.of(0L, 0L, -1L, 0, -1L, 0L),
            Arguments.of(1L, 0L, -SECS_PER_DAY, 0, 0L, 0L)
        );
    }

    @Nested
    @DisplayName("minus()")
    class Minus {
        @ParameterizedTest(name = "from MJD {0} nanos {1}, subtracting {2}s {3}ns results in MJD {4} nanos {5}")
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#minus_testCases")
        void minus_subtractsDurationAndHandlesRollover(
                long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            UtcInstant result = initial.minus(Duration.ofSeconds(minusSeconds, minusNanos));
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }
    }

    public static Stream<Arguments> minus_testCases() {
        // Arguments: initialMjd, initialNanos, secondsToSubtract, nanosToSubtract, expectedMjd, expectedNanos
        return Stream.of(
            // --- Subtractions without day rollover ---
            Arguments.of(0L, 1L, 0L, 1, 0L, 0L),
            Arguments.of(0L, NANOS_PER_SEC, 1L, 0, 0L, 0L),
            // --- Subtractions causing day rollover ---
            Arguments.of(0L, 0L, 0L, 1, -1L, NANOS_PER_DAY - 1),
            Arguments.of(1L, 0L, SECS_PER_DAY, 0, 0L, 0L),
            // --- Additions (negative duration) without day rollover ---
            Arguments.of(0L, 0L, 0L, -1, 0L, 1L),
            // --- Additions (negative duration) causing day rollover ---
            Arguments.of(0L, 0L, -SECS_PER_DAY, 0, 1L, 0L),
            Arguments.of(-1L, 0L, -SECS_PER_DAY, 0, 0L, 0L)
        );
    }

    @Nested
    @DisplayName("toString() and parse()")
    class ToStringAndParse {
        @ParameterizedTest(name = "for MJD {0} nanos {1}, toString() is {2}")
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#toString_testCases")
        void toString_returnsIso8601Format(long mjd, long nod, String expected) {
            assertEquals(expected, UtcInstant.ofModifiedJulianDay(mjd, nod).toString());
        }

        @ParameterizedTest(name = "for string {2}, parse() returns MJD {0} nanos {1}")
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#toString_testCases")
        void parse_reversesToString(long mjd, long nod, String str) {
            assertEquals(UtcInstant.ofModifiedJulianDay(mjd, nod), UtcInstant.parse(str));
        }
    }

    public static Stream<Arguments> toString_testCases() {
        // Arguments: mjd, nanoOfDay, expectedString
        return Stream.of(
            Arguments.of(40587L, 0L, "1970-01-01T00:00:00Z"),
            Arguments.of(40588L, 1L, "1970-01-02T00:00:00.000000001Z"),
            Arguments.of(40588L, 999L, "1970-01-02T00:00:00.000000999Z"),
            Arguments.of(40588L, 1000L, "1970-01-02T00:00:00.000001Z"),
            Arguments.of(40588L, 999000L, "1970-01-02T00:00:00.000999Z"),
            Arguments.of(40588L, 1000000L, "1970-01-02T00:00:00.001Z"),
            Arguments.of(40618L, 999999999L, "1970-02-01T00:00:00.999999999Z"),
            Arguments.of(40619L, NANOS_PER_SEC, "1970-02-02T00:00:01Z"),
            Arguments.of(40620L, 60L * NANOS_PER_SEC, "1970-02-03T00:01:00Z"),
            Arguments.of(40621L, 60L * 60L * NANOS_PER_SEC, "1970-02-04T01:00:00Z"),
            // Leap second cases
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
            Arguments.of(MJD_1973_01_01, 0L, "1973-01-01T00:00:00Z")
        );
    }

    @Test
    @DisplayName("durationUntil() across a leap day returns correct duration")
    void durationUntil_acrossLeapDay_isCorrect() {
        // Setup: An instant at the start of a leap day, and an instant at the start of the next day.
        UtcInstant startOfLeapDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);
        UtcInstant startOfNextDay = UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, 0);

        // Act: Calculate the duration between them.
        Duration duration = startOfLeapDay.durationUntil(startOfNextDay);

        // Assert: The duration should be 86401 seconds (a normal day + 1 leap second).
        assertEquals(86401, duration.getSeconds());
        assertEquals(0, duration.getNano());
    }
}