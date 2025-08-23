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
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Symmetry010Chronology}.
 * <p>
 * This test suite is organized into nested classes to group related tests,
 * such as factory methods, date queries, and date modifications.
 */
@DisplayName("Symmetry010Chronology")
class Symmetry010ChronologyTest {

    //-----------------------------------------------------------------------
    // Conversion and Factory Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion and Factory")
    class ConversionAndFactoryTests {

        static Stream<Arguments> sampleSymmetryAndIsoDates() {
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

        @ParameterizedTest
        @MethodSource("sampleSymmetryAndIsoDates")
        void fromLocalDate_shouldBeCorrect(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Date.from(iso));
        }

        @ParameterizedTest
        @MethodSource("sampleSymmetryAndIsoDates")
        void toLocalDate_shouldBeCorrect(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym010));
        }

        @ParameterizedTest
        @MethodSource("sampleSymmetryAndIsoDates")
        void toEpochDay_shouldBeCorrect(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym010.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("sampleSymmetryAndIsoDates")
        void dateFromEpochDay_shouldBeCorrect(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("sampleSymmetryAndIsoDates")
        void dateFromTemporal_shouldBeCorrect(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.date(iso));
        }

        static Stream<Arguments> invalidDateProvider() {
            return Stream.of(
                Arguments.of(-1, 13, 28), Arguments.of(-1, 13, 29),
                Arguments.of(2000, -2, 1), Arguments.of(2000, 13, 1), Arguments.of(2000, 15, 1),
                Arguments.of(2000, 1, -1), Arguments.of(2000, 1, 0), Arguments.of(2000, 0, 1),
                Arguments.of(2000, -1, 0), Arguments.of(2000, -1, 1),
                Arguments.of(2000, 1, 31), // Jan has 30 days
                Arguments.of(2000, 2, 32), // Feb has 31 days
                Arguments.of(2000, 3, 31), // Mar has 30 days
                Arguments.of(2000, 4, 31), // Apr has 30 days
                Arguments.of(2000, 5, 32), // May has 31 days
                Arguments.of(2000, 6, 31), // Jun has 30 days
                Arguments.of(2000, 7, 31), // Jul has 30 days
                Arguments.of(2000, 8, 32), // Aug has 31 days
                Arguments.of(2000, 9, 31), // Sep has 30 days
                Arguments.of(2000, 10, 31), // Oct has 30 days
                Arguments.of(2000, 11, 32), // Nov has 31 days
                Arguments.of(2000, 12, 31), // Dec has 30 days in non-leap year
                Arguments.of(2004, 12, 38)  // Dec has 37 days in leap year
            );
        }

        @ParameterizedTest
        @MethodSource("invalidDateProvider")
        void of_shouldThrowException_forInvalidDateParts(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dom));
        }

        static Stream<Arguments> nonLeapYearsProvider() {
            return Stream.of(
                Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(2000)
            );
        }

        @ParameterizedTest
        @MethodSource("nonLeapYearsProvider")
        void of_shouldThrowException_forLeapDayInNonLeapYear(int year) {
            // Day 37 of month 12 only exists in a leap year
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    //-----------------------------------------------------------------------
    // Query Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date component queries")
    class QueryTests {

        static Stream<Arguments> lengthOfMonthProvider() {
            return Stream.of(
                Arguments.of(2000, 1, 28, 30), Arguments.of(2000, 2, 28, 31),
                Arguments.of(2000, 3, 28, 30), Arguments.of(2000, 4, 28, 30),
                Arguments.of(2000, 5, 28, 31), Arguments.of(2000, 6, 28, 30),
                Arguments.of(2000, 7, 28, 30), Arguments.of(2000, 8, 28, 31),
                Arguments.of(2000, 9, 28, 30), Arguments.of(2000, 10, 28, 30),
                Arguments.of(2000, 11, 28, 31), Arguments.of(2000, 12, 28, 30),
                Arguments.of(2004, 12, 20, 37) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, day).lengthOfMonth());
        }

        static Stream<Arguments> dateFieldRangeProvider() {
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)),
                Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)), // Leap year
                Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)),
                Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)), // Leap year
                Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
                Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)), // Feb has 31 days, but weeks are aligned differently
                Arguments.of(2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)), // Leap week
                Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)) // Leap week
            );
        }

        @ParameterizedTest
        @MethodSource("dateFieldRangeProvider")
        void range_shouldReturnCorrectRange_forField(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, Symmetry010Date.of(year, month, dom).range(field));
        }

        static Stream<Arguments> dateFieldGetProvider() {
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
                Arguments.of(2015, 12, 37, DAY_OF_WEEK, 5L), // Leap day
                Arguments.of(2015, 12, 37, DAY_OF_MONTH, 37L),
                Arguments.of(2015, 12, 37, DAY_OF_YEAR, 371L), // 4*(4+5+4)*7 + 7
                Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_MONTH, 6L),
                Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53L)
            );
        }

        @ParameterizedTest
        @MethodSource("dateFieldGetProvider")
        void getLong_shouldReturnCorrectValue_forField(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry010Date.of(year, month, dom).getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    // Modification Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date modification")
    class ModificationTests {

        static Stream<Arguments> withFieldProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26),
                Arguments.of(2015, 12, 37, YEAR, 2004, 2004, 12, 37), // from leap to leap
                Arguments.of(2015, 12, 37, YEAR, 2013, 2013, 12, 30)  // from leap to non-leap, day adjusted
            );
        }

        @ParameterizedTest
        @MethodSource("withFieldProvider")
        void with_shouldReturnAdjustedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            Symmetry010Date initial = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, initial.with(field, value));
        }

        static Stream<Arguments> withInvalidFieldProvider() {
            return Stream.of(
                Arguments.of(2013, 1, 1, DAY_OF_WEEK, 8),
                Arguments.of(2013, 1, 1, DAY_OF_MONTH, 31),
                Arguments.of(2013, 1, 1, DAY_OF_YEAR, 365), // non-leap year
                Arguments.of(2015, 1, 1, DAY_OF_YEAR, 372), // leap year
                Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14),
                Arguments.of(2013, 1, 1, YEAR, 1_000_001)
            );
        }

        @ParameterizedTest
        @MethodSource("withInvalidFieldProvider")
        void with_shouldThrowException_forInvalidFieldValue(int y, int m, int d, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(y, m, d).with(field, value));
        }

        static Stream<Arguments> lastDayOfMonthProvider() {
            return Stream.of(
                Arguments.of(2012, 1, 23, 2012, 1, 30),
                Arguments.of(2012, 2, 23, 2012, 2, 31),
                Arguments.of(2012, 12, 23, 2012, 12, 30),
                Arguments.of(2009, 12, 23, 2009, 12, 37) // leap year
            );
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthProvider")
        void with_lastDayOfMonth_shouldReturnLastDayOfMonth(int y, int m, int d, int ey, int em, int ed) {
            Symmetry010Date initial = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, initial.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> plusProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26),
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
                Arguments.of(2014, 5, 26, -3, DAYS, 2014, 5, 23),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, -1, MILLENNIA, 1014, 5, 26)
            );
        }

        @ParameterizedTest
        @MethodSource("plusProvider")
        void plus_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry010Date initial = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, initial.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusProvider")
        void minus_shouldReturnCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry010Date initial = Symmetry010Date.of(y, m, d);
            Symmetry010Date end = Symmetry010Date.of(ey, em, ed);
            assertEquals(initial, end.minus(amount, unit));
        }
    }

    //-----------------------------------------------------------------------
    // Period and Until Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Period and Duration")
    class PeriodAndDurationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest$ConversionAndFactoryTests#sampleSymmetryAndIsoDates")
        void until_withSameDate_returnsZero(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(sym010));
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(iso));
            assertEquals(Period.ZERO, iso.until(sym010));
        }

        static Stream<Arguments> untilUnitProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0L),
                Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 9L),
                Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6L),
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
        @MethodSource("untilUnitProvider")
        void until_withUnit_shouldReturnCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodProvider() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 9),
                Arguments.of(2014, 5, 26, 2014, 5, 20, 0, 0, -6),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 25, 0, 11, 29),
                Arguments.of(2014, 5, 26, 2024, 5, 25, 9, 11, 29)
            );
        }

        @ParameterizedTest
        @MethodSource("untilPeriodProvider")
        void until_withDate_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry010Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    //-----------------------------------------------------------------------
    // API Compliance Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("API Compliance")
    class ApiComplianceTests {

        static Stream<Arguments> toStringProvider() {
            return Stream.of(
                Arguments.of(Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"),
                Arguments.of(Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"),
                Arguments.of(Symmetry010Date.of(2000, 8, 31), "Sym010 CE 2000/08/31"),
                Arguments.of(Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37")
            );
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        void toString_shouldReturnCorrectFormatting(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }

        @Test
        void eras_shouldReturnBceAndCe() {
            List<Era> eras = Symmetry010Chronology.INSTANCE.eras();
            assertEquals(2, eras.size());
            assertTrue(eras.contains(IsoEra.BCE));
            assertTrue(eras.contains(IsoEra.CE));
        }

        static Stream<Arguments> invalidEraProvider() {
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
        @MethodSource("invalidEraProvider")
        void prolepticYear_shouldThrowException_forInvalidEra(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }
}