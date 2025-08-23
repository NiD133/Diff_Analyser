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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link InternationalFixedChronology} and {@link InternationalFixedDate}.
 */
class InternationalFixedChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Object[][] sampleDatePairs() {
        return new Object[][] {
            {InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {InternationalFixedDate.of(1, 1, 2), LocalDate.of(1, 1, 2)},
            // Non-leap year, around where a leap day would be
            {InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)},
            {InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)},
            {InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)},
            // Year Day (month 13, day 29)
            {InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)},
            // Leap year, around the leap day (month 6, day 29)
            {InternationalFixedDate.of(4, 6, 28), LocalDate.of(4, 6, 16)},
            {InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)}, // The leap day
            {InternationalFixedDate.of(4, 7, 1), LocalDate.of(4, 6, 18)},
            // Modern dates
            {InternationalFixedDate.of(1945, 10, 27), LocalDate.of(1945, 10, 6)},
            {InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)},
            {InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4)},
        };
    }

    static Object[][] invalidDateComponents() {
        return new Object[][] {
            {-1, 13, 28}, {-1, 13, 29}, {0, 1, 1},
            {1900, -2, 1}, {1900, 14, 1}, {1900, 15, 1},
            {1900, 1, -1}, {1900, 1, 0}, {1900, 1, 29}, // Month 1 has 28 days
            {1900, 2, 29}, // Month 2 has 28 days
            {1900, 13, 30}, // Month 13 has 29 days
        };
    }

    static Object[][] nonLeapYears() {
        return new Object[][] {{1}, {100}, {200}, {300}, {1900}};
    }

    static Object[][] monthLengths() {
        return new Object[][] {
            // Non-leap year: all months 28 days, except month 13 (Year Day) is 29
            {1900, 1, 28, 28},
            {1900, 6, 28, 28},
            {1900, 12, 28, 28},
            {1900, 13, 29, 29},
            // Leap year: month 6 (Leap Day) and 13 (Year Day) have 29 days
            {1904, 6, 29, 29},
        };
    }

    static Object[][] fieldRanges() {
        return new Object[][] {
            // In a leap year, month 6 (with leap day) has 29 days
            {2012, 6, 29, DAY_OF_MONTH, ValueRange.of(1, 29)},
            // In a leap year, month 13 (with year day) has 29 days
            {2012, 13, 29, DAY_OF_MONTH, ValueRange.of(1, 29)},
            // A standard month has 28 days
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            // A leap year has 366 days
            {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366)},
            // A non-leap year has 365 days
            {2011, 13, 23, DAY_OF_YEAR, ValueRange.of(1, 365)},
            // All years have 13 months
            {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)},
            // Leap Day and Year Day are special, not part of the weekly cycle
            {2012, 6, 29, DAY_OF_WEEK, ValueRange.of(0, 0)},
            {2012, 13, 29, DAY_OF_WEEK, ValueRange.of(0, 0)},
            // Regular days are part of the 7-day week
            {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
        };
    }

    static Object[][] dateFields() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 5},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, (4 * 28) + 26},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 20},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            // Proleptic month is (year * 13) + month - 1, assuming year 1/month 1 is 13
            {2014, 5, 26, PROLEPTIC_MONTH, (2014L * 13) + 5 - 1},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},
            // Leap year calculations
            {2012, 9, 26, DAY_OF_YEAR, (8 * 28) + 1 + 26}, // +1 for leap day in month 6
            // Year Day (month 13, day 29)
            {2014, 13, 29, DAY_OF_WEEK, 0}, // Not part of a week
            {2014, 13, 29, DAY_OF_MONTH, 29},
            {2014, 13, 29, DAY_OF_YEAR, (12 * 28) + 28 + 1}, // 365th day
            {2012, 13, 29, DAY_OF_YEAR, (12 * 28) + 28 + 1 + 1}, // 366th day in a leap year
            // Leap Day (month 6, day 29)
            {2012, 6, 29, DAY_OF_WEEK, 0}, // Not part of a week
            {2012, 6, 29, DAY_OF_MONTH, 29},
            {2012, 6, 29, DAY_OF_YEAR, (5 * 28) + 28 + 1},
        };
    }

    static Object[][] withFieldAdjustments() {
        return new Object[][] {
            // Field, value to set, expected date
            {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
            {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 13, 28},
            {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            // Adjusting a leap day to a non-leap year moves it to the 28th
            {2012, 6, 29, YEAR, 2013, 2013, 6, 28},
            // Adjusting a regular day in a leap year to another leap year keeps the day
            {2012, 6, 22, YEAR, 2016, 2016, 6, 22},
            // Adjusting day of month to 29 in a month that supports it
            {2012, 6, 22, DAY_OF_MONTH, 29, 2012, 6, 29},
            // Adjusting month of year from a 29-day month to a 28-day month
            {2014, 13, 29, MONTH_OF_YEAR, 1, 2014, 1, 28},
        };
    }

    static Object[][] plusAmountCases() {
        return new Object[][] {
            // Base Date, Amount, Unit, Expected Date
            {2014, 5, 26, 8, DAYS, 2014, 6, 6},
            {2014, 5, 26, -3, DAYS, 2014, 5, 23},
            {2014, 5, 26, 3, WEEKS, 2014, 6, 19},
            {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
            {2014, 5, 26, -5, MONTHS, 2013, 13, 26},
            {2014, 5, 26, 3, YEARS, 2017, 5, 26},
            {2014, 5, 26, 3, DECADES, 2044, 5, 26},
            {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
            {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
        };
    }

    static Object[][] plusAmountCasesForSpecialDays() {
        return new Object[][] {
            // Year Day (non-leap year)
            {2014, 13, 29, 8, DAYS, 2015, 1, 8},
            {2014, 13, 29, 3, MONTHS, 2015, 3, 28}, // Adding months from Year Day lands on 28th
            {2014, 13, 29, 1, YEARS, 2015, 13, 29},
            // Leap Day
            {2012, 6, 29, 8, DAYS, 2012, 7, 8},
            {2012, 6, 29, 3, MONTHS, 2012, 9, 28}, // Adding months from Leap Day lands on 28th
            {2012, 6, 29, 1, YEARS, 2013, 6, 28}, // Adding 1 year from Leap Day lands on 28th
            {2012, 6, 29, 4, YEARS, 2016, 6, 29}, // Adding 4 years lands on the next Leap Day
        };
    }

    static Object[][] untilPeriodCases() {
        return new Object[][] {
            // Start Date, End Date, Expected Period (Y, M, D)
            {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
            {2014, 5, 26, 2014, 6, 4, 0, 0, 6},
            {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
            {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
            // Spanning leap day
            {2011, 12, 28, 2012, 13, 1, 1, 0, 1},
            // From Year Day
            {2003, 13, 29, 2004, 6, 29, 0, 6, 0}, // To Leap Day
            {2003, 13, 29, 2004, 7, 1, 0, 6, 1},
            // From Leap Day
            {2004, 6, 29, 2004, 13, 29, 0, 7, 0}, // To Year Day
        };
    }

    @Nested
    @DisplayName("Conversion Tests")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairs")
        void fromInternationalFixedDate_shouldReturnCorrespondingIsoDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(ifcDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairs")
        void fromIsoLocalDate_shouldReturnCorrespondingInternationalFixedDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairs")
        void toEpochDay_shouldReturnCorrectValue(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), ifcDate.toEpochDay());
        }
    }

    @Nested
    @DisplayName("Chronology API Tests")
    class ChronologyApiTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairs")
        void chronologyDateFromEpochDay_shouldReturnCorrectDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairs")
        void chronologyDateFromTemporal_shouldReturnCorrectDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }

        @Test
        void eraOf_withInvalidValue_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(-1));
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(0));
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(2));
        }

        @Test
        void prolepticYear_withInvalidYearForEra_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 0));
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, -1));
        }
    }

    @Nested
    @DisplayName("Factory and Validation Tests")
    class FactoryTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#invalidDateComponents")
        void of_withInvalidDateComponents_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dayOfMonth));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#nonLeapYears")
        void of_withLeapDayInNonLeapYear_shouldThrowException(int year) {
            // Month 6, day 29 is the leap day, which is invalid in a non-leap year.
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }
    }

    @Nested
    @DisplayName("Field Accessor Tests")
    class FieldTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#monthLengths")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, day).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#fieldRanges")
        void range_forField_shouldReturnCorrectRange(int year, int month, int dayOfMonth, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, InternationalFixedDate.of(year, month, dayOfMonth).range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#dateFields")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int dayOfMonth, TemporalField field, long expected) {
            assertEquals(expected, InternationalFixedDate.of(year, month, dayOfMonth).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation Tests")
    class ManipulationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairs")
        void plusDays_shouldMatchIsoDatePlusDays(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(ifcDate.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(ifcDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(ifcDate.plus(35, DAYS)));

            // Avoid underflowing year to 0 or negative, which is not supported by this chronology.
            if (isoDate.isAfter(LocalDate.of(1, 2, 28))) { // Day 59
                assertEquals(isoDate.plusDays(-1), LocalDate.from(ifcDate.plus(-1, DAYS)));
                assertEquals(isoDate.plusDays(-60), LocalDate.from(ifcDate.plus(-60, DAYS)));
            }
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairs")
        void minusDays_shouldMatchIsoDateMinusDays(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(ifcDate.minus(0, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(ifcDate.minus(-1, DAYS)));

            // Avoid underflowing year to 0 or negative.
            if (isoDate.isAfter(LocalDate.of(1, 2, 4))) { // Day 35
                assertEquals(isoDate.minusDays(1), LocalDate.from(ifcDate.minus(1, DAYS)));
                assertEquals(isoDate.minusDays(35), LocalDate.from(ifcDate.minus(35, DAYS)));
            }
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#withFieldAdjustments")
        void with_fieldAndValue_shouldReturnAdjustedDate(
                int year, int month, int dom, TemporalField field, long value,
                int expectedYear, int expectedMonth, int expectedDom) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, dom);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.with(field, value));
        }

        @Test
        void with_lastDayOfMonthAdjuster_shouldReturnLastDayOfMonth() {
            InternationalFixedDate date = InternationalFixedDate.of(2012, 6, 23); // Leap year, month 6
            InternationalFixedDate expected = InternationalFixedDate.of(2012, 6, 29);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#plusAmountCases")
        void plus_amountAndUnit_shouldReturnCorrectlyAddedDate(
                int year, int month, int dom, long amount, TemporalUnit unit,
                int expectedYear, int expectedMonth, int expectedDom) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, dom);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#plusAmountCasesForSpecialDays")
        void plus_onSpecialDays_shouldReturnCorrectlyAddedDate(
                int year, int month, int dom, long amount, TemporalUnit unit,
                int expectedYear, int expectedMonth, int expectedDom) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, dom);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }
    }

    @Nested
    @DisplayName("Period and Until Tests")
    class UntilTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairs")
        void until_sameDate_returnsZeroPeriod(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(ifcDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairs")
        void until_equivalentIsoDate_returnsZeroPeriod(InternationalFixedDate ifcDate, LocalDate isoDate) {
            // Check that until() works correctly with a different Chronology's date
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairs")
        void isoDateUntilEquivalentIfcDate_returnsZeroPeriod(InternationalFixedDate ifcDate, LocalDate isoDate) {
            // Check that ISO LocalDate.until() works correctly with an IfcDate
            assertEquals(Period.ZERO, isoDate.until(ifcDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#untilPeriodCases")
        void until_endDate_shouldReturnCorrectPeriod(
                int year1, int month1, int dom1, int year2, int month2, int dom2,
                int expectedYears, int expectedMonths, int expectedDays) {
            InternationalFixedDate start = InternationalFixedDate.of(year1, month1, dom1);
            InternationalFixedDate end = InternationalFixedDate.of(year2, month2, dom2);
            ChronoPeriod expectedPeriod = InternationalFixedChronology.INSTANCE.period(expectedYears, expectedMonths, expectedDays);
            assertEquals(expectedPeriod, start.until(end));
        }
    }

    @Nested
    @DisplayName("toString Tests")
    class ToStringTests {
        @Test
        void toString_shouldReturnCorrectFormatting() {
            assertEquals("Ifc CE 1/01/01", InternationalFixedDate.of(1, 1, 1).toString());
            assertEquals("Ifc CE 2012/06/23", InternationalFixedDate.of(2012, 6, 23).toString());
            assertEquals("Ifc CE 2012/06/29", InternationalFixedDate.of(2012, 6, 29).toString()); // Leap Day
            assertEquals("Ifc CE 2012/13/29", InternationalFixedDate.of(2012, 13, 29).toString()); // Year Day
        }
    }
}