package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link UtcInstant}.
 */
class UtcInstantTest {

    // A day before a leap second day
    private static final long MJD_1972_12_30 = 41681;
    // A day with a leap second, making it 86401 seconds long
    private static final long MJD_1972_12_31_LEAP = 41682;
    // A day after a leap second day
    private static final long MJD_1973_01_01 = 41683;
    // A non-leap day one year after the 1972 leap day, for testing adjustments across years
    private static final long MJD_1973_12_31 = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 86400L;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    //-----------------------------------------------------------------------
    //- Factory methods
    //-----------------------------------------------------------------------
    @Nested
    class FactoryMethods {

        @Test
        void ofInstant_createsCorrectUtcInstant() {
            // 1970-01-01 is MJD 40587
            UtcInstant test = UtcInstant.of(Instant.ofEpochSecond(0, 2));
            assertEquals(40587, test.getModifiedJulianDay());
            assertEquals(2, test.getNanoOfDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#provideInvalidParseStrings")
        void parse_withInvalidFormat_throwsDateTimeException(String text) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(text));
        }
    }

    static Stream<Arguments> provideInvalidParseStrings() {
        return Stream.of(
            Arguments.of(""),
            Arguments.of("A"), // Not a date
            Arguments.of("2012-13-01T00:00:00Z") // Invalid month
        );
    }

    //-----------------------------------------------------------------------
    //- withModifiedJulianDay()
    //-----------------------------------------------------------------------
    @Nested
    class WithModifiedJulianDay {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#provideValidModifiedJulianDayArgs")
        void withModifiedJulianDay_givenValidDay_returnsUpdatedInstant(
            long initialMjd, long initialNanos, long newMjd, long expectedMjd, long expectedNanos) {

            UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            UtcInstant result = initial.withModifiedJulianDay(newMjd);

            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#provideInvalidModifiedJulianDayArgs")
        void withModifiedJulianDay_givenInvalidDayForNanos_throwsDateTimeException(
            long initialMjd, long initialNanos, long newMjd) {

            UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjd));
        }
    }

    static Stream<Arguments> provideValidModifiedJulianDayArgs() {
        return Stream.of(
            // initialMjd, initialNanos, newMjd, expectedMjd, expectedNanos
            Arguments.of(0L, 12345L, 1L, 1L, 12345L),
            Arguments.of(0L, 12345L, -1L, -1L, 12345L),
            Arguments.of(7L, 12345L, 2L, 2L, 12345L),
            Arguments.of(7L, 12345L, -2L, -2L, 12345L),
            // Nano-of-day is valid for both leap and non-leap days
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31, MJD_1973_12_31, NANOS_PER_DAY)
        );
    }

    static Stream<Arguments> provideInvalidModifiedJulianDayArgs() {
        return Stream.of(
            // initialMjd, initialNanos, newMjd (that will cause exception)
            // Nano-of-day is the leap second, but new day is not a leap day
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_30),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1, MJD_1973_01_01)
        );
    }

    //-----------------------------------------------------------------------
    //- withNanoOfDay()
    //-----------------------------------------------------------------------
    @Nested
    class WithNanoOfDay {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#provideValidNanoOfDayArgs")
        void withNanoOfDay_givenValidNanos_returnsUpdatedInstant(
            long initialMjd, long initialNanos, long newNanos, long expectedMjd, long expectedNanos) {

            UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            UtcInstant result = initial.withNanoOfDay(newNanos);

            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#provideInvalidNanoOfDayArgs")
        void withNanoOfDay_givenInvalidNanos_throwsDateTimeException(
            long initialMjd, long initialNanos, long newNanos) {

            UtcInstant initial = UtcInstant.ofModifiedJulianDay(initialMjd, initialNanos);
            assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(newNanos));
        }
    }

    static Stream<Arguments> provideValidNanoOfDayArgs() {
        return Stream.of(
            // initialMjd, initialNanos, newNanos, expectedMjd, expectedNanos
            Arguments.of(0L, 12345L, 1L, 0L, 1L),
            Arguments.of(7L, 12345L, 2L, 7L, 2L),
            Arguments.of(-99L, 12345L, 3L, -99L, 3L),
            // Test with max valid nanos for a standard day
            Arguments.of(MJD_1972_12_30, NANOS_PER_DAY - 1, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
            // Test with max valid nanos for a leap day
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - 1, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1),
            // Test setting nanos to the leap second itself
            Arguments.of(MJD_1972_12_31_LEAP, 0, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY)
        );
    }

    static Stream<Arguments> provideInvalidNanoOfDayArgs() {
        return Stream.of(
            // initialMjd, initialNanos, newNanos
            Arguments.of(0L, 12345L, -1L), // Negative nanos are invalid
            // Nanos exceed length of a standard day
            Arguments.of(MJD_1972_12_30, 0, NANOS_PER_DAY),
            Arguments.of(MJD_1973_01_01, 0, NANOS_PER_DAY),
            // Nanos exceed length of a leap day
            Arguments.of(MJD_1972_12_31_LEAP, 0, NANOS_PER_LEAP_DAY)
        );
    }

    //-----------------------------------------------------------------------
    //- plus(Duration) and minus(Duration)
    //-----------------------------------------------------------------------
    @Nested
    class Arithmetic {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#providePlusDurations")
        void plus_addsDurationCorrectly(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            Duration duration = Duration.ofSeconds(plusSeconds, plusNanos);
            UtcInstant result = initial.plus(duration);

            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#provideMinusDurations")
        void minus_subtractsDurationCorrectly(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            Duration duration = Duration.ofSeconds(minusSeconds, minusNanos);
            UtcInstant result = initial.minus(duration);

            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }
    }

    static Stream<Arguments> providePlusDurations() {
        return Stream.of(
            // mjd, nanos, plusSeconds, plusNanos, expectedMjd, expectedNanos
            // --- Additions to an instant at MJD 0, 0 nanos ---
            // Negative duration, crossing day boundaries
            Arguments.of(0, 0, -2 * SECS_PER_DAY, 5, -2, 5),
            Arguments.of(0, 0, -1 * SECS_PER_DAY, 1, -1, 1),
            // Negative duration, within the same day
            Arguments.of(0, 0, 0, -2, -1, NANOS_PER_DAY - 2),
            Arguments.of(0, 0, 0, -1, -1, NANOS_PER_DAY - 1),
            // Zero duration
            Arguments.of(0, 0, 0, 0, 0, 0),
            // Positive duration, nanos only
            Arguments.of(0, 0, 0, 1, 0, 1),
            // Positive duration, crossing day boundaries
            Arguments.of(0, 0, SECS_PER_DAY, 1, 1, 1),
            Arguments.of(0, 0, 2 * SECS_PER_DAY, 5, 2, 5),
            // --- Additions to an instant at MJD 1, 0 nanos ---
            Arguments.of(1, 0, -2 * SECS_PER_DAY, 5, -1, 5),
            Arguments.of(1, 0, -1 * SECS_PER_DAY, 1, 0, 1),
            Arguments.of(1, 0, 0, -1, 0, NANOS_PER_DAY - 1),
            Arguments.of(1, 0, 0, 0, 1, 0),
            Arguments.of(1, 0, 0, 1, 1, 1),
            Arguments.of(1, 0, SECS_PER_DAY, 1, 2, 1)
        );
    }

    static Stream<Arguments> provideMinusDurations() {
        return Stream.of(
            // mjd, nanos, minusSeconds, minusNanos, expectedMjd, expectedNanos
            // --- Subtractions from an instant at MJD 0, 0 nanos ---
            // Positive duration, crossing day boundaries
            Arguments.of(0, 0, 2 * SECS_PER_DAY, -5, -2, 5),
            Arguments.of(0, 0, SECS_PER_DAY, -1, -1, 1),
            // Positive duration, within the same day
            Arguments.of(0, 0, 0, 2, -1, NANOS_PER_DAY - 2),
            Arguments.of(0, 0, 0, 1, -1, NANOS_PER_DAY - 1),
            // Zero duration
            Arguments.of(0, 0, 0, 0, 0, 0),
            // Negative duration, nanos only
            Arguments.of(0, 0, 0, -1, 0, 1),
            // Negative duration, crossing day boundaries
            Arguments.of(0, 0, -SECS_PER_DAY, -1, 1, 1),
            Arguments.of(0, 0, -2 * SECS_PER_DAY, -5, 2, 5),
            // --- Subtractions from an instant at MJD 1, 0 nanos ---
            Arguments.of(1, 0, 2 * SECS_PER_DAY, -5, -1, 5),
            Arguments.of(1, 0, SECS_PER_DAY, -1, 0, 1),
            Arguments.of(1, 0, 0, 1, 0, NANOS_PER_DAY - 1),
            Arguments.of(1, 0, 0, 0, 1, 0),
            Arguments.of(1, 0, 0, -1, 1, 1),
            Arguments.of(1, 0, -SECS_PER_DAY, -1, 2, 1)
        );
    }

    //-----------------------------------------------------------------------
    //- toString() and parse() round-trip
    //-----------------------------------------------------------------------
    @Nested
    class StringConversion {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#provideInstantsForToString")
        void toString_returnsCorrectIsoFormat(long mjd, long nanoOfDay, String expected) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
            assertEquals(expected, instant.toString());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.scale.UtcInstantTest#provideInstantsForToString")
        void parse_ofToStringResult_recreatesOriginalInstant(long mjd, long nanoOfDay, String textToParse) {
            UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
            assertEquals(expected, UtcInstant.parse(textToParse));
        }
    }

    static Stream<Arguments> provideInstantsForToString() {
        return Stream.of(
            // mjd, nanoOfDay, expectedString
            Arguments.of(40587, 0, "1970-01-01T00:00:00Z"), // Unix epoch start
            Arguments.of(40588, 1, "1970-01-02T00:00:00.000000001Z"),
            Arguments.of(40588, 999, "1970-01-02T00:00:00.000000999Z"),
            Arguments.of(40588, 1000, "1970-01-02T00:00:00.000001Z"),
            Arguments.of(40588, 999000, "1970-01-02T00:00:00.000999Z"),
            Arguments.of(40588, 1000000, "1970-01-02T00:00:00.001Z"),
            Arguments.of(40618, 999999999, "1970-02-01T00:00:00.999999999Z"),
            Arguments.of(40619, NANOS_PER_SEC, "1970-02-02T00:00:01Z"),
            Arguments.of(40620, 60 * NANOS_PER_SEC, "1970-02-03T00:01:00Z"),
            Arguments.of(40621, 60 * 60 * NANOS_PER_SEC, "1970-02-04T01:00:00Z"),
            // Leap second handling
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY - NANOS_PER_SEC, "1972-12-31T23:59:59Z"),
            Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"), // The leap second
            Arguments.of(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z") // Day after leap second
        );
    }
}