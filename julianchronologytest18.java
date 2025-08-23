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
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.stream.Stream;

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

@DisplayName("JulianDate Tests")
class JulianDateTest {

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> provideJulianAndIsoDatePairs() {
        return Stream.of(
                Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
                Arguments.of(JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
                Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
                Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)), // Julian leap year
                Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)), // Julian leap, ISO non-leap
                Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
                Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
                Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    @Nested
    @DisplayName("Factory and Conversion Tests")
    class FactoryAndConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianAndIsoDatePairs")
        @DisplayName("A JulianDate should be convertible to an ISO LocalDate")
        void shouldConvertToIsoDate(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianAndIsoDatePairs")
        @DisplayName("A JulianDate should be creatable from an ISO LocalDate")
        void shouldCreateFromIsoDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianAndIsoDatePairs")
        @DisplayName("The JulianChronology should create a JulianDate from an epoch day")
        void chronologyShouldCreateDateFromEpochDay(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianAndIsoDatePairs")
        @DisplayName("toEpochDay should return the correct epoch day value")
        void toEpochDay_shouldReturnCorrectValue(JulianDate julian, LocalDate iso) {
            assertEquals(iso.toEpochDay(), julian.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianAndIsoDatePairs")
        @DisplayName("The JulianChronology should create a JulianDate from a TemporalAccessor")
        void chronologyShouldCreateDateFromTemporal(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.date(iso));
        }
    }

    @Nested
    @DisplayName("Invalid Date Creation Tests")
    class InvalidDateCreationTests {

        static Stream<Arguments> provideInvalidDateComponents() {
            return Stream.of(
                    Arguments.of(1900, 0, 1, "invalid month"),
                    Arguments.of(1900, 13, 1, "invalid month"),
                    Arguments.of(1900, 1, 0, "invalid day-of-month"),
                    Arguments.of(1900, 1, 32, "invalid day-of-month"),
                    Arguments.of(1899, 2, 29, "non-leap year"), // 1899 is not a Julian leap year
                    Arguments.of(1900, 2, 30, "invalid day for Julian leap year")
            );
        }

        @ParameterizedTest(name = "year={0}, month={1}, day={2} ({3})")
        @MethodSource("provideInvalidDateComponents")
        @DisplayName("of() should throw DateTimeException for invalid date components")
        void of_shouldThrowException_forInvalidDate(int year, int month, int dom, String description) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dom));
        }
    }

    @Nested
    @DisplayName("Date Component Tests")
    class DateComponentTests {

        static Stream<Arguments> provideYearMonthAndExpectedLength() {
            return Stream.of(
                    Arguments.of(1900, 1, 31),
                    Arguments.of(1900, 2, 29), // 1900 is a leap year in Julian
                    Arguments.of(1901, 2, 28),
                    Arguments.of(1904, 2, 29),
                    Arguments.of(2000, 2, 29)
            );
        }

        @ParameterizedTest(name = "{0}-{1} should have {2} days")
        @MethodSource("provideYearMonthAndExpectedLength")
        @DisplayName("lengthOfMonth() should return the correct number of days")
        void lengthOfMonth_shouldReturnCorrectNumberOfDays(int year, int month, int length) {
            assertEquals(length, JulianDate.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> provideDateAndFieldForRangeCheck() {
            return Stream.of(
                    Arguments.of(2012, 1, 23, DAY_OF_MONTH, 1, 31),
                    Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29), // Leap year
                    Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28), // Non-leap year
                    Arguments.of(2012, 1, 23, DAY_OF_YEAR, 1, 366), // Leap year
                    Arguments.of(2011, 1, 23, DAY_OF_YEAR, 1, 365), // Non-leap year
                    Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5)
            );
        }

        @ParameterizedTest(name = "range of {3} for {0}-{1}-{2} should be {4}-{5}")
        @MethodSource("provideDateAndFieldForRangeCheck")
        @DisplayName("range() should return the correct value range for a given field")
        void range_shouldReturnCorrectValueRangeForField(int year, int month, int dom, TemporalField field, long expectedMin, long expectedMax) {
            ValueRange expectedRange = ValueRange.of(expectedMin, expectedMax);
            assertEquals(expectedRange, JulianDate.of(year, month, dom).range(field));
        }

        static Stream<Arguments> provideDateAndFieldForGetLong() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7), // A Sunday in Julian
                    Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                    Arguments.of(2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26),
                    Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                    Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12L + 5 - 1),
                    Arguments.of(2014, 5, 26, YEAR, 2014),
                    Arguments.of(2014, 5, 26, ERA, 1), // AD
                    Arguments.of(0, 6, 8, ERA, 0)      // BC
            );
        }

        @ParameterizedTest(name = "getLong({3}) for {0}-{1}-{2} should be {4}")
        @MethodSource("provideDateAndFieldForGetLong")
        @DisplayName("getLong() should return the correct value for a given field")
        void getLong_shouldReturnCorrectValueForField(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Arithmetic Operation Tests")
    class ArithmeticOperationTests {

        static Stream<Arguments> provideDateAndFieldAndValueToSet() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22),
                    Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31),
                    Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26),
                    Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                    Arguments.of(2014, 5, 26, ERA, 0, -2013, 5, 26), // Change from AD to BC
                    Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29), // Adjust to shorter month in leap year
                    Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28)  // Adjust to shorter month in common year
            );
        }

        @ParameterizedTest(name = "with({3}, {4})")
        @MethodSource("provideDateAndFieldAndValueToSet")
        @DisplayName("with() should correctly set a temporal field")
        void with_shouldSetFieldToNewValue(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            JulianDate start = JulianDate.of(y, m, d);
            JulianDate expected = JulianDate.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Arguments> provideDateAmountAndExpectedResultForAddition() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
                    Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                    Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                    Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                    Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                    Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                    Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                    Arguments.of(2014, 5, 26, -1, ERAS, -2013, 5, 26)
            );
        }

        @ParameterizedTest(name = "plus({3}, {4})")
        @MethodSource("provideDateAmountAndExpectedResultForAddition")
        @DisplayName("plus() should correctly add a temporal amount")
        void plus_shouldAddAmountToDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            JulianDate start = JulianDate.of(y, m, d);
            JulianDate expected = JulianDate.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "minus({3}, {4})")
        @MethodSource("provideDateAmountAndExpectedResultForAddition")
        @DisplayName("minus() should be the inverse of plus()")
        void minus_shouldBeInverseOfPlus(int startY, int startM, int startD, long amount, TemporalUnit unit, int endY, int endM, int endD) {
            JulianDate start = JulianDate.of(startY, startM, startD);
            JulianDate end = JulianDate.of(endY, endM, endD);
            assertEquals(start, end.minus(amount, unit));
        }

        static Stream<Arguments> provideStartAndEndDatesForUntil() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, 2014, 6, 1, DAYS, 6),
                    Arguments.of(2014, 5, 26, 2014, 6, 2, WEEKS, 1),
                    Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1),
                    Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1),
                    Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1),
                    Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1),
                    Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1),
                    Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1)
            );
        }

        @ParameterizedTest(name = "until({3}-{4}-{5}, {6})")
        @MethodSource("provideStartAndEndDatesForUntil")
        @DisplayName("until() should calculate the correct amount of time between two dates")
        void until_shouldCalculateAmountBetweenDates(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate end = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @Test
        @DisplayName("with(TemporalAdjuster) should correctly adjust the date")
        void with_temporalAdjuster_shouldCorrectlyAdjustDate() {
            JulianDate date = JulianDate.of(2012, 2, 23);
            JulianDate lastDay = date.with(TemporalAdjusters.lastDayOfMonth());
            assertEquals(JulianDate.of(2012, 2, 29), lastDay);
        }

        @Test
        @DisplayName("until() with the same date should return a zero period")
        void until_sameDate_returnsZeroPeriod() {
            JulianDate date = JulianDate.of(2012, 6, 22);
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianAndIsoDatePairs")
        @DisplayName("until() with an equivalent date in a different chronology should return a zero period")
        void until_equivalentDateInDifferentChronology_returnsZeroPeriod(JulianDate julian, LocalDate iso) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(iso));
            assertEquals(Period.ZERO, iso.until(julian));
        }
    }

    @Nested
    @DisplayName("Formatting Tests")
    class FormattingTests {

        static Stream<Arguments> provideDateAndExpectedString() {
            return Stream.of(
                    Arguments.of(JulianDate.of(1, 1, 1), "Julian AD 1-01-01"),
                    Arguments.of(JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23")
            );
        }

        @ParameterizedTest(name = "{0} should be formatted as ''{1}''")
        @MethodSource("provideDateAndExpectedString")
        @DisplayName("toString() should return the correct string representation")
        void toString_shouldReturnCorrectRepresentation(JulianDate julian, String expected) {
            assertEquals(expected, julian.toString());
        }
    }
}