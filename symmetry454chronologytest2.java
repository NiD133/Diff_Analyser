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
import static org.threeten.extra.chrono.Symmetry454Chronology.DAYS_IN_MONTH;
import static org.threeten.extra.chrono.Symmetry454Chronology.DAYS_IN_MONTH_LONG;
import static org.threeten.extra.chrono.Symmetry454Chronology.DAYS_IN_QUARTER;
import static org.threeten.extra.chrono.Symmetry454Chronology.DAYS_IN_YEAR_LONG;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Symmetry454Chronology")
public class Symmetry454ChronologyTest {

    // -----------------------------------------------------------------------
    // Data Providers
    // -----------------------------------------------------------------------

    public static Object[][] sampleSymmetryAndIsoDates() {
        return new Object[][] {
            {Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)},
            {Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)},
            {Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)},
            {Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)},
            {Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)},
            {Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1)},
        };
    }

    // -----------------------------------------------------------------------
    // Tests
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("Inter-conversion with ISO calendar")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("converts to LocalDate")
        void convertsToLocalDate(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("converts from LocalDate")
        void convertsFromLocalDate(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("creates from epoch day")
        void createsFromEpochDay(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("converts to epoch day")
        void convertsToEpochDay(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symmetryDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("creates from TemporalAccessor")
        void createsFromTemporalAccessor(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Chronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("calculates a zero period for equivalent dates")
        void until_withEquivalentDate_isZero(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), symmetryDate.until(symmetryDate));
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), symmetryDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(symmetryDate));
        }
    }

    @Nested
    @DisplayName("Factory methods")
    class FactoryMethodTests {

        public static Object[][] invalidDateParts() {
            return new Object[][] {
                // year, month, dayOfMonth
                {-1, 13, 28}, {2000, -2, 1}, {2000, 13, 1}, {2000, 1, -1},
                {2000, 1, 0}, {2000, 1, 29}, {2000, 2, 36}, {2000, 3, 29},
                {2004, 12, 36}
            };
        }

        @ParameterizedTest
        @MethodSource("invalidDateParts")
        @DisplayName("of() throws exception for invalid date parts")
        void of_throwsForInvalidDateParts(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dayOfMonth));
        }

        public static Object[][] nonLeapYears() {
            return new Object[][] {{1}, {100}, {200}, {2000}};
        }

        @ParameterizedTest
        @MethodSource("nonLeapYears")
        @DisplayName("of() throws exception for invalid leap day in non-leap year")
        void of_throwsForInvalidLeapDay(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }

        @Test
        @DisplayName("dateYearDay() throws exception for day of year too large")
        void dateYearDay_throwsForDayOfYearTooLarge() {
            assertThrows(DateTimeException.class, () -> Symmetry454Chronology.INSTANCE.dateYearDay(2000, 365));
        }
    }

    @Nested
    @DisplayName("Date manipulation")
    class ManipulationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("plus(DAYS) is consistent with LocalDate")
        void plusDays_isConsistentWithLocalDate(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(symmetryDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(symmetryDate.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(symmetryDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(symmetryDate.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("minus(DAYS) is consistent with LocalDate")
        void minusDays_isConsistentWithLocalDate(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate.minus(0, DAYS)));
            assertEquals(isoDate.minusDays(1), LocalDate.from(symmetryDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(symmetryDate.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(symmetryDate.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(symmetryDate.minus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("until(DAYS) is consistent with LocalDate")
        void until_days_isConsistentWithLocalDate(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(0, symmetryDate.until(isoDate.plusDays(0), DAYS));
            assertEquals(1, symmetryDate.until(isoDate.plusDays(1), DAYS));
            assertEquals(35, symmetryDate.until(isoDate.plusDays(35), DAYS));
            assertEquals(-40, symmetryDate.until(isoDate.minusDays(40), DAYS));
        }

        public static Object[][] withFieldAdjustments() {
            return new Object[][] {
                // year, month, day, field, value, expectedYear, expectedMonth, expectedDay
                {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
                {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
                {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19},
                {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
                {2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                {2015, 12, 29, YEAR, 2014, 2014, 12, 28},
                {2015, 3, 28, DAY_OF_YEAR, 371, 2015, 12, 35},
            };
        }

        @ParameterizedTest
        @MethodSource("withFieldAdjustments")
        @DisplayName("with() adjusts date correctly")
        void with_adjustsDateCorrectly(int year, int month, int dom, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.with(field, value));
        }

        public static Object[][] invalidWithFieldValues() {
            return new Object[][] {
                // year, month, day, field, value
                {2013, 1, 1, ALIGNED_WEEK_OF_MONTH, 5},
                {2013, 1, 1, ALIGNED_WEEK_OF_YEAR, 53},
                {2013, 1, 1, DAY_OF_WEEK, 8},
                {2013, 1, 1, DAY_OF_MONTH, 29},
                {2013, 1, 1, DAY_OF_YEAR, 365},
                {2013, 1, 1, MONTH_OF_YEAR, 14},
                {2013, 1, 1, YEAR, 1_000_001},
            };
        }

        @ParameterizedTest
        @MethodSource("invalidWithFieldValues")
        @DisplayName("with() throws exception for invalid value")
        void with_throwsForInvalidValue(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom).with(field, value));
        }

        public static Object[][] lastDayOfMonthAdjustments() {
            return new Object[][] {
                // year, month, day, expectedYear, expectedMonth, expectedDay
                {2012, 1, 23, 2012, 1, 28}, {2012, 2, 23, 2012, 2, 35},
                {2012, 3, 23, 2012, 3, 28}, {2009, 12, 23, 2009, 12, 35},
            };
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthAdjustments")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) adjusts to end of month")
        void with_lastDayOfMonth_adjustsToEndOfMonth(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
            Symmetry454Date base = Symmetry454Date.of(year, month, day);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }

        public static Object[][] plusMinusSamples() {
            return new Object[][] {
                // year, month, day, amount, unit, expectedYear, expectedMonth, expectedDay
                {2014, 5, 26, 8, DAYS, 2014, 5, 34}, {2014, 5, 26, 3, WEEKS, 2014, 6, 12},
                {2014, 5, 26, 3, MONTHS, 2014, 8, 26}, {2014, 5, 26, 3, YEARS, 2017, 5, 26},
                {2014, 5, 26, 3, DECADES, 2044, 5, 26}, {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
                {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
            };
        }

        @ParameterizedTest
        @MethodSource("plusMinusSamples")
        @DisplayName("plus() adds the specified amount")
        void plus_withVariousUnits(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusMinusSamples")
        @DisplayName("minus() subtracts the specified amount")
        void minus_withVariousUnits(int expectedYear, int expectedMonth, int expectedDom, long amount, TemporalUnit unit, int year, int month, int dom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.minus(amount, unit));
        }

        public static Object[][] plusMinusLeapWeekSamples() {
            return new Object[][] {
                // year, month, day, amount, unit, expectedYear, expectedMonth, expectedDay
                {2015, 12, 28, 8, DAYS, 2016, 1, 1}, {2015, 12, 28, 3, WEEKS, 2016, 1, 14},
                {2015, 12, 28, 52, WEEKS, 2016, 12, 21}, {2015, 12, 28, 3, MONTHS, 2016, 3, 28},
                {2015, 12, 28, 1, YEARS, 2016, 12, 28},
            };
        }

        @ParameterizedTest
        @MethodSource("plusMinusLeapWeekSamples")
        @DisplayName("plus() correctly handles leap week")
        void plus_withVariousUnits_acrossLeapWeek(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusMinusLeapWeekSamples")
        @DisplayName("minus() correctly handles leap week")
        void minus_withVariousUnits_acrossLeapWeek(int expectedYear, int expectedMonth, int expectedDom, long amount, TemporalUnit unit, int year, int month, int dom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.minus(amount, unit));
        }

        public static Object[][] untilSamples() {
            return new Object[][] {
                // y1, m1, d1, y2, m2, d2, unit, expected
                {2014, 5, 26, 2014, 6, 4, DAYS, 13}, {2014, 5, 26, 2014, 6, 5, WEEKS, 1},
                {2014, 5, 26, 2014, 6, 26, MONTHS, 1}, {2014, 5, 26, 2015, 5, 26, YEARS, 1},
                {2014, 5, 26, 2024, 5, 26, DECADES, 1}, {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
                {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1}, {2014, 5, 26, 3014, 5, 26, ERAS, 0},
            };
        }

        @ParameterizedTest
        @MethodSource("untilSamples")
        @DisplayName("until() calculates amount between dates")
        void until_withVariousUnits(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        public static Object[][] untilPeriodSamples() {
            return new Object[][] {
                // y1, m1, d1, y2, m2, d2, expectedYears, expectedMonths, expectedDays
                {2014, 5, 26, 2014, 5, 26, 0, 0, 0}, {2014, 5, 26, 2014, 6, 4, 0, 0, 13},
                {2014, 5, 26, 2014, 6, 26, 0, 1, 0}, {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
                {2014, 5, 26, 2024, 5, 25, 9, 11, 27},
            };
        }

        @ParameterizedTest
        @MethodSource("untilPeriodSamples")
        @DisplayName("until() returns correct period")
        void until_returnsCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int expectedY, int expectedM, int expectedD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(expectedY, expectedM, expectedD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Field access")
    class FieldAccessTests {

        public static Object[][] monthLengths() {
            return new Object[][] {
                // year, month, day, expectedLength
                {2000, 1, 28, 28}, {2000, 2, 28, 35}, {2000, 3, 28, 28},
                {2000, 4, 28, 28}, {2000, 5, 28, 35}, {2000, 6, 28, 28},
                {2000, 7, 28, 28}, {2000, 8, 28, 35}, {2000, 9, 28, 28},
                {2000, 10, 28, 28}, {2000, 11, 28, 35}, {2000, 12, 28, 28},
                {2004, 12, 20, 35},
            };
        }

        @ParameterizedTest
        @MethodSource("monthLengths")
        @DisplayName("lengthOfMonth() returns correct length")
        void lengthOfMonth_returnsCorrectLength(int year, int month, int day, int length) {
            assertEquals(length, Symmetry454Date.of(year, month, day).lengthOfMonth());
        }

        public static Object[][] fieldRanges() {
            return new Object[][] {
                // year, month, day, field, expectedRange
                {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
                {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
                {2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
                {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
                {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)},
                {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)},
                {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},
                {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
                {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},
                {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
                {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)},
            };
        }

        @ParameterizedTest
        @MethodSource("fieldRanges")
        @DisplayName("range() returns correct range for field")
        void range_returnsCorrectRangeForField(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, Symmetry454Date.of(year, month, dom).range(field));
        }

        public static Object[][] dateFields() {
            return new Object[][] {
                // year, month, day, field, expectedValue
                {2014, 5, 26, DAY_OF_WEEK, 5},
                {2014, 5, 26, DAY_OF_MONTH, 26},
                {2014, 5, 26, DAY_OF_YEAR, DAYS_IN_MONTH + DAYS_IN_MONTH_LONG + DAYS_IN_MONTH + DAYS_IN_MONTH + 26},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4 + 5 + 4 + 4 + 4},
                {2014, 5, 26, MONTH_OF_YEAR, 5},
                {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
                {2014, 5, 26, YEAR, 2014},
                {2014, 5, 26, ERA, 1},
                {2012, 9, 26, DAY_OF_YEAR, 3 * DAYS_IN_QUARTER - 2},
                {2015, 12, 35, DAY_OF_YEAR, DAYS_IN_YEAR_LONG},
                {2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53},
            };
        }

        @ParameterizedTest
        @MethodSource("dateFields")
        @DisplayName("getLong() returns correct field value")
        void getLong_returnsCorrectFieldValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApiTests {

        public static Object[][] unsupportedEras() {
            return new Era[][] {
                {AccountingEra.BCE}, {CopticEra.AM}, {DiscordianEra.YOLD},
                {EthiopicEra.INCARNATION}, {HijrahEra.AH}, {InternationalFixedEra.CE},
                {JapaneseEra.HEISEI}, {JulianEra.AD}, {MinguoEra.ROC},
                {PaxEra.CE}, {ThaiBuddhistEra.BE},
            };
        }

        @ParameterizedTest
        @MethodSource("unsupportedEras")
        @DisplayName("prolepticYear() throws exception for unsupported era")
        void prolepticYear_throwsForUnsupportedEra(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("Object methods")
    class ObjectMethodTests {

        public static Object[][] toStringSamples() {
            return new Object[][] {
                {Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"},
                {Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"},
                {Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35"},
                {Symmetry454Date.of(1970, 12, 35), "Sym454 CE 1970/12/35"},
            };
        }

        @ParameterizedTest
        @MethodSource("toStringSamples")
        @DisplayName("toString() returns correct format")
        void toString_returnsCorrectFormat(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}