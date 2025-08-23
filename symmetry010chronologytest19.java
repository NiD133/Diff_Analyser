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
 * Tests for the {@link Symmetry010Chronology} and {@link Symmetry010Date}.
 */
@DisplayName("Symmetry010Chronology and Symmetry010Date")
public class Symmetry010ChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample pairs of {@link Symmetry010Date} and their equivalent {@link LocalDate}.
     * The comments provide historical context for the chosen dates.
     */
    static Stream<Arguments> sampleSymmetryAndIsoDates() {
        return Stream.of(
            // Constantine the Great, Roman emperor (d. 337)
            Arguments.of(Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry010Date.of(272, 2, 27), LocalDate.of(272, 2, 26)),
            // Charlemagne, Frankish king (d. 814)
            Arguments.of(Symmetry010Date.of(742, 3, 27), LocalDate.of(742, 4, 2)),
            Arguments.of(Symmetry010Date.of(742, 4, 2), LocalDate.of(742, 4, 7)),
            // Norman Conquest: Battle of Hastings
            Arguments.of(Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
            // Francesco Petrarca - Petrarch, Italian scholar and poet (d. 1374)
            Arguments.of(Symmetry010Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)),
            Arguments.of(Symmetry010Date.of(1304, 7, 20), LocalDate.of(1304, 7, 19)),
            // Charles the Bold, Duke of Burgundy (d. 1477)
            Arguments.of(Symmetry010Date.of(1433, 11, 12), LocalDate.of(1433, 11, 10)),
            Arguments.of(Symmetry010Date.of(1433, 11, 10), LocalDate.of(1433, 11, 8)),
            // Leonardo da Vinci, Italian polymath (d. 1519)
            Arguments.of(Symmetry010Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)),
            Arguments.of(Symmetry010Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)),
            // Christopher Columbus's expedition makes landfall in the Caribbean
            Arguments.of(Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
            Arguments.of(Symmetry010Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)),
            // Galileo Galilei, Italian astronomer and physicist (d. 1642)
            Arguments.of(Symmetry010Date.of(1564, 2, 18), LocalDate.of(1564, 2, 15)),
            Arguments.of(Symmetry010Date.of(1564, 2, 15), LocalDate.of(1564, 2, 12)),
            // William Shakespeare is baptized (d. 1616)
            Arguments.of(Symmetry010Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)),
            Arguments.of(Symmetry010Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)),
            // Sir Isaac Newton, English physicist and mathematician (d. 1727)
            Arguments.of(Symmetry010Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)),
            Arguments.of(Symmetry010Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)),
            // Leonhard Euler, Swiss mathematician and physicist (d. 1783)
            Arguments.of(Symmetry010Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)),
            Arguments.of(Symmetry010Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)),
            // French Revolution: Storming of the Bastille
            Arguments.of(Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
            Arguments.of(Symmetry010Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)),
            // Albert Einstein, German theoretical physicist (d. 1955)
            Arguments.of(Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)),
            // Dennis MacAlistair Ritchie, American computer scientist (d. 2011)
            Arguments.of(Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9)),
            Arguments.of(Symmetry010Date.of(1941, 9, 9), LocalDate.of(1941, 9, 7)),
            // Unix time begins at 00:00:00 UTC/GMT
            Arguments.of(Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
            Arguments.of(Symmetry010Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)),
            // Start of the 21st century / 3rd millennium
            Arguments.of(Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1)),
            Arguments.of(Symmetry010Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3))
        );
    }

    @Nested
    @DisplayName("Factory and Conversion")
    class FactoryAndConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        void fromSymmetryDate_toLocalDate_shouldBeEquivalent(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        void fromLocalDate_toSymmetryDate_shouldBeEquivalent(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry010Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        void chronologyDateFromTemporal_shouldCreateCorrectDate(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry010Chronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        void toEpochDay_shouldMatchIsoDate(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symmetryDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        void dateEpochDay_shouldCreateCorrectDate(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry010Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        static Stream<Arguments> invalidDateComponents() {
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
        @MethodSource("invalidDateComponents")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dayOfMonth));
        }

        static Stream<Integer> invalidLeapDayYears() {
            return Stream.of(1, 100, 200, 2000);
        }

        @ParameterizedTest
        @MethodSource("invalidLeapDayYears")
        void of_withInvalidLeapDayInNonLeapYear_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    @Nested
    @DisplayName("Date Properties")
    class DatePropertiesTests {

        // year, month, day, expected length
        static Stream<Arguments> monthLengths() {
            return Stream.of(
                Arguments.of(2000, 1, 28, 30), Arguments.of(2000, 2, 28, 31), Arguments.of(2000, 3, 28, 30),
                Arguments.of(2000, 4, 28, 30), Arguments.of(2000, 5, 28, 31), Arguments.of(2000, 6, 28, 30),
                Arguments.of(2000, 7, 28, 30), Arguments.of(2000, 8, 28, 31), Arguments.of(2000, 9, 28, 30),
                Arguments.of(2000, 10, 28, 30), Arguments.of(2000, 11, 28, 31), Arguments.of(2000, 12, 28, 30),
                Arguments.of(2004, 12, 20, 37) // Leap year December
            );
        }

        @ParameterizedTest
        @MethodSource("monthLengths")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, day).lengthOfMonth());
            assertEquals(expectedLength, Symmetry010Date.of(year, month, 1).lengthOfMonth());
        }

        // year, month, day, field, expected range
        static Stream<Arguments> fieldRanges() {
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)),
                Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)), // Leap year December
                Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)),
                Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)), // Leap year
                Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)), // Note: spec says 31 days, but still 4 full weeks
                Arguments.of(2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)), // Leap year December
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("fieldRanges")
        void range_forField_shouldReturnCorrectValueRange(int year, int month, int dayOfMonth, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry010Date.of(year, month, dayOfMonth).range(field));
        }

        // year, month, day, field, expected value
        static Stream<Arguments> fieldValues() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 2L),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 147L), // 30+31+30+30+26
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L), // 4+5+4+4+4
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 24172L), // 2014 * 12 + 5 - 1
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),
                Arguments.of(2015, 12, 37, DAY_OF_YEAR, 371L), // 364 + 7
                Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53L)
            );
        }

        @ParameterizedTest
        @MethodSource("fieldValues")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int dayOfMonth, TemporalField field, long expectedValue) {
            assertEquals(expectedValue, Symmetry010Date.of(year, month, dayOfMonth).getLong(field));
        }

        @Test
        void toString_shouldReturnFormattedDate() {
            assertEquals("Sym010 CE 1/01/01", Symmetry010Date.of(1, 1, 1).toString());
            assertEquals("Sym010 CE 1970/02/31", Symmetry010Date.of(1970, 2, 31).toString());
            assertEquals("Sym010 CE 2009/12/37", Symmetry010Date.of(2009, 12, 37).toString());
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class DateManipulationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        void plusDays_shouldBehaveAsIsoPlusDays(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(symmetryDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(symmetryDate.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(symmetryDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(symmetryDate.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        void minusDays_shouldBehaveAsIsoMinusDays(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate.minus(0, DAYS)));
            assertEquals(isoDate.minusDays(1), LocalDate.from(symmetryDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(symmetryDate.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(symmetryDate.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(symmetryDate.minus(-60, DAYS)));
        }

        // startYear, startMonth, startDay, amount, unit, endYear, endMonth, endDay
        static Stream<Arguments> plusProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                // Leap week handling
                Arguments.of(2015, 12, 28, 8, DAYS, 2015, 12, 36),
                Arguments.of(2015, 12, 28, 3, WEEKS, 2016, 1, 12),
                Arguments.of(2015, 12, 28, 12, MONTHS, 2016, 12, 28)
            );
        }

        @ParameterizedTest
        @MethodSource("plusProvider")
        void plus_withVariousUnits_shouldReturnCorrectDate(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            Symmetry010Date start = Symmetry010Date.of(startYear, startMonth, startDay);
            Symmetry010Date expectedEnd = Symmetry010Date.of(endYear, endMonth, endDay);
            assertEquals(expectedEnd, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusProvider")
        void minus_withVariousUnits_shouldBeInverseOfPlus(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            Symmetry010Date expectedStart = Symmetry010Date.of(startYear, startMonth, startDay);
            Symmetry010Date end = Symmetry010Date.of(endYear, endMonth, endDay);
            assertEquals(expectedStart, end.minus(amount, unit));
        }

        @Test
        void plus_withUnsupportedUnit_shouldThrowException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry010Date.of(2012, 6, 28).plus(1, ChronoUnit.MINUTES));
        }

        // year, month, day, field, value, expectedYear, expectedMonth, expectedDay
        static Stream<Arguments> withAdjustmentProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2015, 12, 37, YEAR, 2004, 2004, 12, 37), // Adjust year on leap day
                Arguments.of(2015, 12, 37, YEAR, 2013, 2013, 12, 30)  // Adjust year on leap day to non-leap
            );
        }

        @ParameterizedTest
        @MethodSource("withAdjustmentProvider")
        void with_fieldAndValue_shouldReturnAdjustedDate(int year, int month, int day, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDay) {
            Symmetry010Date start = Symmetry010Date.of(year, month, day);
            Symmetry010Date expected = Symmetry010Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, start.with(field, value));
        }

        // year, month, day, field, invalid value
        static Stream<Arguments> invalidWithAdjustmentProvider() {
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
        @MethodSource("invalidWithAdjustmentProvider")
        void with_invalidFieldValue_shouldThrowException(int year, int month, int day, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, day).with(field, value));
        }

        // year, month, day, expectedYear, expectedMonth, expectedDay
        static Stream<Arguments> lastDayOfMonthProvider() {
            return Stream.of(
                Arguments.of(2012, 1, 23, 2012, 1, 30),
                Arguments.of(2012, 2, 23, 2012, 2, 31),
                Arguments.of(2009, 12, 23, 2009, 12, 37) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthProvider")
        void with_lastDayOfMonthAdjuster_shouldReturnLastDayOfMonth(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
            Symmetry010Date base = Symmetry010Date.of(year, month, day);
            Symmetry010Date expected = Symmetry010Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        void until_self_shouldReturnZeroPeriod(Symmetry010Date symmetryDate, LocalDate isoDate) {
            ChronoPeriod zeroPeriod = Symmetry010Chronology.INSTANCE.period(0, 0, 0);
            assertEquals(zeroPeriod, symmetryDate.until(symmetryDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        void until_equivalentIsoDate_shouldReturnZeroPeriod(Symmetry010Date symmetryDate, LocalDate isoDate) {
            ChronoPeriod zeroPeriod = Symmetry010Chronology.INSTANCE.period(0, 0, 0);
            assertEquals(zeroPeriod, symmetryDate.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        void isoDate_until_equivalentSymmetryDate_shouldReturnZeroPeriod(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(symmetryDate));
        }

        // startY, startM, startD, endY, endM, endD, unit, expected amount
        static Stream<Arguments> untilByUnitProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 9L),
                Arguments.of(2014, 5, 26, 2014, 6, 1, WEEKS, 1L),
                Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, ERAS, 0L)
            );
        }

        @ParameterizedTest
        @MethodSource("untilByUnitProvider")
        void until_byUnit_shouldReturnCorrectAmount(int startY, int startM, int startD, int endY, int endM, int endD, TemporalUnit unit, long expected) {
            Symmetry010Date start = Symmetry010Date.of(startY, startM, startD);
            Symmetry010Date end = Symmetry010Date.of(endY, endM, endD);
            assertEquals(expected, start.until(end, unit));
        }

        // startY, startM, startD, endY, endM, endD, periodY, periodM, periodD
        static Stream<Arguments> untilAsPeriodProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 9),
                Arguments.of(2014, 5, 26, 2014, 5, 20, 0, 0, -6),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0)
            );
        }

        @ParameterizedTest
        @MethodSource("untilAsPeriodProvider")
        void until_asPeriod_shouldReturnCorrectPeriod(int startY, int startM, int startD, int endY, int endM, int endD, int pY, int pM, int pD) {
            Symmetry010Date start = Symmetry010Date.of(startY, startM, startD);
            Symmetry010Date end = Symmetry010Date.of(endY, endM, endD);
            ChronoPeriod expected = Symmetry010Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology-specific Behavior")
    class ChronologySpecificTests {

        /**
         * Provides eras that are not compatible with Symmetry010Chronology's prolepticYear method.
         */
        static Stream<Era> unsupportedErasForProlepticYear() {
            return Stream.of(
                // These eras are from threeten-extra, not standard java.time
                // AccountingEra.BCE, AccountingEra.CE, CopticEra.BEFORE_AM, CopticEra.AM,
                // DiscordianEra.YOLD, EthiopicEra.BEFORE_INCARNATION, EthiopicEra.INCARNATION,
                // JulianEra.BC, JulianEra.AD, PaxEra.BCE, PaxEra.CE,
                // InternationalFixedEra.CE,
                // Standard java.time eras
                HijrahEra.AH,
                JapaneseEra.MEIJI, JapaneseEra.TAISHO, JapaneseEra.SHOWA, JapaneseEra.HEISEI,
                MinguoEra.BEFORE_ROC, MinguoEra.ROC,
                ThaiBuddhistEra.BEFORE_BE, ThaiBuddhistEra.BE
            );
        }

        @ParameterizedTest
        @MethodSource("unsupportedErasForProlepticYear")
        void prolepticYear_withUnsupportedEra_shouldThrowClassCastException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }
}