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
import static org.threeten.extra.chrono.Symmetry454Chronology.WEEKS_IN_MONTH;
import static org.threeten.extra.chrono.Symmetry454Chronology.WEEKS_IN_MONTH_LONG;
import static org.threeten.extra.chrono.Symmetry454Chronology.WEEKS_IN_YEAR;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

/**
 * Tests for the {@link Symmetry454Chronology} and {@link Symmetry454Date}.
 */
class Symmetry454ChronologyTest {

    // Month constants for readable test data
    private static final int JAN = 1, FEB = 2, MAR = 3, APR = 4, MAY = 5, JUN = 6,
                             JUL = 7, AUG = 8, SEP = 9, OCT = 10, NOV = 11, DEC = 12;

    static Object[][] sampleSymmetryAndIsoDates() {
        return new Object[][] {
            { Symmetry454Date.of(1, JAN, 1), LocalDate.of(1, 1, 1) },
            { Symmetry454Date.of(272, FEB, 30), LocalDate.of(272, 2, 27) },
            { Symmetry454Date.of(272, FEB, 27), LocalDate.of(272, 2, 24) },
            { Symmetry454Date.of(742, MAR, 25), LocalDate.of(742, 4, 2) },
            { Symmetry454Date.of(742, APR, 2), LocalDate.of(742, 4, 7) },
            { Symmetry454Date.of(1066, OCT, 14), LocalDate.of(1066, 10, 14) },
            { Symmetry454Date.of(1304, JUL, 21), LocalDate.of(1304, 7, 20) },
            { Symmetry454Date.of(1304, JUL, 20), LocalDate.of(1304, 7, 19) },
            { Symmetry454Date.of(1433, NOV, 14), LocalDate.of(1433, 11, 10) },
            { Symmetry454Date.of(1433, NOV, 10), LocalDate.of(1433, 11, 6) },
            { Symmetry454Date.of(1452, APR, 11), LocalDate.of(1452, 4, 15) },
            { Symmetry454Date.of(1452, APR, 15), LocalDate.of(1452, 4, 19) },
            { Symmetry454Date.of(1492, OCT, 10), LocalDate.of(1492, 10, 12) },
            { Symmetry454Date.of(1492, OCT, 12), LocalDate.of(1492, 10, 14) },
            { Symmetry454Date.of(1564, FEB, 20), LocalDate.of(1564, 2, 15) },
            { Symmetry454Date.of(1564, FEB, 15), LocalDate.of(1564, 2, 10) },
            { Symmetry454Date.of(1564, APR, 28), LocalDate.of(1564, 4, 26) },
            { Symmetry454Date.of(1564, APR, 26), LocalDate.of(1564, 4, 24) },
            { Symmetry454Date.of(1643, JAN, 7), LocalDate.of(1643, 1, 4) },
            { Symmetry454Date.of(1643, JAN, 4), LocalDate.of(1643, 1, 1) },
            { Symmetry454Date.of(1707, APR, 12), LocalDate.of(1707, 4, 15) },
            { Symmetry454Date.of(1707, APR, 15), LocalDate.of(1707, 4, 18) },
            { Symmetry454Date.of(1789, JUL, 16), LocalDate.of(1789, 7, 14) },
            { Symmetry454Date.of(1789, JUL, 14), LocalDate.of(1789, 7, 12) },
            { Symmetry454Date.of(1879, MAR, 12), LocalDate.of(1879, 3, 14) },
            { Symmetry454Date.of(1879, MAR, 14), LocalDate.of(1879, 3, 16) },
            { Symmetry454Date.of(1941, SEP, 9), LocalDate.of(1941, 9, 9) },
            { Symmetry454Date.of(1970, JAN, 4), LocalDate.of(1970, 1, 1) },
            { Symmetry454Date.of(1970, JAN, 1), LocalDate.of(1969, 12, 29) },
            { Symmetry454Date.of(1999, DEC, 27), LocalDate.of(2000, 1, 1) },
            { Symmetry454Date.of(2000, JAN, 1), LocalDate.of(2000, 1, 3) },
        };
    }

    @Nested
    @DisplayName("Conversion Tests")
    class ConversionTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void fromSymmetry454Date_shouldReturnCorrespondingIsoDate(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void fromIsoDate_shouldReturnCorrespondingSymmetry454Date(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void fromEpochDay_shouldReturnCorrespondingSymmetry454Date(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void toEpochDay_shouldReturnCorrespondingEpochDay(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symmetryDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void fromTemporal_shouldReturnCorrespondingSymmetry454Date(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Chronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Invalid Date Creation")
    class InvalidDateCreationTests {
        static Object[][] invalidDateComponents() {
            return new Object[][] {
                { -1, DEC, 28 }, { -1, DEC, 29 }, { 2000, -2, 1 }, { 2000, 13, 1 },
                { 2000, 15, 1 }, { 2000, JAN, -1 }, { 2000, JAN, 0 }, { 2000, 0, 1 },
                { 2000, -1, 0 }, { 2000, -1, 1 }, { 2000, JAN, 29 }, { 2000, FEB, 36 },
                { 2000, MAR, 29 }, { 2000, APR, 29 }, { 2000, MAY, 36 }, { 2000, JUN, 29 },
                { 2000, JUL, 29 }, { 2000, AUG, 36 }, { 2000, SEP, 29 }, { 2000, OCT, 29 },
                { 2000, NOV, 36 }, { 2000, DEC, 29 }, { 2004, DEC, 36 },
            };
        }

        @ParameterizedTest
        @MethodSource("invalidDateComponents")
        void of_withInvalidDateComponents_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dayOfMonth));
        }

        static Object[][] nonLeapYearsForLeapDay() {
            return new Object[][] { { 1 }, { 100 }, { 200 }, { 2000 } };
        }

        @ParameterizedTest
        @MethodSource("nonLeapYearsForLeapDay")
        void of_withInvalidLeapDay_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, DEC, 29));
        }
    }

    @Nested
    @DisplayName("Date Properties")
    class DatePropertiesTests {
        static Object[][] monthLengths() {
            return new Object[][] {
                { 2000, JAN, 28, 28 }, { 2000, FEB, 28, 35 }, { 2000, MAR, 28, 28 },
                { 2000, APR, 28, 28 }, { 2000, MAY, 28, 35 }, { 2000, JUN, 28, 28 },
                { 2000, JUL, 28, 28 }, { 2000, AUG, 28, 35 }, { 2000, SEP, 28, 28 },
                { 2000, OCT, 28, 28 }, { 2000, NOV, 28, 35 }, { 2000, DEC, 28, 28 },
                { 2004, DEC, 20, 35 },
            };
        }

        @ParameterizedTest
        @MethodSource("monthLengths")
        void lengthOfMonth_shouldBeCorrect(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, day).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("monthLengths")
        void lengthOfMonth_shouldBeCorrectForFirstDay(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, 1).lengthOfMonth());
        }

        static Object[][] fieldRanges() {
            return new Object[][] {
                { 2012, JAN, 23, DAY_OF_MONTH, ValueRange.of(1, 28) },
                { 2012, FEB, 23, DAY_OF_MONTH, ValueRange.of(1, 35) },
                { 2015, DEC, 23, DAY_OF_MONTH, ValueRange.of(1, 35) },
                { 2012, JAN, 23, DAY_OF_WEEK, ValueRange.of(1, 7) },
                { 2012, JAN, 23, DAY_OF_YEAR, ValueRange.of(1, 364) },
                { 2015, JAN, 23, DAY_OF_YEAR, ValueRange.of(1, 371) },
                { 2012, JAN, 23, MONTH_OF_YEAR, ValueRange.of(1, 12) },
                { 2012, JAN, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7) },
                { 2012, JAN, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4) },
                { 2012, FEB, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5) },
                { 2015, DEC, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5) },
                { 2012, JAN, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7) },
                { 2012, JAN, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52) },
                { 2015, DEC, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53) },
            };
        }

        @ParameterizedTest
        @MethodSource("fieldRanges")
        void range_forField_shouldBeCorrect(int year, int month, int dayOfMonth, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(year, month, dayOfMonth).range(field));
        }

        static Object[][] fieldGetValues() {
            return new Object[][] {
                // date, field, expected value
                { 2014, MAY, 26, DAY_OF_WEEK, 5L },
                { 2014, MAY, 26, DAY_OF_MONTH, 26L },
                { 2014, MAY, 26, DAY_OF_YEAR, (long) DAYS_IN_MONTH + DAYS_IN_MONTH_LONG + DAYS_IN_MONTH + DAYS_IN_MONTH + 26 },
                { 2014, MAY, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5L },
                { 2014, MAY, 26, ALIGNED_WEEK_OF_MONTH, 4L },
                { 2014, MAY, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5L },
                { 2014, MAY, 26, ALIGNED_WEEK_OF_YEAR, (long) WEEKS_IN_MONTH + WEEKS_IN_MONTH_LONG + WEEKS_IN_MONTH + WEEKS_IN_MONTH + 4 },
                { 2014, MAY, 26, MONTH_OF_YEAR, 5L },
                { 2014, MAY, 26, PROLEPTIC_MONTH, 2014L * 12 + 5 - 1 },
                { 2014, MAY, 26, YEAR, 2014L },
                { 2014, MAY, 26, ERA, 1L },
                { 1, MAY, 8, ERA, 1L },
                { 2012, SEP, 26, DAY_OF_WEEK, 5L },
                { 2012, SEP, 26, DAY_OF_YEAR, 2 * DAYS_IN_QUARTER + DAYS_IN_MONTH + 26 },
                { 2012, SEP, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5L },
                { 2012, SEP, 26, ALIGNED_WEEK_OF_MONTH, 4L },
                { 2012, SEP, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5L },
                { 2012, SEP, 26, ALIGNED_WEEK_OF_YEAR, 2 * (WEEKS_IN_MONTH + WEEKS_IN_MONTH_LONG + WEEKS_IN_MONTH) + WEEKS_IN_MONTH + 4 },
                { 2015, DEC, 35, DAY_OF_WEEK, 7L },
                { 2015, DEC, 35, DAY_OF_MONTH, 35L },
                { 2015, DEC, 35, DAY_OF_YEAR, (long) DAYS_IN_YEAR_LONG },
                { 2015, DEC, 35, ALIGNED_DAY_OF_WEEK_IN_MONTH, 7L },
                { 2015, DEC, 35, ALIGNED_WEEK_OF_MONTH, 5L },
                { 2015, DEC, 35, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7L },
                { 2015, DEC, 35, ALIGNED_WEEK_OF_YEAR, (long) WEEKS_IN_YEAR + 1 },
                { 2015, DEC, 35, MONTH_OF_YEAR, 12L },
                { 2015, DEC, 35, PROLEPTIC_MONTH, 2016L * 12 - 1 },
            };
        }

        @ParameterizedTest
        @MethodSource("fieldGetValues")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int dayOfMonth, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, dayOfMonth).getLong(field));
        }

        static Object[][] toStringSamples() {
            return new Object[][] {
                { Symmetry454Date.of(1, JAN, 1), "Sym454 CE 1/01/01" },
                { Symmetry454Date.of(1970, FEB, 35), "Sym454 CE 1970/02/35" },
                { Symmetry454Date.of(2000, AUG, 35), "Sym454 CE 2000/08/35" },
                { Symmetry454Date.of(1970, DEC, 35), "Sym454 CE 1970/12/35" },
            };
        }

        @ParameterizedTest
        @MethodSource("toStringSamples")
        void toString_shouldReturnCorrectFormat(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }

    @Nested
    @DisplayName("Date Arithmetic")
    class ArithmeticTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void plusDays_shouldAddCorrectly(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(symmetryDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(symmetryDate.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(symmetryDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(symmetryDate.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void minusDays_shouldSubtractCorrectly(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate.minus(0, DAYS)));
            assertEquals(isoDate.minusDays(1), LocalDate.from(symmetryDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(symmetryDate.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(symmetryDate.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(symmetryDate.minus(-60, DAYS)));
        }

        static Object[][] plusSamples() {
            return new Object[][] {
                { 2014, MAY, 26, 0, DAYS, 2014, MAY, 26 },
                { 2014, MAY, 26, 8, DAYS, 2014, MAY, 34 },
                { 2014, MAY, 26, -3, DAYS, 2014, MAY, 23 },
                { 2014, MAY, 26, 3, WEEKS, 2014, JUN, 12 },
                { 2014, MAY, 26, 3, MONTHS, 2014, AUG, 26 },
                { 2014, MAY, 26, 3, YEARS, 2017, MAY, 26 },
                { 2014, MAY, 26, 3, DECADES, 2044, MAY, 26 },
                { 2014, MAY, 26, 3, CENTURIES, 2314, MAY, 26 },
                { 2014, MAY, 26, 3, MILLENNIA, 5014, MAY, 26 },
                { 2014, DEC, 26, 3, WEEKS, 2015, JAN, 19 },
                { 2013, JUN, 21, 6 * 52 + 1, WEEKS, 2019, JUN, 21 },
            };
        }

        @ParameterizedTest
        @MethodSource("plusSamples")
        void plus_withUnit_shouldAddCorrectly(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusSamples")
        void minus_withUnit_shouldSubtractCorrectly(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Object[][] plusLeapWeekSamples() {
            return new Object[][] {
                { 2015, DEC, 28, 8, DAYS, 2016, JAN, 1 },
                { 2015, DEC, 28, 3, WEEKS, 2016, JAN, 14 },
                { 2015, DEC, 28, 52, WEEKS, 2016, DEC, 21 },
                { 2015, DEC, 28, 3, MONTHS, 2016, MAR, 28 },
                { 2015, DEC, 28, 12, MONTHS, 2016, DEC, 28 },
                { 2015, DEC, 28, 3, YEARS, 2018, DEC, 28 },
            };
        }

        @ParameterizedTest
        @MethodSource("plusLeapWeekSamples")
        void plus_withUnitAcrossLeapWeek_shouldAddCorrectly(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusLeapWeekSamples")
        void minus_withUnitAcrossLeapWeek_shouldSubtractCorrectly(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }
    }

    @Nested
    @DisplayName("`until` method tests")
    class UntilTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void until_sameSymmetry454Date_shouldReturnZeroPeriod(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), symmetryDate.until(symmetryDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void until_equivalentIsoDate_shouldReturnZeroPeriod(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), symmetryDate.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void until_fromIsoDateToEquivalentSymmetry454Date_shouldReturnZeroPeriod(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(symmetryDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void until_days_shouldReturnCorrectDuration(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(0, symmetryDate.until(isoDate.plusDays(0), DAYS));
            assertEquals(1, symmetryDate.until(isoDate.plusDays(1), DAYS));
            assertEquals(35, symmetryDate.until(isoDate.plusDays(35), DAYS));
            assertEquals(-40, symmetryDate.until(isoDate.minusDays(40), DAYS));
        }

        static Object[][] untilTemporalUnitSamples() {
            return new Object[][] {
                { 2014, MAY, 26, 2014, MAY, 26, DAYS, 0 },
                { 2014, MAY, 26, 2014, JUN, 4, DAYS, 13 },
                { 2014, MAY, 26, 2014, MAY, 20, DAYS, -6 },
                { 2014, MAY, 26, 2014, JUN, 5, WEEKS, 1 },
                { 2014, MAY, 26, 2014, JUN, 26, MONTHS, 1 },
                { 2014, MAY, 26, 2015, MAY, 26, YEARS, 1 },
                { 2014, MAY, 26, 2024, MAY, 26, DECADES, 1 },
                { 2014, MAY, 26, 2114, MAY, 26, CENTURIES, 1 },
                { 2014, MAY, 26, 3014, MAY, 26, MILLENNIA, 1 },
                { 2014, MAY, 26, 3014, MAY, 26, ERAS, 0 },
            };
        }

        @ParameterizedTest
        @MethodSource("untilTemporalUnitSamples")
        void until_withUnit_shouldReturnCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Object[][] untilPeriodSamples() {
            return new Object[][] {
                { 2014, MAY, 26, 2014, MAY, 26, 0, 0, 0 },
                { 2014, MAY, 26, 2014, JUN, 4, 0, 0, 13 },
                { 2014, MAY, 26, 2014, MAY, 20, 0, 0, -6 },
                { 2014, MAY, 26, 2014, JUN, 26, 0, 1, 0 },
                { 2014, MAY, 26, 2015, MAY, 25, 0, 11, 27 },
                { 2014, MAY, 26, 2015, MAY, 26, 1, 0, 0 },
            };
        }

        @ParameterizedTest
        @MethodSource("untilPeriodSamples")
        void until_asPeriod_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("`with` and Adjuster Tests")
    class WithAndAdjusterTests {
        static Object[][] withFieldSamples() {
            return new Object[][] {
                // initial date, field, new value, expected date
                { 2014, MAY, 26, DAY_OF_WEEK, 1, 2014, MAY, 22 },
                { 2014, MAY, 26, DAY_OF_MONTH, 28, 2014, MAY, 28 },
                { 2014, MAY, 26, DAY_OF_YEAR, 364, 2014, DEC, 28 },
                { 2014, MAY, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3, 2014, MAY, 24 },
                { 2014, MAY, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, MAY, 5 },
                { 2014, MAY, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2, 2014, MAY, 23 },
                { 2014, MAY, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, JUN, 19 },
                { 2014, MAY, 26, MONTH_OF_YEAR, APR, 2014, APR, 26 },
                { 2014, MAY, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, MAR, 26 },
                { 2014, MAY, 26, YEAR, 2012, 2012, MAY, 26 },
                { 2014, MAY, 26, YEAR_OF_ERA, 2012, 2012, MAY, 26 },
                { 2014, MAY, 26, ERA, 1, 2014, MAY, 26 },
                { 2015, DEC, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 7, 2015, DEC, 28 },
                { 2015, DEC, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7, 2015, DEC, 28 },
                { 2015, DEC, 29, ALIGNED_WEEK_OF_MONTH, 3, 2015, DEC, 15 },
                { 2015, DEC, 29, ALIGNED_WEEK_OF_YEAR, 3, 2015, JAN, 15 },
                { 2015, DEC, 28, DAY_OF_WEEK, 1, 2015, DEC, 22 },
                { 2015, DEC, 29, DAY_OF_MONTH, 3, 2015, DEC, 3 },
                { 2015, DEC, 29, MONTH_OF_YEAR, FEB, 2015, FEB, 29 },
                { 2015, DEC, 29, YEAR, 2014, 2014, DEC, 28 },
                { 2015, MAR, 28, DAY_OF_YEAR, 371, 2015, DEC, 35 },
            };
        }

        @ParameterizedTest
        @MethodSource("withFieldSamples")
        void with_field_shouldReturnCorrectlyAdjustedDate(int y, int m, int d, TemporalField f, long val, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.with(f, val));
        }

        static Object[][] withInvalidFieldSamples() {
            return new Object[][] {
                { 2013, JAN, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, 8 },
                { 2013, JAN, 1, ALIGNED_WEEK_OF_MONTH, 5 },
                { 2013, FEB, 1, ALIGNED_WEEK_OF_MONTH, 6 },
                { 2013, JAN, 1, ALIGNED_WEEK_OF_YEAR, 53 },
                { 2015, JAN, 1, ALIGNED_WEEK_OF_YEAR, 54 },
                { 2013, JAN, 1, DAY_OF_WEEK, 8 },
                { 2013, JAN, 1, DAY_OF_MONTH, 29 },
                { 2015, DEC, 1, DAY_OF_MONTH, 36 },
                { 2013, JAN, 1, DAY_OF_YEAR, 365 },
                { 2015, JAN, 1, DAY_OF_YEAR, 372 },
                { 2013, JAN, 1, MONTH_OF_YEAR, 14 },
                { 2013, JAN, 1, EPOCH_DAY, -365_961_481 },
                { 2013, JAN, 1, YEAR, 1_000_001 },
            };
        }

        @ParameterizedTest
        @MethodSource("withInvalidFieldSamples")
        void with_invalidFieldValue_shouldThrowException(int y, int m, int d, TemporalField f, long val) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(y, m, d).with(f, val));
        }

        static Object[][] lastDayOfMonthSamples() {
            return new Object[][] {
                { 2012, JAN, 23, 2012, JAN, 28 }, { 2012, FEB, 23, 2012, FEB, 35 },
                { 2012, MAR, 23, 2012, MAR, 28 }, { 2012, APR, 23, 2012, APR, 28 },
                { 2012, MAY, 23, 2012, MAY, 35 }, { 2012, JUN, 23, 2012, JUN, 28 },
                { 2012, JUL, 23, 2012, JUL, 28 }, { 2012, AUG, 23, 2012, AUG, 35 },
                { 2012, SEP, 23, 2012, SEP, 28 }, { 2012, OCT, 23, 2012, OCT, 28 },
                { 2012, NOV, 23, 2012, NOV, 35 }, { 2012, DEC, 23, 2012, DEC, 28 },
                { 2009, DEC, 23, 2009, DEC, 35 },
            };
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthSamples")
        void with_lastDayOfMonthAdjuster_shouldReturnCorrectDate(int y, int m, int d, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void adjustLocalDateTimeToSymmetry454Date() {
            Symmetry454Date sym454 = Symmetry454Date.of(2012, JUL, 19);
            LocalDateTime test = LocalDateTime.MIN.with(sym454);
            assertEquals(LocalDateTime.of(2012, 7, 20, 0, 0), test);
        }
    }

    @Nested
    @DisplayName("Era Tests")
    class EraTests {
        // These eras are from the threeten-extra library, not standard java.time.
        // The test confirms that using non-ISO/Symmetry454 eras throws an exception.
        static Object[][] invalidErasForProlepticYear() {
            return new Era[][] {
                { AccountingEra.BCE }, { AccountingEra.CE },
                { CopticEra.BEFORE_AM }, { CopticEra.AM },
                { DiscordianEra.YOLD },
                { EthiopicEra.BEFORE_INCARNATION }, { EthiopicEra.INCARNATION },
                { HijrahEra.AH },
                { InternationalFixedEra.CE },
                { JapaneseEra.MEIJI }, { JapaneseEra.TAISHO }, { JapaneseEra.SHOWA }, { JapaneseEra.HEISEI },
                { JulianEra.BC }, { JulianEra.AD },
                { MinguoEra.BEFORE_ROC }, { MinguoEra.ROC },
                { PaxEra.BCE }, { PaxEra.CE },
                { ThaiBuddhistEra.BEFORE_BE }, { ThaiBuddhistEra.BE },
            };
        }

        @ParameterizedTest
        @MethodSource("invalidErasForProlepticYear")
        void prolepticYear_withInvalidEra_shouldThrowException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }
}