package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertAll;
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

@DisplayName("Tests for UtcInstant modification and arithmetic")
public class UtcInstantModificationAndArithmeticTest {

    private static final long MJD_1972_12_30 = 41681;
    private static final long MJD_1972_12_31_LEAP = 41682; // A day with a leap second
    private static final long MJD_1973_01_01 = 41683;
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    @Nested
    @DisplayName("Factory method ofModifiedJulianDay()")
    class OfModifiedJulianDay {
        @Test
        @DisplayName("creates correct instant at end of a normal day preceding a leap day")
        void ofModifiedJulianDay_atEndOfNormalDayBeforeLeapDay_isCorrect() {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1);

            assertAll(
                () -> assertEquals(MJD_1972_12_31_LEAP, instant.getModifiedJulianDay()),
                () -> assertEquals(NANOS_PER_DAY - 1, instant.getNanoOfDay()),
                () -> assertFalse(instant.isLeapSecond()),
                () -> assertEquals("1972-12-31T23:59:59.999999999Z", instant.toString())
            );
        }
    }

    @Nested
    @DisplayName("Factory method parse()")
    class Parse {
        static Stream<String> provideInvalidStringsForParsing() {
            return Stream.of(
                "",                      // Empty string
                "A",                     // Not a date-time
                "2012-13-01T00:00:00Z"   // Invalid month
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidStringsForParsing")
        @DisplayName("throws exception for invalid string formats")
        void parse_withInvalidString_throwsException(String invalidString) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
        }
    }

    @Nested
    @DisplayName("Modification with withModifiedJulianDay()")
    class WithModifiedJulianDay {

        static Stream<Arguments> provideValidMjdChanges() {
            return Stream.of(
                Arguments.of(0L, 12345L, 1L, 1L, 12345L),
                Arguments.of(0L, 12345L, -1L, -1L, 12345L),
                Arguments.of(7L, 12345L, 2L, 2L, 12345L),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
            );
        }

        @ParameterizedTest
        @MethodSource("provideValidMjdChanges")
        @DisplayName("returns updated instant for valid day")
        void withModifiedJulianDay_onValidDay_returnsUpdatedInstant(
            long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {
            
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            UtcInstant result = initialInstant.withModifiedJulianDay(newMjd);

            assertAll(
                () -> assertEquals(expectedMjd, result.getModifiedJulianDay()),
                () -> assertEquals(expectedNanos, result.getNanoOfDay())
            );
        }

        static Stream<Arguments> provideInvalidMjdChanges() {
            return Stream.of(
                // Attempting to set a day that makes the existing nano-of-day invalid
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30), // Leap nano-of-day on a non-leap day
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)  // Leap nano-of-day on a non-leap day
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidMjdChanges")
        @DisplayName("throws exception for invalid day")
        void withModifiedJulianDay_onInvalidDay_throwsException(long initialMjd, long initialNanos, long invalidNewMjd) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            assertThrows(DateTimeException.class, () -> instant.withModifiedJulianDay(invalidNewMjd));
        }
    }

    @Nested
    @DisplayName("Modification with withNanoOfDay()")
    class WithNanoOfDay {

        static Stream<Arguments> provideValidNanoOfDayChanges() {
            return Stream.of(
                Arguments.of(0L, 12345L, 1L, 0L, 1L),
                Arguments.of(7L, 12345L, 2L, 7L, 2L),
                Arguments.of(-99L, 12345L, 3L, -99L, 3L),
                Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1)
            );
        }

        @ParameterizedTest
        @MethodSource("provideValidNanoOfDayChanges")
        @DisplayName("returns updated instant for valid nano-of-day")
        void withNanoOfDay_withValidNanos_returnsUpdatedInstant(
            long initialMjd, long initialNanos, long newNanos, long expectedMjd, long expectedNanos) {
            
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            UtcInstant result = initialInstant.withNanoOfDay(newNanos);

            assertAll(
                () -> assertEquals(expectedMjd, result.getModifiedJulianDay()),
                () -> assertEquals(expectedNanos, result.getNanoOfDay())
            );
        }

        static Stream<Arguments> provideInvalidNanoOfDayChanges() {
            return Stream.of(
                Arguments.of(0L, 12345L, -1L), // Negative nano-of-day
                Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY), // Exceeds max nanos for a normal day
                Arguments.of(MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_DAY), // Exceeds max nanos for a normal day
                Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1), // Exceeds max nanos for a normal day
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY) // Exceeds max nanos for a leap day
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidNanoOfDayChanges")
        @DisplayName("throws exception for invalid nano-of-day")
        void withNanoOfDay_withInvalidNanos_throwsException(long initialMjd, long initialNanos, long invalidNewNanos) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            assertThrows(DateTimeException.class, () -> instant.withNanoOfDay(invalidNewNanos));
        }
    }

    @Nested
    @DisplayName("Arithmetic with plus() and minus()")
    class ArithmeticTests {

        static Stream<Arguments> providePlusDurations() {
            // Arguments: initialMjd, initialNanos, secondsToAdd, nanosToAdd, expectedMjd, expectedNanos
            return Stream.of(
                // --- Tests starting from MJD 0, nano 0 ---
                Arguments.of(0, 0, -2 * SECS_PER_DAY, 5, -2, 5), // Subtract days
                Arguments.of(0, 0, -1 * SECS_PER_DAY, 0, -1, 0),
                Arguments.of(0, 0, 0, -1, -1, NANOS_PER_DAY - 1), // Subtract nanos, crossing day boundary
                Arguments.of(0, 0, 0, 0, 0, 0), // Add zero
                Arguments.of(0, 0, 0, 1, 0, 1), // Add nanos
                Arguments.of(0, 0, 1 * SECS_PER_DAY, 1, 1, 1), // Add days
                // --- Tests starting from MJD 1, nano 0 ---
                Arguments.of(1, 0, -2 * SECS_PER_DAY, 5, -1, 5),
                Arguments.of(1, 0, -1 * SECS_PER_DAY, 0, 0, 0),
                Arguments.of(1, 0, 0, -1, 0, NANOS_PER_DAY - 1),
                Arguments.of(1, 0, 0, 0, 1, 0),
                Arguments.of(1, 0, 1 * SECS_PER_DAY, 1, 2, 1)
            );
        }

        @ParameterizedTest
        @MethodSource("providePlusDurations")
        @DisplayName("plus() adds duration correctly")
        void plus_addsDurationCorrectly(long initialMjd, long initialNanos, long secondsToAdd, int nanosToAdd, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            UtcInstant result = initial.plus(Duration.ofSeconds(secondsToAdd, nanosToAdd));

            assertAll(
                () -> assertEquals(expectedMjd, result.getModifiedJulianDay(), "MJD mismatch"),
                () -> assertEquals(expectedNanos, result.getNanoOfDay(), "Nano-of-day mismatch")
            );
        }

        static Stream<Arguments> provideMinusDurations() {
            // Arguments: initialMjd, initialNanos, secondsToSubtract, nanosToSubtract, expectedMjd, expectedNanos
            return Stream.of(
                // --- Tests starting from MJD 0, nano 0 ---
                Arguments.of(0, 0, 2 * SECS_PER_DAY, -5, -2, 5), // Subtract days
                Arguments.of(0, 0, 1 * SECS_PER_DAY, 0, -1, 0),
                Arguments.of(0, 0, 0, 1, -1, NANOS_PER_DAY - 1), // Subtract nanos, crossing day boundary
                Arguments.of(0, 0, 0, 0, 0, 0), // Subtract zero
                Arguments.of(0, 0, 0, -1, 0, 1), // Subtract negative nanos
                Arguments.of(0, 0, -1 * SECS_PER_DAY, -1, 1, 1), // Subtract negative days
                // --- Tests starting from MJD 1, nano 0 ---
                Arguments.of(1, 0, 2 * SECS_PER_DAY, -5, -1, 5),
                Arguments.of(1, 0, 1 * SECS_PER_DAY, 0, 0, 0),
                Arguments.of(1, 0, 0, 1, 0, NANOS_PER_DAY - 1),
                Arguments.of(1, 0, 0, 0, 1, 0),
                Arguments.of(1, 0, -1 * SECS_PER_DAY, -1, 2, 1)
            );
        }

        @ParameterizedTest
        @MethodSource("provideMinusDurations")
        @DisplayName("minus() subtracts duration correctly")
        void minus_subtractsDurationCorrectly(long initialMjd, long initialNanos, long secondsToSubtract, int nanosToSubtract, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            UtcInstant result = initial.minus(Duration.ofSeconds(secondsToSubtract, nanosToSubtract));

            assertAll(
                () -> assertEquals(expectedMjd, result.getModifiedJulianDay(), "MJD mismatch"),
                () -> assertEquals(expectedNanos, result.getNanoOfDay(), "Nano-of-day mismatch")
            );
        }
    }

    @Nested
    @DisplayName("String Conversion and Parsing")
    class StringConversionTests {

        static Stream<Arguments> provideToStringAndParseCases() {
            return Stream.of(
                Arguments.of(40587, 0, "1970-01-01T00:00:00Z"),
                Arguments.of(40588, 1, "1970-01-02T00:00:00.000000001Z"),
                Arguments.of(40588, 999, "1970-01-02T00:00:00.000000999Z"),
                Arguments.of(40588, 1000, "1970-01-02T00:00:00.000001Z"),
                Arguments.of(40618, 999999999, "1970-02-01T00:00:00.999999999Z"),
                Arguments.of(40619, NANOS_PER_SEC, "1970-02-02T00:00:01Z"),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"), // Leap second
                Arguments.of(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z")
            );
        }

        @ParameterizedTest
        @MethodSource("provideToStringAndParseCases")
        @DisplayName("toString() returns correctly formatted ISO string")
        void toString_returnsCorrectlyFormattedString(long mjd, long nanoOfDay, String expectedString) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
            assertEquals(expectedString, instant.toString());
        }

        @ParameterizedTest
        @MethodSource("provideToStringAndParseCases")
        @DisplayName("parse() correctly reconstructs an instant from its string representation")
        void parse_ofValidString_recreatesEqualInstant(long mjd, long nanoOfDay, String stringRepresentation) {
            UtcInstant expectedInstant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
            UtcInstant parsedInstant = UtcInstant.parse(stringRepresentation);
            assertEquals(expectedInstant, parsedInstant);
        }
    }
}