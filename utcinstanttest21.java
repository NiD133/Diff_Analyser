package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.time.DateTimeException;
import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test class for {@link UtcInstant} focusing on arithmetic, modification, and parsing.
 */
public class UtcInstantArithmeticAndParsingTest {

    // A standard, non-leap day for testing.
    private static final long MJD_1972_12_30 = 41681;
    // A day that contains a leap second.
    private static final long MJD_1972_12_31_LEAP = 41682;
    // The day immediately following a leap-second day.
    private static final long MJD_1973_01_01 = 41683;
    // Another leap-second day, one year later.
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 86400L;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @ParameterizedTest(name = "Parsing \"{0}\" should fail")
    @ValueSource(strings = {"", "A", "2012-13-01T00:00:00Z"})
    public void parse_withInvalidFormat_throwsDateTimeException(String invalidString) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withModifiedJulianDay()
    //-----------------------------------------------------------------------
    static Stream<Arguments> data_withModifiedJulianDay_success() {
        return Stream.of(
                // mjd, nanoOfDay, newMjd, expectedMjd, expectedNanoOfDay
                arguments(0L, 12345L, 1L, 1L, 12345L),
                arguments(0L, 12345L, -1L, -1L, 12345L),
                // Change MJD on a leap day, keeping a valid nano-of-day
                arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
                arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @ParameterizedTest(name = "MJD {0} with nanos {1}, when changed to MJD {2}, becomes MJD {3} with nanos {4}")
    @MethodSource("data_withModifiedJulianDay_success")
    public void withModifiedJulianDay_returnsUpdatedInstant(long mjd, long nanos, long newMjd, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.withModifiedJulianDay(newMjd);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    static Stream<Arguments> data_withModifiedJulianDay_failure() {
        return Stream.of(
                // mjd, nanoOfDay, newMjd (invalid for the given nanoOfDay)
                // Start on a leap day with a leap second, move to a non-leap day
                arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
                arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
        );
    }

    @ParameterizedTest(name = "MJD {0} with nanos {1}, when changed to MJD {2}, should fail")
    @MethodSource("data_withModifiedJulianDay_failure")
    public void withModifiedJulianDay_forInvalidDay_throwsDateTimeException(long mjd, long nanos, long newMjd) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjd));
    }

    //-----------------------------------------------------------------------
    // withNanoOfDay()
    //-----------------------------------------------------------------------
    static Stream<Arguments> data_withNanoOfDay_success() {
        return Stream.of(
                // mjd, nanoOfDay, newNanoOfDay, expectedMjd, expectedNanoOfDay
                arguments(0L, 12345L, 1L, 0L, 1L),
                arguments(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
                arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1),
                arguments(MJD_1973_01_01, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1973_01_01, NANOS_PER_DAY - 1),
                // Set nano-of-day to the leap second on a leap day
                arguments(MJD_1972_12_31_LEAP, 0, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
                arguments(MJD_1972_12_31_LEAP, 0, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1)
        );
    }

    @ParameterizedTest(name = "MJD {0}, when nanos changed to {2}, becomes nanos {4}")
    @MethodSource("data_withNanoOfDay_success")
    public void withNanoOfDay_returnsUpdatedInstant(long mjd, long nanos, long newNanoOfDay, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.withNanoOfDay(newNanoOfDay);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    static Stream<Arguments> data_withNanoOfDay_failure() {
        return Stream.of(
                // mjd, nanoOfDay, newNanoOfDay (invalid for the given mjd)
                arguments(0L, 12345L, -1L), // Negative nanos are invalid
                // Set nano-of-day to a leap second on a non-leap day
                arguments(MJD_1972_12_30, 0, NANOS_PER_DAY),
                arguments(MJD_1973_01_01, 0, NANOS_PER_DAY),
                arguments(MJD_1972_12_30, 0, NANOS_PER_LEAP_DAY - 1),
                arguments(MJD_1973_01_01, 0, NANOS_PER_LEAP_DAY - 1),
                // Set nano-of-day beyond the leap second range
                arguments(MJD_1972_12_31_LEAP, 0, NANOS_PER_LEAP_DAY)
        );
    }

    @ParameterizedTest(name = "MJD {0}, when nanos changed to {2}, should fail")
    @MethodSource("data_withNanoOfDay_failure")
    public void withNanoOfDay_forInvalidNanos_throwsDateTimeException(long mjd, long nanos, long newNanoOfDay) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(newNanoOfDay));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------
    static Stream<Arguments> data_plus() {
        return Stream.of(
                // mjd, nanos, plusSeconds, plusNanos, expectedMjd, expectedNanos
                // --- Add zero ---
                arguments(0, 0, 0, 0, 0, 0),
                // --- No day crossing ---
                arguments(0, 0, 0, 1, 0, 1),
                arguments(0, 0, 1, 0, 0, NANOS_PER_SEC),
                arguments(0, 0, 3, 333333333, 0, 3 * NANOS_PER_SEC + 333333333),
                // --- Crossing to next day ---
                arguments(0, 0, SECS_PER_DAY, 0, 1, 0),
                arguments(0, 0, SECS_PER_DAY, 1, 1, 1),
                arguments(0, 0, 2 * SECS_PER_DAY, 5, 2, 5),
                // --- Crossing to previous day ---
                arguments(0, 0, 0, -1, -1, NANOS_PER_DAY - 1),
                arguments(0, 0, -SECS_PER_DAY, 0, -1, 0),
                arguments(0, 0, -SECS_PER_DAY, 1, -1, 1),
                // --- Start from non-zero MJD ---
                arguments(1, 0, SECS_PER_DAY, 0, 2, 0),
                arguments(1, 0, -SECS_PER_DAY, 0, 0, 0)
        );
    }

    @ParameterizedTest(name = "[{index}] plus({2}s, {3}ns)")
    @MethodSource("data_plus")
    public void plus_duration_returnsCorrectInstant(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration duration = Duration.ofSeconds(plusSeconds, plusNanos);
        UtcInstant result = initial.plus(duration);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------
    static Stream<Arguments> data_minus() {
        return Stream.of(
                // mjd, nanos, minusSeconds, minusNanos, expectedMjd, expectedNanos
                // --- Subtract zero ---
                arguments(0, 0, 0, 0, 0, 0),
                // --- No day crossing ---
                arguments(0, 0, 0, -1, 0, 1), // minus negative is plus
                arguments(0, 0, -1, 0, 0, NANOS_PER_SEC),
                arguments(0, 0, -3, -333333333, 0, 3 * NANOS_PER_SEC + 333333333),
                // --- Crossing to previous day ---
                arguments(0, 0, 0, 1, -1, NANOS_PER_DAY - 1),
                arguments(0, 0, SECS_PER_DAY, 0, -1, 0),
                arguments(0, 0, SECS_PER_DAY, -1, -1, 1),
                // --- Crossing to next day ---
                arguments(0, 0, -SECS_PER_DAY, 0, 1, 0),
                arguments(0, 0, -SECS_PER_DAY, -1, 1, 1),
                arguments(0, 0, -2 * SECS_PER_DAY, -5, 2, 5),
                // --- Start from non-zero MJD ---
                arguments(1, 0, SECS_PER_DAY, 0, 0, 0),
                arguments(1, 0, -SECS_PER_DAY, 0, 2, 0)
        );
    }

    @ParameterizedTest(name = "[{index}] minus({2}s, {3}ns)")
    @MethodSource("data_minus")
    public void minus_duration_returnsCorrectInstant(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration duration = Duration.ofSeconds(minusSeconds, minusNanos);
        UtcInstant result = initial.minus(duration);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    // toString() and parse()
    //-----------------------------------------------------------------------
    static Stream<Arguments> data_toStringAndParse() {
        return Stream.of(
                // mjd, nanoOfDay, expectedString
                arguments(40587, 0, "1970-01-01T00:00:00Z"),
                arguments(40588, 1, "1970-01-02T00:00:00.000000001Z"),
                arguments(40588, 999, "1970-01-02T00:00:00.000000999Z"),
                arguments(40588, 1000, "1970-01-02T00:00:00.000001Z"),
                arguments(40588, 999000, "1970-01-02T00:00:00.000999Z"),
                arguments(40588, 1000000, "1970-01-02T00:00:00.001Z"),
                arguments(40618, 999999999, "1970-02-01T00:00:00.999999999Z"),
                arguments(40619, NANOS_PER_SEC, "1970-02-02T00:00:01Z"),
                arguments(40620, 60 * NANOS_PER_SEC, "1970-02-03T00:01:00Z"),
                arguments(40621, 60 * 60 * NANOS_PER_SEC, "1970-02-04T01:00:00Z"),
                // Leap second cases
                arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
                arguments(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
                arguments(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z")
        );
    }

    @ParameterizedTest(name = "\"{2}\"")
    @MethodSource("data_toStringAndParse")
    public void toString_returnsCorrectIsoFormat(long mjd, long nanoOfDay, String expectedString) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        assertEquals(expectedString, instant.toString());
    }

    @ParameterizedTest(name = "\"{2}\"")
    @MethodSource("data_toStringAndParse")
    public void parse_ofToStringOutput_isReversible(long mjd, long nanoOfDay, String isoString) {
        UtcInstant expectedInstant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        UtcInstant parsedInstant = UtcInstant.parse(isoString);
        assertEquals(expectedInstant, parsedInstant);
    }

    //-----------------------------------------------------------------------
    // durationUntil()
    //-----------------------------------------------------------------------
    @Test
    public void durationUntil_forStandardDay_isCorrect() {
        UtcInstant startOfDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_30, 0);
        UtcInstant startOfNextDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);

        Duration duration = startOfDay.durationUntil(startOfNextDay);

        assertEquals(SECS_PER_DAY, duration.getSeconds());
        assertEquals(0, duration.getNano());
    }
}