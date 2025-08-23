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
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;
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

@DisplayName("Tests for Symmetry454Date and Symmetry454Chronology")
class Symmetry454DateTest {

    //-----------------------------------------------------------------------
    // Test Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleSymmetry454AndIsoDates() {
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

    static Stream<Arguments> invalidDateProvider() {
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

    static Stream<Arguments> invalidLeapDayProvider() {
        return Stream.of(
            Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(2000)
        );
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory and validation")
    class FactoryAndValidation {

        @ParameterizedTest(name = "year={0}, month={1}, day={2}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#invalidDateProvider")
        @DisplayName("of(year, month, day) throws for invalid values")
        void of_throwsExceptionForInvalidDateParts(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom));
        }

        @ParameterizedTest(name = "year={0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#invalidLeapDayProvider")
        @DisplayName("of(year, 12, 29) throws for non-leap years")
        void of_throwsExceptionForInvalidLeapDay(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    @DisplayName("Conversion")
    class Conversion {

        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetry454AndIsoDates")
        @DisplayName("to LocalDate")
        void toLocalDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym454));
        }

        @ParameterizedTest(name = "{1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetry454AndIsoDates")
        @DisplayName("from LocalDate")
        void fromLocalDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Date.from(iso));
        }

        @ParameterizedTest(name = "{0} -> epoch day {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetry454AndIsoDates")
        @DisplayName("to epoch day")
        void toEpochDay(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym454.toEpochDay());
        }

        @ParameterizedTest(name = "epoch day {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetry454AndIsoDates")
        @DisplayName("from epoch day")
        void fromEpochDay(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }
    }

    @Nested
    @DisplayName("Date component access")
    class Accessors {

        static Stream<Arguments> lengthOfMonthProvider() {
            return Stream.of(
                Arguments.of(2000, 1, 28), Arguments.of(2000, 2, 35), Arguments.of(2000, 3, 28),
                Arguments.of(2000, 4, 28), Arguments.of(2000, 5, 35), Arguments.of(2000, 6, 28),
                Arguments.of(2000, 7, 28), Arguments.of(2000, 8, 35), Arguments.of(2000, 9, 28),
                Arguments.of(2000, 10, 28), Arguments.of(2000, 11, 35), Arguments.of(2000, 12, 28),
                Arguments.of(2004, 12, 35) // Leap year December
            );
        }

        @ParameterizedTest(name = "{0}-{1} has {2} days")
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth_isCorrect(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> rangeProvider() {
            return Stream.of(
                Arguments.of(2012, 1, DAY_OF_MONTH, ValueRange.of(1, 28)),
                Arguments.of(2012, 2, DAY_OF_MONTH, ValueRange.of(1, 35)),
                Arguments.of(2015, 12, DAY_OF_MONTH, ValueRange.of(1, 35)), // Leap year
                Arguments.of(2012, 1, DAY_OF_WEEK, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, DAY_OF_YEAR, ValueRange.of(1, 364)),
                Arguments.of(2015, 1, DAY_OF_YEAR, ValueRange.of(1, 371)), // Leap year
                Arguments.of(2012, 1, MONTH_OF_YEAR, ValueRange.of(1, 12)),
                Arguments.of(2012, 1, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(2012, 2, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
                Arguments.of(2012, 1, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                Arguments.of(2015, 12, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)) // Leap year
            );
        }

        @ParameterizedTest(name = "range of {2} for {0}-{1} is {3}")
        @MethodSource("rangeProvider")
        void range_isCorrect(int year, int month, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(year, month, 1).range(field));
        }

        static Stream<Arguments> getLongProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5L),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                // Day of year for 2014-05-26: days in Jan-Apr (28+35+28+28) + 26
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 147L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
                // Week of year for 2014-05-26: weeks in Jan-Apr (4+5+4+4) + 4
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014L * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),
                // Leap year date
                Arguments.of(2015, 12, 35, DAY_OF_WEEK, 7L),
                Arguments.of(2015, 12, 35, DAY_OF_MONTH, 35L),
                // Day of year for 2015-12-35 (leap day): 364 (normal year) + 7
                Arguments.of(2015, 12, 35, DAY_OF_YEAR, 371L),
                Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53L)
            );
        }

        @ParameterizedTest(name = "{3} of {0}-{1}-{2} is {4}")
        @MethodSource("getLongProvider")
        void getLong_returnsCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date manipulation")
    class Manipulation {

        static Stream<Arguments> withProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29), // Day of month adjusted
                Arguments.of(2015, 3, 28, DAY_OF_YEAR, 371, 2015, 12, 35) // Leap year
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with {3}={4} -> {5}-{6}-{7}")
        @MethodSource("withProvider")
        void with_adjustsDateCorrectly(int y, int m, int d, TemporalField f, long v, int ey, int em, int ed) {
            Symmetry454Date initial = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, initial.with(f, v));
        }

        static Stream<Arguments> withInvalidValueProvider() {
            return Stream.of(
                Arguments.of(DAY_OF_MONTH, 0), Arguments.of(DAY_OF_MONTH, 29), // For a 28-day month
                Arguments.of(DAY_OF_YEAR, 0), Arguments.of(DAY_OF_YEAR, 365), // For a 364-day year
                Arguments.of(MONTH_OF_YEAR, 13),
                Arguments.of(YEAR, 1_000_001)
            );
        }

        @ParameterizedTest(name = "with({0}, {1}) throws exception")
        @MethodSource("withInvalidValueProvider")
        void with_throwsForInvalidValue(TemporalField field, long value) {
            Symmetry454Date date = Symmetry454Date.of(2013, 1, 1);
            assertThrows(DateTimeException.class, () -> date.with(field, value));
        }

        @Test
        void with_throwsForUnsupportedField() {
            Symmetry454Date date = Symmetry454Date.of(2012, 6, 28);
            assertThrows(UnsupportedTemporalTypeException.class, () -> date.with(MINUTE_OF_DAY, 10));
        }

        static Stream<Arguments> lastDayOfMonthProvider() {
            return Stream.of(
                Arguments.of(2012, 1, 23, 2012, 1, 28),
                Arguments.of(2012, 2, 23, 2012, 2, 35),
                Arguments.of(2009, 12, 23, 2009, 12, 35) // Leap year
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} -> {3}-{4}-{5}")
        @MethodSource("lastDayOfMonthProvider")
        void with_lastDayOfMonth_adjustsCorrectly(int y, int m, int d, int ey, int em, int ed) {
            Symmetry454Date initial = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, initial.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> plusProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 5, 34),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 12),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("plusProvider")
        void plus_addsAmount(int y, int m, int d, long amt, TemporalUnit u, int ey, int em, int ed) {
            assertEquals(Symmetry454Date.of(ey, em, ed), Symmetry454Date.of(y, m, d).plus(amt, u));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4} -> {0}-{1}-{2}")
        @MethodSource("plusProvider")
        void minus_subtractsAmount(int y, int m, int d, long amt, TemporalUnit u, int ey, int em, int ed) {
            assertEquals(Symmetry454Date.of(y, m, d), Symmetry454Date.of(ey, em, ed).minus(amt, u));
        }

        static Stream<Arguments> plusLeapWeekProvider() {
            return Stream.of(
                Arguments.of(2015, 12, 28, 8, DAYS, 2016, 1, 1),
                Arguments.of(2015, 12, 28, 3, WEEKS, 2016, 1, 14),
                Arguments.of(2015, 12, 28, 3, MONTHS, 2016, 3, 28),
                Arguments.of(2015, 12, 28, 1, YEARS, 2016, 12, 28)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("plusLeapWeekProvider")
        void plus_handlesLeapWeek(int y, int m, int d, long amt, TemporalUnit u, int ey, int em, int ed) {
            assertEquals(Symmetry454Date.of(ey, em, ed), Symmetry454Date.of(y, m, d).plus(amt, u));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4} -> {0}-{1}-{2}")
        @MethodSource("plusLeapWeekProvider")
        void minus_handlesLeapWeek(int y, int m, int d, long amt, TemporalUnit u, int ey, int em, int ed) {
            assertEquals(Symmetry454Date.of(y, m, d), Symmetry454Date.of(ey, em, ed).minus(amt, u));
        }

        static Stream<Arguments> untilUnitProvider() {
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

        @ParameterizedTest(name = "amount in {6} from {0}-{1}-{2} to {3}-{4}-{5} is {7}")
        @MethodSource("untilUnitProvider")
        void until_calculatesAmountInUnit(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit u, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, u));
        }

        static Stream<Arguments> untilPeriodProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 13),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 25, 0, 11, 27)
            );
        }

        @ParameterizedTest(name = "period from {0}-{1}-{2} to {3}-{4}-{5} is P{6}Y{7}M{8}D")
        @MethodSource("untilPeriodProvider")
        void until_calculatesPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }

        @Test
        @DisplayName("until a date is zero for the same date")
        void until_isZeroForSameDate() {
            Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        @Test
        @DisplayName("until(Temporal) returns zero period for equivalent date in different chronology")
        void until_equivalentDateInAnotherChronology_isZero() {
            Symmetry454Date symDate = Symmetry454Date.of(2014, 5, 26);
            LocalDate isoDate = LocalDate.from(symDate);
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), symDate.until(isoDate));
        }

        @Test
        @DisplayName("LocalDate.until(Symmetry454Date) returns zero period for equivalent date")
        void localDate_until_equivalentSymmetry454Date_isZero() {
            LocalDate isoDate = LocalDate.of(2014, 6, 18);
            Symmetry454Date symDate = Symmetry454Date.from(isoDate);
            assertEquals(Period.ZERO, isoDate.until(symDate));
        }
    }

    @Nested
    @DisplayName("Chronology-specific behavior")
    class ChronologyBehavior {

        @ParameterizedTest(name = "{1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454DateTest#sampleSymmetry454AndIsoDates")
        @DisplayName("chronology.date(TemporalAccessor)")
        void chronology_date_fromTemporal(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Chronology.INSTANCE.date(iso));
        }

        static Stream<Arguments> unsupportedEraProvider() {
            return Stream.of(
                Arguments.of(HijrahEra.AH), Arguments.of(JapaneseEra.HEISEI),
                Arguments.of(MinguoEra.ROC), Arguments.of(ThaiBuddhistEra.BE)
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("unsupportedEraProvider")
        @DisplayName("prolepticYear() throws ClassCastException for unsupported era types")
        void prolepticYear_throwsForUnsupportedEra(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("Object contract")
    class ObjectContract {

        static Stream<Arguments> toStringProvider() {
            return Stream.of(
                Arguments.of(Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"),
                Arguments.of(Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"),
                Arguments.of(Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35"),
                Arguments.of(Symmetry454Date.of(1970, 12, 35), "Sym454 CE 1970/12/35")
            );
        }

        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("toStringProvider")
        void testToString(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}