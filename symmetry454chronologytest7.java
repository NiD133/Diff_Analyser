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
import java.time.chrono.IsoEra;
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

/**
 * Tests for {@link Symmetry454Date} and its interactions with {@link Symmetry454Chronology}.
 */
@DisplayName("Symmetry454Date and Symmetry454Chronology")
public class Symmetry454DateTest {

    /**
     * Provides sample Symmetry454 dates and their equivalent ISO dates.
     * @return A stream of arguments, each containing a {@link Symmetry454Date} and a {@link LocalDate}.
     */
    static Stream<Arguments> sampleSymmetryAndIsoDates() {
        return Stream.of(
            Arguments.of(Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)), // Constantine the Great (d. 337)
            Arguments.of(Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry454Date.of(272, 2, 27), LocalDate.of(272, 2, 24)),
            Arguments.of(Symmetry454Date.of(742, 3, 25), LocalDate.of(742, 4, 2)), // Charlemagne (d. 814)
            Arguments.of(Symmetry454Date.of(742, 4, 2), LocalDate.of(742, 4, 7)),
            Arguments.of(Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)), // Battle of Hastings
            Arguments.of(Symmetry454Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)), // Petrarch (d. 1374)
            Arguments.of(Symmetry454Date.of(1304, 7, 20), LocalDate.of(1304, 7, 19)),
            Arguments.of(Symmetry454Date.of(1433, 11, 14), LocalDate.of(1433, 11, 10)), // Charles the Bold (d. 1477)
            Arguments.of(Symmetry454Date.of(1433, 11, 10), LocalDate.of(1433, 11, 6)),
            Arguments.of(Symmetry454Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)), // Leonardo da Vinci (d. 1519)
            Arguments.of(Symmetry454Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)),
            Arguments.of(Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)), // Columbus makes landfall
            Arguments.of(Symmetry454Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)),
            Arguments.of(Symmetry454Date.of(1564, 2, 20), LocalDate.of(1564, 2, 15)), // Galileo Galilei (d. 1642)
            Arguments.of(Symmetry454Date.of(1564, 2, 15), LocalDate.of(1564, 2, 10)),
            Arguments.of(Symmetry454Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)), // William Shakespeare baptized
            Arguments.of(Symmetry454Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)),
            Arguments.of(Symmetry454Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)), // Sir Isaac Newton (d. 1727)
            Arguments.of(Symmetry454Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)),
            Arguments.of(Symmetry454Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)), // Leonhard Euler (d. 1783)
            Arguments.of(Symmetry454Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)),
            Arguments.of(Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)), // Storming of the Bastille
            Arguments.of(Symmetry454Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)),
            Arguments.of(Symmetry454Date.of(1879, 3, 12), LocalDate.of(1879, 3, 14)), // Albert Einstein (d. 1955)
            Arguments.of(Symmetry454Date.of(1879, 3, 14), LocalDate.of(1879, 3, 16)),
            Arguments.of(Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)), // Dennis Ritchie (d. 2011)
            Arguments.of(Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)), // Unix time begins
            Arguments.of(Symmetry454Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)),
            Arguments.of(Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1)), // Start of 21st century
            Arguments.of(Symmetry454Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3))
        );
    }

    /**
     * Tests conversions between Symmetry454Date and other date representations like LocalDate and epoch-day.
     */
    @Nested
    @DisplayName("Conversion Tests")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetryAndIsoDates")
        void fromSymmetry454Date_shouldReturnCorrectLocalDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym454));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetryAndIsoDates")
        void fromLocalDate_shouldReturnCorrectSymmetry454Date(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Date.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetryAndIsoDates")
        void toEpochDay_shouldReturnCorrectValue(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym454.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetryAndIsoDates")
        void chronology_dateFromEpochDay_shouldReturnCorrectDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetryAndIsoDates")
        void chronology_dateFromTemporal_shouldReturnCorrectDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Chronology.INSTANCE.date(iso));
        }
    }

    /**
     * Tests the creation of Symmetry454Date instances and validation of date components.
     */
    @Nested
    @DisplayName("Factory and Validation Tests")
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

        @ParameterizedTest
        @MethodSource("invalidDateComponents")
        void of_withInvalidDateComponents_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dayOfMonth));
        }

        static Stream<Arguments> invalidLeapDayDates() {
            return Stream.of(
                Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(2000)
            );
        }

        @ParameterizedTest
        @MethodSource("invalidLeapDayDates")
        void of_withInvalidLeapDayInNonLeapYear_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    /**
     * Tests related to accessing date fields, ranges, and lengths of months/years.
     */
    @Nested
    @DisplayName("Field, Range, and Length Tests")
    class FieldAndRangeTests {

        static Stream<Arguments> monthLengths() {
            return Stream.of(
                Arguments.of(2000, 1, 28), Arguments.of(2000, 2, 35), Arguments.of(2000, 3, 28),
                Arguments.of(2000, 4, 28), Arguments.of(2000, 5, 35), Arguments.of(2000, 6, 28),
                Arguments.of(2000, 7, 28), Arguments.of(2000, 8, 35), Arguments.of(2000, 9, 28),
                Arguments.of(2000, 10, 28), Arguments.of(2000, 11, 35), Arguments.of(2000, 12, 28),
                Arguments.of(2004, 12, 35) // Leap year with leap week in December
            );
        }

        @ParameterizedTest
        @MethodSource("monthLengths")
        void lengthOfMonth_shouldBeCorrect(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> fieldRanges() {
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35)),
                Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 35)), // Leap year
                Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)),
                Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)), // Leap year
                Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("fieldRanges")
        void range_shouldReturnCorrectRangeForField(int year, int month, int day, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(year, month, day).range(field));
        }

        static Stream<Arguments> fieldValues() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 145), // 28+35+28+28+26
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21), // 4+5+4+4+4
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 24172), // 2014*12 + 5 - 1
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                Arguments.of(2012, 9, 26, DAY_OF_YEAR, 271), // 3*(4+5+4)*7 - 2
                Arguments.of(2012, 9, 26, ALIGNED_WEEK_OF_YEAR, 39), // 3*(4+5+4)
                Arguments.of(2015, 12, 35, DAY_OF_WEEK, 7), // Leap day
                Arguments.of(2015, 12, 35, DAY_OF_MONTH, 35),
                Arguments.of(2015, 12, 35, DAY_OF_YEAR, 371), // 4*(4+5+4)*7 + 7
                Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_MONTH, 5),
                Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53),
                Arguments.of(2015, 12, 35, PROLEPTIC_MONTH, 24191) // 2016*12 - 1
            );
        }

        @ParameterizedTest
        @MethodSource("fieldValues")
        void getLong_shouldReturnCorrectValueForField(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, day).getLong(field));
        }
    }

    /**
     * Tests arithmetic operations like plus, minus, and until.
     */
    @Nested
    @DisplayName("Arithmetic Operation Tests")
    class ArithmeticTests {

        @Test
        void until_self_returnsZeroPeriod() {
            Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
            LocalDate isoDate = LocalDate.from(date);

            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), date.until(date));
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), date.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetryAndIsoDates")
        void plusDays_shouldBehaveLikeIsoDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso.plusDays(0), LocalDate.from(sym454.plus(0, DAYS)));
            assertEquals(iso.plusDays(1), LocalDate.from(sym454.plus(1, DAYS)));
            assertEquals(iso.plusDays(35), LocalDate.from(sym454.plus(35, DAYS)));
            assertEquals(iso.plusDays(-1), LocalDate.from(sym454.plus(-1, DAYS)));
            assertEquals(iso.plusDays(-60), LocalDate.from(sym454.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetryAndIsoDates")
        void minusDays_shouldBehaveLikeIsoDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso.minusDays(0), LocalDate.from(sym454.minus(0, DAYS)));
            assertEquals(iso.minusDays(1), LocalDate.from(sym454.minus(1, DAYS)));
            assertEquals(iso.minusDays(35), LocalDate.from(sym454.minus(35, DAYS)));
            assertEquals(iso.minusDays(-1), LocalDate.from(sym454.minus(-1, DAYS)));
            assertEquals(iso.minusDays(-60), LocalDate.from(sym454.minus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetryAndIsoDates")
        void until_withDaysUnit_shouldReturnCorrectDayCount(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(0, sym454.until(iso.plusDays(0), DAYS));
            assertEquals(1, sym454.until(iso.plusDays(1), DAYS));
            assertEquals(35, sym454.until(iso.plusDays(35), DAYS));
            assertEquals(-40, sym454.until(iso.minusDays(40), DAYS));
        }

        static Stream<Arguments> plusSamples() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 5, 34),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 12),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                Arguments.of(2012, 6, 21, 53, WEEKS, 2013, 6, 28) // 52 + 1 weeks
            );
        }

        @ParameterizedTest
        @MethodSource("plusSamples")
        void plus_withVariousUnits_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
            assertEquals(start, expected.minus(amount, unit));
        }

        static Stream<Arguments> plusLeapWeekSamples() {
            return Stream.of(
                Arguments.of(2015, 12, 28, 8, DAYS, 2016, 1, 1),
                Arguments.of(2015, 12, 28, 3, WEEKS, 2016, 1, 14),
                Arguments.of(2015, 12, 28, 3, MONTHS, 2016, 3, 28),
                Arguments.of(2015, 12, 28, 1, YEARS, 2016, 12, 28)
            );
        }

        @ParameterizedTest
        @MethodSource("plusLeapWeekSamples")
        void plus_acrossLeapWeek_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
            assertEquals(start, expected.minus(amount, unit));
        }

        static Stream<Arguments> untilSamples() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 13),
                Arguments.of(2014, 5, 26, 2014, 6, 5, WEEKS, 1),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1),
                Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1),
                Arguments.of(2014, 5, 26, 3014, 5, 26, ERAS, 0)
            );
        }

        @ParameterizedTest
        @MethodSource("untilSamples")
        void until_withVariousUnits_shouldReturnCorrectDuration(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodSamples() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 13),
                Arguments.of(2014, 5, 26, 2014, 5, 20, 0, 0, -6),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                Arguments.of(2014, 5, 26, 2024, 5, 25, 9, 11, 27)
            );
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

    /**
     * Tests for modifying date fields using `with(...)`.
     */
    @Nested
    @DisplayName("Date Modification Tests")
    class ModificationTests {

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
                Arguments.of(2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29), // Day adjusted
                Arguments.of(2015, 12, 29, YEAR, 2014, 2014, 12, 28) // Day adjusted
            );
        }

        @ParameterizedTest
        @MethodSource("withFieldSamples")
        void with_usingTemporalField_shouldReturnModifiedDate(int y, int m, int d, TemporalField f, long v, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.with(f, v));
        }

        static Stream<Arguments> withInvalidFieldValues() {
            return Stream.of(
                Arguments.of(2013, 1, 1, DAY_OF_WEEK, 8),
                Arguments.of(2013, 1, 1, DAY_OF_MONTH, 29),
                Arguments.of(2013, 1, 1, DAY_OF_YEAR, 365),
                Arguments.of(2015, 1, 1, DAY_OF_YEAR, 372),
                Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14),
                Arguments.of(2013, 1, 1, YEAR, 1_000_001)
            );
        }

        @ParameterizedTest
        @MethodSource("withInvalidFieldValues")
        void with_withInvalidValue_shouldThrowException(int year, int month, int day, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, day).with(field, value));
        }

        static Stream<Arguments> lastDayOfMonthSamples() {
            return Stream.of(
                Arguments.of(2012, 1, 23, 2012, 1, 28),
                Arguments.of(2012, 2, 23, 2012, 2, 35),
                Arguments.of(2009, 12, 23, 2009, 12, 35) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthSamples")
        void with_lastDayOfMonthAdjuster_shouldReturnLastDayOfMonth(int y, int m, int d, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    /**
     * Tests era-related functionality.
     */
    @Nested
    @DisplayName("Era-related Tests")
    class EraTests {

        static Stream<Arguments> nonSymmetry454Eras() {
            return Stream.of(
                Arguments.of(AccountingEra.BCE), Arguments.of(CopticEra.AM), Arguments.of(DiscordianEra.YOLD),
                Arguments.of(EthiopicEra.INCARNATION), Arguments.of(HijrahEra.AH), Arguments.of(InternationalFixedEra.CE),
                Arguments.of(JapaneseEra.HEISEI), Arguments.of(JulianEra.AD), Arguments.of(MinguoEra.ROC),
                Arguments.of(PaxEra.CE), Arguments.of(ThaiBuddhistEra.BE)
            );
        }

        @ParameterizedTest
        @MethodSource("nonSymmetry454Eras")
        void prolepticYear_withUnsupportedEra_shouldThrowException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }

        @Test
        void dateYearDay_shouldBeConsistentForEraAndYear() {
            for (int year = 1; year < 200; year++) {
                Symmetry454Date fromYear = Symmetry454Chronology.INSTANCE.dateYearDay(year, 1);
                assertEquals(year, fromYear.get(YEAR));
                assertEquals(IsoEra.CE, fromYear.getEra());
                assertEquals(year, fromYear.get(YEAR_OF_ERA));

                Symmetry454Date fromEraAndYear = Symmetry454Chronology.INSTANCE.dateYearDay(IsoEra.CE, year, 1);
                assertEquals(fromYear, fromEraAndYear);
            }
        }
    }

    /**
     * Tests the string representation of Symmetry454Date.
     */
    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests {

        static Stream<Arguments> toStringSamples() {
            return Stream.of(
                Arguments.of(Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"),
                Arguments.of(Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"),
                Arguments.of(2000, 8, 35, "Sym454 CE 2000/08/35"),
                Arguments.of(1970, 12, 28, "Sym454 CE 1970/12/28")
            );
        }

        @ParameterizedTest
        @MethodSource("toStringSamples")
        void toString_shouldReturnCorrectFormat(int year, int month, int day, String expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, day).toString());
        }

        @ParameterizedTest
        @MethodSource("toStringSamples")
        void toString_withDateObject_shouldReturnCorrectFormat(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}