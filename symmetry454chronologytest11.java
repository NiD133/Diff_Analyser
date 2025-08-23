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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Symmetry454Date and Symmetry454Chronology")
class Symmetry454ChronologyTest {

    //
    // Data Providers
    //

    static Stream<Arguments> sampleDateEquivalences() {
        return Stream.of(
            Arguments.of(Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
            Arguments.of(Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)),
            Arguments.of(Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
            Arguments.of(Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)),
            Arguments.of(Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
            Arguments.of(Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1))
        );
    }

    //
    // Tests
    //

    @Nested
    @DisplayName("Factory and Conversion")
    class FactoryAndConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleDateEquivalences")
        @DisplayName("converts to LocalDate")
        void convertsToLocalDate(Symmetry454Date symmDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleDateEquivalences")
        @DisplayName("creates from LocalDate")
        void createsFromLocalDate(Symmetry454Date symmDate, LocalDate isoDate) {
            assertEquals(symmDate, Symmetry454Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleDateEquivalences")
        @DisplayName("creates from epoch day via Chronology")
        void createsFromEpochDay(Symmetry454Date symmDate, LocalDate isoDate) {
            assertEquals(symmDate, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleDateEquivalences")
        @DisplayName("converts to epoch day")
        void convertsToEpochDay(Symmetry454Date symmDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symmDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleDateEquivalences")
        @DisplayName("creates from TemporalAccessor via Chronology")
        void createsFromTemporal(Symmetry454Date symmDate, LocalDate isoDate) {
            assertEquals(symmDate, Symmetry454Chronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Factory `of` with invalid dates")
    class InvalidDateCreationTests {

        static Stream<Arguments> invalidDateComponents() {
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
        @MethodSource("invalidDateComponents")
        @DisplayName("throws for invalid year, month, or day")
        void of_invalidDate_throws(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom));
        }

        static Stream<Arguments> nonLeapYears() {
            return Stream.of(Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(2000));
        }

        @ParameterizedTest
        @MethodSource("nonLeapYears")
        @DisplayName("throws for leap day in non-leap year")
        void of_leapDayInNonLeapYear_throws(int year) {
            // December 29th is only valid in a leap year (when December has 35 days).
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    @DisplayName("Date properties")
    class DatePropertyTests {

        static Stream<Arguments> monthLengths() {
            return Stream.of(
                Arguments.of(2000, 1, 28, 28), Arguments.of(2000, 2, 28, 35), Arguments.of(2000, 3, 28, 28),
                Arguments.of(2000, 4, 28, 28), Arguments.of(2000, 5, 28, 35), Arguments.of(2000, 6, 28, 28),
                Arguments.of(2000, 7, 28, 28), Arguments.of(2000, 8, 28, 35), Arguments.of(2000, 9, 28, 28),
                Arguments.of(2000, 10, 28, 28), Arguments.of(2000, 11, 28, 35), Arguments.of(2000, 12, 28, 28),
                Arguments.of(2004, 12, 20, 35) // Leap year case
            );
        }

        @ParameterizedTest
        @MethodSource("monthLengths")
        @DisplayName("lengthOfMonth is correct")
        void lengthOfMonth_isCorrect(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, day).lengthOfMonth());
            // Verify it's independent of the day of the month
            assertEquals(expectedLength, Symmetry454Date.of(year, month, 1).lengthOfMonth());
        }
    }

    @Nested
    @DisplayName("Field access and ranges")
    class FieldAndRangeTests {

        static Stream<Arguments> fieldValues() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26),
                // DAY_OF_YEAR: Jan(28) + Feb(35) + Mar(28) + Apr(28) + 26
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 28 + 35 + 28 + 28 + 26),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5),
                // ALIGNED_WEEK_OF_YEAR: Jan(4) + Feb(5) + Mar(4) + Apr(4) + 4
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4 + 5 + 4 + 4 + 4),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014),
                Arguments.of(2014, 5, 26, ERA, 1),
                Arguments.of(1, 5, 8, ERA, 1),
                // Leap year cases
                Arguments.of(2015, 12, 35, DAY_OF_WEEK, 7),
                Arguments.of(2015, 12, 35, DAY_OF_MONTH, 35),
                // DAY_OF_YEAR: 11 months (3*35 + 8*28) + 35
                Arguments.of(2015, 12, 35, DAY_OF_YEAR, 364 + 7),
                Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53)
            );
        }

        @ParameterizedTest
        @MethodSource("fieldValues")
        @DisplayName("getLong returns correct value for field")
        void getLong_forField_returnsCorrectValue(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field));
        }

        static Stream<Arguments> fieldRanges() {
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
        @MethodSource("fieldRanges")
        @DisplayName("range returns correct range for field")
        void range_forField_returnsCorrectRange(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, Symmetry454Date.of(year, month, dom).range(field));
        }
    }

    @Nested
    @DisplayName("Modification with `with`")
    class ModificationWithTests {

        static Stream<Arguments> withFieldSamples() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26),
                Arguments.of(2014, 5, 26, YEAR, 2012, 2012, 5, 26),
                Arguments.of(2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29), // Day of month adjustment
                Arguments.of(2015, 12, 29, YEAR, 2014, 2014, 12, 28) // Day of month adjustment
            );
        }

        @ParameterizedTest
        @MethodSource("withFieldSamples")
        @DisplayName("returns new date with updated field")
        void with_field_returnsUpdatedDate(int y, int m, int d, TemporalField field, long val, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.with(field, val));
        }

        static Stream<Arguments> withInvalidFieldValues() {
            return Stream.of(
                Arguments.of(DAY_OF_MONTH, 29), // Month has 28 days
                Arguments.of(DAY_OF_YEAR, 365), // Year has 364 days
                Arguments.of(ALIGNED_WEEK_OF_MONTH, 5), // Month has 4 weeks
                Arguments.of(ALIGNED_WEEK_OF_YEAR, 53) // Year has 52 weeks
            );
        }

        @ParameterizedTest
        @MethodSource("withInvalidFieldValues")
        @DisplayName("throws for invalid value")
        void with_invalidValue_throws(TemporalField field, long value) {
            Symmetry454Date date = Symmetry454Date.of(2013, 1, 1); // A non-leap year
            assertThrows(DateTimeException.class, () -> date.with(field, value));
        }

        static Stream<Arguments> lastDayOfMonthSamples() {
            return Stream.of(
                Arguments.of(2012, 1, 23, 2012, 1, 28),
                Arguments.of(2012, 2, 23, 2012, 2, 35),
                Arguments.of(2009, 12, 23, 2009, 12, 35) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("lastDayOfMonthSamples")
        @DisplayName("adjusts to last day of month")
        void with_lastDayOfMonth_adjustsCorrectly(int y, int m, int d, int ey, int em, int ed) {
            Symmetry454Date base = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Arithmetic with `plus` and `minus`")
    class ArithmeticTests {

        static Stream<Arguments> plusSamples() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 8, DAYS, 2014, 5, 34),
                Arguments.of(2014, 5, 26, 3, WEEKS, 2014, 6, 12),
                Arguments.of(2014, 5, 26, 3, MONTHS, 2014, 8, 26),
                Arguments.of(2014, 5, 26, 3, YEARS, 2017, 5, 26),
                Arguments.of(2014, 5, 26, 3, DECADES, 2044, 5, 26),
                Arguments.of(2014, 5, 26, 3, CENTURIES, 2314, 5, 26),
                Arguments.of(2014, 5, 26, 3, MILLENNIA, 5014, 5, 26),
                // Cross-year
                Arguments.of(2014, 12, 26, 3, WEEKS, 2015, 1, 19),
                // Leap week
                Arguments.of(2015, 12, 28, 8, DAYS, 2016, 1, 1),
                Arguments.of(2015, 12, 28, 3, WEEKS, 2016, 1, 14)
            );
        }

        @ParameterizedTest
        @MethodSource("plusSamples")
        @DisplayName("plus adds amount correctly")
        void plus_addsAmount(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(y, m, d);
            Symmetry454Date expected = Symmetry454Date.of(ey, em, ed);
            assertEquals(expected, start.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusSamples")
        @DisplayName("minus subtracts amount correctly")
        void minus_subtractsAmount(int y, int m, int d, long amount, TemporalUnit unit, int ey, int em, int ed) {
            Symmetry454Date start = Symmetry454Date.of(ey, em, ed);
            Symmetry454Date expected = Symmetry454Date.of(y, m, d);
            assertEquals(expected, start.minus(amount, unit));
        }
    }

    @Nested
    @DisplayName("Arithmetic with `until`")
    class UntilTests {

        @Test
        @DisplayName("until self is zero")
        void until_self_isZero() {
            Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        @Test
        @DisplayName("until equivalent ISO date is zero")
        void until_isoDate_isZero() {
            Symmetry454Date symmDate = Symmetry454Date.of(1941, 9, 9);
            LocalDate isoDate = LocalDate.of(1941, 9, 9);
            assertEquals(Period.ZERO, isoDate.until(symmDate));
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), symmDate.until(isoDate));
        }

        static Stream<Arguments> untilUnitSamples() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 6, 4, DAYS, 13),
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

        @ParameterizedTest
        @MethodSource("untilUnitSamples")
        @DisplayName("calculates amount between dates in units")
        void until_inUnits_calculatesAmount(int y1, int m1, int d1, int y2, int m2, int d2, TemporalUnit unit, long expected) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodSamples() {
            return Stream.of(
                Arguments.of(2014, 5, 26, 2014, 5, 26, 0, 0, 0),
                Arguments.of(2014, 5, 26, 2014, 6, 4, 0, 0, 13),
                Arguments.of(2014, 5, 26, 2014, 6, 26, 0, 1, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 26, 1, 0, 0),
                Arguments.of(2014, 5, 26, 2015, 5, 25, 0, 11, 27)
            );
        }

        @ParameterizedTest
        @MethodSource("untilPeriodSamples")
        @DisplayName("calculates period between dates")
        void until_period_calculatesPeriod(int y1, int m1, int d1, int y2, int m2, int d2, int pY, int pM, int pD) {
            Symmetry454Date start = Symmetry454Date.of(y1, m1, d1);
            Symmetry454Date end = Symmetry454Date.of(y2, m2, d2);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(pY, pM, pD);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology-specific methods")
    class ChronologyTests {

        static Stream<Arguments> unsupportedEras() {
            return Stream.of(
                Arguments.of(CopticEra.AM), Arguments.of(DiscordianEra.YOLD),
                Arguments.of(EthiopicEra.INCARNATION), Arguments.of(HijrahEra.AH),
                Arguments.of(InternationalFixedEra.CE), Arguments.of(JapaneseEra.HEISEI),
                Arguments.of(JulianEra.AD), Arguments.of(MinguoEra.ROC),
                Arguments.of(PaxEra.CE), Arguments.of(ThaiBuddhistEra.BE)
            );
        }

        @ParameterizedTest
        @MethodSource("unsupportedEras")
        @DisplayName("prolepticYear throws for unsupported era")
        void prolepticYear_unsupportedEra_throws(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }

        @Test
        @DisplayName("eras returns BCE and CE")
        void eras_returnsBceAndCe() {
            List<Era> eras = Symmetry454Chronology.INSTANCE.eras();
            assertEquals(2, eras.size());
            assertTrue(eras.contains(IsoEra.BCE));
            assertTrue(eras.contains(IsoEra.CE));
        }
    }

    @Nested
    @DisplayName("Object method overrides")
    class ObjectMethodTests {

        static Stream<Arguments> toStringSamples() {
            return Stream.of(
                Arguments.of(Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"),
                Arguments.of(Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"),
                Arguments.of(Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35")
            );
        }

        @ParameterizedTest
        @MethodSource("toStringSamples")
        @DisplayName("toString returns correct format")
        void toString_returnsCorrectFormat(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}