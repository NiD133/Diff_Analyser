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
 * Tests for {@link InternationalFixedDate}.
 */
@DisplayName("InternationalFixedDate")
public class InternationalFixedDateTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample pairs of InternationalFixedDate and their equivalent ISO LocalDate.
     * Covers regular dates, leap years, special days (Leap Day, Year Day), and historical dates.
     */
    static Object[][] sampleDatePairsProvider() {
        return new Object[][] {
            {InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {InternationalFixedDate.of(1, 1, 2), LocalDate.of(1, 1, 2)},
            {InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)},
            {InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)},
            {InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)},
            {InternationalFixedDate.of(1, 7, 2), LocalDate.of(1, 6, 19)},
            {InternationalFixedDate.of(1, 13, 28), LocalDate.of(1, 12, 30)},
            {InternationalFixedDate.of(1, 13, 27), LocalDate.of(1, 12, 29)},
            {InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)},
            {InternationalFixedDate.of(2, 1, 1), LocalDate.of(2, 1, 1)},
            {InternationalFixedDate.of(4, 6, 27), LocalDate.of(4, 6, 15)},
            {InternationalFixedDate.of(4, 6, 28), LocalDate.of(4, 6, 16)},
            {InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)}, // Leap Day
            {InternationalFixedDate.of(4, 7, 1), LocalDate.of(4, 6, 18)},
            {InternationalFixedDate.of(4, 7, 2), LocalDate.of(4, 6, 19)},
            {InternationalFixedDate.of(4, 13, 28), LocalDate.of(4, 12, 30)},
            {InternationalFixedDate.of(4, 13, 27), LocalDate.of(4, 12, 29)},
            {InternationalFixedDate.of(4, 13, 29), LocalDate.of(4, 12, 31)}, // Year Day
            {InternationalFixedDate.of(5, 1, 1), LocalDate.of(5, 1, 1)},
            {InternationalFixedDate.of(100, 6, 28), LocalDate.of(100, 6, 17)}, // Non-leap century
            {InternationalFixedDate.of(400, 6, 29), LocalDate.of(400, 6, 17)}, // Leap century
            {InternationalFixedDate.of(1945, 10, 27), LocalDate.of(1945, 10, 6)},
            {InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)},
            {InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4)},
        };
    }

    static Object[][] invalidDateProvider() {
        return new Object[][] {
            // Invalid year
            {-1, 13, 28}, {0, 1, 1},
            // Invalid month
            {1900, -2, 1}, {1900, 14, 1}, {1900, 15, 1},
            // Invalid day of month
            {1900, 1, -1}, {1900, 1, 0}, {1900, 1, 29}, // Month 1 has 28 days
            {1900, 2, 29}, {1900, 12, 29},
            {1900, 13, 30}, // Month 13 has 29 days
        };
    }

    static Object[][] nonLeapYearsProvider() {
        return new Object[][] {{1}, {100}, {200}, {300}, {1900}};
    }

    static Object[][] monthLengthsProvider() {
        return new Object[][] {
            {1900, 1, 28, 28},
            {1900, 6, 28, 28}, // Month 6 in non-leap year
            {1900, 13, 29, 29}, // Month 13 (Year Day)
            {1904, 6, 29, 29}, // Month 6 in leap year (Leap Day)
        };
    }

    static Object[][] invalidEraValuesProvider() {
        return new Object[][] {{-1}, {0}, {2}};
    }

    static Object[][] invalidProlepticYearProvider() {
        return new Object[][] {{-10}, {-1}, {0}};
    }

    static Object[][] fieldRangeProvider() {
        return new Object[][] {
            // --- Leap year (2012) ---
            // DAY_OF_MONTH
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 29)}, // Leap month
            {2012, 6, 29, DAY_OF_MONTH, ValueRange.of(1, 29)}, // Leap Day
            {2012, 13, 23, DAY_OF_MONTH, ValueRange.of(1, 29)}, // Year Day month
            {2012, 13, 29, DAY_OF_MONTH, ValueRange.of(1, 29)}, // Year Day
            // DAY_OF_YEAR
            {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366)},
            // MONTH_OF_YEAR
            {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)},
            // ALIGNED_DAY_OF_WEEK_IN_MONTH (0 for special days, 1-7 otherwise)
            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
            {2012, 6, 29, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(0, 0)},
            {2012, 13, 29, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(0, 0)},
            // ALIGNED_WEEK_OF_MONTH (0 for special days, 1-4 otherwise)
            {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
            {2012, 6, 29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)},
            {2012, 13, 29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)},
            // --- Non-leap year (2011) ---
            {2011, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2011, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 365)},
        };
    }

    static Object[][] fieldValueProvider() {
        return new Object[][] {
            // --- Date: 2014-05-26 (non-leap) ---
            {2014, 5, 26, DAY_OF_WEEK, 5L},
            {2014, 5, 26, DAY_OF_MONTH, 26L},
            {2014, 5, 26, DAY_OF_YEAR, 138L /* (5-1)*28 + 26 */},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 20L},
            {2014, 5, 26, MONTH_OF_YEAR, 5L},
            {2014, 5, 26, PROLEPTIC_MONTH, 26186L /* 2014*13 + 5 - 1 */},
            {2014, 5, 26, YEAR, 2014L},
            {2014, 5, 26, ERA, 1L},

            // --- Date: 2012-09-26 (leap) ---
            {2012, 9, 26, DAY_OF_WEEK, 5L},
            {2012, 9, 26, DAY_OF_YEAR, 251L /* (9-1)*28 + 26 + 1 (leap day) */},
            {2012, 9, 26, ALIGNED_WEEK_OF_YEAR, 36L},

            // --- Date: 2014-13-29 (Year Day, non-leap) ---
            {2014, 13, 29, DAY_OF_WEEK, 0L},
            {2014, 13, 29, DAY_OF_MONTH, 29L},
            {2014, 13, 29, DAY_OF_YEAR, 365L /* 13*28 + 1 */},
            {2014, 13, 29, ALIGNED_WEEK_OF_YEAR, 0L},

            // --- Date: 2012-06-29 (Leap Day) ---
            {2012, 6, 29, DAY_OF_WEEK, 0L},
            {2012, 6, 29, DAY_OF_MONTH, 29L},
            {2012, 6, 29, DAY_OF_YEAR, 169L /* 6*28 + 1 */},
            {2012, 6, 29, ALIGNED_WEEK_OF_YEAR, 0L},

            // --- Date: 2012-13-29 (Year Day, leap) ---
            {2012, 13, 29, DAY_OF_YEAR, 366L /* 13*28 + 1 (leap) + 1 (year day) */},
        };
    }

    static Object[][] withFieldValueProvider() {
        return new Object[][] {
            // Field, Value, Expected Date (Y, M, D)
            {2014, 5, 26, DAY_OF_WEEK, 1L, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_MONTH, 28L, 2014, 5, 28},
            {2014, 5, 26, DAY_OF_YEAR, 364L, 2014, 13, 28},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1L, 2014, 5, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23L, 2014, 6, 19},
            {2014, 5, 26, MONTH_OF_YEAR, 4L, 2014, 4, 26},
            {2014, 5, 26, PROLEPTIC_MONTH, 26164L /* 2013*13+3-1 */, 2013, 3, 26},
            {2014, 5, 26, YEAR, 2012L, 2012, 5, 26},
            {2014, 5, 26, YEAR_OF_ERA, 2012L, 2012, 5, 26},
            {2014, 5, 26, ERA, 1L, 2014, 5, 26},
            // Adjust day of month for month with different length
            {2012, 6, 29, YEAR, 2013L, 2013, 6, 28},
        };
    }

    static Object[][] withInvalidFieldValueProvider() {
        return new Object[][] {
            // Field, Value
            {2013, 1, 1, DAY_OF_WEEK, 0L},
            {2013, 1, 1, DAY_OF_WEEK, 8L},
            {2013, 1, 1, DAY_OF_MONTH, 0L},
            {2013, 1, 1, DAY_OF_MONTH, 29L}, // Not a leap month
            {2012, 6, 1, DAY_OF_MONTH, 30L}, // Leap month has only 29 days
            {2013, 1, 1, DAY_OF_YEAR, 0L},
            {2013, 1, 1, DAY_OF_YEAR, 366L}, // Not a leap year
            {2012, 1, 1, DAY_OF_YEAR, 367L}, // Leap year has 366 days
            {2013, 1, 1, MONTH_OF_YEAR, 0L},
            {2013, 1, 1, MONTH_OF_YEAR, 14L},
            {2013, 1, 1, YEAR, 0L},
        };
    }

    static Object[][] lastDayOfMonthAdjusterProvider() {
        return new Object[][] {
            {2012, 6, 23, 2012, 6, 29},
            {2012, 6, 29, 2012, 6, 29},
            {2009, 6, 23, 2009, 6, 28},
            {2007, 13, 23, 2007, 13, 29},
            {2005, 13, 29, 2005, 13, 29},
        };
    }

    static Object[][] plusProvider() {
        return new Object[][] {
            // Start Date (Y, M, D), Amount, Unit, Expected Date (Y, M, D)
            {2014, 5, 26, 0L, DAYS, 2014, 5, 26},
            {2014, 5, 26, 8L, DAYS, 2014, 6, 6},
            {2014, 5, 26, -3L, DAYS, 2014, 5, 23},
            {2014, 5, 26, 3L, WEEKS, 2014, 6, 19},
            {2014, 5, 26, 3L, MONTHS, 2014, 8, 26},
            {2014, 5, 26, 3L, YEARS, 2017, 5, 26},
            {2014, 5, 26, 3L, DECADES, 2044, 5, 26},
            {2014, 5, 26, 3L, CENTURIES, 2314, 5, 26},
            {2014, 5, 26, 3L, MILLENNIA, 5014, 5, 26},
        };
    }

    static Object[][] plusSpecialDayProvider() {
        return new Object[][] {
            // Start Date (Y, M, D), Amount, Unit, Expected Date (Y, M, D)
            // --- Year Day ---
            {2014, 13, 29, 8L, DAYS, 2015, 1, 8},
            {2014, 13, 29, 3L, WEEKS, 2015, 1, 21},
            {2014, 13, 29, 3L, MONTHS, 2015, 3, 28},
            {2014, 13, 29, 3L, YEARS, 2017, 13, 29},
            // --- Leap Day ---
            {2012, 6, 29, 8L, DAYS, 2012, 7, 8},
            {2012, 6, 29, 3L, WEEKS, 2012, 7, 22},
            {2012, 6, 29, 3L, MONTHS, 2012, 9, 28},
            {2012, 6, 29, 3L, YEARS, 2015, 6, 28}, // Becomes non-leap
            {2012, 6, 29, 4L, YEARS, 2016, 6, 29}, // Stays leap
        };
    }

    static Object[][] untilUnitProvider() {
        return new Object[][] {
            // Start (Y,M,D), End (Y,M,D), Unit, Expected Amount
            {2014, 5, 26, 2014, 5, 26, DAYS, 0L},
            {2014, 5, 26, 2014, 6, 4, DAYS, 6L},
            {2014, 5, 26, 2014, 5, 20, DAYS, -6L},
            {2014, 5, 26, 2014, 6, 5, WEEKS, 1L},
            {2014, 5, 26, 2014, 6, 26, MONTHS, 1L},
            {2014, 5, 26, 2015, 5, 26, YEARS, 1L},
            {2014, 5, 26, 2024, 5, 26, DECADES, 1L},
            {2014, 5, 26, 2114, 5, 26, CENTURIES, 1L},
            {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L},
            {2014, 5, 26, 3014, 5, 26, ERAS, 0L},
            // Spanning special days
            {2012, 6, 28, 2012, 7, 1, DAYS, 2L}, // Across Leap Day
            {2014, 13, 28, 2015, 1, 1, DAYS, 2L}, // Across Year Day
            {2012, 6, 29, 2012, 13, 29, MONTHS, 7L}, // From Leap Day to Year Day
        };
    }

    static Object[][] untilPeriodProvider() {
        return new Object[][] {
            // Start (Y,M,D), End (Y,M,D), Expected Period (Y, M, D)
            {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
            {2014, 5, 26, 2014, 6, 4, 0, 0, 6},
            {2014, 5, 26, 2014, 5, 20, 0, 0, -6},
            {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
            {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
            // Spanning special days
            {2003, 13, 29, 2004, 6, 29, 0, 6, 0}, // From Year Day to Leap Day
            {2004, 6, 29, 2004, 13, 29, 0, 7, 0}, // From Leap Day to Year Day
        };
    }

    static Object[][] toStringProvider() {
        return new Object[][] {
            {InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"},
            {InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"},
            {InternationalFixedDate.of(1, 13, 29), "Ifc CE 1/13/29"},
            {InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"},
            {InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29"},
        };
    }

    //-----------------------------------------------------------------------
    // Test Cases
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory and consistency tests")
    class FactoryAndConsistencyTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleDatePairsProvider")
        @DisplayName("test consistency with ISO date")
        void test_isoConsistency(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(ifcDate));
            assertEquals(ifcDate, InternationalFixedDate.from(isoDate));
            assertEquals(isoDate.toEpochDay(), ifcDate.toEpochDay());
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#invalidDateProvider")
        @DisplayName("of() throws for invalid date parts")
        void test_of_throwsForInvalidDateParts(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dayOfMonth));
        }

        @ParameterizedTest(name = "of({0}, 6, 29)")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#nonLeapYearsProvider")
        @DisplayName("of() throws for leap day in a non-leap year")
        void test_of_throwsForLeapDayInNonLeapYear(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }
    }

    @Nested
    @DisplayName("Field access")
    class FieldAccessTests {
        @ParameterizedTest(name = "{0}-{1}-{2}, lengthOfMonth = {3}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#monthLengthsProvider")
        @DisplayName("lengthOfMonth()")
        void test_lengthOfMonth(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, day).lengthOfMonth());
        }

        @ParameterizedTest(name = "{0}-{1}-{2}, range({3}) = {4}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#fieldRangeProvider")
        @DisplayName("range() for field")
        void test_range_forField(int year, int month, int day, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, InternationalFixedDate.of(year, month, day).range(field));
        }

        @ParameterizedTest(name = "{0}-{1}-{2}, getLong({3}) = {4}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#fieldValueProvider")
        @DisplayName("getLong() for field")
        void test_getLong_forField(int year, int month, int day, TemporalField field, long expectedValue) {
            assertEquals(expectedValue, InternationalFixedDate.of(year, month, day).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date modification")
    class ModificationTests {
        @ParameterizedTest(name = "with({3}, {4})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#withFieldValueProvider")
        @DisplayName("with(field, value)")
        void test_with_fieldAndValue(int year, int month, int day, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDay) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, day);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.with(field, value));
        }

        @ParameterizedTest(name = "with({3}, {4}) throws")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#withInvalidFieldValueProvider")
        @DisplayName("with() throws for invalid field value")
        void test_with_throwsForInvalidFieldValue(int year, int month, int day, TemporalField field, long value) {
            InternationalFixedDate date = InternationalFixedDate.of(year, month, day);
            assertThrows(DateTimeException.class, () -> date.with(field, value));
        }

        @ParameterizedTest(name = "{0}-{1}-{2} -> {3}-{4}-{5}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#lastDayOfMonthAdjusterProvider")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth())")
        void test_with_lastDayOfMonthAdjuster(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
            InternationalFixedDate base = InternationalFixedDate.of(year, month, day);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Date arithmetic")
    class ArithmeticTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#plusProvider")
        @DisplayName("plus(amount, unit)")
        void test_plus_amountWithUnit(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            InternationalFixedDate start = InternationalFixedDate.of(startYear, startMonth, startDay);
            InternationalFixedDate expected = InternationalFixedDate.of(endYear, endMonth, endDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#plusProvider")
        @DisplayName("minus(amount, unit)")
        void test_minus_amountWithUnit(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            InternationalFixedDate end = InternationalFixedDate.of(endYear, endMonth, endDay);
            InternationalFixedDate expected = InternationalFixedDate.of(startYear, startMonth, startDay);
            assertEquals(expected, end.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#plusSpecialDayProvider")
        @DisplayName("plus(amount, unit) for special days")
        void test_plus_amountWithUnit_forSpecialDays(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            InternationalFixedDate start = InternationalFixedDate.of(startYear, startMonth, startDay);
            InternationalFixedDate expected = InternationalFixedDate.of(endYear, endMonth, endDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#untilUnitProvider")
        @DisplayName("until(endDate, unit)")
        void test_until_withUnit(int year1, int month1, int day1, int year2, int month2, int day2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(year1, month1, day1);
            InternationalFixedDate end = InternationalFixedDate.of(year2, month2, day2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#untilPeriodProvider")
        @DisplayName("until(endDate) returns ChronoPeriod")
        void test_until_returnsChronoPeriod(int year1, int month1, int day1, int year2, int month2, int day2, int expectedY, int expectedM, int expectedD) {
            InternationalFixedDate start = InternationalFixedDate.of(year1, month1, day1);
            InternationalFixedDate end = InternationalFixedDate.of(year2, month2, day2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(expectedY, expectedM, expectedD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology-specific methods")
    class ChronologyTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#invalidEraValuesProvider")
        @DisplayName("eraOf() throws for invalid value")
        void test_eraOf_throwsForInvalidValue(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#invalidProlepticYearProvider")
        @DisplayName("prolepticYear() throws for invalid value")
        void test_prolepticYear_throwsForInvalidValue(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }
    }

    @Nested
    @DisplayName("General method behavior")
    class GeneralMethodTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleDatePairsProvider")
        @DisplayName("until(self) is zero")
        void test_until_self_isZero(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(ifcDate));
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(ifcDate));
        }

        @ParameterizedTest(name = "{1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#toStringProvider")
        @DisplayName("toString() returns correct format")
        void test_toString_returnsFormattedDate(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }

        @Test
        @DisplayName("getLong() throws for unsupported field")
        void test_getLong_throwsForUnsupportedField() {
            InternationalFixedDate date = InternationalFixedDate.of(2012, 6, 28);
            assertThrows(UnsupportedTemporalTypeException.class, () -> date.getLong(MINUTE_OF_DAY));
        }
    }
}