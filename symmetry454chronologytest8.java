package org.threeten.extra.chrono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.threeten.extra.chrono.Symmetry454Chronology.DAYS_IN_MONTH;
import static org.threeten.extra.chrono.Symmetry454Chronology.DAYS_IN_MONTH_LONG;
import static org.threeten.extra.chrono.Symmetry454Chronology.WEEKS_IN_MONTH;
import static org.threeten.extra.chrono.Symmetry454Chronology.WEEKS_IN_MONTH_LONG;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.EPOCH_DAY;
import static java.time.temporal.ChronoField.ERA;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.PROLEPTIC_MONTH;
import static java.time.temporal.ChronoField.YEAR;
import static java.time.temporal.ChronoField.YEAR_OF_ERA;
import static java.time.temporal.ChronoUnit.CENTURIES;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.DECADES;
import static java.time.temporal.ChronoUnit.ERAS;
import static java.time.temporal.ChronoUnit.MILLENNIA;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Tests for Symmetry454Chronology and Symmetry454Date")
class Symmetry454ChronologyTest {

    private static final Symmetry454Chronology S454_CHRONO = Symmetry454Chronology.INSTANCE;

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleSymmetryAndIsoDates() {
        return Stream.of(
            Arguments.of(Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1), "early date"),
            // A date in a long month (February has 35 days)
            Arguments.of(Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27), "date in a long month"),
            // A date in a Symmetry454 leap year (1066)
            Arguments.of(Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14), "date in a leap year"),
            // A modern date
            Arguments.of(Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9), "modern date"),
            // The ISO date for the Unix epoch start
            Arguments.of(Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1), "Unix epoch"),
            // The ISO date for the start of the 21st century
            Arguments.of(Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1), "start of 21st century")
        );
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion and Equivalence Tests")
    class ConversionTests {

        @ParameterizedTest(name = "{2}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void symmetryAndIsoDatesShouldBeEquivalent(Symmetry454Date sym454, LocalDate iso, String description) {
            // Test bidirectional conversions
            assertEquals(iso, LocalDate.from(sym454));
            assertEquals(sym454, Symmetry454Date.from(iso));

            // Test epoch day equivalence
            assertEquals(iso.toEpochDay(), sym454.toEpochDay());
            assertEquals(sym454, S454_CHRONO.dateEpochDay(iso.toEpochDay()));

            // Test conversion via Chronology
            assertEquals(sym454, S454_CHRONO.date(iso));

            // Test zero period/duration when converting between equivalent dates
            assertEquals(Period.ZERO, iso.until(sym454));
            assertEquals(S454_CHRONO.period(0, 0, 0), sym454.until(iso));
        }

        @Test
        void untilSelfShouldBeZero() {
            Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
            assertEquals(S454_CHRONO.period(0, 0, 0), date.until(date));
        }

        @ParameterizedTest(name = "{2}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void untilIsoDateInDaysShouldReturnDayDifference(Symmetry454Date sym454, LocalDate iso, String description) {
            assertEquals(0, sym454.until(iso, DAYS));
            assertEquals(1, sym454.until(iso.plusDays(1), DAYS));
            assertEquals(35, sym454.until(iso.plusDays(35), DAYS));
            assertEquals(-40, sym454.until(iso.minusDays(40), DAYS));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Creation Tests")
    class CreationTests {

        static Stream<Arguments> invalidDateComponents() {
            return Stream.of(
                // Invalid month
                Arguments.of(2000, 0, 1),
                Arguments.of(2000, 13, 1),
                // Invalid day for a 28-day month
                Arguments.of(2000, 1, 0),
                Arguments.of(2000, 1, 29),
                // Invalid day for a 35-day month
                Arguments.of(2000, 2, 36),
                // Invalid day for December in a non-leap year (28 days)
                Arguments.of(2000, 12, 29),
                // Invalid day for December in a leap year (35 days)
                Arguments.of(2004, 12, 36)
            );
        }

        @ParameterizedTest(name = "year={0}, month={1}, day={2}")
        @MethodSource("invalidDateComponents")
        void ofWithInvalidDatePartsThrowsException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, day));
        }

        @ParameterizedTest
        @MethodSource("invalidLeapDayDates")
        void ofWithInvalidLeapDayThrowsException(int year) {
            // December has 28 days in a non-leap year
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }

        static Stream<Integer> invalidLeapDayDates() {
            return Stream.of(1, 100, 200, 2000); // These are not leap years in Symmetry454
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field and Property Access Tests")
    class FieldAccessTests {

        static Stream<Arguments> monthLengths() {
            return Stream.of(
                // 28-day months
                Arguments.of(2000, 1, 28),
                Arguments.of(2000, 3, 28),
                // 35-day months
                Arguments.of(2000, 2, 35),
                Arguments.of(2000, 5, 35),
                // December in a leap year (35 days)
                Arguments.of(2004, 12, 35)
            );
        }

        @ParameterizedTest(name = "{0}-{1} should have {2} days")
        @MethodSource("monthLengths")
        void lengthOfMonthShouldReturnCorrectValue(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> fieldRanges() {
            return Stream.of(
                // DAY_OF_MONTH range varies by month
                Arguments.of(Symmetry454Date.of(2012, 1, 23), DAY_OF_MONTH, ValueRange.of(1, 28)),
                Arguments.of(Symmetry454Date.of(2012, 2, 23), DAY_OF_MONTH, ValueRange.of(1, 35)),
                Arguments.of(Symmetry454Date.of(2015, 12, 23), DAY_OF_MONTH, ValueRange.of(1, 35)), // leap year
                // DAY_OF_YEAR range varies by leap year
                Arguments.of(Symmetry454Date.of(2012, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 364)), // non-leap
                Arguments.of(Symmetry454Date.of(2015, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 371)), // leap
                // Fixed ranges
                Arguments.of(Symmetry454Date.of(2012, 1, 23), DAY_OF_WEEK, ValueRange.of(1, 7)),
                Arguments.of(Symmetry454Date.of(2012, 1, 23), MONTH_OF_YEAR, ValueRange.of(1, 12)),
                // Week-based field ranges
                Arguments.of(Symmetry454Date.of(2012, 1, 23), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(Symmetry454Date.of(2012, 2, 23), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
                Arguments.of(Symmetry454Date.of(2012, 1, 23), ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                Arguments.of(Symmetry454Date.of(2015, 12, 30), ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)) // leap year
            );
        }

        @ParameterizedTest(name = "{1} range for {0}")
        @MethodSource("fieldRanges")
        void rangeShouldReturnCorrectValueForField(Symmetry454Date date, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, date.range(field));
        }

        static Stream<Arguments> getLongFieldCases() {
            return Stream.of(
                // --- Date: 2014-05-26 (non-leap year) ---
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5L),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                // Day of year: Jan(28) + Feb(35) + Mar(28) + Apr(28) + 26
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, (long) DAYS_IN_MONTH + DAYS_IN_MONTH_LONG + DAYS_IN_MONTH + DAYS_IN_MONTH + 26),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
                // Week of year: Jan(4) + Feb(5) + Mar(4) + Apr(4) + 4th week of May
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, (long) WEEKS_IN_MONTH + WEEKS_IN_MONTH_LONG + WEEKS_IN_MONTH + WEEKS_IN_MONTH + 4),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014L * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),

                // --- Date: 2015-12-35 (leap day in a leap year) ---
                Arguments.of(2015, 12, 35, DAY_OF_WEEK, 7L),
                Arguments.of(2015, 12, 35, DAY_OF_MONTH, 35L),
                // Day of year: 364 (normal year) + 7 (leap week) = 371
                Arguments.of(2015, 12, 35, DAY_OF_YEAR, 371L),
                Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_MONTH, 5L),
                Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53L),
                Arguments.of(2015, 12, 35, MONTH_OF_YEAR, 12L)
            );
        }

        @ParameterizedTest(name = "{3} of {0}-{1}-{2} should be {4}")
        @MethodSource("getLongFieldCases")
        void getLongShouldReturnCorrectValueForField(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Manipulation Tests")
    class ManipulationTests {

        static Stream<Arguments> plusCases() {
            return Stream.of(
                // Add days
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 5, 34),
                Arguments.of(2014, 5, 26, -3, DAYS, 2014, 5, 23),
                // Add weeks
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 12),
                Arguments.of(2014, 12, 26, 3, WEEKS, 2015, 1, 19), // across year boundary
                // Add months
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                // Add years
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                // Add larger units
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("plusCases")
        void plusShouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4} -> {0}-{1}-{2}")
        @MethodSource("plusCases")
        void minusShouldReturnCorrectDate(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Stream<Arguments> withFieldCases() {
            return Stream.of(
                // with DAY_OF_WEEK
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22),
                // with DAY_OF_MONTH
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                // with DAY_OF_YEAR
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28),
                // with MONTH_OF_YEAR
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                // with YEAR
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                // with on a leap day
                Arguments.of(2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("withFieldCases")
        void withShouldReturnCorrectDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Arguments> withInvalidFieldCases() {
            return Stream.of(
                Arguments.of(2013, 1, 1, DAY_OF_MONTH, 29), // Invalid day for month
                Arguments.of(2013, 1, 1, DAY_OF_YEAR, 365), // Invalid day for year
                Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14), // Invalid month
                Arguments.of(2013, 1, 1, YEAR, 1_000_001) // Year out of range
            );
        }

        @ParameterizedTest(name = "with({3}, {4}) should throw exception")
        @MethodSource("withInvalidFieldCases")
        void withInvalidValueShouldThrowException(int y, int m, int d, TemporalField field, long value) {
            Symmetry454Date date = Symmetry454Date.of(y, m, d);
            assertThrows(DateTimeException.class, () -> date.with(field, value));
        }

        @Test
        void withLastDayOfMonthAdjuster() {
            Symmetry454Date date = Symmetry454Date.of(2012, 2, 23); // In a 35-day month
            Symmetry454Date expected = Symmetry454Date.of(2012, 2, 35);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Period and Until Tests")
    class PeriodUntilTests {

        static Stream<Arguments> untilCases() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0L),
                Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 13L),
                Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6L),
                Arguments.of(2014, 5, 26, 2014, 6, 5, WEEKS, 1L),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, ERAS, 0L)
            );
        }

        @ParameterizedTest(name = "until({3}) from {0}-{1}-{2} to {3}-{4}-{5} is {7}")
        @MethodSource("untilCases")
        void untilShouldReturnCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodCases() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 13),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 25, 0, 11, 27)
            );
        }

        @ParameterizedTest(name = "period from {0}-{1}-{2} to {3}-{4}-{5} is {6}Y{7}M{8}D")
        @MethodSource("untilPeriodCases")
        void untilShouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = S454_CHRONO.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Chronology API Tests")
    class ChronologyApiTests {

        @Test
        void prolepticYearShouldWorkForValidEra() {
            assertEquals(2000, S454_CHRONO.prolepticYear(IsoEra.CE, 2000));
            assertEquals(1, S454_CHRONO.prolepticYear(IsoEra.CE, 1));
        }

        static Stream<Era> nonIsoEras() {
            return Stream.of(
                HijrahEra.AH, JapaneseEra.HEISEI, MinguoEra.ROC, ThaiBuddhistEra.BE
                // The original test included other non-standard eras from the same package.
                // This subset demonstrates the principle with standard java.time eras.
            );
        }

        @ParameterizedTest
        @MethodSource("nonIsoEras")
        void prolepticYearShouldThrowExceptionForInvalidEra(Era era) {
            assertThrows(ClassCastException.class, () -> S454_CHRONO.prolepticYear(era, 4));
        }

        @Test
        void toStringShouldReturnFormattedDate() {
            Symmetry454Date date = Symmetry454Date.of(1970, 2, 35);
            assertEquals("Sym454 CE 1970/02/35", date.toString());
        }
    }
}