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
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.TemporalAdjusters;
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
import java.time.chrono.ChronoPeriod;

@DisplayName("Tests for BritishCutoverChronology and BritishCutoverDate")
public class BritishCutoverChronologyTest {

    // A shared set of representative dates for testing conversions and basic properties.
    static Stream<Arguments> provideSampleCutoverAndIsoDates() {
        return Stream.of(
                // --- Far past Julian dates ---
                Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
                Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
                // --- Julian leap years ---
                Arguments.of(BritishCutoverDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
                Arguments.of(BritishCutoverDate.of(100, 2, 29), LocalDate.of(100, 2, 27)), // Julian leap, not Gregorian
                // --- Dates just before the cutover ---
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), LocalDate.of(1752, 9, 12)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)), // Last Julian date
                // --- Dates inside the cutover gap (leniently accepted) ---
                Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)), // First day of the gap
                Arguments.of(BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)),// Last day of the gap
                // --- First Gregorian date ---
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)),
                // --- Modern Gregorian dates ---
                Arguments.of(BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12)),
                Arguments.of(BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6))
        );
    }

    @Nested
    @DisplayName("Factory and Conversion Tests")
    class FactoryAndConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        @DisplayName("LocalDate.from(cutoverDate) should return the correct ISO date")
        void conversionToLocalDate_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        @DisplayName("BritishCutoverDate.from(isoDate) should return the correct cutover date")
        void conversionFromLocalDate_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        @DisplayName("chronology.dateEpochDay should create the correct date")
        void chronologyDateEpochDay_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        @DisplayName("toEpochDay should return the correct epoch day value")
        void toEpochDay_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        @DisplayName("chronology.date(temporal) should create the correct date")
        void chronologyDateFromTemporal_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }

        static Stream<Arguments> provideInvalidDateParts() {
            return Stream.of(
                    Arguments.of(1900, 0, 0),
                    Arguments.of(1900, 13, 1),
                    Arguments.of(1900, 1, 32),
                    Arguments.of(1900, 2, 30), // Not a leap year in Gregorian
                    Arguments.of(1899, 2, 29)  // Not a leap year
            );
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("provideInvalidDateParts")
        @DisplayName("of() with invalid date parts should throw DateTimeException")
        void of_withInvalidDateParts_throwsException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
        }
    }

    @Nested
    @DisplayName("Date Property Tests")
    class DatePropertyTests {

        static Stream<Arguments> provideLengthOfMonthData() {
            return Stream.of(
                    // Julian leap year (divisible by 4)
                    Arguments.of(1700, 2, 29),
                    // Non-leap year
                    Arguments.of(1751, 2, 28),
                    // Gregorian leap year (divisible by 4, not 100 unless 400)
                    Arguments.of(1752, 2, 29),
                    // The cutover month of September 1752 had 19 days (30 - 11)
                    Arguments.of(1752, 9, 19),
                    // Standard Gregorian month
                    Arguments.of(1753, 2, 28),
                    // Gregorian non-leap century
                    Arguments.of(1800, 2, 28),
                    // Gregorian leap century
                    Arguments.of(2000, 2, 29)
            );
        }

        @ParameterizedTest(name = "{0}-{1} should have {2} days")
        @MethodSource("provideLengthOfMonthData")
        @DisplayName("lengthOfMonth() should return the correct length for the month")
        void lengthOfMonth_isCorrect(int year, int month, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> provideLengthOfYearData() {
            return Stream.of(
                    // Julian leap year
                    Arguments.of(1700, 366),
                    // Standard non-leap year
                    Arguments.of(1751, 365),
                    // The cutover year 1752 had 11 days removed (366 - 11)
                    Arguments.of(1752, 355),
                    // Standard non-leap year
                    Arguments.of(1753, 365),
                    // Gregorian non-leap century
                    Arguments.of(1900, 365),
                    // Gregorian leap year
                    Arguments.of(2004, 366)
            );
        }

        @ParameterizedTest(name = "Year {0} should have {1} days")
        @MethodSource("provideLengthOfYearData")
        @DisplayName("lengthOfYear() should return the correct length for the year")
        void lengthOfYear_isCorrect(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
            assertEquals(expectedLength, BritishCutoverDate.of(year, 12, 31).lengthOfYear());
        }

        static Stream<Arguments> provideRangeData() {
            return Stream.of(
                    // Day of month in a Julian leap February
                    Arguments.of(1700, 2, 1, DAY_OF_MONTH, 1, 29),
                    // Day of month in the cutover month (September 1752)
                    Arguments.of(1752, 9, 1, DAY_OF_MONTH, 1, 30), // Range is lenient, length is 19
                    // Day of year in the cutover year 1752 (366 - 11 days)
                    Arguments.of(1752, 1, 1, DAY_OF_YEAR, 1, 355),
                    // Day of year in a standard Gregorian leap year
                    Arguments.of(2012, 1, 1, DAY_OF_YEAR, 1, 366),
                    // Aligned week of month in the cutover month
                    Arguments.of(1752, 9, 1, ALIGNED_WEEK_OF_MONTH, 1, 3)
            );
        }

        @ParameterizedTest(name = "range({3}) for {0}-{1}-{2} should be {4}-{5}")
        @MethodSource("provideRangeData")
        @DisplayName("range() for a given field should return the correct value range")
        void range_forField_isCorrect(int year, int month, int day, TemporalField field, long min, long max) {
            assertEquals(ValueRange.of(min, max), BritishCutoverDate.of(year, month, day).range(field));
        }

        static Stream<Arguments> provideGetLongData() {
            return Stream.of(
                    // Before cutover
                    Arguments.of(1752, 9, 2, DAY_OF_WEEK, 3), // Wednesday
                    Arguments.of(1752, 9, 2, DAY_OF_YEAR, 246),
                    // After cutover
                    Arguments.of(1752, 9, 14, DAY_OF_WEEK, 4), // Thursday
                    Arguments.of(1752, 9, 14, DAY_OF_YEAR, 246 + 1), // Day after 9/2
                    // Modern date
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1), // Monday
                    Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 4),
                    Arguments.of(2014, 5, 26, YEAR, 2014),
                    Arguments.of(2014, 5, 26, ERA, 1), // AD
                    Arguments.of(0, 6, 8, ERA, 0)      // BC
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} getLong({3}) should be {4}")
        @MethodSource("provideGetLongData")
        @DisplayName("getLong() for a given field should return the correct value")
        void getLong_forField_isCorrect(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, day).getLong(field));
        }
    }

    @Nested
    @DisplayName("Manipulation and Arithmetic Tests")
    class ManipulationAndArithmeticTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        @DisplayName("plusDays should correctly add days across the cutover")
        void plusDays_onSampleDates_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.plusDays(1), LocalDate.from(cutoverDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(cutoverDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(cutoverDate.plus(35, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        @DisplayName("minusDays should correctly subtract days across the cutover")
        void minusDays_onSampleDates_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.minusDays(1), LocalDate.from(cutoverDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(cutoverDate.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(cutoverDate.minus(35, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        @DisplayName("until(other, DAYS) should return the correct number of days")
        void until_days_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(1, cutoverDate.until(isoDate.plusDays(1), DAYS));
            assertEquals(-1, cutoverDate.until(isoDate.minusDays(1), DAYS));
            assertEquals(35, cutoverDate.until(isoDate.plusDays(35), DAYS));
        }

        static Stream<Arguments> provideWithData() {
            return Stream.of(
                    // --- Adjustments around the cutover gap ---
                    // Adjust from a date before the gap to one after
                    Arguments.of(1752, 9, 2, DAY_OF_WEEK, 4, 1752, 9, 14),
                    // Leniently adjust from a date before the gap into the gap
                    Arguments.of(1752, 9, 2, DAY_OF_MONTH, 3, 1752, 9, 14),
                    // Adjust from a date after the gap to one before
                    Arguments.of(1752, 9, 14, DAY_OF_WEEK, 3, 1752, 9, 2),
                    // --- Standard adjustments ---
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 28),
                    Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26),
                    Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                    // Adjusting month can change day if day is invalid for new month
                    Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("provideWithData")
        @DisplayName("with(field, value) should return the correctly adjusted date")
        void with_fieldAndValue_isCorrect(int y, int m, int d, TemporalField f, long v, int ey, int em, int ed) {
            assertEquals(BritishCutoverDate.of(ey, em, ed), BritishCutoverDate.of(y, m, d).with(f, v));
        }

        static Stream<Arguments> providePlusData() {
            return Stream.of(
                    // --- Crossing the cutover gap ---
                    Arguments.of(1752, 9, 2, 1, DAYS, 1752, 9, 14, true),
                    Arguments.of(1752, 9, 14, -1, DAYS, 1752, 9, 2, true),
                    // --- Month arithmetic over the cutover ---
                    Arguments.of(1752, 8, 12, 1, MONTHS, 1752, 9, 23, false), // Not bidirectional
                    Arguments.of(1752, 10, 12, -1, MONTHS, 1752, 9, 23, false),// Not bidirectional
                    // --- Standard arithmetic ---
                    Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26, true),
                    Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26, true)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("providePlusData")
        @DisplayName("plus(amount, unit) should return the correctly added date")
        void plus_amountAndUnit_isCorrect(int y, int m, int d, long amt, TemporalUnit u, int ey, int em, int ed, boolean bidi) {
            assertEquals(BritishCutoverDate.of(ey, em, ed), BritishCutoverDate.of(y, m, d).plus(amt, u));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus({3}, {4}) -> {0}-{1}-{2}")
        @MethodSource("providePlusData")
        @DisplayName("minus(amount, unit) should return the correctly subtracted date")
        void minus_amountAndUnit_isCorrect(int ey, int em, int ed, long amt, TemporalUnit u, int y, int m, int d, boolean bidi) {
            // Only test bidirectional cases, as some month additions are not reversible
            if (bidi) {
                assertEquals(BritishCutoverDate.of(ey, em, ed), BritishCutoverDate.of(y, m, d).minus(amt, u));
            }
        }

        static Stream<Arguments> provideUntilPeriodData() {
            return Stream.of(
                    // Period across the cutover gap
                    Arguments.of(1752, 7, 2, 1752, 9, 14, 0, 2, 1),
                    // Period within the same month after the cutover
                    Arguments.of(1752, 9, 14, 1752, 9, 30, 0, 0, 16),
                    // Period of exactly one month
                    Arguments.of(1752, 9, 14, 1752, 10, 14, 0, 1, 0)
            );
        }

        @ParameterizedTest(name = "until({3}-{4}-{5}) from {0}-{1}-{2} is P{6}Y{7}M{8}D")
        @MethodSource("provideUntilPeriodData")
        @DisplayName("until(endDate) should calculate the correct period")
        void until_period_isCorrect(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            ChronoPeriod expected = BritishCutoverChronology.INSTANCE.period(ey, em, ed);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest(name = "start.plus(start.until(end)) should equal end")
        @MethodSource("provideUntilPeriodData")
        @DisplayName("plus(period) should correctly reconstruct the end date")
        void plus_periodFromUntil_reconstructsEndDate(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            assertEquals(end, start.plus(start.until(end)));
        }

        @Test
        @DisplayName("until() with an unsupported unit should throw exception")
        void until_unsupportedUnit_throwsException() {
            BritishCutoverDate start = BritishCutoverDate.of(2012, 6, 30);
            BritishCutoverDate end = BritishCutoverDate.of(2012, 7, 1);
            assertThrows(UnsupportedTemporalTypeException.class, () -> start.until(end, MINUTES));
        }
    }

    @Nested
    @DisplayName("Specialized Until Tests")
    class SpecializedUntilTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        @DisplayName("until(self) should return a zero period")
        void until_self_returnsZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        @DisplayName("until(equivalentIsoDate) should return a zero period")
        void until_equivalentIsoDate_returnsZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        @DisplayName("isoDate.until(equivalentCutoverDate) should return a zero period")
        void isoDateUntil_equivalentCutoverDate_returnsZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
        }
    }

    @Nested
    @DisplayName("Adjuster Tests")
    class AdjusterTests {

        static Stream<Arguments> provideLastDayOfMonthData() {
            return Stream.of(
                    // Cutover month
                    Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 30)),
                    // Leap February
                    Arguments.of(BritishCutoverDate.of(1752, 2, 23), BritishCutoverDate.of(1752, 2, 29)),
                    // Standard month
                    Arguments.of(BritishCutoverDate.of(2012, 6, 23), BritishCutoverDate.of(2012, 6, 30))
            );
        }

        @ParameterizedTest
        @MethodSource("provideLastDayOfMonthData")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) should return the last day")
        void with_lastDayOfMonth_adjustsCorrectly(BritishCutoverDate input, BritishCutoverDate expected) {
            assertEquals(expected, input.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> provideWithLocalDateData() {
            return Stream.of(
                    // Adjusting a pre-cutover date to a different pre-cutover date
                    Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 12), BritishCutoverDate.of(1752, 9, 1)),
                    // Adjusting a post-cutover date to a pre-cutover date
                    Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 12), BritishCutoverDate.of(1752, 9, 1)),
                    // Adjusting to a post-cutover date
                    Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 14))
            );
        }

        @ParameterizedTest
        @MethodSource("provideWithLocalDateData")
        @DisplayName("with(LocalDate) should adjust to the new date correctly")
        void with_localDate_adjustsCorrectly(BritishCutoverDate input, LocalDate newDate, BritishCutoverDate expected) {
            assertEquals(expected, input.with(newDate));
        }
    }

    @Nested
    @DisplayName("String Representation")
    class ToStringTests {

        static Stream<Arguments> provideToStringData() {
            return Stream.of(
                    Arguments.of(BritishCutoverDate.of(1, 1, 1), "BritishCutover AD 1-01-01"),
                    Arguments.of(BritishCutoverDate.of(2012, 6, 23), "BritishCutover AD 2012-06-23")
            );
        }

        @ParameterizedTest
        @MethodSource("provideToStringData")
        @DisplayName("toString() should return the correct formatted string")
        void toString_isCorrect(BritishCutoverDate cutoverDate, String expected) {
            assertEquals(expected, cutoverDate.toString());
        }
    }
}