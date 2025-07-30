package org.threeten.extra.chrono;

import static java.time.temporal.ChronoField.*;
import static java.time.temporal.ChronoUnit.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for the Symmetry010Chronology class.
 */
public class TestSymmetry010Chronology {

    // Test for Chronology.of(String)
    @Test
    public void testChronologyOfString() {
        Chronology chrono = Chronology.of("Sym010");
        assertNotNull(chrono, "Chronology should not be null");
        assertEquals(Symmetry010Chronology.INSTANCE, chrono, "Chronology instance should match");
        assertEquals("Sym010", chrono.getId(), "Chronology ID should be 'Sym010'");
        assertNull(chrono.getCalendarType(), "Calendar type should be null");
    }

    // Data provider for sample dates
    public static Object[][] dataSamples() {
        return new Object[][] {
            { Symmetry010Date.of(1, 1, 1), LocalDate.of(1, 1, 1) },
            { Symmetry010Date.of(272, 2, 28), LocalDate.of(272, 2, 27) },
            // Additional samples...
        };
    }

    // Test conversion from Symmetry010Date to LocalDate
    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testLocalDateFromSymmetry010Date(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(iso, LocalDate.from(sym010), "Conversion to LocalDate should match");
    }

    // Test conversion from LocalDate to Symmetry010Date
    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testSymmetry010DateFromLocalDate(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(sym010, Symmetry010Date.from(iso), "Conversion to Symmetry010Date should match");
    }

    // Test dateEpochDay method
    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testSymmetry010DateChronologyDateEpochDay(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(sym010, Symmetry010Chronology.INSTANCE.dateEpochDay(iso.toEpochDay()), "dateEpochDay should match");
    }

    // Test toEpochDay method
    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testSymmetry010DateToEpochDay(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(iso.toEpochDay(), sym010.toEpochDay(), "toEpochDay should match");
    }

    // Test until method for Symmetry010Date
    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testSymmetry010DateUntilSymmetry010Date(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(sym010), "until should return zero period");
    }

    // Test until method for LocalDate
    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testSymmetry010DateUntilLocalDate(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(Symmetry010Chronology.INSTANCE.period(0, 0, 0), sym010.until(iso), "until should return zero period");
    }

    // Test Chronology.date method
    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testChronologyDateTemporal(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(sym010, Symmetry010Chronology.INSTANCE.date(iso), "Chronology.date should match");
    }

    // Test LocalDate.until method
    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testLocalDateUntilSymmetry010Date(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(Period.ZERO, iso.until(sym010), "until should return zero period");
    }

    // Test plusDays method
    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testPlusDays(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(iso, LocalDate.from(sym010.plus(0, DAYS)), "plusDays(0) should match");
        assertEquals(iso.plusDays(1), LocalDate.from(sym010.plus(1, DAYS)), "plusDays(1) should match");
        // Additional assertions...
    }

    // Test minusDays method
    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testMinusDays(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(iso, LocalDate.from(sym010.minus(0, DAYS)), "minusDays(0) should match");
        assertEquals(iso.minusDays(1), LocalDate.from(sym010.minus(1, DAYS)), "minusDays(1) should match");
        // Additional assertions...
    }

    // Test until method with DAYS unit
    @ParameterizedTest
    @MethodSource("dataSamples")
    public void testUntilDays(Symmetry010Date sym010, LocalDate iso) {
        assertEquals(0, sym010.until(iso.plusDays(0), DAYS), "until(0 days) should be 0");
        assertEquals(1, sym010.until(iso.plusDays(1), DAYS), "until(1 day) should be 1");
        // Additional assertions...
    }

    // Data provider for invalid dates
    public static Object[][] dataBadDates() {
        return new Object[][] {
            {-1, 13, 28},
            {2000, 13, 1},
            // Additional invalid dates...
        };
    }

    // Test invalid dates
    @ParameterizedTest
    @MethodSource("dataBadDates")
    public void testBadDates(int year, int month, int dom) {
        assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dom), "Invalid date should throw exception");
    }

    // Data provider for invalid leap dates
    public static Object[][] dataBadLeapDates() {
        return new Object[][] {
            {1},
            {100},
            // Additional invalid leap years...
        };
    }

    // Test invalid leap day dates
    @ParameterizedTest
    @MethodSource("dataBadLeapDates")
    public void testBadLeapDayDates(int year) {
        assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, 12, 37), "Invalid leap day should throw exception");
    }

    // Test isLeapYear method
    @Test
    public void testIsLeapYear() {
        assertTrue(Symmetry010Chronology.INSTANCE.isLeapYear(3), "Year 3 should be a leap year");
        assertFalse(Symmetry010Chronology.INSTANCE.isLeapYear(6), "Year 6 should not be a leap year");
        // Additional assertions...
    }

    // Test isLeapWeek method
    @Test
    public void testLeapWeek() {
        assertTrue(Symmetry010Date.of(2015, 12, 31).isLeapWeek(), "December 31, 2015 should be in a leap week");
        // Additional assertions...
    }

    // Data provider for length of month
    public static Object[][] dataLengthOfMonth() {
        return new Object[][] {
            {2000, 1, 28, 30},
            {2000, 2, 28, 31},
            // Additional length of month data...
        };
    }

    // Test lengthOfMonth method
    @ParameterizedTest
    @MethodSource("dataLengthOfMonth")
    public void testLengthOfMonth(int year, int month, int day, int length) {
        assertEquals(length, Symmetry010Date.of(year, month, day).lengthOfMonth(), "Length of month should match");
    }

    // Test lengthOfMonth for the first day of the month
    @ParameterizedTest
    @MethodSource("dataLengthOfMonth")
    public void testLengthOfMonthFirst(int year, int month, int day, int length) {
        assertEquals(length, Symmetry010Date.of(year, month, 1).lengthOfMonth(), "Length of month for first day should match");
    }

    // Test lengthOfMonth for specific cases
    @Test
    public void testLengthOfMonthSpecific() {
        assertEquals(30, Symmetry010Date.of(2000, 12, 1).lengthOfMonth(), "December 2000 should have 30 days");
        assertEquals(37, Symmetry010Date.of(2004, 12, 1).lengthOfMonth(), "December 2004 should have 37 days");
    }

    // Test era-related methods
    @Test
    public void testEraLoop() {
        for (int year = 1; year < 200; year++) {
            Symmetry010Date base = Symmetry010Chronology.INSTANCE.date(year, 1, 1);
            assertEquals(year, base.get(YEAR), "Year should match");
            IsoEra era = IsoEra.CE;
            assertEquals(era, base.getEra(), "Era should be CE");
            assertEquals(year, base.get(YEAR_OF_ERA), "Year of era should match");
            Symmetry010Date eraBased = Symmetry010Chronology.INSTANCE.date(era, year, 1, 1);
            assertEquals(base, eraBased, "Era-based date should match");
        }
    }

    // Test prolepticYear method
    @Test
    public void testProlepticYearSpecific() {
        assertEquals(4, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 4), "Proleptic year should match");
        assertEquals(3, Symmetry010Chronology.INSTANCE.prolepticYear(IsoEra.CE, 3), "Proleptic year should match");
        // Additional assertions...
    }

    // Data provider for invalid eras
    public static Object[][] dataProlepticYearBadEra() {
        return new Era[][] {
            { HijrahEra.AH },
            { JapaneseEra.MEIJI },
            // Additional invalid eras...
        };
    }

    // Test prolepticYear method with invalid eras
    @ParameterizedTest
    @MethodSource("dataProlepticYearBadEra")
    public void testProlepticYearBadEra(Era era) {
        assertThrows(ClassCastException.class, () -> Symmetry010Chronology.INSTANCE.prolepticYear(era, 4), "Invalid era should throw exception");
    }

    // Test eraOf method
    @Test
    public void testChronologyEraOf() {
        assertEquals(IsoEra.BCE, Symmetry010Chronology.INSTANCE.eraOf(0), "Era should be BCE");
        assertEquals(IsoEra.CE, Symmetry010Chronology.INSTANCE.eraOf(1), "Era should be CE");
    }

    // Test eraOf method with invalid value
    @Test
    public void testChronologyEraOfInvalid() {
        assertThrows(DateTimeException.class, () -> Symmetry010Chronology.INSTANCE.eraOf(2), "Invalid era value should throw exception");
    }

    // Test eras method
    @Test
    public void testChronologyEras() {
        List<Era> eras = Symmetry010Chronology.INSTANCE.eras();
        assertEquals(2, eras.size(), "There should be two eras");
        assertTrue(eras.contains(IsoEra.BCE), "Eras should contain BCE");
        assertTrue(eras.contains(IsoEra.CE), "Eras should contain CE");
    }

    // Test range method
    @Test
    public void testChronologyRange() {
        assertEquals(ValueRange.of(1, 7), Symmetry010Chronology.INSTANCE.range(ALIGNED_DAY_OF_WEEK_IN_MONTH), "Range should match");
        assertEquals(ValueRange.of(1, 7), Symmetry010Chronology.INSTANCE.range(ALIGNED_DAY_OF_WEEK_IN_YEAR), "Range should match");
        // Additional assertions...
    }

    // Data provider for range tests
    public static Object[][] dataRanges() {
        return new Object[][] {
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 30)},
            {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 31)},
            // Additional range data...
        };
    }

    // Test range method
    @ParameterizedTest
    @MethodSource("dataRanges")
    public void testRange(int year, int month, int dom, TemporalField field, ValueRange range) {
        assertEquals(range, Symmetry010Date.of(year, month, dom).range(field), "Range should match");
    }

    // Test range method with unsupported field
    @Test
    public void testRangeUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry010Date.of(2012, 6, 28).range(MINUTE_OF_DAY), "Unsupported field should throw exception");
    }

    // Data provider for getLong tests
    public static Object[][] dataGetLong() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 2},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            // Additional getLong data...
        };
    }

    // Test getLong method
    @ParameterizedTest
    @MethodSource("dataGetLong")
    public void testGetLong(int year, int month, int dom, TemporalField field, long expected) {
        assertEquals(expected, Symmetry010Date.of(year, month, dom).getLong(field), "getLong should match");
    }

    // Test getLong method with unsupported field
    @Test
    public void testGetLongUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry010Date.of(2012, 6, 28).getLong(MINUTE_OF_DAY), "Unsupported field should throw exception");
    }

    // Data provider for with method tests
    public static Object[][] dataWith() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 20},
            {2014, 5, 26, DAY_OF_WEEK, 5, 2014, 5, 24},
            // Additional with data...
        };
    }

    // Test with method
    @ParameterizedTest
    @MethodSource("dataWith")
    public void testWithTemporalField(int year, int month, int dom, TemporalField field, long value, int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(Symmetry010Date.of(expectedYear, expectedMonth, expectedDom), Symmetry010Date.of(year, month, dom).with(field, value), "with should match");
    }

    // Data provider for invalid with method tests
    public static Object[][] dataWithBad() {
        return new Object[][] {
            {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, -1},
            {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, 8},
            // Additional invalid with data...
        };
    }

    // Test with method with invalid values
    @ParameterizedTest
    @MethodSource("dataWithBad")
    public void testWithTemporalFieldBadValue(int year, int month, int dom, TemporalField field, long value) {
        assertThrows(DateTimeException.class, () -> Symmetry010Date.of(year, month, dom).with(field, value), "Invalid value should throw exception");
    }

    // Test with method with unsupported field
    @Test
    public void testWithTemporalFieldUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry010Date.of(2012, 6, 28).with(MINUTE_OF_DAY, 10), "Unsupported field should throw exception");
    }

    // Data provider for temporal adjusters tests
    public static Object[][] dataTemporalAdjustersLastDayOfMonth() {
        return new Object[][] {
            {2012, 1, 23, 2012, 1, 30},
            {2012, 2, 23, 2012, 2, 31},
            // Additional temporal adjusters data...
        };
    }

    // Test temporal adjusters for last day of month
    @ParameterizedTest
    @MethodSource("dataTemporalAdjustersLastDayOfMonth")
    public void testTemporalAdjustersLastDayOfMonth(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
        Symmetry010Date base = Symmetry010Date.of(year, month, day);
        Symmetry010Date expected = Symmetry010Date.of(expectedYear, expectedMonth, expectedDay);
        Symmetry010Date actual = base.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(expected, actual, "Last day of month should match");
    }

    // Test adjust to LocalDate
    @Test
    public void testAdjustToLocalDate() {
        Symmetry010Date sym010 = Symmetry010Date.of(2000, 1, 4);
        Symmetry010Date test = sym010.with(LocalDate.of(2012, 7, 6));
        assertEquals(Symmetry010Date.of(2012, 7, 5), test, "Adjust to LocalDate should match");
    }

    // Test adjust to Month
    @Test
    public void testAdjustToMonth() {
        Symmetry010Date sym010 = Symmetry010Date.of(2000, 1, 4);
        assertThrows(DateTimeException.class, () -> sym010.with(Month.APRIL), "Adjust to Month should throw exception");
    }

    // Test LocalDate adjust to Symmetry010Date
    @Test
    public void testLocalDateAdjustToSymmetry010Date() {
        Symmetry010Date sym010 = Symmetry010Date.of(2012, 7, 19);
        LocalDate test = LocalDate.MIN.with(sym010);
        assertEquals(LocalDate.of(2012, 7, 20), test, "LocalDate adjust to Symmetry010Date should match");
    }

    // Test LocalDateTime adjust to Symmetry010Date
    @Test
    public void testLocalDateTimeAdjustToSymmetry010Date() {
        Symmetry010Date sym010 = Symmetry010Date.of(2012, 7, 19);
        LocalDateTime test = LocalDateTime.MIN.with(sym010);
        assertEquals(LocalDateTime.of(2012, 7, 20, 0, 0), test, "LocalDateTime adjust to Symmetry010Date should match");
    }

    // Data provider for plus method tests
    public static Object[][] dataPlus() {
        return new Object[][] {
            {2014, 5, 26, 0, DAYS, 2014, 5, 26},
            {2014, 5, 26, 8, DAYS, 2014, 6, 3},
            // Additional plus data...
        };
    }

    // Data provider for plus method tests with leap week
    public static Object[][] dataPlusLeapWeek() {
        return new Object[][] {
            {2015, 12, 28, 0, DAYS, 2015, 12, 28},
            {2015, 12, 28, 8, DAYS, 2015, 12, 36},
            // Additional plus leap week data...
        };
    }

    // Test plus method
    @ParameterizedTest
    @MethodSource("dataPlus")
    public void testPlusTemporalUnit(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(Symmetry010Date.of(expectedYear, expectedMonth, expectedDom), Symmetry010Date.of(year, month, dom).plus(amount, unit), "plus should match");
    }

    // Test plus method with leap week
    @ParameterizedTest
    @MethodSource("dataPlusLeapWeek")
    public void testPlusLeapWeekTemporalUnit(int year, int month, int dom, long amount, TemporalUnit unit, int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(Symmetry010Date.of(expectedYear, expectedMonth, expectedDom), Symmetry010Date.of(year, month, dom).plus(amount, unit), "plus with leap week should match");
    }

    // Test minus method
    @ParameterizedTest
    @MethodSource("dataPlus")
    public void testMinusTemporalUnit(int expectedYear, int expectedMonth, int expectedDom, long amount, TemporalUnit unit, int year, int month, int dom) {
        assertEquals(Symmetry010Date.of(expectedYear, expectedMonth, expectedDom), Symmetry010Date.of(year, month, dom).minus(amount, unit), "minus should match");
    }

    // Test minus method with leap week
    @ParameterizedTest
    @MethodSource("dataPlusLeapWeek")
    public void testMinusLeapWeekTemporalUnit(int expectedYear, int expectedMonth, int expectedDom, long amount, TemporalUnit unit, int year, int month, int dom) {
        assertEquals(Symmetry010Date.of(expectedYear, expectedMonth, expectedDom), Symmetry010Date.of(year, month, dom).minus(amount, unit), "minus with leap week should match");
    }

    // Test plus method with unsupported unit
    @Test
    public void testPlusTemporalUnitUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry010Date.of(2012, 6, 28).plus(0, MINUTES), "Unsupported unit should throw exception");
    }

    // Data provider for until method tests
    public static Object[][] dataUntil() {
        return new Object[][] {
            {2014, 5, 26, 2014, 5, 26, DAYS, 0},
            {2014, 5, 26, 2014, 6, 4, DAYS, 9},
            // Additional until data...
        };
    }

    // Data provider for until method tests with period
    public static Object[][] dataUntilPeriod() {
        return new Object[][] {
            {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
            {2014, 5, 26, 2014, 6, 4, 0, 0, 9},
            // Additional until period data...
        };
    }

    // Test until method
    @ParameterizedTest
    @MethodSource("dataUntil")
    public void testUntilTemporalUnit(int year1, int month1, int dom1, int year2, int month2, int dom2, TemporalUnit unit, long expected) {
        Symmetry010Date start = Symmetry010Date.of(year1, month1, dom1);
        Symmetry010Date end = Symmetry010Date.of(year2, month2, dom2);
        assertEquals(expected, start.until(end, unit), "until should match");
    }

    // Test until method with period
    @ParameterizedTest
    @MethodSource("dataUntilPeriod")
    public void testUntilEnd(int year1, int month1, int dom1, int year2, int month2, int dom2, int yearPeriod, int monthPeriod, int dayPeriod) {
        Symmetry010Date start = Symmetry010Date.of(year1, month1, dom1);
        Symmetry010Date end = Symmetry010Date.of(year2, month2, dom2);
        ChronoPeriod period = Symmetry010Chronology.INSTANCE.period(yearPeriod, monthPeriod, dayPeriod);
        assertEquals(period, start.until(end), "until should match period");
    }

    // Test until method with unsupported unit
    @Test
    public void testUntilTemporalUnitUnsupported() {
        Symmetry010Date start = Symmetry010Date.of(2012, 6, 28);
        Symmetry010Date end = Symmetry010Date.of(2012, 7, 1);
        assertThrows(UnsupportedTemporalTypeException.class, () -> start.until(end, MINUTES), "Unsupported unit should throw exception");
    }

    // Test plus method with period
    @Test
    public void testPlusPeriod() {
        assertEquals(Symmetry010Date.of(2014, 7, 29), Symmetry010Date.of(2014, 5, 21).plus(Symmetry010Chronology.INSTANCE.period(0, 2, 8)), "plus with period should match");
    }

    // Test plus method with ISO period
    @Test
    public void testPlusPeriodISO() {
        assertThrows(DateTimeException.class, () -> Symmetry010Date.of(2014, 5, 26).plus(Period.ofMonths(2)), "ISO period should throw exception");
    }

    // Test minus method with period
    @Test
    public void testMinusPeriod() {
        assertEquals(Symmetry010Date.of(2014, 3, 23), Symmetry010Date.of(2014, 5, 26).minus(Symmetry010Chronology.INSTANCE.period(0, 2, 3)), "minus with period should match");
    }

    // Test minus method with ISO period
    @Test
    public void testMinusPeriodISO() {
        assertThrows(DateTimeException.class, () -> Symmetry010Date.of(2014, 5, 26).minus(Period.ofMonths(2)), "ISO period should throw exception");
    }

    // Test equals and hashCode methods
    @Test
    public void testEqualsAndHashCode() {
        new EqualsTester()
            .addEqualityGroup(Symmetry010Date.of(2000, 1, 3), Symmetry010Date.of(2000, 1, 3))
            .addEqualityGroup(Symmetry010Date.of(2000, 1, 4), Symmetry010Date.of(2000, 1, 4))
            // Additional equality groups...
            .testEquals();
    }

    // Data provider for toString method tests
    public static Object[][] dataToString() {
        return new Object[][] {
            {Symmetry010Date.of(1, 1, 1), "Sym010 CE 1/01/01"},
            {Symmetry010Date.of(1970, 2, 31), "Sym010 CE 1970/02/31"},
            // Additional toString data...
        };
    }

    // Test toString method
    @ParameterizedTest
    @MethodSource("dataToString")
    public void testToString(Symmetry010Date date, String expected) {
        assertEquals(expected, date.toString(), "toString should match");
    }
}