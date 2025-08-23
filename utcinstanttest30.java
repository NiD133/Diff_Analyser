package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.testing.EqualsTester;
import java.time.DateTimeException;
import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("UtcInstant")
class UtcInstantTest {

    private static final long MJD_1972_12_30 = 41681;
    private static final long MJD_1972_12_31_LEAP = 41682; // A day with a leap second
    private static final long MJD_1973_01_01 = 41683;
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365; // Another leap second day

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------

    static Stream<Arguments> provider_invalidParseStrings() {
        return Stream.of(
            Arguments.of("", "Empty string"),
            Arguments.of("A", "Non-date-time string"),
            Arguments.of("2012-13-01T00:00:00Z", "Invalid month")
        );
    }

    @ParameterizedTest(name = "[{index}] {1}")
    @MethodSource("provider_invalidParseStrings")
    @DisplayName("parse() with invalid text throws exception")
    void parse_withInvalidText_throwsDateTimeException(String text, String description) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(text));
    }

    //-----------------------------------------------------------------------
    // withModifiedJulianDay()
    //-----------------------------------------------------------------------

    static Stream<Arguments> provider_withModifiedJulianDay_success() {
        return Stream.of(
            // Simple cases
            Arguments.of(0L, 12345L, 1L, 1L, 12345L),
            Arguments.of(0L, 12345L, -1L, -1L, 12345L),
            // Change from one leap day to another
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withModifiedJulianDay_success")
    @DisplayName("withModifiedJulianDay() returns updated instant")
    void withModifiedJulianDay_returnsUpdatedInstant(long mjd, long nanos, long newMjd, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.withModifiedJulianDay(newMjd);
        
        assertAll(
            () -> assertEquals(expectedMjd, result.getModifiedJulianDay()),
            () -> assertEquals(expectedNanos, result.getNanoOfDay())
        );
    }

    static Stream<Arguments> provider_withModifiedJulianDay_failure() {
        return Stream.of(
            // Try to set a non-leap day when nanoOfDay is on a leap second
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withModifiedJulianDay_failure")
    @DisplayName("withModifiedJulianDay() with invalid day for leap second throws exception")
    void withModifiedJulianDay_onLeapSecondWithInvalidNewDay_throwsDateTimeException(long mjd, long nanos, long newMjd) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjd));
    }

    //-----------------------------------------------------------------------
    // withNanoOfDay()
    //-----------------------------------------------------------------------

    static Stream<Arguments> provider_withNanoOfDay_success() {
        return Stream.of(
            // Simple case
            Arguments.of(0L, 12345L, 1L, 0L, 1L),
            // Set to a valid nano on a non-leap day
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
            // Set to a valid nano on a leap day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1),
            // Set to the leap second on a leap day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
            // Set to the last nano of a leap day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withNanoOfDay_success")
    @DisplayName("withNanoOfDay() returns updated instant")
    void withNanoOfDay_returnsUpdatedInstant(long mjd, long nanos, long newNanoOfDay, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.withNanoOfDay(newNanoOfDay);

        assertAll(
            () -> assertEquals(expectedMjd, result.getModifiedJulianDay()),
            () -> assertEquals(expectedNanos, result.getNanoOfDay())
        );
    }

    static Stream<Arguments> provider_withNanoOfDay_failure() {
        return Stream.of(
            // Negative nano-of-day
            Arguments.of(0L, 12345L, -1L),
            // Nano-of-day too large for a non-leap day
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_DAY),
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_LEAP_DAY - 1),
            Arguments.of(MJD_1972_12_30, 0L, NANOS_PER_LEAP_DAY),
            // Nano-of-day too large for a leap day
            Arguments.of(MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_withNanoOfDay_failure")
    @DisplayName("withNanoOfDay() with invalid value throws exception")
    void withNanoOfDay_withInvalidNanos_throwsDateTimeException(long mjd, long nanos, long newNanoOfDay) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(newNanoOfDay));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------

    static Stream<Arguments> provider_plusDuration() {
        return Stream.of(
            // Add zero
            Arguments.of(0L, 0L, Duration.ZERO, 0L, 0L),
            // Add nanos without day rollover
            Arguments.of(0L, 0L, Duration.ofNanos(1), 0L, 1L),
            // Add seconds without day rollover
            Arguments.of(0L, 0L, Duration.ofSeconds(1), 0L, NANOS_PER_SEC),
            // Add nanos causing day rollover
            Arguments.of(0L, NANOS_PER_DAY - 1, Duration.ofNanos(2), 1L, 1L),
            // Subtract nanos causing day rollover
            Arguments.of(0L, 0L, Duration.ofNanos(-1), -1L, NANOS_PER_DAY - 1),
            // Add days
            Arguments.of(0L, 5L, Duration.ofDays(2), 2L, 5L),
            // Subtract days
            Arguments.of(0L, 5L, Duration.ofDays(-2), -2L, 5L)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_plusDuration")
    @DisplayName("plus() adds duration correctly")
    void plus_addsDurationCorrectly(long mjd, long nanos, Duration duration, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.plus(duration);

        assertAll(
            () -> assertEquals(expectedMjd, result.getModifiedJulianDay()),
            () -> assertEquals(expectedNanos, result.getNanoOfDay())
        );
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------

    static Stream<Arguments> provider_minusDuration() {
        return Stream.of(
            // Subtract zero
            Arguments.of(0L, 0L, Duration.ZERO, 0L, 0L),
            // Subtract nanos without day rollover
            Arguments.of(0L, 1L, Duration.ofNanos(1), 0L, 0L),
            // Subtract seconds without day rollover
            Arguments.of(0L, NANOS_PER_SEC, Duration.ofSeconds(1), 0L, 0L),
            // Subtract nanos causing day rollover
            Arguments.of(0L, 0L, Duration.ofNanos(1), -1L, NANOS_PER_DAY - 1),
            // Add nanos by subtracting negative duration
            Arguments.of(0L, NANOS_PER_DAY - 1, Duration.ofNanos(-2), 1L, 1L),
            // Subtract days
            Arguments.of(2L, 5L, Duration.ofDays(2), 0L, 5L),
            // Add days by subtracting negative duration
            Arguments.of(0L, 5L, Duration.ofDays(-2), 2L, 5L)
        );
    }

    @ParameterizedTest
    @MethodSource("provider_minusDuration")
    @DisplayName("minus() subtracts duration correctly")
    void minus_subtractsDurationCorrectly(long mjd, long nanos, Duration duration, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.minus(duration);

        assertAll(
            () -> assertEquals(expectedMjd, result.getModifiedJulianDay()),
            () -> assertEquals(expectedNanos, result.getNanoOfDay())
        );
    }

    //-----------------------------------------------------------------------
    // toString() and parse()
    //-----------------------------------------------------------------------

    static Stream<Arguments> provider_toStringAndParseCases() {
        return Stream.of(
            Arguments.of(40587, 0, "1970-01-01T00:00:00Z"),
            Arguments.of(40588, 1, "1970-01-02T00:00:00.000000001Z"),
            Arguments.of(40588, 999, "1970-01-02T00:00:00.000000999Z"),
            Arguments.of(40588, 1000, "1970-01-02T00:00:00.000001Z"),
            Arguments.of(40618, 999999999, "1970-02-01T00:00:00.999999999Z"),
            Arguments.of(40619, NANOS_PER_SEC, "1970-02-02T00:00:01Z"),
            Arguments.of(40620, 60 * NANOS_PER_SEC, "1970-02-03T00:01:00Z"),
            Arguments.of(40621, 60 * 60 * NANOS_PER_SEC, "1970-02-04T01:00:00Z"),
            // Leap second cases
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
            Arguments.of(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z")
        );
    }

    @ParameterizedTest
    @MethodSource("provider_toStringAndParseCases")
    @DisplayName("toString() returns correct ISO-8601 format")
    void toString_returnsIso8601Format(long mjd, long nod, String expected) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nod);
        assertEquals(expected, instant.toString());
    }

    @ParameterizedTest
    @MethodSource("provider_toStringAndParseCases")
    @DisplayName("parse() correctly reverses toString()")
    void parse_reversesToString(long mjd, long nod, String str) {
        UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nod);
        assertEquals(expected, UtcInstant.parse(str));
    }

    //-----------------------------------------------------------------------
    // equals() and hashCode()
    //-----------------------------------------------------------------------

    @Test
    @DisplayName("equals() and hashCode() follow contract")
    void equals_and_hashCode_followContract() {
        new EqualsTester()
            .addEqualityGroup(
                UtcInstant.ofModifiedJulianDay(5L, 20),
                UtcInstant.ofModifiedJulianDay(5L, 20))
            .addEqualityGroup(
                UtcInstant.ofModifiedJulianDay(5L, 30),
                UtcInstant.ofModifiedJulianDay(5L, 30))
            .addEqualityGroup(
                UtcInstant.ofModifiedJulianDay(6L, 20),
                UtcInstant.ofModifiedJulianDay(6L, 20))
            .testEquals();
    }
}