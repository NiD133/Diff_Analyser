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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link BritishCutoverChronology} and {@link BritishCutoverDate}.
 * This class focuses on date creation, conversion, property access, and manipulation.
 */
public class BritishCutoverChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample BritishCutoverDates and their equivalent ISO LocalDates.
     * @return Stream of arguments: {BritishCutoverDate, corresponding LocalDate}
     */
    public static Object[][] sampleBritishAndIsoDates() {
        return new Object[][] {
            {BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
            {BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
            {BritishCutoverDate.of(4, 2, 29), LocalDate.of(4, 2, 27)},
            {BritishCutoverDate.of(100, 3, 2), LocalDate.of(100, 3, 1)},
            {BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
            {BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)}, // Before cutover
            {BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)}, // Leniently handles invalid date in the gap
            {BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)}, // First day after cutover
            {BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6)}
        };
    }

    /**
     * Provides invalid date components.
     * @return Stream of arguments: {year, month, dayOfMonth}
     */
    public static Object[][] invalidDateProvider() {
        return new Object[][] {
            {1900, 0, 1}, {1900, 13, 1}, {1900, 1, 0}, {1900, 1, 32},
            {1900, 2, 30}, // Not a leap year in Gregorian
            {1899, 2, 29}, // Not a leap year in Julian
        };
    }

    /**
     * Provides data for testing month lengths.
     * @return Stream of arguments: {year, month, expectedLength}
     */
    public static Object[][] lengthOfMonthProvider() {
        return new Object[][] {
            {1700, 2, 29}, // Leap year in Julian
            {1751, 2, 28}, // Not a leap year
            {1752, 2, 29}, // Leap year
            {1752, 9, 19}, // Cutover month, 30-11 = 19 days
            {1800, 2, 28}, // Not a leap year in Gregorian
            {1900, 2, 28}, // Not a leap year in Gregorian
            {2000, 2, 29}, // Leap year in Gregorian
        };
    }

    /**
     * Provides data for testing year lengths.
     * @return Stream of arguments: {year, expectedLength}
     */
    public static Object[][] lengthOfYearProvider() {
        return new Object[][] {
            {1700, 366}, // Leap year in Julian
            {1752, 355}, // Cutover year, 366 - 11 = 355 days
            {1800, 365}, // Not a leap year in Gregorian
            {1900, 365}, // Not a leap year in Gregorian
            {2000, 366}, // Leap year in Gregorian
        };
    }

    /**
     * Provides data for testing field ranges.
     * @return Stream of arguments: {year, month, day, field, expectedMin, expectedMax}
     */
    public static Object[][] fieldRangeProvider() {
        return new Object[][] {
            {1700, 2, 23, DAY_OF_MONTH, 1, 29},
            {1752, 9, 23, DAY_OF_MONTH, 1, 30}, // Range is lenient, length is 19
            {2011, 2, 23, DAY_OF_MONTH, 1, 28},
            {1751, 1, 23, DAY_OF_YEAR, 1, 365},
            {1752, 1, 23, DAY_OF_YEAR, 1, 355},
            {1753, 1, 23, DAY_OF_YEAR, 1, 365},
            {1752, 9, 23, ALIGNED_WEEK_OF_MONTH, 1, 3}, // Short month
            {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, 1, 5},
            {1752, 12, 23, ALIGNED_WEEK_OF_YEAR, 1, 51}, // Short year
            {2011, 2, 23, ALIGNED_WEEK_OF_YEAR, 1, 53},
        };
    }

    /**
     * Provides data for testing getLong(field).
     * @return Stream of arguments: {year, month, day, field, expectedValue}
     */
    public static Object[][] fieldValueProvider() {
        return new Object[][] {
            // Day of year for 1752-09-02 (Julian): 31+29+31+30+31+30+31+31+2 = 246
            {1752, 9, 2, DAY_OF_YEAR, 246},
            // Day of year for 1752-09-14 (Gregorian, but day count is continuous): 246 + 1 = 247
            {1752, 9, 14, DAY_OF_YEAR, 247},
            {2014, 5, 26, DAY_OF_YEAR, 146},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
            {2014, 5, 26, YEAR, 2014},
            {1, 6, 8, ERA, 1},
            {0, 6, 8, ERA, 0},
        };
    }

    /**
     * Provides data for testing with(field, value).
     * @return Stream of arguments: {startYear, startMonth, startDay, field, newValue, expectedYear, expectedMonth, expectedDay}
     */
    public static Object[][] withFieldProvider() {
        return new Object[][] {
            {1752, 9, 2, DAY_OF_WEEK, 4, 1752, 9, 14}, // Set to Thursday, lands after cutover
            {1752, 9, 2, DAY_OF_MONTH, 14, 1752, 9, 14},
            {1752, 9, 2, DAY_OF_YEAR, 247, 1752, 9, 14}, // Day 247 is Sep 14
            {1752, 8, 4, MONTH_OF_YEAR, 9, 1752, 9, 15}, // Setting month to Sep lands in gap, resolves to valid date
            {1751, 9, 4, YEAR, 1752, 1752, 9, 15}, // Setting year to 1752 lands in gap, resolves
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            {2012, 2, 29, YEAR, 2011, 2011, 2, 28}, // Adjusts for non-leap year
        };
    }

    /**
     * Provides data for testing plus/minus operations.
     * @return Stream of arguments: {y, m, d, amount, unit, expectedY, expectedM, expectedD, isBidirectional}
     */
    public static Object[][] plusMinusUnitProvider() {
        return new Object[][] {
            {1752, 9, 2, 1, DAYS, 1752, 9, 14, true}, // Crosses the cutover gap
            {1752, 9, 14, -1, DAYS, 1752, 9, 2, true},
            {1752, 9, 2, 1, WEEKS, 1752, 9, 20, true},
            {1752, 8, 12, 1, MONTHS, 1752, 9, 23, false}, // Adding month lands in gap, day is adjusted
            {2014, 5, 26, 3, YEARS, 2017, 5, 26, true},
            {2014, 5, 26, -5, DECADES, 1964, 5, 26, true},
            {2014, 5, 26, 3, CENTURIES, 2314, 5, 26, true},
            {2014, 5, 26, -1, ERAS, -2013, 5, 26, true},
        };
    }

    /**
     * Provides data for testing until(end, unit).
     * @return Stream of arguments: {y1, m1, d1, y2, m2, d2, unit, expectedAmount}
     */
    public static Object[][] untilWithUnitProvider() {
        return new Object[][] {
            {1752, 9, 2, 1752, 9, 14, DAYS, 1}, // 1 day between Sep 2 and Sep 14
            {1752, 9, 1, 1752, 9, 14, DAYS, 2},
            {1752, 9, 1, 1752, 9, 19, WEEKS, 1},
            {1752, 9, 2, 1752, 10, 2, MONTHS, 1},
            {2014, 5, 26, 2015, 5, 26, YEARS, 1},
            {2014, 5, 26, 2024, 5, 26, DECADES, 1},
            {-2013, 5, 26, 2014, 5, 26, ERAS, 1},
        };
    }

    /**
     * Provides data for testing until(end) to produce a ChronoPeriod.
     * @return Stream of arguments: {y1, m1, d1, y2, m2, d2, expectedY, expectedM, expectedD}
     */
    public static Object[][] untilAsChronoPeriodProvider() {
        return new Object[][] {
            // Simple case, no cutover
            {2000, 1, 1, 2001, 2, 3, 1, 1, 2},
            // Across the cutover
            {1752, 9, 2, 1752, 9, 14, 0, 0, 1}, // 1 day period
            {1752, 9, 2, 1752, 10, 2, 0, 1, 0}, // 1 month period
            {1752, 8, 2, 1752, 10, 2, 0, 2, 0}, // 2 month period
            {1752, 7, 2, 1752, 9, 1, 0, 1, 30}, // Period calculation is complex near cutover
        };
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Conversion Tests")
    class ConversionTests {

        @ParameterizedTest(name = "{index}: {0} <-> {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleBritishAndIsoDates")
        @DisplayName("should be symmetric for BritishCutoverDate and ISO LocalDate")
        void isoConversion_shouldBeSymmetric(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest(name = "{index}: {0} <-> {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleBritishAndIsoDates")
        @DisplayName("should be symmetric for epoch day")
        void epochDayConversion_shouldBeSymmetric(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleBritishAndIsoDates")
        void chronologyDateFromTemporal_shouldReturnCorrectDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Factory and Property Tests")
    class FactoryTests {

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#invalidDateProvider")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, day));
        }

        @ParameterizedTest(name = "{0}-{1} should have {2} days")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#lengthOfMonthProvider")
        void lengthOfMonth_shouldReturnCorrectValue(int year, int month, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        @ParameterizedTest(name = "Year {0} should have {1} days")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#lengthOfYearProvider")
        void lengthOfYear_shouldReturnCorrectValue(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
            // Also check from the end of the year for robustness
            if (year != 1752) { // Sep 31 is not a valid day to create
                assertEquals(expectedLength, BritishCutoverDate.of(year, 12, 31).lengthOfYear());
            }
        }
    }

    @Nested
    @DisplayName("Field Access and Range Tests")
    class FieldTests {

        @ParameterizedTest(name = "{3} range for {0}-{1}-{2} is {4}-{5}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#fieldRangeProvider")
        void range_forField_shouldReturnCorrectRange(int year, int month, int day, TemporalField field, long min, long max) {
            BritishCutoverDate date = BritishCutoverDate.of(year, month, day);
            assertEquals(ValueRange.of(min, max), date.range(field));
        }

        @ParameterizedTest(name = "{3} of {0}-{1}-{2} is {4}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#fieldValueProvider")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, day).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation Tests")
    class ManipulationTests {

        @ParameterizedTest(name = "{index}: {0} with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#withFieldProvider")
        void with_fieldAndValue_shouldReturnCorrectlyAdjustedDate(
                int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
            BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        @Test
        void with_lastDayOfMonthAdjuster_shouldReturnCorrectDate() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 30);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @ParameterizedTest(name = "{index}: {0} plus/minus {1} days")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleBritishAndIsoDates")
        void plusAndMinusDays_shouldBeEquivalentToIso(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            long[] testDays = {0, 1, 35, -1, -60};
            for (long days : testDays) {
                assertEquals(isoDate.plusDays(days), LocalDate.from(cutoverDate.plus(days, DAYS)), "Testing plus " + days + " days");
                assertEquals(isoDate.minusDays(days), LocalDate.from(cutoverDate.minus(days, DAYS)), "Testing minus " + days + " days");
            }
        }

        @ParameterizedTest(name = "{index}: {0} plus/minus {3} {4}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#plusMinusUnitProvider")
        void plusAndMinus_forVariousUnits_shouldProduceCorrectDate(
                int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed, boolean isBidirectional) {

            BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
            BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);

            assertEquals(expected, start.plus(amount, unit));

            if (isBidirectional) {
                assertEquals(start, expected.minus(amount, unit));
            }
        }
    }

    @Nested
    @DisplayName("Period and 'until' Tests")
    class PeriodTests {

        @ParameterizedTest(name = "{index}: until({1})")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleBritishAndIsoDates")
        void until_self_shouldReturnZero(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(cutoverDate));
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
        }

        @ParameterizedTest(name = "until({3}-{4}-{5}, {6}) should be {7}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#untilWithUnitProvider")
        void until_withTemporalUnit_shouldReturnCorrectAmount(
                int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest(name = "period between {0}-{1}-{2} and {3}-{4}-{5}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#untilAsChronoPeriodProvider")
        void until_asChronoPeriod_shouldReturnCorrectPeriod(
                int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            ChronoPeriod expected = BritishCutoverChronology.INSTANCE.period(ey, em, ed);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest(name = "start.plus(until(end)) should equal end")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#untilAsChronoPeriodProvider")
        void plus_periodFromUntil_shouldReturnOriginalEndDate(
                int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            ChronoPeriod period = start.until(end);
            assertEquals(end, start.plus(period));
        }
    }

    @Nested
    @DisplayName("API Compliance Tests")
    class ApiTests {

        @Test
        void toString_shouldReturnFormattedString() {
            BritishCutoverDate date = BritishCutoverDate.of(2012, 6, 23);
            assertEquals("BritishCutover AD 2012-06-23", date.toString());
        }

        @Test
        void eraOf_shouldReturnCorrectEra() {
            assertEquals(JulianEra.AD, BritishCutoverChronology.INSTANCE.eraOf(1));
            assertEquals(JulianEra.BC, BritishCutoverChronology.INSTANCE.eraOf(0));
        }
    }
}