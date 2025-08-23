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

/**
 * Tests for modification, formatting, and parsing of {@link UtcInstant}.
 */
@DisplayName("UtcInstant modification, formatting, and parsing")
class UtcInstantModificationAndFormattingTest {

    private static final long MJD_1972_12_30 = 41681;
    private static final long MJD_1972_12_31_LEAP = 41682; // A day with a leap second
    private static final long MJD_1973_01_01 = 41683;
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------

    static Stream<String> provider_invalidParseStrings() {
        return Stream.of(
                "",                      // Empty string
                "A",                     // Not a date-time
                "2012-13-01T00:00:00Z"   // Invalid month
        );
    }

    @ParameterizedTest
    @MethodSource("provider_invalidParseStrings")
    void parse_withInvalidString_throwsDateTimeException(String invalidString) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withModifiedJulianDay()
    //-----------------------------------------------------------------------

    /**
     * Provides arguments for {@link #withModifiedJulianDay_givenValidDay_returnsUpdatedInstant}.
     * @return a stream of arguments: {initialMjd, initialNanos, newMjd, expectedMjd, expectedNanos}.
     */
    static Stream<Arguments> provider_validModifiedJulianDayCases() {
        return Stream.of(
                Arguments.of(0L, 12345L, 1L, 1L, 12345L),
                Arguments.of(0L, 12345L, -1L, -1L, 12345L),
                Arguments.of(7L, 12345L, 2L, 2L, 12345L),
                Arguments.of(7L, 12345L, -2L, -2L, 12345L),
                Arguments.of(-99L, 12345L, 3L, 3L, 12345L),
                Arguments.of(-99L, 12345L, -3L, -3L, 12345L),
                // On a leap day, setting MJD to another leap day should preserve nano-of-day
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_validModifiedJulianDayCases")
    void withModifiedJulianDay_givenValidDay_returnsUpdatedInstant(
            long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        UtcInstant result = initial.withModifiedJulianDay(newMjd);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    /**
     * Provides arguments for {@link #withModifiedJulianDay_givenInvalidDayForNanos_throwsDateTimeException}.
     * @return a stream of arguments: {initialMjd, initialNanos, newMjd}.
     */
    static Stream<Arguments> provider_invalidModifiedJulianDayCases() {
        return Stream.of(
                // Attempting to set a non-leap day when nano-of-day is in the leap second
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_invalidModifiedJulianDayCases")
    void withModifiedJulianDay_givenInvalidDayForNanos_throwsDateTimeException(long initialMjd, long initialNanos, long newMjd) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjd));
    }

    //-----------------------------------------------------------------------
    // withNanoOfDay()
    //-----------------------------------------------------------------------

    /**
     * Provides arguments for {@link #withNanoOfDay_givenValidNanos_returnsUpdatedInstant}.
     * @return a stream of arguments: {mjd, initialNanos, newNanos, expectedMjd, expectedNanos}.
     */
    static Stream<Arguments> provider_validNanoOfDayCases() {
        return Stream.of(
                Arguments.of(0L, 12345L, 1L, 0L, 1L),
                Arguments.of(7L, 12345L, 2L, 7L, 2L),
                Arguments.of(-99L, 12345L, 3L, -99L, 3L),
                // Set to max valid nanos on a non-leap day
                Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
                // Set to max valid nanos on a leap day
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1),
                // Set to a value valid on a leap day, but not a normal day
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_validNanoOfDayCases")
    void withNanoOfDay_givenValidNanos_returnsUpdatedInstant(
            long mjd, long initialNanos, long newNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
        UtcInstant result = initial.withNanoOfDay(newNanos);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    /**
     * Provides arguments for {@link #withNanoOfDay_givenInvalidNanosForDay_throwsDateTimeException}.
     * @return a stream of arguments: {mjd, initialNanos, invalidNewNanos}.
     */
    static Stream<Arguments> provider_invalidNanoOfDayCases() {
        return Stream.of(
                Arguments.of(0L, 12345L, -1L), // Negative nanos are invalid
                // Nanos equal to a full day are invalid on a non-leap day
                Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY),
                Arguments.of(MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_DAY),
                // Nanos within leap second range are invalid on a non-leap day
                Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1),
                Arguments.of(MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1),
                // Nanos equal to a full leap day are invalid on any day
                Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY),
                Arguments.of(MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_invalidNanoOfDayCases")
    void withNanoOfDay_givenInvalidNanosForDay_throwsDateTimeException(long mjd, long initialNanos, long invalidNewNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, initialNanos);
        assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(invalidNewNanos));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------

    /**
     * Provides arguments for {@link #plus_withDuration_addsCorrectly}.
     * @return a stream of arguments: {mjd, nanos, plusSeconds, plusNanos, expectedMjd, expectedNanos}.
     */
    static Stream<Arguments> provider_plusDuration() {
        return Stream.of(
            Arguments.of(0, 0, -2 * SECS_PER_DAY, 5, -2, 5),
            Arguments.of(0, 0, -1 * SECS_PER_DAY, 1, -1, 1),
            Arguments.of(0, 0, -1 * SECS_PER_DAY, 0, -1, 0),
            Arguments.of(0, 0, 0, -2, -1, NANOS_PER_DAY - 2),
            Arguments.of(0, 0, 0, -1, -1, NANOS_PER_DAY - 1),
            Arguments.of(0, 0, 0, 0, 0, 0),
            Arguments.of(0, 0, 0, 1, 0, 1),
            Arguments.of(0, 0, 0, 2, 0, 2),
            Arguments.of(0, 0, 1, 0, 0, NANOS_PER_SEC),
            Arguments.of(0, 0, 2, 0, 0, 2 * NANOS_PER_SEC),
            Arguments.of(0, 0, 3, 333333333, 0, 3 * NANOS_PER_SEC + 333333333),
            Arguments.of(0, 0, SECS_PER_DAY, 0, 1, 0),
            Arguments.of(0, 0, SECS_PER_DAY, 1, 1, 1),
            Arguments.of(0, 0, 2 * SECS_PER_DAY, 5, 2, 5),
            Arguments.of(1, 0, -2 * SECS_PER_DAY, 5, -1, 5),
            Arguments.of(1, 0, -1 * SECS_PER_DAY, 1, 0, 1),
            Arguments.of(1, 0, -1 * SECS_PER_DAY, 0, 0, 0),
            Arguments.of(1, 0, 0, -2, 0, NANOS_PER_DAY - 2),
            Arguments.of(1, 0, 0, -1, 0, NANOS_PER_DAY - 1),
            Arguments.of(1, 0, 0, 0, 1, 0),
            Arguments.of(1, 0, 0, 1, 1, 1),
            Arguments.of(1, 0, 0, 2, 1, 2),
            Arguments.of(1, 0, 1, 0, 1, NANOS_PER_SEC),
            Arguments.of(1, 0, 2, 0, 1, 2 * NANOS_PER_SEC),
            Arguments.of(1, 0, 3, 333333333, 1, 3 * NANOS_PER_SEC + 333333333),
            Arguments.of(1, 0, SECS_PER_DAY, 0, 2, 0),
            Arguments.of(1, 0, SECS_PER_DAY, 1, 2, 1),
            Arguments.of(1, 0, 2 * SECS_PER_DAY, 5, 3, 5)
        );
    }

    @ParameterizedTest(name = "[{index}] {0} MJD + {1} nanos, plus {2}s {3}ns -> {4} MJD + {5} nanos")
    @MethodSource("provider_plusDuration")
    void plus_withDuration_addsCorrectly(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.plus(Duration.ofSeconds(plusSeconds, plusNanos));
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------

    /**
     * Provides arguments for {@link #minus_withDuration_subtractsCorrectly}.
     * @return a stream of arguments: {mjd, nanos, minusSeconds, minusNanos, expectedMjd, expectedNanos}.
     */
    static Stream<Arguments> provider_minusDuration() {
        return Stream.of(
            Arguments.of(0, 0, 2 * SECS_PER_DAY, -5, -2, 5),
            Arguments.of(0, 0, SECS_PER_DAY, -1, -1, 1),
            Arguments.of(0, 0, SECS_PER_DAY, 0, -1, 0),
            Arguments.of(0, 0, 0, 2, -1, NANOS_PER_DAY - 2),
            Arguments.of(0, 0, 0, 1, -1, NANOS_PER_DAY - 1),
            Arguments.of(0, 0, 0, 0, 0, 0),
            Arguments.of(0, 0, 0, -1, 0, 1),
            Arguments.of(0, 0, 0, -2, 0, 2),
            Arguments.of(0, 0, -1, 0, 0, NANOS_PER_SEC),
            Arguments.of(0, 0, -2, 0, 0, 2 * NANOS_PER_SEC),
            Arguments.of(0, 0, -3, -333333333, 0, 3 * NANOS_PER_SEC + 333333333),
            Arguments.of(0, 0, -SECS_PER_DAY, 0, 1, 0),
            Arguments.of(0, 0, -SECS_PER_DAY, -1, 1, 1),
            Arguments.of(0, 0, -2 * SECS_PER_DAY, -5, 2, 5),
            Arguments.of(1, 0, 2 * SECS_PER_DAY, -5, -1, 5),
            Arguments.of(1, 0, SECS_PER_DAY, -1, 0, 1),
            Arguments.of(1, 0, SECS_PER_DAY, 0, 0, 0),
            Arguments.of(1, 0, 0, 2, 0, NANOS_PER_DAY - 2),
            Arguments.of(1, 0, 0, 1, 0, NANOS_PER_DAY - 1),
            Arguments.of(1, 0, 0, 0, 1, 0),
            Arguments.of(1, 0, 0, -1, 1, 1),
            Arguments.of(1, 0, 0, -2, 1, 2),
            Arguments.of(1, 0, -1, 0, 1, NANOS_PER_SEC),
            Arguments.of(1, 0, -2, 0, 1, 2 * NANOS_PER_SEC),
            Arguments.of(1, 0, -3, -333333333, 1, 3 * NANOS_PER_SEC + 333333333),
            Arguments.of(1, 0, -SECS_PER_DAY, 0, 2, 0),
            Arguments.of(1, 0, -SECS_PER_DAY, -1, 2, 1),
            Arguments.of(1, 0, -2 * SECS_PER_DAY, -5, 3, 5)
        );
    }

    @ParameterizedTest(name = "[{index}] {0} MJD + {1} nanos, minus {2}s {3}ns -> {4} MJD + {5} nanos")
    @MethodSource("provider_minusDuration")
    void minus_withDuration_subtractsCorrectly(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.minus(Duration.ofSeconds(minusSeconds, minusNanos));
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    // toString() and parse() round-trip
    //-----------------------------------------------------------------------

    /**
     * Provides arguments for {@link #toString_returnsCorrectIso8601Format} and {@link #parse_withValidString_returnsCorrectInstant}.
     * @return a stream of arguments: {mjd, nanoOfDay, expectedString}.
     */
    static Stream<Arguments> provider_toStringAndParseCases() {
        return Stream.of(
                Arguments.of(40587, 0, "1970-01-01T00:00:00Z"),
                Arguments.of(40588, 1, "1970-01-02T00:00:00.000000001Z"),
                Arguments.of(40588, 999, "1970-01-02T00:00:00.000000999Z"),
                Arguments.of(40588, 1000, "1970-01-02T00:00:00.000001Z"),
                Arguments.of(40588, 999000, "1970-01-02T00:00:00.000999Z"),
                Arguments.of(40588, 1000000, "1970-01-02T00:00:00.001Z"),
                Arguments.of(40618, 999999999, "1970-02-01T00:00:00.999999999Z"),
                Arguments.of(40619, NANOS_PER_SEC, "1970-02-02T00:00:01Z"),
                Arguments.of(40620, 60L * NANOS_PER_SEC, "1970-2-03T00:01:00Z"),
                Arguments.of(40621, 60L * 60L * NANOS_PER_SEC, "1970-02-04T01:00:00Z"),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
                Arguments.of(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z")
        );
    }

    @ParameterizedTest
    @MethodSource("provider_toStringAndParseCases")
    void toString_returnsCorrectIso8601Format(long mjd, long nanoOfDay, String expectedString) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        assertEquals(expectedString, instant.toString());
    }

    @ParameterizedTest
    @MethodSource("provider_toStringAndParseCases")
    void parse_withValidString_returnsCorrectInstant(long mjd, long nanoOfDay, String isoString) {
        UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        assertEquals(expected, UtcInstant.parse(isoString));
    }

    //-----------------------------------------------------------------------
    // ofModifiedJulianDay() with invalid arguments
    //-----------------------------------------------------------------------
    @Test
    void ofModifiedJulianDay_withExcessiveNanosOnNonLeapDay_throwsDateTimeException() {
        assertThrows(DateTimeException.class, () -> UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, NANOS_PER_DAY));
    }
}