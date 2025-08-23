package org.threeten.extra.chrono;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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

/**
 * Comprehensive tests for {@link BritishCutoverDate} and {@link BritishCutoverChronology}.
 */
@DisplayName("BritishCutoverChronology and BritishCutoverDate")
class BritishCutoverChronologyTest {

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Stream<Object[]> sampleDatePairsProvider() {
        return Stream.of(new Object[][]{
                // Pre-cutover Julian dates
                {BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
                {BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
                {BritishCutoverDate.of(4, 2, 29), LocalDate.of(4, 2, 27)}, // Julian leap year
                {BritishCutoverDate.of(100, 2, 29), LocalDate.of(100, 2, 27)}, // Julian leap year
                {BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
                {BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)},

                // Dates during the cutover gap (leniently accepted)
                {BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)},
                {BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)},

                // Post-cutover Gregorian dates
                {BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)},
                {BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12)},
                {BritishCutoverDate.of(2012, 7, 5), LocalDate.of(2012, 7, 5)},
        });
    }

    @Nested
    @DisplayName("Date Creation and Conversion")
    class DateCreationAndConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDatePairsProvider")
        void from_shouldCreateCorrectLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDatePairsProvider")
        void from_shouldCreateCorrectBritishCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDatePairsProvider")
        void chronology_shouldCreateCorrectDateFromEpochDay(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDatePairsProvider")
        void toEpochDay_shouldReturnCorrectValue(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDatePairsProvider")
        void chronology_shouldCreateCorrectDateFromTemporal(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }

        static Stream<Object[]> invalidDateProvider() {
            return Stream.of(new Object[][]{
                    {1900, 0, 0}, {1900, -1, 1}, {1900, 13, 1}, {1900, 1, -1},
                    {1900, 1, 32}, {1900, 2, 30}, {1899, 2, 29} // Not a leap year in Gregorian, but is in Julian
            });
        }

        @ParameterizedTest
        @MethodSource("invalidDateProvider")
        void of_shouldThrowExceptionForInvalidDateComponents(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
        }
    }

    @Nested
    @DisplayName("Field and Property Access")
    class FieldAndPropertyTests {

        static Stream<Object[]> lengthOfMonthProvider() {
            return Stream.of(new Object[][]{
                    {1700, Month.FEBRUARY, 29}, // Julian leap year
                    {1751, Month.FEBRUARY, 28},
                    {1752, Month.FEBRUARY, 29}, // Gregorian leap year
                    {1752, Month.SEPTEMBER, 19}, // Cutover month
                    {1753, Month.FEBRUARY, 28},
                    {1800, Month.FEBRUARY, 28}, // Not a leap year in Gregorian
                    {1900, Month.FEBRUARY, 28}, // Not a leap year in Gregorian
                    {2000, Month.FEBRUARY, 29}, // Gregorian leap year
            });
        }

        @ParameterizedTest
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth_shouldReturnCorrectLength(int year, Month month, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month.getValue(), 1).lengthOfMonth());
        }

        static Stream<Object[]> lengthOfYearProvider() {
            return Stream.of(new Object[][]{
                    {1700, 366}, // Julian leap year
                    {1751, 365},
                    {1752, 355}, // Cutover year with 11 days removed
                    {1753, 365},
                    {1800, 365},
                    {1900, 365},
                    {2000, 366},
            });
        }

        @ParameterizedTest
        @MethodSource("lengthOfYearProvider")
        void lengthOfYear_shouldReturnCorrectLength(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
        }

        static Stream<Object[]> rangeProvider() {
            return Stream.of(new Object[][]{
                    {BritishCutoverDate.of(1700, 2, 23), DAY_OF_MONTH, 1, 29},
                    {BritishCutoverDate.of(1752, 9, 23), DAY_OF_MONTH, 1, 30}, // Day of month is lenient
                    {BritishCutoverDate.of(1752, 1, 23), DAY_OF_YEAR, 1, 355},
                    {BritishCutoverDate.of(2011, 2, 23), DAY_OF_YEAR, 1, 365},
                    {BritishCutoverDate.of(2012, 2, 23), DAY_OF_YEAR, 1, 366},
                    {BritishCutoverDate.of(1752, 9, 23), ALIGNED_WEEK_OF_MONTH, 1, 3},
                    {BritishCutoverDate.of(1752, 12, 23), ALIGNED_WEEK_OF_YEAR, 1, 51},
            });
        }

        @ParameterizedTest
        @MethodSource("rangeProvider")
        void range_shouldReturnCorrectRangeForField(BritishCutoverDate date, TemporalField field, long min, long max) {
            assertEquals(ValueRange.of(min, max), date.range(field));
        }

        static Stream<Object[]> getLongProvider() {
            return Stream.of(new Object[][]{
                    {BritishCutoverDate.of(1752, 9, 2), DAY_OF_WEEK, 3}, // Wednesday
                    {BritishCutoverDate.of(1752, 9, 14), DAY_OF_WEEK, 4}, // Thursday
                    {BritishCutoverDate.of(1752, 9, 2), DAY_OF_YEAR, 246},
                    {BritishCutoverDate.of(1752, 9, 14), DAY_OF_YEAR, 246}, // Same day-of-year as 9/2
                    {BritishCutoverDate.of(1752, 9, 15), DAY_OF_YEAR, 247},
                    {BritishCutoverDate.of(2014, 5, 26), PROLEPTIC_MONTH, 2014 * 12 + 4},
                    {BritishCutoverDate.of(2014, 5, 26), YEAR, 2014},
                    {BritishCutoverDate.of(1, 6, 8), ERA, 1},
                    {BritishCutoverDate.of(0, 6, 8), ERA, 0},
            });
        }

        @ParameterizedTest
        @MethodSource("getLongProvider")
        void getLong_shouldReturnCorrectValueForField(BritishCutoverDate date, TemporalField field, long expected) {
            assertEquals(expected, date.getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Adjustment with 'with' and Adjusters")
    class WithAndAdjusterTests {

        static Stream<Object[]> withTemporalFieldProvider() {
            return Stream.of(new Object[][]{
                    // Adjusting across the cutover gap
                    {BritishCutoverDate.of(1752, 9, 2), DAY_OF_WEEK, 4, BritishCutoverDate.of(1752, 9, 14)},
                    {BritishCutoverDate.of(1752, 9, 14), DAY_OF_WEEK, 3, BritishCutoverDate.of(1752, 9, 2)},
                    // Leniently adjust into the gap, resulting in a valid date after the gap
                    {BritishCutoverDate.of(1752, 9, 2), DAY_OF_MONTH, 3, BritishCutoverDate.of(1752, 9, 14)},
                    // Standard adjustments
                    {BritishCutoverDate.of(2014, 5, 26), DAY_OF_WEEK, 3, BritishCutoverDate.of(2014, 5, 28)},
                    {BritishCutoverDate.of(2012, 3, 31), MONTH_OF_YEAR, 2, BritishCutoverDate.of(2012, 2, 29)},
                    {BritishCutoverDate.of(2012, 2, 29), YEAR, 2011, BritishCutoverDate.of(2011, 2, 28)},
            });
        }

        @ParameterizedTest
        @MethodSource("withTemporalFieldProvider")
        void with_temporalField_shouldReturnCorrectlyAdjustedDate(BritishCutoverDate base, TemporalField field, long value, BritishCutoverDate expected) {
            assertEquals(expected, base.with(field, value));
        }

        @Test
        void with_lastDayOfMonth_shouldAdjustCorrectly() {
            assertEquals(BritishCutoverDate.of(1752, 9, 30), BritishCutoverDate.of(1752, 9, 2).with(TemporalAdjusters.lastDayOfMonth()));
            assertEquals(BritishCutoverDate.of(2012, 2, 29), BritishCutoverDate.of(2012, 2, 1).with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_localDate_shouldAdjustCorrectly() {
            // Adjusting to a date that falls within the cutover gap
            assertEquals(BritishCutoverDate.of(1752, 9, 1), BritishCutoverDate.of(1752, 9, 14).with(LocalDate.of(1752, 9, 12)));
            // Standard adjustment
            assertEquals(BritishCutoverDate.of(2012, 2, 23), BritishCutoverDate.of(1752, 9, 14).with(LocalDate.of(2012, 2, 23)));
        }
    }

    @Nested
    @DisplayName("Date Arithmetic with 'plus' and 'minus'")
    class ArithmeticTests {

        static Stream<Object[]> plusProvider() {
            return Stream.of(new Object[][]{
                    // Crossing the cutover gap forward
                    {BritishCutoverDate.of(1752, 9, 2), 1, DAYS, BritishCutoverDate.of(1752, 9, 14)},
                    {BritishCutoverDate.of(1752, 9, 2), 1, WEEKS, BritishCutoverDate.of(1752, 9, 20)},
                    {BritishCutoverDate.of(1752, 8, 12), 1, MONTHS, BritishCutoverDate.of(1752, 9, 23)},
                    // Standard addition
                    {BritishCutoverDate.of(2014, 5, 26), 8, DAYS, BritishCutoverDate.of(2014, 6, 3)},
                    {BritishCutoverDate.of(2014, 5, 26), 3, WEEKS, BritishCutoverDate.of(2014, 6, 16)},
                    {BritishCutoverDate.of(2014, 5, 26), 3, MONTHS, BritishCutoverDate.of(2014, 8, 26)},
                    {BritishCutoverDate.of(2014, 5, 26), 3, YEARS, BritishCutoverDate.of(2017, 5, 26)},
                    {BritishCutoverDate.of(2014, 5, 26), 3, DECADES, BritishCutoverDate.of(2044, 5, 26)},
                    {BritishCutoverDate.of(2014, 5, 26), 3, CENTURIES, BritishCutoverDate.of(2314, 5, 26)},
                    {BritishCutoverDate.of(2014, 5, 26), 3, MILLENNIA, BritishCutoverDate.of(5014, 5, 26)},
                    {BritishCutoverDate.of(2014, 5, 26), -1, ERAS, BritishCutoverDate.of(-2013, 5, 26)},
            });
        }

        @ParameterizedTest
        @MethodSource("plusProvider")
        void plus_shouldAddAmountCorrectly(BritishCutoverDate base, long amount, TemporalUnit unit, BritishCutoverDate expected) {
            assertEquals(expected, base.plus(amount, unit));
        }

        static Stream<Object[]> minusProvider() {
            return Stream.of(new Object[][]{
                    // Crossing the cutover gap backward
                    {BritishCutoverDate.of(1752, 9, 14), 1, DAYS, BritishCutoverDate.of(1752, 9, 2)},
                    {BritishCutoverDate.of(1752, 9, 20), 1, WEEKS, BritishCutoverDate.of(1752, 9, 2)},
                    {BritishCutoverDate.of(1752, 10, 12), 1, MONTHS, BritishCutoverDate.of(1752, 9, 23)},
                    // Standard subtraction
                    {BritishCutoverDate.of(2014, 6, 3), 8, DAYS, BritishCutoverDate.of(2014, 5, 26)},
                    {BritishCutoverDate.of(2014, 6, 16), 3, WEEKS, BritishCutoverDate.of(2014, 5, 26)},
                    {BritishCutoverDate.of(2014, 8, 26), 3, MONTHS, BritishCutoverDate.of(2014, 5, 26)},
            });
        }

        @ParameterizedTest
        @MethodSource("minusProvider")
        void minus_shouldSubtractAmountCorrectly(BritishCutoverDate base, long amount, TemporalUnit unit, BritishCutoverDate expected) {
            assertEquals(expected, base.minus(amount, unit));
        }

        @Test
        void plus_period_shouldAddCorrectly() {
            ChronoPeriod period = BritishCutoverChronology.INSTANCE.period(0, 1, 3);
            assertEquals(BritishCutoverDate.of(1752, 10, 5), BritishCutoverDate.of(1752, 9, 2).plus(period));
        }
    }

    @Nested
    @DisplayName("Period Calculation with 'until'")
    class UntilTests {

        static Stream<Object[]> untilTemporalUnitProvider() {
            return Stream.of(new Object[][]{
                    {BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 14), DAYS, 1},
                    {BritishCutoverDate.of(1752, 9, 1), BritishCutoverDate.of(1752, 9, 14), DAYS, 2},
                    {BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 20), WEEKS, 1},
                    {BritishCutoverDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 10, 14), MONTHS, 1},
                    {BritishCutoverDate.of(2014, 5, 26), BritishCutoverDate.of(2015, 5, 26), YEARS, 1},
            });
        }

        @ParameterizedTest
        @MethodSource("untilTemporalUnitProvider")
        void until_temporalUnit_shouldCalculateCorrectDuration(BritishCutoverDate start, BritishCutoverDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Object[]> untilPeriodProvider() {
            return Stream.of(new Object[][]{
                    // --- Basic cases ---
                    {1752, 7, 2, 1752, 7, 2, 0, 0, 0},
                    {1752, 7, 2, 1752, 7, 1, 0, 0, -1},

                    // --- Spanning the cutover gap (simple) ---
                    // Start date is the day before the gap, end date is the day after.
                    {1752, 9, 2, 1752, 9, 14, 0, 0, 1},
                    // Start date is the day after the gap, end date is the day before.
                    {1752, 9, 14, 1752, 9, 2, 0, 0, -1},

                    // --- Month calculations across the cutover ---
                    // From July 2nd to Sep 2nd is exactly 2 months.
                    {1752, 7, 2, 1752, 9, 2, 0, 2, 0},
                    // From July 2nd to Sep 14th is 2 months and 1 day (since 9/14 is the day after 9/2).
                    {1752, 7, 2, 1752, 9, 14, 0, 2, 1},
                    // From Aug 2nd to Sep 2nd is exactly 1 month.
                    {1752, 8, 2, 1752, 9, 2, 0, 1, 0},
                    // From Aug 2nd to Sep 14th is 1 month and 1 day.
                    {1752, 8, 2, 1752, 9, 14, 0, 1, 1},
            });
        }

        @ParameterizedTest(name = "from {0}-{1}-{2} to {3}-{4}-{5} should be P{6}Y{7}M{8}D")
        @MethodSource("untilPeriodProvider")
        void until_chronoLocalDate_shouldCalculateCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            ChronoPeriod expected = BritishCutoverChronology.INSTANCE.period(ey, em, ed);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest(name = "start.plus(start.until(end)) should equal end")
        @MethodSource("untilPeriodProvider")
        void until_plus_shouldBeReversible(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            assertEquals(end, start.plus(start.until(end)));
        }

        @Test
        void until_shouldReturnZeroPeriodForSameDate() {
            BritishCutoverDate date = BritishCutoverDate.of(2012, 7, 5);
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        @Test
        void until_shouldReturnZeroPeriodForEqualIsoDate() {
            BritishCutoverDate date = BritishCutoverDate.of(2012, 7, 5);
            LocalDate isoDate = LocalDate.of(2012, 7, 5);
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), date.until(isoDate));
        }

        @Test
        void until_isoDate_shouldReturnZeroPeriodForEqualBritishCutoverDate() {
            BritishCutoverDate date = BritishCutoverDate.of(2012, 7, 5);
            LocalDate isoDate = LocalDate.of(2012, 7, 5);
            assertEquals(Period.ZERO, isoDate.until(date));
        }
    }

    @Nested
    @DisplayName("General API Tests")
    class GeneralApiTests {

        @Test
        void toString_shouldReturnCorrectFormat() {
            assertEquals("BritishCutover AD 1-01-01", BritishCutoverDate.of(1, 1, 1).toString());
            assertEquals("BritishCutover AD 2012-06-23", BritishCutoverDate.of(2012, 6, 23).toString());
        }

        @Test
        void equals_and_hashCode_shouldAdhereToContract() {
            new EqualsTester()
                    .addEqualityGroup(
                            BritishCutoverDate.of(2012, 7, 5),
                            BritishCutoverDate.of(2012, 7, 5))
                    .addEqualityGroup(
                            BritishCutoverDate.of(2012, 7, 6))
                    .addEqualityGroup(
                            BritishCutoverDate.of(1752, 9, 2)) // Pre-cutover
                    .addEqualityGroup(
                            BritishCutoverDate.of(1752, 9, 14)) // Post-cutover
                    .testEquals();
        }
    }
}