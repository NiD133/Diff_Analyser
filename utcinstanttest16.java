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
 * Tests for {@link UtcInstant}.
 * This class focuses on modification, arithmetic, parsing, and formatting.
 */
class UtcInstantTest {

    // A Modified Julian Date for a non-leap day: 1972-12-30
    private static final long MJD_1972_12_30 = 41681;
    // A Modified Julian Date for a day with a leap second: 1972-12-31
    private static final long MJD_1972_12_31_LEAP = 41682;
    // A Modified Julian Date for a non-leap day: 1973-01-01
    private static final long MJD_1973_01_01 = 41683;
    // A Modified Julian Date for a day with a leap second: 1973-12-31
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 86400L;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    // A day with a leap second has one extra second.
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    @Nested
    @DisplayName("Parse Tests")
    class ParseTests {

        @ParameterizedTest
        @ValueSource(strings = {"", "A", "2012-13-01T00:00:00Z"})
        void parse_withInvalidText_throwsDateTimeException(String invalidText) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidText));
        }

        @Test
        void parse_withNullText_throwsNullPointerException() {
            assertThrows(NullPointerException.class, () -> UtcInstant.parse(null));
        }
    }

    @Nested
    @DisplayName("Modification Tests")
    class ModificationTests {

        // --- withModifiedJulianDay ---

        static Stream<Arguments> data_withModifiedJulianDay_valid() {
            // initialMjd, initialNanoOfDay, newMjd, expectedNanoOfDay
            return Stream.of(
                // Simple cases with various MJD values
                Arguments.of(0L, 12345L, 1L, 12345L),
                Arguments.of(0L, 12345L, -1L, 12345L),
                Arguments.of(7L, 12345L, 2L, 12345L),
                // No-op change on a leap day
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
                // Change from one leap day to another, preserving the leap second nano-of-day
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
            );
        }

        @ParameterizedTest
        @MethodSource("data_withModifiedJulianDay_valid")
        void withModifiedJulianDay_givenValidDay_returnsUpdatedInstant(
            long initialMjd, long initialNanoOfDay, long newMjd, long expectedNanoOfDay) {
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanoOfDay);
            UtcInstant updatedInstant = initialInstant.withModifiedJulianDay(newMjd);

            assertEquals(newMjd, updatedInstant.getModifiedJulianDay());
            assertEquals(expectedNanoOfDay, updatedInstant.getNanoOfDay());
        }

        static Stream<Arguments> data_withModifiedJulianDay_invalid() {
            // initialMjd, initialNanoOfDay, newMjd (which is invalid for the nanoOfDay)
            return Stream.of(
                // Attempt to set a non-leap day while nanoOfDay is a leap second value
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
            );
        }

        @ParameterizedTest
        @MethodSource("data_withModifiedJulianDay_invalid")
        void withModifiedJulianDay_givenInvalidDay_throwsException(
            long initialMjd, long initialNanoOfDay, long invalidNewMjd) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanoOfDay);
            assertThrows(DateTimeException.class, () -> instant.withModifiedJulianDay(invalidNewMjd));
        }

        // --- withNanoOfDay ---

        static Stream<Arguments> data_withNanoOfDay_valid() {
            // initialMjd, initialNanoOfDay, newNanoOfDay
            return Stream.of(
                // Simple cases
                Arguments.of(0L, 12345L, 1L),
                Arguments.of(7L, 12345L, 2L),
                Arguments.of(-99L, 12345L, 3L),
                // Set to max valid nano-of-day on a non-leap day
                Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY - 1),
                // Set to a valid nano-of-day on a leap day
                Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY - 1),
                // Set to the leap second nano-of-day on a leap day
                Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY),
                // Set to the max valid nano-of-day on a leap day
                Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY - 1)
            );
        }

        @ParameterizedTest
        @MethodSource("data_withNanoOfDay_valid")
        void withNanoOfDay_givenValidNanos_returnsUpdatedInstant(
            long mjd, long initialNanoOfDay, long newNanoOfDay) {
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, initialNanoOfDay);
            UtcInstant updatedInstant = initialInstant.withNanoOfDay(newNanoOfDay);

            assertEquals(mjd, updatedInstant.getModifiedJulianDay());
            assertEquals(newNanoOfDay, updatedInstant.getNanoOfDay());
        }

        static Stream<Arguments> data_withNanoOfDay_invalid() {
            // mjd, initialNanoOfDay, invalidNewNanoOfDay
            return Stream.of(
                // Negative nano-of-day is invalid
                Arguments.of(0L, 12345L, -1L),
                // Exceeding max nano-of-day for a non-leap day
                Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY),
                Arguments.of(MJD_1973_01_01, 0L, NANOS_PER_DAY),
                Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_LEAP_DAY - 1),
                // Exceeding max nano-of-day for a leap day
                Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY)
            );
        }

        @ParameterizedTest
        @MethodSource("data_withNanoOfDay_invalid")
        void withNanoOfDay_givenInvalidNanos_throwsException(
            long mjd, long initialNanoOfDay, long invalidNewNanoOfDay) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, initialNanoOfDay);
            assertThrows(DateTimeException.class, () -> instant.withNanoOfDay(invalidNewNanoOfDay));
        }
    }

    @Nested
    @DisplayName("Arithmetic Tests")
    class ArithmeticTests {

        static Stream<Arguments> data_plus() {
            // initialMjd, initialNanoOfDay, secondsToAdd, nanosToAdd, expectedMjd, expectedNanoOfDay
            return Stream.of(
                // Add zero
                Arguments.of(0L, 0L, 0L, 0, 0L, 0L),
                // Add small nano values, no day change
                Arguments.of(0L, 0L, 0L, 1, 0L, 1L),
                // Add nano values causing day boundary cross
                Arguments.of(0L, NANOS_PER_DAY - 1, 0L, 1, 1L, 0L),
                // Subtract nano values causing day boundary cross
                Arguments.of(0L, 0L, 0L, -1, -1L, NANOS_PER_DAY - 1),
                // Add seconds, no day change
                Arguments.of(0L, 0L, 1L, 0, 0L, NANOS_PER_SEC),
                // Add exactly one day in seconds
                Arguments.of(0L, 0L, SECS_PER_DAY, 0, 1L, 0L),
                // Add more than one day
                Arguments.of(0L, 0L, 2 * SECS_PER_DAY, 5, 2L, 5L),
                // Subtract exactly one day in seconds
                Arguments.of(1L, 0L, -SECS_PER_DAY, 0, 0L, 0L)
            );
        }

        @ParameterizedTest
        @MethodSource("data_plus")
        void plus_addsDurationCorrectly(
            long initialMjd, long initialNanoOfDay, long secondsToAdd, int nanosToAdd,
            long expectedMjd, long expectedNanoOfDay) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanoOfDay);
            Duration duration = Duration.ofSeconds(secondsToAdd, nanosToAdd);
            UtcInstant result = initial.plus(duration);

            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanoOfDay, result.getNanoOfDay());
        }

        static Stream<Arguments> data_minus() {
            // initialMjd, initialNanoOfDay, secondsToSubtract, nanosToSubtract, expectedMjd, expectedNanoOfDay
            return Stream.of(
                // Subtract zero
                Arguments.of(0L, 0L, 0L, 0, 0L, 0L),
                // Subtract small nano values, no day change
                Arguments.of(0L, 1L, 0L, 1, 0L, 0L),
                // Subtract nano values causing day boundary cross
                Arguments.of(1L, 0L, 0L, 1, 0L, NANOS_PER_DAY - 1),
                // Add nano values (by subtracting negative) causing day boundary cross
                Arguments.of(0L, NANOS_PER_DAY - 1, 0L, -1, 1L, 0L),
                // Subtract seconds, no day change
                Arguments.of(0L, NANOS_PER_SEC, 1L, 0, 0L, 0L),
                // Subtract exactly one day in seconds
                Arguments.of(1L, 0L, SECS_PER_DAY, 0, 0L, 0L),
                // Subtract more than one day
                Arguments.of(2L, 5L, 2 * SECS_PER_DAY, 5, 0L, 0L),
                // Add exactly one day (by subtracting negative)
                Arguments.of(0L, 0L, -SECS_PER_DAY, 0, 1L, 0L)
            );
        }

        @ParameterizedTest
        @MethodSource("data_minus")
        void minus_subtractsDurationCorrectly(
            long initialMjd, long initialNanoOfDay, long secondsToSubtract, int nanosToSubtract,
            long expectedMjd, long expectedNanoOfDay) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanoOfDay);
            Duration duration = Duration.ofSeconds(secondsToSubtract, nanosToSubtract);
            UtcInstant result = initial.minus(duration);

            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanoOfDay, result.getNanoOfDay());
        }
    }

    @Nested
    @DisplayName("Format and Parse Tests")
    class FormatAndParseTests {

        static Stream<Arguments> data_toString() {
            // mjd, nanoOfDay, expectedString
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
                // Leap second string representation
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
                Arguments.of(MJD_1973_01_01, 0L, "1973-01-01T00:00:00Z")
            );
        }

        @ParameterizedTest
        @MethodSource("data_toString")
        void toString_returnsCorrectlyFormattedString(long mjd, long nanoOfDay, String expectedString) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
            assertEquals(expectedString, instant.toString());
        }

        @ParameterizedTest
        @MethodSource("data_toString")
        void parse_recreatesEquivalentInstantFromToStringOutput(long mjd, long nanoOfDay, String isoString) {
            UtcInstant expectedInstant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
            UtcInstant parsedInstant = UtcInstant.parse(isoString);
            assertEquals(expectedInstant, parsedInstant);
        }
    }
}