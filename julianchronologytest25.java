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

@DisplayName("JulianDate")
class JulianDateTest {

    /**
     * Provides pairs of equivalent Julian and ISO dates for conversion and comparison tests.
     */
    static Stream<Arguments> provideEquivalentJulianAndIsoDates() {
        return Stream.of(
            Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)), // Julian leap year
            Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)), // Julian leap year, not Gregorian
            Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)), // Day before Gregorian cutover
            Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)), // Day of Gregorian cutover
            Arguments.of(JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)),
            Arguments.of(JulianDate.of(2012, 6, 22), LocalDate.of(2012, 7, 5))
        );
    }

    @Nested
    @DisplayName("Factory and Validation")
    class FactoryAndValidation {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideInvalidDateParts")
        @DisplayName("of(year, month, day) with invalid values throws exception")
        void of_forInvalidDate_throwsDateTimeException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, day));
        }

        private static Stream<Arguments> provideInvalidDateParts() {
            return Stream.of(
                Arguments.of(1900, 0, 1),   // Invalid month
                Arguments.of(1900, 13, 1),  // Invalid month
                Arguments.of(1900, 1, 0),   // Invalid day
                Arguments.of(1900, 1, 32),  // Invalid day
                Arguments.of(1900, 2, 30),  // Invalid day in leap year
                Arguments.of(1899, 2, 29),  // Invalid day in common year
                Arguments.of(1900, 4, 31)   // Invalid day for 30-day month
            );
        }
    }

    @Nested
    @DisplayName("Interoperability with ISO")
    class InteroperabilityWithIso {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideEquivalentJulianAndIsoDates")
        @DisplayName("LocalDate.from(julianDate) returns equivalent ISO date")
        void fromJulianDate_shouldReturnEquivalentIsoDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(julianDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideEquivalentJulianAndIsoDates")
        @DisplayName("JulianDate.from(isoDate) returns equivalent Julian date")
        void fromIsoDate_shouldReturnEquivalentJulianDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideEquivalentJulianAndIsoDates")
        @DisplayName("toEpochDay() returns the same value as its ISO equivalent")
        void toEpochDay_returnsSameValueAsIso(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), julianDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideEquivalentJulianAndIsoDates")
        @DisplayName("chronology.dateEpochDay() creates correct date")
        void chronologyDateEpochDay_createsCorrectDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideEquivalentJulianAndIsoDates")
        @DisplayName("chronology.date(temporal) creates correct date from ISO")
        void chronologyDate_fromIsoDate_returnsCorrectJulianDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Field Access")
    class FieldAccess {

        @ParameterizedTest
        @MethodSource
        @DisplayName("lengthOfMonth() returns correct length")
        void lengthOfMonth_returnsCorrectValueForLeapAndCommonYears(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }

        private static Stream<Arguments> provideMonthLengths() {
            return Stream.of(
                Arguments.of(1900, 1, 31),
                Arguments.of(1900, 2, 29), // Julian leap year
                Arguments.of(1901, 2, 28), // Julian common year
                Arguments.of(1904, 2, 29), // Julian leap year
                Arguments.of(2000, 2, 29), // Julian leap year
                Arguments.of(2100, 2, 29)  // Julian leap year
            );
        }

        @ParameterizedTest
        @MethodSource
        @DisplayName("range(field) returns correct value range")
        void range_forField_returnsCorrectValueRange(int year, int month, int day, TemporalField field, long min, long max) {
            assertEquals(ValueRange.of(min, max), JulianDate.of(year, month, day).range(field));
        }

        private static Stream<Arguments> provideFieldRanges() {
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, 1, 31),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29), // Leap year
                Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28), // Common year
                Arguments.of(2012, 4, 23, DAY_OF_MONTH, 1, 30),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, 1, 366), // Leap year
                Arguments.of(2011, 2, 23, DAY_OF_YEAR, 1, 365), // Common year
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
                Arguments.of(2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4)
            );
        }

        @ParameterizedTest
        @MethodSource
        @DisplayName("getLong(field) returns correct value")
        void getLong_forField_returnsCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, day).getLong(field));
        }

        private static Stream<Arguments> provideFieldGetValues() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12L + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                Arguments.of(0, 6, 8, ERA, 0),
                Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7)
            );
        }
    }

    @Nested
    @DisplayName("Date Arithmetic")
    class DateArithmetic {

        @Test
        @DisplayName("until() with an equivalent date returns a zero period")
        void until_withEquivalentDate_returnsZeroPeriod() {
            JulianDate julianDate = JulianDate.of(2012, 6, 22);
            LocalDate isoDate = LocalDate.of(2012, 7, 5); // Equivalent date

            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(julianDate));
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(julianDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideEquivalentJulianAndIsoDates")
        @DisplayName("plusDays() on sample dates returns correct result")
        void plusDays_onSampleDates_returnsCorrectResult(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(julianDate.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(julianDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(julianDate.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(julianDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(julianDate.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideEquivalentJulianAndIsoDates")
        @DisplayName("minusDays() on sample dates returns correct result")
        void minusDays_onSampleDates_returnsCorrectResult(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(julianDate.minus(0, DAYS)));
            assertEquals(isoDate.minusDays(1), LocalDate.from(julianDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(julianDate.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(julianDate.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(julianDate.minus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource
        @DisplayName("with(field, value) returns correctly adjusted date")
        void with_usingField_returnsAdjustedDate(JulianDate baseDate, TemporalField field, long value, JulianDate expectedDate) {
            assertEquals(expectedDate, baseDate.with(field, value));
        }

        private static Stream<Arguments> provideWithFieldArguments() {
            return Stream.of(
                Arguments.of(JulianDate.of(2014, 5, 26), DAY_OF_WEEK, 3, JulianDate.of(2014, 5, 22)),
                Arguments.of(JulianDate.of(2014, 5, 26), DAY_OF_MONTH, 31, JulianDate.of(2014, 5, 31)),
                Arguments.of(JulianDate.of(2014, 5, 26), DAY_OF_YEAR, 365, JulianDate.of(2014, 12, 31)),
                Arguments.of(JulianDate.of(2014, 5, 26), MONTH_OF_YEAR, 7, JulianDate.of(2014, 7, 26)),
                Arguments.of(JulianDate.of(2014, 5, 26), YEAR, 2012, JulianDate.of(2012, 5, 26)),
                Arguments.of(JulianDate.of(2014, 5, 26), ERA, 0, JulianDate.of(-2013, 5, 26)),
                Arguments.of(JulianDate.of(2012, 3, 31), MONTH_OF_YEAR, 2, JulianDate.of(2012, 2, 29)), // Adjust to end of month
                Arguments.of(JulianDate.of(2012, 2, 29), YEAR, 2011, JulianDate.of(2011, 2, 28)) // Adjust leap to non-leap
            );
        }

        @ParameterizedTest
        @MethodSource("providePlusAndMinusArguments")
        @DisplayName("plus(amount, unit) returns correctly added date")
        void plus_usingUnit_returnsAddedDate(JulianDate startDate, long amount, TemporalUnit unit, JulianDate endDate) {
            assertEquals(endDate, startDate.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("providePlusAndMinusArguments")
        @DisplayName("minus(amount, unit) returns correctly subtracted date")
        void minus_usingUnit_returnsSubtractedDate(JulianDate startDate, long amount, TemporalUnit unit, JulianDate endDate) {
            // Test that endDate - amount = startDate
            assertEquals(startDate, endDate.minus(amount, unit));
        }

        private static Stream<Arguments> providePlusAndMinusArguments() {
            return Stream.of(
                Arguments.of(JulianDate.of(2014, 5, 26), 8L, DAYS, JulianDate.of(2014, 6, 3)),
                Arguments.of(JulianDate.of(2014, 5, 26), 3L, WEEKS, JulianDate.of(2014, 6, 16)),
                Arguments.of(JulianDate.of(2014, 5, 26), 3L, MONTHS, JulianDate.of(2014, 8, 26)),
                Arguments.of(JulianDate.of(2014, 5, 26), 3L, YEARS, JulianDate.of(2017, 5, 26)),
                Arguments.of(JulianDate.of(2014, 5, 26), 3L, DECADES, JulianDate.of(2044, 5, 26)),
                Arguments.of(JulianDate.of(2014, 5, 26), 3L, CENTURIES, JulianDate.of(2314, 5, 26)),
                Arguments.of(JulianDate.of(2014, 5, 26), 3L, MILLENNIA, JulianDate.of(5014, 5, 26)),
                Arguments.of(JulianDate.of(2014, 5, 26), -1L, ERAS, JulianDate.of(-2013, 5, 26))
            );
        }

        @ParameterizedTest
        @MethodSource
        @DisplayName("until(endDate, unit) returns correct duration")
        void until_usingUnit_returnsCorrectDuration(JulianDate start, JulianDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        private static Stream<Arguments> provideUntilArguments() {
            return Stream.of(
                Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2014, 6, 1), DAYS, 6),
                Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2014, 5, 20), DAYS, -6),
                Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2014, 6, 2), WEEKS, 1),
                Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2014, 6, 26), MONTHS, 1),
                Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2015, 5, 26), YEARS, 1),
                Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2024, 5, 26), DECADES, 1),
                Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(2114, 5, 26), CENTURIES, 1),
                Arguments.of(JulianDate.of(2014, 5, 26), JulianDate.of(3014, 5, 26), MILLENNIA, 1),
                Arguments.of(JulianDate.of(-2013, 5, 26), JulianDate.of(2014, 5, 26), ERAS, 1)
            );
        }

        @Test
        @DisplayName("plus(Period) returns correctly added date")
        void plus_withPeriod_returnsAddedDate() {
            JulianDate start = JulianDate.of(2014, 5, 26);
            // Period of 2 months and 3 days
            Period period = JulianChronology.INSTANCE.period(0, 2, 3);
            JulianDate expected = JulianDate.of(2014, 7, 29);
            assertEquals(expected, start.plus(period));
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentation {

        @ParameterizedTest
        @MethodSource
        @DisplayName("toString() returns correct format")
        void toString_returnsCorrectFormat(JulianDate julianDate, String expected) {
            assertEquals(expected, julianDate.toString());
        }

        private static Stream<Arguments> provideToStringArguments() {
            return Stream.of(
                Arguments.of(JulianDate.of(1, 1, 1), "Julian AD 1-01-01"),
                Arguments.of(JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23")
            );
        }
    }
}