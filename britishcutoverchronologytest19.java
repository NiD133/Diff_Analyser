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
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link BritishCutoverDate} class.
 */
@DisplayName("BritishCutoverDate")
class BritishCutoverDateTest {

    /**
     * Provides sample BritishCutoverDates and their equivalent ISO LocalDates.
     * This includes dates before, after, and during the 1752 cutover.
     *
     * @return a stream of arguments: (BritishCutoverDate, LocalDate)
     */
    static Stream<Arguments> provideSampleCutoverAndIsoDates() {
        return Stream.of(
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(BritishCutoverDate.of(4, 2, 29), LocalDate.of(4, 2, 27)),
            Arguments.of(BritishCutoverDate.of(100, 3, 1), LocalDate.of(100, 2, 28)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)), // Leniently accepts invalid date
            Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)), // Leniently accepts invalid date
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)),
            Arguments.of(BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6))
        );
    }

    @Nested
    @DisplayName("Factory and Conversion Tests")
    class FactoryAndConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#provideSampleCutoverAndIsoDates")
        void from_shouldCreateCorrectDateFromLocalDate(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(cutover, BritishCutoverDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#provideSampleCutoverAndIsoDates")
        void toLocalDate_shouldConvertToCorrectIsoDate(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(iso, LocalDate.from(cutover));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#provideSampleCutoverAndIsoDates")
        void toEpochDay_shouldMatchIsoDateEpochDay(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(iso.toEpochDay(), cutover.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#provideSampleCutoverAndIsoDates")
        void chronology_dateEpochDay_shouldCreateCorrectDate(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(cutover, BritishCutoverChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverDateTest#provideSampleCutoverAndIsoDates")
        void chronology_dateFromTemporal_shouldCreateCorrectDate(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(cutover, BritishCutoverChronology.INSTANCE.date(iso));
        }

        @Test
        void of_shouldThrowExceptionForInvalidDateComponents() {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(1900, 2, 30));
        }

        @Test
        void with_shouldAdjustFromLocalDate() {
            BritishCutoverDate cutoverDate = BritishCutoverDate.of(2012, 6, 23);
            LocalDate localDate = LocalDate.MIN.with(cutoverDate);
            assertEquals(LocalDate.of(2012, 6, 23), localDate);
        }
    }

    @Nested
    @DisplayName("Property and Field Access Tests")
    class PropertyAndFieldAccessTests {

        @ParameterizedTest
        @MethodSource
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> lengthOfMonth_shouldReturnCorrectLength() {
            return Stream.of(
                Arguments.of(1700, 2, 29), // Julian leap year
                Arguments.of(1751, 2, 28),
                Arguments.of(1752, 9, 19), // Cutover month
                Arguments.of(1753, 2, 28),
                Arguments.of(1800, 2, 28), // Gregorian non-leap year
                Arguments.of(2000, 2, 29)  // Gregorian leap year
            );
        }

        @ParameterizedTest
        @MethodSource
        void lengthOfYear_shouldReturnCorrectLength(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear(), "Length from start of year");
            assertEquals(expectedLength, BritishCutoverDate.of(year, 12, 31).lengthOfYear(), "Length from end of year");
        }

        static Stream<Arguments> lengthOfYear_shouldReturnCorrectLength() {
            return Stream.of(
                Arguments.of(1700, 366), // Julian leap year
                Arguments.of(1751, 365),
                Arguments.of(1752, 355), // Cutover year (lost 11 days)
                Arguments.of(1753, 365),
                Arguments.of(1800, 365), // Gregorian non-leap year
                Arguments.of(2000, 366)  // Gregorian leap year
            );
        }

        @ParameterizedTest
        @MethodSource
        void range_shouldReturnCorrectRangeForField(BritishCutoverDate date, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, date.range(field));
        }

        static Stream<Arguments> range_shouldReturnCorrectRangeForField() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1700, 2, 1), DAY_OF_MONTH, ValueRange.of(1, 29)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), DAY_OF_MONTH, ValueRange.of(1, 30)), // Range is lenient
                Arguments.of(BritishCutoverDate.of(1752, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 355)),
                Arguments.of(BritishCutoverDate.of(2011, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 365)),
                Arguments.of(BritishCutoverDate.of(2012, 1, 1), DAY_OF_YEAR, ValueRange.of(1, 366))
            );
        }

        @ParameterizedTest
        @MethodSource
        void getLong_shouldReturnCorrectValueForField(BritishCutoverDate date, TemporalField field, long expected) {
            assertEquals(expected, date.getLong(field));
        }

        static Stream<Arguments> getLong_shouldReturnCorrectValueForField() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), DAY_OF_WEEK, 3L), // Wednesday
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), DAY_OF_WEEK, 4L), // Thursday
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), DAY_OF_YEAR, 246L), // 245 in ISO, but this calendar has a gap
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), DAY_OF_WEEK, 1L), // Monday
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), PROLEPTIC_MONTH, 2014 * 12 + 4)
            );
        }
    }

    @Nested
    @DisplayName("Modification and Adjustment Tests")
    class ModificationAndAdjustmentTests {

        @ParameterizedTest
        @MethodSource
        void with_temporalField_shouldSetFieldToNewValue(BritishCutoverDate start, TemporalField field, long value, BritishCutoverDate expected) {
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Arguments> with_temporalField_shouldSetFieldToNewValue() {
            // {start date, field, new value, expected date}
            return Stream.of(
                // Cross the cutover gap
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), DAY_OF_MONTH, 14, BritishCutoverDate.of(1752, 9, 14)),
                // Standard adjustment
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), YEAR, 2012, BritishCutoverDate.of(2012, 5, 26)),
                // Adjusting month in a leap year to a non-leap year
                Arguments.of(BritishCutoverDate.of(2012, 2, 29), YEAR, 2011, BritishCutoverDate.of(2011, 2, 28))
            );
        }

        @Test
        void with_lastDayOfMonthAdjuster_shouldReturnLastDayOfMonth() {
            // Before cutover
            assertEquals(BritishCutoverDate.of(1752, 2, 29), BritishCutoverDate.of(1752, 2, 23).with(TemporalAdjusters.lastDayOfMonth()));
            // During cutover month
            assertEquals(BritishCutoverDate.of(1752, 9, 30), BritishCutoverDate.of(1752, 9, 2).with(TemporalAdjusters.lastDayOfMonth()));
            // After cutover
            assertEquals(BritishCutoverDate.of(2012, 2, 29), BritishCutoverDate.of(2012, 2, 23).with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Arithmetic Tests")
    class ArithmeticTests {

        @ParameterizedTest
        @MethodSource
        void plus_shouldCorrectlyAddAmountOfUnit(BritishCutoverDate start, long amount, TemporalUnit unit, BritishCutoverDate expected) {
            assertEquals(expected, start.plus(amount, unit));
        }

        static Stream<Arguments> plus_shouldCorrectlyAddAmountOfUnit() {
            // {start date, amount, unit, expected date}
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), 1, DAYS, BritishCutoverDate.of(1752, 9, 14)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), 1, WEEKS, BritishCutoverDate.of(1752, 9, 20)),
                Arguments.of(BritishCutoverDate.of(1752, 8, 12), 1, MONTHS, BritishCutoverDate.of(1752, 9, 23)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 3, YEARS, BritishCutoverDate.of(2017, 5, 26))
            );
        }

        @ParameterizedTest
        @MethodSource
        void minus_shouldCorrectlySubtractAmountOfUnit(BritishCutoverDate start, long amount, TemporalUnit unit, BritishCutoverDate expected) {
            assertEquals(expected, start.minus(amount, unit));
        }

        static Stream<Arguments> minus_shouldCorrectlySubtractAmountOfUnit() {
            // {start date, amount, unit, expected date}
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), 1, DAYS, BritishCutoverDate.of(1752, 9, 2)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), 1, WEEKS, BritishCutoverDate.of(1752, 8, 27)),
                Arguments.of(BritishCutoverDate.of(1752, 10, 12), 1, MONTHS, BritishCutoverDate.of(1752, 9, 23)),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), 5, YEARS, BritishCutoverDate.of(2009, 5, 26))
            );
        }

        @ParameterizedTest
        @MethodSource
        void until_temporalUnit_shouldCalculateCorrectAmount(BritishCutoverDate start, BritishCutoverDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> until_temporalUnit_shouldCalculateCorrectAmount() {
            // {start date, end date, unit, expected amount}
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), BritishCutoverDate.of(1752, 9, 14), DAYS, 2L),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 14), DAYS, 1L),
                Arguments.of(BritishCutoverDate.of(1752, 9, 1, 1), BritishCutoverDate.of(1752, 10, 1), MONTHS, 1L),
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), BritishCutoverDate.of(2015, 5, 26), YEARS, 1L)
            );
        }

        @ParameterizedTest
        @MethodSource
        void until_chronoPeriod_shouldCalculateCorrectPeriodAndBeReversible(BritishCutoverDate start, BritishCutoverDate end, Period expectedPeriod) {
            assertEquals(expectedPeriod, start.until(end));
            assertEquals(end, start.plus(start.until(end)), "Adding the period back should result in the end date");
        }

        static Stream<Arguments> until_chronoPeriod_shouldCalculateCorrectPeriodAndBeReversible() {
            // {start date, end date, expected period}
            return Stream.of(
                // Across the cutover
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 14), Period.of(0, 0, 1)),
                Arguments.of(BritishCutoverDate.of(1752, 8, 16), BritishCutoverDate.of(1752, 9, 16), Period.of(0, 1, 0)),
                // Standard period
                Arguments.of(BritishCutoverDate.of(2020, 1, 15), BritishCutoverDate.of(2021, 3, 18), Period.of(1, 2, 3))
            );
        }
    }

    @Nested
    @DisplayName("Object Method Tests")
    class ObjectMethodTests {

        @ParameterizedTest
        @MethodSource
        void toString_shouldReturnCorrectRepresentation(BritishCutoverDate cutover, String expected) {
            assertEquals(expected, cutover.toString());
        }

        static Stream<Arguments> toString_shouldReturnCorrectRepresentation() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1, 1, 1), "BritishCutover AD 1-01-01"),
                Arguments.of(BritishCutoverDate.of(2012, 6, 23), "BritishCutover AD 2012-06-23")
            );
        }

        @Test
        void equals_and_hashCode_shouldAdhereToContract() {
            BritishCutoverDate date1 = BritishCutoverDate.of(2023, 10, 26);
            BritishCutoverDate date2 = BritishCutoverDate.of(2023, 10, 26);
            BritishCutoverDate date3 = BritishCutoverDate.of(2023, 10, 27);
            BritishCutoverDate date4 = BritishCutoverDate.of(1700, 1, 1);

            // Reflexive
            assertEquals(date1, date1);

            // Symmetric
            assertEquals(date1, date2);
            assertEquals(date2, date1);

            // HashCode
            assertEquals(date1.hashCode(), date2.hashCode());

            // Not equal
            assertEquals(false, date1.equals(date3));
            assertEquals(false, date1.equals(date4));
            assertEquals(false, date1.equals(null));
            assertEquals(false, date1.equals(new Object()));
        }
    }
}