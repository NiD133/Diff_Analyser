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
 * Tests for {@link JulianDate} and its integration with {@link JulianChronology}.
 */
@DisplayName("JulianDate Tests")
class JulianDateTest {

    //-----------------------------------------------------------------------
    // Conversion and Equivalence Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversions to/from other calendar systems")
    class ConversionTests {

        static Stream<Arguments> julianAndIsoDateProvider() {
            return Stream.of(
                Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
                Arguments.of(JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
                Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
                Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
                Arguments.of(JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
                Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
                Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
                Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
            );
        }

        @ParameterizedTest
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("should be consistent with ISO date representation")
        void conversionsBetweenJulianAndIsoAreConsistent(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(julianDate));
            assertEquals(julianDate, JulianDate.from(isoDate));
            assertEquals(isoDate.toEpochDay(), julianDate.toEpochDay());
            assertEquals(julianDate, JulianChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
            assertEquals(julianDate, JulianChronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("until() should return zero for equivalent dates")
        void until_forEquivalentDates_returnsZero(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(julianDate));
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(julianDate));
        }
    }

    //-----------------------------------------------------------------------
    // Date Arithmetic Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Arithmetic")
    class ArithmeticTests {

        static Stream<Arguments> plusAmountProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
                Arguments.of(2014, 5, 26, -3, DAYS, 2014, 5, 23),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, -5, MONTHS, 2013, 12, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                Arguments.of(2014, 5, 26, -1, ERAS, -2013, 5, 26)
            );
        }

        @ParameterizedTest
        @MethodSource("plusAmountProvider")
        @DisplayName("plus() should add a temporal amount correctly")
        void plus_shouldAddTemporalAmount(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            JulianDate start = JulianDate.of(year, month, dom);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusAmountProvider")
        @DisplayName("minus() should be the inverse of plus()")
        void minus_shouldBeInverseOfPlus(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            JulianDate start = JulianDate.of(year, month, dom);
            JulianDate end = JulianDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(start, end.minus(amount, unit));
        }

        static Stream<Arguments> withFieldValueProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2014, 5, 26, ERA, 0, -2013, 5, 26),
                Arguments.of(2012, 2, 29, YEAR, 2011, 2011, 2, 28) // Adjust for leap year
            );
        }

        @ParameterizedTest
        @MethodSource("withFieldValueProvider")
        @DisplayName("with() should adjust a temporal field correctly")
        void with_shouldSetFieldToValue(int year, int month, int dom, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDom) {
            JulianDate start = JulianDate.of(year, month, dom);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Arguments> untilProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 1, DAYS, 6),
                Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6),
                Arguments.of(2014, 5, 26, 2014, 6, 2, WEEKS, 1),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1),
                Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1),
                Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1)
            );
        }

        @ParameterizedTest
        @MethodSource("untilProvider")
        @DisplayName("until() should calculate the amount of time between two dates")
        void until_shouldCalculateAmountBetweenDates(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate end = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }
    }

    //-----------------------------------------------------------------------
    // Field and Property Access Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field and Property Access")
    class FieldAndPropertyTests {

        static Stream<Arguments> getLongProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                // Julian 2014 is not a leap year: Jan(31) + Feb(28) + Mar(31) + Apr(30) + 26 = 146
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 146),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 4),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                Arguments.of(0, 6, 8, ERA, 0),
                Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7)
            );
        }

        @ParameterizedTest
        @MethodSource("getLongProvider")
        @DisplayName("getLong() should return the correct value for a given field")
        void getLong_shouldReturnCorrectFieldValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, dom).getLong(field));
        }

        static Stream<Arguments> rangeProvider() {
            return Stream.of(
                Arguments.of(2012, 1, DAY_OF_MONTH, 1, 31), // Leap year
                Arguments.of(2012, 2, DAY_OF_MONTH, 1, 29), // Leap year
                Arguments.of(2011, 2, DAY_OF_MONTH, 1, 28), // Common year
                Arguments.of(2012, 1, DAY_OF_YEAR, 1, 366),  // Leap year
                Arguments.of(2011, 1, DAY_OF_YEAR, 1, 365),  // Common year
                Arguments.of(2012, 2, ALIGNED_WEEK_OF_MONTH, 1, 5),
                Arguments.of(2011, 2, ALIGNED_WEEK_OF_MONTH, 1, 4)
            );
        }

        @ParameterizedTest
        @MethodSource("rangeProvider")
        @DisplayName("range() should return the correct value range for a field")
        void range_shouldReturnCorrectValueRange(int year, int month, TemporalField field, int min, int max) {
            assertEquals(ValueRange.of(min, max), JulianDate.of(year, month, 1).range(field));
        }

        static Stream<Arguments> lengthOfMonthProvider() {
            return Stream.of(
                Arguments.of(1900, 1, 31),
                Arguments.of(1900, 2, 29), // Julian 1900 is a leap year
                Arguments.of(1901, 2, 28),
                Arguments.of(1904, 2, 29),
                Arguments.of(2000, 2, 29),
                Arguments.of(2100, 2, 29)  // Julian 2100 is a leap year
            );
        }

        @ParameterizedTest
        @MethodSource("lengthOfMonthProvider")
        @DisplayName("lengthOfMonth() should return the correct number of days")
        void lengthOfMonth_shouldReturnCorrectDayCount(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }
    }

    //-----------------------------------------------------------------------
    // Factory and Validation Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Factory Methods and Validation")
    class FactoryAndValidationTests {

        static Stream<Arguments> invalidDateComponentProvider() {
            return Stream.of(
                Arguments.of(1900, 0, 1),   // Invalid month
                Arguments.of(1900, 13, 1),  // Invalid month
                Arguments.of(1900, 1, 0),   // Invalid day
                Arguments.of(1900, 1, 32),  // Invalid day
                Arguments.of(1899, 2, 29),  // Invalid day for non-leap year
                Arguments.of(1900, 2, 30),  // Invalid day for leap year
                Arguments.of(1900, 4, 31)   // Invalid day for 30-day month
            );
        }

        @ParameterizedTest
        @MethodSource("invalidDateComponentProvider")
        @DisplayName("of() should throw DateTimeException for invalid date components")
        void of_forInvalidDateComponents_shouldThrowException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dom));
        }

        @Test
        @DisplayName("chronology.eraOf() should throw DateTimeException for invalid era value")
        void chronology_eraOf_withInvalidValue_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> JulianChronology.INSTANCE.eraOf(2));
        }
    }

    //-----------------------------------------------------------------------
    // String Representation Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("String Representation")
    class StringRepresentationTests {

        static Stream<Arguments> toStringProvider() {
            return Stream.of(
                Arguments.of(JulianDate.of(1, 1, 1), "Julian AD 1-01-01"),
                Arguments.of(2012, 6, 23, "Julian AD 2012-06-23")
            );
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        @DisplayName("toString() should return the correct formatted string")
        void toString_shouldReturnCorrectRepresentation(JulianDate julianDate, String expected) {
            assertEquals(expected, julianDate.toString());
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        void toString_withExplicitDate(int year, int month, int day, String expected) {
            assertEquals(expected, JulianDate.of(year, month, day).toString());
        }
    }
}