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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

/**
 * Tests for {@link InternationalFixedChronology} and {@link InternationalFixedDate}.
 */
public class InternationalFixedChronologyTest {

    // Helper to make date creation in providers cleaner
    private static InternationalFixedDate date(int year, int month, int day) {
        return InternationalFixedDate.of(year, month, day);
    }

    @Nested
    @DisplayName("Factory, Conversion, and Basic Properties")
    class FactoryAndConversionTests {

        static Stream<Arguments> sampleDateConversions() {
            return Stream.of(
                // Early dates
                Arguments.of(date(1, 1, 1), LocalDate.of(1, 1, 1)),
                Arguments.of(date(1, 1, 2), LocalDate.of(1, 1, 2)),
                // Around the special "Leap Day" (inserted after June 28th in leap years)
                Arguments.of(date(1, 6, 27), LocalDate.of(1, 6, 16)),
                Arguments.of(date(1, 6, 28), LocalDate.of(1, 6, 17)),
                Arguments.of(date(1, 7, 1), LocalDate.of(1, 6, 18)),
                Arguments.of(date(1, 7, 2), LocalDate.of(1, 6, 19)),
                // Around the special "Year Day" (inserted after month 13, day 28)
                Arguments.of(date(1, 13, 27), LocalDate.of(1, 12, 29)),
                Arguments.of(date(1, 13, 28), LocalDate.of(1, 12, 30)),
                Arguments.of(date(1, 13, 29), LocalDate.of(1, 12, 31)), // This is "Year Day"
                Arguments.of(date(2, 1, 1), LocalDate.of(2, 1, 1)),
                // Leap year (year 4)
                Arguments.of(date(4, 6, 27), LocalDate.of(4, 6, 15)),
                Arguments.of(date(4, 6, 28), LocalDate.of(4, 6, 16)),
                Arguments.of(date(4, 6, 29), LocalDate.of(4, 6, 17)), // This is "Leap Day"
                Arguments.of(date(4, 7, 1), LocalDate.of(4, 6, 18)),
                Arguments.of(date(4, 7, 2), LocalDate.of(4, 6, 19)),
                Arguments.of(date(4, 13, 27), LocalDate.of(4, 12, 29)),
                Arguments.of(date(4, 13, 28), LocalDate.of(4, 12, 30)),
                Arguments.of(date(4, 13, 29), LocalDate.of(4, 12, 31)), // This is "Year Day"
                Arguments.of(date(5, 1, 1), LocalDate.of(5, 1, 1)),
                // Century non-leap year (year 100)
                Arguments.of(date(100, 6, 27), LocalDate.of(100, 6, 16)),
                Arguments.of(date(100, 6, 28), LocalDate.of(100, 6, 17)),
                Arguments.of(date(100, 7, 1), LocalDate.of(100, 6, 18)),
                // Century leap year (year 400)
                Arguments.of(date(400, 6, 29), LocalDate.of(400, 6, 17)), // "Leap Day"
                // Modern dates
                Arguments.of(date(2012, 6, 15), LocalDate.of(2012, 6, 3)),
                Arguments.of(date(2012, 6, 16), LocalDate.of(2012, 6, 4))
            );
        }

        @ParameterizedTest
        @MethodSource("sampleDateConversions")
        void toLocalDate_convertsCorrectly(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso, LocalDate.from(fixed));
        }

        @ParameterizedTest
        @MethodSource("sampleDateConversions")
        void fromLocalDate_convertsCorrectly(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("sampleDateConversions")
        void dateFromEpochDay_convertsCorrectly(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("sampleDateConversions")
        void toEpochDay_convertsCorrectly(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso.toEpochDay(), fixed.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("sampleDateConversions")
        void until_zeroPeriodForSameDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixed.until(fixed));
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixed.until(iso));
            assertEquals(Period.ZERO, iso.until(fixed));
        }

        @ParameterizedTest
        @MethodSource("sampleDateConversions")
        void chronologyDateFromTemporal_convertsCorrectly(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedChronology.INSTANCE.date(iso));
        }

        static Stream<Arguments> invalidDatePartsProvider() {
            return Stream.of(
                // Invalid year
                Arguments.of(0, 1, 1, "Year zero is invalid"),
                // Invalid month
                Arguments.of(1900, -2, 1, "Negative month"),
                Arguments.of(1900, 0, 1, "Zero month"),
                Arguments.of(1900, 14, 1, "Month > 13"),
                // Invalid day of month
                Arguments.of(1900, 1, -1, "Negative day"),
                Arguments.of(1900, 1, 0, "Zero day"),
                Arguments.of(1900, 1, 29, "Day > 28 for a standard month"),
                Arguments.of(1900, 2, 29, "Day > 28 for a standard month"),
                Arguments.of(1900, 13, 30, "Day > 29 for the 13th month")
            );
        }

        @ParameterizedTest(name = "[{index}] {3}")
        @MethodSource("invalidDatePartsProvider")
        void of_throwsExceptionForInvalidDateParts(int year, int month, int dom, String description) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dom));
        }

        static Stream<Integer> nonLeapYearsProvider() {
            return Stream.of(1, 100, 200, 300, 1900);
        }

        @ParameterizedTest
        @MethodSource("nonLeapYearsProvider")
        void of_throwsExceptionForLeapDayInNonLeapYear(int year) {
            // Month 6, Day 29 is the leap day, which is only valid in leap years.
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }

        static Stream<Arguments> monthLengthProvider() {
            return Stream.of(
                Arguments.of(1900, 1, 28),
                Arguments.of(1900, 6, 28), // Month 6 has 28 days in a non-leap year
                Arguments.of(1900, 12, 28),
                Arguments.of(1900, 13, 29), // Month 13 always has 29 days (28 + Year Day)
                Arguments.of(1904, 6, 29)  // Month 6 has 29 days in a leap year
            );
        }

        @ParameterizedTest
        @MethodSource("monthLengthProvider")
        void lengthOfMonth_returnsCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, date(year, month, 1).lengthOfMonth());
        }
    }

    @Nested
    @DisplayName("Date Field Access and Range")
    class FieldAndRangeTests {

        static Stream<Arguments> fieldRangeProvider() {
            return Stream.of(
                // --- Day of Month ---
                Arguments.of(date(2012, 1, 23), DAY_OF_MONTH, ValueRange.of(1, 28), "Standard month"),
                Arguments.of(date(2012, 6, 23), DAY_OF_MONTH, ValueRange.of(1, 29), "Leap month (June in leap year)"),
                Arguments.of(date(2011, 6, 23), DAY_OF_MONTH, ValueRange.of(1, 28), "Non-leap month (June in non-leap year)"),
                Arguments.of(date(2012, 13, 23), DAY_OF_MONTH, ValueRange.of(1, 29), "Final month (always 29 days)"),

                // --- Day of Year ---
                Arguments.of(date(2011, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 365), "Standard year"),
                Arguments.of(date(2012, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 366), "Leap year"),

                // --- Month of Year ---
                Arguments.of(date(2012, 1, 1), MONTH_OF_YEAR, ValueRange.of(1, 13), "Always 13 months"),

                // --- Aligned Week of Month ---
                Arguments.of(date(2012, 1, 23), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4), "Standard day"),
                Arguments.of(date(2012, 6, 29), ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0), "Leap Day is outside weeks"),
                Arguments.of(date(2012, 13, 29), ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0), "Year Day is outside weeks")
            );
        }

        @ParameterizedTest(name = "[{index}] {3}")
        @MethodSource("fieldRangeProvider")
        void range_returnsCorrectRangeForField(InternationalFixedDate date, TemporalField field, ValueRange expectedRange, String description) {
            assertEquals(expectedRange, date.range(field));
        }

        static Stream<Arguments> fieldValueProvider() {
            return Stream.of(
                // --- Regular day in a non-leap year ---
                Arguments.of(date(2014, 5, 26), DAY_OF_WEEK, 5L, "Day of week"),
                Arguments.of(date(2014, 5, 26), DAY_OF_MONTH, 26L, "Day of month"),
                Arguments.of(date(2014, 5, 26), DAY_OF_YEAR, 138L, "Day of year (4*28 + 26)"),
                Arguments.of(date(2014, 5, 26), ALIGNED_WEEK_OF_MONTH, 4L, "Week of month"),
                Arguments.of(date(2014, 5, 26), ALIGNED_WEEK_OF_YEAR, 20L, "Week of year"),
                Arguments.of(date(2014, 5, 26), MONTH_OF_YEAR, 5L, "Month of year"),
                Arguments.of(date(2014, 5, 26), PROLEPTIC_MONTH, 2014 * 13 + 4, "Proleptic month"),
                Arguments.of(date(2014, 5, 26), YEAR, 2014L, "Year"),
                Arguments.of(date(2014, 5, 26), ERA, 1L, "Era"),

                // --- Leap Day (June 29 in a leap year) ---
                Arguments.of(date(2012, 6, 29), DAY_OF_WEEK, 0L, "Leap Day is not a day of week"),
                Arguments.of(date(2012, 6, 29), DAY_OF_MONTH, 29L, "Leap Day is 29th of June"),
                Arguments.of(date(2012, 6, 29), DAY_OF_YEAR, 169L, "Day of year (6*28 + 1)"),
                Arguments.of(date(2012, 6, 29), ALIGNED_WEEK_OF_MONTH, 0L, "Leap Day is not in a week of month"),
                Arguments.of(date(2012, 6, 29), ALIGNED_WEEK_OF_YEAR, 0L, "Leap Day is not in a week of year"),
                Arguments.of(date(2012, 6, 29), MONTH_OF_YEAR, 6L, "Leap Day is in month 6"),

                // --- Year Day (Day 29 of month 13) ---
                Arguments.of(date(2014, 13, 29), DAY_OF_WEEK, 0L, "Year Day is not a day of week"),
                Arguments.of(date(2014, 13, 29), DAY_OF_MONTH, 29L, "Year Day is 29th of month 13"),
                Arguments.of(date(2014, 13, 29), DAY_OF_YEAR, 365L, "Day of year (13*28 + 1)"),
                Arguments.of(date(2012, 13, 29), DAY_OF_YEAR, 366L, "Day of year in leap year (13*28 + 1 + 1)"),
                Arguments.of(date(2014, 13, 29), ALIGNED_WEEK_OF_MONTH, 0L, "Year Day is not in a week of month"),
                Arguments.of(date(2014, 13, 29), ALIGNED_WEEK_OF_YEAR, 0L, "Year Day is not in a week of year")
            );
        }

        @ParameterizedTest(name = "[{index}] {3}")
        @MethodSource("fieldValueProvider")
        void getLong_returnsCorrectValueForField(InternationalFixedDate date, TemporalField field, long expected, String description) {
            assertEquals(expected, date.getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class ManipulationTests {

        static Stream<Arguments> withFieldProvider() {
            return Stream.of(
                // --- Standard adjustments ---
                Arguments.of(date(2014, 5, 26), DAY_OF_WEEK, 1L, date(2014, 5, 22)),
                Arguments.of(date(2014, 5, 26), DAY_OF_MONTH, 28L, date(2014, 5, 28)),
                Arguments.of(date(2014, 5, 26), DAY_OF_YEAR, 364L, date(2014, 13, 28)),
                Arguments.of(date(2014, 5, 26), ALIGNED_WEEK_OF_MONTH, 1L, date(2014, 5, 5)),
                Arguments.of(date(2014, 5, 26), ALIGNED_WEEK_OF_YEAR, 23L, date(2014, 6, 19)),
                Arguments.of(date(2014, 5, 26), MONTH_OF_YEAR, 4L, date(2014, 4, 26)),
                Arguments.of(date(2014, 5, 26), YEAR, 2012L, date(2012, 5, 26)),

                // --- Adjustments on special days (Year Day) ---
                Arguments.of(date(2014, 13, 29), DAY_OF_MONTH, 1L, date(2014, 13, 1)),
                Arguments.of(date(2014, 13, 29), MONTH_OF_YEAR, 1L, date(2014, 1, 28)), // Day clamps to 28
                Arguments.of(date(2014, 13, 29), YEAR, 2013L, date(2013, 13, 29)),

                // --- Adjustments on special days (Leap Day) ---
                Arguments.of(date(2012, 6, 29), DAY_OF_MONTH, 1L, date(2012, 6, 1)),
                Arguments.of(date(2012, 6, 29), MONTH_OF_YEAR, 7L, date(2012, 7, 28)), // Day clamps to 28
                Arguments.of(date(2012, 6, 29), YEAR, 2013L, date(2013, 6, 28)), // Becomes non-leap, so day clamps
                Arguments.of(date(2012, 6, 29), YEAR, 2016L, date(2016, 6, 29)) // Stays leap day
            );
        }

        @ParameterizedTest
        @MethodSource("withFieldProvider")
        void with_returnsAdjustedDate(InternationalFixedDate base, TemporalField field, long value, InternationalFixedDate expected) {
            assertEquals(expected, base.with(field, value));
        }

        static Stream<Arguments> withInvalidFieldValueProvider() {
            return Stream.of(
                Arguments.of(date(2013, 1, 1), DAY_OF_WEEK, 0L, "Day of week cannot be 0"),
                Arguments.of(date(2013, 1, 1), DAY_OF_WEEK, 8L, "Day of week cannot be > 7"),
                Arguments.of(date(2013, 1, 1), DAY_OF_MONTH, 29L, "Day 29 invalid for standard month"),
                Arguments.of(date(2012, 6, 1), DAY_OF_MONTH, 30L, "Day 30 invalid for leap month"),
                Arguments.of(date(2013, 1, 1), DAY_OF_YEAR, 366L, "Day 366 invalid for non-leap year"),
                Arguments.of(date(2012, 1, 1), DAY_OF_YEAR, 367L, "Day 367 invalid for leap year"),
                Arguments.of(date(2013, 1, 1), MONTH_OF_YEAR, 14L, "Month > 13 invalid")
            );
        }

        @ParameterizedTest(name = "[{index}] {3}")
        @MethodSource("withInvalidFieldValueProvider")
        void with_throwsExceptionForInvalidFieldValue(InternationalFixedDate base, TemporalField field, long value, String description) {
            assertThrows(DateTimeException.class, () -> base.with(field, value));
        }

        static Stream<Arguments> lastDayOfMonthProvider() {
            return Stream.of(
                Arguments.of(date(2012, 6, 23), date(2012, 6, 29)), // Leap month
                Arguments.of(date(2009, 6, 23), date(2009, 6, 28)), // Non-leap month
                Arguments.of(date(2007, 13, 23), date(2007, 13, 29)) // Final month
            );
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthProvider")
        void with_lastDayOfMonth_adjustsToLastDay(InternationalFixedDate base, InternationalFixedDate expected) {
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> plusProvider() {
            return Stream.of(
                // --- Standard additions ---
                Arguments.of(date(2014, 5, 26), 8, DAYS, date(2014, 6, 6)),
                Arguments.of(date(2014, 5, 26), 3, WEEKS, date(2014, 6, 19)),
                Arguments.of(date(2014, 5, 26), 3, MONTHS, date(2014, 8, 26)),
                Arguments.of(date(2014, 5, 26), 3, YEARS, date(2017, 5, 26)),
                Arguments.of(date(2014, 5, 26), 3, DECADES, date(2044, 5, 26)),

                // --- Additions crossing special days ---
                Arguments.of(date(2012, 6, 26), 3, WEEKS, date(2012, 7, 19)), // Crosses Leap Day
                Arguments.of(date(2014, 13, 26), 3, WEEKS, date(2015, 1, 19)), // Crosses Year Day

                // --- Additions from special days ---
                Arguments.of(date(2012, 6, 29), 8, DAYS, date(2012, 7, 8)), // From Leap Day
                Arguments.of(date(2014, 13, 29), 8, DAYS, date(2015, 1, 8)), // From Year Day
                Arguments.of(date(2012, 6, 29), 3, YEARS, date(2015, 6, 28)) // Add years from Leap Day to non-leap year
            );
        }

        @ParameterizedTest
        @MethodSource("plusProvider")
        void plus_addsAmountCorrectly(InternationalFixedDate base, long amount, TemporalUnit unit, InternationalFixedDate expected) {
            assertEquals(expected, base.plus(amount, unit));
        }

        static Stream<Arguments> minusProvider() {
            // Invert the plusProvider data: expected.minus(amount) == base
            return plusProvider().map(args -> {
                Object[] a = args.get();
                return Arguments.of(a[3], (long) a[1], (TemporalUnit) a[2], a[0]);
            });
        }

        @ParameterizedTest
        @MethodSource("minusProvider")
        void minus_subtractsAmountCorrectly(InternationalFixedDate base, long amount, TemporalUnit unit, InternationalFixedDate expected) {
            assertEquals(expected, base.minus(amount, unit));
        }

        @Test
        void minus_withPeriod_subtractsPeriodCorrectly() {
            InternationalFixedDate base = date(2014, 5, 26);
            ChronoPeriod period = InternationalFixedChronology.INSTANCE.period(0, 2, 3);
            InternationalFixedDate expected = date(2014, 3, 23);
            assertEquals(expected, base.minus(period));
        }

        static Stream<Arguments> untilUnitProvider() {
            return Stream.of(
                // --- Simple cases ---
                Arguments.of(date(2014, 5, 26), date(2014, 5, 26), DAYS, 0L),
                Arguments.of(date(2014, 5, 26), date(2014, 6, 4), DAYS, 6L),
                Arguments.of(date(2014, 5, 26), date(2014, 6, 5), WEEKS, 1L),
                Arguments.of(date(2014, 5, 26), date(2014, 6, 26), MONTHS, 1L),
                Arguments.of(date(2014, 5, 26), date(2015, 5, 26), YEARS, 1L),

                // --- Spanning special days ---
                Arguments.of(date(2012, 6, 28), date(2012, 7, 1), DAYS, 2L), // Spans Leap Day
                Arguments.of(date(2014, 13, 28), date(2015, 1, 1), DAYS, 2L), // Spans Year Day

                // --- From/To special days ---
                Arguments.of(date(2012, 6, 29), date(2012, 13, 29), DAYS, 197L), // Leap Day to Year Day
                Arguments.of(date(2012, 6, 29), date(2012, 13, 29), WEEKS, 28L),
                Arguments.of(date(2012, 6, 29), date(2012, 13, 29), MONTHS, 7L)
            );
        }

        @ParameterizedTest
        @MethodSource("untilUnitProvider")
        void until_calculatesAmountBetweenDatesInUnit(InternationalFixedDate start, InternationalFixedDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodProvider() {
            return Stream.of(
                // --- Simple cases ---
                Arguments.of(date(2014, 5, 26), date(2014, 5, 26), 0, 0, 0),
                Arguments.of(date(2014, 5, 26), date(2014, 6, 4), 0, 0, 6),
                Arguments.of(date(2014, 5, 26), date(2014, 6, 26), 0, 1, 0),
                Arguments.of(date(2014, 5, 26), date(2015, 5, 26), 1, 0, 0),

                // --- Spanning leap years ---
                Arguments.of(date(2011, 12, 28), date(2012, 13, 1), 1, 0, 1), // Spans 2012 leap year

                // --- From/To special days ---
                Arguments.of(date(2003, 13, 29), date(2004, 6, 29), 0, 6, 0), // Year Day to Leap Day
                Arguments.of(date(2004, 6, 29), date(2004, 13, 29), 0, 7, 0)  // Leap Day to Year Day
            );
        }

        @ParameterizedTest
        @MethodSource("untilPeriodProvider")
        void until_calculatesPeriodBetweenDates(InternationalFixedDate start, InternationalFixedDate end, int y, int m, int d) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(y, m, d), start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApiTests {

        @Test
        void chronologySingleton_isNotNull() {
            assertNotNull(InternationalFixedChronology.INSTANCE);
        }

        @Test
        void chronologyIdAndCalendarType_areCorrect() {
            assertEquals("Ifc", InternationalFixedChronology.INSTANCE.getId());
            assertEquals(null, InternationalFixedChronology.INSTANCE.getCalendarType());
        }

        @Test
        void eras_areCorrect() {
            assertEquals(1, InternationalFixedChronology.INSTANCE.eras().size());
            assertEquals(InternationalFixedEra.CE, InternationalFixedChronology.INSTANCE.eras().get(0));
        }

        static Stream<Integer> invalidEraValueProvider() {
            return Stream.of(-1, 0, 2);
        }

        @ParameterizedTest
        @MethodSource("invalidEraValueProvider")
        void eraOf_throwsForInvalidValue(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        static Stream<Integer> invalidProlepticYearProvider() {
            return Stream.of(-10, -1, 0);
        }

        @ParameterizedTest
        @MethodSource("invalidProlepticYearProvider")
        void prolepticYear_throwsForInvalidYear(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }
    }

    @Nested
    @DisplayName("Object methods")
    class ObjectMethodTests {

        static Stream<Arguments> toStringProvider() {
            return Stream.of(
                Arguments.of(date(1, 1, 1), "Ifc CE 1/01/01"),
                Arguments.of(date(2012, 6, 23), "Ifc CE 2012/06/23"),
                Arguments.of(date(1, 13, 29), "Ifc CE 1/13/29"), // Year Day
                Arguments.of(date(2012, 6, 29), "Ifc CE 2012/06/29") // Leap Day
            );
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        void toString_returnsCorrectFormat(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }

        @Test
        void equalsAndHashCode_behaveCorrectly() {
            InternationalFixedDate date1 = date(2014, 5, 26);
            InternationalFixedDate date2 = date(2014, 5, 26);
            InternationalFixedDate date3 = date(2014, 5, 27);

            assertTrue(date1.equals(date2));
            assertTrue(date2.equals(date1));
            assertEquals(date1.hashCode(), date2.hashCode());

            assertFalse(date1.equals(date3));
            assertFalse(date3.equals(date1));
        }
    }
}