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

/**
 * Tests for modification, arithmetic, and parsing of {@link UtcInstant}.
 */
@DisplayName("UtcInstant Modification, Arithmetic, and Parsing")
class UtcInstantModificationAndArithmeticTest {

    // MJD for 1972-12-30, a non-leap day
    private static final long MJD_1972_12_30 = 41681;
    // MJD for 1972-12-31, a day with a leap second
    private static final long MJD_1972_12_31_LEAP = 41682;
    // MJD for 1973-01-01, a non-leap day
    private static final long MJD_1973_01_01 = 41683;
    // MJD for 1973-12-31, another day with a leap second
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("parse()")
    class Parse {

        static Stream<Arguments> provider_invalidParseStrings() {
            return Stream.of(
                Arguments.of(""),                      // Empty string
                Arguments.of("A"),                     // Not a date-time
                Arguments.of("2012-13-01T00:00:00Z")  // Invalid month
            );
        }

        @ParameterizedTest
        @MethodSource("provider_invalidParseStrings")
        void withInvalidText_throwsDateTimeException(String invalidText) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidText));
        }

        @Test
        void withLeapSecondOnNonLeapDay_throwsDateTimeException() {
            // A leap second (23:59:60) is only valid on a day designated to have one.
            assertThrows(DateTimeException.class, () -> UtcInstant.parse("1972-11-11T23:59:60Z"));
        }
    }

    //-----------------------------------------------------------------------
    // withModifiedJulianDay()
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("withModifiedJulianDay()")
    class WithModifiedJulianDay {

        /**
         * Provides arguments for testing withModifiedJulianDay success cases.
         *
         * @return a stream of [initialMjd, initialNanos, newMjd, expectedMjd, expectedNanos]
         */
        static Stream<Arguments> provider_validDayChanges() {
            return Stream.of(
                // Simple changes on a non-leap day
                Arguments.of(0L, 12345L, 1L, 1L, 12345L),
                Arguments.of(0L, 12345L, -1L, -1L, 12345L),
                // Change from one leap day to another, preserving nano-of-day
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY),
                // Change to the same day should have no effect
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_validDayChanges")
        void returnsNewInstanceWithUpdatedDay(long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {
            // Arrange
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);

            // Act
            UtcInstant result = initialInstant.withModifiedJulianDay(newMjd);

            // Assert
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        /**
         * Provides arguments for testing withModifiedJulianDay failure cases.
         * The nano-of-day from the initial instant is invalid for the new day.
         *
         * @return a stream of [initialMjd, initialNanos, newMjd]
         */
        static Stream<Arguments> provider_invalidDayChanges() {
            return Stream.of(
                // Change from a leap day to a non-leap day, where nano-of-day is in the leap second
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_invalidDayChanges")
        void whenNanoOfDayIsInvalidForNewDay_throwsDateTimeException(long initialMjd, long initialNanos, long newMjd) {
            // Arrange
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);

            // Act & Assert
            assertThrows(DateTimeException.class, () -> initialInstant.withModifiedJulianDay(newMjd));
        }
    }

    //-----------------------------------------------------------------------
    // withNanoOfDay()
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("withNanoOfDay()")
    class WithNanoOfDay {

        /**
         * Provides arguments for testing withNanoOfDay success cases.
         *
         * @return a stream of [mjd, initialNanos, newNanos, expectedMjd, expectedNanos]
         */
        static Stream<Arguments> provider_validNanoChanges() {
            return Stream.of(
                // Simple changes
                Arguments.of(0L, 12345L, 1L, 0L, 1L),
                Arguments.of(7L, 12345L, 2L, 7L, 2L),
                // Set to max valid nanos on a non-leap day
                Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
                // Set to max valid nanos on a leap day (standard day length)
                Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1),
                // Set to the leap second nano value on a leap day
                Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
                // Set to max valid nanos on a leap day
                Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_validNanoChanges")
        void returnsNewInstanceWithUpdatedNanos(long mjd, long initialNanos, long newNanos, long expectedMjd, long expectedNanos) {
            // Arrange
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);

            // Act
            UtcInstant result = initialInstant.withNanoOfDay(newNanos);

            // Assert
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        /**
         * Provides arguments for testing withNanoOfDay failure cases.
         *
         * @return a stream of [mjd, initialNanos, invalidNewNanos]
         */
        static Stream<Arguments> provider_invalidNanoChanges() {
            return Stream.of(
                // Negative nanos are always invalid
                Arguments.of(0L, 12345L, -1L),
                // Nanos greater than or equal to a standard day's worth on a non-leap day
                Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY),
                Arguments.of(MJD_1973_01_01, 0L, NANOS_PER_DAY),
                // Nanos greater than or equal to a leap day's worth on a leap day
                Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_invalidNanoChanges")
        void whenNanosAreInvalidForDay_throwsDateTimeException(long mjd, long initialNanos, long invalidNewNanos) {
            // Arrange
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);

            // Act & Assert
            assertThrows(DateTimeException.class, () -> initialInstant.withNanoOfDay(invalidNewNanos));
        }
    }

    //-----------------------------------------------------------------------
    // plus()
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("plus()")
    class Plus {

        /**
         * Provides arguments for testing plus(Duration).
         *
         * @return a stream of [mjd, nanos, plusSeconds, plusNanos, expectedMjd, expectedNanos]
         */
        static Stream<Arguments> provider_plusDuration() {
            return Stream.of(
                // --- Test cases for initial instant at MJD=0, nano=0 ---
                // Add negative duration, crossing day boundary
                Arguments.of(0, 0, -2 * SECS_PER_DAY, 5, -2, 5),
                Arguments.of(0, 0, -1 * SECS_PER_DAY, 1, -1, 1),
                // Add negative duration, within a day
                Arguments.of(0, 0, 0, -1, -1, NANOS_PER_DAY - 1),
                Arguments.of(0, 0, 0, -2, -1, NANOS_PER_DAY - 2),
                // Add zero duration
                Arguments.of(0, 0, 0, 0, 0, 0),
                // Add positive duration, within a day
                Arguments.of(0, 0, 0, 1, 0, 1),
                Arguments.of(0, 0, 3, 333333333, 0, 3 * NANOS_PER_SEC + 333333333),
                // Add positive duration, crossing day boundary
                Arguments.of(0, 0, 1 * SECS_PER_DAY, 0, 1, 0),
                Arguments.of(0, 0, 2 * SECS_PER_DAY, 5, 2, 5)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_plusDuration")
        void addsDurationCorrectly(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
            // Arrange
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            Duration durationToAdd = Duration.ofSeconds(plusSeconds, plusNanos);

            // Act
            UtcInstant result = initialInstant.plus(durationToAdd);

            // Assert
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }
    }

    //-----------------------------------------------------------------------
    // minus()
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("minus()")
    class Minus {

        /**
         * Provides arguments for testing minus(Duration).
         *
         * @return a stream of [mjd, nanos, minusSeconds, minusNanos, expectedMjd, expectedNanos]
         */
        static Stream<Arguments> provider_minusDuration() {
            return Stream.of(
                // --- Test cases for initial instant at MJD=0, nano=0 ---
                // Subtract positive duration, crossing day boundary
                Arguments.of(0, 0, 2 * SECS_PER_DAY, -5, -2, 5),
                Arguments.of(0, 0, 1 * SECS_PER_DAY, 0, -1, 0),
                // Subtract positive duration, within a day
                Arguments.of(0, 0, 0, 1, -1, NANOS_PER_DAY - 1),
                Arguments.of(0, 0, 0, 2, -1, NANOS_PER_DAY - 2),
                // Subtract zero duration
                Arguments.of(0, 0, 0, 0, 0, 0),
                // Subtract negative duration, within a day
                Arguments.of(0, 0, 0, -1, 0, 1),
                Arguments.of(0, 0, -3, -333333333, 0, 3 * NANOS_PER_SEC + 333333333),
                // Subtract negative duration, crossing day boundary
                Arguments.of(0, 0, -1 * SECS_PER_DAY, 0, 1, 0),
                Arguments.of(0, 0, -2 * SECS_PER_DAY, -5, 2, 5)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_minusDuration")
        void subtractsDurationCorrectly(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
            // Arrange
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            Duration durationToSubtract = Duration.ofSeconds(minusSeconds, minusNanos);

            // Act
            UtcInstant result = initialInstant.minus(durationToSubtract);

            // Assert
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }
    }

    //-----------------------------------------------------------------------
    // toString() and round-trip parsing
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("toString() and parsing")
    class ToStringAndParse {

        /**
         * Provides arguments for testing toString() and parsing.
         *
         * @return a stream of [mjd, nanoOfDay, expectedString]
         */
        static Stream<Arguments> provider_toStringCases() {
            return Stream.of(
                Arguments.of(40587, 0, "1970-01-01T00:00:00Z"),
                Arguments.of(40588, 1, "1970-01-02T00:00:00.000000001Z"),
                Arguments.of(40588, 999, "1970-01-02T00:00:00.000000999Z"),
                Arguments.of(40588, 1000, "1970-01-02T00:00:00.000001Z"),
                Arguments.of(40588, 999000, "1970-01-02T00:00:00.000999Z"),
                Arguments.of(40588, 1000000, "1970-01-02T00:00:00.001Z"),
                Arguments.of(40618, 999999999, "1970-02-01T00:00:00.999999999Z"),
                Arguments.of(40619, 1000000000, "1970-02-02T00:00:01Z"),
                Arguments.of(40620, 60L * NANOS_PER_SEC, "1970-02-03T00:01:00Z"),
                Arguments.of(40621, 60L * 60L * NANOS_PER_SEC, "1970-02-04T01:00:00Z"),
                // Leap second cases
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
                Arguments.of(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z")
            );
        }

        @ParameterizedTest
        @MethodSource("provider_toStringCases")
        void toString_returnsCorrectlyFormattedString(long mjd, long nanoOfDay, String expectedString) {
            // Arrange
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);

            // Act & Assert
            assertEquals(expectedString, instant.toString());
        }

        @ParameterizedTest
        @MethodSource("provider_toStringCases")
        void parse_ofStringFromToString_returnsEqualInstance(long mjd, long nanoOfDay, String isoString) {
            // Arrange
            UtcInstant expectedInstant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);

            // Act
            UtcInstant parsedInstant = UtcInstant.parse(isoString);

            // Assert
            assertEquals(expectedInstant, parsedInstant);
        }
    }
}