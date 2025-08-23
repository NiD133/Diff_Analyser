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
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link InternationalFixedChronology} and {@link InternationalFixedDate}.
 *
 * <h2>International Fixed Calendar System Rules:</h2>
 * Based on the implementation and test data, the calendar has the following structure:
 * <ul>
 *     <li><b>13 Months:</b> The year is divided into 13 months.</li>
 *     <li><b>Month Lengths:</b>
 *         <ul>
 *             <li>Months 1-5 and 7-12 have 28 days.</li>
 *             <li>Month 6 (June) has 28 days in a common year and 29 days in a leap year (the extra day is Leap Day).</li>
 *             <li>Month 13 has 29 days. The 29th day is the "Year Day".</li>
 *         </ul>
 *     </li>
 *     <li><b>Leap Years:</b> The leap year rule is the same as the Gregorian calendar (every 4 years, except for years divisible by 100 but not by 400).</li>
 *     <li><b>Total Days:</b> A common year has 365 days (12 * 28 + 29), and a leap year has 366 days (11 * 28 + 2 * 29).</li>
 * </ul>
 */
class InternationalFixedChronologyTest {

    private static final int DAYS_IN_STD_MONTH = 28;

    //-----------------------------------------------------------------------
    // Factory and Conversion Tests
    //-----------------------------------------------------------------------
    @Nested
    class FactoryAndConversionTests {

        static Stream<Object[]> provideDateSamples() {
            return Stream.of(new Object[][]{
                // Common dates
                {InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)},
                {InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)},
                {InternationalFixedDate.of(1945, 10, 27), LocalDate.of(1945, 10, 6)},

                // Dates around the leap day in a non-leap year
                {InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)},
                {InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)},
                {InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)},

                // Dates around the leap day in a leap year
                {InternationalFixedDate.of(4, 6, 28), LocalDate.of(4, 6, 16)},
                {InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)}, // Leap day
                {InternationalFixedDate.of(4, 7, 1), LocalDate.of(4, 6, 18)},

                // Dates around the year day
                {InternationalFixedDate.of(1, 13, 28), LocalDate.of(1, 12, 30)},
                {InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)}, // Year day
                {InternationalFixedDate.of(2, 1, 1), LocalDate.of(2, 1, 1)},
            });
        }

        @ParameterizedTest
        @MethodSource("provideDateSamples")
        void fromFixedDateConvertsToCorrectLocalDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso, LocalDate.from(fixed));
        }

        @ParameterizedTest
        @MethodSource("provideDateSamples")
        void fromLocalDateConvertsToCorrectFixedDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("provideDateSamples")
        void dateFromEpochDayIsCorrect(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("provideDateSamples")
        void toEpochDayIsCorrect(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso.toEpochDay(), fixed.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("provideDateSamples")
        void chronologyDateFromTemporalIsCorrect(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedChronology.INSTANCE.date(iso));
        }

        static Stream<Object[]> provideInvalidDateComponents() {
            return Stream.of(new Object[][]{
                {1900, 1, 29}, // Month 1 has 28 days
                {1900, 2, 29}, // Month 2 has 28 days
                {1900, 6, 29}, // Month 6 has 28 days in a non-leap year
                {1900, 13, 30}, // Month 13 has 29 days
                {1900, 14, 1}, // Invalid month
                {1900, 0, 1}, // Invalid month
                {1900, 1, 0}, // Invalid day
                {0, 1, 1}, // Invalid year (proleptic starts at 1)
            });
        }

        @ParameterizedTest
        @MethodSource("provideInvalidDateComponents")
        void ofFailsForInvalidDateComponents(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dom));
        }

        static Stream<Integer> provideNonLeapYears() {
            return Stream.of(1, 100, 200, 300, 1900);
        }

        @ParameterizedTest
        @MethodSource("provideNonLeapYears")
        void ofFailsForLeapDayInNonLeapYear(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }

        static Stream<Integer> provideInvalidEraValues() {
            return Stream.of(-1, 0, 2);
        }

        @ParameterizedTest
        @MethodSource("provideInvalidEraValues")
        void eraOfFailsForInvalidEraValue(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        static Stream<Integer> provideInvalidProlepticYearsForEra() {
            return Stream.of(-10, -1, 0);
        }

        @ParameterizedTest
        @MethodSource("provideInvalidProlepticYearsForEra")
        void prolepticYearFailsForInvalidYearAndEraCombination(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }
    }

    //-----------------------------------------------------------------------
    // Field Access and Range Tests
    //-----------------------------------------------------------------------
    @Nested
    class FieldAccessAndRangeTests {

        static Stream<Object[]> provideDatesAndExpectedMonthLengths() {
            return Stream.of(new Object[][]{
                {1900, 1, 28},  // Month 1, non-leap year -> 28 days
                {1900, 6, 28},  // Month 6, non-leap year -> 28 days
                {1904, 6, 29},  // Month 6, leap year -> 29 days
                {1900, 13, 29}, // Month 13, non-leap year -> 29 days
                {1904, 13, 29}, // Month 13, leap year -> 29 days
            });
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndExpectedMonthLengths")
        void lengthOfMonthIsCorrect(int year, int month, int expectedLength) {
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Object[]> provideDatesAndFieldRanges() {
            return Stream.of(new Object[][]{
                // For a standard day in a leap year
                {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
                {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366)},
                {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)},
                {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
                {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},

                // For the leap day (June 29th in a leap year)
                {2012, 6, 29, DAY_OF_MONTH, ValueRange.of(1, 29)},
                {2012, 6, 29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)}, // Special day, not in a week
                {2012, 6, 29, DAY_OF_WEEK, ValueRange.of(0, 0)}, // Special day, not a day of week

                // For the year day (in month 13)
                {2012, 13, 29, DAY_OF_MONTH, ValueRange.of(1, 29)},
                {2012, 13, 29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)}, // Special day, not in a week
                {2012, 13, 29, DAY_OF_WEEK, ValueRange.of(0, 0)}, // Special day, not a day of week
            });
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndFieldRanges")
        void rangeReturnsCorrectValueRangeForField(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, InternationalFixedDate.of(year, month, dom).range(field));
        }

        static Stream<Object[]> provideDatesAndFieldValues() {
            return Stream.of(new Object[][]{
                {2014, 5, 26, DAY_OF_WEEK, 5L},
                {2014, 5, 26, DAY_OF_MONTH, 26L},
                // Day of year for 2014-05-26: (4 months * 28 days) + 26 days = 138
                {2014, 5, 26, DAY_OF_YEAR, (4 * DAYS_IN_STD_MONTH) + 26L},
                {2014, 5, 26, MONTH_OF_YEAR, 5L},
                {2014, 5, 26, YEAR, 2014L},
                {2014, 5, 26, ERA, 1L},

                // 2012 is a leap year, so month 6 has 29 days.
                // Day of year for 2012-09-26: (5*28) + 29 + (2*28) + 26 = 251
                {2012, 9, 26, DAY_OF_YEAR, (5 * DAYS_IN_STD_MONTH) + 29 + (2 * DAYS_IN_STD_MONTH) + 26L},

                // Year Day (2014-13-29) is a special day
                {2014, 13, 29, DAY_OF_WEEK, 0L},
                {2014, 13, 29, DAY_OF_MONTH, 29L},
                // Day of year for 2014-13-29: 12 * 28 + 29 = 365
                {2014, 13, 29, DAY_OF_YEAR, (12 * DAYS_IN_STD_MONTH) + 29L},

                // Leap Day (2012-06-29) is a special day
                {2012, 6, 29, DAY_OF_WEEK, 0L},
                {2012, 6, 29, DAY_OF_MONTH, 29L},
                // Day of year for 2012-06-29: 5 * 28 + 29 = 169
                {2012, 6, 29, DAY_OF_YEAR, (5 * DAYS_IN_STD_MONTH) + 29L},
            });
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndFieldValues")
        void getLongReturnsCorrectValueForField(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, InternationalFixedDate.of(year, month, dom).getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    // Adjustment Tests
    //-----------------------------------------------------------------------
    @Nested
    class AdjustmentTests {

        static Stream<Object[]> provideWithAdjustmentCases() {
            return Stream.of(new Object[][]{
                {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
                {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
                {2014, 5, 26, DAY_OF_YEAR, 365, 2014, 13, 29},
                {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                // Adjusting a leap day to a non-leap year
                {2012, 6, 29, YEAR, 2013, 2013, 6, 28},
                // Adjusting a day that doesn't exist in the target month
                {2014, 13, 29, MONTH_OF_YEAR, 1, 2014, 1, 28},
            });
        }

        @ParameterizedTest
        @MethodSource("provideWithAdjustmentCases")
        void withAdjustsDateCorrectly(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Object[]> provideInvalidWithAdjustmentCases() {
            return Stream.of(new Object[][]{
                {2013, 1, 1, DAY_OF_MONTH, 29},
                {2013, 1, 1, DAY_OF_YEAR, 366},
                {2012, 1, 1, DAY_OF_YEAR, 367},
                {2013, 1, 1, MONTH_OF_YEAR, 14},
                {2013, 1, 1, DAY_OF_WEEK, 0},
            });
        }

        @ParameterizedTest
        @MethodSource("provideInvalidWithAdjustmentCases")
        void withFailsForInvalidFieldValue(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dom).with(field, value));
        }

        static Stream<Object[]> provideLastDayOfMonthCases() {
            return Stream.of(new Object[][]{
                {2012, 6, 23, 2012, 6, 29}, // Leap year, month 6
                {2009, 6, 23, 2009, 6, 28}, // Common year, month 6
                {2007, 13, 23, 2007, 13, 29}, // Any year, month 13
            });
        }

        @ParameterizedTest
        @MethodSource("provideLastDayOfMonthCases")
        void withLastDayOfMonthAdjusterWorksCorrectly(int y, int m, int d, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    //-----------------------------------------------------------------------
    // Addition and Subtraction Tests
    //-----------------------------------------------------------------------
    @Nested
    class AdditionAndSubtractionTests {

        static Stream<Object[]> providePlusCases() {
            return Stream.of(new Object[][]{
                {2014, 5, 26, 8, DAYS, 2014, 6, 6},
                {2014, 5, 26, 3, WEEKS, 2014, 6, 19},
                {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
                {2014, 5, 26, 3, YEARS, 2017, 5, 26},
                {2014, 5, 26, 3, DECADES, 2044, 5, 26},
                {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
                {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
                // Across year boundary
                {2014, 13, 26, 3, WEEKS, 2015, 1, 19},
                // Across leap day
                {2012, 6, 26, 3, WEEKS, 2012, 7, 19},
                // From leap day
                {2012, 6, 29, 8, DAYS, 2012, 7, 8},
                // From year day
                {2014, 13, 29, 8, DAYS, 2015, 1, 8},
            });
        }

        @ParameterizedTest
        @MethodSource("providePlusCases")
        void plusAddsAmountToDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        static Stream<Object[]> provideMinusCases() {
            // This is the reverse of providePlusCases for better readability
            return Stream.of(new Object[][]{
                {2014, 6, 6, 8, DAYS, 2014, 5, 26},
                {2014, 6, 19, 3, WEEKS, 2014, 5, 26},
                {2014, 8, 26, 3, MONTHS, 2014, 5, 26},
                {2017, 5, 26, 3, YEARS, 2014, 5, 26},
                {2044, 5, 26, 3, DECADES, 2014, 5, 26},
                {2314, 5, 26, 3, CENTURIES, 2014, 5, 26},
                {5014, 5, 26, 3, MILLENNIA, 2014, 5, 26},
                // Across year boundary
                {2015, 1, 19, 3, WEEKS, 2014, 13, 26},
                // Across leap day
                {2012, 7, 19, 3, WEEKS, 2012, 6, 26},
                // To leap day
                {2012, 7, 8, 8, DAYS, 2012, 6, 29},
                // To year day
                {2015, 1, 8, 8, DAYS, 2014, 13, 29},
            });
        }

        @ParameterizedTest
        @MethodSource("provideMinusCases")
        void minusSubtractsAmountFromDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }

        @Test
        void plusUnsupportedPeriodUnitFails() {
            InternationalFixedDate date = InternationalFixedDate.of(2014, 5, 26);
            // Period is an ISO-specific period, cannot be added to a non-ISO date.
            assertThrows(UnsupportedTemporalTypeException.class, () -> date.plus(Period.ofMonths(2)));
        }
    }

    //-----------------------------------------------------------------------
    // Until Tests
    //-----------------------------------------------------------------------
    @Nested
    class UntilTests {

        static Stream<Object[]> provideUntilCases() {
            return Stream.of(new Object[][]{
                {2014, 5, 26, 2014, 5, 26, DAYS, 0L},
                {2014, 5, 26, 2014, 6, 4, DAYS, 6L},
                {2014, 5, 26, 2014, 5, 20, DAYS, -6L},
                {2014, 5, 26, 2014, 6, 5, WEEKS, 1L},
                {2014, 5, 26, 2014, 6, 26, MONTHS, 1L},
                {2014, 5, 26, 2015, 5, 26, YEARS, 1L},
                {2014, 5, 26, 2024, 5, 26, DECADES, 1L},
                {2014, 5, 26, 2114, 5, 26, CENTURIES, 1L},
                {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L},
                {2014, 5, 26, 3014, 5, 26, ERAS, 0L},
                // Across year-end
                {2014, 13, 28, 2015, 1, 1, DAYS, 2L},
                // Across leap day
                {2012, 6, 28, 2012, 7, 1, DAYS, 2L},
            });
        }

        @ParameterizedTest
        @MethodSource("provideUntilCases")
        void untilReturnsCorrectAmountInUnit(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Object[]> provideUntilPeriodCases() {
            return Stream.of(new Object[][]{
                {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
                {2014, 5, 26, 2014, 6, 4, 0, 0, 6},
                {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
                {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
                // Start date is a leap day
                {2008, 6, 29, 2012, 6, 29, 4, 0, 0},
                // End date is a leap day
                {2004, 6, 28, 2004, 13, 29, 0, 7, 1},
            });
        }

        @ParameterizedTest
        @MethodSource("provideUntilPeriodCases")
        void untilReturnsCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    //-----------------------------------------------------------------------
    // toString() Tests
    //-----------------------------------------------------------------------
    @Nested
    class ToStringTests {

        static Stream<Object[]> provideToStringCases() {
            return Stream.of(new Object[][]{
                {InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"},
                {InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"},
                {InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"}, // Leap Day
                {InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29"}, // Year Day
            });
        }

        @ParameterizedTest
        @MethodSource("provideToStringCases")
        void toStringReturnsCorrectFormat(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}