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
import java.time.chrono.ChronoPeriod;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("BritishCutoverDate")
class BritishCutoverDateTest {

    /**
     * Provides sample BritishCutoverDate instances and their equivalent ISO LocalDate.
     *
     * @return a stream of arguments: (BritishCutoverDate, equivalent ISO LocalDate)
     */
    static Stream<Arguments> sampleCutoverAndIsoDates() {
        return Stream.of(
            // Pre-cutover dates (Julian calendar)
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(BritishCutoverDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
            Arguments.of(BritishCutoverDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(BritishCutoverDate.of(1751, 12, 31), LocalDate.of(1752, 1, 11)),
            // Dates around the 1752 cutover
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)),
            // Leniently accept dates that fall within the gap
            Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)),
            // Post-cutover dates (Gregorian calendar)
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)),
            Arguments.of(BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12)),
            Arguments.of(BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6))
        );
    }

    @Nested
    @DisplayName("Factory and Conversion Tests")
    class FactoryAndConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        @DisplayName("from(BritishCutoverDate) should return correct LocalDate")
        void fromBritishCutoverDate_shouldReturnCorrectLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        @DisplayName("from(LocalDate) should return correct BritishCutoverDate")
        void fromLocalDate_shouldReturnCorrectCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        @DisplayName("chronology.dateEpochDay should create correct date")
        void chronologyDateEpochDay_shouldCreateCorrectCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        @DisplayName("toEpochDay should return correct value")
        void toEpochDay_shouldReturnCorrectValue(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        @DisplayName("chronology.date(TemporalAccessor) should create correct date")
        void chronologyDateFromTemporal_shouldCreateCorrectCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Invalid Date Creation Tests")
    class InvalidDateCreationTests {

        @ParameterizedTest
        @CsvSource({
            "1900,  0,  0", "1900, -1,  1", "1900,  0,  1", "1900, 13,  1", "1900, 14,  1",
            "1900,  1, -1", "1900,  1,  0", "1900,  1, 32",
            "1900,  2, 30", "1899,  2, 29", // 1899 is not a leap year
            "1900,  4, 31", "1900,  6, 31", "1900,  9, 31", "1900, 11, 31"
        })
        @DisplayName("of(y, m, d) with invalid components should throw exception")
        void of_withInvalidDateComponents_shouldThrowException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, day));
        }
    }

    @Nested
    @DisplayName("Date Property Tests")
    class DatePropertyTests {

        @ParameterizedTest
        @CsvSource({
            "1700, 1, 31", "1700, 2, 29", // Julian leap year
            "1751, 2, 28",
            "1752, 2, 29", "1752, 9, 19", // Cutover month
            "1753, 2, 28",
            "1800, 2, 28", // Gregorian non-leap year
            "1900, 2, 28", // Gregorian non-leap year
            "2000, 2, 29", // Gregorian leap year
            "2004, 2, 29"
        })
        @DisplayName("lengthOfMonth should return correct value")
        void lengthOfMonth_shouldReturnCorrectValue(int year, int month, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        @ParameterizedTest
        @CsvSource({
            "1600, 366", "1700, 366", // Julian leap years
            "1751, 365",
            "1752, 355", // Cutover year with 11 days removed
            "1753, 365",
            "1800, 365", // Gregorian non-leap year
            "1900, 365", // Gregorian non-leap year
            "2000, 366", // Gregorian leap year
            "2004, 366"
        })
        @DisplayName("lengthOfYear should return correct value")
        void lengthOfYear_shouldReturnCorrectValue(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
            assertEquals(expectedLength, BritishCutoverDate.of(year, 12, 31).lengthOfYear());
        }

        @ParameterizedTest
        @CsvSource({
            // Julian calendar rules (year <= 1752)
            "1700, true",   // Leap in Julian
            "1600, true",   // Leap in Julian
            "1752, true",   // Leap in Julian
            "1751, false",
            // Gregorian calendar rules (year > 1752)
            "1753, false",
            "1800, false",  // Not a leap year in Gregorian
            "1900, false",  // Not a leap year in Gregorian
            "2000, true",   // Leap in Gregorian
            "2100, false"   // Not a leap year in Gregorian
        })
        @DisplayName("isLeapYear should follow Julian/Gregorian rules across cutover")
        void isLeapYear_shouldFollowJulianAndGregorianRulesAcrossCutover(int year, boolean expected) {
            assertEquals(expected, BritishCutoverChronology.INSTANCE.isLeapYear(year));
            assertEquals(expected, BritishCutoverDate.of(year, 1, 1).isLeapYear());
        }

        /**
         * Provides arguments for testing the range of a temporal field.
         * @return a stream of arguments: (year, month, day, field, min, max)
         */
        static Stream<Arguments> fieldRangeSamples() {
            return Stream.of(
                Arguments.of(1751, 2, 23, DAY_OF_MONTH, 1, 28),
                Arguments.of(1752, 2, 23, DAY_OF_MONTH, 1, 29),
                Arguments.of(1752, 9, 23, DAY_OF_MONTH, 1, 30), // Day of month range is lenient
                Arguments.of(1753, 2, 23, DAY_OF_MONTH, 1, 28),
                Arguments.of(1751, 1, 23, DAY_OF_YEAR, 1, 365),
                Arguments.of(1752, 1, 23, DAY_OF_YEAR, 1, 355),
                Arguments.of(1753, 1, 23, DAY_OF_YEAR, 1, 365),
                Arguments.of(1752, 9, 23, ALIGNED_WEEK_OF_MONTH, 1, 3),
                Arguments.of(1752, 12, 23, ALIGNED_WEEK_OF_YEAR, 1, 51)
            );
        }

        @ParameterizedTest
        @MethodSource("fieldRangeSamples")
        @DisplayName("range(TemporalField) should return correct value range")
        void rangeOfField_shouldReturnCorrectValueRange(int year, int month, int day, TemporalField field, long expectedMin, long expectedMax) {
            ValueRange expectedRange = ValueRange.of(expectedMin, expectedMax);
            assertEquals(expectedRange, BritishCutoverDate.of(year, month, day).range(field));
        }

        /**
         * Provides arguments for testing getLong(TemporalField).
         * @return a stream of arguments: (year, month, day, field, expected value)
         */
        static Stream<Arguments> fieldValueSamples() {
            return Stream.of(
                // Date before the cutover gap
                Arguments.of(1752, 9, 2, DAY_OF_WEEK, 3), // Wednesday
                Arguments.of(1752, 9, 2, DAY_OF_MONTH, 2),
                Arguments.of(1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 2),
                // Date after the cutover gap
                Arguments.of(1752, 9, 14, DAY_OF_WEEK, 4), // Thursday
                Arguments.of(1752, 9, 14, DAY_OF_MONTH, 14),
                Arguments.of(1752, 9, 14, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3), // Day 3 of cutover month
                // Modern date
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1), // Monday
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                Arguments.of(0, 6, 8, ERA, 0),
                Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 1)
            );
        }

        @ParameterizedTest
        @MethodSource("fieldValueSamples")
        @DisplayName("getLong(TemporalField) should return correct value")
        void getLong_shouldReturnCorrectValueForField(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, day).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Adjustment Tests")
    class AdjustmentTests {

        /**
         * Provides arguments for testing with(TemporalField, long).
         * @return a stream of arguments: (start date, field, value, expected date)
         */
        static Stream<Arguments> dateAdjustmentSamples() {
            return Stream.of(
                // Adjustments around the cutover
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), DAY_OF_WEEK, 4, BritishCutoverDate.of(1752, 9, 14)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), DAY_OF_MONTH, 14, BritishCutoverDate.of(1752, 9, 14)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), DAY_OF_WEEK, 3, BritishCutoverDate.of(1752, 9, 2)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), DAY_OF_MONTH, 2, BritishCutoverDate.of(1752, 9, 2)),
                // Leniently adjust into the gap
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), DAY_OF_MONTH, 3, BritishCutoverDate.of(1752, 9, 14)),
                // Modern date adjustments
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), DAY_OF_WEEK, 3, BritishCutoverDate.of(2014, 5, 28)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), YEAR, 2012, BritishCutoverDate.of(2012, 5, 26)),
                Arguments.of(BritishCutoverDate.of(2012, 2, 29), YEAR, 2011, BritishCutoverDate.of(2011, 2, 28)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), ERA, 0, BritishCutoverDate.of(-2013, 5, 26))
            );
        }

        @ParameterizedTest
        @MethodSource("dateAdjustmentSamples")
        @DisplayName("with(TemporalField, long) should adjust date correctly")
        void withTemporalField_shouldAdjustDateCorrectly(BritishCutoverDate startDate, TemporalField field, long value, BritishCutoverDate expectedDate) {
            assertEquals(expectedDate, startDate.with(field, value));
        }

        @ParameterizedTest
        @MethodSource
        @DisplayName("with(TemporalAdjuster) for lastDayOfMonth should return correct date")
        void withLastDayOfMonthAdjuster_shouldReturnCorrectDate(BritishCutoverDate input, BritishCutoverDate expected) {
            assertEquals(expected, input.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> withLastDayOfMonthAdjuster_shouldReturnCorrectDate() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 2, 23), BritishCutoverDate.of(1752, 2, 29)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 30)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 30)),
                Arguments.of(BritishCutoverDate.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 29))
            );
        }

        @ParameterizedTest
        @MethodSource
        @DisplayName("with(TemporalAdjuster) for LocalDate should return correct date")
        void withLocalDateAdjuster_shouldReturnCorrectDate(BritishCutoverDate input, LocalDate local, BritishCutoverDate expected) {
            assertEquals(expected, input.with(local));
        }

        static Stream<Arguments> withLocalDateAdjuster_shouldReturnCorrectDate() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 12), BritishCutoverDate.of(1752, 9, 1)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 14)),
                Arguments.of(BritishCutoverDate.of(2012, 2, 23), LocalDate.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 23))
            );
        }
    }

    @Nested
    @DisplayName("Arithmetic Tests")
    class ArithmeticTests {

        /**
         * Provides arguments for testing plus() and minus().
         * @return a stream of arguments: (start date, amount, unit, end date, isBidirectional)
         */
        static Stream<Arguments> plusMinusSamples() {
            return Stream.of(
                // Bidi: Bidirectional - true if date.plus(amount).minus(amount) == date
                // Days
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), 1L, DAYS, BritishCutoverDate.of(1752, 9, 14), true),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), -1L, DAYS, BritishCutoverDate.of(1752, 9, 2), true),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 8L, DAYS, BritishCutoverDate.of(2014, 6, 3), true),
                // Weeks
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), 1L, WEEKS, BritishCutoverDate.of(1752, 9, 20), true),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), -1L, WEEKS, BritishCutoverDate.of(1752, 8, 27), true),
                // Months
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), 1L, MONTHS, BritishCutoverDate.of(1752, 10, 2), true),
                Arguments.of(BritishCutoverDate.of(1752, 8, 12), 1L, MONTHS, BritishCutoverDate.of(1752, 9, 23), false),
                // Years and larger units
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3L, YEARS, BritishCutoverDate.of(2017, 5, 26), true),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3L, DECADES, BritishCutoverDate.of(2044, 5, 26), true),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3L, CENTURIES, BritishCutoverDate.of(2314, 5, 26), true),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), -1L, ERAS, BritishCutoverDate.of(-2013, 5, 26), true)
            );
        }

        @ParameterizedTest
        @MethodSource("plusMinusSamples")
        @DisplayName("plus(amount, unit) should add amount correctly")
        void plus_shouldAddAmountCorrectly(BritishCutoverDate startDate, long amount, TemporalUnit unit, BritishCutoverDate expectedEndDate, boolean isBidirectional) {
            assertEquals(expectedEndDate, startDate.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusMinusSamples")
        @DisplayName("minus(amount, unit) should subtract amount correctly")
        void minus_shouldSubtractAmountCorrectly(BritishCutoverDate startDate, long amount, TemporalUnit unit, BritishCutoverDate expectedEndDate, boolean isBidirectional) {
            if (isBidirectional) {
                assertEquals(startDate, expectedEndDate.minus(amount, unit));
            }
        }

        @ParameterizedTest
        @MethodSource
        @DisplayName("until(end, unit) should calculate correct duration")
        void until_withUnit_shouldCalculateCorrectDuration(BritishCutoverDate start, BritishCutoverDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> until_withUnit_shouldCalculateCorrectDuration() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), BritishCutoverDate.of(1752, 9, 2), DAYS, 1),
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), BritishCutoverDate.of(1752, 9, 14), DAYS, 2),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 14), DAYS, 1),
                Arguments.of(BritishCutoverDate.of(1752, 9, 1, 1752, 9, 19), WEEKS, 1),
                Arguments.of(BritishCutoverDate.of(1752, 9, 1, 1752, 10, 1), MONTHS, 1),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), BritishCutoverDate.of(2015, 5, 26), YEARS, 1),
                Arguments.of(BritishCutoverDate.of(-2013, 5, 26), BritishCutoverDate.of(2014, 5, 26), ERAS, 1)
            );
        }

        @ParameterizedTest
        @MethodSource
        @DisplayName("until(end) should calculate correct ChronoPeriod")
        void until_asChronoPeriod_shouldCalculateCorrectPeriod(BritishCutoverDate start, BritishCutoverDate end, ChronoPeriod expected) {
            assertEquals(expected, start.until(end));
        }

        static Stream<Arguments> until_asChronoPeriod_shouldCalculateCorrectPeriod() {
            return Stream.of(
                // 2 months after 1752-07-02 is 1752-09-02
                Arguments.of(BritishCutoverDate.of(1752, 7, 2), BritishCutoverDate.of(1752, 9, 2), BritishCutoverChronology.INSTANCE.period(0, 2, 0)),
                // 1 day after 1752-09-02 is 1752-09-14
                Arguments.of(BritishCutoverDate.of(1752, 7, 2), BritishCutoverDate.of(1752, 9, 14), BritishCutoverChronology.INSTANCE.period(0, 2, 1)),
                // 1 month after 1752-08-16 is 1752-09-16
                Arguments.of(BritishCutoverDate.of(1752, 8, 16), BritishCutoverDate.of(1752, 9, 16), BritishCutoverChronology.INSTANCE.period(0, 1, 0))
            );
        }

        @ParameterizedTest
        @MethodSource("until_asChronoPeriod_shouldCalculateCorrectPeriod")
        @DisplayName("plus(period) should correctly add period returned by until()")
        void plusPeriodReturnedByUntil_shouldReturnEndDate(BritishCutoverDate start, BritishCutoverDate end, ChronoPeriod ignored) {
            assertEquals(end, start.plus(start.until(end)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        @DisplayName("until(same date) should return zero period")
        void untilSameDate_shouldReturnZero(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(cutoverDate));
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
        }
    }

    @Nested
    @DisplayName("String Representation")
    class ToStringTests {

        @ParameterizedTest
        @CsvSource({
            "1, 1, 1, 'BritishCutover AD 1-01-01'",
            "2012, 6, 23, 'BritishCutover AD 2012-06-23'"
        })
        @DisplayName("toString should return correct representation")
        void toString_shouldReturnCorrectRepresentation(int year, int month, int day, String expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, day).toString());
        }
    }
}