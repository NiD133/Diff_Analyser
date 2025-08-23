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

@DisplayName("Tests for Symmetry454Chronology and Symmetry454Date")
class Symmetry454ChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleSymmetryAndIsoDates() {
        return Stream.of(
            Arguments.of(Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry454Date.of(272, 2, 27), LocalDate.of(272, 2, 24)),
            Arguments.of(Symmetry454Date.of(742, 3, 25), LocalDate.of(742, 4, 2)),
            Arguments.of(Symmetry454Date.of(742, 4, 2), LocalDate.of(742, 4, 7)),
            Arguments.of(Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
            Arguments.of(Symmetry454Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)),
            Arguments.of(Symmetry454Date.of(1304, 7, 20), LocalDate.of(1304, 7, 19)),
            Arguments.of(Symmetry454Date.of(1433, 11, 14), LocalDate.of(1433, 11, 10)),
            Arguments.of(Symmetry454Date.of(1433, 11, 10), LocalDate.of(1433, 11, 6)),
            Arguments.of(Symmetry454Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)),
            Arguments.of(Symmetry454Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)),
            Arguments.of(Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
            Arguments.of(Symmetry454Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)),
            Arguments.of(Symmetry454Date.of(1564, 2, 20), LocalDate.of(1564, 2, 15)),
            Arguments.of(Symmetry454Date.of(1564, 2, 15), LocalDate.of(1564, 2, 10)),
            Arguments.of(Symmetry454Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)),
            Arguments.of(Symmetry454Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)),
            Arguments.of(Symmetry454Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)),
            Arguments.of(Symmetry454Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)),
            Arguments.of(Symmetry454Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)),
            Arguments.of(Symmetry454Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)),
            Arguments.of(Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
            Arguments.of(Symmetry454Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)),
            Arguments.of(Symmetry454Date.of(1879, 3, 12), LocalDate.of(1879, 3, 14)),
            Arguments.of(Symmetry454Date.of(1879, 3, 14), LocalDate.of(1879, 3, 16)),
            Arguments.of(Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)),
            Arguments.of(Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
            Arguments.of(Symmetry454Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)),
            Arguments.of(Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1)),
            Arguments.of(Symmetry454Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3))
        );
    }

    @Nested
    @DisplayName("Factory methods and validation")
    class FactoryAndValidationTests {

        static Stream<Arguments> invalidDateComponents() {
            return Stream.of(
                Arguments.of(-1, 13, 28), Arguments.of(-1, 13, 29), Arguments.of(2000, -2, 1),
                Arguments.of(2000, 13, 1), Arguments.of(2000, 15, 1), Arguments.of(2000, 1, -1),
                Arguments.of(2000, 1, 0), Arguments.of(2000, 0, 1), Arguments.of(2000, -1, 0),
                Arguments.of(2000, -1, 1), Arguments.of(2000, 1, 29), Arguments.of(2000, 2, 36),
                Arguments.of(2000, 3, 29), Arguments.of(2000, 4, 29), Arguments.of(2000, 5, 36),
                Arguments.of(2000, 6, 29), Arguments.of(2000, 7, 29), Arguments.of(2000, 8, 36),
                Arguments.of(2000, 9, 29), Arguments.of(2000, 10, 29), Arguments.of(2000, 11, 36),
                Arguments.of(2000, 12, 29), Arguments.of(2004, 12, 36)
            );
        }

        @ParameterizedTest(name = "year={0}, month={1}, day={2}")
        @MethodSource("invalidDateComponents")
        @DisplayName("of() with invalid date components should throw exception")
        void of_withInvalidDate_shouldThrowException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom));
        }

        static Stream<Arguments> invalidLeapYearDates() {
            return Stream.of(Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(2000));
        }

        @ParameterizedTest(name = "year={0}")
        @MethodSource("invalidLeapYearDates")
        @DisplayName("of() with invalid leap day for non-leap year should throw exception")
        void of_withInvalidLeapDay_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    @DisplayName("Conversion to/from other calendar systems")
    class ConversionTests {

        @ParameterizedTest(name = "[{index}] {0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("Convert Symmetry454Date to LocalDate")
        void toLocalDate_shouldReturnCorrectDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym454));
        }

        @ParameterizedTest(name = "[{index}] {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("Convert LocalDate to Symmetry454Date")
        void fromLocalDate_shouldReturnCorrectDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Date.from(iso));
        }

        @ParameterizedTest(name = "[{index}] {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("Create Symmetry454Date from TemporalAccessor (LocalDate)")
        void chronologyDate_fromTemporal_shouldReturnCorrectDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Chronology.INSTANCE.date(iso));
        }
    }

    @Nested
    @DisplayName("Epoch Day operations")
    class EpochDayTests {

        @ParameterizedTest(name = "[{index}] epochDay={1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("Create Symmetry454Date from epoch day")
        void dateEpochDay_shouldCreateCorrectDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest(name = "[{index}] {0} -> epochDay={1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("Convert Symmetry454Date to epoch day")
        void toEpochDay_shouldReturnCorrectValue(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym454.toEpochDay());
        }
    }

    @Nested
    @DisplayName("Date properties")
    class DatePropertiesTests {

        static Stream<Arguments> monthLengths() {
            return Stream.of(
                Arguments.of(2000, 1, 28, 28), Arguments.of(2000, 2, 28, 35),
                Arguments.of(2000, 3, 28, 28), Arguments.of(2000, 4, 28, 28),
                Arguments.of(2000, 5, 28, 35), Arguments.of(2000, 6, 28, 28),
                Arguments.of(2000, 7, 28, 28), Arguments.of(2000, 8, 28, 35),
                Arguments.of(2000, 9, 28, 28), Arguments.of(2000, 10, 28, 28),
                Arguments.of(2000, 11, 28, 35), Arguments.of(2000, 12, 28, 28),
                Arguments.of(2004, 12, 20, 35)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} should have month length {3}")
        @MethodSource("monthLengths")
        @DisplayName("lengthOfMonth() should return correct number of days")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, day).lengthOfMonth());
        }
    }

    @Nested
    @DisplayName("Era handling")
    class EraTests {

        static Stream<Era> nonSymmetry454Eras() {
            return Stream.of(
                AccountingEra.BCE, AccountingEra.CE, CopticEra.BEFORE_AM, CopticEra.AM,
                DiscordianEra.YOLD, EthiopicEra.BEFORE_INCARNATION, EthiopicEra.INCARNATION,
                HijrahEra.AH, InternationalFixedEra.CE, JapaneseEra.MEIJI, JapaneseEra.TAISHO,
                JapaneseEra.SHOWA, JapaneseEra.HEISEI, JulianEra.BC, JulianEra.AD,
                MinguoEra.BEFORE_ROC, MinguoEra.ROC, PaxEra.BCE, PaxEra.CE,
                ThaiBuddhistEra.BEFORE_BE, ThaiBuddhistEra.BE
            );
        }

        @ParameterizedTest
        @MethodSource("nonSymmetry454Eras")
        @DisplayName("prolepticYear() with unsupported era should throw exception")
        void prolepticYear_withUnsupportedEra_shouldThrowException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("Field access")
    class FieldAccessTests {

        static Stream<Arguments> fieldRanges() {
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35)),
                Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 35)),
                Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)),
                Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)),
                Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
                Arguments.of(2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53))
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2}, field {3}, range {4}")
        @MethodSource("fieldRanges")
        @DisplayName("range() for a given field should return the correct value range")
        void range_forField_shouldReturnCorrectRange(int year, int month, int dom, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(year, month, dom).range(field));
        }

        static Stream<Arguments> dateFieldsAndValues() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 28 + 35 + 28 + 28 + 26),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4 + 5 + 4 + 4 + 4),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                Arguments.of(2015, 12, 35, DAY_OF_WEEK, 7),
                Arguments.of(2015, 12, 35, DAY_OF_MONTH, 35),
                Arguments.of(2015, 12, 35, DAY_OF_YEAR, 4 * (4 + 5 + 4) * 7 + 7),
                Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_MONTH, 5),
                Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53),
                Arguments.of(2015, 12, 35, MONTH_OF_YEAR, 12),
                Arguments.of(2015, 12, 35, PROLEPTIC_MONTH, 2016 * 12 - 1)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2}, field {3}, value {4}")
        @MethodSource("dateFieldsAndValues")
        @DisplayName("getLong() for a given field should return the correct value")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field));
        }

        static Stream<Arguments> withFieldAdjustments() {
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
                Arguments.of(2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29),
                Arguments.of(2015, 12, 29, YEAR, 2014, 2014, 12, 28),
                Arguments.of(2015, 3, 28, DAY_OF_YEAR, 371, 2015, 12, 35),
                Arguments.of(2012, 3, 28, DAY_OF_YEAR, 364, 2012, 12, 28)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("withFieldAdjustments")
        @DisplayName("with() should return a correctly adjusted date")
        void with_fieldAndValue_shouldReturnAdjustedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            var baseDate = Symmetry454Date.of(y, m, d);
            var expectedDate = Symmetry454Date.of(ey, em, ed);
            assertEquals(expectedDate, baseDate.with(field, value));
        }

        static Stream<Arguments> invalidWithFieldAdjustments() {
            return Stream.of(
                Arguments.of(2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, 8),
                Arguments.of(2013, 1, 1, ALIGNED_WEEK_OF_MONTH, 5),
                Arguments.of(2013, 1, 1, ALIGNED_WEEK_OF_YEAR, 53),
                Arguments.of(2013, 1, 1, DAY_OF_MONTH, 29),
                Arguments.of(2013, 1, 1, DAY_OF_YEAR, 365),
                Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14),
                Arguments.of(2013, 1, 1, YEAR, 1_000_001)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4})")
        @MethodSource("invalidWithFieldAdjustments")
        @DisplayName("with() invalid field value should throw exception")
        void with_invalidFieldValue_shouldThrowException(int y, int m, int d, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(y, m, d).with(field, value));
        }
    }

    @Nested
    @DisplayName("Date arithmetic")
    class ArithmeticTests {

        static Stream<Arguments> datePlusAmountCases() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 5, 34),
                Arguments.of(2014, 5, 26, -3, DAYS, 2014, 5, 23),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 12),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                Arguments.of(2014, 12, 26, 3, WEEKS, 2015, 1, 19),
                Arguments.of(2013, 6, 21, 6 * 52 + 1, WEEKS, 2019, 6, 21)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("datePlusAmountCases")
        @DisplayName("plus() should return a correctly adjusted date")
        void plus_amountAndUnit_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            var baseDate = Symmetry454Date.of(y, m, d);
            var expectedDate = Symmetry454Date.of(ey, em, ed);
            assertEquals(expectedDate, baseDate.plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus({3}, {4}) -> {0}-{1}-{2}")
        @MethodSource("datePlusAmountCases")
        @DisplayName("minus() should return a correctly adjusted date")
        void minus_amountAndUnit_shouldReturnCorrectDate(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            var baseDate = Symmetry454Date.of(y, m, d);
            var expectedDate = Symmetry454Date.of(ey, em, ed);
            assertEquals(expectedDate, baseDate.minus(amount, unit));
        }

        static Stream<Arguments> untilInUnitsCases() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 13),
                Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6),
                Arguments.of(2014, 5, 26, 2014, 6, 5, WEEKS, 1),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1),
                Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1),
                Arguments.of(2014, 5, 26, 3014, 5, 26, ERAS, 0)
            );
        }

        @ParameterizedTest(name = "from {0}-{1}-{2} until {3}-{4}-{5} in {6} is {7}")
        @MethodSource("untilInUnitsCases")
        @DisplayName("until() with a unit should return the correct duration")
        void until_endDateAndUnit_shouldReturnCorrectDuration(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            var start = Symmetry454Date.of(y1, m1, d1);
            var end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilAsPeriodCases() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 13),
                Arguments.of(2014, 5, 26, 2014, 5, 20, 0, 0, -6),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                Arguments.of(2014, 5, 26, 2024, 5, 25, 9, 11, 27)
            );
        }

        @ParameterizedTest(name = "from {0}-{1}-{2} until {3}-{4}-{5} is P{6}Y{7}M{8}D")
        @MethodSource("untilAsPeriodCases")
        @DisplayName("until() should return the correct period")
        void until_endDate_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            var start = Symmetry454Date.of(y1, m1, d1);
            var end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expectedPeriod = Symmetry454Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expectedPeriod, start.until(end));
        }

        @ParameterizedTest(name = "[{index}] {0} until self should be zero")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void until_self_shouldReturnZeroPeriod(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454.until(sym454));
        }

        @ParameterizedTest(name = "[{index}] {0} until equivalent ISO date should be zero")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void until_equivalentLocalDate_shouldReturnZeroPeriod(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454.until(iso));
        }

        @ParameterizedTest(name = "[{index}] ISO {1} until equivalent Sym454 date should be zero")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetryAndIsoDates")
        void isoDate_until_sym454Date_shouldReturnZeroPeriod(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(sym454));
        }
    }

    @Nested
    @DisplayName("TemporalAdjuster support")
    class AdjusterTests {

        static Stream<Arguments> lastDayOfMonthCases() {
            return Stream.of(
                Arguments.of(2012, 1, 23, 2012, 1, 28),
                Arguments.of(2012, 2, 23, 2012, 2, 35),
                Arguments.of(2012, 3, 23, 2012, 3, 28),
                Arguments.of(2009, 12, 23, 2009, 12, 35)
            );
        }

        @ParameterizedTest(name = "last day of month for {0}-{1}-{2} is {3}-{4}-{5}")
        @MethodSource("lastDayOfMonthCases")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) should return correct date")
        void with_lastDayOfMonthAdjuster_shouldReturnLastDayOfMonth(int y, int m, int d, int ey, int em, int ed) {
            var base = Symmetry454Date.of(y, m, d);
            var expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Object method overrides")
    class ObjectMethodTests {

        static Stream<Arguments> toStringCases() {
            return Stream.of(
                Arguments.of(Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"),
                Arguments.of(Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"),
                Arguments.of(Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35"),
                Arguments.of(Symmetry454Date.of(1970, 12, 35), "Sym454 CE 1970/12/35")
            );
        }

        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("toStringCases")
        @DisplayName("toString() should return correctly formatted string")
        void toString_shouldReturnFormattedDate(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }

    @Nested
    @DisplayName("Unsupported operations")
    class UnsupportedOperationsTests {

        @Test
        @DisplayName("plus() with an unsupported unit should throw exception")
        void plus_unsupportedUnit_shouldThrowException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry454Date.of(2012, 6, 28).plus(1, MINUTES));
        }
    }
}