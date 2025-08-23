package org.threeten.extra.chrono;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;

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

/**
 * Comprehensive tests for the {@link BritishCutoverDate} class.
 * This suite covers conversions, date manipulation, field access, and edge cases
 * related to the Julian-Gregorian calendar transition in 1752.
 */
@DisplayName("BritishCutoverDate")
class BritishCutoverDateTest {

    // A constant for the special year of the calendar transition.
    private static final int CUTOVER_YEAR = 1752;

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleCutoverAndIsoDates() {
        return Stream.of(
            // --- Pre-cutover (Julian calendar rules) ---
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(BritishCutoverDate.of(4, 2, 29), LocalDate.of(4, 2, 27)), // Julian leap year
            Arguments.of(BritishCutoverDate.of(100, 2, 29), LocalDate.of(100, 2, 27)), // Julian leap century

            // --- Dates around the 1752 cutover ---
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(BritishCutoverDate.of(1751, 12, 31), LocalDate.of(1752, 1, 11)),
            Arguments.of(BritishCutoverDate.of(CUTOVER_YEAR, 9, 2), LocalDate.of(CUTOVER_YEAR, 9, 13)), // Last day before the gap
            Arguments.of(BritishCutoverDate.of(CUTOVER_YEAR, 9, 3), LocalDate.of(CUTOVER_YEAR, 9, 14)), // Leniently handles invalid date in the gap
            Arguments.of(BritishCutoverDate.of(CUTOVER_YEAR, 9, 13), LocalDate.of(CUTOVER_YEAR, 9, 24)), // Leniently handles invalid date in the gap
            Arguments.of(BritishCutoverDate.of(CUTOVER_YEAR, 9, 14), LocalDate.of(CUTOVER_YEAR, 9, 14)), // First day after the gap

            // --- Post-cutover (Gregorian calendar rules) ---
            Arguments.of(BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12)),
            Arguments.of(BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6))
        );
    }

    static Stream<Arguments> invalidDateComponents() {
        return Stream.of(
            Arguments.of(1900, 0, 1),   // Invalid month
            Arguments.of(1900, 13, 1),  // Invalid month
            Arguments.of(1900, 1, 0),   // Invalid day
            Arguments.of(1900, 1, 32),  // Invalid day
            Arguments.of(1900, 2, 30),  // Invalid day for Feb
            Arguments.of(1899, 2, 29)   // Invalid day for non-leap Feb
        );
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion and Factory Methods")
    class ConversionTests {

        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        @DisplayName("LocalDate.from(cutoverDate) should return the equivalent ISO date")
        void from_convertsBritishCutoverDateToEquivalentLocalDate(BritishCutoverDate cutoverDate, LocalDate expectedIsoDate) {
            assertEquals(expectedIsoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest(name = "{1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        @DisplayName("BritishCutoverDate.from(isoDate) should return the equivalent Cutover date")
        void from_convertsIsoDateToEquivalentBritishCutoverDate(BritishCutoverDate expectedCutoverDate, LocalDate isoDate) {
            assertEquals(expectedCutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest(name = "epochDay({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        @DisplayName("toEpochDay() should match ISO date's epoch day")
        void toEpochDay_shouldMatchIsoEpochDay(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest(name = "dateEpochDay({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        @DisplayName("chronology.dateEpochDay() should create the correct date")
        void chronology_dateEpochDay(BritishCutoverDate expectedCutoverDate, LocalDate isoDate) {
            assertEquals(expectedCutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest(name = "date({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        @DisplayName("chronology.date(temporal) should create the correct date from an ISO date")
        void chronology_dateFromTemporal(BritishCutoverDate expectedCutoverDate, LocalDate isoDate) {
            assertEquals(expectedCutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Validation")
    class DateValidationTests {

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#invalidDateComponents")
        @DisplayName("of(year, month, day) should throw exception for invalid dates")
        void of_throwsExceptionForInvalidDate(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Calendar Properties")
    class CalendarPropertiesTests {

        static Stream<Arguments> monthLengths() {
            return Stream.of(
                // Julian leap century (1700 is a leap year in Julian)
                Arguments.of(1700, Month.FEBRUARY, 29),
                // Gregorian non-leap century (1800 is not a leap year)
                Arguments.of(1800, Month.FEBRUARY, 28),
                // Gregorian leap century (2000 is a leap year)
                Arguments.of(2000, Month.FEBRUARY, 29),
                // Cutover year: September has 19 days (2nd is followed by 14th)
                Arguments.of(CUTOVER_YEAR, Month.SEPTEMBER, 19),
                Arguments.of(CUTOVER_YEAR, Month.FEBRUARY, 29), // Leap year
                // Standard cases
                Arguments.of(2011, Month.FEBRUARY, 28),
                Arguments.of(2012, Month.APRIL, 30),
                Arguments.of(2013, Month.JANUARY, 31)
            );
        }

        @ParameterizedTest(name = "{0} {1} should have {2} days")
        @MethodSource("monthLengths")
        @DisplayName("lengthOfMonth() should return correct number of days")
        void lengthOfMonth_isCorrect(int year, Month month, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month.getValue(), 1).lengthOfMonth());
        }

        static Stream<Arguments> yearLengths() {
            return Stream.of(
                Arguments.of(1700, 366), // Julian leap year
                Arguments.of(1800, 365), // Gregorian non-leap year
                Arguments.of(1900, 365), // Gregorian non-leap year
                Arguments.of(2000, 366), // Gregorian leap year
                Arguments.of(CUTOVER_YEAR, 355) // The cutover year is 11 days shorter
            );
        }

        @ParameterizedTest(name = "Year {0} should have {1} days")
        @MethodSource("yearLengths")
        @DisplayName("lengthOfYear() should return correct number of days")
        void lengthOfYear_isCorrect(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
        }

        @Test
        @DisplayName("isLeapYear() should correctly identify leap years across the cutover")
        void isLeapYear_handlesJulianAndGregorianRules() {
            assertEquals(true, BritishCutoverChronology.INSTANCE.isLeapYear(4));    // Julian leap
            assertEquals(true, BritishCutoverChronology.INSTANCE.isLeapYear(1600)); // Julian leap
            assertEquals(true, BritishCutoverChronology.INSTANCE.isLeapYear(1700)); // Julian leap
            assertEquals(true, BritishCutoverChronology.INSTANCE.isLeapYear(CUTOVER_YEAR)); // Julian leap
            assertEquals(false, BritishCutoverChronology.INSTANCE.isLeapYear(1800)); // Gregorian non-leap
            assertEquals(false, BritishCutoverChronology.INSTANCE.isLeapYear(1900)); // Gregorian non-leap
            assertEquals(true, BritishCutoverChronology.INSTANCE.isLeapYear(2000));  // Gregorian leap
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Manipulation")
    class ManipulationTests {

        static Stream<Arguments> plusAndMinusCases() {
            // Parameters: startYear, startMonth, startDay, amount, unit, expectedYear, expectedMonth, expectedDay, isReversible
            // isReversible: true if minus(amount) from the result gets back to the start. False for ambiguous cases around the cutover.
            return Stream.of(
                // --- Days ---
                Arguments.of(CUTOVER_YEAR, 9, 2, 1L, DAYS, CUTOVER_YEAR, 9, 14, true), // Crosses the gap forward
                Arguments.of(CUTOVER_YEAR, 9, 14, -1L, DAYS, CUTOVER_YEAR, 9, 2, true), // Crosses the gap backward
                Arguments.of(2014, 5, 26, 8L, DAYS, 2014, 6, 3, true),

                // --- Weeks ---
                Arguments.of(CUTOVER_YEAR, 9, 2, 1L, WEEKS, CUTOVER_YEAR, 9, 20, true),
                Arguments.of(CUTOVER_YEAR, 9, 14, -1L, WEEKS, CUTOVER_YEAR, 8, 27, true),

                // --- Months ---
                Arguments.of(CUTOVER_YEAR, 8, 12, 1L, MONTHS, CUTOVER_YEAR, 9, 23, false), // Adding a month lands in the gap, so it adjusts
                Arguments.of(CUTOVER_YEAR, 10, 12, -1L, MONTHS, CUTOVER_YEAR, 9, 23, false), // Subtracting a month lands in the gap
                Arguments.of(2014, 5, 26, 3L, MONTHS, 2014, 8, 26, true),

                // --- Larger Units ---
                Arguments.of(2014, 5, 26, 3L, YEARS, 2017, 5, 26, true),
                Arguments.of(2014, 5, 26, 3L, DECADES, 2044, 5, 26, true),
                Arguments.of(2014, 5, 26, 3L, CENTURIES, 2314, 5, 26, true),
                Arguments.of(2014, 5, 26, 3L, MILLENNIA, 5014, 5, 26, true),
                Arguments.of(2014, 5, 26, -1L, ERAS, -2013, 5, 26, true)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("plusAndMinusCases")
        @DisplayName("plus() should correctly add amounts across various units")
        void plus_addsAmount(int y1, int m1, int d1, long amount, TemporalUnit unit, int y2, int m2, int d2, boolean isReversible) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate expected = BritishCutoverDate.of(y2, m2, d2);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4} -> {0}-{1}-{2}")
        @MethodSource("plusAndMinusCases")
        @DisplayName("minus() should correctly subtract amounts across various units")
        void minus_subtractsAmount(int y1, int m1, int d1, long amount, TemporalUnit unit, int y2, int m2, int d2, boolean isReversible) {
            if (isReversible) {
                BritishCutoverDate start = BritishCutoverDate.of(y2, m2, d2);
                BritishCutoverDate expected = BritishCutoverDate.of(y1, m1, d1);
                assertEquals(expected, start.minus(amount, unit));
            }
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("`until` Method Behavior")
    class UntilTests {

        @ParameterizedTest(name = "Period from {0} to itself is ZERO")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        @DisplayName("until(sameDate) should return a zero period")
        void until_sameDate_returnsZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(cutoverDate));
        }

        @ParameterizedTest(name = "Period from {0} to {1} is ZERO")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        @DisplayName("until(equivalentIsoDate) should return a zero period")
        void until_equivalentIsoDate_returnsZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
        }

        @ParameterizedTest(name = "Period from {1} to {0} is ZERO")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        @DisplayName("isoDate.until(equivalentCutoverDate) should return a zero period")
        void isoDate_until_equivalentCutoverDate_returnsZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
        }

        static Stream<Arguments> untilUnitCases() {
            return Stream.of(
                // --- DAYS ---
                Arguments.of(date(CUTOVER_YEAR, 9, 1), date(CUTOVER_YEAR, 9, 2), DAYS, 1L),
                Arguments.of(date(CUTOVER_YEAR, 9, 1), date(CUTOVER_YEAR, 9, 14), DAYS, 2L), // Crosses the 11-day gap
                Arguments.of(date(CUTOVER_YEAR, 9, 2), date(CUTOVER_YEAR, 9, 14), DAYS, 1L), // Adjacent days across the gap
                Arguments.of(date(2014, 5, 20), date(2014, 5, 26), DAYS, 6L),

                // --- WEEKS ---
                Arguments.of(date(CUTOVER_YEAR, 9, 2), date(CUTOVER_YEAR, 9, 20), WEEKS, 1L), // 1 week is 7 physical days
                Arguments.of(date(2014, 5, 26), date(2014, 6, 2), WEEKS, 1L),

                // --- MONTHS ---
                Arguments.of(date(CUTOVER_YEAR, 9, 2), date(CUTOVER_YEAR, 10, 2), MONTHS, 1L),
                Arguments.of(date(2014, 5, 26), date(2014, 6, 26), MONTHS, 1L),

                // --- LARGER UNITS ---
                Arguments.of(date(2014, 5, 26), date(2015, 5, 26), YEARS, 1L),
                Arguments.of(date(2014, 5, 26), date(2024, 5, 26), DECADES, 1L),
                Arguments.of(date(2014, 5, 26), date(2114, 5, 26), CENTURIES, 1L),
                Arguments.of(date(-2013, 5, 26), date(2014, 5, 26), ERAS, 1L)
            );
        }

        @ParameterizedTest(name = "until({1}, {2}) on {0} should be {3}")
        @MethodSource("untilUnitCases")
        @DisplayName("until(endDate, unit) should calculate duration correctly")
        void until_calculatesDurationInUnits(BritishCutoverDate start, BritishCutoverDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodCases() {
            return Stream.of(
                // --- Simple cases, no cutover involved ---
                Arguments.of(date(1750, 7, 2), date(1750, 7, 1), 0, 0, -1),
                Arguments.of(date(1750, 7, 2), date(1750, 7, 2), 0, 0, 0),
                Arguments.of(date(1750, 7, 2), date(1750, 9, 2), 0, 2, 0),

                // --- Spanning the cutover ---
                // Start before gap, end is the day after the gap
                Arguments.of(date(CUTOVER_YEAR, 9, 2), date(CUTOVER_YEAR, 9, 14), 0, 0, 1),
                // Start before gap, end is later in the same month
                Arguments.of(date(CUTOVER_YEAR, 9, 2), date(CUTOVER_YEAR, 9, 30), 0, 0, 17),
                // Start before gap, end is in the next month
                Arguments.of(date(CUTOVER_YEAR, 9, 2), date(CUTOVER_YEAR, 10, 2), 0, 1, 0),
                // Start is the day after the gap, end is before
                Arguments.of(date(CUTOVER_YEAR, 9, 14), date(CUTOVER_YEAR, 9, 2), 0, 0, -1)
            );
        }

        @ParameterizedTest(name = "Period from {0} to {1} should be {2}Y {3}M {4}D")
        @MethodSource("untilPeriodCases")
        @DisplayName("until(endDate) should return correct ChronoPeriod")
        void until_calculatesChronoPeriod(BritishCutoverDate start, BritishCutoverDate end, int y, int m, int d) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(y, m, d), start.until(end));
        }

        @ParameterizedTest(name = "{0} + period({2}Y {3}M {4}D) = {1}")
        @MethodSource("untilPeriodCases")
        @DisplayName("start.plus(start.until(end)) should equal end")
        void until_plusPeriod_returnsEndDate(BritishCutoverDate start, BritishCutoverDate end, int y, int m, int d) {
            assertEquals(end, start.plus(start.until(end)));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field Access and Adjustment")
    class FieldTests {

        static Stream<Arguments> fieldValues() {
            return Stream.of(
                // Date just before the cutover gap
                Arguments.of(date(CUTOVER_YEAR, 9, 2), DAY_OF_WEEK, 3L), // Wednesday
                Arguments.of(date(CUTOVER_YEAR, 9, 2), DAY_OF_MONTH, 2L),
                Arguments.of(date(CUTOVER_YEAR, 9, 2), DAY_OF_YEAR, 246L), // 31+29+31+30+31+30+31+31+2
                Arguments.of(date(CUTOVER_YEAR, 9, 2), MONTH_OF_YEAR, 9L),

                // Date just after the cutover gap
                Arguments.of(date(CUTOVER_YEAR, 9, 14), DAY_OF_WEEK, 4L), // Thursday
                Arguments.of(date(CUTOVER_YEAR, 9, 14), DAY_OF_MONTH, 14L),
                Arguments.of(date(CUTOVER_YEAR, 9, 14), DAY_OF_YEAR, 247L), // Day after 246
                Arguments.of(date(CUTOVER_YEAR, 9, 14), MONTH_OF_YEAR, 9L),

                // Modern date
                Arguments.of(date(2014, 5, 26), DAY_OF_WEEK, 1L), // Monday
                Arguments.of(date(2014, 5, 26), PROLEPTIC_MONTH, 2014 * 12 + 4),
                Arguments.of(date(2014, 5, 26), YEAR, 2014L),
                Arguments.of(date(2014, 5, 26), ERA, 1L), // AD
                Arguments.of(date(0, 6, 8), ERA, 0L)      // BC
            );
        }

        @ParameterizedTest(name = "{0} .getLong({1}) should be {2}")
        @MethodSource("fieldValues")
        @DisplayName("getLong(field) should return correct value for various fields")
        void getLong_returnsCorrectFieldValue(BritishCutoverDate date, TemporalField field, long expected) {
            assertEquals(expected, date.getLong(field));
        }

        static Stream<Arguments> fieldRanges() {
            return Stream.of(
                // DAY_OF_MONTH range for the shortened September 1752
                Arguments.of(date(CUTOVER_YEAR, 9, 23), DAY_OF_MONTH, 1, 30), // Range is based on full month
                // DAY_OF_YEAR range for the shortened year 1752
                Arguments.of(date(CUTOVER_YEAR, 1, 23), DAY_OF_YEAR, 1, 355),
                // ALIGNED_WEEK_OF_MONTH for the shortened September 1752
                Arguments.of(date(CUTOVER_YEAR, 9, 23), ALIGNED_WEEK_OF_MONTH, 1, 3),
                // Standard year
                Arguments.of(date(2011, 2, 23), DAY_OF_YEAR, 1, 365),
                // Leap year
                Arguments.of(date(2012, 2, 23), DAY_OF_YEAR, 1, 366)
            );
        }

        @ParameterizedTest(name = "{0} .range({1}) should be {2}-{3}")
        @MethodSource("fieldRanges")
        @DisplayName("range(field) should return correct value range")
        void range_returnsCorrectValueRange(BritishCutoverDate date, TemporalField field, long min, long max) {
            assertEquals(ValueRange.of(min, max), date.range(field));
        }

        static Stream<Arguments> withFieldAdjustmentCases() {
            return Stream.of(
                // Adjusting DAY_OF_MONTH into the gap (leniently moves to after the gap)
                Arguments.of(date(CUTOVER_YEAR, 9, 2), DAY_OF_MONTH, 3, date(CUTOVER_YEAR, 9, 14)),
                // Adjusting DAY_OF_YEAR into the gap
                Arguments.of(date(CUTOVER_YEAR, 9, 2), DAY_OF_YEAR, 247, date(CUTOVER_YEAR, 9, 14)),
                // Adjusting across month boundary for a day that doesn't exist
                Arguments.of(date(2011, 3, 31), MONTH_OF_YEAR, 2, date(2011, 2, 28)),
                // Adjusting a leap day to a non-leap year
                Arguments.of(date(2012, 2, 29), YEAR, 2011, date(2011, 2, 28)),
                // Standard adjustment
                Arguments.of(date(2014, 5, 26), DAY_OF_WEEK, 3, date(2014, 5, 28))
            );
        }

        @ParameterizedTest(name = "{0} .with({1}, {2}) should be {3}")
        @MethodSource("withFieldAdjustmentCases")
        @DisplayName("with(field, value) should adjust date correctly")
        void with_adjustsFieldCorrectly(BritishCutoverDate start, TemporalField field, long value, BritishCutoverDate expected) {
            assertEquals(expected, start.with(field, value));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("TemporalAdjuster Behavior")
    class AdjusterTests {

        @Test
        @DisplayName("with(lastDayOfMonth) should work correctly for the cutover month")
        void adjust_lastDayOfMonth_handlesCutover() {
            BritishCutoverDate dateInCutoverMonth = BritishCutoverDate.of(CUTOVER_YEAR, 9, 2);
            BritishCutoverDate expected = BritishCutoverDate.of(CUTOVER_YEAR, 9, 30);
            assertEquals(expected, dateInCutoverMonth.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        @DisplayName("with(LocalDate) should adjust to the specified ISO date")
        void adjust_withLocalDate() {
            BritishCutoverDate start = BritishCutoverDate.of(2012, 6, 23);
            LocalDate targetIso = LocalDate.of(1752, 9, 14);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 14);
            assertEquals(expected, start.with(targetIso));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("String Representation")
    class ToStringTests {
        @Test
        @DisplayName("toString() should return a descriptive string")
        void toString_returnsDescriptiveString() {
            assertEquals("BritishCutover AD 2012-06-23", BritishCutoverDate.of(2012, 6, 23).toString());
            assertEquals("BritishCutover BC 1-01-01", BritishCutoverDate.of(0, 1, 1).toString());
        }
    }

    // Helper to make test data creation more concise
    private static BritishCutoverDate date(int year, int month, int day) {
        return BritishCutoverDate.of(year, month, day);
    }
}