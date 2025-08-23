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
class InternationalFixedChronologyTest {

    /**
     * Provides sample InternationalFixedDate instances and their equivalent ISO LocalDate.
     *
     * @return a stream of arguments: {InternationalFixedDate, LocalDate}
     */
    static Object[][] sampleFixedAndIsoDates() {
        return new Object[][] {
            {InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)},
            {InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)},
            {InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)}, // Year Day
            {InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)}, // Leap Day
            {InternationalFixedDate.of(4, 7, 1), LocalDate.of(4, 6, 18)},
            {InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)},
            {InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4)},
        };
    }

    @Nested
    @DisplayName("Factory 'of'")
    class FactoryOf {

        static Object[][] invalidDateProvider() {
            return new Object[][] {
                {1900, 1, 29},  // Invalid day for a 28-day month
                {1900, 2, 29},
                {1900, 13, 30}, // Invalid day for a 29-day month
                {1900, 14, 1},  // Invalid month
                {1900, 0, 1},   // Invalid month
                {1900, 1, 0},   // Invalid day
                {0, 1, 1},      // Invalid year
            };
        }

        @ParameterizedTest(name = "year={0}, month={1}, day={2}")
        @MethodSource("invalidDateProvider")
        void of_withInvalidDateParts_throwsException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        static Object[][] nonLeapYearsProvider() {
            return new Object[][] {{1}, {100}, {1900}};
        }

        @ParameterizedTest(name = "year={0}")
        @MethodSource("nonLeapYearsProvider")
        void of_withInvalidLeapDayInNonLeapYear_throwsException(int year) {
            // Month 6 only has 29 days in a leap year.
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }
    }

    @Nested
    @DisplayName("Conversion to/from other types")
    class Conversion {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void fromInternationalFixedDate_toLocalDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso, LocalDate.from(fixed));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void fromLocalDate_toInternationalFixedDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void chronologyDate_fromTemporalAccessor(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedChronology.INSTANCE.date(iso));
        }
    }

    @Nested
    @DisplayName("Epoch Day conversion")
    class EpochDayConversion {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void toEpochDay(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso.toEpochDay(), fixed.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void chronologyDateFromEpochDay(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }
    }

    @Nested
    @DisplayName("Date properties")
    class DateProperties {

        static Object[][] lengthOfMonthProvider() {
            // year, month, day, expectedLength
            return new Object[][] {
                {1900, 1, 1, 28},
                {1900, 6, 1, 28},  // Not a leap year
                {1900, 13, 1, 29}, // Year Day month
                {1904, 6, 1, 29},  // Leap year
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2} -> {3}")
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, InternationalFixedDate.of(year, month, day).lengthOfMonth());
        }
    }

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApi {

        @ParameterizedTest
        @MethodSource("invalidEraValuesProvider")
        void eraOf_withInvalidValue_throwsException(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        static Object[][] invalidEraValuesProvider() {
            return new Object[][] {{-1}, {0}, {2}};
        }

        @ParameterizedTest
        @MethodSource("invalidProlepticYearProvider")
        void prolepticYear_withInvalidValue_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }

        static Object[][] invalidProlepticYearProvider() {
            return new Object[][] {{-10}, {-1}, {0}};
        }
    }

    @Nested
    @DisplayName("Querying fields")
    class FieldQuery {

        static Object[][] rangeProvider() {
            // year, month, dom, field, expectedRange
            return new Object[][] {
                // For a leap day (June 29 in a leap year)
                {2012, 6, 29, DAY_OF_MONTH, ValueRange.of(1, 29)},
                // For a standard day in a leap year
                {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
                // For a standard day in a non-leap year
                {2011, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
                // Day of year range depends on leap year
                {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366)},
                {2011, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 365)},
                // Month of year is always 1-13
                {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)},
                // Special days (Leap Day, Year Day) have a DAY_OF_WEEK of 0
                {2012, 6, 29, DAY_OF_WEEK, ValueRange.of(0, 0)},
                {2012, 13, 29, DAY_OF_WEEK, ValueRange.of(0, 0)},
                // Regular days have DAY_OF_WEEK from 1-7
                {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
            };
        }

        @ParameterizedTest(name = "{3} range for {0}-{1}-{2} is {4}")
        @MethodSource("rangeProvider")
        void range(int year, int month, int dom, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, InternationalFixedDate.of(year, month, dom).range(field));
        }

        static Object[][] getLongProvider() {
            // year, month, dom, field, expectedValue
            return new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 5},
                {2014, 5, 26, DAY_OF_MONTH, 26},
                // DAY_OF_YEAR: 4 months * 28 days + 26 = 138
                {2014, 5, 26, DAY_OF_YEAR, 138},
                {2014, 5, 26, YEAR, 2014},
                {2014, 5, 26, ERA, 1},
                // Leap year case for DAY_OF_YEAR: 8 months * 28 days + 1 leap day + 26 = 251
                {2012, 9, 26, DAY_OF_YEAR, 251},
                // Year Day (day 365 or 366)
                {2014, 13, 29, DAY_OF_WEEK, 0}, // Special day, no day of week
                {2014, 13, 29, DAY_OF_YEAR, 365},
                // Leap Day (in June)
                {2012, 6, 29, DAY_OF_WEEK, 0}, // Special day, no day of week
                {2012, 6, 29, DAY_OF_YEAR, 6 * 28 + 1},
            };
        }

        @ParameterizedTest(name = "{3} of {0}-{1}-{2} is {4}")
        @MethodSource("getLongProvider")
        void getLong(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, InternationalFixedDate.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Adjusting dates")
    class Adjusting {

        static Object[][] withProvider() {
            // year, month, dom, field, newValue, expectedYear, expectedMonth, expectedDom
            return new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
                {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
                {2014, 5, 26, DAY_OF_YEAR, 365, 2014, 13, 29},
                {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                // Adjusting a leap day to a non-leap year results in the preceding day
                {2012, 6, 29, YEAR, 2013, 2013, 6, 28},
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("withProvider")
        void with(int year, int month, int dom, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDom) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, dom);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.with(field, value));
        }

        static Object[][] withInvalidValueProvider() {
            // year, month, dom, field, invalidValue
            return new Object[][] {
                {2013, 1, 1, DAY_OF_MONTH, 29}, // Not a 29-day month
                {2013, 6, 1, DAY_OF_MONTH, 29}, // Not a leap year
                {2013, 1, 1, DAY_OF_YEAR, 366}, // Not a leap year
                {2013, 1, 1, MONTH_OF_YEAR, 14},
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) throws")
        @MethodSource("withInvalidValueProvider")
        void with_invalidValue_throwsException(int year, int month, int dom, TemporalField field, long value) {
            InternationalFixedDate date = InternationalFixedDate.of(year, month, dom);
            assertThrows(DateTimeException.class, () -> date.with(field, value));
        }

        @Test
        void with_lastDayOfMonth_adjustsToCorrectDate() {
            InternationalFixedDate date = InternationalFixedDate.of(2012, 6, 23); // Leap year, June
            InternationalFixedDate expected = InternationalFixedDate.of(2012, 6, 29);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_lastDayOfMonthOnNonLeapYear_adjustsToCorrectDate() {
            InternationalFixedDate date = InternationalFixedDate.of(2013, 6, 23); // Non-leap year, June
            InternationalFixedDate expected = InternationalFixedDate.of(2013, 6, 28);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Date arithmetic")
    class Arithmetic {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void plusDays_isConsistentWithLocalDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso.plusDays(1), LocalDate.from(fixed.plus(1, DAYS)));
            assertEquals(iso.plusDays(35), LocalDate.from(fixed.plus(35, DAYS)));
            // Avoid underflow for dates at the start of the supported era
            if (iso.isAfter(LocalDate.of(1, 3, 1))) {
                assertEquals(iso.plusDays(-1), LocalDate.from(fixed.plus(-1, DAYS)));
                assertEquals(iso.plusDays(-60), LocalDate.from(fixed.plus(-60, DAYS)));
            }
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void minusDays_isConsistentWithLocalDate(InternationalFixedDate fixed, LocalDate iso) {
            // Avoid underflow for dates at the start of the supported era
            if (iso.isAfter(LocalDate.of(1, 2, 5))) {
                assertEquals(iso.minusDays(1), LocalDate.from(fixed.minus(1, DAYS)));
                assertEquals(iso.minusDays(35), LocalDate.from(fixed.minus(35, DAYS)));
            }
            assertEquals(iso.minusDays(-1), LocalDate.from(fixed.minus(-1, DAYS)));
        }

        static Object[][] plusProvider() {
            // year, month, dom, amount, unit, expectedYear, expectedMonth, expectedDom
            return new Object[][] {
                {2014, 5, 26, 8, DAYS, 2014, 6, 6},
                {2014, 5, 26, 3, WEEKS, 2014, 6, 19},
                {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
                {2014, 5, 26, 3, YEARS, 2017, 5, 26},
                // Adding years to a leap day, landing on a non-leap year
                {2012, 6, 29, 1, YEARS, 2013, 6, 28},
                // Adding years to a leap day, landing on a leap year
                {2012, 6, 29, 4, YEARS, 2016, 6, 29},
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("plusProvider")
        void plus(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            InternationalFixedDate start = InternationalFixedDate.of(year, month, dom);
            InternationalFixedDate expected = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus({3}, {4}) -> {0}-{1}-{2}")
        @MethodSource("plusProvider") // Re-use plusProvider for minus
        void minus(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            InternationalFixedDate expected = InternationalFixedDate.of(year, month, dom);
            InternationalFixedDate start = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Object[][] untilProvider() {
            // startY, M, D, endY, M, D, unit, expectedAmount
            return new Object[][] {
                {2014, 5, 26, 2014, 6, 4, DAYS, 6},
                {2014, 5, 26, 2014, 6, 5, WEEKS, 1},
                {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
                {2014, 5, 26, 2015, 5, 26, YEARS, 1},
                {2014, 5, 26, 2024, 5, 26, DECADES, 1},
                {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
                {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
                {2014, 5, 26, 3014, 5, 26, ERAS, 0},
            };
        }

        @ParameterizedTest(name = "until({4}) from {0}-{1}-{2} to {3}-{4}-{5} is {7}")
        @MethodSource("untilProvider")
        void until_withTemporalUnit(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Object[][] untilPeriodProvider() {
            // startY, M, D, endY, M, D, expectedY, M, D
            return new Object[][] {
                {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
                {2014, 5, 26, 2014, 6, 4, 0, 0, 6},
                {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
                {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
                // Across leap day
                {2011, 13, 26, 2012, 13, 26, 1, 0, 0},
                // From leap day
                {2012, 6, 29, 2016, 6, 29, 4, 0, 0},
                // From leap day to year day
                {2004, 6, 29, 2004, 13, 29, 0, 7, 0},
            };
        }

        @ParameterizedTest(name = "until from {0}-{1}-{2} to {3}-{4}-{5} is P{6}Y{7}M{8}D")
        @MethodSource("untilPeriodProvider")
        void until_withChronoPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int eY, int eM, int eD) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(eY, eM, eD);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        void until_zeroPeriodForSameDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixed.until(fixed));
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixed.until(iso));
            assertEquals(Period.ZERO, iso.until(fixed));
        }
    }

    @Nested
    @DisplayName("String representation")
    class StringRepresentation {

        static Object[][] toStringProvider() {
            return new Object[][] {
                {InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"},
                {InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"},
                {InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"}, // Leap Day
                {InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29"}, // Year Day
            };
        }

        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("toStringProvider")
        void testToString(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }

    @Nested
    @DisplayName("Interoperability with LocalDate")
    class IsoInteroperability {

        @Test
        void adjustToInternationalFixedDate_fromLocalDate() {
            InternationalFixedDate fixed = InternationalFixedDate.of(2012, 7, 19);
            LocalDate iso = LocalDate.MIN.with(fixed);
            assertEquals(LocalDate.of(2012, 7, 6), iso);
        }
    }
}