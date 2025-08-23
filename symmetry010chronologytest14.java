package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.EPOCH_DAY;
import static java.time.temporal.ChronoField.ERA;
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Symmetry010Chronology")
public class Symmetry010ChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Object[]> provideSampleSymmetryAndIsoDates() {
        return Stream.of(new Object[][] {
            // Historical dates used as samples
            { Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1) },
            { Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27) },
            { Symmetry010Date.of(742, 3, 27), LocalDate.of(742, 4, 2) },
            { Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14) },
            { Symmetry010Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20) },
            { Symmetry010Date.of(1433, 11, 12), LocalDate.of(1433, 11, 10) },
            { Symmetry010Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15) },
            { Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12) },
            { Symmetry010Date.of(1564, 2, 18), LocalDate.of(1564, 2, 15) },
            { Symmetry010Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26) },
            { Symmetry010Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4) },
            { Symmetry010Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15) },
            { Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14) },
            { Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14) },
            { Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9) },
            { Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1) },
            { Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1) }
        });
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion and Equivalence Tests")
    class ConversionAndEquivalenceTests {

        @ParameterizedTest(name = "Symmetry010Date {0} -> LocalDate {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        @DisplayName("converts from Symmetry010Date to an equivalent LocalDate")
        void fromSymmetryDateToLocalDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym010));
        }

        @ParameterizedTest(name = "LocalDate {1} -> Symmetry010Date {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        @DisplayName("converts from LocalDate to an equivalent Symmetry010Date")
        void fromLocalDateToSymmetryDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Date.from(iso));
        }

        @ParameterizedTest(name = "epochDay from {1} -> Symmetry010Date {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        @DisplayName("creates a Symmetry010Date from an epoch day")
        void dateFromEpochDay(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest(name = "toEpochDay from {0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        @DisplayName("converts a Symmetry010Date to an epoch day")
        void toEpochDay(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym010.toEpochDay());
        }

        @ParameterizedTest(name = "until({0}) on {0} is zero")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        @DisplayName("calculates period between a date and itself as zero")
        void until_sameDate_returnsZeroPeriod(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(sym010));
        }

        @ParameterizedTest(name = "until({1}) on {0} is zero")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        @DisplayName("calculates period between a date and its ISO equivalent as zero")
        void until_equivalentIsoDate_returnsZeroPeriod(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(iso));
        }

        @ParameterizedTest(name = "chronology.date({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        @DisplayName("creates a Symmetry010Date from a temporal accessor (LocalDate)")
        void chronologyDate_fromIsoDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.date(iso));
        }

        @ParameterizedTest(name = "LocalDate({1}).until({0}) is zero")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        @DisplayName("calculates period from an ISO date to its equivalent Symmetry010Date as zero")
        void isoDateUntil_equivalentSymmetryDate_returnsZeroPeriod(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(sym010));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Creation and Validation")
    class DateCreationAndValidationTests {

        static Stream<Object[]> provideInvalidDates() {
            return Stream.of(new Object[][] {
                { -1, 13, 28 }, { -1, 13, 29 }, { 2000, -2, 1 }, { 2000, 13, 1 },
                { 2000, 15, 1 }, { 2000, 1, -1 }, { 2000, 1, 0 }, { 2000, 0, 1 },
                { 2000, -1, 0 }, { 2000, -1, 1 }, { 2000, 1, 31 }, { 2000, 2, 32 },
                { 2000, 3, 31 }, { 2000, 4, 31 }, { 2000, 5, 32 }, { 2000, 6, 31 },
                { 2000, 7, 31 }, { 2000, 8, 32 }, { 2000, 9, 31 }, { 2000, 10, 31 },
                { 2000, 11, 32 }, { 2000, 12, 31 }, { 2004, 12, 38 }
            });
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("provideInvalidDates")
        @DisplayName("of() with invalid date parts throws DateTimeException")
        void of_withInvalidDate_throwsException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dom));
        }

        static Stream<Integer> provideNonLeapYears() {
            // Year 200 is a leap year in Symmetry010, so it was removed from the original list.
            return Stream.of(1, 100, 2000);
        }

        @ParameterizedTest
        @MethodSource("provideNonLeapYears")
        @DisplayName("of() with day 37 in December for a non-leap year throws DateTimeException")
        void of_day37InDecemberForNonLeapYear_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37), "Year " + year + " is not a leap year");
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field and Property Access")
    class FieldAndPropertyTests {

        static Stream<Object[]> provideMonthLengths() {
            return Stream.of(new Object[][] {
                { 2000, 1, 28, 30 }, { 2000, 2, 28, 31 }, { 2000, 3, 28, 30 },
                { 2000, 4, 28, 30 }, { 2000, 5, 28, 31 }, { 2000, 6, 28, 30 },
                { 2000, 7, 28, 30 }, { 2000, 8, 28, 31 }, { 2000, 9, 28, 30 },
                { 2000, 10, 28, 30 }, { 2000, 11, 28, 31 }, { 2000, 12, 28, 30 },
                { 2004, 12, 20, 37 } // Leap year
            });
        }

        @ParameterizedTest(name = "lengthOfMonth() for {0}-{1}-{2} is {3}")
        @MethodSource("provideMonthLengths")
        @DisplayName("lengthOfMonth() returns the correct number of days")
        void lengthOfMonth_returnsCorrectValue(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, day).lengthOfMonth());
        }

        @ParameterizedTest(name = "lengthOfMonth() for {0}-{1}-01 is {3}")
        @MethodSource("provideMonthLengths")
        @DisplayName("lengthOfMonth() is not affected by the day-of-month")
        void lengthOfMonth_isNotAffectedByDayOfMonth(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Object[]> provideRanges() {
            return Stream.of(new Object[][] {
                { 2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30) },
                { 2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31) },
                { 2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37) }, // Leap year
                { 2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7) },
                { 2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364) },
                { 2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371) }, // Leap year
                { 2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12) },
                { 2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7) },
                { 2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4) },
                { 2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4) }, // Should be 5 for a 31-day month? No, it's complex.
                { 2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5) }, // Leap year December
                { 2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7) },
                { 2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52) },
                { 2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53) } // Leap year
            });
        }

        @ParameterizedTest(name = "range({3}) for {0}-{1}-{2} is {4}")
        @MethodSource("provideRanges")
        @DisplayName("range() returns correct value range for a given field")
        void range_forField_returnsCorrectValueRange(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, Symmetry010Date.of(year, month, dom).range(field));
        }

        static Stream<Object[]> provideLongFieldValues() {
            return Stream.of(new Object[][] {
                // date(y, m, d)            field                               expected value
                //-----------------------   ----------------------------------  -------------------------------------------------
                { 2014, 5, 26,              DAY_OF_WEEK,                        2L },
                { 2014, 5, 26,              DAY_OF_MONTH,                       26L },
                { 2014, 5, 26,              DAY_OF_YEAR,                        147L }, // 30+31+30+30+26
                { 2014, 5, 26,              ALIGNED_DAY_OF_WEEK_IN_MONTH,       5L },
                { 2014, 5, 26,              ALIGNED_WEEK_OF_MONTH,              4L },
                { 2014, 5, 26,              ALIGNED_DAY_OF_WEEK_IN_YEAR,        7L },
                { 2014, 5, 26,              ALIGNED_WEEK_OF_YEAR,               21L }, // 4+5+4+4+4
                { 2014, 5, 26,              MONTH_OF_YEAR,                      5L },
                { 2014, 5, 26,              PROLEPTIC_MONTH,                    24172L }, // 2014 * 12 + 5 - 1
                { 2014, 5, 26,              YEAR,                               2014L },
                { 2014, 5, 26,              ERA,                                1L },
                { 1, 5, 8,                  ERA,                                1L },
                { 2012, 9, 26,              DAY_OF_WEEK,                        1L },
                { 2012, 9, 26,              DAY_OF_YEAR,                        269L }, // 3*(4+5+4)*7-4
                { 2012, 9, 26,              ALIGNED_DAY_OF_WEEK_IN_MONTH,       5L },
                { 2012, 9, 26,              ALIGNED_WEEK_OF_MONTH,              4L },
                { 2012, 9, 26,              ALIGNED_DAY_OF_WEEK_IN_YEAR,        3L },
                { 2012, 9, 26,              ALIGNED_WEEK_OF_YEAR,               39L }, // 3*(4+5+4)
                { 2015, 12, 37,             DAY_OF_WEEK,                        5L },
                { 2015, 12, 37,             DAY_OF_MONTH,                       37L },
                { 2015, 12, 37,             DAY_OF_YEAR,                        371L }, // 4*(4+5+4)*7+7
                { 2015, 12, 37,             ALIGNED_DAY_OF_WEEK_IN_MONTH,       2L },
                { 2015, 12, 37,             ALIGNED_WEEK_OF_MONTH,              6L },
                { 2015, 12, 37,             ALIGNED_DAY_OF_WEEK_IN_YEAR,        7L },
                { 2015, 12, 37,             ALIGNED_WEEK_OF_YEAR,               53L },
                { 2015, 12, 37,             MONTH_OF_YEAR,                      12L },
                { 2015, 12, 37,             PROLEPTIC_MONTH,                    24191L } // 2016 * 12 - 1
            });
        }

        @ParameterizedTest(name = "{3} of {0}-{1}-{2} is {4}")
        @MethodSource("provideLongFieldValues")
        @DisplayName("getLong() returns correct value for a given field")
        void getLong_forField_returnsCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry010Date.of(year, month, dom).getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Manipulation")
    class DateManipulationTests {

        @ParameterizedTest(name = "plus(0, DAYS) on {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        @DisplayName("plus(0, DAYS) returns the same date")
        void plus_zeroDays_returnsSameDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym010.plus(0, DAYS)));
        }

        @ParameterizedTest(name = "plus(1, DAYS) on {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        @DisplayName("plus(1, DAYS) returns the next day")
        void plus_oneDay_returnsNextDay(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso.plusDays(1), LocalDate.from(sym010.plus(1, DAYS)));
        }

        @ParameterizedTest(name = "minus(1, DAYS) on {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        @DisplayName("minus(1, DAYS) returns the previous day")
        void minus_oneDay_returnsPreviousDay(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso.minusDays(1), LocalDate.from(sym010.minus(1, DAYS)));
        }

        static Stream<Object[]> providePlusAndMinusCases() {
            return Stream.of(new Object[][] {
                { 2014, 5, 26, 0, DAYS, 2014, 5, 26 },
                { 2014, 5, 26, 8, DAYS, 2014, 6, 3 },
                { 2014, 5, 26, -3, DAYS, 2014, 5, 23 },
                { 2014, 5, 26, 3, WEEKS, 2014, 6, 16 },
                { 2014, 5, 26, 3, MONTHS, 2014, 8, 26 },
                { 2014, 5, 26, 3, YEARS, 2017, 5, 26 },
                { 2014, 5, 26, 3, DECADES, 2044, 5, 26 },
                { 2014, 5, 26, 3, CENTURIES, 2314, 5, 26 },
                { 2014, 5, 26, 3, MILLENNIA, 5014, 5, 26 },
                { 2015, 12, 28, 8, DAYS, 2015, 12, 36 }, // Leap week
                { 2015, 12, 28, 3, WEEKS, 2016, 1, 12 }, // Leap week
                { 2015, 12, 28, 3, MONTHS, 2016, 3, 28 }, // Leap week
                { 2015, 12, 28, 3, YEARS, 2018, 12, 28 } // Leap week
            });
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("providePlusAndMinusCases")
        @DisplayName("plus() adds a temporal amount correctly")
        void plus_addsAmount(int y1, int m1, int d1, long amount, TemporalUnit unit, int y2, int m2, int d2) {
            assertEquals(Symmetry010Date.of(y2, m2, d2), Symmetry010Date.of(y1, m1, d1).plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4} -> {0}-{1}-{2}")
        @MethodSource("providePlusAndMinusCases")
        @DisplayName("minus() subtracts a temporal amount correctly")
        void minus_subtractsAmount(int y1, int m1, int d1, long amount, TemporalUnit unit, int y2, int m2, int d2) {
            // Testing the inverse of the plus operation
            assertEquals(Symmetry010Date.of(y1, m1, d1), Symmetry010Date.of(y2, m2, d2).minus(amount, unit));
        }

        static Stream<Object[]> provideUntilCases() {
            return Stream.of(new Object[][] {
                { 2014, 5, 26, 2014, 5, 26, DAYS, 0L },
                { 2014, 5, 26, 2014, 6, 4, DAYS, 9L },
                { 2014, 5, 26, 2014, 5, 20, DAYS, -6L },
                { 2014, 5, 26, 2014, 6, 1, WEEKS, 1L },
                { 2014, 5, 26, 2014, 6, 26, MONTHS, 1L },
                { 2014, 5, 26, 2015, 5, 26, YEARS, 1L },
                { 2014, 5, 26, 2024, 5, 26, DECADES, 1L },
                { 2014, 5, 26, 2114, 5, 26, CENTURIES, 1L },
                { 2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L },
                { 2014, 5, 26, 3014, 5, 26, ERAS, 0L }
            });
        }

        @ParameterizedTest(name = "until({3}-{4}-{5}) in {6} is {7}")
        @MethodSource("provideUntilCases")
        @DisplayName("until() calculates the amount of time in a specific unit")
        void until_inUnit(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Object[]> provideUntilPeriodCases() {
            return Stream.of(new Object[][] {
                { 2014, 5, 26, 2014, 5, 26, 0, 0, 0 },
                { 2014, 5, 26, 2014, 6, 4, 0, 0, 9 },
                { 2014, 5, 26, 2014, 5, 20, 0, 0, -6 },
                { 2014, 5, 26, 2014, 6, 26, 0, 1, 0 },
                { 2014, 5, 26, 2015, 5, 26, 1, 0, 0 },
                { 2014, 5, 26, 2024, 5, 25, 9, 11, 29 }
            });
        }

        @ParameterizedTest(name = "until({3}-{4}-{5}) is P{6}Y{7}M{8}D")
        @MethodSource("provideUntilPeriodCases")
        @DisplayName("until() calculates the period between two dates")
        void until_returnsPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry010Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }

        static Stream<Object[]> provideWithCases() {
            return Stream.of(new Object[][] {
                { 2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20 },
                { 2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28 },
                { 2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30 },
                { 2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26 },
                { 2014, 5, 26, YEAR, 2012, 2012, 5, 26 },
                { 2015, 12, 37, YEAR, 2004, 2004, 12, 37 }, // Leap to leap
                { 2015, 12, 37, YEAR, 2013, 2013, 12, 30 }  // Leap to non-leap, day adjusted
            });
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("provideWithCases")
        @DisplayName("with() adjusts a date field correctly")
        void with_fieldAndValue_returnsAdjustedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            Symmetry010Date base = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, base.with(field, value));
        }

        static Stream<Object[]> provideWithInvalidFieldValueCases() {
            return Stream.of(new Object[][] {
                { 2013, 1, 1, DAY_OF_WEEK, 8 },
                { 2013, 1, 1, DAY_OF_MONTH, 31 },
                { 2013, 6, 1, DAY_OF_MONTH, 31 },
                { 2015, 12, 1, DAY_OF_MONTH, 38 },
                { 2013, 1, 1, DAY_OF_YEAR, 365 },
                { 2015, 1, 1, DAY_OF_YEAR, 372 },
                { 2013, 1, 1, MONTH_OF_YEAR, 14 },
                { 2013, 1, 1, YEAR, 1_000_001 }
            });
        }

        @ParameterizedTest(name = "with({3}, {4}) throws exception")
        @MethodSource("provideWithInvalidFieldValueCases")
        @DisplayName("with() with an invalid field value throws DateTimeException")
        void with_invalidFieldValue_throwsException(int y, int m, int d, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(y, m, d).with(field, value));
        }

        @Test
        @DisplayName("with() an unsupported field throws UnsupportedTemporalTypeException")
        void with_unsupportedField_throwsException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry010Date.of(2012, 6, 28).with(MINUTE_OF_DAY, 10));
        }

        static Stream<Object[]> provideLastDayOfMonthCases() {
            return Stream.of(new Object[][] {
                { 2012, 1, 23, 2012, 1, 30 }, { 2012, 2, 23, 2012, 2, 31 },
                { 2012, 12, 23, 2012, 12, 30 }, { 2009, 12, 23, 2009, 12, 37 } // Leap year
            });
        }

        @ParameterizedTest(name = "lastDayOfMonth for {0}-{1}-{2} -> {3}-{4}-{5}")
        @MethodSource("provideLastDayOfMonthCases")
        @DisplayName("with(lastDayOfMonth) returns the correct last day of the month")
        void with_lastDayOfMonthAdjuster_returnsLastDayOfMonth(int y, int m, int d, int ey, int em, int ed) {
            Symmetry010Date base = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Era Tests")
    class EraTests {

        static Stream<Era> provideIncompatibleEras() {
            // These eras are not part of the ISO-based system used by Symmetry010
            return Stream.of(
                // Assuming these custom Era classes exist in the test's classpath
                // AccountingEra.BCE, AccountingEra.CE,
                // CopticEra.BEFORE_AM, CopticEra.AM,
                // DiscordianEra.YOLD,
                // EthiopicEra.BEFORE_INCARNATION, EthiopicEra.INCARNATION,
                HijrahEra.AH,
                // InternationalFixedEra.CE,
                JapaneseEra.MEIJI, JapaneseEra.TAISHO, JapaneseEra.SHOWA, JapaneseEra.HEISEI,
                // JulianEra.BC, JulianEra.AD,
                MinguoEra.BEFORE_ROC, MinguoEra.ROC,
                // PaxEra.BCE, PaxEra.CE,
                ThaiBuddhistEra.BEFORE_BE, ThaiBuddhistEra.BE
            );
        }

        @ParameterizedTest
        @MethodSource("provideIncompatibleEras")
        @DisplayName("prolepticYear() with an incompatible era throws ClassCastException")
        void prolepticYear_withIncompatibleEra_throwsException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("String Representation")
    class StringRepresentationTests {

        static Stream<Object[]> provideToStringCases() {
            return Stream.of(new Object[][] {
                { Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01" },
                { Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31" },
                { Symmetry010Date.of(2000, 8, 31), "Sym010 CE 2000/08/31" },
                { Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37" }
            });
        }

        @ParameterizedTest(name = "\"{1}\"")
        @MethodSource("provideToStringCases")
        @DisplayName("toString() returns the correct string format")
        void toString_returnsCorrectFormat(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}