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
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for modification, arithmetic, and formatting of {@link UtcInstant}.
 */
class UtcInstantModificationAndArithmeticTest {

    private static final long MJD_1972_12_30 = 41681;
    private static final long MJD_1972_12_31_LEAP = 41682;
    private static final long MJD_1973_01_01 = 41683;
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    // ofModifiedJulianDay()
    //-----------------------------------------------------------------------

    @Test
    @DisplayName("ofModifiedJulianDay() with a negative nano-of-day throws an exception")
    void ofModifiedJulianDay_withNegativeNanoOfDay_throwsException() {
        assertThrows(DateTimeException.class, () -> UtcInstant.ofModifiedJulianDay(MJD_1973_01_01, -1));
    }

    //-----------------------------------------------------------------------
    // parse()
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("provider_invalidStringsForParse")
    @DisplayName("parse() with an invalid string format throws an exception")
    void parse_withInvalidFormat_throwsDateTimeException(String invalidText) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidText));
    }

    private static Stream<Arguments> provider_invalidStringsForParse() {
        return Stream.of(
            Arguments.of(""),                  // Empty string
            Arguments.of("A"),                 // Not a date-time
            Arguments.of("2012-13-01T00:00:00Z") // Invalid month
        );
    }

    //-----------------------------------------------------------------------
    // withModifiedJulianDay()
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @CsvSource({
        "0, 12345, 1, 1, 12345",
        "0, 12345, -1, -1, 12345",
        "41682, 86400000000000, 42047, 42047, 86400000000000", // MJD_1972_12_31_LEAP to MJD_1973_12_31_LEAP
    })
    @DisplayName("withModifiedJulianDay() correctly sets a valid day")
    void withModifiedJulianDay_setsValidDay(long mjd, long nanoOfDay, long newMjd, long expectedMjd, long expectedNanoOfDay) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        UtcInstant result = initial.withModifiedJulianDay(newMjd);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanoOfDay, result.getNanoOfDay());
    }

    @Test
    @DisplayName("withModifiedJulianDay() throws exception when nano-of-day is invalid for the new day")
    void withModifiedJulianDay_toNonLeapDay_whenNanoOfDayIsInvalid_throwsException() {
        // This instant is 1972-12-31T23:59:60Z, the leap second.
        UtcInstant instantOnLeapDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY);

        // Try to set the day to a non-leap day, which makes the nano-of-day invalid.
        assertThrows(DateTimeException.class, () -> instantOnLeapDay.withModifiedJulianDay(MJD_1972_12_30));
    }

    //-----------------------------------------------------------------------
    // withNanoOfDay()
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @CsvSource({
        "0, 12345, 1, 0, 1",
        "41681, 86399999999999, 86399999999999, 41681, 86399999999999", // MJD_1972_12_30 (normal day)
        "41682, 86400000000000, 86400000000000, 41682, 86400000000000", // MJD_1972_12_31_LEAP (leap day)
    })
    @DisplayName("withNanoOfDay() correctly sets a valid nano-of-day")
    void withNanoOfDay_setsValidNano(long mjd, long nanoOfDay, long newNanoOfDay, long expectedMjd, long expectedNanoOfDay) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        UtcInstant result = initial.withNanoOfDay(newNanoOfDay);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanoOfDay, result.getNanoOfDay());
    }

    @Test
    @DisplayName("withNanoOfDay() throws exception for a value greater than or equal to a standard day's nanoseconds on a non-leap day")
    void withNanoOfDay_onNormalDay_withValueTooLarge_throwsException() {
        UtcInstant instantOnNormalDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_30, 0);
        assertThrows(DateTimeException.class, () -> instantOnNormalDay.withNanoOfDay(NANOS_PER_DAY));
    }

    @Test
    @DisplayName("withNanoOfDay() throws exception for a value greater than or equal to a leap day's nanoseconds on a leap day")
    void withNanoOfDay_onLeapDay_withValueTooLarge_throwsException() {
        UtcInstant instantOnLeapDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);
        assertThrows(DateTimeException.class, () -> instantOnLeapDay.withNanoOfDay(NANOS_PER_LEAP_DAY));
    }

    //-----------------------------------------------------------------------
    // plus()
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("provider_plus")
    @DisplayName("plus() correctly adds a duration")
    void plus_addsDurationCorrectly(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant start = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration toAdd = Duration.ofSeconds(plusSeconds, plusNanos);
        UtcInstant result = start.plus(toAdd);

        assertEquals(expectedMjd, result.getModifiedJulianDay(), "MJD mismatch");
        assertEquals(expectedNanos, result.getNanoOfDay(), "NanoOfDay mismatch");
    }

    /**
     * Provides arguments for plus(): startMjd, startNanos, plusSeconds, plusNanos, expectedMjd, expectedNanos.
     */
    private static Stream<Arguments> provider_plus() {
        return Stream.of(
            Arguments.of(0, 0, 0, 0, 0, 0),
            // Add nanoseconds
            Arguments.of(0, 0, 0, 2, 0, 2),
            // Subtract nanoseconds, crossing day boundary
            Arguments.of(0, 0, 0, -2, -1, NANOS_PER_DAY - 2),
            // Add seconds
            Arguments.of(0, 0, 3, 333333333, 0, 3 * NANOS_PER_SEC + 333333333),
            // Add days
            Arguments.of(0, 0, 2 * SECS_PER_DAY, 5, 2, 5),
            // Subtract days
            Arguments.of(0, 0, -2 * SECS_PER_DAY, 5, -2, 5)
        );
    }

    //-----------------------------------------------------------------------
    // minus()
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("provider_minus")
    @DisplayName("minus() correctly subtracts a duration")
    void minus_subtractsDurationCorrectly(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant start = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration toSubtract = Duration.ofSeconds(minusSeconds, minusNanos);
        UtcInstant result = start.minus(toSubtract);

        assertEquals(expectedMjd, result.getModifiedJulianDay(), "MJD mismatch");
        assertEquals(expectedNanos, result.getNanoOfDay(), "NanoOfDay mismatch");
    }

    /**
     * Provides arguments for minus(): startMjd, startNanos, minusSeconds, minusNanos, expectedMjd, expectedNanos.
     */
    private static Stream<Arguments> provider_minus() {
        return Stream.of(
            Arguments.of(0, 0, 0, 0, 0, 0),
            // Subtract nanoseconds
            Arguments.of(0, 0, 0, 2, -1, NANOS_PER_DAY - 2),
            // Add nanoseconds (by subtracting a negative)
            Arguments.of(0, 0, 0, -2, 0, 2),
            // Subtract seconds
            Arguments.of(0, 0, -3, -333333333, 0, 3 * NANOS_PER_SEC + 333333333),
            // Subtract days
            Arguments.of(0, 0, 2 * SECS_PER_DAY, -5, -2, 5),
            // Add days (by subtracting a negative)
            Arguments.of(0, 0, -2 * SECS_PER_DAY, -5, 2, 5)
        );
    }

    //-----------------------------------------------------------------------
    // toString() and parse() round-trip
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("provider_toString")
    @DisplayName("toString() returns the correct ISO-8601 representation")
    void toString_returnsCorrectIsoFormat(long mjd, long nanoOfDay, String expected) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        assertEquals(expected, instant.toString());
    }

    @ParameterizedTest
    @MethodSource("provider_toString")
    @DisplayName("parse() correctly parses a string generated by toString()")
    void parse_canParseStringFromToString(long mjd, long nanoOfDay, String text) {
        UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
        assertEquals(expected, UtcInstant.parse(text));
    }

    private static Stream<Arguments> provider_toString() {
        return Stream.of(
            Arguments.of(40587L, 0L, "1970-01-01T00:00:00Z"),
            Arguments.of(40588L, 1L, "1970-01-02T00:00:00.000000001Z"),
            Arguments.of(40588L, 999_999_999L, "1970-01-02T00:00:00.999999999Z"),
            Arguments.of(40619L, NANOS_PER_SEC, "1970-02-02T00:00:01Z"),
            // Leap second case
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"),
            Arguments.of(MJD_1973_01_01, 0L, "1973-01-01T00:00:00Z")
        );
    }
}