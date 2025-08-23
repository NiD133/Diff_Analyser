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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

// Assume these eras are available in the project's dependencies
import org.threeten.extra.chrono.AccountingEra;
import org.threeten.extra.chrono.CopticEra;
import org.threeten.extra.chrono.DiscordianEra;
import org.threeten.extra.chrono.EthiopicEra;
import org.threeten.extra.chrono.InternationalFixedEra;
import org.threeten.extra.chrono.JulianEra;
import org.threeten.extra.chrono.PaxEra;

@DisplayName("Symmetry454Chronology and Symmetry454Date")
class Symmetry454ChronologyTest {

    // Constants for month lengths, improving readability of test data.
    private static final int DAYS_IN_SHORT_MONTH = 28;
    private static final int DAYS_IN_LONG_MONTH = 35;
    private static final int WEEKS_IN_SHORT_MONTH = 4;
    private static final int WEEKS_IN_LONG_MONTH = 5;

    /**
     * Provides pairs of equivalent Symmetry454 and ISO dates for conversion tests.
     */
    static Object[][] provideSampleSymmetry454AndIsoDates() {
        return new Object[][] {
            {Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)}, // Constantine the Great, Roman emperor (d. 337)
            {Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)},
            {Symmetry454Date.of(272, 2, 27), LocalDate.of(272, 2, 24)}, // Charlemagne, Frankish king (d. 814)
            {Symmetry454Date.of(742, 3, 25), LocalDate.of(742, 4, 2)},
            {Symmetry454Date.of(742, 4, 2), LocalDate.of(742, 4, 7)}, // Norman Conquest: Battle of Hastings
            {Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)}, // Francesco Petrarca - Petrarch (d. 1374)
            {Symmetry454Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)},
            {Symmetry454Date.of(1304, 7, 20), LocalDate.of(1304, 7, 19)}, // Charles the Bold (d. 1477)
            {Symmetry454Date.of(1433, 11, 14), LocalDate.of(1433, 11, 10)},
            {Symmetry454Date.of(1433, 11, 10), LocalDate.of(1433, 11, 6)}, // Leonardo da Vinci (d. 1519)
            {Symmetry454Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)},
            {Symmetry454Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)}, // Christopher Columbus's expedition
            {Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)},
            {Symmetry454Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)}, // Galileo Galilei (d. 1642)
            {Symmetry454Date.of(1564, 2, 20), LocalDate.of(1564, 2, 15)},
            {Symmetry454Date.of(1564, 2, 15), LocalDate.of(1564, 2, 10)}, // William Shakespeare baptized (d. 1616)
            {Symmetry454Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)},
            {Symmetry454Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)}, // Sir Isaac Newton (d. 1727)
            {Symmetry454Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)},
            {Symmetry454Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)}, // Leonhard Euler (d. 1783)
            {Symmetry454Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)},
            {Symmetry454Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)}, // French Revolution: Storming of the Bastille
            {Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)},
            {Symmetry454Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)}, // Albert Einstein (d. 1955)
            {Symmetry454Date.of(1879, 3, 12), LocalDate.of(1879, 3, 14)},
            {Symmetry454Date.of(1879, 3, 14), LocalDate.of(1879, 3, 16)}, // Dennis MacAlistair Ritchie (d. 2011)
            {Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)}, // Unix time begins
            {Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)},
            {Symmetry454Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)}, // Start of the 21st century
            {Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1)},
            {Symmetry454Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3)}
        };
    }

    @Nested
    @DisplayName("Date Creation and Validation")
    class Creation {
        /**
         * Provides invalid date components (year, month, day) that should cause an exception.
         */
        static Object[][] provideInvalidDateComponents() {
            return new Object[][] {
                {-1, 13, 28}, {2000, 13, 1}, {2000, 1, 0}, {2000, 1, 29}, // Invalid month, day, or short month overflow
                {2000, 2, 36}, // Long month overflow
                {2004, 12, 36}  // Leap-week month overflow
            };
        }

        @ParameterizedTest
        @MethodSource("provideInvalidDateComponents")
        @DisplayName("of(y, m, d) should throw for invalid dates")
        void of_throwsForInvalidDate(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom));
        }

        /**
         * Provides years that are not leap years in the Symmetry454 calendar.
         */
        static Object[][] provideNonLeapYears() {
            return new Object[][] {{1}, {100}, {200}, {2000}};
        }

        @ParameterizedTest
        @MethodSource("provideNonLeapYears")
        @DisplayName("of(y, 12, 29) should throw for leap day in non-leap year")
        void of_throwsForLeapDayInNonLeapYear(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    @DisplayName("Conversions to and from other date types")
    class Conversions {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSampleSymmetry454AndIsoDates")
        @DisplayName("should convert to LocalDate")
        void convertsToLocalDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym454));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSampleSymmetry454AndIsoDates")
        @DisplayName("should convert from LocalDate")
        void convertsFromLocalDate(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Date.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSampleSymmetry454AndIsoDates")
        @DisplayName("should convert to epoch day")
        void convertsToEpochDay(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym454.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSampleSymmetry454AndIsoDates")
        @DisplayName("should create from epoch day")
        void createsFromEpochDay(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSampleSymmetry454AndIsoDates")
        @DisplayName("should create from a temporal accessor (LocalDate)")
        void createsFromTemporal(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Chronology.INSTANCE.date(iso));
        }
    }

    @Nested
    @DisplayName("Field, Range, and Property Access")
    class Access {
        /**
         * Provides dates and their expected month lengths.
         */
        static Object[][] provideDatesAndExpectedMonthLengths() {
            return new Object[][] {
                {2000, 1, 28, DAYS_IN_SHORT_MONTH},
                {2000, 2, 28, DAYS_IN_LONG_MONTH},
                {2000, 3, 28, DAYS_IN_SHORT_MONTH},
                {2004, 12, 20, DAYS_IN_LONG_MONTH} // Leap year December
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndExpectedMonthLengths")
        @DisplayName("lengthOfMonth() should return correct length")
        void lengthOfMonth_returnsCorrectLength(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, day).lengthOfMonth());
        }

        /**
         * Provides dates, fields, and their expected value ranges.
         */
        static Object[][] provideDatesAndFieldRanges() {
            return new Object[][] {
                {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
                {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
                {2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 35)}, // Leap year
                {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
                {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)}, // Normal year
                {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)}, // Leap year
                {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},
                {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
                {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},
                {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
                {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)} // Leap year
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndFieldRanges")
        @DisplayName("range() should return correct range for a given field")
        void range_returnsCorrectRange(int year, int month, int dom, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(year, month, dom).range(field));
        }

        /**
         * Provides dates, fields, and their expected long values.
         */
        static Object[][] provideDatesAndExpectedFieldValues() {
            return new Object[][] {
                // Date: 2014-05-26
                {2014, 5, 26, DAY_OF_WEEK, 5L},
                {2014, 5, 26, DAY_OF_MONTH, 26L},
                {2014, 5, 26, DAY_OF_YEAR, (long) DAYS_IN_SHORT_MONTH * 3 + DAYS_IN_LONG_MONTH + 26},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L},
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, (long) WEEKS_IN_SHORT_MONTH * 3 + WEEKS_IN_LONG_MONTH + 4},
                {2014, 5, 26, MONTH_OF_YEAR, 5L},
                {2014, 5, 26, PROLEPTIC_MONTH, 2014L * 12 + 5 - 1},
                {2014, 5, 26, YEAR, 2014L},
                {2014, 5, 26, ERA, 1L},
                // Date: 2015-12-35 (leap year, last day)
                {2015, 12, 35, DAY_OF_WEEK, 7L},
                {2015, 12, 35, DAY_OF_MONTH, 35L},
                {2015, 12, 35, DAY_OF_YEAR, 371L},
                {2015, 12, 35, ALIGNED_WEEK_OF_MONTH, 5L},
                {2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53L},
                {2015, 12, 35, MONTH_OF_YEAR, 12L},
                {2015, 12, 35, PROLEPTIC_MONTH, 2015L * 12 + 12 - 1},
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndExpectedFieldValues")
        @DisplayName("getLong() should return correct value for a given field")
        void getLong_returnsCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class Manipulation {
        /**
         * Provides test cases for plus() operations.
         */
        static Object[][] providePlusCases() {
            return new Object[][] {
                {2014, 5, 26, 0, DAYS, 2014, 5, 26},
                {2014, 5, 26, 8, DAYS, 2014, 5, 34},
                {2014, 5, 26, 3, WEEKS, 2014, 6, 12},
                {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
                {2014, 5, 26, 3, YEARS, 2017, 5, 26},
                {2014, 5, 26, 3, DECADES, 2044, 5, 26},
                {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
                {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
            };
        }

        @ParameterizedTest
        @MethodSource("providePlusCases")
        @DisplayName("plus() should add amounts correctly")
        void plus_addsAmount(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("providePlusCases")
        @DisplayName("minus() should subtract amounts correctly")
        void minus_subtractsAmount(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.minus(amount, unit));
        }

        /**
         * Provides test cases for with() adjustments.
         */
        static Object[][] provideWithAdjustmentCases() {
            return new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
                {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
                {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28},
                {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
                {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
                {2015, 12, 29, YEAR, 2014, 2014, 12, 28}, // Adjusting from leap week day to non-leap year
            };
        }

        @ParameterizedTest
        @MethodSource("provideWithAdjustmentCases")
        @DisplayName("with() should adjust fields correctly")
        void with_adjustsField(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        /**
         * Provides test cases for with() adjustments that should fail.
         */
        static Object[][] provideWithInvalidFieldValues() {
            return new Object[][] {
                {2013, 1, 1, DAY_OF_MONTH, 29}, {2013, 1, 1, DAY_OF_YEAR, 365}, {2013, 1, 1, MONTH_OF_YEAR, 14},
            };
        }

        @ParameterizedTest
        @MethodSource("provideWithInvalidFieldValues")
        @DisplayName("with() should throw for invalid field values")
        void with_throwsForInvalidValue(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom).with(field, value));
        }

        /**
         * Provides test cases for the lastDayOfMonth adjuster.
         */
        static Object[][] provideLastDayOfMonthCases() {
            return new Object[][] {
                {2012, 1, 23, 2012, 1, 28}, {2012, 2, 23, 2012, 2, 35}, {2009, 12, 23, 2009, 12, 35}, // Leap year
            };
        }

        @ParameterizedTest
        @MethodSource("provideLastDayOfMonthCases")
        @DisplayName("with(lastDayOfMonth) should return the last day of the month")
        void with_lastDayOfMonth(int y, int m, int d, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        @DisplayName("minus(Period) should throw for non-Symmetry454 period")
        void minus_throwsForIsoPeriod() {
            Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
            assertThrows(DateTimeException.class, () -> date.minus(Period.ofMonths(2)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSampleSymmetry454AndIsoDates")
        @DisplayName("until(self) should return a zero period")
        void until_self_isZeroPeriod(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454.until(sym454));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSampleSymmetry454AndIsoDates")
        @DisplayName("until(equivalent ISO date) should return a zero period")
        void until_equivalentIsoDate_isZeroPeriod(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454.until(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSampleSymmetry454AndIsoDates")
        @DisplayName("ISO date until(equivalent Symmetry454 date) should return a zero period")
        void isoDate_until_equivalentSymmetry454Date_isZeroPeriod(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(sym454));
        }

        /**
         * Provides test cases for until(end, unit).
         */
        static Object[][] provideUntilCases() {
            return new Object[][] {
                {2014, 5, 26, 2014, 5, 26, DAYS, 0},
                {2014, 5, 26, 2014, 6, 4, DAYS, 13},
                {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
                {2014, 5, 26, 2015, 5, 26, YEARS, 1},
                {2014, 5, 26, 2024, 5, 26, DECADES, 1},
                {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
                {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
                {2014, 5, 26, 3014, 5, 26, ERAS, 0},
            };
        }

        @ParameterizedTest
        @MethodSource("provideUntilCases")
        @DisplayName("until(end, unit) should calculate duration correctly")
        void until_calculatesDurationInUnit(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        /**
         * Provides test cases for until(end) returning a ChronoPeriod.
         */
        static Object[][] provideUntilPeriodCases() {
            return new Object[][] {
                {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
                {2014, 5, 26, 2014, 6, 4, 0, 0, 13},
                {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
                {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
                {2014, 5, 26, 2015, 5, 25, 0, 11, 27},
            };
        }

        @ParameterizedTest
        @MethodSource("provideUntilPeriodCases")
        @DisplayName("until(end) should calculate period correctly")
        void until_calculatesPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology-specific Methods")
    class ChronologySpecific {
        /**
         * Provides eras from other chronologies that are not supported.
         */
        static Object[][] provideUnsupportedEras() {
            return new Era[][] {
                {AccountingEra.BCE}, {CopticEra.AM}, {DiscordianEra.YOLD}, {EthiopicEra.INCARNATION},
                {HijrahEra.AH}, {InternationalFixedEra.CE}, {JapaneseEra.HEISEI}, {JulianEra.AD},
                {MinguoEra.ROC}, {PaxEra.CE}, {ThaiBuddhistEra.BE}
            };
        }

        @ParameterizedTest
        @MethodSource("provideUnsupportedEras")
        @DisplayName("prolepticYear() should throw for unsupported era types")
        void prolepticYear_throwsForUnsupportedEra(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("Object Behavior")
    class ObjectBehavior {
        /**
         * Provides dates and their expected string representations.
         */
        static Object[][] provideToStringCases() {
            return new Object[][] {
                {Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"},
                {Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"},
                {Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35"},
            };
        }

        @ParameterizedTest
        @MethodSource("provideToStringCases")
        @DisplayName("toString() should return correct format")
        void toString_returnsCorrectFormat(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}