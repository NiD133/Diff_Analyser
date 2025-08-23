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
import static java.time.temporal.ChronoField.MINUTE_OF_DAY;
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
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link Symmetry010Chronology} and {@link Symmetry010Date}.
 */
class Symmetry010ChronologyTest {

    private static final Symmetry010Chronology CHRONO = Symmetry010Chronology.INSTANCE;

    //-----------------------------------------------------------------------
    // Data Providers
    //-----------------------------------------------------------------------

    /**
     * Provides equivalent dates in the Symmetry010 and ISO calendar systems.
     *
     * @return a stream of arguments: { Symmetry010Date, LocalDate }
     */
    static Stream<Arguments> provideEquivalentSymmetryAndIsoDates() {
        return Stream.of(
            Arguments.of(Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27)),
            Arguments.of(Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
            Arguments.of(Symmetry010Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)),
            Arguments.of(Symmetry010Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)),
            Arguments.of(Symmetry010Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)),
            Arguments.of(Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)),
            Arguments.of(Symmetry010Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)),
            Arguments.of(Symmetry010Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3))
        );
    }

    @Nested
    class FactoryAndConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideEquivalentSymmetryAndIsoDates")
        void fromIsoLocalDate_shouldCreateCorrectSymmetryDate(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(symDate, Symmetry010Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideEquivalentSymmetryAndIsoDates")
        void toIsoLocalDate_shouldConvertToCorrectSymmetryDate(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideEquivalentSymmetryAndIsoDates")
        void toEpochDay_shouldMatchIsoDate(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideEquivalentSymmetryAndIsoDates")
        void chronology_dateFromEpochDay_shouldCreateCorrectDate(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(symDate, CHRONO.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideEquivalentSymmetryAndIsoDates")
        void chronology_dateFromTemporal_shouldCreateCorrectDate(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(symDate, CHRONO.date(isoDate));
        }
    }

    @Nested
    class InvalidDateTests {

        /**
         * Provides invalid date components.
         *
         * @return a stream of arguments: { year, month, dayOfMonth }
         */
        static Stream<Arguments> provideInvalidDateParts() {
            return Stream.of(
                Arguments.of(-1, 13, 28), Arguments.of(2000, 13, 1),
                Arguments.of(2000, 1, -1), Arguments.of(2000, 1, 0),
                Arguments.of(2000, 0, 1), Arguments.of(2000, 1, 31), // Jan has 30 days
                Arguments.of(2000, 2, 32), // Feb has 31 days
                Arguments.of(2000, 4, 31), // Apr has 30 days
                Arguments.of(2004, 12, 38) // Dec 2004 is a leap month with 37 days
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidDateParts")
        void of_withInvalidDateParts_shouldThrowException(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dom));
        }

        /**
         * Provides years that are not leap years.
         *
         * @return a stream of arguments: { year }
         */
        static Stream<Arguments> provideNonLeapYears() {
            return Stream.of(
                Arguments.of(1), Arguments.of(100), Arguments.of(200), Arguments.of(2000)
            );
        }

        @ParameterizedTest
        @MethodSource("provideNonLeapYears")
        void of_withLeapDayInNonLeapYear_shouldThrowException(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    @Nested
    class FieldAndRangeTests {

        /**
         * Provides dates, a temporal field, and the expected value from getLong().
         *
         * @return a stream of arguments: { year, month, day, TemporalField, expectedValue }
         */
        static Stream<Arguments> provideDatesForGetLong() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 2L),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                // Day of year for 2014-05-26: (Jan-Apr days) + 26 = (30+31+30+30) + 26 = 147
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 147L),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
                Arguments.of(2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7L),
                // Aligned week of year for 2014-05-26: (Jan-Apr weeks) + 4 = (4+5+4+4) + 4 = 21
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 21L),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                // Proleptic month: (2014 * 12) + 5 - 1 = 24172
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014L * 12 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),
                // --- Leap Year Date ---
                Arguments.of(2015, 12, 37, DAY_OF_WEEK, 5L),
                Arguments.of(2015, 12, 37, DAY_OF_MONTH, 37L),
                // Day of year for 2015-12-37: 364 (normal year) + 7 (leap week) = 371
                Arguments.of(2015, 12, 37, DAY_OF_YEAR, 371L),
                Arguments.of(2015, 12, 37, ALIGNED_DAY_OF_WEEK_IN_MONTH, 2L),
                Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_MONTH, 6L),
                Arguments.of(2015, 12, 37, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7L),
                Arguments.of(2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53L),
                Arguments.of(2015, 12, 37, MONTH_OF_YEAR, 12L),
                // Proleptic month: (2015 * 12) + 12 - 1 = 24191
                Arguments.of(2015, 12, 37, PROLEPTIC_MONTH, 2015L * 12 + 12 - 1)
            );
        }

        @ParameterizedTest
        @MethodSource("provideDatesForGetLong")
        void getLong_forVariousFields_returnsCorrectValue(int y, int m, int d, TemporalField field, long expected) {
            Symmetry010Date date = Symmetry010Date.of(y, m, d);
            assertEquals(expected, date.getLong(field));
        }

        /**
         * Provides dates, a temporal field, and the expected valid value range.
         *
         * @return a stream of arguments: { year, month, day, TemporalField, expectedRange }
         */
        static Stream<Arguments> provideDatesForRange() {
            return Stream.of(
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)),
                Arguments.of(2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)),
                Arguments.of(2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 37)), // Leap year
                Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)),
                Arguments.of(2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)), // Leap year
                Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                Arguments.of(2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)),
                Arguments.of(2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 6)), // Leap month
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                Arguments.of(2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)) // Leap year
            );
        }

        @ParameterizedTest
        @MethodSource("provideDatesForRange")
        void range_forVariousFields_returnsCorrectRange(int y, int m, int d, TemporalField field, ValueRange expected) {
            Symmetry010Date date = Symmetry010Date.of(y, m, d);
            assertEquals(expected, date.range(field));
        }

        @Test
        void range_forUnsupportedField_throwsException() {
            Symmetry010Date date = Symmetry010Date.of(2012, 6, 28);
            assertThrows(UnsupportedTemporalTypeException.class, () -> date.range(MINUTE_OF_DAY));
        }
    }

    @Nested
    class DateManipulationTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideEquivalentSymmetryAndIsoDates")
        void plusDays_matchesIsoBehavior(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(isoDate.plusDays(0), LocalDate.from(symDate.plus(0, DAYS)));
            assertEquals(isoDate.plusDays(1), LocalDate.from(symDate.plus(1, DAYS)));
            assertEquals(isoDate.plusDays(35), LocalDate.from(symDate.plus(35, DAYS)));
            assertEquals(isoDate.plusDays(-1), LocalDate.from(symDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(symDate.plus(-60, DAYS)));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideEquivalentSymmetryAndIsoDates")
        void minusDays_matchesIsoBehavior(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(isoDate.minusDays(0), LocalDate.from(symDate.minus(0, DAYS)));
            assertEquals(isoDate.minusDays(1), LocalDate.from(symDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(symDate.minus(35, DAYS)));
            assertEquals(isoDate.minusDays(-1), LocalDate.from(symDate.minus(-1, DAYS)));
            assertEquals(isoDate.minusDays(-60), LocalDate.from(symDate.minus(-60, DAYS)));
        }

        /**
         * Provides arguments for testing the `with()` method.
         *
         * @return a stream of arguments: { baseDate, field, value, expectedDate }
         */
        static Stream<Arguments> provideWithArguments() {
            return Stream.of(
                Arguments.of(Symmetry010Date.of(2014, 5, 26), DAY_OF_WEEK, 1, Symmetry010Date.of(2014, 5, 20)),
                Arguments.of(Symmetry010Date.of(2014, 5, 26), DAY_OF_MONTH, 28, Symmetry010Date.of(2014, 5, 28)),
                Arguments.of(Symmetry010Date.of(2014, 5, 26), DAY_OF_YEAR, 364, Symmetry010Date.of(2014, 12, 30)),
                Arguments.of(Symmetry010Date.of(2014, 5, 26), ALIGNED_WEEK_OF_MONTH, 1, Symmetry010Date.of(2014, 5, 5)),
                Arguments.of(Symmetry010Date.of(2014, 5, 26), MONTH_OF_YEAR, 4, Symmetry010Date.of(2014, 4, 26)),
                Arguments.of(Symmetry010Date.of(2014, 5, 26), YEAR, 2012, Symmetry010Date.of(2012, 5, 26)),
                Arguments.of(Symmetry010Date.of(2015, 12, 37), YEAR, 2004, Symmetry010Date.of(2004, 12, 37)) // Leap day to another leap year
            );
        }

        @ParameterizedTest
        @MethodSource("provideWithArguments")
        void with_shouldAdjustToCorrectDate(Symmetry010Date base, TemporalField field, long value, Symmetry010Date expected) {
            assertEquals(expected, base.with(field, value));
        }

        /**
         * Provides arguments for testing invalid `with()` calls.
         *
         * @return a stream of arguments: { baseDate, field, invalidValue }
         */
        static Stream<Arguments> provideInvalidWithArguments() {
            return Stream.of(
                Arguments.of(Symmetry010Date.of(2013, 1, 1), DAY_OF_WEEK, 8),
                Arguments.of(Symmetry010Date.of(2013, 1, 1), DAY_OF_MONTH, 31),
                Arguments.of(Symmetry010Date.of(2013, 1, 1), DAY_OF_YEAR, 365), // Non-leap year
                Arguments.of(Symmetry010Date.of(2015, 1, 1), DAY_OF_YEAR, 372), // Leap year
                Arguments.of(Symmetry010Date.of(2013, 1, 1), MONTH_OF_YEAR, 14),
                Arguments.of(Symmetry010Date.of(2013, 1, 1), YEAR, 1_000_001)
            );
        }

        @ParameterizedTest
        @MethodSource("provideInvalidWithArguments")
        void with_invalidValue_shouldThrowException(Symmetry010Date base, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> base.with(field, value));
        }

        @Test
        void with_lastDayOfMonth_shouldReturnCorrectDate() {
            Symmetry010Date date = Symmetry010Date.of(2012, 1, 23); // Jan has 30 days
            Symmetry010Date expected = Symmetry010Date.of(2012, 1, 30);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry010ChronologyTest#provideEquivalentSymmetryAndIsoDates")
        void until_withEquivalentDates_returnsZeroPeriod(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(CHRONO.period(0, 0, 0), symDate.until(symDate));
            assertEquals(CHRONO.period(0, 0, 0), symDate.until(isoDate));
            assertEquals(Period.ZERO, isoDate.until(symDate));
        }

        @Test
        void until_withLaterDate_calculatesPeriodCorrectly() {
            Symmetry010Date start = Symmetry010Date.of(2014, 5, 26);
            Symmetry010Date end = Symmetry010Date.of(2015, 6, 28);
            ChronoPeriod expected = CHRONO.period(1, 1, 2);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    class ChronologyApiTests {

        /**
         * Provides dates and their expected month lengths.
         *
         * @return a stream of arguments: { year, month, day, expectedLength }
         */
        static Stream<Arguments> provideDatesAndExpectedMonthLengths() {
            return Stream.of(
                Arguments.of(2000, 1, 28, 30), // Jan
                Arguments.of(2000, 2, 28, 31), // Feb
                Arguments.of(2000, 11, 28, 31), // Nov
                Arguments.of(2000, 12, 28, 30), // Dec in non-leap year
                Arguments.of(2004, 12, 20, 37)  // Dec in leap year
            );
        }

        @ParameterizedTest
        @MethodSource("provideDatesAndExpectedMonthLengths")
        void lengthOfMonth_shouldReturnCorrectNumberOfDays(int year, int month, int day, int expectedLength) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, day).lengthOfMonth());
        }

        /**
         * Provides eras that are not compatible with Symmetry010Chronology.
         *
         * @return a stream of arguments: { era }
         */
        static Stream<Arguments> provideIncompatibleEras() {
            return Stream.of(
                Arguments.of(HijrahEra.AH),
                Arguments.of(JapaneseEra.HEISEI),
                Arguments.of(MinguoEra.ROC),
                Arguments.of(ThaiBuddhistEra.BE)
            );
        }

        @ParameterizedTest
        @MethodSource("provideIncompatibleEras")
        void prolepticYear_withIncompatibleEra_shouldThrowException(Era era) {
            assertThrows(ClassCastException.class, () -> CHRONO.prolepticYear(era, 1));
        }

        @Test
        void toString_shouldReturnFormattedString() {
            Symmetry010Date date = Symmetry010Date.of(2009, 12, 37);
            assertEquals("Sym010 CE 2009/12/37", date.toString());
        }
    }
}