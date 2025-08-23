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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Symmetry454Chronology Tests")
class Symmetry454ChronologyTest {

    // A sample date where Sym454 and ISO dates are the same, for easier reasoning in tests.
    private static final Symmetry454Date SAMPLE_DATE = Symmetry454Date.of(1941, 9, 9);
    private static final LocalDate SAMPLE_ISO_DATE = LocalDate.of(1941, 9, 9);

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Object[][] sampleSymmetry454AndIsoDates() {
        return new Object[][] {
            {Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)},
            {Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)},
            {Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)},
            {Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)},
            {Symmetry454Date.of(1879, 3, 12), LocalDate.of(1879, 3, 14)},
            {Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)},
            {Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)},
            {Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1)},
        };
    }

    @Nested
    @DisplayName("Factory and Conversion")
    class FactoryAndConversionTests {

        @DisplayName("should be consistent between Symmetry454Date and ISO LocalDate")
        @ParameterizedTest(name = "{index}: {0} <-> {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void conversionToAndFromIsoDateShouldBeConsistent(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertAll("Conversions between Sym454 and ISO",
                () -> assertEquals(isoDate, LocalDate.from(sym454Date), "Symmetry454Date -> LocalDate"),
                () -> assertEquals(sym454Date, Symmetry454Date.from(isoDate), "LocalDate -> Symmetry454Date"),
                () -> assertEquals(isoDate.toEpochDay(), sym454Date.toEpochDay(), "Symmetry454Date -> epochDay"),
                () -> assertEquals(sym454Date, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()), "epochDay -> Symmetry454Date"),
                () -> assertEquals(sym454Date, Symmetry454Chronology.INSTANCE.date(isoDate), "Chronology.date(TemporalAccessor) -> Symmetry454Date")
            );
        }

        // provides: year, month, dayOfMonth
        static Object[][] invalidDateParts() {
            return new Object[][] {
                {-1, 13, 28}, {2000, 13, 1}, {2000, 1, 0}, {2000, 1, 29}, // 28-day month
                {2000, 2, 36}, // 35-day month
                {2004, 12, 36}  // 35-day month in leap year
            };
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("invalidDateParts")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom));
        }

        // provides: year
        static Object[][] nonLeapYearsForDecember() {
            return new Object[][] {{1}, {100}, {200}, {2000}};
        }

        @ParameterizedTest(name = "of({0}, 12, 29)")
        @MethodSource("nonLeapYearsForDecember")
        void of_withInvalidLeapDayInNonLeapYear_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    @DisplayName("Date component access")
    class FieldAccessTests {

        // provides: year, month, expectedLength
        static Object[][] monthLengths() {
            return new Object[][] {
                {2000, 1, 28}, {2000, 2, 35}, {2000, 3, 28},
                {2000, 4, 28}, {2000, 5, 35}, {2000, 6, 28},
                {2000, 7, 28}, {2000, 8, 35}, {2000, 9, 28},
                {2000, 10, 28}, {2000, 11, 35}, {2000, 12, 28},
                {2004, 12, 35} // Leap year December
            };
        }

        @ParameterizedTest(name = "{0}-{1} should have {2} days")
        @MethodSource("monthLengths")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, 1).lengthOfMonth());
        }

        // provides: year, month, dom, field, expectedRange
        static Object[][] fieldRanges() {
            return new Object[][] {
                {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
                {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
                {2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
                {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
                {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)},
                {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)},
                {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},
                {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
                {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},
                {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
                {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)}
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2} range({3}) is {4}")
        @MethodSource("fieldRanges")
        void range_forField_shouldReturnCorrectValueRange(int year, int month, int dom, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(year, month, dom).range(field));
        }

        // provides: year, month, dom, field, expectedValue
        static Object[][] fieldValues() {
            return new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 5L},
                {2014, 5, 26, DAY_OF_MONTH, 26L},
                {2014, 5, 26, DAY_OF_YEAR, (long) 28 + 35 + 28 + 28 + 26},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L},
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4 + 5 + 4 + 4 + 4L},
                {2014, 5, 26, MONTH_OF_YEAR, 5L},
                {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1L},
                {2014, 5, 26, YEAR, 2014L},
                {2014, 5, 26, ERA, 1L},
                {2015, 12, 35, DAY_OF_WEEK, 7L},
                {2015, 12, 35, DAY_OF_YEAR, 371L},
                {2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53L}
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2} getLong({3}) is {4}")
        @MethodSource("fieldValues")
        void getLong_forField_shouldReturnCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class ManipulationTests {

        static Stream<Arguments> dayAmounts() {
            return Stream.of(Arguments.of(0), Arguments.of(1), Arguments.of(35), Arguments.of(-1), Arguments.of(-60));
        }

        @ParameterizedTest
        @MethodSource("dayAmounts")
        void plusDays_shouldAddCorrectNumberOfDays(int daysToAdd) {
            Symmetry454Date result = SAMPLE_DATE.plus(daysToAdd, DAYS);
            LocalDate expected = SAMPLE_ISO_DATE.plusDays(daysToAdd);
            assertEquals(expected, LocalDate.from(result));
        }

        @ParameterizedTest
        @MethodSource("dayAmounts")
        void minusDays_shouldSubtractCorrectNumberOfDays(int daysToSubtract) {
            Symmetry454Date result = SAMPLE_DATE.minus(daysToSubtract, DAYS);
            LocalDate expected = SAMPLE_ISO_DATE.minusDays(daysToSubtract);
            assertEquals(expected, LocalDate.from(result));
        }

        // provides: fromYear, fromMonth, fromDom, field, value, toYear, toMonth, toDom
        static Object[][] dateAdjustments() {
            return new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
                {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
                {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19},
                {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                {2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29}, // day of month adjusted
                {2015, 12, 29, YEAR, 2014, 2014, 12, 28} // day of month adjusted for leap
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("dateAdjustments")
        void with_forField_shouldReturnAdjustedDate(int year, int month, int dom, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry454Date initialDate = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expectedDate = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expectedDate, initialDate.with(field, value));
        }

        // provides: year, month, dom, field, invalidValue
        static Object[][] invalidDateAdjustments() {
            return new Object[][] {
                {2013, 1, 1, DAY_OF_MONTH, 29},
                {2013, 1, 1, DAY_OF_YEAR, 365},
                {2015, 1, 1, DAY_OF_YEAR, 372},
                {2013, 1, 1, MONTH_OF_YEAR, 14},
                {2013, 1, 1, YEAR, 1_000_001}
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) should fail")
        @MethodSource("invalidDateAdjustments")
        void with_forFieldWithInvalidValue_shouldThrowException(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom).with(field, value));
        }

        // provides: year, month, day, expectedYear, expectedMonth, expectedDay
        static Object[][] lastDayOfMonthDates() {
            return new Object[][] {
                {2012, 1, 23, 2012, 1, 28},
                {2012, 2, 23, 2012, 2, 35},
                {2009, 12, 23, 2009, 12, 35}
            };
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with lastDayOfMonth is {3}-{4}-{5}")
        @MethodSource("lastDayOfMonthDates")
        void with_lastDayOfMonthAdjuster_shouldReturnLastDayOfMonth(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
            Symmetry454Date base = Symmetry454Date.of(year, month, day);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Period and Duration")
    class PeriodTests {

        @Test
        void until_withSameDateAsTemporal_shouldReturnZeroPeriod() {
            Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
            LocalDate isoDate = LocalDate.from(date);

            assertAll("Period to self should be zero",
                () -> assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), date.until(date), "until self (Symmetry454Date)"),
                () -> assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), date.until(isoDate), "until self (LocalDate)"),
                () -> assertEquals(Period.ZERO, isoDate.until(date), "ISO until self (Symmetry454Date)")
            );
        }

        static Stream<Arguments> dayDifferenceAmounts() {
            return Stream.of(Arguments.of(0), Arguments.of(1), Arguments.of(35), Arguments.of(-40));
        }

        @ParameterizedTest
        @MethodSource("dayDifferenceAmounts")
        void until_withDaysUnit_shouldReturnCorrectDayDifference(int dayDifference) {
            long actual = SAMPLE_DATE.until(SAMPLE_ISO_DATE.plusDays(dayDifference), DAYS);
            assertEquals(dayDifference, actual);
        }

        // provides: startY, M, D, endY, M, D, unit, expectedAmount
        static Object[][] untilAmounts() {
            return new Object[][] {
                {2014, 5, 26, 2014, 6, 4, DAYS, 13L},
                {2014, 5, 26, 2014, 6, 5, WEEKS, 1L},
                {2014, 5, 26, 2014, 6, 26, MONTHS, 1L},
                {2014, 5, 26, 2015, 5, 26, YEARS, 1L},
                {2014, 5, 26, 2024, 5, 26, DECADES, 1L},
                {2014, 5, 26, 2114, 5, 26, CENTURIES, 1L},
                {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L},
                {2014, 5, 26, 3014, 5, 26, ERAS, 0L}
            };
        }

        @ParameterizedTest(name = "from {0}-{1}-{2} to {3}-{4}-{5} in {6} is {7}")
        @MethodSource("untilAmounts")
        void until_withTemporalUnit_shouldReturnCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        // provides: startY, M, D, endY, M, D, periodY, M, D
        static Object[][] untilPeriods() {
            return new Object[][] {
                {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
                {2014, 5, 26, 2014, 6, 4, 0, 0, 13},
                {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
                {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
                {2014, 5, 26, 2024, 5, 25, 9, 11, 27}
            };
        }

        @ParameterizedTest(name = "from {0}-{1}-{2} to {3}-{4}-{5} is P{6}Y{7}M{8}D")
        @MethodSource("untilPeriods")
        void until_withEndDate_shouldReturnCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology object")
    class ChronologyInstanceTests {

        @Test
        void chronology_of_shouldReturnSingletonInstance() {
            Chronology chrono = Chronology.of("Sym454");
            assertNotNull(chrono);
            assertEquals(Symmetry454Chronology.INSTANCE, chrono);
            assertEquals("Sym454", chrono.getId());
            assertEquals(null, chrono.getCalendarType());
        }

        // provides: unsupported Era
        static Object[][] unsupportedEras() {
            return new Object[][] {
                {AccountingEra.BCE}, {CopticEra.AM}, {DiscordianEra.YOLD},
                {EthiopicEra.INCARNATION}, {HijrahEra.AH}, {InternationalFixedEra.CE},
                {JapaneseEra.HEISEI}, {JulianEra.AD}, {MinguoEra.ROC},
                {PaxEra.CE}, {ThaiBuddhistEra.BE}
            };
        }

        @ParameterizedTest
        @MethodSource("unsupportedEras")
        void prolepticYear_withUnsupportedEra_shouldThrowException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }

        @Test
        void toString_shouldReturnFormattedDateString() {
            Symmetry454Date date = Symmetry454Date.of(1970, 2, 35);
            assertEquals("Sym454 CE 1970/02/35", date.toString());
        }
    }
}