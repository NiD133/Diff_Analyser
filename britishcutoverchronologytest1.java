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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
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

@DisplayName("BritishCutoverChronology and BritishCutoverDate")
class BritishCutoverChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleDateEquivalents() {
        return Stream.of(
            // Pre-cutover dates (Julian calendar rules)
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(BritishCutoverDate.of(4, 2, 29), LocalDate.of(4, 2, 27)), // Julian leap year
            Arguments.of(BritishCutoverDate.of(100, 2, 29), LocalDate.of(100, 2, 27)), // Julian leap year
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(BritishCutoverDate.of(1751, 12, 31), LocalDate.of(1752, 1, 11)),

            // Dates around the 1752 cutover
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)), // Last Julian day
            Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)), // Leniently accepts invalid date, maps to first Gregorian day
            Arguments.of(BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)), // Leniently accepts invalid date
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)), // First Gregorian day

            // Post-cutover dates (Gregorian calendar rules)
            Arguments.of(BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12)),
            Arguments.of(BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6))
        );
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion and Factory Tests")
    class ConversionAndFactoryTests {

        @ParameterizedTest(name = "{index}: {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDateEquivalents")
        @DisplayName("BritishCutoverDate.from(LocalDate)")
        void from_localDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest(name = "{index}: {0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDateEquivalents")
        @DisplayName("LocalDate.from(BritishCutoverDate)")
        void to_localDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest(name = "{index}: {0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDateEquivalents")
        @DisplayName("toEpochDay")
        void toEpochDay_isConsistentWithIso(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest(name = "{index}: {1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDateEquivalents")
        @DisplayName("chronology.dateEpochDay")
        void dateEpochDay_isConsistentWithIso(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest(name = "{index}: {0}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDateEquivalents")
        @DisplayName("chronology.date(TemporalAccessor)")
        void date_fromTemporal(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }

        static Stream<Arguments> badDates() {
            return Stream.of(
                Arguments.of(1900, 0, 1), Arguments.of(1900, 13, 1),
                Arguments.of(1900, 1, 0), Arguments.of(1900, 1, 32),
                Arguments.of(1900, 2, 30), // Not a leap year in Gregorian, but is in Julian
                Arguments.of(1899, 2, 29)  // Not a leap year
            );
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("badDates")
        @DisplayName("of() with invalid date parts throws exception")
        void of_withInvalidDateParts_throwsException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, day));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Property Tests")
    class PropertyTests {

        static Stream<Arguments> lengthOfMonthData() {
            return Stream.of(
                Arguments.of(1700, 2, 29), // Julian leap year
                Arguments.of(1751, 2, 28), // Julian non-leap year
                Arguments.of(1752, 2, 29), // Julian leap year
                Arguments.of(1752, 9, 19), // The cutover month
                Arguments.of(1753, 2, 28), // Gregorian non-leap year
                Arguments.of(1800, 2, 28), // Gregorian non-leap year
                Arguments.of(1900, 2, 28), // Gregorian non-leap year
                Arguments.of(2000, 2, 29)  // Gregorian leap year
            );
        }

        @ParameterizedTest(name = "{0}-{1} should have {2} days")
        @MethodSource("lengthOfMonthData")
        @DisplayName("lengthOfMonth is correct for various years")
        void lengthOfMonth_isCorrect(int year, int month, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> lengthOfYearData() {
            return Stream.of(
                Arguments.of(1700, 366), // Julian leap year
                Arguments.of(1751, 365), // Julian non-leap year
                Arguments.of(1752, 355), // The cutover year (lost 10 days)
                Arguments.of(1753, 365), // Gregorian non-leap year
                Arguments.of(1800, 365), // Gregorian non-leap year
                Arguments.of(1900, 365), // Gregorian non-leap year
                Arguments.of(2000, 366), // Gregorian leap year
                Arguments.of(2004, 366)  // Gregorian leap year
            );
        }

        @ParameterizedTest(name = "Year {0} should have {1} days")
        @MethodSource("lengthOfYearData")
        @DisplayName("lengthOfYear is correct for various years")
        void lengthOfYear_isCorrect(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
            assertEquals(expectedLength, BritishCutoverDate.of(year, 12, 31).lengthOfYear());
        }

        static Stream<Arguments> rangeData() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), DAY_OF_MONTH, ValueRange.of(1, 30)),
                Arguments.of(BritishCutoverDate.of(1752, 2, 1), DAY_OF_MONTH, ValueRange.of(1, 29)),
                Arguments.of(BritishCutoverDate.of(1752, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 355)),
                Arguments.of(BritishCutoverDate.of(1753, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 365)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 3)),
                Arguments.of(BritishCutoverDate.of(1752, 10, 1), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
                Arguments.of(BritishCutoverDate.of(1752, 1, 1), ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 51))
            );
        }

        @ParameterizedTest(name = "{0} range({1}) should be {2}")
        @MethodSource("rangeData")
        @DisplayName("range() returns correct value range for fields")
        void range_returnsCorrectRanges(BritishCutoverDate date, TemporalField field, ValueRange expected) {
            assertEquals(expected, date.range(field));
        }

        static Stream<Arguments> getLongData() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), DAY_OF_WEEK, 3L), // Wednesday
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), DAY_OF_WEEK, 4L), // Thursday
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), DAY_OF_YEAR, 246L + 1L - 11L), // 246th day in Julian, but 11 days skipped
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), YEAR, 2014L),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), ERA, 1L),
                Arguments.of(BritishCutoverDate.of(0, 6, 8), ERA, 0L)
            );
        }

        @ParameterizedTest(name = "{0}.getLong({1}) should be {2}")
        @MethodSource("getLongData")
        @DisplayName("getLong() returns correct value for fields")
        void getLong_returnsCorrectValues(BritishCutoverDate date, TemporalField field, long expected) {
            assertEquals(expected, date.getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Arithmetic Tests")
    class ArithmeticTests {

        static Stream<Arguments> withData() {
            return Stream.of(
                // Adjusting into the cutover gap
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), DAY_OF_MONTH, 3, BritishCutoverDate.of(1752, 9, 14)), // lenient
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), DAY_OF_MONTH, 2, BritishCutoverDate.of(1752, 9, 2)), // lenient
                Arguments.of(BritishCutoverDate.of(1752, 8, 4), MONTH_OF_YEAR, 9, BritishCutoverDate.of(1752, 9, 15)), // lenient
                Arguments.of(BritishCutoverDate.of(1751, 9, 4), YEAR, 1752, BritishCutoverDate.of(1752, 9, 15)), // lenient
                // Standard adjustments
                Arguments.of(BritishCutoverDate.of(2012, 2, 29), YEAR, 2011, BritishCutoverDate.of(2011, 2, 28)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), DAY_OF_WEEK, 3, BritishCutoverDate.of(2014, 5, 28))
            );
        }

        @ParameterizedTest(name = "{0}.with({1}, {2}) should be {3}")
        @MethodSource("withData")
        @DisplayName("with(TemporalField, long) adjusts correctly")
        void with_field(BritishCutoverDate start, TemporalField field, long value, BritishCutoverDate expected) {
            assertEquals(expected, start.with(field, value));
        }

        @Test
        @DisplayName("with(TemporalAdjuster.lastDayOfMonth) adjusts correctly")
        void with_lastDayOfMonth() {
            assertEquals(BritishCutoverDate.of(1752, 9, 30), BritishCutoverDate.of(1752, 9, 2).with(TemporalAdjusters.lastDayOfMonth()));
            assertEquals(BritishCutoverDate.of(2012, 2, 29), BritishCutoverDate.of(2012, 2, 1).with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> plusData() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), 1, DAYS, BritishCutoverDate.of(1752, 9, 14)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), 1, WEEKS, BritishCutoverDate.of(1752, 9, 20)),
                Arguments.of(BritishCutoverDate.of(1752, 8, 12), 1, MONTHS, BritishCutoverDate.of(1752, 9, 23)), // Crosses cutover
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, YEARS, BritishCutoverDate.of(2017, 5, 26)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, DECADES, BritishCutoverDate.of(2044, 5, 26)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 1, CENTURIES, BritishCutoverDate.of(2114, 5, 26)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 1, MILLENNIA, BritishCutoverDate.of(3014, 5, 26)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), -1, ERAS, BritishCutoverDate.of(-2013, 5, 26))
            );
        }

        @ParameterizedTest(name = "{0}.plus({1}, {2}) should be {3}")
        @MethodSource("plusData")
        @DisplayName("plus(long, TemporalUnit) adds correctly")
        void plus_addsAmountCorrectly(BritishCutoverDate start, long amount, TemporalUnit unit, BritishCutoverDate expected) {
            assertEquals(expected, start.plus(amount, unit));
        }

        static Stream<Arguments> minusData() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), 1, DAYS, BritishCutoverDate.of(1752, 9, 2)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 20), 1, WEEKS, BritishCutoverDate.of(1752, 9, 2)),
                Arguments.of(BritishCutoverDate.of(1752, 10, 12), 1, MONTHS, BritishCutoverDate.of(1752, 9, 23)), // Crosses cutover
                Arguments.of(BritishCutoverDate.of(2017, 5, 26), 3, YEARS, BritishCutoverDate.of(2014, 5, 26)),
                Arguments.of(BritishCutoverDate.of(-2013, 5, 26), -1, ERAS, BritishCutoverDate.of(2014, 5, 26))
            );
        }

        @ParameterizedTest(name = "{0}.minus({1}, {2}) should be {3}")
        @MethodSource("minusData")
        @DisplayName("minus(long, TemporalUnit) subtracts correctly")
        void minus_subtractsAmountCorrectly(BritishCutoverDate start, long amount, TemporalUnit unit, BritishCutoverDate expected) {
            assertEquals(expected, start.minus(amount, unit));
        }

        static Stream<Arguments> untilUnitData() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 14), DAYS, 1L),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 20), WEEKS, 1L),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 10, 14), MONTHS, 1L),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), BritishCutoverDate.of(2015, 5, 26), YEARS, 1L)
            );
        }

        @ParameterizedTest(name = "{0}.until({1}, {2}) should be {3}")
        @MethodSource("untilUnitData")
        @DisplayName("until(end, unit) calculates duration correctly")
        void until_calculatesDurationInUnit(BritishCutoverDate start, BritishCutoverDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodData() {
            return Stream.of(
                // until(start, end) -> expected period
                Arguments.of(BritishCutoverDate.of(1752, 7, 2), BritishCutoverDate.of(1752, 9, 1), Period.of(0, 1, 30)),
                Arguments.of(BritishCutoverDate.of(1752, 7, 2), BritishCutoverDate.of(1752, 9, 2), Period.of(0, 2, 0)),
                Arguments.of(BritishCutoverDate.of(1752, 7, 2), BritishCutoverDate.of(1752, 9, 14), Period.of(0, 2, 1)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 14), Period.of(0, 0, 1)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 2), Period.of(0, 0, -1))
            );
        }

        @ParameterizedTest(name = "{0}.until({1}) should be {2}")
        @MethodSource("untilPeriodData")
        @DisplayName("until(end) calculates period correctly")
        void until_calculatesPeriod(BritishCutoverDate start, BritishCutoverDate end, Period period) {
            ChronoPeriod expected = BritishCutoverChronology.INSTANCE.period(period.getYears(), period.getMonths(), period.getDays());
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest(name = "{0}.plus({0}.until({1})) should be {1}")
        @MethodSource("untilPeriodData")
        @DisplayName("Property: start.plus(start.until(end)) == end")
        void until_periodAddedToStartDate_returnsEndDate(BritishCutoverDate start, BritishCutoverDate end, Period ignored) {
            ChronoPeriod period = start.until(end);
            assertEquals(end, start.plus(period));
        }

        @ParameterizedTest(name = "{index}: {0} vs {1}")
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleDateEquivalents")
        @DisplayName("until() between BritishCutoverDate and LocalDate is zero")
        void until_mixedTypes_isZero(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
        }
    }

    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Chronology API Tests")
    class ChronologyApiTests {

        @Test
        @DisplayName("Chronology.of(\"BritishCutover\") returns singleton instance")
        void chronologyOf_returnsSingleton() {
            Chronology chrono = Chronology.of("BritishCutover");
            assertNotNull(chrono);
            assertEquals(BritishCutoverChronology.INSTANCE, chrono);
            assertEquals("BritishCutover", chrono.getId());
            assertEquals(null, chrono.getCalendarType());
        }

        static Stream<Arguments> toStringData() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1, 1, 1), "BritishCutover AD 1-01-01"),
                Arguments.of(BritishCutoverDate.of(2012, 6, 23), "BritishCutover AD 2012-06-23")
            );
        }

        @ParameterizedTest(name = "{0} toString() should be \"{1}\"")
        @MethodSource("toStringData")
        @DisplayName("toString() returns correct representation")
        void toString_returnsCorrectString(BritishCutoverDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}