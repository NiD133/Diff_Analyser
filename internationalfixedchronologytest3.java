package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
import static java.time.temporal.ChronoField.EPOCH_DAY;
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
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("InternationalFixedChronology and InternationalFixedDate")
class InternationalFixedChronologyTest {

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleFixedAndIsoDates() {
        return Stream.of(
                Arguments.of(InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)),
                Arguments.of(InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)),
                Arguments.of(InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)), // Year Day
                Arguments.of(InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)),   // Leap Day
                Arguments.of(InternationalFixedDate.of(4, 13, 29), LocalDate.of(4, 12, 31)), // Year Day in leap year
                Arguments.of(InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)),
                Arguments.of(InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4))
        );
    }

    //-----------------------------------------------------------------------
    // Conversion tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion to/from other temporal types")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void from_ifcDate_toLocalDate_shouldReturnCorrectIsoDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(ifcDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void from_isoDate_toIfcDate_shouldReturnCorrectIfcDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void toEpochDay_shouldMatchIsoDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), ifcDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void chronology_dateFromEpochDay_shouldReturnCorrectIfcDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void chronology_dateFromTemporal_shouldReturnCorrectIfcDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }
    }

    //-----------------------------------------------------------------------
    // Factory method tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Factory methods")
    class FactoryTests {

        static Stream<Arguments> invalidDateComponents() {
            return Stream.of(
                    Arguments.of(0, 1, 1, "zero year"),
                    Arguments.of(1900, 14, 1, "invalid month"),
                    Arguments.of(1900, 1, 29, "invalid day for standard month"),
                    Arguments.of(1900, 2, 29, "invalid day for standard month"),
                    Arguments.of(1900, 6, 29, "invalid day for month 6 in non-leap year"),
                    Arguments.of(1900, 13, 30, "invalid day for month 13")
            );
        }

        @ParameterizedTest(name = "[{index}] {3}: year={0}, month={1}, day={2}")
        @MethodSource("invalidDateComponents")
        void of_withInvalidDateParts_throwsException(int year, int month, int dayOfMonth, String description) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dayOfMonth));
        }

        static Stream<Integer> nonLeapYears() {
            return Stream.of(1, 100, 200, 300, 1900);
        }

        @ParameterizedTest
        @MethodSource("nonLeapYears")
        void of_withLeapDayInNonLeapYear_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }
    }

    //-----------------------------------------------------------------------
    // Field access tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field access")
    class FieldAccessTests {

        static Stream<Arguments> getLongDataProvider() {
            return Stream.of(
                    // Common year date: 2014-05-26
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5L),
                    Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                    Arguments.of(2014, 5, 26, DAY_OF_YEAR, 138L), // 4 * 28 + 26
                    Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                    Arguments.of(2014, 5, 26, YEAR, 2014L),
                    Arguments.of(2014, 5, 26, ERA, 1L),

                    // Leap year date: 2012-09-26
                    Arguments.of(2012, 9, 26, DAY_OF_YEAR, 251L), // 8 * 28 + 1 (leap day) + 26

                    // Year Day (non-leap year): 2014-13-29
                    Arguments.of(2014, 13, 29, DAY_OF_WEEK, 0L), // Year day is not a day of the week
                    Arguments.of(2014, 13, 29, DAY_OF_MONTH, 29L),
                    Arguments.of(2014, 13, 29, DAY_OF_YEAR, 365L), // 13 * 28 + 1
                    Arguments.of(2014, 13, 29, MONTH_OF_YEAR, 13L),

                    // Leap Day: 2012-06-29
                    Arguments.of(2012, 6, 29, DAY_OF_WEEK, 0L), // Leap day is not a day of the week
                    Arguments.of(2012, 6, 29, DAY_OF_MONTH, 29L),
                    Arguments.of(2012, 6, 29, DAY_OF_YEAR, 169L), // 6 * 28 + 1
                    Arguments.of(2012, 6, 29, MONTH_OF_YEAR, 6L)
            );
        }

        @ParameterizedTest(name = "{3} of {0}-{1}-{2} is {4}")
        @MethodSource("getLongDataProvider")
        void getLong_forField_returnsCorrectValue(int year, int month, int day, TemporalField field, long expectedValue) {
            InternationalFixedDate date = InternationalFixedDate.of(year, month, day);
            assertEquals(expectedValue, date.getLong(field));
        }

        static Stream<Arguments> rangeDataProvider() {
            return Stream.of(
                    // DAY_OF_MONTH ranges
                    Arguments.of(2012, 6, 29, DAY_OF_MONTH, ValueRange.of(1, 29), "Month 6 in leap year"),
                    Arguments.of(2011, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 28), "Month 6 in common year"),
                    Arguments.of(2012, 13, 23, DAY_OF_MONTH, ValueRange.of(1, 29), "Month 13 (always 29 days)"),
                    Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28), "Standard month"),

                    // DAY_OF_YEAR ranges
                    Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366), "Leap year"),
                    Arguments.of(2011, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 365), "Common year"),

                    // MONTH_OF_YEAR range is always 1-13
                    Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13), "Any year"),

                    // DAY_OF_WEEK ranges (special days are 0, regular days are 1-7)
                    Arguments.of(2012, 6, 29, DAY_OF_WEEK, ValueRange.of(0, 0), "Leap Day"),
                    Arguments.of(2012, 13, 29, DAY_OF_WEEK, ValueRange.of(0, 0), "Year Day"),
                    Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7), "Regular day")
            );
        }

        @ParameterizedTest(name = "[{index}] {4}: range of {2} for {0}-{1} is {3}")
        @MethodSource("rangeDataProvider")
        void range_forField_returnsCorrectValueRange(int year, int month, int day, TemporalField field, ValueRange expectedRange, String description) {
            assertEquals(expectedRange, InternationalFixedDate.of(year, month, day).range(field));
        }

        static Stream<Arguments> lengthOfMonthProvider() {
            return Stream.of(
                    Arguments.of(1900, 1, 28, "Standard month"),
                    Arguments.of(1900, 6, 28, "Month 6 in common year"),
                    Arguments.of(1904, 6, 29, "Month 6 in leap year"),
                    Arguments.of(1900, 13, 29, "Month 13 (always long)")
            );
        }

        @ParameterizedTest(name = "[{index}] {3}: year={0}, month={1}")
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth_returnsCorrectNumberOfDays(int year, int month, int expectedLength, String description) {
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, 1).lengthOfMonth());
        }
    }

    //-----------------------------------------------------------------------
    // Adjustment tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Adjustment methods")
    class AdjustmentTests {

        static Stream<Arguments> withDataProvider() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22),
                    Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                    Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 13, 28),
                    Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                    Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                    // Adjusting a leap day to a regular day of week
                    Arguments.of(2012, 6, 29, DAY_OF_WEEK, 7, 2012, 6, 28)
            );
        }

        @ParameterizedTest(name = "with({3}, {4}) on {0}-{1}-{2} -> {5}-{6}-{7}")
        @MethodSource("withDataProvider")
        void with_fieldAndValue_returnsAdjustedDate(int year, int month, int day, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDay) {
            InternationalFixedDate initialDate = InternationalFixedDate.of(year, month, day);
            InternationalFixedDate expectedDate = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expectedDate, initialDate.with(field, value));
        }

        static Stream<Arguments> withInvalidDataProvider() {
            return Stream.of(
                    Arguments.of(2013, 1, 1, DAY_OF_MONTH, 29, "Day 29 in standard month"),
                    Arguments.of(2013, 6, 1, DAY_OF_MONTH, 29, "Day 29 in month 6 of common year"),
                    Arguments.of(2013, 1, 1, DAY_OF_YEAR, 366, "Day 366 in common year"),
                    Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14, "Month 14")
            );
        }

        @ParameterizedTest(name = "[{index}] {4}: with({2}, {3}) on {0}-{1}")
        @MethodSource("withInvalidDataProvider")
        void with_invalidFieldValue_throwsException(int year, int month, int day, TemporalField field, long value, String description) {
            InternationalFixedDate date = InternationalFixedDate.of(year, month, day);
            assertThrows(DateTimeException.class, () -> date.with(field, value));
        }

        @Test
        void with_lastDayOfMonth_adjustsToEndOfMonth() {
            InternationalFixedDate date = InternationalFixedDate.of(2012, 6, 23);
            InternationalFixedDate expected = InternationalFixedDate.of(2012, 6, 29);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    //-----------------------------------------------------------------------
    // Addition and Subtraction tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Addition and Subtraction")
    class PlusMinusTests {

        static Stream<Arguments> plusMinusDataProvider() {
            return Stream.of(
                    // Days
                    Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 6),
                    // Weeks
                    Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 19),
                    // Months
                    Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                    // Years
                    Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                    // Decades
                    Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                    // Centuries
                    Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                    // Millennia
                    Arguments.of(2014, 5, 26, -1, MILLENNIA, 1014, 5, 26),
                    // Across year boundary
                    Arguments.of(2014, 13, 26, 3, WEEKS, 2015, 1, 19),
                    // Handling leap day
                    Arguments.of(2012, 6, 21, 53, WEEKS, 2013, 6, 28)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("plusMinusDataProvider")
        void plus_amountOfUnit_returnsCorrectlyAddedDate(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            InternationalFixedDate startDate = InternationalFixedDate.of(startYear, startMonth, startDay);
            InternationalFixedDate expectedDate = InternationalFixedDate.of(endYear, endMonth, endDay);
            assertEquals(expectedDate, startDate.plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4} -> {0}-{1}-{2}")
        @MethodSource("plusMinusDataProvider")
        void minus_amountOfUnit_returnsCorrectlySubtractedDate(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            InternationalFixedDate expectedDate = InternationalFixedDate.of(startYear, startMonth, startDay);
            InternationalFixedDate endDate = InternationalFixedDate.of(endYear, endMonth, endDay);
            assertEquals(expectedDate, endDate.minus(amount, unit));
        }
    }

    //-----------------------------------------------------------------------
    // Until tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Until method")
    class UntilTests {

        static Stream<Arguments> untilDataProvider() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0L),
                    Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 6L),
                    Arguments.of(2014, 5, 26, 2014, 6, 5, WEEKS, 1L),
                    Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
                    Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
                    // Across year boundary with Year Day
                    Arguments.of(2014, 13, 28, 2015, 1, 1, DAYS, 2L),
                    // Across leap day
                    Arguments.of(2012, 6, 28, 2012, 7, 1, DAYS, 2L)
            );
        }

        @ParameterizedTest(name = "until({4}) from {0}-{1}-{2} to {3} is {5}")
        @MethodSource("untilDataProvider")
        void until_withUnit_returnsCorrectDuration(int year1, int month1, int day1, int year2, int month2, int day2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(year1, month1, day1);
            InternationalFixedDate end = InternationalFixedDate.of(year2, month2, day2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodProvider() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                    Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 6),
                    Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                    Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                    // Special case: period between leap day and year day
                    Arguments.of(2004, 6, 29, 2004, 13, 28, 0, 6, 28, "Not 7 months flat")
            );
        }

        @ParameterizedTest(name = "period from {0}-{1}-{2} to {3}-{4}-{5} is P{6}Y{7}M{8}D")
        @MethodSource("untilPeriodProvider")
        void until_asPeriod_returnsCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD, String... description) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void until_sameDate_returnsZeroPeriod(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(ifcDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void until_equivalentIsoDate_returnsZeroIfcPeriod(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void isoDate_until_equivalentIfcDate_returnsZeroIsoPeriod(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(ifcDate));
        }
    }

    //-----------------------------------------------------------------------
    // Object method tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Object methods")
    class ObjectMethodTests {

        static Stream<Arguments> toStringProvider() {
            return Stream.of(
                    Arguments.of(InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"),
                    Arguments.of(InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"),
                    Arguments.of(InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"), // Leap Day
                    Arguments.of(InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29") // Year Day
            );
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        void toString_returnsCorrectFormatting(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }

        @Test
        void isLeapYear_matchesGregorianRule() {
            for (int year = 1; year < 500; year++) {
                boolean expected = java.time.chrono.IsoChronology.INSTANCE.isLeapYear(year);
                assertEquals(expected, InternationalFixedChronology.INSTANCE.isLeapYear(year));
            }
        }
    }
}