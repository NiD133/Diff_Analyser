package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

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

@DisplayName("UtcInstant Modification, Arithmetic, and Formatting")
public class UtcInstantModificationAndArithmeticTest {

    // A non-leap day, Dec 30, 1972
    private static final long MJD_1972_12_30 = 41681;
    // A leap day, Dec 31, 1972, which had a leap second
    private static final long MJD_1972_12_31_LEAP = 41682;
    // A non-leap day, Jan 01, 1973
    private static final long MJD_1973_01_01 = 41683;
    // A leap day, Dec 31, 1973, which had a leap second
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    // A leap day has one extra second
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    @Nested
    @DisplayName("Factory: parse(CharSequence)")
    class Parse {

        @ParameterizedTest
        @ValueSource(strings = {"", "A", "2012-13-01T00:00:00Z"})
        void parse_withInvalidString_throwsException(String invalidString) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
        }
    }

    @Nested
    @DisplayName("Method: withModifiedJulianDay(long)")
    class WithModifiedJulianDay {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantModificationAndArithmeticTest#validMjdChanges")
        void returnsUpdatedInstant_forValidDay(
                long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            UtcInstant result = initialInstant.withModifiedJulianDay(newMjd);
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantModificationAndArithmeticTest#invalidMjdChanges")
        void throwsException_forInvalidDay(long initialMjd, long initialNanos, long newMjd) {
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            assertThrows(DateTimeException.class, () -> initialInstant.withModifiedJulianDay(newMjd));
        }
    }

    @Nested
    @DisplayName("Method: withNanoOfDay(long)")
    class WithNanoOfDay {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantModificationAndArithmeticTest#validNanoOfDayChanges")
        void returnsUpdatedInstant_forValidNanoOfDay(
                long mjd, long initialNanos, long newNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
            UtcInstant result = initialInstant.withNanoOfDay(newNanos);
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantModificationAndArithmeticTest#invalidNanoOfDayChanges")
        void throwsException_forInvalidNanoOfDay(long mjd, long initialNanos, long newNanos) {
            UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
            assertThrows(DateTimeException.class, () -> initialInstant.withNanoOfDay(newNanos));
        }
    }

    @Nested
    @DisplayName("Method: plus(Duration)")
    class PlusDuration {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantModificationAndArithmeticTest#plusCases")
        void addsDurationCorrectly(
                long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            Duration duration = Duration.ofSeconds(plusSeconds, plusNanos);
            UtcInstant result = initial.plus(duration);
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }
    }

    @Nested
    @DisplayName("Method: minus(Duration)")
    class MinusDuration {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantModificationAndArithmeticTest#minusCases")
        void subtractsDurationCorrectly(
                long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            Duration duration = Duration.ofSeconds(minusSeconds, minusNanos);
            UtcInstant result = initial.minus(duration);
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }
    }

    @Nested
    @DisplayName("Comparison Methods")
    class Comparisons {

        @Test
        void comparisonMethods_workCorrectly() {
            UtcInstant instant1 = UtcInstant.ofModifiedJulianDay(-1, NANOS_PER_DAY - 1);
            UtcInstant instant2 = UtcInstant.ofModifiedJulianDay(0, 0);
            UtcInstant instant3 = UtcInstant.ofModifiedJulianDay(0, 1);
            UtcInstant instant4 = UtcInstant.ofModifiedJulianDay(1, 0);

            assertChronologicalOrder(instant1, instant2, instant3, instant4);
        }

        private void assertChronologicalOrder(UtcInstant... instants) {
            for (int i = 0; i < instants.length; i++) {
                for (int j = 0; j < instants.length; j++) {
                    UtcInstant a = instants[i];
                    UtcInstant b = instants[j];
                    if (i < j) {
                        assertTrue(a.isBefore(b), a + " should be before " + b);
                        assertFalse(a.isAfter(b), a + " should not be after " + b);
                        assertEquals(-1, a.compareTo(b), a + " compareTo " + b + " should be -1");
                        assertNotEquals(a, b);
                    } else if (i > j) {
                        assertFalse(a.isBefore(b), a + " should not be before " + b);
                        assertTrue(a.isAfter(b), a + " should be after " + b);
                        assertEquals(1, a.compareTo(b), a + " compareTo " + b + " should be 1");
                        assertNotEquals(a, b);
                    } else {
                        assertFalse(a.isBefore(b), a + " should not be before " + b);
                        assertFalse(a.isAfter(b), a + " should not be after " + b);
                        assertEquals(0, a.compareTo(b), a + " compareTo " + b + " should be 0");
                        assertEquals(a, b);
                    }
                }
            }
        }
    }

    @Nested
    @DisplayName("String Conversion")
    class StringConversion {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantModificationAndArithmeticTest#toStringAndParseCases")
        void toString_returnsIso8601Format(long mjd, long nanoOfDay, String expectedString) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
            assertEquals(expectedString, instant.toString());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantModificationAndArithmeticTest#toStringAndParseCases")
        void parse_reversesToString(long mjd, long nanoOfDay, String stringRepresentation) {
            UtcInstant expectedInstant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
            UtcInstant parsedInstant = UtcInstant.parse(stringRepresentation);
            assertEquals(expectedInstant, parsedInstant);
        }
    }

    @Nested
    @DisplayName("Factory: of(TaiInstant)")
    class FactoryOfTaiInstant {
        @Test
        void of_givenNull_throwsException() {
            assertThrows(NullPointerException.class, () -> UtcInstant.of((TaiInstant) null));
        }
    }

    // --- Data Providers ---

    static Stream<Arguments> validMjdChanges() {
        return Stream.of(
            arguments(0L, 12345L, 1L, 1L, 12345L),
            arguments(0L, 12345L, -1L, -1L, 12345L),
            // Change from one leap day to another, preserving nano-of-day
            arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY),
            // Change to the same day
            arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    static Stream<Arguments> invalidMjdChanges() {
        return Stream.of(
            // Nano-of-day is valid for a leap day, but not for a standard day
            arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
            arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
        );
    }

    static Stream<Arguments> validNanoOfDayChanges() {
        return Stream.of(
            arguments(0L, 12345L, 1L, 0L, 1L),
            // Set to max nano on a standard day
            arguments(MJD_1972_12_30, 0, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
            // Set to max nano on a leap day (end of 23:59:59)
            arguments(MJD_1972_12_31_LEAP, 0, NANOS_PER_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1),
            // Set to the leap second nano on a leap day (23:59:60)
            arguments(MJD_1972_12_31_LEAP, 0, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
            // Set to max nano on a leap day
            arguments(MJD_1972_12_31_LEAP, 0, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1)
        );
    }

    static Stream<Arguments> invalidNanoOfDayChanges() {
        return Stream.of(
            arguments(0L, 12345L, -1L), // Negative nano is invalid
            // Set to leap second nano on a non-leap day
            arguments(MJD_1972_12_30, 0, NANOS_PER_DAY),
            arguments(MJD_1973_01_01, 0, NANOS_PER_DAY),
            // Set to nano > max nano on a non-leap day
            arguments(MJD_1972_12_30, 0, NANOS_PER_LEAP_DAY - 1),
            // Set to nano > max nano on a leap day
            arguments(MJD_1972_12_31_LEAP, 0, NANOS_PER_LEAP_DAY)
        );
    }

    static Stream<Arguments> plusCases() {
        return Stream.of(
            // Simple additions, no day change
            arguments(0, 0, 0, 0, 0, 0),
            arguments(0, 0, 0, 1, 0, 1),
            arguments(0, 0, 1, 0, 0, NANOS_PER_SEC),
            // Subtraction resulting in previous day
            arguments(0, 0, 0, -1, -1, NANOS_PER_DAY - 1),
            // Addition resulting in next day
            arguments(0, 0, SECS_PER_DAY, 0, 1, 0),
            arguments(0, 0, SECS_PER_DAY, 1, 1, 1),
            // Subtraction resulting in day before previous
            arguments(0, 0, -SECS_PER_DAY, 0, -1, 0),
            arguments(1, 0, -SECS_PER_DAY, 0, 0, 0)
        );
    }

    static Stream<Arguments> minusCases() {
        return Stream.of(
            // Simple subtractions, no day change
            arguments(1, 1, 0, 1, 1, 0),
            arguments(1, NANOS_PER_SEC, 1, 0, 1, 0),
            // Subtraction resulting in previous day
            arguments(1, 0, 0, 1, 0, NANOS_PER_DAY - 1),
            arguments(1, 0, SECS_PER_DAY, 0, 0, 0),
            // Addition (minus negative) resulting in next day
            arguments(1, 0, -SECS_PER_DAY, 0, 2, 0)
        );
    }

    static Stream<Arguments> toStringAndParseCases() {
        return Stream.of(
            arguments(40587, 0, "1970-01-01T00:00:00Z"),
            arguments(40588, 1, "1970-01-02T00:00:00.000000001Z"),
            arguments(40588, 999, "1970-01-02T00:00:00.000000999Z"),
            arguments(40588, 1000, "1970-01-02T00:00:00.000001Z"),
            arguments(40618, 999999999, "1970-02-01T00:00:00.999999999Z"),
            arguments(40619, NANOS_PER_SEC, "1970-02-02T00:00:01Z"),
            // Leap second representation
            arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
            // Day after leap second
            arguments(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z")
        );
    }
}