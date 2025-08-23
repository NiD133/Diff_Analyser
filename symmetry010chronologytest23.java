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
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Symmetry010Chronology} and {@link Symmetry010Date} classes.
 * This suite covers date creation, conversion, field access, and arithmetic.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Symmetry010Chronology and Symmetry010Date Tests")
class Symmetry010ChronologyTest {

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    /**
     * Provides pairs of equivalent dates in the Symmetry010 and ISO calendar systems.
     * The comments provide historical context for the chosen dates.
     */
    static Object[][] sampleSymmetryAndIsoDates() {
        return new Object[][] {
            // Constantine the Great, Roman emperor (d. 337)
            {Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)},
            {Symmetry010Date.of(272, 2, 27), LocalDate.of(272, 2, 26)},
            // Charlemagne, Frankish king (d. 814)
            {Symmetry010Date.of(742, 3, 27), LocalDate.of(742, 4, 2)},
            {Symmetry010Date.of(742, 4, 2), LocalDate.of(742, 4, 7)},
            // Norman Conquest: Battle of Hastings
            {Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)},
            // Francesco Petrarca - Petrarch, Italian scholar and poet (d. 1374)
            {Symmetry010Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)},
            {Symmetry010Date.of(1304, 7, 20), LocalDate.of(1304, 7, 19)},
            // Charles the Bold, Duke of Burgundy (d. 1477)
            {Symmetry010Date.of(1433, 11, 12), LocalDate.of(1433, 11, 10)},
            {Symmetry010Date.of(1433, 11, 10), LocalDate.of(1433, 11, 8)},
            // Leonardo da Vinci, Italian polymath (d. 1519)
            {Symmetry010Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)},
            {Symmetry010Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)},
            // Christopher Columbus's expedition makes landfall in the Caribbean
            {Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)},
            {Symmetry010Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)},
            // Galileo Galilei, Italian astronomer (d. 1642)
            {Symmetry010Date.of(1564, 2, 18), LocalDate.of(1564, 2, 15)},
            {Symmetry010Date.of(1564, 2, 15), LocalDate.of(1564, 2, 12)},
            // William Shakespeare is baptized (d. 1616)
            {Symmetry010Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)},
            {Symmetry010Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)},
            // Sir Isaac Newton, English physicist (d. 1727)
            {Symmetry010Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)},
            {Symmetry010Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)},
            // Leonhard Euler, Swiss mathematician (d. 1783)
            {Symmetry010Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)},
            {Symmetry010Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)},
            // French Revolution: Storming of the Bastille
            {Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)},
            {Symmetry010Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)},
            // Albert Einstein, German theoretical physicist (d. 1955)
            {Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)},
            // Dennis MacAlistair Ritchie, American computer scientist (d. 2011)
            {Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9)},
            {Symmetry010Date.of(1941, 9, 9), LocalDate.of(1941, 9, 7)},
            // Unix time begins at 00:00:00 UTC/GMT
            {Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)},
            {Symmetry010Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)},
            // Start of the 21st century / 3rd millennium
            {Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1)},
            {Symmetry010Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3)}
        };
    }

    @Nested
    @DisplayName("Factory and Conversion Tests")
    class FactoryAndConversionTests {

        @ParameterizedTest(name = "ISO({1}) should be created from Symmetry010({0})")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        void localDateFromSymmetry010Date(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym010));
        }

        @ParameterizedTest(name = "Symmetry010({0}) should be created from ISO({1})")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        void symmetry010DateFromLocalDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Date.from(iso));
        }

        @ParameterizedTest(name = "Symmetry010({0}) should be created from epoch day of ISO({1})")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        void dateFromEpochDay(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest(name = "Symmetry010({0}) and ISO({1}) should have the same epoch day")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        void toEpochDay(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym010.toEpochDay());
        }

        @ParameterizedTest(name = "Chronology.date(TemporalAccessor) should convert ISO({1}) to Symmetry010({0})")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        void chronologyDateFromTemporal(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.date(iso));
        }
    }

    @Nested
    @DisplayName("Invalid Date Creation Tests")
    class InvalidDateCreationTests {

        static Object[][] invalidDateProvider() {
            return new Object[][] {
                {-1, 13, 28}, {2000, 13, 1}, {2000, 1, 0}, {2000, 1, 31},
                {2000, 2, 32}, {2000, 4, 31}, {2000, 12, 31}, {2004, 12, 38}
            };
        }

        @ParameterizedTest(name = "of({0}, {1}, {2}) should throw DateTimeException")
        @MethodSource("invalidDateProvider")
        void of_throwsExceptionForInvalidDate(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dom));
        }

        static Object[][] invalidLeapDayProvider() {
            return new Object[][] {{1}, {100}, {200}, {2000}};
        }

        @ParameterizedTest(name = "of({0}, 12, 37) should throw for non-leap year")
        @MethodSource("invalidLeapDayProvider")
        void of_throwsExceptionForInvalidLeapDayInNonLeapYear(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    @Nested
    @DisplayName("Field, Range and Property Tests")
    class FieldAndRangeTests {

        static Object[][] lengthOfMonthProvider() {
            return new Object[][] {
                {2000, 1, 30}, {2000, 2, 31}, {2000, 3, 30}, {2000, 4, 30},
                {2000, 5, 31}, {2000, 6, 30}, {2000, 7, 30}, {2000, 8, 31},
                {2000, 9, 30}, {2000, 10, 30}, {2000, 11, 31}, {2000, 12, 30},
                {2004, 12, 37} // Leap year
            };
        }

        @ParameterizedTest(name = "Year {0}, Month {1} should have {2} days")
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth(int year, int month, int expectedLength) {
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
                {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
                {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)}, // Should be 5
                {2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},
                {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
                {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)} // Leap year
            };
        }

        @ParameterizedTest(name = "range({3}) for date {0}-{1}-{2} should be {4}")
        @MethodSource("rangeProvider")
        void range(int year, int month, int dom, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry010Date.of(year, month, dom).range(field));
        }

        static Object[][] getLongProvider() {
            return new Object[][] {
                // Date: 2014-05-26
                {2014, 5, 26, DAY_OF_WEEK, 2},
                {2014, 5, 26, DAY_OF_MONTH, 26},
                // Day of year: days in Jan(30) + Feb(31) + Mar(30) + Apr(30) + 26 days in May
                {2014, 5, 26, DAY_OF_YEAR, 30 + 31 + 30 + 30 + 26},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
                // Weeks in year: Jan(4) + Feb(5) + Mar(4) + Apr(4) + 4th week of May
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4 + 5 + 4 + 4 + 4},
                {2014, 5, 26, MONTH_OF_YEAR, 5},
                {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
                {2014, 5, 26, YEAR, 2014},
                {2014, 5, 26, ERA, 1},
                // Date: 2015-12-37 (leap year)
                {2015, 12, 37, DAY_OF_WEEK, 5},
                {2015, 12, 37, DAY_OF_MONTH, 37},
                // Day of year: 364 (normal year) + 7 (leap week)
                {2015, 12, 37, DAY_OF_YEAR, 364 + 7},
                {2015, 12, 37, ALIGNED_WEEK_OF_MONTH, 6},
                {2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53},
                {2015, 12, 37, MONTH_OF_YEAR, 12},
                {2015, 12, 37, PROLEPTIC_MONTH, 2016 * 12 - 1}
            };
        }

        @ParameterizedTest(name = "getLong({3}) for {0}-{1}-{2} should be {4}")
        @MethodSource("getLongProvider")
        void getLong(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry010Date.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Arithmetic and Manipulation Tests")
    class ArithmeticTests {

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
                {2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26},
                {2015, 12, 37, YEAR, 2004, 2004, 12, 37}, // from leap to leap
                {2015, 12, 37, YEAR, 2013, 2013, 12, 30}  // from leap to non-leap
            };
        }

        @ParameterizedTest(name = "with({3}, {4}) on {0}-{1}-{2} should be {5}-{6}-{7}")
        @MethodSource("withProvider")
        void with(int y, int m, int d, TemporalField field, long val, int ey, int em, int ed) {
            Symmetry010Date base = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, base.with(field, val));
        }

        static Object[][] withInvalidValueProvider() {
            return new Object[][] {
                {2013, 1, 1, DAY_OF_WEEK, 8},
                {2013, 1, 1, DAY_OF_MONTH, 31},
                {2013, 1, 1, DAY_OF_YEAR, 365},
                {2015, 1, 1, DAY_OF_YEAR, 372},
                {2013, 1, 1, MONTH_OF_YEAR, 14},
                {2013, 1, 1, YEAR, 1_000_001}
            };
        }

        @ParameterizedTest(name = "with({3}, {4}) on {0}-{1}-{2} should throw DateTimeException")
        @MethodSource("withInvalidValueProvider")
        void with_throwsExceptionForInvalidValue(int y, int m, int d, TemporalField f, long v) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(y, m, d).with(f, v));
        }

        static Object[][] lastDayOfMonthAdjustmentProvider() {
            return new Object[][] {
                {2012, 1, 23, 2012, 1, 30}, {2012, 2, 23, 2012, 2, 31},
                {2012, 12, 23, 2012, 12, 30}, {2009, 12, 23, 2009, 12, 37}
            };
        }

        @ParameterizedTest(name = "lastDayOfMonth() on {0}-{1}-{2} should be {3}-{4}-{5}")
        @MethodSource("lastDayOfMonthAdjustmentProvider")
        void with_lastDayOfMonth_adjustsToEndOfMonth(int y, int m, int d, int ey, int em, int ed) {
            Symmetry010Date base = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Object[][] plusProvider() {
            return new Object[][] {
                {2014, 5, 26, 8, DAYS, 2014, 6, 3},
                {2014, 5, 26, 3, WEEKS, 2014, 6, 16},
                {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
                {2014, 5, 26, 3, YEARS, 2017, 5, 26},
                {2014, 5, 26, 3, DECADES, 2044, 5, 26},
                {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
                {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
                {2012, 6, 21, 52 + 1, WEEKS, 2013, 6, 28}
            };
        }

        @ParameterizedTest(name = "plus({3}, {4}) on {0}-{1}-{2} should be {5}-{6}-{7}")
        @MethodSource("plusProvider")
        void plus(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            assertEquals(Symmetry010Date.of(ey, em, ed), Symmetry010Date.of(y, m, d).plus(amount, unit));
        }

        @ParameterizedTest(name = "minus({3}, {4}) on {5}-{6}-{7} should be {0}-{1}-{2}")
        @MethodSource("plusProvider")
        void minus(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            assertEquals(Symmetry010Date.of(ey, em, ed), Symmetry010Date.of(y, m, d).minus(amount, unit));
        }

        @Test
        @DisplayName("minus(Period) subtracts years, months, and days correctly")
        void minus_period() {
            Symmetry010Date base = Symmetry010Date.of(2014, 5, 26);
            ChronoPeriod period = Symmetry010Chronology.INSTANCE.period(0, 2, 3);
            Symmetry010Date expected = Symmetry010Date.of(2014, 3, 23);
            assertEquals(expected, base.minus(period));
        }

        static Object[][] untilPeriodProvider() {
            return new Object[][] {
                {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
                {2014, 5, 26, 2014, 6, 4, 0, 0, 9},
                {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
                {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
                {2014, 5, 26, 2024, 5, 25, 9, 11, 29}
            };
        }

        @ParameterizedTest(name = "until({3}-{4}-{5}) from {0}-{1}-{2} should be P{6}Y{7}M{8}D")
        @MethodSource("untilPeriodProvider")
        void until_returnsCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry010Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }

        @Test
        @DisplayName("until(other) should return zero period for equivalent ISO date")
        void until_isoDate_returnsZeroPeriod() {
            Symmetry010Date symDate = Symmetry010Date.of(1970, 1, 4);
            LocalDate isoDate = LocalDate.of(1970, 1, 1);
            assertEquals(Period.ZERO, isoDate.until(symDate));
        }
    }

    @Nested
    @DisplayName("Chronology-specific API Tests")
    class ChronologyApiTests {

        static Object[][] invalidErasProvider() {
            return new Era[][] {
                {IsoEra.BCE}, {CopticEra.AM}, {DiscordianEra.YOLD}, {EthiopicEra.INCARNATION},
                {HijrahEra.AH}, {JapaneseEra.HEISEI}, {JulianEra.BC}, {MinguoEra.ROC},
                {ThaiBuddhistEra.BE}
            };
        }

        @ParameterizedTest(name = "prolepticYear() should throw ClassCastException for era {0}")
        @MethodSource("invalidErasProvider")
        void prolepticYear_throwsExceptionForInvalidEra(Era era) {
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
                {Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37"}
            };
        }

        @ParameterizedTest(name = "toString() for {0} should be \"{1}\"")
        @MethodSource("toStringProvider")
        void toString(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}