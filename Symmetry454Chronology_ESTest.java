package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.HijrahEra;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.temporal.ChronoField;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.List;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.evosuite.runtime.mock.java.time.MockLocalDateTime;
import org.evosuite.runtime.mock.java.time.MockOffsetDateTime;
import org.threeten.extra.chrono.EthiopicDate;
import org.threeten.extra.chrono.PaxDate;

/**
 * Test suite for Symmetry454Chronology class.
 * Tests the functionality of the Symmetry454 calendar system including
 * date creation, validation, leap year calculations, and era handling.
 */
public class Symmetry454ChronologyTest {

    private static final Symmetry454Chronology CHRONOLOGY = Symmetry454Chronology.INSTANCE;

    // ========== Basic Properties Tests ==========
    
    @Test
    public void testGetId_ReturnsCorrectIdentifier() {
        String id = CHRONOLOGY.getId();
        assertEquals("Sym454", id);
    }

    @Test
    public void testGetCalendarType_ReturnsNull() {
        String calendarType = CHRONOLOGY.getCalendarType();
        assertNull("Calendar type should be null as not defined in LDML", calendarType);
    }

    @Test
    public void testEras_ReturnsListOfEras() {
        List<Era> eras = CHRONOLOGY.eras();
        assertNotNull("Eras list should not be null", eras);
        assertFalse("Eras list should not be empty", eras.isEmpty());
    }

    // ========== Era Handling Tests ==========

    @Test
    public void testEraOf_ValidBceEra() {
        IsoEra era = CHRONOLOGY.eraOf(0);
        assertEquals("Era 0 should be BCE", IsoEra.BCE, era);
    }

    @Test
    public void testEraOf_ValidCeEra() {
        IsoEra era = CHRONOLOGY.eraOf(1);
        assertEquals("Era 1 should be CE", IsoEra.CE, era);
    }

    @Test(expected = DateTimeException.class)
    public void testEraOf_InvalidEraValue_ThrowsException() {
        CHRONOLOGY.eraOf(-334);
    }

    // ========== Proleptic Year Tests ==========

    @Test
    public void testProlepticYear_BceEra() {
        int prolepticYear = CHRONOLOGY.prolepticYear(IsoEra.BCE, 1996);
        assertEquals("BCE era should return positive year", 1996, prolepticYear);
    }

    @Test
    public void testProlepticYear_CeEra() {
        int prolepticYear = CHRONOLOGY.prolepticYear(IsoEra.CE, -1390);
        assertEquals("CE era should return the same year", -1390, prolepticYear);
    }

    @Test
    public void testProlepticYear_BceEraWithZero() {
        Era bceEra = CHRONOLOGY.eraOf(0);
        int prolepticYear = CHRONOLOGY.prolepticYear(bceEra, 0);
        assertEquals("BCE era with year 0 should return 0", 0, prolepticYear);
    }

    @Test(expected = DateTimeException.class)
    public void testProlepticYear_InvalidYearOfEra_ThrowsException() {
        CHRONOLOGY.prolepticYear(IsoEra.BCE, -2133538947);
    }

    @Test(expected = ClassCastException.class)
    public void testProlepticYear_InvalidEraType_ThrowsException() {
        CHRONOLOGY.prolepticYear(JapaneseEra.SHOWA, 29);
    }

    // ========== Leap Year Tests ==========

    @Test
    public void testIsLeapYear_LeapYear() {
        boolean isLeap = CHRONOLOGY.isLeapYear(3L);
        assertTrue("Year 3 should be a leap year", isLeap);
    }

    @Test
    public void testIsLeapYear_NonLeapYear() {
        boolean isLeap = CHRONOLOGY.isLeapYear(32L);
        assertFalse("Year 32 should not be a leap year", isLeap);
    }

    @Test
    public void testGetLeapYearsBefore_PositiveYear() {
        long leapYears = Symmetry454Chronology.getLeapYearsBefore(719162L);
        assertEquals("Leap years before 719162 should be 127633", 127633L, leapYears);
    }

    @Test
    public void testGetLeapYearsBefore_NegativeYear() {
        long leapYears = Symmetry454Chronology.getLeapYearsBefore(-60L);
        assertEquals("Leap years before -60 should be -11", -11L, leapYears);
    }

    @Test
    public void testGetLeapYearsBefore_YearZero() {
        long leapYears = Symmetry454Chronology.getLeapYearsBefore(0L);
        assertEquals("Leap years before year 0 should be 0", 0L, leapYears);
    }

    // ========== Date Creation Tests ==========

    @Test
    public void testDate_ValidDateWithEra() {
        Symmetry454Date date = CHRONOLOGY.date(IsoEra.CE, 4, 4, 4);
        assertEquals("Date should have CE era", IsoEra.CE, date.getEra());
    }

    @Test
    public void testDate_ValidDateWithoutEra() {
        Symmetry454Date date = CHRONOLOGY.date(3, 2, 3);
        assertEquals("Date should have CE era by default", IsoEra.CE, date.getEra());
    }

    @Test(expected = DateTimeException.class)
    public void testDate_InvalidMonth_ThrowsException() {
        CHRONOLOGY.date(IsoEra.BCE, 3322, -2136356788, 8);
    }

    @Test(expected = DateTimeException.class)
    public void testDate_InvalidDayOfMonth_ThrowsException() {
        CHRONOLOGY.date(1, 1, 35);
    }

    @Test(expected = DateTimeException.class)
    public void testDate_InvalidMonthRange_ThrowsException() {
        CHRONOLOGY.date(1363, 1363, 1363);
    }

    @Test(expected = ClassCastException.class)
    public void testDate_InvalidEraType_ThrowsException() {
        CHRONOLOGY.date(HijrahEra.AH, -2164, -2164, -2164);
    }

    // ========== Date Year Day Tests ==========

    @Test
    public void testDateYearDay_ValidDateWithEra() {
        Symmetry454Date date = CHRONOLOGY.dateYearDay(IsoEra.CE, 29, 11);
        assertEquals("Date should have CE era", IsoEra.CE, date.getEra());
    }

    @Test
    public void testDateYearDay_ValidDateWithoutEra() {
        Symmetry454Date date = CHRONOLOGY.dateYearDay(19, 19);
        assertEquals("Date should have CE era by default", IsoEra.CE, date.getEra());
    }

    @Test(expected = DateTimeException.class)
    public void testDateYearDay_InvalidDayOfYear_ThrowsException() {
        CHRONOLOGY.dateYearDay(IsoEra.BCE, -313, -313);
    }

    @Test(expected = DateTimeException.class)
    public void testDateYearDay_LeapDayInNonLeapYear_ThrowsException() {
        CHRONOLOGY.dateYearDay(371, 371); // Day 371 in non-leap year
    }

    @Test(expected = DateTimeException.class)
    public void testDateYearDay_LeapDayInNonLeapYearWithEra_ThrowsException() {
        CHRONOLOGY.dateYearDay(IsoEra.BCE, 1145, 371);
    }

    @Test(expected = ClassCastException.class)
    public void testDateYearDay_InvalidEraType_ThrowsException() {
        CHRONOLOGY.dateYearDay(JapaneseEra.SHOWA, -2315, -2315);
    }

    @Test(expected = DateTimeException.class)
    public void testDateYearDay_NegativeDayOfYear_ThrowsException() {
        CHRONOLOGY.dateYearDay(-2501, -2501);
    }

    // ========== Date From Epoch Day Tests ==========

    @Test
    public void testDateEpochDay_ValidEpochDay() {
        Symmetry454Date date = CHRONOLOGY.dateEpochDay(132L);
        assertEquals("Date should have CE era", IsoEra.CE, date.getEra());
    }

    @Test(expected = DateTimeException.class)
    public void testDateEpochDay_EpochDayOutOfRange_ThrowsException() {
        CHRONOLOGY.dateEpochDay(365242134L);
    }

    // ========== Current Date Tests ==========

    @Test
    public void testDateNow_DefaultZone() {
        Symmetry454Date date = CHRONOLOGY.dateNow();
        assertEquals("Current date should have CE era", IsoEra.CE, date.getEra());
    }

    @Test
    public void testDateNow_SpecificZone() {
        ZoneOffset zone = ZoneOffset.MIN;
        Symmetry454Date date = CHRONOLOGY.dateNow(zone);
        assertEquals("Current date should have CE era", IsoEra.CE, date.getEra());
    }

    @Test
    public void testDateNow_WithClock() {
        Clock clock = MockClock.systemDefaultZone();
        Symmetry454Date date = CHRONOLOGY.dateNow(clock);
        assertEquals("Current date should have CE era", IsoEra.CE, date.getEra());
    }

    @Test(expected = NullPointerException.class)
    public void testDateNow_NullZone_ThrowsException() {
        CHRONOLOGY.dateNow((ZoneId) null);
    }

    @Test(expected = NullPointerException.class)
    public void testDateNow_NullClock_ThrowsException() {
        CHRONOLOGY.dateNow((Clock) null);
    }

    // ========== Date From Temporal Accessor Tests ==========

    @Test
    public void testDate_FromTemporalAccessor() {
        ZoneId zoneId = ZoneId.systemDefault();
        Symmetry454Date sourceDate = Symmetry454Date.now(zoneId);
        Symmetry454Date convertedDate = CHRONOLOGY.date(sourceDate);
        assertEquals("Converted date should have CE era", IsoEra.CE, convertedDate.getEra());
    }

    @Test(expected = UnsupportedTemporalTypeException.class)
    public void testDate_FromInvalidTemporalAccessor_ThrowsException() {
        CHRONOLOGY.date(IsoEra.CE);
    }

    @Test(expected = DateTimeException.class)
    public void testDate_FromOutOfRangeTemporalAccessor_ThrowsException() {
        PaxDate paxDate = PaxDate.ofEpochDay(719162L);
        PaxDate outOfRangeDate = paxDate.minusYears(719162L * 100); // Way out of range
        CHRONOLOGY.date(outOfRangeDate);
    }

    @Test(expected = NullPointerException.class)
    public void testDate_NullTemporalAccessor_ThrowsException() {
        CHRONOLOGY.date(null);
    }

    // ========== Local Date Time Tests ==========

    @Test
    public void testLocalDateTime_ValidTemporalAccessor() {
        LocalDateTime localDateTime = MockLocalDateTime.now();
        ChronoLocalDateTime<Symmetry454Date> chronoLocalDateTime = CHRONOLOGY.localDateTime(localDateTime);
        assertNotNull("ChronoLocalDateTime should not be null", chronoLocalDateTime);
    }

    @Test(expected = DateTimeException.class)
    public void testLocalDateTime_IncompatibleTemporalAccessor_ThrowsException() {
        EthiopicDate ethiopicDate = EthiopicDate.ofEpochDay(719162L);
        CHRONOLOGY.localDateTime(ethiopicDate);
    }

    @Test(expected = NullPointerException.class)
    public void testLocalDateTime_NullTemporalAccessor_ThrowsException() {
        CHRONOLOGY.localDateTime(null);
    }

    // ========== Zoned Date Time Tests ==========

    @Test
    public void testZonedDateTime_FromInstant() {
        ZoneOffset zoneOffset = ZoneOffset.UTC;
        Instant instant = MockInstant.ofEpochSecond(3L, 0L);
        ChronoZonedDateTime<Symmetry454Date> zonedDateTime = CHRONOLOGY.zonedDateTime(instant, zoneOffset);
        assertNotNull("ChronoZonedDateTime should not be null", zonedDateTime);
    }

    @Test
    public void testZonedDateTime_FromTemporalAccessor() {
        OffsetDateTime offsetDateTime = MockOffsetDateTime.now();
        ChronoZonedDateTime<Symmetry454Date> zonedDateTime = CHRONOLOGY.zonedDateTime(offsetDateTime);
        assertNotNull("ChronoZonedDateTime should not be null", zonedDateTime);
    }

    @Test(expected = DateTimeException.class)
    public void testZonedDateTime_IncompatibleTemporalAccessor_ThrowsException() {
        CHRONOLOGY.zonedDateTime(JapaneseEra.TAISHO);
    }

    @Test(expected = NullPointerException.class)
    public void testZonedDateTime_NullInstantWithZone_ThrowsException() {
        Instant instant = MockInstant.ofEpochSecond(719162L);
        CHRONOLOGY.zonedDateTime(instant, null);
    }

    @Test(expected = NullPointerException.class)
    public void testZonedDateTime_NullTemporalAccessor_ThrowsException() {
        CHRONOLOGY.zonedDateTime(null);
    }

    // ========== Field Range Tests ==========

    @Test
    public void testRange_SecondOfMinute() {
        ValueRange range = CHRONOLOGY.range(ChronoField.SECOND_OF_MINUTE);
        assertNotNull("Range should not be null", range);
    }

    @Test
    public void testRange_Year() {
        ValueRange range = CHRONOLOGY.range(ChronoField.YEAR);
        assertNotNull("Range should not be null", range);
    }

    @Test
    public void testRange_YearOfEra() {
        ValueRange range = CHRONOLOGY.range(ChronoField.YEAR_OF_ERA);
        assertNotNull("Range should not be null", range);
    }

    @Test
    public void testRange_ProlepticMonth() {
        ValueRange range = CHRONOLOGY.range(ChronoField.PROLEPTIC_MONTH);
        assertNotNull("Range should not be null", range);
    }

    @Test
    public void testRange_Era() {
        ValueRange range = CHRONOLOGY.range(ChronoField.ERA);
        assertNotNull("Range should not be null", range);
    }

    @Test
    public void testRange_EpochDay() {
        ValueRange range = CHRONOLOGY.range(ChronoField.EPOCH_DAY);
        assertNotNull("Range should not be null", range);
    }

    @Test
    public void testRange_DayOfYear() {
        ValueRange range = CHRONOLOGY.range(ChronoField.DAY_OF_YEAR);
        assertNotNull("Range should not be null", range);
    }

    @Test
    public void testRange_DayOfMonth() {
        ValueRange range = CHRONOLOGY.range(ChronoField.DAY_OF_MONTH);
        assertNotNull("Range should not be null", range);
    }

    @Test
    public void testRange_AlignedWeekOfYear() {
        ValueRange range = CHRONOLOGY.range(ChronoField.ALIGNED_WEEK_OF_YEAR);
        assertNotNull("Range should not be null", range);
    }

    @Test
    public void testRange_AlignedWeekOfMonth() {
        ValueRange range = CHRONOLOGY.range(ChronoField.ALIGNED_WEEK_OF_MONTH);
        assertNotNull("Range should not be null", range);
    }

    @Test
    public void testRange_DayOfWeek() {
        ValueRange range = CHRONOLOGY.range(ChronoField.DAY_OF_WEEK);
        assertNotNull("Range should not be null", range);
    }

    @Test
    public void testRange_AlignedDayOfWeekInMonth() {
        ValueRange range = CHRONOLOGY.range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_MONTH);
        assertNotNull("Range should not be null", range);
    }

    @Test
    public void testRange_MonthOfYear() {
        ValueRange range = CHRONOLOGY.range(ChronoField.MONTH_OF_YEAR);
        assertNotNull("Range should not be null", range);
    }

    @Test
    public void testRange_AlignedDayOfWeekInYear() {
        ValueRange range = CHRONOLOGY.range(ChronoField.ALIGNED_DAY_OF_WEEK_IN_YEAR);
        assertNotNull("Range should not be null", range);
    }

    @Test(expected = NullPointerException.class)
    public void testRange_NullField_ThrowsException() {
        CHRONOLOGY.range(null);
    }
}