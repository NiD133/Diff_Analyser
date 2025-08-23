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
import java.time.Month;
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
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link JulianDate} class.
 */
@DisplayName("JulianDate")
class JulianDateTest {

    /**
     * Provides pairs of equivalent Julian and ISO dates for conversion testing.
     *
     * @return a stream of arguments: (JulianDate, LocalDate)
     */
    static Stream<Arguments> provideDatePairs() {
        return Stream.of(
            Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)), // Julian leap year
            Arguments.of(JulianDate.of(4, 3, 3), LocalDate.of(4, 3, 1)),
            Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)), // Julian leap year, not Gregorian
            Arguments.of(JulianDate.of(100, 3, 2), LocalDate.of(100, 3, 1)),
            Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)), // Day before Gregorian cutover
            Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)), // Day of Gregorian cutover
            Arguments.of(JulianDate.of(2012, 6, 22), LocalDate.of(2012, 7, 5))
        );
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Factory and Validation")
    class FactoryAndValidation {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideDatePairs")
        @DisplayName("JulianDate.from(LocalDate) should create the correct date")
        void from_localDate_shouldCreateCorrectJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideDatePairs")
        @DisplayName("JulianChronology.INSTANCE.date(LocalDate) should create the correct date")
        void chronology_dateFromTemporal_shouldCreateCorrectJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.date(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideDatePairs")
        @DisplayName("JulianChronology.INSTANCE.dateEpochDay() should create the correct date")
        void chronology_dateFromEpochDay_shouldCreateCorrectJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @CsvSource({
            "1900,  0,  1",  // Invalid month (zero)
            "1900, 13,  1",  // Invalid month (too high)
            "1900,  1,  0",  // Invalid day (zero)
            "1900,  1, 32",  // Invalid day (too high for month)
            "1900,  2, 30",  // Invalid day (February in Julian leap year)
            "1899,  2, 29",  // Invalid day (February in common year)
            "1900,  4, 31"   // Invalid day (31 in a 30-day month)
        })
        @DisplayName("of() with invalid date parts should throw DateTimeException")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, day));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversions to ISO")
    class ConversionsToIso {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideDatePairs")
        @DisplayName("LocalDate.from(JulianDate) should create the correct ISO date")
        void localDateFrom_shouldCreateCorrectIsoDate(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideDatePairs")
        @DisplayName("toEpochDay() should return the correct epoch day")
        void toEpochDay_shouldReturnCorrectEpochDay(JulianDate julian, LocalDate iso) {
            assertEquals(iso.toEpochDay(), julian.toEpochDay());
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Queries")
    class Queries {

        @ParameterizedTest(name = "Year {0}, Month {1} -> Length {2}")
        @CsvSource({
            "1900,  1, 31",
            "1900,  2, 29", // 1900 is a leap year in Julian calendar
            "1900,  3, 31",
            "1900,  4, 30",
            "1901,  2, 28",
            "1904,  2, 29",
            "2000,  2, 29",
            "2100,  2, 29"  // 2100 is a leap year in Julian calendar
        })
        @DisplayName("lengthOfMonth() should return the correct length for the month")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }

        // Data for range() tests
        static Stream<Arguments> provideRangeData() {
            return Stream.of(
                // year, month, day, field, min, max
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
        @MethodSource("provideRangeData")
        @DisplayName("range() for a given field should return the correct value range")
        void range_forGivenField_shouldReturnCorrectRange(int y, int m, int d, TemporalField field, long min, long max) {
            assertEquals(ValueRange.of(min, max), JulianDate.of(y, m, d).range(field));
        }

        // Data for getLong() tests
        static Stream<Arguments> provideGetLongData() {
            return Stream.of(
                // year, month, day, field, expected value
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7), // A Sunday
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12L + 4),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                Arguments.of(0, 6, 8, ERA, 0),
                Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7)
            );
        }

        @ParameterizedTest
        @MethodSource("provideGetLongData")
        @DisplayName("getLong() for a given field should return the correct value")
        void getLong_forGivenField_shouldReturnCorrectValue(int y, int m, int d, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(y, m, d).getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Adjustments")
    class Adjustments {

        // Data for with() tests
        static Stream<Arguments> provideWithData() {
            return Stream.of(
                // start Y, M, D, field, new value, expected Y, M, D
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2014, 5, 26, ERA, 0, -2013, 5, 26),
                Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28), // Adjust to shorter month
                Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29), // Adjust to shorter month (leap)
                Arguments.of(2012, 2, 29, YEAR, 2011, 2011, 2, 28)      // Adjust leap day to common year
            );
        }

        @ParameterizedTest
        @MethodSource("provideWithData")
        @DisplayName("with() should return a correctly adjusted date")
        void with_shouldReturnAdjustedDate(int y, int m, int d, TemporalField field, long val, int ey, int em, int ed) {
            JulianDate start = JulianDate.of(y, m, d);
            JulianDate expected = JulianDate.of(ey, em, ed);
            assertEquals(expected, start.with(field, val));
        }

        @Test
        @DisplayName("with(Month) should throw UnsupportedTemporalTypeException")
        void with_isoMonth_shouldThrowException() {
            JulianDate julian = JulianDate.of(2000, 1, 4);
            assertThrows(UnsupportedTemporalTypeException.class, () -> julian.with(Month.APRIL));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Arithmetic")
    class Arithmetic {

        // Data for plus() and minus() tests
        static Stream<Arguments> provideArithmeticData() {
            return Stream.of(
                // start Y, M, D, amount, unit, expected Y, M, D
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                Arguments.of(2014, 5, 26, 1, ERAS, -2013, 5, 26)
            );
        }

        @ParameterizedTest
        @MethodSource("provideArithmeticData")
        @DisplayName("plus() should return a correctly advanced date")
        void plus_shouldReturnAdvancedDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            JulianDate start = JulianDate.of(y, m, d);
            JulianDate expected = JulianDate.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("provideArithmeticData")
        @DisplayName("minus() should return a correctly rewound date")
        void minus_shouldReturnRewoundDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            JulianDate expected = JulianDate.of(y, m, d);
            JulianDate start = JulianDate.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }

        // Data for until() tests
        static Stream<Arguments> provideUntilData() {
            return Stream.of(
                // start Y, M, D, end Y, M, D, unit, expected amount
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

        @ParameterizedTest
        @MethodSource("provideUntilData")
        @DisplayName("until() should calculate the correct amount of time between dates")
        void until_shouldCalculateCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate end = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @Test
        @DisplayName("until(self) should return a zero period")
        void until_self_shouldReturnZeroPeriod() {
            JulianDate date = JulianDate.of(2014, 5, 26);
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), date.until(date));
        }
        
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideDatePairs")
        @DisplayName("until(equivalent LocalDate) should return a zero period")
        void until_equivalentLocalDate_shouldReturnZeroPeriod(JulianDate julian, LocalDate iso) {
            assertEquals(Period.ZERO, julian.until(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#provideDatePairs")
        @DisplayName("LocalDate.until(equivalent JulianDate) should return a zero period")
        void localDate_until_equivalentJulianDate_shouldReturnZeroPeriod(JulianDate julian, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(julian));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Formatting")
    class Formatting {

        @ParameterizedTest(name = "of({0}, {1}, {2}) -> \"{3}\"")
        @CsvSource({
            "1, 1, 1, 'Julian AD 1-01-01'",
            "2012, 6, 23, 'Julian AD 2012-06-23'",
            "0, 12, 31, 'Julian BC 1-12-31'"
        })
        @DisplayName("toString() should return the correct standard representation")
        void toString_shouldReturnFormattedString(int year, int month, int day, String expected) {
            assertEquals(expected, JulianDate.of(year, month, day).toString());
        }
    }
}