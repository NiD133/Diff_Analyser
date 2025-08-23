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
 * Tests for {@link JulianDate} focusing on conversions, interoperability, and date calculations.
 */
@DisplayName("JulianDate Interoperability and Calculations")
class JulianDateConversionAndInteroperabilityTest {

    //-----------------------------------------------------------------------
    // Test Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleJulianAndIsoDates() {
        return Stream.of(
            // Start of proleptic Julian calendar
            Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            // Around a non-leap year in Julian
            Arguments.of(JulianDate.of(1, 2, 28), LocalDate.of(1, 2, 26)),
            Arguments.of(JulianDate.of(1, 3, 1), LocalDate.of(1, 2, 27)),
            // Around a Julian leap year (year 4)
            Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
            Arguments.of(JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)),
            // Around a Julian leap year (year 100) which is not a Gregorian leap year
            Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)),
            Arguments.of(JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
            // Around the Gregorian calendar reform (Julian 1582-10-04 followed by Gregorian 1582-10-15)
            Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            // Modern dates
            Arguments.of(JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)),
            Arguments.of(JulianDate.of(2012, 6, 22), LocalDate.of(2012, 7, 5)),
            Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    static Stream<Arguments> invalidDateProvider() {
        return Stream.of(
            Arguments.of(1900, 0, 1),   // Invalid month
            Arguments.of(1900, 13, 1),  // Invalid month
            Arguments.of(1900, 1, 0),   // Invalid day
            Arguments.of(1900, 1, 32),  // Invalid day for Jan
            Arguments.of(1899, 2, 29),  // Invalid day for Feb in a non-leap year
            Arguments.of(1900, 2, 30),  // Invalid day for Feb in a leap year
            Arguments.of(1900, 4, 31)   // Invalid day for Apr
        );
    }

    //-----------------------------------------------------------------------
    // Conversion and Factory Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion and Factory Methods")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateConversionAndInteroperabilityTest#sampleJulianAndIsoDates")
        @DisplayName("LocalDate.from(julianDate) should return the equivalent ISO date")
        void testFromJulianDateToLocalDate(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateConversionAndInteroperabilityTest#sampleJulianAndIsoDates")
        @DisplayName("JulianDate.from(localDate) should return the equivalent Julian date")
        void testFromLocalDateToJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateConversionAndInteroperabilityTest#sampleJulianAndIsoDates")
        @DisplayName("chronology.dateEpochDay should create the correct Julian date")
        void testChronologyDateFromEpochDay(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateConversionAndInteroperabilityTest#sampleJulianAndIsoDates")
        @DisplayName("toEpochDay should return the same epoch day as the equivalent ISO date")
        void testToEpochDay(JulianDate julian, LocalDate iso) {
            assertEquals(iso.toEpochDay(), julian.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateConversionAndInteroperabilityTest#sampleJulianAndIsoDates")
        @DisplayName("chronology.date(temporal) should create the correct Julian date from an ISO date")
        void testChronologyDateFromTemporal(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.date(iso));
        }
    }

    //-----------------------------------------------------------------------
    // Validation and Property Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Validation and Date Properties")
    class ValidationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateConversionAndInteroperabilityTest#invalidDateProvider")
        @DisplayName("of(y, m, d) should throw DateTimeException for invalid dates")
        void testFactoryOfFailsForInvalidDate(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, day));
        }

        @Test
        @DisplayName("lengthOfMonth should be correct for leap and non-leap years")
        void testLengthOfMonth() {
            assertEquals(31, JulianDate.of(1900, 1, 1).lengthOfMonth());
            assertEquals(29, JulianDate.of(1900, 2, 1).lengthOfMonth()); // Julian leap year
            assertEquals(28, JulianDate.of(1901, 2, 1).lengthOfMonth()); // Julian non-leap year
            assertEquals(30, JulianDate.of(1900, 4, 1).lengthOfMonth());
        }

        @Test
        @DisplayName("toString should return a formatted string with era")
        void testToString() {
            assertEquals("Julian AD 2012-06-23", JulianDate.of(2012, 6, 23).toString());
            assertEquals("Julian BC 1-01-01", JulianDate.of(0, 1, 1).toString());
        }
    }

    //-----------------------------------------------------------------------
    // Field Accessor Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field Accessors")
    class FieldAccessorTests {

        static Stream<Arguments> fieldRangeProvider() {
            return Stream.of(
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29), // Leap year
                Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28), // Non-leap year
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, 1, 366),  // Leap year
                Arguments.of(2011, 1, 23, DAY_OF_YEAR, 1, 365),  // Non-leap year
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
                Arguments.of(2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4)
            );
        }

        @ParameterizedTest
        @MethodSource("fieldRangeProvider")
        @DisplayName("range(field) should return the correct value range")
        void testRange(int year, int month, int day, TemporalField field, int expectedMin, int expectedMax) {
            JulianDate date = JulianDate.of(year, month, day);
            assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
        }

        @Test
        @DisplayName("range(field) should throw exception for unsupported fields")
        void testRangeForUnsupportedField() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> JulianDate.of(2012, 6, 30).range(MINUTE_OF_DAY));
        }

        static Stream<Arguments> fieldValueProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7L), // A Sunday
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                // Day of year for 2014-05-26 is 31(Jan)+28(Feb)+31(Mar)+30(Apr)+26(May) = 146
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 146L),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1L),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L), // AD
                Arguments.of(0, 6, 8, ERA, 0L)      // BC
            );
        }

        @ParameterizedTest
        @MethodSource("fieldValueProvider")
        @DisplayName("getLong(field) should return the correct field value")
        void testGetLong(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, day).getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    // Adjustment (with) Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Adjustments with 'with'")
    class AdjustmentTests {

        static Stream<Arguments> dateWithFieldProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, JulianDate.of(2014, 5, 31)),
                Arguments.of(2014, 5, 26, YEAR, 2012, JulianDate.of(2012, 5, 26)),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7, JulianDate.of(2014, 7, 26)),
                // Adjusting month to one with fewer days
                Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2, JulianDate.of(2012, 2, 29)), // Leap year
                Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2, JulianDate.of(2011, 2, 28)), // Non-leap year
                // Adjusting year of a leap day
                Arguments.of(2012, 2, 29, YEAR, 2011, JulianDate.of(2011, 2, 28)),
                Arguments.of(2014, 5, 26, ERA, 0, JulianDate.of(-2013, 5, 26)),
                Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 3, JulianDate.of(2014, 5, 22))
            );
        }

        @ParameterizedTest
        @MethodSource("dateWithFieldProvider")
        @DisplayName("with(field, value) should return a correctly adjusted date")
        void testWith(int year, int month, int day, TemporalField field, long value, JulianDate expected) {
            assertEquals(expected, JulianDate.of(year, month, day).with(field, value));
        }
    }

    //-----------------------------------------------------------------------
    // Arithmetic (plus/minus) Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Arithmetic")
    class ArithmeticTests {

        static Stream<Arguments> datePlusAmountProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 1, MILLENNIA, 3014, 5, 26),
                Arguments.of(2014, 5, 26, 1, ERAS, -2013, 5, 26)
            );
        }

        @ParameterizedTest
        @MethodSource("datePlusAmountProvider")
        @DisplayName("plus(amount, unit) should add the specified amount correctly")
        void testPlus(int year, int month, int day, long amount, TemporalUnit unit, int exYear, int exMonth, int exDay) {
            JulianDate start = JulianDate.of(year, month, day);
            JulianDate expected = JulianDate.of(exYear, exMonth, exDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("datePlusAmountProvider")
        @DisplayName("minus(amount, unit) should subtract the specified amount correctly")
        void testMinus(int year, int month, int day, long amount, TemporalUnit unit, int exYear, int exMonth, int exDay) {
            JulianDate start = JulianDate.of(exYear, exMonth, exDay); // The result of plus becomes the start for minus
            JulianDate expected = JulianDate.of(year, month, day);   // The start of plus becomes the result for minus
            assertEquals(expected, start.minus(amount, unit));
        }
    }

    //-----------------------------------------------------------------------
    // Comparison (until) Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Comparison with 'until'")
    class ComparisonTests {

        @Test
        @DisplayName("until(sameDate) should return a zero period")
        void testUntilSelfIsZero() {
            JulianDate date = JulianDate.of(2014, 5, 26);
            assertEquals(0, date.until(date, DAYS));
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateConversionAndInteroperabilityTest#sampleJulianAndIsoDates")
        @DisplayName("until(equivalentLocalDate) should return a zero period")
        void testUntilEquivalentLocalDateIsZero(JulianDate julian, LocalDate iso) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateConversionAndInteroperabilityTest#sampleJulianAndIsoDates")
        @DisplayName("LocalDate.until(equivalentJulianDate) should return a zero period")
        void testLocalDateUntilEquivalentJulianDateIsZero(JulianDate julian, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(julian));
        }

        static Stream<Arguments> dateUntilProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 6, 1, DAYS, 6),
                Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6),
                Arguments.of(2014, 5, 26, 2014, 6, 2, WEEKS, 1),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1),
                Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1)
            );
        }

        @ParameterizedTest
        @MethodSource("dateUntilProvider")
        @DisplayName("until(endDate, unit) should calculate the correct duration")
        void testUntil(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate end = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }
    }
}