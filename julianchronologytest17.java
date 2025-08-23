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
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("JulianDate")
class JulianDateTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Object[]> julianAndIsoDateProvider() {
        return Stream.of(new Object[][] {
            // Start of Julian calendar, equivalent to Dec 30, 0000 in proleptic ISO
            {JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
            // Day after start of Julian calendar
            {JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)},
            // Jan 1, 1 AD in ISO
            {JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
            // Around a non-leap year in Julian (year 1)
            {JulianDate.of(1, 2, 28), LocalDate.of(1, 2, 26)},
            {JulianDate.of(1, 3, 1), LocalDate.of(1, 2, 27)},
            // Around a leap year in Julian (year 4)
            {JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)},
            {JulianDate.of(4, 3, 1), LocalDate.of(4, 2, 28)},
            // Around a Julian leap year (100) that is not a Gregorian leap year
            {JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)},
            {JulianDate.of(100, 3, 1), LocalDate.of(100, 2, 28)},
            // Dates in year 0
            {JulianDate.of(0, 12, 31), LocalDate.of(0, 12, 29)},
            // Near the Gregorian calendar reform
            {JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
            {JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)},
            // Modern dates
            {JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)},
            {JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6)}
        });
    }

    static Stream<Object[]> invalidDateProvider() {
        return Stream.of(new Object[][]{
            {1900, 0, 0}, {1900, -1, 1}, {1900, 0, 1}, {1900, 13, 1}, {1900, 1, 0},
            {1900, 1, 32}, {1900, 2, 30}, // 1900 is a leap year in Julian, so Feb has 29 days
            {1899, 2, 29}, // 1899 is not a leap year in Julian
            {1900, 4, 31}, {1900, 6, 31}, {1900, 9, 31}, {1900, 11, 31}
        });
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Conversion and Factory Methods")
    class ConversionAndFactoryTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#julianAndIsoDateProvider")
        @DisplayName("should convert from JulianDate to LocalDate")
        void julianDateCanBeConvertedToLocalDate(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#julianAndIsoDateProvider")
        @DisplayName("should create JulianDate from LocalDate")
        void julianDateCanBeCreatedFromLocalDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#julianAndIsoDateProvider")
        @DisplayName("should create JulianDate from epoch day")
        void julianDateCanBeCreatedFromEpochDay(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#julianAndIsoDateProvider")
        @DisplayName("toEpochDay should return the correct value")
        void toEpochDay_returnsCorrectValue(JulianDate julian, LocalDate iso) {
            assertEquals(iso.toEpochDay(), julian.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#julianAndIsoDateProvider")
        @DisplayName("should create JulianDate from a temporal accessor")
        void julianChronologyCanCreateDateFromTemporalAccessor(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.date(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#invalidDateProvider")
        @DisplayName("of() should throw DateTimeException for invalid date values")
        void of_forInvalidDate_throwsDateTimeException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dom));
        }
    }

    @Nested
    @DisplayName("Date Properties")
    class DatePropertiesTests {

        static Stream<Object[]> lengthOfMonthProvider() {
            return Stream.of(new Object[][]{
                {1900, 1, 31}, {1900, 2, 29}, {1900, 3, 31}, {1900, 4, 30}, {1900, 12, 31},
                {1901, 2, 28}, {1903, 2, 28},
                {1904, 2, 29}, // Leap year
                {2000, 2, 29}, // Leap year
                {2100, 2, 29}  // Leap year in Julian, not in Gregorian
            });
        }

        @ParameterizedTest
        @MethodSource("lengthOfMonthProvider")
        @DisplayName("lengthOfMonth() should return the correct number of days")
        void lengthOfMonth_returnsCorrectDayCount(int year, int month, int length) {
            assertEquals(length, JulianDate.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Object[]> fieldRangeProvider() {
            return Stream.of(new Object[][]{
                {2012, 1, 23, DAY_OF_MONTH, 1, 31},
                {2012, 2, 23, DAY_OF_MONTH, 1, 29}, // 2012 is a leap year
                {2011, 2, 23, DAY_OF_MONTH, 1, 28}, // 2011 is not a leap year
                {2012, 1, 23, DAY_OF_YEAR, 1, 366},
                {2011, 1, 23, DAY_OF_YEAR, 1, 365},
                {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5},
                {2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4}
            });
        }

        @ParameterizedTest
        @MethodSource("fieldRangeProvider")
        @DisplayName("range() should return the correct value range for a given field")
        void range_forField_returnsCorrectRange(int year, int month, int dom, TemporalField field, int expectedMin, int expectedMax) {
            assertEquals(ValueRange.of(expectedMin, expectedMax), JulianDate.of(year, month, dom).range(field));
        }

        static Stream<Object[]> fieldValueProvider() {
            return Stream.of(new Object[][]{
                {2014, 5, 26, DAY_OF_WEEK, 7},
                {2014, 5, 26, DAY_OF_MONTH, 26},
                // Day of year for May 26 in non-leap 2014: 31+28+31+30+26 = 146
                {2014, 5, 26, DAY_OF_YEAR, 146},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21},
                {2014, 5, 26, MONTH_OF_YEAR, 5},
                // Proleptic month: 2014 * 12 + 5 - 1 = 24172
                {2014, 5, 26, PROLEPTIC_MONTH, 24172},
                {2014, 5, 26, YEAR, 2014},
                {2014, 5, 26, ERA, 1},
                {0, 6, 8, ERA, 0},
                {2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7}
            });
        }

        @ParameterizedTest
        @MethodSource("fieldValueProvider")
        @DisplayName("getLong() should return the correct value for a given field")
        void getLong_forField_returnsCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class DateManipulationTests {

        static Stream<Object[]> withFieldProvider() {
            return Stream.of(new Object[][]{
                {2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22},
                {2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31},
                {2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31},
                {2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                {2014, 5, 26, ERA, 0, -2013, 5, 26},
                // Adjusting month to one with fewer days
                {2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28},
                // Adjusting month in a leap year
                {2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29},
                // Adjusting year of a leap day to a non-leap year
                {2012, 2, 29, YEAR, 2011, 2011, 2, 28}
            });
        }

        @ParameterizedTest
        @MethodSource("withFieldProvider")
        @DisplayName("with() should return a correctly adjusted date")
        void with_fieldAndValue_returnsAdjustedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            assertEquals(JulianDate.of(ey, em, ed), JulianDate.of(y, m, d).with(field, value));
        }

        @Test
        @DisplayName("with(TemporalAdjuster) should return a correctly adjusted date")
        void with_temporalAdjuster_returnsAdjustedDate() {
            JulianDate base = JulianDate.of(2012, 6, 23);
            JulianDate test = base.with(TemporalAdjusters.lastDayOfMonth());
            assertEquals(JulianDate.of(2012, 6, 30), test);
        }

        static Stream<Object[]> dateAndAmountProvider() {
            return Stream.of(new Object[][]{
                {2014, 5, 26, 0, DAYS, 2014, 5, 26},
                {2014, 5, 26, 8, DAYS, 2014, 6, 3},
                {2014, 5, 26, -3, DAYS, 2014, 5, 23},
                {2014, 5, 26, 3, WEEKS, 2014, 6, 16},
                {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
                {2014, 5, 26, 3, YEARS, 2017, 5, 26},
                {2014, 5, 26, 3, DECADES, 2044, 5, 26},
                {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
                {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
                {2014, 5, 26, -1, ERAS, -2013, 5, 26}
            });
        }

        @ParameterizedTest
        @MethodSource("dateAndAmountProvider")
        @DisplayName("plus() should return a correctly added date")
        void plus_amountAndUnit_returnsAddedDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            assertEquals(JulianDate.of(ey, em, ed), JulianDate.of(y, m, d).plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("dateAndAmountProvider")
        @DisplayName("minus() should return a correctly subtracted date")
        void minus_amountAndUnit_returnsSubtractedDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            // This test reuses the 'plus' provider data by reversing the operation:
            // We assert that `endDate.minus(amount)` equals `startDate`.
            JulianDate startDate = JulianDate.of(y, m, d);
            JulianDate endDate = JulianDate.of(ey, em, ed);
            assertEquals(startDate, endDate.minus(amount, unit));
        }
    }

    @Nested
    @DisplayName("Period and Duration Calculation")
    class PeriodAndDurationTests {

        @Test
        @DisplayName("until() a date with the same value returns a zero period")
        void until_sameDate_returnsZeroPeriod() {
            JulianDate date = JulianDate.of(2012, 6, 23);
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#julianAndIsoDateProvider")
        @DisplayName("until() an equivalent ISO date returns a zero period")
        void until_equivalentIsoDate_returnsZeroChronoPeriod(JulianDate julian, LocalDate iso) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#julianAndIsoDateProvider")
        @DisplayName("ISO date until() an equivalent Julian date returns a zero period")
        void isoDate_until_equivalentJulianDate_returnsZeroPeriod(JulianDate julian, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(julian));
        }

        static Stream<Object[]> dateUntilProvider() {
            return Stream.of(new Object[][]{
                {2014, 5, 26, 2014, 5, 26, DAYS, 0},
                {2014, 5, 26, 2014, 6, 1, DAYS, 6},
                {2014, 5, 26, 2014, 5, 20, DAYS, -6},
                {2014, 5, 26, 2014, 6, 2, WEEKS, 1},
                {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
                {2014, 5, 26, 2015, 5, 26, YEARS, 1},
                {2014, 5, 26, 2024, 5, 26, DECADES, 1},
                {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
                {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
                {-2013, 5, 26, 2014, 5, 26, ERAS, 1}
            });
        }

        @ParameterizedTest
        @MethodSource("dateUntilProvider")
        @DisplayName("until() should return the correct duration for a given unit")
        void until_endDateAndUnit_returnsCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate end = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }
    }

    @Nested
    @DisplayName("Object Methods")
    class ObjectMethodTests {

        static Stream<Object[]> toStringProvider() {
            return Stream.of(new Object[][]{
                {JulianDate.of(1, 1, 1), "Julian AD 1-01-01"},
                {JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23"}
            });
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        @DisplayName("toString() should return a correctly formatted string")
        void toString_returnsCorrectlyFormattedString(JulianDate julian, String expected) {
            assertEquals(expected, julian.toString());
        }
    }
}