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

import com.google.common.testing.EqualsTester;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
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
 * This class contains tests for the {@link JulianDate} class, focusing on its interaction
 * with the ISO calendar system ({@link java.time.LocalDate}) and its adherence to the
 * {@link java.time.temporal.Temporal} contract.
 */
@DisplayName("JulianDate Interoperability and Core Functionality Tests")
class JulianDateInteropTest {

    private static final ChronoPeriod ZERO_JULIAN_PERIOD = JulianChronology.INSTANCE.period(0, 0, 0);

    //-----------------------------------------------------------------------
    // Conversion and Interoperability Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversions and Interoperability with ISO Calendar")
    class ConversionAndInteropTests {

        static Object[][] julianAndIsoDateProvider() {
            return new Object[][]{
                {JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
                {JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)},
                {JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
                {JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
                {JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)},
                {JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)},
                {JulianDate.of(2012, 6, 22), LocalDate.of(2012, 7, 5)},
                {JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6)},
            };
        }

        @ParameterizedTest(name = "{index}: Julian {0} -> ISO {1}")
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("should convert from JulianDate to correct LocalDate")
        void julianDateToLocalDate_shouldConvertCorrectly(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(julianDate));
        }

        @ParameterizedTest(name = "{index}: ISO {1} -> Julian {0}")
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("should convert from LocalDate to correct JulianDate")
        void localDateToJulianDate_shouldConvertCorrectly(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianDate.from(isoDate));
        }

        @ParameterizedTest(name = "{index}: ISO Epoch Day for {1}")
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("should create correct JulianDate from ISO epoch day")
        void chronologyDateFromEpochDay_shouldMatchIsoEpochDay(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest(name = "{index}: Julian Epoch Day for {0}")
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("toEpochDay should match equivalent LocalDate epoch day")
        void toEpochDay_shouldMatchIsoEpochDay(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), julianDate.toEpochDay());
        }

        @Test
        @DisplayName("until a date with itself should return a zero period")
        void until_withSameJulianDate_returnsZeroPeriod() {
            JulianDate date = JulianDate.of(2012, 6, 22);
            assertEquals(ZERO_JULIAN_PERIOD, date.until(date));
        }

        @ParameterizedTest(name = "{index}: Julian {0} until ISO {1}")
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("until an equivalent LocalDate should return a zero period")
        void until_withEquivalentLocalDate_returnsZeroPeriod(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(ZERO_JULIAN_PERIOD, julianDate.until(isoDate));
        }

        @ParameterizedTest(name = "{index}: ISO {1} until Julian {0}")
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("LocalDate::until an equivalent JulianDate should return a zero period")
        void localDateUntilJulianDate_forEquivalentDates_returnsZeroPeriod(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(julianDate));
        }

        @ParameterizedTest(name = "{index}: Julian from ISO {1}")
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("should create correct JulianDate from a TemporalAccessor (LocalDate)")
        void chronologyDateFromTemporal_shouldConvertFromLocalDate(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(julianDate, JulianChronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("plusDays should behave like LocalDate::plusDays")
        void plusDays_shouldBehaveLikeLocalDatePlusDays(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(julianDate.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(julianDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(julianDate.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(julianDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(julianDate.plus(-60, DAYS)));
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("minusDays should behave like LocalDate::minusDays")
        void minusDays_shouldBehaveLikeLocalDateMinusDays(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(julianDate.minus(0, DAYS)));
            assertEquals(isoDate.minusDays(1), LocalDate.from(julianDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(julianDate.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(julianDate.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(julianDate.minus(-60, DAYS)));
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("julianAndIsoDateProvider")
        @DisplayName("until(..., DAYS) should calculate the correct day difference")
        void until_withDaysUnit_shouldCalculateCorrectDayDifference(JulianDate julianDate, LocalDate isoDate) {
            assertEquals(0, julianDate.until(isoDate.plusDays(0), DAYS));
            assertEquals(1, julianDate.until(isoDate.plusDays(1), DAYS));
            assertEquals(35, julianDate.until(isoDate.plusDays(35), DAYS));
            assertEquals(-40, julianDate.until(isoDate.minusDays(40), DAYS));
        }
    }

    //-----------------------------------------------------------------------
    // Factory and Validation Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Creation and Validation")
    class FactoryAndValidationTests {

        static Object[][] invalidDateProvider() {
            return new Object[][]{
                {1900, 0, 0}, {1900, -1, 1}, {1900, 0, 1}, {1900, 13, 1},
                {1900, 1, 0}, {1900, 1, 32}, {1900, 2, 30}, {1899, 2, 29},
            };
        }

        @ParameterizedTest(name = "y={0}, m={1}, d={2}")
        @MethodSource("invalidDateProvider")
        @DisplayName("of(year, month, day) with invalid values should throw DateTimeException")
        void of_withInvalidDateParts_throwsDateTimeException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, day));
        }

        static Object[][] dateAndExpectedLengthOfMonthProvider() {
            return new Object[][]{
                {1900, 1, 31}, {1900, 2, 29}, // Julian leap year
                {1900, 3, 31}, {1900, 4, 30}, {1900, 12, 31},
                {1901, 2, 28}, {1904, 2, 29}, // Julian leap year
                {2000, 2, 29}, // Julian leap year
                {2100, 2, 29}, // Julian leap year
            };
        }

        @ParameterizedTest(name = "{0}-{1} should have {2} days")
        @MethodSource("dateAndExpectedLengthOfMonthProvider")
        @DisplayName("lengthOfMonth should return the correct number of days")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, JulianDate.of(year, month, 1).lengthOfMonth());
        }
    }

    //-----------------------------------------------------------------------
    // Field and Unit Query Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Field and Unit Queries")
    class FieldAndUnitQueryTests {

        static Object[][] dateAndFieldWithExpectedRangeProvider() {
            return new Object[][]{
                {2012, 1, 23, DAY_OF_MONTH, 1, 31},
                {2012, 2, 23, DAY_OF_MONTH, 1, 29}, // Leap year
                {2011, 2, 23, DAY_OF_MONTH, 1, 28}, // Non-leap year
                {2012, 4, 23, DAY_OF_MONTH, 1, 30},
                {2012, 1, 23, DAY_OF_YEAR, 1, 366}, // Leap year
                {2011, 2, 23, DAY_OF_YEAR, 1, 365}, // Non-leap year
                {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5},
                {2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4},
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2}, {3}: {4}-{5}")
        @MethodSource("dateAndFieldWithExpectedRangeProvider")
        @DisplayName("range(field) should return the correct value range")
        void range_forField_shouldReturnCorrectValueRange(int year, int month, int day, TemporalField field, int expectedMin, int expectedMax) {
            JulianDate date = JulianDate.of(year, month, day);
            assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
        }

        static Object[][] dateAndFieldWithExpectedValueProvider() {
            return new Object[][]{
                {2014, 5, 26, DAY_OF_WEEK, 7},
                {2014, 5, 26, DAY_OF_MONTH, 26},
                {2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26},
                {2014, 5, 26, MONTH_OF_YEAR, 5},
                {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
                {2014, 5, 26, YEAR, 2014},
                {2014, 5, 26, ERA, 1},
                {1, 6, 8, ERA, 1},
                {0, 6, 8, ERA, 0},
                {2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7},
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2}, getLong({3}) -> {4}")
        @MethodSource("dateAndFieldWithExpectedValueProvider")
        @DisplayName("getLong(field) should return the correct value")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, day).getLong(field));
        }

        static Object[][] dateDifferenceProvider() {
            return new Object[][]{
                {2014, 5, 26, 2014, 5, 26, DAYS, 0},
                {2014, 5, 26, 2014, 6, 1, DAYS, 6},
                {2014, 5, 26, 2014, 5, 20, DAYS, -6},
                {2014, 5, 26, 2014, 6, 25, MONTHS, 0},
                {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
                {2014, 5, 26, 2015, 5, 25, YEARS, 0},
                {2014, 5, 26, 2015, 5, 26, YEARS, 1},
                {-2013, 5, 26, 2014, 5, 26, ERAS, 1},
            };
        }

        @ParameterizedTest(name = "({0}-{1}-{2} until {3}-{4}-{5}) in {6} is {7}")
        @MethodSource("dateDifferenceProvider")
        @DisplayName("until(end, unit) should calculate the correct amount of time")
        void until_withTemporalUnit_shouldCalculateCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate end = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }
    }

    //-----------------------------------------------------------------------
    // Date Modification Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Modification")
    class DateModificationTests {

        static Object[][] dateAndFieldModificationProvider() {
            return new Object[][]{
                {2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22},
                {2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31},
                {2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31},
                {2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                {2014, 5, 26, ERA, 0, -2013, 5, 26},
                {2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28}, // Adjust to end of month
                {2012, 2, 29, YEAR, 2011, 2011, 2, 28}, // Adjust leap day
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("dateAndFieldModificationProvider")
        @DisplayName("with(field, value) should return correctly modified date")
        void with_fieldAndValue_shouldReturnModifiedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            JulianDate start = JulianDate.of(y, m, d);
            JulianDate expected = JulianDate.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        static Object[][] dateAndAmountToAddProvider() {
            return new Object[][]{
                {2014, 5, 26, 8, DAYS, 2014, 6, 3},
                {2014, 5, 26, 3, WEEKS, 2014, 6, 16},
                {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
                {2014, 5, 26, 3, YEARS, 2017, 5, 26},
                {2014, 5, 26, 3, DECADES, 2044, 5, 26},
                {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
                {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
                {2014, 5, 26, -1, ERAS, -2013, 5, 26},
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("dateAndAmountToAddProvider")
        @DisplayName("plus(amount, unit) should return correctly added date")
        void plus_amountAndUnit_shouldReturnAddedDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            JulianDate start = JulianDate.of(y, m, d);
            JulianDate expected = JulianDate.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4} -> {0}-{1}-{2}")
        @MethodSource("dateAndAmountToAddProvider")
        @DisplayName("minus(amount, unit) should return correctly subtracted date")
        void minus_amountAndUnit_shouldReturnSubtractedDate(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            JulianDate end = JulianDate.of(endYear, endMonth, endDay);
            JulianDate expected = JulianDate.of(startYear, startMonth, startDay);
            assertEquals(expected, end.minus(amount, unit));
        }
    }

    //-----------------------------------------------------------------------
    // Object Method Overrides
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Object Method Overrides")
    class ObjectMethodTests {

        static Object[][] dateAndExpectedStringProvider() {
            return new Object[][]{
                {JulianDate.of(1, 1, 1), "Julian AD 1-01-01"},
                {JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23"},
                {JulianDate.of(0, 12, 30), "Julian BC 1-12-30"},
            };
        }

        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("dateAndExpectedStringProvider")
        @DisplayName("toString should return correctly formatted string")
        void toString_shouldReturnFormattedString(JulianDate date, String expected) {
            assertEquals(expected, date.toString());
        }

        @Test
        @DisplayName("equals and hashCode should follow contract")
        void equalsAndHashCode_shouldFollowContract() {
            new EqualsTester()
                .addEqualityGroup(JulianDate.of(2000, 1, 3), JulianDate.of(2000, 1, 3))
                .addEqualityGroup(JulianDate.of(2000, 1, 4))
                .addEqualityGroup(JulianDate.of(2000, 2, 3))
                .addEqualityGroup(JulianDate.of(2001, 1, 3))
                .testEquals();
        }
    }
}