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
import java.time.chrono.IsoEra;
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
 * Tests for the {@link Symmetry010Chronology} and {@link Symmetry010Date}.
 */
@DisplayName("Symmetry010Chronology and Symmetry010Date")
class Symmetry010ChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleSymmetryAndIsoDates() {
        return Stream.of(
            // Historical event: Constantine the Great, Roman emperor (d. 337)
            Arguments.of(Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry010Date.of(272, 2, 27), LocalDate.of(272, 2, 26)),
            // Historical event: Charlemagne, Frankish king (d. 814)
            Arguments.of(Symmetry010Date.of(742, 3, 27), LocalDate.of(742, 4, 2)),
            Arguments.of(Symmetry010Date.of(742, 4, 2), LocalDate.of(742, 4, 7)),
            // Historical event: Norman Conquest: Battle of Hastings
            Arguments.of(Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
            // Historical event: Albert Einstein, German theoretical physicist (d. 1955)
            Arguments.of(Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)),
            // Historical event: Dennis MacAlistair Ritchie, American computer scientist (d. 2011)
            Arguments.of(Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9)),
            Arguments.of(Symmetry010Date.of(1941, 9, 9), LocalDate.of(1941, 9, 7)),
            // Historical event: Unix time begins at 00:00:00 UTC/GMT.
            Arguments.of(Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
            Arguments.of(Symmetry010Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)),
            // Historical event: Start of the 21st century / 3rd millennium
            Arguments.of(Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1)),
            Arguments.of(Symmetry010Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3))
        );
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Creation")
    class DateCreationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#invalidDateComponents")
        @DisplayName("of() should throw exception for invalid date components")
        void of_shouldThrowExceptionForInvalidDateComponents(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dayOfMonth));
        }

        static Stream<Arguments> invalidDateComponents() {
            return Stream.of(
                Arguments.of(2000, 13, 1),   // Invalid month
                Arguments.of(2000, 0, 1),    // Invalid month
                Arguments.of(2000, 1, 0),    // Invalid day
                Arguments.of(2000, 1, 31),   // Invalid day for month 1 (30 days)
                Arguments.of(2000, 2, 32),   // Invalid day for month 2 (31 days)
                Arguments.of(2004, 12, 38)  // Invalid day for leap month (37 days)
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#invalidLeapDayDates")
        @DisplayName("of() should throw exception for non-existent leap day")
        void of_shouldThrowExceptionForNonExistentLeapDay(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37), "Leap day should not exist in a non-leap year");
        }

        static Stream<Arguments> invalidLeapDayDates() {
            return Stream.of(
                Arguments.of(1),
                Arguments.of(100),
                Arguments.of(200),
                Arguments.of(2000)
            );
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversions to/from other calendar systems")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("should convert from Symmetry010Date to LocalDate")
        void fromSymmetryDateToLocalDate(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("should convert from LocalDate to Symmetry010Date")
        void fromLocalDateToSymmetryDate(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry010Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("should create date from epoch day")
        void dateFromEpochDay(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry010Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("should convert to epoch day")
        void toEpochDay(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symmetryDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("should create date from a temporal accessor")
        void dateFromTemporal(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry010Chronology.INSTANCE.date(isoDate));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Properties")
    class PropertyTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#monthLengths")
        @DisplayName("lengthOfMonth() should return the correct number of days")
        void lengthOfMonth(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> monthLengths() {
            return Stream.of(
                Arguments.of(2000, 1, 30),
                Arguments.of(2000, 2, 31),
                Arguments.of(2000, 3, 30),
                Arguments.of(2000, 4, 30),
                Arguments.of(2000, 5, 31),
                Arguments.of(2000, 6, 30),
                Arguments.of(2000, 7, 30),
                Arguments.of(2000, 8, 31),
                Arguments.of(2000, 9, 30),
                Arguments.of(2000, 10, 30),
                Arguments.of(2000, 11, 31),
                Arguments.of(2000, 12, 30), // Non-leap year
                Arguments.of(2004, 12, 37)  // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#fieldRanges")
        @DisplayName("range(field) should return the correct value range")
        void range(int year, int month, int day, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry010Date.of(year, month, day).range(field));
        }

        static Stream<Arguments> fieldRanges() {
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)),
                Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)), // Leap year
                Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)),
                Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)), // Leap year
                Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)), // 31-day month is 4 weeks + 3 days
                Arguments.of(2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)), // 37-day month is 5 weeks + 2 days
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#fieldValues")
        @DisplayName("getLong(field) should return the correct value for the field")
        void getLong(int year, int month, int day, TemporalField field, long expectedValue) {
            Symmetry010Date date = Symmetry010Date.of(year, month, day);
            assertEquals(expectedValue, date.getLong(field));
        }

        static Stream<Arguments> fieldValues() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 2L),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                // DAY_OF_YEAR for 2014-05-26: (M1:30 + M2:31 + M3:30 + M4:30) + 26 = 147
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 147L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),
                // Leap year date
                Arguments.of(2015, 12, 37, DAY_OF_WEEK, 5L),
                Arguments.of(2015, 12, 37, DAY_OF_MONTH, 37L),
                // DAY_OF_YEAR for 2015-12-37: 364 (normal year) + 7 (leap week) = 371
                Arguments.of(2015, 12, 37, DAY_OF_YEAR, 371L),
                Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53L)
            );
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Arithmetic")
    class ArithmeticTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#plusSamples")
        @DisplayName("plus() should add the specified amount")
        void plus(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            Symmetry010Date start = Symmetry010Date.of(startYear, startMonth, startDay);
            Symmetry010Date expected = Symmetry010Date.of(endYear, endMonth, endDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#plusLeapWeekSamples")
        @DisplayName("plus() should handle leap weeks correctly")
        void plus_handlesLeapWeeks(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            Symmetry010Date start = Symmetry010Date.of(startYear, startMonth, startDay);
            Symmetry010Date expected = Symmetry010Date.of(endYear, endMonth, endDay);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#plusSamples")
        @DisplayName("minus() should subtract the specified amount")
        void minus(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            Symmetry010Date end = Symmetry010Date.of(endYear, endMonth, endDay);
            Symmetry010Date expected = Symmetry010Date.of(startYear, startMonth, startDay);
            assertEquals(expected, end.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#plusLeapWeekSamples")
        @DisplayName("minus() should handle leap weeks correctly")
        void minus_handlesLeapWeeks(int startYear, int startMonth, int startDay, long amount, TemporalUnit unit, int endYear, int endMonth, int endDay) {
            Symmetry010Date end = Symmetry010Date.of(endYear, endMonth, endDay);
            Symmetry010Date expected = Symmetry010Date.of(startYear, startMonth, startDay);
            assertEquals(expected, end.minus(amount, unit));
        }

        static Stream<Arguments> plusSamples() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26)
            );
        }

        static Stream<Arguments> plusLeapWeekSamples() {
            return Stream.of(
                Arguments.of(2015, 12, 28, 8, DAYS, 2015, 12, 36),
                Arguments.of(2015, 12, 28, 3, WEEKS, 2016, 1, 12),
                Arguments.of(2015, 12, 28, 1, YEARS, 2016, 12, 28)
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#untilSamples")
        @DisplayName("until(end, unit) should return the amount of time between two dates")
        void until(Symmetry010Date start, Symmetry010Date end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilSamples() {
            Symmetry010Date d2014_05_26 = Symmetry010Date.of(2014, 5, 26);
            return Stream.of(
                Arguments.of(d2014_05_26, Symmetry010Date.of(2014, 5, 26), DAYS, 0L),
                Arguments.of(d2014_05_26, Symmetry010Date.of(2014, 6, 4), DAYS, 9L),
                Arguments.of(d2014_05_26, Symmetry010Date.of(2014, 5, 20), DAYS, -6L),
                Arguments.of(d2014_05_26, Symmetry010Date.of(2014, 6, 1), WEEKS, 1L),
                Arguments.of(d2014_05_26, Symmetry010Date.of(2014, 6, 26), MONTHS, 1L),
                Arguments.of(d2014_05_26, Symmetry010Date.of(2015, 5, 26), YEARS, 1L),
                Arguments.of(d2014_05_26, Symmetry010Date.of(2024, 5, 26), DECADES, 1L),
                Arguments.of(d2014_05_26, Symmetry010Date.of(2114, 5, 26), CENTURIES, 1L),
                Arguments.of(d2014_05_26, Symmetry010Date.of(3014, 5, 26), MILLENNIA, 1L),
                Arguments.of(d2014_05_26, Symmetry010Date.of(3014, 5, 26), ERAS, 0L)
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#untilPeriodSamples")
        @DisplayName("until(end) should return the period between two dates")
        void until_period(Symmetry010Date start, Symmetry010Date end, ChronoPeriod expected) {
            assertEquals(expected, start.until(end));
        }

        static Stream<Arguments> untilPeriodSamples() {
            Symmetry010Date d2014_05_26 = Symmetry010Date.of(2014, 5, 26);
            return Stream.of(
                Arguments.of(d2014_05_26, Symmetry010Date.of(2014, 5, 26), Symmetry010Chronology.INSTANCE.period(0, 0, 0)),
                Arguments.of(d2014_05_26, Symmetry010Date.of(2014, 6, 4), Symmetry010Chronology.INSTANCE.period(0, 0, 9)),
                Arguments.of(d2014_05_26, Symmetry010Date.of(2014, 6, 26), Symmetry010Chronology.INSTANCE.period(0, 1, 0)),
                Arguments.of(d2014_05_26, Symmetry010Date.of(2015, 5, 26), Symmetry010Chronology.INSTANCE.period(1, 0, 0)),
                Arguments.of(d2014_05_26, Symmetry010Date.of(2015, 6, 27), Symmetry010Chronology.INSTANCE.period(1, 1, 1))
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("until() with an equivalent ISO date should return a zero period")
        void until_withEquivalentIsoDate_returnsZeroPeriod(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(symmetryDate));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Adjusters")
    class AdjusterTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#withFieldSamples")
        @DisplayName("with(field, value) should return a new date with the field adjusted")
        void with(Symmetry010Date baseDate, TemporalField field, long value, Symmetry010Date expectedDate) {
            assertEquals(expectedDate, baseDate.with(field, value));
        }

        static Stream<Arguments> withFieldSamples() {
            return Stream.of(
                Arguments.of(Symmetry010Date.of(2014, 5, 26), DAY_OF_WEEK, 1, Symmetry010Date.of(2014, 5, 20)),
                Arguments.of(Symmetry010Date.of(2014, 5, 26), DAY_OF_MONTH, 28, Symmetry010Date.of(2014, 5, 28)),
                Arguments.of(Symmetry010Date.of(2014, 5, 26), DAY_OF_YEAR, 364, Symmetry010Date.of(2014, 12, 30)),
                Arguments.of(Symmetry010Date.of(2014, 5, 26), MONTH_OF_YEAR, 4, Symmetry010Date.of(2014, 4, 26)),
                Arguments.of(Symmetry010Date.of(2014, 5, 26), YEAR, 2012, Symmetry010Date.of(2012, 5, 26)),
                Arguments.of(Symmetry010Date.of(2015, 12, 37), YEAR, 2004, Symmetry010Date.of(2004, 12, 37)) // Adjust year of leap day
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#withInvalidFieldValues")
        @DisplayName("with(field, value) should throw exception for invalid values")
        void with_shouldThrowExceptionForInvalidValue(Symmetry010Date baseDate, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> baseDate.with(field, value));
        }

        static Stream<Arguments> withInvalidFieldValues() {
            Symmetry010Date date = Symmetry010Date.of(2013, 1, 1);
            return Stream.of(
                Arguments.of(date, DAY_OF_WEEK, 8),
                Arguments.of(date, DAY_OF_MONTH, 31),
                Arguments.of(date, DAY_OF_YEAR, 365), // Non-leap year
                Arguments.of(date, MONTH_OF_YEAR, 13),
                Arguments.of(date, YEAR, 1_000_001)
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#lastDayOfMonthSamples")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) should return the last day of the month")
        void with_lastDayOfMonth(Symmetry010Date date, Symmetry010Date expected) {
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> lastDayOfMonthSamples() {
            return Stream.of(
                Arguments.of(Symmetry010Date.of(2012, 1, 23), Symmetry010Date.of(2012, 1, 30)),
                Arguments.of(Symmetry010Date.of(2012, 2, 23), Symmetry010Date.of(2012, 2, 31)),
                Arguments.of(Symmetry010Date.of(2009, 12, 23), Symmetry010Date.of(2009, 12, 37)) // Leap year
            );
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Chronology API")
    class ChronologyApiTests {

        @Test
        @DisplayName("prolepticYear() should return correct year for CE era")
        void prolepticYear_forCeEra() {
            assertEquals(4, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 4));
            assertEquals(1, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 1));
            assertEquals(2000, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 2000));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#nonIsoEras")
        @DisplayName("prolepticYear() should throw ClassCastException for non-ISO eras")
        void prolepticYear_throwsForNonIsoEra(Era nonIsoEra) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(nonIsoEra, 4));
        }
    }

    static Stream<Arguments> nonIsoEras() {
        // A representative sample of non-ISO eras
        return Stream.of(
            Arguments.of(org.threeten.extra.chrono.CopticEra.AM),
            Arguments.of(java.time.chrono.HijrahEra.AH),
            Arguments.of(java.time.chrono.JapaneseEra.HEISEI),
            Arguments.of(java.time.chrono.MinguoEra.ROC),
            Arguments.of(java.time.chrono.ThaiBuddhistEra.BE)
        );
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("toString()")
    class ToStringTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#toStringSamples")
        @DisplayName("should return the correct string representation")
        void toString(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }

        static Stream<Arguments> toStringSamples() {
            return Stream.of(
                Arguments.of(Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"),
                Arguments.of(Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"),
                Arguments.of(Symmetry010Date.of(2000, 8, 31), "Sym010 CE 2000/08/31"),
                Arguments.of(Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37")
            );
        }
    }
}