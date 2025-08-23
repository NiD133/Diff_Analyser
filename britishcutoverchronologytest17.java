package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
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
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.time.temporal.WeekFields;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link BritishCutoverChronology} and {@link BritishCutoverDate}.
 * This class is organized into nested classes to group related tests.
 */
public class BritishCutoverChronologyTest {

    /**
     * Provides sample BritishCutoverDates and their equivalent ISO LocalDates.
     * This data is used for various conversion and calculation tests.
     *
     * @return A stream of arguments: { BritishCutoverDate, corresponding LocalDate }.
     */
    static Stream<Arguments> sampleCutoverAndIsoDates() {
        return Stream.of(
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
            Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(BritishCutoverDate.of(4, 3, 2), LocalDate.of(4, 2, 29)),
            Arguments.of(BritishCutoverDate.of(100, 3, 2), LocalDate.of(100, 3, 1)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
            Arguments.of(BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)), // leniently accept invalid
            Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)), // leniently accept invalid
            Arguments.of(BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)),
            Arguments.of(BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6))
        );
    }

    @Nested
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void fromBritishCutoverDate_shouldReturnCorrectIsoDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void fromIsoLocalDate_shouldReturnCorrectBritishCutoverDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void toEpochDay_shouldMatchIsoDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void chronology_dateEpochDay_shouldMatchIsoDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void chronology_dateFromTemporal_shouldReturnCorrectDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    class FactoryAndValidationTests {

        static Stream<Arguments> invalidDateProvider() {
            return Stream.of(
                Arguments.of(1900, 0, 0), { 1900, -1, 1 }, { 1900, 0, 1 },
                Arguments.of(1900, 13, 1), { 1900, 14, 1 },
                Arguments.of(1900, 1, -1), { 1900, 1, 0 }, { 1900, 1, 32 },
                Arguments.of(1900, 2, 30), // 1900 is not a leap year in Gregorian, but is in Julian
                Arguments.of(1899, 2, 29)  // 1899 is not a leap year
            );
        }

        @ParameterizedTest
        @MethodSource("invalidDateProvider")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
        }
    }

    @Nested
    class FieldAndRangeTests {

        static Stream<Arguments> lengthOfMonthProvider() {
            return Stream.of(
                Arguments.of(1700, 2, 29), // Julian leap year
                Arguments.of(1751, 2, 28), // Julian non-leap year
                Arguments.of(1752, 2, 29), // Gregorian leap year (before cutover month)
                Arguments.of(1752, 9, 19), // Cutover month has 19 days (2nd + 17 days from 14th-30th)
                Arguments.of(1800, 2, 28), // Gregorian non-leap year
                Arguments.of(1900, 2, 28), // Gregorian non-leap year
                Arguments.of(2000, 2, 29)  // Gregorian leap year
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
                Arguments.of(1751, 365), // Julian non-leap year
                Arguments.of(1752, 355), // Cutover year, 11 days removed
                Arguments.of(1753, 365), // Gregorian non-leap year
                Arguments.of(1800, 365), // Gregorian non-leap year
                Arguments.of(2000, 366)  // Gregorian leap year
            );
        }

        @ParameterizedTest
        @MethodSource("lengthOfYearProvider")
        void lengthOfYear_shouldReturnCorrectLength(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
        }

        static Stream<Arguments> fieldRangeProvider() {
            return Stream.of(
                Arguments.of(1752, 9, 1, DAY_OF_MONTH, 1, 30), // Range is lenient
                Arguments.of(1752, 9, 1, ALIGNED_WEEK_OF_MONTH, 1, 3), // Range reflects actual weeks
                Arguments.of(1752, 1, 1, DAY_OF_YEAR, 1, 355),
                Arguments.of(1752, 1, 1, ALIGNED_WEEK_OF_YEAR, 1, 51),
                Arguments.of(2012, 2, 1, DAY_OF_MONTH, 1, 29),
                Arguments.of(2012, 1, 1, DAY_OF_YEAR, 1, 366)
            );
        }

        @ParameterizedTest
        @MethodSource("fieldRangeProvider")
        void range_forGivenField_shouldBeCorrect(int year, int month, int day, TemporalField field, long min, long max) {
            ValueRange expectedRange = ValueRange.of(min, max);
            assertEquals(expectedRange, BritishCutoverDate.of(year, month, day).range(field));
        }

        static Stream<Arguments> fieldValueProvider() {
            return Stream.of(
                // Date: 1752-09-02 (before cutover, but in cutover month)
                Arguments.of(1752, 9, 2, DAY_OF_WEEK, 3L), // Wednesday
                Arguments.of(1752, 9, 2, DAY_OF_MONTH, 2L),
                Arguments.of(1752, 9, 2, DAY_OF_YEAR, 246L), // 31+29+31+30+31+30+31+31+2
                // Date: 1752-09-14 (first day of Gregorian calendar in Britain)
                Arguments.of(1752, 9, 14, DAY_OF_WEEK, 4L), // Thursday
                Arguments.of(1752, 9, 14, DAY_OF_MONTH, 14L),
                Arguments.of(1752, 9, 14, DAY_OF_YEAR, 247L), // Day after 1752-09-02
                // Date: 2014-05-26 (modern date)
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1L), // Monday
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 146L), // 31+28+31+30+26
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 4),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),
                Arguments.of(0, 6, 8, ERA, 0L) // BCE era
            );
        }

        @ParameterizedTest
        @MethodSource("fieldValueProvider")
        void getLong_forGivenField_shouldReturnCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, day).getLong(field));
        }
    }

    @Nested
    class AdjustmentTests {

        static Stream<Arguments> withFieldProvider() {
            return Stream.of(
                // --- Adjustments around the cutover ---
                // Adjust DAY_OF_WEEK from a pre-cutover date to a post-cutover date
                Arguments.of(1752, 9, 2, DAY_OF_WEEK, 4, BritishCutoverDate.of(1752, 9, 14)),
                // Adjust DAY_OF_MONTH into the gap (leniently moves to post-cutover)
                Arguments.of(1752, 9, 2, DAY_OF_MONTH, 3, BritishCutoverDate.of(1752, 9, 14)),
                // Adjust MONTH_OF_YEAR across the cutover
                Arguments.of(1752, 8, 4, MONTH_OF_YEAR, 9, BritishCutoverDate.of(1752, 9, 15)), // Lenient
                // Adjust YEAR across the cutover
                Arguments.of(1751, 9, 4, YEAR, 1752, BritishCutoverDate.of(1752, 9, 15)), // Lenient

                // --- Standard adjustments ---
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 3, BritishCutoverDate.of(2014, 5, 28)),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 31, BritishCutoverDate.of(2014, 5, 31)),
                Arguments.of(2012, 3, 31, MONTH_OF_YEAR, 2, BritishCutoverDate.of(2012, 2, 29)), // Adjust to shorter month
                Arguments.of(2012, 2, 29, YEAR, 2011, BritishCutoverDate.of(2011, 2, 28)) // Adjust leap day to non-leap year
            );
        }

        @ParameterizedTest
        @MethodSource("withFieldProvider")
        void with_temporalField_shouldReturnAdjustedDate(int year, int month, int day, TemporalField field, long value, BritishCutoverDate expected) {
            BritishCutoverDate start = BritishCutoverDate.of(year, month, day);
            assertEquals(expected, start.with(field, value));
        }

        @Test
        void with_unsupportedField_shouldThrowException() {
            assertThrows(UnsupportedTemporalTypeException.class, () -> BritishCutoverDate.of(2012, 6, 30).with(MINUTE_OF_DAY, 0));
        }

        static Stream<Arguments> lastDayOfMonthAdjustmentProvider() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1752, 2, 23), BritishCutoverDate.of(1752, 2, 29)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 30)),
                Arguments.of(BritishCutoverDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 30)),
                Arguments.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 29))
            );
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthAdjustmentProvider")
        void with_lastDayOfMonthAdjuster_shouldReturnCorrectDate(BritishCutoverDate input, BritishCutoverDate expected) {
            assertEquals(expected, input.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> withLocalDateAdjustmentProvider() {
            return Stream.of(
                // Adjusting a pre-cutover date to a date within the ISO gap
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 12), BritishCutoverDate.of(1752, 9, 1)),
                // Adjusting a pre-cutover date to a post-cutover date
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 14)),
                // Adjusting a post-cutover date to a date within the ISO gap
                Arguments.of(BritishCutoverDate.of(1752, 9, 15), LocalDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 14))
            );
        }

        @ParameterizedTest
        @MethodSource("withLocalDateAdjustmentProvider")
        void with_localDateAdjuster_shouldReturnCorrectDate(BritishCutoverDate input, LocalDate adjuster, BritishCutoverDate expected) {
            assertEquals(expected, input.with(adjuster));
        }
    }

    @Nested
    class AdditionAndSubtractionTests {

        static Stream<Arguments> plusAmountProvider() {
            return Stream.of(
                // Days across the cutover
                Arguments.of(1752, 9, 2, 1, DAYS, BritishCutoverDate.of(1752, 9, 14)),
                Arguments.of(1752, 9, 2, 2, DAYS, BritishCutoverDate.of(1752, 9, 15)),
                // Weeks across the cutover
                Arguments.of(1752, 9, 2, 1, WEEKS, BritishCutoverDate.of(1752, 9, 20)),
                // Months across the cutover
                Arguments.of(1752, 8, 12, 1, MONTHS, BritishCutoverDate.of(1752, 9, 23)),
                // Years
                Arguments.of(2014, 5, 26, 3, YEARS, BritishCutoverDate.of(2017, 5, 26)),
                // Eras
                Arguments.of(2014, 5, 26, -1, ERAS, BritishCutoverDate.of(-2013, 5, 26))
            );
        }

        @ParameterizedTest
        @MethodSource("plusAmountProvider")
        void plus_withTemporalUnit_shouldReturnCorrectDate(int year, int month, int day, long amount, TemporalUnit unit, BritishCutoverDate expected) {
            BritishCutoverDate start = BritishCutoverDate.of(year, month, day);
            assertEquals(expected, start.plus(amount, unit));
        }

        static Stream<Arguments> minusAmountProvider() {
            // Invert the plus provider: end.minus(amount) should equal start
            return plusAmountProvider().map(args -> {
                Object[] original = args.get();
                BritishCutoverDate start = BritishCutoverDate.of((int) original[0], (int) original[1], (int) original[2]);
                long amount = (long) original[3];
                TemporalUnit unit = (TemporalUnit) original[4];
                BritishCutoverDate end = (BritishCutoverDate) original[5];
                return Arguments.of(end, amount, unit, start);
            });
        }

        @ParameterizedTest
        @MethodSource("minusAmountProvider")
        void minus_withTemporalUnit_shouldReturnCorrectDate(BritishCutoverDate start, long amount, TemporalUnit unit, BritishCutoverDate expected) {
            assertEquals(expected, start.minus(amount, unit));
        }
    }

    @Nested
    class UntilTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleCutoverAndIsoDates")
        void until_self_returnsZeroPeriod(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(cutoverDate));
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutoverDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(cutoverDate));
        }

        static Stream<Arguments> untilAmountProvider() {
            return Stream.of(
                // Days across the cutover
                Arguments.of(BritishCutoverDate.of(1752, 9, 1), BritishCutoverDate.of(1752, 9, 14), DAYS, 2L),
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 14), DAYS, 1L),
                // Weeks across the cutover
                Arguments.of(BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 20), WEEKS, 1L),
                // Months
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), BritishCutoverDate.of(2014, 6, 26), MONTHS, 1L),
                // Years
                Arguments.of(BritishCutoverDate.of(2014, 5, 26), BritishCutoverDate.of(2015, 5, 26), YEARS, 1L)
            );
        }

        @ParameterizedTest
        @MethodSource("untilAmountProvider")
        void until_withTemporalUnit_shouldReturnCorrectAmount(BritishCutoverDate start, BritishCutoverDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> periodUntilProvider() {
            return Stream.of(
                // Start: 1752-07-02, End: 1752-09-14 (crosses cutover) -> Expected: 2 months, 1 day
                Arguments.of(1752, 7, 2, 1752, 9, 14, 0, 2, 1),
                // Start: 1752-09-02, End: 1752-09-14 (adjacent logical days) -> Expected: 0 months, 1 day
                Arguments.of(1752, 9, 2, 1752, 9, 14, 0, 0, 1),
                // Start: 1752-09-14, End: 1752-07-14 (reverse) -> Expected: -2 months, 0 days
                Arguments.of(1752, 9, 14, 1752, 7, 14, 0, -2, 0),
                // Standard period
                Arguments.of(2020, 1, 15, 2021, 3, 18, 1, 2, 3)
            );
        }

        @ParameterizedTest(name = "Period from {0}-{1}-{2} to {3}-{4}-{5} should be {6}Y {7}M {8}D")
        @MethodSource("periodUntilProvider")
        void until_asChronoPeriod_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int eY, int eM, int eD) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            ChronoPeriod expected = BritishCutoverChronology.INSTANCE.period(eY, eM, eD);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest(name = "Period from {0}-{1}-{2} to {3}-{4}-{5} should be reversible")
        @MethodSource("periodUntilProvider")
        void until_periodCalculationIsReversibleWithPlus(int y1, int m1, int d1, int y2, int m2, int d2, int eY, int eM, int eD) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            assertEquals(end, start.plus(start.until(end)));
        }
    }

    @Nested
    class ToStringTests {

        static Stream<Arguments> toStringProvider() {
            return Stream.of(
                Arguments.of(BritishCutoverDate.of(1, 1, 1), "BritishCutover AD 1-01-01"),
                Arguments.of(2012, 6, 23, "BritishCutover AD 2012-06-23")
            );
        }

        @ParameterizedTest
        @MethodSource("toStringProvider")
        void toString_shouldReturnCorrectFormat(BritishCutoverDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}