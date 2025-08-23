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
 * Tests for the {@link InternationalFixedDate} class.
 * This test class is organized into nested classes to group related test cases.
 */
@DisplayName("InternationalFixedDate")
public class InternationalFixedChronologyTestTest9 {

    //-----------------------------------------------------------------------
    // Test data providers
    //-----------------------------------------------------------------------

    /**
     * Provides sample InternationalFixedDate instances and their equivalent ISO LocalDate.
     *
     * @return a stream of arguments: { InternationalFixedDate fixed, LocalDate iso }
     */
    static Stream<Arguments> provideSampleDates() {
        return Stream.of(
            Arguments.of(InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)),
            Arguments.of(InternationalFixedDate.of(1, 1, 2), LocalDate.of(1, 1, 2)),
            Arguments.of(InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)),
            Arguments.of(InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)),
            Arguments.of(InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)),
            Arguments.of(InternationalFixedDate.of(1, 7, 2), LocalDate.of(1, 6, 19)),
            Arguments.of(InternationalFixedDate.of(1, 13, 28), LocalDate.of(1, 12, 30)),
            Arguments.of(InternationalFixedDate.of(1, 13, 27), LocalDate.of(1, 12, 29)),
            Arguments.of(InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)),
            Arguments.of(InternationalFixedDate.of(2, 1, 1), LocalDate.of(2, 1, 1)),
            Arguments.of(InternationalFixedDate.of(4, 6, 27), LocalDate.of(4, 6, 15)),
            Arguments.of(InternationalFixedDate.of(4, 6, 28), LocalDate.of(4, 6, 16)),
            Arguments.of(InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)),
            Arguments.of(InternationalFixedDate.of(4, 7, 1), LocalDate.of(4, 6, 18)),
            Arguments.of(InternationalFixedDate.of(4, 7, 2), LocalDate.of(4, 6, 19)),
            Arguments.of(InternationalFixedDate.of(4, 13, 28), LocalDate.of(4, 12, 30)),
            Arguments.of(InternationalFixedDate.of(4, 13, 27), LocalDate.of(4, 12, 29)),
            Arguments.of(InternationalFixedDate.of(4, 13, 29), LocalDate.of(4, 12, 31)),
            Arguments.of(InternationalFixedDate.of(5, 1, 1), LocalDate.of(5, 1, 1)),
            Arguments.of(InternationalFixedDate.of(100, 6, 27), LocalDate.of(100, 6, 16)),
            Arguments.of(InternationalFixedDate.of(100, 6, 28), LocalDate.of(100, 6, 17)),
            Arguments.of(InternationalFixedDate.of(100, 7, 1), LocalDate.of(100, 6, 18)),
            Arguments.of(InternationalFixedDate.of(100, 7, 2), LocalDate.of(100, 6, 19)),
            Arguments.of(InternationalFixedDate.of(400, 6, 27), LocalDate.of(400, 6, 15)),
            Arguments.of(InternationalFixedDate.of(400, 6, 28), LocalDate.of(400, 6, 16)),
            Arguments.of(InternationalFixedDate.of(400, 6, 29), LocalDate.of(400, 6, 17)),
            Arguments.of(InternationalFixedDate.of(400, 7, 1), LocalDate.of(400, 6, 18)),
            Arguments.of(InternationalFixedDate.of(400, 7, 2), LocalDate.of(400, 6, 19)),
            Arguments.of(InternationalFixedDate.of(1582, 9, 28), LocalDate.of(1582, 9, 9)),
            Arguments.of(InternationalFixedDate.of(1582, 10, 1), LocalDate.of(1582, 9, 10)),
            Arguments.of(InternationalFixedDate.of(1945, 10, 27), LocalDate.of(1945, 10, 6)),
            Arguments.of(InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)),
            Arguments.of(InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4))
        );
    }

    @Nested
    @DisplayName("Conversion and Interoperability")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest9#provideSampleDates")
        void toLocalDate_convertsCorrectly(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso, LocalDate.from(fixed));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest9#provideSampleDates")
        void fromLocalDate_convertsCorrectly(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedDate.from(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest9#provideSampleDates")
        void toEpochDay_convertsCorrectly(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(iso.toEpochDay(), fixed.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest9#provideSampleDates")
        void chronology_createsCorrectDateFromEpochDay(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedChronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest9#provideSampleDates")
        void chronology_createsCorrectDateFromTemporalAccessor(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(fixed, InternationalFixedChronology.INSTANCE.date(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest9#provideSampleDates")
        void until_fixedDate_returnsZeroPeriodForSameDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixed.until(fixed));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest9#provideSampleDates")
        void until_isoDate_returnsZeroPeriodForSameDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), fixed.until(iso));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.InternationalFixedChronologyTestTest9#provideSampleDates")
        void isoDate_until_fixedDate_returnsZeroPeriodForSameDate(InternationalFixedDate fixed, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(fixed));
        }
    }

    @Nested
    @DisplayName("Factory and Validation")
    class FactoryAndValidationTests {

        /**
         * @return a stream of arguments: { int year, int month, int dayOfMonth }
         */
        static Stream<Arguments> provideInvalidDateComponents() {
            return Stream.of(
                Arguments.of(-1, 13, 28), Arguments.of(-1, 13, 29), Arguments.of(0, 1, 1),
                Arguments.of(1900, -2, 1), Arguments.of(1900, 14, 1), Arguments.of(1900, 15, 1),
                Arguments.of(1900, 1, -1), Arguments.of(1900, 1, 0), Arguments.of(1900, 1, 29),
                Arguments.of(1904, -1, -2), Arguments.of(1904, -1, 0), Arguments.of(1904, -1, 1),
                Arguments.of(1900, -1, 0), Arguments.of(1900, -1, -2), Arguments.of(1900, 0, -1),
                Arguments.of(1900, 0, 1), Arguments.of(1900, 0, 2), Arguments.of(1900, 2, 29),
                Arguments.of(1900, 3, 29), Arguments.of(1900, 4, 29), Arguments.of(1900, 5, 29),
                Arguments.of(1900, 6, 29), Arguments.of(1900, 7, 29), Arguments.of(1900, 8, 29),
                Arguments.of(1900, 9, 29), Arguments.of(1900, 10, 29), Arguments.of(1900, 11, 29),
                Arguments.of(1900, 12, 29), Arguments.of(1900, 13, 30)
            );
        }

        @ParameterizedTest(name = "year={0}, month={1}, day={2}")
        @MethodSource("provideInvalidDateComponents")
        void of_throwsExceptionForInvalidDate(int year, int month, int dom) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, month, dom));
        }

        /**
         * @return a stream of arguments: { int year }
         */
        static Stream<Integer> provideNonLeapYears() {
            return Stream.of(1, 100, 200, 300, 1900);
        }

        @ParameterizedTest
        @MethodSource("provideNonLeapYears")
        void of_throwsExceptionForLeapDayInNonLeapYear(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedDate.of(year, 6, 29));
        }

        /**
         * @return a stream of arguments: { int eraValue }
         */
        static Stream<Integer> provideInvalidEraValues() {
            return Stream.of(-1, 0, 2);
        }

        @ParameterizedTest
        @MethodSource("provideInvalidEraValues")
        void eraOf_throwsExceptionForInvalidValue(int eraValue) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue));
        }

        /**
         * @return a stream of arguments: { int year }
         */
        static Stream<Integer> provideInvalidProlepticYears() {
            return Stream.of(-10, -1, 0);
        }

        @ParameterizedTest
        @MethodSource("provideInvalidProlepticYears")
        void prolepticYear_throwsExceptionForInvalidYearOfEra(int year) {
            assertThrows(DateTimeException.class, () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year));
        }

        @Test
        void prolepticYear_returnsCorrectYearForCEEra() {
            assertEquals(4, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 4));
            assertEquals(3, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 3));
            assertEquals(2, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 2));
            assertEquals(1, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 1));
            assertEquals(2000, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 2000));
            assertEquals(1582, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 1582));
        }
    }

    @Nested
    @DisplayName("Field and Property Access")
    class FieldAndPropertyTests {

        /**
         * @return a stream of arguments: { int year, int month, int day, int expectedLength }
         */
        static Stream<Arguments> provideDatesAndMonthLengths() {
            return Stream.of(
                Arguments.of(1900, 1, 28, 28), Arguments.of(1900, 2, 28, 28),
                Arguments.of(1900, 3, 28, 28), Arguments.of(1900, 4, 28, 28),
                Arguments.of(1900, 5, 28, 28), Arguments.of(1900, 6, 28, 28),
                Arguments.of(1900, 7, 28, 28), Arguments.of(1900, 8, 28, 28),
                Arguments.of(1900, 9, 28, 28), Arguments.of(1900, 10, 28, 28),
                Arguments.of(1900, 11, 28, 28), Arguments.of(1900, 12, 28, 28),
                Arguments.of(1900, 13, 29, 29), Arguments.of(1904, 6, 29, 29)
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} has month length {3}")
        @MethodSource("provideDatesAndMonthLengths")
        void lengthOfMonth_returnsCorrectLength(int year, int month, int day, int length) {
            assertEquals(length, InternationalFixedDate.of(year, month, day).lengthOfMonth());
        }

        /**
         * @return a stream of arguments: { int year, int month, int dom, TemporalField field, ValueRange range }
         */
        static Stream<Arguments> provideFieldRanges() {
            return Stream.of(
                // Leap Day and Year Day are members of months
                Arguments.of(2012, 6, 29, DAY_OF_MONTH, ValueRange.of(1, 29)),
                Arguments.of(2012, 13, 29, DAY_OF_MONTH, ValueRange.of(1, 29)),
                Arguments.of(2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)),
                Arguments.of(2012, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 29)),
                Arguments.of(2012, 13, 23, DAY_OF_MONTH, ValueRange.of(1, 29)),
                Arguments.of(2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366)),
                Arguments.of(2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)),
                // Leap Day/Year Day in own months, so (0 to 0) or (1 to 7)
                Arguments.of(2012, 6, 29, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(0, 0)),
                Arguments.of(2012, 13, 29, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(0, 0)),
                Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)),
                // Leap Day/Year Day in own months, so (0 to 0) or (1 to 4)
                Arguments.of(2012, 6, 29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)),
                Arguments.of(2012, 13, 29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)),
                // Leap Day/Year Day in own months, so (0 to 0) or (1 to 7)
                Arguments.of(2012, 6, 29, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(0, 0)),
                Arguments.of(2012, 13, 29, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(0, 0)),
                Arguments.of(2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)),
                // Leap Day/Year Day in own months, so (0 to 0) or (1 to 52)
                Arguments.of(2012, 6, 29, ALIGNED_WEEK_OF_YEAR, ValueRange.of(0, 0)),
                Arguments.of(2012, 13, 29, ALIGNED_WEEK_OF_YEAR, ValueRange.of(0, 0)),
                Arguments.of(2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)),
                // Leap Day and Year Day in own 'week's, so (0 to 0) or (1 to 7)
                Arguments.of(2012, 6, 29, DAY_OF_WEEK, ValueRange.of(0, 0)),
                Arguments.of(2012, 13, 29, DAY_OF_WEEK, ValueRange.of(0, 0)),
                Arguments.of(2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)),
                Arguments.of(2011, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 28)),
                Arguments.of(2011, 13, 23, DAY_OF_YEAR, ValueRange.of(1, 365)),
                Arguments.of(2011, 13, 23, MONTH_OF_YEAR, ValueRange.of(1, 13))
            );
        }

        @ParameterizedTest(name = "{index}: For {0}-{1}-{2}, range of {3} is {4}")
        @MethodSource("provideFieldRanges")
        void range_returnsCorrectRangeForField(int year, int month, int dom, TemporalField field, ValueRange range) {
            assertEquals(range, InternationalFixedDate.of(year, month, dom).range(field));
        }

        /**
         * @return a stream of arguments: { int year, int month, int dom, TemporalField field, long expectedValue }
         */
        static Stream<Arguments> provideFieldValues() {
            return Stream.of(
                Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5L),
                Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                Arguments.of(2014, 5, 26, DAY_OF_YEAR, 138L), // (5-1)*28 + 26
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
                Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 20L),
                Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014L * 13 + 5 - 1),
                Arguments.of(2014, 5, 26, YEAR, 2014L),
                Arguments.of(2014, 5, 26, ERA, 1L),
                Arguments.of(2012, 9, 26, DAY_OF_YEAR, 251L), // (9-1)*28 + 26 + 1 (leap)
                Arguments.of(2012, 9, 28, ALIGNED_WEEK_OF_YEAR, 36L),
                Arguments.of(2014, 13, 29, DAY_OF_WEEK, 0L), // Year Day
                Arguments.of(2014, 13, 29, DAY_OF_MONTH, 29L),
                Arguments.of(2014, 13, 29, DAY_OF_YEAR, 365L), // 13*28 + 1
                Arguments.of(2012, 13, 29, DAY_OF_YEAR, 366L), // 13*28 + 1 + 1 (leap)
                Arguments.of(2012, 6, 29, DAY_OF_WEEK, 0L), // Leap Day
                Arguments.of(2012, 6, 29, DAY_OF_MONTH, 29L),
                Arguments.of(2012, 6, 29, DAY_OF_YEAR, 169L), // 6*28 + 1
                Arguments.of(2012, 7, 1, DAY_OF_YEAR, 170L) // 6*28 + 2
            );
        }

        @ParameterizedTest(name = "{index}: For {0}-{1}-{2}, getLong({3}) is {4}")
        @MethodSource("provideFieldValues")
        void getLong_returnsCorrectValueForField(int year, int month, int dom, TemporalField field, long expected) {
            assertEquals(expected, InternationalFixedDate.of(year, month, dom).getLong(field));
        }

        /**
         * @return a stream of arguments: { InternationalFixedDate date, String expected }
         */
        static Stream<Arguments> provideToStringScenarios() {
            return Stream.of(
                Arguments.of(InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"),
                Arguments.of(InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"),
                Arguments.of(InternationalFixedDate.of(1, 13, 29), "Ifc CE 1/13/29"),
                Arguments.of(InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"), // Leap Day
                Arguments.of(InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29") // Year Day
            );
        }

        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("provideToStringScenarios")
        void toString_returnsCorrectFormat(InternationalFixedDate date, String expected) {
            assertEquals(expected, date.toString());
        }
    }

    @Nested
    @DisplayName("Adjustment")
    class AdjustmentTests {
        /**
         * @return a stream of arguments: { baseDate, field, value, expectedDate }
         */
        static Stream<Arguments> provideWithScenarios() {
            return Stream.of(
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), DAY_OF_WEEK, 1, InternationalFixedDate.of(2014, 5, 22)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), DAY_OF_MONTH, 28, InternationalFixedDate.of(2014, 5, 28)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), DAY_OF_YEAR, 364, InternationalFixedDate.of(2014, 13, 28)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), ALIGNED_WEEK_OF_MONTH, 1, InternationalFixedDate.of(2014, 5, 5)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), MONTH_OF_YEAR, 4, InternationalFixedDate.of(2014, 4, 26)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), YEAR, 2012, InternationalFixedDate.of(2012, 5, 26)),
                Arguments.of(InternationalFixedDate.of(2012, 6, 29), YEAR, 2013, InternationalFixedDate.of(2013, 6, 28)), // Leap day to non-leap year
                Arguments.of(InternationalFixedDate.of(2013, 6, 28), YEAR, 2012, InternationalFixedDate.of(2012, 6, 28)) // Non-leap to leap year
            );
        }

        @ParameterizedTest(name = "{index}: {0}.with({1}, {2}) should be {3}")
        @MethodSource("provideWithScenarios")
        void with_returnsAdjustedDate(InternationalFixedDate base, TemporalField field, long value, InternationalFixedDate expected) {
            assertEquals(expected, base.with(field, value));
        }

        /**
         * @return a stream of arguments: { baseDate, field, value }
         */
        static Stream<Arguments> provideInvalidWithScenarios() {
            return Stream.of(
                Arguments.of(InternationalFixedDate.of(2013, 1, 1), DAY_OF_MONTH, 29),
                Arguments.of(InternationalFixedDate.of(2013, 6, 1), DAY_OF_MONTH, 29),
                Arguments.of(InternationalFixedDate.of(2012, 6, 1), DAY_OF_MONTH, 30),
                Arguments.of(InternationalFixedDate.of(2013, 1, 1), DAY_OF_YEAR, 366),
                Arguments.of(InternationalFixedDate.of(2012, 1, 1), DAY_OF_YEAR, 367),
                Arguments.of(InternationalFixedDate.of(2013, 1, 1), MONTH_OF_YEAR, 14),
                Arguments.of(InternationalFixedDate.of(2013, 1, 1), YEAR, 0)
            );
        }

        @ParameterizedTest(name = "{index}: {0}.with({1}, {2}) should throw")
        @MethodSource("provideInvalidWithScenarios")
        void with_throwsExceptionForInvalidValue(InternationalFixedDate base, TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> base.with(field, value));
        }

        /**
         * @return a stream of arguments: { baseDate, expectedDate }
         */
        static Stream<Arguments> provideLastDayOfMonthScenarios() {
            return Stream.of(
                Arguments.of(InternationalFixedDate.of(2012, 6, 23), InternationalFixedDate.of(2012, 6, 29)),
                Arguments.of(InternationalFixedDate.of(2012, 6, 29), InternationalFixedDate.of(2012, 6, 29)),
                Arguments.of(InternationalFixedDate.of(2009, 6, 23), InternationalFixedDate.of(2009, 6, 28)),
                Arguments.of(InternationalFixedDate.of(2007, 13, 23), InternationalFixedDate.of(2007, 13, 29)),
                Arguments.of(InternationalFixedDate.of(2005, 13, 29), InternationalFixedDate.of(2005, 13, 29))
            );
        }

        @ParameterizedTest
        @MethodSource("provideLastDayOfMonthScenarios")
        void with_lastDayOfMonth_returnsCorrectDate(InternationalFixedDate base, InternationalFixedDate expected) {
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Calculation")
    class CalculationTests {

        /**
         * @return a stream of arguments: { baseDate, amount, unit, expectedDate }
         */
        static Stream<Arguments> providePlusScenarios() {
            return Stream.of(
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), 0, DAYS, InternationalFixedDate.of(2014, 5, 26)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), 8, DAYS, InternationalFixedDate.of(2014, 6, 6)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), -3, DAYS, InternationalFixedDate.of(2014, 5, 23)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), 3, WEEKS, InternationalFixedDate.of(2014, 6, 19)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), 3, MONTHS, InternationalFixedDate.of(2014, 8, 26)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), 3, YEARS, InternationalFixedDate.of(2017, 5, 26)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), 3, DECADES, InternationalFixedDate.of(2044, 5, 26)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), 3, CENTURIES, InternationalFixedDate.of(2314, 5, 26)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), 3, MILLENNIA, InternationalFixedDate.of(5014, 5, 26)),
                // Special days (Leap Day, Year Day)
                Arguments.of(InternationalFixedDate.of(2014, 13, 29), 8, DAYS, InternationalFixedDate.of(2015, 1, 8)),
                Arguments.of(InternationalFixedDate.of(2014, 13, 29), 3, MONTHS, InternationalFixedDate.of(2015, 3, 28)),
                Arguments.of(InternationalFixedDate.of(2012, 6, 29), 8, DAYS, InternationalFixedDate.of(2012, 7, 8)),
                Arguments.of(InternationalFixedDate.of(2012, 6, 29), 3, MONTHS, InternationalFixedDate.of(2012, 9, 28)),
                Arguments.of(InternationalFixedDate.of(2012, 6, 29), 4, YEARS, InternationalFixedDate.of(2016, 6, 29))
            );
        }

        @ParameterizedTest(name = "{index}: {0}.plus({1}, {2}) should be {3}")
        @MethodSource("providePlusScenarios")
        void plus_addsAmountCorrectly(InternationalFixedDate base, long amount, TemporalUnit unit, InternationalFixedDate expected) {
            assertEquals(expected, base.plus(amount, unit));
        }

        @ParameterizedTest(name = "{index}: {3}.minus({1}, {2}) should be {0}")
        @MethodSource("providePlusScenarios")
        void minus_subtractsAmountCorrectly(InternationalFixedDate expected, long amount, TemporalUnit unit, InternationalFixedDate base) {
            assertEquals(expected, base.minus(amount, unit));
        }

        /**
         * @return a stream of arguments: { startDate, endDate, unit, expectedAmount }
         */
        static Stream<Arguments> provideUntilScenarios() {
            return Stream.of(
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), InternationalFixedDate.of(2014, 5, 26), DAYS, 0L),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), InternationalFixedDate.of(2014, 6, 4), DAYS, 6L),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), InternationalFixedDate.of(2014, 5, 20), DAYS, -6L),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), InternationalFixedDate.of(2014, 6, 5), WEEKS, 1L),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), InternationalFixedDate.of(2014, 6, 26), MONTHS, 1L),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), InternationalFixedDate.of(2015, 5, 26), YEARS, 1L),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), InternationalFixedDate.of(2024, 5, 26), DECADES, 1L),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), InternationalFixedDate.of(2114, 5, 26), CENTURIES, 1L),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), InternationalFixedDate.of(3014, 5, 26), MILLENNIA, 1L),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), InternationalFixedDate.of(3014, 5, 26), ERAS, 0L),
                // Special days
                Arguments.of(InternationalFixedDate.of(2012, 6, 29), InternationalFixedDate.of(2012, 13, 29), DAYS, 197L),
                Arguments.of(InternationalFixedDate.of(2012, 6, 29), InternationalFixedDate.of(2012, 13, 29), WEEKS, 28L),
                Arguments.of(InternationalFixedDate.of(2012, 6, 29), InternationalFixedDate.of(2012, 13, 29), MONTHS, 7L),
                Arguments.of(InternationalFixedDate.of(2012, 6, 29), InternationalFixedDate.of(2016, 6, 29), YEARS, 4L)
            );
        }

        @ParameterizedTest(name = "{index}: {0}.until({1}, {2}) should be {3}")
        @MethodSource("provideUntilScenarios")
        void until_calculatesAmountInUnit(InternationalFixedDate start, InternationalFixedDate end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        /**
         * @return a stream of arguments: { startDate, endDate, expectedPeriod }
         */
        static Stream<Arguments> provideUntilPeriodScenarios() {
            return Stream.of(
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), InternationalFixedDate.of(2014, 5, 26), InternationalFixedChronology.INSTANCE.period(0, 0, 0)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), InternationalFixedDate.of(2014, 6, 4), InternationalFixedChronology.INSTANCE.period(0, 0, 6)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), InternationalFixedDate.of(2014, 5, 20), InternationalFixedChronology.INSTANCE.period(0, 0, -6)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), InternationalFixedDate.of(2014, 6, 26), InternationalFixedChronology.INSTANCE.period(0, 1, 0)),
                Arguments.of(InternationalFixedDate.of(2014, 5, 26), InternationalFixedDate.of(2015, 5, 26), InternationalFixedChronology.INSTANCE.period(1, 0, 0)),
                // Special days
                Arguments.of(InternationalFixedDate.of(2012, 6, 29), InternationalFixedDate.of(2016, 6, 29), InternationalFixedChronology.INSTANCE.period(4, 0, 0)),
                Arguments.of(InternationalFixedDate.of(2004, 6, 29), InternationalFixedDate.of(2004, 13, 29), InternationalFixedChronology.INSTANCE.period(0, 7, 0)),
                Arguments.of(InternationalFixedDate.of(2003, 13, 29), InternationalFixedDate.of(2004, 6, 29), InternationalFixedChronology.INSTANCE.period(0, 6, 0))
            );
        }

        @ParameterizedTest(name = "{index}: {0}.until({1}) should be {2}")
        @MethodSource("provideUntilPeriodScenarios")
        void until_calculatesPeriod(InternationalFixedDate start, InternationalFixedDate end, ChronoPeriod expected) {
            assertEquals(expected, start.until(end));
        }
    }
}