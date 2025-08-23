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

/**
 * Tests for {@link UtcInstant}.
 * This selection of tests covers parsing, modification, and arithmetic operations.
 */
@DisplayName("UtcInstant Tests")
class UtcInstantTestTest19 {

    // A Modified Julian Day known to have a leap second.
    private static final long MJD_1972_12_31_LEAP = 41682;
    // A normal day preceding the leap day.
    private static final long MJD_1972_12_30 = 41681;
    // The day after the leap day.
    private static final long MJD_1973_01_01 = 41683;
    // Another leap day, one year later.
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 86400L;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    // Test for UtcInstant.parse()
    //-----------------------------------------------------------------------

    @DisplayName("parse() with invalid text throws DateTimeException")
    @ParameterizedTest(name = "Parsing \"{0}\"")
    @ValueSource(strings = {"", "A", "2012-13-01T00:00:00Z"}) // Empty, malformed, invalid month
    void parse_withInvalidText_throwsException(String invalidText) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidText));
    }

    //-----------------------------------------------------------------------
    // Tests for withModifiedJulianDay()
    //-----------------------------------------------------------------------

    static Stream<Arguments> provider_withModifiedJulianDay_success() {
        return Stream.of(
            // Simple cases, nanoOfDay is unaffected
            Arguments.of(0L, 12345L, 1L, 1L, 12345L),
            Arguments.of(0L, 12345L, -1L, -1L, 12345L),
            // Leap day to leap day, nanoOfDay is valid for both
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY),
            // Setting to the same day should be a no-op
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withModifiedJulianDay_success")
    void withModifiedJulianDay_givenValidDay_returnsUpdatedInstant(long initialMjd, long nanoOfDay, long newMjd, long expectedMjd, long expectedNanoOfDay) {
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, nanoOfDay);
        UtcInstant updatedInstant = initialInstant.withModifiedJulianDay(newMjd);
        assertEquals(expectedMjd, updatedInstant.getModifiedJulianDay());
        assertEquals(expectedNanoOfDay, updatedInstant.getNanoOfDay());
    }

    static Stream<Arguments> provider_withModifiedJulianDay_throwsException() {
        return Stream.of(
            // From a leap day with 60th second, to a normal day (nanoOfDay becomes invalid)
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withModifiedJulianDay_throwsException")
    void withModifiedJulianDay_whenNanoOfDayIsInvalidForNewDay_throwsException(long initialMjd, long nanoOfDay, long newMjd) {
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, nanoOfDay);
        assertThrows(DateTimeException.class, () -> initialInstant.withModifiedJulianDay(newMjd));
    }

    //-----------------------------------------------------------------------
    // Tests for withNanoOfDay()
    //-----------------------------------------------------------------------

    static Stream<Arguments> provider_withNanoOfDay_success() {
        return Stream.of(
            // Simple cases
            Arguments.of(0L, 12345L, 1L, 0L, 1L),
            // Set to max valid nano on a normal day
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
            // Set to max valid nano on a leap day (which is one second longer)
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1),
            // Set to the 60th second on a leap day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withNanoOfDay_success")
    void withNanoOfDay_givenValidNanoOfDay_returnsUpdatedInstant(long mjd, long initialNanoOfDay, long newNanoOfDay, long expectedMjd, long expectedNanoOfDay) {
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, initialNanoOfDay);
        UtcInstant updatedInstant = initialInstant.withNanoOfDay(newNanoOfDay);
        assertEquals(expectedMjd, updatedInstant.getModifiedJulianDay());
        assertEquals(expectedNanoOfDay, updatedInstant.getNanoOfDay());
    }

    static Stream<Arguments> provider_withNanoOfDay_throwsException() {
        return Stream.of(
            // Negative nano-of-day is always invalid
            Arguments.of(0L, 12345L, -1L),
            // Attempt to set nano-of-day to the leap second value on a normal day
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY),
            // Attempt to set nano-of-day beyond the leap second on a leap day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withNanoOfDay_throwsException")
    void withNanoOfDay_givenInvalidNanoOfDay_throwsException(long mjd, long initialNanoOfDay, long newNanoOfDay) {
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, initialNanoOfDay);
        assertThrows(DateTimeException.class, () -> initialInstant.withNanoOfDay(newNanoOfDay));
    }

    //-----------------------------------------------------------------------
    // Tests for plus(Duration)
    //-----------------------------------------------------------------------

    static Stream<Arguments> provider_plus() {
        return Stream.of(
            // --- Basic cases, no rollovers ---
            Arguments.of(0, 0, 0, 0, 0, 0), // Add zero
            Arguments.of(0, 0, 0, 1, 0, 1), // Add one nano
            Arguments.of(0, 0, 1, 0, 0, NANOS_PER_SEC), // Add one second
            Arguments.of(1, 0, 2, 333, 1, 2 * NANOS_PER_SEC + 333), // Add seconds and nanos

            // --- Nano of day rollovers ---
            Arguments.of(0, 0, 0, -1, -1, NANOS_PER_DAY - 1), // Subtract one nano, rolls day back
            Arguments.of(1, 0, 0, -2, 0, NANOS_PER_DAY - 2), // Subtract two nanos, rolls day back

            // --- Day rollovers ---
            Arguments.of(0, 0, SECS_PER_DAY, 0, 1, 0), // Add one full day in seconds
            Arguments.of(0, 0, 2 * SECS_PER_DAY, 5, 2, 5), // Add two full days and some nanos
            Arguments.of(1, 0, -SECS_PER_DAY, 0, 0, 0) // Subtract one full day
        );
    }

    @ParameterizedTest(name = "[{index}] MJD {0}, NOD {1} + ({2}s, {3}ns) -> MJD {4}, NOD {5}")
    @MethodSource("provider_plus")
    void plus_addsDuration_returnsCorrectUtcInstant(
        long initialMjd, long initialNanos,
        long secondsToAdd, int nanosToAdd,
        long expectedMjd, long expectedNanos) {

        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        Duration durationToAdd = Duration.ofSeconds(secondsToAdd, nanosToAdd);
        UtcInstant result = initialInstant.plus(durationToAdd);

        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    // Tests for minus(Duration)
    //-----------------------------------------------------------------------

    static Stream<Arguments> provider_minus() {
        // Note: minus(Duration) is equivalent to plus(Duration.negated())
        return Stream.of(
            // --- Basic cases, no rollovers ---
            Arguments.of(0, 0, 0, 0, 0, 0), // Subtract zero
            Arguments.of(0, 2, 0, 1, 0, 1), // Subtract one nano
            Arguments.of(0, NANOS_PER_SEC, 1, 0, 0, 0), // Subtract one second

            // --- Subtracting a negative duration (i.e., addition) ---
            Arguments.of(0, 0, 0, -1, 0, 1), // Subtract -1 nano
            Arguments.of(0, 0, -1, 0, 0, NANOS_PER_SEC), // Subtract -1 second

            // --- Day rollovers ---
            Arguments.of(1, 0, SECS_PER_DAY, 0, 0, 0), // Subtract one full day
            Arguments.of(2, 5, 2 * SECS_PER_DAY, 5, 0, 0), // Subtract two full days
            Arguments.of(0, 0, -SECS_PER_DAY, 0, 1, 0) // Subtract negative one day (add)
        );
    }

    @ParameterizedTest(name = "[{index}] MJD {0}, NOD {1} - ({2}s, {3}ns) -> MJD {4}, NOD {5}")
    @MethodSource("provider_minus")
    void minus_subtractsDuration_returnsCorrectUtcInstant(
        long initialMjd, long initialNanos,
        long secondsToSubtract, int nanosToSubtract,
        long expectedMjd, long expectedNanos) {

        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        Duration durationToSubtract = Duration.ofSeconds(secondsToSubtract, nanosToSubtract);
        UtcInstant result = initialInstant.minus(durationToSubtract);

        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    @Test
    void minus_fromMinMjd_throwsArithmeticException() {
        UtcInstant minInstant = UtcInstant.ofModifiedJulianDay(Long.MIN_VALUE, 0);
        Duration oneNano = Duration.ofNanos(1);
        assertThrows(ArithmeticException.class, () -> minInstant.minus(oneNano));
    }

    //-----------------------------------------------------------------------
    // Tests for toString() and parse() round-trip
    //-----------------------------------------------------------------------

    static Stream<Arguments> provider_toStringAndParse() {
        return Stream.of(
            // Epoch
            Arguments.of(40587, 0L, "1970-01-01T00:00:00Z"),
            // With nanoseconds
            Arguments.of(40588, 1L, "1970-01-02T00:00:00.000000001Z"),
            // Nanosecond formatting variations
            Arguments.of(40588, 999_000L, "1970-01-02T00:00:00.000999Z"),
            Arguments.of(40588, 1_000_000L, "1970-01-02T00:00:00.001Z"),
            // End of a second
            Arguments.of(40618, 999_999_999L, "1970-02-01T00:00:00.999999999Z"),
            // Rollover to next second, minute, and hour
            Arguments.of(40619, NANOS_PER_SEC, "1970-02-02T00:00:01Z"),
            Arguments.of(40620, 60 * NANOS_PER_SEC, "1970-02-03T00:01:00Z"),
            Arguments.of(40621, 60 * 60 * NANOS_PER_SEC, "1970-02-04T01:00:00Z"),
            // --- Leap Second cases ---
            // Just before a leap second
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
            // The leap second itself (23:59:60)
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
            // The second after the leap second, on the next day
            Arguments.of(MJD_1973_01_01, 0L, "1973-01-01T00:00:00Z")
        );
    }

    @ParameterizedTest(name = "[{index}] toString of {2}")
    @MethodSource("provider_toStringAndParse")
    void toString_returnsCorrectIso8601Format(long mjd, long nanoOfDay, String expectedString) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        assertEquals(expectedString, instant.toString());
    }

    @ParameterizedTest(name = "[{index}] parse of {2}")
    @MethodSource("provider_toStringAndParse")
    void parse_canParseStringFromToString(long mjd, long nanoOfDay, String isoString) {
        UtcInstant expectedInstant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        UtcInstant parsedInstant = UtcInstant.parse(isoString);
        assertEquals(expectedInstant, parsedInstant);
    }
}