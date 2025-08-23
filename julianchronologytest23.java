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
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("JulianDate Tests")
class JulianDateTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample pairs of equivalent Julian and ISO dates.
     * The Julian calendar drifts from the ISO (Gregorian) calendar over time.
     * These samples cover various points in history, including leap years and the period
     * around the Gregorian reform (though Julian is proleptic and doesn't have the gap).
     *
     * @return a stream of arguments: (JulianDate, corresponding LocalDate).
     */
    static Object[][] sampleJulianAndIsoDates() {
        return new Object[][]{
                // Early dates
                {JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
                {JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
                // Julian leap year (4), which is also an ISO leap year
                {JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)},
                {JulianDate.of(4, 3, 2), LocalDate.of(4, 2, 29)}, // ISO leap day
                // Julian leap year (100), which is NOT a Gregorian leap year
                {JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)},
                {JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)},
                // Near Gregorian reform (proleptic Julian does not have the gap)
                {JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
                {JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)},
                // Modern dates
                {JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)},
                {JulianDate.of(2012, 6, 22), LocalDate.of(2012, 7, 5)},
        };
    }

    /**
     * Provides invalid date components for testing factory methods.
     * @return stream of arguments: (year, month, dayOfMonth)
     */
    static Object[][] invalidDateProvider() {
        return new Object[][]{
                {1900, 0, 0}, {1900, -1, 1}, {1900, 0, 1}, {1900, 13, 1}, {1900, 14, 1},
                {1900, 1, -1}, {1900, 1, 0}, {1900, 1, 32},
                {1900, 2, -1}, {1900, 2, 0}, {1900, 2, 30}, // Feb 1900 is a leap year in Julian (29 days)
                {1899, 2, 29}, // Feb 1899 is not a leap year (28 days)
                {1900, 4, 31}, // April has 30 days
                {1900, 6, 31}, // June has 30 days
                {1900, 9, 31}, // September has 30 days
                {1900, 11, 31}, // November has 30 days
        };
    }

    /**
     * Provides data for testing `lengthOfMonth`.
     * @return stream of arguments: (year, month, expectedLength)
     */
    static Object[][] lengthOfMonthProvider() {
        return new Object[][]{
                {1900, 1, 31}, {1900, 2, 29}, {1900, 3, 31}, {1900, 4, 30}, {1900, 5, 31},
                {1900, 6, 30}, {1900, 7, 31}, {1900, 8, 31}, {1900, 9, 30}, {1900, 10, 31},
                {1900, 11, 30}, {1900, 12, 31},
                {1901, 2, 28}, {1903, 2, 28}, // Non-leap years
                {1904, 2, 29}, {2000, 2, 29}, {2100, 2, 29}, // Julian leap years
        };
    }

    /**
     * Provides data for testing `range(TemporalField)`.
     * @return stream of arguments: (year, month, day, field, expectedMin, expectedMax)
     */
    static Object[][] dateRangeProvider() {
        return new Object[][]{
                {2012, 1, 23, DAY_OF_MONTH, 1, 31},
                {2012, 2, 23, DAY_OF_MONTH, 1, 29}, // Leap year
                {2011, 2, 23, DAY_OF_MONTH, 1, 28}, // Non-leap year
                {2012, 4, 23, DAY_OF_MONTH, 1, 30},
                {2012, 1, 23, DAY_OF_YEAR, 1, 366}, // Leap year
                {2011, 2, 23, DAY_OF_YEAR, 1, 365}, // Non-leap year
                {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, 1, 5},
                {2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4},
        };
    }

    /**
     * Provides data for testing `getLong(TemporalField)`.
     * @return stream of arguments: (year, month, day, field, expectedValue)
     */
    static Object[][] dateGetLongProvider() {
        return new Object[][]{
                {2014, 5, 26, DAY_OF_WEEK, 7}, {2014, 5, 26, DAY_OF_MONTH, 26},
                {2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26},
                {2014, 5, 26, MONTH_OF_YEAR, 5},
                {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12L + 5 - 1},
                {2014, 5, 26, YEAR, 2014},
                {2014, 5, 26, ERA, 1},
                {0, 6, 8, ERA, 0},
                {2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7},
        };
    }

    /**
     * Provides data for testing `with(TemporalField, long)`.
     * @return stream of arguments: (year, month, day, field, newValue, expectedYear, expectedMonth, expectedDay)
     */
    static Object[][] dateWithFieldProvider() {
        return new Object[][]{
                {2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22},
                {2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31},
                {2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31},
                {2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                {2014, 5, 26, ERA, 0, -2013, 5, 26},
                // Adjusting month to a shorter month
                {2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28},
                {2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29},
                // Adjusting year on a leap day
                {2012, 2, 29, YEAR, 2011, 2011, 2, 28},
        };
    }

    /**
     * Provides data for testing `plus(long, TemporalUnit)`.
     * @return stream of arguments: (year, month, day, amount, unit, expectedYear, expectedMonth, expectedDay)
     */
    static Object[][] datePlusUnitProvider() {
        return new Object[][]{
                {2014, 5, 26, 0, DAYS, 2014, 5, 26},
                {2014, 5, 26, 8, DAYS, 2014, 6, 3},
                {2014, 5, 26, 3, WEEKS, 2014, 6, 16},
                {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
                {2014, 5, 26, 3, YEARS, 2017, 5, 26},
                {2014, 5, 26, 3, DECADES, 2044, 5, 26},
                {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
                {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
                {2014, 5, 26, 1, ERAS, -2013, 5, 26}, // plus(-1, ERAS) is not supported, so this is plus(1, ERAS) on year -2013
        };
    }

    /**
     * Provides data for testing `minus(long, TemporalUnit)`.
     * @return stream of arguments: (year, month, day, amount, unit, expectedYear, expectedMonth, expectedDay)
     */
    static Object[][] dateMinusUnitProvider() {
        return new Object[][]{
                {2014, 5, 26, 0, DAYS, 2014, 5, 26},
                {2014, 5, 26, 3, DAYS, 2014, 5, 23},
                {2014, 5, 26, 5, WEEKS, 2014, 4, 21},
                {2014, 5, 26, 5, MONTHS, 2013, 12, 26},
                {2014, 5, 26, 5, YEARS, 2009, 5, 26},
                {2014, 5, 26, 5, DECADES, 1964, 5, 26},
                {2014, 5, 26, 5, CENTURIES, 1514, 5, 26},
                {2014, 5, 26, 5, MILLENNIA, 2014 - 5000, 5, 26},
                {-2013, 5, 26, 1, ERAS, 2014, 5, 26},
        };
    }

    /**
     * Provides data for testing `until(Temporal, TemporalUnit)`.
     * @return stream of arguments: (y1, m1, d1, y2, m2, d2, unit, expectedAmount)
     */
    static Object[][] dateUntilProvider() {
        return new Object[][]{
                {2014, 5, 26, 2014, 5, 26, DAYS, 0},
                {2014, 5, 26, 2014, 6, 1, DAYS, 6},
                {2014, 5, 26, 2014, 6, 2, WEEKS, 1},
                {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
                {2014, 5, 26, 2015, 5, 26, YEARS, 1},
                {2014, 5, 26, 2024, 5, 26, DECADES, 1},
                {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
                {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
                {-2013, 5, 26, 2014, 5, 26, ERAS, 1},
        };
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Conversion and Interoperability Tests")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("from(JulianDate) should return correct LocalDate")
        void fromJulianDate_shouldReturnCorrectLocalDate(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("from(LocalDate) should return correct JulianDate")
        void fromLocalDate_shouldReturnCorrectJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("chronology.date(Temporal) should return correct JulianDate")
        void chronologyDateFromTemporal_shouldReturnCorrectJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.date(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("toEpochDay should match equivalent LocalDate")
        void toEpochDay_shouldMatchLocalDate(JulianDate julian, LocalDate iso) {
            assertEquals(iso.toEpochDay(), julian.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("chronology.dateEpochDay should create correct JulianDate")
        void chronologyDateFromEpochDay_shouldCreateCorrectJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("plus(days) should be consistent with LocalDate")
        void plusDays_shouldBeConsistentWithLocalDate(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian.plus(0, DAYS)));
            assertEquals(iso.plusDays(1), LocalDate.from(julian.plus(1, DAYS)));
            assertEquals(iso.plusDays(35), LocalDate.from(julian.plus(35, DAYS)));
            assertEquals(iso.plusDays(-1), LocalDate.from(julian.plus(-1, DAYS)));
            assertEquals(iso.plusDays(-60), LocalDate.from(julian.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("minus(days) should be consistent with LocalDate")
        void minusDays_shouldBeConsistentWithLocalDate(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian.minus(0, DAYS)));
            assertEquals(iso.minusDays(1), LocalDate.from(julian.minus(1, DAYS)));
            assertEquals(iso.minusDays(35), LocalDate.from(julian.minus(35, DAYS)));
            assertEquals(iso.minusDays(-1), LocalDate.from(julian.minus(-1, DAYS)));
            assertEquals(iso.minusDays(-60), LocalDate.from(julian.minus(-60, DAYS)));
        }
    }

    @Nested
    @DisplayName("Factory and Property Tests")
    class FactoryAndPropertyTests {

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#invalidDateProvider")
        @DisplayName("of() with invalid date should throw exception")
        void of_withInvalidDate_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dayOfMonth));
        }

        @ParameterizedTest(name = "{0}-{1} should have {2} days")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#lengthOfMonthProvider")
        @DisplayName("lengthOfMonth should return correct number of days")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }

        @ParameterizedTest(name = "range({3}) for {0}-{1}-{2} is {4}-{5}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#dateRangeProvider")
        @DisplayName("range() should return correct value range for a field")
        void range_forField_shouldReturnCorrectValueRange(int year, int month, int day, TemporalField field, int expectedMin, int expectedMax) {
            JulianDate date = JulianDate.of(year, month, day);
            assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
        }

        @ParameterizedTest(name = "getLong({3}) for {0}-{1}-{2} is {4}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#dateGetLongProvider")
        @DisplayName("getLong() should return correct value for a field")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, day).getLong(field));
        }
    }

    @Nested
    @DisplayName("Modification Tests")
    class ModificationTests {

        @ParameterizedTest(name = "({0}-{1}-{2}).with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#dateWithFieldProvider")
        @DisplayName("with() should return a correctly modified date")
        void with_shouldReturnCorrectlyModifiedDate(int year, int month, int day, TemporalField field, long value, int exYear, int exMonth, int exDay) {
            JulianDate initial = JulianDate.of(year, month, day);
            JulianDate expected = JulianDate.of(exYear, exMonth, exDay);
            assertEquals(expected, initial.with(field, value));
        }

        @ParameterizedTest(name = "({0}-{1}-{2}).plus({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#datePlusUnitProvider")
        @DisplayName("plus() should return a correctly modified date")
        void plus_shouldReturnCorrectlyModifiedDate(int year, int month, int day, long amount, TemporalUnit unit, int exYear, int exMonth, int exDay) {
            JulianDate initial = JulianDate.of(year, month, day);
            JulianDate expected = JulianDate.of(exYear, exMonth, exDay);
            assertEquals(expected, initial.plus(amount, unit));
        }

        @ParameterizedTest(name = "({0}-{1}-{2}).minus({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#dateMinusUnitProvider")
        @DisplayName("minus() should return a correctly modified date")
        void minus_shouldReturnCorrectlyModifiedDate(int year, int month, int day, long amount, TemporalUnit unit, int exYear, int exMonth, int exDay) {
            JulianDate initial = JulianDate.of(year, month, day);
            JulianDate expected = JulianDate.of(exYear, exMonth, exDay);
            assertEquals(expected, initial.minus(amount, unit));
        }

        @Test
        @DisplayName("plus() with unsupported unit should throw exception")
        void plus_withUnsupportedUnit_shouldThrowException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> JulianDate.of(2012, 6, 30).plus(1, MINUTES));
        }
    }

    @Nested
    @DisplayName("Until Tests")
    class UntilTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("until(same date) should return zero period")
        void until_sameDate_shouldReturnZeroPeriod(JulianDate julian, LocalDate iso) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(julian));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("until(equivalent ISO date) should return zero period")
        void until_equivalentIsoDate_shouldReturnZeroPeriod(JulianDate julian, LocalDate iso) {
            // This confirms that the distance between a Julian date and its equivalent ISO date is zero
            // when calculated within the Julian chronology system.
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("LocalDate.until(equivalent Julian date) should return zero period")
        void localDateUntil_equivalentJulianDate_shouldReturnZeroPeriod(JulianDate julian, LocalDate iso) {
            // This confirms that the distance is zero when calculated by the ISO chronology.
            assertEquals(Period.ZERO, iso.until(julian));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("until(other date, DAYS) should calculate correct day difference")
        void until_withDaysUnit_shouldCalculateCorrectDayDifference(JulianDate julian, LocalDate iso) {
            assertEquals(0, julian.until(iso.plusDays(0), DAYS));
            assertEquals(1, julian.until(iso.plusDays(1), DAYS));
            assertEquals(35, julian.until(iso.plusDays(35), DAYS));
            assertEquals(-40, julian.until(iso.minusDays(40), DAYS));
        }

        @ParameterizedTest(name = "until({6}) from {0}-{1}-{2} to {3}-{4}-{5} is {7}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#dateUntilProvider")
        @DisplayName("until() should calculate correct amount for a unit")
        void until_forUnit_shouldCalculateCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate end = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {
        @Test
        void toString_shouldReturnCorrectFormat() {
            assertEquals("Julian AD 1-01-01", JulianDate.of(1, 1, 1).toString());
            assertEquals("Julian AD 2012-06-23", JulianDate.of(2012, 6, 23).toString());
            assertEquals("Julian BC 1-01-01", JulianDate.of(0, 1, 1).toString());
        }
    }
}