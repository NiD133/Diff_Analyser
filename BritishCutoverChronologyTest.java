package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.*;
import java.time.chrono.*;
import java.time.temporal.*;
import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for the BritishCutoverChronology class.
 */
public class TestBritishCutoverChronology {

    // Test Chronology.of(String)
    @Test
    public void testChronologyOfName() {
        Chronology chrono = Chronology.of("BritishCutover");
        assertNotNull(chrono, "Chronology should not be null");
        assertEquals(BritishCutoverChronology.INSTANCE, chrono, "Chronology instance mismatch");
        assertEquals("BritishCutover", chrono.getId(), "Chronology ID mismatch");
        assertNull(chrono.getCalendarType(), "Calendar type should be null");
    }

    // Test creation and conversion to LocalDate
    public static Object[][] dataSamples() {
        return new Object[][] {
            {BritishCutoverDate.of(1, 1, 1), LocalDate.of(0, 12, 30)},
            {BritishCutoverDate.of(1, 1, 2), LocalDate.of(0, 12, 31)},
            {BritishCutoverDate.of(1, 1, 3), LocalDate.of(1, 1, 1)},
            // Additional test cases...
        };
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testLocalDateFromBritishCutoverDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(iso, LocalDate.from(cutover), "Conversion to LocalDate failed");
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testBritishCutoverDateFromLocalDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(cutover, BritishCutoverDate.from(iso), "Conversion from LocalDate failed");
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testBritishCutoverDateChronologyDateEpochDay(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(cutover, BritishCutoverChronology.INSTANCE.dateEpochDay(iso.toEpochDay()), "Date epoch day mismatch");
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testBritishCutoverDateToEpochDay(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(iso.toEpochDay(), cutover.toEpochDay(), "Epoch day mismatch");
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testBritishCutoverDateUntilBritishCutoverDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutover.until(cutover), "Period mismatch");
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testBritishCutoverDateUntilLocalDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(BritishCutoverChronology.INSTANCE.period(0, 0, 0), cutover.until(iso), "Period mismatch");
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testLocalDateUntilBritishCutoverDate(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(Period.ZERO, iso.until(cutover), "Period mismatch");
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testChronologyDateTemporal(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(cutover, BritishCutoverChronology.INSTANCE.date(iso), "Date mismatch");
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testPlusDays(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(iso, LocalDate.from(cutover.plus(0, DAYS)), "Plus days mismatch");
        assertEquals(iso.plusDays(1), LocalDate.from(cutover.plus(1, DAYS)), "Plus days mismatch");
        assertEquals(iso.plusDays(35), LocalDate.from(cutover.plus(35, DAYS)), "Plus days mismatch");
        assertEquals(iso.plusDays(-1), LocalDate.from(cutover.plus(-1, DAYS)), "Plus days mismatch");
        assertEquals(iso.plusDays(-60), LocalDate.from(cutover.plus(-60, DAYS)), "Plus days mismatch");
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testMinusDays(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(iso, LocalDate.from(cutover.minus(0, DAYS)), "Minus days mismatch");
        assertEquals(iso.minusDays(1), LocalDate.from(cutover.minus(1, DAYS)), "Minus days mismatch");
        assertEquals(iso.minusDays(35), LocalDate.from(cutover.minus(35, DAYS)), "Minus days mismatch");
        assertEquals(iso.minusDays(-1), LocalDate.from(cutover.minus(-1, DAYS)), "Minus days mismatch");
        assertEquals(iso.minusDays(-60), LocalDate.from(cutover.minus(-60, DAYS)), "Minus days mismatch");
    }

    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testUntilDays(BritishCutoverDate cutover, LocalDate iso) {
        assertEquals(0, cutover.until(iso.plusDays(0), DAYS), "Until days mismatch");
        assertEquals(1, cutover.until(iso.plusDays(1), DAYS), "Until days mismatch");
        assertEquals(35, cutover.until(iso.plusDays(35), DAYS), "Until days mismatch");
        assertEquals(-40, cutover.until(iso.minusDays(40), DAYS), "Until days mismatch");
    }

    // Test invalid dates
    public static Object[][] dataBadDates() {
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

    @ParameterizedTest
    @MethodSource("dataBadDates")
    public void testBadDates(int year, int month, int dom) {
        assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(year, month, dom), "Expected DateTimeException");
    }

    @Test
    public void testChronologyDateYearDayBadDate() {
        assertThrows(DateTimeException.class, () -> BritishCutoverChronology.INSTANCE.dateYearDay(2001, 366), "Expected DateTimeException");
    }

    // Test isLeapYear()
    @Test
    public void testChronologyIsLeapYearLoop() {
        for (int year = -200; year < 200; year++) {
            BritishCutoverDate base = BritishCutoverDate.of(year, 1, 1);
            assertEquals((year % 4) == 0, base.isLeapYear(), "Leap year mismatch");
            assertEquals((year % 4) == 0, BritishCutoverChronology.INSTANCE.isLeapYear(year), "Leap year mismatch");
        }
    }

    @Test
    public void testChronologyIsLeapYearSpecific() {
        assertEquals(true, BritishCutoverChronology.INSTANCE.isLeapYear(8), "Leap year mismatch");
        assertEquals(false, BritishCutoverChronology.INSTANCE.isLeapYear(7), "Leap year mismatch");
        assertEquals(false, BritishCutoverChronology.INSTANCE.isLeapYear(6), "Leap year mismatch");
        assertEquals(false, BritishCutoverChronology.INSTANCE.isLeapYear(5), "Leap year mismatch");
        assertEquals(true, BritishCutoverChronology.INSTANCE.isLeapYear(4), "Leap year mismatch");
        assertEquals(false, BritishCutoverChronology.INSTANCE.isLeapYear(3), "Leap year mismatch");
        assertEquals(false, BritishCutoverChronology.INSTANCE.isLeapYear(2), "Leap year mismatch");
        assertEquals(false, BritishCutoverChronology.INSTANCE.isLeapYear(1), "Leap year mismatch");
        assertEquals(true, BritishCutoverChronology.INSTANCE.isLeapYear(0), "Leap year mismatch");
        assertEquals(false, BritishCutoverChronology.INSTANCE.isLeapYear(-1), "Leap year mismatch");
        assertEquals(false, BritishCutoverChronology.INSTANCE.isLeapYear(-2), "Leap year mismatch");
        assertEquals(false, BritishCutoverChronology.INSTANCE.isLeapYear(-3), "Leap year mismatch");
        assertEquals(true, BritishCutoverChronology.INSTANCE.isLeapYear(-4), "Leap year mismatch");
        assertEquals(false, BritishCutoverChronology.INSTANCE.isLeapYear(-5), "Leap year mismatch");
        assertEquals(false, BritishCutoverChronology.INSTANCE.isLeapYear(-6), "Leap year mismatch");
    }

    // Test getCutover()
    @Test
    public void testChronologyGetCutover() {
        assertEquals(LocalDate.of(1752, 9, 14), BritishCutoverChronology.INSTANCE.getCutover(), "Cutover date mismatch");
    }

    // Test lengthOfMonth()
    public static Object[][] dataLengthOfMonth() {
        return new Object[][] {
            {1700, 1, 31},
            {1700, 2, 29},
            {1700, 3, 31},
            {1700, 4, 30},
            {1700, 5, 31},
            {1700, 6, 30},
            {1700, 7, 31},
            {1700, 8, 31},
            {1700, 9, 30},
            {1700, 10, 31},
            {1700, 11, 30},
            {1700, 12, 31},
            // Additional test cases...
        };
    }

    @ParameterizedTest
    @MethodSource("dataLengthOfMonth")
    public void testLengthOfMonth(int year, int month, int length) {
        assertEquals(length, BritishCutoverDate.of(year, month, 1).lengthOfMonth(), "Length of month mismatch");
    }

    // Test lengthOfYear()
    public static Object[][] dataLengthOfYear() {
        return new Object[][] {
            {-101, 365},
            {-100, 366},
            {-99, 365},
            {-1, 365},
            {0, 366},
            {100, 366},
            {1600, 366},
            {1700, 366},
            {1751, 365},
            {1748, 366},
            {1749, 365},
            {1750, 365},
            {1751, 365},
            {1752, 355},
            {1753, 365},
            {1500, 366},
            {1600, 366},
            {1700, 366},
            {1800, 365},
            {1900, 365},
            {1901, 365},
            {1902, 365},
            {1903, 365},
            {1904, 366},
            {2000, 366},
            {2100, 365},
            // Additional test cases...
        };
    }

    @ParameterizedTest
    @MethodSource("dataLengthOfYear")
    public void testLengthOfYearAtStart(int year, int length) {
        assertEquals(length, BritishCutoverDate.of(year, 1, 1).lengthOfYear(), "Length of year mismatch");
    }

    @ParameterizedTest
    @MethodSource("dataLengthOfYear")
    public void testLengthOfYearAtEnd(int year, int length) {
        assertEquals(length, BritishCutoverDate.of(year, 12, 31).lengthOfYear(), "Length of year mismatch");
    }

    // Test era, prolepticYear and dateYearDay
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

    @Test
    public void testEraYearDayLoop() {
        for (int year = -200; year < 200; year++) {
            BritishCutoverDate base = BritishCutoverChronology.INSTANCE.dateYearDay(year, 1);
            assertEquals(year, base.get(YEAR), "Year mismatch");
            JulianEra era = (year <= 0 ? JulianEra.BC : JulianEra.AD);
            assertEquals(era, base.getEra(), "Era mismatch");
            int yoe = (year <= 0 ? 1 - year : year);
            assertEquals(yoe, base.get(YEAR_OF_ERA), "Year of era mismatch");
            BritishCutoverDate eraBased = BritishCutoverChronology.INSTANCE.dateYearDay(era, yoe, 1);
            assertEquals(base, eraBased, "Era-based date mismatch");
        }
    }

    @Test
    public void testEraYearDay() {
        assertEquals(BritishCutoverDate.of(1752, 1, 1), BritishCutoverChronology.INSTANCE.dateYearDay(1752, 1), "Date mismatch");
        assertEquals(BritishCutoverDate.of(1752, 8, 31), BritishCutoverChronology.INSTANCE.dateYearDay(1752, 244), "Date mismatch");
        assertEquals(BritishCutoverDate.of(1752, 9, 2), BritishCutoverChronology.INSTANCE.dateYearDay(1752, 246), "Date mismatch");
        assertEquals(BritishCutoverDate.of(1752, 9, 14), BritishCutoverChronology.INSTANCE.dateYearDay(1752, 247), "Date mismatch");
        assertEquals(BritishCutoverDate.of(1752, 9, 24), BritishCutoverChronology.INSTANCE.dateYearDay(1752, 257), "Date mismatch");
        assertEquals(BritishCutoverDate.of(1752, 9, 25), BritishCutoverChronology.INSTANCE.dateYearDay(1752, 258), "Date mismatch");
        assertEquals(BritishCutoverDate.of(1752, 12, 31), BritishCutoverChronology.INSTANCE.dateYearDay(1752, 355), "Date mismatch");
        assertEquals(BritishCutoverDate.of(2014, 1, 1), BritishCutoverChronology.INSTANCE.dateYearDay(2014, 1), "Date mismatch");
    }

    @Test
    public void testProlepticYearSpecific() {
        assertEquals(4, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.AD, 4), "Proleptic year mismatch");
        assertEquals(3, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.AD, 3), "Proleptic year mismatch");
        assertEquals(2, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.AD, 2), "Proleptic year mismatch");
        assertEquals(1, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.AD, 1), "Proleptic year mismatch");
        assertEquals(0, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.BC, 1), "Proleptic year mismatch");
        assertEquals(-1, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.BC, 2), "Proleptic year mismatch");
        assertEquals(-2, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.BC, 3), "Proleptic year mismatch");
        assertEquals(-3, BritishCutoverChronology.INSTANCE.prolepticYear(JulianEra.BC, 4), "Proleptic year mismatch");
    }

    @Test
    public void testProlepticYearBadEra() {
        assertThrows(ClassCastException.class, () -> BritishCutoverChronology.INSTANCE.prolepticYear(IsoEra.CE, 4), "Expected ClassCastException");
    }

    @Test
    public void testChronologyEraOf() {
        assertEquals(JulianEra.AD, BritishCutoverChronology.INSTANCE.eraOf(1), "Era mismatch");
        assertEquals(JulianEra.BC, BritishCutoverChronology.INSTANCE.eraOf(0), "Era mismatch");
    }

    @Test
    public void testChronologyEraOfInvalid() {
        assertThrows(DateTimeException.class, () -> BritishCutoverChronology.INSTANCE.eraOf(2), "Expected DateTimeException");
    }

    @Test
    public void testChronologyEras() {
        List<Era> eras = BritishCutoverChronology.INSTANCE.eras();
        assertEquals(2, eras.size(), "Eras size mismatch");
        assertTrue(eras.contains(JulianEra.BC), "Eras should contain BC");
        assertTrue(eras.contains(JulianEra.AD), "Eras should contain AD");
    }

    // Test Chronology.range
    @Test
    public void testChronologyRange() {
        assertEquals(ValueRange.of(1, 7), BritishCutoverChronology.INSTANCE.range(DAY_OF_WEEK), "Range mismatch");
        assertEquals(ValueRange.of(1, 28, 31), BritishCutoverChronology.INSTANCE.range(DAY_OF_MONTH), "Range mismatch");
        assertEquals(ValueRange.of(1, 355, 366), BritishCutoverChronology.INSTANCE.range(DAY_OF_YEAR), "Range mismatch");
        assertEquals(ValueRange.of(1, 12), BritishCutoverChronology.INSTANCE.range(MONTH_OF_YEAR), "Range mismatch");
        assertEquals(ValueRange.of(1, 3, 5), BritishCutoverChronology.INSTANCE.range(ALIGNED_WEEK_OF_MONTH), "Range mismatch");
        assertEquals(ValueRange.of(1, 51, 53), BritishCutoverChronology.INSTANCE.range(ALIGNED_WEEK_OF_YEAR), "Range mismatch");
    }

    // Test BritishCutoverDate.range
    public static Object[][] dataRanges() {
        return new Object[][] {
            {1700, 1, 23, DAY_OF_MONTH, 1, 31},
            {1700, 2, 23, DAY_OF_MONTH, 1, 29},
            {1700, 3, 23, DAY_OF_MONTH, 1, 31},
            {1700, 4, 23, DAY_OF_MONTH, 1, 30},
            {1700, 5, 23, DAY_OF_MONTH, 1, 31},
            {1700, 6, 23, DAY_OF_MONTH, 1, 30},
            {1700, 7, 23, DAY_OF_MONTH, 1, 31},
            {1700, 8, 23, DAY_OF_MONTH, 1, 31},
            {1700, 9, 23, DAY_OF_MONTH, 1, 30},
            {1700, 10, 23, DAY_OF_MONTH, 1, 31},
            {1700, 11, 23, DAY_OF_MONTH, 1, 30},
            {1700, 12, 23, DAY_OF_MONTH, 1, 31},
            // Additional test cases...
        };
    }

    @ParameterizedTest
    @MethodSource("dataRanges")
    public void testRange(int year, int month, int dom, TemporalField field, int expectedMin, int expectedMax) {
        assertEquals(ValueRange.of(expectedMin, expectedMax), BritishCutoverDate.of(year, month, dom).range(field), "Range mismatch");
    }

    @Test
    public void testRangeUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> BritishCutoverDate.of(2012, 6, 30).range(MINUTE_OF_DAY), "Expected UnsupportedTemporalTypeException");
    }

    // Test BritishCutoverDate.getLong
    public static Object[][] dataGetLong() {
        return new Object[][] {
            {1752, 5, 26, DAY_OF_WEEK, 2},
            {1752, 5, 26, DAY_OF_MONTH, 26},
            {1752, 5, 26, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 26},
            {1752, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {1752, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {1752, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7},
            {1752, 5, 26, ALIGNED_WEEK_OF_YEAR, 21},
            {1752, 5, 26, MONTH_OF_YEAR, 5},
            // Additional test cases...
        };
    }

    @ParameterizedTest
    @MethodSource("dataGetLong")
    public void testGetLong(int year, int month, int dom, TemporalField field, long expected) {
        assertEquals(expected, BritishCutoverDate.of(year, month, dom).getLong(field), "Get long mismatch");
    }

    @Test
    public void testGetLongUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> BritishCutoverDate.of(2012, 6, 30).getLong(MINUTE_OF_DAY), "Expected UnsupportedTemporalTypeException");
    }

    // Test BritishCutoverDate.with
    public static Object[][] dataWith() {
        return new Object[][] {
            {1752, 9, 2, DAY_OF_WEEK, 1, 1752, 8, 31},
            {1752, 9, 2, DAY_OF_WEEK, 4, 1752, 9, 14},
            {1752, 9, 2, DAY_OF_MONTH, 1, 1752, 9, 1},
            {1752, 9, 2, DAY_OF_MONTH, 3, 1752, 9, 14},  // lenient
            {1752, 9, 2, DAY_OF_MONTH, 13, 1752, 9, 24},  // lenient
            {1752, 9, 2, DAY_OF_MONTH, 14, 1752, 9, 14},
            {1752, 9, 2, DAY_OF_MONTH, 30, 1752, 9, 30},
            {1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 1, 1752, 9, 1},
            {1752, 9, 2, DAY_OF_YEAR, 31 + 29 + 31 + 30 + 31 + 30 + 31 + 31 + 3, 1752, 9, 14},
            {1752, 9, 2, DAY_OF_YEAR, 356, 1753, 1, 1},  // lenient
            {1752, 9, 2, DAY_OF_YEAR, 366, 1753, 1, 11},  // lenient
            {1752, 9, 2, ALIGNED_DAY_OF_WEEK_IN_MONTH, 1, 1752, 9, 1},
            {1752, 9, 2, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3, 1752, 9, 14},
            {1752, 9, 2, ALIGNED_WEEK_OF_MONTH, 2, 1752, 9, 20},
            {1752, 9, 2, ALIGNED_WEEK_OF_MONTH, 3, 1752, 9, 27},
            {1752, 9, 2, ALIGNED_WEEK_OF_MONTH, 4, 1752, 10, 4},  // lenient
            {1752, 9, 2, ALIGNED_WEEK_OF_MONTH, 5, 1752, 10, 11},  // lenient
            {1752, 9, 2, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2, 1752, 9, 14},
            {1752, 9, 2, ALIGNED_DAY_OF_WEEK_IN_YEAR, 3, 1752, 9, 15},
            {1752, 9, 2, ALIGNED_WEEK_OF_YEAR, 1, 1752, 1, 1},
            {1752, 9, 2, ALIGNED_WEEK_OF_YEAR, 35, 1752, 8, 26},
            {1752, 9, 2, ALIGNED_WEEK_OF_YEAR, 37, 1752, 9, 20},
            {1752, 9, 2, ALIGNED_WEEK_OF_YEAR, 51, 1752, 12, 27},
            {1752, 9, 2, ALIGNED_WEEK_OF_YEAR, 52, 1753, 1, 3},  // lenient
            {1752, 9, 2, MONTH_OF_YEAR, 8, 1752, 8, 2},
            {1752, 9, 2, MONTH_OF_YEAR, 10, 1752, 10, 2},
            // Additional test cases...
        };
    }

    @ParameterizedTest
    @MethodSource("dataWith")
    public void testWithTemporalField(int year, int month, int dom,
            TemporalField field, long value,
            int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(BritishCutoverDate.of(expectedYear, expectedMonth, expectedDom), BritishCutoverDate.of(year, month, dom).with(field, value), "With field mismatch");
    }

    @Test
    public void testWithTemporalFieldUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> BritishCutoverDate.of(2012, 6, 30).with(MINUTE_OF_DAY, 0), "Expected UnsupportedTemporalTypeException");
    }

    // Test BritishCutoverDate.with(TemporalAdjuster)
    public static Object[][] dataLastDayOfMonth() {
        return new Object[][] {
            {BritishCutoverDate.of(1752, 2, 23), BritishCutoverDate.of(1752, 2, 29)},
            {BritishCutoverDate.of(1752, 6, 23), BritishCutoverDate.of(1752, 6, 30)},
            {BritishCutoverDate.of(1752, 9, 2), BritishCutoverDate.of(1752, 9, 30)},
            {BritishCutoverDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 30)},
            {BritishCutoverDate.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 29)},
            {BritishCutoverDate.of(2012, 6, 23), BritishCutoverDate.of(2012, 6, 30)},
            // Additional test cases...
        };
    }

    @ParameterizedTest
    @MethodSource("dataLastDayOfMonth")
    public void testAdjustLastDayOfMonth(BritishCutoverDate input, BritishCutoverDate expected) {
        BritishCutoverDate test = input.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(expected, test, "Last day of month mismatch");
    }

    // Test BritishCutoverDate.with(Local*)
    public static Object[][] dataWithLocalDate() {
        return new Object[][] {
            {BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 12), BritishCutoverDate.of(1752, 9, 1)},
            {BritishCutoverDate.of(1752, 9, 14), LocalDate.of(1752, 9, 12), BritishCutoverDate.of(1752, 9, 1)},
            {BritishCutoverDate.of(1752, 9, 2), LocalDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 14)},
            {BritishCutoverDate.of(1752, 9, 15), LocalDate.of(1752, 9, 14), BritishCutoverDate.of(1752, 9, 14)},
            {BritishCutoverDate.of(2012, 2, 23), LocalDate.of(2012, 2, 23), BritishCutoverDate.of(2012, 2, 23)},
            // Additional test cases...
        };
    }

    @ParameterizedTest
    @MethodSource("dataWithLocalDate")
    public void testAdjustLocalDate(BritishCutoverDate input, LocalDate local, BritishCutoverDate expected) {
        BritishCutoverDate test = input.with(local);
        assertEquals(expected, test, "Local date adjustment mismatch");
    }

    @Test
    public void testAdjustToMonth() {
        BritishCutoverDate cutover = BritishCutoverDate.of(2000, 1, 4);
        assertThrows(DateTimeException.class, () -> cutover.with(Month.APRIL), "Expected DateTimeException");
    }

    // Test LocalDate.with(BritishCutoverDate)
    @Test
    public void testLocalDateWithBritishCutoverDate() {
        BritishCutoverDate cutover = BritishCutoverDate.of(2012, 6, 23);
        LocalDate test = LocalDate.MIN.with(cutover);
        assertEquals(LocalDate.of(2012, 6, 23), test, "Local date mismatch");
    }

    @Test
    public void testLocalDateTimeWithBritishCutoverDate() {
        BritishCutoverDate cutover = BritishCutoverDate.of(2012, 6, 23);
        LocalDateTime test = LocalDateTime.MIN.with(cutover);
        assertEquals(LocalDateTime.of(2012, 6, 23, 0, 0), test, "Local date-time mismatch");
    }

    // Test BritishCutoverDate.plus
    public static Object[][] dataPlus() {
        return new Object[][] {
            {1752, 9, 2, -1, DAYS, 1752, 9, 1, true},
            {1752, 9, 2, 0, DAYS, 1752, 9, 2, true},
            {1752, 9, 2, 1, DAYS, 1752, 9, 14, true},
            {1752, 9, 2, 2, DAYS, 1752, 9, 15, true},
            {1752, 9, 14, -1, DAYS, 1752, 9, 2, true},
            {1752, 9, 14, 0, DAYS, 1752, 9, 14, true},
            {1752, 9, 14, 1, DAYS, 1752, 9, 15, true},
            {2014, 5, 26, 0, DAYS, 2014, 5, 26, true},
            {2014, 5, 26, 8, DAYS, 2014, 6, 3, true},
            {2014, 5, 26, -3, DAYS, 2014, 5, 23, true},
            // Additional test cases...
        };
    }

    @ParameterizedTest
    @MethodSource("dataPlus")
    public void testPlusTemporalUnit(int year, int month, int dom,
            long amount, TemporalUnit unit,
            int expectedYear, int expectedMonth, int expectedDom, boolean bidi) {
        assertEquals(BritishCutoverDate.of(expectedYear, expectedMonth, expectedDom), BritishCutoverDate.of(year, month, dom).plus(amount, unit), "Plus unit mismatch");
    }

    @ParameterizedTest
    @MethodSource("dataPlus")
    public void testMinusTemporalUnit(
            int expectedYear, int expectedMonth, int expectedDom,
            long amount, TemporalUnit unit,
            int year, int month, int dom, boolean bidi) {
        if (bidi) {
            assertEquals(BritishCutoverDate.of(expectedYear, expectedMonth, expectedDom), BritishCutoverDate.of(year, month, dom).minus(amount, unit), "Minus unit mismatch");
        }
    }

    @Test
    public void testPlusTemporalUnitUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> BritishCutoverDate.of(2012, 6, 30).plus(0, MINUTES), "Expected UnsupportedTemporalTypeException");
    }

    // Test BritishCutoverDate.until
    public static Object[][] dataUntil() {
        return new Object[][] {
            {1752, 9, 1, 1752, 9, 2, DAYS, 1},
            {1752, 9, 1, 1752, 9, 14, DAYS, 2},
            {1752, 9, 2, 1752, 9, 14, DAYS, 1},
            {1752, 9, 2, 1752, 9, 15, DAYS, 2},
            {1752, 9, 14, 1752, 9, 1, DAYS, -2},
            {1752, 9, 14, 1752, 9, 2, DAYS, -1},
            {2014, 5, 26, 2014, 5, 26, DAYS, 0},
            {2014, 5, 26, 2014, 6, 1, DAYS, 6},
            {2014, 5, 26, 2014, 5, 20, DAYS, -6},
            // Additional test cases...
        };
    }

    @ParameterizedTest
    @MethodSource("dataUntil")
    public void testUntilTemporalUnit(
            int year1, int month1, int dom1,
            int year2, int month2, int dom2,
            TemporalUnit unit, long expected) {
        BritishCutoverDate start = BritishCutoverDate.of(year1, month1, dom1);
        BritishCutoverDate end = BritishCutoverDate.of(year2, month2, dom2);
        assertEquals(expected, start.until(end, unit), "Until unit mismatch");
    }

    @Test
    public void testUntilTemporalUnitUnsupported() {
        BritishCutoverDate start = BritishCutoverDate.of(2012, 6, 30);
        BritishCutoverDate end = BritishCutoverDate.of(2012, 7, 1);
        assertThrows(UnsupportedTemporalTypeException.class, () -> start.until(end, MINUTES), "Expected UnsupportedTemporalTypeException");
    }

    // Test plus and minus Period
    @Test
    public void testPlusPeriod() {
        assertEquals(
            BritishCutoverDate.of(1752, 10, 5),
            BritishCutoverDate.of(1752, 9, 2).plus(BritishCutoverChronology.INSTANCE.period(0, 1, 3)),
            "Plus period mismatch");
        assertEquals(
            BritishCutoverDate.of(1752, 9, 23),
            BritishCutoverDate.of(1752, 8, 12).plus(BritishCutoverChronology.INSTANCE.period(0, 1, 0)),
            "Plus period mismatch");
        assertEquals(
            BritishCutoverDate.of(2014, 7, 29),
            BritishCutoverDate.of(2014, 5, 26).plus(BritishCutoverChronology.INSTANCE.period(0, 2, 3)),
            "Plus period mismatch");
    }

    @Test
    public void testPlusPeriodISO() {
        assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(2014, 5, 26).plus(Period.ofMonths(2)), "Expected DateTimeException");
    }

    @Test
    public void testMinusPeriod() {
        assertEquals(
            BritishCutoverDate.of(1752, 9, 23),
            BritishCutoverDate.of(1752, 10, 12).minus(BritishCutoverChronology.INSTANCE.period(0, 1, 0)),
            "Minus period mismatch");
        assertEquals(
            BritishCutoverDate.of(2014, 3, 23),
            BritishCutoverDate.of(2014, 5, 26).minus(BritishCutoverChronology.INSTANCE.period(0, 2, 3)),
            "Minus period mismatch");
    }

    @Test
    public void testMinusPeriodISO() {
        assertThrows(DateTimeException.class, () -> BritishCutoverDate.of(2014, 5, 26).minus(Period.ofMonths(2)), "Expected DateTimeException");
    }

    // Test until ChronoLocalDate
    public static Object[][] dataUntilCLD() {
        return new Object[][] {
            {1752, 7, 2, 1752, 7, 1, 0, 0, -1},
            {1752, 7, 2, 1752, 7, 2, 0, 0, 0},
            {1752, 7, 2, 1752, 9, 1, 0, 1, 30},  // 30 days after 1752-08-02
            {1752, 7, 2, 1752, 9, 2, 0, 2, 0},  // 2 whole months
            {1752, 7, 2, 1752, 9, 14, 0, 2, 1},  // 1 day after 1752-09-02
            {1752, 7, 2, 1752, 9, 30, 0, 2, 17},  // 17 days after 1752-09-02
            {1752, 7, 2, 1752, 10, 1, 0, 2, 18},  // 18 days after 1752-09-02
            {1752, 7, 2, 1752, 10, 2, 0, 3, 0},  // 3 whole months
            {1752, 7, 2, 1752, 10, 3, 0, 3, 1},
            {1752, 7, 2, 1752, 10, 30, 0, 3, 28},
            {1752, 7, 2, 1752, 11, 1, 0, 3, 30},
            {1752, 7, 2, 1752, 11, 2, 0, 4, 0},
            // Additional test cases...
        };
    }

    @ParameterizedTest
    @MethodSource("dataUntilCLD")
    public void testUntilCLD(
            int year1, int month1, int dom1,
            int year2, int month2, int dom2,
            int expectedYears, int expectedMonths, int expectedDays) {
        BritishCutoverDate a = BritishCutoverDate.of(year1, month1, dom1);
        BritishCutoverDate b = BritishCutoverDate.of(year2, month2, dom2);
        ChronoPeriod c = a.until(b);
        assertEquals(
            BritishCutoverChronology.INSTANCE.period(expectedYears, expectedMonths, expectedDays),
            c, "Until CLD mismatch");
    }

    @ParameterizedTest
    @MethodSource("dataUntilCLD")
    public void testUntilCLDPlus(
            int year1, int month1, int dom1,
            int year2, int month2, int dom2,
            int expectedYears, int expectedMonths, int expectedDays) {
        BritishCutoverDate a = BritishCutoverDate.of(year1, month1, dom1);
        BritishCutoverDate b = BritishCutoverDate.of(year2, month2, dom2);
        ChronoPeriod c = a.until(b);
        assertEquals(b, a.plus(c), "Until CLD plus mismatch");
    }

    // Test atTime(LocalTime)
    @Test
    public void testAtTime() {
        BritishCutoverDate date = BritishCutoverDate.of(2014, 10, 12);
        ChronoLocalDateTime<BritishCutoverDate> test = date.atTime(LocalTime.of(12, 30));
        assertEquals(date, test.toLocalDate(), "Local date mismatch");
        assertEquals(LocalTime.of(12, 30), test.toLocalTime(), "Local time mismatch");
        ChronoLocalDateTime<BritishCutoverDate> test2 =
            BritishCutoverChronology.INSTANCE.localDateTime(LocalDateTime.from(test));
        assertEquals(test, test2, "Local date-time mismatch");
    }

    @Test
    public void testAtTimeNull() {
        assertThrows(NullPointerException.class, () -> BritishCutoverDate.of(2014, 5, 26).atTime(null), "Expected NullPointerException");
    }

    // Cross-check against GregorianCalendar
    @Test
    public void testCrossCheck() {
        BritishCutoverDate test = BritishCutoverDate.of(1700, 1, 1);
        BritishCutoverDate end = BritishCutoverDate.of(1800, 1, 1);
        Instant cutover = ZonedDateTime.of(1752, 9, 14, 0, 0, 0, 0, ZoneOffset.UTC).toInstant();
        GregorianCalendar gcal = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        gcal.setGregorianChange(Date.from(cutover));
        gcal.clear();
        gcal.set(1700, Calendar.JANUARY, 1);
        while (test.isBefore(end)) {
            assertEquals(gcal.get(Calendar.YEAR), test.get(YEAR_OF_ERA), "Year mismatch");
            assertEquals(gcal.get(Calendar.MONTH) + 1, test.get(MONTH_OF_YEAR), "Month mismatch");
            assertEquals(gcal.get(Calendar.DAY_OF_MONTH), test.get(DAY_OF_MONTH), "Day mismatch");
            assertEquals(gcal.toZonedDateTime().toLocalDate(), LocalDate.from(test), "Local date mismatch");
            gcal.add(Calendar.DAY_OF_MONTH, 1);
            test = test.plus(1, DAYS);
        }
    }

    // Test equals() / hashCode()
    @Test
    public void testEqualsAndHashCode() {
        new EqualsTester()
            .addEqualityGroup(BritishCutoverDate.of(2000, 1, 3), BritishCutoverDate.of(2000, 1, 3))
            .addEqualityGroup(BritishCutoverDate.of(2000, 1, 4), BritishCutoverDate.of(2000, 1, 4))
            .addEqualityGroup(BritishCutoverDate.of(2000, 2, 3), BritishCutoverDate.of(2000, 2, 3))
            .addEqualityGroup(BritishCutoverDate.of(2001, 1, 3), BritishCutoverDate.of(2001, 1, 3))
            .testEquals();
    }

    // Test toString()
    public static Object[][] dataToString() {
        return new Object[][] {
            {BritishCutoverDate.of(1, 1, 1), "BritishCutover AD 1-01-01"},
            {BritishCutoverDate.of(2012, 6, 23), "BritishCutover AD 2012-06-23"},
            // Additional test cases...
        };
    }

    @ParameterizedTest
    @MethodSource("dataToString")
    public void testToString(BritishCutoverDate cutover, String expected) {
        assertEquals(expected, cutover.toString(), "ToString mismatch");
    }
}