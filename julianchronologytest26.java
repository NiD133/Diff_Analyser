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
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link JulianDate} class.
 */
@DisplayName("JulianDate")
class JulianDateTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides pairs of equivalent Julian and ISO dates.
     * The data includes:
     * - The start of the Julian calendar (1-1-1).
     * - Dates around Julian leap years (year 4, 100).
     * - The date of the Gregorian calendar reform (1582-10-04 Julian is 1582-10-14 Gregorian).
     * - Modern dates.
     */
    private static Stream<Arguments> sampleJulianAndIsoDates() {
        return Stream.of(
            Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(JulianDate.of(1, 2, 28), LocalDate.of(1, 2, 26)),
            Arguments.of(JulianDate.of(1, 3, 1), LocalDate.of(1, 2, 27)),
            Arguments.of(JulianDate.of(1, 3, 2), LocalDate.of(1, 2, 28)),
            Arguments.of(JulianDate.of(1, 3, 3), LocalDate.of(1, 3, 1)),
            Arguments.of(JulianDate.of(4, 2, 28), LocalDate.of(4, 2, 26)),
            Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)), // Julian leap year
            Arguments.of(JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)),
            Arguments.of(JulianDate.of(4, 3, 2), LocalDate.of(4, 2, 29)),
            Arguments.of(JulianDate.of(4, 3, 3), LocalDate.of(4, 3, 1)),
            Arguments.of(JulianDate.of(100, 2, 28), LocalDate.of(100, 2, 26)),
            Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)), // Julian leap year, but not Gregorian
            Arguments.of(JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
            Arguments.of(JulianDate.of(100, 3, 2), LocalDate.of(100, 3, 1)),
            Arguments.of(JulianDate.of(100, 3, 3), LocalDate.of(100, 3, 2)),
            Arguments.of(JulianDate.of(0, 12, 31), LocalDate.of(0, 12, 29)),
            Arguments.of(JulianDate.of(0, 12, 30), LocalDate.of(0, 12, 28)),
            Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)), // Day before Gregorian calendar cutover
            Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)), // Day of Gregorian calendar cutover
            Arguments.of(JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)),
            Arguments.of(JulianDate.of(2012, 6, 22), LocalDate.of(2012, 7, 5)),
            Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion to and from other date types")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void convertsToLocalDateCorrectly(JulianDate julianDate, LocalDate expectedIsoDate) {
            assertEquals(expectedIsoDate, LocalDate.from(julianDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void createsFromLocalDateCorrectly(JulianDate expectedJulianDate, LocalDate isoDate) {
            assertEquals(expectedJulianDate, JulianDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void createsFromEpochDayCorrectly(JulianDate expectedJulianDate, LocalDate isoDate) {
            assertEquals(expectedJulianDate, JulianChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void convertsToEpochDayCorrectly(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), julianDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void createsFromTemporalAccessorCorrectly(JulianDate expectedJulianDate, LocalDate isoDate) {
            assertEquals(expectedJulianDate, JulianChronology.INSTANCE.date(isoDate));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Calculating periods and durations")
    class PeriodAndDurationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void until_withSameJulianDate_returnsZeroPeriod(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(julianDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void until_withEquivalentLocalDate_returnsZeroPeriod(JulianDate julianDate, LocalDate isoDate) {
            // The period between a JulianDate and its equivalent LocalDate should be zero.
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void until_fromLocalDateToEquivalentJulianDate_returnsZeroPeriod(JulianDate julianDate, LocalDate isoDate) {
            // LocalDate.until() returns an ISO Period, which should be zero.
            assertEquals(Period.ZERO, isoDate.until(julianDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void until_days(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(0, julianDate.until(isoDate.plusDays(0), DAYS));
            assertEquals(1, julianDate.until(isoDate.plusDays(1), DAYS));
            assertEquals(35, julianDate.until(isoDate.plusDays(35), DAYS));
            assertEquals(-40, julianDate.until(isoDate.minusDays(40), DAYS));
        }

        /**
         * Arguments: year1, month1, day1, year2, month2, day2, unit, expectedAmount
         */
        private static Stream<Arguments> untilTemporalUnitProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0L),
                Arguments.of(2014, 5, 26, 2014, 6, 1, DAYS, 6L),
                Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6L),
                Arguments.of(2014, 5, 26, 2014, 5, 26, WEEKS, 0L),
                Arguments.of(2014, 5, 26, 2014, 6, 1, WEEKS, 0L),
                Arguments.of(2014, 5, 26, 2014, 6, 2, WEEKS, 1L),
                Arguments.of(2014, 5, 26, 2014, 5, 26, MONTHS, 0L),
                Arguments.of(2014, 5, 26, 2014, 6, 25, MONTHS, 0L),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
                Arguments.of(2014, 5, 26, 2014, 5, 26, YEARS, 0L),
                Arguments.of(2014, 5, 26, 2015, 5, 25, YEARS, 0L),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
                Arguments.of(2014, 5, 26, 2014, 5, 26, DECADES, 0L),
                Arguments.of(2014, 5, 26, 2024, 5, 25, DECADES, 0L),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
                Arguments.of(2014, 5, 26, 2014, 5, 26, CENTURIES, 0L),
                Arguments.of(2014, 5, 26, 2114, 5, 25, CENTURIES, 0L),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
                Arguments.of(2014, 5, 26, 2014, 5, 26, MILLENNIA, 0L),
                Arguments.of(2014, 5, 26, 3014, 5, 25, MILLENNIA, 0L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
                Arguments.of(-2013, 5, 26, 0, 5, 26, ERAS, 0L),
                Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1L)
            );
        }

        @ParameterizedTest
        @MethodSource("untilTemporalUnitProvider")
        void until_calculatesAmountBetweenDatesCorrectly(int year1, int month1, int day1, int year2, int month2, int day2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(year1, month1, day1);
            JulianDate end = JulianDate.of(year2, month2, day2);
            assertEquals(expected, start.until(end, unit));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date creation and validation")
    class FactoryAndValidationTests {

        /**
         * Arguments: year, month, dayOfMonth
         */
        private static Stream<Arguments> invalidDateProvider() {
            return Stream.of(
                Arguments.of(1900, 0, 0),
                Arguments.of(1900, -1, 1),
                Arguments.of(1900, 0, 1),
                Arguments.of(1900, 13, 1),
                Arguments.of(1900, 14, 1),
                Arguments.of(1900, 1, -1),
                Arguments.of(1900, 1, 0),
                Arguments.of(1900, 1, 32),
                Arguments.of(1900, 2, 30), // February in a leap year has 29 days
                Arguments.of(1899, 2, 29), // February in a common year has 28 days
                Arguments.of(1900, 4, 31)  // April has 30 days
            );
        }

        @ParameterizedTest
        @MethodSource("invalidDateProvider")
        void of_shouldThrowExceptionForInvalidDate(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dayOfMonth));
        }

        /**
         * Arguments: year, month, expectedLength
         */
        private static Stream<Arguments> lengthOfMonthProvider() {
            return Stream.of(
                Arguments.of(1900, 1, 31),
                Arguments.of(1900, 2, 29), // Julian leap year
                Arguments.of(1900, 3, 31),
                Arguments.of(1900, 4, 30),
                Arguments.of(1900, 12, 31),
                Arguments.of(1901, 2, 28), // Common year
                Arguments.of(1904, 2, 29), // Julian leap year
                Arguments.of(2000, 2, 29), // Julian leap year
                Arguments.of(2100, 2, 29)  // Julian leap year
            );
        }

        @ParameterizedTest
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth_shouldReturnCorrectValue(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Accessing date fields and ranges")
    class FieldAndRangeTests {

        /**
         * Arguments: year, month, dayOfMonth, field, expectedMin, expectedMax
         */
        private static Stream<Arguments> rangeProvider() {
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, 1, 31),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29),
                Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28),
                Arguments.of(2012, 4, 23, DAY_OF_MONTH, 1, 30),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, 1, 366),
                Arguments.of(2011, 2, 23, DAY_OF_YEAR, 1, 365),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
                Arguments.of(2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4)
            );
        }

        @ParameterizedTest
        @MethodSource("rangeProvider")
        void range_shouldReturnCorrectRangeForField(int year, int month, int dayOfMonth, TemporalField field, int expectedMin, int expectedMax) {
            JulianDate date = JulianDate.of(year, month, dayOfMonth);
            assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
        }

        /**
         * Arguments: year, month, dayOfMonth, field, expectedValue
         */
        private static Stream<Arguments> getLongProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12L + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                Arguments.of(1, 6, 8, ERA, 1),
                Arguments.of(0, 6, 8, ERA, 0),
                Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7)
            );
        }

        @ParameterizedTest
        @MethodSource("getLongProvider")
        void getLong_shouldReturnCorrectValueForField(int year, int month, int dayOfMonth, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, dayOfMonth).getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date manipulation")
    class ManipulationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void plusDays_shouldBeEquivalentToIsoPlusDays(JulianDate julianDate, LocalDate isoDate) {
            long[] daysToAdd = {0, 1, 35, -1, -60};
            for (long days : daysToAdd) {
                JulianDate newJulianDate = julianDate.plus(days, DAYS);
                LocalDate newIsoDate = isoDate.plusDays(days);
                assertEquals(newIsoDate, LocalDate.from(newJulianDate),
                    () -> "JulianDate.plus(" + days + " days) should match LocalDate.plusDays(" + days + ")");
            }
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        void minusDays_shouldBeEquivalentToIsoMinusDays(JulianDate julianDate, LocalDate isoDate) {
            long[] daysToSubtract = {0, 1, 35, -1, -60};
            for (long days : daysToSubtract) {
                JulianDate newJulianDate = julianDate.minus(days, DAYS);
                LocalDate newIsoDate = isoDate.minusDays(days);
                assertEquals(newIsoDate, LocalDate.from(newJulianDate),
                    () -> "JulianDate.minus(" + days + " days) should match LocalDate.minusDays(" + days + ")");
            }
        }

        /**
         * Arguments: year, month, day, field, value, expectedYear, expectedMonth, expectedDay
         */
        private static Stream<Arguments> withFieldProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3L, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31L, 2014, 5, 31),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365L, 2014, 12, 31),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1L, 2014, 5, 5),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23L, 2014, 6, 9),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7L, 2014, 7, 26),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12L + 3 - 1, 2013, 3, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012L, 2012, 5, 26),
                Arguments.of(2014, 5, 26, YEAR_OF_ERA, 2012L, 2012, 5, 26),
                Arguments.of(2014, 5, 26, ERA, 0L, -2013, 5, 26),
                Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2L, 2011, 2, 28), // Adjust to shorter month
                Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2L, 2012, 2, 29), // Adjust to shorter leap month
                Arguments.of(2012, 2, 29, YEAR, 2011L, 2011, 2, 28) // Adjust leap day to common year
            );
        }

        @ParameterizedTest
        @MethodSource("withFieldProvider")
        void with_shouldAdjustFieldCorrectly(int year, int month, int day, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDay) {
            JulianDate start = JulianDate.of(year, month, day);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.with(field, value));
        }

        /**
         * Arguments: startYear, startMonth, startDay, amount, unit, endYear, endMonth, endDay
         */
        private static Stream<Arguments> plusAmountProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 0L, DAYS, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 8L, DAYS, 2014, 6, 3),
                Arguments.of(2014, 5, 26, -3L, DAYS, 2014, 5, 23),
                Arguments.of(2014, 5, 26, 3L, WEEKS, 2014, 6, 16),
                Arguments.of(2014, 5, 26, 3L, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, -5L, MONTHS, 2013, 12, 26),
                Arguments.of(2014, 5, 26, 3L, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3L, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3L, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3L, MILLENNIA, 5014, 5, 26),
                Arguments.of(2014, 5, 26, -5L, MILLENNIA, 2014 - 5000, 5, 26),
                Arguments.of(2014, 5, 26, -1L, ERAS, -2013, 5, 26)
            );
        }

        @ParameterizedTest
        @MethodSource("plusAmountProvider")
        void plus_shouldAddAmountCorrectly(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            JulianDate start = JulianDate.of(startYear, startMonth, startDay);
            JulianDate expected = JulianDate.of(endYear, endMonth, endDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusAmountProvider")
        void minus_shouldSubtractAmountCorrectly(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            JulianDate end = JulianDate.of(endYear, endMonth, endDay);
            JulianDate expected = JulianDate.of(startYear, startMonth, startDay);
            assertEquals(expected, end.minus(amount, unit));
        }

        @Test
        void plus_withIsoPeriod_shouldThrowException() {
            JulianDate date = JulianDate.of(2014, 5, 26);
            Period isoPeriod = Period.ofMonths(2);
            assertThrows(DateTimeException.class, () -> date.plus(isoPeriod));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("String representation")
    class ToStringTests {

        /**
         * Arguments: julianDate, expectedString
         */
        private static Stream<Arguments> toStringProvider() {
            return Stream.of(
                Arguments.of(JulianDate.of(1, 1, 1), "Julian AD 1-01-01"),
                Arguments.of(2012, 6, 23, "Julian AD 2012-06-23")
            );
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        void toString_returnsCorrectFormat(JulianDate date, String expected) {
            assertEquals(expected, date.toString());
        }

        // Overloaded variant for simpler data definition
        @ParameterizedTest
        @MethodSource("toStringProvider")
        void toString_returnsCorrectFormat(int year, int month, int day, String expected) {
            assertEquals(expected, JulianDate.of(year, month, day).toString());
        }
    }
}