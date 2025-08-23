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
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("BritishCutoverChronology and BritishCutoverDate")
class BritishCutoverChronologyTest {

    @Nested
    @DisplayName("Conversion to and from other date types")
    class ConversionTests {

        static Stream<Arguments> sampleDates() {
            return Stream.of(
                    Arguments.of(BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)),
                    Arguments.of(BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)),
                    Arguments.of(BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)),
                    Arguments.of(BritishCutoverDate.of(1582, 10, 4), LocalDate.of(1582, 10, 14)),
                    Arguments.of(BritishCutoverDate.of(1582, 10, 5), LocalDate.of(1582, 10, 15)),
                    Arguments.of(BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 13)),
                    // leniently accept invalid date and treat as Julian
                    Arguments.of(BritishCutoverDate.of(1752, 9, 3), LocalDate.of(1752, 9, 14)),
                    Arguments.of(BritishCutoverDate.of(1752, 9, 13), LocalDate.of(1752, 9, 24)),
                    // First Gregorian date
                    Arguments.of(BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 14)),
                    Arguments.of(BritishCutoverDate.of(2012, 7, 6), LocalDate.of(2012, 7, 6))
            );
        }

        @ParameterizedTest(name = "{index}: {0} -> {1}")
        @MethodSource("sampleDates")
        @DisplayName("converts to an equivalent ISO LocalDate")
        void toLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(cutoverDate));
        }

        @ParameterizedTest(name = "{index}: {1} -> {0}")
        @MethodSource("sampleDates")
        @DisplayName("converts from an ISO LocalDate")
        void fromLocalDate(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverDate.from(isoDate));
        }

        @ParameterizedTest(name = "{index}: epochDay({1}) -> {0}")
        @MethodSource("sampleDates")
        @DisplayName("creates from an epoch day")
        void fromEpochDay(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest(name = "{index}: {0}.toEpochDay() -> {1}")
        @MethodSource("sampleDates")
        @DisplayName("converts to an epoch day")
        void toEpochDay(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), cutoverDate.toEpochDay());
        }

        @ParameterizedTest(name = "{index}: date({1}) -> {0}")
        @MethodSource("sampleDates")
        @DisplayName("creates from a TemporalAccessor")
        void fromTemporal(BritishCutoverDate cutoverDate, LocalDate isoDate) {
            assertEquals(cutoverDate, BritishCutoverChronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Factory method of(year, month, day)")
    class FactoryMethodTests {

        static Stream<Arguments> invalidDates() {
            return Stream.of(
                    Arguments.of(1900, 0, 0), { 1900, 0, 0 },
                    Arguments.of(1900, -1, 1),
                    Arguments.of(1900, 0, 1),
                    Arguments.of(1900, 13, 1),
                    Arguments.of(1900, 1, 32),
                    Arguments.of(1900, 2, 30), // 1900 not a Gregorian leap year
                    Arguments.of(1899, 2, 29), // 1899 not a Julian leap year
                    Arguments.of(1900, 4, 31)
            );
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("invalidDates")
        @DisplayName("throws exception for invalid date components")
        void of_withInvalidDate_throwsException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, day));
        }
    }

    @Nested
    @DisplayName("Date properties")
    class PropertyTests {

        static Stream<Arguments> lengthOfMonthData() {
            return Stream.of(
                    Arguments.of(1700, 2, 29), // Julian leap year
                    Arguments.of(1751, 2, 28), // Julian non-leap year
                    Arguments.of(1752, 2, 29), // Julian leap year
                    Arguments.of(1752, 9, 19), // The cutover month
                    Arguments.of(1753, 2, 28), // Gregorian non-leap year
                    Arguments.of(1800, 2, 28), // Gregorian non-leap year
                    Arguments.of(2000, 2, 29)  // Gregorian leap year
            );
        }

        @ParameterizedTest(name = "Month {1} in {0} has {2} days")
        @MethodSource("lengthOfMonthData")
        @DisplayName("lengthOfMonth is correct")
        void lengthOfMonth_isCorrect(int year, int month, int length) {
            assertEquals(length, BritishCutoverDate.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> lengthOfYearData() {
            return Stream.of(
                    Arguments.of(1700, 366), // Julian leap year
                    Arguments.of(1751, 365), // Julian non-leap year
                    Arguments.of(1752, 355), // The cutover year (11 days removed)
                    Arguments.of(1753, 365), // Gregorian non-leap year
                    Arguments.of(1800, 365), // Gregorian non-leap year
                    Arguments.of(1900, 365), // Gregorian non-leap year
                    Arguments.of(2000, 366)  // Gregorian leap year
            );
        }

        @ParameterizedTest(name = "Year {0} has {1} days")
        @MethodSource("lengthOfYearData")
        @DisplayName("lengthOfYear is correct")
        void lengthOfYear_isCorrect(int year, int length) {
            assertEquals(length, BritishCutoverDate.of(year, 1, 1).lengthOfYear());
        }

        static Stream<Arguments> rangeData() {
            return Stream.of(
                    Arguments.of(1752, 9, 1, DAY_OF_MONTH, 1, 30),
                    Arguments.of(1752, 9, 1, DAY_OF_YEAR, 1, 355),
                    Arguments.of(1752, 9, 1, ALIGNED_WEEK_OF_MONTH, 1, 3),
                    Arguments.of(1752, 9, 1, ALIGNED_WEEK_OF_YEAR, 1, 51),
                    Arguments.of(2012, 1, 1, DAY_OF_YEAR, 1, 366),
                    Arguments.of(2011, 1, 1, DAY_OF_YEAR, 1, 365)
            );
        }

        @ParameterizedTest(name = "range of {3} for {0}-{1}-{2} is [{4}, {5}]")
        @MethodSource("rangeData")
        @DisplayName("range of a field is correct")
        void range_forField_returnsCorrectRange(int year, int month, int day, TemporalField field, long min, long max) {
            ValueRange expected = ValueRange.of(min, max);
            assertEquals(expected, BritishCutoverDate.of(year, month, day).range(field));
        }

        static Stream<Arguments> getLongData() {
            return Stream.of(
                    Arguments.of(1752, 9, 2, DAY_OF_WEEK, 3), // Wednesday
                    Arguments.of(1752, 9, 2, DAY_OF_MONTH, 2),
                    Arguments.of(1752, 9, 2, DAY_OF_YEAR, 246),
                    Arguments.of(1752, 9, 14, DAY_OF_WEEK, 4), // Thursday
                    Arguments.of(1752, 9, 14, DAY_OF_MONTH, 14),
                    Arguments.of(1752, 9, 14, DAY_OF_YEAR, 246 + 1), // Day after 9/2
                    Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 4),
                    Arguments.of(2014, 5, 26, YEAR, 2014),
                    Arguments.of(2014, 5, 26, ERA, 1),
                    Arguments.of(0, 6, 8, ERA, 0)
            );
        }

        @ParameterizedTest(name = "getLong({3}) for {0}-{1}-{2} is {4}")
        @MethodSource("getLongData")
        @DisplayName("getLong for a field returns correct value")
        void getLong_forField_returnsCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, BritishCutoverDate.of(year, month, day).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date manipulation")
    class ManipulationTests {

        static Stream<Arguments> withData() {
            return Stream.of(
                    // Adjusting a date just before the cutover
                    Arguments.of(1752, 9, 2, DAY_OF_WEEK, 4, 1752, 9, 14), // Set to Thursday, jumps over gap
                    Arguments.of(1752, 9, 2, DAY_OF_MONTH, 14, 1752, 9, 14),
                    Arguments.of(1752, 9, 2, DAY_OF_YEAR, 247, 1752, 9, 14), // 247th day of year
                    // Adjusting a date just after the cutover
                    Arguments.of(1752, 9, 14, DAY_OF_WEEK, 3, 1752, 9, 2), // Set to Wednesday, jumps back
                    Arguments.of(1752, 9, 14, DAY_OF_MONTH, 2, 1752, 9, 2),
                    Arguments.of(1752, 9, 14, DAY_OF_YEAR, 246, 1752, 9, 2), // 246th day of year
                    // Standard adjustments
                    Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                    Arguments.of(2012, 2, 29, YEAR, 2011, 2011, 2, 28), // Adjust to non-leap year
                    Arguments.of(2014, 5, 26, ERA, 0, -2013, 5, 26)
            );
        }

        @ParameterizedTest(name = "with({3}, {4}) on {0}-{1}-{2} -> {5}-{6}-{7}")
        @MethodSource("withData")
        @DisplayName("with(field, value) returns adjusted date")
        void with_field_returnsAdjustedDate(int y, int m, int d, TemporalField f, long val, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
            BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
            assertEquals(expected, start.with(f, val));
        }

        @Test
        @DisplayName("with(lastDayOfMonth) adjuster works correctly")
        void with_lastDayOfMonthAdjuster_returnsCorrectDate() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 30);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        @DisplayName("with(Temporal) adjuster works correctly")
        void with_temporalAdjuster_returnsCorrectDate() {
            BritishCutoverDate date = BritishCutoverDate.of(1752, 9, 2);
            LocalDate isoTarget = LocalDate.of(1752, 9, 14);
            BritishCutoverDate expected = BritishCutoverDate.of(1752, 9, 14);
            assertEquals(expected, date.with(isoTarget));
        }

        private static Stream<Arguments> plusMinusRawData() {
            return Stream.of(
                    // Arguments: startY, M, D, amount, unit, expectedY, M, D, isBidirectional
                    Arguments.of(1752, 9, 2, 1L, DAYS, 1752, 9, 14, true),
                    Arguments.of(1752, 9, 14, -1L, DAYS, 1752, 9, 2, true),
                    Arguments.of(1752, 9, 2, 1L, WEEKS, 1752, 9, 20, true),
                    Arguments.of(1752, 9, 2, 1L, MONTHS, 1752, 10, 2, true),
                    Arguments.of(1752, 8, 12, 1L, MONTHS, 1752, 9, 23, false), // Not bidirectional
                    Arguments.of(2014, 5, 26, 3L, YEARS, 2017, 5, 26, true),
                    Arguments.of(2014, 5, 26, 3L, DECADES, 2044, 5, 26, true),
                    Arguments.of(2014, 5, 26, 1L, CENTURIES, 2114, 5, 26, true),
                    Arguments.of(2014, 5, 26, 1L, MILLENNIA, 3014, 5, 26, true),
                    Arguments.of(2014, 5, 26, -1L, ERAS, -2013, 5, 26, true)
            );
        }

        static Stream<Arguments> plusData() {
            return plusMinusRawData().map(args -> {
                Object[] data = args.get();
                return Arguments.of(data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7]);
            });
        }

        static Stream<Arguments> minusData() {
            return plusMinusRawData()
                    .filter(args -> (Boolean) args.get()[8]) // Filter for bidirectional cases
                    .map(args -> {
                        Object[] data = args.get();
                        // Swap start and end dates for minus() test
                        return Arguments.of(data[5], data[6], data[7], data[3], data[4], data[0], data[1], data[2]);
                    });
        }

        @ParameterizedTest(name = "{0}-{1}-{2} plus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("plusData")
        @DisplayName("plus(amount, unit) adds duration correctly")
        void plus_addsAmountToDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
            BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest(name = "{0}-{1}-{2} minus {3} {4} -> {5}-{6}-{7}")
        @MethodSource("minusData")
        @DisplayName("minus(amount, unit) subtracts duration correctly")
        void minus_subtractsAmountFromDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y, m, d);
            BritishCutoverDate expected = BritishCutoverDate.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }
    }

    @Nested
    @DisplayName("Period calculation with until()")
    class UntilTests {

        @Test
        @DisplayName("until a date representing the same point in time returns zero")
        void until_sameDate_returnsZero() {
            BritishCutoverDate date = BritishCutoverDate.of(1900, 6, 1);
            LocalDate isoDate = LocalDate.from(date);

            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), date.until(date));
            assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), date.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(date));
        }

        static Stream<Arguments> untilTemporalUnitData() {
            return Stream.of(
                    Arguments.of(1752, 9, 2, 1752, 9, 14, DAYS, 1L),
                    Arguments.of(1752, 9, 14, 1752, 9, 2, DAYS, -1L),
                    Arguments.of(1752, 9, 2, 1752, 9, 20, WEEKS, 1L),
                    Arguments.of(1752, 9, 2, 1752, 10, 2, MONTHS, 1L),
                    Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
                    Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
                    Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
                    Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
                    Arguments.of(-2013, 5, 26, 2014, 5, 26, ERAS, 1L)
            );
        }

        @ParameterizedTest(name = "from {0}-{1}-{2} to {3}-{4}-{5} is {6} {5}")
        @MethodSource("untilTemporalUnitData")
        @DisplayName("until(end, unit) calculates amount between dates")
        void until_withTemporalUnit_calculatesAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodData() {
            // Arguments: startY, M, D, endY, M, D, expectedY, M, D
            return Stream.of(
                    // Across the cutover
                    Arguments.of(1752, 7, 2, 1752, 9, 14, 0, 2, 1),
                    Arguments.of(1752, 9, 2, 1752, 9, 14, 0, 0, 1),
                    Arguments.of(1752, 9, 14, 1752, 7, 14, 0, -2, 0),
                    // Standard period
                    Arguments.of(2020, 2, 20, 2022, 5, 25, 2, 3, 5)
            );
        }

        @ParameterizedTest(name = "Period from {0}-{1}-{2} to {3}-{4}-{5} is {6}Y {7}M {8}D")
        @MethodSource("untilPeriodData")
        @DisplayName("until(endDate) calculates period correctly and is reversible with plus(period)")
        void until_calculatesCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            BritishCutoverDate start = BritishCutoverDate.of(y1, m1, d1);
            BritishCutoverDate end = BritishCutoverDate.of(y2, m2, d2);

            ChronoPeriod period = start.until(end);
            assertEquals(BritishCutoverChronology.INSTANCE.period(ey, em, ed), period);

            // Verify that adding the period back to the start date gives the end date
            assertEquals(end, start.plus(period));
        }
    }

    @Nested
    @DisplayName("Era handling")
    class EraTests {

        @Test
        @DisplayName("era and year-of-era are consistent for years around the epoch")
        void eraAndYearOfEra_areConsistentForYearsAroundEpoch() {
            for (int year = -200; year < 200; year++) {
                if (year == 0) continue; // Proleptic year 0 is BC 1
                BritishCutoverDate base = BritishCutoverChronology.INSTANCE.date(year, 1, 1);
                assertEquals(year, base.get(YEAR));

                JulianEra era = (year < 1 ? JulianEra.BC : JulianEra.AD);
                assertEquals(era, base.getEra());

                int yoe = (year < 1 ? 1 - year : year);
                assertEquals(yoe, base.get(YEAR_OF_ERA));

                BritishCutoverDate eraBased = BritishCutoverChronology.INSTANCE.date(era, yoe, 1, 1);
                assertEquals(base, eraBased);
            }
        }
    }

    @Nested
    @DisplayName("String representation")
    class ToStringTests {

        static Stream<Arguments> toStringData() {
            return Stream.of(
                    Arguments.of(BritishCutoverDate.of(1, 1, 1), "BritishCutover AD 1-01-01"),
                    Arguments.of(BritishCutoverDate.of(2012, 6, 23), "BritishCutover AD 2012-06-23"),
                    Arguments.of(BritishCutoverDate.of(0, 12, 31), "BritishCutover BC 1-12-31")
            );
        }

        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("toStringData")
        @DisplayName("toString returns correctly formatted string")
        void toString_returnsFormattedString(BritishCutoverDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}