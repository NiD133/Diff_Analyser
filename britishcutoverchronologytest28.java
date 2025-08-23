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
import java.time.LocalTime;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Test class for {@link BritishCutoverChronology} and {@link BritishCutoverDate}.
 *
 * <p>This class focuses on the correctness of date calculations, conversions,
 * and manipulations, especially around the 1752 Julian-to-Gregorian cutover.
 */
@DisplayName("BritishCutoverChronology and BritishCutoverDate")
class BritishCutoverChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides pairs of (BritishCutoverDate, corresponding ISO LocalDate) for testing conversions.
     */
    static Stream<Arguments> sampleCutoverAndIsoDatesProvider() {
        return Stream.of(
            // --- Pre-cutover dates (Julian calendar rules) ---
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(BritishCutoverDate.of(1, 2, 28), LocalDate.of(1, 2, 26)),
            Arguments.of(BritishCutoverDate.of(1, 3, 1), LocalDate.of(1, 2, 27)),
            Arguments.of(BritishCutoverDate.of(1, 3, 2), LocalDate.of(1, 2, 28)),
            Arguments.of(BritishCutoverDate.of(1, 3, 3), LocalDate.of(1, 3, 1)),
            Arguments.of(BritishCutoverDate.of(4, 2, 28), LocalDate.of(4, 2, 26)),
            Arguments.of(BritishCutoverDate.of(4, 2, 29), LocalDate.of(4, 2, 27)), // Julian leap day
            Arguments.of(BritishCutoverDate.of(4, 3, 1), LocalDate.of(4, 2, 28)),
            Arguments.of(BritishCutoverDate.of(4, 3, 2), LocalDate.of(4, 2, 29)),
            Arguments.of(BritishCutoverDate.of(4, 3, 3), LocalDate.of(4, 3, 1)),
            Arguments.of(BritishCutoverDate.of(100, 2, 28), LocalDate.of(100, 2, 26)),
            Arguments.of(BritishCutoverDate.of(100, 2, 29), LocalDate.of(100, 2, 27)), // Julian leap day (not Gregorian)
            Arguments.of(BritishCutoverDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
            Arguments.of(BritishCutoverDate.of(100, 3, 2), LocalDate.of(100, 3, 1)),
            Arguments.of(BritishCutoverDate.of(0, 12, 31), LocalDate.of(0, 12, 29)),
            Arguments.of(BritishCutoverDate.of(0, 12, 30), LocalDate.of(0, 12, 28)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),

            // --- Dates around the British cutover (1752) ---
            Arguments.of(BritishCutoverDate.of(1751, 12, 20), LocalDate.of(1751, 12, 31)),
            Arguments.of(BritishCutoverDate.of(1751, 12, 31), LocalDate.of(1752, 1, 11)),
            Arguments.of(BritishCutoverDate.of(1752, 1, 1), LocalDate.of(1752, 1, 12)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 1), LocalDate.of(1752, 9, 12)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)), // Last day before the gap
            Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)), // Leniently accepts invalid date
            Arguments.of(BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)), // Leniently accepts invalid date
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)), // First day after the gap

            // --- Post-cutover dates (Gregorian rules) ---
            Arguments.of(BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12)),
            Arguments.of(BritishCutoverDate.of(2012, 7, 5), LocalDate.of(2012, 7, 5)),
            Arguments.of(BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6))
        );
    }

    /**
     * Provides invalid date components for testing factory method validation.
     * Format: {year, month, dayOfMonth}
     */
    static Stream<Arguments> invalidDateProvider() {
        return Stream.of(
            Arguments.of(1900, 0, 0),
            Arguments.of(1900, -1, 1),
            Arguments.of(1900, 0, 1),
            Arguments.of(1900, 13, 1),
            Arguments.of(1900, 14, 1),
            Arguments.of(1900, 1, -1),
            Arguments.of(1900, 1, 0),
            Arguments.of(1900, 1, 32),
            Arguments.of(1900, 2, 30), // Not a leap year
            Arguments.of(1899, 2, 29), // Not a leap year
            Arguments.of(1900, 4, 31)  // April has 30 days
        );
    }

    /**
     * Provides test cases for date addition and subtraction.
     * Format: {startYear, startMonth, startDom, amount, unit, expectedYear, expectedMonth, expectedDom, isBidirectional}
     */
    static Stream<Arguments> plusMinusProvider() {
        return Stream.of(
            // --- Days ---
            Arguments.of(1752, 9, 2, -1L, DAYS, 1752, 9, 1, true),
            Arguments.of(1752, 9, 2, 0L, DAYS, 1752, 9, 2, true),
            Arguments.of(1752, 9, 2, 1L, DAYS, 1752, 9, 14, true), // Crosses cutover gap
            Arguments.of(1752, 9, 2, 2L, DAYS, 1752, 9, 15, true),
            Arguments.of(1752, 9, 14, -1L, DAYS, 1752, 9, 2, true), // Crosses cutover gap
            Arguments.of(1752, 9, 14, 0L, DAYS, 1752, 9, 14, true),
            Arguments.of(1752, 9, 14, 1L, DAYS, 1752, 9, 15, true),
            Arguments.of(2014, 5, 26, 8L, DAYS, 2014, 6, 3, true),

            // --- Weeks ---
            Arguments.of(1752, 9, 2, 1L, WEEKS, 1752, 9, 20, true),
            Arguments.of(1752, 9, 14, -1L, WEEKS, 1752, 8, 27, true),
            Arguments.of(2014, 5, 26, 3L, WEEKS, 2014, 6, 16, true),

            // --- Months ---
            Arguments.of(1752, 9, 2, 1L, MONTHS, 1752, 10, 2, true),
            Arguments.of(1752, 9, 14, -1L, MONTHS, 1752, 8, 14, true),
            Arguments.of(1752, 8, 12, 1L, MONTHS, 1752, 9, 23, false), // Day-of-month adjustment
            Arguments.of(1752, 10, 12, -1L, MONTHS, 1752, 9, 23, false), // Day-of-month adjustment
            Arguments.of(2014, 5, 26, 3L, MONTHS, 2014, 8, 26, true),

            // --- Years, Decades, Centuries, Millennia, Eras ---
            Arguments.of(2014, 5, 26, 3L, YEARS, 2017, 5, 26, true),
            Arguments.of(2014, 5, 26, 3L, DECADES, 2044, 5, 26, true),
            Arguments.of(2014, 5, 26, 3L, CENTURIES, 2314, 5, 26, true),
            Arguments.of(2014, 5, 26, 3L, MILLENNIA, 5014, 5, 26, true),
            Arguments.of(2014, 5, 26, -1L, ERAS, -2013, 5, 26, true)
        );
    }

    /**
     * Provides test cases for period calculations with until(ChronoLocalDate).
     * Format: {y1, m1, d1, y2, m2, d2, expectedY, expectedM, expectedD}
     */
    static Stream<Arguments> untilPeriodProvider() {
        return Stream.of(
            // --- Basic cases ---
            Arguments.of(1752, 7, 2, 1752, 7, 1, 0, 0, -1),
            Arguments.of(1752, 7, 2, 1752, 7, 2, 0, 0, 0),

            // --- Spanning the cutover gap ---
            // Start before gap, end after gap.
            Arguments.of(1752, 7, 2, 1752, 9, 1, 0, 1, 30), // End date is 30 days after 1752-08-02 (Julian)
            Arguments.of(1752, 7, 2, 1752, 9, 14, 0, 2, 1), // End date is 1 day after 1752-09-02
            Arguments.of(1752, 7, 2, 1752, 10, 2, 0, 3, 0), // 3 whole months

            // --- Start within the gap (leniently) ---
            Arguments.of(1752, 9, 2, 1752, 9, 14, 0, 0, 1), // 1 day difference
            Arguments.of(1752, 9, 2, 1752, 10, 2, 0, 1, 0), // 1 month difference

            // --- Start after the gap, end before ---
            Arguments.of(1752, 9, 14, 1752, 9, 2, 0, 0, -1), // -1 day difference
            Arguments.of(1752, 9, 14, 1752, 7, 14, 0, -2, 0), // -2 months difference

            // --- End of month/year transitions ---
            Arguments.of(1752, 8, 16, 1752, 9, 16, 0, 1, 0),
            Arguments.of(1752, 10, 4, 1752, 8, 4, 0, -2, 0)
        );
    }

    //-----------------------------------------------------------------------
    // Test Cases
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory and Conversion Tests")
    class ConversionAndFactoryTests {

        @ParameterizedTest(name = "{index}: LocalDate.from({0}) -> {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDatesProvider")
        void fromCutoverDate_shouldCreateCorrectLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest(name = "{index}: BritishCutoverDate.from({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDatesProvider")
        void fromLocalDate_shouldCreateCorrectCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest(name = "{index}: toEpochDay on {0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDatesProvider")
        void toEpochDay_shouldReturnCorrectValue(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest(name = "{index}: dateEpochDay for {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDatesProvider")
        void dateEpochDay_shouldCreateCorrectCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest(name = "{index}: chronology.date({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDatesProvider")
        void chronologyDateFromTemporal_shouldCreateCorrectCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#invalidDateProvider")
        void of_shouldThrowException_forInvalidDateParts(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
        }
    }

    @Nested
    @DisplayName("Query Tests")
    class QueryTests {

        @Test
        void lengthOfMonth_shouldBeCorrectForCutoverYear() {
            assertEquals(19, BritishCutoverDate.of(1752, 9, 1).lengthOfMonth());
        }

        @Test
        void lengthOfYear_shouldBeShorterForCutoverYear() {
            assertEquals(355, BritishCutoverDate.of(1752, 1, 1).lengthOfYear());
        }

        @Test
        void lengthOfYear_shouldBeCorrectForLeapAndCommonYears() {
            assertEquals(366, BritishCutoverDate.of(1700, 1, 1).lengthOfYear()); // Julian leap year
            assertEquals(365, BritishCutoverDate.of(1800, 1, 1).lengthOfYear()); // Gregorian common year
            assertEquals(366, BritishCutoverDate.of(2000, 1, 1).lengthOfYear()); // Gregorian leap year
        }

        @Test
        void range_shouldBeCorrectForCutoverMonth() {
            // September 1752 has a gap, but the valid day-of-month range is 1-30 for lenient parsing.
            assertEquals(ValueRange.of(1, 30), BritishCutoverDate.of(1752, 9, 1).range(DAY_OF_MONTH));
            assertEquals(ValueRange.of(1, 3), BritishCutoverDate.of(1752, 9, 1).range(ALIGNED_WEEK_OF_MONTH));
        }

        @Test
        void range_shouldBeCorrectForCutoverYear() {
            assertEquals(ValueRange.of(1, 355), BritishCutoverDate.of(1752, 1, 1).range(DAY_OF_YEAR));
            assertEquals(ValueRange.of(1, 51), BritishCutoverDate.of(1752, 1, 1).range(ALIGNED_WEEK_OF_YEAR));
        }

        @ParameterizedTest(name = "getLong({3}) on {0}-{1}-{2} -> {4}")
        @MethodSource("getLongProvider")
        void getLong_shouldReturnCorrectValueForField(int y, int m, int d, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(y, m, d).getLong(field));
        }

        /**
         * Provides test cases for getLong(TemporalField).
         * Format: {year, month, dom, field, expectedValue}
         */
        static Stream<Arguments> getLongProvider() {
            return Stream.of(
                Arguments.of(1752, 9, 2, DAY_OF_WEEK, 3), // Wednesday
                Arguments.of(1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 2),
                Arguments.of(1752, 9, 14, DAY_OF_WEEK, 4), // Thursday
                Arguments.of(1752, 9, 14, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3), // Day after 9/2
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1), // Monday
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                Arguments.of(0, 6, 8, ERA, 0)
            );
        }
    }

    @Nested
    @DisplayName("Date Manipulation Tests")
    class DateManipulationTests {

        @ParameterizedTest(name = "{0} plus {1} {2} -> {3}-{4}-{5}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#plusMinusProvider")
        void plus_shouldAddAmountCorrectly(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed, boolean bidi) {
            BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
            BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{3}-{4}-{5} minus {1} {2} -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#plusMinusProvider")
        void minus_shouldSubtractAmountCorrectly(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d, boolean isBidirectional) {
            if (isBidirectional) {
                BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
                BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
                assertEquals(expected, start.minus(amount, unit));
            }
        }

        @ParameterizedTest(name = "with({3}, {4}) on {0}-{1}-{2} -> {5}-{6}-{7}")
        @MethodSource("withFieldProvider")
        void with_shouldSetFieldToValueCorrectly(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
            BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        /**
         * Provides test cases for with(TemporalField, long).
         * Format: {year, month, dom, field, value, expectedYear, expectedMonth, expectedDom}
         */
        static Stream<Arguments> withFieldProvider() {
            return Stream.of(
                // Adjusting day of week across the cutover
                Arguments.of(1752, 9, 2, DAY_OF_WEEK, 4, 1752, 9, 14),
                // Leniently setting a day inside the gap
                Arguments.of(1752, 9, 2, DAY_OF_MONTH, 3, 1752, 9, 14),
                // Adjusting month, which can land in the gap
                Arguments.of(1752, 8, 4, MONTH_OF_YEAR, 9, 1752, 9, 15), // lenient
                // Adjusting year, which can land in the gap
                Arguments.of(1751, 9, 4, YEAR, 1752, 1752, 9, 15), // lenient
                // Standard adjustments
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 28),
                Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29), // Adjust to leap day
                Arguments.of(2012, 2, 29, YEAR, 2011, 2011, 2, 28) // Adjust from leap day
            );
        }

        @Test
        void with_lastDayOfMonthAdjuster_shouldReturnCorrectDate() {
            assertEquals(BritishCutoverDate.of(1752, 9, 30), BritishCutoverDate.of(1752, 9, 2).with(TemporalAdjusters.lastDayOfMonth()));
            assertEquals(BritishCutoverDate.of(2012, 2, 29), BritishCutoverDate.of(2012, 2, 1).with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_localDateAdjuster_shouldReturnCorrectDate() {
            BritishCutoverDate start = BritishCutoverDate.of(1752, 9, 2);
            LocalDate adjuster = LocalDate.of(1752, 9, 14); // This is an ISO date
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 14);
            assertEquals(expected, start.with(adjuster));
        }
    }

    @Nested
    @DisplayName("Period and Until Tests")
    class PeriodAndUntilTests {

        @ParameterizedTest(name = "{index}: until({0}) should be zero")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDatesProvider")
        void until_shouldReturnZeroPeriod_forEquivalentDates(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(cutoverDate));
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
        }

        @ParameterizedTest(name = "until({0}-{1}-{2}, {3}-{4}-{5}) in {6} -> {7}")
        @MethodSource("untilAmountProvider")
        void until_shouldCalculateCorrectAmountInUnits(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        /**
         * Provides test cases for until(end, unit).
         * Format: {y1, m1, d1, y2, m2, d2, unit, expectedAmount}
         */
        static Stream<Arguments> untilAmountProvider() {
            return Stream.of(
                Arguments.of(1752, 9, 2, 1752, 9, 14, DAYS, 1L),
                Arguments.of(1752, 9, 1, 1752, 9, 14, DAYS, 2L),
                Arguments.of(1752, 9, 2, 1752, 9, 20, WEEKS, 1L),
                Arguments.of(1752, 9, 2, 1752, 10, 2, MONTHS, 1L),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
                Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1L)
            );
        }

        @ParameterizedTest(name = "period between {0}-{1}-{2} and {3}-{4}-{5} -> {6}Y {7}M {8}D")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#untilPeriodProvider")
        void until_period_shouldBeCalculatedCorrectly(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            ChronoPeriod expected = BritishCutoverChronology.INSTANCE.period(ey, em, ed);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest(name = "start.plus(start.until(end)) should equal end")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#untilPeriodProvider")
        void plus_ofAnUntilPeriod_shouldReturnTheEndDate(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            assertEquals(end, start.plus(start.until(end)));
        }
    }

    @Nested
    @DisplayName("General API Tests")
    class GeneralApiTests {

        @Test
        void toString_shouldReturnFormattedString() {
            assertEquals("BritishCutover AD 2012-06-23", BritishCutoverDate.of(2012, 6, 23).toString());
            assertEquals("BritishCutover AD 1-01-01", BritishCutoverDate.of(1, 1, 1).toString());
        }

        @Test
        void atTime_shouldThrowException_forNullTime() {
            BritishCutoverDate date = BritishCutoverDate.of(2014, 5, 26);
            assertThrows(NullPointerException.class, () -> date.atTime(null));
        }

        @Test
        void atTime_shouldReturnCorrectLocalDateTime() {
            BritishCutoverDate date = BritishCutoverDate.of(2014, 5, 26);
            LocalTime time = LocalTime.of(10, 30);
            assertEquals(
                BritishCutoverChronology.INSTANCE.localDateTime(date, time),
                date.atTime(time)
            );
        }
    }
}