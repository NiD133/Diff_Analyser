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
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link BritishCutoverChronology} and {@link BritishCutoverDate}.
 * <p>
 * This test class is structured with nested classes to group related tests,
 * improving organization and readability.
 */
public class BritishCutoverChronologyTest {

    // Helper method to create BritishCutoverDate instances for test data
    private static BritishCutoverDate date(int year, int month, int day) {
        return BritishCutoverDate.of(year, month, day);
    }

    // Helper method to create ChronoPeriod instances for test data
    private static ChronoPeriod period(int years, int months, int days) {
        return BritishCutoverChronology.INSTANCE.period(years, months, days);
    }

    //-----------------------------------------------------------------------
    @Nested
    class FactoryMethodTests {

        @ParameterizedTest
        @CsvSource({
            "1900,  0,  1", // Invalid month
            "1900, -1,  1",
            "1900, 13,  1",
            "1900,  1, -1", // Invalid day
            "1900,  1,  0",
            "1900,  1, 32",
            "1900,  2, 30", // Invalid day for month (non-leap)
            "1899,  2, 29", // Invalid day for month (non-leap)
            "1900,  4, 31", // Invalid day for 30-day month
        })
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, day));
        }

        @Test
        void plus_withIsoPeriod_shouldThrowException() {
            BritishCutoverDate testDate = date(2014, 5, 26);
            assertThrows(DateTimeException.class, () -> testDate.plus(Period.ofMonths(2)));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    class ConversionTests {

        static Stream<Arguments> sampleBritishAndIsoDates() {
            return Stream.of(
                // Pre-cutover Julian dates
                Arguments.of(date(1, 1, 1), LocalDate.of(0, 12, 30)),
                Arguments.of(date(4, 2, 29), LocalDate.of(4, 2, 27)), // Julian leap year
                Arguments.of(date(100, 3, 1), LocalDate.of(100, 2, 28)), // Julian leap year
                Arguments.of(date(1582, 10, 4), LocalDate.of(1582, 10, 14)),

                // Dates around the 1752 cutover
                Arguments.of(date(1752, 9, 2), LocalDate.of(1752, 9, 13)),
                Arguments.of(date(1752, 9, 3), LocalDate.of(1752, 9, 14)), // In the gap, leniently accepted
                Arguments.of(date(1752, 9, 13), LocalDate.of(1752, 9, 24)), // In the gap, leniently accepted
                Arguments.of(date(1752, 9, 14), LocalDate.of(1752, 9, 14)), // First Gregorian day

                // Post-cutover Gregorian dates
                Arguments.of(date(1945, 11, 12), LocalDate.of(1945, 11, 12)),
                Arguments.of(date(2012, 7, 6), LocalDate.of(2012, 7, 6))
            );
        }

        @ParameterizedTest
        @MethodSource("sampleBritishAndIsoDates")
        void shouldConvertBritishCutoverDateToLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("sampleBritishAndIsoDates")
        void shouldCreateBritishCutoverDateFromLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("sampleBritishAndIsoDates")
        void toEpochDay_shouldMatchIsoDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("sampleBritishAndIsoDates")
        void chronology_dateEpochDay_shouldCreateCorrectDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("sampleBritishAndIsoDates")
        void chronology_dateFromTemporal_shouldCreateCorrectDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    class FieldAndRangeTests {

        @ParameterizedTest
        @CsvSource({
            // Julian leap year, not a Gregorian leap year
            "1700, 2, 29",
            // Year before cutover
            "1751, 2, 28",
            // Cutover year, September is shortened
            "1752, 9, 19",
            // Cutover year, other months are normal
            "1752, 2, 29",
            "1752, 10, 31",
            // Gregorian non-leap year divisible by 100
            "1800, 2, 28",
            // Gregorian leap year divisible by 400
            "2000, 2, 29"
        })
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, date(year, month, 1).lengthOfMonth());
        }

        @ParameterizedTest
        @CsvSource({
            "1700, 366", // Julian leap year
            "1751, 365", // Normal year before cutover
            "1752, 355", // Cutover year, 11 days removed
            "1753, 365", // Normal year after cutover
            "1800, 365", // Gregorian non-leap year
            "1900, 365", // Gregorian non-leap year
            "2000, 366", // Gregorian leap year
            "2004, 366"  // Gregorian leap year
        })
        void lengthOfYear_shouldReturnCorrectLength(int year, int expectedLength) {
            assertEquals(expectedLength, date(year, 1, 1).lengthOfYear());
            assertEquals(expectedLength, date(year, 12, 31).lengthOfYear());
        }

        static Stream<Arguments> rangeArguments() {
            return Stream.of(
                // DAY_OF_MONTH range
                Arguments.of(date(1700, 2, 23), DAY_OF_MONTH, ValueRange.of(1, 29)), // Julian leap
                Arguments.of(date(1751, 2, 23), DAY_OF_MONTH, ValueRange.of(1, 28)), // Non-leap
                Arguments.of(date(2012, 2, 23), DAY_OF_MONTH, ValueRange.of(1, 29)), // Gregorian leap

                // DAY_OF_YEAR range
                Arguments.of(date(1700, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 366)), // Julian leap
                Arguments.of(date(1752, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 355)), // Cutover year
                Arguments.of(date(2011, 2, 23), DAY_OF_YEAR, ValueRange.of(1, 365)), // Gregorian non-leap

                // ALIGNED_WEEK_OF_MONTH range
                Arguments.of(date(1752, 9, 23), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 3)), // Short month
                Arguments.of(date(2011, 2, 23), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)), // 28-day month
                Arguments.of(date(2012, 1, 23), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)), // 31-day month

                // ALIGNED_WEEK_OF_YEAR range
                Arguments.of(date(1752, 12, 23), ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 51)), // Short year
                Arguments.of(date(2011, 2, 23), ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53))  // Normal year
            );
        }

        @ParameterizedTest
        @MethodSource("rangeArguments")
        void range_forField_shouldReturnCorrectRange(BritishCutoverDate date, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, date.range(field));
        }

        static Stream<Arguments> getLongArguments() {
            return Stream.of(
                // Pre-cutover date
                Arguments.of(date(1752, 5, 26), DAY_OF_WEEK, 2L), // Tuesday
                Arguments.of(date(1752, 5, 26), DAY_OF_YEAR, 147L),

                // Date in cutover gap (treated as Julian)
                Arguments.of(date(1752, 9, 2), DAY_OF_WEEK, 3L), // Wednesday
                Arguments.of(date(1752, 9, 2), DAY_OF_YEAR, 246L),

                // First Gregorian date
                Arguments.of(date(1752, 9, 14), DAY_OF_WEEK, 4L), // Thursday
                Arguments.of(date(1752, 9, 14), DAY_OF_YEAR, 246L + 1), // Day after 1752-09-02

                // Modern date
                Arguments.of(date(2014, 5, 26), DAY_OF_WEEK, 1L), // Monday
                Arguments.of(date(2014, 5, 26), DAY_OF_YEAR, 146L),
                Arguments.of(date(2014, 5, 26), PROLEPTIC_MONTH, 2014 * 12 + 4),
                Arguments.of(date(2014, 5, 26), YEAR, 2014L),
                Arguments.of(date(2014, 5, 26), ERA, 1L),

                // BC Era
                Arguments.of(date(0, 6, 8), ERA, 0L)
            );
        }

        @ParameterizedTest
        @MethodSource("getLongArguments")
        void getLong_forField_shouldReturnCorrectValue(BritishCutoverDate date, TemporalField field, long expected) {
            assertEquals(expected, date.getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    class WithAndAdjusterTests {

        static Stream<Arguments> withFieldArguments() {
            return Stream.of(
                // Adjusting DAY_OF_WEEK across the cutover
                Arguments.of(date(1752, 9, 2), DAY_OF_WEEK, 4, date(1752, 9, 14)),
                // Adjusting DAY_OF_MONTH into the cutover gap (leniently)
                Arguments.of(date(1752, 9, 2), DAY_OF_MONTH, 3, date(1752, 9, 14)),
                // Adjusting MONTH_OF_YEAR across the cutover
                Arguments.of(date(1752, 8, 4), MONTH_OF_YEAR, 9, date(1752, 9, 15)),
                // Adjusting YEAR across the cutover
                Arguments.of(date(1751, 9, 4), YEAR, 1752, date(1752, 9, 15)),
                // Standard adjustments
                Arguments.of(date(2014, 5, 26), DAY_OF_WEEK, 3, date(2014, 5, 28)),
                Arguments.of(date(2012, 3, 31), MONTH_OF_YEAR, 2, date(2012, 2, 29))
            );
        }

        @ParameterizedTest
        @MethodSource("withFieldArguments")
        void with_fieldAndValue_shouldReturnAdjustedDate(
            BritishCutoverDate initialDate, TemporalField field, long value, BritishCutoverDate expectedDate) {
            assertEquals(expectedDate, initialDate.with(field, value));
        }

        @Test
        void with_lastDayOfMonthAdjuster_shouldReturnCorrectDate() {
            assertEquals(date(1752, 9, 30), date(1752, 9, 2).with(TemporalAdjusters.lastDayOfMonth()));
            assertEquals(date(1752, 9, 30), date(1752, 9, 14).with(TemporalAdjusters.lastDayOfMonth()));
            assertEquals(date(2012, 2, 29), date(2012, 2, 23).with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_localDateAdjuster_shouldReturnCorrectDate() {
            assertEquals(date(1752, 9, 1), date(1752, 9, 2).with(LocalDate.of(1752, 9, 12)));
            assertEquals(date(1752, 9, 14), date(1752, 9, 2).with(LocalDate.of(1752, 9, 14)));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    class ArithmeticTests {

        static Stream<Arguments> plusMinusArguments() {
            return Stream.of(
                // --- DAYS ---
                Arguments.of(date(1752, 9, 2), 1L, DAYS, date(1752, 9, 14), true), // Crosses cutover forward
                Arguments.of(date(1752, 9, 14), -1L, DAYS, date(1752, 9, 2), true), // Crosses cutover backward
                Arguments.of(date(2014, 5, 26), 8L, DAYS, date(2014, 6, 3), true),

                // --- WEEKS ---
                Arguments.of(date(1752, 9, 2), 1L, WEEKS, date(1752, 9, 20), true),
                Arguments.of(date(1752, 9, 14), -1L, WEEKS, date(1752, 8, 27), true),

                // --- MONTHS ---
                Arguments.of(date(1752, 8, 12), 1L, MONTHS, date(1752, 9, 23), false), // Not bidirectional
                Arguments.of(date(1752, 10, 12), -1L, MONTHS, date(1752, 9, 23), false), // Not bidirectional
                Arguments.of(date(2014, 5, 26), 3L, MONTHS, date(2014, 8, 26), true),

                // --- YEARS and larger ---
                Arguments.of(date(2014, 5, 26), 3L, YEARS, date(2017, 5, 26), true),
                Arguments.of(date(2014, 5, 26), 3L, DECADES, date(2044, 5, 26), true),
                Arguments.of(date(2014, 5, 26), -5L, CENTURIES, date(1514, 5, 26), true),
                Arguments.of(date(2014, 5, 26), -1L, ERAS, date(-2013, 5, 26), true)
            );
        }

        @ParameterizedTest
        @MethodSource("plusMinusArguments")
        void plus_withAmountAndUnit_shouldReturnCorrectDate(
            BritishCutoverDate base, long amount, TemporalUnit unit, BritishCutoverDate expected, boolean isBidirectional) {
            assertEquals(expected, base.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusMinusArguments")
        void minus_withAmountAndUnit_shouldReturnCorrectDate(
            BritishCutoverDate base, long amount, TemporalUnit unit, BritishCutoverDate expected, boolean isBidirectional) {
            if (isBidirectional) {
                assertEquals(base, expected.minus(amount, unit));
            }
        }

        static Stream<Arguments> untilTemporalUnitArguments() {
            return Stream.of(
                Arguments.of(date(1752, 9, 1), date(1752, 9, 2), DAYS, 1L),
                Arguments.of(date(1752, 9, 1), date(1752, 9, 14), DAYS, 2L), // Crosses gap
                Arguments.of(date(1752, 9, 2), date(1752, 9, 14), DAYS, 1L),
                Arguments.of(date(1752, 9, 14), date(1752, 9, 1), DAYS, -2L), // Crosses gap backward
                Arguments.of(date(1752, 9, 2), date(1752, 9, 20), WEEKS, 1L),
                Arguments.of(date(1752, 9, 2), date(1752, 10, 2), MONTHS, 1L),
                Arguments.of(date(2014, 5, 26), date(2015, 5, 26), YEARS, 1L)
            );
        }

        @ParameterizedTest
        @MethodSource("untilTemporalUnitArguments")
        void until_withEndDateAndUnit_shouldCalculateCorrectAmount(
            BritishCutoverDate start, BritishCutoverDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("sampleBritishAndIsoDates")
        void until_dateAndItself_returnsZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(period(0, 0, 0), cutoverDate.until(cutoverDate));
            assertEquals(period(0, 0, 0), cutoverDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    class PeriodUntilTests {

        static Stream<Arguments> untilChronoPeriodArguments() {
            return Stream.of(
                // Simple cases
                Arguments.of(date(1752, 7, 2), date(1752, 7, 1), period(0, 0, -1)),
                Arguments.of(date(1752, 7, 2), date(1752, 7, 2), period(0, 0, 0)),

                // Spanning the cutover
                Arguments.of(date(1752, 8, 2), date(1752, 9, 14), period(0, 1, 1)),
                Arguments.of(date(1752, 8, 16), date(1752, 9, 15), period(0, 0, 19)),
                Arguments.of(date(1752, 9, 1), date(1752, 9, 15), period(0, 0, 3)),
                Arguments.of(date(1752, 9, 2), date(1752, 9, 14), period(0, 0, 1)),

                // Reverse direction over cutover
                Arguments.of(date(1752, 9, 14), date(1752, 9, 2), period(0, 0, -1)),
                Arguments.of(date(1752, 10, 3), date(1752, 9, 2), period(0, -1, -1)),

                // Longer periods spanning the cutover
                Arguments.of(date(1752, 7, 2), date(1752, 10, 2), period(0, 3, 0)),
                Arguments.of(date(1752, 7, 14), date(1752, 9, 14), period(0, 2, 0))
            );
        }

        @ParameterizedTest
        @MethodSource("untilChronoPeriodArguments")
        void until_asChronoPeriod_shouldCalculateCorrectly(
            BritishCutoverDate start, BritishCutoverDate end, ChronoPeriod expectedPeriod) {
            assertEquals(expectedPeriod, start.until(end));
        }

        @ParameterizedTest
        @MethodSource("untilChronoPeriodArguments")
        void until_andPlusPeriod_shouldResultInEndDate(
            BritishCutoverDate start, BritishCutoverDate end, ChronoPeriod ignored) {
            // This is a property-based test: start.plus(start.until(end)) == end
            ChronoPeriod period = start.until(end);
            assertEquals(end, start.plus(period));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    class ToStringTests {

        @ParameterizedTest
        @CsvSource({
            "1, 1, 1, 'BritishCutover AD 1-01-01'",
            "2012, 6, 23, 'BritishCutover AD 2012-06-23'"
        })
        void toString_shouldReturnFormattedString(int y, int m, int d, String expected) {
            assertEquals(expected, date(y, m, d).toString());
        }
    }
}