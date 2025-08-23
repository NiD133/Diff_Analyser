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
 * Tests for the {@link JulianDate} class.
 */
@DisplayName("JulianDate")
class JulianDateTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample Julian dates and their equivalent ISO dates.
     * The data includes key dates like the start of the Julian era, leap years (including year 100),
     * and dates around the Gregorian calendar reform.
     */
    static Stream<Arguments> provideJulianToIsoDateSamples() {
        return Stream.of(
            Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
            Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)),
            Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            Arguments.of(JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)),
            Arguments.of(JulianDate.of(2012, 6, 22), LocalDate.of(2012, 7, 5)),
            Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion and Factory Tests")
    class ConversionAndFactoryTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianToIsoDateSamples")
        @DisplayName("should convert from JulianDate to the correct LocalDate")
        void fromJulianDate_shouldCreateCorrectLocalDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(julianDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianToIsoDateSamples")
        @DisplayName("should create a JulianDate from a LocalDate")
        void fromLocalDate_shouldCreateCorrectJulianDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianToIsoDateSamples")
        @DisplayName("should create a JulianDate from an epoch day")
        void dateEpochDay_shouldCreateCorrectJulianDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianToIsoDateSamples")
        @DisplayName("should convert to the correct epoch day")
        void toEpochDay_shouldReturnCorrectValue(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), julianDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianToIsoDateSamples")
        @DisplayName("should create a JulianDate from a TemporalAccessor")
        void chronologyDateFromTemporal_shouldCreateCorrectJulianDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideInvalidDateParts")
        @DisplayName("of() should throw exception for invalid date parts")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dayOfMonth));
        }

        @Test
        @DisplayName("until() with an equivalent date should return a zero period")
        void until_withEquivalentDate_returnsZeroPeriod() {
            JulianDate julianDate = JulianDate.of(2012, 6, 23);
            LocalDate isoDate = LocalDate.of(2012, 7, 6);

            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(julianDate), "julian.until(julian)");
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(isoDate), "julian.until(iso)");
            assertEquals(Period.ZERO, isoDate.until(julianDate), "iso.until(julian)");
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Properties")
    class DatePropertiesTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideYearMonthAndLength")
        @DisplayName("lengthOfMonth() should return correct value")
        void lengthOfMonth_shouldReturnCorrectValue(int year, int month, int length) {
            assertEquals(length, JulianDate.of(year, month, 1).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideDateAndFieldRanges")
        @DisplayName("range() for a field should return correct range")
        void range_forField_shouldReturnCorrectRange(int year, int month, int day, TemporalField field, int expectedMin, int expectedMax) {
            assertEquals(ValueRange.of(expectedMin, expectedMax), JulianDate.of(year, month, day).range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideDateAndFieldValues")
        @DisplayName("getLong() for a field should return correct value")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, day).getLong(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideLeapYearData")
        @DisplayName("isLeapYear() should follow the Julian calendar rule")
        void isLeapYear_shouldFollowJulianRule(long year, boolean isLeap) {
            assertEquals(isLeap, JulianChronology.INSTANCE.isLeapYear(year));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Arithmetic Operations")
    class ArithmeticTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianToIsoDateSamples")
        @DisplayName("plus/minus days should be consistent with ISO calendar")
        void plusAndMinusDays_shouldBehaveAsIso(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian.plus(0, DAYS)));
            assertEquals(iso.plusDays(1), LocalDate.from(julian.plus(1, DAYS)));
            assertEquals(iso.plusDays(35), LocalDate.from(julian.plus(35, DAYS)));
            assertEquals(iso.plusDays(-1), LocalDate.from(julian.plus(-1, DAYS)));
            assertEquals(iso.plusDays(-60), LocalDate.from(julian.plus(-60, DAYS)));

            assertEquals(iso, LocalDate.from(julian.minus(0, DAYS)));
            assertEquals(iso.minusDays(1), LocalDate.from(julian.minus(1, DAYS)));
            assertEquals(iso.minusDays(35), LocalDate.from(julian.minus(35, DAYS)));
            assertEquals(iso.minusDays(-1), LocalDate.from(julian.minus(-1, DAYS)));
            assertEquals(iso.minusDays(-60), LocalDate.from(julian.minus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideDateWithFieldAdjustments")
        @DisplayName("with() a field should return an adjusted date")
        void with_field_shouldReturnAdjustedDate(int year, int month, int day, TemporalField field, long value, int exYear, int exMonth, int exDay) {
            JulianDate start = JulianDate.of(year, month, day);
            JulianDate expected = JulianDate.of(exYear, exMonth, exDay);
            assertEquals(expected, start.with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideDatePlusUnitAndAmount")
        @DisplayName("plus() and minus() should be inverse operations")
        void plusAndMinus_areInverseOperations(int year, int month, int day, long amount, TemporalUnit unit, int exYear, int exMonth, int exDay) {
            JulianDate start = JulianDate.of(year, month, day);
            JulianDate expected = JulianDate.of(exYear, exMonth, exDay);
            assertEquals(expected, start.plus(amount, unit), "plus() operation failed");
            assertEquals(start, expected.minus(amount, unit), "minus() operation failed");
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideDatePairsAndUntilValues")
        @DisplayName("until() with a unit should return correct duration")
        void until_unit_shouldReturnCorrectDuration(int year1, int month1, int day1, int year2, int month2, int day2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(year1, month1, day1);
            JulianDate end = JulianDate.of(year2, month2, day2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideJulianToIsoDateSamples")
        @DisplayName("until() with DAYS unit should return correct day difference")
        void until_withDaysUnit_shouldReturnCorrectDayDifference(JulianDate julian, LocalDate iso) {
            assertEquals(0, julian.until(iso.plusDays(0), DAYS));
            assertEquals(1, julian.until(iso.plusDays(1), DAYS));
            assertEquals(35, julian.until(iso.plusDays(35), DAYS));
            assertEquals(-40, julian.until(iso.minusDays(40), DAYS));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("String Representation")
    class ToStringTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideDatesAndStringRepresentations")
        @DisplayName("toString() should return correctly formatted string")
        void toString_shouldReturnFormattedString(JulianDate julian, String expected) {
            assertEquals(expected, julian.toString());
        }
    }

    //-----------------------------------------------------------------------
    // Data Providers for specific tests
    //-----------------------------------------------------------------------

    static Stream<Arguments> provideInvalidDateParts() {
        return Stream.of(
            Arguments.of(1900, 0, 0), Arguments.of(1900, -1, 1), Arguments.of(1900, 0, 1),
            Arguments.of(1900, 13, 1), Arguments.of(1900, 14, 1), Arguments.of(1900, 1, -1),
            Arguments.of(1900, 1, 0), Arguments.of(1900, 1, 32), Arguments.of(1900, 2, 30),
            Arguments.of(1899, 2, 29), Arguments.of(1900, 4, 31), Arguments.of(1900, 6, 31),
            Arguments.of(1900, 9, 31), Arguments.of(1900, 11, 31)
        );
    }

    static Stream<Arguments> provideYearMonthAndLength() {
        return Stream.of(
            Arguments.of(1900, 1, 31), Arguments.of(1900, 2, 29), Arguments.of(1900, 3, 31),
            Arguments.of(1900, 4, 30), Arguments.of(1900, 5, 31), Arguments.of(1900, 6, 30),
            Arguments.of(1900, 7, 31), Arguments.of(1900, 8, 31), Arguments.of(1900, 9, 30),
            Arguments.of(1900, 10, 31), Arguments.of(1900, 11, 30), Arguments.of(1900, 12, 31),
            Arguments.of(1901, 2, 28), Arguments.of(1904, 2, 29), Arguments.of(2000, 2, 29)
        );
    }

    static Stream<Arguments> provideDateAndFieldRanges() {
        return Stream.of(
            Arguments.of(2012, 1, 23, DAY_OF_MONTH, 1, 31), Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29),
            Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28), Arguments.of(2012, 1, 23, DAY_OF_YEAR, 1, 366),
            Arguments.of(2011, 2, 23, DAY_OF_YEAR, 1, 365), Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
            Arguments.of(2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4)
        );
    }

    static Stream<Arguments> provideDateAndFieldValues() {
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7L), Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 146L), Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5L),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L), Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6L),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L), Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 24172L), Arguments.of(2014, 5, 26, YEAR, 2014L),
            Arguments.of(2014, 5, 26, ERA, 1L), Arguments.of(0, 6, 8, ERA, 0L),
            Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7L)
        );
    }

    static Stream<Arguments> provideDateWithFieldAdjustments() {
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 24158, 2013, 3, 26),
            Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
            Arguments.of(2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26),
            Arguments.of(2014, 5, 26, ERA, 0, -2013, 5, 26),
            Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28),
            Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29),
            Arguments.of(2012, 2, 29, YEAR, 2011, 2011, 2, 28),
            Arguments.of(-2013, 6, 8, YEAR_OF_ERA, 2012, -2011, 6, 8)
        );
    }

    static Stream<Arguments> provideDatePlusUnitAndAmount() {
        return Stream.of(
            Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26), Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
            Arguments.of(2014, 5, 26, -3, DAYS, 2014, 5, 23), Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
            Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26), Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
            Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26), Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
            Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26), Arguments.of(2014, 5, 26, -1, ERAS, -2013, 5, 26)
        );
    }

    static Stream<Arguments> provideDatePairsAndUntilValues() {
        return Stream.of(
            Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0L), Arguments.of(2014, 5, 26, 2014, 6, 1, DAYS, 6L),
            Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6L), Arguments.of(2014, 5, 26, 2014, 6, 2, WEEKS, 1L),
            Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L), Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
            Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L), Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
            Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L), Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1L)
        );
    }

    static Stream<Arguments> provideDatesAndStringRepresentations() {
        return Stream.of(
            Arguments.of(JulianDate.of(1, 1, 1), "Julian AD 1-01-01"),
            Arguments.of(JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23")
        );
    }

    static Stream<Arguments> provideLeapYearData() {
        return Stream.of(
            Arguments.of(8L, true), Arguments.of(7L, false), Arguments.of(6L, false), Arguments.of(5L, false),
            Arguments.of(4L, true), Arguments.of(3L, false), Arguments.of(2L, false), Arguments.of(1L, false),
            Arguments.of(0L, true), Arguments.of(-1L, false), Arguments.of(-2L, false), Arguments.of(-3L, false),
            Arguments.of(-4L, true), Arguments.of(-5L, false), Arguments.of(-6L, false)
        );
    }
}