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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link InternationalFixedChronology} and {@link InternationalFixedDate}.
 */
@DisplayName("InternationalFixedChronology and InternationalFixedDate")
class InternationalFixedChronologyTest {

    //-----------------------------------------------------------------------
    // Factory Methods
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @Test
        @DisplayName("of(year, month, day) creates a date")
        void of_createsDate() {
            InternationalFixedDate date = InternationalFixedDate.of(2012, 6, 29);
            assertEquals(2012, date.getYear());
            assertEquals(6, date.getMonthValue());
            assertEquals(29, date.getDayOfMonth());
        }

        static Stream<Arguments> invalidDateComponents() {
            return Stream.of(
                Arguments.of(0, 1, 1, "year must be 1 or greater"),
                Arguments.of(1900, 0, 1, "month must be between 1 and 13"),
                Arguments.of(1900, 14, 1, "month must be between 1 and 13"),
                Arguments.of(1900, 1, 0, "day of month must be positive"),
                Arguments.of(1900, 1, 29, "day of month 29 is invalid for month 1"),
                Arguments.of(1900, 13, 30, "day of month 30 is invalid for month 13 in a non-leap year")
            );
        }

        @ParameterizedTest(name = "year={0}, month={1}, day={2}")
        @MethodSource("invalidDateComponents")
        @DisplayName("of(year, month, day) throws for invalid components")
        void of_throwsForInvalidDate(int year, int month, int day, String message) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        static Stream<Arguments> nonLeapYears() {
            return Stream.of(
                Arguments.of(1), Arguments.of(100), Arguments.of(1900)
            );
        }

        @ParameterizedTest(name = "year={0}")
        @MethodSource("nonLeapYears")
        @DisplayName("of(year, 6, 29) throws for non-leap years")
        void of_throwsForLeapDayInNonLeapYear(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }
    }

    //-----------------------------------------------------------------------
    // Chronology API
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Chronology API")
    class ChronologyApiTest {

        static Stream<Arguments> invalidEraValues() {
            return Stream.of(Arguments.of(-1), Arguments.of(0), Arguments.of(2));
        }

        @ParameterizedTest(name = "era={0}")
        @MethodSource("invalidEraValues")
        @DisplayName("eraOf() throws for invalid era value")
        void eraOf_throwsForInvalidEra(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        static Stream<Arguments> invalidProlepticYears() {
            return Stream.of(Arguments.of(-10), Arguments.of(-1), Arguments.of(0));
        }

        @ParameterizedTest(name = "year={0}")
        @MethodSource("invalidProlepticYears")
        @DisplayName("prolepticYear() throws for invalid year of era")
        void prolepticYear_throwsForInvalidYear(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }
    }

    //-----------------------------------------------------------------------
    // Conversions
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversions to/from other date systems")
    class ConversionTests {

        static Stream<Arguments> sampleFixedAndIsoDates() {
            return Stream.of(
                // Year boundaries
                Arguments.of(InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)),
                Arguments.of(InternationalFixedDate.of(2, 1, 1), LocalDate.of(2, 1, 1)),
                // Non-leap year dates
                Arguments.of(InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)),
                Arguments.of(InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)),
                Arguments.of(InternationalFixedDate.of(1, 13, 28), LocalDate.of(1, 12, 30)),
                Arguments.of(InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)), // Year Day
                // Leap year dates (year 4 is a leap year)
                Arguments.of(InternationalFixedDate.of(4, 6, 28), LocalDate.of(4, 6, 16)),
                Arguments.of(InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)), // Leap Day
                Arguments.of(InternationalFixedDate.of(4, 7, 1), LocalDate.of(4, 6, 18)),
                Arguments.of(InternationalFixedDate.of(4, 13, 29), LocalDate.of(4, 12, 31)), // Year Day
                // Modern dates
                Arguments.of(InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)),
                Arguments.of(InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4))
            );
        }

        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("sampleFixedAndIsoDates")
        @DisplayName("converts from InternationalFixedDate to LocalDate")
        void toLocalDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso, LocalDate.from(fixed));
        }

        @ParameterizedTest(name = "{1} -> {0}")
        @MethodSource("sampleFixedAndIsoDates")
        @DisplayName("converts from LocalDate to InternationalFixedDate")
        void fromLocalDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedDate.from(iso));
        }

        @ParameterizedTest(name = "epochDay={1}")
        @MethodSource("sampleFixedAndIsoDates")
        @DisplayName("converts from epoch day to InternationalFixedDate")
        void fromEpochDay(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("sampleFixedAndIsoDates")
        @DisplayName("converts from InternationalFixedDate to epoch day")
        void toEpochDay(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso.toEpochDay(), fixed.toEpochDay());
        }

        @ParameterizedTest(name = "{1}")
        @MethodSource("sampleFixedAndIsoDates")
        @DisplayName("creates InternationalFixedDate from a TemporalAccessor (LocalDate)")
        void fromTemporal(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedChronology.INSTANCE.date(iso));
        }
    }

    //-----------------------------------------------------------------------
    // Field Access and Ranges
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field access and ranges")
    class FieldAccessTest {

        static Stream<Arguments> monthLengths() {
            return Stream.of(
                Arguments.of(1900, 1, 28, "Non-leap year, standard month"),
                Arguments.of(1900, 6, 28, "Non-leap year, 'Sol' month"),
                Arguments.of(1900, 13, 29, "Non-leap year, year-end month"),
                Arguments.of(1904, 6, 29, "Leap year, 'Sol' month with leap day")
            );
        }

        @ParameterizedTest(name = "{3}: {0}-{1} has {2} days")
        @MethodSource("monthLengths")
        @DisplayName("lengthOfMonth() returns correct day count")
        void lengthOfMonth_returnsCorrectDayCount(int year, int month, int expectedLength, String description) {
            InternationalFixedDate date = InternationalFixedDate.of(year, month, 1);
            assertEquals(expectedLength, date.lengthOfMonth());
        }

        static Stream<Arguments> fieldRanges() {
            return Stream.of(
                // DAY_OF_MONTH ranges
                Arguments.of(2012, 6, 29, DAY_OF_MONTH, ValueRange.of(1, 29), "Leap day month"),
                Arguments.of(2012, 13, 29, DAY_OF_MONTH, ValueRange.of(1, 29), "Year day month"),
                Arguments.of(2011, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28), "Standard month"),
                // DAY_OF_YEAR ranges
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366), "Leap year"),
                Arguments.of(2011, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 365), "Non-leap year"),
                // MONTH_OF_YEAR range
                Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13), "Always 13 months"),
                // ALIGNED_WEEK_OF_MONTH ranges for special days (Leap/Year day)
                Arguments.of(2012, 6, 29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0), "Leap day is not in a week"),
                Arguments.of(2012, 13, 29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0), "Year day is not in a week"),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4), "Standard day has 4 weeks in month")
            );
        }

        @ParameterizedTest(name = "{4}: {0}-{1}-{2} field {3} has range {4}")
        @MethodSource("fieldRanges")
        @DisplayName("range() returns correct range for a given field")
        void range_returnsCorrectRangeForField(int year, int month, int dom, TemporalField field, ValueRange expected, String description) {
            assertEquals(expected, InternationalFixedDate.of(year, month, dom).range(field));
        }

        static Stream<Arguments> fieldValues() {
            // PROLEPTIC_MONTH is 0-based: (year - 1) * 13 + (month - 1)
            // DAY_OF_YEAR: (month - 1) * 28 + dayOfMonth (+1 if leap year and month > 6)
            return Stream.of(
                // Date: 2014-05-26 (non-leap)
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5L),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, (long) (4 * 28 + 26)),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 20L),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, (2014L - 1) * 13 + (5 - 1)),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),
                // Date: 2012-06-29 (Leap Day)
                Arguments.of(2012, 6, 29, DAY_OF_WEEK, 0L), // Not part of a week
                Arguments.of(2012, 6, 29, DAY_OF_MONTH, 29L),
                Arguments.of(2012, 6, 29, DAY_OF_YEAR, (long) (6 * 28 + 1)),
                Arguments.of(2012, 6, 29, MONTH_OF_YEAR, 6L),
                Arguments.of(2012, 6, 29, PROLEPTIC_MONTH, (2012L - 1) * 13 + (6 - 1)),
                // Date: 2014-13-29 (Year Day)
                Arguments.of(2014, 13, 29, DAY_OF_WEEK, 0L), // Not part of a week
                Arguments.of(2014, 13, 29, DAY_OF_MONTH, 29L),
                Arguments.of(2014, 13, 29, DAY_OF_YEAR, (long) (13 * 28 + 1)),
                Arguments.of(2014, 13, 29, MONTH_OF_YEAR, 13L),
                Arguments.of(2014, 13, 29, PROLEPTIC_MONTH, (2014L - 1) * 13 + (13 - 1))
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2}, field {3} is {4}")
        @MethodSource("fieldValues")
        @DisplayName("getLong() returns correct value for a given field")
        void getLong_returnsCorrectValueForField(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, InternationalFixedDate.of(year, month, dom).getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    // Date Manipulation (with, plus, minus, until)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date manipulation")
    class DateManipulationTest {

        static Stream<Arguments> dateAdjustments() {
            // PROLEPTIC_MONTH is 0-based: (year - 1) * 13 + (month - 1)
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, InternationalFixedDate.of(2014, 5, 22)),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 1, InternationalFixedDate.of(2014, 5, 1)),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365, InternationalFixedDate.of(2014, 13, 29)),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 1, InternationalFixedDate.of(2014, 1, 26)),
                Arguments.of(2014, 5, 26, YEAR, 2012, InternationalFixedDate.of(2012, 5, 26)),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, (2013L - 1) * 13 + (3 - 1), InternationalFixedDate.of(2013, 3, 26)),
                // Adjusting a leap day to a non-leap year
                Arguments.of(2012, 6, 29, YEAR, 2013, InternationalFixedDate.of(2013, 6, 28))
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) -> {5}")
        @MethodSource("dateAdjustments")
        @DisplayName("with(field, value) returns an adjusted date")
        void with_returnsAdjustedDate(int y, int m, int d, TemporalField field, long value, InternationalFixedDate expected) {
            assertEquals(expected, InternationalFixedDate.of(y, m, d).with(field, value));
        }

        static Stream<Arguments> invalidDateAdjustments() {
            return Stream.of(
                Arguments.of(2013, 1, 1, DAY_OF_MONTH, 29, "Invalid day for month"),
                Arguments.of(2013, 1, 1, DAY_OF_YEAR, 366, "Invalid day for non-leap year"),
                Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14, "Invalid month")
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) throws")
        @MethodSource("invalidDateAdjustments")
        @DisplayName("with(field, value) throws for invalid values")
        void with_throwsForInvalidValue(int y, int m, int d, TemporalField field, long value, String description) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(y, m, d).with(field, value));
        }

        @Test
        @DisplayName("with(TemporalAdjuster) adjusts the date")
        void with_adjuster() {
            InternationalFixedDate date = InternationalFixedDate.of(2012, 6, 23);
            InternationalFixedDate expected = InternationalFixedDate.of(2012, 6, 29);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> plusAmounts() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 8, DAYS, InternationalFixedDate.of(2014, 6, 6)),
                Arguments.of(2014, 5, 26, 3, WEEKS, InternationalFixedDate.of(2014, 6, 19)),
                Arguments.of(2014, 5, 26, 3, MONTHS, InternationalFixedDate.of(2014, 8, 26)),
                Arguments.of(2014, 5, 26, 3, YEARS, InternationalFixedDate.of(2017, 5, 26)),
                Arguments.of(2014, 5, 26, 3, DECADES, InternationalFixedDate.of(2044, 5, 26)),
                // Handling leap day when adding years
                Arguments.of(2012, 6, 29, 1, YEARS, InternationalFixedDate.of(2013, 6, 28)),
                Arguments.of(2012, 6, 29, 4, YEARS, InternationalFixedDate.of(2016, 6, 29))
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus({3}, {4}) -> {5}")
        @MethodSource("plusAmounts")
        @DisplayName("plus(amount, unit) adds the specified amount")
        void plus_addsAmount(int y, int m, int d, long amount, TemporalUnit unit, InternationalFixedDate expected) {
            assertEquals(expected, InternationalFixedDate.of(y, m, d).plus(amount, unit));
        }

        @ParameterizedTest(name = "{5} minus({3}, {4}) -> {0}-{1}-{2}")
        @MethodSource("plusAmounts")
        @DisplayName("minus(amount, unit) subtracts the specified amount")
        void minus_subtractsAmount(int y, int m, int d, long amount, TemporalUnit unit, InternationalFixedDate start) {
            assertEquals(InternationalFixedDate.of(y, m, d), start.minus(amount, unit));
        }

        @Test
        @DisplayName("minus(Period) throws for non-IFC period")
        void minus_throwsForIsoPeriod() {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(2014, 5, 26).minus(Period.ofMonths(2)));
        }

        static Stream<Arguments> untilAmounts() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 6L),
                Arguments.of(2014, 5, 26, 2014, 6, 5, WEEKS, 1L),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
                // Across leap/year days
                Arguments.of(2012, 6, 28, 2012, 7, 1, DAYS, 2L),
                Arguments.of(2012, 6, 29, 2012, 13, 29, MONTHS, 7L)
            );
        }

        @ParameterizedTest(name = "until({2}-{3}-{4}) in {5} is {6}")
        @MethodSource("untilAmounts")
        @DisplayName("until(endDate, unit) calculates the amount of time between dates")
        void until_calculatesAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodAmounts() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 6),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                // Complex period involving leap/year days
                Arguments.of(2004, 6, 29, 2004, 13, 28, 0, 6, 28)
            );
        }

        @ParameterizedTest(name = "until({2}-{3}-{4}) is P{5}Y{6}M{7}D")
        @MethodSource("untilPeriodAmounts")
        @DisplayName("until(endDate) calculates the period between dates")
        void until_calculatesPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }

        @Test
        @DisplayName("until(same date) returns a zero period")
        void until_sameDate_returnsZero() {
            InternationalFixedDate date = InternationalFixedDate.of(2023, 1, 1);
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), date.until(date));
        }
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("toString()")
    class ToStringTest {

        static Stream<Arguments> dateToStringRepresentations() {
            return Stream.of(
                Arguments.of(InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"),
                Arguments.of(InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"),
                Arguments.of(InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"), // Leap Day
                Arguments.of(InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29")  // Year Day
            );
        }

        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("dateToStringRepresentations")
        @DisplayName("returns the correct string representation")
        void toString_returnsCorrectFormat(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }

    //-----------------------------------------------------------------------
    // equals() and hashCode()
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("equals() and hashCode()")
    class EqualsAndHashCodeTest {

        @Test
        @DisplayName("adhere to the contract")
        void equalsAndHashCode_contract() {
            new EqualsTester()
                .addEqualityGroup(
                    InternationalFixedDate.of(2014, 5, 26),
                    InternationalFixedDate.of(2014, 5, 26))
                .addEqualityGroup(
                    InternationalFixedDate.of(2014, 6, 26))
                .addEqualityGroup(
                    InternationalFixedDate.of(2015, 5, 26))
                .testEquals();
        }
    }
}