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

/**
 * Tests for the {@link Symmetry454Chronology} and {@link Symmetry454Date}.
 * This class covers conversions, date properties, arithmetic, and exception cases.
 */
public class Symmetry454ChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides pairs of equivalent dates in Symmetry454 and ISO calendar systems.
     */
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

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("ISO/Symmetry454 Date Equivalence and Conversion")
    class ConversionAndEquivalenceTests {

        @ParameterizedTest(name = "{index}: {0} <=> {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void testDateEquivalence(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertAll("Equivalence between " + sym454Date + " and " + isoDate,
                () -> assertEquals(isoDate, LocalDate.from(sym454Date)),
                () -> assertEquals(sym454Date, Symmetry454Date.from(isoDate)),
                () -> assertEquals(isoDate.toEpochDay(), sym454Date.toEpochDay()),
                () -> assertEquals(sym454Date, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay())),
                () -> assertEquals(sym454Date, Symmetry454Chronology.INSTANCE.date(isoDate))
            );
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void until_withEquivalentDate_returnsZeroPeriod(Symmetry454Date sym454Date, LocalDate isoDate) {
            ChronoPeriod zeroPeriod = Symmetry454Chronology.INSTANCE.period(0, 0, 0);
            assertAll("until() should return zero for equivalent dates",
                () -> assertEquals(zeroPeriod, sym454Date.until(sym454Date)),
                () -> assertEquals(zeroPeriod, sym454Date.until(isoDate)),
                () -> assertEquals(Period.ZERO, isoDate.until(sym454Date))
            );
        }
    }

    @Nested
    @DisplayName("Arithmetic Equivalence with ISO")
    class ArithmeticEquivalenceTests {

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void plusDays_shouldBeEquivalentToIsoPlusDays(Symmetry454Date sym454, LocalDate iso) {
            assertAll("plus(DAYS) should match ISO",
                () -> assertEquals(iso, LocalDate.from(sym454.plus(0, DAYS))),
                () -> assertEquals(iso.plusDays(1), LocalDate.from(sym454.plus(1, DAYS))),
                () -> assertEquals(iso.plusDays(35), LocalDate.from(sym454.plus(35, DAYS))),
                () -> assertEquals(iso.plusDays(-1), LocalDate.from(sym454.plus(-1, DAYS))),
                () -> assertEquals(iso.plusDays(-60), LocalDate.from(sym454.plus(-60, DAYS)))
            );
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void minusDays_shouldBeEquivalentToIsoMinusDays(Symmetry454Date sym454, LocalDate iso) {
            assertAll("minus(DAYS) should match ISO",
                () -> assertEquals(iso, LocalDate.from(sym454.minus(0, DAYS))),
                () -> assertEquals(iso.minusDays(1), LocalDate.from(sym454.minus(1, DAYS))),
                () -> assertEquals(iso.minusDays(35), LocalDate.from(sym454.minus(35, DAYS))),
                () -> assertEquals(iso.minusDays(-1), LocalDate.from(sym454.minus(-1, DAYS))),
                () -> assertEquals(iso.minusDays(-60), LocalDate.from(sym454.minus(-60, DAYS)))
            );
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void until_days_shouldBeEquivalentToIsoUntil(Symmetry454Date sym454, LocalDate iso) {
            assertAll("until(DAYS) should match ISO",
                () -> assertEquals(0, sym454.until(iso.plusDays(0), DAYS)),
                () -> assertEquals(1, sym454.until(iso.plusDays(1), DAYS)),
                () -> assertEquals(35, sym454.until(iso.plusDays(35), DAYS)),
                () -> assertEquals(-40, sym454.until(iso.minusDays(40), DAYS))
            );
        }
    }

    @Nested
    @DisplayName("Factory method Symmetry454Date.of()")
    class FactoryOfTests {
        static Stream<Arguments> invalidDateProvider() {
            return Stream.of(
                Arguments.of(-1, 13, 28), Arguments.of(2000, -2, 1), Arguments.of(2000, 13, 1),
                Arguments.of(2000, 1, -1), Arguments.of(2000, 1, 0), Arguments.of(2000, 0, 1),
                Arguments.of(2000, 1, 29), // Jan has 28 days
                Arguments.of(2000, 2, 36), // Feb has 35 days
                Arguments.of(2000, 3, 29), // Mar has 28 days
                Arguments.of(2004, 12, 36) // Dec has 35 days in a leap year
            );
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("invalidDateProvider")
        void of_withInvalidDateParts_throwsException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom));
        }

        static Stream<Arguments> invalidLeapDayProvider() {
            // These years are not leap years in the Symmetry454 calendar
            return Stream.of(Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(2000));
        }

        @ParameterizedTest(name = "of({0}, 12, 29)")
        @MethodSource("invalidLeapDayProvider")
        void of_onNonLeapYear_withLeapDay_throwsException(int year) {
            // Month 12 only has more than 28 days in a leap year.
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    @DisplayName("Date Properties")
    class DatePropertyTests {

        static Stream<Arguments> lengthOfMonthProvider() {
            return Stream.of(
                Arguments.of(2000, 1, 28), Arguments.of(2000, 2, 35), Arguments.of(2000, 3, 28),
                Arguments.of(2000, 4, 28), Arguments.of(2000, 5, 35), Arguments.of(2000, 6, 28),
                Arguments.of(2000, 7, 28), Arguments.of(2000, 8, 35), Arguments.of(2000, 9, 28),
                Arguments.of(2000, 10, 28), Arguments.of(2000, 11, 35), Arguments.of(2000, 12, 28),
                Arguments.of(2004, 12, 35) // Leap year
            );
        }

        @ParameterizedTest(name = "Year {0}, Month {1} -> {2} days")
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth_shouldReturnCorrectDayCount(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, 1).lengthOfMonth());
        }

        @Test
        void range_forVariousFields_isCorrect() {
            Symmetry454Date commonYearDate = Symmetry454Date.of(2012, 1, 23);
            Symmetry454Date leapYearDate = Symmetry454Date.of(2015, 1, 23);

            assertAll("Field ranges",
                // Month-dependent ranges
                () -> assertEquals(ValueRange.of(1, 28), Symmetry454Date.of(2012, 1, 1).range(DAY_OF_MONTH)),
                () -> assertEquals(ValueRange.of(1, 35), Symmetry454Date.of(2012, 2, 1).range(DAY_OF_MONTH)),
                () -> assertEquals(ValueRange.of(1, 35), Symmetry454Date.of(2015, 12, 1).range(DAY_OF_MONTH)), // Leap year December
                () -> assertEquals(ValueRange.of(1, 4), Symmetry454Date.of(2012, 1, 1).range(ALIGNED_WEEK_OF_MONTH)),
                () -> assertEquals(ValueRange.of(1, 5), Symmetry454Date.of(2012, 2, 1).range(ALIGNED_WEEK_OF_MONTH)),

                // Year-dependent ranges
                () -> assertEquals(ValueRange.of(1, 364), commonYearDate.range(DAY_OF_YEAR)),
                () -> assertEquals(ValueRange.of(1, 371), leapYearDate.range(DAY_OF_YEAR)),
                () -> assertEquals(ValueRange.of(1, 52), commonYearDate.range(ALIGNED_WEEK_OF_YEAR)),
                () -> assertEquals(ValueRange.of(1, 53), leapYearDate.range(ALIGNED_WEEK_OF_YEAR)),

                // Constant ranges
                () -> assertEquals(ValueRange.of(1, 7), commonYearDate.range(DAY_OF_WEEK)),
                () -> assertEquals(ValueRange.of(1, 7), commonYearDate.range(ALIGNED_DAY_OF_WEEK_IN_MONTH)),
                () -> assertEquals(ValueRange.of(1, 7), commonYearDate.range(ALIGNED_DAY_OF_WEEK_IN_YEAR)),
                () -> assertEquals(ValueRange.of(1, 12), commonYearDate.range(MONTH_OF_YEAR))
            );
        }

        @Test
        void getLong_forVariousFields_returnsCorrectValues() {
            Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
            assertAll("getLong() on " + date,
                () -> assertEquals(5, date.getLong(DAY_OF_WEEK)),
                () -> assertEquals(26, date.getLong(DAY_OF_MONTH)),
                // DAY_OF_YEAR: Jan(28) + Feb(35) + Mar(28) + Apr(28) + 26 = 145
                () -> assertEquals(145, date.getLong(DAY_OF_YEAR)),
                () -> assertEquals(5, date.getLong(MONTH_OF_YEAR)),
                () -> assertEquals(2014 * 12 + 4, date.getLong(PROLEPTIC_MONTH)),
                () -> assertEquals(2014, date.getLong(YEAR)),
                () -> assertEquals(1, date.getLong(ERA))
            );
        }

        @Test
        void getLong_forWeekBasedFields_returnsCorrectValues() {
            Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
            assertAll("getLong() for week fields on " + date,
                () -> assertEquals(5, date.getLong(ALIGNED_DAY_OF_WEEK_IN_MONTH)),
                () -> assertEquals(4, date.getLong(ALIGNED_WEEK_OF_MONTH)),
                () -> assertEquals(5, date.getLong(ALIGNED_DAY_OF_WEEK_IN_YEAR)),
                // ALIGNED_WEEK_OF_YEAR: Jan(4) + Feb(5) + Mar(4) + Apr(4) + 4th week in May = 21
                () -> assertEquals(21, date.getLong(ALIGNED_WEEK_OF_YEAR))
            );
        }

        @Test
        void getLong_forLeapYearEndDate_returnsCorrectValues() {
            // 2015 is a leap year in Symmetry454
            Symmetry454Date date = Symmetry454Date.of(2015, 12, 35);
            assertAll("getLong() on leap year end date " + date,
                () -> assertEquals(7, date.getLong(DAY_OF_WEEK)),
                () -> assertEquals(35, date.getLong(DAY_OF_MONTH)),
                () -> assertEquals(371, date.getLong(DAY_OF_YEAR)),
                () -> assertEquals(7, date.getLong(ALIGNED_DAY_OF_WEEK_IN_MONTH)),
                () -> assertEquals(5, date.getLong(ALIGNED_WEEK_OF_MONTH)),
                () -> assertEquals(7, date.getLong(ALIGNED_DAY_OF_WEEK_IN_YEAR)),
                () -> assertEquals(53, date.getLong(ALIGNED_WEEK_OF_YEAR)),
                () -> assertEquals(12, date.getLong(MONTH_OF_YEAR))
            );
        }
    }

    @Nested
    @DisplayName("Symmetry454Date.with()")
    class WithTests {

        @Test
        void with_temporalField_returnsCorrectlyAdjustedDate() {
            Symmetry454Date base = Symmetry454Date.of(2014, 5, 26);
            assertAll("with(field, value) on " + base,
                () -> assertEquals(Symmetry454Date.of(2014, 5, 22), base.with(DAY_OF_WEEK, 1)),
                () -> assertEquals(Symmetry454Date.of(2014, 5, 28), base.with(DAY_OF_MONTH, 28)),
                () -> assertEquals(Symmetry454Date.of(2014, 12, 28), base.with(DAY_OF_YEAR, 364)),
                () -> assertEquals(Symmetry454Date.of(2014, 5, 5), base.with(ALIGNED_WEEK_OF_MONTH, 1)),
                () -> assertEquals(Symmetry454Date.of(2014, 6, 19), base.with(ALIGNED_WEEK_OF_YEAR, 23)),
                () -> assertEquals(Symmetry454Date.of(2014, 4, 26), base.with(MONTH_OF_YEAR, 4)),
                () -> assertEquals(Symmetry454Date.of(2013, 3, 26), base.with(PROLEPTIC_MONTH, 2013 * 12 + 2)),
                () -> assertEquals(Symmetry454Date.of(2012, 5, 26), base.with(YEAR, 2012)),
                () -> assertEquals(Symmetry454Date.of(2012, 5, 26), base.with(YEAR_OF_ERA, 2012))
            );
        }

        static Stream<Arguments> invalidWithFieldValuesProvider() {
            return Stream.of(
                Arguments.of(DAY_OF_WEEK, 8), Arguments.of(DAY_OF_MONTH, 29), // Jan has 28 days
                Arguments.of(DAY_OF_YEAR, 365), // Common year has 364 days
                Arguments.of(ALIGNED_WEEK_OF_MONTH, 5), // Jan has 4 weeks
                Arguments.of(ALIGNED_WEEK_OF_YEAR, 53), // Common year has 52 weeks
                Arguments.of(MONTH_OF_YEAR, 13), Arguments.of(YEAR, 1_000_001)
            );
        }

        @ParameterizedTest(name = "with({0}, {1})")
        @MethodSource("invalidWithFieldValuesProvider")
        void with_invalidFieldValue_throwsException(TemporalField field, long value) {
            Symmetry454Date date = Symmetry454Date.of(2013, 1, 1);
            assertThrows(DateTimeException.class, () -> date.with(field, value));
        }

        @Test
        void with_lastDayOfMonthAdjuster_returnsMonthEnd() {
            assertAll("with(lastDayOfMonth())",
                () -> assertEquals(Symmetry454Date.of(2012, 1, 28), Symmetry454Date.of(2012, 1, 23).with(TemporalAdjusters.lastDayOfMonth())),
                () -> assertEquals(Symmetry454Date.of(2012, 2, 35), Symmetry454Date.of(2012, 2, 23).with(TemporalAdjusters.lastDayOfMonth())),
                () -> assertEquals(Symmetry454Date.of(2009, 12, 35), Symmetry454Date.of(2009, 12, 23).with(TemporalAdjusters.lastDayOfMonth()))
            );
        }
    }

    @Nested
    @DisplayName("Date Arithmetic")
    class ArithmeticTests {

        static Stream<Arguments> plusProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 5, 34),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 12),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                // Across year boundary
                Arguments.of(2014, 12, 26, 3, WEEKS, 2015, 1, 19),
                // Across leap week
                Arguments.of(2015, 12, 28, 8, DAYS, 2016, 1, 1)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4}")
        @MethodSource("plusProvider")
        void plus_withVariousUnits_returnsCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4}")
        @MethodSource("plusProvider")
        void minus_withVariousUnits_returnsCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date expected = Symmetry454Date.of(y, m, d);
            Symmetry454Date start = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Stream<Arguments> untilUnitProvider() {
            return Stream.of(
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

        @ParameterizedTest(name = "from {0}-{1}-{2} to {3}-{4}-{5} in {6}")
        @MethodSource("untilUnitProvider")
        void until_withVariousUnits_returnsCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 13),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                Arguments.of(2014, 5, 26, 2024, 5, 25, 9, 11, 27)
            );
        }

        @ParameterizedTest(name = "from {0}-{1}-{2} to {3}-{4}-{5}")
        @MethodSource("untilPeriodProvider")
        void until_withEndDate_returnsCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology-specific methods")
    class ChronologyMethodTests {
        static Stream<Era> nonSymmetryErasProvider() {
            return Stream.of(
                JapaneseEra.MEIJI,
                MinguoEra.ROC,
                ThaiBuddhistEra.BE,
                HijrahEra.AH
            );
        }

        @ParameterizedTest
        @MethodSource("nonSymmetryErasProvider")
        void prolepticYear_withMismatchedEra_throwsClassCastException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("String Representation")
    class ToStringTests {
        static Stream<Arguments> toStringProvider() {
            return Stream.of(
                Arguments.of(Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"),
                Arguments.of(Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"),
                Arguments.of(Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35")
            );
        }

        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("toStringProvider")
        void toString_shouldReturnCorrectFormatting(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}