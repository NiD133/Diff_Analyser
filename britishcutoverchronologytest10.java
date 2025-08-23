package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
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
import java.time.chrono.IsoEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("BritishCutoverDate Tests")
class BritishCutoverDateTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Object[][] sampleCutoverAndIsoDatesProvider() {
        return new Object[][] {
            {BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
            {BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)},
            {BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
            {BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
            {BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)},
            {BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)}, // leniently accept invalid
            {BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)}, // leniently accept invalid
            {BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)},
            {BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)},
            {BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6)},
        };
    }

    static Object[][] invalidDateProvider() {
        return new Object[][] {
            {1900, 0, 0}, {1900, -1, 1}, {1900, 0, 1}, {1900, 13, 1},
            {1900, 1, 0}, {1900, 1, 32}, {1900, 2, 30}, {1899, 2, 29},
        };
    }

    static Object[][] lengthOfMonthProvider() {
        return new Object[][] {
            {1700, 1, 31}, {1700, 2, 29}, // Julian leap year
            {1752, 2, 29}, {1752, 9, 19}, // Cutover month
            {1800, 2, 28}, // Gregorian non-leap year
            {2000, 2, 29}, // Gregorian leap year
        };
    }

    static Object[][] lengthOfYearProvider() {
        return new Object[][] {
            {1700, 366}, // Julian leap year
            {1752, 355}, // Cutover year, 11 days removed
            {1800, 365}, // Gregorian non-leap year
            {2000, 366}, // Gregorian leap year
        };
    }

    static Object[][] fieldRangeProvider() {
        return new Object[][] {
            {1752, 9, 23, DAY_OF_MONTH, 1, 30},
            {1752, 9, 23, DAY_OF_YEAR, 1, 355},
            {1752, 9, 23, ALIGNED_WEEK_OF_MONTH, 1, 3},
            {1752, 12, 23, ALIGNED_WEEK_OF_YEAR, 1, 51},
            {2011, 2, 23, DAY_OF_YEAR, 1, 365},
            {2012, 2, 23, DAY_OF_YEAR, 1, 366},
        };
    }

    static Object[][] fieldValueProvider() {
        return new Object[][] {
            {1752, 9, 2, DAY_OF_WEEK, 3},
            {1752, 9, 14, DAY_OF_WEEK, 4},
            {1752, 9, 14, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
            {2014, 5, 26, YEAR, 2014},
            {1, 6, 8, ERA, 1},
            {0, 6, 8, ERA, 0},
        };
    }

    static Object[][] withFieldProvider() {
        return new Object[][] {
            {1752, 9, 2, DAY_OF_WEEK, 4, 1752, 9, 14},
            {1752, 9, 2, DAY_OF_MONTH, 3, 1752, 9, 14}, // lenient
            {1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3, 1752, 9, 14}, // lenient
            {2012, 2, 29, YEAR, 2011, 2011, 2, 28},
            {2014, 5, 26, ERA, 0, -2013, 5, 26},
        };
    }

    static Object[][] plusMinusProvider() {
        // boolean is for bidirectionality: plus(amount) and minus(amount) should be inverse
        return new Object[][] {
            {1752, 9, 2, 1, DAYS, 1752, 9, 14, true},
            {1752, 9, 14, -1, DAYS, 1752, 9, 2, true},
            {1752, 8, 12, 1, MONTHS, 1752, 9, 23, false}, // Not bidi due to cutover
            {2014, 5, 26, 3, YEARS, 2017, 5, 26, true},
            {2014, 5, 26, -5, CENTURIES, 1514, 5, 26, true},
        };
    }

    static Object[][] untilUnitProvider() {
        return new Object[][] {
            {1752, 9, 1, 1752, 9, 14, DAYS, 2},
            {1752, 9, 2, 1752, 9, 14, DAYS, 1},
            {1752, 9, 1, 1752, 10, 1, MONTHS, 1},
            {2014, 5, 26, 2015, 5, 26, YEARS, 1},
        };
    }

    static Object[][] untilPeriodProvider() {
        return new Object[][] {
            {1752, 7, 2, 1752, 7, 1, 0, 0, -1},
            {1752, 7, 2, 1752, 7, 2, 0, 0, 0},
            // 30 days after 1752-08-02
            {1752, 7, 2, 1752, 9, 1, 0, 1, 30},
            // 2 whole months
            {1752, 7, 2, 1752, 9, 2, 0, 2, 0},
            // 1 day after 1752-09-02
            {1752, 7, 2, 1752, 9, 14, 0, 2, 1},
            // 2 months
            {1752, 7, 3, 1752, 9, 14, 0, 2, 0},
        };
    }

    static Object[][] toStringProvider() {
        return new Object[][] {
            {BritishCutoverDate.of(1, 1, 1), "BritishCutover AD 1-01-01"},
            {BritishCutoverDate.of(2012, 6, 23), "BritishCutover AD 2012-06-23"},
        };
    }

    //-----------------------------------------------------------------------
    // Test Cases
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory and Conversion Tests")
    class FactoryAndConversionTests {

        @ParameterizedTest(name = "{index}: {0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDatesProvider")
        @DisplayName("should convert to LocalDate")
        void convertsToLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest(name = "{index}: {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDatesProvider")
        @DisplayName("should create from LocalDate")
        void createFromLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest(name = "{index}: epochDay({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDatesProvider")
        @DisplayName("should create from epoch day")
        void createFromEpochDay(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest(name = "{index}: {0}.toEpochDay() -> {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDatesProvider")
        @DisplayName("should convert to epoch day")
        void convertToEpochDay(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest(name = "{index}: date({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDatesProvider")
        @DisplayName("should create from TemporalAccessor")
        void createFromTemporal(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#invalidDateProvider")
        @DisplayName("of() with invalid date should throw exception")
        void of_invalidDate_throwsException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
        }
    }

    @Nested
    @DisplayName("Property Tests")
    class PropertyTests {

        @ParameterizedTest(name = "{index}: {0}-{1} has {2} days")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#lengthOfMonthProvider")
        @DisplayName("lengthOfMonth() should return correct length")
        void lengthOfMonth_returnsCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        @ParameterizedTest(name = "{index}: year {0} has {1} days")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#lengthOfYearProvider")
        @DisplayName("lengthOfYear() should return correct length")
        void lengthOfYear_returnsCorrectLength(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
        }

        @ParameterizedTest(name = "{index}: range of {3} for {0}-{1}-{2} is {4}-{5}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#fieldRangeProvider")
        @DisplayName("range() should return correct value range for a field")
        void range_forField_returnsCorrectValueRange(int year, int month, int dayOfMonth, TemporalField field, long expectedMin, long expectedMax) {
            BritishCutoverDate date = BritishCutoverDate.of(year, month, dayOfMonth);
            assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
        }

        @ParameterizedTest(name = "{index}: {3} of {0}-{1}-{2} is {4}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#fieldValueProvider")
        @DisplayName("getLong() should return correct value for a field")
        void getLong_forField_returnsCorrectValue(int year, int month, int dayOfMonth, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, dayOfMonth).getLong(field));
        }
    }

    @Nested
    @DisplayName("Modification Tests")
    class ModificationTests {

        @ParameterizedTest(name = "{index}: {0}-{1}-{2} with {3}={4} -> {5}-{6}-{7}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#withFieldProvider")
        @DisplayName("with() a field should adjust date correctly")
        void with_field_adjustsDate(int year, int month, int dayOfMonth, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDayOfMonth) {
            BritishCutoverDate start = BritishCutoverDate.of(year, month, dayOfMonth);
            BritishCutoverDate expected = BritishCutoverDate.of(expectedYear, expectedMonth, expectedDayOfMonth);
            assertEquals(expected, start.with(field, value));
        }

        @Test
        @DisplayName("with(lastDayOfMonth) should adjust to the last day of the month")
        void with_lastDayOfMonth_adjustsCorrectly() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 30);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        @DisplayName("with(LocalDate) should adjust to the specified date")
        void with_localDate_adjustsCorrectly() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            LocalDate localDate = LocalDate.of(1752, 9, 14);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 14);
            assertEquals(expected, date.with(localDate));
        }
    }

    @Nested
    @DisplayName("Arithmetic Tests")
    class ArithmeticTests {

        @ParameterizedTest(name = "{index}: {0}.plus({3}, {4}) -> {5}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#plusMinusProvider")
        @DisplayName("plus() should add amount correctly")
        void plus_addsAmount(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom, boolean isBidirectional) {
            BritishCutoverDate start = BritishCutoverDate.of(year, month, dom);
            BritishCutoverDate expected = BritishCutoverDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{index}: {5}.minus({3}, {4}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#plusMinusProvider")
        @DisplayName("minus() should subtract amount correctly")
        void minus_subtractsAmount(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom, boolean isBidirectional) {
            if (isBidirectional) {
                BritishCutoverDate start = BritishCutoverDate.of(expectedYear, expectedMonth, expectedDom);
                BritishCutoverDate expected = BritishCutoverDate.of(year, month, dom);
                assertEquals(expected, start.minus(amount, unit));
            }
        }

        @ParameterizedTest(name = "{index}: {0}.until({1}, DAYS) is 0")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDatesProvider")
        @DisplayName("until() with same date should return zero")
        void until_sameDate_returnsZero(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(0, cutoverDate.until(isoDate, DAYS));
        }

        @ParameterizedTest(name = "{index}: {0}.until({1}, DAYS) is 0")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDatesProvider")
        @DisplayName("until() with same date as ChronoLocalDate should return zero period")
        void until_sameChronoDate_returnsZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(cutoverDate));
        }

        @ParameterizedTest(name = "{index}: {1}.until({0}) is ZERO")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDatesProvider")
        @DisplayName("LocalDate.until() with same date should return zero period")
        void localDateUntil_sameDate_returnsZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
        }

        @ParameterizedTest(name = "{index}: until({0}-{1}-{2} to {3}-{4}-{5}) in {6} is {7}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#untilUnitProvider")
        @DisplayName("until() with unit should calculate correct distance")
        void until_withUnit_calculatesDistance(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }
    }

    @Nested
    @DisplayName("Period Calculation Tests")
    class PeriodTests {

        @ParameterizedTest(name = "{index}: until({0}-{1}-{2} to {3}-{4}-{5}) is {6}Y{7}M{8}D")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#untilPeriodProvider")
        @DisplayName("until() should calculate correct period")
        void until_calculatesCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            ChronoPeriod expected = BritishCutoverChronology.INSTANCE.period(ey, em, ed);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest(name = "{index}: {0}-{1}-{2} + period until {3}-{4}-{5} should equal end date")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#untilPeriodProvider")
        @DisplayName("plus(period) should be inverse of until()")
        void plusPeriod_isInverseOfUntil(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            ChronoPeriod period = start.until(end);
            assertEquals(end, start.plus(period));
        }
    }

    @Nested
    @DisplayName("Chronology Method Tests")
    class ChronologyMethodTests {

        @Test
        @DisplayName("prolepticYear() with incorrect era type should throw ClassCastException")
        void prolepticYear_withIncorrectEra_throwsException() {
            assertThrows(ClassCastException.class, () -> BritishCutoverChronology.INSTANCE.prolepticYear(IsoEra.CE, 4));
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentationTests {

        @ParameterizedTest(name = "{index}: toString() of {0} is \"{1}\"")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#toStringProvider")
        @DisplayName("toString() should return correct format")
        void toString_returnsCorrectFormat(BritishCutoverDate cutoverDate, String expected) {
            assertEquals(expected, cutoverDate.toString());
        }
    }
}