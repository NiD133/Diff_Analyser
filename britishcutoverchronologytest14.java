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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link BritishCutoverDate} class.
 * This class covers conversions, property access, modification, and arithmetic.
 */
@DisplayName("BritishCutoverDate Tests")
class BritishCutoverChronologyTestTest14 {

    private static final BritishCutoverChronology CHRONO = BritishCutoverChronology.INSTANCE;

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleCutoverAndIsoDates() {
        return Stream.of(
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30), "Pre-cutover date"),
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14), "Pre-cutover date"),
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13), "Last Julian date before cutover"),
            Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14), "Invalid date, leniently accepted"),
            Arguments.of(BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24), "Invalid date, leniently accepted"),
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14), "First Gregorian date after cutover"),
            Arguments.of(BritishCutoverDate.of(2012, 7, 5), LocalDate.of(2012, 7, 5), "Modern date")
        );
    }

    static Object[][] invalidDateComponents() {
        return new Object[][] {
            {1900, 0, 0}, {1900, -1, 1}, {1900, 13, 1}, {1900, 1, 0}, {1900, 1, 32},
            {1900, 2, 30}, {1899, 2, 29} // 1899 is not a leap year in Gregorian, but is in Julian
        };
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory and Conversion")
    class FactoryAndConversionTests {

        @ParameterizedTest(name = "[{index}] {2}: {0} -> ISO {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTestTest14#sampleCutoverAndIsoDates")
        void convertsToIsoLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate, String description) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest(name = "[{index}] {2}: ISO {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTestTest14#sampleCutoverAndIsoDates")
        void createsFromIsoLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate, String description) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest(name = "[{index}] {2}: Epoch day of {0} is {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTestTest14#sampleCutoverAndIsoDates")
        void convertsToEpochDay(BritishCutoverDate cutoverDate, LocalDate isoDate, String description) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest(name = "[{index}] {2}: Date from epoch day of {1} is {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTestTest14#sampleCutoverAndIsoDates")
        void createsFromEpochDay(BritishCutoverDate cutoverDate, LocalDate isoDate, String description) {
            assertEquals(cutoverDate, CHRONO.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest(name = "[{index}] {2}: Chronology date from {1} is {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTestTest14#sampleCutoverAndIsoDates")
        void createsFromTemporalAccessor(BritishCutoverDate cutoverDate, LocalDate isoDate, String description) {
            assertEquals(cutoverDate, CHRONO.date(isoDate));
        }

        @ParameterizedTest(name = "of({0}, {1}, {2}) should throw DateTimeException")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTestTest14#invalidDateComponents")
        void of_withInvalidDateParts_throwsException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
        }
    }

    @Nested
    @DisplayName("Date Properties")
    class DatePropertiesTests {

        static Object[][] monthLengths() {
            return new Object[][] {
                {1700, 2, 29}, {1751, 2, 28}, {1752, 2, 29}, {1752, 9, 19},
                {1753, 2, 28}, {1800, 2, 28}, {1900, 2, 28}, {2000, 2, 29}
            };
        }

        @ParameterizedTest(name = "Length of month {1}/{0} is {2}")
        @MethodSource("monthLengths")
        void lengthOfMonth_returnsCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        static Object[][] yearLengths() {
            return new Object[][] {
                {1700, 366}, {1751, 365}, {1752, 355}, {1753, 365},
                {1800, 365}, {1900, 365}, {2000, 366}, {2004, 366}
            };
        }

        @ParameterizedTest(name = "Length of year {0} is {1}")
        @MethodSource("yearLengths")
        void lengthOfYear_returnsCorrectLength(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
            assertEquals(expectedLength, BritishCutoverDate.of(year, 12, 31).lengthOfYear());
        }

        @Test
        @DisplayName("toString returns a descriptive string")
        void toString_returnsCorrectFormat() {
            assertEquals("BritishCutover AD 2012-06-23", BritishCutoverDate.of(2012, 6, 23).toString());
            assertEquals("BritishCutover AD 1-01-01", BritishCutoverDate.of(1, 1, 1).toString());
        }
    }

    @Nested
    @DisplayName("Field Access and Ranges")
    class FieldAccessAndRangeTests {

        static Object[][] fieldRanges() {
            // year, month, day, field, expectedMin, expectedMax
            return new Object[][] {
                {1752, 9, 23, DAY_OF_MONTH, 1, 30},
                {1752, 9, 23, ALIGNED_WEEK_OF_MONTH, 1, 3},
                {1752, 1, 23, DAY_OF_YEAR, 1, 355},
                {1752, 12, 23, ALIGNED_WEEK_OF_YEAR, 1, 51},
                {2012, 2, 23, DAY_OF_MONTH, 1, 29},
                {2011, 2, 23, DAY_OF_YEAR, 1, 365}
            };
        }

        @ParameterizedTest(name = "Range of {3} for {0}-{1}-{2} is {4}-{5}")
        @MethodSource("fieldRanges")
        void range_forField_returnsCorrectValueRange(int year, int month, int dom, TemporalField field, int expectedMin, int expectedMax) {
            assertEquals(ValueRange.of(expectedMin, expectedMax), BritishCutoverDate.of(year, month, dom).range(field));
        }

        static Object[][] fieldValues() {
            // year, month, day, field, expectedValue
            return new Object[][] {
                {1752, 9, 2, DAY_OF_WEEK, 3}, // Wednesday
                {1752, 9, 14, DAY_OF_WEEK, 4}, // Thursday
                {1752, 9, 14, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3},
                {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
                {2014, 5, 26, YEAR, 2014},
                {0, 6, 8, ERA, 0}
            };
        }

        @ParameterizedTest(name = "Value of {3} for {0}-{1}-{2} is {4}")
        @MethodSource("fieldValues")
        void getLong_forField_returnsCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, dom).getLong(field));
        }

        @Test
        @DisplayName("Chronology-level range checks")
        void chronology_range() {
            assertEquals(ValueRange.of(1, 7), CHRONO.range(DAY_OF_WEEK));
            assertEquals(ValueRange.of(1, 28, 31), CHRONO.range(DAY_OF_MONTH));
            assertEquals(ValueRange.of(1, 355, 366), CHRONO.range(DAY_OF_YEAR));
            assertEquals(ValueRange.of(1, 12), CHRONO.range(MONTH_OF_YEAR));
        }
    }

    @Nested
    @DisplayName("Date Modification")
    class DateModificationTests {

        static Stream<Arguments> withFieldCases() {
            // startYear, startMonth, startDom, field, value, expectedYear, expectedMonth, expectedDom, description
            return Stream.of(
                Arguments.of(1752, 9, 2, DAY_OF_MONTH, 14, 1752, 9, 14, "Set day of month across cutover"),
                Arguments.of(1752, 9, 2, DAY_OF_MONTH, 3, 1752, 9, 14, "Set day to 3 (leniently becomes 14)"),
                Arguments.of(1752, 9, 2, MONTH_OF_YEAR, 10, 1752, 10, 2, "Set month"),
                Arguments.of(1751, 9, 4, YEAR, 1752, 1752, 9, 15, "Set year to 1752 (leniently adjusts day)"),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26, "Set year for modern date"),
                Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29, "Set month, adjusting day for leap year"),
                Arguments.of(2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28, "Set month, adjusting day for common year")
            );
        }

        @ParameterizedTest(name = "[{index}] {8}")
        @MethodSource("withFieldCases")
        void with_temporalField_returnsAdjustedDate(int y, int m, int d, TemporalField f, long v, int ey, int em, int ed, String desc) {
            BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
            BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
            assertEquals(expected, start.with(f, v));
        }

        @Test
        @DisplayName("with(TemporalAdjuster.lastDayOfMonth) adjusts correctly")
        void with_lastDayOfMonth_adjustsCorrectly() {
            assertEquals(BritishCutoverDate.of(1752, 9, 30), BritishCutoverDate.of(1752, 9, 2).with(TemporalAdjusters.lastDayOfMonth()));
            assertEquals(BritishCutoverDate.of(2012, 2, 29), BritishCutoverDate.of(2012, 2, 1).with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        @DisplayName("with(LocalDate) returns a new date with same calendar system")
        void with_localDate_adjustsCorrectly() {
            BritishCutoverDate start = BritishCutoverDate.of(1752, 9, 2);
            LocalDate newDate = LocalDate.of(2012, 6, 30);
            assertEquals(BritishCutoverDate.of(2012, 6, 30), start.with(newDate));
        }
    }

    @Nested
    @DisplayName("Date Arithmetic")
    class DateArithmeticTests {

        @Test
        @DisplayName("plus/minus days works correctly around the cutover")
        void plusMinusDays_aroundCutover() {
            BritishCutoverDate dayBeforeCutover = BritishCutoverDate.of(1752, 9, 2);
            BritishCutoverDate dayOfCutover = BritishCutoverDate.of(1752, 9, 14);

            assertEquals(dayOfCutover, dayBeforeCutover.plus(1, DAYS));
            assertEquals(dayBeforeCutover, dayOfCutover.minus(1, DAYS));
        }

        static Stream<Arguments> plusUnitCases() {
            // startDate, amount, unit, expectedDate
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), 1, WEEKS, BritishCutoverDate.of(1752, 9, 20)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), -1, WEEKS, BritishCutoverDate.of(1752, 8, 27)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), 1, MONTHS, BritishCutoverDate.of(1752, 10, 2)),
                Arguments.of(BritishCutoverDate.of(1752, 8, 12), 1, MONTHS, BritishCutoverDate.of(1752, 9, 23)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, YEARS, BritishCutoverDate.of(2017, 5, 26)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), -5, DECADES, BritishCutoverDate.of(1964, 5, 26)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), -1, ERAS, BritishCutoverDate.of(-2013, 5, 26))
            );
        }

        @ParameterizedTest(name = "{0} plus {1} {2} -> {3}")
        @MethodSource("plusUnitCases")
        void plus_withTemporalUnit_returnsCorrectlyAddedDate(BritishCutoverDate start, long amount, TemporalUnit unit, BritishCutoverDate expected) {
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{3} minus {1} {2} -> {0}")
        @MethodSource("plusUnitCases")
        void minus_withTemporalUnit_returnsCorrectlySubtractedDate(BritishCutoverDate expected, long amount, TemporalUnit unit, BritishCutoverDate start) {
            assertEquals(expected, start.minus(amount, unit));
        }

        @Test
        @DisplayName("until a date is zero")
        void until_self_isZero() {
            BritishCutoverDate date = BritishCutoverDate.of(2012, 7, 5);
            assertEquals(CHRONO.period(0, 0, 0), date.until(date));

            LocalDate isoDate = LocalDate.of(2012, 7, 5);
            assertEquals(CHRONO.period(0, 0, 0), date.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(date));
        }

        static Stream<Arguments> untilUnitCases() {
            // start, end, unit, expectedAmount
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), BritishCutoverDate.of(1752, 9, 14), DAYS, 2L),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 20), WEEKS, 1L),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 10, 2), MONTHS, 1L),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), BritishCutoverDate.of(2015, 5, 26), YEARS, 1L)
            );
        }

        @ParameterizedTest(name = "{0} until {1} in {2} is {3}")
        @MethodSource("untilUnitCases")
        void until_withTemporalUnit_isCorrect(BritishCutoverDate start, BritishCutoverDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> periodUntilCases() {
            // start, end, expectedPeriod, description
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 7, 2), BritishCutoverDate.of(1752, 9, 14), CHRONO.period(0, 2, 1), "Across cutover gap"),
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), BritishCutoverDate.of(1752, 10, 1), CHRONO.period(0, 1, 0), "One month in cutover year"),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 7, 14), CHRONO.period(0, -2, 0), "Negative period across cutover"),
                Arguments.of(BritishCutoverDate.of(2010, 1, 15), BritishCutoverDate.of(2012, 9, 20), Period.of(2, 8, 5), "Modern period")
            );
        }

        @ParameterizedTest(name = "[{index}] {3}: Period from {0} to {1} is {2}")
        @MethodSource("periodUntilCases")
        void until_asChronoPeriod_isCorrect(BritishCutoverDate start, BritishCutoverDate end, ChronoPeriod expected, String description) {
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest(name = "[{index}] {3}: start.plus(start.until(end)) == end")
        @MethodSource("periodUntilCases")
        void plusPeriod_afterUntil_returnsOriginalEndDate(BritishCutoverDate start, BritishCutoverDate end, ChronoPeriod ignored, String description) {
            assertEquals(end, start.plus(start.until(end)));
        }
    }
}