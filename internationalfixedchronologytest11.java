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
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("InternationalFixedChronology and InternationalFixedDate")
class InternationalFixedChronologyTest {

    // Helper to create an InternationalFixedDate for brevity in data providers
    private static InternationalFixedDate ifd(int year, int month, int day) {
        return InternationalFixedDate.of(year, month, day);
    }

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleDatePairsProvider() {
        return Stream.of(
            // Common year dates
            Arguments.of(ifd(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(ifd(1, 1, 2), LocalDate.of(1, 1, 2)),
            Arguments.of(ifd(1, 6, 27), LocalDate.of(1, 6, 16)),
            Arguments.of(ifd(1, 6, 28), LocalDate.of(1, 6, 17)),
            Arguments.of(ifd(1, 7, 1), LocalDate.of(1, 6, 18)),
            Arguments.of(ifd(1, 7, 2), LocalDate.of(1, 6, 19)),
            Arguments.of(ifd(1, 13, 27), LocalDate.of(1, 12, 29)),
            Arguments.of(ifd(1, 13, 28), LocalDate.of(1, 12, 30)),
            Arguments.of(ifd(1, 13, 29), LocalDate.of(1, 12, 31)), // Year Day
            Arguments.of(ifd(2, 1, 1), LocalDate.of(2, 1, 1)),
            // Leap year dates (year 4)
            Arguments.of(ifd(4, 6, 27), LocalDate.of(4, 6, 15)),
            Arguments.of(ifd(4, 6, 28), LocalDate.of(4, 6, 16)),
            Arguments.of(ifd(4, 6, 29), LocalDate.of(4, 6, 17)), // Leap Day
            Arguments.of(ifd(4, 7, 1), LocalDate.of(4, 6, 18)),
            Arguments.of(ifd(4, 7, 2), LocalDate.of(4, 6, 19)),
            Arguments.of(ifd(4, 13, 28), LocalDate.of(4, 12, 30)),
            Arguments.of(ifd(4, 13, 29), LocalDate.of(4, 12, 31)), // Year Day
            // Modern dates
            Arguments.of(ifd(2012, 6, 15), LocalDate.of(2012, 6, 3)),
            Arguments.of(ifd(2012, 6, 16), LocalDate.of(2012, 6, 4))
        );
    }

    static Stream<Arguments> invalidDateProvider() {
        return Stream.of(
            Arguments.of(-1, 13, 28, "year must be 1 or greater"),
            Arguments.of(0, 1, 1, "year must be 1 or greater"),
            Arguments.of(1900, -2, 1, "month must be between 1 and 13"),
            Arguments.of(1900, 14, 1, "month must be between 1 and 13"),
            Arguments.of(1900, 1, -1, "day of month must be positive"),
            Arguments.of(1900, 1, 0, "day of month must be positive"),
            Arguments.of(1900, 1, 29, "day 29 is invalid for a 28-day month"),
            Arguments.of(1900, 2, 29, "day 29 is invalid for a 28-day month"),
            Arguments.of(1900, 13, 30, "day 30 is invalid for a 29-day month (Year Day month)")
        );
    }

    static Stream<Integer> nonLeapYearsProvider() {
        return Stream.of(1, 100, 200, 300, 1900);
    }

    static Stream<Arguments> lengthOfMonthProvider() {
        return Stream.of(
            Arguments.of(1900, 1, 28),
            Arguments.of(1900, 6, 28),
            Arguments.of(1900, 12, 28),
            Arguments.of(1900, 13, 29), // Year Day month
            Arguments.of(1904, 6, 29)   // Leap Day month
        );
    }

    static Stream<Arguments> fieldRangesProvider() {
        return Stream.of(
            // --- Day of Month ---
            Arguments.of(ifd(2012, 1, 23), DAY_OF_MONTH, ValueRange.of(1, 28)),
            Arguments.of(ifd(2012, 6, 23), DAY_OF_MONTH, ValueRange.of(1, 29)), // Leap month
            Arguments.of(ifd(2012, 13, 23), DAY_OF_MONTH, ValueRange.of(1, 29)),// Year Day month
            // --- Day of Year ---
            Arguments.of(ifd(2011, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 365)), // Common year
            Arguments.of(ifd(2012, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 366)), // Leap year
            // --- Month of Year ---
            Arguments.of(ifd(2012, 1, 23), MONTH_OF_YEAR, ValueRange.of(1, 13)),
            // --- Aligned Week of Month ---
            Arguments.of(ifd(2012, 1, 23), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
            Arguments.of(ifd(2012, 6, 29), ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)), // Leap Day
            Arguments.of(ifd(2012, 13, 29), ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)) // Year Day
        );
    }

    static Stream<Arguments> fieldValuesProvider() {
        return Stream.of(
            // --- Date: 2014-05-26 (common year) ---
            Arguments.of(ifd(2014, 5, 26), DAY_OF_WEEK, 5L),
            Arguments.of(ifd(2014, 5, 26), DAY_OF_MONTH, 26L),
            // DAY_OF_YEAR: (4 months * 28 days) + 26 = 138
            Arguments.of(ifd(2014, 5, 26), DAY_OF_YEAR, 138L),
            Arguments.of(ifd(2014, 5, 26), ALIGNED_WEEK_OF_MONTH, 4L),
            Arguments.of(ifd(2014, 5, 26), ALIGNED_WEEK_OF_YEAR, 20L),
            Arguments.of(ifd(2014, 5, 26), MONTH_OF_YEAR, 5L),
            // PROLEPTIC_MONTH: (2014 - 1) * 13 + (5 - 1) = 26173.
            // The original test used a non-standard formula: 2014 * 13 + 5 - 1 = 26186.
            // We keep the original's logic to not alter the test's intent.
            Arguments.of(ifd(2014, 5, 26), PROLEPTIC_MONTH, 2014L * 13 + 5 - 1),
            Arguments.of(ifd(2014, 5, 26), YEAR, 2014L),
            Arguments.of(ifd(2014, 5, 26), ERA, 1L),

            // --- Date: 2012-06-29 (Leap Day) ---
            Arguments.of(ifd(2012, 6, 29), DAY_OF_WEEK, 0L), // Special value for Leap Day
            Arguments.of(ifd(2012, 6, 29), DAY_OF_MONTH, 29L),
            // DAY_OF_YEAR: 6 * 28 + 1 (Leap Day) = 169
            Arguments.of(ifd(2012, 6, 29), DAY_OF_YEAR, 169L),
            Arguments.of(ifd(2012, 6, 29), ALIGNED_WEEK_OF_MONTH, 0L), // Special value
            Arguments.of(ifd(2012, 6, 29), ALIGNED_WEEK_OF_YEAR, 0L),  // Special value
            Arguments.of(ifd(2012, 6, 29), MONTH_OF_YEAR, 6L),

            // --- Date: 2014-13-29 (Year Day) ---
            Arguments.of(ifd(2014, 13, 29), DAY_OF_WEEK, 0L), // Special value for Year Day
            Arguments.of(ifd(2014, 13, 29), DAY_OF_MONTH, 29L),
            // DAY_OF_YEAR: 13 * 28 + 1 = 365
            Arguments.of(ifd(2014, 13, 29), DAY_OF_YEAR, 365L),
            Arguments.of(ifd(2014, 13, 29), ALIGNED_WEEK_OF_MONTH, 0L), // Special value
            Arguments.of(ifd(2014, 13, 29), ALIGNED_WEEK_OF_YEAR, 0L),  // Special value
            Arguments.of(ifd(2014, 13, 29), MONTH_OF_YEAR, 13L)
        );
    }

    static Stream<Arguments> withFieldProvider() {
        return Stream.of(
            // --- With DAY_OF_WEEK ---
            Arguments.of(ifd(2014, 5, 26), DAY_OF_WEEK, 1, ifd(2014, 5, 22)),
            // --- With DAY_OF_MONTH ---
            Arguments.of(ifd(2014, 5, 26), DAY_OF_MONTH, 28, ifd(2014, 5, 28)),
            // --- With DAY_OF_YEAR ---
            Arguments.of(ifd(2014, 5, 26), DAY_OF_YEAR, 365, ifd(2014, 13, 29)),
            // --- With MONTH_OF_YEAR ---
            Arguments.of(ifd(2014, 5, 26), MONTH_OF_YEAR, 4, ifd(2014, 4, 26)),
            // --- With YEAR ---
            Arguments.of(ifd(2014, 5, 26), YEAR, 2012, ifd(2012, 5, 26)),
            // --- Special case: setting a field on a leap day ---
            Arguments.of(ifd(2012, 6, 29), YEAR, 2013, ifd(2013, 6, 28)), // 2013 is not a leap year
            // --- Special case: setting a field on a year day ---
            Arguments.of(ifd(2014, 13, 29), MONTH_OF_YEAR, 1, ifd(2014, 1, 28)) // day clamps to 28
        );
    }

    static Stream<Arguments> plusProvider() {
        return Stream.of(
            Arguments.of(ifd(2014, 5, 26), 8, DAYS, ifd(2014, 6, 6)),
            Arguments.of(ifd(2014, 5, 26), 3, WEEKS, ifd(2014, 6, 19)),
            Arguments.of(ifd(2014, 5, 26), 3, MONTHS, ifd(2014, 8, 26)),
            Arguments.of(ifd(2014, 5, 26), 3, YEARS, ifd(2017, 5, 26)),
            Arguments.of(ifd(2014, 5, 26), 3, DECADES, ifd(2044, 5, 26)),
            Arguments.of(ifd(2014, 5, 26), 3, CENTURIES, ifd(2314, 5, 26)),
            Arguments.of(ifd(2014, 5, 26), 3, MILLENNIA, ifd(5014, 5, 26))
        );
    }

    static Stream<Arguments> untilPeriodProvider() {
        return Stream.of(
            // Zero period
            Arguments.of(ifd(2014, 5, 26), ifd(2014, 5, 26), InternationalFixedChronology.INSTANCE.period(0, 0, 0)),
            // Days only
            Arguments.of(ifd(2014, 5, 26), ifd(2014, 6, 4), InternationalFixedChronology.INSTANCE.period(0, 0, 6)),
            // Months only
            Arguments.of(ifd(2014, 5, 26), ifd(2014, 6, 26), InternationalFixedChronology.INSTANCE.period(0, 1, 0)),
            // Years only
            Arguments.of(ifd(2014, 5, 26), ifd(2015, 5, 26), InternationalFixedChronology.INSTANCE.period(1, 0, 0)),
            // Mixed
            Arguments.of(ifd(2014, 5, 26), ifd(2015, 6, 28), InternationalFixedChronology.INSTANCE.period(1, 1, 2)),
            // Spanning leap day
            Arguments.of(ifd(2011, 12, 28), ifd(2012, 13, 1), InternationalFixedChronology.INSTANCE.period(1, 0, 1)),
            // From leap day
            Arguments.of(ifd(2004, 6, 29), ifd(2004, 13, 29), InternationalFixedChronology.INSTANCE.period(0, 7, 0)),
            // From year day
            Arguments.of(ifd(2003, 13, 29), ifd(2004, 6, 29), InternationalFixedChronology.INSTANCE.period(0, 6, 0))
        );
    }

    //-----------------------------------------------------------------------
    // Test Cases
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Creation and Validation")
    class CreationAndValidation {

        @ParameterizedTest(name = "Fixed: {0}, ISO: {1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        void fromEpochDay_shouldCreateCorrectDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest(name = "y={0}, m={1}, d={2} -> {3}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#invalidDateProvider")
        void of_withInvalidDateComponents_throwsException(int year, int month, int day, String reason) {
            assertThrows(DateTimeException.class,
                () -> InternationalFixedDate.of(year, month, day),
                "Should throw for " + reason);
        }

        @ParameterizedTest(name = "Year {0} is not a leap year")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#nonLeapYearsProvider")
        void of_forLeapDayInNonLeapYear_throwsException(int year) {
            assertThrows(DateTimeException.class,
                () -> InternationalFixedDate.of(year, 6, 29), // Month 6, Day 29 is the leap day
                "Creating a leap day in a non-leap year should fail.");
        }

        @ParameterizedTest(name = "For {0}-{1}, length should be {2}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#lengthOfMonthProvider")
        void lengthOfMonth_returnsCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, 1).lengthOfMonth());
        }
    }

    @Nested
    @DisplayName("Conversion")
    class Conversion {

        @ParameterizedTest(name = "Fixed: {0} -> ISO: {1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        void fromFixedDate_shouldConvertToCorrectIsoDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(fixedDate));
        }

        @ParameterizedTest(name = "ISO: {1} -> Fixed: {0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        void fromIsoDate_shouldConvertToCorrectFixedDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest(name = "Fixed: {0} -> Epoch Day: {1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        void toEpochDay_shouldReturnCorrectValue(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), fixedDate.toEpochDay());
        }

        @ParameterizedTest(name = "Fixed: {0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        void chronologyDate_fromIsoTemporal_shouldReturnCorrectFixedDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Field and Range Access")
    class FieldAndRangeAccess {

        @ParameterizedTest(name = "range of {1} for {0} should be {2}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#fieldRangesProvider")
        void range_forVariousFields_returnsCorrectRange(InternationalFixedDate date, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, date.range(field));
        }

        @ParameterizedTest(name = "getLong({1}) on {0} should be {2}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#fieldValuesProvider")
        void getLong_forVariousFields_returnsCorrectValue(InternationalFixedDate date, TemporalField field, long expected) {
            assertEquals(expected, date.getLong(field));
        }
    }

    @Nested
    @DisplayName("Arithmetic")
    class Arithmetic {

        @ParameterizedTest(name = "{0}.with({1}, {2}) should be {3}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#withFieldProvider")
        void with_forVariousFields_returnsCorrectDate(InternationalFixedDate baseDate, TemporalField field, long value, InternationalFixedDate expectedDate) {
            assertEquals(expectedDate, baseDate.with(field, value));
        }

        @Test
        void with_lastDayOfMonthAdjuster_returnsCorrectDate() {
            InternationalFixedDate date = ifd(2012, 6, 23); // Leap month
            InternationalFixedDate expected = ifd(2012, 6, 29);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @ParameterizedTest(name = "{0}.plus({1}, {2}) should be {3}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#plusProvider")
        void plus_withVariousUnits_calculatesCorrectDate(InternationalFixedDate baseDate, long amount, TemporalUnit unit, InternationalFixedDate expectedDate) {
            assertEquals(expectedDate, baseDate.plus(amount, unit));
        }

        @ParameterizedTest(name = "{3}.minus({1}, {2}) should be {0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#plusProvider")
        void minus_withVariousUnits_calculatesCorrectDate(InternationalFixedDate expectedDate, long amount, TemporalUnit unit, InternationalFixedDate baseDate) {
            assertEquals(expectedDate, baseDate.minus(amount, unit));
        }

        @ParameterizedTest(name = "until({1})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#untilPeriodProvider")
        void until_calculatesCorrectPeriod(InternationalFixedDate start, InternationalFixedDate end, ChronoPeriod expected) {
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest(name = "Fixed: {0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        void until_self_returnsZeroPeriod(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(fixedDate));
        }

        @ParameterizedTest(name = "Fixed: {0}, ISO: {1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        @DisplayName("until() an equivalent ISO date should return a zero period")
        void until_equivalentIsoDate_returnsZeroPeriod(InternationalFixedDate fixedDate, LocalDate isoDate) {
            // .until(Temporal) converts the argument to the correct chronology before comparing
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(isoDate));
        }

        @ParameterizedTest(name = "Fixed: {0}, ISO: {1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        @DisplayName("ISO date .until() an equivalent Fixed date should return a zero period")
        void isoDateUntil_equivalentFixedDate_returnsZeroPeriod(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(fixedDate));
        }
    }

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApi {

        @Test
        void eraOf_withValidValue_returnsCeEra() {
            assertEquals(InternationalFixedEra.CE, InternationalFixedChronology.INSTANCE.eraOf(1));
        }

        @Test
        void eraOf_withInvalidValue_throwsException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(0));
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(2));
        }

        @Test
        void prolepticYear_withInvalidYearOfEra_throwsException() {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 0));
        }
    }

    @Nested
    @DisplayName("Object Methods")
    class ObjectMethods {

        @Test
        void test_toString() {
            assertEquals("Ifc CE 2012/06/23", ifd(2012, 6, 23).toString());
            assertEquals("Ifc CE 2012/06/29", ifd(2012, 6, 29).toString()); // Leap Day
            assertEquals("Ifc CE 2012/13/29", ifd(2012, 13, 29).toString());// Year Day
        }
    }
}