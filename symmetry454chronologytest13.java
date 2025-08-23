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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Symmetry454Chronology} and {@link Symmetry454Date}.
 */
public class Symmetry454ChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Object[]> sampleSymmetryAndIsoDates() {
        return Stream.of(new Object[][] {
            {Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)},
            {Symmetry454Date.of(272, 2, 27), LocalDate.of(272, 2, 24)},
            {Symmetry454Date.of(742, 3, 25), LocalDate.of(742, 4, 2)},
            {Symmetry454Date.of(742, 4, 2), LocalDate.of(742, 4, 7)},
            {Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)},
            {Symmetry454Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)},
            {Symmetry454Date.of(1304, 7, 20), LocalDate.of(1304, 7, 19)},
            {Symmetry454Date.of(1433, 11, 14), LocalDate.of(1433, 11, 10)},
            {Symmetry454Date.of(1433, 11, 10), LocalDate.of(1433, 11, 6)},
            {Symmetry454Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)},
            {Symmetry454Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)},
            {Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)},
            {Symmetry454Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)},
            {Symmetry454Date.of(1564, 2, 20), LocalDate.of(1564, 2, 15)},
            {Symmetry454Date.of(1564, 2, 15), LocalDate.of(1564, 2, 10)},
            {Symmetry454Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)},
            {Symmetry454Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)},
            {Symmetry454Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)},
            {Symmetry454Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)},
            {Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)},
            {Symmetry454Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)},
            {Symmetry454Date.of(1879, 3, 12), LocalDate.of(1879, 3, 14)},
            {Symmetry454Date.of(1879, 3, 14), LocalDate.of(1879, 3, 16)},
            {Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)},
            {Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)},
            {Symmetry454Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)},
            {Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1)},
            {Symmetry454Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3)}
        });
    }

    @Nested
    class ConversionTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void conversionToAndFromIsoIsConsistent(Symmetry454Date sym454Date, LocalDate isoDate) {
            // Test conversions between Symmetry454Date and ISO (LocalDate)
            assertEquals(isoDate, LocalDate.from(sym454Date));
            assertEquals(sym454Date, Symmetry454Date.from(isoDate));
            assertEquals(sym454Date, Symmetry454Chronology.INSTANCE.date(isoDate));

            // Test epoch day conversions
            assertEquals(isoDate.toEpochDay(), sym454Date.toEpochDay());
            assertEquals(sym454Date, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));

            // A date's period until its equivalent in another chronology should be zero
            assertEquals(Period.ZERO, isoDate.until(sym454Date));
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454Date.until(isoDate));
        }
    }

    @Nested
    class InvalidDateTests {
        static Stream<Object[]> invalidDateComponentsProvider() {
            return Stream.of(new Object[][] {
                {-1, 13, 28}, { -1, 13, 29}, {2000, -2, 1}, {2000, 13, 1}, {2000, 15, 1},
                {2000, 1, -1}, {2000, 1, 0}, {2000, 0, 1}, {2000, -1, 0}, {2000, -1, 1},
                {2000, 1, 29}, {2000, 2, 36}, {2000, 3, 29}, {2000, 4, 29}, {2000, 5, 36},
                {2000, 6, 29}, {2000, 7, 29}, {2000, 8, 36}, {2000, 9, 29}, {2000, 10, 29},
                {2000, 11, 36}, {2000, 12, 29}, {2004, 12, 36}
            });
        }

        @ParameterizedTest
        @MethodSource("invalidDateComponentsProvider")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dayOfMonth));
        }

        static Stream<Object[]> invalidLeapYearDayProvider() {
            return Stream.of(new Object[][] {{1}, {100}, {200}, {2000}});
        }

        @ParameterizedTest
        @MethodSource("invalidLeapYearDayProvider")
        void of_withInvalidLeapDayInNonLeapYear_shouldThrowException(int year) {
            // December has 35 days only in a leap year.
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    class FieldAccessTests {
        static Stream<Object[]> monthLengthProvider() {
            return Stream.of(new Object[][] {
                {2000, 1, 28}, {2000, 2, 35}, {2000, 3, 28}, {2000, 4, 28}, {2000, 5, 35},
                {2000, 6, 28}, {2000, 7, 28}, {2000, 8, 35}, {2000, 9, 28}, {2000, 10, 28},
                {2000, 11, 35}, {2000, 12, 28}, // 2000 is not a leap year in Symmetry454
                {2004, 12, 35} // 2004 is a leap year in Symmetry454
            });
        }

        @ParameterizedTest
        @MethodSource("monthLengthProvider")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Object[]> fieldValueRangeProvider() {
            return Stream.of(new Object[][] {
                // DAY_OF_MONTH
                {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
                {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
                {2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 35)}, // Leap year December

                // DAY_OF_WEEK
                {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},

                // DAY_OF_YEAR
                {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)}, // Non-leap year
                {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)}, // Leap year

                // MONTH_OF_YEAR
                {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},

                // ALIGNED fields
                {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
                {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
                {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)}, // Long month
                {2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)}, // Leap week month
                {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
                {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
                {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)} // Leap week
            });
        }

        @ParameterizedTest
        @MethodSource("fieldValueRangeProvider")
        void range_forField_shouldReturnCorrectValueRange(int year, int month, int day, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(year, month, day).range(field));
        }

        static Stream<Object[]> fieldValueProvider() {
            return Stream.of(new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 5L},
                {2014, 5, 26, DAY_OF_MONTH, 26L},
                {2014, 5, 26, DAY_OF_YEAR, 145L}, // 28+35+28+28+26
                {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5L},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L},
                {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5L},
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L}, // (4+5+4+4)+4
                {2014, 5, 26, MONTH_OF_YEAR, 5L},
                {2014, 5, 26, PROLEPTIC_MONTH, 24172L}, // 2014 * 12 + 5 - 1
                {2014, 5, 26, YEAR, 2014L},
                {2014, 5, 26, ERA, 1L},
                {1, 5, 8, ERA, 1L},

                // Leap year date
                {2015, 12, 35, DAY_OF_WEEK, 7L},
                {2015, 12, 35, DAY_OF_MONTH, 35L},
                {2015, 12, 35, DAY_OF_YEAR, 371L}, // 364 + 7
                {2015, 12, 35, ALIGNED_DAY_OF_WEEK_IN_MONTH, 7L},
                {2015, 12, 35, ALIGNED_WEEK_OF_MONTH, 5L},
                {2015, 12, 35, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7L},
                {2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53L},
                {2015, 12, 35, MONTH_OF_YEAR, 12L},
                {2015, 12, 35, PROLEPTIC_MONTH, 24191L} // 2015 * 12 + 12 - 1
            });
        }

        @ParameterizedTest
        @MethodSource("fieldValueProvider")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, day).getLong(field));
        }
    }

    @Nested
    class DateModificationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void plusDays_shouldBehaveLikeIsoPlusDays(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(sym454Date.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(sym454Date.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(sym454Date.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(sym454Date.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(sym454Date.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void minusDays_shouldBehaveLikeIsoMinusDays(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(sym454Date.minus(0, DAYS)));
            assertEquals(isoDate.minusDays(1), LocalDate.from(sym454Date.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(sym454Date.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(sym454Date.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(sym454Date.minus(-60, DAYS)));
        }

        static Stream<Object[]> dateWithFieldAdjustmentProvider() {
            return Stream.of(new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
                {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
                {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19},
                {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
                {2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                {2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26},
                {2015, 12, 29, YEAR, 2014, 2014, 12, 28}, // Adjusting leap date to non-leap year
                {2015, 3, 28, DAY_OF_YEAR, 371, 2015, 12, 35} // Adjust to end of leap year
            });
        }

        @ParameterizedTest
        @MethodSource("dateWithFieldAdjustmentProvider")
        void with_forField_shouldReturnAdjustedDate(int year, int month, int dom, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Object[]> dateWithInvalidFieldAdjustmentProvider() {
            return Stream.of(new Object[][] {
                {2013, 1, 1, ALIGNED_WEEK_OF_MONTH, 5}, {2013, 2, 1, ALIGNED_WEEK_OF_MONTH, 6},
                {2013, 1, 1, ALIGNED_WEEK_OF_YEAR, 53}, {2015, 1, 1, ALIGNED_WEEK_OF_YEAR, 54},
                {2013, 1, 1, DAY_OF_WEEK, 8}, {2013, 1, 1, DAY_OF_MONTH, 29},
                {2015, 12, 1, DAY_OF_MONTH, 36}, {2013, 1, 1, DAY_OF_YEAR, 365},
                {2015, 1, 1, DAY_OF_YEAR, 372}, {2013, 1, 1, MONTH_OF_YEAR, 14},
                {2013, 1, 1, EPOCH_DAY, 364_523_156L}, {2013, 1, 1, YEAR, 1_000_001}
            });
        }

        @ParameterizedTest
        @MethodSource("dateWithInvalidFieldAdjustmentProvider")
        void with_forFieldWithInvalidValue_shouldThrowException(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom).with(field, value));
        }

        static Stream<Object[]> lastDayOfMonthProvider() {
            return Stream.of(new Object[][] {
                {2012, 1, 23, 2012, 1, 28}, {2012, 2, 23, 2012, 2, 35},
                {2012, 12, 23, 2012, 12, 28}, {2009, 12, 23, 2009, 12, 35} // 2009 is a leap year
            });
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthProvider")
        void with_lastDayOfMonth_shouldReturnCorrectDate(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
            Symmetry454Date base = Symmetry454Date.of(year, month, day);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Object[]> datePlusAmountProvider() {
            return Stream.of(new Object[][] {
                {2014, 5, 26, 0, DAYS, 2014, 5, 26}, {2014, 5, 26, 8, DAYS, 2014, 5, 34},
                {2014, 5, 26, 3, WEEKS, 2014, 6, 12}, {2014, 5, 26, -5, WEEKS, 2014, 4, 19},
                {2014, 5, 26, 3, MONTHS, 2014, 8, 26}, {2014, 5, 26, -5, MONTHS, 2013, 12, 26},
                {2014, 5, 26, 3, YEARS, 2017, 5, 26}, {2014, 5, 26, -5, YEARS, 2009, 5, 26},
                {2014, 5, 26, 3, DECADES, 2044, 5, 26}, {2014, 5, 26, -5, DECADES, 1964, 5, 26},
                {2014, 5, 26, 3, CENTURIES, 2314, 5, 26}, {2014, 5, 26, -5, CENTURIES, 1514, 5, 26},
                {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26}, {2014, 5, 26, -1, MILLENNIA, 1014, 5, 26}
            });
        }

        @ParameterizedTest
        @MethodSource("datePlusAmountProvider")
        void plus_withUnit_shouldReturnCorrectlyAddedDate(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("datePlusAmountProvider")
        void minus_withUnit_shouldReturnCorrectlySubtractedDate(int expectedYear, int expectedMonth, int expectedDom, long amount, TemporalUnit unit, int year, int month, int dom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Stream<Object[]> datePlusAmountLeapWeekProvider() {
            return Stream.of(new Object[][] {
                {2015, 12, 28, 8, DAYS, 2016, 1, 1}, // Crosses leap week
                {2015, 12, 28, 3, WEEKS, 2016, 1, 14},
                {2015, 12, 28, 52, WEEKS, 2016, 12, 21},
                {2015, 12, 28, 3, MONTHS, 2016, 3, 28},
                {2015, 12, 28, 12, MONTHS, 2016, 12, 28}
            });
        }

        @ParameterizedTest
        @MethodSource("datePlusAmountLeapWeekProvider")
        void plus_onDateBeforeLeapWeek_shouldHandleLeapWeekCorrectly(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }
    }

    @Nested
    class PeriodAndDurationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void until_withDaysUnit_shouldCalculateCorrectDistance(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(0, sym454Date.until(isoDate.plusDays(0), DAYS));
            assertEquals(1, sym454Date.until(isoDate.plusDays(1), DAYS));
            assertEquals(35, sym454Date.until(isoDate.plusDays(35), DAYS));
            assertEquals(-40, sym454Date.until(isoDate.minusDays(40), DAYS));
        }

        static Stream<Object[]> dateUntilProvider() {
            return Stream.of(new Object[][] {
                {2014, 5, 26, 2014, 5, 26, DAYS, 0}, {2014, 5, 26, 2014, 6, 4, DAYS, 13},
                {2014, 5, 26, 2014, 5, 20, DAYS, -6}, {2014, 5, 26, 2014, 6, 5, WEEKS, 1},
                {2014, 5, 26, 2014, 6, 26, MONTHS, 1}, {2014, 5, 26, 2015, 5, 26, YEARS, 1},
                {2014, 5, 26, 2024, 5, 26, DECADES, 1}, {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
                {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1}, {2014, 5, 26, 3014, 5, 26, ERAS, 0}
            });
        }

        @ParameterizedTest
        @MethodSource("dateUntilProvider")
        void until_withUnit_shouldReturnCorrectDuration(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Object[]> dateUntilPeriodProvider() {
            return Stream.of(new Object[][] {
                {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
                {2014, 5, 26, 2014, 6, 4, 0, 0, 13},
                {2014, 5, 26, 2014, 5, 20, 0, 0, -6},
                {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
                {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
                {2014, 5, 26, 2024, 5, 25, 9, 11, 27}
            });
        }

        @ParameterizedTest
        @MethodSource("dateUntilPeriodProvider")
        void until_asPeriod_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    class GeneralMethodTests {
        static Stream<Object[]> toStringProvider() {
            return Stream.of(new Object[][] {
                {Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"},
                {Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"},
                {Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35"}
            });
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        void toString_shouldReturnFormattedString(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }

        @Test
        void range_forUnsupportedField_shouldThrowException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry454Date.of(2012, 6, 28).range(MINUTE_OF_DAY));
        }

        static Stream<Era> unsupportedEraProvider() {
            // Provides various Era singletons that are not IsoEra
            return Stream.of(
                HijrahEra.AH,
                JapaneseEra.HEISEI,
                MinguoEra.ROC,
                ThaiBuddhistEra.BE
            );
        }

        @ParameterizedTest
        @MethodSource("unsupportedEraProvider")
        void prolepticYear_withUnsupportedEra_shouldThrowClassCastException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }
}