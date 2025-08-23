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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link Symmetry010Date}.
 */
@DisplayName("Symmetry010Date")
class Symmetry010DateTest {

    // Provides pairs of corresponding Symmetry010 and ISO dates for testing conversions.
    static Object[][] sampleSymmetryAndIsoDates() {
        return new Object[][] {
            // Early date
            {Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            // A common year date
            {Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)},
            // A modern common year date
            {Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)},
            // A modern leap year date
            {Symmetry010Date.of(1941, 9, 9), LocalDate.of(1941, 9, 7)},
            // Unix epoch
            {Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)},
            // Turn of the millennium
            {Symmetry010Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3)},
        };
    }

    @Nested
    @DisplayName("Conversion and Factory Tests")
    class ConversionAndFactoryTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#sampleSymmetryAndIsoDates")
        void conversionsToAndFromIsoDate_shouldBeConsistent(Symmetry010Date sym010, LocalDate iso) {
            assertAll("Conversions between Symmetry010 and ISO",
                () -> assertEquals(iso, LocalDate.from(sym010), "LocalDate.from(Symmetry010Date)"),
                () -> assertEquals(sym010, Symmetry010Date.from(iso), "Symmetry010Date.from(LocalDate)"),
                () -> assertEquals(iso.toEpochDay(), sym010.toEpochDay(), "toEpochDay()"),
                () -> assertEquals(sym010, Symmetry010Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()), "chronology.dateEpochDay()"),
                () -> assertEquals(sym010, Symmetry010Chronology.INSTANCE.date(iso), "chronology.date(TemporalAccessor)")
            );
        }
    }

    @Nested
    @DisplayName("Invalid Date Creation Tests")
    class InvalidDateCreationTests {

        static Object[][] invalidDateProvider() {
            return new Object[][] {
                {-1, 13, 28}, {2000, 13, 1}, {2000, 1, 0}, {2000, 0, 1},
                {2000, 1, 31}, // Jan has 30 days
                {2000, 2, 32}, // Feb has 31 days
                {2000, 4, 31}, // Apr has 30 days
                {2004, 12, 38}, // 2004 is not a leap year, Dec has 30 days
            };
        }

        @ParameterizedTest
        @MethodSource("invalidDateProvider")
        void of_shouldThrowException_forInvalidDateParts(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dom));
        }

        static Object[][] invalidLeapDayYearsProvider() {
            return new Object[][] {{1}, {100}, {200}, {2000}};
        }

        @ParameterizedTest
        @MethodSource("invalidLeapDayYearsProvider")
        void of_shouldThrowException_forLeapDayInNonLeapYear(int year) {
            // Day 37 of month 12 is only valid in a leap year
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    @Nested
    @DisplayName("Field and Range Tests")
    class FieldAndRangeTests {

        static Object[][] lengthOfMonthProvider() {
            return new Object[][] {
                {2000, 1, 28, 30}, {2000, 2, 28, 31}, {2000, 3, 28, 30},
                {2000, 4, 28, 30}, {2000, 5, 28, 31}, {2000, 6, 28, 30},
                {2000, 7, 28, 30}, {2000, 8, 28, 31}, {2000, 9, 28, 30},
                {2000, 10, 28, 30}, {2000, 11, 28, 31}, {2000, 12, 28, 30},
                {2004, 12, 20, 30}, // 2004 is not a leap year
                {2015, 12, 20, 37}, // 2015 is a leap year
            };
        }

        @ParameterizedTest
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth_shouldReturnCorrectValue(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, day).lengthOfMonth());
            assertEquals(expectedLength, Symmetry010Date.of(year, month, 1).lengthOfMonth());
        }

        static Object[][] rangeProvider() {
            return new Object[][] {
                {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
                {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)},
                {2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)}, // Leap year
                {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
                {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)},
                {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)}, // Leap year
                {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},
                {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)}, // Feb has 31 days, but weeks are aligned to 7 days
                {2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)}, // Dec in leap year has 37 days
                {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
                {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)}, // Leap year
            };
        }

        @ParameterizedTest
        @MethodSource("rangeProvider")
        void range_shouldReturnCorrectRange_forField(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, Symmetry010Date.of(year, month, dom).range(field));
        }

        static Object[][] getLongProvider() {
            return new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 2},
                {2014, 5, 26, DAY_OF_MONTH, 26},
                {2014, 5, 26, DAY_OF_YEAR, 147}, // 30+31+30+30 + 26
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21}, // 4+5+4+4 + 4
                {2014, 5, 26, MONTH_OF_YEAR, 5},
                {2014, 5, 26, PROLEPTIC_MONTH, 24172}, // 2014 * 12 + 5 - 1
                {2014, 5, 26, YEAR, 2014},
                {2014, 5, 26, ERA, 1},
                {2015, 12, 37, DAY_OF_WEEK, 5},
                {2015, 12, 37, DAY_OF_MONTH, 37},
                {2015, 12, 37, DAY_OF_YEAR, 371}, // 4*(30+31+30) + 7
                {2015, 12, 37, ALIGNED_WEEK_OF_MONTH, 6},
                {2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53},
            };
        }

        @ParameterizedTest
        @MethodSource("getLongProvider")
        void getLong_shouldReturnCorrectValue_forField(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry010Date.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Modification Tests")
    class ModificationTests {

        static Object[][] withProvider() {
            return new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20},
                {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
                {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9},
                {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
                {2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                {2015, 12, 37, YEAR, 2004, 2004, 12, 30}, // 2004 not leap, so day adjusted
                {2015, 12, 37, YEAR, 2013, 2013, 12, 30}, // 2013 not leap, so day adjusted
            };
        }

        @ParameterizedTest
        @MethodSource("withProvider")
        void with_shouldSetFieldToNewValue(int y, int m, int d, TemporalField field, long val, int ey, int em, int ed) {
            Symmetry010Date start = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, start.with(field, val));
        }

        static Object[][] withInvalidProvider() {
            return new Object[][] {
                {2013, 1, 1, DAY_OF_WEEK, 8},
                {2013, 1, 1, DAY_OF_MONTH, 31},
                {2013, 1, 1, DAY_OF_YEAR, 365}, // Not a leap year
                {2015, 1, 1, DAY_OF_YEAR, 372}, // Leap year has 371 days
                {2013, 1, 1, MONTH_OF_YEAR, 14},
                {2013, 1, 1, YEAR, 1_000_001},
            };
        }

        @ParameterizedTest
        @MethodSource("withInvalidProvider")
        void with_shouldThrowException_forInvalidValue(int y, int m, int d, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(y, m, d).with(field, value));
        }

        static Object[][] lastDayOfMonthProvider() {
            return new Object[][] {
                {2012, 1, 23, 2012, 1, 30},
                {2012, 2, 23, 2012, 2, 31},
                {2015, 12, 23, 2015, 12, 37}, // Leap year
                {2014, 12, 23, 2014, 12, 30}, // Non-leap year
            };
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthProvider")
        void with_lastDayOfMonth_shouldReturnCorrectDate(int y, int m, int d, int ey, int em, int ed) {
            Symmetry010Date start = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, start.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Arithmetic Tests")
    class ArithmeticTests {

        static Object[][] plusMinusProvider() {
            return new Object[][] {
                // Standard cases
                {2014, 5, 26, 8, DAYS, 2014, 6, 3},
                {2014, 5, 26, 3, WEEKS, 2014, 6, 16},
                {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
                {2014, 5, 26, 3, YEARS, 2017, 5, 26},
                {2014, 5, 26, 3, DECADES, 2044, 5, 26},
                {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
                {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
                // Crossing year boundary
                {2014, 12, 26, 3, WEEKS, 2015, 1, 17},
                // Involving leap week
                {2015, 12, 28, 8, DAYS, 2015, 12, 36},
                {2015, 12, 28, 3, WEEKS, 2016, 1, 12},
                {2015, 12, 28, 1, MONTHS, 2016, 1, 28},
            };
        }

        @ParameterizedTest
        @MethodSource("plusMinusProvider")
        void plus_shouldAddAmountToDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry010Date start = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusMinusProvider")
        void minus_shouldSubtractAmountFromDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry010Date start = Symmetry010Date.of(ey, em, ed);
            Symmetry010Date expected = Symmetry010Date.of(y, m, d);
            assertEquals(expected, start.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#sampleSymmetryAndIsoDates")
        void plusDays_shouldBehaveLikeIsoDate(Symmetry010Date sym010, LocalDate iso) {
            assertAll("plus days",
                () -> assertEquals(iso.plusDays(1), LocalDate.from(sym010.plus(1, DAYS))),
                () -> assertEquals(iso.plusDays(35), LocalDate.from(sym010.plus(35, DAYS))),
                () -> assertEquals(iso.plusDays(-60), LocalDate.from(sym010.plus(-60, DAYS)))
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#sampleSymmetryAndIsoDates")
        void minusDays_shouldBehaveLikeIsoDate(Symmetry010Date sym010, LocalDate iso) {
            assertAll("minus days",
                () -> assertEquals(iso.minusDays(1), LocalDate.from(sym010.minus(1, DAYS))),
                () -> assertEquals(iso.minusDays(35), LocalDate.from(sym010.minus(35, DAYS))),
                () -> assertEquals(iso.minusDays(-60), LocalDate.from(sym010.minus(-60, DAYS)))
            );
        }
    }

    @Nested
    @DisplayName("Period and Duration Tests")
    class PeriodAndDurationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#sampleSymmetryAndIsoDates")
        void until_shouldReturnZeroPeriod_forEquivalentDates(Symmetry010Date sym010, LocalDate iso) {
            assertAll("until() with equivalent dates",
                () -> assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(sym010)),
                () -> assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(iso)),
                () -> assertEquals(Period.ZERO, iso.until(sym010))
            );
        }

        static Object[][] untilWithUnitProvider() {
            return new Object[][] {
                {2014, 5, 26, 2014, 6, 4, DAYS, 9},
                {2014, 5, 26, 2014, 5, 20, DAYS, -6},
                {2014, 5, 26, 2014, 6, 5, WEEKS, 1},
                {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
                {2014, 5, 26, 2015, 5, 26, YEARS, 1},
                {2014, 5, 26, 2024, 5, 26, DECADES, 1},
                {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
                {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
                {2014, 5, 26, 3014, 5, 26, ERAS, 0},
            };
        }

        @ParameterizedTest
        @MethodSource("untilWithUnitProvider")
        void until_withUnit_shouldCalculateCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Object[][] untilWithPeriodProvider() {
            return new Object[][] {
                {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
                {2014, 5, 26, 2014, 6, 4, 0, 0, 9},
                {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
                {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
                {2014, 5, 26, 2024, 5, 25, 9, 11, 29},
            };
        }

        @ParameterizedTest
        @MethodSource("untilWithPeriodProvider")
        void until_shouldCalculateCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry010Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Era Tests")
    class EraTests {

        static Object[][] eraAndYearOfEraProvider() {
            return new Object[][] {
                // CE Era
                {1, IsoEra.CE, 1}, {100, IsoEra.CE, 100}, {200, IsoEra.CE, 200},
                // BCE Era
                {-199, IsoEra.BCE, 200}, {-1, IsoEra.BCE, 2}, {0, IsoEra.BCE, 1},
            };
        }

        @ParameterizedTest
        @MethodSource("eraAndYearOfEraProvider")
        void eraMethods_shouldWorkAsExpected(int prolepticYear, Era expectedEra, int yearOfEra) {
            Symmetry010Date date = Symmetry010Chronology.INSTANCE.date(prolepticYear, 1, 1);
            assertAll("Era properties for proleptic year " + prolepticYear,
                () -> assertEquals(prolepticYear, date.get(YEAR), "get(YEAR)"),
                () -> assertEquals(expectedEra, date.getEra(), "getEra()"),
                () -> assertEquals(yearOfEra, date.get(YEAR_OF_ERA), "get(YEAR_OF_ERA)")
            );

            // Test creation from era and year-of-era
            Symmetry010Date dateFromEra = Symmetry010Chronology.INSTANCE.date(expectedEra, yearOfEra, 1, 1);
            assertEquals(date, dateFromEra, "date(era, yearOfEra, month, day)");
        }

        static Object[][] invalidEraProvider() {
            return new Era[][] {
                {HijrahEra.AH}, {JapaneseEra.HEISEI}, {MinguoEra.ROC}, {ThaiBuddhistEra.BE}
            };
        }

        @ParameterizedTest
        @MethodSource("invalidEraProvider")
        void prolepticYear_shouldThrowException_forInvalidEra(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("Formatting Tests")
    class FormattingTests {

        static Object[][] toStringProvider() {
            return new Object[][] {
                {Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"},
                {Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"},
                {Symmetry010Date.of(2000, 8, 31), "Sym010 CE 2000/08/31"},
                {Symmetry010Date.of(2015, 12, 37), "Sym010 CE 2015/12/37"},
            };
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        void toString_shouldReturnFormattedString(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}