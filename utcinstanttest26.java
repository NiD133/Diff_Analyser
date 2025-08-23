package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("UtcInstant Tests")
public class UtcInstantTest {

    // A Modified Julian Day for a known leap day (1972-12-31)
    private static final long MJD_1972_12_31_LEAP = 41682;
    // A Modified Julian Day for a non-leap day (1972-12-30)
    private static final long MJD_1972_12_30 = 41681;
    // A Modified Julian Day for the day after the leap day (1973-01-01)
    private static final long MJD_1973_01_01 = 41683;
    // A Modified Julian Day for another leap day (1973-12-31)
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    @Nested
    @DisplayName("Parsing and Factory Methods")
    class ParsingAndFactory {

        @ParameterizedTest
        @ValueSource(strings = {"", "A", "2012-13-01T00:00:00Z"})
        @DisplayName("parse() with invalid string format throws DateTimeException")
        void parse_withInvalidString_throwsException(String invalidString) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
        }
    }

    @Nested
    @DisplayName("Modification Methods")
    class Modification {

        static Stream<Arguments> provider_withModifiedJulianDay_valid() {
            return Stream.of(
                    Arguments.of(0L, 12345L, 1L, 1L, 12345L),
                    Arguments.of(0L, 12345L, -1L, -1L, 12345L),
                    Arguments.of(7L, 12345L, 2L, 2L, 12345L),
                    Arguments.of(7L, 12345L, -2L, -2L, 12345L),
                    Arguments.of(-99L, 12345L, 3L, 3L, 12345L),
                    Arguments.of(-99L, 12345L, -3L, -3L, 12345L),
                    // Change from one leap day to another
                    Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_withModifiedJulianDay_valid")
        @DisplayName("withModifiedJulianDay() sets a valid new day")
        void withModifiedJulianDay_givenValidDay_returnsUpdatedInstant(long mjd, long nanos, long newMjd, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            UtcInstant result = initial.withModifiedJulianDay(newMjd);
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        static Stream<Arguments> provider_withModifiedJulianDay_invalid() {
            return Stream.of(
                    // Set to a non-leap day, but nano-of-day is for a leap day
                    Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
                    Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_withModifiedJulianDay_invalid")
        @DisplayName("withModifiedJulianDay() throws when nano-of-day becomes invalid for the new day")
        void withModifiedJulianDay_givenDayThatMakesNanosInvalid_throwsException(long mjd, long nanos, long newMjd) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjd));
        }

        static Stream<Arguments> provider_withNanoOfDay_valid() {
            return Stream.of(
                    // Simple cases
                    Arguments.of(0L, 12345L, 1L, 0L, 1L),
                    Arguments.of(7L, 12345L, 2L, 7L, 2L),
                    Arguments.of(-99L, 12345L, 3L, -99L, 3L),
                    // Edge cases on non-leap day
                    Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
                    // Edge cases on leap day
                    Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1),
                    Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
                    Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_withNanoOfDay_valid")
        @DisplayName("withNanoOfDay() sets a valid nano-of-day")
        void withNanoOfDay_givenValidNanos_returnsUpdatedInstant(long mjd, long nanos, long newNanoOfDay, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            UtcInstant result = initial.withNanoOfDay(newNanoOfDay);
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        static Stream<Arguments> provider_withNanoOfDay_invalid() {
            return Stream.of(
                    // Negative nano-of-day
                    Arguments.of(0L, 12345L, -1L),
                    // Exceeding max nanos on a non-leap day
                    Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY),
                    Arguments.of(MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_DAY),
                    Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1),
                    // Exceeding max nanos on a leap day
                    Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_withNanoOfDay_invalid")
        @DisplayName("withNanoOfDay() throws for an invalid nano-of-day value")
        void withNanoOfDay_givenInvalidNanos_throwsException(long mjd, long nanos, long newNanoOfDay) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(newNanoOfDay));
        }
    }

    @Nested
    @DisplayName("Arithmetic Operations")
    class Arithmetic {

        private static Stream<Arguments> provider_plus() {
            return Stream.of(
                    // --- Tests starting from MJD=0, nano=0 ---
                    Arguments.of(0L, 0L, -2 * SECS_PER_DAY, 5, -2L, 5L),
                    Arguments.of(0L, 0L, -1 * SECS_PER_DAY, 1, -1L, 1L),
                    Arguments.of(0L, 0L, -1 * SECS_PER_DAY, 0, -1L, 0L),
                    Arguments.of(0L, 0L, 0L, -2, -1L, NANOS_PER_DAY - 2),
                    Arguments.of(0L, 0L, 0L, -1, -1L, NANOS_PER_DAY - 1),
                    Arguments.of(0L, 0L, 0L, 0, 0L, 0L),
                    Arguments.of(0L, 0L, 0L, 1, 0L, 1L),
                    Arguments.of(0L, 0L, 0L, 2, 0L, 2L),
                    Arguments.of(0L, 0L, 1L, 0, 0L, 1 * NANOS_PER_SEC),
                    Arguments.of(0L, 0L, 2L, 0, 0L, 2 * NANOS_PER_SEC),
                    Arguments.of(0L, 0L, 3L, 333333333, 0L, 3 * NANOS_PER_SEC + 333333333),
                    Arguments.of(0L, 0L, 1 * SECS_PER_DAY, 0, 1L, 0L),
                    Arguments.of(0L, 0L, 1 * SECS_PER_DAY, 1, 1L, 1L),
                    Arguments.of(0L, 0L, 2 * SECS_PER_DAY, 5, 2L, 5L),

                    // --- Tests starting from MJD=1, nano=0 ---
                    Arguments.of(1L, 0L, -2 * SECS_PER_DAY, 5, -1L, 5L),
                    Arguments.of(1L, 0L, -1 * SECS_PER_DAY, 1, 0L, 1L),
                    Arguments.of(1L, 0L, -1 * SECS_PER_DAY, 0, 0L, 0L),
                    Arguments.of(1L, 0L, 0L, -2, 0L, NANOS_PER_DAY - 2),
                    Arguments.of(1L, 0L, 0L, -1, 0L, NANOS_PER_DAY - 1),
                    Arguments.of(1L, 0L, 0L, 0, 1L, 0L),
                    Arguments.of(1L, 0L, 0L, 1, 1L, 1L),
                    Arguments.of(1L, 0L, 0L, 2, 1L, 2L),
                    Arguments.of(1L, 0L, 1L, 0, 1L, 1 * NANOS_PER_SEC),
                    Arguments.of(1L, 0L, 2L, 0, 1L, 2 * NANOS_PER_SEC),
                    Arguments.of(1L, 0L, 3L, 333333333, 1L, 3 * NANOS_PER_SEC + 333333333),
                    Arguments.of(1L, 0L, 1 * SECS_PER_DAY, 0, 2L, 0L),
                    Arguments.of(1L, 0L, 1 * SECS_PER_DAY, 1, 2L, 1L),
                    Arguments.of(1L, 0L, 2 * SECS_PER_DAY, 5, 3L, 5L)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_plus")
        @DisplayName("plus() adds a duration correctly")
        void plus_addsDurationCorrectly(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            Duration duration = Duration.ofSeconds(plusSeconds, plusNanos);
            UtcInstant result = initial.plus(duration);

            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        private static Stream<Arguments> provider_minus() {
            return Stream.of(
                    // --- Tests starting from MJD=0, nano=0 ---
                    Arguments.of(0L, 0L, 2 * SECS_PER_DAY, -5, -2L, 5L),
                    Arguments.of(0L, 0L, 1 * SECS_PER_DAY, -1, -1L, 1L),
                    Arguments.of(0L, 0L, 1 * SECS_PER_DAY, 0, -1L, 0L),
                    Arguments.of(0L, 0L, 0L, 2, -1L, NANOS_PER_DAY - 2),
                    Arguments.of(0L, 0L, 0L, 1, -1L, NANOS_PER_DAY - 1),
                    Arguments.of(0L, 0L, 0L, 0, 0L, 0L),
                    Arguments.of(0L, 0L, 0L, -1, 0L, 1L),
                    Arguments.of(0L, 0L, 0L, -2, 0L, 2L),
                    Arguments.of(0L, 0L, -1L, 0, 0L, 1 * NANOS_PER_SEC),
                    Arguments.of(0L, 0L, -2L, 0, 0L, 2 * NANOS_PER_SEC),
                    Arguments.of(0L, 0L, -3L, -333333333, 0L, 3 * NANOS_PER_SEC + 333333333),
                    Arguments.of(0L, 0L, -1 * SECS_PER_DAY, 0, 1L, 0L),
                    Arguments.of(0L, 0L, -1 * SECS_PER_DAY, -1, 1L, 1L),
                    Arguments.of(0L, 0L, -2 * SECS_PER_DAY, -5, 2L, 5L),

                    // --- Tests starting from MJD=1, nano=0 ---
                    Arguments.of(1L, 0L, 2 * SECS_PER_DAY, -5, -1L, 5L),
                    Arguments.of(1L, 0L, 1 * SECS_PER_DAY, -1, 0L, 1L),
                    Arguments.of(1L, 0L, 1 * SECS_PER_DAY, 0, 0L, 0L),
                    Arguments.of(1L, 0L, 0L, 2, 0L, NANOS_PER_DAY - 2),
                    Arguments.of(1L, 0L, 0L, 1, 0L, NANOS_PER_DAY - 1),
                    Arguments.of(1L, 0L, 0L, 0, 1L, 0L),
                    Arguments.of(1L, 0L, 0L, -1, 1L, 1L),
                    Arguments.of(1L, 0L, 0L, -2, 1L, 2L),
                    Arguments.of(1L, 0L, -1L, 0, 1L, 1 * NANOS_PER_SEC),
                    Arguments.of(1L, 0L, -2L, 0, 1L, 2 * NANOS_PER_SEC),
                    Arguments.of(1L, 0L, -3L, -333333333, 1L, 3 * NANOS_PER_SEC + 333333333),
                    Arguments.of(1L, 0L, -1 * SECS_PER_DAY, 0, 2L, 0L),
                    Arguments.of(1L, 0L, -1 * SECS_PER_DAY, -1, 2L, 1L),
                    Arguments.of(1L, 0L, -2 * SECS_PER_DAY, -5, 3L, 5L)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_minus")
        @DisplayName("minus() subtracts a duration correctly")
        void minus_subtractsDurationCorrectly(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            Duration duration = Duration.ofSeconds(minusSeconds, minusNanos);
            UtcInstant result = initial.minus(duration);

            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }
    }

    @Nested
    @DisplayName("Conversion and Formatting")
    class ConversionAndFormatting {

        static Stream<Arguments> provider_toStringCases() {
            return Stream.of(
                    Arguments.of(40587L, 0L, "1970-01-01T00:00:00Z"),
                    Arguments.of(40588L, 1L, "1970-01-02T00:00:00.000000001Z"),
                    Arguments.of(40588L, 999L, "1970-01-02T00:00:00.000000999Z"),
                    Arguments.of(40588L, 1000L, "1970-01-02T00:00:00.000001Z"),
                    Arguments.of(40588L, 999000L, "1970-01-02T00:00:00.000999Z"),
                    Arguments.of(40588L, 1000000L, "1970-01-02T00:00:00.001Z"),
                    Arguments.of(40618L, 999999999L, "1970-02-01T00:00:00.999999999Z"),
                    Arguments.of(40619L, 1000000000L, "1970-02-02T00:00:01Z"),
                    Arguments.of(40620L, 60L * 1000000000L, "1970-02-03T00:01:00Z"),
                    Arguments.of(40621L, 60L * 60L * 1000000000L, "1970-02-04T01:00:00Z"),
                    Arguments.of(MJD_1972_12_31_LEAP, 24L * 60L * 60L * 1000000000L - 1000000000L, "1972-12-31T23:59:59Z"),
                    Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
                    Arguments.of(MJD_1973_01_01, 0L, "1973-01-01T00:00:00Z")
            );
        }

        @ParameterizedTest
        @MethodSource("provider_toStringCases")
        @DisplayName("toString() formats as an ISO-8601 string")
        void toString_formatsAsIsoString(long mjd, long nod, String expected) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nod);
            assertEquals(expected, instant.toString());
        }

        @ParameterizedTest
        @MethodSource("provider_toStringCases")
        @DisplayName("parse() correctly parses a string generated by toString()")
        void parse_reversesToString(long mjd, long nod, String isoString) {
            UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nod);
            assertEquals(expected, UtcInstant.parse(isoString));
        }

        @Test
        @DisplayName("toInstant() converts correctly for a range of dates around 1980")
        void toInstant_convertsToStandardInstant() {
            // MJD 44239 corresponds to 1980-01-01.
            final long MJD_1980_01_01 = 44239L;
            // The number of seconds from 1970-01-01 epoch to 1980-01-01 is 315,532,800,
            // accounting for leap years in 1972 and 1976.
            final long EPOCH_SECONDS_1980_01_01 = 315_532_800L;

            // This test verifies conversion for 1000 days before and after 1980-01-01.
            for (int dayOffset = -1000; dayOffset < 1000; dayOffset++) {
                for (int secondOfDay = 0; secondOfDay < 10; secondOfDay++) {
                    long currentMjd = MJD_1980_01_01 + dayOffset;
                    long nanoOfDay = secondOfDay * NANOS_PER_SEC + 2; // Test with non-zero nanos
                    UtcInstant test = UtcInstant.ofModifiedJulianDay(currentMjd, nanoOfDay);

                    long expectedEpochSeconds = EPOCH_SECONDS_1980_01_01 + dayOffset * SECS_PER_DAY + secondOfDay;
                    Instant expected = Instant.ofEpochSecond(expectedEpochSeconds, 2);

                    assertEquals(expected, test.toInstant());
                }
            }
        }
    }
}