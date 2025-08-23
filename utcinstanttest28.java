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
 */
public class UtcInstantTest {

    // Modified Julian Day for 1972-12-30 (a normal day)
    private static final long MJD_1972_12_30 = 41681;
    // Modified Julian Day for 1972-12-31 (a leap-second day)
    private static final long MJD_1972_12_31_LEAP = 41682;
    // Modified Julian Day for 1973-01-01 (a normal day)
    private static final long MJD_1973_01_01 = 41683;
    // Modified Julian Day for 1973-12-31 (a leap-second day)
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @DisplayName("parse() should throw DateTimeException for invalid input")
    @ParameterizedTest(name = "Invalid input: \"{0}\"")
    @ValueSource(strings = {
        "",                     // Empty string
        "A",                    // Not a date-time
        "2012-13-01T00:00:00Z"  // Invalid month
    })
    void parse_throwsExceptionForInvalidFormat(String invalidInput) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidInput));
    }

    //-----------------------------------------------------------------------
    // withModifiedJulianDay()
    //-----------------------------------------------------------------------
    @DisplayName("withModifiedJulianDay() should update the day when valid")
    @ParameterizedTest(name = "MJD {0}, nanos {1} -> new MJD {2} -> MJD {2}, nanos {3}")
    @MethodSource("validModifiedJulianDayProvider")
    void withModifiedJulianDay_updatesDayWhenValid(
            long initialMjd, long initialNanos, long newMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        UtcInstant result = initial.withModifiedJulianDay(newMjd);

        assertEquals(newMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    static Stream<Arguments> validModifiedJulianDayProvider() {
        return Stream.of(
            // Simple cases, nanoOfDay is preserved
            Arguments.of(0L, 12345L, 1L, 12345L),
            Arguments.of(0L, 12345L, -1L, 12345L),
            // Leap day to same leap day, nanoOfDay at leap second is preserved
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
            // Leap day to another leap day, nanoOfDay at leap second is preserved
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @DisplayName("withModifiedJulianDay() should throw exception for invalid resulting nano-of-day")
    @ParameterizedTest(name = "MJD {0}, nanos {1}, new MJD {2} -> throws")
    @MethodSource("invalidModifiedJulianDayProvider")
    void withModifiedJulianDay_throwsExceptionForInvalidNanoOfDay(
            long initialMjd, long initialNanos, long newMjd) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjd));
    }

    static Stream<Arguments> invalidModifiedJulianDayProvider() {
        return Stream.of(
            // From a leap day with 60th second, to a non-leap day
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
        );
    }

    //-----------------------------------------------------------------------
    // withNanoOfDay()
    //-----------------------------------------------------------------------
    @DisplayName("withNanoOfDay() should update the nano-of-day when valid")
    @ParameterizedTest(name = "MJD {0}, new nanos {2} -> MJD {0}, nanos {2}")
    @MethodSource("validNanoOfDayProvider")
    void withNanoOfDay_updatesNanoOfDayWhenValid(long mjd, long initialNanos, long newNanoOfDay) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
        UtcInstant result = initial.withNanoOfDay(newNanoOfDay);

        assertEquals(mjd, result.getModifiedJulianDay());
        assertEquals(newNanoOfDay, result.getNanoOfDay());
    }

    static Stream<Arguments> validNanoOfDayProvider() {
        return Stream.of(
            // Simple cases
            Arguments.of(0L, 12345L, 1L),
            // To end of a normal day
            Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1),
            // To end of a leap day (before leap second)
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1),
            // To the leap second on a leap day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY),
            // To end of a leap day (including leap second)
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY - 1)
        );
    }

    @DisplayName("withNanoOfDay() should throw exception for invalid nano-of-day")
    @ParameterizedTest(name = "MJD {0}, new nanos {2} -> throws")
    @MethodSource("invalidNanoOfDayProvider")
    void withNanoOfDay_throwsExceptionForInvalidNanoOfDay(long mjd, long initialNanos, long newNanoOfDay) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
        assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(newNanoOfDay));
    }

    static Stream<Arguments> invalidNanoOfDayProvider() {
        return Stream.of(
            // Negative nano-of-day is always invalid
            Arguments.of(0L, 12345L, -1L),
            // Exceeding nanoseconds on a normal day
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY),
            Arguments.of(MJD_1973_01_01, 0L, NANOS_PER_DAY),
            // Exceeding nanoseconds on a normal day (trying to set leap day value)
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_LEAP_DAY - 1),
            // Exceeding nanoseconds on a leap day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY)
        );
    }

    //-----------------------------------------------------------------------
    // plus()
    //-----------------------------------------------------------------------
    @DisplayName("plus() should add a duration correctly")
    @ParameterizedTest(name = "[{index}] {0}d + {1}ns plus {2}s, {3}ns -> {4}d + {5}ns")
    @MethodSource("plusDurationProvider")
    void plus_addsDurationCorrectly(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration durationToAdd = Duration.ofSeconds(plusSeconds, plusNanos);
        UtcInstant result = initial.plus(durationToAdd);

        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    static Stream<Arguments> plusDurationProvider() {
        // initialMjd, initialNanos, plusSeconds, plusNanos, expectedMjd, expectedNanos
        return Stream.of(
            Arguments.of(0, 0, 0, 0, 0, 0),                               // Add zero
            Arguments.of(0, 0, 0, 1, 0, 1),                               // Add positive nanos, no carry
            Arguments.of(0, 0, 1, 0, 0, 1 * NANOS_PER_SEC),                // Add positive seconds, no carry
            Arguments.of(0, 0, 0, -1, -1, NANOS_PER_DAY - 1),              // Subtract nanos, with borrow
            Arguments.of(0, 0, SECS_PER_DAY, 1, 1, 1),                     // Add seconds, with carry to next day
            Arguments.of(0, 0, -SECS_PER_DAY, 1, -1, 1),                   // Subtract seconds, with borrow from prev day
            Arguments.of(1, 0, SECS_PER_DAY, 1, 2, 1),                     // Add seconds from non-zero day
            Arguments.of(1, 0, -SECS_PER_DAY, 1, 0, 1)                     // Subtract seconds from non-zero day
        );
    }

    //-----------------------------------------------------------------------
    // minus()
    //-----------------------------------------------------------------------
    @DisplayName("minus() should subtract a duration correctly")
    @ParameterizedTest(name = "[{index}] {0}d + {1}ns minus {2}s, {3}ns -> {4}d + {5}ns")
    @MethodSource("minusDurationProvider")
    void minus_subtractsDurationCorrectly(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration durationToSubtract = Duration.ofSeconds(minusSeconds, minusNanos);
        UtcInstant result = initial.minus(durationToSubtract);

        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    static Stream<Arguments> minusDurationProvider() {
        // initialMjd, initialNanos, minusSeconds, minusNanos, expectedMjd, expectedNanos
        return Stream.of(
            Arguments.of(0, 0, 0, 0, 0, 0),                               // Subtract zero
            Arguments.of(0, 0, 0, 1, -1, NANOS_PER_DAY - 1),               // Subtract nanos, with borrow
            Arguments.of(0, 0, 1, 0, -1, NANOS_PER_DAY - NANOS_PER_SEC),   // Subtract seconds, with borrow
            Arguments.of(0, 0, 0, -1, 0, 1),                               // Add nanos (subtract negative), no carry
            Arguments.of(0, 0, -SECS_PER_DAY, -1, 1, 1),                   // Add seconds (subtract negative), with carry
            Arguments.of(1, 0, SECS_PER_DAY, -1, 0, 1),                    // Subtract seconds from non-zero day
            Arguments.of(1, 0, -SECS_PER_DAY, -1, 2, 1)                    // Add seconds from non-zero day
        );
    }

    //-----------------------------------------------------------------------
    // toString() and parse()
    //-----------------------------------------------------------------------
    @DisplayName("toString() should return a correctly formatted ISO-8601 string")
    @ParameterizedTest(name = "{2}")
    @MethodSource("toStringAndParseProvider")
    void toString_returnsCorrectIso8601Format(long mjd, long nanoOfDay, String expectedString) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        assertEquals(expectedString, instant.toString());
    }

    @DisplayName("parse() should correctly parse a valid ISO-8601 string")
    @ParameterizedTest(name = "{2}")
    @MethodSource("toStringAndParseProvider")
    void parse_reversesToString(long mjd, long nanoOfDay, String inputString) {
        UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        assertEquals(expected, UtcInstant.parse(inputString));
    }

    static Stream<Arguments> toStringAndParseProvider() {
        return Stream.of(
            Arguments.of(40587, 0, "1970-01-01T00:00:00Z"),
            Arguments.of(40588, 1, "1970-01-02T00:00:00.000000001Z"),
            Arguments.of(40588, 999999, "1970-01-02T00:00:00.000999999Z"),
            Arguments.of(40588, 1000000, "1970-01-02T00:00:00.001Z"),
            Arguments.of(40619, NANOS_PER_SEC, "1970-02-02T00:00:01Z"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"), // Leap second
            Arguments.of(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z")
        );
    }

    //-----------------------------------------------------------------------
    // compareTo()
    //-----------------------------------------------------------------------
    @Test
    void compareTo_throwsExceptionForNullArgument() {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(0L, 0);
        assertThrows(NullPointerException.class, () -> instant.compareTo(null));
    }
}