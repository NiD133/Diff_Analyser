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
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("JulianDate Tests")
class JulianDateTest {

    @Nested
    @DisplayName("Interoperability with ISO-8601 (LocalDate)")
    class InteroperabilityTests {

        static Stream<Arguments> julianAndIsoDateProvider() {
            return Stream.of(
                Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
                Arguments.of(JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
                Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
                Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
                Arguments.of(JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)),
                Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)),
                Arguments.of(JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
                Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
                Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
                Arguments.of(JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)),
                Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
            );
        }

        @ParameterizedTest
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("should convert to an equivalent LocalDate")
        void testConversionToLocalDate(JulianDate julianDate, LocalDate expectedIsoDate) {
            assertEquals(expectedIsoDate, LocalDate.from(julianDate));
        }

        @ParameterizedTest
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("should be created from an equivalent LocalDate")
        void testCreationFromLocalDate(JulianDate expectedJulianDate, LocalDate isoDate) {
            assertEquals(expectedJulianDate, JulianDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("should be created from an ISO epoch day")
        void testCreationFromEpochDay(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("should convert to an ISO epoch day")
        void testConversionToEpochDay(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), julianDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("should be created from a temporal accessor")
        void testCreationFromTemporal(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Factory and Validation")
    class FactoryAndValidationTests {

        static Stream<Arguments> invalidDateProvider() {
            return Stream.of(
                Arguments.of(1900, 0, 1),   // Invalid month
                Arguments.of(1900, 13, 1),  // Invalid month
                Arguments.of(1900, 1, 0),   // Invalid day
                Arguments.of(1900, 1, 32),  // Invalid day for Jan
                Arguments.of(1900, 2, 30),  // Invalid day for Feb (leap)
                Arguments.of(1899, 2, 29),  // Invalid day for Feb (non-leap)
                Arguments.of(1900, 4, 31)   // Invalid day for Apr
            );
        }

        @ParameterizedTest
        @MethodSource("invalidDateProvider")
        @DisplayName("of(y, m, d) should throw exception for invalid dates")
        void of_shouldThrowExceptionForInvalidDate(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dayOfMonth));
        }

        static Stream<Arguments> lengthOfMonthProvider() {
            return Stream.of(
                Arguments.of(1900, 1, 31),
                Arguments.of(1900, 2, 29), // Julian leap year
                Arguments.of(1901, 2, 28), // Julian non-leap year
                Arguments.of(1904, 2, 29), // Julian leap year
                Arguments.of(2000, 2, 29), // Julian leap year
                Arguments.of(2100, 2, 29)  // Julian leap year
            );
        }

        @ParameterizedTest
        @MethodSource("lengthOfMonthProvider")
        @DisplayName("lengthOfMonth() should return correct day count")
        void lengthOfMonth_shouldReturnCorrectDayCount(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }
    }

    @Nested
    @DisplayName("Field Access")
    class FieldAccessTests {

        static Stream<Arguments> rangeProvider() {
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, 1, 31),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29), // Leap year
                Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28), // Non-leap year
                Arguments.of(2012, 4, 23, DAY_OF_MONTH, 1, 30),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, 1, 366), // Leap year
                Arguments.of(2011, 2, 23, DAY_OF_YEAR, 1, 365), // Non-leap year
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
                Arguments.of(2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4)
            );
        }

        @ParameterizedTest
        @MethodSource("rangeProvider")
        @DisplayName("range(field) should return correct value range")
        void range_shouldReturnCorrectValueRange(int year, int month, int dayOfMonth, TemporalField field, int expectedMin, int expectedMax) {
            JulianDate date = JulianDate.of(year, month, dayOfMonth);
            assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
        }

        static Stream<Arguments> fieldGetProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7L),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                // Day of year for 2014-05-26 is 31(Jan) + 28(Feb) + 31(Mar) + 30(Apr) + 26(May) = 146
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 146L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                // Proleptic month for 2014-05 is (2014 * 12) + 5 - 1 = 24172
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 24172L),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L), // AD
                Arguments.of(0, 6, 8, ERA, 0L),     // BC
                Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7L)
            );
        }

        @ParameterizedTest
        @MethodSource("fieldGetProvider")
        @DisplayName("getLong(field) should return correct field value")
        void getLong_shouldReturnCorrectFieldValue(int year, int month, int dayOfMonth, TemporalField field, long expectedValue) {
            assertEquals(expectedValue, JulianDate.of(year, month, dayOfMonth).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class DateManipulationTests {

        static Stream<Arguments> withProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2014, 5, 26, ERA, 0, -2013, 5, 26), // Switch to BC era
                Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29), // Adjusts day for shorter month
                Arguments.of(2012, 2, 29, YEAR, 2011, 2011, 2, 28) // Adjusts for non-leap year
            );
        }

        @ParameterizedTest
        @MethodSource("withProvider")
        @DisplayName("with(field, value) should return a correctly modified date")
        void with_shouldReturnModifiedDate(int year, int month, int dayOfMonth, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDay) {
            JulianDate start = JulianDate.of(year, month, dayOfMonth);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Arguments> plusProvider() {
            return Stream.of(
                // Arguments: startYear, startMonth, startDay, amount, unit, expectedYear, expectedMonth, expectedDay
                Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
                Arguments.of(2014, 5, 26, -3, DAYS, 2014, 5, 23),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                Arguments.of(2014, 5, 26, -1, ERAS, -2013, 5, 26)
            );
        }

        @ParameterizedTest(name = "[{index}] {0}-{1}-{2} plus {3} {4} = {5}-{6}-{7}")
        @MethodSource("plusProvider")
        @DisplayName("plus(amount, unit) should add the specified amount")
        void plus_shouldAddAmount(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDay) {
            JulianDate start = JulianDate.of(startYear, startMonth, startDay);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "[{index}] {5}-{6}-{7} minus {3} {4} = {0}-{1}-{2}")
        @MethodSource("plusProvider") // Reusing plusProvider: start.plus(amount) == end
        @DisplayName("minus(amount, unit) should be the inverse of plus(amount, unit)")
        void minus_shouldBeInverseOfPlus(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            JulianDate startDate = JulianDate.of(startYear, startMonth, startDay);
            JulianDate endDate = JulianDate.of(endYear, endMonth, endDay);
            assertEquals(startDate, endDate.minus(amount, unit));
        }
    }

    @Nested
    @DisplayName("Period and Duration Calculation")
    class CalculationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest$InteroperabilityTests#julianAndIsoDateProvider")
        @DisplayName("until() should return zero for an equivalent date in a different chronology")
        void until_equivalentDateInDifferentChronology_returnsZero(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(julianDate));
        }

        @Test
        @DisplayName("until() the same date instance should return a zero period")
        void until_sameDate_returnsZero() {
            JulianDate date = JulianDate.of(2014, 5, 26);
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        static Stream<Arguments> untilProvider() {
            return Stream.of(
                // Arguments: startY, startM, startD, endY, endM, endD, unit, expectedAmount
                Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0L),
                Arguments.of(2014, 5, 26, 2014, 6, 1, DAYS, 6L),
                Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6L),
                Arguments.of(2014, 5, 26, 2014, 6, 2, WEEKS, 1L),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
                Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1L)
            );
        }

        @ParameterizedTest
        @MethodSource("untilProvider")
        @DisplayName("until(endDate, unit) should calculate the correct amount of time")
        void until_shouldCalculateAmountBetweenDates(int startY, int startM, int startD, int endY, int endM, int endD, TemporalUnit unit, long expectedAmount) {
            JulianDate start = JulianDate.of(startY, startM, startD);
            JulianDate end = JulianDate.of(endY, endM, endD);
            assertEquals(expectedAmount, start.until(end, unit));
        }

        @Test
        @DisplayName("until() should throw exception for unsupported units")
        void until_shouldThrowForUnsupportedUnits() {
            JulianDate start = JulianDate.of(2012, 6, 30);
            JulianDate end = JulianDate.of(2012, 7, 1);
            assertThrows(UnsupportedTemporalTypeException.class, () -> start.until(end, MINUTES));
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentationTests {

        static Stream<Arguments> toStringProvider() {
            return Stream.of(
                Arguments.of(JulianDate.of(1, 1, 1), "Julian AD 1-01-01"),
                Arguments.of(JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23")
            );
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        @DisplayName("toString() should return the correct string format")
        void toString_shouldReturnCorrectFormat(JulianDate julianDate, String expected) {
            assertEquals(expected, julianDate.toString());
        }
    }
}