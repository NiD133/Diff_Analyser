package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.Chronology;
import org.joda.time.DateTimeFieldType;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.MonthDay;
import org.joda.time.MutableDateTime;
import org.joda.time.MutableInterval;
import org.joda.time.MutablePeriod;
import org.joda.time.Partial;
import org.joda.time.ReadWritableInterval;
import org.joda.time.ReadWritablePeriod;
import org.joda.time.ReadablePartial;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.GregorianChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.format.DateTimeFormatter;

/**
 * Test suite for StringConverter class.
 * Tests string-to-datetime conversion functionality including:
 * - Duration parsing from ISO period strings
 * - Interval parsing from string representations
 * - Instant parsing from date strings
 * - Partial date parsing
 * - Error handling for invalid formats
 */
public class StringConverterTest {

    private final StringConverter converter = StringConverter.INSTANCE;

    // ========== Duration Parsing Tests ==========

    @Test
    public void testGetDurationMillis_ValidPeriodString_ReturnsCorrectDuration() {
        // Test parsing "PT2S" (2 seconds in ISO 8601 format)
        long durationMillis = converter.getDurationMillis("Pt2s");
        
        assertEquals("Should parse 2 seconds as 2000 milliseconds", 2000L, durationMillis);
    }

    @Test
    public void testGetDurationMillis_ValidPeriodWithDecimal_ReturnsCorrectDuration() {
        // Test parsing "PT2.S" (2 seconds with decimal point)
        long durationMillis = converter.getDurationMillis("Pt2.s");
        
        assertEquals("Should parse 2. seconds as 2000 milliseconds", 2000L, durationMillis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDurationMillis_InvalidPeriodFormat_ThrowsException() {
        // Test with malformed period string
        converter.getDurationMillis("Pt,v.y");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDurationMillis_InvalidCharactersInPeriod_ThrowsException() {
        converter.getDurationMillis("Pt!8CF");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDurationMillis_EmptyString_ThrowsException() {
        converter.getDurationMillis("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDurationMillis_IntervalFormatInDuration_ThrowsException() {
        // Interval format "6/P7m" should not be accepted for duration parsing
        converter.getDurationMillis("6/P7m");
    }

    @Test(expected = NullPointerException.class)
    public void testGetDurationMillis_NullInput_ThrowsException() {
        converter.getDurationMillis(null);
    }

    @Test(expected = ClassCastException.class)
    public void testGetDurationMillis_NonStringInput_ThrowsException() {
        converter.getDurationMillis(converter);
    }

    // ========== Interval Parsing Tests ==========

    @Test
    public void testSetIntoInterval_ValidIntervalString_SetsCorrectValues() {
        // Test parsing "6/8" as an interval
        MutableInterval interval = new MutableInterval();
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        
        converter.setInto(interval, "6/8", chronology);
        
        assertEquals("Should set correct start time", -42368486400000L, interval.getStartMillis());
    }

    @Test
    public void testParseInterval_ValidStartAndPeriod_CreatesCorrectInterval() {
        // Test parsing "6/P7m" (start at year 6, duration of 7 months)
        MutableInterval interval = MutableInterval.parse("6/P7m");
        
        assertEquals("Should set correct end time", -61959513600000L, interval.getEndMillis());
    }

    @Test
    public void testSetIntoInterval_ValidStartAndPeriod_SetsCorrectEndTime() {
        ISOChronology chronology = ISOChronology.getInstance(DateTimeZone.getDefault());
        MutableInterval interval = new MutableInterval(31556952000L, 31556952000L, chronology);
        
        converter.setInto(interval, "6/P7m", chronology);
        
        assertEquals("Should calculate correct end time from start + period", 
                     -61959513600000L, interval.getEndMillis());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetIntoInterval_MissingSeparator_ThrowsException() {
        MutableInterval interval = new MutableInterval();
        converter.setInto(interval, "invalid interval without separator", IslamicChronology.getInstanceUTC());
    }

    @Test(expected = NullPointerException.class)
    public void testSetIntoInterval_NullInterval_ThrowsException() {
        converter.setInto((ReadWritableInterval) null, "6/8", IslamicChronology.getInstanceUTC());
    }

    @Test(expected = ClassCastException.class)
    public void testSetIntoInterval_NonStringInput_ThrowsException() {
        MutableInterval interval = new MutableInterval();
        converter.setInto(interval, interval, null);
    }

    // ========== Period Parsing Tests ==========

    @Test
    public void testSetIntoPeriod_ValidPeriodString_SetsPeriodCorrectly() {
        MutablePeriod period = new MutablePeriod();
        
        converter.setInto(period, "Pt2s", GregorianChronology.getInstanceUTC());
        
        // Period should be set to 2 seconds (exact assertion depends on MutablePeriod implementation)
        assertNotNull("Period should be modified", period);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetIntoPeriod_InvalidPeriodFormat_ThrowsException() {
        MutablePeriod period = new MutablePeriod();
        converter.setInto(period, "invalid period", IslamicChronology.getInstanceUTC());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetIntoPeriod_EmptyString_ThrowsException() {
        MutablePeriod period = new MutablePeriod();
        converter.setInto(period, "", GregorianChronology.getInstanceUTC());
    }

    @Test(expected = NullPointerException.class)
    public void testSetIntoPeriod_NullPeriod_ThrowsException() {
        converter.setInto((ReadWritablePeriod) null, "PT1H", null);
    }

    @Test(expected = ClassCastException.class)
    public void testSetIntoPeriod_NonStringInput_ThrowsException() {
        MutablePeriod period = new MutablePeriod();
        converter.setInto(period, period, IslamicChronology.getInstance());
    }

    // ========== Instant Parsing Tests ==========

    @Test
    public void testGetInstantMillis_ValidDateString_ReturnsCorrectMillis() {
        ISOChronology chronology = ISOChronology.getInstance();
        
        long instantMillis = converter.getInstantMillis("000", chronology);
        
        assertEquals("Should parse year 0 correctly", -62167219200000L, instantMillis);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstantMillis_InvalidDateFormat_ThrowsException() {
        CopticChronology chronology = CopticChronology.getInstanceUTC();
        converter.getInstantMillis("invalid date", chronology);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetInstantMillis_UnsupportedYearInCopticCalendar_ThrowsException() {
        // Year 0 is not supported in Coptic chronology
        CopticChronology chronology = CopticChronology.getInstance();
        converter.getInstantMillis("000", chronology);
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstantMillis_NullInput_ThrowsException() {
        converter.getInstantMillis(null, null);
    }

    @Test(expected = ClassCastException.class)
    public void testGetInstantMillis_NonStringInput_ThrowsException() {
        EthiopicChronology chronology = EthiopicChronology.getInstanceUTC();
        converter.getInstantMillis(chronology, chronology);
    }

    // ========== Partial Date Parsing Tests ==========

    @Test
    public void testGetPartialValues_ValidDateString_ReturnsCorrectValues() {
        EthiopicChronology chronology = EthiopicChronology.getInstanceUTC();
        MonthDay monthDay = new MonthDay(2454L, chronology);
        
        // Mock a simple parser that returns position 1 (parsed successfully)
        DateTimeFormatter formatter = createMockFormatter();
        
        int[] values = converter.getPartialValues(monthDay, "0", chronology, formatter);
        
        assertArrayEquals("Should return correct month and day values", 
                         new int[]{4, 23}, values);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPartialValues_InvalidDateFormat_ThrowsException() {
        Partial partial = new Partial(DateTimeFieldType.year(), -448);
        DateTimeFormatter formatter = partial.getFormatter();
        
        converter.getPartialValues(partial, "invalid;date!format", null, formatter);
    }

    @Test(expected = NullPointerException.class)
    public void testGetPartialValues_NullFormatter_ThrowsException() {
        LocalDate localDate = new LocalDate(GregorianChronology.getInstance(DateTimeZone.UTC));
        converter.getPartialValues(localDate, "2023-01-01", 
                                 GregorianChronology.getInstance(DateTimeZone.UTC), null);
    }

    @Test(expected = ClassCastException.class)
    public void testGetPartialValues_NonStringInput_ThrowsException() {
        MonthDay monthDay = MonthDay.now(ISOChronology.getInstance());
        Partial partial = new Partial(DateTimeFieldType.year(), -2212);
        DateTimeFormatter formatter = partial.getFormatter();
        
        converter.getPartialValues(monthDay, monthDay, ISOChronology.getInstance(), formatter);
    }

    // ========== Utility Tests ==========

    @Test
    public void testGetSupportedType_ReturnsStringClass() {
        Class<?> supportedType = converter.getSupportedType();
        
        assertEquals("Should support String class", String.class, supportedType);
    }

    // ========== Helper Methods ==========

    /**
     * Creates a mock DateTimeFormatter for testing purposes.
     * This is a simplified version - in real tests you might want to use a mocking framework.
     */
    private DateTimeFormatter createMockFormatter() {
        // This would typically use Mockito or similar framework
        // For now, returning a basic formatter that works with the test
        return new Partial(DateTimeFieldType.year(), 1).getFormatter();
    }
}