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
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link InternationalFixedDate} class.
 */
@DisplayName("InternationalFixedDate")
class InternationalFixedDateTest {

    // A date early in year 1, used to avoid testing subtractions that would result in an unsupported year (year < 1).
    private static final LocalDate EARLIEST_DATE_FOR_MINUS_DAYS_TEST = LocalDate.ofYearDay(1, 35);
    private static final LocalDate EARLIEST_DATE_FOR_MINUS_LARGE_DAYS_TEST = LocalDate.ofYearDay(1, 60);
    private static final LocalDate EARLIEST_DATE_FOR_UNTIL_TEST = LocalDate.ofYearDay(1, 40);

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Object[][] sampleFixedAndIsoDates() {
        return new Object[][] {
            {InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {InternationalFixedDate.of(1, 1, 2), LocalDate.of(1, 1, 2)},
            {InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)},
            {InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)},
            {InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)},
            {InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)},
            {InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)}, // Leap year
            {InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)},
            {InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4)},
        };
    }

    static Object[][] invalidDateComponents() {
        return new Object[][] {
            // year, month, dayOfMonth
            {-1, 13, 28}, {0, 1, 1},
            {1900, -2, 1}, {1900, 14, 1}, {1900, 15, 1},
            {1900, 1, -1}, {1900, 1, 0}, {1900, 1, 29}, // Month 1 has 28 days
            {1900, 13, 30}, // Month 13 has 29 days
            {1900, 6, 29}, // Month 6 has 28 days in non-leap year 1900
        };
    }

    static Object[][] nonLeapYears() {
        return new Object[][] {{1}, {100}, {200}, {300}, {1900}};
    }

    static Object[][] monthLengths() {
        // year, month, lastDayOfMonth, expectedLength
        return new Object[][] {
            {1900, 1, 28, 28},
            {1900, 6, 28, 28}, // Non-leap year
            {1900, 13, 29, 29},
            {1904, 6, 29, 29}, // Leap year
        };
    }

    static Object[][] invalidEraValues() {
        return new Object[][] {{-1}, {0}, {2}};
    }

    static Object[][] invalidProlepticYears() {
        return new Object[][] {{-10}, {-1}, {0}};
    }

    static Object[][] fieldValueRanges() {
        // year, month, dayOfMonth, field, expectedRange
        return new Object[][] {
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 29)}, // Leap month
            {2012, 13, 23, DAY_OF_MONTH, ValueRange.of(1, 29)}, // Year-day month
            {2011, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 365)}, // Non-leap year
            {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366)}, // Leap year
            {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)},
            // Special handling for leap day and year day
            {2012, 6, 29, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(0, 0)},
            {2012, 13, 29, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(0, 0)},
            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
        };
    }

    static Object[][] fieldValueSamples() {
        // year, month, dayOfMonth, field, expectedValue
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 5},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            // Day of year in non-leap 2014: (5-1)*28 + 26 = 138
            {2014, 5, 26, DAY_OF_YEAR, 138},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 20},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 13 + 5 - 1},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},
            // Day of year in leap 2012, after leap day: (9-1)*28 + 26 + 1 (leap day) = 251
            {2012, 9, 26, DAY_OF_YEAR, 251},
            // Year Day is day 365 in non-leap year, 366 in leap year
            {2014, 13, 29, DAY_OF_YEAR, 365},
            {2012, 13, 29, DAY_OF_YEAR, 366},
            // Leap Day is day 169
            {2012, 6, 29, DAY_OF_YEAR, 169},
        };
    }

    static Object[][] withFieldSamples() {
        // year, month, dom, field, value, expectedYear, expectedMonth, expectedDom
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
            {2014, 5, 26, DAY_OF_YEAR, 365, 2014, 13, 29},
            {2012, 3, 28, DAY_OF_YEAR, 366, 2012, 13, 29},
            {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            // Adjusting a leap day to a non-leap year results in the previous day
            {2012, 6, 29, YEAR, 2013, 2013, 6, 28},
        };
    }

    static Object[][] withInvalidFieldValues() {
        // year, month, dom, field, value
        return new Object[][] {
            {2013, 1, 1, DAY_OF_MONTH, 29},
            {2013, 6, 1, DAY_OF_MONTH, 29}, // Not a leap year
            {2012, 6, 1, DAY_OF_MONTH, 30},
            {2013, 1, 1, DAY_OF_YEAR, 366}, // Not a leap year
            {2012, 1, 1, DAY_OF_YEAR, 367},
            {2013, 1, 1, MONTH_OF_YEAR, 14},
            {2013, 1, 1, YEAR, 0},
        };
    }

    static Object[][] lastDayOfMonthSamples() {
        // year, month, day, expectedYear, expectedMonth, expectedDay
        return new Object[][] {
            {2012, 6, 23, 2012, 6, 29}, // Leap month
            {2012, 6, 29, 2012, 6, 29},
            {2009, 6, 23, 2009, 6, 28}, // Non-leap month
            {2007, 13, 23, 2007, 13, 29}, // Year-day month
        };
    }

    static Object[][] plusMinusSamples() {
        // startYear, startMonth, startDay, amount, unit, endYear, endMonth, endDay
        return new Object[][] {
            {2014, 5, 26, 8, DAYS, 2014, 6, 6},
            {2014, 5, 26, 3, WEEKS, 2014, 6, 19},
            {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
            {2014, 5, 26, 3, YEARS, 2017, 5, 26},
            {2014, 5, 26, 3, DECADES, 2044, 5, 26},
            {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
            {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
        };
    }

    static Object[][] plusOnSpecialDaysSamples() {
        // startYear, startMonth, startDay, amount, unit, endYear, endMonth, endDay
        return new Object[][] {
            // From Year Day
            {2014, 13, 29, 8, DAYS, 2015, 1, 8},
            {2014, 13, 29, 3, MONTHS, 2015, 3, 28},
            {2014, 13, 29, 3, YEARS, 2017, 13, 29},
            // From Leap Day
            {2012, 6, 29, 8, DAYS, 2012, 7, 8},
            {2012, 6, 29, 3, MONTHS, 2012, 9, 28},
            {2012, 6, 29, 3, YEARS, 2015, 6, 28}, // To non-leap year
            {2012, 6, 29, 4, YEARS, 2016, 6, 29}, // To leap year
        };
    }

    static Object[][] minusOnSpecialDaysSamples() {
        // endYear, endMonth, endDay, amount, unit, startYear, startMonth, startDay
        return new Object[][] {
            // To Year Day
            {2015, 1, 8, 8, DAYS, 2014, 13, 29},
            {2015, 3, 28, 3, MONTHS, 2014, 13, 29},
            {2017, 13, 29, 3, YEARS, 2014, 13, 29},
            // To Leap Day
            {2012, 7, 8, 8, DAYS, 2012, 6, 29},
            {2012, 9, 28, 3, MONTHS, 2012, 6, 29},
            {2015, 6, 28, 3, YEARS, 2012, 6, 29}, // From non-leap year
            {2016, 6, 29, 4, YEARS, 2012, 6, 29}, // From leap year
        };
    }

    static Object[][] untilSamples() {
        // y1, m1, d1, y2, m2, d2, unit, expectedAmount
        return new Object[][] {
            {2014, 5, 26, 2014, 6, 4, DAYS, 6},
            {2014, 5, 26, 2014, 6, 5, WEEKS, 1},
            {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
            {2014, 5, 26, 2015, 5, 26, YEARS, 1},
            {2014, 5, 26, 2024, 5, 26, DECADES, 1},
            {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
            {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
            {2014, 5, 26, 3014, 5, 26, ERAS, 0},
            // Across Year Day
            {2014, 13, 28, 2015, 1, 1, DAYS, 2},
            // Across Leap Day
            {2012, 6, 28, 2012, 7, 1, DAYS, 2},
        };
    }

    static Object[][] untilPeriodSamples() {
        // y1, m1, d1, y2, m2, d2, expectedYears, expectedMonths, expectedDays
        return new Object[][] {
            {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
            {2014, 5, 26, 2014, 6, 4, 0, 0, 6},
            {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
            {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
            // Start with Year Day
            {2003, 13, 29, 2004, 6, 29, 0, 6, 0},
            // Start with Leap Day
            {2008, 6, 29, 2012, 6, 29, 4, 0, 0},
        };
    }

    static Object[][] toStringSamples() {
        // date, expectedString
        return new Object[][] {
            {InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"},
            {InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"},
            {InternationalFixedDate.of(1, 13, 29), "Ifc CE 1/13/29"},
            {InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"}, // Leap Day
            {InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29"}, // Year Day
        };
    }

    //-----------------------------------------------------------------------
    // Test Cases
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Creation and Validation")
    class CreationAndValidationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#invalidDateComponents")
        @DisplayName("of(year, month, day) with invalid values throws exception")
        void test_of_withInvalidDateParts_throwsException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dom));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#nonLeapYears")
        @DisplayName("of(year, 6, 29) for a non-leap year throws exception")
        void test_of_withInvalidLeapDay_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleFixedAndIsoDates")
        @DisplayName("chronology.date(TemporalAccessor) creates correct date")
        void test_chronology_date_fromTemporal_createsCorrectDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedChronology.INSTANCE.date(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleFixedAndIsoDates")
        @DisplayName("chronology.dateEpochDay(long) creates correct date")
        void test_chronology_dateEpochDay_createsCorrectDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }
    }

    @Nested
    @DisplayName("Conversions to/from other calendar systems")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleFixedAndIsoDates")
        @DisplayName("LocalDate.from(fixedDate) converts correctly")
        void test_fromFixedDate_toIsoDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso, LocalDate.from(fixed));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleFixedAndIsoDates")
        @DisplayName("InternationalFixedDate.from(isoDate) converts correctly")
        void test_fromIsoDate_toFixedDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleFixedAndIsoDates")
        @DisplayName("toEpochDay() returns correct value")
        void test_toEpochDay_returnsCorrectValue(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso.toEpochDay(), fixed.toEpochDay());
        }
    }

    @Nested
    @DisplayName("Field Access")
    class FieldAccessTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#monthLengths")
        @DisplayName("lengthOfMonth() is independent of the day of month")
        void test_lengthOfMonth_isIndependentOfDay(int year, int month, int lastDayOfMonth, int expectedLength) {
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, 1).lengthOfMonth());
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, lastDayOfMonth).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#fieldValueRanges")
        @DisplayName("range(field) returns correct value range")
        void test_range_forField_returnsCorrectValueRange(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, InternationalFixedDate.of(year, month, dom).range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#fieldValueSamples")
        @DisplayName("getLong(field) returns correct value")
        void test_getLong_forField_returnsCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, InternationalFixedDate.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class DateManipulationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleFixedAndIsoDates")
        @DisplayName("plus(days) adds correctly")
        void test_plusDays_addsCorrectly(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso, LocalDate.from(fixed.plus(0, DAYS)));
            assertEquals(iso.plusDays(1), LocalDate.from(fixed.plus(1, DAYS)));
            assertEquals(iso.plusDays(35), LocalDate.from(fixed.plus(35, DAYS)));

            if (EARLIEST_DATE_FOR_MINUS_LARGE_DAYS_TEST.isBefore(iso)) {
                assertEquals(iso.plusDays(-1), LocalDate.from(fixed.plus(-1, DAYS)));
                assertEquals(iso.plusDays(-60), LocalDate.from(fixed.plus(-60, DAYS)));
            }
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleFixedAndIsoDates")
        @DisplayName("minus(days) subtracts correctly")
        void test_minusDays_subtractsCorrectly(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso, LocalDate.from(fixed.minus(0, DAYS)));

            if (EARLIEST_DATE_FOR_MINUS_DAYS_TEST.isBefore(iso)) {
                assertEquals(iso.minusDays(1), LocalDate.from(fixed.minus(1, DAYS)));
                assertEquals(iso.minusDays(35), LocalDate.from(fixed.minus(35, DAYS)));
            }
            assertEquals(iso.minusDays(-1), LocalDate.from(fixed.minus(-1, DAYS)));
            assertEquals(iso.minusDays(-60), LocalDate.from(fixed.minus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#plusMinusSamples")
        @DisplayName("plus() and minus() are inverse operations")
        void test_plusAndMinus_areInverseOperations(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, dom);
            InternationalFixedDate end = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(end, start.plus(amount, unit));
            assertEquals(start, end.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#plusOnSpecialDaysSamples")
        @DisplayName("plus() on special days (Leap/Year Day) works correctly")
        void test_plus_onSpecialDays_worksCorrectly(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, dom);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#minusOnSpecialDaysSamples")
        @DisplayName("minus() on special days (Leap/Year Day) works correctly")
        void test_minus_onSpecialDays_worksCorrectly(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, dom);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#withFieldSamples")
        @DisplayName("with(field, value) returns correctly adjusted date")
        void test_with_fieldAndValue_returnsAdjustedDate(int year, int month, int dom, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDom) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, dom);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#withInvalidFieldValues")
        @DisplayName("with(field, value) with invalid value throws exception")
        void test_with_invalidFieldValue_throwsException(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dom).with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#lastDayOfMonthSamples")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) returns correct date")
        void test_with_lastDayOfMonthAdjuster_returnsLastDayOfMonth(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
            InternationalFixedDate base = InternationalFixedDate.of(year, month, day);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Period and Until")
    class PeriodAndUntilTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleFixedAndIsoDates")
        @DisplayName("until(same date) returns zero period")
        void test_until_sameDate_returnsZeroPeriod(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixed.until(fixed));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleFixedAndIsoDates")
        @DisplayName("until(equivalent ISO date) returns zero period")
        void test_until_sameDateAsIso_returnsZeroPeriod(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixed.until(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleFixedAndIsoDates")
        @DisplayName("isoDate.until(equivalent fixed date) returns zero period")
        void test_isoDate_until_sameDateAsFixed_returnsZeroPeriod(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(fixed));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleFixedAndIsoDates")
        @DisplayName("until(otherDate, DAYS) calculates difference correctly")
        void test_until_days_calculatesDifferenceCorrectly(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(0, fixed.until(iso.plusDays(0), DAYS));
            assertEquals(1, fixed.until(iso.plusDays(1), DAYS));
            assertEquals(35, fixed.until(iso.plusDays(35), DAYS));
            if (EARLIEST_DATE_FOR_UNTIL_TEST.isBefore(iso)) {
                assertEquals(-40, fixed.until(iso.minusDays(40), DAYS));
            }
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#untilSamples")
        @DisplayName("until(endDate, unit) calculates amount correctly")
        void test_until_temporalUnit_calculatesDifferenceCorrectly(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#untilPeriodSamples")
        @DisplayName("until(endDate) calculates period correctly")
        void test_until_chronoPeriod_calculatesPeriodCorrectly(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }

        @Test
        @DisplayName("until(endDate, unsupported unit) throws exception")
        void test_until_unsupportedUnit_throwsException() {
            InternationalFixedDate start = InternationalFixedDate.of(2012, 6, 28);
            InternationalFixedDate end = InternationalFixedDate.of(2012, 7, 1);
            assertThrows(UnsupportedTemporalTypeException.class, () -> start.until(end, MINUTES));
        }
    }

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApiTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#invalidEraValues")
        @DisplayName("chronology.eraOf(invalid) throws exception")
        void test_eraOf_withInvalidValue_throwsException(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#invalidProlepticYears")
        @DisplayName("chronology.prolepticYear(invalid) throws exception")
        void test_prolepticYear_withInvalidYearForEra_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }
    }

    @Nested
    @DisplayName("String Representation")
    class ToStringTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#toStringSamples")
        @DisplayName("toString() returns correct format")
        void test_toString_returnsCorrectFormat(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}