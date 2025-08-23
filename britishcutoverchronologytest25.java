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
import java.util.Arrays;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link BritishCutoverChronology} and {@link BritishCutoverDate}.
 * This test class is structured with nested classes to group related tests.
 */
class BritishCutoverChronologyTest {

    /**
     * Provides pairs of equivalent BritishCutoverDate and ISO LocalDate instances.
     * @return A stream of arguments: (BritishCutoverDate, LocalDate).
     */
    static Stream<Arguments> sampleEquivalentDates() {
        return Stream.of(
            // --- Julian calendar dates (pre-1752) ---
            Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
            Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
            Arguments.of(BritishCutoverDate.of(4, 2, 29), LocalDate.of(4, 2, 27)), // Julian leap year
            Arguments.of(BritishCutoverDate.of(100, 3, 1), LocalDate.of(100, 2, 28)), // Julian leap year, not ISO
            Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),

            // --- Dates around the start of year change (1752) ---
            Arguments.of(BritishCutoverDate.of(1751, 12, 20), LocalDate.of(1751, 12, 31)),
            Arguments.of(BritishCutoverDate.of(1751, 12, 31), LocalDate.of(1752, 1, 11)),
            Arguments.of(BritishCutoverDate.of(1752, 1, 1), LocalDate.of(1752, 1, 12)),

            // --- Dates around the cutover gap (September 1752) ---
            Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)), // Last day before the gap
            // Leniently handle dates within the gap (Sept 3-13 are invalid) by mapping to the first day after the gap
            Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)),
            Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)), // First day after the gap

            // --- Gregorian calendar dates (post-1752) ---
            Arguments.of(BritishCutoverDate.of(1945, 11, 12), LocalDate.of(1945, 11, 12)),
            Arguments.of(BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6))
        );
    }

    @Nested
    @DisplayName("Conversion and Factory Tests")
    class ConversionAndFactoryTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleEquivalentDates")
        void from_BritishCutoverDate_shouldReturnCorrespondingLocalDate(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(iso, LocalDate.from(cutover));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleEquivalentDates")
        void from_LocalDate_shouldReturnCorrespondingBritishCutoverDate(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(cutover, BritishCutoverDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleEquivalentDates")
        void toEpochDay_shouldReturnCorrectValue(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(iso.toEpochDay(), cutover.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleEquivalentDates")
        void chronology_dateEpochDay_shouldCreateCorrectDate(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(cutover, BritishCutoverChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleEquivalentDates")
        void chronology_dateFromTemporal_shouldCreateCorrectDate(BritishCutoverDate cutover, LocalDate iso) {
            assertEquals(cutover, BritishCutoverChronology.INSTANCE.date(iso));
        }
    }

    @Nested
    @DisplayName("Date Validation Tests")
    class ValidationTests {

        static Stream<Arguments> invalidDateProvider() {
            return Stream.of(
                Arguments.of(1900, 0, 0), Arguments.of(1900, -1, 1), Arguments.of(1900, 0, 1),
                Arguments.of(1900, 13, 1), Arguments.of(1900, 14, 1), Arguments.of(1900, 1, -1),
                Arguments.of(1900, 1, 0), Arguments.of(1900, 1, 32), Arguments.of(1900, 2, 30),
                Arguments.of(1899, 2, 29), // 1899 is not a leap year
                Arguments.of(1900, 4, 31)
            );
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("invalidDateProvider")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dayOfMonth));
        }
    }

    @Nested
    @DisplayName("Field, Property, and Range Tests")
    class FieldAndPropertyTests {

        static Stream<Arguments> lengthOfMonthProvider() {
            return Stream.of(
                Arguments.of(1700, 2, 29), // Julian leap year
                Arguments.of(1751, 2, 28), // Not a leap year
                Arguments.of(1752, 2, 29), // Julian leap year
                Arguments.of(1752, 9, 19), // Cutover month: 2 days before gap + 17 days after = 19
                Arguments.of(1800, 2, 28), // Gregorian non-leap year
                Arguments.of(1900, 2, 28), // Gregorian non-leap year
                Arguments.of(2000, 2, 29)  // Gregorian leap year
            );
        }

        @ParameterizedTest(name = "{0}-{1} should have {2} days")
        @MethodSource("lengthOfMonthProvider")
        void lengthOfMonth_isCorrect(int year, int month, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> lengthOfYearProvider() {
            return Stream.of(
                Arguments.of(1700, 366), // Julian leap year
                Arguments.of(1751, 365), // Normal year
                Arguments.of(1752, 355), // Cutover year (366 - 11 days)
                Arguments.of(1753, 365), // Normal year
                Arguments.of(1800, 365), // Gregorian non-leap year
                Arguments.of(1900, 365), // Gregorian non-leap year
                Arguments.of(2000, 366)  // Gregorian leap year
            );
        }

        @ParameterizedTest(name = "Year {0} should have {1} days")
        @MethodSource("lengthOfYearProvider")
        void lengthOfYear_isCorrect(int year, int expectedLength) {
            assertEquals(expectedLength, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
        }

        static Stream<Arguments> rangeProvider() {
            return Stream.of(
                // DAY_OF_MONTH range is standard, except for leap years and cutover month
                Arguments.of(1700, 2, DAY_OF_MONTH, 1, 29), // Julian leap year
                Arguments.of(1752, 9, DAY_OF_MONTH, 1, 30), // Range is reported as full, though length is 19
                Arguments.of(2011, 2, DAY_OF_MONTH, 1, 28), // Standard non-leap
                // DAY_OF_YEAR range depends on year length
                Arguments.of(1700, 1, DAY_OF_YEAR, 1, 366),
                Arguments.of(1752, 1, DAY_OF_YEAR, 1, 355), // Cutover year
                Arguments.of(2011, 1, DAY_OF_YEAR, 1, 365),
                // ALIGNED_WEEK_OF_MONTH is affected by cutover
                Arguments.of(1752, 9, ALIGNED_WEEK_OF_MONTH, 1, 3), // Shortened month
                Arguments.of(2012, 2, ALIGNED_WEEK_OF_MONTH, 1, 5),
                // ALIGNED_WEEK_OF_YEAR is affected by cutover
                Arguments.of(1752, 12, ALIGNED_WEEK_OF_YEAR, 1, 51) // Shortened year
            );
        }

        @ParameterizedTest(name = "range of {3} in {0}-{1} is {4}-{5}")
        @MethodSource("rangeProvider")
        void range_forField_isCorrect(int year, int month, TemporalField field, int min, int max) {
            ValueRange expected = ValueRange.of(min, max);
            assertEquals(expected, BritishCutoverDate.of(year, month, 1).range(field));
        }

        static Stream<Arguments> getLongProvider() {
            return Stream.of(
                // --- Date: 1752-09-02 (pre-cutover) ---
                // Day of year for 1752-09-02 (Julian leap year): 31+29+31+30+31+30+31+31+2 = 246
                Arguments.of(1752, 9, 2, DAY_OF_YEAR, 246L),
                Arguments.of(1752, 9, 2, DAY_OF_WEEK, 3L), // Wednesday

                // --- Date: 1752-09-14 (post-cutover) ---
                // Day of year for 1752-09-14: 246 (for 1752-09-02) + 1 = 247. The 11-day gap is skipped.
                Arguments.of(1752, 9, 14, DAY_OF_YEAR, 247L),
                Arguments.of(1752, 9, 14, DAY_OF_WEEK, 4L), // Thursday

                // --- Date: 2014-05-26 (Gregorian) ---
                // Day of year for 2014-05-26: 31+28+31+30+26 = 146
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 146L),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L), // AD era
                Arguments.of(0, 6, 8, ERA, 0L)      // BC era
            );
        }

        @ParameterizedTest(name = "getLong({3}) for {0}-{1}-{2} should be {4}")
        @MethodSource("getLongProvider")
        void getLong_forField_isCorrect(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Arithmetic: Plus and Minus")
    class AdditionAndSubtractionTests {

        static LongStream dayAmounts() {
            return LongStream.of(0, 1, 35, -1, -60);
        }

        @ParameterizedTest
        @MethodSource("dayAmounts")
        void plusDays_addsCorrectNumberOfDays(long daysToAdd) {
            sampleEquivalentDates().forEach(args -> {
                BritishCutoverDate cutover = (BritishCutoverDate) args.get()[0];
                LocalDate iso = (LocalDate) args.get()[1];
                LocalDate expected = iso.plusDays(daysToAdd);
                assertEquals(expected, LocalDate.from(cutover.plus(daysToAdd, DAYS)),
                    () -> String.format("%s plus %d days", cutover, daysToAdd));
            });
        }

        @ParameterizedTest
        @MethodSource("dayAmounts")
        void minusDays_subtractsCorrectNumberOfDays(long daysToSubtract) {
            sampleEquivalentDates().forEach(args -> {
                BritishCutoverDate cutover = (BritishCutoverDate) args.get()[0];
                LocalDate iso = (LocalDate) args.get()[1];
                LocalDate expected = iso.minusDays(daysToSubtract);
                assertEquals(expected, LocalDate.from(cutover.minus(daysToSubtract, DAYS)),
                    () -> String.format("%s minus %d days", cutover, daysToSubtract));
            });
        }

        private static final Object[][] PLUS_MINUS_DATA = new Object[][] {
            // date, amount, unit, expected date, is_bidirectional
            { 1752, 9, 2, -1L, DAYS, 1752, 9, 1, true },
            { 1752, 9, 2, 1L, DAYS, 1752, 9, 14, true }, // Crosses the gap
            { 1752, 9, 14, -1L, DAYS, 1752, 9, 2, true }, // Crosses the gap backwards
            { 2014, 5, 26, 8L, DAYS, 2014, 6, 3, true },
            { 1752, 9, 2, 1L, WEEKS, 1752, 9, 20, true },
            { 1752, 9, 2, 1L, MONTHS, 1752, 10, 2, true },
            { 1752, 8, 12, 1L, MONTHS, 1752, 9, 23, false }, // Day-of-month adjustment due to gap
            { 2014, 5, 26, 3L, YEARS, 2017, 5, 26, true },
            { 2014, 5, 26, 3L, DECADES, 2044, 5, 26, true },
            { 2014, 5, 26, 3L, CENTURIES, 2314, 5, 26, true },
            { 2014, 5, 26, -1L, ERAS, -2013, 5, 26, true }
        };

        static Stream<Arguments> plusProvider() {
            return Arrays.stream(PLUS_MINUS_DATA).map(data -> Arguments.of(
                BritishCutoverDate.of((int) data[0], (int) data[1], (int) data[2]),
                (long) data[3],
                (TemporalUnit) data[4],
                BritishCutoverDate.of((int) data[5], (int) data[6], (int) data[7])
            ));
        }

        @ParameterizedTest
        @MethodSource("plusProvider")
        void plus_withAmountAndUnit_isCorrect(BritishCutoverDate start, long amount, TemporalUnit unit, BritishCutoverDate expected) {
            assertEquals(expected, start.plus(amount, unit));
        }

        static Stream<Arguments> minusProvider() {
            return Arrays.stream(PLUS_MINUS_DATA)
                .filter(data -> (boolean) data[8]) // Only use bidirectional data
                .map(data -> Arguments.of(
                    BritishCutoverDate.of((int) data[5], (int) data[6], (int) data[7]),
                    (long) data[3],
                    (TemporalUnit) data[4],
                    BritishCutoverDate.of((int) data[0], (int) data[1], (int) data[2])
                ));
        }

        @ParameterizedTest
        @MethodSource("minusProvider")
        void minus_withAmountAndUnit_isCorrect(BritishCutoverDate start, long amount, TemporalUnit unit, BritishCutoverDate expected) {
            assertEquals(expected, start.minus(amount, unit));
        }

        @Test
        void minus_withPeriod_isCorrect() {
            ChronoPeriod period = BritishCutoverChronology.INSTANCE.period(0, 2, 3);
            BritishCutoverDate start = BritishCutoverDate.of(2014, 5, 26);
            BritishCutoverDate expected = BritishCutoverDate.of(2014, 3, 23);
            assertEquals(expected, start.minus(period));
        }
    }

    @Nested
    @DisplayName("Arithmetic: `until` Calculations")
    class UntilTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.BritishCutoverChronologyTest#sampleEquivalentDates")
        void until_withEquivalentDates_returnsZero(BritishCutoverDate cutover, LocalDate iso) {
            // until(ChronoLocalDate)
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutover.until(iso));
            // until(Temporal)
            assertEquals(Period.ZERO, iso.until(cutover));
        }

        static Stream<Arguments> untilByUnitProvider() {
            return Stream.of(
                Arguments.of(date(1752, 9, 1), date(1752, 9, 14), DAYS, 2L), // Crosses gap
                Arguments.of(date(1752, 9, 14), date(1752, 9, 1), DAYS, -2L),
                Arguments.of(date(2014, 5, 26), date(2014, 6, 1), DAYS, 6L),
                Arguments.of(date(1752, 9, 1), date(1752, 9, 19), WEEKS, 1L),
                Arguments.of(date(1752, 9, 2), date(1752, 10, 2), MONTHS, 1L),
                Arguments.of(date(2014, 5, 26), date(2015, 5, 26), YEARS, 1L),
                Arguments.of(date(-2013, 5, 26), date(2014, 5, 26), ERAS, 1L)
            );
        }

        @ParameterizedTest
        @MethodSource("untilByUnitProvider")
        void until_withUnit_calculatesCorrectAmount(BritishCutoverDate start, BritishCutoverDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilAsPeriodProvider() {
            return Stream.of(
                // Start, End, Expected Period (Y, M, D)
                Arguments.of(date(1752, 7, 2), date(1752, 9, 14), period(0, 2, 1)),
                Arguments.of(date(1752, 9, 1), date(1752, 9, 14), period(0, 0, 2)),
                Arguments.of(date(1752, 9, 14), date(1752, 9, 1), period(0, 0, -2)),
                Arguments.of(date(1752, 9, 14), date(1752, 10, 14), period(0, 1, 0))
            );
        }

        @ParameterizedTest
        @MethodSource("untilAsPeriodProvider")
        void until_asPeriod_calculatesCorrectPeriodAndIsReversible(
                BritishCutoverDate start, BritishCutoverDate end, ChronoPeriod expectedPeriod) {
            ChronoPeriod actualPeriod = start.until(end);
            assertEquals(expectedPeriod, actualPeriod);
            assertEquals(end, start.plus(actualPeriod), "Adding the period should result in the end date");
        }
    }

    @Nested
    @DisplayName("`with` and Adjuster Tests")
    class WithAndAdjusterTests {

        static Stream<Arguments> withProvider() {
            return Stream.of(
                // --- Adjusting across the cutover gap ---
                // Adjusting DAY_OF_MONTH into the gap (leniently moves to next valid day)
                Arguments.of(date(1752, 9, 2), DAY_OF_MONTH, 3, date(1752, 9, 14)),
                // Adjusting DAY_OF_YEAR into the gap
                Arguments.of(date(1752, 9, 2), DAY_OF_YEAR, 247, date(1752, 9, 14)),
                // --- Standard adjustments ---
                Arguments.of(date(2014, 5, 26), DAY_OF_WEEK, 3, date(2014, 5, 28)),
                Arguments.of(date(2014, 5, 26), YEAR, 2012, date(2012, 5, 26)),
                Arguments.of(date(2012, 2, 29), YEAR, 2011, date(2011, 2, 28)), // Adjusts for non-leap year
                Arguments.of(date(2014, 5, 26), ERA, 0, date(-2013, 5, 26))
            );
        }

        @ParameterizedTest
        @MethodSource("withProvider")
        void with_fieldAndValue_adjustsCorrectly(BritishCutoverDate start, TemporalField field, long value, BritishCutoverDate expected) {
            assertEquals(expected, start.with(field, value));
        }

        @Test
        void with_lastDayOfMonth_adjustsCorrectly() {
            assertEquals(date(1752, 9, 30), date(1752, 9, 2).with(TemporalAdjusters.lastDayOfMonth()));
            assertEquals(date(2012, 2, 29), date(2012, 2, 1).with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_temporalAdjuster_adjustsCorrectly() {
            // Adjusting with an ISO date should adopt its epoch day
            assertEquals(date(1752, 9, 1), date(1752, 9, 14).with(LocalDate.of(1752, 9, 12)));
        }
    }

    @Nested
    @DisplayName("toString Tests")
    class ToStringTests {
        @Test
        void toString_returnsCorrectFormat() {
            assertEquals("BritishCutover AD 1-01-01", BritishCutoverDate.of(1, 1, 1).toString());
            assertEquals("BritishCutover AD 2012-06-23", BritishCutoverDate.of(2012, 6, 23).toString());
        }
    }

    // --- Helper methods for creating test data ---

    private static BritishCutoverDate date(int year, int month, int day) {
        return BritishCutoverDate.of(year, month, day);
    }

    private static ChronoPeriod period(int y, int m, int d) {
        return BritishCutoverChronology.INSTANCE.period(y, m, d);
    }
}