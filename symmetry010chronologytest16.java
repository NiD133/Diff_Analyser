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
import java.time.Month;
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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("Symmetry010Chronology Tests")
class Symmetry010ChronologyTest {

    // Helper for creating dates with less verbose code
    private Symmetry010Date symDate(int year, int month, int day) {
        return Symmetry010Date.of(year, month, day);
    }

    @Nested
    @DisplayName("Interoperability with ISO Chronology")
    class InteroperabilityWithIsoTests {

        public static Object[][] provideSampleSymmetryAndIsoDates() {
            return new Object[][] {
                    // A common date that aligns perfectly
                    { symDate(1879, 3, 14), LocalDate.of(1879, 3, 14) },
                    // A date from the original historical examples
                    { symDate(1066, 10, 14), LocalDate.of(1066, 10, 14) },
                    // A date that crosses the ISO year boundary due to Symmetry010's leap week
                    { symDate(1999, 12, 29), LocalDate.of(2000, 1, 1) },
                    // The start of the Unix epoch
                    { symDate(1970, 1, 4), LocalDate.of(1970, 1, 1) },
            };
        }

        @ParameterizedTest
        @MethodSource("provideSampleSymmetryAndIsoDates")
        @DisplayName("should convert from Symmetry010Date to LocalDate")
        void shouldConvertFromSymmetry010DateToLocalDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso, LocalDate.from(sym010));
        }

        @ParameterizedTest
        @MethodSource("provideSampleSymmetryAndIsoDates")
        @DisplayName("should convert from LocalDate to Symmetry010Date")
        void shouldConvertFromLocalDateToSymmetry010Date(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Date.from(iso));
        }

        @ParameterizedTest
        @MethodSource("provideSampleSymmetryAndIsoDates")
        @DisplayName("should create a Symmetry010Date from an ISO date via the chronology")
        void shouldCreateSymmetry010DateFromIsoDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.date(iso));
        }

        @ParameterizedTest
        @MethodSource("provideSampleSymmetryAndIsoDates")
        @DisplayName("should have the same epoch day as its ISO equivalent")
        void shouldHaveSameEpochDayAsIso(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(iso.toEpochDay(), sym010.toEpochDay());
        }

        @ParameterizedTest
        @MethodSource("provideSampleSymmetryAndIsoDates")
        @DisplayName("should create the correct date from an epoch day")
        void shouldCreateCorrectDateFromEpochDay(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(sym010, Symmetry010Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("provideSampleSymmetryAndIsoDates")
        @DisplayName("should calculate a zero period when until() is called with an equivalent ISO date")
        void until_shouldReturnZeroPeriodForEquivalentIsoDate(Symmetry010Date sym010, LocalDate iso) {
            assertEquals(Period.ZERO, iso.until(sym010));
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(iso));
        }
    }

    @Nested
    @DisplayName("Date Creation and Validation")
    class DateCreationTests {

        public static Object[][] provideInvalidDateParts() {
            return new Object[][] {
                    { 2000, 13, 1 }, { 2000, 0, 1 }, { 2000, 1, 0 },
                    { 2000, 1, 31 }, // Jan has 30 days
                    { 2000, 2, 32 }, // Feb has 31 days
                    { 2004, 12, 38 } // Leap year Dec has 37 days
            };
        }

        @ParameterizedTest
        @MethodSource("provideInvalidDateParts")
        @DisplayName("of() should throw DateTimeException for invalid year, month, or day")
        void of_shouldThrowException_forInvalidDateParts(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, day));
        }

        @ParameterizedTest
        @MethodSource("provideNonLeapYears")
        @DisplayName("of() should throw DateTimeException for leap day in a non-leap year")
        void of_shouldThrowException_forInvalidLeapDay(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37));
        }

        public static Object[][] provideNonLeapYears() {
            return new Object[][] { { 1 }, { 100 }, { 2000 } };
        }
    }

    @Nested
    @DisplayName("Field Accessors")
    class FieldAccessorTests {

        public static Object[][] provideDataForLengthOfMonth() {
            return new Object[][] {
                    { 2000, 1, 30 }, { 2000, 2, 31 }, { 2000, 3, 30 },
                    { 2000, 4, 30 }, { 2000, 5, 31 }, { 2000, 6, 30 },
                    { 2000, 7, 30 }, { 2000, 8, 31 }, { 2000, 9, 30 },
                    { 2000, 10, 30 }, { 2000, 11, 31 },
                    { 2000, 12, 30 }, // Common year December
                    { 2004, 12, 37 }  // Leap year December
            };
        }

        @ParameterizedTest
        @MethodSource("provideDataForLengthOfMonth")
        @DisplayName("lengthOfMonth() should return the correct number of days")
        void lengthOfMonth_shouldReturnCorrectLength(int year, int month, int expectedLength) {
            assertEquals(expectedLength, symDate(year, month, 1).lengthOfMonth());
        }

        public static Object[][] provideDataForRanges() {
            return new Object[][] {
                    { symDate(2012, 1, 23), DAY_OF_MONTH, ValueRange.of(1, 30) },
                    { symDate(2012, 2, 23), DAY_OF_MONTH, ValueRange.of(1, 31) },
                    { symDate(2015, 12, 23), DAY_OF_MONTH, ValueRange.of(1, 37) }, // Leap year
                    { symDate(2012, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 364) },  // Common year
                    { symDate(2015, 1, 23), DAY_OF_YEAR, ValueRange.of(1, 371) },  // Leap year
                    { symDate(2012, 1, 23), MONTH_OF_YEAR, ValueRange.of(1, 12) },
                    { symDate(2015, 12, 23), ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5) },
                    { symDate(2015, 12, 30), ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53) }
            };
        }

        @ParameterizedTest
        @MethodSource("provideDataForRanges")
        @DisplayName("range() should return the correct value range for a given field")
        void range_shouldReturnCorrectRangeForField(Symmetry010Date date, TemporalField field, ValueRange expectedRange) {
            assertEquals(expectedRange, date.range(field));
        }

        public static Object[][] provideDataForGetLong() {
            return new Object[][] {
                    // Date: 2014-05-26
                    { 2014, 5, 26, DAY_OF_WEEK, 2L },
                    { 2014, 5, 26, DAY_OF_MONTH, 26L },
                    // Day of Year: Jan(30) + Feb(31) + Mar(30) + Apr(30) + 26 = 147
                    { 2014, 5, 26, DAY_OF_YEAR, 147L },
                    { 2014, 5, 26, MONTH_OF_YEAR, 5L },
                    // Proleptic Month: (2014 - 1) * 12 + 5 = 24161
                    { 2014, 5, 26, PROLEPTIC_MONTH, (2014L - 1) * 12 + 5 -1 },
                    { 2014, 5, 26, YEAR, 2014L },
                    { 2014, 5, 26, ERA, 1L },
                    // Date: 2015-12-37 (Leap Day)
                    { 2015, 12, 37, DAY_OF_WEEK, 5L },
                    { 2015, 12, 37, DAY_OF_MONTH, 37L },
                    // Day of Year: 364 (normal year) + 7 (leap week) = 371
                    { 2015, 12, 37, DAY_OF_YEAR, 371L },
                    { 2015, 12, 37, ALIGNED_WEEK_OF_YEAR, 53L }
            };
        }

        @ParameterizedTest
        @MethodSource("provideDataForGetLong")
        @DisplayName("getLong() should return the correct value for a given field")
        void getLong_shouldReturnCorrectFieldValue(int year, int month, int day, TemporalField field, long expected) {
            assertEquals(expected, symDate(year, month, day).getLong(field));
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class DateManipulationTests {

        public static Object[][] provideDataForWith() {
            return new Object[][] {
                    { 2014, 5, 26, DAY_OF_WEEK, 1, symDate(2014, 5, 20) },
                    { 2014, 5, 26, DAY_OF_MONTH, 28, symDate(2014, 5, 28) },
                    { 2014, 5, 26, DAY_OF_YEAR, 364, symDate(2014, 12, 30) },
                    { 2014, 5, 26, MONTH_OF_YEAR, 4, symDate(2014, 4, 26) },
                    { 2014, 5, 26, YEAR, 2012, symDate(2012, 5, 26) },
                    { 2015, 12, 37, YEAR, 2004, symDate(2004, 12, 37) } // Adjust leap day to another leap year
            };
        }

        @ParameterizedTest
        @MethodSource("provideDataForWith")
        @DisplayName("with() should adjust a field to a new value correctly")
        void with_shouldSetFieldToNewValue(int y, int m, int d, TemporalField f, long val, Symmetry010Date expected) {
            assertEquals(expected, symDate(y, m, d).with(f, val));
        }

        public static Object[][] provideInvalidFieldValues() {
            return new Object[][] {
                    { DAY_OF_WEEK, 8L }, { DAY_OF_MONTH, 38L }, { DAY_OF_YEAR, 372L },
                    { MONTH_OF_YEAR, 14L }, { YEAR, 1_000_001L }
            };
        }

        @ParameterizedTest
        @MethodSource("provideInvalidFieldValues")
        @DisplayName("with() should throw DateTimeException for an invalid field value")
        void with_shouldThrowForInvalidFieldValue(TemporalField field, long value) {
            assertThrows(DateTimeException.class, () -> symDate(2013, 1, 1).with(field, value));
        }

        @Test
        @DisplayName("with() should throw DateTimeException when adjusting with an ISO Month")
        void with_isoMonth_shouldThrowException() {
            assertThrows(DateTimeException.class, () -> symDate(2000, 1, 4).with(Month.APRIL));
        }

        public static Object[][] provideDatesForLastDayOfMonth() {
            return new Object[][] {
                    { symDate(2012, 1, 23), symDate(2012, 1, 30) },
                    { symDate(2012, 2, 23), symDate(2012, 2, 31) },
                    { symDate(2009, 12, 23), symDate(2009, 12, 37) } // Leap year
            };
        }

        @ParameterizedTest
        @MethodSource("provideDatesForLastDayOfMonth")
        @DisplayName("with(TemporalAdjusters.lastDayOfMonth()) should adjust to the correct last day")
        void with_lastDayOfMonth_shouldAdjustToLastDay(Symmetry010Date date, Symmetry010Date expected) {
            assertEquals(expected, date.with(TemporalAdjusters.lastDayOfMonth()));
        }

        public static Object[][] provideDataForPlus() {
            return new Object[][] {
                    { 8, DAYS, symDate(2014, 6, 3) },
                    { 3, WEEKS, symDate(2014, 6, 16) },
                    { 3, MONTHS, symDate(2014, 8, 26) },
                    { 3, YEARS, symDate(2017, 5, 26) },
                    { 3, DECADES, symDate(2044, 5, 26) },
                    { 3, CENTURIES, symDate(2314, 5, 26) },
                    { 3, MILLENNIA, symDate(5014, 5, 26) },
                    // Test crossing leap week
                    { 3, WEEKS, symDate(2016, 1, 12) }
            };
        }

        @ParameterizedTest
        @MethodSource("provideDataForPlus")
        @DisplayName("plus() should add a temporal amount correctly")
        void plus_shouldAddTemporalAmount(long amount, TemporalUnit unit, Symmetry010Date expected) {
            Symmetry010Date start = (unit == WEEKS && amount == 3 && expected.getYear() == 2016)
                    ? symDate(2015, 12, 28) // Start date for leap week test
                    : symDate(2014, 5, 26);
            assertEquals(expected, start.plus(amount, unit));
        }

        @Test
        @DisplayName("minus() should subtract a temporal amount correctly")
        void minus_shouldSubtractTemporalAmount() {
            Symmetry010Date start = symDate(2014, 5, 26);
            assertEquals(symDate(2014, 5, 23), start.minus(3, DAYS));
            assertEquals(symDate(2014, 4, 21), start.minus(5, WEEKS));
            assertEquals(symDate(2009, 5, 26), start.minus(5, YEARS));
        }
    }

    @Nested
    @DisplayName("Period Calculation")
    class PeriodTests {

        @Test
        @DisplayName("until() should return a zero period for the same date")
        void until_shouldReturnZeroPeriodForSameDate() {
            Symmetry010Date date = symDate(2014, 5, 26);
            assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), date.until(date));
        }

        public static Object[][] provideDataForUntil() {
            return new Object[][] {
                    { symDate(2014, 5, 26), symDate(2014, 6, 4), DAYS, 9L },
                    { symDate(2014, 5, 26), symDate(2014, 5, 20), DAYS, -6L },
                    { symDate(2014, 5, 26), symDate(2014, 6, 5), WEEKS, 1L },
                    { symDate(2014, 5, 26), symDate(2014, 6, 26), MONTHS, 1L },
                    { symDate(2014, 5, 26), symDate(2015, 5, 26), YEARS, 1L },
                    { symDate(2014, 5, 26), symDate(2024, 5, 26), DECADES, 1L },
                    { symDate(2014, 5, 26), symDate(2114, 5, 26), CENTURIES, 1L },
                    { symDate(2014, 5, 26), symDate(3014, 5, 26), MILLENNIA, 1L },
                    { symDate(2014, 5, 26), symDate(3014, 5, 26), ERAS, 0L }
            };
        }

        @ParameterizedTest
        @MethodSource("provideDataForUntil")
        @DisplayName("until() should calculate the correct amount for a given temporal unit")
        void until_shouldCalculateCorrectAmountForUnit(Symmetry010Date start, Symmetry010Date end, TemporalUnit unit, long expected) {
            assertEquals(expected, start.until(end, unit));
        }

        public static Object[][] provideDataForUntilPeriod() {
            return new Object[][] {
                    { symDate(2014, 5, 26), symDate(2014, 6, 4), Symmetry010Chronology.INSTANCE.period(0, 0, 9) },
                    { symDate(2014, 5, 26), symDate(2014, 6, 26), Symmetry010Chronology.INSTANCE.period(0, 1, 0) },
                    { symDate(2014, 5, 26), symDate(2015, 5, 26), Symmetry010Chronology.INSTANCE.period(1, 0, 0) },
                    { symDate(2014, 5, 26), symDate(2015, 6, 27), Symmetry010Chronology.INSTANCE.period(1, 1, 1) }
            };
        }

        @ParameterizedTest
        @MethodSource("provideDataForUntilPeriod")
        @DisplayName("until() should return the correct ChronoPeriod")
        void until_shouldReturnCorrectPeriod(Symmetry010Date start, Symmetry010Date end, ChronoPeriod expected) {
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology-specific Behavior")
    class ChronologySpecificTests {

        public static Object[][] provideInvalidEras() {
            return new Era[][] {
                    { HijrahEra.AH }, { JapaneseEra.HEISEI }, { MinguoEra.ROC }, { ThaiBuddhistEra.BE }
            };
        }

        @ParameterizedTest
        @MethodSource("provideInvalidEras")
        @DisplayName("prolepticYear() should throw ClassCastException for non-ISO eras")
        void prolepticYear_shouldThrowExceptionForNonSymmetryEra(Era era) {
            assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4));
        }
    }

    @Nested
    @DisplayName("Object Behavior")
    class ObjectBehaviorTests {

        public static Object[][] provideDataForToString() {
            return new Object[][] {
                    { symDate(1, 1, 1), "Sym010 CE 1/01/01" },
                    { symDate(1970, 2, 31), "Sym010 CE 1970/02/31" },
                    { symDate(2009, 12, 37), "Sym010 CE 2009/12/37" }
            };
        }

        @ParameterizedTest
        @MethodSource("provideDataForToString")
        @DisplayName("toString() should return the expected formatted string")
        void toString_shouldFollowExpectedFormat(Symmetry010Date date, String expected) {
            assertEquals(expected, date.toString());
        }
    }
}