package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("UtcInstant modification, arithmetic, and parsing")
public class UtcInstantTestTest11 {

    // A day that has a leap second (1972-12-31).
    private static final long MJD_1972_12_31_LEAP = 41682;
    // A day before a leap-second day (1972-12-30).
    private static final long MJD_1972_12_30 = 41681;
    // A day after a leap-second day (1973-01-01).
    private static final long MJD_1973_01_01 = 41683;
    // Another leap-second day, one year after the first (1973-12-31).
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365;

    private static final long SECS_PER_DAY = 86400L;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    // The total nanoseconds in a day that includes a leap second.
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    @Nested
    @DisplayName("Factory method: of(Instant)")
    class OfInstant {
        @Test
        void ofInstant_throwsException_forNullInput() {
            assertThrows(NullPointerException.class, () -> UtcInstant.of((Instant) null));
        }
    }

    @Nested
    @DisplayName("Factory method: parse(CharSequence)")
    class Parse {
        public static Object[][] invalidStringProvider() {
            return new Object[][] {
                { "" },                      // Empty string
                { "A" },                     // Not a date-time
                { "2012-13-01T00:00:00Z" }   // Invalid month
            };
        }

        @ParameterizedTest
        @MethodSource("invalidStringProvider")
        void parse_throwsException_forInvalidFormat(String invalidString) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(invalidString));
        }
    }

    @Nested
    @DisplayName("Method: withModifiedJulianDay(long)")
    class WithModifiedJulianDay {
        @Test
        void setsModifiedJulianDay_whenNanoOfDayIsValidForNewDay() {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(0L, 12345L);
            UtcInstant result = initial.withModifiedJulianDay(2L);

            assertEquals(2L, result.getModifiedJulianDay());
            assertEquals(12345L, result.getNanoOfDay());
        }

        @Test
        void setsModifiedJulianDay_whenChangingFromOneLeapDayToAnother() {
            // Both 1972-12-31 and 1973-12-31 are leap days.
            // Create an instant at the leap second 23:59:60.
            UtcInstant instantOnLeapDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY);
            UtcInstant result = instantOnLeapDay.withModifiedJulianDay(MJD_1973_12_31_LEAP);

            assertEquals(MJD_1973_12_31_LEAP, result.getModifiedJulianDay());
            assertEquals(NANOS_PER_DAY, result.getNanoOfDay());
        }

        @Test
        void throwsException_whenChangingToNonLeapDayMakesNanoOfDayInvalid() {
            // 1972-12-31 is a leap day, so it can have a nano-of-day for 23:59:60.
            UtcInstant instantOnLeapDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, NANOS_PER_DAY);
            long normalDayMjd = MJD_1972_12_30; // Not a leap day

            // Attempting to set the day to a normal day while keeping the nano-of-day
            // from the leap second should fail, as that nano-of-day is out of bounds.
            assertThrows(DateTimeException.class, () -> instantOnLeapDay.withModifiedJulianDay(normalDayMjd));
        }
    }

    @Nested
    @DisplayName("Method: withNanoOfDay(long)")
    class WithNanoOfDay {
        @Test
        void setsNanoOfDay_whenValueIsValid() {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(7L, 12345L);
            UtcInstant result = initial.withNanoOfDay(2L);

            assertEquals(7L, result.getModifiedJulianDay());
            assertEquals(2L, result.getNanoOfDay());
        }

        @Test
        void setsNanoOfDay_toLeapSecondValue_onLeapDay() {
            UtcInstant instantOnLeapDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);
            UtcInstant result = instantOnLeapDay.withNanoOfDay(NANOS_PER_DAY); // 23:59:60

            assertEquals(MJD_1972_12_31_LEAP, result.getModifiedJulianDay());
            assertEquals(NANOS_PER_DAY, result.getNanoOfDay());
        }

        @Test
        void throwsException_forNegativeNanoOfDay() {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(0L, 12345L);
            assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(-1L));
        }

        @Test
        void throwsException_whenNanoOfDayExceedsNormalDayLength() {
            UtcInstant instantOnNormalDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_30, 0);
            // Attempting to set nano-of-day to a value that only exists on a leap day (23:59:60).
            assertThrows(DateTimeException.class, () -> instantOnNormalDay.withNanoOfDay(NANOS_PER_DAY));
        }

        @Test
        void throwsException_whenNanoOfDayExceedsLeapDayLength() {
            UtcInstant instantOnLeapDay = UtcInstant.ofModifiedJulianDay(MJD_1972_12_31_LEAP, 0);
            // Attempting to set nano-of-day to a value beyond the leap second.
            assertThrows(DateTimeException.class, () -> instantOnLeapDay.withNanoOfDay(NANOS_PER_LEAP_DAY));
        }
    }

    @Nested
    @DisplayName("Method: plus(Duration)")
    class PlusDuration {
        @Test
        void plus_smallPositiveDuration_doesNotChangeDay() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(0, 0);
            UtcInstant result = start.plus(Duration.ofSeconds(3, 333_333_333));

            assertEquals(0, result.getModifiedJulianDay());
            assertEquals(3 * NANOS_PER_SEC + 333_333_333, result.getNanoOfDay());
        }

        @Test
        void plus_durationOfExactlyOneDay_incrementsDay() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(1, 123);
            UtcInstant result = start.plus(Duration.ofSeconds(SECS_PER_DAY));

            assertEquals(2, result.getModifiedJulianDay());
            assertEquals(123, result.getNanoOfDay());
        }

        @Test
        void plus_durationThatCrossesDayBoundary_updatesDayAndNano() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(0, NANOS_PER_DAY - 1); // End of day 0
            UtcInstant result = start.plus(Duration.ofNanos(2)); // Add 2 nanos

            assertEquals(1, result.getModifiedJulianDay());
            assertEquals(1, result.getNanoOfDay());
        }

        @Test
        void plus_smallNegativeDuration_rollsBackToPreviousDay() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(0, 0);
            UtcInstant result = start.plus(Duration.ofNanos(-1));

            assertEquals(-1, result.getModifiedJulianDay());
            assertEquals(NANOS_PER_DAY - 1, result.getNanoOfDay());
        }

        @Test
        void plus_negativeDurationOfExactlyOneDay_decrementsDay() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(1, 123);
            UtcInstant result = start.plus(Duration.ofSeconds(-SECS_PER_DAY));

            assertEquals(0, result.getModifiedJulianDay());
            assertEquals(123, result.getNanoOfDay());
        }
    }

    @Nested
    @DisplayName("Method: minus(Duration)")
    class MinusDuration {
        @Test
        void minus_smallPositiveDuration_rollsBackToPreviousDay() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(0, 0);
            UtcInstant result = start.minus(Duration.ofNanos(1));

            assertEquals(-1, result.getModifiedJulianDay());
            assertEquals(NANOS_PER_DAY - 1, result.getNanoOfDay());
        }

        @Test
        void minus_durationOfExactlyOneDay_decrementsDay() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(1, 123);
            UtcInstant result = start.minus(Duration.ofSeconds(SECS_PER_DAY));

            assertEquals(0, result.getModifiedJulianDay());
            assertEquals(123, result.getNanoOfDay());
        }

        @Test
        void minus_smallNegativeDuration_doesNotChangeDay() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(0, 0);
            UtcInstant result = start.minus(Duration.ofSeconds(-3, -333_333_333));

            assertEquals(0, result.getModifiedJulianDay());
            assertEquals(3 * NANOS_PER_SEC + 333_333_333, result.getNanoOfDay());
        }

        @Test
        void minus_negativeDurationOfExactlyOneDay_incrementsDay() {
            UtcInstant start = UtcInstant.ofModifiedJulianDay(1, 123);
            UtcInstant result = start.minus(Duration.ofSeconds(-SECS_PER_DAY));

            assertEquals(2, result.getModifiedJulianDay());
            assertEquals(123, result.getNanoOfDay());
        }
    }

    @Nested
    @DisplayName("String conversion")
    class StringConversionAndParsing {
        public static Object[][] toStringAndParseCases() {
            return new Object[][] {
                { 40587L, 0L, "1970-01-01T00:00:00Z" },
                { 40588L, 1L, "1970-01-02T00:00:00.000000001Z" },
                { 40588L, 999_999_999L, "1970-01-02T00:00:00.999999999Z" },
                { 40619L, NANOS_PER_SEC, "1970-02-02T00:00:01Z" },
                // Test case for a leap second
                { MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z" },
                // Test case for the instant immediately after a leap second
                { MJD_1973_01_01, 0L, "1973-01-01T00:00:00Z" }
            };
        }

        @ParameterizedTest(name = "MJD {0}, NoD {1} -> \"{2}\"")
        @MethodSource("toStringAndParseCases")
        void toString_returnsCorrectIsoFormat(long mjd, long nanoOfDay, String expectedString) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
            assertEquals(expectedString, instant.toString());
        }

        @ParameterizedTest(name = "\"{2}\" -> MJD {0}, NoD {1}")
        @MethodSource("toStringAndParseCases")
        void parse_canParseStringsGeneratedByToString(long mjd, long nanoOfDay, String isoString) {
            UtcInstant expectedInstant = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);
            assertEquals(expectedInstant, UtcInstant.parse(isoString));
        }
    }
}