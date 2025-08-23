package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_MONTH;
import static java.time.temporal.ChronoField.ALIGNED_WEEK_OF_YEAR;
import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.DAY_OF_WEEK;
import static java.time.temporal.ChronoField.DAY_OF_YEAR;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
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
class Symmetry010ChronologyTest {

    private static final Symmetry010Date DATE_2014_05_26 = Symmetry010Date.of(2014, 5, 26);
    private static final Symmetry010Date DATE_2015_12_28_LEAP_YEAR = Symmetry010Date.of(2015, 12, 28);
    private static final Symmetry010Date DATE_2015_12_37_LEAP_DAY = Symmetry010Date.of(2015, 12, 37);

    // A sample date in a standard year: 2014-05-26 (Sym010) is 2014-05-26 (ISO)
    private static final Symmetry010Date SAMPLE_SYM_DATE = Symmetry010Date.of(2014, 5, 26);
    private static final LocalDate SAMPLE_ISO_DATE = LocalDate.of(2014, 5, 26);

    @Nested
    @DisplayName("Conversion and Equivalence Tests")
    class ConversionTests {

        static Stream<Arguments> sampleSymmetryAndIsoDates() {
            return Stream.of(
                // Dates chosen to cover various historical periods and calendar shifts
                Arguments.of(Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1)),
                Arguments.of(Symmetry010Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)),
                Arguments.of(Symmetry010Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)),
                Arguments.of(Symmetry010Date.of(1879, 3, 14), LocalDate.of(1879, 3, 14)),
                Arguments.of(Symmetry010Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)),
                Arguments.of(Symmetry010Date.of(2000, 1, 1), LocalDate.of(2000, 1, 3))
            );
        }

        @ParameterizedTest
        @MethodSource("sampleSymmetryAndIsoDates")
        @DisplayName("should convert from Symmetry010Date to LocalDate")
        void fromSymmetryDate_toLocalDate_convertsCorrectly(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(symDate));
        }

        @ParameterizedTest
        @MethodSource("sampleSymmetryAndIsoDates")
        @DisplayName("should convert from LocalDate to Symmetry010Date")
        void fromLocalDate_toSymmetryDate_convertsCorrectly(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(symDate, Symmetry010Date.from(isoDate));
        }

        @ParameterizedTest
        @MethodSource("sampleSymmetryAndIsoDates")
        @DisplayName("should create Symmetry010Date from epoch day")
        void dateEpochDay_createsCorrectDate(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(symDate, Symmetry010Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("sampleSymmetryAndIsoDates")
        @DisplayName("should convert Symmetry010Date to epoch day")
        void toEpochDay_convertsCorrectly(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), symDate.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("sampleSymmetryAndIsoDates")
        @DisplayName("should create Symmetry010Date from a TemporalAccessor (LocalDate)")
        void dateFromTemporal_createsCorrectDate(Symmetry010Date symDate, LocalDate isoDate) {
            assertEquals(symDate, Symmetry010Chronology.INSTANCE.date(isoDate));
        }
    }

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        static Stream<Arguments> invalidDateProvider() {
            return Stream.of(
                Arguments.of(2000, 0, 1, "Zero month"),
                Arguments.of(2000, 13, 1, "Month > 12"),
                Arguments.of(2000, 1, 0, "Zero day"),
                Arguments.of(2000, 1, 31, "Day > 30 for a 30-day month"),
                Arguments.of(2000, 2, 32, "Day > 31 for a 31-day month"),
                Arguments.of(2000, 12, 31, "Day > 30 for a non-leap December")
            );
        }

        @ParameterizedTest(name = "[{index}] {3}: of({0}, {1}, {2})")
        @MethodSource("invalidDateProvider")
        @DisplayName("of() should throw exception for invalid date parts")
        void of_withInvalidDateParts_throwsDateTimeException(int year, int month, int day, String reason) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, day));
        }

        static Stream<Arguments> nonLeapYearsForLeapDayTest() {
            return Stream.of(
                Arguments.of(1, "Year 1 is not a leap year"),
                Arguments.of(2000, "Year 2000 is not a leap year in Sym010"),
                Arguments.of(2014, "Year 2014 is not a leap year")
            );
        }

        @ParameterizedTest(name = "[{index}] {1}: of({0}, 12, 37)")
        @MethodSource("nonLeapYearsForLeapDayTest")
        @DisplayName("of() should throw exception for leap day in a non-leap year")
        void of_withLeapDayInNonLeapYear_throwsDateTimeException(int year, String reason) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }
    }

    @Nested
    @DisplayName("Date Property Tests")
    class PropertyTests {

        static Stream<Arguments> monthLengthProvider() {
            return Stream.of(
                // year, month, expectedLength, description
                Arguments.of(2014, 1, 30, "Standard 30-day month (Jan)"),
                Arguments.of(2014, 2, 31, "Long 31-day month (Feb)"),
                Arguments.of(2014, 12, 30, "Standard December in non-leap year"),
                Arguments.of(2015, 12, 37, "Leap December in leap year (2015 is a leap year)")
            );
        }

        @ParameterizedTest(name = "[{index}] {3}")
        @MethodSource("monthLengthProvider")
        @DisplayName("lengthOfMonth() should return correct value")
        void lengthOfMonth_returnsCorrectValue(int year, int month, int expectedLength, String description) {
            assertEquals(expectedLength, Symmetry010Date.of(year, month, 1).lengthOfMonth());
        }

        static Stream<Arguments> getLongProvider() {
            return Stream.of(
                Arguments.of(DATE_2014_05_26, DAY_OF_WEEK, 2), // A Monday
                Arguments.of(DATE_2014_05_26, DAY_OF_MONTH, 26),
                Arguments.of(DATE_2014_05_26, DAY_OF_YEAR, 146), // (4*30 + 1*31) + 26 - 1 = 146
                Arguments.of(DATE_2014_05_26, MONTH_OF_YEAR, 5),
                Arguments.of(DATE_2014_05_26, YEAR, 2014),
                Arguments.of(DATE_2014_05_26, ERA, 1),
                Arguments.of(DATE_2015_12_37_LEAP_DAY, DAY_OF_WEEK, 5), // A Friday
                Arguments.of(DATE_2015_12_37_LEAP_DAY, DAY_OF_MONTH, 37),
                Arguments.of(DATE_2015_12_37_LEAP_DAY, DAY_OF_YEAR, 371), // Last day of leap year
                Arguments.of(DATE_2015_12_37_LEAP_DAY, ALIGNED_WEEK_OF_YEAR, 53)
            );
        }

        @ParameterizedTest
        @MethodSource("getLongProvider")
        @DisplayName("getLong() should return correct value for field")
        void getLong_forField_returnsCorrectValue(Symmetry010Date date, TemporalField field, long expected) {
            assertEquals(expected, date.getLong(field));
        }

        static Stream<Arguments> rangeProvider() {
            return Stream.of(
                Arguments.of(DAY_OF_MONTH, ValueRange.of(1, 30), "Jan (30 days)"),
                Arguments.of(DAY_OF_MONTH, ValueRange.of(1, 31), "Feb (31 days)"),
                Arguments.of(DAY_OF_MONTH, ValueRange.of(1, 37), "Dec in leap year"),
                Arguments.of(DAY_OF_YEAR, ValueRange.of(1, 364), "Non-leap year"),
                Arguments.of(DAY_OF_YEAR, ValueRange.of(1, 371), "Leap year"),
                Arguments.of(ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4), "Standard month"),
                Arguments.of(ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5), "Leap December"),
                Arguments.of(ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52), "Non-leap year"),
                Arguments.of(ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53), "Leap year")
            );
        }

        @ParameterizedTest(name = "[{index}] range({0}) should be {1} for {2}")
        @MethodSource("rangeProvider")
        @DisplayName("range() should return correct value range for field")
        void range_forField_returnsCorrectRange(TemporalField field, ValueRange expectedRange, String condition) {
            Symmetry010Date date;
            switch(condition) {
                case "Jan (30 days)": date = Symmetry010Date.of(2014, 1, 1); break;
                case "Feb (31 days)": date = Symmetry010Date.of(2014, 2, 1); break;
                case "Dec in leap year":
                case "Leap year":
                case "Leap December":
                    date = DATE_2015_12_28_LEAP_YEAR; break;
                default: date = DATE_2014_05_26; break;
            }
            assertEquals(expectedRange, date.range(field));
        }
    }

    @Nested
    @DisplayName("Arithmetic and Modification Tests")
    class ArithmeticTests {

        static Stream<Arguments> plusProvider() {
            return Stream.of(
                Arguments.of(8, DAYS, Symmetry010Date.of(2014, 6, 3)),
                Arguments.of(3, WEEKS, Symmetry010Date.of(2014, 6, 16)),
                Arguments.of(3, MONTHS, Symmetry010Date.of(2014, 8, 26)),
                Arguments.of(3, YEARS, Symmetry010Date.of(2017, 5, 26)),
                Arguments.of(3, DECADES, Symmetry010Date.of(2044, 5, 26)),
                Arguments.of(1, CENTURIES, Symmetry010Date.of(2114, 5, 26)),
                Arguments.of(1, MILLENNIA, Symmetry010Date.of(3014, 5, 26))
            );
        }

        @ParameterizedTest
        @MethodSource("plusProvider")
        @DisplayName("plus() should add amounts correctly")
        void plus_withVariousUnits_calculatesCorrectly(long amount, TemporalUnit unit, Symmetry010Date expected) {
            assertEquals(expected, DATE_2014_05_26.plus(amount, unit));
        }

        @ParameterizedTest
        @MethodSource("plusProvider")
        @DisplayName("minus() should subtract amounts correctly")
        void minus_withVariousUnits_calculatesCorrectly(long amount, TemporalUnit unit, Symmetry010Date ignored) {
            // Re-using plusProvider data: if start + amount = end, then end - amount = start
            Symmetry010Date end = ignored; // The expected date from the plus() test
            assertEquals(DATE_2014_05_26, end.minus(amount, unit));
        }

        static Stream<Arguments> withProvider() {
            return Stream.of(
                Arguments.of(DAY_OF_WEEK, 1, Symmetry010Date.of(2014, 5, 20)),
                Arguments.of(DAY_OF_MONTH, 1, Symmetry010Date.of(2014, 5, 1)),
                Arguments.of(DAY_OF_YEAR, 1, Symmetry010Date.of(2014, 1, 1)),
                Arguments.of(MONTH_OF_YEAR, 1, Symmetry010Date.of(2014, 1, 26)),
                Arguments.of(YEAR, 2015, Symmetry010Date.of(2015, 5, 26))
            );
        }

        @ParameterizedTest
        @MethodSource("withProvider")
        @DisplayName("with() should adjust field correctly")
        void with_field_adjustsDate(TemporalField field, long value, Symmetry010Date expected) {
            assertEquals(expected, DATE_2014_05_26.with(field, value));
        }

        @Test
        @DisplayName("with() using lastDayOfMonth adjuster should find correct date")
        void with_lastDayOfMonth_adjustsCorrectly() {
            Symmetry010Date date = Symmetry010Date.of(2015, 2, 10); // A long month (31 days) in a leap year
            Symmetry010Date expected = Symmetry010Date.of(2015, 2, 31);
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Until (Difference) Tests")
    class UntilTests {

        @Test
        @DisplayName("until() with another Symmetry010Date should return zero period")
        void until_self_returnsZeroPeriod() {
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), SAMPLE_SYM_DATE.until(SAMPLE_SYM_DATE));
        }

        @Test
        @DisplayName("until() with an equivalent LocalDate should return zero period")
        void until_equivalentIsoDate_returnsZeroPeriod() {
            // Note: until(Temporal) converts the argument to the same chronology
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), SAMPLE_SYM_DATE.until(SAMPLE_ISO_DATE));
        }

        @Test
        @DisplayName("until() with an equivalent LocalDate (ISO chronology) should return zero period")
        void isoUntil_equivalentSymmetryDate_returnsZeroPeriod() {
            assertEquals(Period.ZERO, SAMPLE_ISO_DATE.until(SAMPLE_SYM_DATE));
        }

        static Stream<Arguments> untilUnitProvider() {
            Symmetry010Date start = Symmetry010Date.of(2014, 5, 26);
            return Stream.of(
                Arguments.of(start, Symmetry010Date.of(2014, 6, 4), DAYS, 9L),
                Arguments.of(start, Symmetry010Date.of(2014, 6, 5), WEEKS, 1L),
                Arguments.of(start, Symmetry010Date.of(2014, 6, 26), MONTHS, 1L),
                Arguments.of(start, Symmetry010Date.of(2015, 5, 26), YEARS, 1L),
                Arguments.of(start, Symmetry010Date.of(2024, 5, 26), DECADES, 1L),
                Arguments.of(start, Symmetry010Date.of(2114, 5, 26), CENTURIES, 1L),
                Arguments.of(start, Symmetry010Date.of(3014, 5, 26), MILLENNIA, 1L),
                Arguments.of(start, Symmetry010Date.of(3014, 5, 26), ERAS, 0L)
            );
        }

        @ParameterizedTest
        @MethodSource("untilUnitProvider")
        @DisplayName("until() should calculate difference in units")
        void until_temporalUnit_calculatesDifference(Symmetry010Date start, Symmetry010Date end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        static Stream<Arguments> untilPeriodProvider() {
            Symmetry010Date start = Symmetry010Date.of(2014, 5, 26);
            return Stream.of(
                Arguments.of(start, Symmetry010Date.of(2014, 5, 26), Symmetry010Chronology.INSTANCE.period(0, 0, 0)),
                Arguments.of(start, Symmetry010Date.of(2014, 6, 4), Symmetry010Chronology.INSTANCE.period(0, 0, 9)),
                Arguments.of(start, Symmetry010Date.of(2014, 6, 26), Symmetry010Chronology.INSTANCE.period(0, 1, 0)),
                Arguments.of(start, Symmetry010Date.of(2015, 6, 27), Symmetry010Chronology.INSTANCE.period(1, 1, 1))
            );
        }

        @ParameterizedTest
        @MethodSource("untilPeriodProvider")
        @DisplayName("until() should calculate period between dates")
        void until_chronoPeriod_calculatesPeriod(Symmetry010Date start, Symmetry010Date end, ChronoPeriod expected) {
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology API Tests")
    class ChronologyApiTests {

        @Test
        @DisplayName("Chronology.of() should return singleton instance")
        void chronologyOf_returnsSingleton() {
            Chronology chrono = Chronology.of("Sym010");
            assertNotNull(chrono);
            assertEquals(Symmetry010Chronology.INSTANCE, chrono);
            assertEquals("Sym010", chrono.getId());
            assertEquals(null, chrono.getCalendarType());
        }

        static Stream<Arguments> nonSymmetryEras() {
            return Stream.of(
                Arguments.of(HijrahEra.AH),
                Arguments.of(JapaneseEra.HEISEI),
                Arguments.of(MinguoEra.ROC),
                Arguments.of(ThaiBuddhistEra.BE)
            );
        }

        @ParameterizedTest
        @MethodSource("nonSymmetryEras")
        @DisplayName("prolepticYear() should throw exception for foreign eras")
        void prolepticYear_withForeignEra_throwsClassCastException(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 1));
        }

        @Test
        @DisplayName("toString() should return correct format")
        void toString_returnsCorrectFormat() {
            Symmetry010Date date = Symmetry010Date.of(2009, 12, 37);
            assertEquals("Sym010 CE 2009/12/37", date.toString());
        }
    }
}