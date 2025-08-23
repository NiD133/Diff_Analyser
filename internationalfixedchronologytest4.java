package org.threeten.extra.chrono;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link InternationalFixedDate}.
 */
@DisplayName("InternationalFixedDate")
class InternationalFixedDateTest {

    //-----------------------------------------------------------------------
    // Factory and Conversion Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Factory and Conversion")
    class FactoryAndConversionTests {

        static Stream<Arguments> sampleFixedAndIsoDates() {
            return Stream.of(
                Arguments.of(InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)),
                Arguments.of(InternationalFixedDate.of(1, 1, 2), LocalDate.of(1, 1, 2)),
                Arguments.of(InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)),
                Arguments.of(InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)),
                Arguments.of(InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)),
                Arguments.of(InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)),
                Arguments.of(InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4))
            );
        }

        @ParameterizedTest
        @MethodSource("sampleFixedAndIsoDates")
        void from_fixedDate_shouldReturnCorrectLocalDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(fixedDate));
        }

        @ParameterizedTest
        @MethodSource("sampleFixedAndIsoDates")
        void from_localDate_shouldReturnCorrectFixedDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("sampleFixedAndIsoDates")
        void toEpochDay_shouldReturnCorrectValue(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), fixedDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("sampleFixedAndIsoDates")
        void chronology_dateFromEpochDay_shouldCreateCorrectFixedDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("sampleFixedAndIsoDates")
        void chronology_dateFromTemporal_shouldCreateCorrectFixedDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }

        static Stream<Arguments> invalidDateComponents() {
            return Stream.of(
                Arguments.of(0, 1, 1),      // Invalid year
                Arguments.of(1900, 0, 1),     // Invalid month
                Arguments.of(1900, 14, 1),    // Invalid month
                Arguments.of(1900, 1, 0),      // Invalid day
                Arguments.of(1900, 1, 29),    // Invalid day for month
                Arguments.of(1900, 2, 29),    // Invalid day for month
                Arguments.of(1900, 13, 30)    // Invalid day for Year Day month
            );
        }

        @ParameterizedTest
        @MethodSource("invalidDateComponents")
        void of_withInvalidDateParts_throwsException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        static Stream<Arguments> nonLeapYears() {
            return Stream.of(
                Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(300), Arguments.of(1900)
            );
        }

        @ParameterizedTest
        @MethodSource("nonLeapYears")
        void of_withLeapDayInNonLeapYear_throwsException(int year) {
            // Month 6 (June) only has a 29th day in a leap year.
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }

        static Stream<Arguments> invalidEraValues() {
            return Stream.of(Arguments.of(-1), Arguments.of(0), Arguments.of(2));
        }

        @ParameterizedTest
        @MethodSource("invalidEraValues")
        void eraOf_withInvalidValue_throwsException(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        static Stream<Arguments> invalidProlepticYearForEra() {
            return Stream.of(Arguments.of(-10), Arguments.of(-1), Arguments.of(0));
        }

        @ParameterizedTest
        @MethodSource("invalidProlepticYearForEra")
        void prolepticYear_withInvalidYearOfEra_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }
    }

    //-----------------------------------------------------------------------
    // Date Field Access Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Field Access")
    class DateFieldAccessTests {

        static Stream<Arguments> fieldValueSamples() {
            // Arguments: year, month, day, field, expectedValue
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 28 * 4 + 26),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014L * 13 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                // Leap year specific
                Arguments.of(2012, 9, 26, DAY_OF_YEAR, 28 * 6 + 1 + 28 * 2 + 26),
                // Year Day (special day at end of year)
                Arguments.of(2014, 13, 29, DAY_OF_WEEK, 0),
                Arguments.of(2014, 13, 29, DAY_OF_MONTH, 29),
                Arguments.of(2014, 13, 29, DAY_OF_YEAR, 13 * 28 + 1),
                // Leap Day (special day in June)
                Arguments.of(2012, 6, 29, DAY_OF_WEEK, 0),
                Arguments.of(2012, 6, 29, DAY_OF_MONTH, 29),
                Arguments.of(2012, 6, 29, DAY_OF_YEAR, 6 * 28 + 1)
            );
        }

        @ParameterizedTest
        @MethodSource("fieldValueSamples")
        void getLong_forField_returnsCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            InternationalFixedDate date = InternationalFixedDate.of(year, month, day);
            assertEquals(expected, date.getLong(field));
        }

        static Stream<Arguments> fieldRangeSamples() {
            // Arguments: year, month, day, field, expectedRange
            return Stream.of(
                // Day of month range depends on the month and if it's a leap year
                Arguments.of(2011, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)), // Standard month
                Arguments.of(2012, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 29)), // Leap month in a leap year
                Arguments.of(2011, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 28)), // Leap month in a non-leap year
                Arguments.of(2012, 13, 23, DAY_OF_MONTH, ValueRange.of(1, 29)),// Year-day month
                // Day of year range depends on leap year
                Arguments.of(2011, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 365)), // Non-leap year
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366)), // Leap year
                // Other fields
                Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)),
                Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
                // Special day ranges
                Arguments.of(2012, 6, 29, DAY_OF_WEEK, ValueRange.of(0, 0)), // Leap Day
                Arguments.of(2012, 13, 29, DAY_OF_WEEK, ValueRange.of(0, 0)) // Year Day
            );
        }

        @ParameterizedTest
        @MethodSource("fieldRangeSamples")
        void range_forField_returnsCorrectValueRange(int year, int month, int day, TemporalField field, ValueRange expected) {
            InternationalFixedDate date = InternationalFixedDate.of(year, month, day);
            assertEquals(expected, date.range(field));
        }

        static Stream<Arguments> monthLengthSamples() {
            // Arguments: year, month, expectedLength
            return Stream.of(
                Arguments.of(1900, 1, 28),  // Standard month
                Arguments.of(1900, 13, 29), // Year Day month
                Arguments.of(1904, 6, 29),  // Leap month in leap year
                Arguments.of(1900, 6, 28)   // Leap month in non-leap year
            );
        }

        @ParameterizedTest
        @MethodSource("monthLengthSamples")
        void lengthOfMonth_returnsCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, 1).lengthOfMonth());
        }
    }

    //-----------------------------------------------------------------------
    // Date Manipulation Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Manipulation")
    class DateManipulationTests {

        static Stream<Arguments> withSamples() {
            // Arguments: start(y,m,d), field, value, expected(y,m,d)
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 13, 28),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                // Adjusting a leap day to a non-leap year
                Arguments.of(2012, 6, 29, YEAR, 2013, 2013, 6, 28)
            );
        }

        @ParameterizedTest
        @MethodSource("withSamples")
        void with_forField_returnsCorrectlyAdjustedDate(
            int startYear, int startMonth, int startDay,
            TemporalField field, long value,
            int expectedYear, int expectedMonth, int expectedDay) {

            InternationalFixedDate start = InternationalFixedDate.of(startYear, startMonth, startDay);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Arguments> invalidWithSamples() {
            // Arguments: y, m, d, field, value
            return Stream.of(
                Arguments.of(2013, 1, 1, DAY_OF_MONTH, 29),
                Arguments.of(2013, 6, 1, DAY_OF_MONTH, 29),
                Arguments.of(2013, 1, 1, DAY_OF_YEAR, 366),
                Arguments.of(2012, 1, 1, DAY_OF_YEAR, 367),
                Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14)
            );
        }

        @ParameterizedTest
        @MethodSource("invalidWithSamples")
        void with_forFieldWithInvalidValue_throwsException(int year, int month, int day, TemporalField field, long value) {
            InternationalFixedDate date = InternationalFixedDate.of(year, month, day);
            assertThrows(DateTimeException.class, () -> date.with(field, value));
        }

        static Stream<Arguments> lastDayOfMonthSamples() {
            // Arguments: start(y,m,d), expected(y,m,d)
            return Stream.of(
                Arguments.of(2012, 6, 23, 2012, 6, 29),
                Arguments.of(2009, 6, 23, 2009, 6, 28),
                Arguments.of(2007, 13, 23, 2007, 13, 29)
            );
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthSamples")
        void with_lastDayOfMonth_adjustsToLastDayOfMonth(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, day);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> plusUnitSamples() {
            // Arguments: start(y,m,d), amount, unit, expected(y,m,d)
            return Stream.of(
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 6),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 19),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26)
            );
        }

        @ParameterizedTest
        @MethodSource("plusUnitSamples")
        void plus_withUnit_returnsCorrectlyAdjustedDate(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDay) {
            InternationalFixedDate start = InternationalFixedDate.of(startYear, startMonth, startDay);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        static Stream<Arguments> minusUnitSamples() {
            // This data is derived from plusUnitSamples: if start.plus(amount) == end, then end.minus(amount) == start
            // Arguments: start(y,m,d), amount, unit, expected(y,m,d)
            return plusUnitSamples().map(args -> {
                Object[] original = args.get();
                return Arguments.of(original[5], original[6], original[7], original[3], original[4], original[0], original[1], original[2]);
            });
        }

        @ParameterizedTest
        @MethodSource("minusUnitSamples")
        void minus_withUnit_returnsCorrectlyAdjustedDate(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDay) {
            InternationalFixedDate start = InternationalFixedDate.of(startYear, startMonth, startDay);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Stream<Arguments> plusLeapAndYearDaySamples() {
            // Arguments: start(y,m,d), amount, unit, expected(y,m,d)
            return Stream.of(
                // From Year Day
                Arguments.of(2014, 13, 29, 8, DAYS, 2015, 1, 8),
                Arguments.of(2014, 13, 29, 3, MONTHS, 2015, 3, 28),
                Arguments.of(2014, 13, 29, 3, YEARS, 2017, 13, 29),
                // From Leap Day
                Arguments.of(2012, 6, 29, 8, DAYS, 2012, 7, 8),
                Arguments.of(2012, 6, 29, 3, MONTHS, 2012, 9, 28),
                Arguments.of(2012, 6, 29, 3, YEARS, 2015, 6, 28), // lands on non-leap year
                Arguments.of(2012, 6, 29, 4, YEARS, 2016, 6, 29)  // lands on leap year
            );
        }

        @ParameterizedTest
        @MethodSource("plusLeapAndYearDaySamples")
        void plus_withUnitForLeapAndYearDays_returnsCorrectlyAdjustedDate(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDay) {
            InternationalFixedDate start = InternationalFixedDate.of(startYear, startMonth, startDay);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.plus(amount, unit));
        }
    }

    //-----------------------------------------------------------------------
    // Period and Duration Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Period and Duration")
    class PeriodAndDurationTests {

        static Stream<Arguments> untilUnitSamples() {
            // Arguments: start(y,m,d), end(y,m,d), unit, expectedAmount
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0L),
                Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 6L),
                Arguments.of(2014, 5, 26, 2014, 6, 5, WEEKS, 1L),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, ERAS, 0L),
                // Spanning special days
                Arguments.of(2012, 6, 28, 2012, 7, 1, DAYS, 2L), // across Leap Day
                Arguments.of(2014, 13, 28, 2015, 1, 1, DAYS, 2L)  // across Year Day
            );
        }

        @ParameterizedTest
        @MethodSource("untilUnitSamples")
        void until_withUnit_returnsCorrectDuration(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodSamples() {
            // Arguments: start(y,m,d), end(y,m,d), expectedPeriod(y,m,d)
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 6),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                // Spanning special days
                Arguments.of(2003, 13, 29, 2004, 6, 29, 0, 6, 0), // Year Day to Leap Day
                Arguments.of(2004, 6, 29, 2004, 13, 29, 0, 7, 0)  // Leap Day to Year Day
            );
        }

        @ParameterizedTest
        @MethodSource("untilPeriodSamples")
        void until_withEndDate_returnsCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }

        @Test
        void until_sameDateInDifferentChronology_returnsZeroPeriod() {
            InternationalFixedDate fixedDate = InternationalFixedDate.of(2012, 6, 15);
            LocalDate isoDate = LocalDate.of(2012, 6, 3);

            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(fixedDate));
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(fixedDate));
        }
    }

    //-----------------------------------------------------------------------
    // General Method Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("General Methods")
    class GeneralMethodTests {

        @Test
        void isLeapYear_forSpecificYears_returnsCorrectly() {
            assertTrue(InternationalFixedChronology.INSTANCE.isLeapYear(400));
            assertFalse(InternationalFixedChronology.INSTANCE.isLeapYear(100));
            assertTrue(InternationalFixedChronology.INSTANCE.isLeapYear(4));
            assertFalse(InternationalFixedChronology.INSTANCE.isLeapYear(3));
        }

        static Stream<Arguments> toStringSamples() {
            return Stream.of(
                Arguments.of(InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"),
                Arguments.of(InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"),
                Arguments.of(InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"), // Leap Day
                Arguments.of(InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29") // Year Day
            );
        }

        @ParameterizedTest
        @MethodSource("toStringSamples")
        void toString_returnsCorrectFormatting(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }

        @Test
        void equals_and_hashCode_contract() {
            new EqualsTester()
                .addEqualityGroup(
                    InternationalFixedDate.of(2014, 5, 26),
                    InternationalFixedDate.of(2014, 5, 26))
                .addEqualityGroup(
                    InternationalFixedDate.of(2014, 5, 27))
                .addEqualityGroup(
                    InternationalFixedDate.of(2014, 6, 26))
                .addEqualityGroup(
                    InternationalFixedDate.of(2015, 5, 26))
                .testEquals();
        }
    }
}