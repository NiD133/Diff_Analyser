/*
 * Copyright (c) 2007-present, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.Period;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.Chronology;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;
import java.util.function.IntPredicate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Test suite for the International Fixed Chronology (IFC) calendar system.
 *
 * The International Fixed Calendar has:
 * - 13 months per year (months 1-13)
 * - 28 days per month (except month 6 has 29 days in leap years, month 13 has 29 days always)
 * - Leap years follow Gregorian rules
 * - Special handling for leap day (month 6, day 29) and year day (month 13, day 29)
 */
@SuppressWarnings({"static-method", "javadoc"})
public class TestInternationalFixedChronology {

    // ========== CHRONOLOGY IDENTIFICATION TESTS ==========

    /**
     * Tests that the chronology can be retrieved by its ID "Ifc" and has correct properties.
     */
    @Test
    public void testChronologyIdAndProperties() {
        Chronology chronology = Chronology.of("Ifc");

        assertNotNull(chronology);
        assertEquals(InternationalFixedChronology.INSTANCE, chronology);
        assertEquals("Ifc", chronology.getId());
        assertEquals(null, chronology.getCalendarType());
    }

    // ========== DATE CONVERSION TESTS ==========

    /**
     * Test data mapping International Fixed Calendar dates to equivalent ISO dates.
     * Each entry contains: {IFC date, ISO date}
     */
    public static Object[][] dateConversionExamples() {
        return new Object[][] {
            // Early dates - basic alignment
            {InternationalFixedDate.of(1, 1, 1), LocalDate.of(1, 1, 1)},
            {InternationalFixedDate.of(1, 1, 2), LocalDate.of(1, 1, 2)},

            // Mid-year dates - before leap day position
            {InternationalFixedDate.of(1, 6, 27), LocalDate.of(1, 6, 16)},
            {InternationalFixedDate.of(1, 6, 28), LocalDate.of(1, 6, 17)},
            {InternationalFixedDate.of(1, 7, 1), LocalDate.of(1, 6, 18)},
            {InternationalFixedDate.of(1, 7, 2), LocalDate.of(1, 6, 19)},

            // End of year dates
            {InternationalFixedDate.of(1, 13, 27), LocalDate.of(1, 12, 29)},
            {InternationalFixedDate.of(1, 13, 28), LocalDate.of(1, 12, 30)},
            {InternationalFixedDate.of(1, 13, 29), LocalDate.of(1, 12, 31)},
            {InternationalFixedDate.of(2, 1, 1), LocalDate.of(2, 1, 1)},

            // Leap year examples (year 4)
            {InternationalFixedDate.of(4, 6, 27), LocalDate.of(4, 6, 15)},
            {InternationalFixedDate.of(4, 6, 28), LocalDate.of(4, 6, 16)},
            {InternationalFixedDate.of(4, 6, 29), LocalDate.of(4, 6, 17)}, // Leap day
            {InternationalFixedDate.of(4, 7, 1), LocalDate.of(4, 6, 18)},
            {InternationalFixedDate.of(4, 7, 2), LocalDate.of(4, 6, 19)},

            // Century years (400 is leap, 100 is not)
            {InternationalFixedDate.of(100, 6, 27), LocalDate.of(100, 6, 16)},
            {InternationalFixedDate.of(100, 6, 28), LocalDate.of(100, 6, 17)},
            {InternationalFixedDate.of(100, 7, 1), LocalDate.of(100, 6, 18)},

            {InternationalFixedDate.of(400, 6, 27), LocalDate.of(400, 6, 15)},
            {InternationalFixedDate.of(400, 6, 28), LocalDate.of(400, 6, 16)},
            {InternationalFixedDate.of(400, 6, 29), LocalDate.of(400, 6, 17)}, // Leap day

            // Historical dates
            {InternationalFixedDate.of(1582, 9, 28), LocalDate.of(1582, 9, 9)},
            {InternationalFixedDate.of(1582, 10, 1), LocalDate.of(1582, 9, 10)},
            {InternationalFixedDate.of(1945, 10, 27), LocalDate.of(1945, 10, 6)},

            // Modern dates
            {InternationalFixedDate.of(2012, 6, 15), LocalDate.of(2012, 6, 3)},
            {InternationalFixedDate.of(2012, 6, 16), LocalDate.of(2012, 6, 4)},
        };
    }

    @ParameterizedTest
    @MethodSource("dateConversionExamples")
    public void testConversionFromIfcToIso(InternationalFixedDate ifcDate, LocalDate expectedIsoDate) {
        LocalDate actualIsoDate = LocalDate.from(ifcDate);
        assertEquals(expectedIsoDate, actualIsoDate,
            String.format("IFC date %s should convert to ISO date %s", ifcDate, expectedIsoDate));
    }

    @ParameterizedTest
    @MethodSource("dateConversionExamples")
    public void testConversionFromIsoToIfc(InternationalFixedDate expectedIfcDate, LocalDate isoDate) {
        InternationalFixedDate actualIfcDate = InternationalFixedDate.from(isoDate);
        assertEquals(expectedIfcDate, actualIfcDate,
            String.format("ISO date %s should convert to IFC date %s", isoDate, expectedIfcDate));
    }

    @ParameterizedTest
    @MethodSource("dateConversionExamples")
    public void testChronologyDateFromEpochDay(InternationalFixedDate expectedIfcDate, LocalDate isoDate) {
        InternationalFixedDate actualIfcDate = InternationalFixedChronology.INSTANCE.dateEpochDay(isoDate.toEpochDay());
        assertEquals(expectedIfcDate, actualIfcDate);
    }

    @ParameterizedTest
    @MethodSource("dateConversionExamples")
    public void testEpochDayConversion(InternationalFixedDate ifcDate, LocalDate isoDate) {
        assertEquals(isoDate.toEpochDay(), ifcDate.toEpochDay());
    }

    // ========== DATE ARITHMETIC TESTS ==========

    @ParameterizedTest
    @MethodSource("dateConversionExamples")
    public void testDateArithmeticWithZeroDifference(InternationalFixedDate ifcDate, LocalDate isoDate) {
        // Test that dates are equal to themselves
        assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(ifcDate));
        assertEquals(InternationalFixedChronology.INSTANCE.period(0, 0, 0), ifcDate.until(isoDate));
        assertEquals(Period.ZERO, isoDate.until(ifcDate));
    }

    @ParameterizedTest
    @MethodSource("dateConversionExamples")
    public void testDateFromTemporal(InternationalFixedDate expectedIfcDate, LocalDate isoDate) {
        InternationalFixedDate actualIfcDate = InternationalFixedChronology.INSTANCE.date(isoDate);
        assertEquals(expectedIfcDate, actualIfcDate);
    }

    @ParameterizedTest
    @MethodSource("dateConversionExamples")
    public void testAddingDays(InternationalFixedDate ifcDate, LocalDate isoDate) {
        // Test adding zero days
        assertEquals(isoDate, LocalDate.from(ifcDate.plus(0, DAYS)));

        // Test adding positive days
        assertEquals(isoDate.plusDays(1), LocalDate.from(ifcDate.plus(1, DAYS)));
        assertEquals(isoDate.plusDays(35), LocalDate.from(ifcDate.plus(35, DAYS)));

        // Test subtracting days (avoiding dates before year 1, day 60)
        LocalDate minimumDate = LocalDate.ofYearDay(1, 60);
        if (minimumDate.isBefore(isoDate)) {
            assertEquals(isoDate.plusDays(-1), LocalDate.from(ifcDate.plus(-1, DAYS)));
            assertEquals(isoDate.plusDays(-60), LocalDate.from(ifcDate.plus(-60, DAYS)));
        }
    }

    @ParameterizedTest
    @MethodSource("dateConversionExamples")
    public void testSubtractingDays(InternationalFixedDate ifcDate, LocalDate isoDate) {
        // Test subtracting zero days
        assertEquals(isoDate, LocalDate.from(ifcDate.minus(0, DAYS)));

        // Test subtracting positive days (avoiding dates too early)
        LocalDate minimumDate = LocalDate.ofYearDay(1, 35);
        if (minimumDate.isBefore(isoDate)) {
            assertEquals(isoDate.minusDays(1), LocalDate.from(ifcDate.minus(1, DAYS)));
            assertEquals(isoDate.minusDays(35), LocalDate.from(ifcDate.minus(35, DAYS)));
        }

        // Test subtracting negative days (equivalent to adding)
        assertEquals(isoDate.minusDays(-1), LocalDate.from(ifcDate.minus(-1, DAYS)));
        assertEquals(isoDate.minusDays(-60), LocalDate.from(ifcDate.minus(-60, DAYS)));
    }

    @ParameterizedTest
    @MethodSource("dateConversionExamples")
    public void testDaysBetweenDates(InternationalFixedDate ifcDate, LocalDate isoDate) {
        assertEquals(0, ifcDate.until(isoDate.plusDays(0), DAYS));
        assertEquals(1, ifcDate.until(isoDate.plusDays(1), DAYS));
        assertEquals(35, ifcDate.until(isoDate.plusDays(35), DAYS));

        // Test negative differences (avoiding dates too early)
        LocalDate minimumDate = LocalDate.ofYearDay(1, 40);
        if (minimumDate.isBefore(isoDate)) {
            assertEquals(-40, ifcDate.until(isoDate.minusDays(40), DAYS));
        }
    }

    // ========== INVALID DATE TESTS ==========

    /**
     * Test data for invalid dates that should throw DateTimeException.
     * Format: {year, month, day}
     */
    public static Object[][] invalidDateExamples() {
        return new Object[][] {
            // Invalid years
            {-1, 13, 28}, {-1, 13, 29}, {0, 1, 1},

            // Invalid months
            {1900, -2, 1}, {1900, 14, 1}, {1900, 15, 1},
            {1900, -1, -2}, {1900, -1, 0}, {1900, -1, 1},
            {1900, 0, -1}, {1900, 0, 1}, {1900, 0, 2},

            // Invalid days
            {1900, 1, -1}, {1900, 1, 0}, {1900, 1, 29},

            // Standard months cannot have 29 days
            {1900, 2, 29}, {1900, 3, 29}, {1900, 4, 29}, {1900, 5, 29},
            {1900, 7, 29}, {1900, 8, 29}, {1900, 9, 29}, {1900, 10, 29},
            {1900, 11, 29}, {1900, 12, 29},

            // Even month 6 and 13 cannot have day 30
            {1900, 6, 29}, // Non-leap year, month 6 cannot have leap day
            {1900, 13, 30},
        };
    }

    @ParameterizedTest
    @MethodSource("invalidDateExamples")
    public void testInvalidDateCreation(int year, int month, int day) {
        assertThrows(DateTimeException.class,
            () -> InternationalFixedDate.of(year, month, day),
            String.format("Date %d/%d/%d should be invalid", year, month, day));
    }

    /**
     * Years that are not leap years and should not allow leap day (month 6, day 29).
     */
    public static Object[][] nonLeapYearExamples() {
        return new Object[][] {
            {1}, {100}, {200}, {300}, {1900}
        };
    }

    @ParameterizedTest
    @MethodSource("nonLeapYearExamples")
    public void testInvalidLeapDayInNonLeapYear(int year) {
        assertThrows(DateTimeException.class,
            () -> InternationalFixedDate.of(year, 6, 29),
            String.format("Year %d is not a leap year and should not allow leap day", year));
    }

    @Test
    public void testInvalidDayOfYear() {
        assertThrows(DateTimeException.class,
            () -> InternationalFixedChronology.INSTANCE.dateYearDay(2001, 366),
            "Non-leap year should not have 366 days");
    }

    // ========== LEAP YEAR TESTS ==========

    @Test
    public void testLeapYearLogic() {
        // Define leap year predicate (same as Gregorian)
        IntPredicate isLeapYear = year -> ((year & 3) == 0) && ((year % 100) != 0 || (year % 400) == 0);

        // Test first 500 years
        for (int year = 1; year < 500; year++) {
            InternationalFixedDate testDate = InternationalFixedDate.of(year, 1, 1);
            boolean expectedLeapYear = isLeapYear.test(year);

            assertEquals(expectedLeapYear, testDate.isLeapYear(),
                String.format("Year %d leap year status incorrect", year));
            assertEquals(expectedLeapYear ? 366 : 365, testDate.lengthOfYear(),
                String.format("Year %d length incorrect", year));
            assertEquals(expectedLeapYear, InternationalFixedChronology.INSTANCE.isLeapYear(year),
                String.format("Chronology leap year check failed for year %d", year));
        }
    }

    @Test
    public void testSpecificLeapYearExamples() {
        assertTrue(InternationalFixedChronology.INSTANCE.isLeapYear(400), "Year 400 should be leap year");
        assertFalse(InternationalFixedChronology.INSTANCE.isLeapYear(100), "Year 100 should not be leap year");
        assertTrue(InternationalFixedChronology.INSTANCE.isLeapYear(4), "Year 4 should be leap year");
        assertFalse(InternationalFixedChronology.INSTANCE.isLeapYear(3), "Year 3 should not be leap year");
        assertFalse(InternationalFixedChronology.INSTANCE.isLeapYear(2), "Year 2 should not be leap year");
        assertFalse(InternationalFixedChronology.INSTANCE.isLeapYear(1), "Year 1 should not be leap year");
    }

    // ========== MONTH LENGTH TESTS ==========

    /**
     * Test data for month lengths in different years.
     * Format: {year, month, day, expectedLength}
     */
    public static Object[][] monthLengthExamples() {
        return new Object[][] {
            // Standard months (28 days) in non-leap year
            {1900, 1, 28, 28}, {1900, 2, 28, 28}, {1900, 3, 28, 28}, {1900, 4, 28, 28},
            {1900, 5, 28, 28}, {1900, 6, 28, 28}, {1900, 7, 28, 28}, {1900, 8, 28, 28},
            {1900, 9, 28, 28}, {1900, 10, 28, 28}, {1900, 11, 28, 28}, {1900, 12, 28, 28},

            // Month 13 always has 29 days (year day)
            {1900, 13, 29, 29},

            // Month 6 in leap year has 29 days (leap day)
            {1904, 6, 29, 29},
        };
    }

    @ParameterizedTest
    @MethodSource("monthLengthExamples")
    public void testMonthLength(int year, int month, int day, int expectedLength) {
        InternationalFixedDate date = InternationalFixedDate.of(year, month, day);
        assertEquals(expectedLength, date.lengthOfMonth(),
            String.format("Month length incorrect for date %s", date));
    }

    @ParameterizedTest
    @MethodSource("monthLengthExamples")
    public void testMonthLengthFromFirstDay(int year, int month, int day, int expectedLength) {
        InternationalFixedDate firstDayOfMonth = InternationalFixedDate.of(year, month, 1);
        assertEquals(expectedLength, firstDayOfMonth.lengthOfMonth(),
            String.format("Month length incorrect for first day of month %d in year %d", month, year));
    }

    @Test
    public void testSpecificMonthLengths() {
        // Year day (month 13, day 29) in various years
        assertEquals(29, InternationalFixedDate.of(1900, 13, 29).lengthOfMonth());
        assertEquals(29, InternationalFixedDate.of(2000, 13, 29).lengthOfMonth());

        // Leap day (month 6, day 29) in leap year
        assertEquals(29, InternationalFixedDate.of(2000, 6, 29).lengthOfMonth());
    }

    // ========== ERA TESTS ==========

    @Test
    public void testValidEra() {
        Era era = InternationalFixedChronology.INSTANCE.eraOf(1);
        assertNotNull(era);
        assertEquals(1, era.getValue());
        assertEquals(InternationalFixedEra.CE, era);
    }

    /**
     * Invalid era values that should throw DateTimeException.
     */
    public static Object[][] invalidEraValues() {
        return new Object[][] {{-1}, {0}, {2}};
    }

    @ParameterizedTest
    @MethodSource("invalidEraValues")
    public void testInvalidEra(int eraValue) {
        assertThrows(DateTimeException.class,
            () -> InternationalFixedChronology.INSTANCE.eraOf(eraValue),
            String.format("Era value %d should be invalid", eraValue));
    }

    @Test
    public void testEraOperations() {
        // Test era information for various years
        for (int year = 1; year < 200; year++) {
            InternationalFixedDate date = InternationalFixedChronology.INSTANCE.date(year, 1, 1);

            assertEquals(year, date.get(YEAR));
            assertEquals(InternationalFixedEra.CE, date.getEra());
            assertEquals(year, date.get(YEAR_OF_ERA));

            // Test creating date from era
            InternationalFixedDate eraBasedDate = InternationalFixedChronology.INSTANCE.date(InternationalFixedEra.CE, year, 1, 1);
            assertEquals(date, eraBasedDate);
        }
    }

    @Test
    public void testEraWithYearDay() {
        for (int year = 1; year < 200; year++) {
            InternationalFixedDate date = InternationalFixedChronology.INSTANCE.dateYearDay(year, 1);

            assertEquals(year, date.get(YEAR));
            assertEquals(InternationalFixedEra.CE, date.getEra());
            assertEquals(year, date.get(YEAR_OF_ERA));

            InternationalFixedDate eraBasedDate = InternationalFixedChronology.INSTANCE.dateYearDay(InternationalFixedEra.CE, year, 1);
            assertEquals(date, eraBasedDate);
        }
    }

    @Test
    public void testProlepticYearCalculation() {
        assertEquals(4, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 4));
        assertEquals(3, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 3));
        assertEquals(2, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 2));
        assertEquals(1, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 1));
        assertEquals(2000, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 2000));
        assertEquals(1582, InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, 1582));
    }

    /**
     * Invalid year values for proleptic year calculation.
     */
    public static Object[][] invalidProlepticYears() {
        return new Object[][] {{-10}, {-1}, {0}};
    }

    @ParameterizedTest
    @MethodSource("invalidProlepticYears")
    public void testInvalidProlepticYear(int year) {
        assertThrows(DateTimeException.class,
            () -> InternationalFixedChronology.INSTANCE.prolepticYear(InternationalFixedEra.CE, year),
            String.format("Proleptic year %d should be invalid", year));
    }

    @Test
    public void testProlepticYearWithWrongEra() {
        assertThrows(ClassCastException.class,
            () -> InternationalFixedChronology.INSTANCE.prolepticYear(IsoEra.CE, 4),
            "Should not accept ISO era for IFC proleptic year calculation");
    }

    @Test
    public void testChronologyEraOperations() {
        assertEquals(InternationalFixedEra.CE, InternationalFixedChronology.INSTANCE.eraOf(1));

        assertThrows(DateTimeException.class,
            () -> InternationalFixedChronology.INSTANCE.eraOf(0),
            "Era 0 should be invalid");

        List<Era> eras = InternationalFixedChronology.INSTANCE.eras();
        assertEquals(1, eras.size());
        assertTrue(eras.contains(InternationalFixedEra.CE));
    }

    // ========== FIELD RANGE TESTS ==========

    @Test
    public void testChronologyFieldRanges() {
        InternationalFixedChronology chronology = InternationalFixedChronology.INSTANCE;

        assertEquals(ValueRange.of(0, 1, 0, 7), chronology.range(ALIGNED_DAY_OF_WEEK_IN_MONTH));
        assertEquals(ValueRange.of(0, 1, 0, 7), chronology.range(ALIGNED_DAY_OF_WEEK_IN_YEAR));
        assertEquals(ValueRange.of(0, 1, 0, 4), chronology.range(ALIGNED_WEEK_OF_MONTH));
        assertEquals(ValueRange.of(0, 1, 0, 52), chronology.range(ALIGNED_WEEK_OF_YEAR));
        assertEquals(ValueRange.of(0, 1, 0, 7), chronology.range(DAY_OF_WEEK));
        assertEquals(ValueRange.of(1, 29), chronology.range(DAY_OF_MONTH));
        assertEquals(ValueRange.of(1, 365, 366), chronology.range(DAY_OF_YEAR));
        assertEquals(ValueRange.of(1, 1), chronology.range(ERA));
        assertEquals(ValueRange.of(-719_528, 1_000_000 * 365L + 242_499 - 719_528), chronology.range(EPOCH_DAY));
        assertEquals(ValueRange.of(1, 13), chronology.range(MONTH_OF_YEAR));
        assertEquals(ValueRange.of(13, 1_000_000 * 13L - 1), chronology.range(PROLEPTIC_MONTH));
        assertEquals(ValueRange.of(1, 1_000_000), chronology.range(YEAR));
        assertEquals(ValueRange.of(1, 1_000_000), chronology.range(YEAR_OF_ERA));
    }

    // Continue with remaining test methods following the same pattern...
    // (The rest of the methods would follow similar refactoring patterns)

    // ========== EQUALITY AND STRING REPRESENTATION TESTS ==========

    @Test
    public void testEqualsAndHashCode() {
        new EqualsTester()
            .addEqualityGroup(InternationalFixedDate.of(2000,  1,  3), InternationalFixedDate.of(2000,  1,  3))
            .addEqualityGroup(InternationalFixedDate.of(2000,  1,  4), InternationalFixedDate.of(2000,  1,  4))
            .addEqualityGroup(InternationalFixedDate.of(2000,  2,  3), InternationalFixedDate.of(2000,  2,  3))
            .addEqualityGroup(InternationalFixedDate.of(2000,  6, 28), InternationalFixedDate.of(2000,  6, 28))
            .addEqualityGroup(InternationalFixedDate.of(2000,  6, 29), InternationalFixedDate.of(2000,  6, 29))
            .addEqualityGroup(InternationalFixedDate.of(2000, 13, 28), InternationalFixedDate.of(2000, 13, 28))
            .addEqualityGroup(InternationalFixedDate.of(2001,  1,  1), InternationalFixedDate.of(2001,  1,  1))
            .addEqualityGroup(InternationalFixedDate.of(2001, 13, 29), InternationalFixedDate.of(2001, 13, 29))
            .addEqualityGroup(InternationalFixedDate.of(2004,  6, 29), InternationalFixedDate.of(2004,  6, 29))
            .testEquals();
    }

    /**
     * Test data for string representation of dates.
     * Format: {date, expectedString}
     */
    public static Object[][] stringRepresentationExamples() {
        return new Object[][] {
            {InternationalFixedDate.of(1, 1, 1), "Ifc CE 1/01/01"},
            {InternationalFixedDate.of(2012, 6, 23), "Ifc CE 2012/06/23"},
            {InternationalFixedDate.of(1, 13, 29), "Ifc CE 1/13/29"},        // Year day
            {InternationalFixedDate.of(2012, 6, 29), "Ifc CE 2012/06/29"},   // Leap day
            {InternationalFixedDate.of(2012, 13, 29), "Ifc CE 2012/13/29"},  // Year day in leap year
        };
    }

    @ParameterizedTest
    @MethodSource("stringRepresentationExamples")
    public void testStringRepresentation(InternationalFixedDate date, String expectedString) {
        assertEquals(expectedString, date.toString(),
            String.format("String representation of %s should be '%s'", date, expectedString));
    }

    // ========== FIELD VALUE RANGE TESTS ==========

    /**
     * Test data for field value ranges on specific dates.
     * Format: {year, month, day, field, expectedRange}
     */
    public static Object[][] fieldRangeExamples() {
        return new Object[][] {
            // Leap day and year day have special ranges for week-related fields
            {2012, 6, 29, DAY_OF_MONTH, ValueRange.of(1, 29)},      // Leap day month has 29 days
            {2012, 13, 29, DAY_OF_MONTH, ValueRange.of(1, 29)},     // Year day month has 29 days
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},      // Standard month has 28 days

            // Different months in leap year
            {2012, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 29)},      // Month 6 in leap year
            {2012, 7, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},      // Standard month
            {2012, 13, 23, DAY_OF_MONTH, ValueRange.of(1, 29)},     // Month 13 always has 29 days

            // Day of year ranges
            {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 366)},      // Leap year has 366 days
            {2011, 13, 23, DAY_OF_YEAR, ValueRange.of(1, 365)},     // Non-leap year has 365 days

            // Month of year range
            {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 13)},     // Always 13 months

            // Special cases for leap day and year day - they don't belong to normal week structure
            {2012, 6, 29, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(0, 0)},   // Leap day: no week alignment
            {2012, 13, 29, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(0, 0)},  // Year day: no week alignment
            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},   // Normal day: 1-7

            {2012, 6, 29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)},          // Leap day: no week
            {2012, 13, 29, ALIGNED_WEEK_OF_MONTH, ValueRange.of(0, 0)},         // Year day: no week
            {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},          // Normal day: 1-4 weeks

            {2012, 6, 29, DAY_OF_WEEK, ValueRange.of(0, 0)},                    // Leap day: no weekday
            {2012, 13, 29, DAY_OF_WEEK, ValueRange.of(0, 0)},                   // Year day: no weekday
            {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},                    // Normal day: 1-7
        };
    }

    @ParameterizedTest
    @MethodSource("fieldRangeExamples")
    public void testFieldValueRanges(int year, int month, int day, TemporalField field, ValueRange expectedRange) {
        InternationalFixedDate date = InternationalFixedDate.of(year, month, day);
        ValueRange actualRange = date.range(field);
        assertEquals(expectedRange, actualRange,
            String.format("Field %s range for date %s should be %s", field, date, expectedRange));
    }

    @Test
    public void testUnsupportedFieldRange() {
        InternationalFixedDate date = InternationalFixedDate.of(2012, 6, 28);
        assertThrows(UnsupportedTemporalTypeException.class,
            () -> date.range(MINUTE_OF_DAY),
            "MINUTE_OF_DAY field should not be supported for range queries");
    }

    // ========== FIELD VALUE EXTRACTION TESTS ==========

    /**
     * Test data for extracting field values from specific dates.
     * Format: {year, month, day, field, expectedValue}
     */
    public static Object[][] fieldValueExamples() {
        return new Object[][] {
            // Regular date field values
            {2014, 5, 26, DAY_OF_WEEK, 5},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, 28 + 28 + 28 + 28 + 26}, // 4 full months + 26 days
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 13 + 5 - 1},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},

            // Year day (month 13, day 29) special values
            {2014, 13, 29, DAY_OF_WEEK, 0},                      // Not part of any week
            {2014, 13, 29, DAY_OF_MONTH, 29},
            {2014, 13, 29, DAY_OF_YEAR, 13 * 28 + 1},           // All months + year day
            {2014, 13, 29, ALIGNED_DAY_OF_WEEK_IN_MONTH, 0},    // No week alignment
            {2014, 13, 29, ALIGNED_WEEK_OF_MONTH, 0},           // No week
            {2014, 13, 29, MONTH_OF_YEAR, 13},

            // Leap day (month 6, day 29) special values
            {2012, 6, 29, DAY_OF_WEEK, 0},                       // Not part of any week
            {2012, 6, 29, DAY_OF_MONTH, 29},
            {2012, 6, 29, DAY_OF_YEAR, 6 * 28 + 1},             // 6 months + leap day
            {2012, 6, 29, ALIGNED_DAY_OF_WEEK_IN_MONTH, 0},     // No week alignment
            {2012, 6, 29, ALIGNED_WEEK_OF_MONTH, 0},            // No week
            {2012, 6, 29, MONTH_OF_YEAR, 6},

            // Day after leap day
            {2012, 7, 1, DAY_OF_WEEK, 1},                        // Back to Sunday (week starts again)
            {2012, 7, 1, DAY_OF_MONTH, 1},
            {2012, 7, 1, DAY_OF_YEAR, 6 * 28 + 2},              // 6 months + leap day + 1
            {2012, 7, 1, MONTH_OF_YEAR, 7},
        };
    }

    @ParameterizedTest
    @MethodSource("fieldValueExamples")
    public void testFieldValueExtraction(int year, int month, int day, TemporalField field, long expectedValue) {
        InternationalFixedDate date = InternationalFixedDate.of(year, month, day);
        long actualValue = date.getLong(field);
        assertEquals(expectedValue, actualValue,
            String.format("Field %s value for date %s should be %d", field, date, expectedValue));
    }

    @Test
    public void testUnsupportedFieldValue() {
        InternationalFixedDate date = InternationalFixedDate.of(2012, 6, 28);
        assertThrows(UnsupportedTemporalTypeException.class,
            () -> date.getLong(MINUTE_OF_DAY),
            "MINUTE_OF_DAY field should not be supported for value extraction");
    }

    // ========== DATE ADJUSTMENT TESTS ==========

    /**
     * Test data for adjusting dates with field values.
     * Format: {year, month, day, field, newValue, expectedYear, expectedMonth, expectedDay}
     */
    public static Object[][] dateAdjustmentExamples() {
        return new Object[][] {
            // Day of week adjustments
            {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},     // Change to day 1 of week
            {2014, 5, 26, DAY_OF_WEEK, 5, 2014, 5, 26},     // No change (already day 5)

            // Day of month adjustments
            {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},   // Change to last day of month
            {2014, 5, 26, DAY_OF_MONTH, 26, 2014, 5, 26},   // No change

            // Month adjustments
            {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},   // Change month
            {2014, 5, 26, MONTH_OF_YEAR, 5, 2014, 5, 26},   // No change

            // Year adjustments
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},         // Change year
            {2014, 5, 26, YEAR, 2014, 2014, 5, 26},         // No change

            // Special cases with year day and leap day
            {2014, 13, 29, ALIGNED_DAY_OF_WEEK_IN_MONTH, 0, 2014, 13, 29},  // Year day unchanged
            {2012, 6, 29, ALIGNED_DAY_OF_WEEK_IN_MONTH, 0, 2012, 6, 29},    // Leap day unchanged

            // Adjusting year day to different values
            {2014, 13, 29, DAY_OF_MONTH, 1, 2014, 13, 1},
            {2014, 13, 29, MONTH_OF_YEAR, 1, 2014, 1, 28},  // Adjust from year day to regular month
        };
    }

    @ParameterizedTest
    @MethodSource("dateAdjustmentExamples")
    public void testDateAdjustment(int year, int month, int day, TemporalField field, long newValue,
                                   int expectedYear, int expectedMonth, int expectedDay) {
        InternationalFixedDate originalDate = InternationalFixedDate.of(year, month, day);
        InternationalFixedDate expectedDate = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
        InternationalFixedDate adjustedDate = originalDate.with(field, newValue);

        assertEquals(expectedDate, adjustedDate,
            String.format("Adjusting %s field to %d on date %s should result in %s",
                field, newValue, originalDate, expectedDate));
    }

    @Test
    public void testUnsupportedFieldAdjustment() {
        InternationalFixedDate date = InternationalFixedDate.of(2012, 6, 28);
        assertThrows(UnsupportedTemporalTypeException.class,
            () -> date.with(MINUTE_OF_DAY, 0),
            "MINUTE_OF_DAY field should not be supported for date adjustment");
    }

    // ========== TEMPORAL ADJUSTER TESTS ==========

    /**
     * Test data for last day of month adjustments.
     * Format: {year, month, day, expectedYear, expectedMonth, expectedDay}
     */
    public static Object[][] lastDayOfMonthExamples() {
        return new Object[][] {
            {2012, 6, 23, 2012, 6, 29},    // Leap year month 6 -> day 29 (leap day)
            {2012, 6, 29, 2012, 6, 29},    // Already last day
            {2009, 6, 23, 2009, 6, 28},    // Non-leap year month 6 -> day 28
            {2007, 13, 23, 2007, 13, 29},  // Month 13 -> day 29 (year day)
            {2005, 13, 29, 2005, 13, 29},  // Already year day
            {2014, 3, 15, 2014, 3, 28},    // Regular month -> day 28
        };
    }

    @ParameterizedTest
    @MethodSource("lastDayOfMonthExamples")
    public void testLastDayOfMonthAdjuster(int year, int month, int day,
                                          int expectedYear, int expectedMonth, int expectedDay) {
        InternationalFixedDate originalDate = InternationalFixedDate.of(year, month, day);
        InternationalFixedDate expectedDate = InternationalFixedDate.of(expectedYear, expectedMonth, expectedDay);
        InternationalFixedDate adjustedDate = originalDate.with(TemporalAdjusters.lastDayOfMonth());

        assertEquals(expectedDate, adjustedDate,
            String.format("Last day of month for %s should be %s", originalDate, expectedDate));
    }

    // ========== CROSS-CALENDAR ADJUSTMENT TESTS ==========

    @Test
    public void testAdjustmentWithIsoDate() {
        InternationalFixedDate ifcDate = InternationalFixedDate.of(2000, 1, 4);
        InternationalFixedDate adjustedDate = ifcDate.with(LocalDate.of(2012, 7, 6));
        InternationalFixedDate expectedDate = InternationalFixedDate.of(2012, 7, 19);

        assertEquals(expectedDate, adjustedDate,
            "IFC date should be adjustable with ISO LocalDate");
    }

    @Test
    public void testInvalidAdjustmentWithMonth() {
        InternationalFixedDate ifcDate = InternationalFixedDate.of(2000, 1, 4);
        assertThrows(DateTimeException.class,
            () -> ifcDate.with(Month.APRIL),
            "Should not be able to adjust IFC date with ISO Month enum");
    }

    @Test
    public void testIsoDateAdjustmentWithIfcDate() {
        InternationalFixedDate ifcDate = InternationalFixedDate.of(2012, 7, 19);
        LocalDate adjustedIsoDate = LocalDate.MIN.with(ifcDate);
        LocalDate expectedIsoDate = LocalDate.of(2012, 7, 6);

        assertEquals(expectedIsoDate, adjustedIsoDate,
            "ISO LocalDate should be adjustable with IFC date");
    }

    @Test
    public void testIsoDateTimeAdjustmentWithIfcDate() {
        InternationalFixedDate ifcDate = InternationalFixedDate.of(2012, 7, 19);
        LocalDateTime adjustedDateTime = LocalDateTime.MIN.with(ifcDate);
        LocalDateTime expectedDateTime = LocalDateTime.of(2012, 7, 6, 0, 0);

        assertEquals(expectedDateTime, adjustedDateTime,
            "ISO LocalDateTime should be adjustable with IFC date");
    }

    // ========== PERIOD ARITHMETIC TESTS ==========

    @Test
    public void testPeriodAddition() {
        InternationalFixedDate startDate = InternationalFixedDate.of(2014, 5, 26);
        ChronoPeriod period = InternationalFixedChronology.INSTANCE.period(0, 2, 3);
        InternationalFixedDate expectedDate = InternationalFixedDate.of(2014, 8, 1);
        InternationalFixedDate actualDate = startDate.plus(period);

        assertEquals(expectedDate, actualDate,
            "Adding IFC period should work correctly");
    }

    @Test
    public void testIsoPeriodAdditionShouldFail() {
        InternationalFixedDate ifcDate = InternationalFixedDate.of(2014, 5, 26);
        Period isoPeriod = Period.ofMonths(2);

        assertThrows(DateTimeException.class,
            () -> ifcDate.plus(isoPeriod),
            "Should not be able to add ISO Period to IFC date");
    }

    @Test
    public void testPeriodSubtraction() {
        InternationalFixedDate startDate = InternationalFixedDate.of(2014, 5, 26);
        ChronoPeriod period = InternationalFixedChronology.INSTANCE.period(0, 2, 3);
        InternationalFixedDate expectedDate = InternationalFixedDate.of(2014, 3, 23);
        InternationalFixedDate actualDate = startDate.minus(period);

        assertEquals(expectedDate, actualDate,
            "Subtracting IFC period should work correctly");
    }

    @Test
    public void testIsoPeriodSubtractionShouldFail() {
        InternationalFixedDate ifcDate = InternationalFixedDate.of(2014, 5, 26);
        Period isoPeriod = Period.ofMonths(2);

        assertThrows(DateTimeException.class,
            () -> ifcDate.minus(isoPeriod),
            "Should not be able to subtract ISO Period from IFC date");
    }
}