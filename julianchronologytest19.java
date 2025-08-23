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

@DisplayName("JulianDate Tests")
class JulianChronologyTestTest19 {

    /**
     * Provides sample Julian dates and their equivalent ISO dates.
     *
     * @return a stream of arguments: { JulianDate julianDate, LocalDate isoDate }
     */
    static Stream<Arguments> sampleJulianAndIsoDates() {
        return Stream.of(
            Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(JulianDate.of(1, 2, 28), LocalDate.of(1, 2, 26)),
            Arguments.of(JulianDate.of(1, 3, 1), LocalDate.of(1, 2, 27)),
            Arguments.of(JulianDate.of(1, 3, 2), LocalDate.of(1, 2, 28)),
            Arguments.of(JulianDate.of(1, 3, 3), LocalDate.of(1, 3, 1)),
            Arguments.of(JulianDate.of(4, 2, 28), LocalDate.of(4, 2, 26)),
            Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
            Arguments.of(JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)),
            Arguments.of(JulianDate.of(4, 3, 2), LocalDate.of(4, 2, 29)),
            Arguments.of(JulianDate.of(4, 3, 3), LocalDate.of(4, 3, 1)),
            Arguments.of(JulianDate.of(100, 2, 28), LocalDate.of(100, 2, 26)),
            Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)),
            Arguments.of(JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
            Arguments.of(JulianDate.of(100, 3, 2), LocalDate.of(100, 3, 1)),
            Arguments.of(JulianDate.of(100, 3, 3), LocalDate.of(100, 3, 2)),
            Arguments.of(JulianDate.of(0, 12, 31), LocalDate.of(0, 12, 29)),
            Arguments.of(JulianDate.of(0, 12, 30), LocalDate.of(0, 12, 28)),
            Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            Arguments.of(JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)),
            Arguments.of(JulianDate.of(2012, 6, 22), LocalDate.of(2012, 7, 5)),
            Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    @Nested
    @DisplayName("Conversion to and from ISO")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest19#sampleJulianAndIsoDates")
        void from_JulianDate_shouldReturnEquivalentIsoDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(julianDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest19#sampleJulianAndIsoDates")
        void from_isoDate_shouldReturnEquivalentJulianDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest19#sampleJulianAndIsoDates")
        void chronology_dateEpochDay_shouldCreateEquivalentJulianDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest19#sampleJulianAndIsoDates")
        void toEpochDay_shouldReturnEquivalentIsoEpochDay(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), julianDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest19#sampleJulianAndIsoDates")
        void chronology_dateFromTemporal_shouldCreateEquivalentJulianDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Date Arithmetic")
    class ArithmeticTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest19#sampleJulianAndIsoDates")
        void plusDays_shouldReturnCorrectlyAdvancedDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(julianDate.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(julianDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(julianDate.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(julianDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(julianDate.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest19#sampleJulianAndIsoDates")
        void minusDays_shouldReturnCorrectlyRewoundDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(julianDate.minus(0, DAYS)));
            assertEquals(isoDate.minusDays(1), LocalDate.from(julianDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(julianDate.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(julianDate.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(julianDate.minus(-60, DAYS)));
        }

        /**
         * Provides arguments for date addition tests.
         *
         * @return a stream of arguments: { startYear, startMonth, startDay, amount, unit, expectedYear, expectedMonth, expectedDay }
         */
        static Stream<Arguments> plusUnitAmounts() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 0L, DAYS, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 8L, DAYS, 2014, 6, 3),
                Arguments.of(2014, 5, 26, -3L, DAYS, 2014, 5, 23),
                Arguments.of(2014, 5, 26, 0L, WEEKS, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 3L, WEEKS, 2014, 6, 16),
                Arguments.of(2014, 5, 26, -5L, WEEKS, 2014, 4, 21),
                Arguments.of(2014, 5, 26, 0L, MONTHS, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 3L, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, -5L, MONTHS, 2013, 12, 26),
                Arguments.of(2014, 5, 26, 0L, YEARS, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 3L, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, -5L, YEARS, 2009, 5, 26),
                Arguments.of(2014, 5, 26, 0L, DECADES, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 3L, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, -5L, DECADES, 1964, 5, 26),
                Arguments.of(2014, 5, 26, 0L, CENTURIES, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 3L, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, -5L, CENTURIES, 1514, 5, 26),
                Arguments.of(2014, 5, 26, 0L, MILLENNIA, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 3L, MILLENNIA, 5014, 5, 26),
                Arguments.of(2014, 5, 26, -5L, MILLENNIA, 2014 - 5000, 5, 26),
                Arguments.of(2014, 5, 26, -1L, ERAS, -2013, 5, 26)
            );
        }

        @ParameterizedTest
        @MethodSource("plusUnitAmounts")
        void plus_withAmountAndUnit_shouldReturnAdjustedDate(int year, int month, int dayOfMonth, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDay) {
            JulianDate start = JulianDate.of(year, month, dayOfMonth);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusUnitAmounts")
        void minus_withAmountAndUnit_shouldReturnAdjustedDate(int expectedYear, int expectedMonth, int expectedDay, long amount, TemporalUnit unit, int year, int month, int dayOfMonth) {
            // Reuses plusUnitAmounts data by swapping start and expected dates
            JulianDate start = JulianDate.of(year, month, dayOfMonth);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.minus(amount, unit));
        }
    }

    @Nested
    @DisplayName("Period and Duration")
    class PeriodAndDurationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest19#sampleJulianAndIsoDates")
        void until_sameJulianDate_shouldReturnZeroPeriod(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(julianDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest19#sampleJulianAndIsoDates")
        void until_equivalentIsoDate_shouldReturnZeroPeriod(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest19#sampleJulianAndIsoDates")
        void isoDate_until_equivalentJulianDate_shouldReturnZeroPeriod(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(julianDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTestTest19#sampleJulianAndIsoDates")
        void until_otherDateInDays_shouldReturnCorrectDayCount(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(0, julianDate.until(isoDate.plusDays(0), DAYS));
            assertEquals(1, julianDate.until(isoDate.plusDays(1), DAYS));
            assertEquals(35, julianDate.until(isoDate.plusDays(35), DAYS));
            assertEquals(-40, julianDate.until(isoDate.minusDays(40), DAYS));
        }

        /**
         * Provides arguments for `until` tests.
         *
         * @return a stream of arguments: { year1, month1, day1, year2, month2, day2, unit, expectedAmount }
         */
        static Stream<Arguments> untilUnitAmounts() {
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
        @MethodSource("untilUnitAmounts")
        void until_otherDateInUnit_shouldReturnCorrectAmount(int year1, int month1, int day1, int year2, int month2, int day2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(year1, month1, day1);
            JulianDate end = JulianDate.of(year2, month2, day2);
            assertEquals(expected, start.until(end, unit));
        }
    }

    @Nested
    @DisplayName("Factory and Validation")
    class FactoryAndValidationTests {

        /**
         * Provides invalid date components.
         *
         * @return a stream of arguments: { year, month, dayOfMonth }
         */
        static Stream<Arguments> invalidDateComponents() {
            return Stream.of(
                Arguments.of(1900, 0, 0), Arguments.of(1900, -1, 1), Arguments.of(1900, 0, 1),
                Arguments.of(1900, 13, 1), Arguments.of(1900, 14, 1), Arguments.of(1900, 1, -1),
                Arguments.of(1900, 1, 0), Arguments.of(1900, 1, 32), Arguments.of(1900, 2, -1),
                Arguments.of(1900, 2, 0), Arguments.of(1900, 2, 30), Arguments.of(1900, 2, 31),
                Arguments.of(1900, 2, 32), Arguments.of(1899, 2, -1), Arguments.of(1899, 2, 0),
                Arguments.of(1899, 2, 29), Arguments.of(1899, 2, 30), Arguments.of(1899, 2, 31),
                Arguments.of(1899, 2, 32), Arguments.of(1900, 12, -1), Arguments.of(1900, 12, 0),
                Arguments.of(1900, 12, 32), Arguments.of(1900, 3, 32), Arguments.of(1900, 4, 31),
                Arguments.of(1900, 5, 32), Arguments.of(1900, 6, 31), Arguments.of(1900, 7, 32),
                Arguments.of(1900, 8, 32), Arguments.of(1900, 9, 31), Arguments.of(1900, 10, 32),
                Arguments.of(1900, 11, 31)
            );
        }

        @ParameterizedTest
        @MethodSource("invalidDateComponents")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dayOfMonth));
        }

        /**
         * Provides years, months, and their expected lengths.
         *
         * @return a stream of arguments: { year, month, expectedLength }
         */
        static Stream<Arguments> monthLengths() {
            return Stream.of(
                Arguments.of(1900, 1, 31), Arguments.of(1900, 2, 29), Arguments.of(1900, 3, 31),
                Arguments.of(1900, 4, 30), Arguments.of(1900, 5, 31), Arguments.of(1900, 6, 30),
                Arguments.of(1900, 7, 31), Arguments.of(1900, 8, 31), Arguments.of(1900, 9, 30),
                Arguments.of(1900, 10, 31), Arguments.of(1900, 11, 30), Arguments.of(1900, 12, 31),
                Arguments.of(1901, 2, 28), Arguments.of(1902, 2, 28), Arguments.of(1903, 2, 28),
                Arguments.of(1904, 2, 29), Arguments.of(2000, 2, 29), Arguments.of(2100, 2, 29)
            );
        }

        @ParameterizedTest
        @MethodSource("monthLengths")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }
    }

    @Nested
    @DisplayName("Field Accessors and Ranges")
    class FieldAccessorTests {

        /**
         * Provides dates, fields, and their expected value ranges.
         *
         * @return a stream of arguments: { year, month, day, field, min, max }
         */
        static Stream<Arguments> fieldRanges() {
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, 1, 31),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29),
                Arguments.of(2012, 3, 23, DAY_OF_MONTH, 1, 31),
                Arguments.of(2012, 4, 23, DAY_OF_MONTH, 1, 30),
                Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, 1, 366),
                Arguments.of(2011, 2, 23, DAY_OF_YEAR, 1, 365),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
                Arguments.of(2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4)
            );
        }

        @ParameterizedTest
        @MethodSource("fieldRanges")
        void range_forField_shouldReturnCorrectValueRange(int year, int month, int dayOfMonth, TemporalField field, int expectedMin, int expectedMax) {
            ValueRange expectedRange = ValueRange.of(expectedMin, expectedMax);
            assertEquals(expectedRange, JulianDate.of(year, month, dayOfMonth).range(field));
        }

        /**
         * Provides dates, fields, and their expected long values.
         *
         * @return a stream of arguments: { year, month, day, field, expectedValue }
         */
        static Stream<Arguments> fieldValues() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7L),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 146L), // 31+28+31+30+26
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1L),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),
                Arguments.of(1, 6, 8, ERA, 1L),
                Arguments.of(0, 6, 8, ERA, 0L),
                Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7L)
            );
        }

        @ParameterizedTest
        @MethodSource("fieldValues")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int dayOfMonth, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, dayOfMonth).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Adjustments")
    class AdjustmentTests {

        /**
         * Provides arguments for `with(field, value)` tests.
         *
         * @return a stream of arguments: { startYear, startMonth, startDay, field, newValue, expectedYear, expectedMonth, expectedDay }
         */
        static Stream<Arguments> withFieldAdjustments() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3L, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31L, 2014, 5, 31),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365L, 2014, 12, 31),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3L, 2014, 5, 24),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1L, 2014, 5, 5),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2L, 2014, 5, 22),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23L, 2014, 6, 9),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7L, 2014, 7, 26),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1L, 2013, 3, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012L, 2012, 5, 26),
                Arguments.of(2014, 5, 26, YEAR_OF_ERA, 2012L, 2012, 5, 26),
                Arguments.of(2014, 5, 26, ERA, 0L, -2013, 5, 26),
                Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2L, 2011, 2, 28),
                Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2L, 2012, 2, 29),
                Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 6L, 2012, 6, 30),
                Arguments.of(2012, 2, 29, YEAR, 2011L, 2011, 2, 28),
                Arguments.of(-2013, 6, 8, YEAR_OF_ERA, 2012L, -2011, 6, 8),
                Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 3L, 2014, 5, 22)
            );
        }

        @ParameterizedTest
        @MethodSource("withFieldAdjustments")
        void with_fieldAndValue_shouldReturnAdjustedDate(int year, int month, int dayOfMonth, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDay) {
            JulianDate start = JulianDate.of(year, month, dayOfMonth);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.with(field, value));
        }

        @Test
        void with_usingLocalDateAdjuster_shouldReturnCorrectJulianDate() {
            JulianDate julian = JulianDate.of(2000, 1, 4);
            JulianDate adjusted = julian.with(LocalDate.of(2012, 7, 6));
            assertEquals(JulianDate.of(2012, 6, 23), adjusted);
        }
    }

    @Nested
    @DisplayName("String Representation")
    class FormattingTests {

        /**
         * Provides dates and their expected string representations.
         *
         * @return a stream of arguments: { julianDate, expectedString }
         */
        static Stream<Arguments> toStringRepresentations() {
            return Stream.of(
                Arguments.of(JulianDate.of(1, 1, 1), "Julian AD 1-01-01"),
                Arguments.of(JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23")
            );
        }

        @ParameterizedTest
        @MethodSource("toStringRepresentations")
        void toString_shouldReturnCorrectRepresentation(JulianDate julianDate, String expected) {
            assertEquals(expected, julianDate.toString());
        }
    }
}