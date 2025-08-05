package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.OffsetDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.JapaneseEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.time.temporal.ValueRange;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.evosuite.runtime.mock.java.time.MockLocalDate;
import org.evosuite.runtime.mock.java.time.MockLocalDateTime;
import org.evosuite.runtime.mock.java.time.MockOffsetDateTime;
import org.evosuite.runtime.mock.java.time.chrono.MockHijrahDate;
import org.evosuite.runtime.mock.java.time.chrono.MockJapaneseDate;
import org.junit.runner.RunWith;

import org.threeten.extra.chrono.BritishCutoverDate;
import org.threeten.extra.chrono.JulianChronology;
import org.threeten.extra.chrono.JulianDate;
import org.threeten.extra.chrono.JulianEra;
import org.threeten.extra.chrono.PaxDate;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class JulianChronology_ESTest extends JulianChronology_ESTest_scaffolding {

    // ========== Basic Chronology Properties Tests ==========
    
    @Test(timeout = 4000)
    public void testGetId_ReturnsJulian() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        String id = chronology.getId();
        assertEquals("Julian", id);
    }

    @Test(timeout = 4000)
    public void testGetCalendarType_ReturnsJulian() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        String calendarType = chronology.getCalendarType();
        assertEquals("julian", calendarType);
    }

    @Test(timeout = 4000)
    public void testEras_ReturnsNonEmptyList() throws Throwable {
        JulianChronology chronology = JulianChronology.INSTANCE;
        List<Era> eras = chronology.eras();
        assertFalse(eras.isEmpty());
    }

    // ========== Era Tests ==========
    
    @Test(timeout = 4000)
    public void testEraOf_Zero_ReturnsBCEra() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        JulianEra era = chronology.eraOf(0);
        assertEquals(JulianEra.BC, era);
    }

    @Test(timeout = 4000)
    public void testEraOf_InvalidValue_ThrowsException() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        try { 
            chronology.eraOf(3);
            fail("Expected DateTimeException for invalid era value");
        } catch(DateTimeException e) {
            // Expected - invalid era value
        }
    }

    // ========== Leap Year Tests ==========
    
    @Test(timeout = 4000)
    public void testIsLeapYear_DivisibleByFour_ReturnsTrue() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        boolean isLeap = chronology.isLeapYear(-236); // -236 is divisible by 4
        assertTrue(isLeap);
    }

    @Test(timeout = 4000)
    public void testIsLeapYear_NotDivisibleByFour_ReturnsFalse() throws Throwable {
        JulianChronology chronology = JulianChronology.INSTANCE;
        boolean isLeap = chronology.isLeapYear(2110L); // 2110 is not divisible by 4
        assertFalse(isLeap);
    }

    // ========== Proleptic Year Tests ==========
    
    @Test(timeout = 4000)
    public void testProlepticYear_ADEra_ReturnsPositiveYear() throws Throwable {
        JulianChronology chronology = JulianChronology.INSTANCE;
        LocalDate localDate = MockLocalDate.now();
        BritishCutoverDate cutoverDate = new BritishCutoverDate(localDate);
        JulianEra adEra = cutoverDate.getEra(); // Assuming this returns AD era
        
        int prolepticYear = chronology.prolepticYear(adEra, 1850);
        assertEquals(1850, prolepticYear);
    }

    @Test(timeout = 4000)
    public void testProlepticYear_BCEra_ReturnsNegativeYear() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        JulianEra bcEra = JulianEra.BC;
        int prolepticYear = chronology.prolepticYear(bcEra, 981);
        assertEquals(-980, prolepticYear); // BC year 981 = proleptic year -980
    }

    @Test(timeout = 4000)
    public void testProlepticYear_InvalidEraType_ThrowsClassCastException() throws Throwable {
        JulianChronology chronology = JulianChronology.INSTANCE;
        JapaneseEra japaneseEra = JapaneseEra.HEISEI;
        
        try { 
            chronology.prolepticYear(japaneseEra, -1768);
            fail("Expected ClassCastException for non-Julian era");
        } catch(ClassCastException e) {
            assertTrue(e.getMessage().contains("Era must be JulianEra"));
        }
    }

    // ========== Date Creation Tests ==========
    
    @Test(timeout = 4000)
    public void testDate_ValidProlepticYear_CreatesCorrectDate() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        JulianDate date = chronology.date(3, 3, 3);
        ChronoUnit days = ChronoUnit.DAYS;
        JulianDate adjustedDate = date.minus(3L, days);
        assertEquals(JulianEra.AD, adjustedDate.getEra());
    }

    @Test(timeout = 4000)
    public void testDate_WithEra_CreatesCorrectDate() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        JulianEra adEra = JulianEra.AD;
        JulianDate date = chronology.date(adEra, -2282, 9, 2);
        assertEquals(JulianEra.BC, date.getEra()); // Negative year results in BC era
    }

    @Test(timeout = 4000)
    public void testDate_InvalidMonthValue_ThrowsException() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        try { 
            chronology.date(-3115, -3115, -3115);
            fail("Expected DateTimeException for invalid month");
        } catch(DateTimeException e) {
            assertTrue(e.getMessage().contains("Invalid value for MonthOfYear"));
        }
    }

    // ========== Date from Epoch Day Tests ==========
    
    @Test(timeout = 4000)
    public void testDateEpochDay_NegativeValue_CreatesBCDate() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        JulianDate date = chronology.dateEpochDay(-11999976L);
        assertEquals(JulianEra.BC, date.getEra());
    }

    @Test(timeout = 4000)
    public void testDateEpochDay_OutOfRange_ThrowsException() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        try { 
            chronology.dateEpochDay(365250000L); // Very large epoch day
            fail("Expected DateTimeException for out-of-range epoch day");
        } catch(DateTimeException e) {
            assertTrue(e.getMessage().contains("Invalid value for Year"));
        }
    }

    // ========== Year-Day Date Creation Tests ==========
    
    @Test(timeout = 4000)
    public void testDateYearDay_ValidValues_CreatesCorrectDate() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        JulianDate date = chronology.dateYearDay(327, 327);
        assertEquals(JulianEra.AD, date.getEra());
    }

    @Test(timeout = 4000)
    public void testDateYearDay_WithEra_CreatesCorrectDate() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        JulianEra adEra = JulianEra.AD;
        JulianDate date = chronology.dateYearDay(adEra, 84, 84);
        assertEquals(JulianEra.AD, date.getEra());
    }

    @Test(timeout = 4000)
    public void testDateYearDay_InvalidDayOfYear_ThrowsException() throws Throwable {
        JulianChronology chronology = JulianChronology.INSTANCE;
        JulianEra bcEra = JulianEra.BC;
        
        try { 
            chronology.dateYearDay(bcEra, 2493, 1461); // Invalid day of year
            fail("Expected DateTimeException for invalid day of year");
        } catch(DateTimeException e) {
            assertTrue(e.getMessage().contains("Invalid value for DayOfYear"));
        }
    }

    @Test(timeout = 4000)
    public void testDateYearDay_NonLeapYearDay366_ThrowsException() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        try { 
            chronology.dateYearDay(366, 366); // 366 is not a leap year, but trying day 366
            fail("Expected DateTimeException for day 366 in non-leap year");
        } catch(DateTimeException e) {
            assertTrue(e.getMessage().contains("not a leap year"));
        }
    }

    // ========== Current Date Tests ==========
    
    @Test(timeout = 4000)
    public void testDateNow_DefaultClock_ReturnsCurrentDate() throws Throwable {
        JulianChronology chronology = JulianChronology.INSTANCE;
        JulianDate currentDate = chronology.dateNow();
        assertEquals(JulianEra.AD, currentDate.getEra());
    }

    @Test(timeout = 4000)
    public void testDateNow_WithZone_ReturnsCurrentDate() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        ZoneOffset minOffset = ZoneOffset.MIN;
        JulianDate currentDate = chronology.dateNow(minOffset);
        assertEquals(JulianEra.AD, currentDate.getEra());
    }

    @Test(timeout = 4000)
    public void testDateNow_WithClock_ReturnsCurrentDate() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        Clock systemClock = MockClock.systemDefaultZone();
        JulianDate currentDate = chronology.dateNow(systemClock);
        assertEquals(JulianEra.AD, currentDate.getEra());
    }

    // ========== Date Conversion Tests ==========
    
    @Test(timeout = 4000)
    public void testDate_FromJapaneseDate_ConvertsCorrectly() throws Throwable {
        JulianChronology chronology = JulianChronology.INSTANCE;
        JapaneseDate japaneseDate = MockJapaneseDate.now();
        JulianDate convertedDate = chronology.date(japaneseDate);
        assertEquals(JulianEra.AD, convertedDate.getEra());
    }

    @Test(timeout = 4000)
    public void testDate_FromInvalidTemporalAccessor_ThrowsException() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        Month month = Month.APRIL;
        
        try { 
            chronology.date(month);
            fail("Expected UnsupportedTemporalTypeException for Month");
        } catch(UnsupportedTemporalTypeException e) {
            assertTrue(e.getMessage().contains("Unsupported field: EpochDay"));
        }
    }

    // ========== DateTime Creation Tests ==========
    
    @Test(timeout = 4000)
    public void testLocalDateTime_FromLocalDateTime_CreatesCorrectDateTime() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        LocalDateTime localDateTime = MockLocalDateTime.now();
        ChronoLocalDateTime<JulianDate> chronoDateTime = chronology.localDateTime(localDateTime);
        assertNotNull(chronoDateTime);
    }

    @Test(timeout = 4000)
    public void testZonedDateTime_FromInstantAndZone_CreatesCorrectDateTime() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        Instant instant = MockInstant.ofEpochMilli(3L);
        ZoneOffset maxOffset = ZoneOffset.MAX;
        ChronoZonedDateTime<JulianDate> zonedDateTime = chronology.zonedDateTime(instant, maxOffset);
        assertNotNull(zonedDateTime);
    }

    @Test(timeout = 4000)
    public void testZonedDateTime_FromOffsetDateTime_CreatesCorrectDateTime() throws Throwable {
        JulianChronology chronology = JulianChronology.INSTANCE;
        OffsetDateTime offsetDateTime = MockOffsetDateTime.now();
        ChronoZonedDateTime<JulianDate> zonedDateTime = chronology.zonedDateTime(offsetDateTime);
        assertNotNull(zonedDateTime);
    }

    // ========== Field Range Tests ==========
    
    @Test(timeout = 4000)
    public void testRange_ClockHourField_ReturnsValidRange() throws Throwable {
        JulianChronology chronology = JulianChronology.INSTANCE;
        ChronoField clockHourField = ChronoField.CLOCK_HOUR_OF_AMPM;
        ValueRange range = chronology.range(clockHourField);
        assertNotNull(range);
    }

    @Test(timeout = 4000)
    public void testRange_YearOfEraField_ReturnsValidRange() throws Throwable {
        JulianChronology chronology = JulianChronology.INSTANCE;
        ChronoField yearOfEraField = ChronoField.YEAR_OF_ERA;
        ValueRange range = chronology.range(yearOfEraField);
        assertNotNull(range);
    }

    @Test(timeout = 4000)
    public void testRange_YearField_ReturnsValidRange() throws Throwable {
        JulianChronology chronology = JulianChronology.INSTANCE;
        ChronoField yearField = ChronoField.YEAR;
        ValueRange range = chronology.range(yearField);
        assertNotNull(range);
    }

    @Test(timeout = 4000)
    public void testRange_ProlepticMonthField_ReturnsValidRange() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        ChronoField prolepticMonthField = ChronoField.PROLEPTIC_MONTH;
        ValueRange range = chronology.range(prolepticMonthField);
        assertNotNull(range);
    }

    // ========== Date Resolution Tests ==========
    
    @Test(timeout = 4000)
    public void testResolveDate_WithEpochDay_ResolvesCorrectly() throws Throwable {
        JulianDate originalDate = JulianDate.now();
        JulianChronology chronology = originalDate.getChronology();
        
        HashMap<TemporalField, Long> fieldMap = new HashMap<>();
        ChronoField epochDayField = ChronoField.EPOCH_DAY;
        Long epochDayValue = 3L;
        fieldMap.put(epochDayField, epochDayValue);
        
        ResolverStyle lenientStyle = ResolverStyle.LENIENT;
        JulianDate resolvedDate = chronology.resolveDate(fieldMap, lenientStyle);
        
        assertFalse(resolvedDate.equals(originalDate));
    }

    @Test(timeout = 4000)
    public void testResolveDate_EmptyFieldMap_ReturnsNull() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        HashMap<TemporalField, Long> emptyFieldMap = new HashMap<>();
        ResolverStyle strictStyle = ResolverStyle.STRICT;
        
        JulianDate resolvedDate = chronology.resolveDate(emptyFieldMap, strictStyle);
        assertNull(resolvedDate);
    }

    @Test(timeout = 4000)
    public void testResolveDate_InvalidEraValue_ThrowsException() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        HashMap<TemporalField, Long> fieldMap = new HashMap<>();
        ChronoField eraField = ChronoField.ERA;
        Long invalidEraValue = 867L;
        fieldMap.put(eraField, invalidEraValue);
        
        ResolverStyle strictStyle = ResolverStyle.STRICT;
        try { 
            chronology.resolveDate(fieldMap, strictStyle);
            fail("Expected DateTimeException for invalid era value");
        } catch(DateTimeException e) {
            assertTrue(e.getMessage().contains("Invalid value for Era"));
        }
    }

    // ========== Error Handling Tests ==========
    
    @Test(timeout = 4000)
    public void testNullParameterHandling_ThrowsNullPointerException() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        
        // Test various null parameter scenarios
        try { 
            chronology.zonedDateTime((TemporalAccessor) null);
            fail("Expected NullPointerException for null TemporalAccessor");
        } catch(NullPointerException e) {
            // Expected
        }
        
        try { 
            chronology.dateNow((Clock) null);
            fail("Expected NullPointerException for null Clock");
        } catch(NullPointerException e) {
            assertEquals("clock", e.getMessage());
        }
        
        try { 
            chronology.dateNow((ZoneId) null);
            fail("Expected NullPointerException for null ZoneId");
        } catch(NullPointerException e) {
            assertEquals("zone", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testInvalidTemporalAccessor_ThrowsDateTimeException() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        ZoneOffset offset = ZoneOffset.MAX;
        
        try { 
            chronology.zonedDateTime(offset);
            fail("Expected DateTimeException for invalid TemporalAccessor");
        } catch(DateTimeException e) {
            assertTrue(e.getMessage().contains("Unable to obtain ChronoZonedDateTime"));
        }
        
        ThaiBuddhistEra thaiBuddhistEra = ThaiBuddhistEra.BE;
        try { 
            chronology.localDateTime(thaiBuddhistEra);
            fail("Expected DateTimeException for invalid TemporalAccessor");
        } catch(DateTimeException e) {
            assertTrue(e.getMessage().contains("Unable to obtain ChronoLocalDateTime"));
        }
    }

    @Test(timeout = 4000)
    public void testInvalidEraType_ThrowsClassCastException() throws Throwable {
        JulianChronology chronology = JulianChronology.INSTANCE;
        JapaneseEra japaneseEra = JapaneseEra.HEISEI;
        
        try { 
            chronology.dateYearDay(japaneseEra, -401, -401);
            fail("Expected ClassCastException for non-Julian era");
        } catch(ClassCastException e) {
            assertEquals("Era must be JulianEra", e.getMessage());
        }
        
        try { 
            chronology.date(japaneseEra, -1610612735, -1610612735, -1610612735);
            fail("Expected ClassCastException for non-Julian era");
        } catch(ClassCastException e) {
            assertEquals("Era must be JulianEra", e.getMessage());
        }
    }

    // ========== Edge Case Tests ==========
    
    @Test(timeout = 4000)
    public void testExtremeValues_ThrowsAppropriateExceptions() throws Throwable {
        JulianChronology chronology = new JulianChronology();
        
        // Test extreme clock values
        Clock systemClock = MockClock.systemDefaultZone();
        ChronoUnit foreverUnit = ChronoUnit.FOREVER;
        Duration foreverDuration = foreverUnit.getDuration();
        Clock extremeClock = MockClock.offset(systemClock, foreverDuration);
        
        try { 
            chronology.dateNow(extremeClock);
            fail("Expected ArithmeticException for extreme clock offset");
        } catch(ArithmeticException e) {
            assertEquals("long overflow", e.getMessage());
        }
        
        // Test extreme instant values
        Instant extremeInstant = MockInstant.ofEpochSecond(-1L);
        Period largePeriod = Period.ofDays(-1431655764);
        Instant adjustedInstant = MockInstant.plus(extremeInstant, largePeriod);
        ZoneOffset utcOffset = ZoneOffset.UTC;
        
        try { 
            chronology.zonedDateTime(adjustedInstant, utcOffset);
            fail("Expected DateTimeException for extreme instant");
        } catch(DateTimeException e) {
            assertTrue(e.getMessage().contains("Unable to obtain ChronoLocalDateTime"));
        }
    }
}