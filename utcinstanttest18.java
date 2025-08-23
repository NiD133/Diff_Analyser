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
 * Tests for {@link UtcInstant}.
 */
class UtcInstantTest {

    // A normal day (not a leap second day)
    private static final long MJD_1972_12_30 = 41681;
    // A day with a leap second
    private static final long MJD_1972_12_31_LEAP = 41682;
    // The day after a leap second day
    private static final long MJD_1973_01_01 = 41683;
    // Another leap second day, one year later
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 86400L;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "A", // Not a valid instant format
        "2012-13-01T00:00:00Z" // Invalid month
    })
    void parse_withInvalidFormat_throwsException(String invalidString) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withModifiedJulianDay()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provideValidWithModifiedJulianDayCases() {
        return Stream.of(
            // Simple cases, nano-of-day is unaffected
            Arguments.of(0L, 12345L, 1L, 1L, 12345L),
            Arguments.of(0L, 12345L, -1L, -1L, 12345L),
            // On a leap day, setting MJD to another leap day preserves nano-of-day
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidWithModifiedJulianDayCases")
    void withModifiedJulianDay_givenValidDay_returnsUpdatedInstant(
            long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {
        // Arrange
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);

        // Act
        UtcInstant result = initialInstant.withModifiedJulianDay(newMjd);

        // Assert
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    static Stream<Arguments> provideInvalidWithModifiedJulianDayCases() {
        return Stream.of(
            // A nano-of-day representing the leap second is invalid on a non-leap-second day
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidWithModifiedJulianDayCases")
    void withModifiedJulianDay_givenInvalidDayForNanos_throwsException(long initialMjd, long initialNanos, long newMjd) {
        // Arrange
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);

        // Act & Assert
        assertThrows(DateTimeException.class, () -> initialInstant.withModifiedJulianDay(newMjd));
    }

    //-----------------------------------------------------------------------
    // withNanoOfDay()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provideValidWithNanoOfDayCases() {
        return Stream.of(
            // Simple cases, MJD is unaffected
            Arguments.of(0L, 12345L, 1L, 0L, 1L),
            Arguments.of(7L, 12345L, 2L, 7L, 2L),
            // Setting nano-of-day to the max valid value on a normal day
            Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
            // Setting nano-of-day to the max valid value on a leap second day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1),
            // Setting nano-of-day to the exact leap second moment (23:59:60) on a leap second day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidWithNanoOfDayCases")
    void withNanoOfDay_givenValidNanos_returnsUpdatedInstant(
            long initialMjd, long initialNanos, long newNanos, long expectedMjd, long expectedNanos) {
        // Arrange
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);

        // Act
        UtcInstant result = initialInstant.withNanoOfDay(newNanos);

        // Assert
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    static Stream<Arguments> provideInvalidWithNanoOfDayCases() {
        return Stream.of(
            // Negative nano-of-day is always invalid
            Arguments.of(0L, 12345L, -1L),
            // Setting nano-of-day to the leap second moment on a normal day is invalid
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY),
            Arguments.of(MJD_1973_01_01, 0L, NANOS_PER_DAY),
            // Setting nano-of-day beyond the valid range for a normal day
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_LEAP_DAY - 1),
            // Setting nano-of-day beyond the valid range for a leap second day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidWithNanoOfDayCases")
    void withNanoOfDay_givenInvalidNanosForDay_throwsException(long initialMjd, long initialNanos, long newNanos) {
        // Arrange
        UtcInstant initialInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);

        // Act & Assert
        assertThrows(DateTimeException.class, () -> initialInstant.withNanoOfDay(newNanos));
    }

    //-----------------------------------------------------------------------
    // plus()
    //-----------------------------------------------------------------------
    static Stream<Arguments> providePlusCases() {
        return Stream.of(
            // --- MJD 0, Nanos 0 ---
            // Add zero
            Arguments.of(0, 0, 0, 0, 0, 0),
            // Add positive nanos, no day change
            Arguments.of(0, 0, 0, 1, 0, 1),
            // Add positive seconds, no day change
            Arguments.of(0, 0, 1, 0, 0, NANOS_PER_SEC),
            // Add positive seconds and nanos, no day change
            Arguments.of(0, 0, 3, 333333333, 0, 3 * NANOS_PER_SEC + 333333333),
            // Add exactly one day
            Arguments.of(0, 0, SECS_PER_DAY, 0, 1, 0),
            // Add negative nanos, rolls back one day
            Arguments.of(0, 0, 0, -1, -1, NANOS_PER_DAY - 1),
            // Add negative seconds, rolls back one day
            Arguments.of(0, 0, -1, 0, -1, NANOS_PER_DAY - NANOS_PER_SEC),
            // Add exactly negative one day
            Arguments.of(0, 0, -SECS_PER_DAY, 0, -1, 0),

            // --- MJD 1, Nanos 0 ---
            // Add zero
            Arguments.of(1, 0, 0, 0, 1, 0),
            // Add positive nanos, no day change
            Arguments.of(1, 0, 0, 1, 1, 1),
            // Add exactly one day
            Arguments.of(1, 0, SECS_PER_DAY, 0, 2, 0),
            // Add negative nanos, rolls back one day
            Arguments.of(1, 0, 0, -1, 0, NANOS_PER_DAY - 1),
            // Add exactly negative one day
            Arguments.of(1, 0, -SECS_PER_DAY, 0, 0, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("providePlusCases")
    void plus_addsDurationCorrectly(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
        // Arrange
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration durationToAdd = Duration.ofSeconds(plusSeconds, plusNanos);

        // Act
        UtcInstant result = initial.plus(durationToAdd);

        // Assert
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    @Test
    void plus_whenResultUnderflows_throwsException() {
        // Arrange
        UtcInstant minInstant = UtcInstant.ofModifiedJulianDay(Long.MIN_VALUE, 0);

        // Act & Assert
        assertThrows(ArithmeticException.class, () -> minInstant.plus(Duration.ofNanos(-1)));
    }

    //-----------------------------------------------------------------------
    // minus()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provideMinusCases() {
        return Stream.of(
            // --- MJD 0, Nanos 0 ---
            // Subtract zero
            Arguments.of(0, 0, 0, 0, 0, 0),
            // Subtract positive nanos, rolls back one day
            Arguments.of(0, 0, 0, 1, -1, NANOS_PER_DAY - 1),
            // Subtract positive seconds, rolls back one day
            Arguments.of(0, 0, 1, 0, -1, NANOS_PER_DAY - NANOS_PER_SEC),
            // Subtract exactly one day
            Arguments.of(0, 0, SECS_PER_DAY, 0, -1, 0),
            // Subtract negative nanos, no day change
            Arguments.of(0, 0, 0, -1, 0, 1),
            // Subtract negative seconds, no day change
            Arguments.of(0, 0, -1, 0, 0, NANOS_PER_SEC),
            // Subtract exactly negative one day
            Arguments.of(0, 0, -SECS_PER_DAY, 0, 1, 0),

            // --- MJD 1, Nanos 0 ---
            // Subtract zero
            Arguments.of(1, 0, 0, 0, 1, 0),
            // Subtract positive nanos, rolls back one day
            Arguments.of(1, 0, 0, 1, 0, NANOS_PER_DAY - 1),
            // Subtract exactly one day
            Arguments.of(1, 0, SECS_PER_DAY, 0, 0, 0),
            // Subtract negative nanos, no day change
            Arguments.of(1, 0, 0, -1, 1, 1),
            // Subtract exactly negative one day
            Arguments.of(1, 0, -SECS_PER_DAY, 0, 2, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("provideMinusCases")
    void minus_subtractsDurationCorrectly(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
        // Arrange
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration durationToSubtract = Duration.ofSeconds(minusSeconds, minusNanos);

        // Act
        UtcInstant result = initial.minus(durationToSubtract);

        // Assert
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    // toString() and parse()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provideToStringCases() {
        return Stream.of(
            // Zero nanos
            Arguments.of(40587, 0, "1970-01-01T00:00:00Z"),
            // Various fractional seconds
            Arguments.of(40588, 1, "1970-01-02T00:00:00.000000001Z"),
            Arguments.of(40588, 999, "1970-01-02T00:00:00.000000999Z"),
            Arguments.of(40588, 1000, "1970-01-02T00:00:00.000001Z"),
            Arguments.of(40588, 999000, "1970-01-02T00:00:00.000999Z"),
            Arguments.of(40588, 1000000, "1970-01-02T00:00:00.001Z"),
            Arguments.of(40618, 999999999, "1970-02-01T00:00:00.999999999Z"),
            // Rollover to next second, minute, hour
            Arguments.of(40619, NANOS_PER_SEC, "1970-02-02T00:00:01Z"),
            Arguments.of(40620, 60 * NANOS_PER_SEC, "1970-02-03T00:01:00Z"),
            Arguments.of(40621, 3600 * NANOS_PER_SEC, "1970-02-04T01:00:00Z"),
            // Leap second handling
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"), // The leap second
            Arguments.of(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z")
        );
    }

    @ParameterizedTest
    @MethodSource("provideToStringCases")
    void toString_returnsCorrectIsoFormat(long mjd, long nod, String expected) {
        // Arrange
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nod);

        // Act & Assert
        assertEquals(expected, instant.toString());
    }

    @ParameterizedTest
    @MethodSource("provideToStringCases")
    void parse_handlesIsoFormatFromToString(long mjd, long nod, String isoString) {
        // Arrange
        UtcInstant expectedInstant = UtcInstant.ofModifiedJulianDay(mjd, nod);

        // Act
        UtcInstant parsedInstant = UtcInstant.parse(isoString);

        // Assert
        assertEquals(expectedInstant, parsedInstant);
    }
}