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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Comprehensive tests for the Symmetry454 calendar system.
 */
@DisplayName("Tests for Symmetry454Chronology and Symmetry454Date")
public class Symmetry454ChronologyTest {

    //-----------------------------------------------------------------------
    // Test Data Providers
    //-----------------------------------------------------------------------

    static Object[][] sampleDates() {
        return new Object[][] {
                {Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1)},
                {Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27)},
                {Symmetry454Date.of(742, 3, 25), LocalDate.of(742, 4, 2)},
                {Symmetry454Date.of(1066, 10, 14), LocalDate.of(1066, 10, 14)},
                {Symmetry454Date.of(1304, 7, 21), LocalDate.of(1304, 7, 20)},
                {Symmetry454Date.of(1433, 11, 14), LocalDate.of(1433, 11, 10)},
                {Symmetry454Date.of(1452, 4, 11), LocalDate.of(1452, 4, 15)},
                {Symmetry454Date.of(1492, 10, 10), LocalDate.of(1492, 10, 12)},
                {Symmetry454Date.of(1564, 2, 20), LocalDate.of(1564, 2, 15)},
                {Symmetry454Date.of(1564, 4, 28), LocalDate.of(1564, 4, 26)},
                {Symmetry454Date.of(1643, 1, 7), LocalDate.of(1643, 1, 4)},
                {Symmetry454Date.of(1707, 4, 12), LocalDate.of(1707, 4, 15)},
                {Symmetry454Date.of(1789, 7, 16), LocalDate.of(1789, 7, 14)},
                {Symmetry454Date.of(1879, 3, 12), LocalDate.of(1879, 3, 14)},
                {Symmetry454Date.of(1941, 9, 9), LocalDate.of(1941, 9, 9)},
                {Symmetry454Date.of(1970, 1, 4), LocalDate.of(1970, 1, 1)},
                {Symmetry454Date.of(1999, 12, 27), LocalDate.of(2000, 1, 1)},
        };
    }

    static Object[][] invalidDateProvider() {
        return new Object[][] {
                {2000, 0, 1},  // Invalid month 0
                {2000, 13, 1}, // Invalid month 13
                {2000, 1, 0},  // Invalid day 0
                {2000, 1, 29}, // Invalid day 29 in a 28-day month
                {2000, 2, 36}, // Invalid day 36 in a 35-day month
                {2004, 12, 36} // Invalid day 36 in a leap year's 35-day December
        };
    }

    static Object[][] invalidLeapDayProvider() {
        return new Object[][] {{1}, {100}, {200}, {2000}}; // Years that are not leap years
    }

    //-----------------------------------------------------------------------
    // Tests
    //-----------------------------------------------------------------------

    @Nested
    @DisplayName("Conversion and Factory Methods")
    class ConversionTests {

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleDates")
        @DisplayName("should convert correctly between Symmetry454 and ISO dates")
        void testSymmetricConversion(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(isoDate, LocalDate.from(sym454Date), "Symmetry454 -> ISO");
            assertEquals(sym454Date, Symmetry454Date.from(isoDate), "ISO -> Symmetry454");
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#sampleDates")
        @DisplayName("should have epoch day values consistent with ISO date")
        void testEpochDayConversion(Symmetry454Date sym454Date, LocalDate isoDate) {
            assertEquals(isoDate.toEpochDay(), sym454Date.toEpochDay());
            assertEquals(sym454Date, Symmetry454Chronology.INSTANCE.dateEpochDay(isoDate.toEpochDay()));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#invalidDateProvider")
        @DisplayName("of(y, m, d) should throw DateTimeException for invalid date parts")
        void of_throwsException_forInvalidDateParts(int year, int month, int day) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, day));
        }

        @ParameterizedTest
        @MethodSource("org.threeten.extra.chrono.Symmetry454ChronologyTest#invalidLeapDayProvider")
        @DisplayName("of(y, 12, 29) should throw DateTimeException for non-leap years")
        void of_throwsException_forInvalidLeapDay(int year) {
            assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29));
        }
    }

    @Nested
    @DisplayName("Date Properties and Field Access")
    class DatePropertyTests {

        @Test
        @DisplayName("should report correct length of month")
        void testLengthOfMonth() {
            // Standard 28-day month
            assertEquals(28, Symmetry454Date.of(2020, 1, 1).lengthOfMonth());
            // Long 35-day month
            assertEquals(35, Symmetry454Date.of(2020, 2, 1).lengthOfMonth());
            // Leap year December (35 days)
            assertEquals(35, Symmetry454Date.of(2024, 12, 1).lengthOfMonth());
            // Common year December (28 days)
            assertEquals(28, Symmetry454Date.of(2025, 12, 1).lengthOfMonth());
        }

        @Test
        @DisplayName("should return correct range for date fields")
        void testRange() {
            Symmetry454Date date = Symmetry454Date.of(2020, 2, 23); // In a 35-day month
            assertEquals(ValueRange.of(1, 35), date.range(DAY_OF_MONTH));
            assertEquals(ValueRange.of(1, 364), date.range(DAY_OF_YEAR));
            assertEquals(ValueRange.of(1, 52), date.range(ALIGNED_WEEK_OF_YEAR));

            Symmetry454Date leapDate = Symmetry454Date.of(2024, 12, 30); // Leap year
            assertEquals(ValueRange.of(1, 371), leapDate.range(DAY_OF_YEAR));
            assertEquals(ValueRange.of(1, 53), leapDate.range(ALIGNED_WEEK_OF_YEAR));
        }

        @Test
        @DisplayName("should retrieve correct values via getLong(field)")
        void testGetLong() {
            Symmetry454Date date = Symmetry454Date.of(2014, 5, 26);
            // Day of year for 2014-05-26: Jan(28) + Feb(35) + Mar(28) + Apr(28) + 26 = 145
            long expectedDayOfYear = 28 + 35 + 28 + 28 + 26;
            // Week of year: Jan(4) + Feb(5) + Mar(4) + Apr(4) + 4th week = 21
            long expectedWeekOfYear = 4 + 5 + 4 + 4 + 4;

            assertEquals(5, date.getLong(DAY_OF_WEEK));
            assertEquals(26, date.getLong(DAY_OF_MONTH));
            assertEquals(expectedDayOfYear, date.getLong(DAY_OF_YEAR));
            assertEquals(4, date.getLong(ALIGNED_WEEK_OF_MONTH));
            assertEquals(expectedWeekOfYear, date.getLong(ALIGNED_WEEK_OF_YEAR));
            assertEquals(5, date.getLong(MONTH_OF_YEAR));
            assertEquals(2014, date.getLong(YEAR));
            assertEquals(1, date.getLong(ERA));
        }
    }

    @Nested
    @DisplayName("Date Manipulation")
    class DateManipulationTests {

        @Test
        @DisplayName("with(field, value) should adjust date correctly")
        void testWith() {
            Symmetry454Date base = Symmetry454Date.of(2014, 5, 26);
            assertEquals(Symmetry454Date.of(2014, 5, 22), base.with(DAY_OF_WEEK, 1));
            assertEquals(Symmetry454Date.of(2014, 5, 28), base.with(DAY_OF_MONTH, 28));
            assertEquals(Symmetry454Date.of(2014, 12, 28), base.with(DAY_OF_YEAR, 364));
            assertEquals(Symmetry454Date.of(2014, 4, 26), base.with(MONTH_OF_YEAR, 4));
            assertEquals(Symmetry454Date.of(2012, 5, 26), base.with(YEAR, 2012));
        }

        @Test
        @DisplayName("with(field, value) should throw exception for invalid values")
        void testWith_invalidValue() {
            Symmetry454Date base = Symmetry454Date.of(2013, 1, 1);
            assertThrows(DateTimeException.class, () -> base.with(DAY_OF_WEEK, 8));
            assertThrows(DateTimeException.class, () -> base.with(DAY_OF_MONTH, 29));
            assertThrows(DateTimeException.class, () -> base.with(DAY_OF_YEAR, 365));
        }

        @Test
        @DisplayName("plus(amount, unit) should add time correctly")
        void testPlus() {
            Symmetry454Date base = Symmetry454Date.of(2014, 5, 26);
            assertEquals(Symmetry454Date.of(2014, 5, 34), base.plus(8, DAYS));
            assertEquals(Symmetry454Date.of(2014, 6, 12), base.plus(3, WEEKS));
            assertEquals(Symmetry454Date.of(2014, 8, 26), base.plus(3, MONTHS));
            assertEquals(Symmetry454Date.of(2017, 5, 26), base.plus(3, YEARS));
            assertEquals(Symmetry454Date.of(2044, 5, 26), base.plus(3, DECADES));
        }

        @Test
        @DisplayName("minus(amount, unit) should subtract time correctly")
        void testMinus() {
            Symmetry454Date base = Symmetry454Date.of(2014, 5, 26);
            assertEquals(Symmetry454Date.of(2014, 5, 23), base.minus(3, DAYS));
            assertEquals(Symmetry454Date.of(2014, 4, 19), base.minus(5, WEEKS));
            assertEquals(Symmetry454Date.of(2013, 12, 26), base.minus(5, MONTHS));
            assertEquals(Symmetry454Date.of(2009, 5, 26), base.minus(5, YEARS));
            assertEquals(Symmetry454Date.of(1964, 5, 26), base.minus(5, DECADES));
        }

        @Test
        @DisplayName("with(TemporalAdjuster) should adjust date correctly")
        void testWithAdjuster() {
            Symmetry454Date base = Symmetry454Date.of(2012, 2, 23);
            Symmetry454Date expected = Symmetry454Date.of(2012, 2, 35);
            assertEquals(expected, base.with(TemporalAdjusters.lastDayOfMonth()));
        }
    }

    @Nested
    @DisplayName("Period and Duration Calculations")
    class PeriodUntilTests {

        @Test
        @DisplayName("until(endDate, unit) should calculate distance correctly")
        void testUntilWithUnit() {
            Symmetry454Date start = Symmetry454Date.of(2014, 5, 26);
            Symmetry454Date end = Symmetry454Date.of(2015, 6, 5);

            assertEquals(373, start.until(end, DAYS));
            assertEquals(53, start.until(end, WEEKS));
            assertEquals(12, start.until(end, MONTHS));
            assertEquals(1, start.until(end, YEARS));
            assertEquals(0, start.until(end, DECADES));
        }

        @Test
        @DisplayName("until(endDate) should return the correct ChronoPeriod")
        void testUntilAsPeriod() {
            Symmetry454Date start = Symmetry454Date.of(2014, 5, 26);
            Symmetry454Date end = Symmetry454Date.of(2015, 6, 4);
            ChronoPeriod expected = Symmetry454Chronology.INSTANCE.period(1, 0, 13);
            assertEquals(expected, start.until(end));
        }
    }

    @Nested
    @DisplayName("Chronology-specific Behavior")
    class ChronologyBehaviorTests {

        @Test
        @DisplayName("isLeapYear should correctly identify leap years")
        void testIsLeapYear() {
            assertTrue(Symmetry454Chronology.INSTANCE.isLeapYear(2004));
            assertTrue(Symmetry454Chronology.INSTANCE.isLeapYear(2009));
            assertFalse(Symmetry454Chronology.INSTANCE.isLeapYear(2000));
            assertFalse(Symmetry454Chronology.INSTANCE.isLeapYear(2014));
        }

        @Test
        @DisplayName("prolepticYear should throw exception for incompatible era types")
        void prolepticYear_throwsException_forIncompatibleEra() {
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(HijrahEra.AH, 1));
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(JapaneseEra.HEISEI, 1));
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(MinguoEra.ROC, 1));
            assertThrows(ClassCastException.class, () -> Symmetry454Chronology.INSTANCE.prolepticYear(ThaiBuddhistEra.BE, 1));
        }
    }

    @Nested
    @DisplayName("Object Method Overrides")
    class ObjectMethodTests {

        @Test
        @DisplayName("toString should return a formatted date string")
        void testToString() {
            assertEquals("Sym454 CE 1/01/01", Symmetry454Date.of(1, 1, 1).toString());
            assertEquals("Sym454 CE 1970/02/35", Symmetry454Date.of(1970, 2, 35).toString());
            assertEquals("Sym454 CE 2000/08/35", Symmetry454Date.of(2000, 8, 35).toString());
        }
    }
}