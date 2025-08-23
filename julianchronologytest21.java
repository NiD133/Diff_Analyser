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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link JulianChronology} and {@link JulianDate}.
 */
class JulianChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample Julian dates and their equivalent ISO dates.
     * @return A stream of arguments for parameterized tests, where each is a {JulianDate, LocalDate} pair.
     */
    private static Stream<Arguments> sampleJulianAndIsoDates() {
        return Stream.of(
            // Start of Julian calendar
            Arguments.of(JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            // Non-leap year
            Arguments.of(JulianDate.of(1, 3, 1), LocalDate.of(1, 2, 27)),
            // Julian leap year (year 4)
            Arguments.of(JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
            Arguments.of(JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)),
            // Julian leap year, but not Gregorian (year 100)
            Arguments.of(JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)),
            Arguments.of(JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
            // Year 0
            Arguments.of(JulianDate.of(0, 12, 31), LocalDate.of(0, 12, 29)),
            // Gregorian calendar reform date
            Arguments.of(JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            // Modern dates
            Arguments.of(JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)),
            Arguments.of(JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6))
        );
    }

    private static Stream<Arguments> invalidDateProvider() {
        return Stream.of(
            // Invalid month
            Arguments.of(1900, 0, 1),
            Arguments.of(1900, 13, 1),
            // Invalid day of month
            Arguments.of(1900, 1, 0),
            Arguments.of(1900, 1, 32),
            // Invalid day for February in a non-leap year (Julian 1899 is not a leap year)
            Arguments.of(1899, 2, 29),
            // Invalid day for February in a leap year (Julian 1900 is a leap year)
            Arguments.of(1900, 2, 30),
            // Invalid day for April (30 days)
            Arguments.of(1900, 4, 31)
        );
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDates")
        void toLocalDate_shouldReturnCorrespondingIsoDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(julianDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDates")
        void fromLocalDate_shouldReturnCorrespondingJulianDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDates")
        void toEpochDay_shouldBeConsistentWithIso(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), julianDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDates")
        void dateEpochDay_shouldCreateCorrectJulianDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDates")
        void chronologyDate_fromTemporal_shouldCreateCorrectJulianDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    class CreationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#invalidDateProvider")
        void of_forInvalidDate_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dayOfMonth));
        }
    }

    @Nested
    class FieldAccessTests {

        private static Stream<Arguments> monthLengthsProvider() {
            return Stream.of(
                Arguments.of(1900, 1, 31),
                Arguments.of(1900, 2, 29), // Julian leap year
                Arguments.of(1900, 3, 31),
                Arguments.of(1900, 4, 30),
                Arguments.of(1901, 2, 28), // Julian non-leap year
                Arguments.of(1904, 2, 29), // Julian leap year
                Arguments.of(2000, 2, 29)  // Julian leap year
            );
        }

        @ParameterizedTest
        @MethodSource("monthLengthsProvider")
        void lengthOfMonth_shouldReturnCorrectDayCount(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }

        private static Stream<Arguments> fieldRangeProvider() {
            return Stream.of(
                Arguments.of(2012, 1, DAY_OF_MONTH, 1, 31),
                Arguments.of(2012, 2, DAY_OF_MONTH, 1, 29), // Leap year
                Arguments.of(2011, 2, DAY_OF_MONTH, 1, 28), // Non-leap year
                Arguments.of(2012, 4, DAY_OF_MONTH, 1, 30),
                Arguments.of(2012, 1, DAY_OF_YEAR, 1, 366), // Leap year
                Arguments.of(2011, 1, DAY_OF_YEAR, 1, 365), // Non-leap year
                Arguments.of(2012, 2, ALIGNED_WEEK_OF_MONTH, 1, 5),
                Arguments.of(2011, 2, ALIGNED_WEEK_OF_MONTH, 1, 4)
            );
        }

        @ParameterizedTest
        @MethodSource("fieldRangeProvider")
        void range_forField_shouldReturnCorrectValueRange(int year, int month, TemporalField field, int min, int max) {
            JulianDate date = JulianDate.of(year, month, 1);
            assertEquals(ValueRange.of(min, max), date.range(field));
        }

        private static Stream<Arguments> fieldValueProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 7),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                Arguments.of(0, 6, 8, ERA, 0),
                Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7)
            );
        }

        @ParameterizedTest
        @MethodSource("fieldValueProvider")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int dayOfMonth, TemporalField field, long expectedValue) {
            assertEquals(expectedValue, JulianDate.of(year, month, dayOfMonth).getLong(field));
        }
    }

    @Nested
    class ArithmeticTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDates")
        void plusDays_shouldBeConsistentWithIso(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate.plusDays(1), LocalDate.from(julianDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(julianDate.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(julianDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(julianDate.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyTest#sampleJulianAndIsoDates")
        void minusDays_shouldBeConsistentWithIso(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate.minusDays(1), LocalDate.from(julianDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(julianDate.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(julianDate.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(julianDate.minus(-60, DAYS)));
        }

        private static Stream<Arguments> periodAdditionProvider() {
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

        @ParameterizedTest
        @MethodSource("periodAdditionProvider")
        void plus_withTemporalUnit_shouldReturnCorrectlyAddedDate(
            int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay
        ) {
            JulianDate start = JulianDate.of(startYear, startMonth, startDay);
            JulianDate expected = JulianDate.of(endYear, endMonth, endDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("periodAdditionProvider")
        void minus_withTemporalUnit_shouldReturnCorrectlySubtractedDate(
            int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay
        ) {
            JulianDate expected = JulianDate.of(startYear, startMonth, startDay);
            JulianDate start = JulianDate.of(endYear, endMonth, endDay);
            assertEquals(expected, start.minus(amount, unit));
        }

        @Test
        void until_forSameDate_shouldReturnZeroPeriod() {
            JulianDate julianDate = JulianDate.of(2012, 6, 23);
            LocalDate isoDate = LocalDate.of(2012, 7, 6); // Equivalent ISO date

            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(julianDate));
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(julianDate));
        }

        private static Stream<Arguments> untilPeriodProvider() {
            return Stream.of(
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
        @MethodSource("untilPeriodProvider")
        void until_withTemporalUnit_shouldReturnCorrectAmount(
            int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay, TemporalUnit unit, long expectedAmount
        ) {
            JulianDate start = JulianDate.of(startYear, startMonth, startDay);
            JulianDate end = JulianDate.of(endYear, endMonth, endDay);
            assertEquals(expectedAmount, start.until(end, unit));
        }
    }

    @Nested
    class AdjustmentTests {

        private static Stream<Arguments> dateWithFieldProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26),
                Arguments.of(2014, 5, 26, ERA, 0, -2013, 5, 26),
                // Adjusting month to one with fewer days
                Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28),
                Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29), // Leap year
                // Adjusting year of a leap day to a non-leap year
                Arguments.of(2012, 2, 29, YEAR, 2011, 2011, 2, 28)
            );
        }

        @ParameterizedTest
        @MethodSource("dateWithFieldProvider")
        void with_temporalField_shouldReturnAdjustedDate(
            int year, int month, int dayOfMonth, TemporalField field, long newValue, int expYear, int expMonth, int expDay
        ) {
            JulianDate start = JulianDate.of(year, month, dayOfMonth);
            JulianDate expected = JulianDate.of(expYear, expMonth, expDay);
            assertEquals(expected, start.with(field, newValue));
        }

        @Test
        void with_julianDateAdjuster_shouldAdjustLocalDate() {
            JulianDate julianDate = JulianDate.of(2012, 6, 23);
            LocalDate isoDate = LocalDate.of(2012, 7, 6);
            assertEquals(isoDate, LocalDate.MIN.with(julianDate));
        }
    }

    @Nested
    class ObjectMethodTests {

        @Test
        void toString_shouldReturnFormattedString() {
            JulianDate julianDate = JulianDate.of(2012, 6, 23);
            assertEquals("Julian AD 2012-06-23", julianDate.toString());
        }
    }
}