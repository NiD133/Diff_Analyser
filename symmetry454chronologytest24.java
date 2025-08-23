package org.threeten.extra.chrono;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Symmetry454Chronology and Symmetry454Date Tests")
class Symmetry454ChronologyTest {

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> provideDateConversionSamples() {
        // Each entry is a pair of equivalent dates: (Symmetry454Date, corresponding LocalDate)
        return Stream.of(
                Arguments.of(Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
                Arguments.of(Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
                Arguments.of(Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
                Arguments.of(Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
                Arguments.of(Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)),
                Arguments.of(Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)), // Near Unix epoch
                Arguments.of(Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1))
        );
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Creation and Conversion")
    class CreationAndConversionTests {

        @ParameterizedTest(name = "{0} <-> {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDateConversionSamples")
        void testDateConversionsAndEquivalence(Symmetry454Date symDate, LocalDate isoDate) {
            // Test conversions in both directions
            assertEquals(isoDate, LocalDate.from(symDate));
            assertEquals(symDate, Symmetry454Date.from(isoDate));
            assertEquals(symDate, Symmetry454Chronology.INSTANCE.date(isoDate));

            // Test epoch day equivalence
            assertEquals(isoDate.toEpochDay(), symDate.toEpochDay());
            assertEquals(symDate, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        static Stream<Arguments> provideInvalidDateParts() {
            return Stream.of(
                    // month out of range
                    Arguments.of(2000, 0, 1, "Month 0 is invalid"),
                    Arguments.of(2000, 13, 1, "Month 13 is invalid"),
                    // day out of range for short month (28 days)
                    Arguments.of(2000, 1, 29, "Day 29 is invalid for January (short month)"),
                    // day out of range for long month (35 days)
                    Arguments.of(2000, 2, 36, "Day 36 is invalid for February (long month)"),
                    // day out of range for December in non-leap year (28 days)
                    Arguments.of(2000, 12, 29, "Day 29 is invalid for December in a non-leap year"),
                    // day out of range for December in leap year (35 days)
                    Arguments.of(2004, 12, 36, "Day 36 is invalid for December in a leap year")
            );
        }

        @ParameterizedTest(name = "year={0}, month={1}, day={2} - {3}")
        @MethodSource("provideInvalidDateParts")
        void of_withInvalidParts_throwsException(int year, int month, int day, String description) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, day));
        }

        static Stream<Integer> provideNonLeapYears() {
            return Stream.of(1, 100, 200, 2000); // These are not leap years in Symmetry454
        }

        @ParameterizedTest
        @MethodSource("provideNonLeapYears")
        void of_withInvalidLeapDay_throwsException(int year) {
            // December has 28 days in a non-leap year, so day 29 is invalid.
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field and Range Access")
    class FieldAndRangeTests {

        static Stream<Arguments> provideMonthLengths() {
            return Stream.of(
                    Arguments.of(2000, 1, 28), // Jan: short
                    Arguments.of(2000, 2, 35), // Feb: long
                    Arguments.of(2000, 12, 28), // Dec in non-leap year: short
                    Arguments.of(2004, 12, 35)  // Dec in leap year: long
            );
        }

        @ParameterizedTest(name = "{0}-{1} should have {2} days")
        @MethodSource("provideMonthLengths")
        void lengthOfMonth_returnsCorrectValue(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> provideFieldRanges() {
            return Stream.of(
                    Arguments.of(2012, 1, DAY_OF_MONTH, ValueRange.of(1, 28)),
                    Arguments.of(2012, 2, DAY_OF_MONTH, ValueRange.of(1, 35)),
                    Arguments.of(2015, 12, DAY_OF_MONTH, ValueRange.of(1, 35)), // Leap year December
                    Arguments.of(2012, 1, DAY_OF_YEAR, ValueRange.of(1, 364)),  // Non-leap year
                    Arguments.of(2015, 1, DAY_OF_YEAR, ValueRange.of(1, 371)),  // Leap year
                    Arguments.of(2012, 2, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
                    Arguments.of(2015, 12, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53))
            );
        }

        @ParameterizedTest(name = "range({2}) for {0}-{1} is {3}")
        @MethodSource("provideFieldRanges")
        void range_forField_returnsCorrectRange(int year, int month, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(year, month, 1).range(field));
        }

        static Stream<Arguments> provideFieldValues() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, DAY_OF_YEAR, 28 + 35 + 28 + 28 + 26, "Jan(28)+Feb(35)+Mar(28)+Apr(28)+26"),
                    Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4 + 5 + 4 + 4 + 4, "4+5+4+4 weeks + 4th week in May"),
                    Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014L * 12 + 5 - 1, "Proleptic month calculation"),
                    Arguments.of(2015, 12, 35, DAY_OF_YEAR, 371, "Last day of a leap year"),
                    Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53, "Last week of a leap year")
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2}, getLong({3}) should be {4} ({5})")
        @MethodSource("provideFieldValues")
        void getLong_forField_returnsCorrectValue(int year, int month, int dom, TemporalField field, long expected, String description) {
            assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Manipulation")
    class ManipulationTests {

        static Stream<Arguments> provideWithAdjustments() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, Symmetry454Date.of(2014, 5, 22)),
                    Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, Symmetry454Date.of(2014, 12, 28)),
                    Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, Symmetry454Date.of(2014, 4, 26)),
                    Arguments.of(2014, 5, 26, YEAR, 2012, Symmetry454Date.of(2012, 5, 26))
            );
        }

        @ParameterizedTest(name = "{0} with({1}, {2}) should be {3}")
        @MethodSource("provideWithAdjustments")
        void with_forField_returnsAdjustedDate(int y, int m, int d, TemporalField field, long value, Symmetry454Date expected) {
            assertEquals(expected, Symmetry454Date.of(y, m, d).with(field, value));
        }

        static Stream<Arguments> provideInvalidWithAdjustments() {
            return Stream.of(
                    Arguments.of(DAY_OF_MONTH, 29, "Day 29 is invalid for a 28-day month"),
                    Arguments.of(DAY_OF_YEAR, 365, "Day 365 is invalid for a 364-day year"),
                    Arguments.of(ALIGNED_WEEK_OF_MONTH, 5, "Week 5 is invalid for a 4-week month"),
                    Arguments.of(MONTH_OF_YEAR, 13, "Month 13 is invalid")
            );
        }

        @ParameterizedTest(name = "with({0}, {1}) throws exception: {2}")
        @MethodSource("provideInvalidWithAdjustments")
        void with_forFieldWithInvalidValue_throwsException(TemporalField field, long value, String description) {
            Symmetry454Date date = Symmetry454Date.of(2013, 1, 1); // A known non-leap year date
            assertThrows(DateTimeException.class, () -> date.with(field, value));
        }

        @Test
        void with_lastDayOfMonth_adjustsCorrectly() {
            Symmetry454Date date = Symmetry454Date.of(2012, 2, 23); // February is a 35-day month
            Symmetry454Date expected = Symmetry454Date.of(2012, 2, 35);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Arithmetic")
    class DateArithmeticTests {

        static Stream<Arguments> providePlusMinusCases() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, 8, DAYS, 2014, 5, 34),
                    Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 12),
                    Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                    Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                    Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("providePlusMinusCases")
        void plus_addsAmountCorrectly(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4} -> {0}-{1}-{2}")
        @MethodSource("providePlusMinusCases")
        void minus_subtractsAmountCorrectly(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date expected = Symmetry454Date.of(y, m, d);
            Symmetry454Date start = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }

        @Test
        void plus_acrossLeapWeek_isCorrect() {
            // 2015 is a leap year, Dec has a leap week (35 days)
            Symmetry454Date date = Symmetry454Date.of(2015, 12, 28);
            Symmetry454Date expected = Symmetry454Date.of(2016, 1, 1);
            assertEquals(expected, date.plus(8, DAYS));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Period and Duration")
    class PeriodAndDurationTests {

        @ParameterizedTest(name = "{0} until {1} is 0")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDateConversionSamples")
        void until_withSelf_isZero(Symmetry454Date symDate, LocalDate isoDate) {
            assertEquals(0, symDate.until(symDate, DAYS));
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), symDate.until(symDate));
        }

        @ParameterizedTest(name = "{0} until equivalent ISO date {1} is 0")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDateConversionSamples")
        void until_withEquivalentIsoDate_isZero(Symmetry454Date symDate, LocalDate isoDate) {
            // The period between a date and its direct equivalent in another chronology should be zero.
            assertEquals(Period.ZERO, isoDate.until(symDate));
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), symDate.until(isoDate));
        }

        @Test
        void until_calculatesPeriodCorrectly() {
            Symmetry454Date start = Symmetry454Date.of(2014, 5, 26);
            Symmetry454Date end = Symmetry454Date.of(2015, 6, 28);
            ChronoPeriod period = start.until(end);

            assertEquals(1, period.get(YEARS));
            assertEquals(1, period.get(MONTHS));
            assertEquals(2, period.get(DAYS));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("General API")
    class GeneralApiTests {

        @Test
        void testToString() {
            Symmetry454Date date = Symmetry454Date.of(1970, 2, 35);
            assertEquals("Sym454 CE 1970/02/35", date.toString());
        }

        @Test
        void testEqualsAndHashCode() {
            Symmetry454Date date1 = Symmetry454Date.of(2014, 5, 26);
            Symmetry454Date date2 = Symmetry454Date.of(2014, 5, 26);
            Symmetry454Date date3 = Symmetry454Date.of(2014, 6, 26);
            Symmetry454Date date4 = Symmetry454Date.of(2015, 5, 26);

            new EqualsTester()
                    .addEqualityGroup(date1, date2)
                    .addEqualityGroup(date3)
                    .addEqualityGroup(date4)
                    .testEquals();
        }

        static Stream<Era> provideUnsupportedEras() {
            return Stream.of(
                    HijrahEra.AH, JapaneseEra.HEISEI, MinguoEra.ROC, ThaiBuddhistEra.BE
            );
        }

        @ParameterizedTest
        @MethodSource("provideUnsupportedEras")
        void prolepticYear_withUnsupportedEra_throwsException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 1));
        }
    }
}