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
import java.time.Month;
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
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Parameterized tests for {@link BritishCutoverChronology} and {@link BritishCutoverDate}.
 */
@DisplayName("BritishCutoverChronology and BritishCutoverDate")
public class BritishCutoverChronologyTest {

    /**
     * Provides sample BritishCutoverDates and their equivalent ISO LocalDates.
     * This data is used for conversion and consistency checks.
     * @return a stream of arguments: (BritishCutoverDate, LocalDate)
     */
    static Stream<Arguments> provideSampleCutoverAndIsoDates() {
        return Stream.of(
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(BritishCutoverDate.of(1, 2, 28), LocalDate.of(1, 2, 26)),
            Arguments.of(BritishCutoverDate.of(1, 3, 1), LocalDate.of(1, 2, 27)),
            Arguments.of(BritishCutoverDate.of(1, 3, 2), LocalDate.of(1, 2, 28)),
            Arguments.of(BritishCutoverDate.of(1, 3, 3), LocalDate.of(1, 3, 1)),
            Arguments.of(BritishCutoverDate.of(4, 2, 28), LocalDate.of(4, 2, 26)),
            Arguments.of(BritishCutoverDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
            Arguments.of(BritishCutoverDate.of(4, 3, 1), LocalDate.of(4, 2, 28)),
            Arguments.of(BritishCutoverDate.of(4, 3, 2), LocalDate.of(4, 2, 29)),
            Arguments.of(BritishCutoverDate.of(4, 3, 3), LocalDate.of(4, 3, 1)),
            Arguments.of(BritishCutoverDate.of(100, 2, 28), LocalDate.of(100, 2, 26)),
            Arguments.of(BritishCutoverDate.of(100, 2, 29), LocalDate.of(100, 2, 27)),
            Arguments.of(BritishCutoverDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
            Arguments.of(BritishCutoverDate.of(100, 3, 2), LocalDate.of(100, 3, 1)),
            Arguments.of(BritishCutoverDate.of(100, 3, 3), LocalDate.of(100, 3, 2)),
            Arguments.of(BritishCutoverDate.of(0, 12, 31), LocalDate.of(0, 12, 29)),
            Arguments.of(BritishCutoverDate.of(0, 12, 30), LocalDate.of(0, 12, 28)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            Arguments.of(BritishCutoverDate.of(1751, 12, 20), LocalDate.of(1751, 12, 31)),
            Arguments.of(BritishCutoverDate.of(1751, 12, 31), LocalDate.of(1752, 1, 11)),
            Arguments.of(BritishCutoverDate.of(1752, 1, 1), LocalDate.of(1752, 1, 12)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 1), LocalDate.of(1752, 9, 12)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)),
            // Dates in the cutover gap are leniently accepted
            Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)),
            // After the cutover, dates are the same as ISO
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)),
            Arguments.of(BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12)),
            Arguments.of(BritishCutoverDate.of(2012, 7, 5), LocalDate.of(2012, 7, 5)),
            Arguments.of(BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6))
        );
    }

    @Nested
    @DisplayName("Conversion and Factory Tests")
    class ConversionAndFactoryTests {

        @ParameterizedTest(name = "{index}: {0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        void shouldConvertFromBritishCutoverDateToLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest(name = "{index}: {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        void shouldConvertFromLocalDateToBritishCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest(name = "{index}: epochDay({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        void shouldCreateFromEpochDay(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest(name = "{index}: {0}.toEpochDay() -> {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        void shouldConvertToEpochDay(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest(name = "{index}: from({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        void shouldCreateFromTemporalAccessor(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @CsvSource({
            "1900,  0,  0", "1900, -1,  1", "1900,  0,  1", "1900, 13,  1", "1900, 14,  1",
            "1900,  1, -1", "1900,  1,  0", "1900,  1, 32",
            "1900,  2, 30", // 1900 is not a leap year in Gregorian, but is in Julian
            "1899,  2, 29", // 1899 is not a leap year
            "1900,  4, 31", "1900,  6, 31", "1900,  9, 31", "1900, 11, 31"
        })
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
        }
    }

    @Nested
    @DisplayName("Date Property Tests")
    class DatePropertyTests {

        @ParameterizedTest(name = "{0}-{1} should have {2} days")
        @CsvSource({
            "1700,  1, 31", "1700,  2, 29", "1700,  3, 31", "1700,  4, 30",
            "1751,  2, 28", "1751, 12, 31",
            "1752,  1, 31", "1752,  2, 29", "1752,  8, 31",
            "1752,  9, 19", // September 1752 is the cutover month
            "1752, 10, 31",
            "1753,  2, 28", "1753,  3, 31",
            "1800,  2, 28", "1900,  2, 28", "2000,  2, 29", "2100,  2, 28"
        })
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        @ParameterizedTest(name = "Year {0} should have {1} days")
        @CsvSource({
            "  -1, 365", "   0, 366", " 100, 366", "1600, 366", "1700, 366",
            "1751, 365",
            "1752, 355", // 1752 is the cutover year, 11 days were removed
            "1753, 365",
            "1800, 365", "1900, 365", "1904, 366", "2000, 366", "2100, 365"
        })
        void lengthOfYear_shouldReturnCorrectLength(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
            // Check from end of year as well for robustness
            assertEquals(expectedLength, BritishCutoverDate.of(year, 12, 31).lengthOfYear());
        }

        static Stream<Arguments> provideDateRanges() {
            return Stream.of(
                Arguments.of(1700, 2, 23, DAY_OF_MONTH, 1, 29), // Julian leap year
                Arguments.of(1751, 2, 23, DAY_OF_MONTH, 1, 28), // Julian non-leap year
                Arguments.of(1752, 9, 23, DAY_OF_MONTH, 1, 30), // Cutover month (lenient range)
                Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28), // Gregorian non-leap year
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29), // Gregorian leap year
                Arguments.of(1751, 1, 23, DAY_OF_YEAR, 1, 365),
                Arguments.of(1752, 1, 23, DAY_OF_YEAR, 1, 355), // Cutover year
                Arguments.of(1753, 1, 23, DAY_OF_YEAR, 1, 365),
                Arguments.of(1752, 9, 23, ALIGNED_WEEK_OF_MONTH, 1, 3), // Cutover month
                Arguments.of(1752, 12, 23, ALIGNED_WEEK_OF_YEAR, 1, 51)  // Cutover year
            );
        }

        @ParameterizedTest(name = "{3} in {0}-{1}-{2} should have range {4}-{5}")
        @MethodSource("provideDateRanges")
        void range_shouldReturnCorrectRangeForField(int year, int month, int dayOfMonth, TemporalField field, long expectedMin, long expectedMax) {
            BritishCutoverDate date = BritishCutoverDate.of(year, month, dayOfMonth);
            ValueRange expectedRange = ValueRange.of(expectedMin, expectedMax);
            assertEquals(expectedRange, date.range(field));
        }

        static Stream<Arguments> provideFieldValues() {
            return Stream.of(
                Arguments.of(1752, 9, 2, DAY_OF_WEEK, 3),
                Arguments.of(1752, 9, 2, DAY_OF_MONTH, 2),
                Arguments.of(1752, 9, 2, DAY_OF_YEAR, 246), // 31+29+31+30+31+30+31+31+2
                Arguments.of(1752, 9, 14, DAY_OF_WEEK, 4),
                Arguments.of(1752, 9, 14, DAY_OF_MONTH, 14),
                Arguments.of(1752, 9, 14, DAY_OF_YEAR, 247), // Day after 1752-09-02
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 146), // 31+28+31+30+26
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 4),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                Arguments.of(0, 6, 8, ERA, 0),
                Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 1)
            );
        }

        @ParameterizedTest(name = "{3} of {0}-{1}-{2} should be {4}")
        @MethodSource("provideFieldValues")
        void getLong_shouldReturnCorrectValueForField(int year, int month, int dayOfMonth, TemporalField field, long expectedValue) {
            BritishCutoverDate date = BritishCutoverDate.of(year, month, dayOfMonth);
            assertEquals(expectedValue, date.getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation Tests")
    class ManipulationTests {

        static Stream<Arguments> providePlusArguments() {
            return Stream.of(
                // Around the cutover
                Arguments.of(1752, 9, 2, -1, DAYS, 1752, 9, 1),
                Arguments.of(1752, 9, 2, 0, DAYS, 1752, 9, 2),
                Arguments.of(1752, 9, 2, 1, DAYS, 1752, 9, 14),
                Arguments.of(1752, 9, 2, 2, DAYS, 1752, 9, 15),
                Arguments.of(1752, 9, 14, -1, DAYS, 1752, 9, 2),
                Arguments.of(1752, 9, 14, 0, DAYS, 1752, 9, 14),
                Arguments.of(1752, 9, 14, 1, DAYS, 1752, 9, 15),
                Arguments.of(1752, 9, 2, 1, WEEKS, 1752, 9, 20),
                Arguments.of(1752, 9, 2, 1, MONTHS, 1752, 10, 2),
                // Standard cases
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                Arguments.of(2014, 5, 26, -1, ERAS, -2013, 5, 26)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("providePlusArguments")
        void plus_shouldAddAmountToUnitCorrectly(int startYear, int startMonth, int startDom, long amount, TemporalUnit unit, int endYear, int endMonth, int endDom) {
            BritishCutoverDate startDate = BritishCutoverDate.of(startYear, startMonth, startDom);
            BritishCutoverDate expectedDate = BritishCutoverDate.of(endYear, endMonth, endDom);
            assertEquals(expectedDate, startDate.plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4} -> {0}-{1}-{2}")
        @MethodSource("providePlusArguments") // Reusing the same data, but reversed
        void minus_shouldSubtractAmountToUnitCorrectly(int endYear, int endMonth, int endDom, long amount, TemporalUnit unit, int startYear, int startMonth, int startDom) {
            BritishCutoverDate startDate = BritishCutoverDate.of(startYear, startMonth, startDom);
            BritishCutoverDate expectedDate = BritishCutoverDate.of(endYear, endMonth, endDom);
            assertEquals(startDate, expectedDate.minus(amount, unit));
        }

        static Stream<Arguments> provideWithArguments() {
            return Stream.of(
                // Test with() around the cutover, which can be lenient
                Arguments.of(1752, 9, 2, DAY_OF_WEEK, 4, 1752, 9, 14),
                Arguments.of(1752, 9, 2, DAY_OF_MONTH, 3, 1752, 9, 14), // lenient
                Arguments.of(1752, 9, 2, DAY_OF_YEAR, 247, 1752, 9, 14), // lenient, 247 is day after 246
                Arguments.of(1752, 8, 4, MONTH_OF_YEAR, 9, 1752, 9, 15), // lenient
                Arguments.of(1751, 9, 4, YEAR, 1752, 1752, 9, 15), // lenient
                // Standard cases
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 28),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2014, 5, 26, ERA, 0, -2013, 5, 26),
                Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29), // Adjust to shorter month
                Arguments.of(2012, 2, 29, YEAR, 2011, 2011, 2, 28) // Adjust leap day to non-leap year
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("provideWithArguments")
        void with_shouldSetFieldCorrectly(int startYear, int startMonth, int startDom, TemporalField field, long value, int endYear, int endMonth, int endDom) {
            BritishCutoverDate startDate = BritishCutoverDate.of(startYear, startMonth, startDom);
            BritishCutoverDate expectedDate = BritishCutoverDate.of(endYear, endMonth, endDom);
            assertEquals(expectedDate, startDate.with(field, value));
        }
    }

    @Nested
    @DisplayName("Period and Duration Tests")
    class PeriodAndDurationTests {

        @ParameterizedTest(name = "{index}: until({0}, {1}) should be zero")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        void until_withEquivalentDate_shouldReturnZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            // Test until a date in the same chronology
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(cutoverDate));
            // Test until a date in a different chronology (ISO)
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
            // Test from ISO to BritishCutover
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
        }

        @ParameterizedTest(name = "days between {0} and its variants")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#provideSampleCutoverAndIsoDates")
        void until_withDaysUnit_shouldCalculateDifferenceInDays(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(0, cutoverDate.until(isoDate.plusDays(0), DAYS));
            assertEquals(1, cutoverDate.until(isoDate.plusDays(1), DAYS));
            assertEquals(35, cutoverDate.until(isoDate.plusDays(35), DAYS));
            assertEquals(-40, cutoverDate.until(isoDate.minusDays(40), DAYS));
        }

        static Stream<Arguments> provideUntilPeriodArguments() {
            return Stream.of(
                // Simple case, no cutover involved
                Arguments.of(2000, 5, 10, 2001, 6, 12, 1, 1, 2),
                // Spanning the cutover
                // Start: 1752-07-02, End: 1752-09-02 -> 2 months, 0 days
                Arguments.of(1752, 7, 2, 1752, 9, 2, 0, 2, 0),
                // Start: 1752-07-02, End: 1752-09-14 (day after 09-02) -> 2 months, 1 day
                Arguments.of(1752, 7, 2, 1752, 9, 14, 0, 2, 1),
                // Start: 1752-09-02, End: 1752-09-14 -> 0 months, 1 day
                Arguments.of(1752, 9, 2, 1752, 9, 14, 0, 0, 1),
                // Start: 1752-09-14, End: 1752-09-02 (backwards) -> 0 months, -1 day
                Arguments.of(1752, 9, 14, 1752, 9, 2, 0, 0, -1)
            );
        }

        @ParameterizedTest(name = "period from {0}-{1}-{2} to {3}-{4}-{5} should be Y={6} M={7} D={8}")
        @MethodSource("provideUntilPeriodArguments")
        void until_withChronoLocalDate_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate startDate = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate endDate = BritishCutoverDate.of(y2, m2, d2);
            ChronoPeriod expectedPeriod = BritishCutoverChronology.INSTANCE.period(ey, em, ed);

            assertEquals(expectedPeriod, startDate.until(endDate));
            // Check that adding the period back gives the end date
            assertEquals(endDate, startDate.plus(expectedPeriod));
        }
    }

    @Nested
    @DisplayName("Adjuster Tests")
    class AdjusterTests {

        static Stream<Arguments> provideLastDayOfMonthDates() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 2, 23), BritishCutoverDate.of(1752, 2, 29)),
                Arguments.of(BritishCutoverDate.of(1752, 6, 23), BritishCutoverDate.of(1752, 6, 30)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 30)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 30)),
                Arguments.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 29)
            );
        }

        @ParameterizedTest(name = "lastDayOfMonth for {0} should be {1}")
        @MethodSource("provideLastDayOfMonthDates")
        void with_lastDayOfMonth_shouldReturnCorrectDate(BritishCutoverDate date, BritishCutoverDate expected) {
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_javaTimeMonth_shouldThrowException() {
            BritishCutoverDate date = BritishCutoverDate.of(2000, 1, 4);
            assertThrows(DateTimeException.class, () -> date.with(Month.APRIL));
        }
    }

    @Nested
    @DisplayName("String Representation Tests")
    class ToStringTests {
        @ParameterizedTest
        @CsvSource({
            "1, 1, 1, 'BritishCutover AD 1-01-01'",
            "2012, 6, 23, 'BritishCutover AD 2012-06-23'"
        })
        void toString_shouldReturnFormattedString(int year, int month, int day, String expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, day).toString());
        }
    }
}