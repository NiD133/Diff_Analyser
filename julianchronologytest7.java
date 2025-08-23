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
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("JulianChronology interaction and calculation tests")
class JulianChronologyInteractionTest {

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Object[][] provideDateConversionSamples() {
        return new Object[][] {
            // { JulianDate, equivalent ISO LocalDate }
            { JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30) },
            { JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1) },
            { JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27) }, // Julian leap year
            { JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27) }, // Julian leap year, not Gregorian
            { JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14) }, // Day before Gregorian cutover
            { JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15) }, // Day of Gregorian cutover
            { JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12) },
            { JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6) }
        };
    }

    @Nested
    @DisplayName("Conversion Tests")
    class ConversionTests {

        @ParameterizedTest(name = "{index}: {0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteractionTest#provideDateConversionSamples")
        @DisplayName("LocalDate.from(julianDate) should return equivalent ISO date")
        void localDateFromJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian));
        }

        @ParameterizedTest(name = "{index}: {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteractionTest#provideDateConversionSamples")
        @DisplayName("JulianDate.from(localDate) should return equivalent Julian date")
        void julianDateFromLocalDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianDate.from(iso));
        }

        @ParameterizedTest(name = "{index}: epochDay({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteractionTest#provideDateConversionSamples")
        @DisplayName("JulianChronology.dateEpochDay() should create correct Julian date")
        void chronologyDateFromEpochDay(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest(name = "{index}: {0}.toEpochDay() -> {1}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteractionTest#provideDateConversionSamples")
        @DisplayName("toEpochDay() should return correct epoch day")
        void toEpochDay(JulianDate julian, LocalDate iso) {
            assertEquals(iso.toEpochDay(), julian.toEpochDay());
        }

        @ParameterizedTest(name = "{index}: JulianChronology.date({1}) -> {0}")
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteractionTest#provideDateConversionSamples")
        @DisplayName("JulianChronology.date(TemporalAccessor) should create correct Julian date")
        void chronologyDateFromTemporal(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.date(iso));
        }
    }

    @Nested
    @DisplayName("Factory and Validation Tests")
    class FactoryTests {

        static Object[][] provideInvalidDateParts() {
            return new Object[][] {
                // { year, month, dayOfMonth }
                { 1900, 0, 1 },   // Invalid month
                { 1900, 13, 1 },  // Invalid month
                { 1900, 1, 0 },   // Invalid day
                { 1900, 1, 32 },  // Invalid day for Jan
                { 1899, 2, 29 },  // Invalid day for non-leap Feb
                { 1900, 4, 31 }   // Invalid day for Apr
            };
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("provideInvalidDateParts")
        @DisplayName("of() with invalid date parts should throw DateTimeException")
        void of_withInvalidDateParts_throwsException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dom));
        }
    }

    @Nested
    @DisplayName("Query Tests")
    class QueryTests {

        static Object[][] provideMonthLengths() {
            return new Object[][] {
                // { year, month, expectedLength }
                { 1900, 1, 31 },
                { 1900, 2, 29 }, // Julian leap year
                { 1901, 2, 28 }, // Julian non-leap year
                { 1904, 2, 29 }, // Julian leap year
                { 2000, 2, 29 }, // Julian leap year
                { 2100, 2, 29 }  // Julian leap year
            };
        }

        @ParameterizedTest(name = "{0}-{1} should have {2} days")
        @MethodSource("provideMonthLengths")
        @DisplayName("lengthOfMonth() should return correct length")
        void lengthOfMonth_isCorrect(int year, int month, int length) {
            assertEquals(length, JulianDate.of(year, month, 1).lengthOfMonth());
        }

        static Object[][] provideFieldRanges() {
            return new Object[][] {
                // { year, month, day, field, expectedMin, expectedMax }
                { 2012, 1, 23, DAY_OF_MONTH, 1, 31 },
                { 2012, 2, 23, DAY_OF_MONTH, 1, 29 }, // Leap
                { 2011, 2, 23, DAY_OF_MONTH, 1, 28 }, // Non-leap
                { 2012, 4, 23, DAY_OF_MONTH, 1, 30 },
                { 2012, 1, 23, DAY_OF_YEAR, 1, 366 }, // Leap
                { 2011, 1, 23, DAY_OF_YEAR, 1, 365 }, // Non-leap
                { 2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5 },
                { 2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4 }
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2}, range({3}) is {4}..{5}")
        @MethodSource("provideFieldRanges")
        @DisplayName("range() should return correct value range for a field")
        void range_forField_isCorrect(int year, int month, int dom, TemporalField field, int expectedMin, int expectedMax) {
            JulianDate date = JulianDate.of(year, month, dom);
            assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
        }

        static Object[][] provideFieldValues() {
            return new Object[][] {
                // { year, month, day, field, expectedValue }
                { 2014, 5, 26, DAY_OF_WEEK, 7 },
                { 2014, 5, 26, DAY_OF_MONTH, 26 },
                { 2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26 }, // Jan+Feb+Mar+Apr+May
                { 2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4 },
                { 2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21 },
                { 2014, 5, 26, MONTH_OF_YEAR, 5 },
                { 2014, 5, 26, PROLEPTIC_MONTH, (2014L * 12) + 5 - 1 },
                { 2014, 5, 26, YEAR, 2014 },
                { 2014, 5, 26, ERA, 1 }, // AD
                { 0, 6, 8, ERA, 0 },     // BC
                { 2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7 }
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2}, getLong({3}) is {4}")
        @MethodSource("provideFieldValues")
        @DisplayName("getLong() should return correct value for a field")
        void getLong_forField_isCorrect(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, JulianDate.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Arithmetic Tests")
    class ArithmeticTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteractionTest#provideDateConversionSamples")
        @DisplayName("plus(days) should correctly add days")
        void plusDays_isCorrect(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian.plus(0, DAYS)), "Adding 0 days should not change the date");
            assertEquals(iso.plusDays(1), LocalDate.from(julian.plus(1, DAYS)), "Adding 1 day");
            assertEquals(iso.plusDays(35), LocalDate.from(julian.plus(35, DAYS)), "Adding 35 days");
            assertEquals(iso.plusDays(-1), LocalDate.from(julian.plus(-1, DAYS)), "Adding -1 day");
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteractionTest#provideDateConversionSamples")
        @DisplayName("minus(days) should correctly subtract days")
        void minusDays_isCorrect(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian.minus(0, DAYS)), "Subtracting 0 days should not change the date");
            assertEquals(iso.minusDays(1), LocalDate.from(julian.minus(1, DAYS)), "Subtracting 1 day");
            assertEquals(iso.minusDays(35), LocalDate.from(julian.minus(35, DAYS)), "Subtracting 35 days");
            assertEquals(iso.minusDays(-1), LocalDate.from(julian.minus(-1, DAYS)), "Subtracting -1 day");
        }

        static Object[][] providePlusMinusOperations() {
            return new Object[][] {
                // { y1, m1, d1, amount, unit, y2, m2, d2 }
                { 2014, 5, 26, 8L, DAYS, 2014, 6, 3 },
                { 2014, 5, 26, 3L, WEEKS, 2014, 6, 16 },
                { 2014, 5, 26, 3L, MONTHS, 2014, 8, 26 },
                { 2014, 5, 26, 3L, YEARS, 2017, 5, 26 },
                { 2014, 5, 26, 3L, DECADES, 2044, 5, 26 },
                { 2014, 5, 26, 3L, CENTURIES, 2314, 5, 26 },
                { 2014, 5, 26, 3L, MILLENNIA, 5014, 5, 26 },
                { 2014, 5, 26, -1L, ERAS, -2013, 5, 26 }
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("providePlusMinusOperations")
        @DisplayName("plus() should correctly add various temporal units")
        void plus_withVariousUnits_isCorrect(int y1, int m1, int d1, long amount, TemporalUnit unit, int y2, int m2, int d2) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate expected = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4} -> {0}-{1}-{2}")
        @MethodSource("providePlusMinusOperations")
        @DisplayName("minus() should correctly subtract various temporal units")
        void minus_withVariousUnits_isCorrect(int y1, int m1, int d1, long amount, TemporalUnit unit, int y2, int m2, int d2) {
            JulianDate expected = JulianDate.of(y1, m1, d1);
            JulianDate start = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Object[][] provideWithOperations() {
            return new Object[][] {
                // { y, m, d, field, value, ey, em, ed }
                { 2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22 },
                { 2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31 },
                { 2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31 },
                { 2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26 },
                { 2014, 5, 26, YEAR, 2012, 2012, 5, 26 },
                { 2014, 5, 26, ERA, 0, -2013, 5, 26 }, // Change to BC
                { 2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28 }, // Adjusts day
                { 2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29 }, // Adjusts day (leap)
                { 2012, 2, 29, YEAR, 2011, 2011, 2, 28 } // Adjusts day from leap to non-leap
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("provideWithOperations")
        @DisplayName("with() should correctly adjust date fields")
        void with_forField_isCorrect(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            JulianDate start = JulianDate.of(y, m, d);
            JulianDate expected = JulianDate.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }
    }

    @Nested
    @DisplayName("Until Tests")
    class UntilTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteractionTest#provideDateConversionSamples")
        @DisplayName("until() between equivalent dates should return zero")
        void until_withEquivalentDate_returnsZero(JulianDate julian, LocalDate iso) {
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(julian), "Julian.until(same Julian)");
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(iso), "Julian.until(equivalent ISO)");
            assertEquals(Period.ZERO, iso.until(julian), "ISO.until(equivalent Julian)");
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.JulianChronologyInteractionTest#provideDateConversionSamples")
        @DisplayName("until(DAYS) should return correct number of days")
        void until_days_isCorrect(JulianDate julian, LocalDate iso) {
            assertEquals(0, julian.until(iso.plusDays(0), DAYS));
            assertEquals(1, julian.until(iso.plusDays(1), DAYS));
            assertEquals(35, julian.until(iso.plusDays(35), DAYS));
            assertEquals(-40, julian.until(iso.minusDays(40), DAYS));
        }

        static Object[][] provideUntilOperations() {
            return new Object[][] {
                // { y1, m1, d1, y2, m2, d2, unit, expectedAmount }
                { 2014, 5, 26, 2014, 6, 1, DAYS, 6 },
                { 2014, 5, 26, 2014, 6, 2, WEEKS, 1 },
                { 2014, 5, 26, 2014, 6, 25, MONTHS, 0 }, // Not a full month
                { 2014, 5, 26, 2014, 6, 26, MONTHS, 1 },
                { 2014, 5, 26, 2015, 5, 25, YEARS, 0 }, // Not a full year
                { 2014, 5, 26, 2015, 5, 26, YEARS, 1 },
                { 2014, 5, 26, 2024, 5, 26, DECADES, 1 },
                { 2014, 5, 26, 2114, 5, 26, CENTURIES, 1 },
                { 2014, 5, 26, 3014, 5, 26, MILLENNIA, 1 },
                { -2013, 5, 26, 2014, 5, 26, ERAS, 1 }
            };
        }

        @ParameterizedTest(name = "From {0}-{1}-{2} until {3}-{4}-{5} in {6} is {7}")
        @MethodSource("provideUntilOperations")
        @DisplayName("until() should return correct amount for various units")
        void until_withVariousUnits_isCorrect(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(y1, m1, d1);
            JulianDate end = JulianDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }
    }

    @Nested
    @DisplayName("Era Tests")
    class EraTests {
        @Test
        @DisplayName("Era and year-of-era conversions should be consistent around BC/AD boundary")
        void eraAndYearDayConversion_isConsistent() {
            for (int year = -200; year < 200; year++) {
                if (year == 0) continue; // Proleptic year 0 is BC 1
                JulianDate fromYear = JulianChronology.INSTANCE.dateYearDay(year, 1);
                assertEquals(year, fromYear.get(YEAR), "Proleptic year should match");

                JulianEra era = (year < 1 ? JulianEra.BC : JulianEra.AD);
                int yoe = (year < 1 ? 1 - year : year);
                assertEquals(era, fromYear.getEra(), "Era should be correct for year " + year);
                assertEquals(yoe, fromYear.get(YEAR_OF_ERA), "Year-of-era should be correct for year " + year);

                JulianDate fromEra = JulianChronology.INSTANCE.dateYearDay(era, yoe, 1);
                assertEquals(fromYear, fromEra, "Date from proleptic year should equal date from era/yoe");
            }
        }
    }

    @Nested
    @DisplayName("toString() Tests")
    class ToStringTests {

        static Object[][] provideToStringSamples() {
            return new Object[][] {
                { JulianDate.of(1, 1, 1), "Julian AD 1-01-01" },
                { JulianDate.of(2012, 6, 23), "Julian AD 2012-06-23" },
                { JulianDate.of(0, 12, 30), "Julian BC 1-12-30" }
            };
        }

        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("provideToStringSamples")
        @DisplayName("toString() should return the correct string representation")
        void toString_returnsCorrectFormat(JulianDate julian, String expected) {
            assertEquals(expected, julian.toString());
        }
    }
}