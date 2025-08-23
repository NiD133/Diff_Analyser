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

@DisplayName("Symmetry010Chronology and Symmetry010Date Tests")
class Symmetry010ChronologyTest {

    // This data provider supplies pairs of equivalent dates in Symmetry010 and ISO calendars.
    static Stream<Arguments> sampleSymmetryAndIsoDates() {
        return Stream.of(
                Arguments.of(Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
                Arguments.of(Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)),
                Arguments.of(Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
                Arguments.of(Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
                Arguments.of(Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)),
                Arguments.of(Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
                Arguments.of(Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1))
        );
    }

    @Nested
    @DisplayName("Conversion Tests")
    class ConversionTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("should convert from Symmetry010Date to an equivalent LocalDate")
        void fromSymmetry010Date_convertsToCorrectLocalDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(sym010Date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("should convert from a LocalDate to an equivalent Symmetry010Date")
        void fromLocalDate_convertsToCorrectSymmetry010Date(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(sym010Date, Symmetry010Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("toEpochDay should return the correct epoch day")
        void toEpochDay_returnsCorrectValue(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), sym010Date.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("chronology.date(temporal) should create the correct date")
        void chronologyDateFromTemporal_createsCorrectDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(sym010Date, Symmetry010Chronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("chronology.dateEpochDay should create the correct date")
        void chronologyDateFromEpochDay_createsCorrectDate(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(sym010Date, Symmetry010Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }
    }

    @Nested
    @DisplayName("Creation Tests")
    class CreationTests {
        @ParameterizedTest(name = "of({0}, {1}, {2}) should throw DateTimeException")
        @MethodSource("provideInvalidDateParts")
        void of_withInvalidDate_shouldThrowException(int year, int month, int day, String reason) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, day));
        }

        static Stream<Arguments> provideInvalidDateParts() {
            return Stream.of(
                    Arguments.of(2000, 0, 1, "Invalid month: 0"),
                    Arguments.of(2000, 13, 1, "Invalid month: 13"),
                    Arguments.of(2000, 1, 0, "Invalid day: 0"),
                    Arguments.of(2000, 1, 31, "Invalid day for January (has 30)"),
                    Arguments.of(2000, 2, 32, "Invalid day for February (has 31)"),
                    Arguments.of(2004, 12, 38, "Invalid day for December in a leap year (has 37)")
            );
        }

        @ParameterizedTest(name = "of({0}, 12, 37) should throw for non-leap year")
        @MethodSource("provideNonLeapYears")
        void of_withInvalidLeapDayInNonLeapYear_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }

        static Stream<Integer> provideNonLeapYears() {
            // In Symmetry010, 2000 is not a leap year, but 2004 is.
            return Stream.of(1, 100, 200, 2000);
        }
    }

    @Nested
    @DisplayName("Date Manipulation Tests")
    class ManipulationTests {
        @ParameterizedTest(name = "{0} plus {1} {2} should be {3}")
        @MethodSource
        void plus_addsAmountToDateCorrectly(Symmetry010Date startDate, long amount, TemporalUnit unit, Symmetry010Date expectedDate) {
            assertEquals(expectedDate, startDate.plus(amount, unit));
        }

        static Stream<Arguments> providePlus_addsAmountToDateCorrectly() {
            Symmetry010Date d = Symmetry010Date.of(2014, 5, 26);
            return Stream.of(
                    Arguments.of(d, 0, DAYS, d),
                    Arguments.of(d, 8, DAYS, Symmetry010Date.of(2014, 6, 3)),
                    Arguments.of(d, -3, DAYS, Symmetry010Date.of(2014, 5, 23)),
                    Arguments.of(d, 3, WEEKS, Symmetry010Date.of(2014, 6, 16)),
                    Arguments.of(d, 3, MONTHS, Symmetry010Date.of(2014, 8, 26)),
                    Arguments.of(d, 3, YEARS, Symmetry010Date.of(2017, 5, 26)),
                    Arguments.of(d, 3, DECADES, Symmetry010Date.of(2044, 5, 26)),
                    Arguments.of(d, 3, CENTURIES, Symmetry010Date.of(2314, 5, 26)),
                    Arguments.of(d, -1, MILLENNIA, Symmetry010Date.of(1014, 5, 26))
            );
        }

        @ParameterizedTest(name = "{0}.with({1}, {2}) should be {3}")
        @MethodSource
        void with_adjustsFieldToNewValue(Symmetry010Date baseDate, TemporalField field, long value, Symmetry010Date expectedDate) {
            assertEquals(expectedDate, baseDate.with(field, value));
        }

        static Stream<Arguments> provideWith_adjustsFieldToNewValue() {
            return Stream.of(
                    Arguments.of(Symmetry010Date.of(2014, 5, 26), DAY_OF_WEEK, 1, Symmetry010Date.of(2014, 5, 20)),
                    Arguments.of(Symmetry010Date.of(2014, 5, 26), DAY_OF_MONTH, 28, Symmetry010Date.of(2014, 5, 28)),
                    Arguments.of(Symmetry010Date.of(2014, 5, 26), DAY_OF_YEAR, 364, Symmetry010Date.of(2014, 12, 30)),
                    Arguments.of(Symmetry010Date.of(2014, 5, 26), MONTH_OF_YEAR, 4, Symmetry010Date.of(2014, 4, 26)),
                    Arguments.of(Symmetry010Date.of(2014, 5, 26), YEAR, 2012, Symmetry010Date.of(2012, 5, 26)),
                    Arguments.of(Symmetry010Date.of(2015, 12, 37), YEAR, 2004, Symmetry010Date.of(2004, 12, 37))
            );
        }

        @ParameterizedTest(name = "with({1}, {2}) on {0} should throw DateTimeException")
        @MethodSource
        void with_invalidValue_throwsException(Symmetry010Date baseDate, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> baseDate.with(field, value));
        }

        static Stream<Arguments> provideWith_invalidValue_throwsException() {
            Symmetry010Date d = Symmetry010Date.of(2013, 1, 1);
            return Stream.of(
                    Arguments.of(d, DAY_OF_WEEK, 8),
                    Arguments.of(d, DAY_OF_MONTH, 31),
                    Arguments.of(d, DAY_OF_YEAR, 365), // 2013 is not a leap year
                    Arguments.of(d, MONTH_OF_YEAR, 14),
                    Arguments.of(d, YEAR, 1_000_001)
            );
        }

        @ParameterizedTest(name = "lastDayOfMonth for {0}-{1} should be {2}")
        @MethodSource
        void with_lastDayOfMonth_adjustsToCorrectDate(int year, int month, int expectedDay) {
            Symmetry010Date date = Symmetry010Date.of(year, month, 1);
            Symmetry010Date expected = Symmetry010Date.of(year, month, expectedDay);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> provideWith_lastDayOfMonth_adjustsToCorrectDate() {
            return Stream.of(
                    Arguments.of(2012, 1, 30),
                    Arguments.of(2012, 2, 31),
                    Arguments.of(2012, 12, 30), // 2012 is not a leap year
                    Arguments.of(2009, 12, 37)  // 2009 is a leap year
            );
        }
    }

    @Nested
    @DisplayName("Field and Unit Access Tests")
    class FieldAndUnitAccessTests {
        @ParameterizedTest(name = "length of month for {0} should be {1}")
        @MethodSource
        void lengthOfMonth_returnsCorrectValue(Symmetry010Date dateInMonth, int expectedLength) {
            assertEquals(expectedLength, dateInMonth.lengthOfMonth());
        }

        static Stream<Arguments> provideLengthOfMonth_returnsCorrectValue() {
            return Stream.of(
                    Arguments.of(Symmetry010Date.of(2000, 1, 1), 30),  // Jan
                    Arguments.of(Symmetry010Date.of(2000, 2, 1), 31),  // Feb
                    Arguments.of(Symmetry010Date.of(2000, 12, 1), 30), // Dec in non-leap year
                    Arguments.of(Symmetry010Date.of(2004, 12, 1), 37)  // Dec in leap year
            );
        }

        @ParameterizedTest(name = "range of {1} for {0} should be {2}")
        @MethodSource
        void range_returnsCorrectValueRangeForField(Symmetry010Date date, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, date.range(field));
        }

        static Stream<Arguments> provideRange_returnsCorrectValueRangeForField() {
            return Stream.of(
                    Arguments.of(Symmetry010Date.of(2012, 1, 23), DAY_OF_MONTH, ValueRange.of(1, 30)),
                    Arguments.of(Symmetry010Date.of(2012, 2, 23), DAY_OF_MONTH, ValueRange.of(1, 31)),
                    Arguments.of(Symmetry010Date.of(2015, 12, 23), DAY_OF_MONTH, ValueRange.of(1, 37)),
                    Arguments.of(Symmetry010Date.of(2012, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 364)),
                    Arguments.of(Symmetry010Date.of(2015, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 371)),
                    Arguments.of(Symmetry010Date.of(2012, 1, 23), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                    Arguments.of(Symmetry010Date.of(2015, 12, 23), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5))
            );
        }

        @ParameterizedTest(name = "{0}.getLong({1}) should be {2}")
        @MethodSource
        void getLong_returnsCorrectValueForField(Symmetry010Date date, TemporalField field, long expectedValue) {
            assertEquals(expectedValue, date.getLong(field));
        }

        static Stream<Arguments> provideGetLong_returnsCorrectValueForField() {
            Symmetry010Date date = Symmetry010Date.of(2014, 5, 26);
            // Day of year for 2014-05-26: 30(Jan)+31(Feb)+30(Mar)+30(Apr)+26 = 147
            long dayOfYear = 30 + 31 + 30 + 30 + 26;
            return Stream.of(
                    Arguments.of(date, DAY_OF_WEEK, 2),
                    Arguments.of(date, DAY_OF_MONTH, 26),
                    Arguments.of(date, DAY_OF_YEAR, dayOfYear),
                    Arguments.of(date, MONTH_OF_YEAR, 5),
                    Arguments.of(date, YEAR, 2014),
                    Arguments.of(date, ERA, 1)
            );
        }

        @ParameterizedTest(name = "from {0} until {1} in {2} is {3}")
        @MethodSource
        void until_calculatesDurationInUnitCorrectly(Symmetry010Date start, Symmetry010Date end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> provideUntil_calculatesDurationInUnitCorrectly() {
            Symmetry010Date d1 = Symmetry010Date.of(2014, 5, 26);
            Symmetry010Date d2 = Symmetry010Date.of(2014, 6, 4);
            Symmetry010Date d3 = Symmetry010Date.of(2015, 5, 26);
            return Stream.of(
                    Arguments.of(d1, d1, DAYS, 0),
                    Arguments.of(d1, d2, DAYS, 9),
                    Arguments.of(d2, d1, DAYS, -9),
                    Arguments.of(d1, d2, WEEKS, 1),
                    Arguments.of(d1, d3, YEARS, 1),
                    Arguments.of(d1, d3, MONTHS, 12)
            );
        }

        @ParameterizedTest(name = "period from {0} until {1} is {2}")
        @MethodSource
        void until_calculatesPeriodCorrectly(Symmetry010Date start, Symmetry010Date end, ChronoPeriod expected) {
            assertEquals(expected, start.until(end));
        }

        static Stream<Arguments> provideUntil_calculatesPeriodCorrectly() {
            Symmetry010Date d1 = Symmetry010Date.of(2014, 5, 26);
            return Stream.of(
                    Arguments.of(d1, d1, Symmetry010Chronology.INSTANCE.period(0, 0, 0)),
                    Arguments.of(d1, Symmetry010Date.of(2014, 6, 4), Symmetry010Chronology.INSTANCE.period(0, 0, 9)),
                    Arguments.of(d1, Symmetry010Date.of(2014, 6, 26), Symmetry010Chronology.INSTANCE.period(0, 1, 0)),
                    Arguments.of(d1, Symmetry010Date.of(2015, 5, 26), Symmetry010Chronology.INSTANCE.period(1, 0, 0)),
                    Arguments.of(d1, Symmetry010Date.of(2015, 6, 27), Symmetry010Chronology.INSTANCE.period(1, 1, 1))
            );
        }
    }

    @Nested
    @DisplayName("Chronology API Tests")
    class ChronologyApiTests {
        @ParameterizedTest
        @MethodSource("provideUnsupportedEras")
        @DisplayName("prolepticYear should throw ClassCastException for unsupported eras")
        void prolepticYear_withUnsupportedEra_throwsException(Era unsupportedEra) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(unsupportedEra, 4));
        }

        static Stream<Era> provideUnsupportedEras() {
            // A representative sample of unsupported eras
            return Stream.of(HijrahEra.AH, JapaneseEra.HEISEI, MinguoEra.ROC, ThaiBuddhistEra.BE);
        }

        @ParameterizedTest(name = "chronology.range({0}) should be {1}")
        @MethodSource
        void chronologyRange_returnsCorrectValueRangeForField(TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry010Chronology.INSTANCE.range(field));
        }

        static Stream<Arguments> provideChronologyRange_returnsCorrectValueRangeForField() {
            return Stream.of(
                    Arguments.of(DAY_OF_WEEK, ValueRange.of(1, 7)),
                    Arguments.of(DAY_OF_MONTH, ValueRange.of(1, 30, 37)),
                    Arguments.of(DAY_OF_YEAR, ValueRange.of(1, 364, 371)),
                    Arguments.of(MONTH_OF_YEAR, ValueRange.of(1, 12)),
                    Arguments.of(YEAR, ValueRange.of(-1_000_000L, 1_000_000)),
                    Arguments.of(ERA, ValueRange.of(0, 1))
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("period between a date and its equivalent in another chronology should be zero")
        void period_betweenEquivalentDates_isZero(Symmetry010Date sym010Date, LocalDate isoDate) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010Date.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(sym010Date));
        }
    }

    @Nested
    @DisplayName("Object Method Tests")
    class ObjectMethodTests {
        @ParameterizedTest
        @MethodSource
        void toString_returnsCorrectFormatting(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }

        static Stream<Arguments> provideToString_returnsCorrectFormatting() {
            return Stream.of(
                    Arguments.of(Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"),
                    Arguments.of(Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"),
                    Arguments.of(Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37")
            );
        }
    }
}