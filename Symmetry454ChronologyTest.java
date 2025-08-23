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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.google.common.testing.EqualsTester;

/**
 * Unit tests for Symmetry454Chronology.
 */
public class TestSymmetry454Chronology {

    // Constants for test data
    private static final Chronology SYM454_CHRONOLOGY = Symmetry454Chronology.INSTANCE;

    // Test Chronology.of(String)
    @Test
    public void testChronologyOfString() {
        Chronology chrono = Chronology.of("Sym454");
        assertNotNull(chrono, "Chronology should not be null");
        assertEquals(SYM454_CHRONOLOGY, chrono, "Chronology instance mismatch");
        assertEquals("Sym454", chrono.getId(), "Chronology ID mismatch");
        assertNull(chrono.getCalendarType(), "Calendar type should be null");
    }

    // Data provider for sample date conversions
    public static Object[][] sampleDateConversions() {
        return new Object[][] {
            { Symmetry454Date.of(1, 1, 1), LocalDate.of(1, 1, 1) },
            { Symmetry454Date.of(272, 2, 30), LocalDate.of(272, 2, 27) },
            // More data samples...
        };
    }

    // Test conversion from Symmetry454Date to LocalDate
    @ParameterizedTest
    @MethodSource("sampleDateConversions")
    public void testLocalDateFromSymmetry454Date(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(iso, LocalDate.from(sym454), "Conversion to LocalDate failed");
    }

    // Test conversion from LocalDate to Symmetry454Date
    @ParameterizedTest
    @MethodSource("sampleDateConversions")
    public void testSymmetry454DateFromLocalDate(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(sym454, Symmetry454Date.from(iso), "Conversion to Symmetry454Date failed");
    }

    // Test dateEpochDay conversion
    @ParameterizedTest
    @MethodSource("sampleDateConversions")
    public void testSymmetry454DateChronologyDateEpochDay(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(sym454, SYM454_CHRONOLOGY.dateEpochDay(iso.toEpochDay()), "Date epoch day conversion failed");
    }

    // Test toEpochDay conversion
    @ParameterizedTest
    @MethodSource("sampleDateConversions")
    public void testSymmetry454DateToEpochDay(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(iso.toEpochDay(), sym454.toEpochDay(), "Epoch day conversion failed");
    }

    // Test until methods
    @ParameterizedTest
    @MethodSource("sampleDateConversions")
    public void testSymmetry454DateUntilSymmetry454Date(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(SYM454_CHRONOLOGY.period(0, 0, 0), sym454.until(sym454), "Until method failed");
    }

    @ParameterizedTest
    @MethodSource("sampleDateConversions")
    public void testSymmetry454DateUntilLocalDate(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(SYM454_CHRONOLOGY.period(0, 0, 0), sym454.until(iso), "Until method with LocalDate failed");
    }

    @ParameterizedTest
    @MethodSource("sampleDateConversions")
    public void testChronologyDateTemporal(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(sym454, SYM454_CHRONOLOGY.date(iso), "Chronology date conversion failed");
    }

    @ParameterizedTest
    @MethodSource("sampleDateConversions")
    public void testLocalDateUntilSymmetry454Date(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(Period.ZERO, iso.until(sym454), "LocalDate until Symmetry454Date failed");
    }

    // Test plusDays
    @ParameterizedTest
    @MethodSource("sampleDateConversions")
    public void testPlusDays(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(iso, LocalDate.from(sym454.plus(0, DAYS)), "Plus 0 days failed");
        assertEquals(iso.plusDays(1), LocalDate.from(sym454.plus(1, DAYS)), "Plus 1 day failed");
        assertEquals(iso.plusDays(35), LocalDate.from(sym454.plus(35, DAYS)), "Plus 35 days failed");
        assertEquals(iso.plusDays(-1), LocalDate.from(sym454.plus(-1, DAYS)), "Plus -1 day failed");
        assertEquals(iso.plusDays(-60), LocalDate.from(sym454.plus(-60, DAYS)), "Plus -60 days failed");
    }

    // Test minusDays
    @ParameterizedTest
    @MethodSource("sampleDateConversions")
    public void testMinusDays(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(iso, LocalDate.from(sym454.minus(0, DAYS)), "Minus 0 days failed");
        assertEquals(iso.minusDays(1), LocalDate.from(sym454.minus(1, DAYS)), "Minus 1 day failed");
        assertEquals(iso.minusDays(35), LocalDate.from(sym454.minus(35, DAYS)), "Minus 35 days failed");
        assertEquals(iso.minusDays(-1), LocalDate.from(sym454.minus(-1, DAYS)), "Minus -1 day failed");
        assertEquals(iso.minusDays(-60), LocalDate.from(sym454.minus(-60, DAYS)), "Minus -60 days failed");
    }

    // Test until DAYS
    @ParameterizedTest
    @MethodSource("sampleDateConversions")
    public void testUntilDays(Symmetry454Date sym454, LocalDate iso) {
        assertEquals(0, sym454.until(iso.plusDays(0), DAYS), "Until 0 days failed");
        assertEquals(1, sym454.until(iso.plusDays(1), DAYS), "Until 1 day failed");
        assertEquals(35, sym454.until(iso.plusDays(35), DAYS), "Until 35 days failed");
        assertEquals(-40, sym454.until(iso.minusDays(40), DAYS), "Until -40 days failed");
    }

    // Data provider for invalid dates
    public static Object[][] invalidDates() {
        return new Object[][] {
            {-1, 13, 28},
            {-1, 13, 29},
            {2000, -2, 1},
            {2000, 13, 1},
            {2000, 15, 1},
            {2000, 1, -1},
            {2000, 1, 0},
            {2000, 0, 1},
            {2000, -1, 0},
            {2000, -1, 1},
            {2000, 1, 29},
            {2000, 2, 36},
            {2000, 3, 29},
            {2000, 4, 29},
            {2000, 5, 36},
            {2000, 6, 29},
            {2000, 7, 29},
            {2000, 8, 36},
            {2000, 9, 29},
            {2000, 10, 29},
            {2000, 11, 36},
            {2000, 12, 29},
            {2004, 12, 36},
        };
    }

    // Test invalid dates
    @ParameterizedTest
    @MethodSource("invalidDates")
    public void testInvalidDates(int year, int month, int dom) {
        assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom), "Invalid date should throw exception");
    }

    // Data provider for invalid leap dates
    public static Object[][] invalidLeapDates() {
        return new Object[][] {
            {1},
            {100},
            {200},
            {2000}
        };
    }

    // Test invalid leap day dates
    @ParameterizedTest
    @MethodSource("invalidLeapDates")
    public void testInvalidLeapDayDates(int year) {
        assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, 12, 29), "Invalid leap day date should throw exception");
    }

    // Test Chronology.dateYearDay with invalid date
    @Test
    public void testChronologyDateYearDayInvalidDate() {
        assertThrows(DateTimeException.class, () -> SYM454_CHRONOLOGY.dateYearDay(2000, 365), "Invalid dateYearDay should throw exception");
    }

    // Test Symmetry454Date.isLeapYear
    @Test
    public void testIsLeapYearSpecific() {
        assertTrue(SYM454_CHRONOLOGY.isLeapYear(3), "Year 3 should be a leap year");
        assertFalse(SYM454_CHRONOLOGY.isLeapYear(6), "Year 6 should not be a leap year");
        assertTrue(SYM454_CHRONOLOGY.isLeapYear(9), "Year 9 should be a leap year");
        assertFalse(SYM454_CHRONOLOGY.isLeapYear(2000), "Year 2000 should not be a leap year");
        assertTrue(SYM454_CHRONOLOGY.isLeapYear(2004), "Year 2004 should be a leap year");
    }

    // Test Symmetry454Date.isLeapWeek
    @Test
    public void testLeapWeek() {
        assertTrue(Symmetry454Date.of(2015, 12, 29).isLeapWeek(), "Date should be in leap week");
        assertTrue(Symmetry454Date.of(2015, 12, 30).isLeapWeek(), "Date should be in leap week");
        assertTrue(Symmetry454Date.of(2015, 12, 31).isLeapWeek(), "Date should be in leap week");
        assertTrue(Symmetry454Date.of(2015, 12, 32).isLeapWeek(), "Date should be in leap week");
        assertTrue(Symmetry454Date.of(2015, 12, 33).isLeapWeek(), "Date should be in leap week");
        assertTrue(Symmetry454Date.of(2015, 12, 34).isLeapWeek(), "Date should be in leap week");
        assertTrue(Symmetry454Date.of(2015, 12, 35).isLeapWeek(), "Date should be in leap week");
    }

    // Data provider for length of month
    public static Object[][] lengthOfMonthData() {
        return new Object[][] {
            {2000, 1, 28, 28},
            {2000, 2, 28, 35},
            {2000, 3, 28, 28},
            {2000, 4, 28, 28},
            {2000, 5, 28, 35},
            {2000, 6, 28, 28},
            {2000, 7, 28, 28},
            {2000, 8, 28, 35},
            {2000, 9, 28, 28},
            {2000, 10, 28, 28},
            {2000, 11, 28, 35},
            {2000, 12, 28, 28},
            {2004, 12, 20, 35},
        };
    }

    // Test length of month
    @ParameterizedTest
    @MethodSource("lengthOfMonthData")
    public void testLengthOfMonth(int year, int month, int day, int length) {
        assertEquals(length, Symmetry454Date.of(year, month, day).lengthOfMonth(), "Length of month mismatch");
    }

    @ParameterizedTest
    @MethodSource("lengthOfMonthData")
    public void testLengthOfMonthFirst(int year, int month, int day, int length) {
        assertEquals(length, Symmetry454Date.of(year, month, 1).lengthOfMonth(), "Length of month mismatch for first day");
    }

    @Test
    public void testLengthOfMonthSpecific() {
        assertEquals(28, Symmetry454Date.of(2000, 12, 28).lengthOfMonth(), "Length of month mismatch");
        assertEquals(35, Symmetry454Date.of(2004, 12, 28).lengthOfMonth(), "Length of month mismatch");
    }

    // Test era and proleptic year
    @Test
    public void testEraLoop() {
        for (int year = 1; year < 200; year++) {
            Symmetry454Date base = SYM454_CHRONOLOGY.date(year, 1, 1);
            assertEquals(year, base.get(YEAR), "Year mismatch");
            IsoEra era = IsoEra.CE;
            assertEquals(era, base.getEra(), "Era mismatch");
            assertEquals(year, base.get(YEAR_OF_ERA), "Year of era mismatch");
            Symmetry454Date eraBased = SYM454_CHRONOLOGY.date(era, year, 1, 1);
            assertEquals(base, eraBased, "Era-based date mismatch");
        }

        for (int year = -200; year < 0; year++) {
            Symmetry454Date base = SYM454_CHRONOLOGY.date(year, 1, 1);
            assertEquals(year, base.get(YEAR), "Year mismatch");
            IsoEra era = IsoEra.BCE;
            assertEquals(era, base.getEra(), "Era mismatch");
            assertEquals(1 - year, base.get(YEAR_OF_ERA), "Year of era mismatch");
            Symmetry454Date eraBased = SYM454_CHRONOLOGY.date(era, year, 1, 1);
            assertEquals(base, eraBased, "Era-based date mismatch");
        }
    }

    @Test
    public void testEraYearDayLoop() {
        for (int year = 1; year < 200; year++) {
            Symmetry454Date base = SYM454_CHRONOLOGY.dateYearDay(year, 1);
            assertEquals(year, base.get(YEAR), "Year mismatch");
            IsoEra era = IsoEra.CE;
            assertEquals(era, base.getEra(), "Era mismatch");
            assertEquals(year, base.get(YEAR_OF_ERA), "Year of era mismatch");
            Symmetry454Date eraBased = SYM454_CHRONOLOGY.dateYearDay(era, year, 1);
            assertEquals(base, eraBased, "Era-based date mismatch");
        }
    }

    @Test
    public void testProlepticYearSpecific() {
        assertEquals(4, SYM454_CHRONOLOGY.prolepticYear(IsoEra.CE, 4), "Proleptic year mismatch");
        assertEquals(3, SYM454_CHRONOLOGY.prolepticYear(IsoEra.CE, 3), "Proleptic year mismatch");
        assertEquals(2, SYM454_CHRONOLOGY.prolepticYear(IsoEra.CE, 2), "Proleptic year mismatch");
        assertEquals(1, SYM454_CHRONOLOGY.prolepticYear(IsoEra.CE, 1), "Proleptic year mismatch");
        assertEquals(2000, SYM454_CHRONOLOGY.prolepticYear(IsoEra.CE, 2000), "Proleptic year mismatch");
        assertEquals(1582, SYM454_CHRONOLOGY.prolepticYear(IsoEra.CE, 1582), "Proleptic year mismatch");
    }

    // Data provider for invalid eras
    public static Object[][] invalidEras() {
        return new Era[][] {
            { AccountingEra.BCE },
            { AccountingEra.CE },
            { CopticEra.BEFORE_AM },
            { CopticEra.AM },
            { DiscordianEra.YOLD },
            { EthiopicEra.BEFORE_INCARNATION },
            { EthiopicEra.INCARNATION },
            { HijrahEra.AH },
            { InternationalFixedEra.CE },
            { JapaneseEra.MEIJI },
            { JapaneseEra.TAISHO },
            { JapaneseEra.SHOWA },
            { JapaneseEra.HEISEI },
            { JulianEra.BC },
            { JulianEra.AD },
            { MinguoEra.BEFORE_ROC },
            { MinguoEra.ROC },
            { PaxEra.BCE },
            { PaxEra.CE },
            { ThaiBuddhistEra.BEFORE_BE },
            { ThaiBuddhistEra.BE },
        };
    }

    // Test invalid eras
    @ParameterizedTest
    @MethodSource("invalidEras")
    public void testProlepticYearInvalidEra(Era era) {
        assertThrows(ClassCastException.class, () -> SYM454_CHRONOLOGY.prolepticYear(era, 4), "Invalid era should throw exception");
    }

    // Test Chronology.eraOf
    @Test
    public void testChronologyEraOf() {
        assertEquals(IsoEra.BCE, SYM454_CHRONOLOGY.eraOf(0), "Era mismatch");
        assertEquals(IsoEra.CE, SYM454_CHRONOLOGY.eraOf(1), "Era mismatch");
    }

    // Test Chronology.eraOf with invalid value
    @Test
    public void testChronologyEraOfInvalid() {
        assertThrows(DateTimeException.class, () -> SYM454_CHRONOLOGY.eraOf(2), "Invalid era value should throw exception");
    }

    // Test Chronology.eras
    @Test
    public void testChronologyEras() {
        List<Era> eras = SYM454_CHRONOLOGY.eras();
        assertEquals(2, eras.size(), "Number of eras mismatch");
        assertTrue(eras.contains(IsoEra.BCE), "Eras should contain BCE");
        assertTrue(eras.contains(IsoEra.CE), "Eras should contain CE");
    }

    // Test Chronology.range
    @Test
    public void testChronologyRange() {
        assertEquals(ValueRange.of(1, 7), SYM454_CHRONOLOGY.range(ALIGNED_DAY_OF_WEEK_IN_MONTH), "Range mismatch");
        assertEquals(ValueRange.of(1, 7), SYM454_CHRONOLOGY.range(ALIGNED_DAY_OF_WEEK_IN_YEAR), "Range mismatch");
        assertEquals(ValueRange.of(1, 4, 5), SYM454_CHRONOLOGY.range(ALIGNED_WEEK_OF_MONTH), "Range mismatch");
        assertEquals(ValueRange.of(1, 52, 53), SYM454_CHRONOLOGY.range(ALIGNED_WEEK_OF_YEAR), "Range mismatch");
        assertEquals(ValueRange.of(1, 7), SYM454_CHRONOLOGY.range(DAY_OF_WEEK), "Range mismatch");
        assertEquals(ValueRange.of(1, 28, 35), SYM454_CHRONOLOGY.range(DAY_OF_MONTH), "Range mismatch");
        assertEquals(ValueRange.of(1, 364, 371), SYM454_CHRONOLOGY.range(DAY_OF_YEAR), "Range mismatch");
        assertEquals(ValueRange.of(0, 1), SYM454_CHRONOLOGY.range(ERA), "Range mismatch");
        assertEquals(ValueRange.of(-1_000_000 * 364L - 177_474 * 7 - 719_162, 1_000_000 * 364L + 177_474 * 7 - 719_162), SYM454_CHRONOLOGY.range(EPOCH_DAY), "Range mismatch");
        assertEquals(ValueRange.of(1, 12), SYM454_CHRONOLOGY.range(MONTH_OF_YEAR), "Range mismatch");
        assertEquals(ValueRange.of(-12_000_000L, 11_999_999L), SYM454_CHRONOLOGY.range(PROLEPTIC_MONTH), "Range mismatch");
        assertEquals(ValueRange.of(-1_000_000L, 1_000_000), SYM454_CHRONOLOGY.range(YEAR), "Range mismatch");
        assertEquals(ValueRange.of(-1_000_000, 1_000_000), SYM454_CHRONOLOGY.range(YEAR_OF_ERA), "Range mismatch");
    }

    // Data provider for Symmetry454Date.range
    public static Object[][] dateRanges() {
        return new Object[][] {
            {2012, 1, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 2, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
            {2012, 3, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 4, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 5, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
            {2012, 6, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 7, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 8, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
            {2012, 9, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 10, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2012, 11, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
            {2012, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 28)},
            {2015, 12, 23, DAY_OF_MONTH, ValueRange.of(1, 35)},
            {2012, 1, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
            {2012, 6, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
            {2012, 12, 23, DAY_OF_WEEK, ValueRange.of(1, 7)},
            {2012, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 364)},
            {2015, 1, 23, DAY_OF_YEAR, ValueRange.of(1, 371)},
            {2012, 1, 23, MONTH_OF_YEAR, ValueRange.of(1, 12)},
            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
            {2012, 6, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
            {2012, 12, 23, ALIGNED_DAY_OF_WEEK_IN_MONTH, ValueRange.of(1, 7)},
            {2012, 1, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 4)},
            {2012, 2, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},
            {2015, 12, 23, ALIGNED_WEEK_OF_MONTH, ValueRange.of(1, 5)},
            {2012, 1, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
            {2012, 6, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
            {2012, 12, 23, ALIGNED_DAY_OF_WEEK_IN_YEAR, ValueRange.of(1, 7)},
            {2012, 1, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
            {2012, 6, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
            {2012, 12, 23, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 52)},
            {2015, 12, 30, ALIGNED_WEEK_OF_YEAR, ValueRange.of(1, 53)},
        };
    }

    // Test Symmetry454Date.range
    @ParameterizedTest
    @MethodSource("dateRanges")
    public void testRange(int year, int month, int dom, TemporalField field, ValueRange range) {
        assertEquals(range, Symmetry454Date.of(year, month, dom).range(field), "Range mismatch");
    }

    @Test
    public void testRangeUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry454Date.of(2012, 6, 28).range(MINUTE_OF_DAY), "Unsupported field should throw exception");
    }

    // Data provider for Symmetry454Date.getLong
    public static Object[][] getLongData() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 5},
            {2014, 5, 26, DAY_OF_MONTH, 26},
            {2014, 5, 26, DAY_OF_YEAR, 28 + 35 + 28 + 28 + 26},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 4 + 5 + 4 + 4 + 4},
            {2014, 5, 26, MONTH_OF_YEAR, 5},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1},
            {2014, 5, 26, YEAR, 2014},
            {2014, 5, 26, ERA, 1},
            {1, 5, 8, ERA, 1},
            {2012, 9, 26, DAY_OF_WEEK, 5},
            {2012, 9, 26, DAY_OF_YEAR, 3 * (4 + 5 + 4) * 7 - 2},
            {2012, 9, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5},
            {2012, 9, 26, ALIGNED_WEEK_OF_MONTH, 4},
            {2012, 9, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5},
            {2012, 9, 26, ALIGNED_WEEK_OF_YEAR, 3 * (4 + 5 + 4)},
            {2015, 12, 35, DAY_OF_WEEK, 7},
            {2015, 12, 35, DAY_OF_MONTH, 35},
            {2015, 12, 35, DAY_OF_YEAR, 4 * (4 + 5 + 4) * 7 + 7},
            {2015, 12, 35, ALIGNED_DAY_OF_WEEK_IN_MONTH, 7},
            {2015, 12, 35, ALIGNED_WEEK_OF_MONTH, 5},
            {2015, 12, 35, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7},
            {2015, 12, 35, ALIGNED_WEEK_OF_YEAR, 53},
            {2015, 12, 35, MONTH_OF_YEAR, 12},
            {2015, 12, 35, PROLEPTIC_MONTH, 2016 * 12 - 1},
        };
    }

    // Test Symmetry454Date.getLong
    @ParameterizedTest
    @MethodSource("getLongData")
    public void testGetLong(int year, int month, int dom, TemporalField field, long expected) {
        assertEquals(expected, Symmetry454Date.of(year, month, dom).getLong(field), "GetLong mismatch");
    }

    @Test
    public void testGetLongUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry454Date.of(2012, 6, 28).getLong(MINUTE_OF_DAY), "Unsupported field should throw exception");
    }

    // Data provider for Symmetry454Date.with
    public static Object[][] withData() {
        return new Object[][] {
            {2014, 5, 26, DAY_OF_WEEK, 1, 2014, 5, 22},
            {2014, 5, 26, DAY_OF_WEEK, 5, 2014, 5, 26},
            {2014, 5, 26, DAY_OF_MONTH, 28, 2014, 5, 28},
            {2014, 5, 26, DAY_OF_MONTH, 26, 2014, 5, 26},
            {2014, 5, 26, DAY_OF_YEAR, 364, 2014, 12, 28},
            {2014, 5, 26, DAY_OF_YEAR, 138, 2014, 5, 19},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3, 2014, 5, 24},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5, 2014, 5, 26},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 1, 2014, 5, 5},
            {2014, 5, 26, ALIGNED_WEEK_OF_MONTH, 4, 2014, 5, 26},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2, 2014, 5, 23},
            {2014, 5, 26, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5, 2014, 5, 26},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 23, 2014, 6, 19},
            {2014, 5, 26, ALIGNED_WEEK_OF_YEAR, 20, 2014, 5, 26},
            {2014, 5, 26, MONTH_OF_YEAR, 4, 2014, 4, 26},
            {2014, 5, 26, MONTH_OF_YEAR, 5, 2014, 5, 26},
            {2014, 5, 26, PROLEPTIC_MONTH, 2013 * 12 + 3 - 1, 2013, 3, 26},
            {2014, 5, 26, PROLEPTIC_MONTH, 2014 * 12 + 5 - 1, 2014, 5, 26},
            {2014, 5, 26, YEAR, 2012, 2012, 5, 26},
            {2014, 5, 26, YEAR, 2014, 2014, 5, 26},
            {2014, 5, 26, YEAR_OF_ERA, 2012, 2012, 5, 26},
            {2014, 5, 26, YEAR_OF_ERA, 2014, 2014, 5, 26},
            {2014, 5, 26, ERA, 1, 2014, 5, 26},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 1, 2015, 12, 22},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 2, 2015, 12, 23},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 3, 2015, 12, 24},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 4, 2015, 12, 25},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 5, 2015, 12, 26},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 6, 2015, 12, 27},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_MONTH, 7, 2015, 12, 28},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 1, 2015, 12, 22},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 2, 2015, 12, 23},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 3, 2015, 12, 24},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 4, 2015, 12, 25},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 5, 2015, 12, 26},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 6, 2015, 12, 27},
            {2015, 12, 22, ALIGNED_DAY_OF_WEEK_IN_YEAR, 7, 2015, 12, 28},
            {2015, 12, 29, ALIGNED_WEEK_OF_MONTH, 0, 2015, 12, 29},
            {2015, 12, 29, ALIGNED_WEEK_OF_MONTH, 3, 2015, 12, 15},
            {2015, 12, 29, ALIGNED_WEEK_OF_YEAR, 0, 2015, 12, 29},
            {2015, 12, 29, ALIGNED_WEEK_OF_YEAR, 3, 2015, 1, 15},
            {2015, 12, 29, DAY_OF_WEEK, 0, 2015, 12, 29},
            {2015, 12, 28, DAY_OF_WEEK, 1, 2015, 12, 22},
            {2015, 12, 28, DAY_OF_WEEK, 2, 2015, 12, 23},
            {2015, 12, 28, DAY_OF_WEEK, 3, 2015, 12, 24},
            {2015, 12, 28, DAY_OF_WEEK, 4, 2015, 12, 25},
            {2015, 12, 28, DAY_OF_WEEK, 5, 2015, 12, 26},
            {2015, 12, 28, DAY_OF_WEEK, 6, 2015, 12, 27},
            {2015, 12, 28, DAY_OF_WEEK, 7, 2015, 12, 28},
            {2015, 12, 29, DAY_OF_MONTH, 1, 2015, 12, 1},
            {2015, 12, 29, DAY_OF_MONTH, 3, 2015, 12, 3},
            {2015, 12, 29, MONTH_OF_YEAR, 1, 2015, 1, 28},
            {2015, 12, 29, MONTH_OF_YEAR, 12, 2015, 12, 29},
            {2015, 12, 29, MONTH_OF_YEAR, 2, 2015, 2, 29},
            {2015, 12, 29, YEAR, 2014, 2014, 12, 28},
            {2015, 12, 29, YEAR, 2013, 2013, 12, 28},
            {2014, 3, 28, DAY_OF_MONTH, 1, 2014, 3, 1},
            {2014, 1, 28, DAY_OF_MONTH, 1, 2014, 1, 1},
            {2014, 3, 28, MONTH_OF_YEAR, 1, 2014, 1, 28},
            {2015, 3, 28, DAY_OF_YEAR, 371, 2015, 12, 35},
            {2012, 3, 28, DAY_OF_YEAR, 364, 2012, 12, 28},
        };
    }

    // Test Symmetry454Date.with
    @ParameterizedTest
    @MethodSource("withData")
    public void testWithTemporalField(int year, int month, int dom,
            TemporalField field, long value,
            int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(Symmetry454Date.of(expectedYear, expectedMonth, expectedDom), Symmetry454Date.of(year, month, dom).with(field, value), "With method mismatch");
    }

    // Data provider for invalid with values
    public static Object[][] withInvalidData() {
        return new Object[][] {
            {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, -1},
            {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_MONTH, 8},
            {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_YEAR, -1},
            {2013, 1, 1, ALIGNED_DAY_OF_WEEK_IN_YEAR, 8},
            {2013, 1, 1, ALIGNED_WEEK_OF_MONTH, -1},
            {2013, 1, 1, ALIGNED_WEEK_OF_MONTH, 5},
            {2013, 2, 1, ALIGNED_WEEK_OF_MONTH, 6},
            {2013, 1, 1, ALIGNED_WEEK_OF_YEAR, -1},
            {2013, 1, 1, ALIGNED_WEEK_OF_YEAR, 53},
            {2015, 1, 1, ALIGNED_WEEK_OF_YEAR, 54},
            {2013, 1, 1, DAY_OF_WEEK, -1},
            {2013, 1, 1, DAY_OF_WEEK, 8},
            {2013, 1, 1, DAY_OF_MONTH, -1},
            {2013, 1, 1, DAY_OF_MONTH, 29},
            {2013, 6, 1, DAY_OF_MONTH, 29},
            {2013, 12, 1, DAY_OF_MONTH, 30},
            {2015, 12, 1, DAY_OF_MONTH, 36},
            {2013, 1, 1, DAY_OF_YEAR, -1},
            {2013, 1, 1, DAY_OF_YEAR, 365},
            {2015, 1, 1, DAY_OF_YEAR, 372},
            {2013, 1, 1, MONTH_OF_YEAR, -1},
            {2013, 1, 1, MONTH_OF_YEAR, 14},
            {2013, 1, 1, MONTH_OF_YEAR, -2},
            {2013, 1, 1, MONTH_OF_YEAR, 14},
            {2013, 1, 1, EPOCH_DAY, -365_961_481},
            {2013, 1, 1, EPOCH_DAY,  364_523_156},
            {2013, 1, 1, YEAR, -1_000_001},
            {2013, 1, 1, YEAR,  1_000_001},
        };
    }

    // Test invalid with values
    @ParameterizedTest
    @MethodSource("withInvalidData")
    public void testWithTemporalFieldInvalidValue(int year, int month, int dom, TemporalField field, long value) {
        assertThrows(DateTimeException.class, () -> Symmetry454Date.of(year, month, dom).with(field, value), "Invalid with value should throw exception");
    }

    @Test
    public void testWithTemporalFieldUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry454Date.of(2012, 6, 28).with(MINUTE_OF_DAY, 10), "Unsupported field should throw exception");
    }

    // Data provider for TemporalAdjusters.lastDayOfMonth
    public static Object[][] lastDayOfMonthData() {
        return new Object[][] {
            {2012, 1, 23, 2012, 1, 28},
            {2012, 2, 23, 2012, 2, 35},
            {2012, 3, 23, 2012, 3, 28},
            {2012, 4, 23, 2012, 4, 28},
            {2012, 5, 23, 2012, 5, 35},
            {2012, 6, 23, 2012, 6, 28},
            {2012, 7, 23, 2012, 7, 28},
            {2012, 8, 23, 2012, 8, 35},
            {2012, 9, 23, 2012, 9, 28},
            {2012, 10, 23, 2012,10, 28},
            {2012, 11, 23, 2012, 11, 35},
            {2012, 12, 23, 2012, 12, 28},
            {2009, 12, 23, 2009, 12, 35},
        };
    }

    // Test TemporalAdjusters.lastDayOfMonth
    @ParameterizedTest
    @MethodSource("lastDayOfMonthData")
    public void testTemporalAdjustersLastDayOfMonth(int year, int month, int day, int expectedYear, int expectedMonth, int expectedDay) {
        Symmetry454Date base = Symmetry454Date.of(year, month, day);
        Symmetry454Date expected = Symmetry454Date.of(expectedYear, expectedMonth, expectedDay);
        Symmetry454Date actual = base.with(TemporalAdjusters.lastDayOfMonth());
        assertEquals(expected, actual, "Last day of month mismatch");
    }

    // Test adjust to LocalDate
    @Test
    public void testAdjustToLocalDate() {
        Symmetry454Date sym454 = Symmetry454Date.of(2000, 1, 4);
        Symmetry454Date test = sym454.with(LocalDate.of(2012, 7, 6));
        assertEquals(Symmetry454Date.of(2012, 7, 5), test, "Adjust to LocalDate mismatch");
    }

    // Test adjust to Month
    @Test
    public void testAdjustToMonth() {
        Symmetry454Date sym454 = Symmetry454Date.of(2000, 1, 4);
        assertThrows(DateTimeException.class, () -> sym454.with(Month.APRIL), "Adjust to Month should throw exception");
    }

    // Test LocalDate adjust to Symmetry454Date
    @Test
    public void testLocalDateAdjustToSymmetry454Date() {
        Symmetry454Date sym454 = Symmetry454Date.of(2012, 7, 19);
        LocalDate test = LocalDate.MIN.with(sym454);
        assertEquals(LocalDate.of(2012, 7, 20), test, "LocalDate adjust to Symmetry454Date mismatch");
    }

    // Test LocalDateTime adjust to Symmetry454Date
    @Test
    public void testLocalDateTimeAdjustToSymmetry454Date() {
        Symmetry454Date sym454 = Symmetry454Date.of(2012, 7, 19);
        LocalDateTime test = LocalDateTime.MIN.with(sym454);
        assertEquals(LocalDateTime.of(2012, 7, 20, 0, 0), test, "LocalDateTime adjust to Symmetry454Date mismatch");
    }

    // Data provider for plus and minus operations
    public static Object[][] plusData() {
        return new Object[][] {
            {2014, 5, 26, 0, DAYS, 2014, 5, 26},
            {2014, 5, 26, 8, DAYS, 2014, 5, 34},
            {2014, 5, 26, -3, DAYS, 2014, 5, 23},
            {2014, 5, 26, 0, WEEKS, 2014, 5, 26},
            {2014, 5, 26, 3, WEEKS, 2014, 6, 12},
            {2014, 5, 26, -5, WEEKS, 2014, 4, 19},
            {2014, 5, 26, 0, MONTHS, 2014, 5, 26},
            {2014, 5, 26, 3, MONTHS, 2014, 8, 26},
            {2014, 5, 26, -5, MONTHS, 2013, 12, 26},
            {2014, 5, 26, 0, YEARS, 2014, 5, 26},
            {2014, 5, 26, 3, YEARS, 2017, 5, 26},
            {2014, 5, 26, -5, YEARS, 2009, 5, 26},
            {2014, 5, 26, 0, DECADES, 2014, 5, 26},
            {2014, 5, 26, 3, DECADES, 2044, 5, 26},
            {2014, 5, 26, -5, DECADES, 1964, 5, 26},
            {2014, 5, 26, 0, CENTURIES, 2014, 5, 26},
            {2014, 5, 26, 3, CENTURIES, 2314, 5, 26},
            {2014, 5, 26, -5, CENTURIES, 1514, 5, 26},
            {2014, 5, 26, 0, MILLENNIA, 2014, 5, 26},
            {2014, 5, 26, 3, MILLENNIA, 5014, 5, 26},
            {2014, 5, 26, -1, MILLENNIA, 2014 - 1000, 5, 26},
            {2014, 12, 26, 3, WEEKS, 2015, 1, 19},
            {2014, 1, 26, -5, WEEKS, 2013, 12, 19},
            {2012, 6, 26, 3, WEEKS, 2012, 7, 19},
            {2012, 7, 26, -5, WEEKS, 2012, 6, 19},
            {2012, 6, 21, 52 + 1, WEEKS, 2013, 6, 28},
            {2013, 6, 21, 6 * 52 + 1, WEEKS, 2019, 6, 21},
        };
    }

    // Data provider for plus and minus operations with leap week
    public static Object[][] plusLeapWeekData() {
        return new Object[][] {
            {2015, 12, 28, 0, DAYS, 2015, 12, 28},
            {2015, 12, 28, 8, DAYS, 2016, 1, 1},
            {2015, 12, 28, -3, DAYS, 2015, 12, 25},
            {2015, 12, 28, 0, WEEKS, 2015, 12, 28},
            {2015, 12, 28, 3, WEEKS, 2016, 1, 14},
            {2015, 12, 28, -5, WEEKS, 2015, 11, 28},
            {2015, 12, 28, 52, WEEKS, 2016, 12, 21},
            {2015, 12, 28, 0, MONTHS, 2015, 12, 28},
            {2015, 12, 28, 3, MONTHS, 2016, 3, 28},
            {2015, 12, 28, -5, MONTHS, 2015, 7, 28},
            {2015, 12, 28, 12, MONTHS, 2016, 12, 28},
            {2015, 12, 28, 0, YEARS, 2015, 12, 28},
            {2015, 12, 28, 3, YEARS, 2018, 12, 28},
            {2015, 12, 28, -5, YEARS, 2010, 12, 28},
        };
    }

    // Test plus TemporalUnit
    @ParameterizedTest
    @MethodSource("plusData")
    public void testPlusTemporalUnit(int year, int month, int dom,
            long amount, TemporalUnit unit,
            int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(Symmetry454Date.of(expectedYear, expectedMonth, expectedDom), Symmetry454Date.of(year, month, dom).plus(amount, unit), "Plus operation mismatch");
    }

    // Test plus TemporalUnit with leap week
    @ParameterizedTest
    @MethodSource("plusLeapWeekData")
    public void testPlusLeapWeekTemporalUnit(int year, int month, int dom,
            long amount, TemporalUnit unit,
            int expectedYear, int expectedMonth, int expectedDom) {
        assertEquals(Symmetry454Date.of(expectedYear, expectedMonth, expectedDom), Symmetry454Date.of(year, month, dom).plus(amount, unit), "Plus operation with leap week mismatch");
    }

    // Test minus TemporalUnit
    @ParameterizedTest
    @MethodSource("plusData")
    public void testMinusTemporalUnit(
            int expectedYear, int expectedMonth, int expectedDom,
            long amount, TemporalUnit unit,
            int year, int month, int dom) {
        assertEquals(Symmetry454Date.of(expectedYear, expectedMonth, expectedDom), Symmetry454Date.of(year, month, dom).minus(amount, unit), "Minus operation mismatch");
    }

    // Test minus TemporalUnit with leap week
    @ParameterizedTest
    @MethodSource("plusLeapWeekData")
    public void testMinusLeapWeekTemporalUnit(
            int expectedYear, int expectedMonth, int expectedDom,
            long amount, TemporalUnit unit,
            int year, int month, int dom) {
        assertEquals(Symmetry454Date.of(expectedYear, expectedMonth, expectedDom), Symmetry454Date.of(year, month, dom).minus(amount, unit), "Minus operation with leap week mismatch");
    }

    @Test
    public void testPlusTemporalUnitUnsupported() {
        assertThrows(UnsupportedTemporalTypeException.class, () -> Symmetry454Date.of(2012, 6, 28).plus(0, MINUTES), "Unsupported unit should throw exception");
    }

    // Data provider for until operations
    public static Object[][] untilData() {
        return new Object[][] {
            {2014, 5, 26, 2014, 5, 26, DAYS, 0},
            {2014, 5, 26, 2014, 6, 4, DAYS, 13},
            {2014, 5, 26, 2014, 5, 20, DAYS, -6},
            {2014, 5, 26, 2014, 5, 26, WEEKS, 0},
            {2014, 5, 26, 2014, 6, 4, WEEKS, 0},
            {2014, 5, 26, 2014, 6, 5, WEEKS, 1},
            {2014, 5, 26, 2014, 5, 26, MONTHS, 0},
            {2014, 5, 26, 2014, 6, 25, MONTHS, 0},
            {2014, 5, 26, 2014, 6, 26, MONTHS, 1},
            {2014, 5, 26, 2014, 5, 26, YEARS, 0},
            {2014, 5, 26, 2015, 5, 25, YEARS, 0},
            {2014, 5, 26, 2015, 5, 26, YEARS, 1},
            {2014, 5, 26, 2014, 5, 26, DECADES, 0},
            {2014, 5, 26, 2024, 5, 25, DECADES, 0},
            {2014, 5, 26, 2024, 5, 26, DECADES, 1},
            {2014, 5, 26, 2014, 5, 26, CENTURIES, 0},
            {2014, 5, 26, 2114, 5, 25, CENTURIES, 0},
            {2014, 5, 26, 2114, 5, 26, CENTURIES, 1},
            {2014, 5, 26, 2014, 5, 26, MILLENNIA, 0},
            {2014, 5, 26, 3014, 5, 25, MILLENNIA, 0},
            {2014, 5, 26, 3014, 5, 26, MILLENNIA, 1},
            {2014, 5, 26, 3014, 5, 26, ERAS, 0},
        };
    }

    // Data provider for until period operations
    public static Object[][] untilPeriodData() {
        return new Object[][] {
            {2014, 5, 26, 2014, 5, 26, 0, 0, 0},
            {2014, 5, 26, 2014, 6, 4, 0, 0, 13},
            {2014, 5, 26, 2014, 5, 20, 0, 0, -6},
            {2014, 5, 26, 2014, 6, 5, 0, 0, 14},
            {2014, 5, 26, 2014, 6, 25, 0, 0, 34},
            {2014, 5, 26, 2014, 6, 26, 0, 1, 0},
            {2014, 5, 26, 2015, 5, 25, 0, 11, 27},
            {2014, 5, 26, 2015, 5, 26, 1, 0, 0},
            {2014, 5, 26, 2024, 5, 25, 9, 11, 27},
        };
    }

    // Test until TemporalUnit
    @ParameterizedTest
    @MethodSource("untilData")
    public void testUntilTemporalUnit(
            int year1, int month1, int dom1,
            int year2, int month2, int dom2,
            TemporalUnit unit, long expected) {
        Symmetry454Date start = Symmetry454Date.of(year1, month1, dom1);
        Symmetry454Date end = Symmetry454Date.of(year2, month2, dom2);
        assertEquals(expected, start.until(end, unit), "Until operation mismatch");
    }

    // Test until end
    @ParameterizedTest
    @MethodSource("untilPeriodData")
    public void testUntilEnd(
            int year1, int month1, int dom1,
            int year2, int month2, int dom2,
            int yearPeriod, int monthPeriod, int dayPeriod) {
        Symmetry454Date start = Symmetry454Date.of(year1, month1, dom1);
        Symmetry454Date end = Symmetry454Date.of(year2, month2, dom2);
        ChronoPeriod period = SYM454_CHRONOLOGY.period(yearPeriod, monthPeriod, dayPeriod);
        assertEquals(period, start.until(end), "Until end mismatch");
    }

    @Test
    public void testUntilTemporalUnitUnsupported() {
        Symmetry454Date start = Symmetry454Date.of(2012, 6, 28);
        Symmetry454Date end = Symmetry454Date.of(2012, 7, 1);
        assertThrows(UnsupportedTemporalTypeException.class, () -> start.until(end, MINUTES), "Unsupported unit should throw exception");
    }

    // Test plus Period
    @Test
    public void testPlusPeriod() {
        assertEquals(Symmetry454Date.of(2014, 8, 1),
                Symmetry454Date.of(2014, 5, 21).plus(SYM454_CHRONOLOGY.period(0, 2, 8)), "Plus period mismatch");
    }

    // Test plus Period with ISO
    @Test
    public void testPlusPeriodISO() {
        assertThrows(DateTimeException.class, () -> Symmetry454Date.of(2014, 5, 26).plus(Period.ofMonths(2)), "Plus ISO period should throw exception");
    }

    // Test minus Period
    @Test
    public void testMinusPeriod() {
        assertEquals(Symmetry454Date.of(2014, 3, 23),
                Symmetry454Date.of(2014, 5, 26).minus(SYM454_CHRONOLOGY.period(0, 2, 3)), "Minus period mismatch");
    }

    // Test minus Period with ISO
    @Test
    public void testMinusPeriodISO() {
        assertThrows(DateTimeException.class, () -> Symmetry454Date.of(2014, 5, 26).minus(Period.ofMonths(2)), "Minus ISO period should throw exception");
    }

    // Test equals and hashCode
    @Test
    public void testEqualsAndHashCode() {
        new EqualsTester()
            .addEqualityGroup(Symmetry454Date.of(2000,  1,  3), Symmetry454Date.of(2000,  1,  3))
            .addEqualityGroup(Symmetry454Date.of(2000,  1,  4), Symmetry454Date.of(2000,  1,  4))
            .addEqualityGroup(Symmetry454Date.of(2000,  2,  3), Symmetry454Date.of(2000,  2,  3))
            .addEqualityGroup(Symmetry454Date.of(2001,  1,  3), Symmetry454Date.of(2001,  1,  3))
            .addEqualityGroup(Symmetry454Date.of(2000, 12, 28), Symmetry454Date.of(2000, 12, 28))
            .addEqualityGroup(Symmetry454Date.of(2000, 12, 25), Symmetry454Date.of(2000, 12, 25))
            .addEqualityGroup(Symmetry454Date.of(2001,  1,  1), Symmetry454Date.of(2001,  1,  1))
            .addEqualityGroup(Symmetry454Date.of(2001, 12, 28), Symmetry454Date.of(2001, 12, 28))
            .addEqualityGroup(Symmetry454Date.of(2000,  6, 28), Symmetry454Date.of(2000,  6, 28))
            .addEqualityGroup(Symmetry454Date.of(2000,  6, 23), Symmetry454Date.of(2000,  6, 23))
            .addEqualityGroup(Symmetry454Date.of(2000,  7,  1), Symmetry454Date.of(2000,  7,  1))
            .addEqualityGroup(Symmetry454Date.of(2004,  6, 28), Symmetry454Date.of(2004,  6, 28))
            .testEquals();
    }

    // Data provider for toString
    public static Object[][] toStringData() {
        return new Object[][] {
            {Symmetry454Date.of(1, 1, 1), "Sym454 CE 1/01/01"},
            {Symmetry454Date.of(1970, 2, 35), "Sym454 CE 1970/02/35"},
            {Symmetry454Date.of(2000, 8, 35), "Sym454 CE 2000/08/35"},
            {Symmetry454Date.of(1970, 12, 35), "Sym454 CE 1970/12/35"},
        };
    }

    // Test toString
    @ParameterizedTest
    @MethodSource("toStringData")
    public void testToString(Symmetry454Date date, String expected) {
        assertEquals(expected, date.toString(), "ToString mismatch");
    }
}