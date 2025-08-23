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
 * Tests for the {@link JulianChronology} and {@link JulianDate}.
 * This class focuses on conversions, arithmetic, and field access.
 */
@DisplayName("JulianChronology and JulianDate")
public class JulianChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> provideJulianAndIsoDatePairs() {
        return Stream.of(
            // Arguments.of(julianDate, equivalentIsoDate)
            Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            // Julian leap year
            Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
            Arguments.of(JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)),
            Arguments.of(JulianDate.of(4, 3, 2), LocalDate.of(4, 2, 29)),
            // Julian leap year, but not Gregorian
            Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)),
            Arguments.of(JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
            // Year zero
            Arguments.of(JulianDate.of(0, 12, 31), LocalDate.of(0, 12, 29)),
            // Near Gregorian cutover
            Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            // Modern dates
            Arguments.of(JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)),
            Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion Tests")
    class ConversionTests {

        @DisplayName("should be equivalent to its corresponding ISO date")
        @ParameterizedTest(name = "{index}: {0} <-> {1}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideJulianAndIsoDatePairs")
        void julianAndIsoDatesShouldBeEquivalent(JulianDate julian, LocalDate iso) {
            assertAll(
                () -> assertEquals(iso, LocalDate.from(julian), "LocalDate.from(julianDate)"),
                () -> assertEquals(julian, JulianDate.from(iso), "JulianDate.from(isoDate)"),
                () -> assertEquals(iso.toEpochDay(), julian.toEpochDay(), "toEpochDay()"),
                () -> assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()), "chronology.dateEpochDay()"),
                () -> assertEquals(julian, JulianChronology.INSTANCE.date(iso), "chronology.date(temporal)")
            );
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Arithmetic Tests")
    class ArithmeticTests {

        @DisplayName("should correctly add and subtract days")
        @ParameterizedTest(name = "{index}: on {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideJulianAndIsoDatePairs")
        void plusAndMinusDays(JulianDate julian, LocalDate iso) {
            long[] daysToTest = {0, 1, 35, -1, -60};
            for (long days : daysToTest) {
                assertAll(
                    () -> assertEquals(iso.plusDays(days), LocalDate.from(julian.plus(days, DAYS)), "plus " + days + " days"),
                    () -> assertEquals(iso.minusDays(days), LocalDate.from(julian.minus(days, DAYS)), "minus " + days + " days")
                );
            }
        }

        @DisplayName("should calculate 'until' in days correctly")
        @ParameterizedTest(name = "{index}: from {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideJulianAndIsoDatePairs")
        void untilInDays(JulianDate julian, LocalDate iso) {
            long[] daysToAdd = {0, 1, 35, -40};
            for (long days : daysToAdd) {
                LocalDate otherIso = iso.plusDays(days);
                assertEquals(days, julian.until(otherIso, DAYS), "until " + otherIso);
            }
        }

        @DisplayName("should return a zero period when 'until' is called on self or equivalent date")
        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#provideJulianAndIsoDatePairs")
        void untilSelfOrEquivalentIsZero(JulianDate julian, LocalDate iso) {
            assertAll(
                () -> assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(julian), "until(self)"),
                () -> assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(iso), "until(equivalent ISO date)"),
                () -> assertEquals(Period.ZERO, iso.until(julian), "iso.until(julianDate)")
            );
        }

        static Stream<Arguments> provideArithmeticData() {
            // startYear, startMonth, startDay, amount, unit, expectedYear, expectedMonth, expectedDay
            return Stream.of(
                Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
                Arguments.of(2014, 5, 26, -3, DAYS, 2014, 5, 23),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, -5, MONTHS, 2013, 12, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, -5, YEARS, 2009, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                Arguments.of(2014, 5, 26, -1, ERAS, -2013, 5, 26)
            );
        }

        @DisplayName("plus(amount, unit) should return the correct date")
        @ParameterizedTest(name = "{index}: {0}-{1}-{2} plus {3} {4}")
        @MethodSource("provideArithmeticData")
        void plusAmountShouldReturnCorrectDate(int year, int month, int day, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDay) {
            JulianDate start = JulianDate.of(year, month, day);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        @DisplayName("minus(amount, unit) should return the correct date")
        @ParameterizedTest(name = "{index}: {5}-{6}-{7} minus {3} {4}")
        @MethodSource("provideArithmeticData")
        void minusAmountShouldReturnCorrectDate(int expectedYear, int expectedMonth, int expectedDay, long amount, TemporalUnit unit, int year, int month, int day) {
            JulianDate start = JulianDate.of(year, month, day);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Stream<Arguments> provideUntilData() {
            // y1, m1, d1, y2, m2, d2, unit, expectedAmount
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0L),
                Arguments.of(2014, 5, 26, 2014, 6, 1, DAYS, 6L),
                Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6L),
                Arguments.of(2014, 5, 26, 2014, 6, 2, WEEKS, 1L),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
                Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1L)
            );
        }

        @DisplayName("until(endDate, unit) should calculate the correct amount of time")
        @ParameterizedTest(name = "{index}: from {0}-{1}-{2} to {3}-{4}-{5} in {6}")
        @MethodSource("provideUntilData")
        void untilShouldCalculateCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate end = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @Test
        @DisplayName("minus(Period) should throw exception for ISO-specific periods")
        void minusIsoPeriodShouldThrowException() {
            JulianDate date = JulianDate.of(2014, 5, 26);
            // JulianDate does not support subtracting an ISO Period containing months or years.
            assertThrows(UnsupportedTemporalTypeException.class, () -> date.minus(Period.ofMonths(2)));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Creation and Properties")
    class CreationAndPropertiesTests {

        static Stream<Arguments> provideInvalidDateComponents() {
            // year, month, dayOfMonth
            return Stream.of(
                Arguments.of(1900, 0, 1),   // Invalid month
                Arguments.of(1900, 13, 1),  // Invalid month
                Arguments.of(1900, 1, 0),   // Invalid day
                Arguments.of(1900, 1, 32),  // Invalid day for Jan
                Arguments.of(1900, 2, 30),  // Invalid day for Feb (leap)
                Arguments.of(1899, 2, 29),  // Invalid day for Feb (non-leap)
                Arguments.of(1900, 4, 31)   // Invalid day for Apr
            );
        }

        @DisplayName("of(year, month, day) should throw for invalid dates")
        @ParameterizedTest(name = "{index}: {0}-{1}-{2}")
        @MethodSource("provideInvalidDateComponents")
        void ofWithInvalidDateComponentsShouldThrowException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, day));
        }

        static Stream<Arguments> provideYearMonthAndExpectedLength() {
            // year, month, expectedLength
            return Stream.of(
                Arguments.of(1900, 1, 31),
                Arguments.of(1900, 2, 29), // Julian leap year
                Arguments.of(1901, 2, 28),
                Arguments.of(1904, 2, 29),
                Arguments.of(2000, 2, 29),
                Arguments.of(2100, 2, 29) // Julian leap year, not Gregorian
            );
        }

        @DisplayName("lengthOfMonth() should return the correct number of days")
        @ParameterizedTest(name = "{index}: {0}-{1} -> {2} days")
        @MethodSource("provideYearMonthAndExpectedLength")
        void lengthOfMonthShouldReturnCorrectValue(int year, int month, int length) {
            assertEquals(length, JulianDate.of(year, month, 1).lengthOfMonth());
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field and Range Access")
    class FieldAndRangeTests {

        static Stream<Arguments> provideDateAndFieldForRange() {
            // year, month, day, field, expectedMin, expectedMax
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, 1, 31),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29), // Leap year
                Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28), // Non-leap year
                Arguments.of(2012, 4, 23, DAY_OF_MONTH, 1, 30),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, 1, 366), // Leap year
                Arguments.of(2011, 1, 23, DAY_OF_YEAR, 1, 365), // Non-leap year
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
                Arguments.of(2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4)
            );
        }

        @DisplayName("range(field) should return correct value range")
        @ParameterizedTest(name = "{index}: range of {3} for {0}-{1}-{2}")
        @MethodSource("provideDateAndFieldForRange")
        void rangeOfFieldShouldBeCorrect(int year, int month, int day, TemporalField field, long expectedMin, long expectedMax) {
            JulianDate date = JulianDate.of(year, month, day);
            assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
        }

        static Stream<Arguments> provideDateAndFieldForGetLong() {
            // year, month, day, field, expectedValue
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7L),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 146L), // 31+28+31+30+26
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014L * 12 + 4),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),
                Arguments.of(0, 6, 8, ERA, 0L),
                Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7L)
            );
        }

        @DisplayName("getLong(field) should return the correct value")
        @ParameterizedTest(name = "{index}: get {3} from {0}-{1}-{2}")
        @MethodSource("provideDateAndFieldForGetLong")
        void getLongForFieldShouldReturnCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, day).getLong(field));
        }

        static Stream<Arguments> provideDateAndFieldForWith() {
            // year, month, day, field, newValue, expectedYear, expectedMonth, expectedDay
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3L, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31L, 2014, 5, 31),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365L, 2014, 12, 31),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7L, 2014, 7, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012L, 2012, 5, 26),
                Arguments.of(2014, 5, 26, ERA, 0L, -2013, 5, 26),
                // Adjusting to a month with fewer days
                Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2L, 2011, 2, 28),
                Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2L, 2012, 2, 29),
                // Adjusting year on a leap day
                Arguments.of(2012, 2, 29, YEAR, 2011L, 2011, 2, 28)
            );
        }

        @DisplayName("with(field, value) should return the correct date")
        @ParameterizedTest(name = "{index}: {0}-{1}-{2} with {3} = {4}")
        @MethodSource("provideDateAndFieldForWith")
        void withFieldValueShouldReturnCorrectDate(int year, int month, int day, TemporalField field, long value, int exY, int exM, int exD) {
            JulianDate start = JulianDate.of(year, month, day);
            JulianDate expected = JulianDate.of(exY, exM, exD);
            assertEquals(expected, start.with(field, value));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Object Behavior")
    class ObjectBehaviorTests {

        static Stream<Arguments> provideDateAndStringRepresentation() {
            return Stream.of(
                Arguments.of(JulianDate.of(1, 1, 1), "Julian AD 1-01-01"),
                Arguments.of(2012, 6, 23, "Julian AD 2012-06-23")
            );
        }

        @DisplayName("toString() should return the correct format")
        @ParameterizedTest(name = "{index}: {1}")
        @MethodSource("provideDateAndStringRepresentation")
        void toStringShouldReturnCorrectFormat(JulianDate date, String expected) {
            assertEquals(expected, date.toString());
        }

        // Overload for convenience
        @DisplayName("toString() should return the correct format")
        @ParameterizedTest(name = "{index}: {1}")
        @MethodSource("provideDateAndStringRepresentation")
        void toStringShouldReturnCorrectFormat(int year, int month, int day, String expected) {
            assertEquals(expected, JulianDate.of(year, month, day).toString());
        }
    }
}