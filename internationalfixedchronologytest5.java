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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link InternationalFixedChronology} and {@link InternationalFixedDate}.
 *
 * <p>The International Fixed Calendar, as interpreted by these tests, has the following structure:
 * <ul>
 *   <li>13 months per year.</li>
 *   <li>Months 1-12 have 28 days.</li>
 *   <li>Month 6 has a 29th day (the "Leap Day") in a leap year.</li>
 *   <li>Month 13 has a 29th day (the "Year Day") in every year.</li>
 *   <li>The leap year rule is the same as the Gregorian calendar.</li>
 * </ul>
 */
public class InternationalFixedChronologyTest {

    private static final int IFC_DAYS_IN_MONTH = 28;
    private static final int IFC_MONTHS_IN_YEAR = 13;

    // --- Data Providers ---

    /**
     * Provides sample pairs of equivalent dates in International Fixed and ISO (Gregorian) calendars.
     */
    public static Object[][] provideSampleDates() {
        return new Object[][] {
            {InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)},
            {InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)},
            {InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)}, // Year Day
            {InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)},   // Leap Day
            {InternationalFixedDate.of(4, 7, 1), LocalDate.of(4, 6, 18)},
            {InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)},
            {InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4)},
        };
    }

    // --- Test Nests ---

    @Nested
    @DisplayName("Creation and Conversion")
    class CreationAndConversion {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#provideSampleDates")
        void test_from_InternationalFixedDate_to_LocalDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(ifcDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#provideSampleDates")
        void test_from_LocalDate_to_InternationalFixedDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#provideSampleDates")
        void test_toEpochDay_matchesIsoDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), ifcDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#provideSampleDates")
        void test_chronology_dateEpochDay_recreatesSameDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#provideSampleDates")
        void test_chronology_date_fromIsoDate_createsEquivalentFixedDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }

        public static Object[][] provideInvalidDateComponents() {
            return new Object[][] {
                // Invalid year
                {-1, 13, 28}, {0, 1, 1},
                // Invalid month
                {1900, -2, 1}, {1900, 0, 1}, {1900, 14, 1}, {1900, 15, 1},
                // Invalid day of month
                {1900, 1, -1}, {1900, 1, 0},
                {1900, 1, 29},   // Month 1 has 28 days
                {1900, 2, 29},   // Month 2 has 28 days
                {1900, 6, 29},   // Month 6 has 29 days only in leap years (1900 is not)
                {1900, 13, 30},  // Month 13 has 29 days
            };
        }

        @ParameterizedTest
        @MethodSource("provideInvalidDateComponents")
        void test_of_throwsForInvalidDateComponents(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        public static Object[][] provideNonLeapYears() {
            return new Object[][] {{1}, {100}, {200}, {300}, {1900}};
        }

        @ParameterizedTest
        @MethodSource("provideNonLeapYears")
        void test_of_throwsForLeapDayInNonLeapYear(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }
    }

    @Nested
    @DisplayName("Field and Property Access")
    class FieldAndPropertyAccess {

        public static Object[][] provideDatesForMonthLengthTest() {
            return new Object[][] {
                {1900, 1, 28}, {1900, 2, 28}, {1900, 6, 28}, {1900, 12, 28},
                {1900, 13, 29}, // Year Day month
                {1904, 6, 29},  // Leap Day month
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesForMonthLengthTest")
        void test_lengthOfMonth_isCorrect(int year, int month, int expectedLength) {
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, 1).lengthOfMonth());
        }

        public static Object[][] provideDatesAndFieldsForRangeTest() {
            return new Object[][] {
                // For a leap year (2012)
                {2012, 6, 29, DAY_OF_MONTH, ValueRange.of(1, 29)},
                {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
                {2012, 13, 23, DAY_OF_MONTH, ValueRange.of(1, 29)},
                {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366)},
                {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)},
                // For a non-leap year (2011)
                {2011, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
                {2011, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 365)},
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndFieldsForRangeTest")
        void test_range_returnsCorrectRangeForField(int year, int month, int day, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, InternationalFixedDate.of(year, month, day).range(field));
        }

        public static Object[][] provideDatesAndFieldsForGetLongTest() {
            return new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 5L},
                {2014, 5, 26, DAY_OF_MONTH, 26L},
                {2014, 5, 26, DAY_OF_YEAR, (long) 4 * IFC_DAYS_IN_MONTH + 26},
                {2014, 5, 26, MONTH_OF_YEAR, 5L},
                {2014, 5, 26, PROLEPTIC_MONTH, (long) 2014 * IFC_MONTHS_IN_YEAR + 5 - 1},
                {2014, 5, 26, YEAR, 2014L},
                {2014, 5, 26, ERA, 1L},
                // Leap year (2012)
                {2012, 9, 26, DAY_OF_YEAR, (long) (9 - 1) * IFC_DAYS_IN_MONTH + 26 + 1}, // +1 for leap day
                // Special "Year Day"
                {2014, 13, 29, DAY_OF_WEEK, 0L},
                {2014, 13, 29, DAY_OF_YEAR, (long) IFC_MONTHS_IN_YEAR * IFC_DAYS_IN_MONTH + 1},
                // Special "Leap Day"
                {2012, 6, 29, DAY_OF_WEEK, 0L},
                {2012, 6, 29, DAY_OF_YEAR, (long) 6 * IFC_DAYS_IN_MONTH + 1},
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndFieldsForGetLongTest")
        void test_getLong_returnsCorrectValueForField(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, InternationalFixedDate.of(year, month, day).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Adjustment")
    class DateAdjustment {

        public static Object[][] provideDatesForWithAdjustmentTest() {
            return new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
                {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
                {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 13, 28},
                {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                // Adjusting a leap day
                {2012, 6, 29, DAY_OF_MONTH, 1, 2012, 6, 1},
                // Adjusting year of a leap day to a non-leap year
                {2012, 6, 29, YEAR, 2013, 2013, 6, 28},
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesForWithAdjustmentTest")
        void test_with_returnsAdjustedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            InternationalFixedDate initial = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, initial.with(field, value));
        }

        public static Object[][] provideInvalidWithAdjustmentArguments() {
            return new Object[][] {
                {2013, 1, 1, DAY_OF_MONTH, 29},
                {2012, 6, 1, DAY_OF_MONTH, 30},
                {2013, 1, 1, DAY_OF_YEAR, 366},
                {2012, 1, 1, DAY_OF_YEAR, 367},
                {2013, 1, 1, MONTH_OF_YEAR, 14},
            };
        }

        @ParameterizedTest
        @MethodSource("provideInvalidWithAdjustmentArguments")
        void test_with_throwsForInvalidFieldValue(int y, int m, int d, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(y, m, d).with(field, value));
        }

        public static Object[][] provideDatesForLastDayOfMonthAdjustment() {
            return new Object[][] {
                {2012, 6, 23, 2012, 6, 29}, // Leap month
                {2009, 6, 23, 2009, 6, 28}, // Non-leap month
                {2007, 13, 23, 2007, 13, 29},// Year-end month
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesForLastDayOfMonthAdjustment")
        void test_with_lastDayOfMonth_adjustsToEndOfMonth(int y, int m, int d, int ey, int em, int ed) {
            InternationalFixedDate initial = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, initial.with(TemporalAdjusters.lastDayOfMonth()));
        }

        public static Object[][] provideDatesForPlusOperation() {
            return new Object[][] {
                {2014, 5, 26, 8, DAYS, 2014, 6, 6},
                {2014, 5, 26, -3, DAYS, 2014, 5, 23},
                {2014, 5, 26, 3, WEEKS, 2014, 6, 19},
                {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
                {2014, 5, 26, 3, YEARS, 2017, 5, 26},
                {2014, 5, 26, 3, DECADES, 2044, 5, 26},
                {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
                {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesForPlusOperation")
        void test_plus_addsAmountCorrectly(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate initial = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, initial.plus(amount, unit));
        }

        /**
         * The reverse of the plus operation tests minus. (start + amount = end) implies (end - amount = start).
         */
        @ParameterizedTest
        @MethodSource("provideDatesForPlusOperation")
        void test_minus_subtractsAmountCorrectly(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            InternationalFixedDate initial = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, initial.minus(amount, unit));
        }

        public static Object[][] provideSpecialDatesForPlusOperation() {
            return new Object[][] {
                // From Year Day
                {2014, 13, 29, 8, DAYS, 2015, 1, 8},
                {2014, 13, 29, 3, MONTHS, 2015, 3, 28}, // Day adjusts from 29 to 28
                {2014, 13, 29, 1, YEARS, 2015, 13, 29},
                // From Leap Day
                {2012, 6, 29, 8, DAYS, 2012, 7, 8},
                {2012, 6, 29, 3, MONTHS, 2012, 9, 28}, // Day adjusts from 29 to 28
                {2012, 6, 29, 1, YEARS, 2013, 6, 28},  // Adjusts to non-leap year
                {2012, 6, 29, 4, YEARS, 2016, 6, 29},  // Adjusts to another leap year
            };
        }

        @ParameterizedTest
        @MethodSource("provideSpecialDatesForPlusOperation")
        void test_plus_handlesSpecialDaysCorrectly(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate initial = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, initial.plus(amount, unit));
        }
    }

    @Nested
    @DisplayName("Period and Duration")
    class PeriodAndDuration {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#provideSampleDates")
        void test_until_self_returnsZeroPeriod(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(ifcDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#provideSampleDates")
        void test_until_equivalentIsoDate_returnsZeroPeriod(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#provideSampleDates")
        void test_iso_until_equivalentFixedDate_returnsZeroPeriod(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(ifcDate));
        }

        public static Object[][] provideDatePairsForUntil() {
            return new Object[][] {
                {2014, 5, 26, 2014, 6, 4, DAYS, 6},
                {2014, 5, 26, 2014, 6, 5, WEEKS, 1},
                {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
                {2014, 5, 26, 2015, 5, 26, YEARS, 1},
                {2014, 5, 26, 2024, 5, 26, DECADES, 1},
                {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
                {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
                {2014, 5, 26, 3014, 5, 26, ERAS, 0},
                // Spanning special days
                {2014, 13, 28, 2015, 1, 1, DAYS, 2},
                {2012, 6, 28, 2012, 7, 1, DAYS, 2},
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatePairsForUntil")
        void test_until_calculatesAmountBetweenDates(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        public static Object[][] provideDatePairsForUntilPeriod() {
            return new Object[][] {
                {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
                {2014, 5, 26, 2014, 6, 4, 0, 0, 6},
                {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
                {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
                // Spanning leap/year days
                {2003, 13, 29, 2004, 6, 29, 0, 6, 0}, // YearDay to LeapDay
                {2004, 6, 29, 2004, 13, 29, 0, 7, 0}, // LeapDay to YearDay
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatePairsForUntilPeriod")
        void test_until_returnsCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApi {

        public static Object[][] provideInvalidEraValues() {
            return new Object[][] {{-1}, {0}, {2}};
        }

        @ParameterizedTest
        @MethodSource("provideInvalidEraValues")
        void test_eraOf_throwsForInvalidEraValue(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        public static Object[][] provideInvalidProlepticYears() {
            return new Object[][] {{-10}, {-1}, {0}};
        }

        @ParameterizedTest
        @MethodSource("provideInvalidProlepticYears")
        void test_prolepticYear_throwsForInvalidYearOfEra(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }
    }

    @Nested
    @DisplayName("General Methods")
    class GeneralMethods {

        public static Object[][] provideDatesForToStringTest() {
            return new Object[][] {
                {InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"},
                {InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"},
                {InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"}, // Leap Day
                {InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29"},// Year Day
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesForToStringTest")
        void test_toString_returnsCorrectFormat(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}