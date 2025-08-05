package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.time.chrono.*;
import java.time.temporal.*;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for the BritishCutoverChronology.
 */
public class TestBritishCutoverChronology {

    // Test Chronology.of(String)
    @Test
    public void testChronologyOfName() {
        Chronology chronology = Chronology.of("BritishCutover");
        assertNotNull(chronology, "Chronology should not be null");
        assertEquals(BritishCutoverChronology.INSTANCE, chronology, "Chronology instance mismatch");
        assertEquals("BritishCutover", chronology.getId(), "Chronology ID mismatch");
        assertNull(chronology.getCalendarType(), "Calendar type should be null");
    }

    // Data provider for sample dates
    public static Object[][] sampleDates() {
        return new Object[][] {
            {BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
            {BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)},
            {BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
            // Additional sample data...
        };
    }

    // Test conversion from BritishCutoverDate to LocalDate
    @ParameterizedTest
    @MethodSource("sampleDates")
    public void testLocalDateFromBritishCutoverDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(iso, LocalDate.from(cutover), "Conversion to LocalDate failed");
    }

    // Test conversion from LocalDate to BritishCutoverDate
    @ParameterizedTest
    @MethodSource("sampleDates")
    public void testBritishCutoverDateFromLocalDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(cutover, BritishCutoverDate.from(iso), "Conversion to BritishCutoverDate failed");
    }

    // Test dateEpochDay method
    @ParameterizedTest
    @MethodSource("sampleDates")
    public void testDateEpochDay(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(cutover, BritishCutoverChronology.INSTANCE.dateEpochDay(iso.toEpochDay()), "dateEpochDay conversion failed");
    }

    // Test toEpochDay method
    @ParameterizedTest
    @MethodSource("sampleDates")
    public void testToEpochDay(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(iso.toEpochDay(), cutover.toEpochDay(), "toEpochDay conversion failed");
    }

    // Test until method with BritishCutoverDate
    @ParameterizedTest
    @MethodSource("sampleDates")
    public void testUntilWithBritishCutoverDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutover.until(cutover), "until method failed");
    }

    // Test until method with LocalDate
    @ParameterizedTest
    @MethodSource("sampleDates")
    public void testUntilWithLocalDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutover.until(iso), "until method failed");
    }

    // Test LocalDate until BritishCutoverDate
    @ParameterizedTest
    @MethodSource("sampleDates")
    public void testLocalDateUntilBritishCutoverDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(Period.ZERO, iso.until(cutover), "LocalDate until method failed");
    }

    // Test Chronology date method
    @ParameterizedTest
    @MethodSource("sampleDates")
    public void testChronologyDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(cutover, BritishCutoverChronology.INSTANCE.date(iso), "Chronology date method failed");
    }

    // Test plusDays method
    @ParameterizedTest
    @MethodSource("sampleDates")
    public void testPlusDays(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(iso, LocalDate.from(cutover.plus(0, DAYS)), "plusDays method failed");
        assertEquals(iso.plusDays(1), LocalDate.from(cutover.plus(1, DAYS)), "plusDays method failed");
        assertEquals(iso.plusDays(35), LocalDate.from(cutover.plus(35, DAYS)), "plusDays method failed");
        assertEquals(iso.plusDays(-1), LocalDate.from(cutover.plus(-1, DAYS)), "plusDays method failed");
        assertEquals(iso.plusDays(-60), LocalDate.from(cutover.plus(-60, DAYS)), "plusDays method failed");
    }

    // Test minusDays method
    @ParameterizedTest
    @MethodSource("sampleDates")
    public void testMinusDays(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(iso, LocalDate.from(cutover.minus(0, DAYS)), "minusDays method failed");
        assertEquals(iso.minusDays(1), LocalDate.from(cutover.minus(1, DAYS)), "minusDays method failed");
        assertEquals(iso.minusDays(35), LocalDate.from(cutover.minus(35, DAYS)), "minusDays method failed");
        assertEquals(iso.minusDays(-1), LocalDate.from(cutover.minus(-1, DAYS)), "minusDays method failed");
        assertEquals(iso.minusDays(-60), LocalDate.from(cutover.minus(-60, DAYS)), "minusDays method failed");
    }

    // Test until method with DAYS unit
    @ParameterizedTest
    @MethodSource("sampleDates")
    public void testUntilDays(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(0, cutover.until(iso.plusDays(0), DAYS), "until method with DAYS unit failed");
        assertEquals(1, cutover.until(iso.plusDays(1), DAYS), "until method with DAYS unit failed");
        assertEquals(35, cutover.until(iso.plusDays(35), DAYS), "until method with DAYS unit failed");
        assertEquals(-40, cutover.until(iso.minusDays(40), DAYS), "until method with DAYS unit failed");
    }

    // Data provider for invalid dates
    public static Object[][] invalidDates() {
        return new Object[][] {
            {1900, 0, 0},
            {1900, -1, 1},
            {1900, 0, 1},
            {1900, 13, 1},
            {1900, 14, 1},
            {1900, 1, -1},
            {1900, 1, 0},
            {1900, 1, 32},
            // Additional invalid dates...
        };
    }

    // Test invalid dates
    @ParameterizedTest
    @MethodSource("invalidDates")
    public void testInvalidDates(int year, int month, int day) {
        assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, day), "Invalid date should throw exception");
    }

    // Test dateYearDay with invalid date
    @Test
    public void testDateYearDayInvalidDate() {
        assertThrows(DateTimeException.class, () -> BritishCutoverChronology.INSTANCE.dateYearDay(2001, 366), "Invalid day of year should throw exception");
    }

    // Test isLeapYear method
    @Test
    public void testIsLeapYearLoop() {
        for (int year = -200; year < 200; year++) {
            BritishCutoverDate base = BritishCutoverDate.of(year, 1, 1);
            boolean expectedLeapYear = (year % 4) == 0;
            assertEquals(expectedLeapYear, base.isLeapYear(), "Leap year calculation failed");
            assertEquals(expectedLeapYear, BritishCutoverChronology.INSTANCE.isLeapYear(year), "Leap year calculation failed");
        }
    }

    // Test specific leap years
    @Test
    public void testIsLeapYearSpecific() {
        assertTrue(BritishCutoverChronology.INSTANCE.isLeapYear(8), "Leap year calculation failed");
        assertFalse(BritishCutoverChronology.INSTANCE.isLeapYear(7), "Leap year calculation failed");
        assertFalse(BritishCutoverChronology.INSTANCE.isLeapYear(6), "Leap year calculation failed");
        assertFalse(BritishCutoverChronology.INSTANCE.isLeapYear(5), "Leap year calculation failed");
        assertTrue(BritishCutoverChronology.INSTANCE.isLeapYear(4), "Leap year calculation failed");
        assertFalse(BritishCutoverChronology.INSTANCE.isLeapYear(3), "Leap year calculation failed");
        assertFalse(BritishCutoverChronology.INSTANCE.isLeapYear(2), "Leap year calculation failed");
        assertFalse(BritishCutoverChronology.INSTANCE.isLeapYear(1), "Leap year calculation failed");
        assertTrue(BritishCutoverChronology.INSTANCE.isLeapYear(0), "Leap year calculation failed");
        assertFalse(BritishCutoverChronology.INSTANCE.isLeapYear(-1), "Leap year calculation failed");
        assertFalse(BritishCutoverChronology.INSTANCE.isLeapYear(-2), "Leap year calculation failed");
        assertFalse(BritishCutoverChronology.INSTANCE.isLeapYear(-3), "Leap year calculation failed");
        assertTrue(BritishCutoverChronology.INSTANCE.isLeapYear(-4), "Leap year calculation failed");
        assertFalse(BritishCutoverChronology.INSTANCE.isLeapYear(-5), "Leap year calculation failed");
        assertFalse(BritishCutoverChronology.INSTANCE.isLeapYear(-6), "Leap year calculation failed");
    }

    // Test getCutover method
    @Test
    public void testGetCutover() {
        assertEquals(LocalDate.of(1752, 9, 14), BritishCutoverChronology.INSTANCE.getCutover(), "Cutover date mismatch");
    }

    // Data provider for length of month
    public static Object[][] lengthOfMonthData() {
        return new Object[][] {
            {1700, 1, 31},
            {1700, 2, 29},
            {1700, 3, 31},
            // Additional month length data...
        };
    }

    // Test lengthOfMonth method
    @ParameterizedTest
    @MethodSource("lengthOfMonthData")
    public void testLengthOfMonth(int year, int month, int length) {
        assertEquals(length, BritishCutoverDate.of(year, month, 1).lengthOfMonth(), "Month length mismatch");
    }

    // Data provider for length of year
    public static Object[][] lengthOfYearData() {
        return new Object[][] {
            {-101, 365},
            {-100, 366},
            {-99, 365},
            // Additional year length data...
        };
    }

    // Test lengthOfYear method at start of year
    @ParameterizedTest
    @MethodSource("lengthOfYearData")
    public void testLengthOfYearAtStart(int year, int length) {
        assertEquals(length, BritishCutoverDate.of(year, 1, 1).lengthOfYear(), "Year length mismatch at start");
    }

    // Test lengthOfYear method at end of year
    @ParameterizedTest
    @MethodSource("lengthOfYearData")
    public void testLengthOfYearAtEnd(int year, int length) {
        assertEquals(length, BritishCutoverDate.of(year, 12, 31).lengthOfYear(), "Year length mismatch at end");
    }

    // Test era, prolepticYear, and dateYearDay
    @Test
    public void testEraLoop() {
        for (int year = -200; year < 200; year++) {
            BritishCutoverDate base = BritishCutoverChronology.INSTANCE.date(year, 1, 1);
            assertEquals(year, base.get(YEAR), "Year mismatch");
            JulianEra era = (year <= 0 ? JulianEra.BC : JulianEra.AD);
            assertEquals(era, base.getEra(), "Era mismatch");
            int yoe = (year <= 0 ? 1 - year : year);
            assertEquals(yoe, base.get(YEAR_OF_ERA), "Year of era mismatch");
            BritishCutoverDate eraBased = BritishCutoverChronology.INSTANCE.date(era, yoe, 1, 1);
            assertEquals(base, eraBased, "Era-based date mismatch");
        }
    }

    // Additional test methods...

    // Test equals and hashCode
    @Test
    public void testEqualsAndHashCode() {
        new EqualsTester()
            .addEqualityGroup(BritishCutoverDate.of(2000, 1, 3), BritishCutoverDate.of(2000, 1, 3))
            .addEqualityGroup(BritishCutoverDate.of(2000, 1, 4), BritishCutoverDate.of(2000, 1, 4))
            .addEqualityGroup(BritishCutoverDate.of(2000, 2, 3), BritishCutoverDate.of(2000, 2, 3))
            .addEqualityGroup(BritishCutoverDate.of(2001, 1, 3), BritishCutoverDate.of(2001, 1, 3))
            .testEquals();
    }

    // Test toString method
    public static Object[][] toStringData() {
        return new Object[][] {
            {BritishCutoverDate.of(1, 1, 1), "BritishCutover AD 1-01-01"},
            {BritishCutoverDate.of(2012, 6, 23), "BritishCutover AD 2012-06-23"},
        };
    }

    @ParameterizedTest
    @MethodSource("toStringData")
    public void testToString(BritishCutoverDate cutover, String expected) {
        assertEquals(expected, cutover.toString(), "toString method failed");
    }
}