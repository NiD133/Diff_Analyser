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
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link BritishCutoverChronology} and {@link BritishCutoverDate}.
 */
@DisplayName("BritishCutoverChronology and BritishCutoverDate")
public class BritishCutoverChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample {@link BritishCutoverDate} instances and their equivalent {@link LocalDate}.
     *
     * @return a stream of arguments: (BritishCutoverDate, LocalDate).
     */
    static Object[][] sampleDatesProvider() {
        return new Object[][] {
            {BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
            {BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)},
            {BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
            {BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
            {BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)},
            {BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)},
            // Leniently accept invalid dates in the cutover gap
            {BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)},
            {BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)},
            {BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)},
            {BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6)},
        };
    }

    /**
     * Provides invalid date components.
     *
     * @return a stream of arguments: (year, month, dayOfMonth).
     */
    static Object[][] invalidDateProvider() {
        return new Object[][] {
            {1900, 0, 0}, {1900, -1, 1}, {1900, 0, 1}, {1900, 13, 1}, {1900, 14, 1},
            {1900, 1, -1}, {1900, 1, 0}, {1900, 1, 32}, {1900, 2, 30}, {1899, 2, 29},
            {1900, 4, 31}, {1900, 6, 31}, {1900, 9, 31}, {1900, 11, 31},
        };
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion and Factory Methods")
    class ConversionAndFactoryTests {

        @ParameterizedTest(name = "{index}: {0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDatesProvider")
        void fromBritishCutoverDate_toLocalDate_shouldBeCorrect(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(iso, LocalDate.from(cutover));
        }

        @ParameterizedTest(name = "{index}: {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDatesProvider")
        void fromLocalDate_toBritishCutoverDate_shouldBeCorrect(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(cutover, BritishCutoverDate.from(iso));
        }

        @ParameterizedTest(name = "{index}: epochDay({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDatesProvider")
        void chronologyDateEpochDay_shouldBeCorrect(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(cutover, BritishCutoverChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest(name = "{index}: {0}.toEpochDay() -> {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDatesProvider")
        void toEpochDay_shouldBeCorrect(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(iso.toEpochDay(), cutover.toEpochDay());
        }

        @ParameterizedTest(name = "{index}: chronology.date({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDatesProvider")
        void chronologyDateFromTemporal_shouldBeCorrect(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(cutover, BritishCutoverChronology.INSTANCE.date(iso));
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#invalidDateProvider")
        void of_withInvalidDate_shouldThrowException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dom));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Properties")
    class PropertyTests {

        /**
         * @return a stream of arguments: (year, month, expectedLength).
         */
        static Object[][] lengthOfMonthProvider() {
            return new Object[][] {
                {1700, 1, 31}, {1700, 2, 29}, {1751, 2, 28}, {1752, 1, 31},
                {1752, 2, 29}, {1752, 9, 19}, {1752, 10, 31}, {1753, 2, 28},
                {1800, 2, 28}, {1900, 2, 28}, {2000, 2, 29}, {2100, 2, 28},
            };
        }

        @ParameterizedTest(name = "Month {1} in year {0} should have {2} days")
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int length) {
            assertEquals(length, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        /**
         * @return a stream of arguments: (year, expectedLength).
         */
        static Object[][] lengthOfYearProvider() {
            return new Object[][] {
                {-100, 366}, {-99, 365}, {0, 366}, {100, 366}, {1700, 366},
                {1751, 365}, {1752, 355}, {1753, 365}, {1800, 365}, {1900, 365},
                {1904, 366}, {2000, 366}, {2100, 365},
            };
        }

        @ParameterizedTest(name = "Year {0} should have {1} days")
        @MethodSource("lengthOfYearProvider")
        void lengthOfYear_shouldReturnCorrectLength(int year, int length) {
            assertEquals(length, BritishCutoverDate.of(year, 1, 1).lengthOfYear(), "from start of year");
            assertEquals(length, BritishCutoverDate.of(year, 12, 31).lengthOfYear(), "from end of year");
        }

        /**
         * @return a stream of arguments: (year, month, dom, field, expectedMin, expectedMax).
         */
        static Object[][] rangeProvider() {
            return new Object[][] {
                {1700, 2, 23, DAY_OF_MONTH, 1, 29}, {1751, 2, 23, DAY_OF_MONTH, 1, 28},
                {1752, 9, 23, DAY_OF_MONTH, 1, 30}, {2012, 2, 23, DAY_OF_MONTH, 1, 29},
                {1751, 1, 23, DAY_OF_YEAR, 1, 365}, {1752, 1, 23, DAY_OF_YEAR, 1, 355},
                {1753, 1, 23, DAY_OF_YEAR, 1, 365}, {2012, 1, 23, DAY_OF_YEAR, 1, 366},
                {1752, 9, 23, ALIGNED_WEEK_OF_MONTH, 1, 3},
                {1752, 12, 23, ALIGNED_WEEK_OF_YEAR, 1, 51},
            };
        }

        @ParameterizedTest(name = "range({3}) for {0}-{1}-{2} should be {4}-{5}")
        @MethodSource("rangeProvider")
        void range_forField_shouldReturnCorrectRange(int year, int month, int dom, TemporalField field, int expectedMin, int expectedMax) {
            ValueRange expectedRange = ValueRange.of(expectedMin, expectedMax);
            assertEquals(expectedRange, BritishCutoverDate.of(year, month, dom).range(field));
        }

        /**
         * @return a stream of arguments: (year, month, dom, field, expectedValue).
         */
        static Object[][] getLongProvider() {
            return new Object[][] {
                {1752, 9, 2, DAY_OF_WEEK, 3},
                {1752, 9, 2, DAY_OF_MONTH, 2},
                {1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 2},
                {1752, 9, 14, DAY_OF_WEEK, 4},
                {1752, 9, 14, DAY_OF_MONTH, 14},
                {1752, 9, 14, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3},
                {2014, 5, 26, DAY_OF_WEEK, 1},
                {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
                {2014, 5, 26, YEAR, 2014},
                {2014, 5, 26, ERA, 1},
                {0, 6, 8, ERA, 0},
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2}.getLong({3}) should be {4}")
        @MethodSource("getLongProvider")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, dom).getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Manipulation")
    class ManipulationTests {

        private static Stream<Arguments> dayManipulationProvider() {
            long[] days = {0, 1, 35, -1, -60};
            return Arrays.stream(sampleDatesProvider())
                .flatMap(datePair -> Arrays.stream(days)
                    .mapToObj(day -> Arguments.of(datePair[0], datePair[1], day)));
        }

        @ParameterizedTest(name = "{0} plus {2} days")
        @MethodSource("dayManipulationProvider")
        void plusDays_shouldBehaveLikeIso(BritishCutoverDate cutover, LocalDate iso, long daysToAdd) {
            LocalDate expected = iso.plusDays(daysToAdd);
            BritishCutoverDate actual = cutover.plus(daysToAdd, DAYS);
            assertEquals(expected, LocalDate.from(actual));
        }

        @ParameterizedTest(name = "{0} minus {2} days")
        @MethodSource("dayManipulationProvider")
        void minusDays_shouldBehaveLikeIso(BritishCutoverDate cutover, LocalDate iso, long daysToSubtract) {
            LocalDate expected = iso.minusDays(daysToSubtract);
            BritishCutoverDate actual = cutover.minus(daysToSubtract, DAYS);
            assertEquals(expected, LocalDate.from(actual));
        }

        /**
         * @return a stream of arguments: (startYear, startMonth, startDom, amount, unit, endYear, endMonth, endDom, isBidirectional).
         */
        static Object[][] plusMinusProvider() {
            return new Object[][] {
                {1752, 9, 2, -1, DAYS, 1752, 9, 1, true},
                {1752, 9, 2, 1, DAYS, 1752, 9, 14, true},
                {1752, 9, 14, -1, DAYS, 1752, 9, 2, true},
                {1752, 9, 2, 1, WEEKS, 1752, 9, 20, true},
                {1752, 9, 2, 1, MONTHS, 1752, 10, 2, true},
                {1752, 8, 12, 1, MONTHS, 1752, 9, 23, false}, // Not bidirectional due to cutover
                {2014, 5, 26, 3, YEARS, 2017, 5, 26, true},
                {2014, 5, 26, 3, DECADES, 2044, 5, 26, true},
                {2014, 5, 26, 3, CENTURIES, 2314, 5, 26, true},
                {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26, true},
                {2014, 5, 26, -1, ERAS, -2013, 5, 26, true},
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("plusMinusProvider")
        void plus_temporalUnit_shouldReturnCorrectDate(
            int startYear, int startMonth, int startDom, long amount, TemporalUnit unit,
            int endYear, int endMonth, int endDom, boolean isBidirectional) {
            BritishCutoverDate start = BritishCutoverDate.of(startYear, startMonth, startDom);
            BritishCutoverDate expected = BritishCutoverDate.of(endYear, endMonth, endDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4} -> {0}-{1}-{2}")
        @MethodSource("plusMinusProvider")
        void minus_temporalUnit_shouldBeInverseOfPlus(
            int startYear, int startMonth, int startDom, long amount, TemporalUnit unit,
            int endYear, int endMonth, int endDom, boolean isBidirectional) {
            if (isBidirectional) {
                BritishCutoverDate end = BritishCutoverDate.of(endYear, endMonth, endDom);
                BritishCutoverDate expected = BritishCutoverDate.of(startYear, startMonth, startDom);
                assertEquals(expected, end.minus(amount, unit));
            }
        }

        /**
         * @return a stream of arguments: (year, month, dom, field, value, expectedYear, expectedMonth, expectedDom).
         */
        static Object[][] withFieldProvider() {
            return new Object[][] {
                {1752, 9, 2, DAY_OF_WEEK, 4, 1752, 9, 14},
                // Leniently adjust into the cutover gap
                {1752, 9, 2, DAY_OF_MONTH, 3, 1752, 9, 14},
                {1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3, 1752, 9, 14},
                {1752, 9, 2, MONTH_OF_YEAR, 10, 1752, 10, 2},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                {2012, 2, 29, YEAR, 2011, 2011, 2, 28},
                {2014, 5, 26, WeekFields.ISO.dayOfWeek(), 2, 2014, 5, 27},
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2}.with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("withFieldProvider")
        void with_temporalField_shouldReturnCorrectDate(
            int year, int month, int dom, TemporalField field, long value,
            int expectedYear, int expectedMonth, int expectedDom) {
            BritishCutoverDate start = BritishCutoverDate.of(year, month, dom);
            BritishCutoverDate expected = BritishCutoverDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.with(field, value));
        }

        @Test
        void with_lastDayOfMonthAdjuster_shouldReturnCorrectDate() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 30);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_localDateAdjuster_shouldReturnCorrectDate() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            LocalDate adjuster = LocalDate.of(1752, 9, 14);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 14);
            assertEquals(expected, date.with(adjuster));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Calculating 'until'")
    class UntilTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDatesProvider")
        void until_sameDateAsChronoLocalDate_shouldReturnZeroPeriod(BritishCutoverDate cutover, LocalDate iso) {
            ChronoPeriod zeroPeriod = BritishCutoverChronology.INSTANCE.period(0, 0, 0);
            assertEquals(zeroPeriod, cutover.until(cutover), "until self should be zero");
            assertEquals(zeroPeriod, cutover.until(iso), "until equivalent ISO date should be zero");
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDatesProvider")
        void until_onLocalDate_shouldReturnZeroPeriod(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(cutover));
        }

        private static Stream<Arguments> untilDaysProvider() {
            long[] days = {0, 1, 35, -40};
            return Arrays.stream(sampleDatesProvider())
                .flatMap(datePair -> Arrays.stream(days)
                    .mapToObj(day -> Arguments.of(datePair[0], datePair[1], day)));
        }

        @ParameterizedTest(name = "{0}.until(plus {2} days, DAYS)")
        @MethodSource("untilDaysProvider")
        void until_withDaysUnit_shouldCalculateCorrectDistance(BritishCutoverDate cutover, LocalDate iso, long daysToAdd) {
            LocalDate endDate = iso.plusDays(daysToAdd);
            assertEquals(daysToAdd, cutover.until(endDate, DAYS));
        }

        /**
         * @return a stream of arguments: (year1, month1, dom1, year2, month2, dom2, unit, expectedAmount).
         */
        static Object[][] untilUnitProvider() {
            return new Object[][] {
                {1752, 9, 1, 1752, 9, 2, DAYS, 1},
                {1752, 9, 1, 1752, 9, 14, DAYS, 2},
                {1752, 9, 2, 1752, 9, 14, DAYS, 1},
                {1752, 9, 1, 1752, 9, 19, WEEKS, 1},
                {1752, 9, 1, 1752, 10, 1, MONTHS, 1},
                {2014, 5, 26, 2015, 5, 26, YEARS, 1},
                {2014, 5, 26, 2024, 5, 26, DECADES, 1},
                {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
                {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
                {-2013, 5, 26, 2014, 5, 26, ERAS, 1},
            };
        }

        @ParameterizedTest(name = "from {0}-{1}-{2} to {3}-{4}-{5} in {6} should be {7}")
        @MethodSource("untilUnitProvider")
        void until_temporalUnit_shouldReturnCorrectAmount(
            int year1, int month1, int dom1, int year2, int month2, int dom2,
            TemporalUnit unit, long expected) {
            BritishCutoverDate start = BritishCutoverDate.of(year1, month1, dom1);
            BritishCutoverDate end = BritishCutoverDate.of(year2, month2, dom2);
            assertEquals(expected, start.until(end, unit));
        }

        /**
         * @return a stream of arguments: (startYear, startMonth, startDom, endYear, endMonth, endDom, expectedYears, expectedMonths, expectedDays).
         */
        static Object[][] untilPeriodProvider() {
            return new Object[][] {
                {1752, 7, 2, 1752, 7, 1, 0, 0, -1},
                {1752, 7, 2, 1752, 7, 2, 0, 0, 0},
                // 30 days after 1752-08-02
                {1752, 7, 2, 1752, 9, 1, 0, 1, 30},
                // 2 whole months
                {1752, 7, 2, 1752, 9, 2, 0, 2, 0},
                // 1 day after 1752-09-02
                {1752, 7, 2, 1752, 9, 14, 0, 2, 1},
                {1752, 9, 14, 1752, 7, 14, 0, -2, 0},
                // 2 days before
                {1752, 9, 14, 1752, 9, 1, 0, 0, -2},
                // 1 day before
                {1752, 9, 14, 1752, 9, 2, 0, 0, -1},
            };
        }

        @ParameterizedTest(name = "from {0}-{1}-{2} to {3}-{4}-{5} is {6}Y {7}M {8}D")
        @MethodSource("untilPeriodProvider")
        void until_chronoLocalDate_shouldReturnCorrectPeriod(
            int year1, int month1, int dom1, int year2, int month2, int dom2,
            int expectedYears, int expectedMonths, int expectedDays) {
            BritishCutoverDate start = BritishCutoverDate.of(year1, month1, dom1);
            BritishCutoverDate end = BritishCutoverDate.of(year2, month2, dom2);
            ChronoPeriod expected = BritishCutoverChronology.INSTANCE.period(expectedYears, expectedMonths, expectedDays);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest(name = "start.plus(start.until(end)) should equal end")
        @MethodSource("untilPeriodProvider")
        void plus_periodFromUntil_shouldReturnEndDate(
            int year1, int month1, int dom1, int year2, int month2, int dom2,
            int expectedYears, int expectedMonths, int expectedDays) {
            BritishCutoverDate start = BritishCutoverDate.of(year1, month1, dom1);
            BritishCutoverDate end = BritishCutoverDate.of(year2, month2, dom2);
            ChronoPeriod period = start.until(end);
            assertEquals(end, start.plus(period));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("String Representation")
    class ToStringTests {
        @Test
        void toString_shouldReturnCorrectFormat() {
            assertEquals("BritishCutover AD 1-01-01", BritishCutoverDate.of(1, 1, 1).toString());
            assertEquals("BritishCutover AD 2012-06-23", BritishCutoverDate.of(2012, 6, 23).toString());
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Interoperability with JDK Types")
    class InteroperabilityWithJdkTypesTests {
        @Test
        void with_britishCutoverDate_onLocalDateTime_shouldReturnCorrectDateTime() {
            BritishCutoverDate cutoverDate = BritishCutoverDate.of(2012, 6, 23);
            LocalDateTime baseDateTime = LocalDateTime.MIN;
            LocalDateTime expectedDateTime = LocalDateTime.of(2012, 6, 23, 0, 0);
            assertEquals(expectedDateTime, baseDateTime.with(cutoverDate));
        }
    }
}