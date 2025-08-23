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
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("JulianDate Tests")
class JulianDateTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Object[][] sampleJulianAndIsoDates() {
        return new Object[][] {
            {JulianDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
            {JulianDate.of(1, 1, 2), LocalDate.of(0, 12, 31)},
            {JulianDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
            {JulianDate.of(4, 2, 29), LocalDate.of(4, 2, 27)}, // Julian leap year
            {JulianDate.of(100, 2, 29), LocalDate.of(100, 2, 27)}, // Julian leap year, not Gregorian
            {JulianDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)},
            {JulianDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)},
            {JulianDate.of(1945, 10, 30), LocalDate.of(1945, 11, 12)},
            {JulianDate.of(2012, 6, 23), LocalDate.of(2012, 7, 6)},
        };
    }

    static Object[][] invalidDateComponents() {
        return new Object[][] {
            {1900, 0, 1}, {1900, 13, 1}, {1900, 1, 0}, {1900, 1, 32},
            {1900, 2, 30}, // 1900 is a leap year in Julian
            {1899, 2, 29}, // 1899 is not a leap year in Julian
            {1900, 4, 31},
        };
    }

    static Object[][] monthLengths() {
        return new Object[][] {
            {1900, 1, 31}, {1900, 2, 29}, {1900, 3, 31}, {1900, 4, 30}, {1900, 5, 31},
            {1900, 6, 30}, {1900, 7, 31}, {1900, 8, 31}, {1900, 9, 30}, {1900, 10, 31},
            {1900, 11, 30}, {1900, 12, 31},
            {1901, 2, 28}, {1904, 2, 29}, {2000, 2, 29}, {2100, 2, 29},
        };
    }

    static Object[][] fieldRanges() {
        return new Object[][] {
            {2012, 1, 23, DAY_OF_MONTH, 1, 31},
            {2012, 2, 23, DAY_OF_MONTH, 1, 29}, // leap year
            {2011, 2, 23, DAY_OF_MONTH, 1, 28}, // common year
            {2012, 1, 23, DAY_OF_YEAR, 1, 366}, // leap year
            {2011, 1, 23, DAY_OF_YEAR, 1, 365}, // common year
            {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5},
            {2011, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 4},
        };
    }

    static Object[][] fieldValues() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 7},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            // Day of year for May 26: 31(Jan)+28(Feb)+31(Mar)+30(Apr)+26(May) = 146
            {2014, 5, 26, DAY_OF_YEAR, 146},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12L + 5 - 1},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},
            {0, 6, 8, ERA, 0},
            {2014, 5, 26, WeekFields.ISO.dayOfWeek(), 7},
        };
    }

    static Object[][] withFieldSamples() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 3, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_MONTH, 31, 2014, 5, 31},
            {2014, 5, 26, DAY_OF_YEAR, 365, 2014, 12, 31},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9},
            {2014, 5, 26, MONTH_OF_YEAR, 7, 2014, 7, 26},
            {2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12L + 3 - 1, 2013, 3, 26},
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            {2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26},
            {2014, 5, 26, ERA, 0, -2013, 5, 26},
            {2011, 3, 31, MONTH_OF_YEAR, 2, 2011, 2, 28}, // Adjusts day for shorter month
            {2012, 3, 31, MONTH_OF_YEAR, 2, 2012, 2, 29}, // Adjusts day for shorter leap month
            {2012, 2, 29, YEAR, 2011, 2011, 2, 28}, // Adjusts day for non-leap year
        };
    }

    static Object[][] plusUnitSamples() {
        return new Object[][] {
            {2014, 5, 26, 8, DAYS, 2014, 6, 3},
            {2014, 5, 26, 3, WEEKS, 2014, 6, 16},
            {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
            {2014, 5, 26, 3, YEARS, 2017, 5, 26},
            {2014, 5, 26, 3, DECADES, 2044, 5, 26},
            {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
            {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
            {2014, 5, 26, -1, ERAS, -2013, 5, 26},
        };
    }

    static Object[][] untilSamples() {
        return new Object[][] {
            {2014, 5, 26, 2014, 6, 1, DAYS, 6},
            {2014, 5, 26, 2014, 5, 20, DAYS, -6},
            {2014, 5, 26, 2014, 6, 2, WEEKS, 1},
            {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
            {2014, 5, 26, 2015, 5, 26, YEARS, 1},
            {2014, 5, 26, 2024, 5, 26, DECADES, 1},
            {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
            {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
            {-2013, 5, 26, 2014, 5, 26, ERAS, 1},
        };
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory and Conversion Tests")
    class FactoryAndConversionTests {

        @ParameterizedTest(name = "Julian {0} -> ISO {1}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("from JulianDate to LocalDate")
        void toLocalDate_fromJulianDate(JulianDate julian, LocalDate iso) {
            assertEquals(iso, LocalDate.from(julian));
        }

        @ParameterizedTest(name = "ISO {1} -> Julian {0}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("from LocalDate to JulianDate")
        void fromLocalDate(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianDate.from(iso));
        }

        @ParameterizedTest(name = "EpochDay for {1} -> Julian {0}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("from epoch day")
        void fromEpochDay(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest(name = "Julian {0} -> EpochDay for {1}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("to epoch day")
        void toEpochDay(JulianDate julian, LocalDate iso) {
            assertEquals(iso.toEpochDay(), julian.toEpochDay());
        }

        @ParameterizedTest(name = "JulianDate from TemporalAccessor {1}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("from generic temporal")
        void fromTemporal(JulianDate julian, LocalDate iso) {
            assertEquals(julian, JulianChronology.INSTANCE.date(iso));
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#invalidDateComponents")
        @DisplayName("of() with invalid date throws exception")
        void of_invalidDate_throwsException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> JulianDate.of(year, month, dom));
        }
    }

    @Nested
    @DisplayName("Field and Property Access Tests")
    class FieldAndPropertyAccessTests {

        @ParameterizedTest(name = "{0}-{1} has {2} days")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#monthLengths")
        @DisplayName("lengthOfMonth()")
        void lengthOfMonth(int year, int month, int length) {
            assertEquals(length, JulianDate.of(year, month, 1).lengthOfMonth());
        }

        @ParameterizedTest(name = "range of {3} for {0}-{1}-{2} is {4}-{5}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#fieldRanges")
        @DisplayName("range()")
        void range(int year, int month, int dom, TemporalField field, int expectedMin, int expectedMax) {
            JulianDate date = JulianDate.of(year, month, dom);
            assertEquals(ValueRange.of(expectedMin, expectedMax), date.range(field));
        }

        @ParameterizedTest(name = "getLong({3}) for {0}-{1}-{2} is {4}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#fieldValues")
        @DisplayName("getLong()")
        void getLong(int year, int month, int dom, TemporalField field, long expected) {
            JulianDate date = JulianDate.of(year, month, dom);
            assertEquals(expected, date.getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Modification Tests")
    class ModificationTests {

        @ParameterizedTest(name = "{0}-{1}-{2} with {3}={4} -> {5}-{6}-{7}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#withFieldSamples")
        @DisplayName("with(TemporalField, long)")
        void with_temporalField(int year, int month, int dom, TemporalField field, long value,
                                int expectedYear, int expectedMonth, int expectedDom) {
            JulianDate start = JulianDate.of(year, month, dom);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.with(field, value));
        }
    }

    @Nested
    @DisplayName("Date Arithmetic Tests")
    class ArithmeticTests {

        @Test
        @DisplayName("plus/minus days is consistent with ISO date")
        void plusAndMinusDays_consistentWithIso() {
            JulianDate julian = JulianDate.of(2012, 6, 23);
            LocalDate iso = LocalDate.of(2012, 7, 6);

            assertEquals(iso, LocalDate.from(julian.plus(0, DAYS)));
            assertEquals(iso.plusDays(1), LocalDate.from(julian.plus(1, DAYS)));
            assertEquals(iso.plusDays(35), LocalDate.from(julian.plus(35, DAYS)));
            assertEquals(iso.plusDays(-1), LocalDate.from(julian.plus(-1, DAYS)));
            assertEquals(iso.plusDays(-60), LocalDate.from(julian.plus(-60, DAYS)));

            assertEquals(iso, LocalDate.from(julian.minus(0, DAYS)));
            assertEquals(iso.minusDays(1), LocalDate.from(julian.minus(1, DAYS)));
            assertEquals(iso.minusDays(35), LocalDate.from(julian.minus(35, DAYS)));
            assertEquals(iso.minusDays(-1), LocalDate.from(julian.minus(-1, DAYS)));
            assertEquals(iso.minusDays(-60), LocalDate.from(julian.minus(-60, DAYS)));
        }

        @ParameterizedTest(name = "{0} plus {1} {2} = {3}-{4}-{5}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#plusUnitSamples")
        @DisplayName("plus(long, TemporalUnit)")
        void plus_longAmountAndUnit(int startYear, int startMonth, int startDom,
                                    long amount, TemporalUnit unit,
                                    int expectedYear, int expectedMonth, int expectedDom) {
            JulianDate start = JulianDate.of(startYear, startMonth, startDom);
            JulianDate expected = JulianDate.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{3}-{4}-{5} minus {1} {2} = {0}-{1}-{2}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#plusUnitSamples")
        @DisplayName("minus(long, TemporalUnit) is inverse of plus")
        void minus_isInverseOfPlus(int startYear, int startMonth, int startDom,
                                   long amount, TemporalUnit unit,
                                   int endYear, int endMonth, int endDom) {
            JulianDate start = JulianDate.of(startYear, startMonth, startDom);
            JulianDate end = JulianDate.of(endYear, endMonth, endDom);
            assertEquals(start, end.minus(amount, unit));
        }

        @Test
        @DisplayName("minus(Period)")
        void minus_period() {
            JulianDate start = JulianDate.of(2014, 5, 26);
            ChronoPeriod period = JulianChronology.INSTANCE.period(0, 2, 3);
            JulianDate expected = JulianDate.of(2014, 3, 23);
            assertEquals(expected, start.minus(period));
        }

        @Test
        @DisplayName("until(date) for same date returns zero period")
        void until_sameDate_returnsZeroPeriod() {
            JulianDate date = JulianDate.of(2014, 5, 26);
            // A zero period in the Julian chronology
            ChronoPeriod zeroPeriod = JulianChronology.INSTANCE.period(0, 0, 0);
            assertEquals(zeroPeriod, date.until(date));
        }

        @ParameterizedTest(name = "until({0}, {1}) is consistent with ISO")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#sampleJulianAndIsoDates")
        @DisplayName("until(Temporal) cross-chronology returns zero period for equivalent dates")
        void until_crossChronology_equivalentDates_isZero(JulianDate julian, LocalDate iso) {
            // Julian.until(ISO) should convert ISO to Julian and find a zero difference
            assertEquals(JulianChronology.INSTANCE.period(0, 0, 0), julian.until(iso));
            // ISO.until(Julian) should convert Julian to ISO and find a zero difference
            assertEquals(Period.ZERO, iso.until(julian));
        }

        @ParameterizedTest(name = "until({0}-{1}-{2} to {3}-{4}-{5}) in {6} is {7}")
        @MethodSource("org.threeten.extra.chrono.JulianDateTest#untilSamples")
        @DisplayName("until(Temporal, TemporalUnit)")
        void until_temporalAndUnit(int year1, int month1, int dom1, int year2, int month2, int dom2,
                                   TemporalUnit unit, long expected) {
            JulianDate start = JulianDate.of(year1, month1, dom1);
            JulianDate end = JulianDate.of(year2, month2, dom2);
            assertEquals(expected, start.until(end, unit));
        }
    }

    @Nested
    @DisplayName("Object Behavior Tests")
    class ObjectBehaviorTests {

        @Test
        @DisplayName("toString()")
        void test_toString() {
            JulianDate julianDate = JulianDate.of(2012, 6, 23);
            assertEquals("Julian AD 2012-06-23", julianDate.toString());
        }
    }
}