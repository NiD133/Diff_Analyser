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
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Symmetry454Date Functionality Tests")
public class Symmetry454DateTest {

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
    @DisplayName("Conversion and Factory Method Tests")
    class ConversionAndFactoryTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetryAndIsoDates")
        @DisplayName("should convert from Symmetry454Date to LocalDate")
        void shouldConvertFromSymmetry454DateToLocalDate(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(sym454Date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetryAndIsoDates")
        @DisplayName("should create Symmetry454Date from LocalDate")
        void shouldCreateFromLocalDate(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(sym454Date, Symmetry454Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetryAndIsoDates")
        @DisplayName("should create Symmetry454Date from another TemporalAccessor")
        void shouldCreateFromTemporal(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(sym454Date, Symmetry454Chronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetryAndIsoDates")
        @DisplayName("should create Symmetry454Date from an epoch day")
        void shouldCreateFromEpochDay(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(sym454Date, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetryAndIsoDates")
        @DisplayName("should convert Symmetry454Date to epoch day")
        void shouldConvertToEpochDay(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), sym454Date.toEpochDay());
        }

        static Stream<Arguments> nonSymmetryEras() {
            return Stream.of(
                    Arguments.of(AccountingEra.BCE), Arguments.of(AccountingEra.CE),
                    Arguments.of(CopticEra.BEFORE_AM), Arguments.of(CopticEra.AM),
                    Arguments.of(DiscordianEra.YOLD),
                    Arguments.of(EthiopicEra.BEFORE_INCARNATION), Arguments.of(EthiopicEra.INCARNATION),
                    Arguments.of(HijrahEra.AH),
                    Arguments.of(InternationalFixedEra.CE),
                    Arguments.of(JapaneseEra.MEIJI), Arguments.of(JapaneseEra.TAISHO),
                    Arguments.of(JapaneseEra.SHOWA), Arguments.of(JapaneseEra.HEISEI),
                    Arguments.of(JulianEra.BC), Arguments.of(JulianEra.AD),
                    Arguments.of(MinguoEra.BEFORE_ROC), Arguments.of(MinguoEra.ROC),
                    Arguments.of(PaxEra.BCE), Arguments.of(PaxEra.CE),
                    Arguments.of(ThaiBuddhistEra.BEFORE_BE), Arguments.of(ThaiBuddhistEra.BE)
            );
        }

        @ParameterizedTest
        @MethodSource("nonSymmetryEras")
        @DisplayName("should throw exception when calculating proleptic year with incompatible era")
        void shouldThrowExceptionForProlepticYearWithBadEra(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("Invalid Date Creation Tests")
    class InvalidDateCreationTests {

        static Stream<Arguments> invalidDateComponents() {
            return Stream.of(
                    Arguments.of(-1, 13, 28), Arguments.of(-1, 13, 29),
                    Arguments.of(2000, -2, 1), Arguments.of(2000, 13, 1),
                    Arguments.of(2000, 15, 1), Arguments.of(2000, 1, -1),
                    Arguments.of(2000, 1, 0), Arguments.of(2000, 0, 1),
                    Arguments.of(2000, -1, 0), Arguments.of(2000, -1, 1),
                    Arguments.of(2000, 1, 29), Arguments.of(2000, 2, 36),
                    Arguments.of(2000, 3, 29), Arguments.of(2000, 4, 29),
                    Arguments.of(2000, 5, 36), Arguments.of(2000, 6, 29),
                    Arguments.of(2000, 7, 29), Arguments.of(2000, 8, 36),
                    Arguments.of(2000, 9, 29), Arguments.of(2000, 10, 29),
                    Arguments.of(2000, 11, 36), Arguments.of(2000, 12, 29),
                    Arguments.of(2004, 12, 36)
            );
        }

        @ParameterizedTest
        @MethodSource("invalidDateComponents")
        @DisplayName("should throw exception for invalid date components")
        void shouldThrowExceptionForInvalidDates(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dayOfMonth));
        }

        static Stream<Arguments> invalidLeapDayYears() {
            return Stream.of(
                    Arguments.of(1), Arguments.of(100),
                    Arguments.of(200), Arguments.of(2000)
            );
        }

        @ParameterizedTest
        @MethodSource("invalidLeapDayYears")
        @DisplayName("should throw exception for leap day in a non-leap year")
        void shouldThrowExceptionForInvalidLeapDay(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    @DisplayName("Date Property Tests")
    class DatePropertyTests {

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

        @ParameterizedTest
        @MethodSource("monthLengths")
        @DisplayName("should return correct length of month")
        void shouldReturnCorrectLengthOfMonth(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, day).lengthOfMonth());
            assertEquals(expectedLength, Symmetry454Date.of(year, month, 1).lengthOfMonth());
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

        @ParameterizedTest
        @MethodSource("fieldRanges")
        @DisplayName("should return correct range for a given temporal field")
        void shouldReturnCorrectRangeForField(int year, int month, int day, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(year, month, day).range(field));
        }

        static Stream<Arguments> dateFields() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5),
                    Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                    Arguments.of(2014, 5, 26, DAY_OF_YEAR, 145), // 28+35+28+28+26
                    Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5),
                    Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
                    Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5),
                    Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21), // 4+5+4+4+4
                    Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                    Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
                    Arguments.of(2014, 5, 26, YEAR, 2014),
                    Arguments.of(2014, 5, 26, ERA, 1),
                    Arguments.of(1, 5, 8, ERA, 1),
                    Arguments.of(2012, 9, 26, DAY_OF_WEEK, 5),
                    Arguments.of(2012, 9, 26, DAY_OF_YEAR, 271), // 3*(4+5+4)*7 - 2
                    Arguments.of(2012, 9, 26, ALIGNED_WEEK_OF_YEAR, 39), // 3*(4+5+4)
                    Arguments.of(2015, 12, 35, DAY_OF_WEEK, 7),
                    Arguments.of(2015, 12, 35, DAY_OF_MONTH, 35),
                    Arguments.of(2015, 12, 35, DAY_OF_YEAR, 371), // 4*(4+5+4)*7 + 7
                    Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_MONTH, 5),
                    Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53),
                    Arguments.of(2015, 12, 35, MONTH_OF_YEAR, 12),
                    Arguments.of(2015, 12, 35, PROLEPTIC_MONTH, 2016 * 12 - 1)
            );
        }

        @ParameterizedTest
        @MethodSource("dateFields")
        @DisplayName("should return correct value for getLong() with a given temporal field")
        void shouldReturnCorrectValueForGetLong(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, day).getLong(field));
        }
    }

    @Nested
    @DisplayName("Modification Tests")
    class ModificationTests {

        static Stream<Arguments> withFieldAdjustments() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22),
                    Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                    Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28),
                    Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3, 2014, 5, 24),
                    Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
                    Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2, 2014, 5, 23),
                    Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19),
                    Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                    Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26),
                    Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                    Arguments.of(2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26),
                    Arguments.of(2014, 5, 26, ERA, 1, 2014, 5, 26),
                    Arguments.of(2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29), // Day of month adjusted
                    Arguments.of(2015, 12, 29, YEAR, 2014, 2014, 12, 28) // Day of month adjusted
            );
        }

        @ParameterizedTest
        @MethodSource("withFieldAdjustments")
        @DisplayName("should adjust date correctly using with()")
        void shouldAdjustDateWith(int year, int month, int day, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDay) {
            Symmetry454Date start = Symmetry454Date.of(year, month, day);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Arguments> invalidWithFieldAdjustments() {
            return Stream.of(
                    Arguments.of(2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, 8),
                    Arguments.of(2013, 1, 1, ALIGNED_WEEK_OF_MONTH, 5),
                    Arguments.of(2013, 2, 1, ALIGNED_WEEK_OF_MONTH, 6),
                    Arguments.of(2013, 1, 1, ALIGNED_WEEK_OF_YEAR, 53),
                    Arguments.of(2015, 1, 1, ALIGNED_WEEK_OF_YEAR, 54),
                    Arguments.of(2013, 1, 1, DAY_OF_WEEK, 8),
                    Arguments.of(2013, 1, 1, DAY_OF_MONTH, 29),
                    Arguments.of(2015, 12, 1, DAY_OF_MONTH, 36),
                    Arguments.of(2013, 1, 1, DAY_OF_YEAR, 365),
                    Arguments.of(2015, 1, 1, DAY_OF_YEAR, 372),
                    Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14),
                    Arguments.of(2013, 1, 1, YEAR, 1_000_001)
            );
        }

        @ParameterizedTest
        @MethodSource("invalidWithFieldAdjustments")
        @DisplayName("should throw exception when adjusting with invalid value")
        void shouldThrowExceptionForInvalidWith(int year, int month, int day, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, day).with(field, value));
        }

        static Stream<Arguments> lastDayOfMonthAdjustments() {
            return Stream.of(
                    Arguments.of(2012, 1, 23, 2012, 1, 28),
                    Arguments.of(2012, 2, 23, 2012, 2, 35),
                    Arguments.of(2012, 12, 23, 2012, 12, 28),
                    Arguments.of(2009, 12, 23, 2009, 12, 35) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthAdjustments")
        @DisplayName("should adjust to the last day of the month")
        void shouldAdjustToLastDayOfMonth(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
            Symmetry454Date base = Symmetry454Date.of(year, month, day);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Arithmetic Tests")
    class ArithmeticTests {

        static Stream<Arguments> plusTemporalUnitScenarios() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26),
                    Arguments.of(2014, 5, 26, 8, DAYS, 2014, 5, 34),
                    Arguments.of(2014, 5, 26, -3, DAYS, 2014, 5, 23),
                    Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 12),
                    Arguments.of(2014, 5, 26, -5, WEEKS, 2014, 4, 19),
                    Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                    Arguments.of(2014, 5, 26, -5, MONTHS, 2013, 12, 26),
                    Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                    Arguments.of(2014, 5, 26, -5, YEARS, 2009, 5, 26),
                    Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                    Arguments.of(2014, 5, 26, -5, DECADES, 1964, 5, 26),
                    Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                    Arguments.of(2014, 5, 26, -5, CENTURIES, 1514, 5, 26),
                    Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                    Arguments.of(2014, 5, 26, -1, MILLENNIA, 1014, 5, 26),
                    Arguments.of(2014, 12, 26, 3, WEEKS, 2015, 1, 19),
                    Arguments.of(2013, 6, 21, 6 * 52 + 1, WEEKS, 2019, 6, 21)
            );
        }

        @ParameterizedTest
        @MethodSource("plusTemporalUnitScenarios")
        @DisplayName("should add amounts correctly using plus()")
        void shouldAdd(int year, int month, int day, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDay) {
            Symmetry454Date start = Symmetry454Date.of(year, month, day);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusTemporalUnitScenarios")
        @DisplayName("should subtract amounts correctly using minus()")
        void shouldSubtract(int expectedYear, int expectedMonth, int expectedDay, long amount, TemporalUnit unit, int year, int month, int day) {
            Symmetry454Date start = Symmetry454Date.of(year, month, day);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Stream<Arguments> plusTemporalUnitLeapWeekScenarios() {
            return Stream.of(
                    Arguments.of(2015, 12, 28, 8, DAYS, 2016, 1, 1),
                    Arguments.of(2015, 12, 28, -3, DAYS, 2015, 12, 25),
                    Arguments.of(2015, 12, 28, 3, WEEKS, 2016, 1, 14),
                    Arguments.of(2015, 12, 28, -5, WEEKS, 2015, 11, 28),
                    Arguments.of(2015, 12, 28, 52, WEEKS, 2016, 12, 21),
                    Arguments.of(2015, 12, 28, 3, MONTHS, 2016, 3, 28),
                    Arguments.of(2015, 12, 28, 12, MONTHS, 2016, 12, 28),
                    Arguments.of(2015, 12, 28, 3, YEARS, 2018, 12, 28)
            );
        }

        @ParameterizedTest
        @MethodSource("plusTemporalUnitLeapWeekScenarios")
        @DisplayName("should add amounts correctly across a leap week")
        void shouldAddAcrossLeapWeek(int year, int month, int day, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDay) {
            Symmetry454Date start = Symmetry454Date.of(year, month, day);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusTemporalUnitLeapWeekScenarios")
        @DisplayName("should subtract amounts correctly across a leap week")
        void shouldSubtractAcrossLeapWeek(int expectedYear, int expectedMonth, int expectedDay, long amount, TemporalUnit unit, int year, int month, int day) {
            Symmetry454Date start = Symmetry454Date.of(year, month, day);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.minus(amount, unit));
        }

        @Test
        @DisplayName("should throw exception when adding an unsupported ISO Period")
        void shouldThrowExceptionWhenAddingIsoPeriod() {
            Symmetry454Date start = Symmetry454Date.of(2014, 5, 26);
            assertThrows(DateTimeException.class, () -> start.plus(Period.ofMonths(2)));
        }
    }

    @Nested
    @DisplayName("Period and Duration Tests")
    class PeriodAndDurationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetryAndIsoDates")
        @DisplayName("should calculate zero period between a date and itself")
        void shouldCalculateZeroPeriod(Symmetry454Date sym454Date, LocalDate isoDate) {
            ChronoPeriod zeroPeriod = Symmetry454Chronology.INSTANCE.period(0, 0, 0);
            assertEquals(zeroPeriod, sym454Date.until(sym454Date));
            assertEquals(zeroPeriod, sym454Date.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(sym454Date));
        }

        static Stream<Arguments> untilTemporalUnitScenarios() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0),
                    Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 13),
                    Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6),
                    Arguments.of(2014, 5, 26, 2014, 6, 4, WEEKS, 0),
                    Arguments.of(2014, 5, 26, 2014, 6, 5, WEEKS, 1),
                    Arguments.of(2014, 5, 26, 2014, 6, 25, MONTHS, 0),
                    Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1),
                    Arguments.of(2014, 5, 26, 2015, 5, 25, YEARS, 0),
                    Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1),
                    Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1),
                    Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1),
                    Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1),
                    Arguments.of(2014, 5, 26, 3014, 5, 26, ERAS, 0)
            );
        }

        @ParameterizedTest
        @MethodSource("untilTemporalUnitScenarios")
        @DisplayName("should calculate the duration between two dates in a specific unit")
        void shouldCalculateUntilInUnit(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodScenarios() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                    Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 13),
                    Arguments.of(2014, 5, 26, 2014, 5, 20, 0, 0, -6),
                    Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                    Arguments.of(2014, 5, 26, 2015, 5, 25, 0, 11, 27),
                    Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                    Arguments.of(2014, 5, 26, 2024, 5, 25, 9, 11, 27)
            );
        }

        @ParameterizedTest
        @MethodSource("untilPeriodScenarios")
        @DisplayName("should calculate the period between two dates")
        void shouldCalculateUntilPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Object Method Tests")
    class ObjectMethodTests {

        static Stream<Arguments> toStringScenarios() {
            return Stream.of(
                    Arguments.of(Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"),
                    Arguments.of(Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"),
                    Arguments.of(Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35"),
                    Arguments.of(1970, 12, 35, "Sym454 CE 1970/12/35")
            );
        }

        @ParameterizedTest
        @MethodSource("toStringScenarios")
        @DisplayName("should return the correct string representation")
        void shouldReturnCorrectToString(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}