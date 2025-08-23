package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
 * Tests for {@link UtcInstant}.
 * This class focuses on modification, arithmetic, parsing, and formatting.
 */
public class UtcInstantTest {

    private static final long MJD_1972_12_30 = 41681;
    private static final long MJD_1972_12_31_LEAP = 41682; // A day with a leap second
    private static final long MJD_1973_01_01 = 41683;
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365; // Not a leap second day

    private static final long SECS_PER_DAY = 86400L;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    // parse(CharSequence)
    //-----------------------------------------------------------------------

    static Stream<String> invalidStringsForParsing() {
        return Stream.of(
                "",
                "A", // Not a date-time
                "2012-13-01T00:00:00Z" // Invalid month
        );
    }

    @ParameterizedTest
    @MethodSource("invalidStringsForParsing")
    @DisplayName("Test parse() with invalid strings")
    void parse_withInvalidString_throwsDateTimeException(String invalidString) {
        assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
    }

    //-----------------------------------------------------------------------
    // withModifiedJulianDay(long)
    //-----------------------------------------------------------------------

    static Stream<Arguments> data_withModifiedJulianDay_valid() {
        return Stream.of(
                // initialMjd, initialNanos, newMjd, expectedMjd, expectedNanos
                Arguments.of(0L, 12345L, 1L, 1L, 12345L),
                Arguments.of(0L, 12345L, -1L, -1L, 12345L),
                Arguments.of(7L, 12345L, 2L, 2L, 12345L),
                // On a leap second, setting MJD to the same day is valid
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
                // On a leap second, setting MJD to another non-leap-second day is valid
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("data_withModifiedJulianDay_valid")
    @DisplayName("Test withModifiedJulianDay() with valid day")
    void withModifiedJulianDay_givenValidDay_returnsUpdatedInstant(long mjd, long nanos, long newMjd, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.withModifiedJulianDay(newMjd);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    static Stream<Arguments> data_withModifiedJulianDay_invalid() {
        return Stream.of(
                // On a leap second, changing to a day that is not a leap day causes an exception
                // because the nano-of-day (NANOS_PER_DAY) is too large for a normal day.
                // initialMjd, initialNanos, newMjd
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
        );
    }

    @ParameterizedTest
    @MethodSource("data_withModifiedJulianDay_invalid")
    @DisplayName("Test withModifiedJulianDay() with invalid day for current nanos")
    void withModifiedJulianDay_givenInvalidDayForNanos_throwsException(long mjd, long nanos, long newMjd) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjd));
    }

    //-----------------------------------------------------------------------
    // withNanoOfDay(long)
    //-----------------------------------------------------------------------

    static Stream<Arguments> data_withNanoOfDay_valid() {
        return Stream.of(
                // mjd, nanos, newNanoOfDay, expectedMjd, expectedNanos
                Arguments.of(0L, 12345L, 1L, 0L, 1L),
                Arguments.of(7L, 12345L, 2L, 7L, 2L),
                // Test boundaries on a normal day
                Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
                // Test boundaries on a leap day
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1)
        );
    }

    @ParameterizedTest
    @MethodSource("data_withNanoOfDay_valid")
    @DisplayName("Test withNanoOfDay() with valid nanos")
    void withNanoOfDay_givenValidNanos_returnsUpdatedInstant(long mjd, long nanos, long newNanoOfDay, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        UtcInstant result = initial.withNanoOfDay(newNanoOfDay);
        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    static Stream<Arguments> data_withNanoOfDay_invalid() {
        return Stream.of(
                // mjd, nanos, newNanoOfDay
                Arguments.of(0L, 12345L, -1L), // Negative nanos are invalid
                // Nanos out of bounds for a normal day
                Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY),
                Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY),
                // Nanos out of bounds for a leap day
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY)
        );
    }

    @ParameterizedTest
    @MethodSource("data_withNanoOfDay_invalid")
    @DisplayName("Test withNanoOfDay() with invalid nanos")
    void withNanoOfDay_givenInvalidNanos_throwsException(long mjd, long nanos, long newNanoOfDay) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(newNanoOfDay));
    }

    //-----------------------------------------------------------------------
    // plus(Duration)
    //-----------------------------------------------------------------------

    @DisplayName("Test plus(Duration)")
    @ParameterizedTest(name = "MJD={0}, Nanos={1} plus ({2}s, {3}ns) -> MJD={4}, Nanos={5}")
    @CsvSource({
            // initialMjd, initialNanos, plusSeconds, plusNanos, expectedMjd, expectedNanos
            "0, 0, -172800, 5, -2, 5",                                  // -2 days
            "0, 0, -86400, 1, -1, 1",                                   // -1 day
            "0, 0, 0, -1, -1, 86399999999999",                          // NANOS_PER_DAY - 1
            "0, 0, 0, 0, 0, 0",
            "0, 0, 0, 1, 0, 1",
            "0, 0, 1, 0, 0, 1000000000",                                // 1s
            "0, 0, 86400, 0, 1, 0",                                     // +1 day
            "1, 0, -86400, 1, 0, 1",
            "1, 0, 0, -1, 0, 86399999999999",
            "1, 0, 0, 0, 1, 0",
            "1, 0, 86400, 1, 2, 1",
    })
    void plus_addsDurationCorrectly(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration durationToAdd = Duration.ofSeconds(plusSeconds, plusNanos);

        UtcInstant result = initial.plus(durationToAdd);

        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    // minus(Duration)
    //-----------------------------------------------------------------------

    @DisplayName("Test minus(Duration)")
    @ParameterizedTest(name = "MJD={0}, Nanos={1} minus ({2}s, {3}ns) -> MJD={4}, Nanos={5}")
    @CsvSource({
            // initialMjd, initialNanos, minusSeconds, minusNanos, expectedMjd, expectedNanos
            "0, 0, 172800, -5, -2, 5",
            "0, 0, 86400, -1, -1, 1",
            "0, 0, 0, 1, -1, 86399999999999",
            "0, 0, 0, 0, 0, 0",
            "0, 0, 0, -1, 0, 1",
            "0, 0, -1, 0, 0, 1000000000",
            "0, 0, -86400, 0, 1, 0",
            "1, 0, 86400, -1, 0, 1",
            "1, 0, 0, 1, 0, 86399999999999",
            "1, 0, 0, 0, 1, 0",
            "1, 0, -86400, -1, 2, 1",
    })
    void minus_subtractsDurationCorrectly(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
        UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
        Duration durationToSubtract = Duration.ofSeconds(minusSeconds, minusNanos);

        UtcInstant result = initial.minus(durationToSubtract);

        assertEquals(expectedMjd, result.getModifiedJulianDay());
        assertEquals(expectedNanos, result.getNanoOfDay());
    }

    //-----------------------------------------------------------------------
    // toString() and parse()
    //-----------------------------------------------------------------------

    @DisplayName("Test toString() and parse() are inverse operations")
    @ParameterizedTest(name = "MJD={0}, Nanos={1} <-> \"{2}\"")
    @CsvSource({
            // mjd, nanoOfDay, expectedString
            "40587, 0, '1970-01-01T00:00:00Z'",
            "40588, 1, '1970-01-02T00:00:00.000000001Z'",
            "40588, 999, '1970-01-02T00:00:00.000000999Z'",
            "40588, 1000, '1970-01-02T00:00:00.000001Z'",
            "40619, 1000000000, '1970-02-02T00:00:01Z'",
            "41682, 86399000000000, '1972-12-31T23:59:59Z'",       // MJD_1972_12_31_LEAP, 24*60*60*1e9 - 1e9
            "41682, 86400000000000, '1972-12-31T23:59:60Z'",       // MJD_1972_12_31_LEAP, NANOS_PER_DAY
            "41683, 0, '1973-01-01T00:00:00Z'",
    })
    void toString_and_parse_areInverseOperations(long mjd, long nod, String expectedString) {
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nod);

        // Test toString()
        assertEquals(expectedString, instant.toString());

        // Test parse()
        assertEquals(instant, UtcInstant.parse(expectedString));
    }

    //-----------------------------------------------------------------------
    // ofModifiedJulianDay(long, long)
    //-----------------------------------------------------------------------

    @Test
    @DisplayName("Test ofModifiedJulianDay() on a leap second boundary")
    void ofModifiedJulianDay_onLeapSecondBoundary_createsValidInstant() {
        // This instant represents the leap second 23:59:60 on 1972-12-31
        UtcInstant instant = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY);

        assertEquals(MJD_1972_12_31_LEAP, instant.getModifiedJulianDay());
        assertEquals(NANOS_PER_DAY, instant.getNanoOfDay());
        assertTrue(instant.isLeapSecond(), "Instant should be a leap second");
        assertEquals("1972-12-31T23:59:60Z", instant.toString());
    }
}