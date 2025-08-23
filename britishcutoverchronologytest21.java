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
import static java.time.temporal.ChronoUnit.MINUTES;
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
import java.time.temporal.WeekFields;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link BritishCutoverDate} class.
 * This class focuses on creation, conversion, and manipulation of dates,
 * especially around the Julian-Gregorian cutover in 1752.
 */
class BritishCutoverDateTest {

    //-----------------------------------------------------------------------
    // Test Data Providers
    //-----------------------------------------------------------------------

    private static Stream<Arguments> sampleCutoverAndIsoDates() {
        return Stream.of(
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)), // Before cutover gap
            Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)), // Leniently handles date in gap
            Arguments.of(BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)), // Leniently handles date in gap
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)), // First day after cutover
            Arguments.of(BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6))
        );
    }

    //-----------------------------------------------------------------------
    // Tests for conversions and epoch day
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversions and Epoch Day")
    class ConversionTests {

        @DisplayName("LocalDate.from(cutoverDate) should return the correct ISO date")
        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        void toLocalDate_shouldReturnCorrectIsoDate(BritishCutoverDate cutoverDate, LocalDate expectedIsoDate) {
            assertEquals(expectedIsoDate, LocalDate.from(cutoverDate));
        }

        @DisplayName("BritishCutoverDate.from(isoDate) should return the correct cutover date")
        @ParameterizedTest(name = "{1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        void fromLocalDate_shouldReturnCorrectBritishCutoverDate(BritishCutoverDate expectedCutoverDate, LocalDate isoDate) {
            assertEquals(expectedCutoverDate, BritishCutoverDate.from(isoDate));
        }

        @DisplayName("toEpochDay() should return the correct epoch day")
        @ParameterizedTest(name = "{0} -> epoch day of {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        void toEpochDay_shouldReturnCorrectValue(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @DisplayName("chronology.dateEpochDay() should create the correct date")
        @ParameterizedTest(name = "epoch day of {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        void chronologyDateFromEpochDay_shouldReturnCorrectDate(BritishCutoverDate expectedCutoverDate, LocalDate isoDate) {
            assertEquals(expectedCutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @DisplayName("chronology.date(temporal) should create the correct date")
        @ParameterizedTest(name = "{1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleCutoverAndIsoDates")
        void chronologyDateFromTemporal_shouldReturnCorrectDate(BritishCutoverDate expectedCutoverDate, LocalDate isoDate) {
            assertEquals(expectedCutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }
    }

    //-----------------------------------------------------------------------
    // Tests for date creation with invalid values
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Factory method 'of'")
    class FactoryOfTests {
        private static Stream<Arguments> invalidDateProvider() {
            return Stream.of(
                Arguments.of(1900, 0, 1, "invalid month"),
                Arguments.of(1900, 13, 1, "invalid month"),
                Arguments.of(1900, 1, 0, "invalid day"),
                Arguments.of(1900, 1, 32, "invalid day"),
                Arguments.of(1900, 2, 30, "invalid day for Feb"),
                Arguments.of(1899, 2, 29, "invalid day for non-leap Feb"),
                Arguments.of(1900, 4, 31, "invalid day for Apr")
            );
        }

        @ParameterizedTest(name = "year={0}, month={1}, day={2} ({3})")
        @MethodSource("invalidDateProvider")
        void of_withInvalidDateParts_shouldThrowDateTimeException(int year, int month, int dayOfMonth, String description) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
        }
    }

    //-----------------------------------------------------------------------
    // Tests for date properties (length of month/year, ranges)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Properties")
    class PropertyTests {

        private static Stream<Arguments> lengthOfMonthProvider() {
            // year, month, expectedLength
            return Stream.of(
                Arguments.of(1700, 2, 29, "Julian leap year"),
                Arguments.of(1752, 2, 29, "Cutover year, Julian leap"),
                Arguments.of(1752, 9, 19, "Cutover month has 19 days"),
                Arguments.of(1800, 2, 28, "Gregorian non-leap year"),
                Arguments.of(1900, 2, 28, "Gregorian non-leap year"),
                Arguments.of(2000, 2, 29, "Gregorian leap year")
            );
        }

        @ParameterizedTest(name = "{3}: {0}-{1} should have {2} days")
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength, String description) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        private static Stream<Arguments> lengthOfYearProvider() {
            // year, expectedLength
            return Stream.of(
                Arguments.of(1700, 366, "Julian leap year"),
                Arguments.of(1751, 365, "Year before cutover"),
                Arguments.of(1752, 355, "Cutover year (lost 10 days)"),
                Arguments.of(1753, 365, "Year after cutover"),
                Arguments.of(1800, 365, "Gregorian non-leap year"),
                Arguments.of(2000, 366, "Gregorian leap year")
            );
        }

        @ParameterizedTest(name = "{2}: Year {0} should have {1} days")
        @MethodSource("lengthOfYearProvider")
        void lengthOfYear_shouldReturnCorrectLength(int year, int expectedLength, String description) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
            assertEquals(expectedLength, BritishCutoverDate.of(year, 12, 31).lengthOfYear());
        }

        private static Stream<Arguments> rangeProvider() {
            // field, date, expectedMin, expectedMax, description
            return Stream.of(
                Arguments.of(DAY_OF_MONTH, BritishCutoverDate.of(1700, 2, 23), 1, 29, "DOM in Julian leap Feb"),
                Arguments.of(DAY_OF_MONTH, BritishCutoverDate.of(1752, 9, 23), 1, 30, "DOM in cutover month"),
                Arguments.of(DAY_OF_YEAR, BritishCutoverDate.of(1752, 1, 23), 1, 355, "DOY in cutover year"),
                Arguments.of(ALIGNED_WEEK_OF_MONTH, BritishCutoverDate.of(1752, 9, 23), 1, 3, "AWOM in cutover month"),
                Arguments.of(ALIGNED_WEEK_OF_YEAR, BritishCutoverDate.of(1752, 12, 23), 1, 51, "AWOY in cutover year")
            );
        }

        @ParameterizedTest(name = "{4}: range of {0} for {1} should be {2}-{3}")
        @MethodSource("rangeProvider")
        void range_forField_shouldReturnCorrectValueRange(TemporalField field, BritishCutoverDate date, long expectedMin, long expectedMax, String description) {
            assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
        }
    }

    //-----------------------------------------------------------------------
    // Tests for getLong(field)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("getLong(TemporalField)")
    class GetLongTests {
        private static Stream<Arguments> getLongProvider() {
            // date, field, expectedValue
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), DAY_OF_WEEK, 3L),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), DAY_OF_WEEK, 4L),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), DAY_OF_YEAR, 246L), // 31+29+31+30+31+30+31+31+2
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), DAY_OF_YEAR, 247L), // Day after 9/2 is 9/14
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), PROLEPTIC_MONTH, 2014 * 12 + 4),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), YEAR, 2014L),
                Arguments.of(BritishCutoverDate.of(0, 6, 8), ERA, 0L),
                Arguments.of(BritishCutoverDate.of(1, 6, 8), ERA, 1L),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), WeekFields.ISO.dayOfWeek(), 1L)
            );
        }

        @ParameterizedTest(name = "{0} .getLong({1}) should be {2}")
        @MethodSource("getLongProvider")
        void getLong_forField_shouldReturnCorrectValue(BritishCutoverDate date, TemporalField field, long expectedValue) {
            assertEquals(expectedValue, date.getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    // Tests for with(field, value) and with(adjuster)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("with(...) adjusters")
    class WithTests {
        private static Stream<Arguments> withFieldAndValueProvider() {
            // description, baseDate, field, value, expectedDate
            return Stream.of(
                Arguments.of("Set DOW before cutover", BritishCutoverDate.of(1752, 9, 2), DAY_OF_WEEK, 1, BritishCutoverDate.of(1752, 8, 31)),
                Arguments.of("Set DOW after cutover", BritishCutoverDate.of(1752, 9, 2), DAY_OF_WEEK, 4, BritishCutoverDate.of(1752, 9, 14)),
                Arguments.of("Set DOM into cutover gap (lenient)", BritishCutoverDate.of(1752, 9, 2), DAY_OF_MONTH, 3, BritishCutoverDate.of(1752, 9, 14)),
                Arguments.of("Set DOY into cutover gap (lenient)", BritishCutoverDate.of(1752, 9, 2), DAY_OF_YEAR, 247, BritishCutoverDate.of(1752, 9, 14)),
                Arguments.of("Set Month across cutover", BritishCutoverDate.of(1752, 8, 4), MONTH_OF_YEAR, 9, BritishCutoverDate.of(1752, 9, 15)),
                Arguments.of("Set Year across cutover", BritishCutoverDate.of(1751, 9, 4), YEAR, 1752, BritishCutoverDate.of(1752, 9, 15)),
                Arguments.of("Set Month on day not in target month", BritishCutoverDate.of(2011, 3, 31), MONTH_OF_YEAR, 2, BritishCutoverDate.of(2011, 2, 28)),
                Arguments.of("Set Year on leap day to non-leap year", BritishCutoverDate.of(2012, 2, 29), YEAR, 2011, BritishCutoverDate.of(2011, 2, 28)),
                Arguments.of("Set ERA from AD to BC", BritishCutoverDate.of(2014, 5, 26), ERA, 0, BritishCutoverDate.of(-2013, 5, 26))
            );
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("withFieldAndValueProvider")
        void with_temporalField_shouldSetFieldToValue(String description, BritishCutoverDate baseDate, TemporalField field, long value, BritishCutoverDate expectedDate) {
            assertEquals(expectedDate, baseDate.with(field, value));
        }

        @Test
        void with_lastDayOfMonthAdjuster_shouldReturnLastDayOfMonth() {
            assertEquals(BritishCutoverDate.of(1752, 9, 30), BritishCutoverDate.of(1752, 9, 2).with(TemporalAdjusters.lastDayOfMonth()));
            assertEquals(BritishCutoverDate.of(2012, 2, 29), BritishCutoverDate.of(2012, 2, 23).with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_localDateAdjuster_shouldReturnCorrectDate() {
            BritishCutoverDate baseDate = BritishCutoverDate.of(1752, 9, 2);
            LocalDate newIsoDate = LocalDate.of(1752, 9, 14); // This is a valid date after the cutover
            assertEquals(BritishCutoverDate.of(1752, 9, 14), baseDate.with(newIsoDate));
        }
    }

    //-----------------------------------------------------------------------
    // Tests for plus/minus
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("plus() and minus()")
    class PlusMinusTests {
        private static Stream<Arguments> plusProvider() {
            // description, baseDate, amount, unit, expectedDate
            return Stream.of(
                Arguments.of("Add 1 day across cutover", BritishCutoverDate.of(1752, 9, 2), 1, DAYS, BritishCutoverDate.of(1752, 9, 14)),
                Arguments.of("Add 1 week across cutover", BritishCutoverDate.of(1752, 9, 2), 1, WEEKS, BritishCutoverDate.of(1752, 9, 20)),
                Arguments.of("Add 1 month across cutover", BritishCutoverDate.of(1752, 8, 12), 1, MONTHS, BritishCutoverDate.of(1752, 9, 23)),
                Arguments.of("Add 1 year", BritishCutoverDate.of(2014, 5, 26), 1, YEARS, BritishCutoverDate.of(2015, 5, 26)),
                Arguments.of("Add 1 decade", BritishCutoverDate.of(2014, 5, 26), 1, DECADES, BritishCutoverDate.of(2024, 5, 26)),
                Arguments.of("Add 1 century", BritishCutoverDate.of(2014, 5, 26), 1, CENTURIES, BritishCutoverDate.of(2114, 5, 26)),
                Arguments.of("Add 1 millennium", BritishCutoverDate.of(2014, 5, 26), 1, MILLENNIA, BritishCutoverDate.of(3014, 5, 26)),
                Arguments.of("Add -1 era", BritishCutoverDate.of(2014, 5, 26), -1, ERAS, BritishCutoverDate.of(-2013, 5, 26))
            );
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("plusProvider")
        void plus_withTemporalUnit_shouldAddAmountCorrectly(String description, BritishCutoverDate baseDate, long amount, TemporalUnit unit, BritishCutoverDate expectedDate) {
            assertEquals(expectedDate, baseDate.plus(amount, unit));
        }

        private static Stream<Arguments> minusProvider() {
            // description, baseDate, amount, unit, expectedDate
            return Stream.of(
                Arguments.of("Subtract 1 day across cutover", BritishCutoverDate.of(1752, 9, 14), 1, DAYS, BritishCutoverDate.of(1752, 9, 2)),
                Arguments.of("Subtract 1 week across cutover", BritishCutoverDate.of(1752, 9, 21), 1, WEEKS, BritishCutoverDate.of(1752, 9, 14)),
                Arguments.of("Subtract 1 month across cutover", BritishCutoverDate.of(1752, 10, 12), 1, MONTHS, BritishCutoverDate.of(1752, 9, 23)),
                Arguments.of("Subtract 1 year", BritishCutoverDate.of(2014, 5, 26), 1, YEARS, BritishCutoverDate.of(2013, 5, 26))
            );
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("minusProvider")
        void minus_withTemporalUnit_shouldSubtractAmountCorrectly(String description, BritishCutoverDate baseDate, long amount, TemporalUnit unit, BritishCutoverDate expectedDate) {
            assertEquals(expectedDate, baseDate.minus(amount, unit));
        }

        @Test
        void plus_withUnsupportedUnit_shouldThrowException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> BritishCutoverDate.of(2012, 6, 30).plus(1, MINUTES));
        }
    }

    //-----------------------------------------------------------------------
    // Tests for until(...)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("until(...) calculations")
    class UntilTests {

        @Test
        void until_aTemporalRepresentingTheSameDate_shouldReturnZero() {
            BritishCutoverDate date = BritishCutoverDate.of(2012, 7, 5);
            LocalDate isoDate = LocalDate.of(2012, 7, 5);

            // until(ChronoLocalDate)
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), date.until(date));
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), date.until(isoDate));

            // until(Temporal)
            assertEquals(Period.ZERO, isoDate.until(date));
        }

        private static Stream<Arguments> periodUntilProvider() {
            // description, start, end, expectedYears, expectedMonths, expectedDays
            return Stream.of(
                Arguments.of("Simple one day diff", BritishCutoverDate.of(1752, 7, 1), BritishCutoverDate.of(1752, 7, 2), 0, 0, 1),
                Arguments.of("Period crossing cutover", BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 14), 0, 0, 1),
                Arguments.of("Month period crossing cutover", BritishCutoverDate.of(1752, 8, 2), BritishCutoverDate.of(1752, 10, 2), 0, 2, 0),
                Arguments.of("Reverse period crossing cutover", BritishCutoverDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 2), 0, 0, -1)
            );
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("periodUntilProvider")
        void until_chronoLocalDate_shouldReturnCorrectPeriod(String description, BritishCutoverDate start, BritishCutoverDate end, int expectedYears, int expectedMonths, int expectedDays) {
            ChronoPeriod expectedPeriod = BritishCutoverChronology.INSTANCE.period(expectedYears, expectedMonths, expectedDays);
            assertEquals(expectedPeriod, start.until(end));
        }

        @ParameterizedTest(name = "[{index}] {0} (property check)")
        @MethodSource("periodUntilProvider")
        void plus_periodFromUntil_shouldReturnEndDate(String description, BritishCutoverDate start, BritishCutoverDate end, int y, int m, int d) {
            ChronoPeriod period = start.until(end);
            assertEquals(end, start.plus(period));
        }

        private static Stream<Arguments> untilUnitProvider() {
            // description, start, end, unit, expectedAmount
            return Stream.of(
                Arguments.of("Days across cutover", BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 14), DAYS, 1L),
                Arguments.of("Weeks across cutover", BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 20), WEEKS, 1L),
                Arguments.of("Months across cutover", BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 10, 2), MONTHS, 1L),
                Arguments.of("Years after cutover", BritishCutoverDate.of(2014, 5, 26), BritishCutoverDate.of(2015, 5, 26), YEARS, 1L)
            );
        }

        @ParameterizedTest(name = "[{index}] {0}")
        @MethodSource("untilUnitProvider")
        void until_temporalUnit_shouldCalculateAmountBetweenDates(String description, BritishCutoverDate start, BritishCutoverDate end, TemporalUnit unit, long expectedAmount) {
            assertEquals(expectedAmount, start.until(end, unit));
        }
    }

    //-----------------------------------------------------------------------
    // Test for toString()
    //-----------------------------------------------------------------------
    @Test
    void toString_shouldReturnFormattedString() {
        assertEquals("BritishCutover AD 1-01-01", BritishCutoverDate.of(1, 1, 1).toString());
        assertEquals("BritishCutover AD 2012-06-23", BritishCutoverDate.of(2012, 6, 23).toString());
        assertEquals("BritishCutover BC 1-12-31", BritishCutoverDate.of(0, 12, 31).toString());
    }
}