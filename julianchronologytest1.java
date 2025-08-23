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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.testing.EqualsTester;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.Chronology;
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
 * Tests for the {@link JulianDate} class.
 *
 * This test class is structured to cover factory methods, conversions,
 * queries, adjustments, and core object methods like equals, hashCode, and toString.
 */
@DisplayName("JulianDate")
public class JulianDateTest {

    //-----------------------------------------------------------------------
    // Test Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample pairs of JulianDate and their equivalent ISO LocalDate.
     *
     * @return a stream of arguments, each containing a JulianDate and a LocalDate.
     */
    static Stream<Arguments> provideSampleJulianAndIsoDates() {
        return Stream.of(
            Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)), // Julian leap year
            Arguments.of(JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)), // Julian leap year, ISO non-leap
            Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)), // Day before Gregorian cutover
            Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)), // Day of Gregorian cutover
            Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethods {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideSampleJulianAndIsoDates")
        @DisplayName("from(TemporalAccessor) creates a JulianDate from an ISO LocalDate")
        void from_onLocalDate_createsCorrectJulianDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideSampleJulianAndIsoDates")
        @DisplayName("chronology.date(TemporalAccessor) creates a JulianDate from an ISO LocalDate")
        void chronologyDate_onLocalDate_createsCorrectJulianDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideSampleJulianAndIsoDates")
        @DisplayName("chronology.dateEpochDay(long) creates a JulianDate from an epoch day")
        void chronologyDateEpochDay_createsCorrectJulianDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @Test
        @DisplayName("of(year, month, day) throws exception for invalid dates")
        void of_withInvalidDateParts_throwsDateTimeException() {
            assertThrows(DateTimeException.class, () -> JulianDate.of(1900, 2, 30), "Feb 30 should be invalid");
            assertThrows(DateTimeException.class, () -> JulianDate.of(1899, 2, 29), "Feb 29 in non-leap year 1899 should be invalid");
            assertThrows(DateTimeException.class, () -> JulianDate.of(1900, 13, 1), "Month 13 should be invalid");
            assertThrows(DateTimeException.class, () -> JulianDate.of(1900, 1, 0), "Day 0 should be invalid");
        }
    }

    @Nested
    @DisplayName("Conversion methods")
    class ConversionMethods {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideSampleJulianAndIsoDates")
        @DisplayName("LocalDate.from(TemporalAccessor) converts a JulianDate to an ISO LocalDate")
        void toLocalDate_convertsCorrectly(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(julianDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideSampleJulianAndIsoDates")
        @DisplayName("toEpochDay() returns the correct epoch day")
        void toEpochDay_returnsCorrectEpochDay(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), julianDate.toEpochDay());
        }
    }

    @Nested
    @DisplayName("Query methods")
    class QueryMethods {

        @Test
        @DisplayName("lengthOfMonth() returns correct length for leap and non-leap years")
        void lengthOfMonth_returnsCorrectLength() {
            assertEquals(31, JulianDate.of(1900, 1, 1).lengthOfMonth());
            assertEquals(29, JulianDate.of(1900, 2, 1).lengthOfMonth(), "1900 is a leap year in Julian");
            assertEquals(28, JulianDate.of(1901, 2, 1).lengthOfMonth(), "1901 is not a leap year in Julian");
            assertEquals(29, JulianDate.of(1904, 2, 1).lengthOfMonth(), "1904 is a leap year in Julian");
            assertEquals(30, JulianDate.of(1900, 4, 1).lengthOfMonth());
        }

        static Stream<Arguments> provideRanges() {
            return Stream.of(
                Arguments.of(2012, 1, DAY_OF_MONTH, 1, 31),
                Arguments.of(2012, 2, DAY_OF_MONTH, 1, 29), // Leap year
                Arguments.of(2011, 2, DAY_OF_MONTH, 1, 28), // Non-leap year
                Arguments.of(2012, 1, DAY_OF_YEAR, 1, 366), // Leap year
                Arguments.of(2011, 1, DAY_OF_YEAR, 1, 365), // Non-leap year
                Arguments.of(2012, 2, ALIGNED_WEEK_OF_MONTH, 1, 5)
            );
        }

        @ParameterizedTest
        @MethodSource("provideRanges")
        @DisplayName("range(TemporalField) returns the correct value range")
        void range_forField_returnsCorrectRange(int year, int month, TemporalField field, int expectedMin, int expectedMax) {
            JulianDate date = JulianDate.of(year, month, 1);
            assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
        }

        static Stream<Arguments> provideFieldValues() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7), // A Sunday in Julian
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                Arguments.of(0, 6, 8, ERA, 0)
            );
        }

        @ParameterizedTest
        @MethodSource("provideFieldValues")
        @DisplayName("getLong(TemporalField) returns the correct value for a given field")
        void getLong_forField_returnsCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            JulianDate date = JulianDate.of(year, month, day);
            assertEquals(expected, date.getLong(field));
        }

        @Test
        @DisplayName("until(end, unit) returns zero for equivalent dates")
        void until_withEquivalentDate_isZero() {
            JulianDate julian = JulianDate.of(2012, 6, 23);
            LocalDate iso = LocalDate.of(2012, 7, 6);

            assertEquals(Period.ZERO, julian.until(julian));
            assertEquals(Period.ZERO, julian.until(iso));
            assertEquals(Period.ZERO, iso.until(julian));
        }

        @Test
        @DisplayName("until(end, DAYS) returns the correct number of days")
        void until_withDaysUnit_returnsCorrectDayCount() {
            JulianDate start = JulianDate.of(2014, 5, 26);
            LocalDate end = LocalDate.from(start);

            assertEquals(1, start.until(end.plusDays(1), DAYS));
            assertEquals(35, start.until(end.plusDays(35), DAYS));
            assertEquals(-40, start.until(end.minusDays(40), DAYS));
        }
    }

    @Nested
    @DisplayName("Adjustment methods")
    class AdjustmentMethods {

        static Stream<Arguments> provideWithAdjustments() {
            return Stream.of(
                Arguments.of(DAY_OF_WEEK, 3, JulianDate.of(2014, 5, 22)),
                Arguments.of(DAY_OF_MONTH, 31, JulianDate.of(2014, 5, 31)),
                Arguments.of(DAY_OF_YEAR, 365, JulianDate.of(2014, 12, 31)),
                Arguments.of(MONTH_OF_YEAR, 7, JulianDate.of(2014, 7, 26)),
                Arguments.of(YEAR, 2012, JulianDate.of(2012, 5, 26)),
                Arguments.of(ERA, 0, JulianDate.of(-2013, 5, 26))
            );
        }

        @ParameterizedTest
        @MethodSource("provideWithAdjustments")
        @DisplayName("with(TemporalField, long) adjusts the date correctly")
        void with_usingField_returnsAdjustedDate(TemporalField field, long value, JulianDate expectedDate) {
            JulianDate start = JulianDate.of(2014, 5, 26);
            assertEquals(expectedDate, start.with(field, value));
        }

        static Stream<Arguments> providePlusAdjustments() {
            return Stream.of(
                Arguments.of(8, DAYS, JulianDate.of(2014, 6, 3)),
                Arguments.of(3, WEEKS, JulianDate.of(2014, 6, 16)),
                Arguments.of(3, MONTHS, JulianDate.of(2014, 8, 26)),
                Arguments.of(3, YEARS, JulianDate.of(2017, 5, 26)),
                Arguments.of(3, DECADES, JulianDate.of(2044, 5, 26)),
                Arguments.of(3, CENTURIES, JulianDate.of(2314, 5, 26)),
                Arguments.of(1, MILLENNIA, JulianDate.of(3014, 5, 26)),
                Arguments.of(1, ERAS, JulianDate.of(-2013, 5, 26))
            );
        }

        @ParameterizedTest
        @MethodSource("providePlusAdjustments")
        @DisplayName("plus(long, TemporalUnit) adds the amount correctly")
        void plus_usingUnit_returnsAddedDate(long amount, TemporalUnit unit, JulianDate expectedDate) {
            JulianDate start = JulianDate.of(2014, 5, 26);
            assertEquals(expectedDate, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("providePlusAdjustments")
        @DisplayName("minus(long, TemporalUnit) subtracts the amount correctly")
        void minus_usingUnit_returnsSubtractedDate(long amount, TemporalUnit unit, JulianDate dateToSubtractFrom) {
            JulianDate expected = JulianDate.of(2014, 5, 26);
            assertEquals(expected, dateToSubtractFrom.minus(amount, unit));
        }
    }

    @Nested
    @DisplayName("Object methods")
    class ObjectMethods {

        @Test
        @DisplayName("equals() and hashCode() follow the contract")
        void testEqualsAndHashCode() {
            new EqualsTester()
                .addEqualityGroup(JulianDate.of(2014, 5, 26), JulianDate.of(2014, 5, 26))
                .addEqualityGroup(JulianDate.of(2014, 5, 27))
                .addEqualityGroup(JulianDate.of(2015, 5, 26))
                .addEqualityGroup(LocalDate.of(2014, 5, 26))
                .testEquals();
        }

        @Test
        @DisplayName("toString() returns the correct format")
        void toString_returnsCorrectString() {
            assertEquals("Julian AD 2012-06-23", JulianDate.of(2012, 6, 23).toString());
            assertEquals("Julian BC 1-01-01", JulianDate.of(0, 1, 1).toString());
        }
    }

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApi {

        @Test
        @DisplayName("Chronology.of('Julian') returns the singleton instance")
        void chronologyOf_returnsSingleton() {
            Chronology chrono = Chronology.of("Julian");
            assertNotNull(chrono);
            assertEquals(JulianChronology.INSTANCE, chrono);
            assertEquals("Julian", chrono.getId());
            assertEquals("julian", chrono.getCalendarType());
        }
    }
}