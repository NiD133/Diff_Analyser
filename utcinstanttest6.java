package org.threeten.extra.scale;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.DateTimeException;
import java.time.Duration;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for modification, arithmetic, and formatting of {@link UtcInstant}.
 */
@DisplayName("UtcInstant Modification, Arithmetic and Formatting")
class UtcInstantModificationAndArithmeticTest {

    // A non-leap day: 1972-12-30
    private static final long MJD_NORMAL_DAY = 41681;
    // A leap day: 1972-12-31, which had a leap second.
    private static final long MJD_LEAP_DAY = 41682;
    // A non-leap day following the leap day: 1973-01-01
    private static final long MJD_DAY_AFTER_LEAP = 41683;
    // Another leap day, one year after the first one: 1973-12-31
    private static final long MJD_ANOTHER_LEAP_DAY = MJD_LEAP_DAY + 365;

    private static final long SECS_PER_DAY = 86400L;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    @Nested
    @DisplayName("Factory ofModifiedJulianDay()")
    class OfModifiedJulianDayTest {
        @Test
        @DisplayName("should correctly handle last nanosecond of a leap second")
        void ofModifiedJulianDay_atEndOfLeapSecond() {
            // A leap day has one extra second, so NANOS_PER_LEAP_DAY nanoseconds in total.
            long nanoOfDay = NANOS_PER_LEAP_DAY - 1;
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(MJD_LEAP_DAY, nanoOfDay);

            assertAll(
                () -> assertEquals(MJD_LEAP_DAY, instant.getModifiedJulianDay()),
                () -> assertEquals(nanoOfDay, instant.getNanoOfDay()),
                () -> assertTrue(instant.isLeapSecond(), "Should be a leap second"),
                () -> assertEquals("1972-12-31T23:59:60.999999999Z", instant.toString())
            );
        }
    }

    @Nested
    @DisplayName("Method withModifiedJulianDay()")
    class WithModifiedJulianDayTest {

        static Stream<Arguments> validModifiedJulianDayCases() {
            return Stream.of(
                Arguments.of("Simple positive change", 0L, 12345L, 1L, 1L, 12345L),
                Arguments.of("Simple negative change", 0L, 12345L, -1L, -1L, 12345L),
                Arguments.of("Leap day to another leap day", MJD_LEAP_DAY, NANOS_PER_DAY, MJD_ANOTHER_LEAP_DAY, MJD_ANOTHER_LEAP_DAY, NANOS_PER_DAY),
                Arguments.of("Leap day to same day", MJD_LEAP_DAY, NANOS_PER_DAY, MJD_LEAP_DAY, MJD_LEAP_DAY, NANOS_PER_DAY)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("validModifiedJulianDayCases")
        @DisplayName("should return updated instant for valid new day")
        void withModifiedJulianDay_givenValidNewDay_returnsUpdatedInstant(
            String description, long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            UtcInstant result = initialInstant.withModifiedJulianDay(newMjd);
            assertAll(
                () -> assertEquals(expectedMjd, result.getModifiedJulianDay(), "Modified Julian Day should match"),
                () -> assertEquals(expectedNanos, result.getNanoOfDay(), "Nano of day should match")
            );
        }

        static Stream<Arguments> invalidModifiedJulianDayCases() {
            return Stream.of(
                // On a leap day, nano-of-day is valid (23:59:60). Change to a non-leap day, making nano-of-day invalid.
                Arguments.of("From leap day to non-leap day with invalid nanos", MJD_LEAP_DAY, NANOS_PER_DAY, MJD_NORMAL_DAY),
                Arguments.of("From leap day to another non-leap day with invalid nanos", MJD_LEAP_DAY, NANOS_PER_DAY, MJD_DAY_AFTER_LEAP)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidModifiedJulianDayCases")
        @DisplayName("should throw exception if new day makes nano-of-day invalid")
        void withModifiedJulianDay_givenNewDayMakingNanosInvalid_throwsException(
            String description, long initialMjd, long initialNanos, long newMjd) {
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            assertThrows(DateTimeException.class, () -> initialInstant.withModifiedJulianDay(newMjd));
        }
    }

    @Nested
    @DisplayName("Method withNanoOfDay()")
    class WithNanoOfDayTest {

        static Stream<Arguments> validNanoOfDayCases() {
            return Stream.of(
                Arguments.of("Simple change", 0L, 1L, 0L, 1L),
                Arguments.of("Normal day, max valid nanos", MJD_NORMAL_DAY, NANOS_PER_DAY - 1, MJD_NORMAL_DAY, NANOS_PER_DAY - 1),
                Arguments.of("Leap day, max valid standard nanos", MJD_LEAP_DAY, NANOS_PER_DAY - 1, MJD_LEAP_DAY, NANOS_PER_DAY - 1),
                Arguments.of("Leap day, leap second nano", MJD_LEAP_DAY, NANOS_PER_DAY, MJD_LEAP_DAY, NANOS_PER_DAY),
                Arguments.of("Leap day, max valid leap nanos", MJD_LEAP_DAY, NANOS_PER_LEAP_DAY - 1, MJD_LEAP_DAY, NANOS_PER_LEAP_DAY - 1)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("validNanoOfDayCases")
        @DisplayName("should return updated instant for valid nano-of-day")
        void withNanoOfDay_givenValidNanos_returnsUpdatedInstant(
            String description, long mjd, long newNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, 0); // initial nano is irrelevant
            UtcInstant result = initialInstant.withNanoOfDay(newNanos);
            assertAll(
                () -> assertEquals(expectedMjd, result.getModifiedJulianDay(), "Modified Julian Day should match"),
                () -> assertEquals(expectedNanos, result.getNanoOfDay(), "Nano of day should match")
            );
        }

        static Stream<Arguments> invalidNanoOfDayCases() {
            return Stream.of(
                Arguments.of("Negative nanos", MJD_NORMAL_DAY, -1L),
                Arguments.of("Normal day, nanos too large", MJD_NORMAL_DAY, NANOS_PER_DAY),
                Arguments.of("Normal day, nanos for leap day", MJD_NORMAL_DAY, NANOS_PER_LEAP_DAY - 1),
                Arguments.of("Leap day, nanos too large", MJD_LEAP_DAY, NANOS_PER_LEAP_DAY)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidNanoOfDayCases")
        @DisplayName("should throw exception for invalid nano-of-day")
        void withNanoOfDay_givenInvalidNanos_throwsException(String description, long mjd, long invalidNanos) {
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, 0);
            assertThrows(DateTimeException.class, () -> initialInstant.withNanoOfDay(invalidNanos));
        }
    }

    @Nested
    @DisplayName("Method plus(Duration)")
    class PlusDurationTest {

        static Stream<Arguments> plusCases() {
            return Stream.of(
                Arguments.of("Add zero duration", 1L, 123L, 0L, 0, 1L, 123L),
                Arguments.of("Add positive nanos, no day change", 1L, 123L, 0L, 1, 1L, 124L),
                Arguments.of("Add negative nanos, no day change", 1L, 123L, 0L, -1, 1L, 122L),
                Arguments.of("Add positive seconds, no day change", 1L, 123L, 5L, 0, 1L, 123L + 5 * NANOS_PER_SEC),
                Arguments.of("Add nanos crossing to next day", 1L, NANOS_PER_DAY - 1, 0L, 2, 2L, 1L),
                Arguments.of("Add nanos crossing to previous day", 1L, 1L, 0L, -2, 0L, NANOS_PER_DAY - 1),
                Arguments.of("Add one day in seconds", 1L, 123L, SECS_PER_DAY, 0, 2L, 123L),
                Arguments.of("Subtract one day in seconds", 1L, 123L, -SECS_PER_DAY, 0, 0L, 123L)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("plusCases")
        @DisplayName("should return correct instant after adding duration")
        void plus_duration_shouldReturnCorrectInstant(
            String description, long initialMjd, long initialNanos,
            long secondsToAdd, int nanosToAdd,
            long expectedMjd, long expectedNanos) {

            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            Duration duration = Duration.ofSeconds(secondsToAdd, nanosToAdd);
            UtcInstant result = initialInstant.plus(duration);

            assertAll(
                () -> assertEquals(expectedMjd, result.getModifiedJulianDay(), "Modified Julian Day should match"),
                () -> assertEquals(expectedNanos, result.getNanoOfDay(), "Nano of day should match")
            );
        }
    }

    @Nested
    @DisplayName("Method minus(Duration)")
    class MinusDurationTest {

        static Stream<Arguments> minusCases() {
            return Stream.of(
                Arguments.of("Subtract zero duration", 1L, 123L, 0L, 0, 1L, 123L),
                Arguments.of("Subtract positive nanos, no day change", 1L, 123L, 0L, 1, 1L, 122L),
                Arguments.of("Subtract negative nanos, no day change", 1L, 123L, 0L, -1, 1L, 124L),
                Arguments.of("Subtract positive seconds, no day change", 1L, 5 * NANOS_PER_SEC, 5L, 0, 1L, 0L),
                Arguments.of("Subtract nanos crossing to previous day", 1L, 1L, 0L, 2, 0L, NANOS_PER_DAY - 1),
                Arguments.of("Subtract nanos crossing to next day", 0L, NANOS_PER_DAY - 1, 0L, -2, 1L, 1L),
                Arguments.of("Subtract one day in seconds", 2L, 123L, SECS_PER_DAY, 0, 1L, 123L),
                Arguments.of("Subtract negative one day in seconds", 1L, 123L, -SECS_PER_DAY, 0, 2L, 123L)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("minusCases")
        @DisplayName("should return correct instant after subtracting duration")
        void minus_duration_shouldReturnCorrectInstant(
            String description, long initialMjd, long initialNanos,
            long secondsToSubtract, int nanosToSubtract,
            long expectedMjd, long expectedNanos) {

            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            Duration duration = Duration.ofSeconds(secondsToSubtract, nanosToSubtract);
            UtcInstant result = initialInstant.minus(duration);

            assertAll(
                () -> assertEquals(expectedMjd, result.getModifiedJulianDay(), "Modified Julian Day should match"),
                () -> assertEquals(expectedNanos, result.getNanoOfDay(), "Nano of day should match")
            );
        }
    }

    @Nested
    @DisplayName("Serialization and Parsing")
    class ToStringAndParseTest {

        static Stream<Arguments> toStringAndParseCases() {
            return Stream.of(
                Arguments.of("Epoch start", 40587L, 0L, "1970-01-01T00:00:00Z"),
                Arguments.of("With nanoseconds", 40588L, 1L, "1970-01-02T00:00:00.000000001Z"),
                Arguments.of("With milliseconds", 40588L, 1_000_000L, "1970-01-02T00:00:00.001Z"),
                Arguments.of("With seconds", 40619L, NANOS_PER_SEC, "1970-02-02T00:00:01Z"),
                Arguments.of("End of day before leap second", MJD_LEAP_DAY, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
                Arguments.of("During leap second", MJD_LEAP_DAY, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
                Arguments.of("Start of day after leap second", MJD_DAY_AFTER_LEAP, 0L, "1973-01-01T00:00:00Z")
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("toStringAndParseCases")
        @DisplayName("toString() should produce correct ISO-8601 format")
        void toString_shouldReturnCorrectFormat(String description, long mjd, long nanoOfDay, String expectedString) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
            assertEquals(expectedString, instant.toString());
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("toStringAndParseCases")
        @DisplayName("parse() should correctly read ISO-8601 format (inverse of toString)")
        void parse_shouldBeInverseOfToString(String description, long mjd, long nanoOfDay, String inputString) {
            UtcInstant expectedInstant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
            assertEquals(expectedInstant, UtcInstant.parse(inputString));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "A", "2012-13-01T00:00:00Z"})
        @DisplayName("parse() should throw exception for invalid formats")
        void parse_withInvalidInput_throwsDateTimeException(String invalidInput) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidInput));
        }
    }
}