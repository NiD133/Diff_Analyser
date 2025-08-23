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
import java.time.Month;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Comprehensive tests for {@link InternationalFixedChronology} and {@link InternationalFixedDate}.
 *
 * <p>This revised test suite focuses on understandability by:
 * <ul>
 *     <li>Grouping related tests into {@code @Nested} classes with {@code @DisplayName}.</li>
 *     <li>Using descriptive test method names.</li>
 *     <li>Replacing large, opaque {@code Object[][]} data providers with more readable
 *         {@code Stream<Arguments>} or {@code @CsvSource}.</li>
 *     <li>Adding comments to explain the purpose of test data, especially for edge cases.</li>
 *     <li>Consolidating redundant tests into clearer, more focused ones.</li>
 * </ul>
 */
public class InternationalFixedChronologyTest {

    // Helper method for creating dates concisely in tests
    private static InternationalFixedDate ifcDate(int year, int month, int day) {
        return InternationalFixedDate.of(year, month, day);
    }

    //-----------------------------------------------------------------------
    // Sample data used across multiple tests
    //-----------------------------------------------------------------------
    static Stream<Arguments> ifcToIsoDateSamples() {
        return Stream.of(
            Arguments.of(ifcDate(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(ifcDate(1, 6, 28), LocalDate.of(1, 6, 17)), // Month of Sol in non-leap year
            Arguments.of(ifcDate(1, 7, 1), LocalDate.of(1, 6, 18)),
            Arguments.of(ifcDate(1, 13, 29), LocalDate.of(1, 12, 31)), // Year Day
            Arguments.of(ifcDate(4, 6, 29), LocalDate.of(4, 6, 17)), // Leap Day
            Arguments.of(ifcDate(4, 7, 1), LocalDate.of(4, 6, 18)),
            Arguments.of(ifcDate(2012, 6, 15), LocalDate.of(2012, 6, 3)),
            Arguments.of(ifcDate(2012, 6, 16), LocalDate.of(2012, 6, 4))
        );
    }

    //-----------------------------------------------------------------------
    // Factory and Invalid Date Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Factory Methods and Invalid Date Handling")
    class FactoryAndInvalidDateTests {

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @CsvSource({
            // Invalid year
            "0, 1, 1",
            "-1, 1, 1",
            // Invalid month
            "2000, 0, 1",
            "2000, 14, 1",
            // Invalid day of month
            "2000, 1, 0",
            "2000, 1, 29",  // Standard month has 28 days
            "1900, 6, 29",  // Month 6 (Sol) has 28 days in a non-leap year
            "2000, 13, 30" // Month 13 (Year Day) has 29 days
        })
        @DisplayName("of(y, m, d) should throw for invalid date components")
        void of_withInvalidDateParts_shouldThrowDateTimeException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        @ParameterizedTest(name = "Year {0}")
        @CsvSource({"1", "100", "200", "300", "1900"})
        @DisplayName("of(year, 6, 29) should throw for non-leap years")
        void of_withInvalidLeapDay_shouldThrowDateTimeException(int nonLeapYear) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(nonLeapYear, 6, 29));
        }
    }

    //-----------------------------------------------------------------------
    // Conversion Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversions to and from other Chronologies")
    class ConversionTests {

        @ParameterizedTest(name = "{1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#ifcToIsoDateSamples")
        @DisplayName("LocalDate.from(ifcDate) should return correct ISO date")
        void fromIfcDate_shouldReturnCorrectLocalDate(InternationalFixedDate ifcDate, LocalDate expectedIsoDate) {
            assertEquals(expectedIsoDate, LocalDate.from(ifcDate));
        }

        @ParameterizedTest(name = "{1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#ifcToIsoDateSamples")
        @DisplayName("InternationalFixedDate.from(isoDate) should return correct IFC date")
        void fromLocalDate_shouldReturnCorrectIfcDate(InternationalFixedDate expectedIfcDate, LocalDate isoDate) {
            assertEquals(expectedIfcDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#ifcToIsoDateSamples")
        @DisplayName("toEpochDay() should match the equivalent ISO date's epoch day")
        void toEpochDay_shouldMatchIsoDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), ifcDate.toEpochDay());
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#ifcToIsoDateSamples")
        @DisplayName("chronology.dateEpochDay() should create the correct IFC date")
        void dateEpochDay_shouldCreateCorrectIfcDate(InternationalFixedDate expectedIfcDate, LocalDate isoDate) {
            long epochDay = isoDate.toEpochDay();
            assertEquals(expectedIfcDate, InternationalFixedChronology.INSTANCE.dateEpochDay(epochDay));
        }

        @ParameterizedTest(name = "{1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#ifcToIsoDateSamples")
        @DisplayName("chronology.date(temporal) should create the correct IFC date from an ISO date")
        void dateFromTemporal_shouldCreateCorrectIfcDate(InternationalFixedDate expectedIfcDate, LocalDate isoDate) {
            assertEquals(expectedIfcDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }
    }

    //-----------------------------------------------------------------------
    // Field Access and Range Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field Access and Value Ranges")
    class FieldAndRangeTests {

        @ParameterizedTest(name = "Month {1} in year {0} should have {2} days")
        @CsvSource({
            "1900, 1, 28",  // Standard month
            "1900, 6, 28",  // Month 6 in non-leap year
            "1900, 13, 29", // Year Day month
            "1904, 6, 29"   // Month 6 in leap year
        })
        void lengthOfMonth_isCorrectForLeapAndNonLeapYears(int year, int month, int expectedLength) {
            // Test that lengthOfMonth is independent of the day of the month
            InternationalFixedDate date = ifcDate(year, month, 1);
            assertEquals(expectedLength, date.lengthOfMonth());
        }

        static Stream<Arguments> getLongFieldValueSamples() {
            InternationalFixedDate regularDate = ifcDate(2014, 5, 26); // Non-leap year
            InternationalFixedDate leapYearDate = ifcDate(2012, 9, 26); // Leap year
            InternationalFixedDate yearDay = ifcDate(2014, 13, 29);
            InternationalFixedDate leapDay = ifcDate(2012, 6, 29);
            InternationalFixedDate afterLeapDay = ifcDate(2012, 7, 1);

            return Stream.of(
                // --- Regular Date: 2014-05-26 ---
                Arguments.of(regularDate, DAY_OF_WEEK, 5L, "DAY_OF_WEEK"),
                Arguments.of(regularDate, DAY_OF_MONTH, 26L, "DAY_OF_MONTH"),
                Arguments.of(regularDate, DAY_OF_YEAR, (long) 4 * 28 + 26, "DAY_OF_YEAR"),
                Arguments.of(regularDate, MONTH_OF_YEAR, 5L, "MONTH_OF_YEAR"),
                Arguments.of(regularDate, YEAR, 2014L, "YEAR"),
                Arguments.of(regularDate, ERA, 1L, "ERA"),

                // --- Leap Day: 2012-06-29 ---
                Arguments.of(leapDay, DAY_OF_WEEK, 0L, "DAY_OF_WEEK on Leap Day"),
                Arguments.of(leapDay, DAY_OF_MONTH, 29L, "DAY_OF_MONTH on Leap Day"),
                Arguments.of(leapDay, DAY_OF_YEAR, (long) 6 * 28 + 1, "DAY_OF_YEAR on Leap Day"),
                Arguments.of(leapDay, MONTH_OF_YEAR, 6L, "MONTH_OF_YEAR on Leap Day"),
                Arguments.of(leapDay, ALIGNED_WEEK_OF_YEAR, 0L, "ALIGNED_WEEK_OF_YEAR on Leap Day"),

                // --- Year Day: 2014-13-29 ---
                Arguments.of(yearDay, DAY_OF_WEEK, 0L, "DAY_OF_WEEK on Year Day"),
                Arguments.of(yearDay, DAY_OF_MONTH, 29L, "DAY_OF_MONTH on Year Day"),
                Arguments.of(yearDay, DAY_OF_YEAR, (long) 13 * 28 + 1, "DAY_OF_YEAR on Year Day"),
                Arguments.of(yearDay, MONTH_OF_YEAR, 13L, "MONTH_OF_YEAR on Year Day"),
                Arguments.of(yearDay, ALIGNED_WEEK_OF_YEAR, 0L, "ALIGNED_WEEK_OF_YEAR on Year Day"),

                // --- Day after Leap Day: 2012-07-01 ---
                Arguments.of(afterLeapDay, DAY_OF_YEAR, (long) 6 * 28 + 2, "DAY_OF_YEAR after Leap Day"),
                Arguments.of(afterLeapDay, ALIGNED_WEEK_OF_YEAR, 25L, "ALIGNED_WEEK_OF_YEAR after Leap Day")
            );
        }

        @ParameterizedTest(name = "[{index}] {3} on {0}")
        @MethodSource("getLongFieldValueSamples")
        void getLong_shouldReturnCorrectValueForField(InternationalFixedDate date, TemporalField field, long expected, String description) {
            assertEquals(expected, date.getLong(field));
        }

        static Stream<Arguments> rangeOfFieldSamples() {
            return Stream.of(
                // --- DAY_OF_MONTH ---
                Arguments.of(ifcDate(2012, 1, 1), DAY_OF_MONTH, ValueRange.of(1, 28), "Regular month"),
                Arguments.of(ifcDate(2012, 6, 1), DAY_OF_MONTH, ValueRange.of(1, 29), "Leap Day month"),
                Arguments.of(ifcDate(2011, 6, 1), DAY_OF_MONTH, ValueRange.of(1, 28), "Non-Leap Day month"),
                Arguments.of(ifcDate(2012, 13, 1), DAY_OF_MONTH, ValueRange.of(1, 29), "Year Day month"),

                // --- DAY_OF_YEAR ---
                Arguments.of(ifcDate(2012, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 366), "Leap year"),
                Arguments.of(ifcDate(2011, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 365), "Non-leap year"),

                // --- MONTH_OF_YEAR ---
                Arguments.of(ifcDate(2012, 1, 1), MONTH_OF_YEAR, ValueRange.of(1, 13), "Any year"),

                // --- DAY_OF_WEEK (special handling for leap/year days) ---
                Arguments.of(ifcDate(2012, 1, 1), DAY_OF_WEEK, ValueRange.of(1, 7), "Regular day"),
                Arguments.of(ifcDate(2012, 6, 29), DAY_OF_WEEK, ValueRange.of(0, 0), "Leap Day"),
                Arguments.of(ifcDate(2012, 13, 29), DAY_OF_WEEK, ValueRange.of(0, 0), "Year Day")
            );
        }

        @ParameterizedTest(name = "[{index}] {3}: range of {1} for {0}")
        @MethodSource("rangeOfFieldSamples")
        void range_shouldReturnCorrectRangeForField(InternationalFixedDate date, TemporalField field, ValueRange expectedRange, String description) {
            assertEquals(expectedRange, date.range(field), "Failed on: " + description);
        }
    }

    //-----------------------------------------------------------------------
    // Date Manipulation Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Manipulation with plus, minus, and with")
    class ManipulationTests {

        static Stream<Arguments> withFieldValueSamples() {
            return Stream.of(
                // Adjust fields on a standard date
                Arguments.of(ifcDate(2014, 5, 26), DAY_OF_MONTH, 1, ifcDate(2014, 5, 1)),
                Arguments.of(ifcDate(2014, 5, 26), MONTH_OF_YEAR, 8, ifcDate(2014, 8, 26)),
                Arguments.of(ifcDate(2014, 5, 26), YEAR, 2012, ifcDate(2012, 5, 26)),
                Arguments.of(ifcDate(2014, 5, 26), DAY_OF_YEAR, 1, ifcDate(2014, 1, 1)),
                // Adjusting a leap day to a non-leap year changes the day
                Arguments.of(ifcDate(2012, 6, 29), YEAR, 2013, ifcDate(2013, 6, 28)),
                // Adjusting month of a day that doesn't exist in the target month
                Arguments.of(ifcDate(2012, 6, 29), MONTH_OF_YEAR, 7, ifcDate(2012, 7, 28))
            );
        }

        @ParameterizedTest(name = "{0} with({1}, {2}) -> {3}")
        @MethodSource("withFieldValueSamples")
        void with_shouldAdjustFieldCorrectly(InternationalFixedDate baseDate, TemporalField field, long value, InternationalFixedDate expectedDate) {
            assertEquals(expectedDate, baseDate.with(field, value));
        }

        @Test
        @DisplayName("with() should throw for unsupported fields like ISO Month")
        void with_unsupportedTemporal_shouldThrowException() {
            InternationalFixedDate date = ifcDate(2000, 1, 4);
            assertThrows(UnsupportedTemporalTypeException.class, () -> date.with(Month.APRIL));
        }

        static Stream<Arguments> plusSamples() {
            return Stream.of(
                // --- Standard additions ---
                Arguments.of(ifcDate(2014, 5, 26), 8, DAYS, ifcDate(2014, 6, 6)),
                Arguments.of(ifcDate(2014, 5, 26), 3, WEEKS, ifcDate(2014, 6, 19)),
                Arguments.of(ifcDate(2014, 5, 26), 3, MONTHS, ifcDate(2014, 8, 26)),
                Arguments.of(ifcDate(2014, 5, 26), 3, YEARS, ifcDate(2017, 5, 26)),
                Arguments.of(ifcDate(2014, 5, 26), 3, DECADES, ifcDate(2044, 5, 26)),
                Arguments.of(ifcDate(2014, 5, 26), 3, CENTURIES, ifcDate(2314, 5, 26)),
                Arguments.of(ifcDate(2014, 5, 26), 3, MILLENNIA, ifcDate(5014, 5, 26)),
                // --- Additions crossing year boundaries ---
                Arguments.of(ifcDate(2014, 13, 26), 3, WEEKS, ifcDate(2015, 1, 19)),
                // --- Additions crossing leap/year days ---
                Arguments.of(ifcDate(2012, 6, 21), 52 + 1, WEEKS, ifcDate(2013, 6, 28)),
                Arguments.of(ifcDate(2014, 13, 29), 8, DAYS, ifcDate(2015, 1, 8)), // Add from Year Day
                Arguments.of(ifcDate(2012, 6, 29), 8, DAYS, ifcDate(2012, 7, 8))  // Add from Leap Day
            );
        }

        @ParameterizedTest(name = "{0} plus({1}, {2}) -> {3}")
        @MethodSource("plusSamples")
        void plus_shouldAddAmountCorrectly(InternationalFixedDate baseDate, long amount, TemporalUnit unit, InternationalFixedDate expectedDate) {
            assertEquals(expectedDate, baseDate.plus(amount, unit));
        }

        @ParameterizedTest(name = "{3} minus({1}, {2}) -> {0}")
        @MethodSource("plusSamples") // Reusing plusSamples for symmetry
        void minus_shouldSubtractAmountCorrectly(InternationalFixedDate expectedDate, long amount, TemporalUnit unit, InternationalFixedDate baseDate) {
            assertEquals(expectedDate, baseDate.minus(amount, unit));
        }

        static Stream<Arguments> untilPeriodSamples() {
            return Stream.of(
                Arguments.of(ifcDate(2014, 5, 26), ifcDate(2014, 5, 26), InternationalFixedChronology.INSTANCE.period(0, 0, 0)),
                Arguments.of(ifcDate(2014, 5, 26), ifcDate(2014, 6, 4), InternationalFixedChronology.INSTANCE.period(0, 0, 6)),
                Arguments.of(ifcDate(2014, 5, 26), ifcDate(2014, 6, 26), InternationalFixedChronology.INSTANCE.period(0, 1, 0)),
                Arguments.of(ifcDate(2014, 5, 26), ifcDate(2015, 5, 26), InternationalFixedChronology.INSTANCE.period(1, 0, 0)),
                // --- Until involving leap/year days ---
                Arguments.of(ifcDate(2003, 13, 29), ifcDate(2004, 6, 29), InternationalFixedChronology.INSTANCE.period(0, 6, 0)),
                Arguments.of(ifcDate(2004, 6, 29), ifcDate(2004, 13, 29), InternationalFixedChronology.INSTANCE.period(0, 7, 0))
            );
        }

        @ParameterizedTest(name = "{0}.until({1}) -> {2}")
        @MethodSource("untilPeriodSamples")
        void until_period_shouldReturnCorrectPeriod(InternationalFixedDate start, InternationalFixedDate end, ChronoPeriod expected) {
            assertEquals(expected, start.until(end));
        }
    }

    //-----------------------------------------------------------------------
    // Temporal Adjuster Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Temporal Adjusters")
    class AdjusterTests {

        static Stream<Arguments> lastDayOfMonthSamples() {
            return Stream.of(
                Arguments.of(ifcDate(2012, 6, 23), ifcDate(2012, 6, 29)), // Leap month
                Arguments.of(ifcDate(2009, 6, 23), ifcDate(2009, 6, 28)), // Non-leap month
                Arguments.of(ifcDate(2007, 13, 23), ifcDate(2007, 13, 29)) // Year Day month
            );
        }

        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("lastDayOfMonthSamples")
        @DisplayName("with(lastDayOfMonth()) should return the correct last day")
        void withLastDayOfMonth_shouldReturnCorrectDate(InternationalFixedDate base, InternationalFixedDate expected) {
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    //-----------------------------------------------------------------------
    // Chronology-specific Behavior
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Chronology-specific Behavior")
    class ChronologySpecificTests {

        @Test
        @DisplayName("until() a date in the same chronology should return zero period")
        void until_self_returnsZeroPeriod() {
            InternationalFixedDate date = ifcDate(2012, 6, 15);
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        @Test
        @DisplayName("until() a date in a different chronology should return zero period")
        void until_differentChronology_returnsZeroPeriod() {
            InternationalFixedDate ifcDate = ifcDate(2012, 6, 15);
            LocalDate isoDate = LocalDate.of(2012, 6, 3);
            // This behavior is defined by ChronoLocalDate.until(ChronoLocalDate)
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(ifcDate));
        }

        @ParameterizedTest
        @CsvSource({"-1", "0", "2"})
        @DisplayName("eraOf() should throw for invalid era values")
        void eraOf_withInvalidValue_shouldThrowException(int invalidEra) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(invalidEra));
        }

        @ParameterizedTest
        @CsvSource({"-10", "-1", "0"})
        @DisplayName("prolepticYear() should throw for invalid year of era")
        void prolepticYear_withInvalidYearOfEra_shouldThrowException(int invalidYearOfEra) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, invalidYearOfEra));
        }
    }

    //-----------------------------------------------------------------------
    // Object Method Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Object Methods")
    class ObjectMethodTests {

        static Stream<Arguments> toStringSamples() {
            return Stream.of(
                Arguments.of(ifcDate(1, 1, 1), "Ifc CE 1/01/01"),
                Arguments.of(ifcDate(2012, 6, 23), "Ifc CE 2012/06/23"),
                Arguments.of(ifcDate(1, 13, 29), "Ifc CE 1/13/29"),
                Arguments.of(ifcDate(2012, 6, 29), "Ifc CE 2012/06/29"), // Leap Day
                Arguments.of(ifcDate(2012, 13, 29), "Ifc CE 2012/13/29") // Year Day
            );
        }

        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("toStringSamples")
        void toString_shouldReturnCorrectFormat(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}