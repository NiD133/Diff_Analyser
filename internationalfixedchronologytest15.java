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
 * Tests for the {@link InternationalFixedDate}.
 * This class focuses on creation, validation, conversion, and manipulation of dates.
 */
public class InternationalFixedDateTest {

    private static InternationalFixedDate ifd(int year, int month, int day) {
        return InternationalFixedDate.of(year, month, day);
    }

    private static LocalDate iso(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    //-----------------------------------------------------------------------
    // Factory and Validation Tests
    //-----------------------------------------------------------------------
    @Nested
    class FactoryAndValidationTests {

        static Stream<Arguments> invalidDateComponents() {
            return Stream.of(
                Arguments.of(-1, 13, 28),
                Arguments.of(-1, 13, 29),
                Arguments.of(0, 1, 1),
                Arguments.of(1900, -2, 1),
                Arguments.of(1900, 14, 1),
                Arguments.of(1900, 15, 1),
                Arguments.of(1900, 1, -1),
                Arguments.of(1900, 1, 0),
                Arguments.of(1900, 1, 29), // Not a long month
                Arguments.of(1900, 2, 29),
                Arguments.of(1900, 13, 30), // Month 13 never has 30 days
                Arguments.of(1904, 6, 30) // Leap month 6 never has 30 days
            );
        }

        @ParameterizedTest
        @MethodSource("invalidDateComponents")
        void of_withInvalidDateParts_throwsException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        static Stream<Arguments> nonLeapYears() {
            return Stream.of(
                Arguments.of(1),
                Arguments.of(100),
                Arguments.of(1900),
                Arguments.of(2100)
            );
        }

        @ParameterizedTest
        @MethodSource("nonLeapYears")
        void of_forNonLeapYear_throwsExceptionForLeapDay(int year) {
            // Month 6 only has 29 days in a leap year.
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }

        @Test
        void eraOf_withInvalidValue_throwsException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(-1));
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(0));
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(2));
        }

        @Test
        void prolepticYear_withInvalidValue_throwsException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 0));
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, -1));
        }
    }

    //-----------------------------------------------------------------------
    // Conversion Tests
    //-----------------------------------------------------------------------
    @Nested
    class ConversionTests {

        static Stream<Arguments> sampleDatePairs() {
            return Stream.of(
                Arguments.of(ifd(1, 1, 1), iso(1, 1, 1)),
                Arguments.of(ifd(1, 6, 28), iso(1, 6, 17)),
                Arguments.of(ifd(1, 7, 1), iso(1, 6, 18)),
                Arguments.of(ifd(1, 13, 29), iso(1, 12, 31)),
                Arguments.of(ifd(4, 6, 29), iso(4, 6, 17)), // Leap year
                Arguments.of(ifd(4, 7, 1), iso(4, 6, 18)),
                Arguments.of(ifd(2012, 6, 15), iso(2012, 6, 3)),
                Arguments.of(ifd(2012, 6, 16), iso(2012, 6, 4))
            );
        }

        @ParameterizedTest
        @MethodSource("sampleDatePairs")
        void bidirectionalConversion_toAndFromLocalDate_isCorrect(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(fixedDate));
            assertEquals(fixedDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("sampleDatePairs")
        void epochDayConversion_isCorrect(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), fixedDate.toEpochDay());
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("sampleDatePairs")
        void chronologyDate_fromTemporalAccessor_isCorrect(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }
    }

    //-----------------------------------------------------------------------
    // Field Accessor Tests
    //-----------------------------------------------------------------------
    @Nested
    class FieldAccessorTests {

        static Stream<Arguments> monthLengths() {
            return Stream.of(
                // Non-leap year
                Arguments.of(ifd(1900, 1, 1), 28),
                Arguments.of(ifd(1900, 6, 1), 28),
                Arguments.of(ifd(1900, 13, 1), 29),
                // Leap year
                Arguments.of(ifd(2012, 1, 1), 28),
                Arguments.of(ifd(2012, 6, 1), 29), // Leap day month
                Arguments.of(ifd(2012, 13, 1), 29) // Year day month
            );
        }

        @ParameterizedTest
        @MethodSource("monthLengths")
        void lengthOfMonth_returnsCorrectValue(InternationalFixedDate date, int expectedLength) {
            assertEquals(expectedLength, date.lengthOfMonth());
        }

        static Stream<Arguments> fieldRanges() {
            return Stream.of(
                // --- Day of Month ---
                Arguments.of(ifd(2011, 1, 23), DAY_OF_MONTH, ValueRange.of(1, 28)),
                Arguments.of(ifd(2012, 6, 23), DAY_OF_MONTH, ValueRange.of(1, 29)), // Leap month
                Arguments.of(ifd(2012, 13, 23), DAY_OF_MONTH, ValueRange.of(1, 29)), // Year-day month

                // --- Day of Year ---
                Arguments.of(ifd(2011, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 365)),
                Arguments.of(ifd(2012, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 366)),

                // --- Month of Year ---
                Arguments.of(ifd(2011, 1, 23), MONTH_OF_YEAR, ValueRange.of(1, 13)),
                Arguments.of(ifd(2012, 1, 23), MONTH_OF_YEAR, ValueRange.of(1, 13)),

                // --- Aligned Week of Month ---
                Arguments.of(ifd(2012, 1, 23), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(ifd(2012, 6, 29), ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)), // Leap Day
                Arguments.of(ifd(2012, 13, 29), ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)) // Year Day
            );
        }

        @ParameterizedTest
        @MethodSource("fieldRanges")
        void range_forField_returnsCorrectValueRange(InternationalFixedDate date, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, date.range(field));
        }

        static Stream<Arguments> dateFieldValues() {
            return Stream.of(
                // --- Regular day in a non-leap year ---
                Arguments.of(ifd(2014, 5, 26), DAY_OF_WEEK, 5L),
                Arguments.of(ifd(2014, 5, 26), DAY_OF_MONTH, 26L),
                // DAY_OF_YEAR for 2014-05-26: 4 full months * 28 days + 26 days = 138
                Arguments.of(ifd(2014, 5, 26), DAY_OF_YEAR, 138L),
                Arguments.of(ifd(2014, 5, 26), ALIGNED_WEEK_OF_MONTH, 4L),
                // ALIGNED_WEEK_OF_YEAR: (138 - 1) / 7 + 1 = 19 + 1 = 20
                Arguments.of(ifd(2014, 5, 26), ALIGNED_WEEK_OF_YEAR, 20L),
                Arguments.of(ifd(2014, 5, 26), MONTH_OF_YEAR, 5L),
                // PROLEPTIC_MONTH: (2014 - 1) * 13 + 5 = 26174
                Arguments.of(ifd(2014, 5, 26), PROLEPTIC_MONTH, (2014L - 1) * 13 + 5),
                Arguments.of(ifd(2014, 5, 26), YEAR, 2014L),
                Arguments.of(ifd(2014, 5, 26), ERA, 1L),

                // --- Year Day (non-leap year) ---
                Arguments.of(ifd(2014, 13, 29), DAY_OF_WEEK, 0L),
                Arguments.of(ifd(2014, 13, 29), DAY_OF_MONTH, 29L),
                Arguments.of(ifd(2014, 13, 29), DAY_OF_YEAR, 365L),
                Arguments.of(ifd(2014, 13, 29), ALIGNED_WEEK_OF_YEAR, 0L),

                // --- Leap Day ---
                Arguments.of(ifd(2012, 6, 29), DAY_OF_WEEK, 0L),
                Arguments.of(ifd(2012, 6, 29), DAY_OF_MONTH, 29L),
                // DAY_OF_YEAR for 2012-06-29: 6 * 28 + 1 = 169
                Arguments.of(ifd(2012, 6, 29), DAY_OF_YEAR, 169L),
                Arguments.of(ifd(2012, 6, 29), ALIGNED_WEEK_OF_YEAR, 0L),

                // --- Day after Leap Day ---
                Arguments.of(ifd(2012, 7, 1), DAY_OF_WEEK, 1L),
                Arguments.of(ifd(2012, 7, 1), DAY_OF_MONTH, 1L),
                // DAY_OF_YEAR for 2012-07-01: 6 * 28 + 2 = 170
                Arguments.of(ifd(2012, 7, 1), DAY_OF_YEAR, 170L),
                // ALIGNED_WEEK_OF_YEAR: (170 - 1 - 1 leap day) / 7 + 1 = 168 / 7 + 1 = 24 + 1 = 25
                Arguments.of(ifd(2012, 7, 1), ALIGNED_WEEK_OF_YEAR, 25L)
            );
        }

        @ParameterizedTest
        @MethodSource("dateFieldValues")
        void getLong_forField_returnsCorrectValue(InternationalFixedDate date, TemporalField field, long expected) {
            assertEquals(expected, date.getLong(field));
        }

        static Stream<Arguments> dateWithFieldValues() {
            return Stream.of(
                // --- DAY_OF_WEEK ---
                Arguments.of(ifd(2014, 5, 26), DAY_OF_WEEK, 1, ifd(2014, 5, 22)),
                // --- DAY_OF_MONTH ---
                Arguments.of(ifd(2014, 5, 26), DAY_OF_MONTH, 28, ifd(2014, 5, 28)),
                // --- DAY_OF_YEAR ---
                Arguments.of(ifd(2014, 5, 26), DAY_OF_YEAR, 365, ifd(2014, 13, 29)),
                Arguments.of(ifd(2012, 3, 28), DAY_OF_YEAR, 366, ifd(2012, 13, 29)),
                // --- MONTH_OF_YEAR ---
                Arguments.of(ifd(2014, 5, 26), MONTH_OF_YEAR, 4, ifd(2014, 4, 26)),
                // --- YEAR ---
                Arguments.of(ifd(2014, 5, 26), YEAR, 2012, ifd(2012, 5, 26)),
                // --- Leap day handling ---
                Arguments.of(ifd(2012, 6, 29), YEAR, 2013, ifd(2013, 6, 28)), // Adjusts to last valid day
                Arguments.of(ifd(2013, 6, 28), YEAR, 2012, ifd(2012, 6, 28))
            );
        }

        @ParameterizedTest
        @MethodSource("dateWithFieldValues")
        void with_forField_returnsAdjustedDate(InternationalFixedDate baseDate, TemporalField field, long value, InternationalFixedDate expectedDate) {
            assertEquals(expectedDate, baseDate.with(field, value));
        }

        static Stream<Arguments> dateWithInvalidFieldValues() {
            return Stream.of(
                Arguments.of(ifd(2013, 1, 1), DAY_OF_MONTH, 29),
                Arguments.of(ifd(2013, 6, 1), DAY_OF_MONTH, 29),
                Arguments.of(ifd(2012, 6, 1), DAY_OF_MONTH, 30),
                Arguments.of(ifd(2013, 1, 1), DAY_OF_YEAR, 366),
                Arguments.of(ifd(2012, 1, 1), DAY_OF_YEAR, 367),
                Arguments.of(ifd(2013, 1, 1), MONTH_OF_YEAR, 14),
                Arguments.of(ifd(2013, 1, 1), YEAR, 0)
            );
        }

        @ParameterizedTest
        @MethodSource("dateWithInvalidFieldValues")
        void with_forFieldWithInvalidValue_throwsException(InternationalFixedDate date, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> date.with(field, value));
        }

        static Stream<Arguments> datesForLastDayOfMonthAdjustment() {
            return Stream.of(
                Arguments.of(ifd(2012, 6, 23), ifd(2012, 6, 29)),
                Arguments.of(ifd(2012, 6, 29), ifd(2012, 6, 29)),
                Arguments.of(ifd(2009, 6, 23), ifd(2009, 6, 28)),
                Arguments.of(ifd(2007, 13, 23), ifd(2007, 13, 29))
            );
        }

        @ParameterizedTest
        @MethodSource("datesForLastDayOfMonthAdjustment")
        void with_lastDayOfMonth_adjustsCorrectly(InternationalFixedDate base, InternationalFixedDate expected) {
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void range_forUnsupportedField_throwsException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> ifd(2012, 6, 28).range(MINUTE_OF_DAY));
        }
    }

    //-----------------------------------------------------------------------
    // Arithmetic Tests
    //-----------------------------------------------------------------------
    @Nested
    class ArithmeticTests {

        static Stream<Arguments> datePlusAmountProvider() {
            return Stream.of(
                // --- By Days ---
                Arguments.of(ifd(2014, 5, 26), 8, DAYS, ifd(2014, 6, 6)),
                Arguments.of(ifd(2014, 5, 26), -3, DAYS, ifd(2014, 5, 23)),
                // --- By Weeks ---
                Arguments.of(ifd(2014, 5, 26), 3, WEEKS, ifd(2014, 6, 19)),
                Arguments.of(ifd(2014, 5, 26), -5, WEEKS, ifd(2014, 4, 19)),
                // --- By Months ---
                Arguments.of(ifd(2014, 5, 26), 3, MONTHS, ifd(2014, 8, 26)),
                Arguments.of(ifd(2014, 5, 26), -5, MONTHS, ifd(2013, 13, 26)),
                // --- By Years ---
                Arguments.of(ifd(2014, 5, 26), 3, YEARS, ifd(2017, 5, 26)),
                Arguments.of(ifd(2014, 5, 26), -5, YEARS, ifd(2009, 5, 26)),
                // --- By Larger Units ---
                Arguments.of(ifd(2014, 5, 26), 3, DECADES, ifd(2044, 5, 26)),
                Arguments.of(ifd(2014, 5, 26), 3, CENTURIES, ifd(2314, 5, 26)),
                Arguments.of(ifd(2014, 5, 26), -1, MILLENNIA, ifd(1014, 5, 26)),
                // --- Leap Day and Year Day Handling ---
                Arguments.of(ifd(2012, 6, 29), 8, DAYS, ifd(2012, 7, 8)), // Add from leap day
                Arguments.of(ifd(2014, 13, 29), 8, DAYS, ifd(2015, 1, 8)), // Add from year day
                Arguments.of(ifd(2012, 6, 29), 3, MONTHS, ifd(2012, 9, 28)), // Add months from leap day, lands on last day
                Arguments.of(ifd(2012, 6, 29), 4, YEARS, ifd(2016, 6, 29)) // Add years from leap day, lands on leap day
            );
        }

        @ParameterizedTest
        @MethodSource("datePlusAmountProvider")
        void plus_withUnit_returnsCorrectDate(InternationalFixedDate base, long amount, TemporalUnit unit, InternationalFixedDate expected) {
            assertEquals(expected, base.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("datePlusAmountProvider")
        void minus_withUnit_returnsCorrectDate(InternationalFixedDate expected, long amount, TemporalUnit unit, InternationalFixedDate base) {
            assertEquals(expected, base.minus(amount, unit));
        }
    }

    //-----------------------------------------------------------------------
    // Period and Until Tests
    //-----------------------------------------------------------------------
    @Nested
    class PeriodUntilTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest$ConversionTests#sampleDatePairs")
        void until_forSameDateInDifferentChronologies_isZero(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(fixedDate));
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(fixedDate));
        }

        static Stream<Arguments> dateUntilAmountProvider() {
            return Stream.of(
                // --- Days ---
                Arguments.of(ifd(2014, 5, 26), ifd(2014, 6, 4), DAYS, 6L),
                Arguments.of(ifd(2014, 5, 26), ifd(2014, 5, 20), DAYS, -6L),
                // --- Weeks ---
                Arguments.of(ifd(2014, 5, 26), ifd(2014, 6, 5), WEEKS, 1L),
                Arguments.of(ifd(2014, 5, 26), ifd(2014, 5, 20), WEEKS, 0L),
                // --- Months ---
                Arguments.of(ifd(2014, 5, 26), ifd(2014, 6, 26), MONTHS, 1L),
                Arguments.of(ifd(2014, 5, 26), ifd(2014, 6, 25), MONTHS, 0L),
                // --- Years ---
                Arguments.of(ifd(2014, 5, 26), ifd(2015, 5, 26), YEARS, 1L),
                Arguments.of(ifd(2014, 5, 26), ifd(2015, 5, 25), YEARS, 0L),
                // --- Leap Day and Year Day Handling ---
                Arguments.of(ifd(2012, 6, 28), ifd(2012, 7, 1), DAYS, 2L), // Crosses leap day
                Arguments.of(ifd(2014, 13, 28), ifd(2015, 1, 1), DAYS, 2L), // Crosses year day
                Arguments.of(ifd(2012, 6, 29), ifd(2012, 13, 29), MONTHS, 7L), // From leap day to year day
                Arguments.of(ifd(2012, 6, 29), ifd(2016, 6, 29), YEARS, 4L) // Leap to leap
            );
        }

        @ParameterizedTest
        @MethodSource("dateUntilAmountProvider")
        void until_withUnit_calculatesDifferenceCorrectly(InternationalFixedDate start, InternationalFixedDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> dateUntilPeriodProvider() {
            return Stream.of(
                // --- Simple cases ---
                Arguments.of(ifd(2014, 5, 26), ifd(2014, 5, 26), InternationalFixedChronology.INSTANCE.period(0, 0, 0)),
                Arguments.of(ifd(2014, 5, 26), ifd(2014, 6, 4), InternationalFixedChronology.INSTANCE.period(0, 0, 6)),
                Arguments.of(ifd(2014, 5, 26), ifd(2014, 6, 26), InternationalFixedChronology.INSTANCE.period(0, 1, 0)),
                Arguments.of(ifd(2014, 5, 26), ifd(2015, 5, 26), InternationalFixedChronology.INSTANCE.period(1, 0, 0)),
                // --- Across leap year ---
                Arguments.of(ifd(2011, 12, 28), ifd(2012, 13, 1), InternationalFixedChronology.INSTANCE.period(1, 0, 1)),
                // --- From Year Day ---
                Arguments.of(ifd(2003, 13, 29), ifd(2004, 6, 29), InternationalFixedChronology.INSTANCE.period(0, 6, 0)),
                // --- From Leap Day ---
                Arguments.of(ifd(2004, 6, 29), ifd(2004, 13, 29), InternationalFixedChronology.INSTANCE.period(0, 7, 0))
            );
        }

        @ParameterizedTest
        @MethodSource("dateUntilPeriodProvider")
        void until_period_calculatesDifferenceCorrectly(InternationalFixedDate start, InternationalFixedDate end, ChronoPeriod expected) {
            assertEquals(expected, start.until(end));
        }
    }

    //-----------------------------------------------------------------------
    // Miscellaneous Tests
    //-----------------------------------------------------------------------
    @Nested
    class MiscellaneousTests {

        static Stream<Arguments> dateToStringProvider() {
            return Stream.of(
                Arguments.of(ifd(1, 1, 1), "Ifc CE 1/01/01"),
                Arguments.of(ifd(2012, 6, 23), "Ifc CE 2012/06/23"),
                Arguments.of(ifd(1, 13, 29), "Ifc CE 1/13/29"),
                Arguments.of(ifd(2012, 6, 29), "Ifc CE 2012/06/29"), // Leap Day
                Arguments.of(ifd(2012, 13, 29), "Ifc CE 2012/13/29") // Year Day
            );
        }

        @ParameterizedTest
        @MethodSource("dateToStringProvider")
        void toString_returnsCorrectlyFormattedString(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}