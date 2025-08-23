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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link JulianChronology} and {@link JulianDate}.
 * This class focuses on conversions, arithmetic, and field/unit interactions.
 */
@DisplayName("JulianChronology and JulianDate")
class JulianChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleJulianAndIsoDatesProvider() {
        return Stream.of(
                // Year 1 in Julian is year 0 in ISO
                Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
                Arguments.of(JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
                Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
                // Non-leap year in Julian (and ISO)
                Arguments.of(JulianDate.of(1, 2, 28), LocalDate.of(1, 2, 26)),
                Arguments.of(JulianDate.of(1, 3, 1), LocalDate.of(1, 2, 27)),
                Arguments.of(JulianDate.of(1, 3, 2), LocalDate.of(1, 2, 28)),
                Arguments.of(JulianDate.of(1, 3, 3), LocalDate.of(1, 3, 1)),
                // Julian leap year (year 4)
                Arguments.of(JulianDate.of(4, 2, 28), LocalDate.of(4, 2, 26)),
                Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
                Arguments.of(JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)),
                Arguments.of(JulianDate.of(4, 3, 2), LocalDate.of(4, 2, 29)),
                Arguments.of(JulianDate.of(4, 3, 3), LocalDate.of(4, 3, 1)),
                // Julian leap year (year 100), which is not a leap year in Gregorian
                Arguments.of(JulianDate.of(100, 2, 28), LocalDate.of(100, 2, 26)),
                Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)),
                Arguments.of(JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
                Arguments.of(JulianDate.of(100, 3, 2), LocalDate.of(100, 3, 1)),
                Arguments.of(JulianDate.of(100, 3, 3), LocalDate.of(100, 3, 2)),
                // Year 0 (BC)
                Arguments.of(JulianDate.of(0, 12, 31), LocalDate.of(0, 12, 29)),
                Arguments.of(JulianDate.of(0, 12, 30), LocalDate.of(0, 12, 28)),
                // Gregorian calendar adoption date
                Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
                Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
                // Modern dates
                Arguments.of(JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)),
                Arguments.of(JulianDate.of(2012, 6, 22), LocalDate.of(2012, 7, 5)),
                Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion to/from ISO")
    class ConversionTests {

        @ParameterizedTest(name = "{index}: Julian {0} -> ISO {1}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDatesProvider")
        void localDateFromJulianDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(julianDate));
        }

        @ParameterizedTest(name = "{index}: ISO {1} -> Julian {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDatesProvider")
        void julianDateFromLocalDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianDate.from(isoDate));
        }

        @ParameterizedTest(name = "{index}: Julian {0} -> Epoch Day {1}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDatesProvider")
        void toEpochDay(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), julianDate.toEpochDay());
        }

        @ParameterizedTest(name = "{index}: Epoch Day for ISO {1} -> Julian {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDatesProvider")
        void chronologyDateFromEpochDay(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest(name = "{index}: Julian {0} from ISO {1}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDatesProvider")
        void chronologyDateFromTemporal(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.date(isoDate));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Arithmetic")
    class ArithmeticTests {

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDatesProvider")
        void plusDays(JulianDate julianDate, LocalDate isoDate) {
            assertAll("Adding days to " + julianDate,
                    () -> assertEquals(isoDate, LocalDate.from(julianDate.plus(0, DAYS)), "Adding 0 days"),
                    () -> assertEquals(isoDate.plusDays(1), LocalDate.from(julianDate.plus(1, DAYS)), "Adding 1 day"),
                    () -> assertEquals(isoDate.plusDays(35), LocalDate.from(julianDate.plus(35, DAYS)), "Adding 35 days"),
                    () -> assertEquals(isoDate.plusDays(-1), LocalDate.from(julianDate.plus(-1, DAYS)), "Adding -1 day"),
                    () -> assertEquals(isoDate.plusDays(-60), LocalDate.from(julianDate.plus(-60, DAYS)), "Adding -60 days")
            );
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDatesProvider")
        void minusDays(JulianDate julianDate, LocalDate isoDate) {
            assertAll("Subtracting days from " + julianDate,
                    () -> assertEquals(isoDate, LocalDate.from(julianDate.minus(0, DAYS)), "Subtracting 0 days"),
                    () -> assertEquals(isoDate.minusDays(1), LocalDate.from(julianDate.minus(1, DAYS)), "Subtracting 1 day"),
                    () -> assertEquals(isoDate.minusDays(35), LocalDate.from(julianDate.minus(35, DAYS)), "Subtracting 35 days"),
                    () -> assertEquals(isoDate.minusDays(-1), LocalDate.from(julianDate.minus(-1, DAYS)), "Subtracting -1 day"),
                    () -> assertEquals(isoDate.minusDays(-60), LocalDate.from(julianDate.minus(-60, DAYS)), "Subtracting -60 days")
            );
        }

        static Stream<Arguments> plusProvider() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26),
                    Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
                    Arguments.of(2014, 5, 26, -3, DAYS, 2014, 5, 23),
                    Arguments.of(2014, 5, 26, 0, WEEKS, 2014, 5, 26),
                    Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                    Arguments.of(2014, 5, 26, -5, WEEKS, 2014, 4, 21),
                    Arguments.of(2014, 5, 26, 0, MONTHS, 2014, 5, 26),
                    Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                    Arguments.of(2014, 5, 26, -5, MONTHS, 2013, 12, 26),
                    Arguments.of(2014, 5, 26, 0, YEARS, 2014, 5, 26),
                    Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                    Arguments.of(2014, 5, 26, -5, YEARS, 2009, 5, 26),
                    Arguments.of(2014, 5, 26, 0, DECADES, 2014, 5, 26),
                    Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                    Arguments.of(2014, 5, 26, -5, DECADES, 1964, 5, 26),
                    Arguments.of(2014, 5, 26, 0, CENTURIES, 2014, 5, 26),
                    Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                    Arguments.of(2014, 5, 26, -5, CENTURIES, 1514, 5, 26),
                    Arguments.of(2014, 5, 26, 0, MILLENNIA, 2014, 5, 26),
                    Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                    Arguments.of(2014, 5, 26, -5, MILLENNIA, 2014 - 5000, 5, 26),
                    Arguments.of(2014, 5, 26, -1, ERAS, -2013, 5, 26)
            );
        }

        @ParameterizedTest(name = "{index}: {0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("plusProvider")
        void plus(int year, int month, int day, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDay) {
            JulianDate start = JulianDate.of(year, month, day);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{index}: {5}-{6}-{7} minus {3} {4} -> {0}-{1}-{2}")
        @MethodSource("plusProvider")
        void minus(int expectedYear, int expectedMonth, int expectedDay, long amount, TemporalUnit unit, int year, int month, int day) {
            JulianDate start = JulianDate.of(year, month, day);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.minus(amount, unit));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Period Calculation (until)")
    class PeriodCalculationTests {

        @Test
        void until_self_returnsZeroPeriod() {
            JulianDate date = JulianDate.of(2012, 6, 22);
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        @ParameterizedTest(name = "{index}: {0} until equivalent ISO date is zero")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDatesProvider")
        void until_equivalentLocalDate_isZero(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(isoDate));
        }

        @ParameterizedTest(name = "{index}: ISO {1} until equivalent Julian date is zero")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDatesProvider")
        void localDate_until_equivalentJulianDate_isZero(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(julianDate));
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDatesProvider")
        void until_days(JulianDate julianDate, LocalDate isoDate) {
            assertAll("Calculating days until from " + julianDate,
                    () -> assertEquals(0, julianDate.until(isoDate.plusDays(0), DAYS)),
                    () -> assertEquals(1, julianDate.until(isoDate.plusDays(1), DAYS)),
                    () -> assertEquals(35, julianDate.until(isoDate.plusDays(35), DAYS)),
                    () -> assertEquals(-40, julianDate.until(isoDate.minusDays(40), DAYS))
            );
        }

        static Stream<Arguments> untilProvider() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0),
                    Arguments.of(2014, 5, 26, 2014, 6, 1, DAYS, 6),
                    Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6),
                    Arguments.of(2014, 5, 26, 2014, 5, 26, WEEKS, 0),
                    Arguments.of(2014, 5, 26, 2014, 6, 1, WEEKS, 0),
                    Arguments.of(2014, 5, 26, 2014, 6, 2, WEEKS, 1),
                    Arguments.of(2014, 5, 26, 2014, 5, 26, MONTHS, 0),
                    Arguments.of(2014, 5, 26, 2014, 6, 25, MONTHS, 0),
                    Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1),
                    Arguments.of(2014, 5, 26, 2014, 5, 26, YEARS, 0),
                    Arguments.of(2014, 5, 26, 2015, 5, 25, YEARS, 0),
                    Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1),
                    Arguments.of(2014, 5, 26, 2014, 5, 26, DECADES, 0),
                    Arguments.of(2014, 5, 26, 2024, 5, 25, DECADES, 0),
                    Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1),
                    Arguments.of(2014, 5, 26, 2014, 5, 26, CENTURIES, 0),
                    Arguments.of(2014, 5, 26, 2114, 5, 25, CENTURIES, 0),
                    Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1),
                    Arguments.of(2014, 5, 26, 2014, 5, 26, MILLENNIA, 0),
                    Arguments.of(2014, 5, 26, 3014, 5, 25, MILLENNIA, 0),
                    Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1),
                    Arguments.of(-2013, 5, 26, 0, 5, 26, ERAS, 0),
                    Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1)
            );
        }

        @ParameterizedTest(name = "{index}: {0}-{1}-{2} until {3}-{4}-{5} in {6} is {7}")
        @MethodSource("untilProvider")
        void until_temporalUnit(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate end = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Factory Methods and Validation")
    class FactoryAndValidationTests {

        static Stream<Arguments> badDatesProvider() {
            return Stream.of(
                    Arguments.of(1900, 0, 0),
                    Arguments.of(1900, -1, 1),
                    Arguments.of(1900, 0, 1),
                    Arguments.of(1900, 13, 1),
                    Arguments.of(1900, 14, 1),
                    Arguments.of(1900, 1, -1),
                    Arguments.of(1900, 1, 0),
                    Arguments.of(1900, 1, 32),
                    Arguments.of(1900, 2, 30), // 1900 is a leap year in Julian
                    Arguments.of(1899, 2, 29), // 1899 is not a leap year
                    Arguments.of(1900, 4, 31)
            );
        }

        @ParameterizedTest(name = "{index}: {0}-{1}-{2}")
        @MethodSource("badDatesProvider")
        void of_invalidDate_throwsException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dayOfMonth));
        }

        @Test
        void chronology_dateYearDay_invalidDay_throwsException() {
            assertThrows(DateTimeException.class, () -> JulianChronology.INSTANCE.dateYearDay(2001, 366));
        }

        static Stream<Arguments> lengthOfMonthProvider() {
            return Stream.of(
                    Arguments.of(1900, 1, 31),
                    Arguments.of(1900, 2, 29), // 1900 is a leap year in Julian
                    Arguments.of(1900, 3, 31),
                    Arguments.of(1900, 4, 30),
                    Arguments.of(1900, 12, 31),
                    Arguments.of(1901, 2, 28), // Not a leap year
                    Arguments.of(1904, 2, 29), // Leap year
                    Arguments.of(2000, 2, 29), // Leap year
                    Arguments.of(2100, 2, 29)  // Leap year in Julian
            );
        }

        @ParameterizedTest(name = "{index}: {0}-{1} has {2} days")
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth(int year, int month, int length) {
            assertEquals(length, JulianDate.of(year, month, 1).lengthOfMonth());
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field and Range Access")
    class FieldAndRangeTests {

        static Stream<Arguments> rangeProvider() {
            return Stream.of(
                    Arguments.of(2012, 1, 23, DAY_OF_MONTH, 1, 31),
                    Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29), // 2012 is a leap year
                    Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28), // 2011 is not a leap year
                    Arguments.of(2012, 4, 23, DAY_OF_MONTH, 1, 30),
                    Arguments.of(2012, 1, 23, DAY_OF_YEAR, 1, 366),
                    Arguments.of(2011, 2, 23, DAY_OF_YEAR, 1, 365),
                    Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
                    Arguments.of(2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4)
            );
        }

        @ParameterizedTest(name = "{index}: range of {3} for {0}-{1}-{2} is {4}-{5}")
        @MethodSource("rangeProvider")
        void range(int year, int month, int day, TemporalField field, int expectedMin, int expectedMax) {
            JulianDate date = JulianDate.of(year, month, day);
            assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
        }

        static Stream<Arguments> getLongProvider() {
            // May 26th is day 146 in a non-leap year (31+28+31+30+26)
            int dayOfYear2014May26 = 31 + 28 + 31 + 30 + 26;
            // Proleptic month is (year * 12) + month - 1
            long prolepticMonth2014May = 2014 * 12L + 5 - 1;

            return Stream.of(
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7),
                    Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                    Arguments.of(2014, 5, 26, DAY_OF_YEAR, dayOfYear2014May26),
                    Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5),
                    Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
                    Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6),
                    Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21),
                    Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                    Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, prolepticMonth2014May),
                    Arguments.of(2014, 5, 26, YEAR, 2014),
                    Arguments.of(2014, 5, 26, ERA, 1), // AD
                    Arguments.of(1, 6, 8, ERA, 1),     // AD
                    Arguments.of(0, 6, 8, ERA, 0),     // BC
                    Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7)
            );
        }

        @ParameterizedTest(name = "{index}: getLong({3}) on {0}-{1}-{2} is {4}")
        @MethodSource("getLongProvider")
        void getLong(int year, int month, int day, TemporalField field, long expected) {
            JulianDate date = JulianDate.of(year, month, day);
            assertEquals(expected, date.getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Modification (with)")
    class ModificationTests {

        static Stream<Arguments> withProvider() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22),
                    Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31),
                    Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31),
                    Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
                    Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9),
                    Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26),
                    Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12L + 3 - 1, 2013, 3, 26),
                    Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                    Arguments.of(2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26),
                    Arguments.of(2014, 5, 26, ERA, 0, -2013, 5, 26), // Switch to BC era
                    Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28), // Adjust to shorter month
                    Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29), // Adjust to shorter leap month
                    Arguments.of(2012, 2, 29, YEAR, 2011, 2011, 2, 28), // Adjust leap day to non-leap year
                    Arguments.of(-2013, 6, 8, YEAR_OF_ERA, 2012, -2011, 6, 8)
            );
        }

        @ParameterizedTest(name = "{index}: {0}-{1}-{2} with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("withProvider")
        void with(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            JulianDate start = JulianDate.of(y, m, d);
            JulianDate expected = JulianDate.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("String Representation")
    class ToStringTests {

        static Stream<Arguments> toStringProvider() {
            return Stream.of(
                    Arguments.of(JulianDate.of(1, 1, 1), "Julian AD 1-01-01"),
                    Arguments.of(JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23")
            );
        }

        @ParameterizedTest(name = "{index}: {0} -> \"{1}\"")
        @MethodSource("toStringProvider")
        void test_toString(JulianDate julianDate, String expected) {
            assertEquals(expected, julianDate.toString());
        }
    }
}