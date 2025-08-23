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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link BritishCutoverDate}.
 */
class BritishCutoverDateTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample {@link BritishCutoverDate} instances and their equivalent ISO {@link LocalDate}.
     * This data covers dates before, during, and after the 1752 cutover, including historically skipped dates
     * that are handled leniently.
     *
     * @return a stream of arguments: {BritishCutoverDate, LocalDate}.
     */
    public static Object[][] sampleDatesProvider() {
        return new Object[][]{
                {BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
                {BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)},
                {BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
                {BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
                {BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)},
                {BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)},
                // Leniently accept invalid dates that were skipped historically
                {BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)},
                {BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)},
                {BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)},
                {BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6)},
        };
    }

    /**
     * Provides invalid date components that should cause a {@link DateTimeException}.
     *
     * @return a stream of arguments: {year, month, dayOfMonth}.
     */
    public static Object[][] invalidDateProvider() {
        return new Object[][]{
                {1900, 0, 0}, {1900, -1, 1}, {1900, 13, 1}, {1900, 1, -1},
                {1900, 1, 32}, {1900, 2, 30}, {1899, 2, 29},
        };
    }

    /**
     * Provides data for testing {@link BritishCutoverDate#lengthOfMonth()}.
     *
     * @return a stream of arguments: {year, month, expectedLength}.
     */
    public static Object[][] lengthOfMonthProvider() {
        return new Object[][]{
                {1700, 2, 29}, // Julian leap year
                {1752, 9, 19}, // Cutover month
                {1800, 2, 28}, // Gregorian non-leap year
                {1900, 2, 28}, // Gregorian non-leap year
                {2000, 2, 29}, // Gregorian leap year
        };
    }

    /**
     * Provides data for testing {@link BritishCutoverDate#lengthOfYear()}.
     *
     * @return a stream of arguments: {year, expectedLength}.
     */
    public static Object[][] lengthOfYearProvider() {
        return new Object[][]{
                {1700, 366}, // Julian leap year
                {1752, 355}, // Cutover year (366 - 11 days)
                {1800, 365}, // Gregorian non-leap year
                {1900, 365}, // Gregorian non-leap year
                {2000, 366}, // Gregorian leap year
        };
    }

    /**
     * Provides data for testing {@link BritishCutoverDate#range(TemporalField)}.
     *
     * @return a stream of arguments: {year, month, day, field, expectedMin, expectedMax}.
     */
    public static Object[][] dateFieldRangeProvider() {
        return new Object[][]{
                {1700, 2, 23, DAY_OF_MONTH, 1, 29},
                {1752, 9, 23, DAY_OF_MONTH, 1, 30}, // Range is based on full month, not its actual length
                {2011, 2, 23, DAY_OF_MONTH, 1, 28},
                {1751, 1, 23, DAY_OF_YEAR, 1, 365},
                {1752, 1, 23, DAY_OF_YEAR, 1, 355},
                {1753, 1, 23, DAY_OF_YEAR, 1, 365},
                {1752, 9, 23, ALIGNED_WEEK_OF_MONTH, 1, 3},
                {2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4},
                {1752, 12, 23, ALIGNED_WEEK_OF_YEAR, 1, 51},
                {2012, 2, 23, ALIGNED_WEEK_OF_YEAR, 1, 53},
        };
    }

    /**
     * Provides data for testing {@link BritishCutoverDate#getLong(TemporalField)}.
     *
     * @return a stream of arguments: {year, month, day, field, expectedValue}.
     */
    public static Object[][] dateFieldGetProvider() {
        return new Object[][]{
                {1752, 9, 2, DAY_OF_WEEK, 3}, // Wednesday
                {1752, 9, 14, DAY_OF_WEEK, 4}, // Thursday
                {1752, 9, 14, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3}, // Day 247 -> 258 - 11
                {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
                {1, 6, 8, ERA, 1},
                {0, 6, 8, ERA, 0},
        };
    }

    /**
     * Provides data for testing {@link BritishCutoverDate#with(TemporalField, long)}.
     *
     * @return a stream of arguments: {year, month, day, field, value, expectedYear, expectedMonth, expectedDay}.
     */
    public static Object[][] withFieldProvider() {
        return new Object[][]{
                // Adjusting into the cutover gap
                {1752, 9, 2, DAY_OF_MONTH, 3, 1752, 9, 14}, // lenient: 1752-09-03 becomes 1752-09-14
                {1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3, 1752, 9, 14}, // lenient
                // Standard adjustments
                {2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 28},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                {2012, 2, 29, YEAR, 2011, 2011, 2, 28}, // Adjusting leap day to non-leap year
        };
    }

    /**
     * Provides data for testing date adjustments.
     *
     * @return a stream of arguments: {inputDate, adjusterDate, expectedDate}.
     */
    public static Object[][] withAdjusterProvider() {
        return new Object[][]{
                {BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 12), BritishCutoverDate.of(1752, 9, 1)},
                {BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 12), BritishCutoverDate.of(1752, 9, 1)},
                {BritishCutoverDate.of(2012, 2, 23), LocalDate.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 23)},
        };
    }

    /**
     * Provides data for testing {@code plus} and {@code minus} operations.
     * The `isBidirectional` flag indicates if `end.minus(amount) == start` holds true.
     * This is not always the case across the cutover.
     *
     * @return a stream of arguments: {startYear, startMonth, startDay, amount, unit, endYear, endMonth, endDay, isBidirectional}.
     */
    public static Object[][] plusMinusProvider() {
        return new Object[][]{
                {1752, 9, 2, 1, DAYS, 1752, 9, 14, true},
                {1752, 9, 14, -1, DAYS, 1752, 9, 2, true},
                {1752, 8, 12, 1, MONTHS, 1752, 9, 23, false}, // Not bidirectional due to cutover
                {2014, 5, 26, 3, YEARS, 2017, 5, 26, true},
                {2014, 5, 26, -5, DECADES, 1964, 5, 26, true},
                {2014, 5, 26, 3, CENTURIES, 2314, 5, 26, true},
                {2014, 5, 26, -5, MILLENNIA, 2014 - 5000, 5, 26, true},
                {2014, 5, 26, -1, ERAS, -2013, 5, 26, true},
        };
    }

    /**
     * Provides data for testing {@code until(end, unit)}.
     *
     * @return a stream of arguments: {startYear, startMonth, startDay, endYear, endMonth, endDay, unit, expectedAmount}.
     */
    public static Object[][] untilUnitProvider() {
        return new Object[][]{
                {1752, 9, 1, 1752, 9, 14, DAYS, 2},
                {1752, 9, 2, 1752, 9, 14, DAYS, 1},
                {1752, 9, 1, 1752, 9, 19, WEEKS, 1},
                {1752, 9, 2, 1752, 10, 2, MONTHS, 1},
                {2014, 5, 26, 2015, 5, 26, YEARS, 1},
                {-2013, 5, 26, 2014, 5, 26, ERAS, 1},
        };
    }

    /**
     * Provides data for testing {@code until(end)} which returns a {@link ChronoPeriod}.
     *
     * @return a stream of arguments: {startYear, startMonth, startDay, endYear, endMonth, endDay, expectedYears, expectedMonths, expectedDays}.
     */
    public static Object[][] untilPeriodProvider() {
        return new Object[][]{
                {1752, 7, 2, 1752, 7, 1, 0, 0, -1},
                {1752, 7, 2, 1752, 7, 2, 0, 0, 0},
                // Period calculation across the cutover
                {1752, 7, 2, 1752, 9, 14, 0, 2, 1}, // P2M1D: 2 months from 1752-07-02 is 1752-09-02. 1 day remains to 1752-09-14.
                {1752, 9, 14, 1752, 7, 14, 0, -2, 0},
        };
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    class FactoryAndConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDatesProvider")
        void from_onBritishCutoverDate_convertsToCorrectLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDatesProvider")
        void from_onLocalDate_convertsToCorrectBritishCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDatesProvider")
        void dateEpochDay_fromIsoEpochDay_createsCorrectDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDatesProvider")
        void toEpochDay_returnsCorrectValue(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDatesProvider")
        void chronologyDate_fromLocalDate_createsCorrectDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#invalidDateProvider")
        void of_withInvalidDateParts_throwsException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
        }
    }

    @Nested
    class FieldAccessTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#lengthOfMonthProvider")
        void lengthOfMonth_returnsCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#lengthOfYearProvider")
        void lengthOfYear_returnsCorrectLength(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#dateFieldRangeProvider")
        void range_forVariousFields_returnsCorrectRange(int year, int month, int day, TemporalField field, int expectedMin, int expectedMax) {
            ValueRange expectedRange = ValueRange.of(expectedMin, expectedMax);
            assertEquals(expectedRange, BritishCutoverDate.of(year, month, day).range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#dateFieldGetProvider")
        void getLong_forVariousFields_returnsCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, day).getLong(field));
        }
    }

    @Nested
    class ManipulationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDatesProvider")
        void plusDays_addsAndSubtractsDaysCorrectly(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(cutoverDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(cutoverDate.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(cutoverDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(cutoverDate.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDatesProvider")
        void minusDays_addsAndSubtractsDaysCorrectly(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate.minus(0, DAYS)));
            assertEquals(isoDate.minusDays(1), LocalDate.from(cutoverDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(cutoverDate.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(cutoverDate.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(cutoverDate.minus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#plusMinusProvider")
        void plus_withAmountAndUnit_calculatesCorrectDate(
                int startYear, int startMonth, int startDay,
                long amount, TemporalUnit unit,
                int endYear, int endMonth, int endDay,
                boolean isBidirectional) {
            BritishCutoverDate start = BritishCutoverDate.of(startYear, startMonth, startDay);
            BritishCutoverDate expectedEnd = BritishCutoverDate.of(endYear, endMonth, endDay);
            assertEquals(expectedEnd, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#plusMinusProvider")
        void minus_withAmountAndUnit_calculatesCorrectDate(
                int startYear, int startMonth, int startDay,
                long amount, TemporalUnit unit,
                int endYear, int endMonth, int endDay,
                boolean isBidirectional) {
            if (isBidirectional) {
                BritishCutoverDate end = BritishCutoverDate.of(endYear, endMonth, endDay);
                BritishCutoverDate expectedStart = BritishCutoverDate.of(startYear, startMonth, startDay);
                assertEquals(expectedStart, end.minus(amount, unit));
            }
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#withFieldProvider")
        void with_usingTemporalField_returnsModifiedDate(
                int year, int month, int day,
                TemporalField field, long value,
                int expectedYear, int expectedMonth, int expectedDay) {
            BritishCutoverDate start = BritishCutoverDate.of(year, month, day);
            BritishCutoverDate expected = BritishCutoverDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.with(field, value));
        }

        @Test
        void with_lastDayOfMonthAdjuster_returnsCorrectDate() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 30);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#withAdjusterProvider")
        void with_temporalAdjuster_returnsAdjustedDate(BritishCutoverDate input, LocalDate adjuster, BritishCutoverDate expected) {
            assertEquals(expected, input.with(adjuster));
        }

        @Test
        void minus_withUnsupportedIsoPeriod_throwsException() {
            BritishCutoverDate date = BritishCutoverDate.of(2014, 5, 26);
            assertThrows(DateTimeException.class, () -> date.minus(Period.ofMonths(2)));
        }
    }

    @Nested
    class UntilTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDatesProvider")
        void until_withSelf_returnsZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDatesProvider")
        void until_withEquivalentLocalDate_returnsZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDatesProvider")
        void until_onLocalDateWithEquivalentBritishCutoverDate_returnsZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleDatesProvider")
        void until_withDaysUnit_calculatesCorrectDistance(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(0, cutoverDate.until(isoDate.plusDays(0), DAYS));
            assertEquals(1, cutoverDate.until(isoDate.plusDays(1), DAYS));
            assertEquals(35, cutoverDate.until(isoDate.plusDays(35), DAYS));
            assertEquals(-40, cutoverDate.until(isoDate.minusDays(40), DAYS));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#untilUnitProvider")
        void until_withEndDateAndUnit_calculatesCorrectAmount(
                int year1, int month1, int day1,
                int year2, int month2, int day2,
                TemporalUnit unit, long expected) {
            BritishCutoverDate start = BritishCutoverDate.of(year1, month1, day1);
            BritishCutoverDate end = BritishCutoverDate.of(year2, month2, day2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#untilPeriodProvider")
        void until_withEndDate_calculatesCorrectPeriod(
                int year1, int month1, int day1,
                int year2, int month2, int day2,
                int expectedYears, int expectedMonths, int expectedDays) {
            BritishCutoverDate start = BritishCutoverDate.of(year1, month1, day1);
            BritishCutoverDate end = BritishCutoverDate.of(year2, month2, day2);
            ChronoPeriod expectedPeriod = BritishCutoverChronology.INSTANCE.period(expectedYears, expectedMonths, expectedDays);
            assertEquals(expectedPeriod, start.until(end));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#untilPeriodProvider")
        void until_periodIsConsistentWithPlus(
                int year1, int month1, int day1,
                int year2, int month2, int day2,
                int expectedYears, int expectedMonths, int expectedDays) {
            BritishCutoverDate start = BritishCutoverDate.of(year1, month1, day1);
            BritishCutoverDate end = BritishCutoverDate.of(year2, month2, day2);
            ChronoPeriod period = start.until(end);
            assertEquals(end, start.plus(period));
        }
    }

    @Nested
    class GeneralMethodTests {

        @Test
        void toString_returnsCorrectFormat() {
            assertEquals("BritishCutover AD 1-01-01", BritishCutoverDate.of(1, 1, 1).toString());
            assertEquals("BritishCutover AD 2012-06-23", BritishCutoverDate.of(2012, 6, 23).toString());
        }
    }
}