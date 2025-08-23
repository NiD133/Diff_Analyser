package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("Tests for UtcInstant")
class UtcInstantTest {

    // A non-leap day, Dec 30, 1972
    private static final long MJD_1972_12_30 = 41681;
    // A leap day, Dec 31, 1972, which had a leap second
    private static final long MJD_1972_12_31_LEAP = 41682;
    // A non-leap day, Jan 01, 1973
    private static final long MJD_1973_01_01 = 41683;
    // A leap day, Dec 31, 1973, which had a leap second
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 86400L;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @DisplayName("parse() with invalid string throws DateTimeException")
    @ParameterizedTest(name = "For string: \"{0}\"")
    @ValueSource(strings = {"", "A", "2012-13-01T00:00:00Z"})
    void parse_withInvalidString_throwsDateTimeException(String invalidString) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withModifiedJulianDay()
    //-----------------------------------------------------------------------
    @DisplayName("withModifiedJulianDay() on valid day returns updated instant")
    @ParameterizedTest(name = "from MJD {0}, nano {1} to new MJD {2}")
    @MethodSource("provideValidMjdChanges")
    void withModifiedJulianDay_onValidDay_returnsUpdatedInstant(
            long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        UtcInstant result = initial.withModifiedJulianDay(newMjd);
        assertEquals(expectedMjd, result.getModifiedJulianDay(), "MJD should be updated");
        assertEquals(expectedNanos, result.getNanoOfDay(), "Nano-of-day should be preserved");
    }

    static Stream<Arguments> provideValidMjdChanges() {
        // Arguments: initialMjd, initialNanos, newMjd, expectedMjd, expectedNanos
        return Stream.of(
            // Simple changes
            Arguments.of(0L, 12345L, 1L, 1L, 12345L),
            Arguments.of(0L, 12345L, -1L, -1L, 12345L),
            Arguments.of(7L, 12345L, 2L, 2L, 12345L),
            // Change from a leap day with a leap second to another leap day
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY),
            // Change to the same day (should be a no-op)
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @DisplayName("withModifiedJulianDay() when nano-of-day becomes invalid throws exception")
    @ParameterizedTest(name = "from MJD {0}, nano {1} to new MJD {2}")
    @MethodSource("provideInvalidMjdChanges")
    void withModifiedJulianDay_whenNanoOfDayBecomesInvalid_throwsException(long initialMjd, long initialNanos, long newMjd) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjd));
    }

    static Stream<Arguments> provideInvalidMjdChanges() {
        // Arguments: initialMjd, initialNanos, newMjd
        // These cases are invalid because the nano-of-day from the initial instant
        // is not valid for the new MJD (e.g., moving a leap second to a non-leap day).
        return Stream.of(
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
        );
    }

    //-----------------------------------------------------------------------
    // withNanoOfDay()
    //-----------------------------------------------------------------------
    @DisplayName("withNanoOfDay() on valid nano value returns updated instant")
    @ParameterizedTest(name = "on MJD {0}, from nano {1} to new nano {2}")
    @MethodSource("provideValidNanoOfDayChanges")
    void withNanoOfDay_onValidNanoOfDay_returnsUpdatedInstant(
            long mjd, long initialNanos, long newNanoOfDay, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
        UtcInstant result = initial.withNanoOfDay(newNanoOfDay);
        assertEquals(expectedMjd, result.getModifiedJulianDay(), "MJD should be preserved");
        assertEquals(expectedNanos, result.getNanoOfDay(), "Nano-of-day should be updated");
    }

    static Stream<Arguments> provideValidNanoOfDayChanges() {
        // Arguments: mjd, initialNanos, newNanoOfDay, expectedMjd, expectedNanos
        return Stream.of(
            // Simple changes on a regular day
            Arguments.of(0L, 12345L, 1L, 0L, 1L),
            Arguments.of(7L, 12345L, 2L, 7L, 2L),
            // Max valid nano on a regular day
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
            // Max valid nano on a leap day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1),
            // Setting the leap second nano on a leap day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @DisplayName("withNanoOfDay() on invalid nano value throws exception")
    @ParameterizedTest(name = "on MJD {0}, setting new nano {1}")
    @MethodSource("provideInvalidNanoOfDayChanges")
    void withNanoOfDay_onInvalidNanoOfDay_throwsException(long mjd, long initialNanos, long newNanoOfDay) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
        assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(newNanoOfDay));
    }

    static Stream<Arguments> provideInvalidNanoOfDayChanges() {
        // Arguments: mjd, initialNanos, newNanoOfDay
        return Stream.of(
            // Negative nano-of-day is always invalid
            Arguments.of(0L, 12345L, -1L),
            // Setting a leap-second nano on a non-leap day
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY),
            Arguments.of(MJD_1973_01_01, 0L, NANOS_PER_DAY),
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_LEAP_DAY - 1),
            // Setting nano greater than max for a leap day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY)
        );
    }

    //-----------------------------------------------------------------------
    // plus()
    //-----------------------------------------------------------------------
    @DisplayName("plus() correctly adds a duration")
    @ParameterizedTest(name = "MJD {0}, nano {1} + {2}s, {3}ns -> MJD {4}, nano {5}")
    @MethodSource("providePlusDurations")
    void plus_addsDurationCorrectly(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration duration = Duration.ofSeconds(plusSeconds, plusNanos);
        UtcInstant result = initial.plus(duration);
        assertEquals(expectedMjd, result.getModifiedJulianDay(), "Resulting MJD is incorrect");
        assertEquals(expectedNanos, result.getNanoOfDay(), "Resulting nano-of-day is incorrect");
    }

    static Stream<Arguments> providePlusDurations() {
        // Arguments: mjd, nanos, plusSeconds, plusNanos, expectedMjd, expectedNanos
        return Stream.of(
            // --- Add zero ---
            Arguments.of(0L, 0L, 0L, 0, 0L, 0L),
            // --- Add small amounts, no day change ---
            Arguments.of(0L, 0L, 0L, 1, 0L, 1L),
            Arguments.of(0L, 0L, 1L, 0, 0L, NANOS_PER_SEC),
            Arguments.of(0L, 0L, 3L, 333, 0L, 3 * NANOS_PER_SEC + 333),
            // --- Cross day boundary (forward) ---
            Arguments.of(0L, 0L, SECS_PER_DAY, 0, 1L, 0L),
            Arguments.of(0L, 0L, SECS_PER_DAY, 1, 1L, 1L),
            // --- Cross day boundary (backward, using negative duration) ---
            Arguments.of(0L, 0L, 0L, -1, -1L, NANOS_PER_DAY - 1),
            Arguments.of(1L, 0L, -SECS_PER_DAY, 0, 0L, 0L),
            // --- Add multiple days ---
            Arguments.of(0L, 0L, 2 * SECS_PER_DAY, 5, 2L, 5L),
            Arguments.of(1L, 0L, 2 * SECS_PER_DAY, 5, 3L, 5L),
            // --- Subtract multiple days ---
            Arguments.of(0L, 0L, -2 * SECS_PER_DAY, 5, -2L, 5L),
            Arguments.of(1L, 0L, -2 * SECS_PER_DAY, 5, -1L, 5L)
        );
    }

    //-----------------------------------------------------------------------
    // minus()
    //-----------------------------------------------------------------------
    @DisplayName("minus() correctly subtracts a duration")
    @ParameterizedTest(name = "MJD {0}, nano {1} - {2}s, {3}ns -> MJD {4}, nano {5}")
    @MethodSource("provideMinusDurations")
    void minus_subtractsDurationCorrectly(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration duration = Duration.ofSeconds(minusSeconds, minusNanos);
        UtcInstant result = initial.minus(duration);
        assertEquals(expectedMjd, result.getModifiedJulianDay(), "Resulting MJD is incorrect");
        assertEquals(expectedNanos, result.getNanoOfDay(), "Resulting nano-of-day is incorrect");
    }

    static Stream<Arguments> provideMinusDurations() {
        // Arguments: mjd, nanos, minusSeconds, minusNanos, expectedMjd, expectedNanos
        return Stream.of(
            // --- Subtract zero ---
            Arguments.of(0L, 0L, 0L, 0, 0L, 0L),
            // --- Subtract small amounts, no day change ---
            Arguments.of(0L, 10L, 0L, 2, 0L, 8L),
            Arguments.of(0L, NANOS_PER_SEC, 1L, 0, 0L, 0L),
            // --- Cross day boundary (backward) ---
            Arguments.of(0L, 0L, 0L, 1, -1L, NANOS_PER_DAY - 1),
            Arguments.of(1L, 0L, SECS_PER_DAY, 0, 0L, 0L),
            // --- Cross day boundary (forward, using negative duration) ---
            Arguments.of(0L, 0L, 0L, -1, 0L, 1L),
            Arguments.of(0L, 0L, -SECS_PER_DAY, 0, 1L, 0L),
            // --- Subtract multiple days ---
            Arguments.of(0L, 0L, 2 * SECS_PER_DAY, 5, -3L, NANOS_PER_DAY - 5),
            Arguments.of(1L, 0L, 2 * SECS_PER_DAY, 5, -2L, NANOS_PER_DAY - 5),
            // --- Add multiple days (by subtracting negative duration) ---
            Arguments.of(0L, 0L, -2 * SECS_PER_DAY, -5, 2L, 5L),
            Arguments.of(1L, 0L, -2 * SECS_PER_DAY, -5, 3L, 5L)
        );
    }

    //-----------------------------------------------------------------------
    // toString() and parse()
    //-----------------------------------------------------------------------
    @DisplayName("toString() formats as an ISO string")
    @ParameterizedTest(name = "MJD {0}, nano {1} -> \"{2}\"")
    @MethodSource("provideToStringCases")
    void toString_formatsAsIsoString(long mjd, long nod, String expected) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nod);
        assertEquals(expected, instant.toString());
    }

    @DisplayName("parse() correctly reads a string formatted by toString()")
    @ParameterizedTest(name = "Parse \"{2}\"")
    @MethodSource("provideToStringCases")
    void parse_recreatesInstantFromToStringOutput(long mjd, long nod, String str) {
        UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nod);
        UtcInstant parsed = UtcInstant.parse(str);
        assertEquals(expected, parsed);
    }

    static Stream<Arguments> provideToStringCases() {
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
            Arguments.of(40620L, 60 * NANOS_PER_SEC, "1970-02-03T00:01:00Z"),
            Arguments.of(40621L, 60 * 60 * NANOS_PER_SEC, "1970-02-04T01:00:00Z"),
            // Leap second cases
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
            Arguments.of(MJD_1973_01_01, 0L, "1973-01-01T00:00:00Z")
        );
    }

    //-----------------------------------------------------------------------
    // compareTo()
    //-----------------------------------------------------------------------
    @Test
    @DisplayName("compareTo() with an incompatible type throws ClassCastException")
    void compareTo_withIncompatibleType_throwsClassCastException() {
        // This test verifies the behavior of the compiler-generated bridge method for Comparable.
        // It ensures that comparing a UtcInstant to an unrelated type throws an exception.
        @SuppressWarnings({"rawtypes", "unchecked"})
        Comparable c = UtcInstant.ofModifiedJulianDay(0L, 2);
        assertThrows(ClassCastException.class, () -> c.compareTo(new Object()));
    }
}