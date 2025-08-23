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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
class Symmetry010ChronologyTest {

    //-----------------------------------------------------------------------
    // Test Date Creation and Validation
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideInvalidDateParts")
        @DisplayName("of() with invalid date parts should throw exception")
        void of_withInvalidDateParts_throwsException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dayOfMonth));
        }

        @ParameterizedTest(name = "of({0}, 12, 37)")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideNonLeapYears")
        @DisplayName("of() with leap day in non-leap year should throw exception")
        void of_withInvalidLeapDayInNonLeapYear_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    //-----------------------------------------------------------------------
    // Test Equivalence with ISO Calendar (LocalDate)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Equivalence with ISO Calendar")
    class EquivalenceWithIsoDateTests {

        @ParameterizedTest(name = "{index}: {0} <=> {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideSampleSymmetryAndIsoDates")
        @DisplayName("Symmetry010Date and its equivalent ISO LocalDate should be consistent")
        void symmetryAndIsoDatesShouldBeEquivalent(Symmetry010Date sym010Date, LocalDate isoDate) {
            // Test conversions between calendar systems
            assertEquals(isoDate, LocalDate.from(sym010Date));
            assertEquals(sym010Date, Symmetry010Date.from(isoDate));
            assertEquals(sym010Date, Symmetry010Chronology.INSTANCE.date(isoDate));

            // Test epoch day conversions
            assertEquals(isoDate.toEpochDay(), sym010Date.toEpochDay());
            assertEquals(sym010Date, Symmetry010Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));

            // Test "until" calculations between equivalent dates
            assertEquals(Period.ZERO, isoDate.until(sym010Date));
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010Date.until(isoDate));
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010Date.until(sym010Date));
        }
    }

    //-----------------------------------------------------------------------
    // Test Date Properties
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Property Tests")
    class DatePropertyTests {

        @ParameterizedTest(name = "{0}-{1}-{2} -> {3} days")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideDatesAndExpectedMonthLengths")
        @DisplayName("lengthOfMonth() should return correct length")
        void lengthOfMonth_returnsCorrectLength(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, day).lengthOfMonth());
            // Also test from the first day of the month
            assertEquals(expectedLength, Symmetry010Date.of(year, month, 1).lengthOfMonth());
        }
    }

    //-----------------------------------------------------------------------
    // Test Chronology-specific methods
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Chronology Method Tests")
    class ChronologyMethodTests {

        @Test
        @DisplayName("isLeapYear() should return correct results for various years")
        void isLeapYear_forVariousYears_returnsCorrectly() {
            assertTrue(Symmetry010Chronology.INSTANCE.isLeapYear(3));
            assertFalse(Symmetry010Chronology.INSTANCE.isLeapYear(6));
            assertTrue(Symmetry010Chronology.INSTANCE.isLeapYear(9));
            assertFalse(Symmetry010Chronology.INSTANCE.isLeapYear(2000));
            assertTrue(Symmetry010Chronology.INSTANCE.isLeapYear(2004));
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideUnsupportedEras")
        @DisplayName("prolepticYear() with an unsupported Era should throw exception")
        void prolepticYear_withUnsupportedEra_throwsException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    //-----------------------------------------------------------------------
    // Test Field Accessors (get/range)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field Accessor Tests")
    class FieldAccessorTests {

        @ParameterizedTest(name = "{0}-{1}-{2}, {3} -> {4}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideDateAndFieldRanges")
        @DisplayName("range() for a field should return the correct value range")
        void range_forField_returnsCorrectRange(int year, int month, int dom, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry010Date.of(year, month, dom).range(field));
        }

        @ParameterizedTest(name = "{0}-{1}-{2}, {3} -> {4}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideDateAndFieldValues")
        @DisplayName("getLong() for a field should return the correct value")
        void getLong_forField_returnsCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry010Date.of(year, month, dom).getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    // Test Date Manipulation (with/plus/minus)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Manipulation Tests")
    class ManipulationTests {

        @ParameterizedTest(name = "({0}-{1}-{2}).with({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideWithAdjustmentData")
        @DisplayName("with() using a field should return correctly adjusted date")
        void with_usingField_returnsAdjustedDate(int year, int month, int dom, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry010Date baseDate = Symmetry010Date.of(year, month, dom);
            Symmetry010Date expectedDate = Symmetry010Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expectedDate, baseDate.with(field, value));
        }

        @ParameterizedTest(name = "({0}-{1}-{2}).with({3}, {4}) throws")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideWithInvalidAdjustmentData")
        @DisplayName("with() using a field with an invalid value should throw exception")
        void with_usingFieldWithInvalidValue_throwsException(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dom).with(field, value));
        }

        @ParameterizedTest(name = "lastDayOfMonth for {0}-{1}-{2} -> {3}-{4}-{5}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideDatesForLastDayOfMonthAdjustment")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) should return correct date")
        void with_lastDayOfMonthAdjuster_returnsCorrectDate(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
            Symmetry010Date baseDate = Symmetry010Date.of(year, month, day);
            Symmetry010Date expectedDate = Symmetry010Date.of(expectedYear, expectedMonth, expectedDay);
            assertEquals(expectedDate, baseDate.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @ParameterizedTest(name = "({0}-{1}-{2}).plus({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#providePlusData")
        void plus_withUnit_returnsCorrectDate(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry010Date baseDate = Symmetry010Date.of(year, month, dom);
            Symmetry010Date expectedDate = Symmetry010Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expectedDate, baseDate.plus(amount, unit));
        }

        @ParameterizedTest(name = "({0}-{1}-{2}).plus({3}, {4}) -> {5}-{6}-{7}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#providePlusDataAcrossLeapWeek")
        void plus_withUnitAcrossLeapWeek_returnsCorrectDate(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry010Date baseDate = Symmetry010Date.of(year, month, dom);
            Symmetry010Date expectedDate = Symmetry010Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expectedDate, baseDate.plus(amount, unit));
        }

        @ParameterizedTest(name = "({5}-{6}-{7}).minus({3}, {4}) -> {0}-{1}-{2}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#providePlusData")
        void minus_withUnit_returnsCorrectDate(int expectedYear, int expectedMonth, int expectedDom, long amount, TemporalUnit unit, int year, int month, int dom) {
            Symmetry010Date baseDate = Symmetry010Date.of(year, month, dom);
            Symmetry010Date expectedDate = Symmetry010Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expectedDate, baseDate.minus(amount, unit));
        }

        @ParameterizedTest(name = "({5}-{6}-{7}).minus({3}, {4}) -> {0}-{1}-{2}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#providePlusDataAcrossLeapWeek")
        void minus_withUnitAcrossLeapWeek_returnsCorrectDate(int expectedYear, int expectedMonth, int expectedDom, long amount, TemporalUnit unit, int year, int month, int dom) {
            Symmetry010Date baseDate = Symmetry010Date.of(year, month, dom);
            Symmetry010Date expectedDate = Symmetry010Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expectedDate, baseDate.minus(amount, unit));
        }
    }

    //-----------------------------------------------------------------------
    // Test Period and Duration (until)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Period and Duration Tests")
    class PeriodAndDurationTests {

        @ParameterizedTest(name = "({0}-{1}-{2}).until({3}-{4}-{5}, {6}) -> {7}")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideUntilData")
        @DisplayName("until() with a unit should return the correct amount")
        void until_withUnit_returnsCorrectAmount(int year1, int month1, int dom1, int year2, int month2, int dom2, TemporalUnit unit, long expected) {
            Symmetry010Date start = Symmetry010Date.of(year1, month1, dom1);
            Symmetry010Date end = Symmetry010Date.of(year2, month2, dom2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest(name = "({0}-{1}-{2}).until({3}-{4}-{5}) -> P{6}Y{7}M{8}D")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideUntilPeriodData")
        @DisplayName("until() should return the correct ChronoPeriod")
        void until_returnsCorrectChronoPeriod(int year1, int month1, int dom1, int year2, int month2, int dom2, int expectedYears, int expectedMonths, int expectedDays) {
            Symmetry010Date start = Symmetry010Date.of(year1, month1, dom1);
            Symmetry010Date end = Symmetry010Date.of(year2, month2, dom2);
            ChronoPeriod expectedPeriod = Symmetry010Chronology.INSTANCE.period(expectedYears, expectedMonths, expectedDays);
            assertEquals(expectedPeriod, start.until(end));
        }
    }

    //-----------------------------------------------------------------------
    // Test Object methods (toString, equals, hashCode)
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Object Method Tests")
    class ObjectMethodTests {

        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideDatesForToString")
        @DisplayName("toString() should return correctly formatted string")
        void toString_returnsCorrectlyFormattedString(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    static Stream<Arguments> provideSampleSymmetryAndIsoDates() {
        return Stream.of(
            Arguments.of(Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry010Date.of(272, 2, 27), LocalDate.of(272, 2, 26)),
            Arguments.of(Symmetry010Date.of(742, 3, 27), LocalDate.of(742, 4, 2)),
            Arguments.of(Symmetry010Date.of(742, 4, 2), LocalDate.of(742, 4, 7)),
            Arguments.of(Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
            Arguments.of(Symmetry010Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)),
            Arguments.of(Symmetry010Date.of(1304, 7, 20), LocalDate.of(1304, 7, 19)),
            Arguments.of(Symmetry010Date.of(1433, 11, 12), LocalDate.of(1433, 11, 10)),
            Arguments.of(Symmetry010Date.of(1433, 11, 10), LocalDate.of(1433, 11, 8)),
            Arguments.of(Symmetry010Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)),
            Arguments.of(Symmetry010Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)),
            Arguments.of(Symmetry010Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
            Arguments.of(Symmetry010Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)),
            Arguments.of(Symmetry010Date.of(1564, 2, 18), LocalDate.of(1564, 2, 15)),
            Arguments.of(Symmetry010Date.of(1564, 2, 15), LocalDate.of(1564, 2, 12)),
            Arguments.of(Symmetry010Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)),
            Arguments.of(Symmetry010Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)),
            Arguments.of(Symmetry010Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)),
            Arguments.of(Symmetry010Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)),
            Arguments.of(Symmetry010Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)),
            Arguments.of(Symmetry010Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)),
            Arguments.of(Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
            Arguments.of(Symmetry010Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)),
            Arguments.of(Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)),
            Arguments.of(Symmetry010Date.of(1941, 9, 11), LocalDate.of(1941, 9, 9)),
            Arguments.of(Symmetry010Date.of(1941, 9, 9), LocalDate.of(1941, 9, 7)),
            Arguments.of(Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
            Arguments.of(Symmetry010Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)),
            Arguments.of(Symmetry010Date.of(1999, 12, 29), LocalDate.of(2000, 1, 1)),
            Arguments.of(Symmetry010Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3))
        );
    }

    static Stream<Arguments> provideInvalidDateParts() {
        return Stream.of(
            // year, month, dayOfMonth
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

    static Stream<Arguments> provideNonLeapYears() {
        return Stream.of(Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(2000));
    }

    static Stream<Arguments> provideDatesAndExpectedMonthLengths() {
        return Stream.of(
            // year, month, day, expectedLength
            Arguments.of(2000, 1, 28, 30), Arguments.of(2000, 2, 28, 31), Arguments.of(2000, 3, 28, 30),
            Arguments.of(2000, 4, 28, 30), Arguments.of(2000, 5, 28, 31), Arguments.of(2000, 6, 28, 30),
            Arguments.of(2000, 7, 28, 30), Arguments.of(2000, 8, 28, 31), Arguments.of(2000, 9, 28, 30),
            Arguments.of(2000, 10, 28, 30), Arguments.of(2000, 11, 28, 31), Arguments.of(2000, 12, 28, 30),
            Arguments.of(2004, 12, 20, 37)
        );
    }

    static Stream<Arguments> provideUnsupportedEras() {
        return Stream.of(
            Arguments.of(HijrahEra.AH), Arguments.of(JapaneseEra.MEIJI), Arguments.of(JapaneseEra.TAISHO),
            Arguments.of(JapaneseEra.SHOWA), Arguments.of(JapaneseEra.HEISEI), Arguments.of(MinguoEra.BEFORE_ROC),
            Arguments.of(MinguoEra.ROC), Arguments.of(ThaiBuddhistEra.BEFORE_BE), Arguments.of(ThaiBuddhistEra.BE)
        );
    }

    static Stream<Arguments> provideDateAndFieldRanges() {
        return Stream.of(
            // year, month, dom, field, expectedRange
            Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)),
            Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)),
            Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)),
            Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
            Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)),
            Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)),
            Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
            Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)),
            Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
            Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)), // Note: spec says 31 days, but still 4 full weeks
            Arguments.of(2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
            Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)),
            Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
            Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53))
        );
    }

    static Stream<Arguments> provideDateAndFieldValues() {
        return Stream.of(
            // year, month, dom, field, expectedValue
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 2),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 30 + 31 + 30 + 30 + 26),
            Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
            Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4 + 5 + 4 + 4 + 4),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12L + 5 - 1),
            Arguments.of(2014, 5, 26, YEAR, 2014),
            Arguments.of(2014, 5, 26, ERA, 1),
            Arguments.of(2015, 12, 37, DAY_OF_WEEK, 5),
            Arguments.of(2015, 12, 37, DAY_OF_MONTH, 37),
            Arguments.of(2015, 12, 37, DAY_OF_YEAR, 4 * (4 + 5 + 4) * 7 + 7),
            Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_MONTH, 6),
            Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53)
        );
    }

    static Stream<Arguments> provideWithAdjustmentData() {
        return Stream.of(
            // year, month, dom, field, value, expectedYear, expectedMonth, expectedDom
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 30),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 9),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12L + 3 - 1, 2013, 3, 26),
            Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
            Arguments.of(2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26),
            Arguments.of(2015, 12, 37, YEAR, 2004, 2004, 12, 37),
            Arguments.of(2015, 12, 37, YEAR, 2013, 2013, 12, 30)
        );
    }

    static Stream<Arguments> provideWithInvalidAdjustmentData() {
        return Stream.of(
            // year, month, dom, field, value
            Arguments.of(2013, 1, 1, DAY_OF_MONTH, 31),
            Arguments.of(2013, 6, 1, DAY_OF_MONTH, 31),
            Arguments.of(2015, 12, 1, DAY_OF_MONTH, 38),
            Arguments.of(2013, 1, 1, DAY_OF_YEAR, 365),
            Arguments.of(2015, 1, 1, DAY_OF_YEAR, 372),
            Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14),
            Arguments.of(2013, 1, 1, YEAR, 1_000_001)
        );
    }

    static Stream<Arguments> provideDatesForLastDayOfMonthAdjustment() {
        return Stream.of(
            // year, month, day, expectedYear, expectedMonth, expectedDay
            Arguments.of(2012, 1, 23, 2012, 1, 30),
            Arguments.of(2012, 2, 23, 2012, 2, 31),
            Arguments.of(2009, 12, 23, 2009, 12, 37)
        );
    }

    static Stream<Arguments> providePlusData() {
        return Stream.of(
            // year, month, dom, amount, unit, expectedYear, expectedMonth, expectedDom
            Arguments.of(2014, 5, 26, 0, DAYS, 2014, 5, 26),
            Arguments.of(2014, 5, 26, 8, DAYS, 2014, 6, 3),
            Arguments.of(2014, 5, 26, -3, DAYS, 2014, 5, 23),
            Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 16),
            Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
            Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
            Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
            Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
            Arguments.of(2014, 5, 26, -1, MILLENNIA, 1014, 5, 26)
        );
    }

    static Stream<Arguments> providePlusDataAcrossLeapWeek() {
        return Stream.of(
            // year, month, dom, amount, unit, expectedYear, expectedMonth, expectedDom
            Arguments.of(2015, 12, 28, 8, DAYS, 2015, 12, 36),
            Arguments.of(2015, 12, 28, 3, WEEKS, 2016, 1, 12),
            Arguments.of(2015, 12, 28, 52, WEEKS, 2016, 12, 21),
            Arguments.of(2015, 12, 28, 3, MONTHS, 2016, 3, 28),
            Arguments.of(2015, 12, 28, 1, YEARS, 2016, 12, 28)
        );
    }

    static Stream<Arguments> provideUntilData() {
        return Stream.of(
            // y1, m1, d1, y2, m2, d2, unit, expectedAmount
            Arguments.of(2014, 5, 26, 2014, 5, 26, DAYS, 0),
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

    static Stream<Arguments> provideUntilPeriodData() {
        return Stream.of(
            // y1, m1, d1, y2, m2, d2, expectedY, expectedM, expectedD
            Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
            Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 9),
            Arguments.of(2014, 5, 26, 2014, 5, 20, 0, 0, -6),
            Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
            Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
            Arguments.of(2014, 5, 26, 2024, 5, 25, 9, 11, 29)
        );
    }

    static Stream<Arguments> provideDatesForToString() {
        return Stream.of(
            Arguments.of(Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"),
            Arguments.of(Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"),
            Arguments.of(Symmetry010Date.of(2000, 8, 31), "Sym010 CE 2000/08/31"),
            Arguments.of(Symmetry010Date.of(2009, 12, 37), "Sym010 CE 2009/12/37")
        );
    }
}