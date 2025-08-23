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
import static org.junit.jupiter.params.provider.Arguments.arguments;

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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link Symmetry010Chronology} and {@link Symmetry010Date}.
 */
class Symmetry010ChronologyTest {

    /**
     * Provides pairs of Symmetry010 dates and their equivalent ISO LocalDates for conversion testing.
     * @return a stream of arguments: {Symmetry010Date, equivalent ISO LocalDate}
     */
    static Stream<Arguments> provideSymmetryAndIsoDatePairs() {
        return Stream.of(
            arguments(Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            arguments(Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)),
            arguments(Symmetry010Date.of(742, 3, 27), LocalDate.of(742, 4, 2)),
            arguments(Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
            arguments(Symmetry010Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)),
            arguments(Symmetry010Date.of(1433, 11, 12), LocalDate.of(1433, 11, 10)),
            arguments(Symmetry010Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)),
            arguments(Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
            arguments(Symmetry010Date.of(1564, 2, 18), LocalDate.of(1564, 2, 15)),
            arguments(Symmetry010Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)),
            arguments(Symmetry010Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)),
            arguments(Symmetry010Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)),
            arguments(Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
            arguments(Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)),
            arguments(Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9)),
            arguments(Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
            arguments(Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1))
        );
    }

    @Nested
    class ConversionTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void fromSymmetryDate_shouldCreateEquivalentLocalDate(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void fromLocalDate_shouldCreateEquivalentSymmetryDate(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry010Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void toEpochDay_shouldMatchIsoDate(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symmetryDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void chronologyDateFromTemporal_shouldCreateEquivalentSymmetryDate(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry010Chronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void chronologyDateEpochDay_shouldCreateEquivalentSymmetryDate(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry010Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }
    }

    @Nested
    class InvalidCreationTests {
        static Stream<Arguments> provideInvalidDateComponents() {
            return Stream.of(
                // Invalid month
                arguments(2000, 0, 1, "Month zero"),
                arguments(2000, -1, 1, "Negative month"),
                arguments(2000, 13, 1, "Month 13"),
                // Invalid day of month
                arguments(2000, 1, 0, "Day zero"),
                arguments(2000, 1, -1, "Negative day"),
                arguments(2000, 1, 31, "Day 31 in a 30-day month (Jan)"),
                arguments(2000, 2, 32, "Day 32 in a 31-day month (Feb)"),
                arguments(2000, 4, 31, "Day 31 in a 30-day month (Apr)"),
                arguments(2000, 12, 31, "Day 31 in a non-leap Dec"),
                arguments(2004, 12, 38, "Day 38 in a leap Dec")
            );
        }

        @ParameterizedTest(name = "[{index}] {3}: of({0}, {1}, {2})")
        @MethodSource("provideInvalidDateComponents")
        void of_shouldThrowExceptionForInvalidDateComponents(int year, int month, int day, String description) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, day));
        }

        static Stream<Arguments> provideNonLeapYears() {
            return Stream.of(
                arguments(1), arguments(100), arguments(200), arguments(2000)
            );
        }

        @ParameterizedTest
        @MethodSource("provideNonLeapYears")
        void of_shouldThrowExceptionForLeapDayInNonLeapYear(int year) {
            // Day 37 of month 12 is the leap day, only valid in a leap year.
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    @Nested
    class FieldAndPropertyTests {
        static Stream<Arguments> provideDatesAndExpectedMonthLengths() {
            return Stream.of(
                arguments(2000, 1, 30), // Jan
                arguments(2000, 2, 31), // Feb
                arguments(2000, 3, 30), // Mar
                arguments(2000, 4, 30), // Apr
                arguments(2000, 5, 31), // May
                arguments(2000, 6, 30), // Jun
                arguments(2000, 7, 30), // Jul
                arguments(2000, 8, 31), // Aug
                arguments(2000, 9, 30), // Sep
                arguments(2000, 10, 30), // Oct
                arguments(2000, 11, 31), // Nov
                arguments(2000, 12, 30), // Dec (non-leap)
                arguments(2004, 12, 37)  // Dec (leap)
            );
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndExpectedMonthLengths")
        void lengthOfMonth_shouldReturnCorrectValue(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> provideDateAndFieldValues() {
            return Stream.of(
                // date, field, expected value
                arguments(Symmetry010Date.of(2014, 5, 26), DAY_OF_WEEK, 2L),
                arguments(Symmetry010Date.of(2014, 5, 26), DAY_OF_MONTH, 26L),
                arguments(Symmetry010Date.of(2014, 5, 26), DAY_OF_YEAR, 147L), // 30+31+30+30+26
                arguments(Symmetry010Date.of(2014, 5, 26), ALIGNED_DAY_OF_WEEK_IN_MONTH, 5L),
                arguments(Symmetry010Date.of(2014, 5, 26), ALIGNED_WEEK_OF_MONTH, 4L),
                arguments(Symmetry010Date.of(2014, 5, 26), ALIGNED_DAY_OF_WEEK_IN_YEAR, 7L),
                arguments(Symmetry010Date.of(2014, 5, 26), ALIGNED_WEEK_OF_YEAR, 21L), // 4+5+4+4+4
                arguments(Symmetry010Date.of(2014, 5, 26), MONTH_OF_YEAR, 5L),
                arguments(Symmetry010Date.of(2014, 5, 26), PROLEPTIC_MONTH, 24172L), // 2014*12 + 5-1
                arguments(Symmetry010Date.of(2014, 5, 26), YEAR, 2014L),
                arguments(Symmetry010Date.of(2014, 5, 26), ERA, 1L),
                arguments(Symmetry010Date.of(2015, 12, 37), DAY_OF_WEEK, 5L),
                arguments(Symmetry010Date.of(2015, 12, 37), DAY_OF_MONTH, 37L),
                arguments(Symmetry010Date.of(2015, 12, 37), DAY_OF_YEAR, 371L), // 364 + 7
                arguments(Symmetry010Date.of(2015, 12, 37), ALIGNED_WEEK_OF_YEAR, 53L)
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndFieldValues")
        void getLong_shouldReturnCorrectValueForField(Symmetry010Date date, TemporalField field, long expected) {
            assertEquals(expected, date.getLong(field), "Value for " + field + " on date " + date);
        }

        static Stream<Arguments> provideDateAndFieldRanges() {
            return Stream.of(
                // date, field, expected range
                arguments(Symmetry010Date.of(2012, 1, 23), DAY_OF_MONTH, ValueRange.of(1, 30)),
                arguments(Symmetry010Date.of(2012, 2, 23), DAY_OF_MONTH, ValueRange.of(1, 31)),
                arguments(Symmetry010Date.of(2015, 12, 23), DAY_OF_MONTH, ValueRange.of(1, 37)), // Leap year
                arguments(Symmetry010Date.of(2012, 1, 23), DAY_OF_WEEK, ValueRange.of(1, 7)),
                arguments(Symmetry010Date.of(2012, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 364)), // Non-leap year
                arguments(Symmetry010Date.of(2015, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 371)), // Leap year
                arguments(Symmetry010Date.of(2012, 1, 23), MONTH_OF_YEAR, ValueRange.of(1, 12)),
                arguments(Symmetry010Date.of(2012, 2, 23), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                arguments(Symmetry010Date.of(2015, 12, 23), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
                arguments(Symmetry010Date.of(2012, 1, 23), ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                arguments(Symmetry010Date.of(2015, 12, 30), ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53))
            );
        }

        @ParameterizedTest
        @MethodSource("provideDateAndFieldRanges")
        void range_shouldReturnCorrectValueRangeForField(Symmetry010Date date, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, date.range(field), "Range for " + field + " on date " + date);
        }
    }

    @Nested
    class ManipulationTests {
        static Stream<Arguments> provideWithAdjustmentCases() {
            return Stream.of(
                // base date, field, new value, expected date
                arguments(Symmetry010Date.of(2014, 5, 26), DAY_OF_WEEK, 1, Symmetry010Date.of(2014, 5, 20)),
                arguments(Symmetry010Date.of(2014, 5, 26), DAY_OF_MONTH, 28, Symmetry010Date.of(2014, 5, 28)),
                arguments(Symmetry010Date.of(2014, 5, 26), DAY_OF_YEAR, 364, Symmetry010Date.of(2014, 12, 30)),
                arguments(Symmetry010Date.of(2014, 5, 26), ALIGNED_WEEK_OF_MONTH, 1, Symmetry010Date.of(2014, 5, 5)),
                arguments(Symmetry010Date.of(2014, 5, 26), MONTH_OF_YEAR, 4, Symmetry010Date.of(2014, 4, 26)),
                arguments(Symmetry010Date.of(2014, 5, 26), YEAR, 2012, Symmetry010Date.of(2012, 5, 26)),
                arguments(Symmetry010Date.of(2015, 12, 37), YEAR, 2004, Symmetry010Date.of(2004, 12, 37)),
                arguments(Symmetry010Date.of(2015, 12, 37), YEAR, 2013, Symmetry010Date.of(2013, 12, 30)) // Adjusts day for non-leap year
            );
        }

        @ParameterizedTest
        @MethodSource("provideWithAdjustmentCases")
        void with_shouldReturnAdjustedDate(Symmetry010Date baseDate, TemporalField field, long newValue, Symmetry010Date expectedDate) {
            assertEquals(expectedDate, baseDate.with(field, newValue));
        }

        static Stream<Arguments> provideInvalidWithAdjustmentCases() {
            return Stream.of(
                // field, invalid value
                arguments(DAY_OF_WEEK, 8),
                arguments(DAY_OF_MONTH, 31),
                arguments(DAY_OF_YEAR, 365), // For non-leap year
                arguments(MONTH_OF_YEAR, 14),
                arguments(YEAR, 1_000_001)
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidWithAdjustmentCases")
        void with_shouldThrowForInvalidFieldValue(TemporalField field, long invalidValue) {
            Symmetry010Date baseDate = Symmetry010Date.of(2013, 1, 1);
            assertThrows(DateTimeException.class, () -> baseDate.with(field, invalidValue));
        }

        static Stream<Arguments> provideLastDayOfMonthCases() {
            return Stream.of(
                arguments(Symmetry010Date.of(2012, 1, 23), Symmetry010Date.of(2012, 1, 30)),
                arguments(Symmetry010Date.of(2012, 2, 23), Symmetry010Date.of(2012, 2, 31)),
                arguments(Symmetry010Date.of(2009, 12, 23), Symmetry010Date.of(2009, 12, 37))
            );
        }

        @ParameterizedTest
        @MethodSource("provideLastDayOfMonthCases")
        void with_lastDayOfMonth_shouldReturnCorrectDate(Symmetry010Date base, Symmetry010Date expected) {
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }

        static Stream<Arguments> provideDatePlusAmountCases() {
            return Stream.of(
                // base date, amount, unit, expected date
                arguments(Symmetry010Date.of(2014, 5, 26), 8, DAYS, Symmetry010Date.of(2014, 6, 3)),
                arguments(Symmetry010Date.of(2014, 5, 26), -3, DAYS, Symmetry010Date.of(2014, 5, 23)),
                arguments(Symmetry010Date.of(2014, 5, 26), 3, WEEKS, Symmetry010Date.of(2014, 6, 16)),
                arguments(Symmetry010Date.of(2014, 5, 26), 3, MONTHS, Symmetry010Date.of(2014, 8, 26)),
                arguments(Symmetry010Date.of(2014, 5, 26), 3, YEARS, Symmetry010Date.of(2017, 5, 26)),
                arguments(Symmetry010Date.of(2014, 5, 26), 3, DECADES, Symmetry010Date.of(2044, 5, 26)),
                arguments(Symmetry010Date.of(2014, 5, 26), 3, CENTURIES, Symmetry010Date.of(2314, 5, 26)),
                arguments(Symmetry010Date.of(2014, 5, 26), 3, MILLENNIA, Symmetry010Date.of(5014, 5, 26)),
                // Cases crossing leap week
                arguments(Symmetry010Date.of(2015, 12, 28), 8, DAYS, Symmetry010Date.of(2015, 12, 36)),
                arguments(Symmetry010Date.of(2015, 12, 28), 3, WEEKS, Symmetry010Date.of(2016, 1, 12))
            );
        }

        @ParameterizedTest
        @MethodSource("provideDatePlusAmountCases")
        void plus_shouldReturnCorrectlyAddedDate(Symmetry010Date base, long amount, TemporalUnit unit, Symmetry010Date expected) {
            assertEquals(expected, base.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("provideDatePlusAmountCases")
        void minus_shouldReturnCorrectlySubtractedDate(Symmetry010Date expected, long amount, TemporalUnit unit, Symmetry010Date base) {
            assertEquals(expected, base.minus(amount, unit));
        }
    }

    @Nested
    class PeriodAndDurationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void until_withEquivalentDates_shouldReturnZero(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), symmetryDate.until(symmetryDate));
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), symmetryDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(symmetryDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void plus_and_minus_days_shouldBehaveLikeIso(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(symmetryDate.plus(35, DAYS)));
            assertEquals(isoDate.minusDays(60), LocalDate.from(symmetryDate.minus(60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSymmetryAndIsoDatePairs")
        void until_days_shouldCalculateCorrectDistance(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(0, symmetryDate.until(isoDate.plusDays(0), DAYS));
            assertEquals(1, symmetryDate.until(isoDate.plusDays(1), DAYS));
            assertEquals(-40, symmetryDate.until(isoDate.minusDays(40), DAYS));
        }

        static Stream<Arguments> provideUntilCases() {
            Symmetry010Date start = Symmetry010Date.of(2014, 5, 26);
            return Stream.of(
                // start, end, unit, expected amount
                arguments(start, Symmetry010Date.of(2014, 6, 4), DAYS, 9L),
                arguments(start, Symmetry010Date.of(2014, 5, 20), DAYS, -6L),
                arguments(start, Symmetry010Date.of(2014, 6, 5), WEEKS, 1L),
                arguments(start, Symmetry010Date.of(2014, 6, 26), MONTHS, 1L),
                arguments(start, Symmetry010Date.of(2015, 5, 26), YEARS, 1L),
                arguments(start, Symmetry010Date.of(2024, 5, 26), DECADES, 1L),
                arguments(start, Symmetry010Date.of(2114, 5, 26), CENTURIES, 1L),
                arguments(start, Symmetry010Date.of(3014, 5, 26), MILLENNIA, 1L),
                arguments(start, Symmetry010Date.of(3014, 5, 26), ERAS, 0L)
            );
        }

        @ParameterizedTest
        @MethodSource("provideUntilCases")
        void until_unit_shouldCalculateCorrectDuration(Symmetry010Date start, Symmetry010Date end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> provideUntilPeriodCases() {
            Symmetry010Date start = Symmetry010Date.of(2014, 5, 26);
            return Stream.of(
                // start, end, expected period (Y, M, D)
                arguments(start, Symmetry010Date.of(2014, 5, 26), Symmetry010Chronology.INSTANCE.period(0, 0, 0)),
                arguments(start, Symmetry010Date.of(2014, 6, 4), Symmetry010Chronology.INSTANCE.period(0, 0, 9)),
                arguments(start, Symmetry010Date.of(2014, 6, 26), Symmetry010Chronology.INSTANCE.period(0, 1, 0)),
                arguments(start, Symmetry010Date.of(2015, 5, 26), Symmetry010Chronology.INSTANCE.period(1, 0, 0)),
                arguments(start, Symmetry010Date.of(2015, 5, 25), Symmetry010Chronology.INSTANCE.period(0, 11, 29))
            );
        }

        @ParameterizedTest
        @MethodSource("provideUntilPeriodCases")
        void until_chronoPeriod_shouldCalculateCorrectPeriod(Symmetry010Date start, Symmetry010Date end, ChronoPeriod expected) {
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    class ChronologyApiTests {
        static Stream<Arguments> provideUnsupportedEras() {
            return Stream.of(
                arguments(AccountingEra.BCE), arguments(CopticEra.AM), arguments(DiscordianEra.YOLD),
                arguments(EthiopicEra.INCARNATION), arguments(HijrahEra.AH), arguments(InternationalFixedEra.CE),
                arguments(JapaneseEra.HEISEI), arguments(JulianEra.AD), arguments(MinguoEra.ROC),
                arguments(PaxEra.CE), arguments(ThaiBuddhistEra.BE)
            );
        }

        @ParameterizedTest
        @MethodSource("provideUnsupportedEras")
        void prolepticYear_shouldThrowExceptionForUnsupportedEra(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    class ObjectMethodTests {
        static Stream<Arguments> provideToStringCases() {
            return Stream.of(
                arguments(Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"),
                arguments(Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"),
                arguments(Symmetry010Date.of(2000, 8, 31), "Sym010 CE 2000/08/31"),
                arguments(Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37")
            );
        }

        @ParameterizedTest
        @MethodSource("provideToStringCases")
        void toString_shouldReturnCorrectFormatting(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }

        @Test
        void plus_isoPeriod_shouldThrowException() {
            Symmetry010Date baseDate = Symmetry010Date.of(2014, 5, 26);
            assertThrows(DateTimeException.class, () -> baseDate.plus(Period.ofMonths(2)));
        }
    }
}