package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.JulianFields;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link UtcInstant} focusing on modification, arithmetic, and formatting.
 */
public class UtcInstantTest {

    // A non-leap day for testing
    private static final long MJD_1972_12_30 = 41681;
    // A leap day for testing (contains a leap second)
    private static final long MJD_1972_12_31_LEAP = 41682;
    // The day after the leap day
    private static final long MJD_1973_01_01 = 41683;
    // Another leap day, one year later
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    // A leap day has one extra second
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @ValueSource(strings = {"", "A", "2012-13-01T00:00:00Z"})
    void parse_withInvalidFormat_throwsException(String invalidString) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withModifiedJulianDay()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_withModifiedJulianDay_success() {
        return Stream.of(
            // Simple cases, nano-of-day is unaffected
            Arguments.of(0L, 12345L, 1L, 1L, 12345L),
            Arguments.of(0L, 12345L, -1L, -1L, 12345L),
            // Change from one leap day to another, preserving leap-second time
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withModifiedJulianDay_success")
    void withModifiedJulianDay_givenValidDay_returnsUpdatedInstant(
        long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {

        UtcInstant baseInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        UtcInstant updatedInstant = baseInstant.withModifiedJulianDay(newMjd);

        assertEquals(expectedMjd, updatedInstant.getModifiedJulianDay());
        assertEquals(expectedNanos, updatedInstant.getNanoOfDay());
    }

    static Stream<Arguments> provider_withModifiedJulianDay_throws() {
        return Stream.of(
            // Set MJD to a non-leap day when nano-of-day is in the leap second
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withModifiedJulianDay_throws")
    void withModifiedJulianDay_onLeapDayWithTooManyNanos_throwsException(
        long initialMjd, long initialNanos, long invalidNewMjd) {

        UtcInstant baseInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        assertThrows(DateTimeException.class, () -> baseInstant.withModifiedJulianDay(invalidNewMjd));
    }

    //-----------------------------------------------------------------------
    // withNanoOfDay()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_withNanoOfDay_success() {
        return Stream.of(
            // Simple cases
            Arguments.of(0L, 12345L, 1L, 0L, 1L),
            Arguments.of(7L, 12345L, 2L, 7L, 2L),
            // Set to last nano of a normal day
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
            // Set to last nano of a leap day (non-leap second)
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1),
            // Set to a nano within the leap second on a leap day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withNanoOfDay_success")
    void withNanoOfDay_adjustsNanosWithinDay(
        long initialMjd, long initialNanos, long newNanoOfDay, long expectedMjd, long expectedNanos) {

        UtcInstant baseInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        UtcInstant updatedInstant = baseInstant.withNanoOfDay(newNanoOfDay);

        assertEquals(expectedMjd, updatedInstant.getModifiedJulianDay());
        assertEquals(expectedNanos, updatedInstant.getNanoOfDay());
    }

    static Stream<Arguments> provider_withNanoOfDay_throws() {
        return Stream.of(
            // Negative nano-of-day is invalid
            Arguments.of(0L, 12345L, -1L),
            // Set to a nano-of-day that only exists on leap days, but on a normal day
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY),
            Arguments.of(MJD_1973_01_01, 0L, NANOS_PER_DAY),
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_LEAP_DAY - 1),
            // Set to a nano-of-day beyond what is valid even for a leap day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withNanoOfDay_throws")
    void withNanoOfDay_withInvalidNanosForDay_throwsException(long initialMjd, long initialNanos, long invalidNewNanoOfDay) {
        UtcInstant baseInstant = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        assertThrows(DateTimeException.class, () -> baseInstant.withNanoOfDay(invalidNewNanoOfDay));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_plus() {
        return Stream.of(
            // --- Basic cases, no day crossing ---
            Arguments.of(0L, 0L, 0, 0, 0L, 0L), // Add zero
            Arguments.of(0L, 0L, 0, 1, 0L, 1L), // Add one nano
            Arguments.of(0L, 0L, 1, 0, 0L, NANOS_PER_SEC), // Add one second
            Arguments.of(0L, 0L, 3, 333333333, 0L, 3 * NANOS_PER_SEC + 333333333), // Add seconds and nanos

            // --- Crossing day boundaries ---
            Arguments.of(0L, 0L, SECS_PER_DAY, 1, 1L, 1L), // Add one day and one nano
            Arguments.of(0L, 0L, 2 * SECS_PER_DAY, 5, 2L, 5L), // Add two days and some nanos
            Arguments.of(0L, 0L, 0, -1, -1L, NANOS_PER_DAY - 1), // Subtract one nano, crossing to previous day
            Arguments.of(0L, 0L, -SECS_PER_DAY, 1, -1L, 1L), // Subtract one day, add one nano

            // --- Starting from a non-zero instant ---
            Arguments.of(1L, 0L, 0, 1, 1L, 1L), // Add one nano
            Arguments.of(1L, 0L, SECS_PER_DAY, 1, 2L, 1L), // Add one day and one nano
            Arguments.of(1L, 0L, 0, -1, 0L, NANOS_PER_DAY - 1) // Subtract one nano, crossing to previous day
        );
    }

    @ParameterizedTest
    @MethodSource("provider_plus")
    void plus_addsDuration_returnsCorrectInstant(
        long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {

        UtcInstant baseInstant = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration durationToAdd = Duration.ofSeconds(plusSeconds, plusNanos);
        UtcInstant result = baseInstant.plus(durationToAdd);

        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_minus() {
        return Stream.of(
            // --- Basic cases, no day crossing ---
            Arguments.of(0L, 0L, 0, 0, 0L, 0L), // Subtract zero
            Arguments.of(0L, 0L, 0, -1, 0L, 1L), // Subtract negative one nano (addition)
            Arguments.of(0L, 0L, -1, 0, 0L, NANOS_PER_SEC), // Subtract negative one second (addition)

            // --- Crossing day boundaries ---
            Arguments.of(0L, 0L, 0, 1, -1L, NANOS_PER_DAY - 1), // Subtract one nano, crossing to previous day
            Arguments.of(0L, 0L, SECS_PER_DAY, 1, -1L, NANOS_PER_DAY - 1), // Subtract one day and one nano
            Arguments.of(0L, 0L, -SECS_PER_DAY, -1, 1L, 1L), // Subtract negative day and nano (addition)

            // --- Starting from a non-zero instant ---
            Arguments.of(1L, 0L, 0, 1, 0L, NANOS_PER_DAY - 1), // Subtract one nano, crossing to previous day
            Arguments.of(1L, 0L, SECS_PER_DAY, 1, -1L, NANOS_PER_DAY - 1), // Subtract one day and one nano
            Arguments.of(1L, 0L, -SECS_PER_DAY, -1, 2L, 1L) // Subtract negative day and nano (addition)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_minus")
    void minus_subtractsDuration_returnsCorrectInstant(
        long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {

        UtcInstant baseInstant = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration durationToSubtract = Duration.ofSeconds(minusSeconds, minusNanos);
        UtcInstant result = baseInstant.minus(durationToSubtract);

        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    // toString() and parse()
    //-----------------------------------------------------------------------
    static Stream<Arguments> provider_toStringAndParse() {
        return Stream.of(
            Arguments.of(40587, 0, "1970-01-01T00:00:00Z"),
            Arguments.of(40588, 1, "1970-01-02T00:00:00.000000001Z"),
            Arguments.of(40588, 999, "1970-01-02T00:00:00.000000999Z"),
            Arguments.of(40588, 1000, "1970-01-02T00:00:00.000001Z"),
            Arguments.of(40588, 999000, "1970-01-02T00:00:00.000999Z"),
            Arguments.of(40588, 1000000, "1970-01-02T00:00:00.001Z"),
            Arguments.of(40618, 999999999, "1970-02-01T00:00:00.999999999Z"),
            Arguments.of(40619, NANOS_PER_SEC, "1970-02-02T00:00:01Z"),
            Arguments.of(40620, 60 * NANOS_PER_SEC, "1970-02-03T00:01:00Z"),
            Arguments.of(40621, 60 * 60 * NANOS_PER_SEC, "1970-02-04T01:00:00Z"),
            // Leap second representation
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
            Arguments.of(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z")
        );
    }

    @ParameterizedTest
    @MethodSource("provider_toStringAndParse")
    void toString_returnsIso8601Format(long mjd, long nanoOfDay, String expectedString) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        assertEquals(expectedString, instant.toString());
    }

    @ParameterizedTest
    @MethodSource("provider_toStringAndParse")
    void parse_canParseStringFromToString(long mjd, long nanoOfDay, String isoString) {
        UtcInstant expectedInstant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        assertEquals(expectedInstant, UtcInstant.parse(isoString));
    }

    //-----------------------------------------------------------------------
    // of(TaiInstant)
    //-----------------------------------------------------------------------
    @Test
    void of_fromTaiInstant_convertsCorrectly() {
        // TAI epoch is 1958-01-01.
        long mjdTaiEpoch = LocalDate.of(1958, 1, 1).getLong(JulianFields.MODIFIED_JULIAN_DAY);

        // This test verifies the conversion from TaiInstant to UtcInstant.
        // The relationship between TAI and UTC is complex, especially before 1972.
        // This test uses a fixed offset of 10 seconds, which was the defined
        // difference when leap seconds were formally introduced in 1972. The test
        // implicitly checks that the library's conversion logic matches this
        // assumption for the dates tested.
        long assumedTaiUtcOffsetSeconds = 10L;

        // Test a range of dates around the TAI epoch
        for (long dayOffset = -1000; dayOffset < 1000; dayOffset++) {
            for (long secondOfDay = 0; secondOfDay < 10; secondOfDay++) {
                long nanoOfSecond = 2L;

                // The expected UtcInstant, constructed directly
                UtcInstant expectedUtc = UtcInstant.ofModifiedJulianDay(
                    mjdTaiEpoch + dayOffset,
                    secondOfDay * NANOS_PER_SEC + nanoOfSecond);

                // The TaiInstant that should correspond to the same point in time.
                // TAI seconds are calculated from its epoch (1958-01-01). The offset
                // is added to align with the proleptic UTC time scale.
                long taiSeconds = (dayOffset * SECS_PER_DAY) + secondOfDay + assumedTaiUtcOffsetSeconds;
                TaiInstant tai = TaiInstant.ofTaiSeconds(taiSeconds, nanoOfSecond);

                assertEquals(expectedUtc, UtcInstant.of(tai));
            }
        }
    }
}