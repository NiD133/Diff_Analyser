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
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Symmetry010Chronology} and {@link Symmetry010Date}.
 */
@DisplayName("Symmetry010Chronology and Symmetry010Date")
public class Symmetry010ChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> sampleSymmetryAndIsoDates() {
        return Stream.of(
            Arguments.of(Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry010Date.of(742, 3, 27), LocalDate.of(742, 4, 2)),
            Arguments.of(Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
            Arguments.of(Symmetry010Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)),
            Arguments.of(Symmetry010Date.of(1433, 11, 12), LocalDate.of(1433, 11, 10)),
            Arguments.of(Symmetry010Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)),
            Arguments.of(Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
            Arguments.of(Symmetry010Date.of(1564, 2, 18), LocalDate.of(1564, 2, 15)),
            Arguments.of(Symmetry010Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)),
            Arguments.of(Symmetry010Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)),
            Arguments.of(Symmetry010Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)),
            Arguments.of(Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
            Arguments.of(Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)),
            Arguments.of(Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9)),
            Arguments.of(Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
            Arguments.of(Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1))
        );
    }

    static Stream<Arguments> invalidDateProvider() {
        return Stream.of(
            Arguments.of(-1, 13, 28), Arguments.of(-1, 13, 29), Arguments.of(2000, -2, 1),
            Arguments.of(2000, 13, 1), Arguments.of(2000, 15, 1), Arguments.of(2000, 1, -1),
            Arguments.of(2000, 1, 0), Arguments.of(2000, 0, 1), Arguments.of(2000, -1, 0),
            Arguments.of(2000, -1, 1), Arguments.of(2000, 1, 31), Arguments.of(2000, 2, 32),
            Arguments.of(2000, 3, 31), Arguments.of(2000, 4, 31), Arguments.of(2000, 5, 32),
            Arguments.of(2000, 6, 31), Arguments.of(2000, 7, 31), Arguments.of(2000, 8, 32),
            Arguments.of(2000, 9, 31), Arguments.of(2000, 10, 31), Arguments.of(2000, 11, 32),
            Arguments.of(2000, 12, 31), Arguments.of(2004, 12, 38)
        );
    }

    static Stream<Arguments> invalidLeapDayProvider() {
        return Stream.of(
            Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(2000)
        );
    }

    static Stream<Arguments> nonSymmetryEraProvider() {
        return Stream.of(
            Arguments.of(AccountingEra.BCE), Arguments.of(AccountingEra.CE),
            Arguments.of(CopticEra.BEFORE_AM), Arguments.of(CopticEra.AM),
            Arguments.of(DiscordianEra.YOLD),
            Arguments.of(EthiopicEra.BEFORE_INCARNATION), Arguments.of(EthiopicEra.INCARNATION),
            Arguments.of(HijrahEra.AH),
            Arguments.of(InternationalFixedEra.CE),
            Arguments.of(JapaneseEra.MEIJI), Arguments.of(JapaneseEra.TAISHO),
            Arguments.of(JapaneseEra.SHOWA), Arguments.of(JapaneseEra.HEISEI),
            Arguments.of(JulianEra.BC), Arguments.of(JulianEra.AD),
            Arguments.of(MinguoEra.BEFORE_ROC), Arguments.of(MinguoEra.ROC),
            Arguments.of(PaxEra.BCE), Arguments.of(PaxEra.CE),
            Arguments.of(ThaiBuddhistEra.BEFORE_BE), Arguments.of(ThaiBuddhistEra.BE)
        );
    }

    static Stream<Arguments> rangeProvider() {
        return Stream.of(
            Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)),
            Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)),
            Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)),
            Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
            Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)),
            Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)),
            Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
            Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)),
            Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
            Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
            Arguments.of(2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
            Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)),
            Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
            Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53))
        );
    }

    static Stream<Arguments> fieldGetProvider() {
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 2),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 147),
            Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
            Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 24172),
            Arguments.of(2014, 5, 26, YEAR, 2014),
            Arguments.of(2014, 5, 26, ERA, 1),
            Arguments.of(2015, 12, 37, DAY_OF_WEEK, 5),
            Arguments.of(2015, 12, 37, DAY_OF_MONTH, 37),
            Arguments.of(2015, 12, 37, DAY_OF_YEAR, 371),
            Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_MONTH, 6),
            Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53),
            Arguments.of(2015, 12, 37, PROLEPTIC_MONTH, 24191)
        );
    }

    static Stream<Arguments> withFieldProvider() {
        return Stream.of(
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26),
            Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
            Arguments.of(2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26),
            Arguments.of(2015, 12, 37, YEAR, 2004, 2004, 12, 37),
            Arguments.of(2015, 12, 37, YEAR, 2013, 2013, 12, 30),
            Arguments.of(2015, 3, 28, DAY_OF_YEAR, 371, 2015, 12, 37),
            Arguments.of(2012, 3, 28, DAY_OF_YEAR, 364, 2012, 12, 30)
        );
    }

    static Stream<Arguments> withInvalidFieldProvider() {
        return Stream.of(
            Arguments.of(2013, 1, 1, DAY_OF_MONTH, 31),
            Arguments.of(2013, 6, 1, DAY_OF_MONTH, 31),
            Arguments.of(2015, 12, 1, DAY_OF_MONTH, 38),
            Arguments.of(2013, 1, 1, DAY_OF_YEAR, 365),
            Arguments.of(2015, 1, 1, DAY_OF_YEAR, 372),
            Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14),
            Arguments.of(2013, 1, 1, YEAR, 1_000_001)
        );
    }

    static Stream<Arguments> plusProvider() {
        return Stream.of(
            Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
            Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
            Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
            Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
            Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
            Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
            Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
            Arguments.of(2012, 6, 21, 52 + 1, WEEKS, 2013, 6, 28)
        );
    }

    static Stream<Arguments> plusLeapWeekProvider() {
        return Stream.of(
            Arguments.of(2015, 12, 28, 8, DAYS, 2015, 12, 36),
            Arguments.of(2015, 12, 28, 3, WEEKS, 2016, 1, 12),
            Arguments.of(2015, 12, 28, 3, MONTHS, 2016, 3, 28),
            Arguments.of(2015, 12, 28, 12, MONTHS, 2016, 12, 28),
            Arguments.of(2015, 12, 28, 3, YEARS, 2018, 12, 28)
        );
    }

    static Stream<Arguments> untilUnitProvider() {
        return Stream.of(
            Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 9),
            Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6),
            Arguments.of(2014, 5, 26, 2014, 6, 5, WEEKS, 1),
            Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1),
            Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1),
            Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1),
            Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1),
            Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1),
            Arguments.of(2014, 5, 26, 3014, 5, 26, ERAS, 0)
        );
    }

    static Stream<Arguments> untilPeriodProvider() {
        return Stream.of(
            Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
            Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 9),
            Arguments.of(2014, 5, 26, 2014, 5, 20, 0, 0, -6),
            Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
            Arguments.of(2014, 5, 26, 2015, 5, 25, 0, 11, 29),
            Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0)
        );
    }

    static Stream<Arguments> toStringProvider() {
        return Stream.of(
            Arguments.of(Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"),
            Arguments.of(Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"),
            Arguments.of(Symmetry010Date.of(2000, 8, 31), "Sym010 CE 2000/08/31"),
            Arguments.of(Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37")
        );
    }

    //-----------------------------------------------------------------------
    // Test Cases
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Conversions and Factory Tests")
    class ConversionTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("should convert from Symmetry010Date to LocalDate")
        void test_toLocalDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym010));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("should convert from LocalDate to Symmetry010Date")
        void test_fromLocalDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Date.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("should create date from epoch day")
        void test_dateFromEpochDay(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("should convert date to epoch day")
        void test_dateToEpochDay(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym010.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("should create date from a temporal accessor")
        void test_dateFromTemporal(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.date(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#invalidDateProvider")
        @DisplayName("of(y, m, d) should throw exception for invalid dates")
        void test_of_throwsForInvalidDate(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, day));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#invalidLeapDayProvider")
        @DisplayName("of(y, m, d) should throw exception for invalid leap day in non-leap year")
        void test_of_throwsForInvalidLeapDay(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    @Nested
    @DisplayName("Field Accessor and Range Tests")
    class FieldAccessorTests {
        @Test
        @DisplayName("lengthOfMonth() should return correct length for various months")
        void test_lengthOfMonth() {
            assertEquals(30, Symmetry010Date.of(2000, 1, 1).lengthOfMonth());
            assertEquals(31, Symmetry010Date.of(2000, 2, 1).lengthOfMonth());
            assertEquals(30, Symmetry010Date.of(2000, 12, 1).lengthOfMonth());
            assertEquals(37, Symmetry010Date.of(2004, 12, 1).lengthOfMonth()); // Leap year
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#rangeProvider")
        @DisplayName("range(field) should return correct value range")
        void test_range(int year, int month, int dom, TemporalField field, ValueRange expected) {
            Symmetry010Date date = Symmetry010Date.of(year, month, dom);
            assertEquals(expected, date.range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#fieldGetProvider")
        @DisplayName("getLong(field) should return correct field value")
        void test_getLong(int year, int month, int dom, TemporalField field, long expected) {
            Symmetry010Date date = Symmetry010Date.of(year, month, dom);
            assertEquals(expected, date.getLong(field));
        }
    }

    @Nested
    @DisplayName("Arithmetic and Adjustment Tests")
    class ArithmeticTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#withFieldProvider")
        @DisplayName("with(field, value) should adjust date correctly")
        void test_with(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            Symmetry010Date base = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            assertEquals(expected, base.with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#withInvalidFieldProvider")
        @DisplayName("with(field, value) should throw exception for invalid values")
        void test_with_throwsForInvalidValue(int y, int m, int d, TemporalField field, long value) {
            Symmetry010Date base = Symmetry010Date.of(y, m, d);
            assertThrows(DateTimeException.class, () -> base.with(field, value));
        }

        @Test
        @DisplayName("with(lastDayOfMonth) should adjust to the last day of the month")
        void test_with_lastDayOfMonth() {
            Symmetry010Date base = Symmetry010Date.of(2012, 2, 23);
            Symmetry010Date expected = Symmetry010Date.of(2012, 2, 31);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @ParameterizedTest
        @MethodSource({
            "org.threeten.extra.chrono.Symmetry010ChronologyTest#plusProvider",
            "org.threeten.extra.chrono.Symmetry010ChronologyTest#plusLeapWeekProvider"
        })
        @DisplayName("plus() and minus() should be inverse operations")
        void test_plusAndMinus(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry010Date base = Symmetry010Date.of(y, m, d);
            Symmetry010Date expected = Symmetry010Date.of(ey, em, ed);
            
            assertEquals(expected, base.plus(amount, unit));
            assertEquals(base, expected.minus(amount, unit));
        }

        @Test
        @DisplayName("plus(ChronoPeriod) should add the period correctly")
        void test_plus_period() {
            Symmetry010Date base = Symmetry010Date.of(2014, 5, 21);
            ChronoPeriod period = Symmetry010Chronology.INSTANCE.period(0, 2, 8);
            Symmetry010Date expected = Symmetry010Date.of(2014, 7, 29);
            assertEquals(expected, base.plus(period));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#untilUnitProvider")
        @DisplayName("until(endDate, unit) should calculate amount of time between dates")
        void test_until_withUnit(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#untilPeriodProvider")
        @DisplayName("until(endDate) should calculate the period between dates")
        void test_until_withPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            Symmetry010Date start = Symmetry010Date.of(y1, m1, d1);
            Symmetry010Date end = Symmetry010Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry010Chronology.INSTANCE.period(ey, em, ed);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#sampleSymmetryAndIsoDates")
        @DisplayName("until() should return zero for equivalent dates")
        void test_until_isZeroForSameDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(sym010));
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(iso));
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(sym010));
        }
    }

    @Nested
    @DisplayName("Era Tests")
    class EraRelatedTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#nonSymmetryEraProvider")
        @DisplayName("prolepticYear() should throw exception for non-Symmetry010 eras")
        void test_prolepticYear_throwsForInvalidEra(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("General Method Tests")
    class GeneralMethodTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#toStringProvider")
        @DisplayName("toString() should return the correct string representation")
        void test_toString(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}