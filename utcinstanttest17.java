package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Test class for modification, parsing, and formatting of {@link UtcInstant}.
 */
public class UtcInstantModificationAndParsingTest {

    // A normal day, used for testing.
    private static final long MJD_NORMAL_DAY = 41681; // 1972-12-30
    // A day with a leap second.
    private static final long MJD_LEAP_DAY = 41682; // 1972-12-31
    // A normal day following a leap day.
    private static final long MJD_DAY_AFTER_LEAP = 41683; // 1973-01-01
    // Another leap day, for testing transitions between leap days.
    private static final long MJD_ANOTHER_LEAP_DAY = MJD_LEAP_DAY + 365; // 1973-12-31

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @ValueSource(strings = {"", "A", "2012-13-01T00:00:00Z"})
    void parse_withInvalidFormat_throwsDateTimeException(String invalidText) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidText));
    }

    //-----------------------------------------------------------------------
    // withModifiedJulianDay()
    //-----------------------------------------------------------------------
    static Stream<Arguments> data_withModifiedJulianDay_valid() {
        return Stream.of(
                Arguments.of("Simple positive change", 0L, 12345L, 1L, 1L, 12345L),
                Arguments.of("Simple negative change", 0L, 12345L, -1L, -1L, 12345L),
                Arguments.of("No-op on leap day", MJD_LEAP_DAY, NANOS_PER_DAY, MJD_LEAP_DAY, MJD_LEAP_DAY, NANOS_PER_DAY),
                Arguments.of("Move from one leap day to another", MJD_LEAP_DAY, NANOS_PER_DAY, MJD_ANOTHER_LEAP_DAY, MJD_ANOTHER_LEAP_DAY, NANOS_PER_DAY)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("data_withModifiedJulianDay_valid")
    void withModifiedJulianDay_whenNewDayIsValid_returnsUpdatedInstant(
            String testCase, long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
        UtcInstant result = initial.withModifiedJulianDay(newMjd);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    static Stream<Arguments> data_withModifiedJulianDay_invalid() {
        UtcInstant instantOnLeapSecond = UtcInstant.ofModifiedJulianDay(MJD_LEAP_DAY, NANOS_PER_DAY);
        return Stream.of(
                Arguments.of("Move from leap day to previous non-leap day", instantOnLeapSecond, MJD_NORMAL_DAY),
                Arguments.of("Move from leap day to next non-leap day", instantOnLeapSecond, MJD_DAY_AFTER_LEAP)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("data_withModifiedJulianDay_invalid")
    void withModifiedJulianDay_whenNanoOfDayIsInvalidForNewDay_throwsException(
            String testCase, UtcInstant initial, long newMjd) {
        assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjd));
    }

    //-----------------------------------------------------------------------
    // withNanoOfDay()
    //-----------------------------------------------------------------------
    static Stream<Arguments> data_withNanoOfDay_valid() {
        return Stream.of(
                Arguments.of("Simple change", 0L, 1L),
                Arguments.of("Max nanos on normal day", MJD_NORMAL_DAY, NANOS_PER_DAY - 1),
                Arguments.of("Leap second nano on leap day", MJD_LEAP_DAY, NANOS_PER_DAY),
                Arguments.of("Max nanos on leap day", MJD_LEAP_DAY, NANOS_PER_LEAP_DAY - 1)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("data_withNanoOfDay_valid")
    void withNanoOfDay_whenValueIsValidForDay_returnsUpdatedInstant(String testCase, long mjd, long newNanoOfDay) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, 0);
        UtcInstant result = initial.withNanoOfDay(newNanoOfDay);
        assertEquals(mjd, result.getModifiedJulianDay());
        assertEquals(newNanoOfDay, result.getNanoOfDay());
    }

    static Stream<Arguments> data_withNanoOfDay_invalid() {
        return Stream.of(
                Arguments.of("Negative nano-of-day on normal day", MJD_NORMAL_DAY, -1L),
                Arguments.of("Nano-of-day for leap second on a normal day", MJD_NORMAL_DAY, NANOS_PER_DAY),
                Arguments.of("Nano-of-day greater than leap second on a normal day", MJD_NORMAL_DAY, NANOS_PER_LEAP_DAY - 1),
                Arguments.of("Nano-of-day greater than leap second on a leap day", MJD_LEAP_DAY, NANOS_PER_LEAP_DAY)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("data_withNanoOfDay_invalid")
    void withNanoOfDay_whenValueIsInvalidForDay_throwsException(String testCase, long mjd, long newNanoOfDay) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, 0);
        assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(newNanoOfDay));
    }

    //-----------------------------------------------------------------------
    // plus()
    //-----------------------------------------------------------------------
    static Stream<Arguments> data_plus() {
        return Stream.of(
                Arguments.of("Add zero", 0L, 0L, Duration.ZERO, 0L, 0L),
                Arguments.of("Add nanos, no second rollover", 0L, 0L, Duration.ofNanos(1), 0L, 1L),
                Arguments.of("Add seconds, no day rollover", 0L, 0L, Duration.ofSeconds(1), 0L, NANOS_PER_SEC),
                Arguments.of("Add seconds and nanos", 0L, 0L, Duration.ofSeconds(3, 333_333_333), 0L, 3 * NANOS_PER_SEC + 333_333_333),
                Arguments.of("Add nanos, causing day rollover", 0L, NANOS_PER_DAY - 1, Duration.ofNanos(1), 1L, 0L),
                Arguments.of("Add seconds, causing day rollover", 0L, 0L, Duration.ofSeconds(SECS_PER_DAY), 1L, 0L),
                Arguments.of("Subtract nanos, causing day rollover", 0L, 0L, Duration.ofNanos(-1), -1L, NANOS_PER_DAY - 1),
                Arguments.of("Subtract seconds, causing day rollover", 0L, 0L, Duration.ofSeconds(-SECS_PER_DAY), -1L, 0L),
                Arguments.of("From non-zero MJD, add seconds causing day rollover", 1L, 0L, Duration.ofSeconds(SECS_PER_DAY, 5), 2L, 5L)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("data_plus")
    void plus_addsDuration_returnsCorrectInstant(
            String testCase, long mjd, long nanos, Duration duration, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.plus(duration);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    @Test
    void plus_onMaxInstant_throwsArithmeticException() {
        UtcInstant maxInstant = UtcInstant.ofModifiedJulianDay(Long.MAX_VALUE, NANOS_PER_DAY - 1);
        assertThrows(ArithmeticException.class, () -> maxInstant.plus(Duration.ofNanos(1)));
    }

    //-----------------------------------------------------------------------
    // minus()
    //-----------------------------------------------------------------------
    static Stream<Arguments> data_minus() {
        return Stream.of(
                Arguments.of("Subtract zero", 0L, 0L, Duration.ZERO, 0L, 0L),
                Arguments.of("Subtract nanos, no second rollover", 0L, 1L, Duration.ofNanos(1), 0L, 0L),
                Arguments.of("Subtract seconds, no day rollover", 0L, NANOS_PER_SEC, Duration.ofSeconds(1), 0L, 0L),
                Arguments.of("Subtract seconds and nanos", 1L, 3 * NANOS_PER_SEC + 333_333_333, Duration.ofSeconds(3, 333_333_333), 1L, 0L),
                Arguments.of("Subtract nanos, causing day rollover", 1L, 0L, Duration.ofNanos(1), 0L, NANOS_PER_DAY - 1),
                Arguments.of("Subtract seconds, causing day rollover", 1L, 0L, Duration.ofSeconds(SECS_PER_DAY), 0L, 0L),
                Arguments.of("Add nanos (by subtracting negative), causing day rollover", 0L, NANOS_PER_DAY - 1, Duration.ofNanos(-1), 1L, 0L),
                Arguments.of("Add seconds (by subtracting negative), causing day rollover", -1L, 0L, Duration.ofSeconds(-SECS_PER_DAY), 0L, 0L)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("data_minus")
    void minus_subtractsDuration_returnsCorrectInstant(
            String testCase, long mjd, long nanos, Duration duration, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.minus(duration);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    // toString() and parse()
    //-----------------------------------------------------------------------
    @ParameterizedTest
    @CsvSource({
            "40587, 0, '1970-01-01T00:00:00Z'",
            "40588, 1, '1970-01-02T00:00:00.000000001Z'",
            "40588, 999, '1970-01-02T00:00:00.000000999Z'",
            "40588, 1000, '1970-01-02T00:00:00.000001Z'",
            "40588, 999000, '1970-01-02T00:00:00.000999Z'",
            "40588, 1000000, '1970-01-02T00:00:00.001Z'",
            "40618, 999999999, '1970-02-01T00:00:00.999999999Z'",
            "40619, 1000000000, '1970-02-02T00:00:01Z'",             // 1_000_000_000 nanos = 1 sec
            "40620, 60000000000, '1970-02-03T00:01:00Z'",            // 60 * 1e9 nanos = 1 min
            "40621, 3600000000000, '1970-02-04T01:00:00Z'",          // 3600 * 1e9 nanos = 1 hour
            "41682, 86399000000000, '1972-12-31T23:59:59Z'",         // MJD_LEAP_DAY, last nano before leap second
            "41682, 86400000000000, '1972-12-31T23:59:60Z'",         // MJD_LEAP_DAY, during leap second
            "41683, 0, '1973-01-01T00:00:00Z'",                      // MJD_DAY_AFTER_LEAP
    })
    void toString_returnsCorrectIsoFormat(long mjd, long nanoOfDay, String expected) {
        assertEquals(expected, UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay).toString());
    }

    @ParameterizedTest
    @CsvSource({
            "40587, 0, '1970-01-01T00:00:00Z'",
            "41682, 86400000000000, '1972-12-31T23:59:60Z'", // MJD_LEAP_DAY, during leap second
            "41683, 0, '1973-01-01T00:00:00Z'",
    })
    void parse_isInverseOfToString(long mjd, long nanoOfDay, String text) {
        UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        assertEquals(expected, UtcInstant.parse(text));
    }
}