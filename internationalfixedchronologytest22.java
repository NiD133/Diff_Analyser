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
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("InternationalFixedChronology and InternationalFixedDate")
class InternationalFixedChronologyTest {

    private static InternationalFixedDate date(int year, int month, int day) {
        return InternationalFixedDate.of(year, month, day);
    }

    @Nested
    @DisplayName("Factory methods and validation")
    class FactoryAndValidationTests {

        static Stream<Arguments> invalidDateProvider() {
            return Stream.of(
                Arguments.of(-1, 13, 28),
                Arguments.of(-1, 13, 29),
                Arguments.of(0, 1, 1),
                Arguments.of(1900, -2, 1),
                Arguments.of(1900, 14, 1),
                Arguments.of(1900, 15, 1),
                Arguments.of(1900, 1, -1),
                Arguments.of(1900, 1, 0),
                Arguments.of(1900, 1, 29),
                Arguments.of(1904, -1, -2),
                Arguments.of(1904, -1, 0),
                Arguments.of(1904, -1, 1),
                Arguments.of(1900, 2, 29),
                Arguments.of(1900, 13, 30)
            );
        }

        @ParameterizedTest
        @MethodSource("invalidDateProvider")
        void of_withInvalidDateParts_throwsException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dom));
        }

        static Stream<Arguments> invalidLeapDayProvider() {
            return Stream.of(
                Arguments.of(1),
                Arguments.of(100),
                Arguments.of(200),
                Arguments.of(300),
                Arguments.of(1900)
            );
        }

        @ParameterizedTest
        @MethodSource("invalidLeapDayProvider")
        void of_withInvalidLeapDayInNonLeapYear_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }
    }

    @Nested
    @DisplayName("Conversion to and from other date types")
    class ConversionTests {

        static Stream<Arguments> sampleDatePairs() {
            return Stream.of(
                Arguments.of(date(1, 1, 1), LocalDate.of(1, 1, 1)),
                Arguments.of(date(1, 6, 27), LocalDate.of(1, 6, 16)),
                Arguments.of(date(1, 13, 29), LocalDate.of(1, 12, 31)),
                Arguments.of(date(2, 1, 1), LocalDate.of(2, 1, 1)),
                Arguments.of(date(4, 6, 29), LocalDate.of(4, 6, 17)),
                Arguments.of(date(2012, 6, 15), LocalDate.of(2012, 6, 3)),
                Arguments.of(date(2012, 6, 16), LocalDate.of(2012, 6, 4))
            );
        }

        @ParameterizedTest
        @MethodSource("sampleDatePairs")
        void shouldConvertFromInternationalFixedDateToLocalDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(ifcDate));
        }

        @ParameterizedTest
        @MethodSource("sampleDatePairs")
        void shouldConvertFromLocalDateToInternationalFixedDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("sampleDatePairs")
        void chronologyDateFromEpochDay_shouldMatchInternationalFixedDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("sampleDatePairs")
        void toEpochDay_shouldMatchLocalDateToEpochDay(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), ifcDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("sampleDatePairs")
        void chronologyDateFromTemporal_shouldMatchInternationalFixedDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Accessing fields and ranges")
    class FieldAndRangeTests {

        static Stream<Arguments> lengthOfMonthProvider() {
            return Stream.of(
                Arguments.of(1900, 1, 28),
                Arguments.of(1900, 6, 28),
                Arguments.of(1900, 13, 29),
                Arguments.of(1904, 6, 29)
            );
        }

        @ParameterizedTest
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, date(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> fieldRangeProvider() {
            // Test ranges for various fields, especially around leap days and year days
            return Stream.of(
                // For a leap day (June 29th in a leap year)
                Arguments.of(date(2012, 6, 29), DAY_OF_MONTH, ValueRange.of(1, 29)),
                Arguments.of(date(2012, 6, 29), ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(0, 0)),
                Arguments.of(date(2012, 6, 29), ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)),
                Arguments.of(date(2012, 6, 29), DAY_OF_WEEK, ValueRange.of(0, 0)),
                // For a year day (last day of 13th month)
                Arguments.of(date(2012, 13, 29), DAY_OF_MONTH, ValueRange.of(1, 29)),
                // For a standard day in a leap year
                Arguments.of(date(2012, 1, 23), DAY_OF_MONTH, ValueRange.of(1, 28)),
                Arguments.of(date(2012, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 366)),
                Arguments.of(date(2012, 1, 23), MONTH_OF_YEAR, ValueRange.of(1, 13)),
                Arguments.of(date(2012, 1, 23), ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)),
                // For a standard day in a non-leap year
                Arguments.of(date(2011, 6, 23), DAY_OF_MONTH, ValueRange.of(1, 28)),
                Arguments.of(date(2011, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 365))
            );
        }

        @ParameterizedTest
        @MethodSource("fieldRangeProvider")
        void range_forField_shouldReturnCorrectRange(InternationalFixedDate date, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, date.range(field));
        }

        static Stream<Arguments> fieldValueProvider() {
            return Stream.of(
                Arguments.of(date(2014, 5, 26), DAY_OF_WEEK, 5L),
                Arguments.of(date(2014, 5, 26), DAY_OF_MONTH, 26L),
                Arguments.of(date(2014, 5, 26), DAY_OF_YEAR, 138L), // 4 * 28 + 26
                Arguments.of(date(2014, 5, 26), ALIGNED_WEEK_OF_MONTH, 4L),
                Arguments.of(date(2014, 5, 26), ALIGNED_WEEK_OF_YEAR, 20L),
                Arguments.of(date(2014, 5, 26), MONTH_OF_YEAR, 5L),
                Arguments.of(date(2014, 5, 26), PROLEPTIC_MONTH, 26181L), // 2014 * 13 + 5 - 1
                Arguments.of(date(2014, 5, 26), YEAR, 2014L),
                Arguments.of(date(2014, 5, 26), ERA, 1L),
                // Leap year
                Arguments.of(date(2012, 9, 26), DAY_OF_YEAR, 253L), // 8 * 28 + 1 (leap day) + 26
                // Year day (non-leap)
                Arguments.of(date(2014, 13, 29), DAY_OF_WEEK, 0L),
                Arguments.of(date(2014, 13, 29), DAY_OF_YEAR, 365L), // 13 * 28 + 1
                Arguments.of(date(2014, 13, 29), ALIGNED_WEEK_OF_YEAR, 0L),
                // Leap day
                Arguments.of(date(2012, 6, 29), DAY_OF_WEEK, 0L),
                Arguments.of(date(2012, 6, 29), DAY_OF_YEAR, 169L), // 6 * 28 + 1
                Arguments.of(date(2012, 6, 29), ALIGNED_WEEK_OF_YEAR, 0L)
            );
        }

        @ParameterizedTest
        @MethodSource("fieldValueProvider")
        void getLong_forField_shouldReturnCorrectValue(InternationalFixedDate date, TemporalField field, long expected) {
            assertEquals(expected, date.getLong(field));
        }
    }

    @Nested
    @DisplayName("Chronology-specific methods")
    class ChronologySpecificTests {

        static Stream<Arguments> invalidEraValueProvider() {
            return Stream.of(Arguments.of(-1), Arguments.of(0), Arguments.of(2));
        }

        @ParameterizedTest
        @MethodSource("invalidEraValueProvider")
        void eraOf_withInvalidValue_throwsException(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        static Stream<Arguments> invalidProlepticYearProvider() {
            return Stream.of(Arguments.of(-10), Arguments.of(-1), Arguments.of(0));
        }

        @ParameterizedTest
        @MethodSource("invalidProlepticYearProvider")
        void prolepticYear_withInvalidYearOfEra_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }
    }

    @Nested
    @DisplayName("Date arithmetic")
    class ArithmeticTests {

        static Stream<Arguments> withFieldProvider() {
            return Stream.of(
                Arguments.of(date(2014, 5, 26), DAY_OF_WEEK, 1, date(2014, 5, 22)),
                Arguments.of(date(2014, 5, 26), DAY_OF_MONTH, 28, date(2014, 5, 28)),
                Arguments.of(date(2014, 5, 26), DAY_OF_YEAR, 364, date(2014, 13, 28)),
                Arguments.of(date(2014, 5, 26), ALIGNED_WEEK_OF_MONTH, 1, date(2014, 5, 5)),
                Arguments.of(date(2014, 5, 26), MONTH_OF_YEAR, 4, date(2014, 4, 26)),
                Arguments.of(date(2014, 5, 26), YEAR, 2012, date(2012, 5, 26)),
                // Adjusting from a year day
                Arguments.of(date(2014, 13, 29), DAY_OF_WEEK, 7, date(2014, 13, 28)),
                // Adjusting from a leap day
                Arguments.of(date(2012, 6, 29), DAY_OF_MONTH, 1, date(2012, 6, 1)),
                Arguments.of(date(2012, 6, 29), YEAR, 2013, date(2013, 6, 28)) // Becomes non-leap
            );
        }

        @ParameterizedTest
        @MethodSource("withFieldProvider")
        void with_fieldAndValue_returnsAdjustedDate(InternationalFixedDate baseDate, TemporalField field, long value, InternationalFixedDate expectedDate) {
            assertEquals(expectedDate, baseDate.with(field, value));
        }

        static Stream<Arguments> withInvalidFieldProvider() {
            return Stream.of(
                Arguments.of(2013, 1, 1, DAY_OF_WEEK, 0),
                Arguments.of(2013, 1, 1, DAY_OF_WEEK, 8),
                Arguments.of(2013, 1, 1, DAY_OF_MONTH, 29),
                Arguments.of(2012, 6, 1, DAY_OF_MONTH, 30),
                Arguments.of(2013, 1, 1, DAY_OF_YEAR, 366),
                Arguments.of(2012, 1, 1, DAY_OF_YEAR, 367),
                Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14),
                Arguments.of(2013, 1, 1, YEAR, 0)
            );
        }

        @ParameterizedTest
        @MethodSource("withInvalidFieldProvider")
        void with_invalidFieldValue_throwsException(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> date(year, month, dom).with(field, value));
        }

        static Stream<Arguments> lastDayOfMonthAdjusterProvider() {
            return Stream.of(
                Arguments.of(date(2012, 6, 23), date(2012, 6, 29)),
                Arguments.of(date(2009, 6, 23), date(2009, 6, 28)),
                Arguments.of(date(2007, 13, 23), date(2007, 13, 29))
            );
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthAdjusterProvider")
        void with_lastDayOfMonth_returnsLastDayOfMonth(InternationalFixedDate base, InternationalFixedDate expected) {
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> plusProvider() {
            return Stream.of(
                Arguments.of(date(2014, 5, 26), 0, DAYS, date(2014, 5, 26)),
                Arguments.of(date(2014, 5, 26), 8, DAYS, date(2014, 6, 6)),
                Arguments.of(date(2014, 5, 26), -3, DAYS, date(2014, 5, 23)),
                Arguments.of(date(2014, 5, 26), 3, WEEKS, date(2014, 6, 19)),
                Arguments.of(date(2014, 5, 26), 3, MONTHS, date(2014, 8, 26)),
                Arguments.of(date(2014, 5, 26), 3, YEARS, date(2017, 5, 26)),
                Arguments.of(date(2014, 5, 26), 3, DECADES, date(2044, 5, 26)),
                Arguments.of(date(2014, 5, 26), 3, CENTURIES, date(2314, 5, 26)),
                Arguments.of(date(2014, 5, 26), 3, MILLENNIA, date(5014, 5, 26)),
                // Year day
                Arguments.of(date(2014, 13, 29), 8, DAYS, date(2015, 1, 8)),
                Arguments.of(date(2014, 13, 29), 3, MONTHS, date(2015, 3, 28)),
                // Leap day
                Arguments.of(date(2012, 6, 29), 8, DAYS, date(2012, 7, 8)),
                Arguments.of(date(2012, 6, 29), 3, YEARS, date(2015, 6, 28))
            );
        }

        @ParameterizedTest
        @MethodSource("plusProvider")
        void plus_withAmountAndUnit_returnsCorrectDate(InternationalFixedDate base, long amount, TemporalUnit unit, InternationalFixedDate expected) {
            assertEquals(expected, base.plus(amount, unit));
        }

        static Stream<Arguments> minusProvider() {
            // Reverse of plusProvider
            return plusProvider().map(args -> {
                Object[] a = args.get();
                return Arguments.of(a[3], a[1], a[2], a[0]);
            });
        }

        @ParameterizedTest
        @MethodSource("minusProvider")
        void minus_withAmountAndUnit_returnsCorrectDate(InternationalFixedDate base, long amount, TemporalUnit unit, InternationalFixedDate expected) {
            assertEquals(expected, base.minus(amount, unit));
        }

        @Test
        void plus_unsupportedUnit_throwsException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> date(2012, 6, 28).plus(1, MINUTES));
        }
    }

    @Nested
    @DisplayName("Period and Duration calculations")
    class PeriodAndDurationTests {

        @Test
        void until_sameDate_returnsZeroPeriod() {
            InternationalFixedDate ifcDate = date(2012, 6, 15);
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(ifcDate));

            // Check against ISO date
            LocalDate isoDate = LocalDate.of(2012, 6, 3);
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(ifcDate));
        }

        static Stream<Arguments> untilUnitProvider() {
            return Stream.of(
                Arguments.of(date(2014, 5, 26), date(2014, 6, 4), DAYS, 6L),
                Arguments.of(date(2014, 5, 26), date(2014, 6, 5), WEEKS, 1L),
                Arguments.of(date(2014, 5, 26), date(2014, 6, 26), MONTHS, 1L),
                Arguments.of(date(2014, 5, 26), date(2015, 5, 26), YEARS, 1L),
                Arguments.of(date(2014, 5, 26), date(2024, 5, 26), DECADES, 1L),
                Arguments.of(date(2014, 5, 26), date(2114, 5, 26), CENTURIES, 1L),
                Arguments.of(date(2014, 5, 26), date(3014, 5, 26), MILLENNIA, 1L),
                Arguments.of(date(2014, 5, 26), date(3014, 5, 26), ERAS, 0L),
                // Across year/leap day boundaries
                Arguments.of(date(2014, 13, 28), date(2015, 1, 1), DAYS, 2L),
                Arguments.of(date(2012, 6, 28), date(2012, 7, 1), DAYS, 2L),
                Arguments.of(date(2012, 6, 29), date(2012, 13, 29), DAYS, 197L)
            );
        }

        @ParameterizedTest
        @MethodSource("untilUnitProvider")
        void until_withUnit_returnsCorrectAmount(InternationalFixedDate start, InternationalFixedDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodProvider() {
            return Stream.of(
                Arguments.of(date(2014, 5, 26), date(2014, 5, 26), InternationalFixedChronology.INSTANCE.period(0, 0, 0)),
                Arguments.of(date(2014, 5, 26), date(2014, 6, 4), InternationalFixedChronology.INSTANCE.period(0, 0, 6)),
                Arguments.of(date(2014, 5, 26), date(2014, 6, 26), InternationalFixedChronology.INSTANCE.period(0, 1, 0)),
                Arguments.of(date(2014, 5, 26), date(2015, 5, 26), InternationalFixedChronology.INSTANCE.period(1, 0, 0)),
                // Across leap year
                Arguments.of(date(2011, 13, 26), date(2012, 13, 26), InternationalFixedChronology.INSTANCE.period(1, 0, 0)),
                // From leap day
                Arguments.of(date(2004, 6, 29), date(2004, 13, 29), InternationalFixedChronology.INSTANCE.period(0, 7, 0))
            );
        }

        @ParameterizedTest
        @MethodSource("untilPeriodProvider")
        void until_withEndDate_returnsCorrectPeriod(InternationalFixedDate start, InternationalFixedDate end, ChronoPeriod expected) {
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Object method overrides")
    class ObjectMethodTests {

        static Stream<Arguments> toStringProvider() {
            return Stream.of(
                Arguments.of(date(1, 1, 1), "Ifc CE 1/01/01"),
                Arguments.of(date(2012, 6, 23), "Ifc CE 2012/06/23"),
                Arguments.of(date(1, 13, 29), "Ifc CE 1/13/29"),
                Arguments.of(date(2012, 6, 29), "Ifc CE 2012/06/29"),
                Arguments.of(date(2012, 13, 29), "Ifc CE 2012/13/29")
            );
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        void toString_shouldReturnFormattedString(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}