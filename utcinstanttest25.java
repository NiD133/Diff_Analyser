package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test class for modification and arithmetic operations on {@link UtcInstant}.
 */
class UtcInstantModificationAndArithmeticTest {

    private static final long MJD_1972_12_30 = 41681;
    private static final long MJD_1972_12_31_LEAP = 41682; // A leap day
    private static final long MJD_1973_01_01 = 41683;
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365; // Another leap day

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @ValueSource(strings = {
        "",                      // empty string
        "A",                     // malformed
        "2012-13-01T00:00:00Z"   // invalid month
    })
    void parse_withInvalidString_throwsDateTimeException(String text) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(text));
    }

    //-----------------------------------------------------------------------
    // withModifiedJulianDay()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_withModifiedJulianDay_valid() {
        return Stream.of(
            // Simple cases, nano-of-day is unaffected
            Arguments.of(0L, 12345L, 1L, 1L, 12345L),
            Arguments.of(0L, 12345L, -1L, -1L, 12345L),
            // Leap day to leap day, nano-of-day is valid for both
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY),
            // Setting to the same day should not change anything
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @ParameterizedTest(name = "MJD {0}, Nanos {1} -> withMJD({2}) -> MJD {3}, Nanos {4}")
    @MethodSource("provider_withModifiedJulianDay_valid")
    void withModifiedJulianDay_givenValidDay_returnsUpdatedInstant(
            long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        UtcInstant result = initial.withModifiedJulianDay(newMjd);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    static Stream<Arguments> provider_withModifiedJulianDay_invalid() {
        // These cases are invalid because the nano-of-day from the initial instant
        // is not valid for the new modified Julian day.
        // Here, the initial instant is on a leap day with a nano-of-day corresponding to the leap second.
        // The new day is not a leap day, so it cannot accommodate this nano-of-day.
        return Stream.of(
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30), // From leap day to non-leap day
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)  // From leap day to non-leap day
        );
    }

    @ParameterizedTest(name = "MJD {0}, Nanos {1} -> withMJD({2}) throws")
    @MethodSource("provider_withModifiedJulianDay_invalid")
    void withModifiedJulianDay_whenNanosBecomeInvalidForNewDay_throwsDateTimeException(
            long initialMjd, long initialNanos, long newMjd) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjd));
    }

    //-----------------------------------------------------------------------
    // withNanoOfDay()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_withNanoOfDay_valid() {
        return Stream.of(
            // Simple cases
            Arguments.of(0L, 12345L, 1L, 0L, 1L),
            Arguments.of(7L, 12345L, 2L, 7L, 2L),
            // On a standard day
            Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
            // On a leap day
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1)
        );
    }

    @ParameterizedTest(name = "MJD {0}, Nanos {1} -> withNanos({2}) -> MJD {3}, Nanos {4}")
    @MethodSource("provider_withNanoOfDay_valid")
    void withNanoOfDay_givenValidNanos_returnsUpdatedInstant(
            long mjd, long nanos, long newNanoOfDay, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.withNanoOfDay(newNanoOfDay);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    static Stream<Arguments> provider_withNanoOfDay_invalid() {
        return Stream.of(
            // Negative nanos are invalid
            Arguments.of(0L, 12345L, -1L),
            // Nanos exceeding a standard day's length
            Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY),
            Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1),
            Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY),
            // Nanos exceeding a leap day's length
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY)
        );
    }

    @ParameterizedTest(name = "MJD {0}, Nanos {1} -> withNanos({2}) throws")
    @MethodSource("provider_withNanoOfDay_invalid")
    void withNanoOfDay_givenInvalidNanos_throwsDateTimeException(long mjd, long nanos, long newNanoOfDay) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(newNanoOfDay));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_plus() {
        return Stream.of(
            // --- Test cases for UtcInstant.ofModifiedJulianDay(0, 0) ---
            Arguments.of(0, 0, 0, 0, 0, 0), // Add zero
            Arguments.of(0, 0, 0, 1, 0, 1), // Add positive nanos, no day change
            Arguments.of(0, 0, 1, 0, 0, NANOS_PER_SEC), // Add positive seconds, no day change
            Arguments.of(0, 0, SECS_PER_DAY, 1, 1, 1), // Add positive duration, crossing day boundary
            Arguments.of(0, 0, 0, -1, -1, NANOS_PER_DAY - 1), // Add negative nanos, crossing day boundary backwards
            Arguments.of(0, 0, -SECS_PER_DAY, 1, -1, 1), // Add negative duration, crossing day boundary backwards

            // --- Test cases for UtcInstant.ofModifiedJulianDay(1, 0) ---
            Arguments.of(1, 0, 0, 0, 1, 0), // Add zero
            Arguments.of(1, 0, 0, 1, 1, 1), // Add positive nanos, no day change
            Arguments.of(1, 0, SECS_PER_DAY, 1, 2, 1), // Add positive duration, crossing day boundary
            Arguments.of(1, 0, 0, -1, 0, NANOS_PER_DAY - 1), // Add negative nanos, crossing day boundary backwards
            Arguments.of(1, 0, -SECS_PER_DAY, 1, 0, 1) // Add negative duration, crossing day boundary backwards
        );
    }

    @ParameterizedTest(name = "MJD {0}, Nanos {1} + {2}s, {3}ns -> MJD {4}, Nanos {5}")
    @MethodSource("provider_plus")
    void plus_duration_shouldCalculateCorrectInstant(
            long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.plus(Duration.ofSeconds(plusSeconds, plusNanos));
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_minus() {
        return Stream.of(
            // --- Test cases for UtcInstant.ofModifiedJulianDay(0, 0) ---
            Arguments.of(0, 0, 0, 0, 0, 0), // Subtract zero
            Arguments.of(0, 0, 0, -1, 0, 1), // Subtract negative nanos (addition)
            Arguments.of(0, 0, 0, 1, -1, NANOS_PER_DAY - 1), // Subtract positive nanos, crossing day boundary
            Arguments.of(0, 0, -SECS_PER_DAY, -1, 1, 1), // Subtract negative duration (addition), crossing day boundary

            // --- Test cases for UtcInstant.ofModifiedJulianDay(1, 0) ---
            Arguments.of(1, 0, 0, 0, 1, 0), // Subtract zero
            Arguments.of(1, 0, 0, 1, 0, NANOS_PER_DAY - 1), // Subtract positive nanos, crossing day boundary
            Arguments.of(1, 0, SECS_PER_DAY, 0, 0, 0), // Subtract one day
            Arguments.of(1, 0, -SECS_PER_DAY, -1, 2, 1) // Subtract negative duration (addition), crossing day boundary
        );
    }

    @ParameterizedTest(name = "MJD {0}, Nanos {1} - {2}s, {3}ns -> MJD {4}, Nanos {5}")
    @MethodSource("provider_minus")
    void minus_duration_shouldCalculateCorrectInstant(
            long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.minus(Duration.ofSeconds(minusSeconds, minusNanos));
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    // toString() and parse()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_toStringAndParse() {
        return Stream.of(
            // Standard cases
            Arguments.of(40587L, 0L, "1970-01-01T00:00:00Z"),
            // Nano-of-second formatting variations
            Arguments.of(40588L, 1L, "1970-01-02T00:00:00.000000001Z"),
            Arguments.of(40588L, 999L, "1970-01-02T00:00:00.000000999Z"),
            Arguments.of(40588L, 1_000L, "1970-01-02T00:00:00.000001Z"),
            Arguments.of(40588L, 999_000L, "1970-01-02T00:00:00.000999Z"),
            Arguments.of(40588L, 1_000_000L, "1970-01-02T00:00:00.001Z"),
            Arguments.of(40618L, 999_999_999L, "1970-02-01T00:00:00.999999999Z"),
            // Time-of-day roll-over
            Arguments.of(40619L, NANOS_PER_SEC, "1970-02-02T00:00:01Z"),
            Arguments.of(40620L, 60L * NANOS_PER_SEC, "1970-02-03T00:01:00Z"),
            Arguments.of(40621L, 60L * 60L * NANOS_PER_SEC, "1970-02-04T01:00:00Z"),
            // Leap second handling
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"), // The leap second itself
            Arguments.of(MJD_1973_01_01, 0L, "1973-01-01T00:00:00Z") // Day after leap second
        );
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("provider_toStringAndParse")
    void toString_shouldFormatAsIsoString(long mjd, long nod, String expected) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nod);
        assertEquals(expected, instant.toString());
    }

    @ParameterizedTest(name = "{2}")
    @MethodSource("provider_toStringAndParse")
    void parse_ofToString_shouldRoundTrip(long mjd, long nod, String str) {
        UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nod);
        assertEquals(expected, UtcInstant.parse(str));
    }

    //-----------------------------------------------------------------------
    // toTaiInstant()
    //-----------------------------------------------------------------------
    @Test
    void toTaiInstant_whenUtcIsAtMaxValue_throwsArithmeticException() {
        UtcInstant utc = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, 0);
        assertThrows(ArithmeticException.class, utc::toTaiInstant);
    }
}