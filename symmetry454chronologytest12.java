package org.threeten.extra.chrono;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

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

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link Symmetry454Chronology} and {@link Symmetry454Date}.
 */
@DisplayName("Symmetry454Chronology and Symmetry454Date Tests")
class Symmetry454ChronologyTest {

    /**
     * Provides sample pairs of equivalent dates in Symmetry454 and ISO calendar systems.
     */
    private static Stream<Object[]> provideSampleSymmetry454AndIsoDates() {
        return Stream.of(
            new Object[]{Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            new Object[]{Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)},
            new Object[]{Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)},
            new Object[]{Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)},
            new Object[]{Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)},
            new Object[]{Symmetry454Date.of(1879, 3, 14), LocalDate.of(1879, 3, 16)},
            new Object[]{Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)},
            new Object[]{Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)},
            new Object[]{Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1)}
        );
    }

    @Nested
    @DisplayName("Conversion Tests")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSampleSymmetry454AndIsoDates")
        @DisplayName("should convert from Symmetry454Date to LocalDate")
        void fromSymmetry454Date_toLocalDate(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(sym454Date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSampleSymmetry454AndIsoDates")
        @DisplayName("should convert from LocalDate to Symmetry454Date")
        void fromLocalDate_toSymmetry454Date(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(sym454Date, Symmetry454Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSampleSymmetry454AndIsoDates")
        @DisplayName("should convert to and from epoch day")
        void toAndFromEpochDay(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), sym454Date.toEpochDay());
            assertEquals(sym454Date, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSampleSymmetry454AndIsoDates")
        @DisplayName("should create Symmetry454Date from a TemporalAccessor")
        void createFromTemporalAccessor(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(sym454Date, Symmetry454Chronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Factory and Validation Tests")
    class FactoryAndValidationTests {

        private static Stream<Object[]> provideInvalidDateComponents() {
            return Stream.of(
                new Object[]{-1, 13, 28}, {2000, 0, 1}, {2000, 13, 1}, {2000, 1, 0},
                {2000, 1, 29}, // Jan has 28 days
                {2000, 2, 36}, // Feb has 35 days
                {2000, 3, 29}, // Mar has 28 days
                {2004, 12, 36} // 2004 is a leap year, Dec has 35 days
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidDateComponents")
        @DisplayName("of(y, m, d) with invalid components should throw DateTimeException")
        void of_withInvalidDate_shouldThrowException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, day));
        }

        /**
         * Provides years that are not leap years in the Symmetry454 calendar.
         * Year 200 was removed as it IS a leap year in this system.
         */
        private static Stream<Integer> provideNonLeapYears() {
            return Stream.of(1, 100, 2000);
        }

        @ParameterizedTest
        @MethodSource("provideNonLeapYears")
        @DisplayName("of(y, 12, 29) for a non-leap year should throw DateTimeException")
        void of_nonLeapYearDec29_shouldThrowException(int year) {
            // Day 29 of month 12 is only valid in a leap year.
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    @DisplayName("Field Access and Range Tests")
    class FieldAccessTests {

        private static Stream<Object[]> provideDatesAndExpectedMonthLengths() {
            return Stream.of(
                new Object[]{Symmetry454Date.of(2000, 1, 1), 28},
                new Object[]{Symmetry454Date.of(2000, 2, 1), 35},
                new Object[]{Symmetry454Date.of(2000, 3, 1), 28},
                new Object[]{Symmetry454Date.of(2000, 12, 1), 28}, // 2000 is not a leap year
                new Object[]{Symmetry454Date.of(2004, 12, 1), 35}  // 2004 is a leap year
            );
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndExpectedMonthLengths")
        @DisplayName("lengthOfMonth() should return correct length")
        void lengthOfMonth_shouldReturnCorrectLength(Symmetry454Date date, int expectedLength) {
            assertEquals(expectedLength, date.lengthOfMonth());
        }

        private static Stream<Object[]> provideFieldRangesForDates() {
            return Stream.of(
                new Object[]{Symmetry454Date.of(2012, 1, 23), DAY_OF_MONTH, ValueRange.of(1, 28)},
                new Object[]{Symmetry454Date.of(2012, 2, 23), DAY_OF_MONTH, ValueRange.of(1, 35)},
                new Object[]{Symmetry454Date.of(2015, 12, 23), DAY_OF_MONTH, ValueRange.of(1, 35)}, // 2015 is a leap year
                new Object[]{Symmetry454Date.of(2012, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 364)}, // 2012 is not a leap year
                new Object[]{Symmetry454Date.of(2015, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 371)}, // 2015 is a leap year
                new Object[]{Symmetry454Date.of(2012, 2, 23), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},
                new Object[]{Symmetry454Date.of(2015, 12, 30), ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)} // Leap week
            );
        }

        @ParameterizedTest
        @MethodSource("provideFieldRangesForDates")
        @DisplayName("range(field) should return correct value range")
        void range_forField_shouldReturnCorrectRange(Symmetry454Date date, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, date.range(field));
        }

        private static Stream<Object[]> provideDateAndFieldValues() {
            return Stream.of(
                new Object[]{2014, 5, 26, DAY_OF_WEEK, 5L},
                new Object[]{2014, 5, 26, DAY_OF_MONTH, 26L},
                // DAY_OF_YEAR for 2014-05-26: 28(Jan)+35(Feb)+28(Mar)+28(Apr)+26(May) = 145
                new Object[]{2014, 5, 26, DAY_OF_YEAR, 145L},
                new Object[]{2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L},
                // ALIGNED_WEEK_OF_YEAR for 2014-05-26: 4+5+4(Q1) + 4(Apr) + 4(May) = 21
                new Object[]{2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L},
                new Object[]{2014, 5, 26, MONTH_OF_YEAR, 5L},
                new Object[]{2014, 5, 26, PROLEPTIC_MONTH, (2014L * 12) + 5 - 1},
                new Object[]{2014, 5, 26, YEAR, 2014L},
                new Object[]{2014, 5, 26, ERA, 1L},
                // 2015 is a leap year, last day is 371
                new Object[]{2015, 12, 35, DAY_OF_YEAR, 371L},
                new Object[]{2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53L}
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndFieldValues")
        @DisplayName("getLong(field) should return correct value")
        void getLong_forField_shouldReturnCorrectValue(int y, int m, int d, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(y, m, d).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation Tests")
    class ManipulationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSampleSymmetry454AndIsoDates")
        @DisplayName("until(self) should return a zero period")
        void until_self_returnsZeroPeriod(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454Date.until(sym454Date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSampleSymmetry454AndIsoDates")
        @DisplayName("until(equivalentTemporal) should return a zero period")
        void until_equivalentTemporal_returnsZeroPeriod(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454Date.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSampleSymmetry454AndIsoDates")
        @DisplayName("isoDate.until(equivalentSymmetry454Date) should return a zero period")
        void isoDateUntil_equivalentSymmetry454Date_returnsZeroPeriod(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(sym454Date));
        }

        private static Stream<Object[]> provideWithAdjustmentCases() {
            return Stream.of(
                new Object[]{2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
                new Object[]{2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
                new Object[]{2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28},
                new Object[]{2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
                new Object[]{2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
                new Object[]{2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                // Adjusting month to one with fewer days truncates the day
                new Object[]{2015, 12, 29, MONTH_OF_YEAR, 1, 2015, 1, 28},
                // Adjusting to a valid day in a 35-day month
                new Object[]{2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29}
            );
        }

        @ParameterizedTest
        @MethodSource("provideWithAdjustmentCases")
        @DisplayName("with(field, value) should return adjusted date")
        void with_fieldAndValue_shouldReturnAdjustedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        private static Stream<Object[]> provideInvalidWithAdjustmentCases() {
            return Stream.of(
                new Object[]{2013, 1, 1, DAY_OF_MONTH, 0},
                new Object[]{2013, 1, 1, DAY_OF_MONTH, 29},
                new Object[]{2013, 1, 1, DAY_OF_WEEK, 0},
                new Object[]{2013, 1, 1, DAY_OF_WEEK, 8},
                new Object[]{2013, 1, 1, ALIGNED_WEEK_OF_MONTH, 0},
                new Object[]{2013, 1, 1, ALIGNED_WEEK_OF_MONTH, 5}, // Month 1 has 4 weeks
                new Object[]{2013, 1, 1, DAY_OF_YEAR, 365}, // Not a leap year
                new Object[]{2015, 1, 1, DAY_OF_YEAR, 372}, // Is a leap year (371 days)
                new Object[]{2013, 1, 1, YEAR, 1_000_001}
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidWithAdjustmentCases")
        @DisplayName("with(field, value) with invalid value should throw DateTimeException")
        void with_invalidFieldAndValue_shouldThrowException(int y, int m, int d, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(y, m, d).with(field, value));
        }

        private static Stream<Object[]> provideDatesForLastDayOfMonthAdjustment() {
            return Stream.of(
                new Object[]{2012, 1, 23, 2012, 1, 28},
                new Object[]{2012, 2, 23, 2012, 2, 35},
                new Object[]{2009, 12, 23, 2009, 12, 35} // 2009 is a leap year
            );
        }

        @ParameterizedTest
        @MethodSource("provideDatesForLastDayOfMonthAdjustment")
        @DisplayName("with(lastDayOfMonth) should return the last day of the month")
        void with_lastDayOfMonthAdjuster_shouldReturnLastDayOfMonth(int y, int m, int d, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.with(TemporalAdjusters.lastDayOfMonth()));
        }

        private static Stream<Object[]> providePlusMinusCases() {
            return Stream.of(
                new Object[]{2014, 5, 26, 8, DAYS, 2014, 5, 34},
                new Object[]{2014, 5, 26, 3, WEEKS, 2014, 6, 12},
                new Object[]{2014, 5, 26, 3, MONTHS, 2014, 8, 26},
                new Object[]{2014, 5, 26, 3, YEARS, 2017, 5, 26},
                new Object[]{2014, 5, 26, 3, DECADES, 2044, 5, 26},
                new Object[]{2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
                new Object[]{2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
                // Across year boundary
                new Object[]{2014, 12, 26, 3, WEEKS, 2015, 1, 19},
                // Across leap week
                new Object[]{2015, 12, 28, 8, DAYS, 2016, 1, 1},
                new Object[]{2015, 12, 28, 3, WEEKS, 2016, 1, 14}
            );
        }

        @ParameterizedTest
        @MethodSource("providePlusMinusCases")
        @DisplayName("plus(amount, unit) should add the correct amount")
        void plus_shouldAddCorrectAmount(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("providePlusMinusCases")
        @DisplayName("minus(amount, unit) should be the inverse of plus")
        void minus_isInverseOfPlus(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date end = Symmetry454Date.of(ey, em, ed);
            assertEquals(start, end.minus(amount, unit));
        }

        private static Stream<Object[]> provideUntilCases() {
            return Stream.of(
                new Object[]{2014, 5, 26, 2014, 5, 26, DAYS, 0L},
                new Object[]{2014, 5, 26, 2014, 6, 4, DAYS, 13L},
                new Object[]{2014, 5, 26, 2014, 5, 20, DAYS, -6L},
                new Object[]{2014, 5, 26, 2014, 6, 5, WEEKS, 1L},
                new Object[]{2014, 5, 26, 2014, 6, 26, MONTHS, 1L},
                new Object[]{2014, 5, 26, 2015, 5, 26, YEARS, 1L},
                new Object[]{2014, 5, 26, 2024, 5, 26, DECADES, 1L},
                new Object[]{2014, 5, 26, 2114, 5, 26, CENTURIES, 1L},
                new Object[]{2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L},
                new Object[]{2014, 5, 26, 3014, 5, 26, ERAS, 0L}
            );
        }

        @ParameterizedTest
        @MethodSource("provideUntilCases")
        @DisplayName("until(end, unit) should calculate correct amount")
        void until_withUnit_shouldCalculateAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        private static Stream<Object[]> provideUntilPeriodCases() {
            return Stream.of(
                new Object[]{2014, 5, 26, 2014, 5, 26, 0, 0, 0},
                new Object[]{2014, 5, 26, 2014, 6, 4, 0, 0, 13},
                new Object[]{2014, 5, 26, 2014, 5, 20, 0, 0, -6},
                new Object[]{2014, 5, 26, 2014, 6, 26, 0, 1, 0},
                new Object[]{2014, 5, 26, 2015, 5, 26, 1, 0, 0},
                new Object[]{2014, 5, 26, 2015, 5, 25, 0, 11, 27}
            );
        }

        @ParameterizedTest
        @MethodSource("provideUntilPeriodCases")
        @DisplayName("until(end) should calculate correct period")
        void until_withEndDate_shouldCalculatePeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology API Tests")
    class ChronologyApiTests {

        private static Stream<Era> provideUnsupportedEras() {
            return Stream.of(
                HijrahEra.AH, JapaneseEra.HEISEI, MinguoEra.ROC, ThaiBuddhistEra.BE
            );
        }

        @ParameterizedTest
        @MethodSource("provideUnsupportedEras")
        @DisplayName("prolepticYear() with an unsupported era should throw ClassCastException")
        void prolepticYear_withUnsupportedEra_shouldThrowException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 1));
        }

        @Test
        @DisplayName("range(field) for chronology should return correct ranges")
        void chronologyRange_shouldReturnCorrectRanges() {
            assertEquals(ValueRange.of(1, 7), Symmetry454Chronology.INSTANCE.range(DAY_OF_WEEK));
            assertEquals(ValueRange.of(1, 28, 35), Symmetry454Chronology.INSTANCE.range(DAY_OF_MONTH));
            assertEquals(ValueRange.of(1, 364, 371), Symmetry454Chronology.INSTANCE.range(DAY_OF_YEAR));
            assertEquals(ValueRange.of(1, 12), Symmetry454Chronology.INSTANCE.range(MONTH_OF_YEAR));
            assertEquals(ValueRange.of(1, 52, 53), Symmetry454Chronology.INSTANCE.range(ALIGNED_WEEK_OF_YEAR));
            assertEquals(ValueRange.of(-1_000_000L, 1_000_000), Symmetry454Chronology.INSTANCE.range(YEAR));
            assertEquals(ValueRange.of(0, 1), Symmetry454Chronology.INSTANCE.range(ERA));
        }
    }

    @Nested
    @DisplayName("toString() Tests")
    class ToStringTests {
        private static Stream<Object[]> provideDateAndStringRepresentation() {
            return Stream.of(
                new Object[]{Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"},
                new Object[]{Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"},
                new Object[]{Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35"}
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndStringRepresentation")
        @DisplayName("toString() should return the correct format")
        void toString_shouldReturnCorrectFormat(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }

    @Test
    @DisplayName("Equals and HashCode contract")
    void equalsAndHashCode() {
        new EqualsTester()
            .addEqualityGroup(Symmetry454Date.of(2014, 5, 26), Symmetry454Date.of(2014, 5, 26))
            .addEqualityGroup(Symmetry454Date.of(2014, 5, 27))
            .addEqualityGroup(Symmetry454Date.of(2014, 6, 26))
            .addEqualityGroup(Symmetry454Date.of(2015, 5, 26))
            .testEquals();
    }
}