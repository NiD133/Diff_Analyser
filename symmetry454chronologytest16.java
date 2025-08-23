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
 * Tests for the {@link Symmetry454Chronology} and {@link Symmetry454Date} classes.
 */
@DisplayName("Symmetry454Chronology and Symmetry454Date")
class Symmetry454ChronologyTest {

    private static final Symmetry454Chronology CHRONO = Symmetry454Chronology.INSTANCE;

    //-----------------------------------------------------------------------
    // Conversion Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Conversion and Factory Methods")
    class ConversionTests {

        static Stream<Arguments> provideEquivalentDateSamples() {
            return Stream.of(
                // Historical date examples
                Arguments.of(Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)), // Constantine the Great
                Arguments.of(Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)),
                Arguments.of(Symmetry454Date.of(272, 2, 27), LocalDate.of(272, 2, 24)),
                Arguments.of(Symmetry454Date.of(742, 3, 25), LocalDate.of(742, 4, 2)), // Charlemagne
                Arguments.of(Symmetry454Date.of(742, 4, 2), LocalDate.of(742, 4, 7)),
                Arguments.of(Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)), // Battle of Hastings
                Arguments.of(Symmetry454Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)), // Petrarch
                Arguments.of(Symmetry454Date.of(1304, 7, 20), LocalDate.of(1304, 7, 19)),
                Arguments.of(Symmetry454Date.of(1433, 11, 14), LocalDate.of(1433, 11, 10)), // Charles the Bold
                Arguments.of(Symmetry454Date.of(1433, 11, 10), LocalDate.of(1433, 11, 6)),
                Arguments.of(Symmetry454Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)), // Leonardo da Vinci
                Arguments.of(Symmetry454Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)),
                Arguments.of(Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)), // Columbus's expedition
                Arguments.of(Symmetry454Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)),
                Arguments.of(Symmetry454Date.of(1564, 2, 20), LocalDate.of(1564, 2, 15)), // Galileo Galilei
                Arguments.of(Symmetry454Date.of(1564, 2, 15), LocalDate.of(1564, 2, 10)),
                Arguments.of(Symmetry454Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)), // William Shakespeare
                Arguments.of(Symmetry454Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)),
                Arguments.of(Symmetry454Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)), // Sir Isaac Newton
                Arguments.of(Symmetry454Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)),
                Arguments.of(Symmetry454Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)), // Leonhard Euler
                Arguments.of(Symmetry454Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)),
                Arguments.of(Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)), // Storming of the Bastille
                Arguments.of(Symmetry454Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)),
                Arguments.of(Symmetry454Date.of(1879, 3, 12), LocalDate.of(1879, 3, 14)), // Albert Einstein
                Arguments.of(Symmetry454Date.of(1879, 3, 14), LocalDate.of(1879, 3, 16)),
                Arguments.of(Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)), // Dennis Ritchie
                Arguments.of(Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)), // Unix epoch
                Arguments.of(Symmetry454Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)),
                Arguments.of(Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1)), // Start of 21st century
                Arguments.of(Symmetry454Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3))
            );
        }

        @ParameterizedTest
        @MethodSource("provideEquivalentDateSamples")
        void toLocalDate_fromSymmetry454Date(Symmetry454Date sym454Date, LocalDate expectedIsoDate) {
            assertEquals(expectedIsoDate, LocalDate.from(sym454Date));
        }

        @ParameterizedTest
        @MethodSource("provideEquivalentDateSamples")
        void fromLocalDate_toSymmetry454Date(Symmetry454Date expectedSym454Date, LocalDate isoDate) {
            assertEquals(expectedSym454Date, Symmetry454Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("provideEquivalentDateSamples")
        void dateFromEpochDay_shouldMatchIso(Symmetry454Date expectedSym454Date, LocalDate isoDate) {
            assertEquals(expectedSym454Date, CHRONO.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("provideEquivalentDateSamples")
        void toEpochDay_shouldMatchIso(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), sym454Date.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("provideEquivalentDateSamples")
        void chronologyDate_fromTemporalAccessor_shouldMatch(Symmetry454Date expectedSym454Date, LocalDate isoDate) {
            assertEquals(expectedSym454Date, CHRONO.date(isoDate));
        }

        @ParameterizedTest
        @MethodSource("provideEquivalentDateSamples")
        @DisplayName("until a LocalDate representing the same day should be zero")
        void until_LocalDate_isZero(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(CHRONO.period(0, 0, 0), sym454Date.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("provideEquivalentDateSamples")
        @DisplayName("LocalDate.until a Symmetry454Date representing the same day should be zero")
        void localDate_until_Symmetry454Date_isZero(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(sym454Date));
        }

        static Stream<Arguments> provideInvalidDateParts() {
            return Stream.of(
                Arguments.of(-1, 13, 28), Arguments.of(-1, 13, 29), Arguments.of(2000, -2, 1),
                Arguments.of(2000, 13, 1), Arguments.of(2000, 15, 1), Arguments.of(2000, 1, -1),
                Arguments.of(2000, 1, 0), Arguments.of(2000, 0, 1), Arguments.of(2000, -1, 0),
                Arguments.of(2000, -1, 1), Arguments.of(2000, 1, 29), Arguments.of(2000, 2, 36),
                Arguments.of(2000, 3, 29), Arguments.of(2000, 4, 29), Arguments.of(2000, 5, 36),
                Arguments.of(2000, 6, 29), Arguments.of(2000, 7, 29), Arguments.of(2000, 8, 36),
                Arguments.of(2000, 9, 29), Arguments.of(2000, 10, 29), Arguments.of(2000, 11, 36),
                Arguments.of(2000, 12, 29), Arguments.of(2004, 12, 36)
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidDateParts")
        void of_withInvalidDateParts_throwsException(int year, int month, int dayOfMonth) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dayOfMonth));
        }

        static Stream<Arguments> provideNonLeapYears() {
            return Stream.of(
                Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(2000)
            );
        }

        @ParameterizedTest
        @MethodSource("provideNonLeapYears")
        void of_withLeapDayInNonLeapYear_throwsException(int year) {
            // Day 29 of month 12 is a leap day which only exists in leap years.
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    //-----------------------------------------------------------------------
    // Field Accessor Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Field Accessors")
    class FieldAccessorTests {

        static Stream<Arguments> provideDatesAndExpectedMonthLengths() {
            return Stream.of(
                Arguments.of(2000, 1, 28), Arguments.of(2000, 2, 35), Arguments.of(2000, 3, 28),
                Arguments.of(2000, 4, 28), Arguments.of(2000, 5, 35), Arguments.of(2000, 6, 28),
                Arguments.of(2000, 7, 28), Arguments.of(2000, 8, 35), Arguments.of(2000, 9, 28),
                Arguments.of(2000, 10, 28), Arguments.of(2000, 11, 35), Arguments.of(2000, 12, 28),
                Arguments.of(2004, 12, 35) // Leap year has long December
            );
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndExpectedMonthLengths")
        void lengthOfMonth_returnsCorrectValue(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> provideFieldRanges() {
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35)),
                Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 35)), // Leap year
                Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)),
                Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)), // Leap year
                Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("provideFieldRanges")
        void range_forField_returnsCorrectRange(int year, int month, int day, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(year, month, day).range(field));
        }

        static Stream<Arguments> provideFieldValues() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 28 + 35 + 28 + 28 + 26),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4 + 5 + 4 + 4 + 4),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                Arguments.of(2015, 12, 35, DAY_OF_WEEK, 7), // Last day of leap year
                Arguments.of(2015, 12, 35, DAY_OF_YEAR, 371),
                Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53)
            );
        }

        @ParameterizedTest
        @MethodSource("provideFieldValues")
        void getLong_forField_returnsCorrectValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, day).getLong(field));
        }
    }

    //-----------------------------------------------------------------------
    // Manipulation Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Date Manipulation")
    class ManipulationTests {

        static Stream<Arguments> providePlusOperations() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 5, 34),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 12),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                // Test crossing leap week
                Arguments.of(2015, 12, 28, 8, DAYS, 2016, 1, 1),
                Arguments.of(2015, 12, 28, 3, WEEKS, 2016, 1, 14)
            );
        }

        @ParameterizedTest
        @MethodSource("providePlusOperations")
        void plus_withUnit_returnsCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("providePlusOperations")
        void minus_withUnit_returnsCorrectDate(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(ey, em, ed);
            Symmetry454Date expected = Symmetry454Date.of(y, m, d);
            assertEquals(expected, start.minus(amount, unit));
        }

        static Stream<Arguments> provideWithOperations() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29), // Adjusts day to be valid
                Arguments.of(2015, 3, 28, DAY_OF_YEAR, 371, 2015, 12, 35) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("provideWithOperations")
        void with_field_returnsAdjustedDate(int y, int m, int d, TemporalField field, long value, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.with(field, value));
        }

        static Stream<Arguments> provideInvalidWithFieldValues() {
            return Stream.of(
                Arguments.of(DAY_OF_MONTH, 29), // Invalid for a 28-day month
                Arguments.of(DAY_OF_YEAR, 365), // Invalid for a non-leap year
                Arguments.of(ALIGNED_WEEK_OF_MONTH, 5), // Invalid for a 4-week month
                Arguments.of(YEAR, 1_000_001) // Out of range
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidWithFieldValues")
        void with_invalidFieldValue_throwsException(TemporalField field, long value) {
            Symmetry454Date date = Symmetry454Date.of(2013, 1, 1);
            assertThrows(DateTimeException.class, () -> date.with(field, value));
        }

        @Test
        void with_lastDayOfMonthAdjuster_returnsLastDayOfMonth() {
            Symmetry454Date date = Symmetry454Date.of(2012, 2, 1); // A 35-day month
            Symmetry454Date expected = Symmetry454Date.of(2012, 2, 35);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @Test
        void with_LocalDateAdjuster_adjustsToEquivalentSymmetry454Date() {
            Symmetry454Date base = Symmetry454Date.of(2000, 1, 4);
            // Adjust to a new date specified by an ISO LocalDate
            Symmetry454Date adjusted = base.with(LocalDate.of(2012, 7, 6));
            // The result is the Symmetry454Date equivalent of 2012-07-06
            assertEquals(Symmetry454Date.of(2012, 7, 5), adjusted);
        }

        @Test
        void until_self_isZeroPeriod() {
            Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
            assertEquals(CHRONO.period(0, 0, 0), date.until(date));
        }

        static Stream<Arguments> provideUntilAsPeriod() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 13),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                Arguments.of(2014, 5, 26, 2024, 5, 25, 9, 11, 27)
            );
        }

        @ParameterizedTest
        @MethodSource("provideUntilAsPeriod")
        void until_asPeriod_returnsCorrectPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = CHRONO.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }

        @Test
        void until_withUnit_returnsCorrectAmount() {
            Symmetry454Date start = Symmetry454Date.of(2014, 5, 26);
            Symmetry454Date end = Symmetry454Date.of(2015, 6, 5);
            assertEquals(378, start.until(end, DAYS));
            assertEquals(54, start.until(end, WEEKS));
            assertEquals(12, start.until(end, MONTHS));
            assertEquals(1, start.until(end, YEARS));
            assertEquals(0, start.until(end, DECADES));
            assertEquals(0, start.until(end, ERAS));
        }
    }

    //-----------------------------------------------------------------------
    // Chronology-specific Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Chronology-specific API")
    class ChronologyApiTests {

        static Stream<Arguments> provideUnsupportedEras() {
            return Stream.of(
                // Eras from other calendar systems that are not compatible
                Arguments.of(AccountingEra.BCE), Arguments.of(CopticEra.AM), Arguments.of(DiscordianEra.YOLD),
                Arguments.of(EthiopicEra.INCARNATION), Arguments.of(HijrahEra.AH), Arguments.of(InternationalFixedEra.CE),
                Arguments.of(JapaneseEra.HEISEI), Arguments.of(JulianEra.AD), Arguments.of(MinguoEra.ROC),
                Arguments.of(PaxEra.CE), Arguments.of(ThaiBuddhistEra.BE)
            );
        }

        @ParameterizedTest
        @MethodSource("provideUnsupportedEras")
        void prolepticYear_withUnsupportedEra_throwsClassCastException(Era era) {
            assertThrows(ClassCastException.class, () -> CHRONO.prolepticYear(era, 4));
        }
    }

    //-----------------------------------------------------------------------
    // Object Method Tests
    //-----------------------------------------------------------------------
    @Nested
    @DisplayName("Object methods")
    class ObjectMethodTests {

        static Stream<Arguments> provideToStringSamples() {
            return Stream.of(
                Arguments.of(Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"),
                Arguments.of(Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"),
                Arguments.of(Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35"),
                Arguments.of(Symmetry454Date.of(1970, 12, 35), "Sym454 CE 1970/12/35")
            );
        }

        @ParameterizedTest
        @MethodSource("provideToStringSamples")
        void toString_returnsCorrectFormat(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}