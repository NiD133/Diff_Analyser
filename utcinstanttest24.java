package org.threeten.extra.scale;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.Duration;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("UtcInstant Modification and Arithmetic")
class UtcInstantModificationAndArithmeticTest {

    private static final long MJD_1972_12_30 = 41681;
    private static final long MJD_1972_12_31_LEAP = 41682; // A day with a leap second
    private static final long MJD_1973_01_01 = 41683;
    private static final long MJD_1973_12_31_LEAP = MJD_1972_12_31_LEAP + 365; // Not a leap day

    private static final long SECS_PER_DAY = 24L * 60 * 60;
    private static final long NANOS_PER_SEC = 1_000_000_000L;
    private static final long NANOS_PER_DAY = SECS_PER_DAY * NANOS_PER_SEC;
    private static final long NANOS_PER_LEAP_DAY = (SECS_PER_DAY + 1) * NANOS_PER_SEC;

    @Nested
    @DisplayName("parse()")
    class ParseTests {

        static Stream<Arguments> invalidTextForParsing() {
            return Stream.of(
                Arguments.of(""),
                Arguments.of("A"), // Not a date-time
                Arguments.of("2012-13-01T00:00:00Z") // Invalid month
            );
        }

        @ParameterizedTest(name = "[{index}] parsing \"{0}\"")
        @MethodSource("invalidTextForParsing")
        void parse_withInvalidText_throwsException(String text) {
            assertThrows(DateTimeException.class, () -> UtcInstant.parse(text));
        }
    }

    @Nested
    @DisplayName("withModifiedJulianDay()")
    class WithModifiedJulianDayTests {

        static Stream<Arguments> validMjdChanges() {
            return Stream.of(
                Arguments.of("Positive MJD", 7L, 12345L, 2L, 2L, 12345L),
                Arguments.of("Negative MJD", -99L, 12345L, 3L, 3L, 12345L),
                Arguments.of("Leap day to leap day", MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_31_LEAP, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
                Arguments.of("Leap day to another leap day", MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_12_31_LEAP, MJD_1973_12_31_LEAP, NANOS_PER_DAY)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("validMjdChanges")
        void withModifiedJulianDay_givenValidDay_returnsUpdatedInstant(
            String description, long mjd, long nanos, long newMjd, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            UtcInstant result = initial.withModifiedJulianDay(newMjd);
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        static Stream<Arguments> invalidMjdChanges() {
            return Stream.of(
                Arguments.of("Leap second nano-of-day to non-leap day", MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1972_12_30),
                Arguments.of("Leap second nano-of-day to another non-leap day", MJD_1972_12_31_LEAP, NANOS_PER_DAY, MJD_1973_01_01)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidMjdChanges")
        void withModifiedJulianDay_givenInvalidDayForNanos_throwsException(String description, long mjd, long nanos, long newMjd) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            assertThrows(DateTimeException.class, () -> initial.withModifiedJulianDay(newMjd));
        }
    }

    @Nested
    @DisplayName("withNanoOfDay()")
    class WithNanoOfDayTests {

        static Stream<Arguments> validNanoOfDayChanges() {
            return Stream.of(
                Arguments.of("Simple change", 7L, 12345L, 2L, 7L, 2L),
                Arguments.of("To max nanos on non-leap day", MJD_1972_12_30, 0L, NANOS_PER_DAY - 1, MJD_1972_12_30, NANOS_PER_DAY - 1),
                Arguments.of("To leap second on leap day", MJD_1972_12_31_LEAP, 0L, NANOS_PER_DAY, MJD_1972_12_31_LEAP, NANOS_PER_DAY),
                Arguments.of("To max nanos on leap day", MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY - 1, MJD_1972_12_31_LEAP, NANOS_PER_LEAP_DAY - 1)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("validNanoOfDayChanges")
        void withNanoOfDay_givenValidNanos_returnsUpdatedInstant(
            String description, long mjd, long nanos, long newNanoOfDay, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            UtcInstant result = initial.withNanoOfDay(newNanoOfDay);
            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }

        static Stream<Arguments> invalidNanoOfDayChanges() {
            return Stream.of(
                Arguments.of("Negative nanos", 0L, 12345L, -1L),
                Arguments.of("Leap second nanos on non-leap day", MJD_1972_12_30, 0L, NANOS_PER_DAY),
                Arguments.of("Nanos > leap day max on leap day", MJD_1972_12_31_LEAP, 0L, NANOS_PER_LEAP_DAY)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidNanoOfDayChanges")
        void withNanoOfDay_givenInvalidNanoOfDayForDay_throwsException(String description, long mjd, long nanos, long newNanoOfDay) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            assertThrows(DateTimeException.class, () -> initial.withNanoOfDay(newNanoOfDay));
        }
    }

    @Nested
    @DisplayName("plus(Duration)")
    class PlusDurationTests {

        static Stream<Arguments> plusCases() {
            return Stream.of(
                // Additions to MJD=0, Nanos=0
                Arguments.of(0, 0, 0, 1, 0, 1),
                Arguments.of(0, 0, 0, -1, -1, NANOS_PER_DAY - 1), // roll back one day
                Arguments.of(0, 0, 1, 0, 0, NANOS_PER_SEC),
                Arguments.of(0, 0, SECS_PER_DAY, 0, 1, 0), // add one day
                Arguments.of(0, 0, -SECS_PER_DAY, 0, -1, 0), // subtract one day

                // Additions to MJD=1, Nanos=0
                Arguments.of(1, 0, 0, 1, 1, 1),
                Arguments.of(1, 0, 0, -1, 0, NANOS_PER_DAY - 1), // roll back one day
                Arguments.of(1, 0, SECS_PER_DAY, 0, 2, 0), // add one day
                Arguments.of(1, 0, -SECS_PER_DAY, 0, 0, 0) // subtract one day
            );
        }

        @ParameterizedTest(name = "MJD {0}, NOD {1} + ({2}s, {3}ns) -> MJD {4}, NOD {5}")
        @MethodSource("plusCases")
        void plus_addsDuration(long mjd, long nanos, long plusSeconds, int plusNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            Duration duration = Duration.ofSeconds(plusSeconds, plusNanos);
            UtcInstant result = initial.plus(duration);

            assertEquals(expectedMjd, result.getModifiedJulianDay());
            assertEquals(expectedNanos, result.getNanoOfDay());
        }
    }

    @Nested
    @DisplayName("minus(Duration)")
    class MinusDurationTests {

        static Stream<Arguments> minusCases() {
            return Stream.of(
                // Subtractions from MJD=0, Nanos=0
                Arguments.of(0, 0, 0, 1, -1, NANOS_PER_DAY - 1), // roll back one day
                Arguments.of(0, 0, 0, -1, 0, 1),
                Arguments.of(0, 0, 1, 0, 0, -NANOS_PER_SEC), // equivalent to plus(-1s)
                Arguments.of(0, 0, SECS_PER_DAY, 0, -1, 0), // subtract one day
                Arguments.of(0, 0, -SECS_PER_DAY, 0, 1, 0), // add one day

                // Subtractions from MJD=1, Nanos=0
                Arguments.of(1, 0, 0, 1, 0, NANOS_PER_DAY - 1), // roll back one day
                Arguments.of(1, 0, 0, -1, 1, 1),
                Arguments.of(1, 0, SECS_PER_DAY, 0, 0, 0), // subtract one day
                Arguments.of(1, 0, -SECS_PER_DAY, 0, 2, 0) // add one day
            );
        }

        @ParameterizedTest(name = "MJD {0}, NOD {1} - ({2}s, {3}ns) -> MJD {4}, NOD {5}")
        @MethodSource("minusCases")
        void minus_subtractsDuration(long mjd, long nanos, long minusSeconds, int minusNanos, long expectedMjd, long expectedNanos) {
            UtcInstant initial = UtcInstant.ofModifiedJulianDay(mjd, nanos);
            Duration duration = Duration.ofSeconds(minusSeconds, minusNanos);
            UtcInstant result = initial.minus(duration);

            // The logic for minus is plus(duration.negated()), so expected results can seem counter-intuitive.
            // A clearer way to assert is to check against a manually constructed expected instant.
            UtcInstant expected = UtcInstant.ofModifiedJulianDay(expectedMjd, expectedNanos);
            assertEquals(expected, result);
        }
    }

    @Nested
    @DisplayName("toString() and parse()")
    class ToStringAndParseTests {

        static Stream<Arguments> toStringCases() {
            return Stream.of(
                Arguments.of(40587, 0, "1970-01-01T00:00:00Z"),
                Arguments.of(40588, 1, "1970-01-02T00:00:00.000000001Z"),
                Arguments.of(40588, 999, "1970-01-02T00:00:00.000000999Z"),
                Arguments.of(40588, 1000, "1970-01-02T00:00:00.000001Z"),
                Arguments.of(40618, 999_999_999, "1970-02-01T00:00:00.999999999Z"),
                Arguments.of(40619, 1_000_000_000, "1970-02-02T00:00:01Z"),
                Arguments.of(MJD_1972_12_31_LEAP, NANOS_PER_DAY, "1972-12-31T23:59:60Z"), // Leap second
                Arguments.of(MJD_1973_01_01, 0, "1973-01-01T00:00:00Z")
            );
        }

        @ParameterizedTest(name = "\"{1}\"")
        @MethodSource("toStringCases")
        void toString_returnsCorrectIsoFormat(long mjd, long nod, String expected) {
            UtcInstant instant = UtcInstant.ofModifiedJulianDay(mjd, nod);
            assertEquals(expected, instant.toString());
        }

        @ParameterizedTest(name = "parse(\"{1}\")")
        @MethodSource("toStringCases")
        void parse_recreatesInstantFromToStringOutput(long mjd, long nod, String text) {
            UtcInstant expected = UtcInstant.ofModifiedJulianDay(mjd, nod);
            assertEquals(expected, UtcInstant.parse(text));
        }
    }

    @Nested
    @DisplayName("Conversions")
    class ConversionTests {

        // The TAI epoch began at 1958-01-01 (MJD 36204).
        private static final long MJD_TAI_EPOCH = 36204L;
        // Before the first leap second in 1972, TAI was ahead of an extrapolated UTC by 10 seconds.
        private static final long TAI_UTC_INITIAL_OFFSET_SECONDS = 10L;

        @Test
        @DisplayName("toTaiInstant() for dates before 1972 uses a constant offset")
        void toTaiInstant_forDatesBefore1972_usesConstantOffset() {
            // This test verifies that for dates before the modern leap-second system (pre-1972),
            // the conversion to TAI is based on a fixed offset from the TAI epoch.
            for (int dayOffset = -100; dayOffset < 100; dayOffset++) {
                for (int secondOfDay = 0; secondOfDay < 10; secondOfDay++) {
                    long mjd = MJD_TAI_EPOCH + dayOffset;
                    long nanoOfDay = secondOfDay * NANOS_PER_SEC + 2L;
                    UtcInstant utc = UtcInstant.ofModifiedJulianDay(mjd, nanoOfDay);

                    TaiInstant tai = utc.toTaiInstant();

                    long expectedTaiSeconds = dayOffset * SECS_PER_DAY + secondOfDay + TAI_UTC_INITIAL_OFFSET_SECONDS;
                    assertEquals(expectedTaiSeconds, tai.getTaiSeconds());
                    assertEquals(2, tai.getNano());
                }
            }
        }
    }
}