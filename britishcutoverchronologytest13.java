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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Era;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Comprehensive tests for {@link BritishCutoverDate}.
 * <p>
 * This test class is structured with nested classes to group related tests,
 * improving readability and maintainability.
 */
class BritishCutoverDateTest {

    // A representative date after the cutover for simple test cases.
    private static final BritishCutoverDate POST_CUTOVER_DATE = BritishCutoverDate.of(2012, 7, 5);
    private static final LocalDate POST_CUTOVER_ISO_DATE = LocalDate.of(2012, 7, 5);

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleBritishAndIsoDates() {
        return Stream.of(
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)), // Before cutover
            Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)), // In cutover gap (lenient)
            Arguments.of(BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)), // In cutover gap (lenient)
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)), // After cutover
            Arguments.of(BritishCutoverDate.of(2012, 7, 5), LocalDate.of(2012, 7, 5))
        );
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion to and from other types")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleBritishAndIsoDates")
        @DisplayName("LocalDate.from(cutoverDate) should return the equivalent ISO date")
        void from_convertsBritishCutoverDateToLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleBritishAndIsoDates")
        @DisplayName("BritishCutoverDate.from(isoDate) should return the equivalent BritishCutoverDate")
        void from_convertsLocalDateToBritishCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleBritishAndIsoDates")
        @DisplayName("chronology.dateEpochDay() should create the correct date from epoch day")
        void chronology_dateEpochDay_createsCorrectDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleBritishAndIsoDates")
        @DisplayName("toEpochDay() should return the correct epoch day value")
        void toEpochDay_returnsCorrectValue(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#sampleBritishAndIsoDates")
        @DisplayName("chronology.date(temporal) should create the correct date")
        void chronology_date_fromTemporal_createsCorrectDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Factory method of(y, m, d)")
    class FactoryMethodTests {

        static Stream<Arguments> invalidDateComponents() {
            return Stream.of(
                Arguments.of(1900, 0, 1),   // Invalid month
                Arguments.of(1900, 13, 1),  // Invalid month
                Arguments.of(1900, 1, 0),   // Invalid day
                Arguments.of(1900, 1, 32),  // Invalid day
                Arguments.of(1899, 2, 29),  // Not a leap year (Julian)
                Arguments.of(1900, 2, 29),  // Not a leap year (Gregorian)
                Arguments.of(1900, 4, 31)   // Invalid day for month
            );
        }

        @ParameterizedTest
        @MethodSource("invalidDateComponents")
        @DisplayName("of(y, m, d) should throw DateTimeException for invalid date components")
        void of_throwsExceptionForInvalidDateComponents(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date property accessors")
    class DatePropertyTests {

        static Stream<Arguments> lengthOfMonthSamples() {
            return Stream.of(
                // year, month, expectedLength
                Arguments.of(1700, 2, 29),  // Julian leap year
                Arguments.of(1751, 2, 28),  // Julian non-leap year
                Arguments.of(1752, 2, 29),  // Gregorian leap year (before cutover month)
                Arguments.of(1752, 9, 19),  // Cutover month has 19 days
                Arguments.of(1753, 2, 28),  // Gregorian non-leap year
                Arguments.of(1800, 2, 28),  // Gregorian non-leap year
                Arguments.of(2000, 2, 29)   // Gregorian leap year
            );
        }

        @ParameterizedTest
        @MethodSource("lengthOfMonthSamples")
        void lengthOfMonth_returnsCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> lengthOfYearSamples() {
            return Stream.of(
                // year, expectedLength
                Arguments.of(1700, 366), // Julian leap year
                Arguments.of(1751, 365), // Normal year before cutover year
                Arguments.of(1752, 355), // Cutover year is 11 days shorter
                Arguments.of(1753, 365), // Normal year after cutover year
                Arguments.of(1800, 365), // Gregorian non-leap year
                Arguments.of(2000, 366)  // Gregorian leap year
            );
        }

        @ParameterizedTest
        @MethodSource("lengthOfYearSamples")
        void lengthOfYear_isCorrect(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
            assertEquals(expectedLength, BritishCutoverDate.of(year, 12, 31).lengthOfYear());
        }

        static Stream<Arguments> fieldRangeSamples() {
            return Stream.of(
                // year, month, day, field, min, max
                Arguments.of(1752, 9, 23, DAY_OF_MONTH, 1, 30),
                Arguments.of(1752, 1, 23, DAY_OF_YEAR, 1, 355),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, 1, 366),
                Arguments.of(1752, 9, 23, ALIGNED_WEEK_OF_MONTH, 1, 3),
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, 1, 5),
                Arguments.of(1752, 12, 23, ALIGNED_WEEK_OF_YEAR, 1, 51),
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_YEAR, 1, 53)
            );
        }

        @ParameterizedTest
        @MethodSource("fieldRangeSamples")
        void range_returnsCorrectValueRangeForField(int year, int month, int dom, TemporalField field, long min, long max) {
            ValueRange expectedRange = ValueRange.of(min, max);
            assertEquals(expectedRange, BritishCutoverDate.of(year, month, dom).range(field));
        }

        static Stream<Arguments> fieldValueSamples() {
            return Stream.of(
                // year, month, day, field, expectedValue
                Arguments.of(1752, 9, 2, DAY_OF_WEEK, 3L),
                Arguments.of(1752, 9, 14, DAY_OF_WEEK, 4L),
                Arguments.of(1752, 9, 2, DAY_OF_YEAR, 246L), // 31+29+31+30+31+30+31+31+2
                Arguments.of(1752, 9, 14, DAY_OF_YEAR, 247L), // Day after 1752-09-02
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 4),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(0, 6, 8, ERA, 0L),
                Arguments.of(1, 6, 8, ERA, 1L)
            );
        }

        @ParameterizedTest
        @MethodSource("fieldValueSamples")
        void getLong_returnsCorrectValueForField(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, dom).getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date modification with 'with', 'plus', and 'minus'")
    class DateModificationTests {

        static Stream<Arguments> withTemporalFieldSamples() {
            // baseYear, baseMonth, baseDay, field, value, expectedYear, expectedMonth, expectedDay
            return Stream.of(
                Arguments.of(1752, 9, 2, DAY_OF_WEEK, 4L, 1752, 9, 14),
                Arguments.of(1752, 9, 2, DAY_OF_MONTH, 14L, 1752, 9, 14),
                Arguments.of(1752, 9, 2, MONTH_OF_YEAR, 10L, 1752, 10, 2),
                Arguments.of(1751, 9, 4, YEAR, 1752L, 1752, 9, 15), // Leniently adjusts into cutover
                Arguments.of(2014, 5, 26, YEAR, 2012L, 2012, 5, 26),
                Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2L, 2012, 2, 29) // Adjusts to last valid day
            );
        }

        @ParameterizedTest
        @MethodSource("withTemporalFieldSamples")
        void with_temporalField_returnsAdjustedDate(int y, int m, int d, TemporalField f, long v, int ey, int em, int ed) {
            BritishCutoverDate base = BritishCutoverDate.of(y, m, d);
            BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
            assertEquals(expected, base.with(f, v));
        }

        @Test
        void with_lastDayOfMonthAdjuster_returnsCorrectDate() {
            BritishCutoverDate dateInCutoverMonth = BritishCutoverDate.of(1752, 9, 2);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 30);
            assertEquals(expected, dateInCutoverMonth.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_localDate_returnsCorrectDate() {
            BritishCutoverDate base = BritishCutoverDate.of(1752, 9, 2);
            LocalDate newDate = LocalDate.of(1752, 9, 14); // This is a Gregorian date
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 14);
            assertEquals(expected, base.with(newDate));
        }

        static Stream<Arguments> plusMinusSamples() {
            // y, m, d, amount, unit, expectedY, expectedM, expectedD
            return Stream.of(
                Arguments.of(1752, 9, 2, 1L, DAYS, 1752, 9, 14), // Crosses the cutover gap
                Arguments.of(1752, 9, 14, -1L, DAYS, 1752, 9, 2), // Crosses the cutover gap backwards
                Arguments.of(1752, 9, 2, 1L, WEEKS, 1752, 9, 20),
                Arguments.of(1752, 8, 12, 1L, MONTHS, 1752, 9, 23), // Adjusts due to cutover
                Arguments.of(2014, 5, 26, 3L, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, -5L, DECADES, 1964, 5, 26),
                Arguments.of(2014, 5, 26, 1L, CENTURIES, 2114, 5, 26),
                Arguments.of(2014, 5, 26, -1L, ERAS, -2013, 5, 26)
            );
        }

        @ParameterizedTest
        @MethodSource("plusMinusSamples")
        void plus_addsAmountToDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            BritishCutoverDate base = BritishCutoverDate.of(y, m, d);
            BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
            assertEquals(expected, base.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusMinusSamples")
        void minus_subtractsAmountFromDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            BritishCutoverDate base = BritishCutoverDate.of(ey, em, ed);
            BritishCutoverDate expected = BritishCutoverDate.of(y, m, d);
            assertEquals(expected, base.minus(amount, unit));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Period and duration calculations")
    class PeriodCalculationTests {

        @Test
        void until_self_returnsZeroPeriod() {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), POST_CUTOVER_DATE.until(POST_CUTOVER_DATE));
        }

        @Test
        void until_equivalentLocalDate_returnsZeroPeriod() {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), POST_CUTOVER_DATE.until(POST_CUTOVER_ISO_DATE));
        }

        @Test
        void until_onLocalDate_withEquivalentBritishCutoverDate_returnsZeroPeriod() {
            assertEquals(Period.ZERO, POST_CUTOVER_ISO_DATE.until(POST_CUTOVER_DATE));
        }

        static Stream<Arguments> untilTemporalUnitSamples() {
            // startY, startM, startD, endY, endM, endD, unit, expectedAmount
            return Stream.of(
                Arguments.of(1752, 9, 1, 1752, 9, 14, DAYS, 2L), // 2 days in this calendar
                Arguments.of(1752, 9, 1, 1752, 9, 19, WEEKS, 1L),
                Arguments.of(1752, 9, 2, 1752, 10, 2, MONTHS, 1L),
                Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
                Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
                Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1L)
            );
        }

        @ParameterizedTest
        @MethodSource("untilTemporalUnitSamples")
        void until_withTemporalUnit_calculatesAmountBetweenDates(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodSamples() {
            // startY, startM, startD, endY, endM, endD, expectedY, expectedM, expectedD
            return Stream.of(
                Arguments.of(1752, 7, 2, 1752, 9, 14, 0, 2, 1), // Crosses cutover
                Arguments.of(1752, 9, 14, 1752, 7, 14, 0, -2, 0), // Crosses cutover backwards
                Arguments.of(2020, 1, 15, 2021, 3, 18, 1, 2, 3) // Standard period
            );
        }

        @ParameterizedTest
        @MethodSource("untilPeriodSamples")
        void until_chronoLocalDate_calculatesCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            ChronoPeriod expected = BritishCutoverChronology.INSTANCE.period(ey, em, ed);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest
        @MethodSource("untilPeriodSamples")
        void plus_periodFromUntil_recreatesEndDate(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            ChronoPeriod period = start.until(end);
            assertEquals(end, start.plus(period));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("General API behavior")
    class GeneralApiTests {

        @Test
        void toString_returnsCorrectFormat() {
            assertEquals("BritishCutover AD 1-01-01", BritishCutoverDate.of(1, 1, 1).toString());
            assertEquals("BritishCutover AD 2012-06-23", BritishCutoverDate.of(2012, 6, 23).toString());
        }

        @Test
        void chronology_eras_returnsBothBCAndAD() {
            List<Era> eras = BritishCutoverChronology.INSTANCE.eras();
            assertEquals(2, eras.size());
            assertTrue(eras.contains(JulianEra.BC));
            assertTrue(eras.contains(JulianEra.AD));
        }
    }
}