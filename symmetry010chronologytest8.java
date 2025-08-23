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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.testing.EqualsTester;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link Symmetry010Chronology} and {@link Symmetry010Date}.
 */
class Symmetry010ChronologyTest {

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> provideSymmetryAndIsoDatePairs() {
        return Stream.of(
            Arguments.of(Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry010Date.of(742, 3, 27), LocalDate.of(742, 4, 2)),
            Arguments.of(Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
            Arguments.of(Symmetry010Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)),
            Arguments.of(Symmetry010Date.of(1433, 11, 12), LocalDate.of(1433, 11, 10)),
            Arguments.of(Symmetry010Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)),
            Arguments.of(Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
            Arguments.of(Symmetry010Date.of(1564, 2, 18), LocalDate.of(1564, 2, 15)),
            Arguments.of(Symmetry010Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)),
            Arguments.of(Symmetry010Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)),
            Arguments.of(Symmetry010Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)),
            Arguments.of(Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
            Arguments.of(Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)),
            Arguments.of(Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9)),
            Arguments.of(Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
            Arguments.of(Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1))
        );
    }

    @Nested
    class ConversionTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void localDateFromSymmetry010Date_isCorrect(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void symmetry010DateFromLocalDate_isCorrect(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(symDate, Symmetry010Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void dateFromEpochDay_isCorrect(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(symDate, Symmetry010Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void toEpochDay_isCorrect(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void dateFromTemporalAccessor_isCorrect(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(symDate, Symmetry010Chronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    class DateCreationTests {
        static Stream<Arguments> provideInvalidDateComponents() {
            return Stream.of(
                Arguments.of(-1, 13, 28), Arguments.of(-1, 13, 29), Arguments.of(2000, -2, 1),
                Arguments.of(2000, 13, 1), Arguments.of(2000, 15, 1), Arguments.of(2000, 1, -1),
                Arguments.of(2000, 1, 0), Arguments.of(2000, 0, 1), Arguments.of(2000, -1, 0),
                Arguments.of(2000, -1, 1), Arguments.of(2000, 1, 31), Arguments.of(2000, 2, 32),
                Arguments.of(2000, 3, 31), Arguments.of(2000, 4, 31), Arguments.of(2000, 5, 32),
                Arguments.of(2000, 6, 31), Arguments.of(2000, 7, 31), Arguments.of(2000, 8, 32),
                Arguments.of(2000, 9, 31), Arguments.of(2000, 10, 31), Arguments.of(2000, 11, 32),
                Arguments.of(2000, 12, 31), Arguments.of(2004, 12, 38)
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidDateComponents")
        void of_withInvalidDateComponents_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dayOfMonth));
        }

        static Stream<Integer> provideNonLeapYearsForInvalidLeapDay() {
            return Stream.of(1, 100, 200, 2000);
        }

        @ParameterizedTest
        @MethodSource("provideNonLeapYearsForInvalidLeapDay")
        void of_withInvalidLeapDayInNonLeapYear_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    @Nested
    class DatePropertyTests {
        static Stream<Arguments> provideDateAndExpectedMonthLength() {
            return Stream.of(
                Arguments.of(2000, 1, 28, 30), Arguments.of(2000, 2, 28, 31),
                Arguments.of(2000, 3, 28, 30), Arguments.of(2000, 4, 28, 30),
                Arguments.of(2000, 5, 28, 31), Arguments.of(2000, 6, 28, 30),
                Arguments.of(2000, 7, 28, 30), Arguments.of(2000, 8, 28, 31),
                Arguments.of(2000, 9, 28, 30), Arguments.of(2000, 10, 28, 30),
                Arguments.of(2000, 11, 28, 31), Arguments.of(2000, 12, 28, 30),
                Arguments.of(2004, 12, 20, 37)
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndExpectedMonthLength")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, day).lengthOfMonth());
            assertEquals(expectedLength, Symmetry010Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> provideDateAndFieldWithExpectedRange() {
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)),
                Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)),
                Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)),
                Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)),
                Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
                Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
                Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53))
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndFieldWithExpectedRange")
        void range_forField_shouldReturnCorrectRange(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, Symmetry010Date.of(year, month, dom).range(field));
        }

        static Stream<Arguments> provideDateFieldAndExpectedValue() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 2L),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 147L),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 24172L),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),
                Arguments.of(2012, 9, 26, DAY_OF_YEAR, 269L),
                Arguments.of(2012, 9, 26, ALIGNED_WEEK_OF_YEAR, 39L),
                Arguments.of(2015, 12, 37, DAY_OF_YEAR, 371L),
                Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_MONTH, 6L),
                Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53L),
                Arguments.of(2015, 12, 37, PROLEPTIC_MONTH, 24191L)
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateFieldAndExpectedValue")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry010Date.of(year, month, dom).getLong(field));
        }

        static Stream<Arguments> provideDateAndExpectedString() {
            return Stream.of(
                Arguments.of(Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"),
                Arguments.of(Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"),
                Arguments.of(Symmetry010Date.of(2000, 8, 31), "Sym010 CE 2000/08/31"),
                Arguments.of(Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37")
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndExpectedString")
        void toString_shouldReturnCorrectRepresentation(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }

    @Nested
    class DateArithmeticTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void plusDays_shouldBehaveIdenticallyToIso(Symmetry010Date symDate, LocalDate isoDate) {
            assertAll("plus days",
                () -> assertEquals(isoDate, LocalDate.from(symDate.plus(0, DAYS))),
                () -> assertEquals(isoDate.plusDays(1), LocalDate.from(symDate.plus(1, DAYS))),
                () -> assertEquals(isoDate.plusDays(35), LocalDate.from(symDate.plus(35, DAYS))),
                () -> assertEquals(isoDate.plusDays(-1), LocalDate.from(symDate.plus(-1, DAYS))),
                () -> assertEquals(isoDate.plusDays(-60), LocalDate.from(symDate.plus(-60, DAYS)))
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void minusDays_shouldBehaveIdenticallyToIso(Symmetry010Date symDate, LocalDate isoDate) {
            assertAll("minus days",
                () -> assertEquals(isoDate, LocalDate.from(symDate.minus(0, DAYS))),
                () -> assertEquals(isoDate.minusDays(1), LocalDate.from(symDate.minus(1, DAYS))),
                () -> assertEquals(isoDate.minusDays(35), LocalDate.from(symDate.minus(35, DAYS))),
                () -> assertEquals(isoDate.minusDays(-1), LocalDate.from(symDate.minus(-1, DAYS))),
                () -> assertEquals(isoDate.minusDays(-60), LocalDate.from(symDate.minus(-60, DAYS)))
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void until_inDays_shouldBehaveIdenticallyToIso(Symmetry010Date symDate, LocalDate isoDate) {
            assertAll("until in days",
                () -> assertEquals(0, symDate.until(isoDate.plusDays(0), DAYS)),
                () -> assertEquals(1, symDate.until(isoDate.plusDays(1), DAYS)),
                () -> assertEquals(35, symDate.until(isoDate.plusDays(35), DAYS)),
                () -> assertEquals(-40, symDate.until(isoDate.minusDays(40), DAYS))
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void until_withEquivalentDate_shouldReturnZeroPeriod(Symmetry010Date symDate, LocalDate isoDate) {
            ChronoPeriod zeroSymmetryPeriod = Symmetry010Chronology.INSTANCE.period(0, 0, 0);
            assertAll("until with equivalent date",
                () -> assertEquals(zeroSymmetryPeriod, symDate.until(symDate), "until(self) should be zero"),
                () -> assertEquals(zeroSymmetryPeriod, symDate.until(isoDate), "until(equivalent iso) should be zero"),
                () -> assertEquals(Period.ZERO, isoDate.until(symDate), "iso.until(equivalent symmetry) should be zero")
            );
        }

        static Stream<Arguments> providePlusData() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                Arguments.of(2013, 6, 21, 6 * 52 + 1, WEEKS, 2019, 6, 21)
            );
        }

        @ParameterizedTest
        @MethodSource("providePlusData")
        void plus_withTemporalUnit_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry010Date start = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("providePlusData")
        void minus_withTemporalUnit_shouldReturnCorrectDate(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            Symmetry010Date start = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Stream<Arguments> provideUntilData() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0L),
                Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 9L),
                Arguments.of(2014, 5, 26, 2014, 6, 5, WEEKS, 1L),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, ERAS, 0L)
            );
        }

        @ParameterizedTest
        @MethodSource("provideUntilData")
        void until_withTemporalUnit_shouldReturnCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> provideUntilPeriodData() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 9),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                Arguments.of(2014, 5, 26, 2024, 5, 25, 9, 11, 29)
            );
        }

        @ParameterizedTest
        @MethodSource("provideUntilPeriodData")
        void until_asPeriod_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry010Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    class WithAdjusterTests {
        static Stream<Arguments> provideWithAdjustmentData() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2015, 12, 37, YEAR, 2004, 2004, 12, 37)
            );
        }

        @ParameterizedTest
        @MethodSource("provideWithAdjustmentData")
        void with_usingField_shouldReturnAdjustedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            Symmetry010Date start = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Arguments> provideWithInvalidAdjustmentData() {
            return Stream.of(
                Arguments.of(2013, 1, 1, DAY_OF_MONTH, 31),
                Arguments.of(2013, 6, 1, DAY_OF_MONTH, 31),
                Arguments.of(2015, 12, 1, DAY_OF_MONTH, 38),
                Arguments.of(2013, 1, 1, DAY_OF_YEAR, 365),
                Arguments.of(2015, 1, 1, DAY_OF_YEAR, 372),
                Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14),
                Arguments.of(2013, 1, 1, YEAR, 1_000_001)
            );
        }

        @ParameterizedTest
        @MethodSource("provideWithInvalidAdjustmentData")
        void with_usingInvalidFieldValue_shouldThrowException(int y, int m, int d, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(y, m, d).with(field, value));
        }

        static Stream<Arguments> provideLastDayOfMonthData() {
            return Stream.of(
                Arguments.of(2012, 1, 23, 2012, 1, 30),
                Arguments.of(2012, 2, 23, 2012, 2, 31),
                Arguments.of(2009, 12, 23, 2009, 12, 37)
            );
        }

        @ParameterizedTest
        @MethodSource("provideLastDayOfMonthData")
        void with_lastDayOfMonthAdjuster_shouldReturnCorrectDate(int y, int m, int d, int ey, int em, int ed) {
            Symmetry010Date start = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, start.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    class ChronologyApiTests {
        static Stream<Era> provideUnsupportedEras() {
            return Stream.of(
                AccountingEra.BCE, CopticEra.AM, DiscordianEra.YOLD, EthiopicEra.INCARNATION,
                HijrahEra.AH, InternationalFixedEra.CE, JapaneseEra.HEISEI, JulianEra.AD,
                MinguoEra.ROC, PaxEra.CE, ThaiBuddhistEra.BE
            );
        }

        @ParameterizedTest
        @MethodSource("provideUnsupportedEras")
        void prolepticYear_withUnsupportedEra_shouldThrowException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }

        @Test
        void eraOf_shouldReturnCorrectEra() {
            assertEquals(IsoEra.BCE, Symmetry010Chronology.INSTANCE.eraOf(0));
            assertEquals(IsoEra.CE, Symmetry010Chronology.INSTANCE.eraOf(1));
        }
    }

    @Nested
    class EqualsAndHashCodeTest {
        @Test
        void equalsAndHashCode_shouldBehaveCorrectly() {
            Symmetry010Date date1 = Symmetry010Date.of(2014, 5, 26);
            Symmetry010Date date2 = Symmetry010Date.of(2014, 5, 26);
            Symmetry010Date date3 = Symmetry010Date.of(2014, 5, 27);
            Symmetry010Date date4 = Symmetry010Date.of(2014, 6, 26);
            Symmetry010Date date5 = Symmetry010Date.of(2015, 5, 26);

            new EqualsTester()
                .addEqualityGroup(date1, date2)
                .addEqualityGroup(date3)
                .addEqualityGroup(date4)
                .addEqualityGroup(date5)
                .testEquals();
        }
    }
}