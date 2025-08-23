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
import java.time.Month;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
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
 * Tests the interoperability and conversion of {@link BritishCutoverDate}.
 */
@DisplayName("BritishCutoverChronology Interoperability and Conversions")
class BritishCutoverChronologyInteroperabilityTest {

    /**
     * Provides sample BritishCutoverDates and their equivalent ISO LocalDates.
     * @return a stream of arguments
     */
    static Stream<Arguments> sampleCutoverAndIsoDatesProvider() {
        return Stream.of(
            // Pre-cutover dates (Julian calendar rules)
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(BritishCutoverDate.of(4, 2, 29), LocalDate.of(4, 2, 27)), // Julian leap year
            Arguments.of(BritishCutoverDate.of(100, 3, 1), LocalDate.of(100, 2, 28)), // Julian leap year
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            // Dates around the British cutover in 1752
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)), // Last day before gap
            Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)), // Leniently accepts invalid date in gap
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)), // First day after gap
            // Post-cutover dates (Gregorian calendar rules)
            Arguments.of(BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12)),
            Arguments.of(BritishCutoverDate.of(2012, 7, 5), LocalDate.of(2012, 7, 5))
        );
    }

    @Nested
    @DisplayName("Conversion and Factory Tests")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyInteroperabilityTest#sampleCutoverAndIsoDatesProvider")
        @DisplayName("LocalDate.from(cutoverDate) should return equivalent ISO date")
        void toLocalDate_shouldReturnEquivalentIsoDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyInteroperabilityTest#sampleCutoverAndIsoDatesProvider")
        @DisplayName("BritishCutoverDate.from(isoDate) should return equivalent Cutover date")
        void fromLocalDate_shouldReturnEquivalentCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyInteroperabilityTest#sampleCutoverAndIsoDatesProvider")
        @DisplayName("toEpochDay() should match ISO date's epoch day")
        void toEpochDay_shouldMatchIsoDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyInteroperabilityTest#sampleCutoverAndIsoDatesProvider")
        @DisplayName("chronology.dateEpochDay() should create correct date")
        void fromEpochDay_shouldCreateCorrectDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyInteroperabilityTest#sampleCutoverAndIsoDatesProvider")
        @DisplayName("chronology.date(temporal) should create correct date from ISO")
        void fromTemporal_shouldCreateCorrectDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Field Access and Date Properties")
    class FieldAccessTests {

        static Stream<Arguments> lengthOfMonthProvider() {
            return Stream.of(
                Arguments.of(1700, 2, 29), // Julian leap year
                Arguments.of(1751, 2, 28), // Standard year
                Arguments.of(1752, 2, 29), // Gregorian leap year
                Arguments.of(1752, 9, 19), // Cutover month has 19 days
                Arguments.of(1800, 2, 28), // Gregorian non-leap century
                Arguments.of(1900, 2, 28), // Gregorian non-leap century
                Arguments.of(2000, 2, 29)  // Gregorian leap century
            );
        }

        @ParameterizedTest
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> lengthOfYearProvider() {
            return Stream.of(
                Arguments.of(1700, 366), // Julian leap year
                Arguments.of(1751, 365), // Standard year before cutover
                Arguments.of(1752, 355), // Cutover year has 355 days
                Arguments.of(1753, 365), // Standard year after cutover
                Arguments.of(1800, 365), // Gregorian non-leap century
                Arguments.of(2000, 366)  // Gregorian leap century
            );
        }

        @ParameterizedTest
        @MethodSource("lengthOfYearProvider")
        void lengthOfYear_shouldReturnCorrectLength(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
        }

        static Stream<Arguments> rangeProvider() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), DAY_OF_MONTH, ValueRange.of(1, 30)),
                Arguments.of(BritishCutoverDate.of(1752, 2, 1), DAY_OF_MONTH, ValueRange.of(1, 29)),
                Arguments.of(BritishCutoverDate.of(1752, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 355)),
                Arguments.of(BritishCutoverDate.of(2012, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 366)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 3)),
                Arguments.of(BritishCutoverDate.of(1752, 10, 1), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5))
            );
        }

        @ParameterizedTest
        @MethodSource("rangeProvider")
        void range_shouldReturnCorrectRangeForField(BritishCutoverDate date, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, date.range(field));
        }

        static Stream<Arguments> getLongProvider() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), DAY_OF_WEEK, 3L), // Wednesday
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), DAY_OF_WEEK, 4L), // Thursday
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), DAY_OF_YEAR, 246L + 3 - 11), // Day of year is adjusted for gap
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), PROLEPTIC_MONTH, 2014 * 12 + 4),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), YEAR, 2014L),
                Arguments.of(BritishCutoverDate.of(1, 6, 8), ERA, 1L),
                Arguments.of(BritishCutoverDate.of(0, 6, 8), ERA, 0L)
            );
        }

        @ParameterizedTest
        @MethodSource("getLongProvider")
        void getLong_shouldReturnCorrectFieldValue(BritishCutoverDate date, TemporalField field, long expectedValue) {
            assertEquals(expectedValue, date.getLong(field));
        }

        static Stream<Arguments> invalidDateProvider() {
            return Stream.of(
                Arguments.of(1900, 0, 1),
                Arguments.of(1900, 13, 1),
                Arguments.of(1900, 1, 0),
                Arguments.of(1900, 1, 32),
                Arguments.of(1900, 2, 30), // Not a leap year in Gregorian
                Arguments.of(1899, 2, 29)  // Not a leap year
            );
        }

        @ParameterizedTest
        @MethodSource("invalidDateProvider")
        void of_forInvalidDateParts_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class ManipulationTests {

        static Stream<Arguments> withFieldProvider() {
            return Stream.of(
                // Leniently adjust date when setting a day within the 1752 gap
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), DAY_OF_MONTH, 3L, BritishCutoverDate.of(1752, 9, 14)),
                // Normal adjustment
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), DAY_OF_WEEK, 3L, BritishCutoverDate.of(2014, 5, 28)),
                Arguments.of(BritishCutoverDate.of(2012, 2, 29), YEAR, 2011L, BritishCutoverDate.of(2011, 2, 28)),
                // Adjusting month across the cutover gap
                Arguments.of(BritishCutoverDate.of(1752, 8, 4), MONTH_OF_YEAR, 9L, BritishCutoverDate.of(1752, 9, 15))
            );
        }

        @ParameterizedTest
        @MethodSource("withFieldProvider")
        void with_usingTemporalField_shouldReturnModifiedDate(BritishCutoverDate baseDate, TemporalField field, long value, BritishCutoverDate expectedDate) {
            assertEquals(expectedDate, baseDate.with(field, value));
        }

        @Test
        void with_lastDayOfMonthAdjuster_shouldReturnLastDayOfMonth() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 30);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_localDate_shouldReturnDateWithSameEpochDay() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            LocalDate isoDateToAdjustTo = LocalDate.of(1752, 9, 14);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 14);
            assertEquals(expected, date.with(isoDateToAdjustTo));
        }

        static Stream<Arguments> plusMinusProvider() {
            return Stream.of(
                // Around cutover
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), 1, DAYS, BritishCutoverDate.of(1752, 9, 14), true),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), -1, DAYS, BritishCutoverDate.of(1752, 9, 2), true),
                Arguments.of(BritishCutoverDate.of(1752, 8, 12), 1, MONTHS, BritishCutoverDate.of(1752, 9, 23), false), // Not reversible
                // Standard cases
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 8, DAYS, BritishCutoverDate.of(2014, 6, 3), true),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, WEEKS, BritishCutoverDate.of(2014, 6, 16), true),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, MONTHS, BritishCutoverDate.of(2014, 8, 26), true),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, YEARS, BritishCutoverDate.of(2017, 5, 26), true),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, DECADES, BritishCutoverDate.of(2044, 5, 26), true),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, CENTURIES, BritishCutoverDate.of(2314, 5, 26), true),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, MILLENNIA, BritishCutoverDate.of(5014, 5, 26), true),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), -1, ERAS, BritishCutoverDate.of(-2013, 5, 26), true)
            );
        }

        @ParameterizedTest
        @MethodSource("plusMinusProvider")
        void plus_shouldAddTemporalAmount(BritishCutoverDate baseDate, long amount, TemporalUnit unit, BritishCutoverDate expectedDate, boolean isReversible) {
            assertEquals(expectedDate, baseDate.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusMinusProvider")
        void minus_shouldSubtractTemporalAmount(BritishCutoverDate expectedDate, long amount, TemporalUnit unit, BritishCutoverDate baseDate, boolean isReversible) {
            // Only test if the operation is reversible to avoid ambiguity with month/year arithmetic
            if (isReversible) {
                assertEquals(expectedDate, baseDate.minus(amount, unit));
            }
        }
    }

    @Nested
    @DisplayName("Period and Duration (until)")
    class PeriodAndDurationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyInteroperabilityTest#sampleCutoverAndIsoDatesProvider")
        @DisplayName("until() with an equivalent date should return a zero period")
        void until_withEquivalentDate_shouldReturnZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
        }

        static Stream<Arguments> untilUnitProvider() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), BritishCutoverDate.of(1752, 9, 14), DAYS, 2L),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 14), DAYS, 1L),
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), BritishCutoverDate.of(1752, 10, 1), MONTHS, 1L),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), BritishCutoverDate.of(2015, 5, 26), YEARS, 1L)
            );
        }

        @ParameterizedTest
        @MethodSource("untilUnitProvider")
        void until_byUnit_shouldCalculateAmountBetweenDates(BritishCutoverDate start, BritishCutoverDate end, TemporalUnit unit, long expectedAmount) {
            assertEquals(expectedAmount, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodProvider() {
            return Stream.of(
                // 30 days after 1752-08-02
                Arguments.of(BritishCutoverDate.of(1752, 7, 2), BritishCutoverDate.of(1752, 9, 1), 0, 1, 30),
                // 2 whole months
                Arguments.of(BritishCutoverDate.of(1752, 7, 2), BritishCutoverDate.of(1752, 9, 2), 0, 2, 0),
                // 1 day after 1752-09-02
                Arguments.of(BritishCutoverDate.of(1752, 7, 2), BritishCutoverDate.of(1752, 9, 14), 0, 2, 1),
                // Crossing the cutover
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 14), 0, 0, 1)
            );
        }

        @ParameterizedTest
        @MethodSource("untilPeriodProvider")
        void until_byPeriod_shouldCalculatePeriodBetweenDates(BritishCutoverDate start, BritishCutoverDate end, int y, int m, int d) {
            ChronoPeriod expectedPeriod = BritishCutoverChronology.INSTANCE.period(y, m, d);
            assertEquals(expectedPeriod, start.until(end));
        }

        @ParameterizedTest
        @MethodSource("untilPeriodProvider")
        void plusPeriod_shouldBeInverseOfUntil(BritishCutoverDate start, BritishCutoverDate end, int y, int m, int d) {
            ChronoPeriod period = start.until(end);
            assertEquals(end, start.plus(period));
        }
    }

    @Nested
    @DisplayName("General Object Methods")
    class GeneralMethodTests {

        @Test
        void toString_shouldReturnFormattedString() {
            BritishCutoverDate date = BritishCutoverDate.of(2012, Month.JUNE, 23);
            assertEquals("BritishCutover AD 2012-06-23", date.toString());
        }

        @Test
        @DisplayName("Era and Year-of-Era should round-trip correctly")
        void eraYearDayLoop_shouldRoundTripCorrectly() {
            for (int year = -200; year < 200; year++) {
                BritishCutoverDate base = BritishCutoverChronology.INSTANCE.dateYearDay(year, 1);
                assertEquals(year, base.get(YEAR));

                Era era = base.getEra();
                int yoe = base.get(YEAR_OF_ERA);
                BritishCutoverDate eraBased = BritishCutoverChronology.INSTANCE.dateYearDay(era, yoe, 1);
                assertEquals(base, eraBased, "Failed at year: " + year);
            }
        }
    }
}