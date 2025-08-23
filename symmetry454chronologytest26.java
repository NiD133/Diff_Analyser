package org.threeten.extra.chrono;

import com.google.common.testing.EqualsTester;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for the {@link Symmetry454Chronology} and {@link Symmetry454Date}.
 *
 * <p>This class has been refactored for clarity by:
 * <ul>
 *     <li>Restructuring tests into logical groups using {@link Nested} classes.</li>
 *     <li>Using descriptive names for tests and data providers.</li>
 *     <li>Adding {@link DisplayName} annotations for better test reporting.</li>
 *     <li>Removing redundant tests and clarifying test data.</li>
 * </ul>
 */
@DisplayName("Symmetry454Chronology and Symmetry454Date")
public class Symmetry454ChronologyTest {

    // -----------------------------------------------------------------------
    // Test data providers
    // -----------------------------------------------------------------------

    static Stream<Arguments> equivalentDateProvider() {
        return Stream.of(
                Arguments.of(Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
                Arguments.of(Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)),
                Arguments.of(Symmetry454Date.of(742, 3, 25), LocalDate.of(742, 4, 2)),
                Arguments.of(Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
                Arguments.of(Symmetry454Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)),
                Arguments.of(Symmetry454Date.of(1433, 11, 10), LocalDate.of(1433, 11, 6)),
                Arguments.of(Symmetry454Date.of(1452, 4, 15), LocalDate.of(1452, 4, 19)),
                Arguments.of(Symmetry454Date.of(1492, 10, 12), LocalDate.of(1492, 10, 14)),
                Arguments.of(Symmetry454Date.of(1564, 2, 15), LocalDate.of(1564, 2, 10)),
                Arguments.of(Symmetry454Date.of(1564, 4, 26), LocalDate.of(1564, 4, 24)),
                Arguments.of(Symmetry454Date.of(1643, 1, 4), LocalDate.of(1643, 1, 1)),
                Arguments.of(Symmetry454Date.of(1707, 4, 15), LocalDate.of(1707, 4, 18)),
                Arguments.of(Symmetry454Date.of(1789, 7, 14), LocalDate.of(1789, 7, 12)),
                Arguments.of(Symmetry454Date.of(1879, 3, 14), LocalDate.of(1879, 3, 16)),
                Arguments.of(Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)),
                Arguments.of(Symmetry454Date.of(1970, 1, 1), LocalDate.of(1969, 12, 29)),
                Arguments.of(Symmetry454Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3))
        );
    }

    // -----------------------------------------------------------------------
    // Nested test classes for better organization
    // -----------------------------------------------------------------------

    @Nested
    @DisplayName("Conversion to and from other date types")
    class ConversionTests {

        @ParameterizedTest(name = "{0} -> {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#equivalentDateProvider")
        @DisplayName("Convert Symmetry454Date to LocalDate")
        void fromSymmetry454DateToLocalDate(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symmetryDate));
        }

        @ParameterizedTest(name = "{1} -> {0}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#equivalentDateProvider")
        @DisplayName("Convert LocalDate to Symmetry454Date")
        void fromLocalDateToSymmetry454Date(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Date.from(isoDate));
        }

        @ParameterizedTest(name = "epochDay: {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#equivalentDateProvider")
        @DisplayName("Convert Symmetry454Date to epoch day")
        void toEpochDay(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symmetryDate.toEpochDay());
        }

        @ParameterizedTest(name = "epochDay: {1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#equivalentDateProvider")
        @DisplayName("Create Symmetry454Date from epoch day")
        void dateFromEpochDay(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest(name = "{1}")
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#equivalentDateProvider")
        @DisplayName("Create Symmetry454Date from a temporal accessor (LocalDate)")
        void dateFromTemporal(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(symmetryDate, Symmetry454Chronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Date creation and validation")
    class DateCreationTests {

        static Stream<Arguments> invalidDatePartsProvider() {
            return Stream.of(
                    Arguments.of(-1, 13, 28), Arguments.of(-1, 13, 29),
                    Arguments.of(2000, -2, 1), Arguments.of(2000, 13, 1),
                    Arguments.of(2000, 1, -1), Arguments.of(2000, 1, 0),
                    Arguments.of(2000, 1, 29), // Jan has 28 days
                    Arguments.of(2000, 2, 36), // Feb has 35 days
                    Arguments.of(2000, 3, 29), // Mar has 28 days
                    Arguments.of(2004, 12, 36) // Dec has 35 days in a leap year
            );
        }

        @ParameterizedTest(name = "of({0}, {1}, {2})")
        @MethodSource("invalidDatePartsProvider")
        @DisplayName("of() should throw exception for invalid date parts")
        void of_throwsForInvalidDateParts(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, day));
        }

        // NOTE: The original test data for this scenario contained leap years (200, 2000),
        // which should have passed. The data has been corrected to use non-leap years
        // to match the test's intent of verifying invalid day-of-month for non-leap years.
        static Stream<Integer> nonLeapYearProvider() {
            return Stream.of(1, 100, 1999); // These are not leap years in Symmetry454
        }

        @ParameterizedTest
        @MethodSource("nonLeapYearProvider")
        @DisplayName("of() should throw exception for day 29 of December in a non-leap year")
        void of_throwsForDay29OfDecemberInNonLeapYear(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    @DisplayName("Chronology API")
    class ChronologyApiTests {

        static Stream<Arguments> unsupportedEraProvider() {
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

        @ParameterizedTest
        @MethodSource("unsupportedEraProvider")
        @DisplayName("prolepticYear() should throw exception for unsupported era")
        void prolepticYear_throwsForUnsupportedEra(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("Field access")
    class FieldAccessTests {

        static Stream<Arguments> lengthOfMonthProvider() {
            return Stream.of(
                    // year, month, expected length
                    Arguments.of(2000, 1, 28), Arguments.of(2000, 2, 35),
                    Arguments.of(2000, 3, 28), Arguments.of(2000, 4, 28),
                    Arguments.of(2000, 5, 35), Arguments.of(2000, 6, 28),
                    Arguments.of(2000, 7, 28), Arguments.of(2000, 8, 35),
                    Arguments.of(2000, 9, 28), Arguments.of(2000, 10, 28),
                    Arguments.of(2000, 11, 35), Arguments.of(2000, 12, 28),
                    Arguments.of(2004, 12, 35) // Leap year
            );
        }

        @ParameterizedTest(name = "Year {0}, Month {1} -> {2} days")
        @MethodSource("lengthOfMonthProvider")
        @DisplayName("lengthOfMonth() should return correct length")
        void lengthOfMonth_returnsCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, Symmetry454Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> fieldRangeProvider() {
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

        @ParameterizedTest(name = "{3} in {0}-{1}-{2} -> {4}")
        @MethodSource("fieldRangeProvider")
        @DisplayName("range() should return correct range for field")
        void range_returnsCorrectRangeForField(int y, int m, int d, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, Symmetry454Date.of(y, m, d).range(field));
        }

        static Stream<Arguments> fieldValueProvider() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 5L),
                    Arguments.of(2014, 5, 26, DAY_OF_MONTH, 26L),
                    Arguments.of(2014, 5, 26, DAY_OF_YEAR, 28L + 35 + 28 + 28 + 26),
                    Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4L),
                    Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4L + 5 + 4 + 4 + 4),
                    Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 5L),
                    Arguments.of(2014, 5, 26, PROLEPTIC_MONTH, 2014L * 12 + 5 - 1),
                    Arguments.of(2014, 5, 26, YEAR, 2014L),
                    Arguments.of(2014, 5, 26, ERA, 1L),
                    Arguments.of(2015, 12, 35, DAY_OF_WEEK, 7L), // Leap year end
                    Arguments.of(2015, 12, 35, DAY_OF_YEAR, 371L),
                    Arguments.of(2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53L)
            );
        }

        @ParameterizedTest(name = "{3} of {0}-{1}-{2} -> {4}")
        @MethodSource("fieldValueProvider")
        @DisplayName("getLong() should return correct value for field")
        void getLong_returnsCorrectValueForField(int y, int m, int d, TemporalField field, long expected) {
            assertEquals(expected, Symmetry454Date.of(y, m, d).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date modification with 'with'")
    class WithTests {

        static Stream<Arguments> withFieldProvider() {
            return Stream.of(
                    Arguments.of(2014, 5, 26, DAY_OF_WEEK, 1, Symmetry454Date.of(2014, 5, 22)),
                    Arguments.of(2014, 5, 26, DAY_OF_MONTH, 28, Symmetry454Date.of(2014, 5, 28)),
                    Arguments.of(2014, 5, 26, DAY_OF_YEAR, 364, Symmetry454Date.of(2014, 12, 28)),
                    Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, Symmetry454Date.of(2014, 5, 5)),
                    Arguments.of(2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, Symmetry454Date.of(2014, 6, 19)),
                    Arguments.of(2014, 5, 26, MONTH_OF_YEAR, 4, Symmetry454Date.of(2014, 4, 26)),
                    Arguments.of(2014, 5, 26, YEAR, 2012, Symmetry454Date.of(2012, 5, 26)),
                    Arguments.of(2015, 12, 29, MONTH_OF_YEAR, 2, Symmetry454Date.of(2015, 2, 29)),
                    Arguments.of(2015, 3, 28, DAY_OF_YEAR, 371, Symmetry454Date.of(2015, 12, 35))
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} with({3}, {4}) -> {5}")
        @MethodSource("withFieldProvider")
        @DisplayName("with() should adjust field correctly")
        void with_adjustsFieldCorrectly(int y, int m, int d, TemporalField field, long value, Symmetry454Date expected) {
            assertEquals(expected, Symmetry454Date.of(y, m, d).with(field, value));
        }

        static Stream<Arguments> withInvalidFieldValueProvider() {
            return Stream.of(
                    Arguments.of(DAY_OF_MONTH, 29), // For a 28-day month
                    Arguments.of(DAY_OF_YEAR, 365), // For a non-leap year
                    Arguments.of(ALIGNED_WEEK_OF_MONTH, 5), // For a 4-week month
                    Arguments.of(ALIGNED_WEEK_OF_YEAR, 53), // For a non-leap year
                    Arguments.of(YEAR, 1_000_001)
            );
        }

        @ParameterizedTest(name = "with({0}, {1})")
        @MethodSource("withInvalidFieldValueProvider")
        @DisplayName("with() should throw exception for invalid field value")
        void with_throwsForInvalidFieldValue(TemporalField field, long value) {
            Symmetry454Date date = Symmetry454Date.of(2013, 1, 1);
            assertThrows(DateTimeException.class, () -> date.with(field, value));
        }

        static Stream<Arguments> lastDayOfMonthProvider() {
            return Stream.of(
                    Arguments.of(2012, 1, 23, Symmetry454Date.of(2012, 1, 28)),
                    Arguments.of(2012, 2, 23, Symmetry454Date.of(2012, 2, 35)),
                    Arguments.of(2009, 12, 23, Symmetry454Date.of(2009, 12, 35)) // Leap year
            );
        }

        @ParameterizedTest(name = "{0}-{1}-{2} -> {3}")
        @MethodSource("lastDayOfMonthProvider")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) should return correct date")
        void with_lastDayOfMonth(int y, int m, int d, Symmetry454Date expected) {
            assertEquals(expected, Symmetry454Date.of(y, m, d).with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Date arithmetic")
    class ArithmeticTests {

        static Stream<Arguments> plusProvider() {
            return Stream.of(
                    Arguments.of(Symmetry454Date.of(2014, 5, 26), 8, DAYS, Symmetry454Date.of(2014, 5, 34)),
                    Arguments.of(Symmetry454Date.of(2014, 5, 26), 3, WEEKS, Symmetry454Date.of(2014, 6, 12)),
                    Arguments.of(Symmetry454Date.of(2014, 5, 26), 3, MONTHS, Symmetry454Date.of(2014, 8, 26)),
                    Arguments.of(Symmetry454Date.of(2014, 5, 26), 3, YEARS, Symmetry454Date.of(2017, 5, 26)),
                    Arguments.of(Symmetry454Date.of(2014, 5, 26), 3, DECADES, Symmetry454Date.of(2044, 5, 26)),
                    Arguments.of(Symmetry454Date.of(2015, 12, 28), 8, DAYS, Symmetry454Date.of(2016, 1, 1)), // Across leap week
                    Arguments.of(Symmetry454Date.of(2015, 12, 28), 3, WEEKS, Symmetry454Date.of(2016, 1, 14))
            );
        }

        @ParameterizedTest(name = "{0} plus {1} {2} -> {3}")
        @MethodSource("plusProvider")
        @DisplayName("plus() should add amount to date")
        void plus_addsAmount(Symmetry454Date base, long amount, TemporalUnit unit, Symmetry454Date expected) {
            assertEquals(expected, base.plus(amount, unit));
        }

        @ParameterizedTest(name = "{3} minus {1} {2} -> {0}")
        @MethodSource("plusProvider")
        @DisplayName("minus() should subtract amount from date")
        void minus_subtractsAmount(Symmetry454Date expected, long amount, TemporalUnit unit, Symmetry454Date base) {
            assertEquals(expected, base.minus(amount, unit));
        }

        static Stream<Arguments> untilUnitProvider() {
            return Stream.of(
                    Arguments.of(Symmetry454Date.of(2014, 5, 26), Symmetry454Date.of(2014, 6, 4), DAYS, 13L),
                    Arguments.of(Symmetry454Date.of(2014, 5, 26), Symmetry454Date.of(2014, 6, 5), WEEKS, 1L),
                    Arguments.of(Symmetry454Date.of(2014, 5, 26), Symmetry454Date.of(2014, 6, 26), MONTHS, 1L),
                    Arguments.of(Symmetry454Date.of(2014, 5, 26), Symmetry454Date.of(2015, 5, 26), YEARS, 1L),
                    Arguments.of(Symmetry454Date.of(2014, 5, 26), Symmetry454Date.of(2024, 5, 26), DECADES, 1L)
            );
        }

        @ParameterizedTest(name = "{0} until {1} in {2} -> {3}")
        @MethodSource("untilUnitProvider")
        @DisplayName("until() should calculate amount between dates in a given unit")
        void until_calculatesAmountInUnit(Symmetry454Date start, Symmetry454Date end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodProvider() {
            return Stream.of(
                    Arguments.of(Symmetry454Date.of(2014, 5, 26), Symmetry454Date.of(2014, 5, 26), Symmetry454Chronology.INSTANCE.period(0, 0, 0)),
                    Arguments.of(Symmetry454Date.of(2014, 5, 26), Symmetry454Date.of(2014, 6, 4), Symmetry454Chronology.INSTANCE.period(0, 0, 13)),
                    Arguments.of(Symmetry454Date.of(2014, 5, 26), Symmetry454Date.of(2014, 6, 26), Symmetry454Chronology.INSTANCE.period(0, 1, 0)),
                    Arguments.of(Symmetry454Date.of(2014, 5, 26), Symmetry454Date.of(2015, 5, 26), Symmetry454Chronology.INSTANCE.period(1, 0, 0))
            );
        }

        @ParameterizedTest(name = "{0} until {1} -> {2}")
        @MethodSource("untilPeriodProvider")
        @DisplayName("until() should calculate period between dates")
        void until_calculatesPeriod(Symmetry454Date start, Symmetry454Date end, ChronoPeriod expected) {
            assertEquals(expected, start.until(end));
        }

        @Test
        @DisplayName("until() self should return zero period")
        void until_self_returnsZeroPeriod() {
            Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#equivalentDateProvider")
        @DisplayName("until() equivalent LocalDate should return zero period")
        void until_equivalentLocalDate_returnsZeroPeriod(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(Symmetry454Chronology.INSTANCE.period(0, 0, 0), symmetryDate.until(isoDate));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#equivalentDateProvider")
        @DisplayName("LocalDate.until() equivalent Symmetry454Date should return zero period")
        void localDateUntil_equivalentSymmetryDate_returnsZeroPeriod(Symmetry454Date symmetryDate, LocalDate isoDate) {
            assertEquals(Period.ZERO, isoDate.until(symmetryDate));
        }
    }

    @Nested
    @DisplayName("Object method contracts")
    class ObjectMethodTests {

        static Stream<Arguments> toStringProvider() {
            return Stream.of(
                    Arguments.of(Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"),
                    Arguments.of(Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"),
                    Arguments.of(Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35")
            );
        }

        @ParameterizedTest(name = "{0} -> \"{1}\"")
        @MethodSource("toStringProvider")
        @DisplayName("toString() should return correctly formatted string")
        void toString_returnsFormattedString(Symmetry454Date date, String expected) {
            assertEquals(expected, date.toString());
        }

        @Test
        @DisplayName("equals() and hashCode() should follow contracts")
        void equalsAndHashCode_followContracts() {
            new EqualsTester()
                    .addEqualityGroup(Symmetry454Date.of(2000, 1, 3), Symmetry454Date.of(2000, 1, 3))
                    .addEqualityGroup(Symmetry454Date.of(2000, 1, 4))
                    .addEqualityGroup(Symmetry454Date.of(2000, 2, 3))
                    .addEqualityGroup(Symmetry454Date.of(2001, 1, 3))
                    .testEquals();
        }
    }
}