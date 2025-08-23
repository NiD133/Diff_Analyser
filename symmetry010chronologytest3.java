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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
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
import java.time.temporal.ValueRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for the {@link Symmetry010Date} class.
 */
@DisplayName("Symmetry010Date Tests")
public class Symmetry010ChronologyTestTest3 {

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Object[][] sampleSymmetryAndIsoDates() {
        return new Object[][] {
            {Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)},
            {Symmetry010Date.of(272, 2, 27), LocalDate.of(272, 2, 26)},
            {Symmetry010Date.of(742, 3, 27), LocalDate.of(742, 4, 2)},
            {Symmetry010Date.of(742, 4, 2), LocalDate.of(742, 4, 7)},
            {Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)},
            {Symmetry010Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)},
            {Symmetry010Date.of(1304, 7, 20), LocalDate.of(1304, 7, 19)},
            {Symmetry010Date.of(1433, 11, 12), LocalDate.of(1433, 11, 10)},
            {Symmetry010Date.of(1433, 11, 10), LocalDate.of(1433, 11, 8)},
            {Symmetry010Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)},
            {Symmetry010Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)},
            {Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)},
            {Symmetry010Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)},
            {Symmetry010Date.of(1564, 2, 18), LocalDate.of(1564, 2, 15)},
            {Symmetry010Date.of(1564, 2, 15), LocalDate.of(1564, 2, 12)},
            {Symmetry010Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)},
            {Symmetry010Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)},
            {Symmetry010Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)},
            {Symmetry010Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)},
            {Symmetry010Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)},
            {Symmetry010Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)},
            {Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)},
            {Symmetry010Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)},
            {Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)},
            {Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9)},
            {Symmetry010Date.of(1941, 9, 9), LocalDate.of(1941, 9, 7)},
            {Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)},
            {Symmetry010Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)},
            {Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1)},
            {Symmetry010Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3)},
        };
    }

    @Nested
    @DisplayName("Conversion to/from ISO")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTestTest3#sampleSymmetryAndIsoDates")
        @DisplayName("should convert correctly between Symmetry010Date and LocalDate")
        void bidirectionalConversion(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symDate));
            assertEquals(symDate, Symmetry010Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTestTest3#sampleSymmetryAndIsoDates")
        @DisplayName("should have epoch day consistent with LocalDate")
        void epochDayConsistency(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symDate.toEpochDay());
            assertEquals(symDate, Symmetry010Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTestTest3#sampleSymmetryAndIsoDates")
        @DisplayName("Symmetry010Chronology.date(TemporalAccessor) should create correct date")
        void chronologyDateFromTemporal(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(symDate, Symmetry010Chronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Factory and Validation")
    class FactoryAndValidationTests {

        static Object[][] invalidDateProvider() {
            return new Object[][] {
                {-1, 13, 28}, {2000, 13, 1}, {2000, 1, 0}, {2000, 1, 31}, // Invalid month/day
                {2000, 2, 32}, {2000, 4, 31}, {2000, 6, 31}, {2000, 12, 31}, // Invalid day for month
                {2004, 12, 38} // Invalid day for leap year
            };
        }

        @ParameterizedTest
        @MethodSource("invalidDateProvider")
        @DisplayName("of(y, m, d) should throw for invalid date parts")
        void of_withInvalidDateParts_throwsException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, day));
        }

        static Object[][] invalidLeapDayProvider() {
            return new Object[][] {{1}, {100}, {200}, {2000}};
        }

        @ParameterizedTest
        @MethodSource("invalidLeapDayProvider")
        @DisplayName("of(y, 12, 37) should throw for non-leap years")
        void of_withInvalidLeapDay_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }

        // NOTE: The following eras are from the threeten-extra library but were not
        // imported in the original test. Assuming they are available on the classpath.
        static Object[][] invalidEraProvider() {
            return new Era[][] {
                // {AccountingEra.BCE}, {CopticEra.AM}, {DiscordianEra.YOLD},
                // {EthiopicEra.INCARNATION}, {JulianEra.BC}, {PaxEra.CE},
                {HijrahEra.AH}, {JapaneseEra.HEISEI}, {MinguoEra.ROC}, {ThaiBuddhistEra.BE}
            };
        }

        @ParameterizedTest
        @MethodSource("invalidEraProvider")
        @DisplayName("prolepticYear() should throw for eras of other chronologies")
        void prolepticYear_withForeignEra_throwsException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("Field Access and Manipulation")
    class FieldAndManipulationTests {

        static Object[][] lengthOfMonthProvider() {
            return new Object[][] {
                {2000, 1, 28, 30}, {2000, 2, 28, 31}, {2000, 3, 28, 30},
                {2000, 4, 28, 30}, {2000, 5, 28, 31}, {2000, 6, 28, 30},
                {2000, 7, 28, 30}, {2000, 8, 28, 31}, {2000, 9, 28, 30},
                {2000, 10, 28, 30}, {2000, 11, 28, 31}, {2000, 12, 28, 30},
                {2004, 12, 20, 37} // Leap year December
            };
        }

        @ParameterizedTest
        @MethodSource("lengthOfMonthProvider")
        @DisplayName("lengthOfMonth() should return correct length")
        void lengthOfMonth_returnsCorrectLength(int year, int month, int ignoredDay, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, 1).lengthOfMonth());
        }

        static Object[][] rangeProvider() {
            return new Object[][] {
                {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
                {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)},
                {2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)},
                {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
                {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)},
                {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)},
                {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},
                {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
                {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)}, // long month has 4 weeks + 3 days
                {2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)}, // leap month has 5 weeks + 2 days
                {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
                {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)},
            };
        }

        @ParameterizedTest
        @MethodSource("rangeProvider")
        @DisplayName("range(field) should return correct value range")
        void range_forField_returnsCorrectValueRange(int year, int month, int day, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry010Date.of(year, month, day).range(field));
        }

        // The inline calculations for expected values are kept for clarity.
        static Object[][] getLongProvider() {
            return new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 2},
                {2014, 5, 26, DAY_OF_MONTH, 26},
                {2014, 5, 26, DAY_OF_YEAR, 30 + 31 + 30 + 30 + 26},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4 + 5 + 4 + 4 + 4},
                {2014, 5, 26, MONTH_OF_YEAR, 5},
                {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
                {2014, 5, 26, YEAR, 2014},
                {2014, 5, 26, ERA, 1},
                {2015, 12, 37, DAY_OF_WEEK, 5},
                {2015, 12, 37, DAY_OF_MONTH, 37},
                {2015, 12, 37, DAY_OF_YEAR, 4 * (4 + 5 + 4) * 7 + 7},
                {2015, 12, 37, ALIGNED_WEEK_OF_MONTH, 6},
                {2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53},
            };
        }

        @ParameterizedTest
        @MethodSource("getLongProvider")
        @DisplayName("getLong(field) should return correct value")
        void getLong_forField_returnsCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, Symmetry010Date.of(year, month, day).getLong(field));
        }

        static Object[][] withFieldProvider() {
            return new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20},
                {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
                {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30},
                {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                {2015, 12, 37, YEAR, 2004, 2004, 12, 37}, // Leap to Leap
                {2015, 12, 37, YEAR, 2013, 2013, 12, 30}, // Leap to Common
            };
        }

        @ParameterizedTest
        @MethodSource("withFieldProvider")
        @DisplayName("with(field, value) should return new date with updated field")
        void with_fieldAndValue_returnsUpdatedDate(int year, int month, int day, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDay) {
            Symmetry010Date initial = Symmetry010Date.of(year, month, day);
            Symmetry010Date expected = Symmetry010Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, initial.with(field, value));
        }

        static Object[][] withInvalidFieldProvider() {
            return new Object[][] {
                {2013, 1, 1, DAY_OF_WEEK, 8},
                {2013, 1, 1, DAY_OF_MONTH, 31},
                {2013, 1, 1, DAY_OF_YEAR, 365},
                {2015, 1, 1, DAY_OF_YEAR, 372},
                {2013, 1, 1, MONTH_OF_YEAR, 14},
                {2013, 1, 1, YEAR, 1_000_001},
            };
        }

        @ParameterizedTest
        @MethodSource("withInvalidFieldProvider")
        @DisplayName("with(field, value) should throw for invalid value")
        void with_invalidFieldValue_throwsException(int year, int month, int day, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, day).with(field, value));
        }

        static Object[][] lastDayOfMonthProvider() {
            return new Object[][] {
                {2012, 1, 23, 2012, 1, 30},
                {2012, 2, 23, 2012, 2, 31},
                {2009, 12, 23, 2009, 12, 37},
            };
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthProvider")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) should return last day of month")
        void with_lastDayOfMonthAdjuster_returnsCorrectDate(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
            Symmetry010Date base = Symmetry010Date.of(year, month, day);
            Symmetry010Date expected = Symmetry010Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Arithmetic Operations")
    class ArithmeticTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTestTest3#sampleSymmetryAndIsoDates")
        @DisplayName("plus/minus days should be consistent with LocalDate")
        void plusAndMinusDays(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(isoDate.plusDays(1), LocalDate.from(symDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(symDate.plus(35, DAYS)));
            assertEquals(isoDate.minusDays(1), LocalDate.from(symDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(60), LocalDate.from(symDate.minus(60, DAYS)));
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
                {2013, 6, 21, 6 * 52 + 1, WEEKS, 2019, 6, 21},
            };
        }

        @ParameterizedTest
        @MethodSource("plusProvider")
        @DisplayName("plus/minus temporal units should work correctly")
        void plusAndMinus_forTemporalUnit_returnsCorrectDate(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            Symmetry010Date start = Symmetry010Date.of(startYear, startMonth, startDay);
            Symmetry010Date expectedEnd = Symmetry010Date.of(endYear, endMonth, endDay);
            assertEquals(expectedEnd, start.plus(amount, unit));
            assertEquals(start, expectedEnd.minus(amount, unit));
        }

        static Object[][] plusLeapWeekProvider() {
            return new Object[][] {
                {2015, 12, 28, 8, DAYS, 2015, 12, 36},
                {2015, 12, 28, 3, WEEKS, 2016, 1, 12},
                {2015, 12, 28, 3, MONTHS, 2016, 3, 28},
                {2015, 12, 28, 1, YEARS, 2016, 12, 28},
            };
        }

        @ParameterizedTest
        @MethodSource("plusLeapWeekProvider")
        @DisplayName("plus/minus temporal units should work correctly across leap week")
        void plusAndMinus_forTemporalUnitAcrossLeapWeek_returnsCorrectDate(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            Symmetry010Date start = Symmetry010Date.of(startYear, startMonth, startDay);
            Symmetry010Date expectedEnd = Symmetry010Date.of(endYear, endMonth, endDay);
            assertEquals(expectedEnd, start.plus(amount, unit));
            assertEquals(start, expectedEnd.minus(amount, unit));
        }

        static Object[][] untilUnitProvider() {
            return new Object[][] {
                {2014, 5, 26, 2014, 6, 4, DAYS, 9},
                {2014, 5, 26, 2014, 6, 1, WEEKS, 1},
                {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
                {2014, 5, 26, 2015, 5, 26, YEARS, 1},
                {2014, 5, 26, 2024, 5, 26, DECADES, 1},
                {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
                {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
                {2014, 5, 26, 3014, 5, 26, ERAS, 0},
            };
        }

        @ParameterizedTest
        @MethodSource("untilUnitProvider")
        @DisplayName("until(endDate, unit) should calculate amount correctly")
        void until_withEndDateAndUnit_calculatesAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Object[][] untilPeriodProvider() {
            return new Object[][] {
                {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
                {2014, 5, 26, 2014, 6, 4, 0, 0, 9},
                {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
                {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
                {2014, 5, 26, 2024, 5, 25, 9, 11, 29},
            };
        }

        @ParameterizedTest
        @MethodSource("untilPeriodProvider")
        @DisplayName("until(endDate) should return correct ChronoPeriod")
        void until_withEndDate_returnsCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry010Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTestTest3#sampleSymmetryAndIsoDates")
        @DisplayName("until(otherDate) should return zero period for equivalent dates")
        void until_withEquivalentDate_returnsZeroPeriod(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), symDate.until(symDate));
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), symDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(symDate));
        }
    }

    @Nested
    @DisplayName("General Behavior")
    class GeneralBehaviorTests {

        static Object[][] toStringProvider() {
            return new Object[][] {
                {Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"},
                {Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"},
                {Symmetry010Date.of(2000, 8, 31), "Sym010 CE 2000/08/31"},
                {Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37"},
            };
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        @DisplayName("toString() should return correct format")
        void toString_returnsCorrectFormat(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }

        @ParameterizedTest
        @ValueSource(ints = {31, 32, 33, 34, 35, 36, 37})
        @DisplayName("isLeapWeek() should be true for all days in a leap week")
        void isLeapWeek_forDaysInLeapWeek_returnsTrue(int dayOfMonth) {
            // 2015 is a leap year in the Symmetry010 calendar
            Symmetry010Date date = Symmetry010Date.of(2015, 12, dayOfMonth);
            assertTrue(date.isLeapWeek());
        }
    }
}