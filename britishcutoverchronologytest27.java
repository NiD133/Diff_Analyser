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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link BritishCutoverChronology} and {@link BritishCutoverDate}.
 * This suite covers conversions, date calculations, and edge cases around the 1752 cutover.
 */
public class BritishCutoverChronologyTest {

    //-----------------------------------------------------------------------
    // data_samples() and related tests
    //-----------------------------------------------------------------------

    /**
     * Provides sample BritishCutoverDates and their equivalent ISO LocalDates.
     * Covers dates before, during, and after the 1752 cutover.
     *
     * @return a stream of arguments: {BritishCutoverDate, corresponding ISO LocalDate}.
     */
    public static Object[][] data_samples() {
        return new Object[][] {
            // Pre-cutover dates (Julian calendar)
            { BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30) },
            { BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1) },
            { BritishCutoverDate.of(4, 2, 29), LocalDate.of(4, 2, 27) }, // Julian leap year
            { BritishCutoverDate.of(100, 2, 29), LocalDate.of(100, 2, 27) }, // Julian leap year
            { BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14) },
            { BritishCutoverDate.of(1751, 12, 31), LocalDate.of(1752, 1, 11) },

            // Dates around the British cutover (September 1752)
            { BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13) }, // Last day of Julian in Britain
            { BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14) }, // Leniently handles invalid date, becomes 14th
            { BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24) }, // Leniently handles invalid date
            { BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14) }, // First day of Gregorian in Britain

            // Post-cutover dates (Gregorian calendar)
            { BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12) },
            { BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6) }
        };
    }

    @ParameterizedTest(name = "{index}: BritishCutoverDate {0} -> ISO {1}")
    @MethodSource("data_samples")
    @DisplayName("Conversion to ISO LocalDate")
    void fromBritishCutoverDate_convertsToCorrectIsoDate(BritishCutoverDate britishCutoverDate, LocalDate expectedIsoDate) {
        assertEquals(expectedIsoDate, LocalDate.from(britishCutoverDate));
    }

    @ParameterizedTest(name = "{index}: ISO {1} -> BritishCutoverDate {0}")
    @MethodSource("data_samples")
    @DisplayName("Conversion from ISO LocalDate")
    void fromIsoDate_convertsToCorrectBritishCutoverDate(BritishCutoverDate expectedBritishDate, LocalDate isoDate) {
        assertEquals(expectedBritishDate, BritishCutoverDate.from(isoDate));
    }

    @ParameterizedTest(name = "{index}: Epoch day of {1} -> BritishCutoverDate {0}")
    @MethodSource("data_samples")
    @DisplayName("Creation from epoch day")
    void chronologyDateFromEpochDay_recreatesCorrectDate(BritishCutoverDate expectedBritishDate, LocalDate isoDate) {
        assertEquals(expectedBritishDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
    }

    @ParameterizedTest(name = "{index}: {0}.toEpochDay() == {1}.toEpochDay()")
    @MethodSource("data_samples")
    @DisplayName("Epoch day calculation")
    void toEpochDay_matchesIsoDateEpochDay(BritishCutoverDate britishCutoverDate, LocalDate isoDate) {
        assertEquals(isoDate.toEpochDay(), britishCutoverDate.toEpochDay());
    }

    @ParameterizedTest(name = "{index}: {0}.until({0}) is zero")
    @MethodSource("data_samples")
    @DisplayName("Period until self is zero")
    void untilSelf_returnsZeroPeriod(BritishCutoverDate britishCutoverDate, LocalDate isoDate) {
        assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), britishCutoverDate.until(britishCutoverDate));
    }

    @ParameterizedTest(name = "{index}: {0}.until({1}) is zero")
    @MethodSource("data_samples")
    @DisplayName("Period until equivalent ISO date is zero")
    void untilEquivalentIsoDate_returnsZeroPeriod(BritishCutoverDate britishCutoverDate, LocalDate isoDate) {
        assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), britishCutoverDate.until(isoDate));
    }

    @ParameterizedTest(name = "{index}: {1}.until({0}) is zero")
    @MethodSource("data_samples")
    @DisplayName("ISO date period until equivalent British date is zero")
    void isoDateUntilEquivalentBritishCutoverDate_returnsZeroPeriod(BritishCutoverDate britishCutoverDate, LocalDate isoDate) {
        assertEquals(Period.ZERO, isoDate.until(britishCutoverDate));
    }

    @ParameterizedTest(name = "{index}: Chronology.date({1}) -> {0}")
    @MethodSource("data_samples")
    @DisplayName("Chronology creation from TemporalAccessor")
    void chronologyDateFromTemporal_convertsToCorrectBritishCutoverDate(BritishCutoverDate expectedBritishDate, LocalDate isoDate) {
        assertEquals(expectedBritishDate, BritishCutoverChronology.INSTANCE.date(isoDate));
    }

    //-----------------------------------------------------------------------
    // Day-based arithmetic tests
    //-----------------------------------------------------------------------

    @ParameterizedTest(name = "{index}: {0}.plus(n, DAYS)")
    @MethodSource("data_samples")
    @DisplayName("Adding days works correctly")
    void plusDays_addsCorrectly(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(isoDate, LocalDate.from(cutoverDate.plus(0, DAYS)));
        assertEquals(isoDate.plusDays(1), LocalDate.from(cutoverDate.plus(1, DAYS)));
        assertEquals(isoDate.plusDays(35), LocalDate.from(cutoverDate.plus(35, DAYS)));
        assertEquals(isoDate.plusDays(-1), LocalDate.from(cutoverDate.plus(-1, DAYS)));
        assertEquals(isoDate.plusDays(-60), LocalDate.from(cutoverDate.plus(-60, DAYS)));
    }

    @ParameterizedTest(name = "{index}: {0}.minus(n, DAYS)")
    @MethodSource("data_samples")
    @DisplayName("Subtracting days works correctly")
    void minusDays_subtractsCorrectly(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(isoDate, LocalDate.from(cutoverDate.minus(0, DAYS)));
        assertEquals(isoDate.minusDays(1), LocalDate.from(cutoverDate.minus(1, DAYS)));
        assertEquals(isoDate.minusDays(35), LocalDate.from(cutoverDate.minus(35, DAYS)));
        assertEquals(isoDate.minusDays(-1), LocalDate.from(cutoverDate.minus(-1, DAYS)));
        assertEquals(isoDate.minusDays(-60), LocalDate.from(cutoverDate.minus(-60, DAYS)));
    }

    @ParameterizedTest(name = "{index}: {0}.until(..., DAYS)")
    @MethodSource("data_samples")
    @DisplayName("Calculating days between dates works correctly")
    void until_calculatesDaysBetweenCorrectly(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(0, cutoverDate.until(isoDate.plusDays(0), DAYS));
        assertEquals(1, cutoverDate.until(isoDate.plusDays(1), DAYS));
        assertEquals(35, cutoverDate.until(isoDate.plusDays(35), DAYS));
        assertEquals(-40, cutoverDate.until(isoDate.minusDays(40), DAYS));
    }

    //-----------------------------------------------------------------------
    // Invalid date tests
    //-----------------------------------------------------------------------

    public static Object[][] data_badDates() {
        return new Object[][] {
            // Invalid month
            { 1900, 0, 1 }, { 1900, -1, 1 }, { 1900, 13, 1 },
            // Invalid day of month
            { 1900, 1, 0 }, { 1900, 1, -1 }, { 1900, 1, 32 },
            // Invalid day for February (non-leap)
            { 1899, 2, 29 }, { 1900, 2, 30 },
            // Invalid day for April
            { 1900, 4, 31 },
        };
    }

    @ParameterizedTest(name = "of({0}, {1}, {2})")
    @MethodSource("data_badDates")
    @DisplayName("Creation with invalid date parts throws exception")
    void of_withInvalidDateParts_throwsDateTimeException(int year, int month, int dayOfMonth) {
        assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
    }

    //-----------------------------------------------------------------------
    // lengthOfMonth() tests
    //-----------------------------------------------------------------------

    public static Object[][] data_lengthOfMonth() {
        return new Object[][] {
            // Julian leap year (divisible by 4)
            { 1700, 2, 29 },
            // Gregorian non-leap year (divisible by 100 but not 400)
            { 1800, 2, 28 }, { 1900, 2, 28 },
            // Gregorian leap year (divisible by 400)
            { 1600, 2, 29 }, { 2000, 2, 29 },
            // Cutover year 1752
            { 1752, 1, 31 }, { 1752, 2, 29 }, // Leap year in Julian
            { 1752, 8, 31 },
            { 1752, 9, 19 }, // September 1752 had 19 days (2nd followed by 14th)
            { 1752, 10, 31 },
            // Standard months
            { 1753, 1, 31 }, { 1753, 2, 28 }, { 1753, 4, 30 },
        };
    }

    @ParameterizedTest(name = "{0}-{1} has {2} days")
    @MethodSource("data_lengthOfMonth")
    @DisplayName("lengthOfMonth returns correct length for various years")
    void lengthOfMonth_returnsCorrectLength(int year, int month, int expectedLength) {
        assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
    }

    //-----------------------------------------------------------------------
    // lengthOfYear() tests
    //-----------------------------------------------------------------------

    public static Object[][] data_lengthOfYear() {
        return new Object[][] {
            // Julian leap years
            { 0, 366 }, { 100, 366 }, { 1600, 366 }, { 1700, 366 },
            // Gregorian non-leap years
            { 1800, 365 }, { 1900, 365 }, { 2100, 365 },
            // Gregorian leap years
            { 1904, 366 }, { 2000, 366 },
            // Cutover year 1752 (lost 11 days)
            { 1752, 355 }, // 366 - 11 = 355
            // Year before cutover
            { 1751, 365 },
            // Year after cutover
            { 1753, 365 },
        };
    }

    @ParameterizedTest(name = "Year {0} has {1} days")
    @MethodSource("data_lengthOfYear")
    @DisplayName("lengthOfYear returns correct length for various years")
    void lengthOfYear_returnsCorrectLength(int year, int expectedLength) {
        assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
        assertEquals(expectedLength, BritishCutoverDate.of(year, 12, 31).lengthOfYear());
    }

    //-----------------------------------------------------------------------
    // range() tests
    //-----------------------------------------------------------------------

    public static Object[][] data_ranges() {
        return new Object[][] {
            // DAY_OF_MONTH
            { 1700, 2, 23, DAY_OF_MONTH, 1, 29 }, // Julian leap year
            { 1751, 2, 23, DAY_OF_MONTH, 1, 28 }, // Julian non-leap
            { 1752, 9, 23, DAY_OF_MONTH, 1, 30 }, // Cutover month has lenient range
            { 2011, 2, 23, DAY_OF_MONTH, 1, 28 }, // Gregorian non-leap
            { 2012, 2, 23, DAY_OF_MONTH, 1, 29 }, // Gregorian leap

            // DAY_OF_YEAR
            { 1700, 1, 23, DAY_OF_YEAR, 1, 366 }, // Julian leap
            { 1751, 1, 23, DAY_OF_YEAR, 1, 365 }, // Julian non-leap
            { 1752, 1, 23, DAY_OF_YEAR, 1, 355 }, // Cutover year
            { 1753, 1, 23, DAY_OF_YEAR, 1, 365 }, // Gregorian non-leap
            { 2012, 1, 23, DAY_OF_YEAR, 1, 366 }, // Gregorian leap

            // ALIGNED_WEEK_OF_MONTH
            { 1752, 9, 23, ALIGNED_WEEK_OF_MONTH, 1, 3 }, // Cutover month has fewer weeks
            { 2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4 }, // 28-day month
            { 2012, 1, 23, ALIGNED_WEEK_OF_MONTH, 1, 5 }, // 31-day month

            // ALIGNED_WEEK_OF_YEAR
            { 1752, 12, 23, ALIGNED_WEEK_OF_YEAR, 1, 51 }, // Cutover year has fewer weeks
            { 2011, 2, 23, ALIGNED_WEEK_OF_YEAR, 1, 53 },
        };
    }

    @ParameterizedTest(name = "{0}-{1}-{2}, range({3}) is {4}-{5}")
    @MethodSource("data_ranges")
    @DisplayName("range() returns correct value range for a given field")
    void range_forGivenField_returnsCorrectValueRange(int year, int month, int day, TemporalField field, int expectedMin, int expectedMax) {
        ValueRange expectedRange = ValueRange.of(expectedMin, expectedMax);
        assertEquals(expectedRange, BritishCutoverDate.of(year, month, day).range(field));
    }

    //-----------------------------------------------------------------------
    // getLong() tests
    //-----------------------------------------------------------------------

    public static Object[][] data_getLong() {
        return new Object[][] {
            // Pre-cutover date: 1752-05-26 (Julian)
            { 1752, 5, 26, DAY_OF_WEEK, 2 }, // Tuesday
            { 1752, 5, 26, DAY_OF_MONTH, 26 },
            { 1752, 5, 26, DAY_OF_YEAR, 147 }, // 31(Jan)+29(Feb-leap)+31(Mar)+30(Apr)+26(May)
            { 1752, 5, 26, MONTH_OF_YEAR, 5 },

            // Date in the cutover gap (treated as Julian): 1752-09-02
            { 1752, 9, 2, DAY_OF_WEEK, 3 }, // Wednesday
            { 1752, 9, 2, DAY_OF_MONTH, 2 },
            { 1752, 9, 2, DAY_OF_YEAR, 246 }, // 31+29+31+30+31+30+31+31+2
            { 1752, 9, 2, MONTH_OF_YEAR, 9 },

            // Date after the cutover: 1752-09-14 (Gregorian)
            { 1752, 9, 14, DAY_OF_WEEK, 4 }, // Thursday
            { 1752, 9, 14, DAY_OF_MONTH, 14 },
            { 1752, 9, 14, DAY_OF_YEAR, 247 }, // Day of year is continuous, 1752-09-02 is day 246
            { 1752, 9, 14, MONTH_OF_YEAR, 9 },

            // Modern date: 2014-05-26 (Gregorian)
            { 2014, 5, 26, DAY_OF_WEEK, 1 }, // Monday
            { 2014, 5, 26, DAY_OF_MONTH, 26 },
            { 2014, 5, 26, DAY_OF_YEAR, 146 }, // 31+28+31+30+26
            { 2014, 5, 26, MONTH_OF_YEAR, 5 },
            { 2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1 },
            { 2014, 5, 26, YEAR, 2014 },
            { 2014, 5, 26, ERA, 1 }, // AD
            { 0, 6, 8, ERA, 0 }, // BC
            { 2014, 5, 26, WeekFields.ISO.dayOfWeek(), 1 },
        };
    }

    @ParameterizedTest(name = "{0}-{1}-{2}, getLong({3}) is {4}")
    @MethodSource("data_getLong")
    @DisplayName("getLong() returns correct value for a given field")
    void getLong_forGivenField_returnsCorrectValue(int year, int month, int day, TemporalField field, long expected) {
        assertEquals(expected, BritishCutoverDate.of(year, month, day).getLong(field));
    }

    //-----------------------------------------------------------------------
    // with() tests
    //-----------------------------------------------------------------------

    public static Object[][] data_with() {
        return new Object[][] {
            // --- Adjustments around the cutover ---
            // Adjusting DAY_OF_WEEK from 1752-09-02 (Julian)
            { 1752, 9, 2, DAY_OF_WEEK, 1, 1752, 8, 31 }, // Monday before
            { 1752, 9, 2, DAY_OF_WEEK, 4, 1752, 9, 14 }, // Thursday after (skips gap)

            // Adjusting DAY_OF_MONTH from 1752-09-02 into the gap (lenient)
            { 1752, 9, 2, DAY_OF_MONTH, 3, 1752, 9, 14 }, // 3rd becomes 14th
            { 1752, 9, 2, DAY_OF_MONTH, 13, 1752, 9, 24 }, // 13th becomes 24th

            // Adjusting DAY_OF_YEAR from 1752-09-02 into the gap (lenient)
            { 1752, 9, 2, DAY_OF_YEAR, 247, 1752, 9, 14 }, // Day 247 is Sep 14

            // Adjusting MONTH_OF_YEAR from a date near the cutover
            { 1752, 8, 4, MONTH_OF_YEAR, 9, 1752, 9, 15 }, // Adjusting into cutover month (lenient)
            { 1752, 10, 8, MONTH_OF_YEAR, 9, 1752, 9, 19 },// Adjusting into cutover month (lenient)

            // Adjusting YEAR across the cutover
            { 1751, 9, 4, YEAR, 1752, 1752, 9, 15 }, // Adjusting into cutover year (lenient)
            { 1753, 9, 8, YEAR, 1752, 1752, 9, 19 }, // Adjusting into cutover year (lenient)

            // --- Standard adjustments ---
            { 2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 28 }, // Monday to Wednesday
            { 2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31 },
            { 2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26 },
            { 2014, 5, 26, YEAR, 2012, 2012, 5, 26 },
            { 2014, 5, 26, ERA, 0, -2013, 5, 26 }, // AD to BC

            // --- Adjustments resulting in shorter months ---
            { 2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28 }, // March to Feb (non-leap)
            { 2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29 }, // March to Feb (leap)
            { 2012, 2, 29, YEAR, 2011, 2011, 2, 28 }, // Leap to non-leap year
        };
    }

    @ParameterizedTest(name = "{0}-{1}-{2}.with({3}, {4}) -> {5}-{6}-{7}")
    @MethodSource("data_with")
    @DisplayName("with() returns correctly adjusted date")
    void with_forGivenFieldAndValue_returnsAdjustedDate(int year, int month, int dom, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDom) {
        BritishCutoverDate start = BritishCutoverDate.of(year, month, dom);
        BritishCutoverDate expected = BritishCutoverDate.of(expectedYear, expectedMonth, expectedDom);
        assertEquals(expected, start.with(field, value));
    }

    //-----------------------------------------------------------------------
    // TemporalAdjuster tests
    //-----------------------------------------------------------------------

    public static Object[][] data_lastDayOfMonth() {
        return new Object[][] {
            { BritishCutoverDate.of(1752, 2, 23), BritishCutoverDate.of(1752, 2, 29) }, // Julian leap
            { BritishCutoverDate.of(1752, 6, 23), BritishCutoverDate.of(1752, 6, 30) },
            { BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 30) }, // Cutover month
            { BritishCutoverDate.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 29) }, // Gregorian leap
        };
    }

    @ParameterizedTest(name = "{0} adjusted to last day of month is {1}")
    @MethodSource("data_lastDayOfMonth")
    @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) returns correct date")
    void with_lastDayOfMonthAdjuster_returnsCorrectDate(BritishCutoverDate input, BritishCutoverDate expected) {
        assertEquals(expected, input.with(TemporalAdjusters.lastDayOfMonth()));
    }

    public static Object[][] data_withLocalDate() {
        return new Object[][] {
            // Adjusting across the cutover
            { BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 12), BritishCutoverDate.of(1752, 9, 1) },
            { BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 12), BritishCutoverDate.of(1752, 9, 1) },
            { BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 14) },
            // Standard adjustment
            { BritishCutoverDate.of(2012, 2, 23), LocalDate.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 23) },
        };
    }

    @ParameterizedTest(name = "{0}.with({1}) is {2}")
    @MethodSource("data_withLocalDate")
    @DisplayName("with(LocalDate) adjuster returns correct date")
    void with_localDateAdjuster_returnsCorrectDate(BritishCutoverDate input, LocalDate adjuster, BritishCutoverDate expected) {
        assertEquals(expected, input.with(adjuster));
    }

    //-----------------------------------------------------------------------
    // plus() and minus() tests
    //-----------------------------------------------------------------------

    public static Object[][] data_plus() {
        return new Object[][] {
            // --- DAYS ---
            { 1752, 9, 2, 1, DAYS, 1752, 9, 14, true }, // Add 1 day crosses the 11-day gap
            { 1752, 9, 14, -1, DAYS, 1752, 9, 2, true }, // Subtract 1 day crosses back
            { 2014, 5, 26, 8, DAYS, 2014, 6, 3, true },

            // --- WEEKS ---
            { 1752, 9, 2, 1, WEEKS, 1752, 9, 20, true }, // Add 1 week from before cutover
            { 1752, 9, 14, -1, WEEKS, 1752, 8, 27, true }, // Subtract 1 week from after cutover

            // --- MONTHS ---
            { 1752, 8, 2, 1, MONTHS, 1752, 9, 2, true },
            { 1752, 9, 2, 1, MONTHS, 1752, 10, 2, true },
            { 1752, 8, 12, 1, MONTHS, 1752, 9, 23, false }, // Not reversible due to day-of-month adjustment
            { 1752, 10, 12, -1, MONTHS, 1752, 9, 23, false }, // Not reversible

            // --- YEARS and larger units ---
            { 2014, 5, 26, 3, YEARS, 2017, 5, 26, true },
            { 2014, 5, 26, 3, DECADES, 2044, 5, 26, true },
            { 2014, 5, 26, 3, CENTURIES, 2314, 5, 26, true },
            { 2014, 5, 26, 3, MILLENNIA, 5014, 5, 26, true },
            { 2014, 5, 26, -1, ERAS, -2013, 5, 26, true },
        };
    }

    @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
    @MethodSource("data_plus")
    @DisplayName("plus() adds amount correctly for various units")
    void plus_forGivenUnit_returnsCorrectlyAddedDate(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom, boolean isReversible) {
        BritishCutoverDate start = BritishCutoverDate.of(year, month, dom);
        BritishCutoverDate expected = BritishCutoverDate.of(expectedYear, expectedMonth, expectedDom);
        assertEquals(expected, start.plus(amount, unit));
    }

    @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4} -> {0}-{1}-{2}")
    @MethodSource("data_plus")
    @DisplayName("minus() subtracts amount correctly for various units")
    void minus_forGivenUnit_returnsCorrectlySubtractedDate(int expectedYear, int expectedMonth, int expectedDom, long amount, TemporalUnit unit, int year, int month, int dom, boolean isReversible) {
        if (isReversible) {
            BritishCutoverDate start = BritishCutoverDate.of(year, month, dom);
            BritishCutoverDate expected = BritishCutoverDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.minus(amount, unit));
        }
    }

    //-----------------------------------------------------------------------
    // until() tests
    //-----------------------------------------------------------------------

    public static Object[][] data_until() {
        return new Object[][] {
            // --- DAYS ---
            { 1752, 9, 1, 1752, 9, 2, DAYS, 1 },
            { 1752, 9, 2, 1752, 9, 14, DAYS, 1 }, // Only 1 day between Sep 2 and Sep 14
            { 1752, 9, 1, 1752, 9, 14, DAYS, 2 }, // Two single-day steps

            // --- WEEKS ---
            { 1752, 9, 2, 1752, 9, 14, WEEKS, 0 }, // Less than a week
            { 1752, 9, 2, 1752, 9, 20, WEEKS, 1 }, // 1 full week (7 days in epoch)

            // --- MONTHS ---
            { 1752, 9, 2, 1752, 10, 1, MONTHS, 0 }, // Less than a month
            { 1752, 9, 2, 1752, 10, 2, MONTHS, 1 }, // Exactly one month

            // --- YEARS and larger units ---
            { 2014, 5, 26, 2015, 5, 25, YEARS, 0 },
            { 2014, 5, 26, 2015, 5, 26, YEARS, 1 },
            { -2013, 5, 26, 2014, 5, 26, ERAS, 1 },
        };
    }

    @ParameterizedTest(name = "until({0}-{1}-{2} to {3}-{4}-{5}) in {6} is {7}")
    @MethodSource("data_until")
    @DisplayName("until() calculates amount correctly for various units")
    void until_forGivenUnit_returnsCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
        BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
        BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
        assertEquals(expected, start.until(end, unit));
    }

    public static Object[][] data_untilCLD() {
        return new Object[][] {
            // --- Basic cases ---
            { 1752, 7, 2, 1752, 7, 2, 0, 0, 0 }, // Same date
            { 1752, 7, 2, 1752, 7, 1, 0, 0, -1 }, // One day before

            // --- Spanning the cutover ---
            // Start: 1752-08-02, End: 1752-09-02 -> 1 month
            { 1752, 8, 2, 1752, 9, 2, 0, 1, 0 },
            // Start: 1752-08-02, End: 1752-09-14 -> 1 month, 1 day
            { 1752, 8, 2, 1752, 9, 14, 0, 1, 1 },
            // Start: 1752-09-02, End: 1752-09-14 -> 1 day
            { 1752, 9, 2, 1752, 9, 14, 0, 0, 1 },
            // Start: 1752-09-02, End: 1752-10-02 -> 1 month
            { 1752, 9, 2, 1752, 10, 2, 0, 1, 0 },
            // Start: 1752-09-14, End: 1752-10-14 -> 1 month
            { 1752, 9, 14, 1752, 10, 14, 0, 1, 0 },
        };
    }

    @ParameterizedTest(name = "Period from {0}-{1}-{2} to {3}-{4}-{5} is P{6}Y{7}M{8}D")
    @MethodSource("data_untilCLD")
    @DisplayName("until() calculates ChronoPeriod correctly across cutover")
    void until_periodBetweenTwoDates_isCalculatedCorrectly(int y1, int m1, int d1, int y2, int m2, int d2, int eY, int eM, int eD) {
        BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
        BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
        ChronoPeriod expectedPeriod = BritishCutoverChronology.INSTANCE.period(eY, eM, eD);
        assertEquals(expectedPeriod, start.until(end));
    }

    @ParameterizedTest(name = "start.plus(start.until(end)) == end")
    @MethodSource("data_untilCLD")
    @DisplayName("plus(period) is the inverse of until()")
    void plusPeriod_isInverseOfUntil(int y1, int m1, int d1, int y2, int m2, int d2, int eY, int eM, int eD) {
        BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
        BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
        ChronoPeriod period = start.until(end);
        assertEquals(end, start.plus(period));
    }

    //-----------------------------------------------------------------------
    // toString() and atTime() tests
    //-----------------------------------------------------------------------

    public static Object[][] data_toString() {
        return new Object[][] {
            { BritishCutoverDate.of(1, 1, 1), "BritishCutover AD 1-01-01" },
            { BritishCutoverDate.of(2012, 6, 23), "BritishCutover AD 2012-06-23" },
        };
    }

    @ParameterizedTest(name = "{0}.toString() -> \"{1}\"")
    @MethodSource("data_toString")
    @DisplayName("toString() returns correctly formatted string")
    void toString_returnsFormattedDateString(BritishCutoverDate cutoverDate, String expected) {
        assertEquals(expected, cutoverDate.toString());
    }

    @Test
    @DisplayName("atTime() combines date with time correctly")
    void atTime_combinesDateWithTimeCorrectly() {
        BritishCutoverDate date = BritishCutoverDate.of(2014, 10, 12);
        LocalTime time = LocalTime.of(12, 30);
        ChronoLocalDateTime<BritishCutoverDate> dateTime = date.atTime(time);

        assertEquals(date, dateTime.toLocalDate());
        assertEquals(time, dateTime.toLocalTime());

        // Verify that converting back and forth yields the same object
        ChronoLocalDateTime<BritishCutoverDate> convertedDateTime =
            BritishCutoverChronology.INSTANCE.localDateTime(LocalDateTime.from(dateTime));
        assertEquals(dateTime, convertedDateTime);
    }
}