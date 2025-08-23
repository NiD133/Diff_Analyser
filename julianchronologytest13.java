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
import static org.junit.jupiter.api.Assertions.assertAll;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link JulianDate} class.
 */
@DisplayName("JulianDate")
public class JulianDateTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    private static Stream<Arguments> provideJulianAndIsoDates() {
        return Stream.of(
            // Start of epoch
            Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            // Non-leap year
            Arguments.of(JulianDate.of(1, 3, 1), LocalDate.of(1, 2, 27)),
            // Julian leap year (4)
            Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
            Arguments.of(JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)),
            // Julian leap year, but not Gregorian (100)
            Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)),
            Arguments.of(JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
            // Before Christ era
            Arguments.of(JulianDate.of(0, 12, 31), LocalDate.of(0, 12, 29)),
            // Around Gregorian cutover date (for reference, not a switch in this proleptic calendar)
            Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            // Modern dates
            Arguments.of(JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)),
            Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    private static Stream<Arguments> provideInvalidDateParts() {
        return Stream.of(
            Arguments.of(1900, 0, 1),   // Invalid month 0
            Arguments.of(1900, 13, 1),  // Invalid month 13
            Arguments.of(1900, 1, 0),   // Invalid day 0
            Arguments.of(1900, 1, 32),  // Invalid day 32 for Jan
            Arguments.of(1900, 2, 30),  // Invalid day 30 for Feb (leap)
            Arguments.of(1899, 2, 29)   // Invalid day 29 for Feb (non-leap)
        );
    }

    private static Stream<Arguments> provideMonthLengths() {
        return Stream.of(
            Arguments.of(1900, 1, 31),
            Arguments.of(1900, 2, 29), // 1900 is a leap year in Julian
            Arguments.of(1900, 3, 31),
            Arguments.of(1900, 4, 30),
            Arguments.of(1901, 2, 28), // 1901 is not a leap year
            Arguments.of(1904, 2, 29), // 1904 is a leap year
            Arguments.of(2000, 2, 29)  // 2000 is a leap year
        );
    }

    private static Stream<Arguments> provideFieldRanges() {
        return Stream.of(
            Arguments.of(2012, 1, 23, DAY_OF_MONTH, 1, 31),
            Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29), // Leap year
            Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28), // Non-leap year
            Arguments.of(2012, 4, 23, DAY_OF_MONTH, 1, 30),
            Arguments.of(2012, 1, 23, DAY_OF_YEAR, 1, 366), // Leap year
            Arguments.of(2011, 2, 23, DAY_OF_YEAR, 1, 365), // Non-leap year
            Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
            Arguments.of(2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4)
        );
    }

    private static Stream<Arguments> provideFieldValues() {
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7L),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
            // Day of year for May 26 in a non-leap year (31+28+31+30+26)
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 146L),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014L * 12 + 5 - 1),
            Arguments.of(2014, 5, 26, YEAR, 2014L),
            Arguments.of(2014, 5, 26, ERA, 1L),
            Arguments.of(0, 6, 8, ERA, 0L),
            Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7L)
        );
    }

    private static Stream<Arguments> provideWithAdjustments() {
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3L, 2014, 5, 22),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31L, 2014, 5, 31),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365L, 2014, 12, 31),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7L, 2014, 7, 26),
            Arguments.of(2014, 5, 26, YEAR, 2012L, 2012, 5, 26),
            Arguments.of(2014, 5, 26, ERA, 0L, -2013, 5, 26),
            // Adjusting month to a shorter month
            Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2L, 2011, 2, 28),
            // Adjusting year of a leap day to a non-leap year
            Arguments.of(2012, 2, 29, YEAR, 2011L, 2011, 2, 28)
        );
    }

    private static Stream<Arguments> providePlusMinusArguments() {
        return Stream.of(
            Arguments.of(2014, 5, 26, 8L, DAYS, 2014, 6, 3),
            Arguments.of(2014, 5, 26, 3L, WEEKS, 2014, 6, 16),
            Arguments.of(2014, 5, 26, 3L, MONTHS, 2014, 8, 26),
            Arguments.of(2014, 5, 26, 3L, YEARS, 2017, 5, 26),
            Arguments.of(2014, 5, 26, 3L, DECADES, 2044, 5, 26),
            Arguments.of(2014, 5, 26, 3L, CENTURIES, 2314, 5, 26),
            Arguments.of(2014, 5, 26, 3L, MILLENNIA, 5014, 5, 26),
            Arguments.of(2014, 5, 26, 1L, ERAS, -2013, 5, 26)
        );
    }

    private static Stream<Arguments> provideUntilArguments() {
        return Stream.of(
            Arguments.of(2014, 5, 26, 2014, 6, 1, DAYS, 6L),
            Arguments.of(2014, 5, 26, 2014, 6, 2, WEEKS, 1L),
            Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
            Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
            Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
            Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
            Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
            Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1L)
        );
    }

    //-----------------------------------------------------------------------
    // Conversion and Interoperability
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("provideJulianAndIsoDates")
    @DisplayName("should convert consistently to and from ISO LocalDate")
    void testIsoConversions(JulianDate julianDate, LocalDate isoDate) {
        assertAll("Conversions between JulianDate and LocalDate should be consistent",
            () -> assertEquals(isoDate, LocalDate.from(julianDate), "LocalDate.from(JulianDate)"),
            () -> assertEquals(julianDate, JulianDate.from(isoDate), "JulianDate.from(LocalDate)"),
            () -> assertEquals(isoDate.toEpochDay(), julianDate.toEpochDay(), "toEpochDay()"),
            () -> assertEquals(julianDate, JulianChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()), "chronology.dateEpochDay()"),
            () -> assertEquals(julianDate, JulianChronology.INSTANCE.date(isoDate), "chronology.date(TemporalAccessor)")
        );
    }

    @ParameterizedTest
    @MethodSource("provideJulianAndIsoDates")
    @DisplayName("period until equivalent ISO date should be zero")
    void testUntilEquivalentIsoDateIsZero(JulianDate julianDate, LocalDate isoDate) {
        assertAll("Period between equivalent Julian and ISO dates should be zero",
            () -> assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(isoDate)),
            () -> assertEquals(Period.ZERO, isoDate.until(julianDate))
        );
    }

    //-----------------------------------------------------------------------
    // Factory and Validation
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("provideInvalidDateParts")
    @DisplayName("of() should throw for invalid date parts")
    void testOfWithInvalidDateThrowsException(int year, int month, int dayOfMonth) {
        assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dayOfMonth));
    }

    //-----------------------------------------------------------------------
    // Properties
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("provideMonthLengths")
    @DisplayName("should have correct length of month")
    void testLengthOfMonth(int year, int month, int expectedLength) {
        assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
    }

    @ParameterizedTest
    @MethodSource("provideFieldRanges")
    @DisplayName("should return correct range for a temporal field")
    void testRange(int year, int month, int dayOfMonth, TemporalField field, int expectedMin, int expectedMax) {
        JulianDate date = JulianDate.of(year, month, dayOfMonth);
        assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
    }

    @ParameterizedTest
    @MethodSource("provideFieldValues")
    @DisplayName("should return correct value for a temporal field")
    void testGetLong(int year, int month, int dayOfMonth, TemporalField field, long expected) {
        JulianDate date = JulianDate.of(year, month, dayOfMonth);
        assertEquals(expected, date.getLong(field));
    }

    @Test
    @DisplayName("chronology should return correct ranges for fields")
    void testChronologyRange() {
        assertEquals(ValueRange.of(1, 7), JulianChronology.INSTANCE.range(DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, 28, 31), JulianChronology.INSTANCE.range(DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, 365, 366), JulianChronology.INSTANCE.range(DAY_OF_YEAR));
        assertEquals(ValueRange.of(1, 12), JulianChronology.INSTANCE.range(MONTH_OF_YEAR));
    }

    //-----------------------------------------------------------------------
    // Modification
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("provideWithAdjustments")
    @DisplayName("with() should adjust date correctly")
    void testWith(int year, int month, int dayOfMonth, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDayOfMonth) {
        JulianDate start = JulianDate.of(year, month, dayOfMonth);
        JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDayOfMonth);
        assertEquals(expected, start.with(field, value));
    }

    //-----------------------------------------------------------------------
    // Arithmetic
    //-----------------------------------------------------------------------

    @ParameterizedTest
    @MethodSource("providePlusMinusArguments")
    @DisplayName("plus() should add amounts correctly")
    void testPlus(int startYear, int startMonth, int startDom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
        JulianDate start = JulianDate.of(startYear, startMonth, startDom);
        JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDom);
        assertEquals(expected, start.plus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("providePlusMinusArguments")
    @DisplayName("minus() should subtract amounts correctly")
    void testMinus(int expectedYear, int expectedMonth, int expectedDom, long amount, TemporalUnit unit, int startYear, int startMonth, int startDom) {
        JulianDate start = JulianDate.of(startYear, startMonth, startDom);
        JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDom);
        assertEquals(expected, start.minus(amount, unit));
    }

    @ParameterizedTest
    @MethodSource("provideUntilArguments")
    @DisplayName("until() should calculate the period between two dates")
    void testUntil(int year1, int month1, int dom1, int year2, int month2, int dom2, TemporalUnit unit, long expected) {
        JulianDate start = JulianDate.of(year1, month1, dom1);
        JulianDate end = JulianDate.of(year2, month2, dom2);
        assertEquals(expected, start.until(end, unit));
    }

    @Test
    @DisplayName("until() self should return a zero period")
    void testUntilSelfIsZeroPeriod() {
        JulianDate date = JulianDate.of(2012, 6, 22);
        assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), date.until(date));
    }

    //-----------------------------------------------------------------------
    // String Representation
    //-----------------------------------------------------------------------

    @Test
    @DisplayName("toString() should return correct string representation")
    void testToString() {
        assertEquals("Julian AD 1-01-01", JulianDate.of(1, 1, 1).toString());
        assertEquals("Julian AD 2012-06-23", JulianDate.of(2012, 6, 23).toString());
    }
}