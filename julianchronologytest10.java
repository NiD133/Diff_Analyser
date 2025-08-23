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
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the behavior of {@link JulianDate}.
 * This class covers date creation, validation, conversions, field access, and arithmetic.
 */
public class JulianDateTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample Julian dates and their equivalent ISO dates.
     */
    public static Object[][] julianAndIsoDateProvider() {
        return new Object[][] {
            {JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
            {JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
            {JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)},
            {JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)},
            {JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)},
            {JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)},
            {JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
            {JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)},
            {JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6)}
        };
    }

    /**
     * Provides invalid date components for testing exceptions.
     */
    public static Object[][] invalidDateProvider() {
        return new Object[][] {
            {1900, 0, 1}, {1900, 13, 1}, {1900, 1, 0}, {1900, 1, 32},
            {1900, 2, 30}, // Leap in Julian, but not in Gregorian
            {1899, 2, 29}, // Not a leap year
            {1900, 4, 31}
        };
    }

    /**
     * Provides year, month, and expected length of the month.
     */
    public static Object[][] monthLengthProvider() {
        return new Object[][] {
            {1900, 1, 31}, {1900, 2, 29}, {1900, 3, 31}, {1900, 4, 30},
            {1901, 2, 28}, {1904, 2, 29}, {2000, 2, 29}, {2100, 2, 29}
        };
    }

    /**
     * Provides dates, fields, and expected value ranges.
     */
    public static Object[][] fieldRangeProvider() {
        return new Object[][] {
            {2012, 1, 23, DAY_OF_MONTH, 1, 31},
            {2012, 2, 23, DAY_OF_MONTH, 1, 29}, // Leap year
            {2011, 2, 23, DAY_OF_MONTH, 1, 28}, // Non-leap year
            {2012, 1, 23, DAY_OF_YEAR, 1, 366},
            {2011, 1, 23, DAY_OF_YEAR, 1, 365},
            {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5},
            {2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4}
        };
    }

    /**
     * Provides dates, fields, and expected long values for those fields.
     */
    public static Object[][] fieldValueProvider() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 7},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            // Day of year for Julian May 26, 2014 is 31(Jan)+28(Feb)+31(Mar)+30(Apr)+26 = 146
            {2014, 5, 26, DAY_OF_YEAR, 146},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12L + 5 - 1},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},
            {0, 6, 8, ERA, 0},
            {2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7}
        };
    }

    /**
     * Provides dates, fields to adjust, new values, and expected resulting dates.
     */
    public static Object[][] dateWithFieldAdjustmentProvider() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31},
            {2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9},
            {2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26},
            {2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12L + 3 - 1, 2013, 3, 26},
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            {2014, 5, 26, ERA, 0, -2013, 5, 26},
            {2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29}, // Adjust to shorter month in leap year
            {2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28}, // Adjust to shorter month in non-leap year
            {2012, 2, 29, YEAR, 2011, 2011, 2, 28} // Adjust leap day to non-leap year
        };
    }

    /**
     * Provides start dates, amounts/units to add, and expected resulting dates.
     */
    public static Object[][] datePlusAmountProvider() {
        return new Object[][] {
            {2014, 5, 26, 8, DAYS, 2014, 6, 3},
            {2014, 5, 26, -3, DAYS, 2014, 5, 23},
            {2014, 5, 26, 3, WEEKS, 2014, 6, 16},
            {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
            {2014, 5, 26, 3, YEARS, 2017, 5, 26},
            {2014, 5, 26, 3, DECADES, 2044, 5, 26},
            {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
            {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
            {2014, 5, 26, -1, ERAS, -2013, 5, 26}
        };
    }

    /**
     * Provides start/end dates, units, and the expected duration.
     */
    public static Object[][] dateUntilProvider() {
        return new Object[][] {
            {2014, 5, 26, 2014, 6, 1, DAYS, 6},
            {2014, 5, 26, 2014, 5, 20, DAYS, -6},
            {2014, 5, 26, 2014, 6, 2, WEEKS, 1},
            {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
            {2014, 5, 26, 2015, 5, 26, YEARS, 1},
            {2014, 5, 26, 2024, 5, 26, DECADES, 1},
            {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
            {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
            {-2013, 5, 26, 2014, 5, 26, ERAS, 1}
        };
    }

    /**
     * Provides dates and their expected string representations.
     */
    public static Object[][] toStringProvider() {
        return new Object[][] {
            {JulianDate.of(1, 1, 1), "Julian AD 1-01-01"},
            {JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23"}
        };
    }

    @Nested
    class ConversionAndEpochTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#julianAndIsoDateProvider")
        void conversionsBetweenJulianAndIsoAreConsistent(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(julianDate));
            assertEquals(julianDate, JulianDate.from(isoDate));
            assertEquals(isoDate.toEpochDay(), julianDate.toEpochDay());
            assertEquals(julianDate, JulianChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
            assertEquals(julianDate, JulianChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    class FactoryAndValidationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#invalidDateProvider")
        void of_withInvalidDateParts_throwsDateTimeException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dayOfMonth));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#monthLengthProvider")
        void lengthOfMonth_returnsCorrectDayCount(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }
    }



    @Nested
    class FieldAccessTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#fieldRangeProvider")
        void range_forSupportedFields_returnsCorrectRange(int year, int month, int dayOfMonth, TemporalField field, int expectedMin, int expectedMax) {
            JulianDate date = JulianDate.of(year, month, dayOfMonth);
            assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#fieldValueProvider")
        void getLong_forSupportedFields_returnsCorrectValue(int year, int month, int dayOfMonth, TemporalField field, long expected) {
            JulianDate date = JulianDate.of(year, month, dayOfMonth);
            assertEquals(expected, date.getLong(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#dateWithFieldAdjustmentProvider")
        void with_forSupportedFields_returnsAdjustedDate(int year, int month, int dayOfMonth, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDayOfMonth) {
            JulianDate start = JulianDate.of(year, month, dayOfMonth);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDayOfMonth);
            assertEquals(expected, start.with(field, value));
        }
    }

    @Nested
    class ArithmeticTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#datePlusAmountProvider")
        void plus_forVariousUnits_returnsAdjustedDate(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDay) {
            JulianDate start = JulianDate.of(startYear, startMonth, startDay);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#datePlusAmountProvider")
        void minus_forVariousUnits_returnsAdjustedDate(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            JulianDate startDate = JulianDate.of(startYear, startMonth, startDay);
            JulianDate endDate = JulianDate.of(endYear, endMonth, endDay);
            assertEquals(startDate, endDate.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#dateUntilProvider")
        void until_forVariousUnits_calculatesAmountCorrectly(int year1, int month1, int day1, int year2, int month2, int day2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(year1, month1, day1);
            JulianDate end = JulianDate.of(year2, month2, day2);
            assertEquals(expected, start.until(end, unit));
        }

        @Test
        void until_sameDateInstance_returnsZeroPeriod() {
            JulianDate date = JulianDate.of(2012, 6, 23);
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        @Test
        void until_equivalentIsoDate_returnsZeroPeriod() {
            JulianDate julianDate = JulianDate.of(1582, 10, 5);
            LocalDate isoDate = LocalDate.of(1582, 10, 15); // Equivalent date
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julianDate.until(isoDate));
        }

        @Test
        void isoUntil_equivalentJulianDate_returnsZeroPeriod() {
            JulianDate julianDate = JulianDate.of(1582, 10, 5);
            LocalDate isoDate = LocalDate.of(1582, 10, 15); // Equivalent date
            assertEquals(Period.ZERO, isoDate.until(julianDate));
        }
    }

    @Nested
    class GeneralMethodTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#toStringProvider")
        void toString_returnsCorrectlyFormattedString(JulianDate julian, String expected) {
            assertEquals(expected, julian.toString());
        }

        @Test
        void chronologyEraOf_returnsCorrectEra() {
            assertEquals(JulianEra.AD, JulianChronology.INSTANCE.eraOf(1));
            assertEquals(JulianEra.BC, JulianChronology.INSTANCE.eraOf(0));
        }
    }
}