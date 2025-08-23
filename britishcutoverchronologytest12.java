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
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Tests for BritishCutoverChronology and BritishCutoverDate")
class BritishCutoverChronologyTest {

    // -----------------------------------------------------------------------
    // Data Providers
    // -----------------------------------------------------------------------

    private static Stream<Arguments> sampleCutoverAndIsoDates() {
        return Stream.of(
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)), // leniently accept invalid
            Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)), // leniently accept invalid
            Arguments.of(BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)),
            Arguments.of(BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6))
        );
    }

    // -----------------------------------------------------------------------
    @Nested
    @DisplayName("Factory and Conversion Tests")
    class FactoryAndConversionTests {

        @ParameterizedTest(name = "from {0}, to LocalDate should be {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void from_conversionsBetweenCutoverAndIsoDates_areSymmetric(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest(name = "{0} should have epoch day {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void epochDay_conversions_areCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest(name = "Chronology.date({1}) should be {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void chronologyDate_fromTemporal_isCorrect(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }

        private static Stream<Arguments> invalidDateComponents() {
            return Stream.of(
                Arguments.of(1900, 0, 0), Arguments.of(1900, -1, 1), Arguments.of(1900, 0, 1),
                Arguments.of(1900, 13, 1), Arguments.of(1900, 14, 1), Arguments.of(1900, 1, -1),
                Arguments.of(1900, 1, 0), Arguments.of(1900, 1, 32), Arguments.of(1900, 2, 30),
                Arguments.of(1899, 2, 29), Arguments.of(1900, 4, 31), Arguments.of(1900, 6, 31),
                Arguments.of(1900, 9, 31), Arguments.of(1900, 11, 31)
            );
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("invalidDateComponents")
        void of_withInvalidDate_throwsException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
        }
    }

    // -----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Property Tests")
    class PropertyTests {

        private static Stream<Arguments> monthLengths() {
            return Stream.of(
                Arguments.of(1700, 2, 29), Arguments.of(1751, 2, 28),
                Arguments.of(1752, 1, 31), Arguments.of(1752, 2, 29),
                Arguments.of(1752, 9, 19), Arguments.of(1753, 2, 28),
                Arguments.of(1800, 2, 28), Arguments.of(1900, 2, 28),
                Arguments.of(2000, 2, 29)
            );
        }

        @ParameterizedTest(name = "Year {0}, Month {1} should have {2} days")
        @MethodSource("monthLengths")
        void lengthOfMonth_isCorrect(int year, int month, int length) {
            assertEquals(length, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        private static Stream<Arguments> yearLengths() {
            return Stream.of(
                Arguments.of(1700, 366), Arguments.of(1751, 365),
                Arguments.of(1752, 355), Arguments.of(1753, 365),
                Arguments.of(1800, 365), Arguments.of(1900, 365),
                Arguments.of(2000, 366)
            );
        }

        @ParameterizedTest(name = "Year {0} should have {1} days")
        @MethodSource("yearLengths")
        void lengthOfYear_isCorrect(int year, int length) {
            assertEquals(length, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
            assertEquals(length, BritishCutoverDate.of(year, 12, 31).lengthOfYear());
        }

        private static Stream<Arguments> fieldRanges() {
            return Stream.of(
                Arguments.of(1752, 9, 23, DAY_OF_MONTH, 1, 30),
                Arguments.of(1752, 9, 23, DAY_OF_YEAR, 1, 355),
                Arguments.of(1752, 9, 23, ALIGNED_WEEK_OF_MONTH, 1, 3),
                Arguments.of(1752, 12, 23, ALIGNED_WEEK_OF_YEAR, 1, 51),
                Arguments.of(2011, 2, 23, DAY_OF_MONTH, 1, 28),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, 1, 29),
                Arguments.of(2012, 2, 23, DAY_OF_YEAR, 1, 366)
            );
        }

        @ParameterizedTest(name = "For {0}-{1}-{2}, range of {3} is {4}-{5}")
        @MethodSource("fieldRanges")
        void range_isCorrect(int year, int month, int dom, TemporalField field, long expectedMin, long expectedMax) {
            ValueRange expectedRange = ValueRange.of(expectedMin, expectedMax);
            assertEquals(expectedRange, BritishCutoverDate.of(year, month, dom).range(field));
        }

        private static Stream<Arguments> fieldValues() {
            return Stream.of(
                Arguments.of(1752, 9, 2, DAY_OF_WEEK, 3),
                Arguments.of(1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 2),
                Arguments.of(1752, 9, 14, DAY_OF_WEEK, 4),
                Arguments.of(1752, 9, 14, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3),
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 31 + 28 + 31 + 30 + 26),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(0, 6, 8, ERA, 0),
                Arguments.of(1, 6, 8, ERA, 1)
            );
        }

        @ParameterizedTest(name = "For {0}-{1}-{2}, getLong({3}) is {4}")
        @MethodSource("fieldValues")
        void getLong_returnsCorrectFieldValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, dom).getLong(field));
        }
    }

    // -----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Arithmetic Tests")
    class ArithmeticTests {

        private void assertPlusDaysIsEquivalent(BritishCutoverDate cutover, LocalDate iso, long days) {
            LocalDate expected = iso.plusDays(days);
            assertEquals(expected, LocalDate.from(cutover.plus(days, DAYS)), "Adding " + days + " days");
        }

        @ParameterizedTest(name = "plusDays on {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void plusDays_isEquivalentToIsoDatePlusDays(BritishCutoverDate cutover, LocalDate iso) {
            assertPlusDaysIsEquivalent(cutover, iso, 0);
            assertPlusDaysIsEquivalent(cutover, iso, 1);
            assertPlusDaysIsEquivalent(cutover, iso, 35);
            assertPlusDaysIsEquivalent(cutover, iso, -1);
            assertPlusDaysIsEquivalent(cutover, iso, -60);
        }

        private void assertMinusDaysIsEquivalent(BritishCutoverDate cutover, LocalDate iso, long days) {
            LocalDate expected = iso.minusDays(days);
            assertEquals(expected, LocalDate.from(cutover.minus(days, DAYS)), "Subtracting " + days + " days");
        }

        @ParameterizedTest(name = "minusDays on {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void minusDays_isEquivalentToIsoDateMinusDays(BritishCutoverDate cutover, LocalDate iso) {
            assertMinusDaysIsEquivalent(cutover, iso, 0);
            assertMinusDaysIsEquivalent(cutover, iso, 1);
            assertMinusDaysIsEquivalent(cutover, iso, 35);
            assertMinusDaysIsEquivalent(cutover, iso, -1);
            assertMinusDaysIsEquivalent(cutover, iso, -60);
        }

        private static Stream<Arguments> dateArithmeticCases() {
            return Stream.of(
                Arguments.of(1752, 9, 2, 1L, DAYS, 1752, 9, 14, true),
                Arguments.of(1752, 9, 14, -1L, DAYS, 1752, 9, 2, true),
                Arguments.of(2014, 5, 26, 8L, DAYS, 2014, 6, 3, true),
                Arguments.of(1752, 9, 2, 1L, WEEKS, 1752, 9, 20, true),
                Arguments.of(1752, 9, 14, -1L, WEEKS, 1752, 8, 27, true),
                Arguments.of(1752, 9, 2, 1L, MONTHS, 1752, 10, 2, true),
                Arguments.of(1752, 8, 12, 1L, MONTHS, 1752, 9, 23, false), // not bidirectional
                Arguments.of(2014, 5, 26, 3L, YEARS, 2017, 5, 26, true),
                Arguments.of(2014, 5, 26, 3L, DECADES, 2044, 5, 26, true),
                Arguments.of(2014, 5, 26, 3L, CENTURIES, 2314, 5, 26, true),
                Arguments.of(2014, 5, 26, 3L, MILLENNIA, 5014, 5, 26, true),
                Arguments.of(2014, 5, 26, -1L, ERAS, -2013, 5, 26, true)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("dateArithmeticCases")
        void plus_withTemporalUnit_isCorrect(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed, boolean bidi) {
            BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
            BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{5}-{6}-{7} minus {3} {4} -> {0}-{1}-{2}")
        @MethodSource("dateArithmeticCases")
        void minus_withTemporalUnit_isInverseOfPlus(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed, boolean isBidirectional) {
            Assumptions.assumeTrue(isBidirectional, "Skipping non-bidirectional case for minus test");
            BritishCutoverDate end = BritishCutoverDate.of(ey, em, ed);
            BritishCutoverDate expectedStart = BritishCutoverDate.of(y, m, d);
            assertEquals(expectedStart, end.minus(amount, unit));
        }
    }

    // -----------------------------------------------------------------------
    @Nested
    @DisplayName("`with()` and Adjuster Tests")
    class WithAndAdjusterTests {

        private static Stream<Arguments> withFieldAdjustmentCases() {
            return Stream.of(
                Arguments.of(1752, 9, 2, DAY_OF_WEEK, 4L, 1752, 9, 14),
                // lenient
                Arguments.of(1752, 9, 2, DAY_OF_MONTH, 3L, 1752, 9, 14),
                Arguments.of(1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3, 1752, 9, 14),
                Arguments.of(2014, 5, 26, YEAR, 2012L, 2012, 5, 26),
                Arguments.of(2014, 5, 26, ERA, 0L, -2013, 5, 26),
                Arguments.of(2012, 2, 29, YEAR, 2011L, 2011, 2, 28),
                Arguments.of(2014, 5, 26, WeekFields.ISO.dayOfWeek(), 2L, 2014, 5, 27)
            );
        }

        @ParameterizedTest(name = "from {0}-{1}-{2}, with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("withFieldAdjustmentCases")
        void with_temporalField_adjustsDateCorrectly(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
            BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        @Test
        void with_lastDayOfMonthAdjuster_isCorrect() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 30);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_localDateAdjuster_isCorrect() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            LocalDate adjuster = LocalDate.of(1752, 9, 14);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 14);
            assertEquals(expected, date.with(adjuster));
        }
    }

    // -----------------------------------------------------------------------
    @Nested
    @DisplayName("`until()` Tests")
    class UntilTests {

        @ParameterizedTest(name = "until({1}) for {0} should be zero")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void until_betweenEquivalentCutoverAndIsoDates_isZero(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(cutoverDate));
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
        }

        @ParameterizedTest(name = "until({1}) in DAYS for {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void until_withDaysUnit_isEquivalentToIsoDateUntil(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(0, cutover.until(iso.plusDays(0), DAYS));
            assertEquals(1, cutover.until(iso.plusDays(1), DAYS));
            assertEquals(35, cutover.until(iso.plusDays(35), DAYS));
            assertEquals(-40, cutover.until(iso.minusDays(40), DAYS));
        }

        private static Stream<Arguments> temporalUnitUntilCases() {
            return Stream.of(
                Arguments.of(1752, 9, 1, 1752, 9, 14, DAYS, 2L),
                Arguments.of(1752, 9, 2, 1752, 9, 14, DAYS, 1L),
                Arguments.of(1752, 9, 1, 1752, 9, 19, WEEKS, 1L),
                Arguments.of(1752, 9, 1, 1752, 10, 1, MONTHS, 1L),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
                Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
                Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
                Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1L)
            );
        }

        @ParameterizedTest(name = "from {0}-{1}-{2} until {3}-{4}-{5} in {6} is {7}")
        @MethodSource("temporalUnitUntilCases")
        void until_withTemporalUnit_calculatesAmountCorrectly(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        private static Stream<Arguments> chronoPeriodUntilCases() {
            return Stream.of(
                Arguments.of(1752, 7, 2, 1752, 7, 1, 0, 0, -1),
                Arguments.of(1752, 7, 2, 1752, 9, 1, 0, 1, 30),
                Arguments.of(1752, 7, 2, 1752, 9, 14, 0, 2, 1),
                Arguments.of(1752, 9, 14, 1752, 7, 13, 0, -2, -1),
                Arguments.of(1752, 9, 14, 1752, 9, 1, 0, 0, -2)
            );
        }

        @ParameterizedTest(name = "from {0}-{1}-{2} until {3}-{4}-{5} is P{6}Y{7}M{8}D")
        @MethodSource("chronoPeriodUntilCases")
        void until_asChronoPeriod_isCorrectAndReversibleWithPlus(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            ChronoPeriod expectedPeriod = BritishCutoverChronology.INSTANCE.period(ey, em, ed);

            ChronoPeriod actualPeriod = start.until(end);

            assertEquals(expectedPeriod, actualPeriod, "Period calculation should be correct");
            assertEquals(end, start.plus(actualPeriod), "Adding the calculated period should return the end date");
        }
    }

    // -----------------------------------------------------------------------
    @Nested
    @DisplayName("Other API Tests")
    class OtherApiTests {

        @Test
        void toString_returnsCorrectRepresentation() {
            assertEquals("BritishCutover AD 1-01-01", BritishCutoverDate.of(1, 1, 1).toString());
            assertEquals("BritishCutover AD 2012-06-23", BritishCutoverDate.of(2012, 6, 23).toString());
        }

        @Test
        void chronologyEraOf_withInvalidValue_throwsException() {
            assertThrows(DateTimeException.class, () -> BritishCutoverChronology.INSTANCE.eraOf(2));
        }
    }
}