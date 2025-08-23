package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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

@DisplayName("UtcInstant Modification and Arithmetic")
class UtcInstantModificationAndArithmeticTest {

    private static final long MJD_1972_12_30 = 41681;
    private static final long MJD_1972_12_31_LEAP = 41682; // A day with a leap second
    private static final long MJD_1973_01_01 = 41683;
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365; // Another day with a leap second

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    @Nested
    @DisplayName("Factory and Parsing")
    class FactoryAndParsing {

        @Test
        void ofModifiedJulianDay_createsInstanceWithCorrectValues() {
            for (long i = -2; i <= 2; i++) {
                for (int j = 0; j < 10; j++) {
                    UtcInstant t = UtcInstant.ofModifiedJulianDay(i, j);
                    assertEquals(i, t.getModifiedJulianDay());
                    assertEquals(j, t.getNanoOfDay());
                    assertFalse(t.isLeapSecond());
                }
            }
        }

        @ParameterizedTest
        @ValueSource(strings = {
            "",                      // empty string
            "A",                     // not a date
            "2012-13-01T00:00:00Z"   // invalid month
        })
        void parse_withInvalidText_throwsException(String text) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(text));
        }
    }

    @Nested
    @DisplayName("Adjusters")
    class Adjusters {

        @Nested
        @DisplayName("withModifiedJulianDay()")
        class WithModifiedJulianDay {

            /**
             * Provides arguments for withModifiedJulianDay() success cases.
             *
             * @return a stream of arguments: {initialMjd, initialNanos, newMjd, expectedMjd, expectedNanos}
             */
            static Stream<Arguments> validScenarios() {
                return Stream.of(
                    Arguments.of(0L, 12345L, 1L, 1L, 12345L),
                    Arguments.of(0L, 12345L, -1L, -1L, 12345L),
                    // Change from one leap day to another, preserving nano-of-day
                    Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
                );
            }

            @ParameterizedTest
            @MethodSource("validScenarios")
            void should_returnUpdatedInstant_whenNewDayIsValid(
                long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {
                UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
                UtcInstant result = initial.withModifiedJulianDay(newMjd);
                assertEquals(expectedMjd, result.getModifiedJulianDay());
                assertEquals(expectedNanos, result.getNanoOfDay());
            }

            /**
             * Provides arguments for withModifiedJulianDay() failure cases.
             *
             * @return a stream of arguments: {initialMjd, initialNanos, newMjdToSet}
             */
            static Stream<Arguments> invalidScenarios() {
                return Stream.of(
                    // Set to a non-leap day when nano-of-day is in the leap second
                    Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
                    // Set to a non-leap day when nano-of-day is in the leap second
                    Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
                );
            }

            @ParameterizedTest
            @MethodSource("invalidScenarios")
            void should_throwException_whenNanoOfDayBecomesInvalid(long initialMjd, long initialNanos, long newMjdToSet) {
                UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
                assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjdToSet));
            }
        }

        @Nested
        @DisplayName("withNanoOfDay()")
        class WithNanoOfDay {

            /**
             * Provides arguments for withNanoOfDay() success cases.
             *
             * @return a stream of arguments: {mjd, initialNanos, newNanos, expectedMjd, expectedNanos}
             */
            static Stream<Arguments> validScenarios() {
                return Stream.of(
                    Arguments.of(0L, 12345L, 1L, 0L, 1L),
                    // Set to max nanos on a normal day
                    Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
                    // Set to max nanos on a leap day
                    Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1),
                    // Set to the leap second nano on a leap day
                    Arguments.of(MJD_1972_12_31_LEAP, 0, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY)
                );
            }

            @ParameterizedTest
            @MethodSource("validScenarios")
            void should_returnUpdatedInstant_whenNewNanoIsValid(
                long mjd, long initialNanos, long newNanos, long expectedMjd, long expectedNanos) {
                UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
                UtcInstant result = initial.withNanoOfDay(newNanos);
                assertEquals(expectedMjd, result.getModifiedJulianDay());
                assertEquals(expectedNanos, result.getNanoOfDay());
            }

            /**
             * Provides arguments for withNanoOfDay() failure cases.
             *
             * @return a stream of arguments: {mjd, initialNanos, invalidNewNanos}
             */
            static Stream<Arguments> invalidScenarios() {
                return Stream.of(
                    // Negative nano-of-day
                    Arguments.of(0L, 12345L, -1L),
                    // Exceeds max nanos on a normal day
                    Arguments.of(MJD_1972_12_30, 0, NANOS_PER_DAY),
                    // Exceeds max nanos on a leap day
                    Arguments.of(MJD_1972_12_31_LEAP, 0, NANOS_PER_LEAP_DAY)
                );
            }

            @ParameterizedTest
            @MethodSource("invalidScenarios")
            void should_throwException_whenNewNanoIsInvalid(long mjd, long initialNanos, long invalidNewNanos) {
                UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
                assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(invalidNewNanos));
            }
        }
    }

    @Nested
    @DisplayName("Arithmetic")
    class Arithmetic {

        /**
         * Provides arguments for plus() tests.
         *
         * @return a stream of arguments: {initialMjd, initialNanos, secondsToAdd, nanosToAdd, expectedMjd, expectedNanos}
         */
        static Stream<Arguments> plusScenarios() {
            return Stream.of(
                // --- Basic cases from MJD=0, Nanos=0 ---
                Arguments.of(0L, 0L, 0L, 0, 0L, 0L), // Add zero
                Arguments.of(0L, 0L, 0L, 1, 0L, 1L), // Add one nano
                Arguments.of(0L, 0L, 1L, 0, 0L, NANOS_PER_SEC), // Add one second
                Arguments.of(0L, 0L, SECS_PER_DAY, 0, 1L, 0L), // Add one full day in seconds
                Arguments.of(0L, 0L, -1L, 0, -1L, NANOS_PER_DAY - NANOS_PER_SEC), // Subtract one second, crossing day boundary
                Arguments.of(0L, 0L, 0L, -1, -1L, NANOS_PER_DAY - 1), // Subtract one nano, crossing day boundary
                Arguments.of(0L, 0L, -SECS_PER_DAY, 0, -1L, 0L), // Subtract one full day in seconds

                // --- Basic cases from MJD=1, Nanos=0 ---
                Arguments.of(1L, 0L, 0L, 0, 1L, 0L), // Add zero
                Arguments.of(1L, 0L, 0L, 1, 1L, 1L), // Add one nano
                Arguments.of(1L, 0L, SECS_PER_DAY, 0, 2L, 0L), // Add one full day
                Arguments.of(1L, 0L, -SECS_PER_DAY, 0, 0L, 0L)  // Subtract one full day
            );
        }

        @ParameterizedTest
        @MethodSource("plusScenarios")
        void plus_addsDurationCorrectly(
            long initialMjd, long initialNanos, long secondsToAdd, int nanosToAdd, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            Duration duration = Duration.ofSeconds(secondsToAdd, nanosToAdd);
            UtcInstant result = initial.plus(duration);
            assertEquals(expectedMjd, result.getModifiedJulianDay(), "MJD mismatch");
            assertEquals(expectedNanos, result.getNanoOfDay(), "Nano-of-day mismatch");
        }

        /**
         * Provides arguments for minus() tests.
         *
         * @return a stream of arguments: {initialMjd, initialNanos, secondsToSubtract, nanosToSubtract, expectedMjd, expectedNanos}
         */
        static Stream<Arguments> minusScenarios() {
            return Stream.of(
                // --- Basic cases from MJD=0, Nanos=0 ---
                Arguments.of(0L, 0L, 0L, 0, 0L, 0L), // Subtract zero
                Arguments.of(0L, 0L, 0L, 1, -1L, NANOS_PER_DAY - 1), // Subtract one nano, crossing day boundary
                Arguments.of(0L, 0L, 1L, 0, -1L, NANOS_PER_DAY - NANOS_PER_SEC), // Subtract one second, crossing day boundary
                Arguments.of(0L, 0L, SECS_PER_DAY, 0, -1L, 0L), // Subtract one full day in seconds
                Arguments.of(0L, 0L, -1L, 0, 0L, NANOS_PER_SEC), // Add one second (subtract negative)
                Arguments.of(0L, 0L, 0L, -1, 0L, 1L), // Add one nano (subtract negative)
                Arguments.of(0L, 0L, -SECS_PER_DAY, 0, 1L, 0L), // Add one full day in seconds (subtract negative)

                // --- Basic cases from MJD=1, Nanos=0 ---
                Arguments.of(1L, 0L, 0L, 0, 1L, 0L), // Subtract zero
                Arguments.of(1L, 0L, 0L, 1, 0L, NANOS_PER_DAY - 1), // Subtract one nano, crossing day boundary
                Arguments.of(1L, 0L, SECS_PER_DAY, 0, 0L, 0L), // Subtract one full day
                Arguments.of(1L, 0L, -SECS_PER_DAY, 0, 2L, 0L)  // Add one full day (subtract negative)
            );
        }

        @ParameterizedTest
        @MethodSource("minusScenarios")
        void minus_subtractsDurationCorrectly(
            long initialMjd, long initialNanos, long secondsToSubtract, int nanosToSubtract, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            Duration duration = Duration.ofSeconds(secondsToSubtract, nanosToSubtract);
            UtcInstant result = initial.minus(duration);
            assertEquals(expectedMjd, result.getModifiedJulianDay(), "MJD mismatch");
            assertEquals(expectedNanos, result.getNanoOfDay(), "Nano-of-day mismatch");
        }
    }

    @Nested
    @DisplayName("String Conversion")
    class StringConversion {

        /**
         * Provides arguments for toString() and parse() tests.
         *
         * @return a stream of arguments: {mjd, nanoOfDay, expectedString}
         */
        static Stream<Arguments> toStringScenarios() {
            return Stream.of(
                Arguments.of(40587L, 0L, "1970-01-01T00:00:00Z"),
                Arguments.of(40588L, 1L, "1970-01-02T00:00:00.000000001Z"),
                Arguments.of(40588L, 999L, "1970-01-02T00:00:00.000000999Z"),
                Arguments.of(40588L, 1_000L, "1970-01-02T00:00:00.000001Z"),
                Arguments.of(40588L, 999_000L, "1970-01-02T00:00:00.000999Z"),
                Arguments.of(40588L, 1_000_000L, "1970-01-02T00:00:00.001Z"),
                Arguments.of(40618L, 999_999_999L, "1970-02-01T00:00:00.999999999Z"),
                Arguments.of(40619L, 1_000_000_000L, "1970-02-02T00:00:01Z"),
                Arguments.of(40620L, 60L * NANOS_PER_SEC, "1970-02-03T00:01:00Z"),
                Arguments.of(40621L, 60L * 60L * NANOS_PER_SEC, "1970-02-04T01:00:00Z"),
                // Leap second representation
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
                Arguments.of(MJD_1973_01_01, 0L, "1973-01-01T00:00:00Z")
            );
        }

        @ParameterizedTest
        @MethodSource("toStringScenarios")
        void toString_returnsCorrectIsoFormat(long mjd, long nanoOfDay, String expectedString) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
            assertEquals(expectedString, instant.toString());
        }

        @ParameterizedTest
        @MethodSource("toStringScenarios")
        void parse_canParseStringFromToString(long mjd, long nanoOfDay, String textToParse) {
            UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
            UtcInstant parsed = UtcInstant.parse(textToParse);
            assertEquals(expected, parsed);
        }
    }
}