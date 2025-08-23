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

import com.google.common.testing.EqualsTester;
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

@DisplayName("Symmetry454Date and Symmetry454Chronology")
class Symmetry454ChronologyTest {

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides a diverse sample of equivalent dates in the Symmetry454 and ISO calendar systems
     * to test conversions and date properties.
     */
    static Stream<Arguments> provideSymmetryAndIsoDates() {
        return Stream.of(
            Arguments.of(Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry454Date.of(742, 3, 25), LocalDate.of(742, 4, 2)),
            Arguments.of(Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
            Arguments.of(Symmetry454Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)),
            Arguments.of(Symmetry454Date.of(1433, 11, 14), LocalDate.of(1433, 11, 10)),
            Arguments.of(Symmetry454Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)),
            Arguments.of(Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
            Arguments.of(Symmetry454Date.of(1564, 2, 20), LocalDate.of(1564, 2, 15)),
            Arguments.of(Symmetry454Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)),
            Arguments.of(Symmetry454Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)),
            Arguments.of(Symmetry454Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)),
            Arguments.of(Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
            Arguments.of(Symmetry454Date.of(1879, 3, 12), LocalDate.of(1879, 3, 14)),
            Arguments.of(Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)),
            Arguments.of(Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
            Arguments.of(Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1))
        );
    }

    static Stream<Arguments> provideInvalidDateParts() {
        return Stream.of(
            Arguments.of(-1, 13, 28), Arguments.of(-1, 13, 29), Arguments.of(2000, -2, 1),
            Arguments.of(2000, 13, 1), Arguments.of(2000, 15, 1), Arguments.of(2000, 1, -1),
            Arguments.of(2000, 1, 0), Arguments.of(2000, 0, 1), Arguments.of(2000, -1, 0),
            Arguments.of(2000, -1, 1), Arguments.of(2000, 1, 29), // Jan has 28 days
            Arguments.of(2000, 2, 36), // Feb has 35 days
            Arguments.of(2000, 3, 29), // Mar has 28 days
            Arguments.of(2004, 12, 36) // 2004 is not a leap year, Dec has 28 days
        );
    }

    /**
     * Provides non-leap years. In the Symmetry454 calendar, December has 28 days in a normal year,
     * so creating a date for December 29th should fail.
     */
    static Stream<Arguments> provideNonLeapYearsForInvalidDecember29() {
        return Stream.of(Arguments.of(1), Arguments.of(2), Arguments.of(4), Arguments.of(100));
    }

    static Stream<Arguments> provideDataForLengthOfMonth() {
        return Stream.of(
            // year, month, day, expected length
            Arguments.of(2000, 1, 28, 28), Arguments.of(2000, 2, 28, 35),
            Arguments.of(2000, 3, 28, 28), Arguments.of(2000, 4, 28, 28),
            Arguments.of(2000, 5, 28, 35), Arguments.of(2000, 6, 28, 28),
            Arguments.of(2000, 7, 28, 28), Arguments.of(2000, 8, 28, 35),
            Arguments.of(2000, 9, 28, 28), Arguments.of(2000, 10, 28, 28),
            Arguments.of(2000, 11, 28, 35), Arguments.of(2000, 12, 28, 28),
            Arguments.of(2004, 12, 20, 35) // 2004 is a leap year
        );
    }

    static Stream<Arguments> provideUnsupportedErasForProlepticYear() {
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

    static Stream<Arguments> provideDataForRanges() {
        return Stream.of(
            // year, month, day, field, expected range
            Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)),
            Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35)),
            Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 35)), // leap year
            Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
            Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)), // non-leap year
            Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)), // leap year
            Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
            Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
            Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
            Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
            Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)) // leap year
        );
    }

    static Stream<Arguments> provideDataForGetLong() {
        return Stream.of(
            // year, month, day, field, expected value
            // Day of year for 2014-05-26: 28(Jan)+35(Feb)+28(Mar)+28(Apr)+26 = 145
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 145L),
            // Week of year for 2014-05-26: 4(Jan)+5(Feb)+4(Mar)+4(Apr) + 4th week in May = 21
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L),
            Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
            Arguments.of(2014, 5, 26, YEAR, 2014),
            Arguments.of(2014, 5, 26, ERA, 1),
            // Day of year for 2012-09-26: 28+35+28+28+35+28+28+35+26 = 271
            Arguments.of(2012, 9, 26, DAY_OF_YEAR, 271L),
            // Week of year for 2012-09-26: 4+5+4+4+5+4+4+5 + 4th week in Sep = 39
            Arguments.of(2012, 9, 26, ALIGNED_WEEK_OF_YEAR, 39L),
            // Day of year for 2015-12-35 (leap year): 364 (normal year days) + 7 = 371
            Arguments.of(2015, 12, 35, DAY_OF_YEAR, 371L),
            Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53L)
        );
    }

    static Stream<Arguments> provideDataForWith() {
        return Stream.of(
            // year, month, dom, field, value, expectedYear, expectedMonth, expectedDom
            Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22),
            Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
            Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28),
            Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
            Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
            Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
            Arguments.of(2015, 12, 29, YEAR, 2014, 2014, 12, 28) // day adjusted for non-leap year
        );
    }

    static Stream<Arguments> provideDataForWithInvalidValue() {
        return Stream.of(
            // year, month, dom, field, value
            Arguments.of(2013, 1, 1, DAY_OF_MONTH, 29), // Jan has 28 days
            Arguments.of(2013, 1, 1, DAY_OF_YEAR, 365), // Non-leap year has 364 days
            Arguments.of(2015, 1, 1, DAY_OF_YEAR, 372), // Leap year has 371 days
            Arguments.of(2013, 1, 1, MONTH_OF_YEAR, 14),
            Arguments.of(2013, 1, 1, YEAR, 1_000_001)
        );
    }

    static Stream<Arguments> provideDataForLastDayOfMonth() {
        return Stream.of(
            // year, month, day, expectedYear, expectedMonth, expectedDay
            Arguments.of(2012, 1, 23, 2012, 1, 28),
            Arguments.of(2012, 2, 23, 2012, 2, 35),
            Arguments.of(2009, 12, 23, 2009, 12, 35) // 2009 is a leap year
        );
    }

    static Stream<Arguments> provideDataForPlus() {
        return Stream.of(
            // year, month, dom, amount, unit, expectedYear, expectedMonth, expectedDom
            Arguments.of(2014, 5, 26, 8, DAYS, 2014, 5, 34),
            Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 12),
            Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
            Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
            Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
            Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
            Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26)
        );
    }

    static Stream<Arguments> provideDataForPlusLeapWeek() {
        return Stream.of(
            // year, month, dom, amount, unit, expectedYear, expectedMonth, expectedDom
            Arguments.of(2015, 12, 28, 8, DAYS, 2016, 1, 1),
            Arguments.of(2015, 12, 28, 3, WEEKS, 2016, 1, 14),
            Arguments.of(2015, 12, 28, 12, MONTHS, 2016, 12, 28)
        );
    }

    static Stream<Arguments> provideDataForUntil() {
        return Stream.of(
            // y1, m1, d1, y2, m2, d2, unit, expected
            Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 13L),
            Arguments.of(2014, 5, 26, 2014, 5, 20, DAYS, -6L),
            Arguments.of(2014, 5, 26, 2014, 6, 5, WEEKS, 1L),
            Arguments.of(2014, 5, 26, 2014, 6, 26, MONTHS, 1L),
            Arguments.of(2014, 5, 26, 2015, 5, 26, YEARS, 1L),
            Arguments.of(2014, 5, 26, 2024, 5, 26, DECADES, 1L),
            Arguments.of(2014, 5, 26, 2114, 5, 26, CENTURIES, 1L),
            Arguments.of(2014, 5, 26, 3014, 5, 26, MILLENNIA, 1L),
            Arguments.of(2014, 5, 26, 3014, 5, 26, ERAS, 0L)
        );
    }

    static Stream<Arguments> provideDataForUntilPeriod() {
        return Stream.of(
            // y1, m1, d1, y2, m2, d2, expectedYears, expectedMonths, expectedDays
            Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
            Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 13),
            Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
            Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
            Arguments.of(2014, 5, 26, 2024, 5, 25, 9, 11, 27)
        );
    }

    static Stream<Arguments> provideDataForToString() {
        return Stream.of(
            Arguments.of(Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"),
            Arguments.of(Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"),
            Arguments.of(Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35")
        );
    }

    //-----------------------------------------------------------------------
    // Test Cases
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Factory methods")
    class FactoryTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideInvalidDateParts")
        @DisplayName("of(y, m, d) with invalid parts throws exception")
        void of_withInvalidDateParts_throwsException(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, day));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideNonLeapYearsForInvalidDecember29")
        @DisplayName("of(y, 12, 29) in a non-leap year throws exception")
        void of_onDecember29InNonLeapYear_throwsException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    @DisplayName("Conversion to/from other calendar systems")
    class ConversionTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSymmetryAndIsoDates")
        @DisplayName("LocalDate.from(Symmetry454Date) returns equivalent date")
        void from_Symmetry454Date_toLocalDate_producesEquivalentDate(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSymmetryAndIsoDates")
        @DisplayName("Symmetry454Date.from(LocalDate) returns equivalent date")
        void from_LocalDate_toSymmetry454Date_producesEquivalentDate(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSymmetryAndIsoDates")
        @DisplayName("chronology.date(TemporalAccessor) returns equivalent date")
        void chronology_date_fromTemporal_producesEquivalentDate(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Chronology.INSTANCE.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSymmetryAndIsoDates")
        @DisplayName("toEpochDay() returns correct value")
        void toEpochDay_producesCorrectValue(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symmetryDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSymmetryAndIsoDates")
        @DisplayName("chronology.dateEpochDay() returns correct date")
        void chronology_dateEpochDay_producesCorrectDate(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }
    }

    @Nested
    @DisplayName("Field access and modification")
    class FieldAccessorTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDataForLengthOfMonth")
        @DisplayName("lengthOfMonth() returns correct value")
        void lengthOfMonth_returnsCorrectValue(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, day).lengthOfMonth());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDataForRanges")
        @DisplayName("range(field) returns correct range")
        void range_forField_returnsCorrectRange(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, Symmetry454Date.of(year, month, dom).range(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDataForGetLong")
        @DisplayName("getLong(field) returns correct value")
        void getLong_forField_returnsCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDataForWith")
        @DisplayName("with(field, value) returns adjusted date")
        void with_forField_returnsAdjustedDate(int year, int month, int dom, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDom) {
            Symmetry454Date start = Symmetry454Date.of(year, month, dom);
            Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDom);
            assertEquals(expected, start.with(field, value));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDataForWithInvalidValue")
        @DisplayName("with(field, invalidValue) throws exception")
        void with_forFieldWithInvalidValue_throwsException(int year, int month, int dom, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom).with(field, value));
        }
    }

    @Nested
    @DisplayName("Date arithmetic")
    class ArithmeticTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSymmetryAndIsoDates")
        @DisplayName("plus(days) adds correct number of days")
        void plusDays_addsCorrectNumberOfDays(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(symmetryDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(symmetryDate.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(symmetryDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(symmetryDate.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideSymmetryAndIsoDates")
        @DisplayName("minus(days) subtracts correct number of days")
        void minusDays_subtractsCorrectNumberOfDays(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate.minus(0, DAYS)));
            assertEquals(isoDate.minusDays(1), LocalDate.from(symmetryDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(symmetryDate.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(symmetryDate.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(symmetryDate.minus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDataForPlus")
        @DisplayName("plus(amount, unit) returns correct date")
        void plus_withUnit_returnsCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDataForPlusLeapWeek")
        @DisplayName("plus(amount, unit) across leap week returns correct date")
        void plus_withUnit_leapWeekScenario_returnsCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDataForPlus")
        @DisplayName("minus(amount, unit) correctly reverses plus()")
        void minus_withUnit_reversesPlus(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            Symmetry454Date end = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, end.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDataForPlusLeapWeek")
        @DisplayName("minus(amount, unit) across leap week correctly reverses plus()")
        void minus_withUnit_leapWeekScenario_reversesPlus(int ey, int em, int ed, long amount, TemporalUnit unit, int y, int m, int d) {
            Symmetry454Date end = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, end.minus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDataForUntil")
        @DisplayName("until(endDate, unit) calculates correct amount")
        void until_withUnit_calculatesCorrectAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDataForUntilPeriod")
        @DisplayName("until(endDate) calculates correct period")
        void until_withPeriod_calculatesCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(ey, em, ed);
            assertEquals(expected, start.until(end));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDataForLastDayOfMonth")
        @DisplayName("with(lastDayOfMonth) adjuster returns correct date")
        void with_lastDayOfMonthAdjuster_returnsCorrectDate(int y, int m, int d, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Comparison and Equality")
    class ComparisonAndEqualityTests {
        @Test
        @DisplayName("equals() and hashCode() adhere to contract")
        void equalsAndHashCode_adhereToContract() {
            Symmetry454Date date1 = Symmetry454Date.of(2014, 5, 26);
            Symmetry454Date date1b = Symmetry454Date.of(2014, 5, 26);
            Symmetry454Date date2 = Symmetry454Date.of(2014, 5, 27);
            Symmetry454Date date3 = Symmetry454Date.of(2014, 6, 26);
            Symmetry454Date date4 = Symmetry454Date.of(2015, 5, 26);

            new EqualsTester()
                .addEqualityGroup(date1, date1b)
                .addEqualityGroup(date2)
                .addEqualityGroup(date3)
                .addEqualityGroup(date4)
                .testEquals();
        }

        @Test
        @DisplayName("until(equivalentDate) is zero")
        void until_anEquivalentDate_isZero() {
            Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
            LocalDate isoDate = LocalDate.from(date);

            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), date.until(date));
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), date.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(date));
        }
    }

    @Nested
    @DisplayName("Symmetry454Chronology singleton")
    class ChronologySingletonTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideUnsupportedErasForProlepticYear")
        @DisplayName("prolepticYear() with unsupported era throws exception")
        void prolepticYear_withUnsupportedEra_throwsException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }

        @Test
        @DisplayName("eraOf() with invalid value throws exception")
        void eraOf_withInvalidValue_throwsException() {
            assertThrows(DateTimeException.class, () -> Symmetry454Chronology.INSTANCE.eraOf(2));
        }
    }

    @Nested
    @DisplayName("String representation")
    class ToStringTests {
        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#provideDataForToString")
        @DisplayName("toString() returns correct format")
        void toString_returnsCorrectFormat(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}