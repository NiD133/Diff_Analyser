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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Extensive tests for {@link BritishCutoverChronology} and {@link BritishCutoverDate}.
 * This class covers conversions, calculations, and edge cases around the cutover period.
 */
public class BritishCutoverChronologyTest {

    //-----------------------------------------------------------------------
    // data_samples
    //-----------------------------------------------------------------------
    static Stream<Arguments> sampleCutoverAndIsoDatesProvider() {
        return Stream.of(
            // BritishCutoverDate, Corresponding ISO LocalDate
            // Pre-cutover Julian dates
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(BritishCutoverDate.of(4, 2, 29), LocalDate.of(4, 2, 27)), // Julian leap year
            Arguments.of(BritishCutoverDate.of(100, 2, 29), LocalDate.of(100, 2, 27)), // Julian leap year, not Gregorian

            // Dates around the 1582 Gregorian cutover (not applicable in Britain)
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),

            // Dates around the 1752 British cutover
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)), // Last Julian day
            Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)), // Leniently accepts invalid date, maps to first Gregorian day
            Arguments.of(BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)), // Leniently accepts invalid date
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)), // First Gregorian day

            // Post-cutover Gregorian dates
            Arguments.of(BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12)),
            Arguments.of(BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6))
        );
    }

    @ParameterizedTest(name = "{index}: {0} -> {1}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    @DisplayName("Conversion from BritishCutoverDate to LocalDate")
    void testFromBritishCutoverDateToLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(isoDate, LocalDate.from(cutoverDate));
    }

    @ParameterizedTest(name = "{index}: {1} -> {0}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    @DisplayName("Conversion from LocalDate to BritishCutoverDate")
    void testFromLocalDateToBritishCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
    }

    @ParameterizedTest(name = "{index}: epochDay({1}) -> {0}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    @DisplayName("Creation from epoch day")
    void testDateEpochDay(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
    }

    @ParameterizedTest(name = "{index}: {0}.toEpochDay() -> {1}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    @DisplayName("Conversion to epoch day")
    void testToEpochDay(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
    }

    @ParameterizedTest(name = "{index}: {0} vs {1}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    @DisplayName("until() between equivalent dates should be zero")
    void testUntilWithEquivalentDateIsZero(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(cutoverDate));
        assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
        assertEquals(Period.ZERO, isoDate.until(cutoverDate));
    }

    @ParameterizedTest(name = "{index}: Chronology.date({1}) -> {0}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    @DisplayName("Creation from a TemporalAccessor (LocalDate)")
    void testChronologyDateFromTemporal(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    @DisplayName("plusDays() operation")
    void testPlusDays(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(isoDate, LocalDate.from(cutoverDate.plus(0, DAYS)));
        assertEquals(isoDate.plusDays(1), LocalDate.from(cutoverDate.plus(1, DAYS)));
        assertEquals(isoDate.plusDays(35), LocalDate.from(cutoverDate.plus(35, DAYS)));
        assertEquals(isoDate.plusDays(-1), LocalDate.from(cutoverDate.plus(-1, DAYS)));
        assertEquals(isoDate.plusDays(-60), LocalDate.from(cutoverDate.plus(-60, DAYS)));
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    @DisplayName("minusDays() operation")
    void testMinusDays(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(isoDate, LocalDate.from(cutoverDate.minus(0, DAYS)));
        assertEquals(isoDate.minusDays(1), LocalDate.from(cutoverDate.minus(1, DAYS)));
        assertEquals(isoDate.minusDays(35), LocalDate.from(cutoverDate.minus(35, DAYS)));
        assertEquals(isoDate.minusDays(-1), LocalDate.from(cutoverDate.minus(-1, DAYS)));
        assertEquals(isoDate.minusDays(-60), LocalDate.from(cutoverDate.minus(-60, DAYS)));
    }

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("sampleCutoverAndIsoDatesProvider")
    @DisplayName("until() in DAYS")
    void testUntilInDays(BritishCutoverDate cutoverDate, LocalDate isoDate) {
        assertEquals(0, cutoverDate.until(isoDate.plusDays(0), DAYS));
        assertEquals(1, cutoverDate.until(isoDate.plusDays(1), DAYS));
        assertEquals(35, cutoverDate.until(isoDate.plusDays(35), DAYS));
        assertEquals(-40, cutoverDate.until(isoDate.minusDays(40), DAYS));
    }

    //-----------------------------------------------------------------------
    // data_badDates
    //-----------------------------------------------------------------------
    static Stream<Arguments> invalidDateProvider() {
        return Stream.of(
            // Invalid month
            Arguments.of(1900, 0, 1), Arguments.of(1900, -1, 1), Arguments.of(1900, 13, 1),
            // Invalid day of month
            Arguments.of(1900, 1, 0), Arguments.of(1900, 1, -1), Arguments.of(1900, 1, 32),
            Arguments.of(1900, 2, 30), // Not a leap year
            Arguments.of(1899, 2, 29), // Not a leap year
            Arguments.of(1900, 4, 31)  // April has 30 days
        );
    }

    @ParameterizedTest(name = "of({0}, {1}, {2})")
    @MethodSource("invalidDateProvider")
    @DisplayName("of() should throw for invalid dates")
    void testOfWithInvalidDate(int year, int month, int dayOfMonth) {
        assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
    }

    //-----------------------------------------------------------------------
    // data_lengthOfMonth
    //-----------------------------------------------------------------------
    static Stream<Arguments> lengthOfMonthProvider() {
        return Stream.of(
            // Julian leap year, not Gregorian
            Arguments.of(1700, 1, 31), Arguments.of(1700, 2, 29), Arguments.of(1700, 3, 31),
            // Pre-cutover year
            Arguments.of(1751, 1, 31), Arguments.of(1751, 2, 28), Arguments.of(1751, 12, 31),
            // Cutover year 1752
            Arguments.of(1752, 1, 31), Arguments.of(1752, 2, 29), // Leap day
            Arguments.of(1752, 8, 31),
            Arguments.of(1752, 9, 19), // September 1752 had 19 days (30 - 11 skipped)
            Arguments.of(1752, 10, 31),
            // Post-cutover year
            Arguments.of(1753, 1, 31), Arguments.of(1753, 2, 28), Arguments.of(1753, 12, 31),
            // Gregorian non-leap century
            Arguments.of(1800, 2, 28), Arguments.of(1900, 2, 28),
            // Gregorian leap century
            Arguments.of(2000, 2, 29)
        );
    }

    @ParameterizedTest(name = "{0}-{1} should have {2} days")
    @MethodSource("lengthOfMonthProvider")
    @DisplayName("lengthOfMonth()")
    void testLengthOfMonth(int year, int month, int expectedLength) {
        assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
    }

    //-----------------------------------------------------------------------
    // data_lengthOfYear
    //-----------------------------------------------------------------------
    static Stream<Arguments> lengthOfYearProvider() {
        return Stream.of(
            Arguments.of(1700, 366), // Julian leap year
            Arguments.of(1751, 365), // Normal year before cutover
            Arguments.of(1752, 355), // Cutover year (366 - 11 skipped days)
            Arguments.of(1753, 365), // Normal year after cutover
            Arguments.of(1800, 365), // Gregorian non-leap
            Arguments.of(1900, 365), // Gregorian non-leap
            Arguments.of(2000, 366), // Gregorian leap
            Arguments.of(2004, 366)  // Gregorian leap
        );
    }

    @ParameterizedTest(name = "Year {0} should have {1} days")
    @MethodSource("lengthOfYearProvider")
    @DisplayName("lengthOfYear()")
    void testLengthOfYear(int year, int expectedLength) {
        assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear(), "from start of year");
        assertEquals(expectedLength, BritishCutoverDate.of(year, 12, 31).lengthOfYear(), "from end of year");
    }

    //-----------------------------------------------------------------------
    // data_ranges
    //-----------------------------------------------------------------------
    static Stream<Arguments> fieldRangeProvider() {
        return Stream.of(
            // DAY_OF_MONTH
            Arguments.of(1700, 2, 23, DAY_OF_MONTH, 1, 29), // Julian leap year
            Arguments.of(1751, 2, 23, DAY_OF_MONTH, 1, 28), // Normal year
            Arguments.of(1752, 9, 23, DAY_OF_MONTH, 1, 30), // Cutover month (range is logical, not physical length)
            Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29), // Gregorian leap year

            // DAY_OF_YEAR
            Arguments.of(1700, 1, 23, DAY_OF_YEAR, 1, 366), // Julian leap year
            Arguments.of(1752, 1, 23, DAY_OF_YEAR, 1, 355), // Cutover year
            Arguments.of(2011, 2, 23, DAY_OF_YEAR, 1, 365), // Gregorian normal year

            // ALIGNED_WEEK_OF_MONTH
            Arguments.of(1752, 9, 23, ALIGNED_WEEK_OF_MONTH, 1, 3), // Cutover month
            Arguments.of(2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4), // Normal month
            Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, 1, 5), // Normal month

            // ALIGNED_WEEK_OF_YEAR
            Arguments.of(1752, 12, 23, ALIGNED_WEEK_OF_YEAR, 1, 51), // Cutover year
            Arguments.of(2011, 2, 23, ALIGNED_WEEK_OF_YEAR, 1, 53)  // Normal year
        );
    }

    @ParameterizedTest(name = "{0}-{1}-{2}, {3}: {4}-{5}")
    @MethodSource("fieldRangeProvider")
    @DisplayName("range() for various temporal fields")
    void testRange(int year, int month, int day, TemporalField field, int expectedMin, int expectedMax) {
        ValueRange expectedRange = ValueRange.of(expectedMin, expectedMax);
        assertEquals(expectedRange, BritishCutoverDate.of(year, month, day).range(field));
    }

    //-----------------------------------------------------------------------
    // data_getLong
    //-----------------------------------------------------------------------
    static Stream<Arguments> fieldValueProvider() {
        return Stream.of(
            // Date before cutover gap: 1752-09-02
            Arguments.of(1752, 9, 2, DAY_OF_WEEK, 3), // Wednesday
            Arguments.of(1752, 9, 2, DAY_OF_MONTH, 2),
            Arguments.of(1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 2), // 246
            Arguments.of(1752, 9, 2, MONTH_OF_YEAR, 9),

            // Date after cutover gap: 1752-09-14
            Arguments.of(1752, 9, 14, DAY_OF_WEEK, 4), // Thursday
            Arguments.of(1752, 9, 14, DAY_OF_MONTH, 14),
            Arguments.of(1752, 9, 14, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3), // 247 (11 days skipped)
            Arguments.of(1752, 9, 14, MONTH_OF_YEAR, 9),

            // Modern date: 2014-05-26
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1), // Monday
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26), // 146
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
            Arguments.of(2014, 5, 26, YEAR, 2014),
            Arguments.of(2014, 5, 26, ERA, 1), // AD

            // BC Era
            Arguments.of(0, 6, 8, ERA, 0), // BC

            // Other fields
            Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 1)
        );
    }

    @ParameterizedTest(name = "{0}-{1}-{2}, getLong({3}) -> {4}")
    @MethodSource("fieldValueProvider")
    @DisplayName("getLong() for various temporal fields")
    void testGetLong(int year, int month, int day, TemporalField field, long expected) {
        assertEquals(expected, BritishCutoverDate.of(year, month, day).getLong(field));
    }

    //-----------------------------------------------------------------------
    // data_with
    //-----------------------------------------------------------------------
    static Stream<Arguments> withFieldValueProvider() {
        return Stream.of(
            // --- Adjustments around the cutover ---
            // Start date: 1752-09-02 (last Julian day)
            Arguments.of(1752, 9, 2, DAY_OF_WEEK, 4, 1752, 9, 14), // Set to Thursday, jumps over gap
            Arguments.of(1752, 9, 2, DAY_OF_MONTH, 14, 1752, 9, 14), // Set to 14th, becomes Gregorian
            Arguments.of(1752, 9, 2, DAY_OF_YEAR, 247, 1752, 9, 14), // Day 247 is 1752-09-14

            // Start date: 1752-09-14 (first Gregorian day)
            Arguments.of(1752, 9, 14, DAY_OF_WEEK, 3, 1752, 9, 2), // Set to Wednesday, jumps back
            Arguments.of(1752, 9, 14, DAY_OF_MONTH, 2, 1752, 9, 2), // Set to 2nd, becomes Julian

            // --- Standard adjustments ---
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 28), // Monday to Wednesday
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26),
            Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
            Arguments.of(2014, 5, 26, ERA, 0, -2013, 5, 26), // AD to BC

            // --- Adjustments resulting in different day of month ---
            Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28), // March 31 -> Feb 28
            Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29), // March 31 -> Feb 29 (leap)
            Arguments.of(2012, 2, 29, YEAR, 2011, 2011, 2, 28)       // Feb 29 -> Feb 28
        );
    }

    @ParameterizedTest(name = "({0}-{1}-{2}).with({3}, {4}) -> {5}-{6}-{7}")
    @MethodSource("withFieldValueProvider")
    @DisplayName("with() a new field value")
    void testWithFieldValue(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
        BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
        BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
        assertEquals(expected, start.with(field, value));
    }

    //-----------------------------------------------------------------------
    // data_lastDayOfMonth
    //-----------------------------------------------------------------------
    static Stream<Arguments> lastDayOfMonthProvider() {
        return Stream.of(
            Arguments.of(BritishCutoverDate.of(1752, 2, 23), BritishCutoverDate.of(1752, 2, 29)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 30)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 30)),
            Arguments.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 29))
        );
    }

    @ParameterizedTest
    @MethodSource("lastDayOfMonthProvider")
    @DisplayName("with(TemporalAdjusters.lastDayOfMonth())")
    void testAdjustToLastDayOfMonth(BritishCutoverDate input, BritishCutoverDate expected) {
        assertEquals(expected, input.with(TemporalAdjusters.lastDayOfMonth()));
    }

    //-----------------------------------------------------------------------
    // data_plus
    //-----------------------------------------------------------------------
    static Stream<Arguments> plusMinusProvider() {
        return Stream.of(
            // --- DAYS ---
            Arguments.of(1752, 9, 2, 1, DAYS, 1752, 9, 14, true), // Crosses the gap forward
            Arguments.of(1752, 9, 14, -1, DAYS, 1752, 9, 2, true), // Crosses the gap backward
            Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3, true),

            // --- WEEKS ---
            Arguments.of(1752, 9, 2, 1, WEEKS, 1752, 9, 20, true),
            Arguments.of(1752, 9, 14, -1, WEEKS, 1752, 8, 27, true),

            // --- MONTHS ---
            Arguments.of(1752, 8, 12, 1, MONTHS, 1752, 9, 23, false), // Lands in the gap, resolves
            Arguments.of(1752, 10, 12, -1, MONTHS, 1752, 9, 23, false), // Lands in the gap, resolves
            Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26, true),

            // --- YEARS and larger ---
            Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26, true),
            Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26, true),
            Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26, true),
            Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26, true),
            Arguments.of(2014, 5, 26, -1, ERAS, -2013, 5, 26, true)
        );
    }

    @ParameterizedTest(name = "({0}-{1}-{2}).plus({3}, {4}) -> {5}-{6}-{7}")
    @MethodSource("plusMinusProvider")
    @DisplayName("plus() and minus() with TemporalUnit")
    void testPlusAndMinusTemporalUnit(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed, boolean isBidirectional) {
        BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
        BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
        assertEquals(expected, start.plus(amount, unit), "plus() operation failed");

        if (isBidirectional) {
            assertEquals(start, expected.minus(amount, unit), "minus() operation failed");
        }
    }

    //-----------------------------------------------------------------------
    // data_until
    //-----------------------------------------------------------------------
    static Stream<Arguments> untilTemporalUnitProvider() {
        return Stream.of(
            // --- DAYS ---
            Arguments.of(1752, 9, 1, 1752, 9, 2, DAYS, 1),
            Arguments.of(1752, 9, 2, 1752, 9, 14, DAYS, 1), // Across the gap
            Arguments.of(1752, 9, 14, 1752, 9, 2, DAYS, -1), // Across the gap backward

            // --- WEEKS ---
            Arguments.of(1752, 9, 2, 1752, 9, 20, WEEKS, 1),
            Arguments.of(2014, 5, 26, 2014, 6, 2, WEEKS, 1),

            // --- MONTHS ---
            Arguments.of(1752, 9, 2, 1752, 10, 2, MONTHS, 1),
            Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1),

            // --- YEARS and larger ---
            Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1),
            Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1),
            Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1),
            Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1),
            Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1)
        );
    }

    @ParameterizedTest(name = "({0}-{1}-{2}).until({3}-{4}-{5}, {6}) -> {7}")
    @MethodSource("untilTemporalUnitProvider")
    @DisplayName("until() with TemporalUnit")
    void testUntilTemporalUnit(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
        BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
        BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
        assertEquals(expected, start.until(end, unit));
    }

    //-----------------------------------------------------------------------
    // data_untilCLD (ChronoPeriod)
    //-----------------------------------------------------------------------
    static Stream<Arguments> untilPeriodProvider() {
        return Stream.of(
            // Start Date, End Date, Expected Period (Y, M, D)
            Arguments.of(1752, 7, 2, 1752, 7, 1, 0, 0, -1),
            Arguments.of(1752, 7, 2, 1752, 7, 2, 0, 0, 0),

            // --- Spanning the cutover ---
            // From 1752-07-02 to 1752-09-14 (2 months and 1 day, accounting for gap)
            Arguments.of(1752, 7, 2, 1752, 9, 14, 0, 2, 1),
            // From 1752-08-16 to 1752-09-16 (1 month, 0 days)
            Arguments.of(1752, 8, 16, 1752, 9, 16, 0, 1, 0),
            // From 1752-09-01 to 1752-09-14 (2 days apart on timeline)
            Arguments.of(1752, 9, 1, 1752, 9, 14, 0, 0, 2),
            // From 1752-09-02 to 1752-09-14 (1 day apart on timeline)
            Arguments.of(1752, 9, 2, 1752, 9, 14, 0, 0, 1),
            // From 1752-09-14 back to 1752-09-02 (-1 day on timeline)
            Arguments.of(1752, 9, 14, 1752, 9, 2, 0, 0, -1),
            // From 1752-10-03 back to 1752-09-02 (-1 month, -1 day)
            Arguments.of(1752, 10, 3, 1752, 9, 2, 0, -1, -1)
        );
    }

    @ParameterizedTest(name = "({0}-{1}-{2}).until({3}-{4}-{5}) -> P{6}Y{7}M{8}D")
    @MethodSource("untilPeriodProvider")
    @DisplayName("until() returning a ChronoPeriod")
    void testUntilPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
        BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
        BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
        ChronoPeriod expectedPeriod = BritishCutoverChronology.INSTANCE.period(ey, em, ed);

        assertEquals(expectedPeriod, start.until(end));
    }

    @ParameterizedTest(name = "({0}-{1}-{2}).plus(until({3}-{4}-{5})) should be reversible")
    @MethodSource("untilPeriodProvider")
    @DisplayName("until() and plus() should be reversible")
    void testUntilAndPlusIsReversible(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
        BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
        BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
        ChronoPeriod period = start.until(end);
        assertEquals(end, start.plus(period));
    }

    //-----------------------------------------------------------------------
    // toString()
    //-----------------------------------------------------------------------
    static Stream<Arguments> toStringProvider() {
        return Stream.of(
            Arguments.of(BritishCutoverDate.of(1, 1, 1), "BritishCutover AD 1-01-01"),
            Arguments.of(BritishCutoverDate.of(2012, 6, 23), "BritishCutover AD 2012-06-23")
        );
    }

    @ParameterizedTest
    @MethodSource("toStringProvider")
    @DisplayName("toString() format")
    void testToString(BritishCutoverDate cutoverDate, String expected) {
        assertEquals(expected, cutoverDate.toString());
    }

    //-----------------------------------------------------------------------
    @Test
    @DisplayName("Chronology.getCutover() should return the correct date")
    void testGetCutoverDate() {
        assertEquals(LocalDate.of(1752, 9, 14), BritishCutoverChronology.INSTANCE.getCutover());
    }
}