package org.threeten.extra.chrono;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link InternationalFixedDate} class.
 */
@DisplayName("InternationalFixedDate")
class InternationalFixedDateTest {

    //-----------------------------------------------------------------------
    // Test Data Providers
    //-----------------------------------------------------------------------

    static Object[][] sampleDatePairs() {
        return new Object[][]{
                // Common dates
                {InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)},
                {InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)},
                {InternationalFixedDate.of(1945, 10, 27), LocalDate.of(1945, 10, 6)},

                // Dates around the leap month in a non-leap year
                {InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)},
                {InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)},
                {InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)},

                // Dates around the leap month in a leap year
                {InternationalFixedDate.of(4, 6, 28), LocalDate.of(4, 6, 16)},
                {InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)}, // Leap day
                {InternationalFixedDate.of(4, 7, 1), LocalDate.of(4, 6, 18)},

                // Dates around the year-end month
                {InternationalFixedDate.of(1, 13, 28), LocalDate.of(1, 12, 30)},
                {InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)}, // Year day
                {InternationalFixedDate.of(2, 1, 1), LocalDate.of(2, 1, 1)},
        };
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Conversion")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleDatePairs")
        @DisplayName("to ISO LocalDate")
        void toIsoLocalDate_shouldConvert(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(ifcDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleDatePairs")
        @DisplayName("from ISO LocalDate")
        void fromIsoLocalDate_shouldConvert(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleDatePairs")
        @DisplayName("to Epoch Day")
        void toEpochDay_shouldBeCorrect(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), ifcDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleDatePairs")
        @DisplayName("from Epoch Day")
        void fromEpochDay_shouldBeCorrect(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleDatePairs")
        @DisplayName("from TemporalAccessor (LocalDate)")
        void fromTemporal_shouldBeCorrect(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Factory and invalid date creation")
    class FactoryAndInvalidDateTests {

        static Object[][] invalidDateComponents() {
            return new Object[][]{
                    {1900, 1, 0}, {1900, 1, 29}, // Invalid day
                    {1900, 0, 1}, {1900, 14, 1}, // Invalid month
                    {0, 1, 1},                  // Invalid year
                    {1900, 6, 29},              // Invalid leap day in non-leap year
                    {1900, 13, 30},             // Invalid year day
            };
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("invalidDateComponents")
        void of_forInvalidDateComponents_throwsException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dayOfMonth));
        }

        static int[] nonLeapYears() {
            return new int[]{1, 100, 200, 300, 1900};
        }

        @ParameterizedTest(name = "year {0}")
        @MethodSource("nonLeapYears")
        void of_forLeapDayInNonLeapYear_throwsException(int year) {
            // Month 6, Day 29 is the leap day, which is only valid in leap years.
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }
    }

    @Nested
    @DisplayName("Date properties")
    class DatePropertyTests {

        static Object[][] monthLengths() {
            return new Object[][]{
                    {1900, 1, 28},
                    {1900, 6, 28},  // Non-leap year
                    {1900, 13, 29}, // Year-end month
                    {1904, 6, 29},  // Leap year
            };
        }

        @ParameterizedTest
        @MethodSource("monthLengths")
        void lengthOfMonth_returnsCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, 1).lengthOfMonth());
        }

        static Object[][] getLongFieldSamples() {
            final long DAYS_IN_MONTH = 28;
            final long MONTHS_IN_YEAR = 13;

            return new Object[][]{
                    // Date: 2014-05-26 (non-leap year)
                    {2014, 5, 26, DAY_OF_WEEK, 5L},
                    {2014, 5, 26, DAY_OF_MONTH, 26L},
                    {2014, 5, 26, DAY_OF_YEAR, (4 * DAYS_IN_MONTH) + 26L},
                    {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L},
                    {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 20L},
                    {2014, 5, 26, MONTH_OF_YEAR, 5L},
                    {2014, 5, 26, PROLEPTIC_MONTH, (2014L - 1) * MONTHS_IN_YEAR + (5L - 1)},
                    {2014, 5, 26, YEAR, 2014L},
                    {2014, 5, 26, ERA, 1L},

                    // Date: 2012-09-26 (leap year)
                    {2012, 9, 26, DAY_OF_YEAR, (6 * DAYS_IN_MONTH) + 1L + (2 * DAYS_IN_MONTH) + 26L}, // Includes leap day
                    {2012, 9, 28, ALIGNED_WEEK_OF_YEAR, 36L},

                    // Date: 2014-13-29 (Year Day)
                    {2014, 13, 29, DAY_OF_WEEK, 0L}, // Special value for Year Day
                    {2014, 13, 29, DAY_OF_MONTH, 29L},
                    {2014, 13, 29, DAY_OF_YEAR, 12 * DAYS_IN_MONTH + 28 + 1},

                    // Date: 2012-06-29 (Leap Day)
                    {2012, 6, 29, DAY_OF_WEEK, 0L}, // Special value for Leap Day
                    {2012, 6, 29, DAY_OF_MONTH, 29L},
                    {2012, 6, 29, DAY_OF_YEAR, 5 * DAYS_IN_MONTH + 28 + 1},
            };
        }

        @ParameterizedTest(name = "{3} of {0}-{1}-{2} is {4}")
        @MethodSource("getLongFieldSamples")
        void getLong_forField_returnsCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, InternationalFixedDate.of(year, month, day).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date modification")
    class ModificationTests {

        static Object[][] withFieldSamples() {
            return new Object[][]{
                    // field, value => expected date
                    {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
                    {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
                    {2014, 5, 26, DAY_OF_YEAR, 365, 2014, 13, 29}, // Set to Year Day
                    {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
                    {2014, 5, 26, YEAR, 2012, 2012, 5, 26}, // Change to leap year
                    {2012, 6, 29, YEAR, 2013, 2013, 6, 28}, // Adjusts leap day when moving to non-leap year
                    {2012, 6, 29, YEAR, 2016, 2016, 6, 29}, // Stays leap day when moving to another leap year
            };
        }

        @ParameterizedTest(name = "with({3}, {4})")
        @MethodSource("withFieldSamples")
        void with_field_returnsModifiedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        static Object[][] plusSamples() {
            return new Object[][]{
                    // Regular additions
                    {2014, 5, 26, 8, DAYS, 2014, 6, 6},
                    {2014, 5, 26, 3, WEEKS, 2014, 6, 19},
                    {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
                    {2014, 5, 26, 3, YEARS, 2017, 5, 26},
                    // Additions crossing leap/year days
                    {2014, 13, 26, 3, WEEKS, 2015, 1, 19}, // Crosses Year Day
                    {2012, 6, 26, 3, WEEKS, 2012, 7, 19},  // Crosses Leap Day
                    // Additions starting from leap/year days
                    {2014, 13, 29, 8, DAYS, 2015, 1, 8},   // From Year Day
                    {2012, 6, 29, 8, DAYS, 2012, 7, 8},    // From Leap Day
                    {2012, 6, 29, 4, YEARS, 2016, 6, 29},  // Leap year to leap year
                    {2012, 6, 29, 3, YEARS, 2015, 6, 28},  // Leap year to non-leap year (day adjusted)
            };
        }

        @ParameterizedTest(name = "plus({3}, {4})")
        @MethodSource("plusSamples")
        void plus_addsAmountCorrectly(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "minus({3}, {4})")
        @MethodSource("plusSamples")
        void minus_subtractsAmountCorrectly(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(ey, em, ed);
            InternationalFixedDate expected = InternationalFixedDate.of(y, m, d);
            assertEquals(expected, start.minus(amount, unit));
        }

        @Test
        @DisplayName("with(lastDayOfMonth)")
        void with_lastDayOfMonth_adjustsToEndOfMonth() {
            InternationalFixedDate date = InternationalFixedDate.of(2012, 6, 23); // Leap month
            InternationalFixedDate expected = InternationalFixedDate.of(2012, 6, 29);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Period and Duration")
    class PeriodAndDurationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedDateTest#sampleDatePairs")
        @DisplayName("until a date in a different chronology (LocalDate)")
        void until_differentChronology_isZero(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(ifcDate));
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(isoDate));
        }

        static Object[][] untilInUnitSamples() {
            return new Object[][]{
                    {2014, 5, 26, 2014, 6, 4, DAYS, 6L},
                    {2014, 5, 26, 2014, 5, 20, DAYS, -6L},
                    {2014, 5, 26, 2014, 6, 5, WEEKS, 1L},
                    {2014, 5, 26, 2014, 6, 26, MONTHS, 1L},
                    {2014, 5, 26, 2015, 5, 26, YEARS, 1L},
                    // Spanning Year Day
                    {2014, 13, 28, 2015, 1, 1, DAYS, 2L},
                    // Spanning Leap Day
                    {2012, 6, 28, 2012, 7, 1, DAYS, 2L},
            };
        }

        @ParameterizedTest(name = "from {0}-{1}-{2} to {3}-{4}-{5} in {6}")
        @MethodSource("untilInUnitSamples")
        void until_inTemporalUnit_calculatesAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Object[][] untilAsPeriodSamples() {
            return new Object[][]{
                    {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
                    {2014, 5, 26, 2014, 6, 4, 0, 0, 6},
                    {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
                    {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
                    // Across leap year boundary
                    {2011, 12, 28, 2012, 13, 1, 1, 0, 1},
                    // From leap day
                    {2008, 6, 29, 2012, 6, 29, 4, 0, 0},
                    // From leap day to year day in same year
                    {2004, 6, 29, 2004, 13, 29, 0, 7, 0},
            };
        }

        @ParameterizedTest(name = "from {0}-{1}-{2} to {3}-{4}-{5}")
        @MethodSource("untilAsPeriodSamples")
        void until_asChronoPeriod_calculatesPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Object methods")
    class ObjectMethodTests {

        static Object[][] toStringSamples() {
            return new Object[][]{
                    {InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"},
                    {InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"},
                    {InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"}, // Leap Day
                    {InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29"}, // Year Day
            };
        }

        @ParameterizedTest
        @MethodSource("toStringSamples")
        void test_toString(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }

        @Test
        void equals_and_hashCode_followContract() {
            new EqualsTester()
                    .addEqualityGroup(InternationalFixedDate.of(2000, 1, 3), InternationalFixedDate.of(2000, 1, 3))
                    .addEqualityGroup(InternationalFixedDate.of(2000, 1, 4))
                    .addEqualityGroup(InternationalFixedDate.of(2000, 2, 3))
                    .addEqualityGroup(InternationalFixedDate.of(2001, 1, 3))
                    .addEqualityGroup(InternationalFixedDate.of(2000, 6, 29)) // Leap Day
                    .addEqualityGroup(InternationalFixedDate.of(2000, 13, 29)) // Year Day
                    .testEquals();
        }
    }
}