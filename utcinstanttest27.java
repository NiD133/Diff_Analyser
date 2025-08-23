package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
 * Test class for {@link UtcInstant}.
 * This version focuses on understandability through structure and clear naming.
 */
class UtcInstantTest {

    // Constants for common date and time values
    private static final long MJD_1972_12_30 = 41681;
    private static final long MJD_1972_12_31_LEAP = 41682; // A day with a leap second
    private static final long MJD_1973_01_01 = 41683;
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365; // Not a leap day in reality, but used for testing

    private static final long SECS_PER_DAY = 86400L;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    @Nested
    @DisplayName("Factory and Parsing Tests")
    class FactoryAndParsing {

        static Stream<Arguments> provider_invalidParseStrings() {
            return Stream.of(
                Arguments.of(""),                         // Empty string
                Arguments.of("A"),                        // Not a date-time
                Arguments.of("2012-13-01T00:00:00Z")      // Invalid month
            );
        }

        @ParameterizedTest
        @MethodSource("provider_invalidParseStrings")
        void parse_withInvalidFormat_throwsDateTimeException(String invalidString) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
        }
    }

    @Nested
    @DisplayName("Field-based Adjuster Tests")
    class WithAdjusters {

        // --- withModifiedJulianDay ---

        static Stream<Arguments> provider_withModifiedJulianDay_valid() {
            return Stream.of(
                // Simple cases, nano-of-day is unaffected
                Arguments.of(0L, 12345L, 1L, 1L, 12345L),
                Arguments.of(7L, 12345L, 2L, 2L, 12345L),
                Arguments.of(-99L, 12345L, -3L, -3L, 12345L),
                // Change to a day that can hold the existing nano-of-day
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_withModifiedJulianDay_valid")
        void withModifiedJulianDay_givenValidDay_returnsUpdatedInstant(
            long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {
            // Given
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);

            // When
            UtcInstant result = initialInstant.withModifiedJulianDay(newMjd);

            // Then
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        static Stream<Arguments> provider_withModifiedJulianDay_invalid() {
            return Stream.of(
                // Start on a leap day with a nano-of-day value that is only valid on that leap day.
                // Then, try to switch to a non-leap day, which cannot contain that nano-of-day.
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30), // Previous day
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)  // Next day
            );
        }

        @ParameterizedTest
        @MethodSource("provider_withModifiedJulianDay_invalid")
        void withModifiedJulianDay_givenInvalidDayForNanos_throwsDateTimeException(
            long initialMjd, long initialNanos, long invalidNewMjd) {
            // Given
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);

            // When & Then
            assertThrows(DateTimeException.class, () -> initialInstant.withModifiedJulianDay(invalidNewMjd));
        }

        // --- withNanoOfDay ---

        static Stream<Arguments> provider_withNanoOfDay_valid() {
            return Stream.of(
                // Simple cases
                Arguments.of(0L, 12345L, 1L, 0L, 1L),
                Arguments.of(7L, 12345L, 2L, 7L, 2L),
                // Set to max valid nanos on a non-leap day
                Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
                // Set to max valid nanos on a leap day
                Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1),
                // Set to nano value corresponding to the leap second itself
                Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_withNanoOfDay_valid")
        void withNanoOfDay_givenValidNanos_returnsUpdatedInstant(
            long initialMjd, long initialNanos, long newNanos, long expectedMjd, long expectedNanos) {
            // Given
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);

            // When
            UtcInstant result = initialInstant.withNanoOfDay(newNanos);

            // Then
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        static Stream<Arguments> provider_withNanoOfDay_invalid() {
            return Stream.of(
                // Negative nanos are never valid
                Arguments.of(0L, 12345L, -1L),
                // Nanos greater than or equal to a standard day's length on a non-leap day
                Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY),
                Arguments.of(MJD_1973_01_01, 0L, NANOS_PER_DAY),
                // Nanos greater than or equal to a leap day's length on a leap day
                Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_withNanoOfDay_invalid")
        void withNanoOfDay_givenInvalidNanosForDay_throwsDateTimeException(
            long initialMjd, long initialNanos, long invalidNewNanos) {
            // Given
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);

            // When & Then
            assertThrows(DateTimeException.class, () -> initialInstant.withNanoOfDay(invalidNewNanos));
        }
    }

    @Nested
    @DisplayName("Arithmetic Tests")
    class Arithmetic {

        static Stream<Arguments> provider_plus() {
            // initialMjd, initialNanos, plusSeconds, plusNanos, expectedMjd, expectedNanos
            return Stream.of(
                // --- Zero duration ---
                Arguments.of(0, 0, 0, 0, 0, 0),
                // --- Add small amounts, no day change ---
                Arguments.of(0, 0, 0, 1, 0, 1),
                Arguments.of(0, 0, 2, 333, 0, 2 * NANOS_PER_SEC + 333),
                // --- Add amounts that cross day boundary ---
                Arguments.of(0, 0, SECS_PER_DAY, 1, 1, 1),
                Arguments.of(0, 0, 2 * SECS_PER_DAY, 5, 2, 5),
                // --- Subtract small amounts, crossing day boundary ---
                Arguments.of(0, 0, 0, -1, -1, NANOS_PER_DAY - 1),
                Arguments.of(0, 0, 0, -2, -1, NANOS_PER_DAY - 2),
                // --- Subtract amounts that cross day boundary ---
                Arguments.of(0, 0, -1 * SECS_PER_DAY, 0, -1, 0),
                Arguments.of(1, 0, -1 * SECS_PER_DAY, 0, 0, 0)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_plus")
        void plus_duration_returnsCorrectlyCalculatedInstant(
            long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
            // Given
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            Duration durationToAdd = Duration.ofSeconds(plusSeconds, plusNanos);

            // When
            UtcInstant result = initial.plus(durationToAdd);

            // Then
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        static Stream<Arguments> provider_minus() {
            // initialMjd, initialNanos, minusSeconds, minusNanos, expectedMjd, expectedNanos
            return Stream.of(
                // --- Zero duration ---
                Arguments.of(0, 0, 0, 0, 0, 0),
                // --- Subtract small amounts, no day change ---
                Arguments.of(0, 2, 0, 1, 0, 1),
                Arguments.of(1, 3 * NANOS_PER_SEC, 2, 333, 1, 1 * NANOS_PER_SEC - 333),
                // --- Subtract amounts that cross day boundary ---
                Arguments.of(1, 0, SECS_PER_DAY, 0, 0, 0),
                Arguments.of(2, 5, 2 * SECS_PER_DAY, 5, 0, 0),
                // --- Add small amounts (subtracting negative), crossing day boundary ---
                Arguments.of(0, NANOS_PER_DAY - 1, 0, -1, 1, 0),
                Arguments.of(0, NANOS_PER_DAY - 2, 0, -2, 1, 0),
                // --- Add amounts (subtracting negative) that cross day boundary ---
                Arguments.of(0, 0, -1 * SECS_PER_DAY, 0, 1, 0),
                Arguments.of(0, 0, -2 * SECS_PER_DAY, -5, 2, 5)
            );
        }

        @ParameterizedTest
        @MethodSource("provider_minus")
        void minus_duration_returnsCorrectlyCalculatedInstant(
            long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
            // Given
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            Duration durationToSubtract = Duration.ofSeconds(minusSeconds, minusNanos);

            // When
            UtcInstant result = initial.minus(durationToSubtract);

            // Then
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }
    }

    @Nested
    @DisplayName("Comparison Tests")
    class ComparisonTests {

        @Test
        void compareTo_isAfter_isBefore_areConsistent() {
            // Given an ordered list of instants
            UtcInstant[] orderedInstants = new UtcInstant[]{
                UtcInstant.ofModifiedJulianDay(-2L, 0),
                UtcInstant.ofModifiedJulianDay(-2L, NANOS_PER_DAY - 2),
                UtcInstant.ofModifiedJulianDay(-2L, NANOS_PER_DAY - 1),
                UtcInstant.ofModifiedJulianDay(-1L, 0),
                UtcInstant.ofModifiedJulianDay(-1L, 1),
                UtcInstant.ofModifiedJulianDay(-1L, NANOS_PER_DAY - 2),
                UtcInstant.ofModifiedJulianDay(-1L, NANOS_PER_DAY - 1),
                UtcInstant.ofModifiedJulianDay(0L, 0),
                UtcInstant.ofModifiedJulianDay(0L, 1),
                UtcInstant.ofModifiedJulianDay(0L, 2),
                UtcInstant.ofModifiedJulianDay(0L, NANOS_PER_DAY - 1),
                UtcInstant.ofModifiedJulianDay(1L, 0),
                UtcInstant.ofModifiedJulianDay(2L, 0)
            };

            // When & Then: verify all pairwise comparisons
            assertOrdering(orderedInstants);
        }

        /**
         * Helper to assert that a list of instants is correctly ordered and that
         * comparison methods are consistent.
         */
        private void assertOrdering(UtcInstant... instants) {
            for (int i = 0; i < instants.length; i++) {
                UtcInstant a = instants[i];
                for (int j = 0; j < instants.length; j++) {
                    UtcInstant b = instants[j];
                    if (i < j) {
                        assertTrue(a.compareTo(b) < 0, a + " should be less than " + b);
                        assertTrue(a.isBefore(b), a + ".isBefore(" + b + ")");
                        assertFalse(a.isAfter(b), "!" + a + ".isAfter(" + b + ")");
                        assertFalse(a.equals(b), "!" + a + ".equals(" + b + ")");
                    } else if (i > j) {
                        assertTrue(a.compareTo(b) > 0, a + " should be greater than " + b);
                        assertFalse(a.isBefore(b), "!" + a + ".isBefore(" + b + ")");
                        assertTrue(a.isAfter(b), a + ".isAfter(" + b + ")");
                        assertFalse(a.equals(b), "!" + a + ".equals(" + b + ")");
                    } else {
                        assertEquals(0, a.compareTo(b), a + " should be equal to " + b);
                        assertFalse(a.isBefore(b), "!" + a + ".isBefore(" + b + ")");
                        assertFalse(a.isAfter(b), "!" + a + ".isAfter(" + b + ")");
                        assertTrue(a.equals(b), a + ".equals(" + b + ")");
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("String Conversion and Parsing")
    class StringConversion {

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
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"), // The leap second
                Arguments.of(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z")
            );
        }

        @ParameterizedTest
        @MethodSource("provider_toStringCases")
        void toString_returnsCorrectIsoFormat(long mjd, long nanoOfDay, String expectedString) {
            // Given
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
            
            // When
            String actualString = instant.toString();

            // Then
            assertEquals(expectedString, actualString);
        }

        @ParameterizedTest
        @MethodSource("provider_toStringCases")
        void parse_isInverseOfToString(long mjd, long nanoOfDay, String isoString) {
            // Given
            UtcInstant expectedInstant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);

            // When
            UtcInstant parsedInstant = UtcInstant.parse(isoString);

            // Then
            assertEquals(expectedInstant, parsedInstant);
        }
    }
}