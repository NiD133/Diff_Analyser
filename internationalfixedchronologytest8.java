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
import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("InternationalFixedChronology")
public class InternationalFixedChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    public static Object[][] sampleFixedAndIsoDates() {
        return new Object[][] {
            {InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {InternationalFixedDate.of(1, 1, 2), LocalDate.of(1, 1, 2)},
            {InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)},
            {InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)},
            {InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)},
            {InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)},
            {InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4)},
        };
    }

    public static Object[][] invalidDateParts() {
        return new Object[][] {
            {-1, 13, 28}, {0, 1, 1}, {1900, 14, 1}, {1900, 1, 0},
            {1900, 1, 29}, {1900, 13, 30},
        };
    }

    public static Object[][] nonLeapYears() {
        return new Object[][] {{1}, {100}, {1900}};
    }

    public static Object[][] monthLengths() {
        return new Object[][] {
            {1900, 1, 28, 28}, {1900, 6, 28, 28}, {1900, 13, 29, 29},
            {1904, 6, 29, 29},
        };
    }

    public static Object[][] invalidEraValues() {
        return new Object[][] {{-1}, {0}, {2}};
    }

    public static Object[][] invalidProlepticYears() {
        return new Object[][] {{-10}, {-1}, {0}};
    }

    public static Object[][] fieldRanges() {
        return new Object[][] {
            {2012, 6, 29, DAY_OF_MONTH, ValueRange.of(1, 29)},
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366)},
            {2011, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 365)},
            {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)},
            {2012, 6, 29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)},
            {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
        };
    }

    public static Object[][] fieldValues() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 5},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, 28 * 4 + 26},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, YEAR, 2014},
            {2012, 6, 29, DAY_OF_WEEK, 0},
            {2012, 6, 29, DAY_OF_MONTH, 29},
            {2012, 6, 29, DAY_OF_YEAR, 6 * 28 + 1},
        };
    }

    public static Object[][] dateAdjustments() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
            {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 13, 28},
            {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            {2012, 6, 29, YEAR, 2013, 2013, 6, 28},
        };
    }

    public static Object[][] invalidDateAdjustments() {
        return new Object[][] {
            {2013, 1, 1, DAY_OF_MONTH, 29},
            {2012, 6, 1, DAY_OF_MONTH, 30},
            {2013, 1, 1, DAY_OF_YEAR, 366},
            {2012, 1, 1, DAY_OF_YEAR, 367},
            {2013, 1, 1, MONTH_OF_YEAR, 14},
        };
    }

    public static Object[][] lastDayOfMonthSamples() {
        return new Object[][] {
            {2012, 6, 23, 2012, 6, 29},
            {2009, 6, 23, 2009, 6, 28},
            {2007, 13, 23, 2007, 13, 29},
        };
    }

    public static Object[][] plusSamples() {
        return new Object[][] {
            {2014, 5, 26, 8, DAYS, 2014, 6, 6},
            {2014, 5, 26, 3, WEEKS, 2014, 6, 19},
            {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
            {2014, 5, 26, 3, YEARS, 2017, 5, 26},
            {2014, 5, 26, 3, DECADES, 2044, 5, 26},
            {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
            {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
        };
    }

    public static Stream<Object[]> minusSamples() {
        return Arrays.stream(plusSamples())
            .map(p -> new Object[]{p[5], p[6], p[7], p[3], p[4], p[0], p[1], p[2]});
    }

    public static Object[][] plusSamplesForSpecialDays() {
        return new Object[][] {
            {2014, 13, 29, 8, DAYS, 2015, 1, 8},
            {2014, 13, 29, 3, WEEKS, 2015, 1, 21},
            {2014, 13, 29, 3, MONTHS, 2015, 3, 28},
            {2014, 13, 29, 3, YEARS, 2017, 13, 29},
            {2012, 6, 29, 8, DAYS, 2012, 7, 8},
            {2012, 6, 29, 3, WEEKS, 2012, 7, 22},
            {2012, 6, 29, 3, MONTHS, 2012, 9, 28},
            {2012, 6, 29, 3, YEARS, 2015, 6, 28},
        };
    }

    public static Stream<Object[]> minusSamplesForSpecialDays() {
        return Arrays.stream(plusSamplesForSpecialDays())
            .map(p -> new Object[]{p[5], p[6], p[7], p[3], p[4], p[0], p[1], p[2]});
    }

    public static Object[][] untilAmountSamples() {
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

    public static Object[][] untilPeriodSamples() {
        return new Object[][] {
            {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
            {2014, 5, 26, 2014, 6, 4, 0, 0, 6},
            {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
            {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
            {2012, 6, 29, 2016, 6, 29, 4, 0, 0},
            {2004, 6, 29, 2004, 13, 29, 0, 7, 0},
        };
    }

    public static Object[][] toStringSamples() {
        return new Object[][] {
            {InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"},
            {InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"},
            {InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"},
            {InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29"},
        };
    }

    @Nested
    @DisplayName("Date Creation")
    class CreationTests {
        @ParameterizedTest(name = "{1} {0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("dateEpochDay() creates correct date")
        void dateEpochDay_createsCorrectFixedDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest(name = "year={0}, month={1}, day={2}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#invalidDateParts")
        @DisplayName("of(y, m, d) with invalid parts throws exception")
        void of_withInvalidDateParts_throwsException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, day));
        }

        @ParameterizedTest(name = "year={0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#nonLeapYears")
        @DisplayName("of(y, 6, 29) in non-leap year throws exception")
        void of_withLeapDayInNonLeapYear_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }
    }

    @Nested
    @DisplayName("ISO Chronology Interoperability")
    class IsoInteroperabilityTests {
        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("LocalDate.from(InternationalFixedDate) converts correctly")
        void fixedDateToLocalDate_shouldConvertCorrectly(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(fixedDate));
        }

        @ParameterizedTest(name = "{1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("InternationalFixedDate.from(LocalDate) converts correctly")
        void localDateToFixedDate_shouldConvertCorrectly(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedDate.from(isoDate));
        }

        @ParameterizedTest(name = "{1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("toEpochDay() returns correct value")
        void toEpochDay_shouldReturnCorrectEpochDay(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), fixedDate.toEpochDay());
        }

        @ParameterizedTest(name = "{1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("chronology.date(TemporalAccessor) creates correct date from LocalDate")
        void chronologyDate_fromLocalDate_shouldCreateCorrectDate(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(fixedDate, InternationalFixedChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Field Access")
    class FieldAccessTests {
        @ParameterizedTest(name = "{0}, {1}, {2} -> {3}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#monthLengths")
        @DisplayName("lengthOfMonth() returns correct value")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int day, int length) {
            assertEquals(length, InternationalFixedDate.of(year, month, day).lengthOfMonth());
        }

        @ParameterizedTest(name = "{0}-{1}-{2} {3}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#fieldRanges")
        @DisplayName("range(TemporalField) returns correct range")
        void range_shouldReturnCorrectValueRange(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, InternationalFixedDate.of(year, month, dom).range(field));
        }

        @ParameterizedTest(name = "{0}-{1}-{2} {3}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#fieldValues")
        @DisplayName("getLong(TemporalField) returns correct value")
        void getLong_shouldReturnCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, InternationalFixedDate.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Adjustment")
    class AdjustmentTests {
        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#dateAdjustments")
        @DisplayName("with(TemporalField, long) returns adjusted date")
        void with_shouldReturnAdjustedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#invalidDateAdjustments")
        @DisplayName("with(TemporalField, long) with invalid value throws exception")
        void with_withInvalidValue_shouldThrowException(int y, int m, int d, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(y, m, d).with(field, value));
        }

        @ParameterizedTest(name = "{0}-{1}-{2}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#lastDayOfMonthSamples")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) returns last day of month")
        void with_lastDayOfMonth_shouldReturnLastDayOfMonth(int y, int m, int d, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Date Arithmetic")
    class ArithmeticTests {
        @ParameterizedTest(name = "{0} plus({1}, {2})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#plusSamples")
        void plus_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{0} plus({1}, {2})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#plusSamplesForSpecialDays")
        void plus_forSpecialDays_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{0} minus({1}, {2})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#minusSamples")
        void minus_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }

        @ParameterizedTest(name = "{0} minus({1}, {2})")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#minusSamplesForSpecialDays")
        void minus_forSpecialDays_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            InternationalFixedDate start = InternationalFixedDate.of(y, m, d);
            InternationalFixedDate expected = InternationalFixedDate.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }

        @ParameterizedTest(name = "{0} until {1} in {2}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#untilAmountSamples")
        void until_withUnit_shouldReturnCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest(name = "{0} until {1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#untilPeriodSamples")
        void until_withDate_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int yp, int mp, int dp) {
            InternationalFixedDate start = InternationalFixedDate.of(y1, m1, d1);
            InternationalFixedDate end = InternationalFixedDate.of(y2, m2, d2);
            ChronoPeriod expected = InternationalFixedChronology.INSTANCE.period(yp, mp, dp);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("until(same date) returns zero period")
        void until_sameFixedDate_returnsZeroPeriod(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(fixedDate));
        }

        @ParameterizedTest(name = "{0} and {1}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("until(equivalent ISO date) returns zero period")
        void until_equivalentIsoDate_returnsZeroPeriod(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixedDate.until(isoDate));
        }

        @ParameterizedTest(name = "{1} and {0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#sampleFixedAndIsoDates")
        @DisplayName("LocalDate.until(equivalent Fixed date) returns zero period")
        void isoDateUntil_equivalentFixedDate_returnsZeroPeriod(InternationalFixedDate fixedDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(fixedDate));
        }
    }

    @Nested
    @DisplayName("Era Handling")
    class EraTests {
        @ParameterizedTest(name = "era={0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#invalidEraValues")
        @DisplayName("eraOf() with invalid value throws exception")
        void eraOf_withInvalidValue_throwsException(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        @ParameterizedTest(name = "year={0}")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#invalidProlepticYears")
        @DisplayName("prolepticYear() with invalid year for CE era throws exception")
        void prolepticYear_withInvalidYearForCE_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }

        @Test
        @DisplayName("dateYearDay() with era is consistent with proleptic year version")
        void dateYearDay_withEra_isConsistentWithProlepticYear() {
            for (int year = 1; year < 200; year++) {
                InternationalFixedDate fromProlepticYear = InternationalFixedChronology.INSTANCE.dateYearDay(year, 1);
                assertEquals(year, fromProlepticYear.get(YEAR));
                assertEquals(InternationalFixedEra.CE, fromProlepticYear.getEra());
                assertEquals(year, fromProlepticYear.get(YEAR_OF_ERA));

                InternationalFixedDate fromEraAndYear = InternationalFixedChronology.INSTANCE.dateYearDay(InternationalFixedEra.CE, year, 1);
                assertEquals(fromProlepticYear, fromEraAndYear);
            }
        }
    }

    @Nested
    @DisplayName("String Representation")
    class ToStringTests {
        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTest#toStringSamples")
        @DisplayName("toString() returns correct format")
        void toString_shouldReturnCorrectString(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}