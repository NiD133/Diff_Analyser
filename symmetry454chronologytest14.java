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
import static org.threeten.extra.chrono.Symmetry454Chronology.DAYS_IN_MONTH;
import static org.threeten.extra.chrono.Symmetry454Chronology.DAYS_IN_MONTH_LONG;
import static org.threeten.extra.chrono.Symmetry454Chronology.DAYS_IN_QUARTER;
import static org.threeten.extra.chrono.Symmetry454Chronology.WEEKS_IN_MONTH;
import static org.threeten.extra.chrono.Symmetry454Chronology.WEEKS_IN_MONTH_LONG;

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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Symmetry454Date} class.
 */
class Symmetry454DateTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Object[][] provideSampleSymmetryAndIsoDates() {
        return new Object[][] {
            // Pairs of equivalent dates in Symmetry454 and ISO calendars.
            // Comments indicate the historical significance of the date.
            {Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)}, // Constantine the Great, Roman emperor (d. 337)
            {Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)},
            {Symmetry454Date.of(272, 2, 27), LocalDate.of(272, 2, 24)}, // Charlemagne, Frankish king (d. 814)
            {Symmetry454Date.of(742, 3, 25), LocalDate.of(742, 4, 2)},
            {Symmetry454Date.of(742, 4, 2), LocalDate.of(742, 4, 7)}, // Norman Conquest: Battle of Hastings
            {Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)}, // Francesco Petrarca - Petrarch, Italian scholar and poet (d. 1374).
            {Symmetry454Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)},
            {Symmetry454Date.of(1304, 7, 20), LocalDate.of(1304, 7, 19)}, // Charles the Bold, Duke of Burgundy (d. 1477)
            {Symmetry454Date.of(1433, 11, 14), LocalDate.of(1433, 11, 10)},
            {Symmetry454Date.of(1433, 11, 10), LocalDate.of(1433, 11, 6)}, // Leonardo da Vinci, Italian polymath (d. 1519)
            {Symmetry454Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)},
            {Symmetry454Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)}, // Christopher Columbus's expedition makes landfall in the Caribbean
            {Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)},
            {Symmetry454Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)}, // Galileo Galilei, Italian astronomer (d. 1642)
            {Symmetry454Date.of(1564, 2, 20), LocalDate.of(1564, 2, 15)},
            {Symmetry454Date.of(1564, 2, 15), LocalDate.of(1564, 2, 10)}, // William Shakespeare is baptized (d. 1616).
            {Symmetry454Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)},
            {Symmetry454Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)}, // Sir Isaac Newton, English physicist (d. 1727)
            {Symmetry454Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)},
            {Symmetry454Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)}, // Leonhard Euler, Swiss mathematician (d. 1783)
            {Symmetry454Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)},
            {Symmetry454Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)}, // French Revolution: Storming of the Bastille.
            {Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)},
            {Symmetry454Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)}, // Albert Einstein, German theoretical physicist (d. 1955).
            {Symmetry454Date.of(1879, 3, 12), LocalDate.of(1879, 3, 14)},
            {Symmetry454Date.of(1879, 3, 14), LocalDate.of(1879, 3, 16)}, // Dennis MacAlistair Ritchie, American computer scientist (d. 2011)
            {Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)}, // Unix time begins at 00:00:00 UTC/GMT.
            {Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)},
            {Symmetry454Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)}, // Start of the 21st century or 3rd millennium
            {Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1)},
            {Symmetry454Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3)}
        };
    }

    /**
     * Groups tests related to conversions and factory methods.
     */
    @Nested
    class ConversionAndFactoryTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#provideSampleSymmetryAndIsoDates")
        void from_isoDate_shouldCreateCorrectSymmetry454Date(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Date.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#provideSampleSymmetryAndIsoDates")
        void from_symmetry454Date_shouldCreateCorrectIsoDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym454));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#provideSampleSymmetryAndIsoDates")
        void chronology_dateFromIsoDate_shouldCreateCorrectSymmetry454Date(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Chronology.INSTANCE.date(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#provideSampleSymmetryAndIsoDates")
        void toEpochDay_shouldReturnSameValueAsIsoDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym454.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#provideSampleSymmetryAndIsoDates")
        void chronology_dateFromEpochDay_shouldCreateCorrectSymmetry454Date(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }
    }

    /**
     * Groups tests for creating invalid dates, which should throw exceptions.
     */
    @Nested
    class InvalidDateCreationTests {
        static Object[][] provideInvalidDateComponents() {
            return new Object[][] {
                {-1, 13, 28}, { -1, 13, 29}, {2000, -2, 1}, {2000, 13, 1}, {2000, 15, 1},
                {2000, 1, -1}, {2000, 1, 0}, {2000, 0, 1}, {2000, -1, 0}, {2000, -1, 1},
                {2000, 1, 29}, {2000, 2, 36}, {2000, 3, 29}, {2000, 4, 29}, {2000, 5, 36},
                {2000, 6, 29}, {2000, 7, 29}, {2000, 8, 36}, {2000, 9, 29}, {2000, 10, 29},
                {2000, 11, 36}, {2000, 12, 29}, {2004, 12, 36}
            };
        }

        @ParameterizedTest
        @MethodSource("provideInvalidDateComponents")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom));
        }

        static Object[][] provideYearsWithInvalidLeapDay() {
            return new Object[][] {{1}, {100}, {200}, {2000}};
        }

        @ParameterizedTest
        @MethodSource("provideYearsWithInvalidLeapDay")
        void of_withInvalidLeapDayInNonLeapYear_shouldThrowException(int year) {
            // December 29th is only valid in certain non-leap years.
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    /**
     * Groups tests for field access, ranges, and date properties.
     */
    @Nested
    class FieldAccessAndRangeTests {
        static Object[][] provideDatesAndExpectedMonthLengths() {
            return new Object[][] {
                {2000, 1, 28, 28}, {2000, 2, 28, 35}, {2000, 3, 28, 28},
                {2000, 4, 28, 28}, {2000, 5, 28, 35}, {2000, 6, 28, 28},
                {2000, 7, 28, 28}, {2000, 8, 28, 35}, {2000, 9, 28, 28},
                {2000, 10, 28, 28}, {2000, 11, 28, 35}, {2000, 12, 28, 28},
                {2004, 12, 20, 35} // Leap year
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndExpectedMonthLengths")
        void lengthOfMonth_shouldReturnCorrectValue(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, day).lengthOfMonth());
        }

        static Object[][] provideDatesAndFieldRanges() {
            return new Object[][] {
                {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
                {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
                {2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 35)}, // Leap year
                {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
                {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)},
                {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)}, // Leap year
                {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},
                {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
                {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},
                {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
                {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)} // Leap year
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndFieldRanges")
        void range_forField_shouldReturnCorrectRange(int year, int month, int dom, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(year, month, dom).range(field));
        }

        static Object[][] provideDatesAndExpectedFieldValues() {
            // The expected values are calculated using constants for clarity.
            return new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 5L},
                {2014, 5, 26, DAY_OF_MONTH, 26L},
                {2014, 5, 26, DAY_OF_YEAR, (long) DAYS_IN_MONTH + DAYS_IN_MONTH_LONG + DAYS_IN_MONTH + DAYS_IN_MONTH + 26},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L},
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, (long) WEEKS_IN_MONTH + WEEKS_IN_MONTH_LONG + WEEKS_IN_MONTH + WEEKS_IN_MONTH + 4},
                {2014, 5, 26, MONTH_OF_YEAR, 5L},
                {2014, 5, 26, PROLEPTIC_MONTH, 2014L * 12 + 5 - 1},
                {2014, 5, 26, YEAR, 2014L},
                {2014, 5, 26, ERA, 1L},
                {2012, 9, 26, DAY_OF_YEAR, (long) 2 * DAYS_IN_QUARTER + DAYS_IN_MONTH + DAYS_IN_MONTH_LONG + 26},
                {2012, 9, 26, ALIGNED_WEEK_OF_YEAR, (long) 3 * (WEEKS_IN_MONTH + WEEKS_IN_MONTH_LONG + WEEKS_IN_MONTH)},
                {2015, 12, 35, DAY_OF_WEEK, 7L}, // Leap year, last day
                {2015, 12, 35, DAY_OF_YEAR, (long) 4 * DAYS_IN_QUARTER + 7},
                {2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53L},
                {2015, 12, 35, PROLEPTIC_MONTH, 2015L * 12 + 12 - 1}
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndExpectedFieldValues")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field));
        }

        @Test
        void getLong_forUnsupportedField_shouldThrowException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry454Date.of(2012, 6, 28).getLong(MINUTE_OF_DAY));
        }
    }

    /**
     * Groups tests for date arithmetic (plus, minus, until).
     */
    @Nested
    class ArithmeticTests {
        @Test
        void until_self_returnsZeroPeriod() {
            Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        @Test
        void until_equivalentIsoDate_returnsZeroPeriod() {
            Symmetry454Date symDate = Symmetry454Date.of(1970, 1, 4);
            LocalDate isoDate = LocalDate.of(1970, 1, 1); // Equivalent date
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), symDate.until(isoDate));
        }

        @Test
        void isoDate_until_equivalentSymmetry454Date_returnsZeroPeriod() {
            LocalDate isoDate = LocalDate.of(1970, 1, 1);
            Symmetry454Date symDate = Symmetry454Date.from(isoDate); // Equivalent date
            assertEquals(Period.ZERO, isoDate.until(symDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#provideSampleSymmetryAndIsoDates")
        void plusDays_shouldAddCorrectly(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym454.plus(0, DAYS)));
            assertEquals(iso.plusDays(1), LocalDate.from(sym454.plus(1, DAYS)));
            assertEquals(iso.plusDays(35), LocalDate.from(sym454.plus(35, DAYS)));
            assertEquals(iso.plusDays(-1), LocalDate.from(sym454.plus(-1, DAYS)));
            assertEquals(iso.plusDays(-60), LocalDate.from(sym454.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#provideSampleSymmetryAndIsoDates")
        void minusDays_shouldSubtractCorrectly(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym454.minus(0, DAYS)));
            assertEquals(iso.minusDays(1), LocalDate.from(sym454.minus(1, DAYS)));
            assertEquals(iso.minusDays(35), LocalDate.from(sym454.minus(35, DAYS)));
            assertEquals(iso.minusDays(-1), LocalDate.from(sym454.minus(-1, DAYS)));
            assertEquals(iso.minusDays(-60), LocalDate.from(sym454.minus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#provideSampleSymmetryAndIsoDates")
        void until_withDaysUnit_shouldCalculateCorrectDifference(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(0, sym454.until(iso.plusDays(0), DAYS));
            assertEquals(1, sym454.until(iso.plusDays(1), DAYS));
            assertEquals(35, sym454.until(iso.plusDays(35), DAYS));
            assertEquals(-40, sym454.until(iso.minusDays(40), DAYS));
        }

        static Object[][] providePlusOperations() {
            return new Object[][] {
                {2014, 5, 26, 0, DAYS, 2014, 5, 26},
                {2014, 5, 26, 8, DAYS, 2014, 5, 34},
                {2014, 5, 26, -3, DAYS, 2014, 5, 23},
                {2014, 5, 26, 3, WEEKS, 2014, 6, 12},
                {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
                {2014, 5, 26, 3, YEARS, 2017, 5, 26},
                {2014, 5, 26, 3, DECADES, 2044, 5, 26},
                {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
                {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
                {2012, 6, 21, 52 + 1, WEEKS, 2013, 6, 28}, // Across year boundary
            };
        }

        @ParameterizedTest
        @MethodSource("providePlusOperations")
        void plus_withAmountAndUnit_shouldAddCorrectly(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("providePlusOperations")
        void minus_withAmountAndUnit_shouldSubtractCorrectly(int expectedYear, int expectedMonth, int expectedDom, long amount, TemporalUnit unit, int year, int month, int dom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Object[][] provideUntilOperations() {
            return new Object[][] {
                {2014, 5, 26, 2014, 5, 26, DAYS, 0L},
                {2014, 5, 26, 2014, 6, 4, DAYS, 13L},
                {2014, 5, 26, 2014, 5, 20, DAYS, -6L},
                {2014, 5, 26, 2014, 6, 5, WEEKS, 1L},
                {2014, 5, 26, 2014, 6, 26, MONTHS, 1L},
                {2014, 5, 26, 2015, 5, 26, YEARS, 1L},
                {2014, 5, 26, 2024, 5, 26, DECADES, 1L},
                {2014, 5, 26, 2114, 5, 26, CENTURIES, 1L},
                {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L},
                {2014, 5, 26, 3014, 5, 26, ERAS, 0L}
            };
        }

        @ParameterizedTest
        @MethodSource("provideUntilOperations")
        void until_withTemporalUnit_shouldCalculateCorrectDifference(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Object[][] provideUntilPeriodOperations() {
            return new Object[][] {
                {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
                {2014, 5, 26, 2014, 6, 4, 0, 0, 13},
                {2014, 5, 26, 2014, 5, 20, 0, 0, -6},
                {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
                {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
                {2014, 5, 26, 2024, 5, 25, 9, 11, 27}
            };
        }

        @ParameterizedTest
        @MethodSource("provideUntilPeriodOperations")
        void until_asPeriod_shouldCalculateCorrectly(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    /**
     * Groups tests for date adjustments using `with()`.
     */
    @Nested
    class AdjusterTests {
        static Object[][] provideWithOperations() {
            return new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
                {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
                {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19},
                {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
                {2014, 5, 26, PROLEPTIC_MONTH, 2013L * 12 + 3 - 1, 2013, 3, 26},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                {2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26},
                {2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29}, // Adjust to a day that doesn't exist, should truncate
                {2015, 12, 29, YEAR, 2014, 2014, 12, 28}, // Adjust to non-leap year, should truncate
                {2015, 3, 28, DAY_OF_YEAR, 371, 2015, 12, 35} // Leap year
            };
        }

        @ParameterizedTest
        @MethodSource("provideWithOperations")
        void with_fieldAndValue_shouldAdjustCorrectly(int y, int m, int d, TemporalField field, long value, int eY, int eM, int eD) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(eY, eM, eD);
            assertEquals(expected, start.with(field, value));
        }

        static Object[][] provideInvalidWithOperations() {
            return new Object[][] {
                {2013, 1, 1, DAY_OF_WEEK, 8},
                {2013, 1, 1, DAY_OF_MONTH, 29},
                {2013, 1, 1, DAY_OF_YEAR, 365},
                {2015, 1, 1, DAY_OF_YEAR, 372}, // Leap year
                {2013, 1, 1, MONTH_OF_YEAR, 14},
                {2013, 1, 1, YEAR, 1_000_001}
            };
        }

        @ParameterizedTest
        @MethodSource("provideInvalidWithOperations")
        void with_invalidFieldAndValue_shouldThrowException(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom).with(field, value));
        }

        static Object[][] provideLastDayOfMonthAdjustments() {
            return new Object[][] {
                {2012, 1, 23, 2012, 1, 28},
                {2012, 2, 23, 2012, 2, 35},
                {2009, 12, 23, 2009, 12, 35} // Leap year
            };
        }

        @ParameterizedTest
        @MethodSource("provideLastDayOfMonthAdjustments")
        void with_lastDayOfMonthAdjuster_shouldReturnCorrectDate(int y, int m, int d, int eY, int eM, int eD) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(eY, eM, eD);
            assertEquals(expected, start.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    /**
     * Groups tests for interactions with the Chronology.
     */
    @Nested
    class ChronologyInteractionTests {
        static Object[][] provideUnsupportedErasForProlepticYear() {
            // Test with a few representative eras from other calendar systems.
            // The method should only accept IsoEra.
            return new Object[][] {
                {HijrahEra.AH},
                {JapaneseEra.HEISEI},
                {MinguoEra.ROC},
                {ThaiBuddhistEra.BE}
            };
        }

        @ParameterizedTest
        @MethodSource("provideUnsupportedErasForProlepticYear")
        void prolepticYear_withUnsupportedEra_shouldThrowException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    /**
     * Groups miscellaneous tests like toString().
     */
    @Nested
    class MiscellaneousTests {
        static Object[][] provideDatesAndStringRepresentations() {
            return new Object[][] {
                {Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"},
                {Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"},
                {Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35"}
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndStringRepresentations")
        void toString_shouldReturnCorrectFormat(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}