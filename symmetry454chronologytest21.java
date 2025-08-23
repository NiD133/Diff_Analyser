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
import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.MONTHS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Comprehensive tests for {@link Symmetry454Chronology} and {@link Symmetry454Date}.
 *
 * <p>This test suite is organized into nested classes to group related tests,
 * improving clarity and maintainability.
 */
class Symmetry454ChronologyTest {

    // -----------------------------------------------------------------------
    // Data Providers
    // -----------------------------------------------------------------------

    static Stream<Arguments> sampleSymmetryAndIsoDates() {
        return Stream.of(
            Arguments.of(Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry454Date.of(742, 3, 25), LocalDate.of(742, 4, 2)),
            Arguments.of(Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
            Arguments.of(Symmetry454Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)),
            Arguments.of(Symmetry454Date.of(1433, 11, 14), LocalDate.of(1433, 11, 10)),
            Arguments.of(Symmetry454Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)),
            Arguments.of(Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
            Arguments.of(Symmetry454Date.of(1564, 2, 20), LocalDate.of(1564, 2, 15)),
            Arguments.of(Symmetry454Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)),
            Arguments.of(Symmetry454Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)),
            Arguments.of(Symmetry454Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)),
            Arguments.of(Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
            Arguments.of(Symmetry454Date.of(1879, 3, 12), LocalDate.of(1879, 3, 14)),
            Arguments.of(Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)),
            Arguments.of(Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
            Arguments.of(Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1))
        );
    }

    static Stream<Arguments> invalidDateComponents() {
        return Stream.of(
            Arguments.of(-1, 13, 28), Arguments.of(-1, 13, 29),
            Arguments.of(2000, -2, 1), Arguments.of(2000, 13, 1), Arguments.of(2000, 15, 1),
            Arguments.of(2000, 1, -1), Arguments.of(2000, 1, 0), Arguments.of(2000, 0, 1),
            Arguments.of(2000, -1, 0), Arguments.of(2000, -1, 1),
            Arguments.of(2000, 1, 29), Arguments.of(2000, 2, 36), Arguments.of(2000, 3, 29),
            Arguments.of(2000, 4, 29), Arguments.of(2000, 5, 36), Arguments.of(2000, 6, 29),
            Arguments.of(2000, 7, 29), Arguments.of(2000, 8, 36), Arguments.of(2000, 9, 29),
            Arguments.of(2000, 10, 29), Arguments.of(2000, 11, 36), Arguments.of(2000, 12, 29),
            Arguments.of(2004, 12, 36)
        );
    }

    static Stream<Integer> commonYearsWithInvalidDecember29() {
        // In Symmetry454, Dec 29 only exists in leap years. These are common years.
        // Year 200 was removed as it is a leap year in this chronology.
        return Stream.of(1, 100, 2000);
    }

    static Stream<Arguments> monthLengths() {
        return Stream.of(
            Arguments.of(2000, 1, 28), Arguments.of(2000, 2, 35), Arguments.of(2000, 3, 28),
            Arguments.of(2000, 4, 28), Arguments.of(2000, 5, 35), Arguments.of(2000, 6, 28),
            Arguments.of(2000, 7, 28), Arguments.of(2000, 8, 35), Arguments.of(2000, 9, 28),
            Arguments.of(2000, 10, 28), Arguments.of(2000, 11, 35), Arguments.of(2000, 12, 28),
            Arguments.of(2004, 12, 35) // Leap year example
        );
    }

    static Stream<Era> unsupportedErasForProlepticYear() {
        // Symmetry454Chronology only supports IsoEra.
        return Stream.of(
            HijrahEra.AH,
            JapaneseEra.HEISEI,
            MinguoEra.ROC,
            ThaiBuddhistEra.BE
        );
    }

    static Stream<Arguments> fieldRanges() {
        return Stream.of(
            Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)),
            Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35)),
            Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 35)),
            Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
            Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)),
            Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)),
            Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
            Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)),
            Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
            Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
            Arguments.of(2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
            Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)),
            Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
            Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53))
        );
    }

    static Stream<Arguments> fieldValues() {
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5L),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 145L), // 28+35+28+28+26
            Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5L),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
            Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5L),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L), // 4+5+4+4+4
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 24172L), // 2014*12 + 5 - 1
            Arguments.of(2014, 5, 26, YEAR, 2014L),
            Arguments.of(2014, 5, 26, ERA, 1L),
            Arguments.of(1, 5, 8, ERA, 1L),
            Arguments.of(2012, 9, 26, DAY_OF_YEAR, 271L), // 3*(4+5+4)*7 - 2
            Arguments.of(2012, 9, 26, ALIGNED_WEEK_OF_YEAR, 39L), // 3*(4+5+4)
            Arguments.of(2015, 12, 35, DAY_OF_YEAR, 371L), // 4*(4+5+4)*7 + 7
            Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53L),
            Arguments.of(2015, 12, 35, PROLEPTIC_MONTH, 24191L) // 2016*12 - 1
        );
    }

    static Stream<Arguments> withFieldSamples() {
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26),
            Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
            Arguments.of(2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26),
            Arguments.of(2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29), // day adjusted
            Arguments.of(2015, 12, 29, YEAR, 2014, 2014, 12, 28) // day adjusted
        );
    }

    static Stream<Arguments> withInvalidFieldValues() {
        return Stream.of(
            Arguments.of(2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, 8),
            Arguments.of(2013, 1, 1, ALIGNED_WEEK_OF_MONTH, 5),
            Arguments.of(2013, 1, 1, ALIGNED_WEEK_OF_YEAR, 53),
            Arguments.of(2015, 1, 1, ALIGNED_WEEK_OF_YEAR, 54),
            Arguments.of(2013, 1, 1, DAY_OF_WEEK, 8),
            Arguments.of(2013, 1, 1, DAY_OF_MONTH, 29),
            Arguments.of(2015, 12, 1, DAY_OF_MONTH, 36),
            Arguments.of(2013, 1, 1, DAY_OF_YEAR, 365),
            Arguments.of(2015, 1, 1, DAY_OF_YEAR, 372),
            Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14),
            Arguments.of(2013, 1, 1, EPOCH_DAY, 364_523_156L),
            Arguments.of(2013, 1, 1, YEAR, 1_000_001)
        );
    }

    static Stream<Arguments> lastDayOfMonthSamples() {
        return Stream.of(
            Arguments.of(2012, 1, 23, 2012, 1, 28),
            Arguments.of(2012, 2, 23, 2012, 2, 35),
            Arguments.of(2009, 12, 23, 2009, 12, 35) // leap year
        );
    }

    static Stream<Arguments> plusPeriodSamples() {
        return Stream.of(
            Arguments.of(2014, 5, 26, 8, DAYS, 2014, 5, 34),
            Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 12),
            Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
            Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
            Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
            Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
            Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
            Arguments.of(2013, 6, 21, 6 * 52 + 1, WEEKS, 2019, 6, 21)
        );
    }

    static Stream<Arguments> plusPeriodLeapWeekSamples() {
        return Stream.of(
            Arguments.of(2015, 12, 28, 8, DAYS, 2016, 1, 1),
            Arguments.of(2015, 12, 28, 3, WEEKS, 2016, 1, 14),
            Arguments.of(2015, 12, 28, 3, MONTHS, 2016, 3, 28),
            Arguments.of(2015, 12, 28, 1, YEARS, 2016, 12, 28)
        );
    }

    static Stream<Arguments> untilUnitSamples() {
        return Stream.of(
            Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 13L),
            Arguments.of(2014, 5, 26, 2014, 6, 5, WEEKS, 1L),
            Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
            Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
            Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
            Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
            Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
            Arguments.of(2014, 5, 26, 3014, 5, 26, ERAS, 0L)
        );
    }

    static Stream<Arguments> untilPeriodSamples() {
        return Stream.of(
            Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
            Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 13),
            Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
            Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
            Arguments.of(2014, 5, 26, 2024, 5, 25, 9, 11, 27)
        );
    }

    static Stream<Arguments> toStringSamples() {
        return Stream.of(
            Arguments.of(Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"),
            Arguments.of(Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"),
            Arguments.of(Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35")
        );
    }

    // -----------------------------------------------------------------------
    // Test Cases
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("Conversion and Factory Tests")
    class ConversionAndFactoryTest {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void fromSymmetry454Date_convertsToCorrectLocalDate(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void fromLocalDate_convertsToCorrectSymmetry454Date(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void chronology_dateFromTemporal_convertsToCorrectSymmetry454Date(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Chronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void toEpochDay_isCorrect(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symmetryDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void chronology_dateFromEpochDay_isCorrect(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#invalidDateComponents")
        void of_withInvalidDateParts_throwsException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dayOfMonth));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#commonYearsWithInvalidDecember29")
        void of_withInvalidDayInCommonYear_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    @DisplayName("Field Access and Manipulation Tests")
    class FieldAccessTest {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#monthLengths")
        void lengthOfMonth_returnsCorrectValue(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, 1).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#fieldRanges")
        void range_forField_isCorrect(int year, int month, int dom, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(year, month, dom).range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#fieldValues")
        void getLong_forField_isCorrect(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#withFieldSamples")
        void with_fieldAndValue_returnsAdjustedDate(int year, int month, int dom, TemporalField field, long value, int exYear, int exMonth, int exDom) {
            Symmetry454Date baseDate = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expectedDate = Symmetry454Date.of(exYear, exMonth, exDom);
            assertEquals(expectedDate, baseDate.with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#withInvalidFieldValues")
        void with_invalidFieldAndValue_throwsException(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom).with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#lastDayOfMonthSamples")
        void with_lastDayOfMonthAdjuster_returnsCorrectDate(int year, int month, int day, int exYear, int exMonth, int exDay) {
            Symmetry454Date base = Symmetry454Date.of(year, month, day);
            Symmetry454Date expected = Symmetry454Date.of(exYear, exMonth, exDay);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Arithmetic Tests")
    class ArithmeticTest {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#plusPeriodSamples")
        void plus_period_returnsCorrectDate(int year, int month, int dom, long amount, TemporalUnit unit, int exYear, int exMonth, int exDom) {
            Symmetry454Date baseDate = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expectedDate = Symmetry454Date.of(exYear, exMonth, exDom);
            assertEquals(expectedDate, baseDate.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#plusPeriodLeapWeekSamples")
        void plus_periodAcrossLeapWeek_returnsCorrectDate(int year, int month, int dom, long amount, TemporalUnit unit, int exYear, int exMonth, int exDom) {
            Symmetry454Date baseDate = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expectedDate = Symmetry454Date.of(exYear, exMonth, exDom);
            assertEquals(expectedDate, baseDate.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#plusPeriodSamples")
        void minus_period_returnsCorrectDate(int exYear, int exMonth, int exDom, long amount, TemporalUnit unit, int year, int month, int dom) {
            Symmetry454Date baseDate = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expectedDate = Symmetry454Date.of(exYear, exMonth, exDom);
            assertEquals(expectedDate, baseDate.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#plusPeriodLeapWeekSamples")
        void minus_periodAcrossLeapWeek_returnsCorrectDate(int exYear, int exMonth, int exDom, long amount, TemporalUnit unit, int year, int month, int dom) {
            Symmetry454Date baseDate = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expectedDate = Symmetry454Date.of(exYear, exMonth, exDom);
            assertEquals(expectedDate, baseDate.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#untilUnitSamples")
        void until_withTemporalUnit_returnsCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#untilPeriodSamples")
        void until_withEndDate_returnsCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expectedPeriod = Symmetry454Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expectedPeriod, start.until(end));
        }

        @Test
        void until_unsupportedUnit_throwsException() {
            Symmetry454Date start = Symmetry454Date.of(2012, 6, 28);
            Symmetry454Date end = Symmetry454Date.of(2012, 7, 1);
            assertThrows(UnsupportedTemporalTypeException.class, () -> start.until(end, MINUTES));
        }

        @Test
        void until_sameDate_returnsZeroPeriod() {
            Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void until_withEquivalentDateInOtherChronology_returnsZeroPeriod(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), symmetryDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(symmetryDate));
        }
    }

    @Nested
    @DisplayName("Chronology API Tests")
    class ChronologyApiTest {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#unsupportedErasForProlepticYear")
        void prolepticYear_withUnsupportedEra_throwsException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("toString Tests")
    class ToStringTest {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#toStringSamples")
        void toString_returnsCorrectFormat(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}