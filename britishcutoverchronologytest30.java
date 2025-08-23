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

import com.google.common.testing.EqualsTester;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link BritishCutoverDate}.
 * <p>
 * This class focuses on the conversion, manipulation, and properties of
 * {@code BritishCutoverDate} instances, including their interaction with
 * {@code LocalDate} and various temporal fields and units.
 */
public class BritishCutoverDateTest {

    //-----------------------------------------------------------------------
    // data_samples
    //-----------------------------------------------------------------------
    static Object[][] sampleCutoverAndIsoDatesProvider() {
        return new Object[][] {
            // Early Julian dates
            { BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30) },
            { BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31) },
            { BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1) },
            { BritishCutoverDate.of(4, 3, 2), LocalDate.of(4, 2, 29) },
            { BritishCutoverDate.of(100, 3, 2), LocalDate.of(100, 3, 1) },
            { BritishCutoverDate.of(0, 12, 31), LocalDate.of(0, 12, 29) },
            // Dates around Gregorian cutover (not the British one)
            { BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14) },
            { BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15) },
            // Dates around British cutover year start change
            { BritishCutoverDate.of(1751, 12, 20), LocalDate.of(1751, 12, 31) },
            { BritishCutoverDate.of(1751, 12, 31), LocalDate.of(1752, 1, 11) },
            { BritishCutoverDate.of(1752, 1, 1), LocalDate.of(1752, 1, 12) },
            // Dates around British cutover gap
            { BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13) },
            { BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14) }, // leniently accept invalid date
            { BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24) },
            { BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14) },
            // Modern dates
            { BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12) },
            { BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6) }
        };
    }

    @ParameterizedTest(name = "{index}: {0} -> {1}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    void fromBritishCutoverDate_shouldConvertToCorrectLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(isoDate, LocalDate.from(cutoverDate));
    }

    @ParameterizedTest(name = "{index}: {1} -> {0}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    void fromLocalDate_shouldConvertToCorrectBritishCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
    }

    @ParameterizedTest(name = "{index}: epochDay({1}) -> {0}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    void chronologyDateEpochDay_shouldCreateCorrectDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
    }

    @ParameterizedTest(name = "{index}: {0}.toEpochDay() -> {1}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    void toEpochDay_shouldConvertToCorrectEpochDay(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
    }

    @ParameterizedTest(name = "{index}: {0}.until({0}) is zero")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    void until_sameDate_shouldReturnZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(cutoverDate));
    }

    @ParameterizedTest(name = "{index}: {0}.until({1}) is zero")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    void until_equivalentLocalDate_shouldReturnZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
    }

    @ParameterizedTest(name = "{index}: {1}.until({0}) is zero")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    void localDateUntil_equivalentBritishCutoverDate_shouldReturnZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(Period.ZERO, isoDate.until(cutoverDate));
    }

    @ParameterizedTest(name = "{index}: chronology.date({1}) -> {0}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    void chronologyDate_fromLocalDate_shouldCreateCorrectDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    void plusDays_shouldBehaveConsistentlyWithLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(isoDate, LocalDate.from(cutoverDate.plus(0, DAYS)));
        assertEquals(isoDate.plusDays(1), LocalDate.from(cutoverDate.plus(1, DAYS)));
        assertEquals(isoDate.plusDays(35), LocalDate.from(cutoverDate.plus(35, DAYS)));
        assertEquals(isoDate.plusDays(-1), LocalDate.from(cutoverDate.plus(-1, DAYS)));
        assertEquals(isoDate.plusDays(-60), LocalDate.from(cutoverDate.plus(-60, DAYS)));
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    void minusDays_shouldBehaveConsistentlyWithLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(isoDate, LocalDate.from(cutoverDate.minus(0, DAYS)));
        assertEquals(isoDate.minusDays(1), LocalDate.from(cutoverDate.minus(1, DAYS)));
        assertEquals(isoDate.minusDays(35), LocalDate.from(cutoverDate.minus(35, DAYS)));
        assertEquals(isoDate.minusDays(-1), LocalDate.from(cutoverDate.minus(-1, DAYS)));
        assertEquals(isoDate.minusDays(-60), LocalDate.from(cutoverDate.minus(-60, DAYS)));
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    void until_days_shouldCalculateCorrectDistance(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(0, cutoverDate.until(isoDate.plusDays(0), DAYS));
        assertEquals(1, cutoverDate.until(isoDate.plusDays(1), DAYS));
        assertEquals(35, cutoverDate.until(isoDate.plusDays(35), DAYS));
        assertEquals(-40, cutoverDate.until(isoDate.minusDays(40), DAYS));
    }

    //-----------------------------------------------------------------------
    // of(y, m, d) - invalid
    //-----------------------------------------------------------------------
    static Object[][] invalidDateProvider() {
        return new Object[][] {
            { 1900, 0, 0 }, { 1900, -1, 1 }, { 1900, 0, 1 }, { 1900, 13, 1 },
            { 1900, 1, -1 }, { 1900, 1, 0 }, { 1900, 1, 32 },
            { 1900, 2, 30 }, // Not a leap year
            { 1899, 2, 29 }, // Not a leap year
            { 1900, 4, 31 }, { 1900, 6, 31 }, { 1900, 9, 31 }, { 1900, 11, 31 }
        };
    }

    @ParameterizedTest(name = "of({0}, {1}, {2})")
    @MethodSource("invalidDateProvider")
    void of_withInvalidDateComponents_throwsException(int year, int month, int dayOfMonth) {
        assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
    }

    //-----------------------------------------------------------------------
    // lengthOfMonth()
    //-----------------------------------------------------------------------
    static Object[][] lengthOfMonthProvider() {
        return new Object[][] {
            { 1700, 1, 31 }, { 1700, 2, 29 }, { 1700, 3, 31 }, // Julian leap year
            { 1752, 2, 29 }, { 1752, 8, 31 }, { 1752, 9, 19 }, { 1752, 10, 31 }, // Cutover year
            { 1800, 2, 28 }, { 1900, 2, 28 }, // Gregorian non-leap years
            { 1904, 2, 29 }, { 2000, 2, 29 }  // Gregorian leap years
        };
    }

    @ParameterizedTest(name = "{0}-{1}, expected length={2}")
    @MethodSource("lengthOfMonthProvider")
    void lengthOfMonth_returnsCorrectLength(int year, int month, int expectedLength) {
        assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
    }

    //-----------------------------------------------------------------------
    // lengthOfYear()
    //-----------------------------------------------------------------------
    static Object[][] lengthOfYearProvider() {
        return new Object[][] {
            { 0, 366 }, { 100, 366 }, { 1600, 366 }, { 1700, 366 }, // Julian leap years
            { 1751, 365 }, { 1752, 355 }, { 1753, 365 }, // Around cutover
            { 1800, 365 }, { 1900, 365 }, // Gregorian non-leap years
            { 1904, 366 }, { 2000, 366 }  // Gregorian leap years
        };
    }

    @ParameterizedTest(name = "Year {0}, expected length={1}")
    @MethodSource("lengthOfYearProvider")
    void lengthOfYear_fromStartOfYear_returnsCorrectLength(int year, int expectedLength) {
        assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
    }

    @ParameterizedTest(name = "Year {0}, expected length={1}")
    @MethodSource("lengthOfYearProvider")
    void lengthOfYear_fromEndOfYear_returnsCorrectLength(int year, int expectedLength) {
        assertEquals(expectedLength, BritishCutoverDate.of(year, 12, 31).lengthOfYear());
    }

    //-----------------------------------------------------------------------
    // range(TemporalField)
    //-----------------------------------------------------------------------
    static Object[][] dateFieldRangeProvider() {
        return new Object[][] {
            // DAY_OF_MONTH
            { 1700, 2, 23, DAY_OF_MONTH, 1, 29 }, // Julian leap
            { 1751, 2, 23, DAY_OF_MONTH, 1, 28 }, // Julian non-leap
            { 1752, 9, 23, DAY_OF_MONTH, 1, 30 }, // Cutover month (lenient range)
            { 2011, 2, 23, DAY_OF_MONTH, 1, 28 }, // Gregorian non-leap
            { 2012, 2, 23, DAY_OF_MONTH, 1, 29 }, // Gregorian leap

            // DAY_OF_YEAR
            { 1700, 1, 23, DAY_OF_YEAR, 1, 366 }, // Julian leap
            { 1751, 1, 23, DAY_OF_YEAR, 1, 365 }, // Julian non-leap
            { 1752, 1, 23, DAY_OF_YEAR, 1, 355 }, // Cutover year
            { 2011, 2, 23, DAY_OF_YEAR, 1, 365 }, // Gregorian non-leap
            { 2012, 1, 23, DAY_OF_YEAR, 1, 366 }, // Gregorian leap

            // ALIGNED_WEEK_OF_MONTH
            { 1752, 9, 23, ALIGNED_WEEK_OF_MONTH, 1, 3 }, // Cutover month
            { 2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4 },
            { 2012, 1, 23, ALIGNED_WEEK_OF_MONTH, 1, 5 },

            // ALIGNED_WEEK_OF_YEAR
            { 1752, 12, 23, ALIGNED_WEEK_OF_YEAR, 1, 51 }, // Cutover year
            { 2011, 2, 23, ALIGNED_WEEK_OF_YEAR, 1, 53 },
            { 2012, 2, 23, ALIGNED_WEEK_OF_YEAR, 1, 53 },
        };
    }

    @ParameterizedTest(name = "{0}-{1}-{2}, range({3})")
    @MethodSource("dateFieldRangeProvider")
    void range_forField_returnsCorrectRange(int year, int month, int dayOfMonth, TemporalField field, long expectedMin, long expectedMax) {
        ValueRange expectedRange = ValueRange.of(expectedMin, expectedMax);
        assertEquals(expectedRange, BritishCutoverDate.of(year, month, dayOfMonth).range(field));
    }

    //-----------------------------------------------------------------------
    // getLong(TemporalField)
    //-----------------------------------------------------------------------
    static Object[][] dateFieldGetProvider() {
        return new Object[][] {
            { 1752, 9, 2, DAY_OF_WEEK, 3 },
            { 1752, 9, 2, DAY_OF_MONTH, 2 },
            { 1752, 9, 2, DAY_OF_YEAR, 246 },
            { 1752, 9, 14, DAY_OF_WEEK, 4 },
            { 1752, 9, 14, DAY_OF_MONTH, 14 },
            { 1752, 9, 14, DAY_OF_YEAR, 247 },
            { 2014, 5, 26, DAY_OF_WEEK, 1 },
            { 2014, 5, 26, DAY_OF_MONTH, 26 },
            { 2014, 5, 26, DAY_OF_YEAR, 146 },
            { 2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 4 },
            { 2014, 5, 26, YEAR, 2014 },
            { 2014, 5, 26, ERA, 1 },
            { 0, 6, 8, ERA, 0 },
            { 2014, 5, 26, WeekFields.ISO.dayOfWeek(), 1 },
        };
    }

    @ParameterizedTest(name = "{0}-{1}-{2}, getLong({3}) = {4}")
    @MethodSource("dateFieldGetProvider")
    void getLong_forField_returnsCorrectValue(int year, int month, int dayOfMonth, TemporalField field, long expected) {
        assertEquals(expected, BritishCutoverDate.of(year, month, dayOfMonth).getLong(field));
    }

    //-----------------------------------------------------------------------
    // with(TemporalField, long)
    //-----------------------------------------------------------------------
    static Object[][] dateWithFieldProvider() {
        return new Object[][] {
            // Adjustments around the cutover gap
            { 1752, 9, 2, DAY_OF_WEEK, 4, 1752, 9, 14 },
            { 1752, 9, 2, DAY_OF_MONTH, 3, 1752, 9, 14 }, // lenient
            { 1752, 9, 2, DAY_OF_YEAR, 247, 1752, 9, 14 }, // lenient
            { 1752, 9, 14, DAY_OF_WEEK, 3, 1752, 9, 2 },
            { 1752, 9, 14, DAY_OF_MONTH, 3, 1752, 9, 14 }, // lenient
            { 1752, 9, 14, DAY_OF_YEAR, 246, 1752, 9, 2 }, // lenient

            // Adjustments in modern dates
            { 2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 28 },
            { 2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31 },
            { 2014, 5, 26, YEAR, 2012, 2012, 5, 26 },
            { 2014, 5, 26, ERA, 0, -2013, 5, 26 },

            // Adjustments resulting in different day-of-month
            { 2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28 },
            { 2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29 },
            { 2012, 2, 29, YEAR, 2011, 2011, 2, 28 },
        };
    }

    @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) = {5}-{6}-{7}")
    @MethodSource("dateWithFieldProvider")
    void with_field_returnsAdjustedDate(int year, int month, int dayOfMonth, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDom) {
        BritishCutoverDate start = BritishCutoverDate.of(year, month, dayOfMonth);
        BritishCutoverDate expected = BritishCutoverDate.of(expectedYear, expectedMonth, expectedDom);
        assertEquals(expected, start.with(field, value));
    }

    //-----------------------------------------------------------------------
    // with(TemporalAdjuster)
    //-----------------------------------------------------------------------
    static Object[][] lastDayOfMonthProvider() {
        return new Object[][] {
            { BritishCutoverDate.of(1752, 2, 23), BritishCutoverDate.of(1752, 2, 29) },
            { BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 30) },
            { BritishCutoverDate.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 29) },
            { BritishCutoverDate.of(2012, 6, 23), BritishCutoverDate.of(2012, 6, 30) }
        };
    }

    @ParameterizedTest
    @MethodSource("lastDayOfMonthProvider")
    void with_lastDayOfMonthAdjuster_returnsLastDayOfMonth(BritishCutoverDate input, BritishCutoverDate expected) {
        assertEquals(expected, input.with(TemporalAdjusters.lastDayOfMonth()));
    }

    static Object[][] withLocalDateProvider() {
        return new Object[][] {
            { BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 12), BritishCutoverDate.of(1752, 9, 1) },
            { BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 12), BritishCutoverDate.of(1752, 9, 1) },
            { BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 14) },
            { BritishCutoverDate.of(2012, 2, 23), LocalDate.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 23) }
        };
    }

    @ParameterizedTest
    @MethodSource("withLocalDateProvider")
    void with_localDate_returnsCorrectDate(BritishCutoverDate input, LocalDate localDateToAdjustWith, BritishCutoverDate expected) {
        assertEquals(expected, input.with(localDateToAdjustWith));
    }

    //-----------------------------------------------------------------------
    // plus/minus(long, TemporalUnit)
    //-----------------------------------------------------------------------
    static Object[][] plusMinusProvider() {
        // The 'isBidirectional' flag indicates if minus is a perfect inverse of plus.
        // It is false for month/year additions where the day-of-month is adjusted.
        return new Object[][] {
            // Days
            { 1752, 9, 2, 1, DAYS, 1752, 9, 14, true },
            { 1752, 9, 14, -1, DAYS, 1752, 9, 2, true },
            // Weeks
            { 1752, 9, 2, 1, WEEKS, 1752, 9, 20, true },
            { 1752, 9, 14, -1, WEEKS, 1752, 8, 27, true },
            // Months
            { 1752, 9, 2, 1, MONTHS, 1752, 10, 2, true },
            { 1752, 8, 12, 1, MONTHS, 1752, 9, 23, false },
            // Years
            { 2014, 5, 26, 3, YEARS, 2017, 5, 26, true },
            // Decades, Centuries, Millennia, Eras
            { 2014, 5, 26, 3, DECADES, 2044, 5, 26, true },
            { 2014, 5, 26, 3, CENTURIES, 2314, 5, 26, true },
            { 2014, 5, 26, 3, MILLENNIA, 5014, 5, 26, true },
            { 2014, 5, 26, -1, ERAS, -2013, 5, 26, true },
        };
    }

    @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} = {5}-{6}-{7}")
    @MethodSource("plusMinusProvider")
    void plus_amountOfUnit_returnsCorrectlyAddedDate(
            int year, int month, int dayOfMonth,
            long amount, TemporalUnit unit,
            int expectedYear, int expectedMonth, int expectedDom, boolean isBidirectional) {
        BritishCutoverDate start = BritishCutoverDate.of(year, month, dayOfMonth);
        BritishCutoverDate expected = BritishCutoverDate.of(expectedYear, expectedMonth, expectedDom);
        assertEquals(expected, start.plus(amount, unit));
    }

    /**
     * Tests the {@code minus(long, TemporalUnit)} method.
     * <p>
     * This test reuses the {@link #plusMinusProvider()} data. The parameter order is
     * reversed to test the inverse operation (end.minus(amount) == start).
     * The {@code isBidirectional} flag ensures we only test cases where minus is a true inverse.
     */
    @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4} = {0}-{1}-{2}")
    @MethodSource("plusMinusProvider")
    void minus_amountOfUnit_returnsCorrectlySubtractedDate(
            int expectedYear, int expectedMonth, int expectedDom,
            long amount, TemporalUnit unit,
            int year, int month, int dayOfMonth, boolean isBidirectional) {
        if (isBidirectional) {
            BritishCutoverDate endDate = BritishCutoverDate.of(year, month, dayOfMonth);
            BritishCutoverDate expectedStartDate = BritishCutoverDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expectedStartDate, endDate.minus(amount, unit));
        }
    }

    //-----------------------------------------------------------------------
    // until(Temporal, TemporalUnit)
    //-----------------------------------------------------------------------
    static Object[][] untilUnitProvider() {
        return new Object[][] {
            { 1752, 9, 1, 1752, 9, 14, DAYS, 2 },
            { 1752, 9, 2, 1752, 9, 14, DAYS, 1 },
            { 2014, 5, 26, 2014, 6, 1, DAYS, 6 },
            { 1752, 9, 1, 1752, 9, 19, WEEKS, 1 },
            { 1752, 9, 2, 1752, 9, 14, WEEKS, 0 },
            { 1752, 9, 1, 1752, 10, 1, MONTHS, 1 },
            { 1752, 9, 2, 1752, 10, 1, MONTHS, 0 },
            { 2014, 5, 26, 2015, 5, 26, YEARS, 1 },
            { 2014, 5, 26, 2024, 5, 26, DECADES, 1 },
            { 2014, 5, 26, 2114, 5, 26, CENTURIES, 1 },
            { 2014, 5, 26, 3014, 5, 26, MILLENNIA, 1 },
            { -2013, 5, 26, 2014, 5, 26, ERAS, 1 },
        };
    }

    @ParameterizedTest(name = "until({0}-{1}-{2}, {3}-{4}-{5}, {6}) = {7}")
    @MethodSource("untilUnitProvider")
    void until_unit_calculatesCorrectAmountBetweenDates(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
        BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
        BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
        assertEquals(expected, start.until(end, unit));
    }

    //-----------------------------------------------------------------------
    // until(ChronoLocalDate)
    //-----------------------------------------------------------------------
    static Object[][] untilPeriodProvider() {
        return new Object[][] {
            // Start before cutover, end in or after cutover
            { 1752, 7, 2, 1752, 9, 1, 0, 1, 30 }, // 30 days after 1752-08-02
            { 1752, 7, 2, 1752, 9, 14, 0, 2, 1 }, // 1 day after 1752-09-02
            { 1752, 8, 16, 1752, 9, 16, 0, 1, 0 },
            // Start in cutover, end after
            { 1752, 9, 1, 1752, 10, 1, 0, 1, 0 },
            { 1752, 9, 2, 1752, 10, 2, 0, 1, 0 },
            // Start after cutover, end before (negative period)
            { 1752, 9, 14, 1752, 7, 14, 0, -2, 0 },
            { 1752, 9, 14, 1752, 9, 2, 0, 0, -1 },
            // Zero period
            { 1752, 7, 2, 1752, 7, 2, 0, 0, 0 },
        };
    }

    @ParameterizedTest(name = "until({0}-{1}-{2}, {3}-{4}-{5}) = P{6}Y{7}M{8}D")
    @MethodSource("untilPeriodProvider")
    void until_period_calculatesCorrectPeriodBetweenDates(int y1, int m1, int d1, int y2, int m2, int d2, int eY, int eM, int eD) {
        BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
        BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
        ChronoPeriod period = start.until(end);
        ChronoPeriod expectedPeriod = BritishCutoverChronology.INSTANCE.period(eY, eM, eD);
        assertEquals(expectedPeriod, period);
    }

    @ParameterizedTest(name = "start.plus(start.until(end)) == end")
    @MethodSource("untilPeriodProvider")
    void plus_period_isInverseOfUntil(int y1, int m1, int d1, int y2, int m2, int d2, int eY, int eM, int eD) {
        BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
        BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
        ChronoPeriod period = start.until(end);
        assertEquals(end, start.plus(period));
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    static Object[][] toStringProvider() {
        return new Object[][] {
            { BritishCutoverDate.of(1, 1, 1), "BritishCutover AD 1-01-01" },
            { BritishCutoverDate.of(2012, 6, 23), "BritishCutover AD 2012-06-23" }
        };
    }

    @ParameterizedTest
    @MethodSource("toStringProvider")
    void toString_returnsFormattedString(BritishCutoverDate cutoverDate, String expected) {
        assertEquals(expected, cutoverDate.toString());
    }

    //-----------------------------------------------------------------------
    // equals() and hashCode()
    //-----------------------------------------------------------------------
    @Test
    void equalsAndHashCode_shouldFollowContract() {
        new EqualsTester()
            .addEqualityGroup(BritishCutoverDate.of(2000, 1, 3), BritishCutoverDate.of(2000, 1, 3))
            .addEqualityGroup(BritishCutoverDate.of(2000, 1, 4))
            .addEqualityGroup(BritishCutoverDate.of(2000, 2, 3))
            .addEqualityGroup(BritishCutoverDate.of(2001, 1, 3))
            .testEquals();
    }
}