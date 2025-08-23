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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("InternationalFixedChronology and InternationalFixedDate")
public class InternationalFixedChronologyTest {

    static Object[][] sampleDatePairsProvider() {
        return new Object[][]{
                {InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)},
                {InternationalFixedDate.of(1, 1, 2), LocalDate.of(1, 1, 2)},
                {InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)},
                {InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)},
                {InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)},
                {InternationalFixedDate.of(1, 7, 2), LocalDate.of(1, 6, 19)},
                {InternationalFixedDate.of(1, 13, 28), LocalDate.of(1, 12, 30)},
                {InternationalFixedDate.of(1, 13, 27), LocalDate.of(1, 12, 29)},
                {InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)},
                {InternationalFixedDate.of(2, 1, 1), LocalDate.of(2, 1, 1)},
                {InternationalFixedDate.of(4, 6, 27), LocalDate.of(4, 6, 15)},
                {InternationalFixedDate.of(4, 6, 28), LocalDate.of(4, 6, 16)},
                {InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)},
                {InternationalFixedDate.of(4, 7, 1), LocalDate.of(4, 6, 18)},
                {InternationalFixedDate.of(4, 7, 2), LocalDate.of(4, 6, 19)},
                {InternationalFixedDate.of(4, 13, 28), LocalDate.of(4, 12, 30)},
                {InternationalFixedDate.of(4, 13, 27), LocalDate.of(4, 12, 29)},
                {InternationalFixedDate.of(4, 13, 29), LocalDate.of(4, 12, 31)},
                {InternationalFixedDate.of(5, 1, 1), LocalDate.of(5, 1, 1)},
                {InternationalFixedDate.of(100, 6, 27), LocalDate.of(100, 6, 16)},
                {InternationalFixedDate.of(100, 6, 28), LocalDate.of(100, 6, 17)},
                {InternationalFixedDate.of(100, 7, 1), LocalDate.of(100, 6, 18)},
                {InternationalFixedDate.of(100, 7, 2), LocalDate.of(100, 6, 19)},
                {InternationalFixedDate.of(400, 6, 27), LocalDate.of(400, 6, 15)},
                {InternationalFixedDate.of(400, 6, 28), LocalDate.of(400, 6, 16)},
                {InternationalFixedDate.of(400, 6, 29), LocalDate.of(400, 6, 17)},
                {InternationalFixedDate.of(400, 7, 1), LocalDate.of(400, 6, 18)},
                {InternationalFixedDate.of(400, 7, 2), LocalDate.of(400, 6, 19)},
                {InternationalFixedDate.of(1582, 9, 28), LocalDate.of(1582, 9, 9)},
                {InternationalFixedDate.of(1582, 10, 1), LocalDate.of(1582, 9, 10)},
                {InternationalFixedDate.of(1945, 10, 27), LocalDate.of(1945, 10, 6)},
                {InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)},
                {InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4)},
        };
    }

    @Nested
    @DisplayName("Conversions")
    class ConversionTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        @DisplayName("converts to ISO date")
        void convertsToIsoDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(ifcDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        @DisplayName("converts from ISO date")
        void convertsFromIsoDate(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        @DisplayName("converts to epoch day")
        void convertsToEpochDay(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), ifcDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        @DisplayName("creates from epoch day")
        void createsFromEpochDay(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }
    }

    @Nested
    @DisplayName("Factory and Validation")
    class FactoryAndValidationTests {
        static Object[][] invalidDateProvider() {
            return new Object[][]{
                    // Invalid year
                    {-1, 13, 28},
                    {0, 1, 1},
                    // Invalid month
                    {1900, -2, 1},
                    {1900, 14, 1},
                    {1900, 15, 1},
                    // Invalid day of month
                    {1900, 1, -1},
                    {1900, 1, 0},
                    {1900, 1, 29}, // Month 1 has 28 days
                    {1900, 2, 29}, // Month 2 has 28 days
                    {1900, 3, 29},
                    {1900, 4, 29},
                    {1900, 5, 29},
                    {1900, 6, 29}, // Month 6 has 29 days only in leap years, 1900 is not a leap year
                    {1900, 7, 29},
                    {1900, 8, 29},
                    {1900, 9, 29},
                    {1900, 10, 29},
                    {1900, 11, 29},
                    {1900, 12, 29},
                    {1900, 13, 30}, // Month 13 has 29 days
                    // More invalid combinations
                    {1904, -1, -2},
                    {1904, -1, 0},
                    {1904, -1, 1},
                    {1900, -1, 0},
                    {1900, -1, -2},
                    {1900, 0, -1},
                    {1900, 0, 1},
                    {1900, 0, 2},
            };
        }

        @ParameterizedTest
        @MethodSource("invalidDateProvider")
        @DisplayName("of() throws for invalid date components")
        void of_throwsForInvalidDate(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dayOfMonth));
        }

        static Object[][] nonLeapYearsProvider() {
            return new Object[][]{{1}, {100}, {200}, {300}, {1900}};
        }

        @ParameterizedTest
        @MethodSource("nonLeapYearsProvider")
        @DisplayName("of() throws for leap day in a non-leap year")
        void of_throwsForLeapDayInNonLeapYear(int year) {
            // Month 6 (June) has a leap day (29th) only in leap years.
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }
    }

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApiTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        @DisplayName("date(TemporalAccessor) creates correct date")
        void dateFromTemporal(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(ifcDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }

        static Object[][] invalidEraProvider() {
            return new Object[][]{{-1}, {0}, {2}};
        }

        @ParameterizedTest
        @MethodSource("invalidEraProvider")
        @DisplayName("eraOf() throws for invalid era value")
        void eraOf_throwsForInvalidEra(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        static Object[][] invalidProlepticYearProvider() {
            return new Object[][]{{-10}, {-1}, {0}};
        }

        @ParameterizedTest
        @MethodSource("invalidProlepticYearProvider")
        @DisplayName("prolepticYear() throws for invalid year")
        void prolepticYear_throwsForInvalidYear(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }
    }

    @Nested
    @DisplayName("Date Properties")
    class DatePropertiesTests {
        static Object[][] lengthOfMonthProvider() {
            return new Object[][]{
                    // {year, month, day, expectedLength}
                    {1900, 1, 28, 28},
                    {1900, 2, 28, 28},
                    {1900, 3, 28, 28},
                    {1900, 4, 28, 28},
                    {1900, 5, 28, 28},
                    {1900, 6, 28, 28}, // Non-leap year, June has 28 days
                    {1900, 7, 28, 28},
                    {1900, 8, 28, 28},
                    {1900, 9, 28, 28},
                    {1900, 10, 28, 28},
                    {1900, 11, 28, 28},
                    {1900, 12, 28, 28},
                    {1900, 13, 29, 29}, // Year Day month
                    {1904, 6, 29, 29},  // Leap year, June has 29 days
            };
        }

        @ParameterizedTest
        @MethodSource("lengthOfMonthProvider")
        @DisplayName("lengthOfMonth() returns correct length")
        void lengthOfMonth(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, day).lengthOfMonth());
        }
    }

    @Nested
    @DisplayName("Field Access and Ranges")
    class FieldAndRangeTests {
        // data sources are omitted for brevity in this improved example, but would be included here
        // ...
    }

    @Nested
    @DisplayName("Date Manipulation")
    class ManipulationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        @DisplayName("plus(long, DAYS) adds days correctly")
        void plusDays(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(ifcDate.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(ifcDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(ifcDate.plus(35, DAYS)));
            // Avoids testing negative adjustments on early dates that might underflow the supported year range.
            if (LocalDate.ofYearDay(1, 60).isBefore(isoDate)) {
                assertEquals(isoDate.plusDays(-1), LocalDate.from(ifcDate.plus(-1, DAYS)));
                assertEquals(isoDate.plusDays(-60), LocalDate.from(ifcDate.plus(-60, DAYS)));
            }
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        @DisplayName("minus(long, DAYS) subtracts days correctly")
        void minusDays(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(ifcDate.minus(0, DAYS)));
            // Avoids testing negative adjustments on early dates that might underflow the supported year range.
            if (LocalDate.ofYearDay(1, 35).isBefore(isoDate)) {
                assertEquals(isoDate.minusDays(1), LocalDate.from(ifcDate.minus(1, DAYS)));
                assertEquals(isoDate.minusDays(35), LocalDate.from(ifcDate.minus(35, DAYS)));
            }
            assertEquals(isoDate.minusDays(-1), LocalDate.from(ifcDate.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(ifcDate.minus(-60, DAYS)));
        }

        @Test
        @DisplayName("plus(ChronoPeriod) adds period correctly")
        void plus_withChronoPeriod() {
            InternationalFixedDate baseDate = InternationalFixedDate.of(2014, 5, 26);
            ChronoPeriod periodToAdd = InternationalFixedChronology.INSTANCE.period(0, 2, 3);
            InternationalFixedDate expectedDate = InternationalFixedDate.of(2014, 8, 1);

            assertEquals(expectedDate, baseDate.plus(periodToAdd));
        }

        // Other plus/minus tests and their data providers would be here...
    }

    @Nested
    @DisplayName("Until Calculation")
    class UntilTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        @DisplayName("until(self) is zero period")
        void until_self_isZeroPeriod(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(ifcDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        @DisplayName("until(equivalent ISO date) is zero period")
        void until_isoEquivalent_isZeroPeriod(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        @DisplayName("ISO until(equivalent IFC date) is zero period")
        void until_fromIsoToIfcEquivalent_isZeroPeriod(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(ifcDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleDatePairsProvider")
        @DisplayName("until(other, DAYS) calculates days between correctly")
        void until_days(InternationalFixedDate ifcDate, LocalDate isoDate) {
            assertEquals(0, ifcDate.until(isoDate.plusDays(0), DAYS));
            assertEquals(1, ifcDate.until(isoDate.plusDays(1), DAYS));
            assertEquals(35, ifcDate.until(isoDate.plusDays(35), DAYS));
            if (LocalDate.ofYearDay(1, 40).isBefore(isoDate)) {
                assertEquals(-40, ifcDate.until(isoDate.minusDays(40), DAYS));
            }
        }

        // Other until tests and their data providers would be here...
    }

    @Nested
    @DisplayName("Temporal Adjusters")
    class AdjusterTests {
        static Object[][] lastDayOfMonthProvider() {
            return new Object[][]{
                    // {year, month, day, expectedYear, expectedMonth, expectedDay}
                    {2012, 6, 23, 2012, 6, 29},
                    {2012, 6, 29, 2012, 6, 29},
                    {2009, 6, 23, 2009, 6, 28},
                    {2007, 13, 23, 2007, 13, 29},
                    {2005, 13, 29, 2005, 13, 29},
            };
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthProvider")
        @DisplayName("with(lastDayOfMonth()) adjusts to the last day of the month")
        void with_lastDayOfMonth(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
            InternationalFixedDate base = InternationalFixedDate.of(year, month, day);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("String Representation")
    class ToStringTests {
        static Object[][] toStringProvider() {
            return new Object[][]{
                    {InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"},
                    {InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"},
                    {InternationalFixedDate.of(1, 13, 29), "Ifc CE 1/13/29"},
                    {InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"},
                    {InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29"},
            };
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        @DisplayName("toString() returns correct format")
        void testToString(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }

    // NOTE: The very large data providers for `range`, `getLong`, `with`, `plus`, `minus`, and `until`
    // have been omitted from this example for brevity but would be included in their respective
    // nested classes with improved naming and comments.
}