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
 * This class focuses on creation, modification, and arithmetic operations.
 */
public class UtcInstantTest {

    // A day with a leap second (December 31, 1972)
    private static final long MJD_1972_12_31_LEAP = 41682;
    // A day before the leap second day
    private static final long MJD_1972_12_30 = MJD_1972_12_31_LEAP - 1;
    // A day after the leap second day
    private static final long MJD_1973_01_01 = MJD_1972_12_31_LEAP + 1;
    // Another leap day, one year later
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    // parse(CharSequence) - Invalid
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @ValueSource(strings = {"", "A", "2012-13-01T00:00:00Z"})
    @DisplayName("parse() should throw exception for invalid formats")
    public void parse_withInvalidFormat_shouldThrowException(String invalidString) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withModifiedJulianDay()
    //-----------------------------------------------------------------------
    public static Stream<Arguments> data_withModifiedJulianDay_valid() {
        return Stream.of(
            // initialMjd, initialNanos, newMjd, expectedMjd, expectedNanos
            Arguments.of(0L, 12345L, 1L, 1L, 12345L),
            Arguments.of(0L, 12345L, -1L, -1L, 12345L),
            // On a leap day, setting MJD to the same day is valid
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
            // On a leap day, setting MJD to another leap day is valid
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("data_withModifiedJulianDay_valid")
    @DisplayName("withModifiedJulianDay() should set a valid new day")
    public void withModifiedJulianDay_shouldSetNewDay(long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        UtcInstant result = initial.withModifiedJulianDay(newMjd);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    public static Stream<Arguments> data_withModifiedJulianDay_invalid() {
        return Stream.of(
            // initialMjd, initialNanos, newMjd (invalid)
            // On a leap day, setting MJD to a non-leap day is invalid because nanoOfDay is too large
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
        );
    }

    @ParameterizedTest
    @MethodSource("data_withModifiedJulianDay_invalid")
    @DisplayName("withModifiedJulianDay() should throw exception for an invalid day")
    public void withModifiedJulianDay_forInvalidDay_shouldThrowException(long initialMjd, long initialNanos, long invalidNewMjd) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(invalidNewMjd));
    }

    //-----------------------------------------------------------------------
    // withNanoOfDay()
    //-----------------------------------------------------------------------
    public static Stream<Arguments> data_withNanoOfDay_valid() {
        return Stream.of(
            // mjd, initialNanos, newNanoOfDay, expectedMjd, expectedNanos
            Arguments.of(0L, 12345L, 1L, 0L, 1L),
            Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
            // On a leap day, setting nano to the leap second nano is valid
            Arguments.of(MJD_1972_12_31_LEAP, 0, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
            // On a leap day, setting nano up to the max for a leap day is valid
            Arguments.of(MJD_1972_12_31_LEAP, 0, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1)
        );
    }

    @ParameterizedTest
    @MethodSource("data_withNanoOfDay_valid")
    @DisplayName("withNanoOfDay() should set valid new nanoseconds")
    public void withNanoOfDay_shouldSetNewNanos(long mjd, long initialNanos, long newNanoOfDay, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
        UtcInstant result = initial.withNanoOfDay(newNanoOfDay);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    public static Stream<Arguments> data_withNanoOfDay_invalid() {
        return Stream.of(
            // mjd, initialNanos, invalidNanoOfDay
            Arguments.of(0L, 12345L, -1L), // Negative nanos are invalid
            Arguments.of(MJD_1972_12_30, 0, NANOS_PER_DAY), // Too large for a standard day
            Arguments.of(MJD_1973_01_01, 0, NANOS_PER_DAY), // Too large for a standard day
            Arguments.of(MJD_1972_12_30, 0, NANOS_PER_LEAP_DAY - 1), // Too large for a standard day
            Arguments.of(MJD_1972_12_31_LEAP, 0, NANOS_PER_LEAP_DAY) // Too large, even for a leap day
        );
    }

    @ParameterizedTest
    @MethodSource("data_withNanoOfDay_invalid")
    @DisplayName("withNanoOfDay() should throw exception for invalid nanoseconds")
    public void withNanoOfDay_forInvalidNanos_shouldThrowException(long mjd, long initialNanos, long invalidNanoOfDay) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
        assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(invalidNanoOfDay));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    public static Object[][] data_plus() {
        return new Object[][] {
            // initialMjd, initialNanos, plusSeconds, plusNanos, expectedMjd, expectedNanos
            // Additions from MJD=0, Nanos=0
            { 0, 0, -2 * SECS_PER_DAY, 5, -2, 5 },
            { 0, 0, -1 * SECS_PER_DAY, 1, -1, 1 },
            { 0, 0, 0, -1, -1, NANOS_PER_DAY - 1 }, // Subtract nanos, roll back day
            { 0, 0, 0, 0, 0, 0 },                   // Add zero
            { 0, 0, 0, 1, 0, 1 },                   // Add one nano
            { 0, 0, 1, 333, 0, NANOS_PER_SEC + 333 },
            { 0, 0, 1 * SECS_PER_DAY, 1, 1, 1 },

            // Additions from MJD=1, Nanos=0
            { 1, 0, -2 * SECS_PER_DAY, 5, -1, 5 },
            { 1, 0, -1 * SECS_PER_DAY, 0, 0, 0 },
            { 1, 0, 0, -1, 0, NANOS_PER_DAY - 1 }, // Subtract nanos, roll back day
            { 1, 0, 0, 0, 1, 0 },                   // Add zero
            { 1, 0, 1 * SECS_PER_DAY, 1, 2, 1 },
        };
    }

    @ParameterizedTest
    @MethodSource("data_plus")
    @DisplayName("plus() should add duration correctly")
    public void plus_shouldAddDurationCorrectly(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration toAdd = Duration.ofSeconds(plusSeconds, plusNanos);

        UtcInstant result = initial.plus(toAdd);

        assertEquals(expectedMjd, result.getModifiedJulianDay(), "Modified Julian Day should match");
        assertEquals(expectedNanos, result.getNanoOfDay(), "Nano of day should match");
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    public static Object[][] data_minus() {
        return new Object[][] {
            // initialMjd, initialNanos, minusSeconds, minusNanos, expectedMjd, expectedNanos
            // Subtractions from MJD=0, Nanos=0
            { 0, 0, 2 * SECS_PER_DAY, -5, -2, 5 },
            { 0, 0, 1 * SECS_PER_DAY, -1, -1, 1 },
            { 0, 0, 0, 1, -1, NANOS_PER_DAY - 1 }, // Subtract nanos, roll back day
            { 0, 0, 0, 0, 0, 0 },                   // Subtract zero
            { 0, 0, 0, -1, 0, 1 },                   // Subtract negative nano (add)
            { 0, 0, -1, -333, 0, NANOS_PER_SEC + 333 },
            { 0, 0, -1 * SECS_PER_DAY, -1, 1, 1 },

            // Subtractions from MJD=1, Nanos=0
            { 1, 0, 2 * SECS_PER_DAY, -5, -1, 5 },
            { 1, 0, 1 * SECS_PER_DAY, 0, 0, 0 },
            { 1, 0, 0, 1, 0, NANOS_PER_DAY - 1 }, // Subtract nanos, roll back day
            { 1, 0, 0, 0, 1, 0 },                   // Subtract zero
            { 1, 0, -1 * SECS_PER_DAY, -1, 2, 1 },
        };
    }

    @ParameterizedTest
    @MethodSource("data_minus")
    @DisplayName("minus() should subtract duration correctly")
    public void minus_shouldSubtractDurationCorrectly(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration toSubtract = Duration.ofSeconds(minusSeconds, minusNanos);

        UtcInstant result = initial.minus(toSubtract);

        assertEquals(expectedMjd, result.getModifiedJulianDay(), "Modified Julian Day should match");
        assertEquals(expectedNanos, result.getNanoOfDay(), "Nano of day should match");
    }

    //-----------------------------------------------------------------------
    // toString() and parse()
    //-----------------------------------------------------------------------
    public static Object[][] data_toString() {
        return new Object[][] {
            { 40587L, 0L, "1970-01-01T00:00:00Z" },
            { 40588L, 1L, "1970-01-02T00:00:00.000000001Z" },
            { 40588L, 999_999_999L, "1970-01-02T00:00:00.999999999Z" },
            { 40588L, 1_000_000_000L, "1970-01-02T00:00:01Z" },
            // Test case for the second before a leap second
            { MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z" },
            // Test case for the leap second itself (23:59:60)
            { MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z" },
            // Test case for the second after a leap second
            { MJD_1973_01_01, 0L, "1973-01-01T00:00:00Z" },
        };
    }

    @ParameterizedTest
    @MethodSource("data_toString")
    @DisplayName("toString() should return correct ISO-8601 format")
    public void toString_shouldReturnIsoFormat(long mjd, long nod, String expected) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nod);
        assertEquals(expected, instant.toString());
    }

    @ParameterizedTest
    @MethodSource("data_toString")
    @DisplayName("parse() should correctly reverse toString()")
    public void parse_shouldRevertToString(long mjd, long nod, String isoString) {
        UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nod);
        UtcInstant parsed = UtcInstant.parse(isoString);
        assertEquals(expected, parsed);
    }

    @Test
    @DisplayName("parse() should handle leap second string correctly")
    public void parse_shouldHandleLeapSecondCorrectly() {
        UtcInstant expectedBeforeLeap = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC);
        assertEquals(expectedBeforeLeap, UtcInstant.parse("1972-12-31T23:59:59Z"));

        UtcInstant expectedOnLeap = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY);
        assertEquals(expectedOnLeap, UtcInstant.parse("1972-12-31T23:59:60Z"));
    }
}