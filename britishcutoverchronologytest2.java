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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link BritishCutoverDate}.
 */
class BritishCutoverDateTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleDateConversions() {
        return Stream.of(
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(BritishCutoverDate.of(4, 3, 2), LocalDate.of(4, 2, 29)), // Julian leap year
            Arguments.of(BritishCutoverDate.of(100, 3, 2), LocalDate.of(100, 3, 1)), // Julian leap year
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            // Dates around the British cutover in September 1752
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)), // Last Julian date
            Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)), // Invalid date, leniently accepted as next valid
            Arguments.of(BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)), // Invalid date, leniently accepted
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)), // First Gregorian date
            // Modern dates
            Arguments.of(BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12)),
            Arguments.of(BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6))
        );
    }

    static Stream<Arguments> invalidDateParts() {
        return Stream.of(
            Arguments.of(1900, 0, 1),   // Invalid month
            Arguments.of(1900, 13, 1),  // Invalid month
            Arguments.of(1900, 1, 0),   // Invalid day
            Arguments.of(1900, 1, 32),  // Invalid day
            Arguments.of(1900, 2, 30),  // Invalid day for month
            Arguments.of(1899, 2, 29),  // Invalid day for non-leap year
            Arguments.of(1900, 4, 31)   // Invalid day for month
        );
    }

    static Stream<Arguments> monthLengths() {
        return Stream.of(
            // Julian years
            Arguments.of(1700, 1, 31),
            Arguments.of(1700, 2, 29), // 1700 is a leap year in Julian
            // Gregorian non-leap year
            Arguments.of(1751, 2, 28),
            // Cutover year 1752
            Arguments.of(1752, 2, 29), // Leap year
            Arguments.of(1752, 9, 19), // September 1752 had 19 days (2 + 17)
            // Gregorian years
            Arguments.of(1753, 2, 28),
            Arguments.of(1800, 2, 28), // Not a leap year in Gregorian
            Arguments.of(1900, 2, 28), // Not a leap year in Gregorian
            Arguments.of(2000, 2, 29)  // Leap year in Gregorian
        );
    }

    static Stream<Arguments> yearLengths() {
        return Stream.of(
            Arguments.of(0, 366),    // Julian leap year
            Arguments.of(100, 366),  // Julian leap year
            Arguments.of(1700, 366), // Julian leap year
            Arguments.of(1751, 365), // Standard Gregorian year
            Arguments.of(1752, 355), // Cutover year, 11 days removed
            Arguments.of(1753, 365), // Standard Gregorian year
            Arguments.of(1800, 365), // Gregorian non-leap
            Arguments.of(1900, 365), // Gregorian non-leap
            Arguments.of(2000, 366), // Gregorian leap
            Arguments.of(2004, 366)  // Gregorian leap
        );
    }

    static Stream<Arguments> fieldRanges() {
        return Stream.of(
            // DAY_OF_MONTH
            Arguments.of(1700, 2, 1, DAY_OF_MONTH, 1, 29), // Julian leap year
            Arguments.of(1752, 9, 1, DAY_OF_MONTH, 1, 30), // Cutover month (lenient range)
            Arguments.of(2011, 2, 1, DAY_OF_MONTH, 1, 28), // Gregorian non-leap
            Arguments.of(2012, 2, 1, DAY_OF_MONTH, 1, 29), // Gregorian leap
            // DAY_OF_YEAR
            Arguments.of(1700, 1, 1, DAY_OF_YEAR, 1, 366), // Julian leap year
            Arguments.of(1752, 1, 1, DAY_OF_YEAR, 1, 355), // Cutover year
            Arguments.of(2011, 1, 1, DAY_OF_YEAR, 1, 365), // Gregorian non-leap
            Arguments.of(2012, 1, 1, DAY_OF_YEAR, 1, 366), // Gregorian leap
            // ALIGNED_WEEK_OF_MONTH
            Arguments.of(1752, 9, 1, ALIGNED_WEEK_OF_MONTH, 1, 3), // Cutover month
            Arguments.of(2011, 2, 1, ALIGNED_WEEK_OF_MONTH, 1, 4),
            Arguments.of(2012, 3, 1, ALIGNED_WEEK_OF_MONTH, 1, 5),
            // ALIGNED_WEEK_OF_YEAR
            Arguments.of(1752, 1, 1, ALIGNED_WEEK_OF_YEAR, 1, 51), // Cutover year
            Arguments.of(2011, 1, 1, ALIGNED_WEEK_OF_YEAR, 1, 53),
            Arguments.of(2012, 1, 1, ALIGNED_WEEK_OF_YEAR, 1, 53)
        );
    }

    static Stream<Arguments> dateFields() {
        return Stream.of(
            // --- Date: 1752-05-26 (Julian) ---
            Arguments.of(1752, 5, 26, DAY_OF_WEEK, 2), // Tuesday
            Arguments.of(1752, 5, 26, DAY_OF_MONTH, 26),
            Arguments.of(1752, 5, 26, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 26),
            Arguments.of(1752, 5, 26, MONTH_OF_YEAR, 5),
            // --- Date: 1752-09-02 (Last Julian day) ---
            Arguments.of(1752, 9, 2, DAY_OF_WEEK, 3), // Wednesday
            Arguments.of(1752, 9, 2, DAY_OF_MONTH, 2),
            Arguments.of(1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 2),
            Arguments.of(1752, 9, 2, MONTH_OF_YEAR, 9),
            // --- Date: 1752-09-14 (First Gregorian day) ---
            Arguments.of(1752, 9, 14, DAY_OF_WEEK, 4), // Thursday
            Arguments.of(1752, 9, 14, DAY_OF_MONTH, 14),
            Arguments.of(1752, 9, 14, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3), // Day after 1752-09-02
            Arguments.of(1752, 9, 14, MONTH_OF_YEAR, 9),
            // --- Date: 2014-05-26 (Gregorian) ---
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1), // Monday
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
            Arguments.of(2014, 5, 26, YEAR, 2014),
            Arguments.of(2014, 5, 26, ERA, 1), // AD
            Arguments.of(0, 6, 8, ERA, 0),     // BC
            Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 1)
        );
    }

    static Stream<Arguments> withFieldSamples() {
        return Stream.of(
            // --- With DAY_OF_WEEK ---
            Arguments.of(1752, 9, 2, DAY_OF_WEEK, 4, 1752, 9, 14), // Wednesday to Thursday (crosses cutover)
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 28), // Monday to Wednesday
            // --- With DAY_OF_MONTH ---
            Arguments.of(1752, 9, 2, DAY_OF_MONTH, 14, 1752, 9, 14), // Set to a valid day
            Arguments.of(1752, 9, 2, DAY_OF_MONTH, 3, 1752, 9, 14),  // Lenient: set to invalid day 3, becomes 14
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31),
            // --- With DAY_OF_YEAR ---
            Arguments.of(1752, 9, 2, DAY_OF_YEAR, 355, 1752, 12, 31), // Last day of cutover year
            Arguments.of(1752, 9, 2, DAY_OF_YEAR, 356, 1753, 1, 1),   // Lenient: wraps to next year
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31),
            // --- With MONTH_OF_YEAR ---
            Arguments.of(1752, 8, 4, MONTH_OF_YEAR, 9, 1752, 9, 15), // Lenient: day 4 of Sep is invalid, becomes 15
            Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28), // Adjusts to last day of month
            Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29), // Adjusts to last day of leap month
            // --- With YEAR ---
            Arguments.of(1751, 9, 4, YEAR, 1752, 1752, 9, 15), // Lenient: day 4 of Sep is invalid
            Arguments.of(2012, 2, 29, YEAR, 2011, 2011, 2, 28), // Adjusts leap day
            // --- With ERA ---
            Arguments.of(2014, 5, 26, ERA, 0, -2013, 5, 26) // AD to BC
        );
    }

    static Stream<Arguments> lastDayOfMonthSamples() {
        return Stream.of(
            Arguments.of(BritishCutoverDate.of(1752, 2, 23), BritishCutoverDate.of(1752, 2, 29)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 30)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 30)),
            Arguments.of(BritishCutoverDate.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 29))
        );
    }

    static Stream<Arguments> withLocalDateSamples() {
        return Stream.of(
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 12), BritishCutoverDate.of(1752, 9, 1)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 14)),
            Arguments.of(BritishCutoverDate.of(2012, 2, 23), LocalDate.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 23))
        );
    }

    private static final Object[][] PLUS_MINUS_DATA = {
        // --- DAYS ---
        { 1752, 9, 2, 1L, DAYS, 1752, 9, 14, true }, // Crosses cutover
        { 1752, 9, 14, -1L, DAYS, 1752, 9, 2, true }, // Crosses cutover
        { 2014, 5, 26, 8L, DAYS, 2014, 6, 3, true },
        // --- WEEKS ---
        { 1752, 9, 2, 1L, WEEKS, 1752, 9, 20, true },
        { 2014, 5, 26, 3L, WEEKS, 2014, 6, 16, true },
        // --- MONTHS ---
        { 1752, 8, 12, 1L, MONTHS, 1752, 9, 23, false }, // Not bidirectional due to cutover adjustment
        { 1752, 10, 12, -1L, MONTHS, 1752, 9, 23, false }, // Not bidirectional
        { 2014, 5, 26, 3L, MONTHS, 2014, 8, 26, true },
        // --- YEARS and larger ---
        { 2014, 5, 26, 3L, YEARS, 2017, 5, 26, true },
        { 2014, 5, 26, 3L, DECADES, 2044, 5, 26, true },
        { 2014, 5, 26, 3L, CENTURIES, 2314, 5, 26, true },
        { 2014, 5, 26, 3L, MILLENNIA, 5014, 5, 26, true },
        { 2014, 5, 26, -1L, ERAS, -2013, 5, 26, true }
    };

    static Stream<Arguments> plusUnitSamples() {
        return Stream.of(PLUS_MINUS_DATA).map(data -> Arguments.of(
            BritishCutoverDate.of((int) data[0], (int) data[1], (int) data[2]),
            (long) data[3],
            (TemporalUnit) data[4],
            BritishCutoverDate.of((int) data[5], (int) data[6], (int) data[7])
        ));
    }

    static Stream<Arguments> minusUnitSamples() {
        return Stream.of(PLUS_MINUS_DATA)
            .filter(data -> (boolean) data[8]) // Only use bidirectional data
            .map(data -> Arguments.of(
                BritishCutoverDate.of((int) data[5], (int) data[6], (int) data[7]), // end date
                (long) data[3], // amount
                (TemporalUnit) data[4], // unit
                BritishCutoverDate.of((int) data[0], (int) data[1], (int) data[2]) // start date
            ));
    }

    static Stream<Arguments> untilUnitSamples() {
        return Stream.of(
            // --- DAYS ---
            Arguments.of(1752, 9, 1, 1752, 9, 2, DAYS, 1L),
            Arguments.of(1752, 9, 1, 1752, 9, 14, DAYS, 2L), // Skips 11 days
            Arguments.of(1752, 9, 2, 1752, 9, 14, DAYS, 1L),
            Arguments.of(2014, 5, 26, 2014, 6, 1, DAYS, 6L),
            // --- WEEKS ---
            Arguments.of(1752, 9, 1, 1752, 9, 19, WEEKS, 1L),
            Arguments.of(2014, 5, 26, 2014, 6, 2, WEEKS, 1L),
            // --- MONTHS ---
            Arguments.of(1752, 9, 1, 1752, 10, 1, MONTHS, 1L),
            Arguments.of(1752, 9, 14, 1752, 10, 14, MONTHS, 1L),
            Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
            // --- YEARS and larger ---
            Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
            Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
            Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
            Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
            Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1L)
        );
    }

    static Stream<Arguments> untilPeriodSamples() {
        return Stream.of(
            // --- Start date, End date, Expected Period (Y, M, D) ---
            // Simple case, no cutover
            Arguments.of(1752, 7, 2, 1752, 7, 1, 0, 0, -1),
            // End date is one month and 30 days after start date
            // P0Y1M30D: 1752-07-02 + 1M = 1752-08-02. 1752-08-02 to 1752-09-01 is 30 days.
            Arguments.of(1752, 7, 2, 1752, 9, 1, 0, 1, 30),
            // End date is exactly 2 months after start date
            Arguments.of(1752, 7, 2, 1752, 9, 2, 0, 2, 0),
            // End date is 2 months and 1 day after start date (crossing the cutover)
            // P0Y2M1D: 1752-07-02 + 2M = 1752-09-02. 1752-09-02 to 1752-09-14 is 1 day.
            Arguments.of(1752, 7, 2, 1752, 9, 14, 0, 2, 1),
            // Start date is just before cutover, end date is just after
            Arguments.of(1752, 9, 2, 1752, 9, 14, 0, 0, 1),
            // Reverse direction, crossing cutover
            Arguments.of(1752, 9, 14, 1752, 9, 2, 0, 0, -1),
            // Start date is after cutover, end date is before
            Arguments.of(1752, 10, 3, 1752, 9, 2, -1, -1, -1)
        );
    }

    static Stream<Arguments> toStringSamples() {
        return Stream.of(
            Arguments.of(BritishCutoverDate.of(1, 1, 1), "BritishCutover AD 1-01-01"),
            Arguments.of(BritishCutoverDate.of(2012, 6, 23), "BritishCutover AD 2012-06-23")
        );
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    class FactoryAndValidation {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#invalidDateParts")
        void of_invalidDate_throwsException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
        }

        @Test
        void dateYearDay_invalidDayOfYear_throwsException() {
            // 2001 is not a leap year in Gregorian, so it has 365 days.
            assertThrows(DateTimeException.class, () -> BritishCutoverChronology.INSTANCE.dateYearDay(2001, 366));
        }
    }

    @Nested
    class Conversion {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDateConversions")
        void convertsToCorrectLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDateConversions")
        void fromLocalDate_createsCorrectBritishCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDateConversions")
        void toEpochDay_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDateConversions")
        void fromEpochDay_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDateConversions")
        void fromTemporalAccessor_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    class FieldAccess {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#monthLengths")
        void lengthOfMonth_isCorrect(int year, int month, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#yearLengths")
        void lengthOfYear_isCorrect(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
            // Also check from the end of the year, as implementation might differ
            if (year != 1752) { // 1752-12-31 is valid
                assertEquals(expectedLength, BritishCutoverDate.of(year, 12, 31).lengthOfYear());
            }
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#fieldRanges")
        void range_isCorrect(int year, int month, int day, TemporalField field, int expectedMin, int expectedMax) {
            BritishCutoverDate date = BritishCutoverDate.of(year, month, day);
            assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#dateFields")
        void getLong_isCorrect(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, day).getLong(field));
        }
    }

    @Nested
    class DateModification {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#withFieldSamples")
        void with_temporalField_returnsCorrectDate(int year, int month, int dom, TemporalField field, long value, int exYear, int exMonth, int exDom) {
            BritishCutoverDate start = BritishCutoverDate.of(year, month, dom);
            BritishCutoverDate expected = BritishCutoverDate.of(exYear, exMonth, exDom);
            assertEquals(expected, start.with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#lastDayOfMonthSamples")
        void with_lastDayOfMonth_adjustsCorrectly(BritishCutoverDate date, BritishCutoverDate expected) {
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#withLocalDateSamples")
        void with_localDate_adjustsCorrectly(BritishCutoverDate date, LocalDate localDate, BritishCutoverDate expected) {
            assertEquals(expected, date.with(localDate));
        }
    }

    @Nested
    class Arithmetic {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDateConversions")
        void plusDays_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(cutoverDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(cutoverDate.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(cutoverDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(cutoverDate.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDateConversions")
        void minusDays_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate.minus(0, DAYS)));
            assertEquals(isoDate.minusDays(1), LocalDate.from(cutoverDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(cutoverDate.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(cutoverDate.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(cutoverDate.minus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#plusUnitSamples")
        void plus_isCorrect(BritishCutoverDate start, long amount, TemporalUnit unit, BritishCutoverDate expected) {
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#minusUnitSamples")
        void minus_isCorrect(BritishCutoverDate start, long amount, TemporalUnit unit, BritishCutoverDate expected) {
            assertEquals(expected, start.minus(amount, unit));
        }
    }

    @Nested
    class PeriodAndDuration {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDateConversions")
        void until_self_isZero(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDateConversions")
        void until_equivalentLocalDate_isZero(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDateConversions")
        void until_equivalentBritishCutoverDate_isZero(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDateConversions")
        void until_days_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(0, cutoverDate.until(isoDate.plusDays(0), DAYS));
            assertEquals(1, cutoverDate.until(isoDate.plusDays(1), DAYS));
            assertEquals(35, cutoverDate.until(isoDate.plusDays(35), DAYS));
            assertEquals(-40, cutoverDate.until(isoDate.minusDays(40), DAYS));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#untilUnitSamples")
        void until_temporalUnit_isCorrect(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#untilPeriodSamples")
        void until_chronoPeriod_isCorrect(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            ChronoPeriod expected = BritishCutoverChronology.INSTANCE.period(ey, em, ed);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#untilPeriodSamples")
        void until_chronoPeriod_isReversibleByPlus(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            assertEquals(end, start.plus(start.until(end)));
        }
    }

    @Nested
    class GeneralBehavior {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#toStringSamples")
        void toString_returnsCorrectFormat(BritishCutoverDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}