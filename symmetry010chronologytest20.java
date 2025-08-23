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
import static java.time.temporal.ChronoUnit.MINUTES;
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
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Symmetry010Chronology} and {@link Symmetry010Date}.
 */
@DisplayName("Symmetry010Chronology and Symmetry010Date")
public class Symmetry010ChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Object[][] sampleSymmetryAndIsoDates() {
        return new Object[][] {
            {Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)},
            {Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)},
            {Symmetry010Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)},
            {Symmetry010Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)},
            {Symmetry010Date.of(1564, 2, 15), LocalDate.of(1564, 2, 12)},
            {Symmetry010Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)},
            {Symmetry010Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)},
            {Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)},
            {Symmetry010Date.of(1941, 9, 9), LocalDate.of(1941, 9, 7)},
            {Symmetry010Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)},
            {Symmetry010Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3)},
        };
    }

    static Object[][] invalidDateComponents() {
        return new Object[][] {
            { -1, 13, 28 }, { 2000, 13, 1 }, { 2000, 1, 0 }, { 2000, 1, 31 },
            { 2000, 2, 32 }, { 2000, 4, 31 }, { 2004, 12, 38 }
        };
    }

    static Object[][] invalidLeapYearDay() {
        return new Object[][] { { 1 }, { 100 }, { 200 }, { 2000 } };
    }

    static Object[][] monthLengths() {
        return new Object[][] {
            { 2000, 1, 30 }, { 2000, 2, 31 }, { 2000, 3, 30 }, { 2000, 4, 30 },
            { 2000, 5, 31 }, { 2000, 6, 30 }, { 2000, 7, 30 }, { 2000, 8, 31 },
            { 2000, 9, 30 }, { 2000, 10, 30 }, { 2000, 11, 31 }, { 2000, 12, 30 },
            // 2004 is a leap year, so December has 37 days
            { 2004, 12, 37 }
        };
    }

    static Object[][] unsupportedEras() {
        return new Era[][] {
            { CopticEra.AM }, { DiscordianEra.YOLD }, { EthiopicEra.INCARNATION },
            { HijrahEra.AH }, { InternationalFixedEra.CE }, { JapaneseEra.HEISEI },
            { JulianEra.AD }, { MinguoEra.ROC }, { PaxEra.CE }, { ThaiBuddhistEra.BE }
        };
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Date Creation and Conversion")
    class CreationAndConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("conversion to and from LocalDate should be symmetric")
        void conversionToAndFromLocalDateIsSymmetric(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate));
            assertEquals(symmetryDate, Symmetry010Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("epoch-day conversion should be consistent")
        void epochDayConversionIsConsistent(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symmetryDate.toEpochDay());
            assertEquals(symmetryDate, Symmetry010Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("chronology.date(temporal) should create correct date")
        void chronologyDateFromTemporal(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry010Chronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#invalidDateComponents")
        @DisplayName("of(year, month, day) with invalid components should throw exception")
        void ofWithInvalidDateComponentsThrowsException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, day));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#invalidLeapYearDay")
        @DisplayName("of() with invalid day in a non-leap year should throw exception")
        void ofWithInvalidDayInNonLeapYearThrowsException(int year) {
            // Day 37 of month 12 is only valid in a leap year
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    @Nested
    @DisplayName("Field and Range Access")
    class FieldAndRangeTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#monthLengths")
        @DisplayName("lengthOfMonth() should return correct number of days")
        void lengthOfMonthReturnsCorrectValue(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, 1).lengthOfMonth());
        }

        static Object[][] fieldRanges() {
            return new Object[][] {
                { 2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30) },
                { 2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31) },
                { 2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37) }, // Leap year
                { 2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7) },
                { 2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364) },
                { 2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371) }, // Leap year
                { 2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12) },
                { 2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4) },
                { 2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4) }, // 31 days is still 4 full weeks + 3 days
                { 2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5) }, // 37 days is 5 full weeks + 2 days
                { 2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52) },
                { 2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53) }, // Leap year
            };
        }

        @ParameterizedTest
        @MethodSource("fieldRanges")
        @DisplayName("range(field) should return correct value range")
        void rangeForFieldReturnsCorrectValueRange(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, Symmetry010Date.of(year, month, dom).range(field));
        }

        static Object[][] fieldValues() {
            return new Object[][] {
                {2014, 5, 26, DAY_OF_WEEK, 2L},
                {2014, 5, 26, DAY_OF_MONTH, 26L},
                // DAY_OF_YEAR: 30(Jan)+31(Feb)+30(Mar)+30(Apr)+26(May) = 147
                {2014, 5, 26, DAY_OF_YEAR, 147L},
                {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L},
                // ALIGNED_WEEK_OF_YEAR: (147 - 1) / 7 + 1 = 21
                {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L},
                {2014, 5, 26, PROLEPTIC_MONTH, (2014L * 12) + 5 - 1},
                {2014, 5, 26, YEAR, 2014L},
                {2014, 5, 26, ERA, 1L},
                // 2012 is not a leap year in Symmetry010
                // DAY_OF_YEAR: 2*91(Q1,Q2) + 30(Jul)+31(Aug)+26(Sep) = 182 + 87 = 269
                {2012, 9, 26, DAY_OF_YEAR, 269L},
                // ALIGNED_WEEK_OF_YEAR: (269 - 1) / 7 + 1 = 39
                {2012, 9, 26, ALIGNED_WEEK_OF_YEAR, 39L},
                // 2015 is a leap year in Symmetry010, so it has 371 days
                {2015, 12, 37, DAY_OF_YEAR, 371L},
                // ALIGNED_WEEK_OF_YEAR: (371 - 1) / 7 + 1 = 53
                {2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53L},
            };
        }

        @ParameterizedTest
        @MethodSource("fieldValues")
        @DisplayName("getLong(field) should return correct value")
        void getLongForFieldReturnsCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry010Date.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Modification")
    class DateModificationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("plus(days) should add correct number of days")
        void plusDaysAddsCorrectly(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate.plusDays(1), LocalDate.from(symmetryDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(symmetryDate.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(symmetryDate.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("minus(days) should subtract correct number of days")
        void minusDaysSubtractsCorrectly(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate.minusDays(1), LocalDate.from(symmetryDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(symmetryDate.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(symmetryDate.minus(-60, DAYS)));
        }

        static Object[][] dateWithFieldAdjustments() {
            return new Object[][] {
                { 2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20 },
                { 2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28 },
                { 2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30 },
                { 2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26 },
                { 2014, 5, 26, YEAR, 2012, 2012, 5, 26 },
                { 2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26 },
                { 2015, 12, 37, YEAR, 2004, 2004, 12, 37 }, // 2004 is a leap year
                { 2015, 12, 37, YEAR, 2013, 2013, 12, 30 }, // 2013 not a leap year, day adjusted
            };
        }

        @ParameterizedTest
        @MethodSource("dateWithFieldAdjustments")
        @DisplayName("with(field, value) should return correctly adjusted date")
        void withFieldValueReturnsAdjustedDate(int y, int m, int d, TemporalField f, long val, int ey, int em, int ed) {
            Symmetry010Date initial = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, initial.with(f, val));
        }

        static Object[][] invalidDateWithFieldAdjustments() {
            return new Object[][] {
                { 2013, 1, 1, DAY_OF_MONTH, 31 }, { 2013, 6, 1, DAY_OF_MONTH, 31 },
                { 2015, 12, 1, DAY_OF_MONTH, 38 }, { 2013, 1, 1, DAY_OF_YEAR, 365 },
                { 2015, 1, 1, DAY_OF_YEAR, 372 }, { 2013, 1, 1, MONTH_OF_YEAR, 14 },
                { 2013, 1, 1, YEAR, 1_000_001 }
            };
        }

        @ParameterizedTest
        @MethodSource("invalidDateWithFieldAdjustments")
        @DisplayName("with(field, value) with an invalid value should throw exception")
        void withInvalidFieldValueThrowsException(int y, int m, int d, TemporalField f, long val) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(y, m, d).with(f, val));
        }

        static Object[][] lastDayOfMonthAdjustments() {
            return new Object[][] {
                { 2012, 1, 23, 2012, 1, 30 }, { 2012, 2, 23, 2012, 2, 31 },
                { 2012, 12, 23, 2012, 12, 30 }, { 2009, 12, 23, 2009, 12, 37 } // 2009 is a leap year
            };
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthAdjustments")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) should return correct date")
        void withLastDayOfMonthAdjusterReturnsCorrectDate(int y, int m, int d, int ey, int em, int ed) {
            Symmetry010Date initial = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, initial.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Period and Until")
    class PeriodAndUntilTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("until(otherDate, DAYS) should return correct number of days")
        void untilInDays(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(0, symmetryDate.until(isoDate.plusDays(0), DAYS));
            assertEquals(1, symmetryDate.until(isoDate.plusDays(1), DAYS));
            assertEquals(35, symmetryDate.until(isoDate.plusDays(35), DAYS));
            assertEquals(-40, symmetryDate.until(isoDate.minusDays(40), DAYS));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("iso.until(symmetryDate) should return zero period")
        void isoUntilSymmetryDate(Symmetry010Date symmetryDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(symmetryDate));
        }

        static Object[][] dateUntilAsPeriod() {
            return new Object[][] {
                { 2014, 5, 26, 2014, 5, 26, 0, 0, 0 },
                { 2014, 5, 26, 2014, 6, 4, 0, 0, 9 },
                { 2014, 5, 26, 2014, 6, 26, 0, 1, 0 },
                { 2014, 5, 26, 2015, 5, 26, 1, 0, 0 },
                { 2014, 5, 26, 2024, 5, 25, 9, 11, 29 },
            };
        }

        @ParameterizedTest
        @MethodSource("dateUntilAsPeriod")
        @DisplayName("until(endDate) should return correct ChronoPeriod")
        void untilReturnsCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry010Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }

        @Test
        @DisplayName("until() with an unsupported unit should throw exception")
        void untilWithUnsupportedUnitThrowsException() {
            Symmetry010Date start = Symmetry010Date.of(2012, 6, 28);
            Symmetry010Date end = Symmetry010Date.of(2012, 7, 1);
            assertThrows(UnsupportedTemporalTypeException.class, () -> start.until(end, MINUTES));
        }
    }

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApiTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#unsupportedEras")
        @DisplayName("prolepticYear() with an unsupported era should throw exception")
        void prolepticYearThrowsExceptionForUnsupportedEra(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("String Representation")
    class ToStringTests {
        static Object[][] dateToStringRepresentations() {
            return new Object[][] {
                { Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01" },
                { Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31" },
                { Symmetry010Date.of(2000, 8, 31), "Sym010 CE 2000/08/31" },
                { Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37" },
            };
        }

        @ParameterizedTest
        @MethodSource("dateToStringRepresentations")
        @DisplayName("toString() should return correct formatted string")
        void toStringReturnsCorrectFormatting(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}