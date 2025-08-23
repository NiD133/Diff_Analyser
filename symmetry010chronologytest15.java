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
 * Comprehensive tests for the Symmetry010Date class, covering creation, conversion,
 * manipulation, and property access.
 */
@DisplayName("Symmetry010Date: A date in the Symmetry010 calendar system")
public class Symmetry010DateTest {

    /**
     * Provides sample pairs of Symmetry010Date and their equivalent ISO LocalDate.
     * The dates are chosen based on significant historical events to ensure a wide range of tests.
     */
    static Object[][] provideSymmetryAndIsoDates() {
        return new Object[][] {
                {Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)},
                // Constantine the Great, Roman emperor (d. 337)
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
                // Christopher Columbus's expedition makes landfall in the Caribbean.
                {Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)},
                {Symmetry010Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)},
                // Galileo Galilei, Italian astronomer and physicist (d. 1642)
                {Symmetry010Date.of(1564, 2, 18), LocalDate.of(1564, 2, 15)},
                {Symmetry010Date.of(1564, 2, 15), LocalDate.of(1564, 2, 12)},
                // William Shakespeare is baptized (d. 1616).
                {Symmetry010Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)},
                {Symmetry010Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)},
                // Sir Isaac Newton, English physicist and mathematician (d. 1727)
                {Symmetry010Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)},
                {Symmetry010Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)},
                // Leonhard Euler, Swiss mathematician and physicist (d. 1783)
                {Symmetry010Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)},
                {Symmetry010Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)},
                // French Revolution: Storming of the Bastille.
                {Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)},
                {Symmetry010Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)},
                // Albert Einstein, German theoretical physicist (d. 1955)
                {Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)},
                // Dennis MacAlistair Ritchie, American computer scientist (d. 2011)
                {Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9)},
                {Symmetry010Date.of(1941, 9, 9), LocalDate.of(1941, 9, 7)},
                // Unix time begins at 00:00:00 UTC/GMT.
                {Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)},
                {Symmetry010Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)},
                // Start of the 21st century / 3rd millennium
                {Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1)},
                {Symmetry010Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3)}
        };
    }

    @Nested
    @DisplayName("Creation and Validation")
    class CreationAndValidation {

        static Object[][] provideInvalidDateComponents() {
            return new Object[][] {
                    {-1, 13, 28}, {-1, 13, 29}, {2000, -2, 1}, {2000, 13, 1}, {2000, 15, 1},
                    {2000, 1, -1}, {2000, 1, 0}, {2000, 0, 1}, {2000, -1, 0}, {2000, -1, 1},
                    {2000, 1, 31}, {2000, 2, 32}, {2000, 3, 31}, {2000, 4, 31}, {2000, 5, 32},
                    {2000, 6, 31}, {2000, 7, 31}, {2000, 8, 32}, {2000, 9, 31}, {2000, 10, 31},
                    {2000, 11, 32}, {2000, 12, 31}, {2004, 12, 38}
            };
        }

        @ParameterizedTest
        @MethodSource("provideInvalidDateComponents")
        @DisplayName("of(y, m, d) should throw exception for invalid date components")
        void shouldThrowExceptionForInvalidDateComponents(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dom));
        }

        static Object[][] provideInvalidLeapYearDay() {
            return new Object[][] {{1}, {100}, {200}, {2000}};
        }

        @ParameterizedTest
        @MethodSource("provideInvalidLeapYearDay")
        @DisplayName("of(y, 12, 37) should throw exception for non-leap years")
        void shouldThrowExceptionForInvalidLeapYearDay(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    @Nested
    @DisplayName("Conversions to and from other date types")
    class Conversions {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#provideSymmetryAndIsoDates")
        @DisplayName("should convert to an equivalent ISO LocalDate")
        void shouldConvertToIsoLocalDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym010));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#provideSymmetryAndIsoDates")
        @DisplayName("should be created from an equivalent ISO LocalDate")
        void shouldCreateFromIsoLocalDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Date.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#provideSymmetryAndIsoDates")
        @DisplayName("should convert to the correct epoch day")
        void shouldConvertToEpochDay(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym010.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#provideSymmetryAndIsoDates")
        @DisplayName("should be created from the correct epoch day")
        void shouldCreateFromEpochDay(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#provideSymmetryAndIsoDates")
        @DisplayName("should be created from a temporal accessor")
        void shouldCreateFromTemporal(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.date(iso));
        }
    }

    @Nested
    @DisplayName("Accessing date properties and fields")
    class FieldAccess {

        static Object[][] provideDatesAndExpectedMonthLengths() {
            return new Object[][] {
                    {2000, 1, 28, 30}, {2000, 2, 28, 31}, {2000, 3, 28, 30},
                    {2000, 4, 28, 30}, {2000, 5, 28, 31}, {2000, 6, 28, 30},
                    {2000, 7, 28, 30}, {2000, 8, 28, 31}, {2000, 9, 28, 30},
                    {2000, 10, 28, 30}, {2000, 11, 28, 31}, {2000, 12, 28, 30},
                    {2004, 12, 20, 37}
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndExpectedMonthLengths")
        @DisplayName("lengthOfMonth() should return the correct number of days")
        void shouldReturnCorrectLengthOfMonth(int year, int month, int day, int length) {
            assertEquals(length, Symmetry010Date.of(year, month, day).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndExpectedMonthLengths")
        @DisplayName("lengthOfMonth() should be correct when checked from the first day")
        void shouldReturnCorrectLengthOfMonthFromFirstDay(int year, int month, int day, int length) {
            assertEquals(length, Symmetry010Date.of(year, month, 1).lengthOfMonth());
        }

        static Object[][] provideFieldsAndExpectedRanges() {
            return new Object[][] {
                    {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
                    {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)},
                    {2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)},
                    {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
                    {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)},
                    {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)},
                    {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},
                    {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
                    {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
                    {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
                    {2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},
                    {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
                    {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
                    {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)}
            };
        }

        @ParameterizedTest
        @MethodSource("provideFieldsAndExpectedRanges")
        @DisplayName("range(field) should return the correct value range")
        void shouldReturnCorrectRangeForField(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, Symmetry010Date.of(year, month, dom).range(field));
        }

        static Object[][] provideFieldsAndExpectedValues() {
            return new Object[][] {
                    {2014, 5, 26, DAY_OF_WEEK, 2},
                    {2014, 5, 26, DAY_OF_MONTH, 26},
                    {2014, 5, 26, DAY_OF_YEAR, 147}, // 30+31+30+30+26
                    {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
                    {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
                    {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7},
                    {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21}, // 4+5+4+4+4
                    {2014, 5, 26, MONTH_OF_YEAR, 5},
                    {2014, 5, 26, PROLEPTIC_MONTH, 24172}, // 2014*12 + 5-1
                    {2014, 5, 26, YEAR, 2014},
                    {2014, 5, 26, ERA, 1},
                    {1, 5, 8, ERA, 1},
                    {2012, 9, 26, DAY_OF_WEEK, 1},
                    {2012, 9, 26, DAY_OF_YEAR, 269}, // 3*(4+5+4)*7 - 4
                    {2012, 9, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
                    {2012, 9, 26, ALIGNED_WEEK_OF_MONTH, 4},
                    {2012, 9, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 3},
                    {2012, 9, 26, ALIGNED_WEEK_OF_YEAR, 39}, // 3*(4+5+4)
                    {2015, 12, 37, DAY_OF_WEEK, 5},
                    {2015, 12, 37, DAY_OF_MONTH, 37},
                    {2015, 12, 37, DAY_OF_YEAR, 371}, // 4*(4+5+4)*7 + 7
                    {2015, 12, 37, ALIGNED_DAY_OF_WEEK_IN_MONTH, 2},
                    {2015, 12, 37, ALIGNED_WEEK_OF_MONTH, 6},
                    {2015, 12, 37, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7},
                    {2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53},
                    {2015, 12, 37, MONTH_OF_YEAR, 12},
                    {2015, 12, 37, PROLEPTIC_MONTH, 24191} // 2016*12 - 1
            };
        }

        @ParameterizedTest
        @MethodSource("provideFieldsAndExpectedValues")
        @DisplayName("getLong(field) should return the correct value")
        void shouldReturnCorrectValueForField(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry010Date.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date arithmetic and modification")
    class Manipulation {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#provideSymmetryAndIsoDates")
        @DisplayName("plus(0, DAYS) should be idempotent")
        void plusZeroDaysIsIdempotent(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym010.plus(0, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#provideSymmetryAndIsoDates")
        @DisplayName("plus(n, DAYS) should add days correctly")
        void shouldAddDays(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso.plusDays(1), LocalDate.from(sym010.plus(1, DAYS)));
            assertEquals(iso.plusDays(35), LocalDate.from(sym010.plus(35, DAYS)));
            assertEquals(iso.plusDays(-1), LocalDate.from(sym010.plus(-1, DAYS)));
            assertEquals(iso.plusDays(-60), LocalDate.from(sym010.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#provideSymmetryAndIsoDates")
        @DisplayName("minus(n, DAYS) should subtract days correctly")
        void shouldSubtractDays(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso.minusDays(1), LocalDate.from(sym010.minus(1, DAYS)));
            assertEquals(iso.minusDays(35), LocalDate.from(sym010.minus(35, DAYS)));
            assertEquals(iso.minusDays(-1), LocalDate.from(sym010.minus(-1, DAYS)));
            assertEquals(iso.minusDays(-60), LocalDate.from(sym010.minus(-60, DAYS)));
        }

        static Object[][] provideDatePlusAmountCases() {
            return new Object[][] {
                    {2014, 5, 26, 0, DAYS, 2014, 5, 26}, {2014, 5, 26, 8, DAYS, 2014, 6, 3},
                    {2014, 5, 26, -3, DAYS, 2014, 5, 23}, {2014, 5, 26, 0, WEEKS, 2014, 5, 26},
                    {2014, 5, 26, 3, WEEKS, 2014, 6, 16}, {2014, 5, 26, -5, WEEKS, 2014, 4, 21},
                    {2014, 5, 26, 0, MONTHS, 2014, 5, 26}, {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
                    {2014, 5, 26, -5, MONTHS, 2013, 12, 26}, {2014, 5, 26, 0, YEARS, 2014, 5, 26},
                    {2014, 5, 26, 3, YEARS, 2017, 5, 26}, {2014, 5, 26, -5, YEARS, 2009, 5, 26},
                    {2014, 5, 26, 3, DECADES, 2044, 5, 26}, {2014, 5, 26, -5, DECADES, 1964, 5, 26},
                    {2014, 5, 26, 3, CENTURIES, 2314, 5, 26}, {2014, 5, 26, -5, CENTURIES, 1514, 5, 26},
                    {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26}, {2014, 5, 26, -1, MILLENNIA, 1014, 5, 26},
                    {2014, 12, 26, 3, WEEKS, 2015, 1, 17}, {2014, 1, 26, -5, WEEKS, 2013, 12, 21},
                    {2012, 6, 26, 3, WEEKS, 2012, 7, 17}, {2012, 7, 26, -5, WEEKS, 2012, 6, 21},
                    {2012, 6, 21, 53, WEEKS, 2013, 6, 28}, {2013, 6, 21, 313, WEEKS, 2019, 6, 21}
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatePlusAmountCases")
        @DisplayName("plus(amount, unit) should add various temporal units correctly")
        void shouldAddVariousTemporalUnits(int y, int m, int d, long amt, TemporalUnit unit, int ey, int em, int ed) {
            assertEquals(Symmetry010Date.of(ey, em, ed), Symmetry010Date.of(y, m, d).plus(amt, unit));
        }

        @ParameterizedTest
        @MethodSource("provideDatePlusAmountCases")
        @DisplayName("minus(amount, unit) should subtract various temporal units correctly")
        void shouldSubtractVariousTemporalUnits(int ey, int em, int ed, long amt, TemporalUnit unit, int y, int m, int d) {
            assertEquals(Symmetry010Date.of(ey, em, ed), Symmetry010Date.of(y, m, d).minus(amt, unit));
        }

        static Object[][] provideDatePlusAmountCasesAcrossLeapWeek() {
            return new Object[][] {
                    {2015, 12, 28, 0, DAYS, 2015, 12, 28}, {2015, 12, 28, 8, DAYS, 2015, 12, 36},
                    {2015, 12, 28, -3, DAYS, 2015, 12, 25}, {2015, 12, 28, 0, WEEKS, 2015, 12, 28},
                    {2015, 12, 28, 3, WEEKS, 2016, 1, 12}, {2015, 12, 28, -5, WEEKS, 2015, 11, 24},
                    {2015, 12, 28, 52, WEEKS, 2016, 12, 21}, {2015, 12, 28, 0, MONTHS, 2015, 12, 28},
                    {2015, 12, 28, 3, MONTHS, 2016, 3, 28}, {2015, 12, 28, -5, MONTHS, 2015, 7, 28},
                    {2015, 12, 28, 12, MONTHS, 2016, 12, 28}, {2015, 12, 28, 0, YEARS, 2015, 12, 28},
                    {2015, 12, 28, 3, YEARS, 2018, 12, 28}, {2015, 12, 28, -5, YEARS, 2010, 12, 28}
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatePlusAmountCasesAcrossLeapWeek")
        @DisplayName("plus(amount, unit) should handle leap weeks correctly")
        void shouldAddVariousTemporalUnitsAcrossLeapWeek(int y, int m, int d, long amt, TemporalUnit unit, int ey, int em, int ed) {
            assertEquals(Symmetry010Date.of(ey, em, ed), Symmetry010Date.of(y, m, d).plus(amt, unit));
        }

        @ParameterizedTest
        @MethodSource("provideDatePlusAmountCasesAcrossLeapWeek")
        @DisplayName("minus(amount, unit) should handle leap weeks correctly")
        void shouldSubtractVariousTemporalUnitsAcrossLeapWeek(int ey, int em, int ed, long amt, TemporalUnit unit, int y, int m, int d) {
            assertEquals(Symmetry010Date.of(ey, em, ed), Symmetry010Date.of(y, m, d).minus(amt, unit));
        }

        static Object[][] provideFieldModificationCases() {
            return new Object[][] {
                    {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20},
                    {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
                    {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30},
                    {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3, 2014, 5, 24},
                    {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
                    {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2, 2014, 5, 21},
                    {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9},
                    {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
                    {2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26},
                    {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                    {2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26},
                    {2014, 5, 26, ERA, 1, 2014, 5, 26},
                    {2015, 12, 37, YEAR, 2004, 2004, 12, 37},
                    {2015, 12, 37, YEAR, 2013, 2013, 12, 30},
                    {2015, 3, 28, DAY_OF_YEAR, 371, 2015, 12, 37},
                    {2012, 3, 28, DAY_OF_YEAR, 364, 2012, 12, 30}
            };
        }

        @ParameterizedTest
        @MethodSource("provideFieldModificationCases")
        @DisplayName("with(field, value) should return a new date with the modified field")
        void shouldModifyFieldsCorrectly(int y, int m, int d, TemporalField f, long val, int ey, int em, int ed) {
            assertEquals(Symmetry010Date.of(ey, em, ed), Symmetry010Date.of(y, m, d).with(f, val));
        }

        static Object[][] provideInvalidFieldModificationCases() {
            return new Object[][] {
                    {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, 8},
                    {2013, 1, 1, ALIGNED_WEEK_OF_YEAR, -1},
                    {2015, 1, 1, ALIGNED_WEEK_OF_YEAR, 54},
                    {2013, 1, 1, DAY_OF_WEEK, 8},
                    {2013, 1, 1, DAY_OF_MONTH, 31},
                    {2015, 12, 1, DAY_OF_MONTH, 38},
                    {2013, 1, 1, DAY_OF_YEAR, 365},
                    {2015, 1, 1, DAY_OF_YEAR, 372},
                    {2013, 1, 1, MONTH_OF_YEAR, 14},
                    {2013, 1, 1, EPOCH_DAY, -365_961_481L},
                    {2013, 1, 1, YEAR, 1_000_001}
            };
        }

        @ParameterizedTest
        @MethodSource("provideInvalidFieldModificationCases")
        @DisplayName("with(field, value) should throw exception for invalid values")
        void shouldThrowExceptionForInvalidFieldModification(int y, int m, int d, TemporalField f, long val) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(y, m, d).with(f, val));
        }

        static Object[][] provideDatesForLastDayOfMonthAdjustment() {
            return new Object[][] {
                    {2012, 1, 23, 2012, 1, 30}, {2012, 2, 23, 2012, 2, 31},
                    {2012, 3, 23, 2012, 3, 30}, {2012, 4, 23, 2012, 4, 30},
                    {2012, 5, 23, 2012, 5, 31}, {2012, 6, 23, 2012, 6, 30},
                    {2012, 7, 23, 2012, 7, 30}, {2012, 8, 23, 2012, 8, 31},
                    {2012, 9, 23, 2012, 9, 30}, {2012, 10, 23, 2012, 10, 30},
                    {2012, 11, 23, 2012, 11, 31}, {2012, 12, 23, 2012, 12, 30},
                    {2009, 12, 23, 2009, 12, 37}
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesForLastDayOfMonthAdjustment")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) should adjust to the last day")
        void shouldAdjustToLastDayOfMonth(int y, int m, int d, int ey, int em, int ed) {
            Symmetry010Date base = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        @DisplayName("with(LocalDate) should adjust to the date from the provided LocalDate")
        void shouldAdjustToDateFromLocalDate() {
            Symmetry010Date base = Symmetry010Date.of(2000, 1, 4);
            Symmetry010Date adjusted = base.with(LocalDate.of(2012, 7, 6));
            assertEquals(Symmetry010Date.of(2012, 7, 5), adjusted);
        }
    }

    @Nested
    @DisplayName("Calculating periods between dates")
    class PeriodCalculation {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#provideSymmetryAndIsoDates")
        @DisplayName("until(sameDate) should return a zero period")
        void untilSelfShouldReturnZeroPeriod(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(sym010));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#provideSymmetryAndIsoDates")
        @DisplayName("until(equivalentIsoDate) should return a zero period")
        void untilEquivalentIsoDateShouldReturnZeroPeriod(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#provideSymmetryAndIsoDates")
        @DisplayName("isoDate.until(equivalentSymmetryDate) should return a zero period")
        void isoDateUntilEquivalentSymmetryDateShouldReturnZeroPeriod(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(sym010));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010DateTest#provideSymmetryAndIsoDates")
        @DisplayName("until(target, DAYS) should calculate the difference in days")
        void shouldCalculateDaysUntil(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(0, sym010.until(iso.plusDays(0), DAYS));
            assertEquals(1, sym010.until(iso.plusDays(1), DAYS));
            assertEquals(35, sym010.until(iso.plusDays(35), DAYS));
            assertEquals(-40, sym010.until(iso.minusDays(40), DAYS));
        }

        static Object[][] provideDatePairsForUntilCalculation() {
            return new Object[][] {
                    {2014, 5, 26, 2014, 5, 26, DAYS, 0}, {2014, 5, 26, 2014, 6, 4, DAYS, 9},
                    {2014, 5, 26, 2014, 5, 20, DAYS, -6}, {2014, 5, 26, 2014, 5, 26, WEEKS, 0},
                    {2014, 5, 26, 2014, 6, 1, WEEKS, 1}, {2014, 5, 26, 2014, 6, 5, WEEKS, 1},
                    {2014, 5, 26, 2014, 5, 26, MONTHS, 0}, {2014, 5, 26, 2014, 6, 25, MONTHS, 0},
                    {2014, 5, 26, 2014, 6, 26, MONTHS, 1}, {2014, 5, 26, 2014, 5, 26, YEARS, 0},
                    {2014, 5, 26, 2015, 5, 25, YEARS, 0}, {2014, 5, 26, 2015, 5, 26, YEARS, 1},
                    {2014, 5, 26, 2024, 5, 26, DECADES, 1}, {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
                    {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1}, {2014, 5, 26, 3014, 5, 26, ERAS, 0}
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatePairsForUntilCalculation")
        @DisplayName("until(endDate, unit) should calculate the amount of time in the specified unit")
        void shouldCalculateAmountUntilEndDateWithUnit(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Object[][] provideDatePairsForUntilPeriodCalculation() {
            return new Object[][] {
                    {2014, 5, 26, 2014, 5, 26, 0, 0, 0}, {2014, 5, 26, 2014, 6, 4, 0, 0, 9},
                    {2014, 5, 26, 2014, 5, 20, 0, 0, -6}, {2014, 5, 26, 2014, 6, 5, 0, 0, 10},
                    {2014, 5, 26, 2014, 6, 25, 0, 0, 30}, {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
                    {2014, 5, 26, 2015, 5, 25, 0, 11, 29}, {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
                    {2014, 5, 26, 2024, 5, 25, 9, 11, 29}
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatePairsForUntilPeriodCalculation")
        @DisplayName("until(endDate) should return the correct ChronoPeriod")
        void shouldCalculatePeriodUntilEndDate(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry010Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology-specific behavior")
    class ChronologyInteraction {

        static Object[][] provideUnsupportedEras() {
            return new Era[][] {
                    {AccountingEra.BCE}, {AccountingEra.CE}, {CopticEra.BEFORE_AM}, {CopticEra.AM},
                    {DiscordianEra.YOLD}, {EthiopicEra.BEFORE_INCARNATION}, {EthiopicEra.INCARNATION},
                    {HijrahEra.AH}, {InternationalFixedEra.CE}, {JapaneseEra.MEIJI},
                    {JapaneseEra.TAISHO}, {JapaneseEra.SHOWA}, {JapaneseEra.HEISEI},
                    {JulianEra.BC}, {JulianEra.AD}, {MinguoEra.BEFORE_ROC}, {MinguoEra.ROC},
                    {PaxEra.BCE}, {PaxEra.CE}, {ThaiBuddhistEra.BEFORE_BE}, {ThaiBuddhistEra.BE}
            };
        }

        @ParameterizedTest
        @MethodSource("provideUnsupportedEras")
        @DisplayName("prolepticYear() should throw exception for unsupported eras")
        void prolepticYearShouldThrowExceptionForUnsupportedEra(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("String representation")
    class Formatting {

        static Object[][] provideDatesAndStringRepresentations() {
            return new Object[][] {
                    {Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"},
                    {Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"},
                    {Symmetry010Date.of(2000, 8, 31), "Sym010 CE 2000/08/31"},
                    {Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37"}
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndStringRepresentations")
        @DisplayName("toString() should return the correct format")
        void shouldProvideCorrectStringRepresentation(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}