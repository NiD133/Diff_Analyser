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
import java.time.temporal.WeekFields;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("BritishCutoverChronology and BritishCutoverDate")
class BritishCutoverChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Object[][] sampleCutoverAndIsoDates() {
        return new Object[][] {
            // Early Julian dates
            { BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30) },
            { BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31) },
            { BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1) },
            // Julian leap years
            { BritishCutoverDate.of(4, 3, 2), LocalDate.of(4, 2, 29) },
            { BritishCutoverDate.of(100, 3, 1), LocalDate.of(100, 2, 28) },
            // Dates around the Gregorian cutover (not the British one)
            { BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14) },
            { BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15) },
            // Dates around the British cutover year
            { BritishCutoverDate.of(1751, 12, 31), LocalDate.of(1752, 1, 11) },
            { BritishCutoverDate.of(1752, 1, 1), LocalDate.of(1752, 1, 12) },
            // Dates around the British cutover gap (September 1752)
            { BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13) }, // leniently accept invalid
            { BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14) }, // leniently accept invalid
            { BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24) },
            { BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14) },
            // Modern dates (Gregorian)
            { BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12) },
            { BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6) }
        };
    }

    static Object[][] invalidDateParts() {
        return new Object[][] {
            { 1900, 0, 0 }, { 1900, -1, 1 }, { 1900, 0, 1 }, { 1900, 13, 1 },
            { 1900, 1, 0 }, { 1900, 1, 32 }, { 1900, 2, 30 }, { 1899, 2, 29 }
        };
    }

    static Object[][] monthLengths() {
        return new Object[][] {
            { 1700, 2, 29 }, // Julian leap year
            { 1751, 2, 28 }, // Julian non-leap year
            { 1752, 2, 29 }, // Julian leap year
            { 1752, 9, 19 }, // Cutover month
            { 1753, 2, 28 }, // Gregorian non-leap year
            { 1800, 2, 28 }, // Gregorian non-leap year
            { 1900, 2, 28 }, // Gregorian non-leap year
            { 2000, 2, 29 }  // Gregorian leap year
        };
    }

    static Object[][] yearLengths() {
        return new Object[][] {
            { 0, 366 },    // Julian leap year
            { 100, 366 },  // Julian leap year
            { 1700, 366 }, // Julian leap year
            { 1751, 365 }, // Julian non-leap year
            { 1752, 355 }, // Cutover year, 11 days removed
            { 1753, 365 }, // Gregorian non-leap year
            { 1800, 365 }, // Gregorian non-leap year
            { 1900, 365 }, // Gregorian non-leap year
            { 2000, 366 }, // Gregorian leap year
            { 2100, 365 }  // Gregorian non-leap year
        };
    }

    static Object[][] fieldRanges() {
        // year, month, day, field, expectedMin, expectedMax
        return new Object[][] {
            // DAY_OF_MONTH
            { 1752, 2, 23, DAY_OF_MONTH, 1, 29 },
            { 1752, 9, 23, DAY_OF_MONTH, 1, 30 }, // Note: range is lenient for cutover month
            { 2011, 2, 23, DAY_OF_MONTH, 1, 28 },
            { 2012, 2, 23, DAY_OF_MONTH, 1, 29 },
            // DAY_OF_YEAR
            { 1751, 1, 23, DAY_OF_YEAR, 1, 365 },
            { 1752, 1, 23, DAY_OF_YEAR, 1, 355 },
            { 1753, 1, 23, DAY_OF_YEAR, 1, 365 },
            { 2012, 1, 23, DAY_OF_YEAR, 1, 366 },
            // ALIGNED_WEEK_OF_MONTH
            { 1752, 9, 23, ALIGNED_WEEK_OF_MONTH, 1, 3 },
            { 2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4 },
            { 2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5 },
            // ALIGNED_WEEK_OF_YEAR
            { 1752, 12, 23, ALIGNED_WEEK_OF_YEAR, 1, 51 },
            { 2011, 2, 23, ALIGNED_WEEK_OF_YEAR, 1, 53 },
        };
    }

    static Object[][] fieldValues() {
        // year, month, day, field, expectedValue
        return new Object[][] {
            // Pre-cutover date
            { 1752, 5, 26, DAY_OF_WEEK, 2 },
            { 1752, 5, 26, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 26 },
            // Date in the cutover gap (lenient)
            { 1752, 9, 2, DAY_OF_WEEK, 3 },
            { 1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 2 },
            // First day after cutover gap
            { 1752, 9, 14, DAY_OF_WEEK, 4 },
            { 1752, 9, 14, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3 },
            // Modern date
            { 2014, 5, 26, DAY_OF_WEEK, 1 },
            { 2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26 },
            { 2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1 },
            { 2014, 5, 26, YEAR, 2014 },
            { 2014, 5, 26, ERA, 1 },
            // BC Era
            { 0, 6, 8, ERA, 0 },
            // Other fields
            { 2014, 5, 26, WeekFields.ISO.dayOfWeek(), 1 }
        };
    }

    static Object[][] withFieldArguments() {
        // year, month, day, field, value, expectedYear, expectedMonth, expectedDay
        return new Object[][] {
            // --- DAY_OF_WEEK ---
            { 1752, 9, 2, DAY_OF_WEEK, 4, 1752, 9, 14 },
            { 2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 28 },
            // --- DAY_OF_MONTH ---
            { 1752, 9, 2, DAY_OF_MONTH, 3, 1752, 9, 14 }, // lenient
            { 1752, 9, 2, DAY_OF_MONTH, 14, 1752, 9, 14 },
            { 2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31 },
            // --- DAY_OF_YEAR ---
            { 1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3, 1752, 9, 14 }, // lenient
            { 2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31 },
            // --- MONTH_OF_YEAR ---
            { 1752, 8, 4, MONTH_OF_YEAR, 9, 1752, 9, 15 }, // lenient, into cutover
            { 2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28 },
            { 2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29 },
            // --- YEAR ---
            { 1751, 9, 4, YEAR, 1752, 1752, 9, 15 }, // lenient
            { 2012, 2, 29, YEAR, 2011, 2011, 2, 28 },
            // --- ERA ---
            { 2014, 5, 26, ERA, 0, -2013, 5, 26 },
        };
    }

    static Object[][] plusMinusArguments() {
        // year, month, day, amount, unit, expectedYear, expectedMonth, expectedDay, isBidirectional
        return new Object[][] {
            // --- DAYS ---
            { 1752, 9, 2, 1, DAYS, 1752, 9, 14, true },
            { 1752, 9, 14, -1, DAYS, 1752, 9, 2, true },
            { 2014, 5, 26, 8, DAYS, 2014, 6, 3, true },
            // --- WEEKS ---
            { 1752, 9, 2, 1, WEEKS, 1752, 9, 20, true },
            { 1752, 9, 14, -1, WEEKS, 1752, 8, 27, true },
            // --- MONTHS ---
            { 1752, 9, 2, 1, MONTHS, 1752, 10, 2, true },
            { 1752, 8, 12, 1, MONTHS, 1752, 9, 23, false }, // Day adjusted due to cutover
            { 2014, 5, 26, 3, MONTHS, 2014, 8, 26, true },
            // --- YEARS and larger ---
            { 2014, 5, 26, 3, YEARS, 2017, 5, 26, true },
            { 2014, 5, 26, 3, DECADES, 2044, 5, 26, true },
            { 2014, 5, 26, 3, CENTURIES, 2314, 5, 26, true },
            { 2014, 5, 26, 3, MILLENNIA, 5014, 5, 26, true },
            { 2014, 5, 26, -1, ERAS, -2013, 5, 26, true }
        };
    }

    static Object[][] untilPeriodArguments() {
        // startY, startM, startD, endY, endM, endD, expectedY, expectedM, expectedD, comment
        return new Object[][] {
            { 1752, 7, 2, 1752, 7, 1, 0, 0, -1, "One day before" },
            { 1752, 7, 2, 1752, 7, 2, 0, 0, 0, "Same day" },
            { 1752, 7, 2, 1752, 9, 2, 0, 2, 0, "Exactly 2 months" },
            { 1752, 7, 2, 1752, 9, 14, 0, 2, 1, "2 months and 1 day (across cutover)" },
            { 1752, 9, 14, 1752, 9, 2, 0, 0, -1, "1 day before (across cutover)" },
            { 1752, 9, 14, 1752, 10, 14, 0, 1, 0, "Exactly 1 month" },
        };
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory and Conversion")
    class FactoryAndConversionTests {

        @DisplayName("of(y, m, d) should throw for invalid dates")
        @ParameterizedTest(name = "y={0}, m={1}, d={2}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#invalidDateParts")
        void of_invalidDate_throwsException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, day));
        }

        @DisplayName("from(LocalDate) should create correct BritishCutoverDate")
        @ParameterizedTest(name = "iso={1} -> cutover={0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void from_localDate_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @DisplayName("LocalDate.from(BritishCutoverDate) should return correct ISO date")
        @ParameterizedTest(name = "cutover={0} -> iso={1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void to_localDate_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @DisplayName("toEpochDay() should match ISO date's epoch day")
        @ParameterizedTest(name = "cutover={0}, iso={1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void toEpochDay_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @DisplayName("chronology.dateEpochDay() should create correct date")
        @ParameterizedTest(name = "iso={1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void chronology_dateEpochDay_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @DisplayName("chronology.date(TemporalAccessor) should create correct date")
        @ParameterizedTest(name = "iso={1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void chronology_date_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Date Properties")
    class DatePropertyTests {

        @DisplayName("lengthOfMonth() should be correct")
        @ParameterizedTest(name = "y={0}, m={1} -> length={2}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#monthLengths")
        void lengthOfMonth_isCorrect(int year, int month, int length) {
            assertEquals(length, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        @DisplayName("lengthOfYear() should be correct")
        @ParameterizedTest(name = "y={0} -> length={1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#yearLengths")
        void lengthOfYear_isCorrect(int year, int length) {
            assertEquals(length, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
            assertEquals(length, BritishCutoverDate.of(year, 12, 31).lengthOfYear());
        }

        @DisplayName("range(TemporalField) should return correct range")
        @ParameterizedTest(name = "{3} for {0}-{1}-{2}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#fieldRanges")
        void range_isCorrect(int year, int month, int day, TemporalField field, int expectedMin, int expectedMax) {
            assertEquals(ValueRange.of(expectedMin, expectedMax), BritishCutoverDate.of(year, month, day).range(field));
        }

        @DisplayName("getLong(TemporalField) should return correct value")
        @ParameterizedTest(name = "{3} for {0}-{1}-{2} is {4}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#fieldValues")
        void getLong_isCorrect(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, day).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class DateManipulationTests {

        @DisplayName("with(TemporalField, value) should set field correctly")
        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4})")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#withFieldArguments")
        void with_temporalField_isCorrect(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
            BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        @Test
        @DisplayName("with(TemporalAdjuster) for lastDayOfMonth is correct")
        void with_lastDayOfMonth_adjusterIsCorrect() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 30);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        @DisplayName("with(TemporalAdjuster) for another date is correct")
        void with_localDate_adjusterIsCorrect() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            LocalDate localDate = LocalDate.of(1752, 9, 14);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 14);
            assertEquals(expected, date.with(localDate));
        }

        @DisplayName("plus(amount, unit) should add duration correctly")
        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#plusMinusArguments")
        void plus_isCorrect(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed, boolean bidi) {
            BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
            BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @DisplayName("minus(amount, unit) should subtract duration correctly")
        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#plusMinusArguments")
        void minus_isCorrect(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d, boolean bidi) {
            if (bidi) {
                BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
                BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
                assertEquals(expected, start.minus(amount, unit));
            }
        }

        @DisplayName("until(endDate, unit) should calculate distance correctly")
        @ParameterizedTest(name = "from {0} to {1} in DAYS")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void until_days_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(0, cutoverDate.until(isoDate, DAYS));
            assertEquals(1, cutoverDate.until(isoDate.plusDays(1), DAYS));
            assertEquals(-1, cutoverDate.until(isoDate.minusDays(1), DAYS));
        }

        @DisplayName("until(endDate) should return correct period")
        @ParameterizedTest(name = "{9}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#untilPeriodArguments")
        void until_period_isCorrect(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed, String comment) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            ChronoPeriod expected = BritishCutoverChronology.INSTANCE.period(ey, em, ed);
            assertEquals(expected, start.until(end));
        }

        @DisplayName("start.plus(start.until(end)) should equal end")
        @ParameterizedTest(name = "from {0}-{1}-{2} to {3}-{4}-{5}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#untilPeriodArguments")
        void until_period_plus_roundtrip(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed, String comment) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            assertEquals(end, start.plus(start.until(end)));
        }

        @DisplayName("until() with an equivalent date should return a zero period")
        @ParameterizedTest(name = "{0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void until_equivalentDate_returnsZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(cutoverDate));
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
        }
    }

    @Nested
    @DisplayName("Chronology and Object Methods")
    class ChronologyAndObjectMethods {

        @Test
        @DisplayName("toString() should return correct representation")
        void toString_isCorrect() {
            assertEquals("BritishCutover AD 1-01-01", BritishCutoverDate.of(1, 1, 1).toString());
            assertEquals("BritishCutover AD 2012-06-23", BritishCutoverDate.of(2012, 6, 23).toString());
        }

        @Test
        @DisplayName("prolepticYear() should convert from era and year-of-era correctly")
        void prolepticYear_isCorrect() {
            assertEquals(4, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.AD, 4));
            assertEquals(1, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.AD, 1));
            assertEquals(0, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.BC, 1));
            assertEquals(-1, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.BC, 2));
        }
    }
}