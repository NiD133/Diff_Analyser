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
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.testing.EqualsTester;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Month;
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

@DisplayName("Symmetry454Date and Symmetry454Chronology Tests")
public class Symmetry454DateAndChronologyTest {

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleSymmetry454AndIsoDates() {
        return Stream.of(
            Arguments.of(Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry454Date.of(742, 4, 2), LocalDate.of(742, 4, 7)),
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

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion and Equivalence Tests")
    class ConversionAndEquivalenceTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateAndChronologyTest#sampleSymmetry454AndIsoDates")
        @DisplayName("Conversion from Symmetry454Date to LocalDate is correct")
        void conversionFromSymmetry454DateToLocalDateIsCorrect(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateAndChronologyTest#sampleSymmetry454AndIsoDates")
        @DisplayName("Conversion from LocalDate to Symmetry454Date is correct")
        void conversionFromLocalDateToSymmetry454DateIsCorrect(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateAndChronologyTest#sampleSymmetry454AndIsoDates")
        @DisplayName("toEpochDay returns the correct value")
        void toEpochDayIsCorrect(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symmetryDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateAndChronologyTest#sampleSymmetry454AndIsoDates")
        @DisplayName("until a date returns a zero period for the same date")
        void untilSameDateReturnsZeroPeriod(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), symmetryDate.until(symmetryDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateAndChronologyTest#sampleSymmetry454AndIsoDates")
        @DisplayName("until an equivalent ISO date returns a zero period")
        void untilEquivalentIsoDateReturnsZeroPeriod(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), symmetryDate.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateAndChronologyTest#sampleSymmetry454AndIsoDates")
        @DisplayName("ISO date until an equivalent Symmetry454Date returns a zero period")
        void isoDateUntilEquivalentSymmetry454DateReturnsZeroPeriod(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(symmetryDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateAndChronologyTest#sampleSymmetry454AndIsoDates")
        @DisplayName("plus(days) should maintain equivalence with ISO date")
        void plusDaysMaintainsEquivalenceWithIso(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertAll(
                () -> assertEquals(isoDate, LocalDate.from(symmetryDate.plus(0, DAYS))),
                () -> assertEquals(isoDate.plusDays(1), LocalDate.from(symmetryDate.plus(1, DAYS))),
                () -> assertEquals(isoDate.plusDays(35), LocalDate.from(symmetryDate.plus(35, DAYS))),
                () -> assertEquals(isoDate.plusDays(-1), LocalDate.from(symmetryDate.plus(-1, DAYS))),
                () -> assertEquals(isoDate.plusDays(-60), LocalDate.from(symmetryDate.plus(-60, DAYS)))
            );
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateAndChronologyTest#sampleSymmetry454AndIsoDates")
        @DisplayName("minus(days) should maintain equivalence with ISO date")
        void minusDaysMaintainsEquivalenceWithIso(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertAll(
                () -> assertEquals(isoDate, LocalDate.from(symmetryDate.minus(0, DAYS))),
                () -> assertEquals(isoDate.minusDays(1), LocalDate.from(symmetryDate.minus(1, DAYS))),
                () -> assertEquals(isoDate.minusDays(35), LocalDate.from(symmetryDate.minus(35, DAYS))),
                () -> assertEquals(isoDate.minusDays(-1), LocalDate.from(symmetryDate.minus(-1, DAYS))),
                () -> assertEquals(isoDate.minusDays(-60), LocalDate.from(symmetryDate.minus(-60, DAYS)))
            );
        }
    }

    //-----------------------------------------------------------------------
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

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("invalidDateComponents")
        @DisplayName("of() with invalid date components should throw DateTimeException")
        void ofWithInvalidDatePartsThrowsException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dayOfMonth));
        }

        static Stream<Arguments> yearsWithInvalidLeapDay() {
            return Stream.of(
                Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(2000)
            );
        }

        @ParameterizedTest(name = "of({0}, 12, 29)")
        @MethodSource("yearsWithInvalidLeapDay")
        @DisplayName("of() with a leap day in a non-leap year should throw DateTimeException")
        void ofWithInvalidLeapDayThrowsException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field Accessor Tests")
    class FieldAccessorTests {
        private static final int SHORT_MONTH_DAYS = 28;
        private static final int LONG_MONTH_DAYS = 35;
        private static final int SHORT_MONTH_WEEKS = 4;
        private static final int LONG_MONTH_WEEKS = 5;

        // Day counts for months in a common year
        private static final int JAN_DAYS = SHORT_MONTH_DAYS;
        private static final int FEB_DAYS = LONG_MONTH_DAYS;
        private static final int MAR_DAYS = SHORT_MONTH_DAYS;
        private static final int APR_DAYS = SHORT_MONTH_DAYS;
        private static final int MAY_DAYS = LONG_MONTH_DAYS;

        static Stream<Arguments> getLongData() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5L),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, (long) JAN_DAYS + FEB_DAYS + MAR_DAYS + APR_DAYS + 26),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, (long) SHORT_MONTH_WEEKS * 4 + 4),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014L * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),
                Arguments.of(1, 5, 8, ERA, 1L),
                Arguments.of(2015, 12, 35, DAY_OF_WEEK, 7L),
                Arguments.of(2015, 12, 35, DAY_OF_MONTH, 35L),
                Arguments.of(2015, 12, 35, DAY_OF_YEAR, 371L),
                Arguments.of(2015, 12, 35, ALIGNED_DAY_OF_WEEK_IN_MONTH, 7L),
                Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_MONTH, 5L),
                Arguments.of(2015, 12, 35, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7L),
                Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53L),
                Arguments.of(2015, 12, 35, MONTH_OF_YEAR, 12L),
                Arguments.of(2015, 12, 35, PROLEPTIC_MONTH, 2016L * 12 - 1)
            );
        }

        @ParameterizedTest(name = "{3} of {0}-{1}-{2} is {4}")
        @MethodSource("getLongData")
        @DisplayName("getLong() for a field returns the correct value")
        void getLongForFieldReturnsCorrectValue(int year, int month, int dayOfMonth, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, dayOfMonth).getLong(field));
        }

        static Stream<Arguments> rangeData() {
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
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53))
            );
        }

        @ParameterizedTest(name = "range of {3} for {0}-{1}-{2} is {4}")
        @MethodSource("rangeData")
        @DisplayName("range() for a field returns the correct value range")
        void rangeForFieldIsCorrect(int year, int month, int dayOfMonth, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(year, month, dayOfMonth).range(field));
        }

        static Stream<Arguments> lengthOfMonthData() {
            return Stream.of(
                Arguments.of(2000, 1, 28, 28), Arguments.of(2000, 2, 28, 35),
                Arguments.of(2000, 3, 28, 28), Arguments.of(2000, 4, 28, 28),
                Arguments.of(2000, 5, 28, 35), Arguments.of(2000, 6, 28, 28),
                Arguments.of(2000, 7, 28, 28), Arguments.of(2000, 8, 28, 35),
                Arguments.of(2000, 9, 28, 28), Arguments.of(2000, 10, 28, 28),
                Arguments.of(2000, 11, 28, 35), Arguments.of(2000, 12, 28, 28),
                Arguments.of(2004, 12, 20, 35) // Leap year with long December
            );
        }

        @ParameterizedTest(name = "Year {0}, Month {1} -> {3} days")
        @MethodSource("lengthOfMonthData")
        @DisplayName("lengthOfMonth() returns the correct number of days")
        void lengthOfMonthIsCorrect(int year, int month, int dayOfMonth, int expectedLength) {
            Symmetry454Date date = Symmetry454Date.of(year, month, dayOfMonth);
            assertEquals(expectedLength, date.lengthOfMonth());
        }

        @Test
        @DisplayName("isLeapWeek() correctly identifies days in the leap week")
        void isLeapWeekIdentifiesLeapWeekDays() {
            assertAll(
                () -> assertTrue(Symmetry454Date.of(2015, 12, 29).isLeapWeek()),
                () -> assertTrue(Symmetry454Date.of(2015, 12, 30).isLeapWeek()),
                () -> assertTrue(Symmetry454Date.of(2015, 12, 31).isLeapWeek()),
                () -> assertTrue(Symmetry454Date.of(2015, 12, 32).isLeapWeek()),
                () -> assertTrue(Symmetry454Date.of(2015, 12, 33).isLeapWeek()),
                () -> assertTrue(Symmetry454Date.of(2015, 12, 34).isLeapWeek()),
                () -> assertTrue(Symmetry454Date.of(2015, 12, 35).isLeapWeek())
            );
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Manipulation Tests")
    class ManipulationTests {

        static Stream<Arguments> withFieldData() {
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

        @ParameterizedTest(name = "with({3}, {4}) on {0}-{1}-{2}")
        @MethodSource("withFieldData")
        @DisplayName("with(field, value) returns the correctly adjusted date")
        void withFieldValueReturnsCorrectDate(int y, int m, int d, TemporalField f, long val, int ey, int em, int ed) {
            Symmetry454Date base = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, base.with(f, val));
        }

        static Stream<Arguments> withInvalidFieldData() {
            return Stream.of(
                Arguments.of(2013, 1, 1, DAY_OF_MONTH, 29),
                Arguments.of(2013, 1, 1, DAY_OF_YEAR, 365),
                Arguments.of(2015, 1, 1, DAY_OF_YEAR, 372),
                Arguments.of(2013, 1, 1, ALIGNED_WEEK_OF_MONTH, 5),
                Arguments.of(2013, 1, 1, ALIGNED_WEEK_OF_YEAR, 53),
                Arguments.of(2013, 1, 1, YEAR, 1_000_001)
            );
        }

        @ParameterizedTest(name = "with({3}, {4}) on {0}-{1}-{2}")
        @MethodSource("withInvalidFieldData")
        @DisplayName("with(field, value) with an invalid value throws DateTimeException")
        void withInvalidFieldValueThrowsException(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom).with(field, value));
        }

        static Stream<Arguments> plusData() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 5, 34),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 12),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                Arguments.of(2015, 12, 28, 8, DAYS, 2016, 1, 1) // Across leap week
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4}")
        @MethodSource("plusData")
        @DisplayName("plus(amount, unit) returns the correctly adjusted date")
        void plusAmountOfUnitReturnsCorrectDate(int y, int m, int d, long amt, TemporalUnit u, int ey, int em, int ed) {
            assertEquals(Symmetry454Date.of(ey, em, ed), Symmetry454Date.of(y, m, d).plus(amt, u));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4}")
        @MethodSource("plusData") // Re-using plusData for minus tests
        @DisplayName("minus(amount, unit) returns the correctly adjusted date")
        void minusAmountOfUnitReturnsCorrectDate(int ey, int em, int ed, long amt, TemporalUnit u, int y, int m, int d) {
            assertEquals(Symmetry454Date.of(ey, em, ed), Symmetry454Date.of(y, m, d).minus(amt, u));
        }

        static Stream<Arguments> untilData() {
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

        @ParameterizedTest(name = "until({2}-{3}-{4}) in {5} is {6}")
        @MethodSource("untilData")
        @DisplayName("until(endDate, unit) returns the correct duration")
        void untilInUnitIsCorrect(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodData() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 13),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                Arguments.of(2014, 5, 26, 2024, 5, 25, 9, 11, 27)
            );
        }

        @ParameterizedTest(name = "period from {0}-{1}-{2} to {3}-{4}-{5}")
        @MethodSource("untilPeriodData")
        @DisplayName("until(endDate) returns the correct ChronoPeriod")
        void untilReturnsCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }

        static Stream<Arguments> lastDayOfMonthData() {
            return Stream.of(
                Arguments.of(2012, 1, 23, 2012, 1, 28),
                Arguments.of(2012, 2, 23, 2012, 2, 35),
                Arguments.of(2009, 12, 23, 2009, 12, 35)
            );
        }

        @ParameterizedTest(name = "lastDayOfMonth for {0}-{1}-{2}")
        @MethodSource("lastDayOfMonthData")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) returns correct date")
        void withLastDayOfMonthAdjusterReturnsCorrectDate(int y, int m, int d, int ey, int em, int ed) {
            Symmetry454Date base = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Chronology API Tests")
    class ChronologyApiTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateAndChronologyTest#sampleSymmetry454AndIsoDates")
        @DisplayName("chronology.date(epochDay) is correct")
        void chronologyDateFromEpochDayIsCorrect(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateAndChronologyTest#sampleSymmetry454AndIsoDates")
        @DisplayName("chronology.date(TemporalAccessor) is correct")
        void chronologyDateFromTemporalIsCorrect(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Chronology.INSTANCE.date(isoDate));
        }

        static Stream<Arguments> unsupportedErasForProlepticYear() {
            return Stream.of(
                Arguments.of(AccountingEra.BCE), Arguments.of(CopticEra.AM),
                Arguments.of(DiscordianEra.YOLD), Arguments.of(EthiopicEra.INCARNATION),
                Arguments.of(HijrahEra.AH), Arguments.of(InternationalFixedEra.CE),
                Arguments.of(JapaneseEra.HEISEI), Arguments.of(JulianEra.AD),
                Arguments.of(MinguoEra.ROC), Arguments.of(PaxEra.CE),
                Arguments.of(ThaiBuddhistEra.BE)
            );
        }

        @ParameterizedTest
        @MethodSource("unsupportedErasForProlepticYear")
        @DisplayName("prolepticYear() with an invalid Era throws ClassCastException")
        void prolepticYearWithInvalidEraThrowsException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }

        static Stream<Arguments> toStringData() {
            return Stream.of(
                Arguments.of(Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"),
                Arguments.of(Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"),
                Arguments.of(Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35"),
                Arguments.of(Symmetry454Date.of(1970, 12, 35), "Sym454 CE 1970/12/35")
            );
        }

        @ParameterizedTest
        @MethodSource("toStringData")
        @DisplayName("toString() returns the correct formatted string")
        void toStringReturnsCorrectFormatting(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Equals and hashCode contract is met")
        void equalsAndHashCodeContract() {
            new EqualsTester()
                .addEqualityGroup(Symmetry454Date.of(2014, 5, 26), Symmetry454Date.of(2014, 5, 26))
                .addEqualityGroup(Symmetry454Date.of(2014, 5, 27))
                .addEqualityGroup(Symmetry454Date.of(2014, 6, 26))
                .addEqualityGroup(Symmetry454Date.of(2015, 5, 26))
                .testEquals();
        }
    }
}