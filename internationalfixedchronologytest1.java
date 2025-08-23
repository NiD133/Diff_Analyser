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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link InternationalFixedChronology} and {@link InternationalFixedDate}.
 */
class InternationalFixedChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample pairs of InternationalFixedDate and their equivalent ISO LocalDate.
     */
    private static Object[][] sampleFixedAndIsoDatesProvider() {
        return new Object[][]{
            {InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {InternationalFixedDate.of(1, 1, 2), LocalDate.of(1, 1, 2)},
            {InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)},
            {InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)},
            {InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)},
            {InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)},
            {InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4)},
        };
    }

    /**
     * Provides invalid date components (year, month, dayOfMonth).
     */
    private static Object[][] invalidDateProvider() {
        return new Object[][]{
            {-1, 13, 28}, {0, 1, 1},
            {1900, -2, 1}, {1900, 14, 1}, {1900, 15, 1},
            {1900, 1, -1}, {1900, 1, 0}, {1900, 1, 29},
            {1900, 2, 29}, {1900, 13, 30},
        };
    }

    /**
     * Provides years that are not leap years.
     */
    private static Object[][] nonLeapYearsProvider() {
        return new Object[][]{{1}, {100}, {200}, {300}, {1900}};
    }

    /**
     * Provides test cases for {@code lengthOfMonth()}.
     * Columns: year, month, dayOfMonth, expected length.
     */
    private static Object[][] lengthOfMonthProvider() {
        return new Object[][]{
            {1900, 1, 28, 28},
            {1900, 13, 29, 29},
            {1904, 6, 29, 29},
        };
    }

    /**
     * Provides invalid era values.
     */
    private static Object[][] invalidEraValuesProvider() {
        return new Object[][]{{-1}, {0}, {2}};
    }

    /**
     * Provides invalid proleptic years for the CE era.
     */
    private static Object[][] invalidProlepticYearProvider() {
        return new Object[][]{{-10}, {-1}, {0}};
    }

    /**
     * Provides test cases for {@code range(TemporalField)}.
     * Columns: year, month, dayOfMonth, field, expected range.
     */
    private static Object[][] fieldRangeProvider() {
        return new Object[][]{
            {2012, 6, 29, DAY_OF_MONTH, ValueRange.of(1, 29)},
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366)},
            {2011, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 365)},
            {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)},
            {2012, 6, 29, DAY_OF_WEEK, ValueRange.of(0, 0)},
            {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
        };
    }

    /**
     * Provides test cases for {@code getLong(TemporalField)}.
     * Columns: year, month, dayOfMonth, field, expected value.
     */
    private static Object[][] fieldValueProvider() {
        return new Object[][]{
            {2014, 5, 26, DAY_OF_WEEK, 5},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, 28 * 4 + 26},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},
            {2012, 6, 29, DAY_OF_WEEK, 0},
            {2012, 6, 29, DAY_OF_MONTH, 29},
            {2012, 6, 29, DAY_OF_YEAR, 6 * 28 + 1},
        };
    }

    /**
     * Provides test cases for {@code with(TemporalField, long)}.
     * Columns: year, month, dayOfMonth (start), field, value, year, month, dayOfMonth (expected).
     */
    private static Object[][] withFieldAndValueProvider() {
        return new Object[][]{
            {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
            {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 13, 28},
            {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            {2012, 6, 29, YEAR, 2013, 2013, 6, 28}, // Adjusts leap day
        };
    }

    /**
     * Provides test cases for {@code with(TemporalField, long)} that should throw an exception.
     * Columns: year, month, dayOfMonth, field, invalid value.
     */
    private static Object[][] withFieldAndInvalidValueProvider() {
        return new Object[][]{
            {2013, 1, 1, DAY_OF_WEEK, 0}, {2013, 1, 1, DAY_OF_WEEK, 8},
            {2013, 1, 1, DAY_OF_MONTH, 29}, {2013, 6, 1, DAY_OF_MONTH, 29},
            {2012, 6, 1, DAY_OF_MONTH, 30},
            {2013, 1, 1, DAY_OF_YEAR, 366}, {2012, 1, 1, DAY_OF_YEAR, 367},
            {2013, 1, 1, MONTH_OF_YEAR, 14},
            {2013, 1, 1, YEAR, 0},
        };
    }

    /**
     * Provides test cases for {@code plus(long, TemporalUnit)}.
     * Columns: year, month, dayOfMonth (start), amount, unit, year, month, dayOfMonth (expected).
     */
    private static Object[][] plusProvider() {
        return new Object[][]{
            {2014, 5, 26, 0, DAYS, 2014, 5, 26},
            {2014, 5, 26, 8, DAYS, 2014, 6, 6},
            {2014, 5, 26, 3, WEEKS, 2014, 6, 19},
            {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
            {2014, 5, 26, 3, YEARS, 2017, 5, 26},
            {2014, 5, 26, 3, DECADES, 2044, 5, 26},
            {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
            {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
        };
    }

    /**
     * Provides test cases for {@code plus(long, TemporalUnit)} with leap and year days.
     * Columns: year, month, dayOfMonth (start), amount, unit, year, month, dayOfMonth (expected).
     */
    private static Object[][] plusOnSpecialDaysProvider() {
        return new Object[][]{
            {2014, 13, 29, 8, DAYS, 2015, 1, 8},      // Year Day
            {2014, 13, 29, 3, WEEKS, 2015, 1, 21},
            {2014, 13, 29, 3, MONTHS, 2015, 3, 28},   // Adjusts to last day of month
            {2014, 13, 29, 3, YEARS, 2017, 13, 29},
            {2012, 6, 29, 8, DAYS, 2012, 7, 8},       // Leap Day
            {2012, 6, 29, 3, WEEKS, 2012, 7, 22},
            {2012, 6, 29, 3, MONTHS, 2012, 9, 28},    // Adjusts to last day of month
            {2012, 6, 29, 3, YEARS, 2015, 6, 28},     // Adjusts leap day
            {2012, 6, 29, 4, YEARS, 2016, 6, 29},
        };
    }

    /**
     * Provides test cases for {@code until(ChronoLocalDate, TemporalUnit)}.
     * Columns: y1, m1, d1 (start), y2, m2, d2 (end), unit, expected amount.
     */
    private static Object[][] untilProvider() {
        return new Object[][]{
            {2014, 5, 26, 2014, 5, 26, DAYS, 0},
            {2014, 5, 26, 2014, 6, 4, DAYS, 6},
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

    /**
     * Provides test cases for {@code until(ChronoLocalDate)}.
     * Columns: y1, m1, d1 (start), y2, m2, d2 (end), expected years, months, days.
     */
    private static Object[][] untilPeriodProvider() {
        return new Object[][]{
            {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
            {2014, 5, 26, 2014, 6, 4, 0, 0, 6},
            {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
            {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
            {2003, 13, 29, 2004, 6, 29, 0, 6, 0}, // Year Day to Leap Day
            {2004, 6, 29, 2004, 13, 29, 0, 7, 0}, // Leap Day to Year Day
        };
    }

    /**
     * Provides test cases for {@code toString()}.
     */
    private static Object[][] toStringProvider() {
        return new Object[][]{
            {InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"},
            {InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"},
            {InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"}, // Leap Day
            {InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29"},// Year Day
        };
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    class CreationAndConversionTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDatesProvider")
        void fromIsoDate_shouldReturnCorrespondingFixedDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDatesProvider")
        void toIsoDate_shouldReturnCorrespondingIsoDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(fixedDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDatesProvider")
        void toEpochDay_shouldMatchIsoDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), fixedDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDatesProvider")
        void dateFromEpochDay_shouldCreateCorrectDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDatesProvider")
        void dateFromTemporal_shouldCreateCorrectDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#invalidDateProvider")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dayOfMonth));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#nonLeapYearsProvider")
        void of_withLeapDayInNonLeapYear_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }
    }

    @Nested
    class DatePropertiesTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#lengthOfMonthProvider")
        void lengthOfMonth_shouldBeCorrect(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, day).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#fieldRangeProvider")
        void range_forField_shouldBeCorrect(int year, int month, int dayOfMonth, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, InternationalFixedDate.of(year, month, dayOfMonth).range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#fieldValueProvider")
        void getLong_forField_shouldBeCorrect(int year, int month, int dayOfMonth, TemporalField field, long expectedValue) {
            assertEquals(expectedValue, InternationalFixedDate.of(year, month, dayOfMonth).getLong(field));
        }
    }

    @Nested
    class ManipulationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#withFieldAndValueProvider")
        void with_usingField_shouldReturnCorrectDate(int year, int month, int dom, TemporalField field, long value, int exYear, int exMonth, int exDom) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, dom);
            InternationalFixedDate expected = InternationalFixedDate.of(exYear, exMonth, exDom);
            assertEquals(expected, start.with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#withFieldAndInvalidValueProvider")
        void with_usingFieldAndInvalidValue_shouldThrowException(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dom).with(field, value));
        }

        @Test
        void with_lastDayOfMonthAdjuster_shouldReturnCorrectDate() {
            InternationalFixedDate date = InternationalFixedDate.of(2012, 6, 23);
            InternationalFixedDate expected = InternationalFixedDate.of(2012, 6, 29);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#plusProvider")
        void plus_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#plusProvider")
        void minus_shouldReturnCorrectDate(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#plusOnSpecialDaysProvider")
        void plus_onSpecialDays_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }
    }

    @Nested
    class PeriodAndDifferenceTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDatesProvider")
        void until_self_shouldReturnZeroPeriod(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(fixedDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDatesProvider")
        void until_equivalentIsoDate_shouldReturnZeroPeriod(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDatesProvider")
        void isoDate_until_equivalentFixedDate_shouldReturnZeroPeriod(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(fixedDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#untilProvider")
        void until_withUnit_shouldCalculateCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#untilPeriodProvider")
        void until_shouldCalculateCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(ey, em, ed);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    class ChronologyApiTests {
        @Test
        void chronology_singleton_isCorrectlyLoaded() {
            Chronology chrono = Chronology.of("Ifc");
            assertNotNull(chrono);
            assertEquals(InternationalFixedChronology.INSTANCE, chrono);
            assertEquals("Ifc", chrono.getId());
            assertEquals(null, chrono.getCalendarType());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#invalidEraValuesProvider")
        void eraOf_withInvalidValue_shouldThrowException(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#invalidProlepticYearProvider")
        void prolepticYear_withInvalidYearOfEra_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }
    }

    @Nested
    class GeneralObjectMethodTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#toStringProvider")
        void toString_shouldReturnCorrectFormat(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}