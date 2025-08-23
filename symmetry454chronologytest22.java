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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Symmetry454Chronology} and {@link Symmetry454Date}.
 * This class focuses on conversion, creation, field access, and date arithmetic.
 */
public class Symmetry454ChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Object[][] sampleSymmetry454AndIsoDates() {
        return new Object[][] {
            {Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)},
            {Symmetry454Date.of(742, 3, 25), LocalDate.of(742, 4, 2)},
            {Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)},
            {Symmetry454Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)},
            {Symmetry454Date.of(1433, 11, 14), LocalDate.of(1433, 11, 10)},
            {Symmetry454Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)},
            {Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)},
            {Symmetry454Date.of(1564, 2, 20), LocalDate.of(1564, 2, 15)},
            {Symmetry454Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)},
            {Symmetry454Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)},
            {Symmetry454Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)},
            {Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)},
            {Symmetry454Date.of(1879, 3, 12), LocalDate.of(1879, 3, 14)},
            {Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)},
            {Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)},
            {Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1)},
        };
    }

    static Object[][] invalidDateProvider() {
        return new Object[][] {
            { -1, 13, 28 }, { -1, 13, 29 }, { 2000, -2, 1 }, { 2000, 13, 1 },
            { 2000, 15, 1 }, { 2000, 1, -1 }, { 2000, 1, 0 }, { 2000, 0, 1 },
            { 2000, -1, 0 }, { 2000, -1, 1 }, { 2000, 1, 29 }, { 2000, 2, 36 },
            { 2000, 3, 29 }, { 2000, 4, 29 }, { 2000, 5, 36 }, { 2000, 6, 29 },
            { 2000, 7, 29 }, { 2000, 8, 36 }, { 2000, 9, 29 }, { 2000, 10, 29 },
            { 2000, 11, 36 }, { 2000, 12, 29 }, { 2004, 12, 36 }
        };
    }

    static Object[][] nonSymmetry454LeapYearsProvider() {
        return new Object[][] { {1}, {100}, {200}, {2000} };
    }

    static Object[][] lengthOfMonthProvider() {
        return new Object[][] {
            { 2000, 1, 28, 28 }, { 2000, 2, 28, 35 }, { 2000, 3, 28, 28 },
            { 2000, 4, 28, 28 }, { 2000, 5, 28, 35 }, { 2000, 6, 28, 28 },
            { 2000, 7, 28, 28 }, { 2000, 8, 28, 35 }, { 2000, 9, 28, 28 },
            { 2000, 10, 28, 28 }, { 2000, 11, 28, 35 }, { 2000, 12, 28, 28 },
            { 2004, 12, 20, 35 }
        };
    }

    static Object[][] unsupportedErasProvider() {
        return new Object[][] {
            { AccountingEra.BCE }, { AccountingEra.CE }, { CopticEra.BEFORE_AM }, { CopticEra.AM },
            { DiscordianEra.YOLD }, { EthiopicEra.BEFORE_INCARNATION }, { EthiopicEra.INCARNATION },
            { HijrahEra.AH }, { InternationalFixedEra.CE }, { JapaneseEra.MEIJI }, { JapaneseEra.TAISHO },
            { JapaneseEra.SHOWA }, { JapaneseEra.HEISEI }, { JulianEra.BC }, { JulianEra.AD },
            { MinguoEra.BEFORE_ROC }, { MinguoEra.ROC }, { PaxEra.BCE }, { PaxEra.CE },
            { ThaiBuddhistEra.BEFORE_BE }, { ThaiBuddhistEra.BE }
        };
    }

    static Object[][] fieldRangeProvider() {
        return new Object[][] {
            // Leap Day and Year Day are members of months
            { 2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28) },
            { 2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35) },
            { 2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 35) },
            { 2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7) },
            { 2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364) },
            { 2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371) },
            { 2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12) },
            { 2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7) },
            { 2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4) },
            { 2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5) },
            { 2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5) },
            { 2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7) },
            { 2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52) },
            { 2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53) }
        };
    }

    static Object[][] getLongProvider() {
        return new Object[][] {
            // date(y, m, d), field, expected value
            { 2014, 5, 26, DAY_OF_WEEK, 5L },
            { 2014, 5, 26, DAY_OF_MONTH, 26L },
            // Day of year for 2014-05-26: Jan(28) + Feb(35) + Mar(28) + Apr(28) + 26 = 145
            { 2014, 5, 26, DAY_OF_YEAR, 145L },
            { 2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5L },
            { 2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L },
            { 2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5L },
            // Week of year for 2014-05-26: Jan(4) + Feb(5) + Mar(4) + Apr(4) + 4 = 21
            { 2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L },
            { 2014, 5, 26, MONTH_OF_YEAR, 5L },
            // Proleptic month for 2014-05-26: 2014 * 12 + 5 - 1 = 24172
            { 2014, 5, 26, PROLEPTIC_MONTH, 24172L },
            { 2014, 5, 26, YEAR, 2014L },
            { 2014, 5, 26, ERA, 1L },
            { 1, 5, 8, ERA, 1L },
            // Day of year for 2012-09-26: 3 quarters * 13 weeks/qtr * 7 days/week - 2 = 271
            { 2012, 9, 26, DAY_OF_YEAR, 271L },
            // Week of year for 2012-09-26: 3 quarters * 13 weeks/qtr = 39
            { 2012, 9, 26, ALIGNED_WEEK_OF_YEAR, 39L },
            // Day of year for 2015-12-35 (leap year): 4 quarters * 13 weeks/qtr * 7 days/week + 7 = 371
            { 2015, 12, 35, DAY_OF_YEAR, 371L },
            { 2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53L },
            // Proleptic month for 2015-12-35: (2015+1) * 12 - 1 = 24191
            { 2015, 12, 35, PROLEPTIC_MONTH, 24191L }
        };
    }

    static Object[][] withFieldProvider() {
        return new Object[][] {
            // date(y, m, d), field, value, expected(y, m, d)
            { 2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22 },
            { 2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28 },
            { 2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28 },
            { 2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5 },
            { 2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19 },
            { 2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26 },
            { 2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26 },
            { 2014, 5, 26, YEAR, 2012, 2012, 5, 26 },
            { 2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26 },
            { 2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 7, 2015, 12, 28 },
            { 2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7, 2015, 12, 28 },
            { 2015, 12, 29, ALIGNED_WEEK_OF_MONTH, 3, 2015, 12, 15 },
            { 2015, 12, 29, ALIGNED_WEEK_OF_YEAR, 3, 2015, 1, 15 },
            { 2015, 12, 28, DAY_OF_WEEK, 1, 2015, 12, 22 },
            { 2015, 12, 29, DAY_OF_MONTH, 3, 2015, 12, 3 },
            { 2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29 },
            { 2015, 12, 29, YEAR, 2014, 2014, 12, 28 },
            { 2015, 3, 28, DAY_OF_YEAR, 371, 2015, 12, 35 },
            { 2012, 3, 28, DAY_OF_YEAR, 364, 2012, 12, 28 }
        };
    }

    static Object[][] withInvalidFieldProvider() {
        return new Object[][] {
            { 2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, 8 },
            { 2013, 1, 1, ALIGNED_WEEK_OF_MONTH, 5 },
            { 2013, 1, 1, ALIGNED_WEEK_OF_YEAR, 53 },
            { 2015, 1, 1, ALIGNED_WEEK_OF_YEAR, 54 },
            { 2013, 1, 1, DAY_OF_WEEK, 8 },
            { 2013, 1, 1, DAY_OF_MONTH, 29 },
            { 2015, 12, 1, DAY_OF_MONTH, 36 },
            { 2013, 1, 1, DAY_OF_YEAR, 365 },
            { 2015, 1, 1, DAY_OF_YEAR, 372 },
            { 2013, 1, 1, MONTH_OF_YEAR, 14 },
            { 2013, 1, 1, EPOCH_DAY, 364_523_156L },
            { 2013, 1, 1, YEAR, 1_000_001 }
        };
    }

    static Object[][] lastDayOfMonthProvider() {
        return new Object[][] {
            { 2012, 1, 23, 2012, 1, 28 }, { 2012, 2, 23, 2012, 2, 35 },
            { 2012, 3, 23, 2012, 3, 28 }, { 2012, 4, 23, 2012, 4, 28 },
            { 2012, 5, 23, 2012, 5, 35 }, { 2012, 6, 23, 2012, 6, 28 },
            { 2012, 7, 23, 2012, 7, 28 }, { 2012, 8, 23, 2012, 8, 35 },
            { 2012, 9, 23, 2012, 9, 28 }, { 2012, 10, 23, 2012, 10, 28 },
            { 2012, 11, 23, 2012, 11, 35 }, { 2012, 12, 23, 2012, 12, 28 },
            { 2009, 12, 23, 2009, 12, 35 }
        };
    }

    static Object[][] plusProvider() {
        return new Object[][] {
            // start(y, m, d), amount, unit, expected(y, m, d)
            { 2014, 5, 26, 0, DAYS, 2014, 5, 26 },
            { 2014, 5, 26, 8, DAYS, 2014, 5, 34 },
            { 2014, 5, 26, -3, DAYS, 2014, 5, 23 },
            { 2014, 5, 26, 3, WEEKS, 2014, 6, 12 },
            { 2014, 5, 26, 3, MONTHS, 2014, 8, 26 },
            { 2014, 5, 26, 3, YEARS, 2017, 5, 26 },
            { 2014, 5, 26, 3, DECADES, 2044, 5, 26 },
            { 2014, 5, 26, 3, CENTURIES, 2314, 5, 26 },
            { 2014, 5, 26, 3, MILLENNIA, 5014, 5, 26 },
            { 2014, 12, 26, 3, WEEKS, 2015, 1, 19 },
            { 2013, 6, 21, 6 * 52 + 1, WEEKS, 2019, 6, 21 }
        };
    }

    static Object[][] plusWithLeapWeekProvider() {
        return new Object[][] {
            { 2015, 12, 28, 8, DAYS, 2016, 1, 1 },
            { 2015, 12, 28, 3, WEEKS, 2016, 1, 14 },
            { 2015, 12, 28, 52, WEEKS, 2016, 12, 21 },
            { 2015, 12, 28, 3, MONTHS, 2016, 3, 28 },
            { 2015, 12, 28, 12, MONTHS, 2016, 12, 28 },
            { 2015, 12, 28, 3, YEARS, 2018, 12, 28 }
        };
    }

    static Object[][] untilUnitProvider() {
        return new Object[][] {
            // start(y, m, d), end(y, m, d), unit, expected
            { 2014, 5, 26, 2014, 5, 26, DAYS, 0L },
            { 2014, 5, 26, 2014, 6, 4, DAYS, 13L },
            { 2014, 5, 26, 2014, 5, 20, DAYS, -6L },
            { 2014, 5, 26, 2014, 6, 5, WEEKS, 1L },
            { 2014, 5, 26, 2014, 6, 26, MONTHS, 1L },
            { 2014, 5, 26, 2015, 5, 26, YEARS, 1L },
            { 2014, 5, 26, 2024, 5, 26, DECADES, 1L },
            { 2014, 5, 26, 2114, 5, 26, CENTURIES, 1L },
            { 2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L },
            { 2014, 5, 26, 3014, 5, 26, ERAS, 0L }
        };
    }

    static Object[][] untilPeriodProvider() {
        return new Object[][] {
            // start(y, m, d), end(y, m, d), period(y, m, d)
            { 2014, 5, 26, 2014, 5, 26, 0, 0, 0 },
            { 2014, 5, 26, 2014, 6, 4, 0, 0, 13 },
            { 2014, 5, 26, 2014, 5, 20, 0, 0, -6 },
            { 2014, 5, 26, 2014, 6, 26, 0, 1, 0 },
            { 2014, 5, 26, 2015, 5, 26, 1, 0, 0 },
            { 2014, 5, 26, 2024, 5, 25, 9, 11, 27 }
        };
    }

    static Object[][] toStringProvider() {
        return new Object[][] {
            { Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01" },
            { Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35" },
            { Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35" },
            { Symmetry454Date.of(1970, 12, 35), "Sym454 CE 1970/12/35" }
        };
    }

    @Nested
    class InteroperabilityWithIsoTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void fromSymmetry454Date_toLocalDate_isCorrect(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym454));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void fromLocalDate_toSymmetry454Date_isCorrect(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Date.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void toEpochDay_isCorrect(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym454.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void until_equivalentLocalDate_returnsZeroPeriod(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454.until(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void isoDate_until_equivalentSymmetry454Date_returnsZeroPeriod(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(sym454));
        }
    }

    @Nested
    class FactoryAndValidationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void chronology_dateEpochDay_isCorrect(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void chronology_dateFromTemporal_isCorrect(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(sym454, Symmetry454Chronology.INSTANCE.date(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#invalidDateProvider")
        void of_withInvalidDateParts_throwsException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#nonSymmetry454LeapYearsProvider")
        void of_withDay29InDecemberInNonLeapYear_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    class FieldAccessTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#lengthOfMonthProvider")
        void lengthOfMonth_isCorrect(int year, int month, int day, int length) {
            assertEquals(length, Symmetry454Date.of(year, month, day).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#lengthOfMonthProvider")
        void lengthOfMonth_fromFirstDay_isCorrect(int year, int month, int day, int length) {
            assertEquals(length, Symmetry454Date.of(year, month, 1).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#fieldRangeProvider")
        void range_forField_isCorrect(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, Symmetry454Date.of(year, month, dom).range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#getLongProvider")
        void getLong_forField_isCorrect(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field));
        }
    }

    @Nested
    class DateArithmeticTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void plusDays_isCorrect(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym454.plus(0, DAYS)));
            assertEquals(iso.plusDays(1), LocalDate.from(sym454.plus(1, DAYS)));
            assertEquals(iso.plusDays(35), LocalDate.from(sym454.plus(35, DAYS)));
            assertEquals(iso.plusDays(-1), LocalDate.from(sym454.plus(-1, DAYS)));
            assertEquals(iso.plusDays(-60), LocalDate.from(sym454.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void minusDays_isCorrect(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym454.minus(0, DAYS)));
            assertEquals(iso.minusDays(1), LocalDate.from(sym454.minus(1, DAYS)));
            assertEquals(iso.minusDays(35), LocalDate.from(sym454.minus(35, DAYS)));
            assertEquals(iso.minusDays(-1), LocalDate.from(sym454.minus(-1, DAYS)));
            assertEquals(iso.minusDays(-60), LocalDate.from(sym454.minus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#withFieldProvider")
        void with_fieldAndValue_isCorrect(int year, int month, int dom, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#withInvalidFieldProvider")
        void with_invalidFieldValue_throwsException(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom).with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#lastDayOfMonthProvider")
        void with_lastDayOfMonthAdjuster_isCorrect(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
            Symmetry454Date base = Symmetry454Date.of(year, month, day);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#plusProvider")
        void plus_withUnit_isCorrect(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#plusWithLeapWeekProvider")
        void plus_withUnitAcrossLeapWeek_isCorrect(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#plusProvider")
        void minus_withUnit_isCorrect(int resultYear, int resultMonth, int resultDom, long amount, TemporalUnit unit, int startYear, int startMonth, int startDom) {
            Symmetry454Date start = Symmetry454Date.of(startYear, startMonth, startDom);
            Symmetry454Date expected = Symmetry454Date.of(resultYear, resultMonth, resultDom);
            assertEquals(expected, start.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#plusWithLeapWeekProvider")
        void minus_withUnitAcrossLeapWeek_isCorrect(int resultYear, int resultMonth, int resultDom, long amount, TemporalUnit unit, int startYear, int startMonth, int startDom) {
            Symmetry454Date start = Symmetry454Date.of(startYear, startMonth, startDom);
            Symmetry454Date expected = Symmetry454Date.of(resultYear, resultMonth, resultDom);
            assertEquals(expected, start.minus(amount, unit));
        }

        @Test
        void plus_withPeriod_isCorrect() {
            Symmetry454Date start = Symmetry454Date.of(2014, 5, 21);
            ChronoPeriod period = Symmetry454Chronology.INSTANCE.period(0, 2, 8);
            Symmetry454Date expected = Symmetry454Date.of(2014, 8, 1);
            assertEquals(expected, start.plus(period));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void until_withDaysUnit_isCorrect(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(0, sym454.until(iso.plusDays(0), DAYS));
            assertEquals(1, sym454.until(iso.plusDays(1), DAYS));
            assertEquals(35, sym454.until(iso.plusDays(35), DAYS));
            assertEquals(-40, sym454.until(iso.minusDays(40), DAYS));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#untilUnitProvider")
        void until_withUnit_isCorrect(int year1, int month1, int dom1, int year2, int month2, int dom2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(year1, month1, dom1);
            Symmetry454Date end = Symmetry454Date.of(year2, month2, dom2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#untilPeriodProvider")
        void until_withEndDate_returnsCorrectPeriod(int year1, int month1, int dom1, int year2, int month2, int dom2, int yearPeriod, int monthPeriod, int dayPeriod) {
            Symmetry454Date start = Symmetry454Date.of(year1, month1, dom1);
            Symmetry454Date end = Symmetry454Date.of(year2, month2, dom2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(yearPeriod, monthPeriod, dayPeriod);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleSymmetry454AndIsoDates")
        void until_sameSymmetry454Date_returnsZeroPeriod(Symmetry454Date sym454, LocalDate iso) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), sym454.until(sym454));
        }
    }

    @Nested
    class EraHandlingTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#unsupportedErasProvider")
        void prolepticYear_withUnsupportedEra_throwsException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    class StringRepresentationTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#toStringProvider")
        void toString_returnsCorrectFormat(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}