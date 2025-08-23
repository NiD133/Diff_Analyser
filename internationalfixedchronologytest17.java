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
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link InternationalFixedDate} and its integration with {@link InternationalFixedChronology}.
 */
public class InternationalFixedDateTest {

    static Stream<Arguments> provideDateSamples() {
        return Stream.of(
            Arguments.of(InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)),
            Arguments.of(InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)),
            Arguments.of(InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)),
            Arguments.of(InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)), // Leap year
            Arguments.of(InternationalFixedDate.of(4, 7, 1), LocalDate.of(4, 6, 18)),
            Arguments.of(InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)),
            Arguments.of(InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4))
        );
    }

    @Nested
    class ConversionTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        void fromFixedDate_shouldConvertToCorrectLocalDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(fixedDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        void fromLocalDate_shouldConvertToCorrectFixedDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        void chronologyDateFromEpochDay_shouldCreateCorrectFixedDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        void toEpochDay_shouldReturnCorrectValue(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), fixedDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        void chronologyDateFromTemporal_shouldCreateCorrectFixedDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    class FactoryAndValidationTests {
        static Stream<Arguments> provideInvalidDateParts() {
            return Stream.of(
                Arguments.of(0, 1, 1),      // Invalid year
                Arguments.of(1900, 0, 1),     // Invalid month
                Arguments.of(1900, 14, 1),    // Invalid month
                Arguments.of(1900, 1, 0),      // Invalid day
                Arguments.of(1900, 1, 29),    // Invalid day for a 28-day month
                Arguments.of(1900, 13, 30),   // Invalid day for the 29-day Year Day month
                Arguments.of(1900, 2, 29)     // Invalid day for a 28-day month
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidDateParts")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        static Stream<Arguments> provideNonLeapYears() {
            return Stream.of(
                Arguments.of(1), Arguments.of(100), Arguments.of(1900)
            );
        }

        @ParameterizedTest
        @MethodSource("provideNonLeapYears")
        void of_withInvalidLeapDay_shouldThrowException(int year) {
            // Month 6 (June) only has 29 days in a leap year.
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }

        @Test
        void eraOf_withInvalidValue_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(0));
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(2));
        }

        @Test
        void prolepticYear_withInvalidYearOfEra_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 0));
        }
    }

    @Nested
    class PropertyTests {
        static Stream<Arguments> provideDateAndExpectedMonthLength() {
            return Stream.of(
                Arguments.of(1900, 1, 28, 28),
                Arguments.of(1900, 13, 29, 29), // Year Day month
                Arguments.of(1904, 6, 29, 29)   // Leap Day month in a leap year
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndExpectedMonthLength")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, day).lengthOfMonth());
        }

        static Stream<Arguments> provideDateAndStringRepresentation() {
            return Stream.of(
                Arguments.of(InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"),
                Arguments.of(InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"),
                Arguments.of(InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"), // Leap Day
                Arguments.of(2012, 13, 29, "Ifc CE 2012/13/29") // Year Day
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndStringRepresentation")
        void toString_shouldReturnFormattedDate(Object date, String expected) {
            if (date instanceof InternationalFixedDate) {
                assertEquals(expected, date.toString());
            } else {
                assertEquals(expected, InternationalFixedDate.of((int) date, (int) date, (int) date).toString());
            }
        }
    }

    @Nested
    class FieldAccessTests {
        static Stream<Arguments> provideFieldRanges() {
            return Stream.of(
                // For a leap year (2012)
                Arguments.of(2012, 6, 29, DAY_OF_MONTH, ValueRange.of(1, 29)),
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366)),
                Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)),
                // For a common year (2011)
                Arguments.of(2011, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 28)),
                Arguments.of(2011, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 365))
            );
        }

        @ParameterizedTest
        @MethodSource("provideFieldRanges")
        void range_forField_shouldReturnCorrectValueRange(int year, int month, int day, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, InternationalFixedDate.of(year, month, day).range(field));
        }

        static Stream<Arguments> provideFieldValues() {
            return Stream.of(
                // Date: 2014-05-26 (common year)
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5L),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                // DAY_OF_YEAR: 4 months * 28 days + 26 days = 138
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 4 * 28 + 26L),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),

                // Date: 2012-09-26 (leap year)
                Arguments.of(2012, 9, 26, DAY_OF_WEEK, 5L),
                // DAY_OF_YEAR: 8 months * 28 days + 1 leap day + 26 days = 251
                Arguments.of(2012, 9, 26, DAY_OF_YEAR, 8 * 28 + 1 + 26L),

                // Date: 2014-13-29 (Year Day)
                Arguments.of(2014, 13, 29, DAY_OF_WEEK, 0L), // Year day is not part of a week
                Arguments.of(2014, 13, 29, DAY_OF_MONTH, 29L),
                Arguments.of(2014, 13, 29, DAY_OF_YEAR, 12 * 28 + 28 + 1L), // 365

                // Date: 2012-06-29 (Leap Day)
                Arguments.of(2012, 6, 29, DAY_OF_WEEK, 0L), // Leap day is not part of a week
                Arguments.of(2012, 6, 29, DAY_OF_MONTH, 29L),
                Arguments.of(2012, 6, 29, DAY_OF_YEAR, 6 * 28 + 1L)
            );
        }

        @ParameterizedTest
        @MethodSource("provideFieldValues")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, InternationalFixedDate.of(year, month, day).getLong(field));
        }

        static Stream<Arguments> provideWithFieldAdjustments() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365, 2014, 13, 29),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                // Adjusting a leap day to a non-leap year results in the previous day
                Arguments.of(2012, 6, 29, YEAR, 2013, 2013, 6, 28)
            );
        }

        @ParameterizedTest
        @MethodSource("provideWithFieldAdjustments")
        void with_forField_shouldReturnAdjustedDate(int year, int month, int day, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDay) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, day);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.with(field, value));
        }

        @Test
        void with_forUnsupportedField_shouldThrowException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> InternationalFixedDate.of(2012, 6, 28).with(MINUTE_OF_DAY, 0));
        }
    }

    @Nested
    class ArithmeticTests {
        static Stream<Arguments> providePlusAdjustments() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26),
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
        @MethodSource("providePlusAdjustments")
        void plus_withUnit_shouldReturnCorrectlyAdvancedDate(int year, int month, int day, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDay) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, day);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("providePlusAdjustments")
        void minus_withUnit_shouldReturnCorrectlyRetreatedDate(int expectedYear, int expectedMonth, int expectedDay, long amount, TemporalUnit unit, int year, int month, int day) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, day);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Stream<Arguments> provideUntilDurations() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0L),
                Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 6L),
                Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6L),
                Arguments.of(2014, 5, 26, 2014, 6, 5, WEEKS, 1L),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, ERAS, 0L),
                // Spanning Year Day
                Arguments.of(2014, 13, 28, 2015, 1, 1, DAYS, 2L),
                // Spanning Leap Day
                Arguments.of(2012, 6, 28, 2012, 7, 1, DAYS, 2L)
            );
        }

        @ParameterizedTest
        @MethodSource("provideUntilDurations")
        void until_withUnit_shouldCalculateCorrectDuration(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> provideUntilPeriods() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 6),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                // Spanning leap day
                Arguments.of(2011, 12, 28, 2012, 13, 1, 1, 0, 1)
            );
        }

        @ParameterizedTest
        @MethodSource("provideUntilPeriods")
        void until_withEndDate_shouldCalculateCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    class InterChronologyUntilTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        void until_sameDate_returnsZeroPeriod(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(fixedDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        void until_isoDate_returnsZeroPeriodForSameDay(InternationalFixedDate fixedDate, LocalDate isoDate) {
            // until() between different chronologies is specified to work if the other is an ISO date.
            // It should represent the period between two equivalent points in time.
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#provideDateSamples")
        void until_fromIsoDate_returnsZeroPeriodForSameDay(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(fixedDate));
        }
    }

    @Nested
    class TemporalAdjusterTests {
        static Stream<Arguments> provideDatesForLastDayOfMonthAdjustment() {
            return Stream.of(
                Arguments.of(2012, 6, 23, 2012, 6, 29), // Leap month
                Arguments.of(2009, 6, 23, 2009, 6, 28), // Common month
                Arguments.of(2007, 13, 23, 2007, 13, 29) // Year day month
            );
        }

        @ParameterizedTest
        @MethodSource("provideDatesForLastDayOfMonthAdjustment")
        void with_lastDayOfMonth_shouldReturnLastDayOfThatMonth(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, day);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }
}