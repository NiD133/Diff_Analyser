package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.ERA;
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;
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
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link BritishCutoverDate}.
 * This class focuses on the behavior of dates in the British Cutover calendar system,
 * including conversions, arithmetic, and handling of the 1752 cutover.
 */
public class BritishCutoverDateTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample BritishCutoverDates and their equivalent ISO LocalDates.
     * Format: {BritishCutoverDate, corresponding ISO LocalDate}
     */
    public static Object[][] sampleDatesProvider() {
        return new Object[][]{
                {BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
                {BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)},
                {BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
                {BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
                {BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)},
                // Dates around the 1752 cutover
                {BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)}, // last Julian date
                {BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)}, // leniently accept invalid
                {BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)}, // leniently accept invalid
                {BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)}, // first Gregorian date
                // Modern dates
                {BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12)},
                {BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6)},
        };
    }

    /**
     * Provides invalid date components for testing exceptions.
     * Format: {year, month, dayOfMonth}
     */
    public static Object[][] invalidDateProvider() {
        return new Object[][]{
                {1900, 0, 0}, {1900, -1, 1}, {1900, 13, 1}, {1900, 1, -1},
                {1900, 1, 32}, {1900, 2, 30}, {1899, 2, 29}, {1900, 4, 31},
        };
    }

    /**
     * Provides month lengths for various dates.
     * Format: {year, month, expectedLength}
     */
    public static Object[][] monthLengthProvider() {
        return new Object[][]{
                {1700, 2, 29}, // Julian leap year
                {1751, 2, 28},
                {1752, 2, 29}, // Gregorian leap year
                {1752, 9, 19}, // Cutover month
                {1753, 2, 28},
                {1800, 2, 28}, // Not a leap year in Gregorian
                {1900, 2, 28}, // Not a leap year in Gregorian
                {2000, 2, 29}, // Leap year in Gregorian
        };
    }

    /**
     * Provides year lengths for various years.
     * Format: {year, expectedLength}
     */
    public static Object[][] yearLengthProvider() {
        return new Object[][]{
                {1700, 366}, // Julian leap year
                {1751, 365},
                {1752, 355}, // Cutover year, 11 days removed
                {1753, 365},
                {1800, 365}, // Not a leap year in Gregorian
                {1900, 365}, // Not a leap year in Gregorian
                {2000, 366}, // Leap year in Gregorian
        };
    }

    /**
     * Provides field ranges for various dates.
     * Format: {year, month, day, field, expectedMin, expectedMax}
     */
    public static Object[][] fieldRangeProvider() {
        return new Object[][]{
                {1752, 9, 23, DAY_OF_MONTH, 1, 30},
                {1752, 9, 23, DAY_OF_YEAR, 1, 355},
                {1752, 9, 23, ALIGNED_WEEK_OF_MONTH, 1, 3},
                {1752, 12, 23, ALIGNED_WEEK_OF_YEAR, 1, 51},
                {2011, 2, 23, DAY_OF_MONTH, 1, 28},
                {2012, 2, 23, DAY_OF_MONTH, 1, 29},
                {2012, 2, 23, DAY_OF_YEAR, 1, 366},
        };
    }

    /**
     * Provides expected values for temporal fields.
     * Format: {year, month, day, field, expectedValue}
     */
    public static Object[][] fieldValueProvider() {
        return new Object[][]{
                {1752, 9, 2, DAY_OF_WEEK, 3}, // Wednesday
                {1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 2},
                {1752, 9, 14, DAY_OF_WEEK, 4}, // Thursday
                {1752, 9, 14, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3},
                {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
                {2014, 5, 26, YEAR, 2014},
                {2014, 5, 26, ERA, 1},
                {0, 6, 8, ERA, 0},
        };
    }

    /**
     * Provides data for testing `with(TemporalField, long)`.
     * Format: {startYear, startMonth, startDay, field, value, endYear, endMonth, endDay}
     */
    public static Object[][] withFieldAdjustmentsProvider() {
        return new Object[][]{
                // Adjustments around the cutover
                {1752, 9, 2, DAY_OF_WEEK, 4, 1752, 9, 14},
                {1752, 9, 2, DAY_OF_MONTH, 3, 1752, 9, 14}, // lenient
                {1752, 9, 14, DAY_OF_WEEK, 3, 1752, 9, 2},
                {1752, 9, 14, DAY_OF_MONTH, 2, 1752, 9, 2}, // lenient
                // Standard adjustments
                {2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 28},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                {2012, 2, 29, YEAR, 2011, 2011, 2, 28}, // Adjust leap day
        };
    }

    /**
     * Provides data for testing `plus(long, TemporalUnit)`.
     * Format: {startYear, startMonth, startDay, amount, unit, endYear, endMonth, endDay, isBidirectional}
     */
    public static Object[][] plusMinusProvider() {
        return new Object[][]{
                // Around cutover
                {1752, 9, 2, 1, DAYS, 1752, 9, 14, true},
                {1752, 9, 14, -1, DAYS, 1752, 9, 2, true},
                {1752, 8, 12, 1, MONTHS, 1752, 9, 23, false}, // Not bidirectional
                // Standard
                {2014, 5, 26, 8, DAYS, 2014, 6, 3, true},
                {2014, 5, 26, 3, WEEKS, 2014, 6, 16, true},
                {2014, 5, 26, 3, MONTHS, 2014, 8, 26, true},
                {2014, 5, 26, 3, YEARS, 2017, 5, 26, true},
        };
    }

    /**
     * Provides data for testing `until(end, unit)`.
     * Format: {y1, m1, d1, y2, m2, d2, unit, expectedAmount}
     */
    public static Object[][] untilTemporalUnitProvider() {
        return new Object[][]{
                {1752, 9, 1, 1752, 9, 14, DAYS, 2},
                {1752, 9, 2, 1752, 9, 14, DAYS, 1},
                {1752, 9, 1, 1752, 9, 19, WEEKS, 1},
                {1752, 9, 1, 1752, 10, 1, MONTHS, 1},
                {2014, 5, 26, 2015, 5, 26, YEARS, 1},
        };
    }

    /**
     * Provides data for testing `until(end)`.
     * Format: {y1, m1, d1, y2, m2, d2, expectedYears, expectedMonths, expectedDays}
     */
    public static Object[][] untilPeriodProvider() {
        return new Object[][]{
                // 1 day after 1752-09-02 is 1752-09-14
                {1752, 7, 2, 1752, 9, 14, 0, 2, 1},
                // 2 months after 1752-07-02 is 1752-09-02
                {1752, 7, 2, 1752, 9, 2, 0, 2, 0},
                {1752, 9, 14, 1752, 7, 14, 0, -2, 0},
        };
    }

    //-----------------------------------------------------------------------
    // Conversion Tests
    //-----------------------------------------------------------------------

    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("sampleDatesProvider")
    @DisplayName("Conversion from BritishCutoverDate to LocalDate")
    void fromBritishCutoverDate_shouldConvertToCorrectLocalDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(iso, LocalDate.from(cutover));
    }

    @ParameterizedTest(name = "{1} -> {0}")
    @MethodSource("sampleDatesProvider")
    @DisplayName("Conversion from LocalDate to BritishCutoverDate")
    void fromLocalDate_shouldConvertToCorrectBritishCutoverDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(cutover, BritishCutoverDate.from(iso));
    }

    @ParameterizedTest(name = "EpochDay of {1} -> {0}")
    @MethodSource("sampleDatesProvider")
    @DisplayName("Creation from epoch day")
    void fromEpochDay_shouldCreateCorrectDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(cutover, BritishCutoverChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
    }

    @ParameterizedTest(name = "toEpochDay of {0} -> {1}")
    @MethodSource("sampleDatesProvider")
    @DisplayName("Conversion to epoch day")
    void toEpochDay_shouldReturnCorrectValue(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(iso.toEpochDay(), cutover.toEpochDay());
    }

    @ParameterizedTest(name = "From temporal {1} -> {0}")
    @MethodSource("sampleDatesProvider")
    @DisplayName("Creation from a temporal accessor")
    void fromTemporal_shouldCreateCorrectDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(cutover, BritishCutoverChronology.INSTANCE.date(iso));
    }

    //-----------------------------------------------------------------------
    // Date Creation and Validation
    //-----------------------------------------------------------------------

    @ParameterizedTest(name = "of({0}, {1}, {2})")
    @MethodSource("invalidDateProvider")
    @DisplayName("of() should throw for invalid date components")
    void of_withInvalidDate_shouldThrowException(int year, int month, int day) {
        assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, day));
    }

    @Test
    void range_forUnsupportedField_shouldThrowException() {
        BritishCutoverDate date = BritishCutoverDate.of(2012, 6, 30);
        assertThrows(UnsupportedTemporalTypeException.class, () -> date.range(MINUTE_OF_DAY));
    }

    //-----------------------------------------------------------------------
    // Date Properties
    //-----------------------------------------------------------------------

    @ParameterizedTest(name = "Year {0}, Month {1} should have {2} days")
    @MethodSource("monthLengthProvider")
    void lengthOfMonth_shouldReturnCorrectValue(int year, int month, int length) {
        assertEquals(length, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
    }

    @ParameterizedTest(name = "Year {0} should have {1} days")
    @MethodSource("yearLengthProvider")
    void lengthOfYear_shouldBeConsistentForTheWholeYear(int year, int length) {
        assertEquals(length, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
        assertEquals(length, BritishCutoverDate.of(year, 12, 31).lengthOfYear());
    }

    @ParameterizedTest(name = "range of {3} for {0}-{1}-{2} is {4}-{5}")
    @MethodSource("fieldRangeProvider")
    void range_forField_shouldReturnCorrectRange(int y, int m, int d, TemporalField field, int min, int max) {
        assertEquals(ValueRange.of(min, max), BritishCutoverDate.of(y, m, d).range(field));
    }

    @ParameterizedTest(name = "getLong({3}) for {0}-{1}-{2} is {4}")
    @MethodSource("fieldValueProvider")
    void getLong_forField_shouldReturnCorrectValue(int y, int m, int d, TemporalField field, long expected) {
        assertEquals(expected, BritishCutoverDate.of(y, m, d).getLong(field));
    }

    //-----------------------------------------------------------------------
    // Adjustments
    //-----------------------------------------------------------------------

    @ParameterizedTest(name = "with({3}, {4})")
    @MethodSource("withFieldAdjustmentsProvider")
    void with_fieldAndValue_shouldReturnAdjustedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
        BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
        BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
        assertEquals(expected, start.with(field, value));
    }

    @Test
    void with_lastDayOfMonthAdjuster_shouldReturnLastDayOfMonth() {
        BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
        BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 30);
        assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
    }

    //-----------------------------------------------------------------------
    // Arithmetic: plus/minus
    //-----------------------------------------------------------------------

    @ParameterizedTest(name = "plus({3}, {4})")
    @MethodSource("plusMinusProvider")
    void plus_amountOfUnit_shouldReturnAdjustedDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed, boolean bidi) {
        BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
        BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
        assertEquals(expected, start.plus(amount, unit));
    }

    @ParameterizedTest(name = "minus({3}, {4})")
    @MethodSource("plusMinusProvider")
    void minus_amountOfUnit_shouldReturnAdjustedDate(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d, boolean bidi) {
        if (bidi) {
            BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
            BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }
    }

    //-----------------------------------------------------------------------
    // Arithmetic: until
    //-----------------------------------------------------------------------

    @Test
    void until_sameDateInstance_returnsZeroPeriod() {
        BritishCutoverDate date = BritishCutoverDate.of(2012, 7, 5);
        assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), date.until(date));
    }

    @Test
    void until_equivalentLocalDate_returnsZeroPeriod() {
        BritishCutoverDate cutoverDate = BritishCutoverDate.of(2012, 7, 5);
        LocalDate isoDate = LocalDate.of(2012, 7, 5);
        assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
    }

    @Test
    void until_fromEquivalentLocalDate_returnsZeroPeriod() {
        BritishCutoverDate cutoverDate = BritishCutoverDate.of(2012, 7, 5);
        LocalDate isoDate = LocalDate.of(2012, 7, 5);
        assertEquals(Period.ZERO, isoDate.until(cutoverDate));
    }

    @ParameterizedTest(name = "until({3}-{4}-{5}, {6}) is {7}")
    @MethodSource("untilTemporalUnitProvider")
    void until_endDateAndUnit_shouldReturnCorrectDuration(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
        BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
        BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
        assertEquals(expected, start.until(end, unit));
    }

    @ParameterizedTest(name = "until({3}-{4}-{5}) is P{6}Y{7}M{8}D")
    @MethodSource("untilPeriodProvider")
    void until_endDate_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
        BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
        BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
        ChronoPeriod expected = BritishCutoverChronology.INSTANCE.period(ey, em, ed);
        assertEquals(expected, start.until(end));
    }

    @ParameterizedTest(name = "start.plus(start.until(end)) == end")
    @MethodSource("untilPeriodProvider")
    void plus_periodFromUntil_shouldYieldEndDate(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
        BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
        BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
        ChronoPeriod period = start.until(end);
        assertEquals(end, start.plus(period));
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------

    @Test
    void toString_shouldReturnStandardRepresentation() {
        BritishCutoverDate date = BritishCutoverDate.of(2012, 6, 23);
        assertEquals("BritishCutover AD 2012-06-23", date.toString());
    }
}