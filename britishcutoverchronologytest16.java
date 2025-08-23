package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
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

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
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
 * Tests for the {@link BritishCutoverDate} class.
 */
@DisplayName("BritishCutoverDate")
class BritishCutoverDateTest {

    //-----------------------------------------------------------------------
    // Factory and Conversion Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Factory and Conversion Tests")
    class FactoryAndConversionTests {

        static Stream<Arguments> sampleBritishToIsoDates() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
                Arguments.of(BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
                Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
                Arguments.of(BritishCutoverDate.of(4, 3, 2), LocalDate.of(4, 2, 29)),
                Arguments.of(BritishCutoverDate.of(100, 3, 2), LocalDate.of(100, 3, 1)),
                Arguments.of(BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)), // leniently accept invalid
                Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)), // leniently accept invalid
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)),
                Arguments.of(BritishCutoverDate.of(2012, 7, 5), LocalDate.of(2012, 7, 5))
            );
        }

        @ParameterizedTest
        @MethodSource("sampleBritishToIsoDates")
        @DisplayName("should convert from BritishCutoverDate to the correct LocalDate")
        void fromBritishCutoverDate_shouldConvertToCorrectLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("sampleBritishToIsoDates")
        @DisplayName("should create a BritishCutoverDate from a LocalDate")
        void fromLocalDate_shouldCreateCorrectBritishCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("sampleBritishToIsoDates")
        @DisplayName("toEpochDay should be consistent with LocalDate")
        void toEpochDay_shouldBeConsistentWithLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("sampleBritishToIsoDates")
        @DisplayName("chronology.dateEpochDay should be consistent with LocalDate")
        void chronologyDateEpochDay_shouldBeConsistentWithLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("sampleBritishToIsoDates")
        @DisplayName("chronology.date(Temporal) should be consistent with from(Temporal)")
        void chronologyDateFromTemporal_shouldBeConsistentWithFrom(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }

        static Stream<Arguments> invalidDateProvider() {
            return Stream.of(
                Arguments.of(1900, 0, 0), Arguments.of(1900, 13, 1), Arguments.of(1900, 1, 0),
                Arguments.of(1900, 1, 32), Arguments.of(1900, 2, 30), Arguments.of(1899, 2, 29),
                Arguments.of(1900, 4, 31)
            );
        }

        @ParameterizedTest
        @MethodSource("invalidDateProvider")
        @DisplayName("of() with invalid date components should throw DateTimeException")
        void of_withInvalidDate_shouldThrowDateTimeException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, day));
        }
    }

    //-----------------------------------------------------------------------
    // Field, Range, and Length Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field, Range, and Length Tests")
    class FieldAndRangeTests {

        static Stream<Arguments> monthLengthProvider() {
            return Stream.of(
                Arguments.of(1700, 2, 29), // Julian leap year
                Arguments.of(1751, 2, 28), // Julian non-leap year
                Arguments.of(1752, 2, 29), // Gregorian leap year
                Arguments.of(1752, 9, 19), // The cutover month
                Arguments.of(1753, 2, 28), // Gregorian non-leap year
                Arguments.of(1800, 2, 28), // Gregorian non-leap year
                Arguments.of(2000, 2, 29)  // Gregorian leap year
            );
        }

        @ParameterizedTest
        @MethodSource("monthLengthProvider")
        @DisplayName("lengthOfMonth() should return correct length")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int length) {
            assertEquals(length, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> yearLengthProvider() {
            return Stream.of(
                Arguments.of(1700, 366), // Julian leap year
                Arguments.of(1751, 365), // Julian non-leap year before cutover
                Arguments.of(1752, 355), // The cutover year (lost 11 days)
                Arguments.of(1753, 365), // Gregorian non-leap year after cutover
                Arguments.of(1800, 365), // Gregorian non-leap year
                Arguments.of(2000, 366), // Gregorian leap year
                Arguments.of(2001, 365)  // Gregorian non-leap year
            );
        }

        @ParameterizedTest
        @MethodSource("yearLengthProvider")
        @DisplayName("lengthOfYear() should return correct length")
        void lengthOfYear_shouldReturnCorrectLength(int year, int length) {
            assertEquals(length, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
        }

        static Stream<Arguments> dateFieldRangeProvider() {
            return Stream.of(
                Arguments.of(1700, 2, DAY_OF_MONTH, 1, 29),
                Arguments.of(1751, 2, DAY_OF_MONTH, 1, 28),
                Arguments.of(1752, 9, DAY_OF_MONTH, 1, 30), // Range includes gap days
                Arguments.of(2011, 2, DAY_OF_MONTH, 1, 28),
                Arguments.of(2012, 2, DAY_OF_MONTH, 1, 29),
                Arguments.of(1751, 1, DAY_OF_YEAR, 1, 365),
                Arguments.of(1752, 1, DAY_OF_YEAR, 1, 355),
                Arguments.of(1753, 1, DAY_OF_YEAR, 1, 365),
                Arguments.of(1752, 9, ALIGNED_WEEK_OF_MONTH, 1, 3),
                Arguments.of(1752, 12, ALIGNED_WEEK_OF_YEAR, 1, 51)
            );
        }

        @ParameterizedTest(name = "range({3}) for {0}-{1} should be {4}-{5}")
        @MethodSource("dateFieldRangeProvider")
        void range_forField_shouldReturnCorrectRange(int year, int month, TemporalField field, int expectedMin, int expectedMax) {
            ValueRange expectedRange = ValueRange.of(expectedMin, expectedMax);
            assertEquals(expectedRange, BritishCutoverDate.of(year, month, 1).range(field));
        }

        static Stream<Arguments> dateFieldGetProvider() {
            return Stream.of(
                // Pre-cutover date
                Arguments.of(1752, 5, 26, DAY_OF_WEEK, 2L),
                Arguments.of(1752, 5, 26, DAY_OF_YEAR, 147L), // Julian: 31+29+31+30+26
                // Date in the gap (leniently treated as Julian)
                Arguments.of(1752, 9, 2, DAY_OF_WEEK, 3L),
                Arguments.of(1752, 9, 2, DAY_OF_YEAR, 246L), // Julian: 31+29+..+31+2
                // Post-cutover date
                Arguments.of(1752, 9, 14, DAY_OF_WEEK, 4L),
                Arguments.of(1752, 9, 14, DAY_OF_YEAR, 247L), // Day after 1752-09-02
                // Modern date
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1L),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, (long) LocalDate.of(2014, 5, 26).getDayOfYear()),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),
                Arguments.of(0, 6, 8, ERA, 0L)
            );
        }

        @ParameterizedTest(name = "getLong({3}) for {0}-{1}-{2} should be {4}")
        @MethodSource("dateFieldGetProvider")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, dom).getLong(field));
        }

        @Test
        void getLong_forUnsupportedField_shouldThrowException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> BritishCutoverDate.of(2012, 6, 30).getLong(MINUTE_OF_DAY));
        }
    }

    //-----------------------------------------------------------------------
    // Arithmetic Operation Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Arithmetic Operation Tests")
    class ArithmeticTests {

        static Stream<Arguments> plusProvider() {
            return Stream.of(
                // Around the cutover
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), 1, DAYS, BritishCutoverDate.of(1752, 9, 14)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), -1, DAYS, BritishCutoverDate.of(1752, 9, 2)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), 1, WEEKS, BritishCutoverDate.of(1752, 9, 20)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), 1, MONTHS, BritishCutoverDate.of(1752, 10, 2)),
                // Modern date
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 8, DAYS, BritishCutoverDate.of(2014, 6, 3)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, WEEKS, BritishCutoverDate.of(2014, 6, 16)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, MONTHS, BritishCutoverDate.of(2014, 8, 26)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, YEARS, BritishCutoverDate.of(2017, 5, 26)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, DECADES, BritishCutoverDate.of(2044, 5, 26)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, CENTURIES, BritishCutoverDate.of(2314, 5, 26)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, MILLENNIA, BritishCutoverDate.of(5014, 5, 26)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), -1, ERAS, BritishCutoverDate.of(-2013, 5, 26))
            );
        }

        @ParameterizedTest(name = "{0} plus {1} {2} should be {3}")
        @MethodSource("plusProvider")
        void plus_withAmountAndUnit_shouldReturnCorrectDate(BritishCutoverDate start, long amount, TemporalUnit unit, BritishCutoverDate expected) {
            assertEquals(expected, start.plus(amount, unit));
        }

        static Stream<Arguments> minusProvider() {
            return Stream.of(
                // Around the cutover
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), 1, DAYS, BritishCutoverDate.of(1752, 9, 2)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), -1, DAYS, BritishCutoverDate.of(1752, 9, 14)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 20), 1, WEEKS, BritishCutoverDate.of(1752, 9, 2)),
                Arguments.of(BritishCutoverDate.of(1752, 10, 2), 1, MONTHS, BritishCutoverDate.of(1752, 9, 2)),
                // Modern date
                Arguments.of(BritishCutoverDate.of(2014, 6, 3), 8, DAYS, BritishCutoverDate.of(2014, 5, 26)),
                Arguments.of(BritishCutoverDate.of(2014, 6, 16), 3, WEEKS, BritishCutoverDate.of(2014, 5, 26)),
                Arguments.of(BritishCutoverDate.of(2014, 8, 26), 3, MONTHS, BritishCutoverDate.of(2014, 5, 26)),
                Arguments.of(BritishCutoverDate.of(2017, 5, 26), 3, YEARS, BritishCutoverDate.of(2014, 5, 26))
            );
        }

        @ParameterizedTest(name = "{0} minus {1} {2} should be {3}")
        @MethodSource("minusProvider")
        void minus_withAmountAndUnit_shouldReturnCorrectDate(BritishCutoverDate start, long amount, TemporalUnit unit, BritishCutoverDate expected) {
            assertEquals(expected, start.minus(amount, unit));
        }

        static Stream<Arguments> untilUnitProvider() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), BritishCutoverDate.of(1752, 9, 14), DAYS, 2L),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 14), DAYS, 1L),
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), BritishCutoverDate.of(1752, 9, 19), WEEKS, 1L),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 10, 2), MONTHS, 1L),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), BritishCutoverDate.of(2015, 5, 26), YEARS, 1L)
            );
        }

        @ParameterizedTest(name = "until({1}, {2}) from {0} should be {3}")
        @MethodSource("untilUnitProvider")
        void until_withEndDateAndUnit_shouldReturnCorrectAmount(BritishCutoverDate start, BritishCutoverDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodProvider() {
            return Stream.of(
                // Start before cutover, end in/after cutover
                Arguments.of(1752, 7, 2, 1752, 9, 1, 0, 1, 30), // 30 days after 1752-08-02
                Arguments.of(1752, 7, 2, 1752, 9, 14, 0, 2, 1), // 1 day after 1752-09-02
                // Start in gap, end after cutover
                Arguments.of(1752, 9, 2, 1752, 10, 2, 0, 1, 0),
                // Start and end after cutover
                Arguments.of(1752, 9, 14, 1752, 10, 14, 0, 1, 0),
                // End before start (negative period)
                Arguments.of(1752, 9, 14, 1752, 9, 2, 0, 0, -1)
            );
        }

        @ParameterizedTest(name = "period from {0}-{1}-{2} to {3}-{4}-{5} should be {6}Y {7}M {8}D")
        @MethodSource("untilPeriodProvider")
        void until_withEndDate_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            ChronoPeriod expected = BritishCutoverChronology.INSTANCE.period(ey, em, ed);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest(name = "start.plus(start.until(end)) should equal end")
        @MethodSource("untilPeriodProvider")
        void plus_withReturnedPeriod_shouldRestoreOriginalDate(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            assertEquals(end, start.plus(start.until(end)));
        }

        @Test
        void until_sameDate_returnsZeroPeriod() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 14);
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        @Test
        void until_isoDateUntilSameDateAsBritishCutoverDate_returnsZeroPeriod() {
            LocalDate isoDate = LocalDate.of(2012, 7, 5);
            BritishCutoverDate cutoverDate = BritishCutoverDate.from(isoDate);
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
        }
    }

    //-----------------------------------------------------------------------
    // Adjustment Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Adjustment Tests")
    class AdjustmentTests {

        static Stream<Arguments> withFieldProvider() {
            return Stream.of(
                // Adjusting into the gap (leniently moves to valid date)
                Arguments.of(1752, 9, 2, DAY_OF_MONTH, 3, BritishCutoverDate.of(1752, 9, 14)),
                // Adjusting within the cutover month
                Arguments.of(1752, 9, 2, DAY_OF_WEEK, 4, BritishCutoverDate.of(1752, 9, 14)),
                // Adjusting month across the cutover
                Arguments.of(1752, 8, 4, MONTH_OF_YEAR, 9, BritishCutoverDate.of(1752, 9, 15)),
                // Adjusting year across the cutover
                Arguments.of(1751, 9, 4, YEAR, 1752, BritishCutoverDate.of(1752, 9, 15)),
                // Modern date adjustment
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, BritishCutoverDate.of(2014, 5, 28)),
                Arguments.of(2012, 2, 29, YEAR, 2011, BritishCutoverDate.of(2011, 2, 28))
            );
        }

        @ParameterizedTest(name = "{0} with({1}, {2}) should be {3}")
        @MethodSource("withFieldProvider")
        void with_usingField_shouldReturnAdjustedDate(BritishCutoverDate start, TemporalField field, long value, BritishCutoverDate expected) {
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Arguments> lastDayOfMonthProvider() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 2, 23), BritishCutoverDate.of(1752, 2, 29)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 30)),
                Arguments.of(2012, 2, 23, 2012, 2, 29)
            );
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthProvider")
        void with_lastDayOfMonth_shouldReturnCorrectDate(BritishCutoverDate input, BritishCutoverDate expected) {
            assertEquals(expected, input.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthProvider")
        void with_lastDayOfMonth_shouldReturnCorrectDate(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
            BritishCutoverDate input = BritishCutoverDate.of(year, month, day);
            BritishCutoverDate expected = BritishCutoverDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, input.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    //-----------------------------------------------------------------------
    // Object Method Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Object Method Tests")
    class ObjectMethodTests {

        static Stream<Arguments> toStringProvider() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1, 1, 1), "BritishCutover AD 1-01-01"),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), "BritishCutover AD 1752-09-14"),
                Arguments.of(BritishCutoverDate.of(2012, 6, 23), "BritishCutover AD 2012-06-23")
            );
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        void toString_shouldReturnCorrectFormat(BritishCutoverDate cutoverDate, String expected) {
            assertEquals(expected, cutoverDate.toString());
        }
    }
}