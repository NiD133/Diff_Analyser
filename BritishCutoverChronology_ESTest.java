package org.threeten.extra.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.time.chrono.ChronoPeriod;
import java.time.chrono.ChronoZonedDateTime;
import java.time.chrono.Era;
import java.time.chrono.IsoEra;
import java.time.chrono.JapaneseEra;
import java.time.chrono.MinguoEra;
import java.time.chrono.ThaiBuddhistEra;
import java.time.format.ResolverStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalField;
import java.time.temporal.ValueRange;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.MockClock;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.evosuite.runtime.mock.java.time.MockZonedDateTime;
import org.junit.runner.RunWith;
import org.threeten.extra.chrono.BritishCutoverChronology;
import org.threeten.extra.chrono.BritishCutoverDate;
import org.threeten.extra.chrono.JulianEra;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class BritishCutoverChronology_ESTest extends BritishCutoverChronology_ESTest_scaffolding {

    // ========== Date Creation Tests ==========
    
    @Test(timeout = 4000)
    public void testCreateDate_WithValidParameters_ShouldSucceed() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        BritishCutoverDate date = chronology.date(12, 6, 12);
        
        assertNotNull("Date should be created successfully", date);
    }
    
    @Test(timeout = 4000)
    public void testCreateDate_WithInvalidMonth_ShouldThrowException() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        JulianEra era = JulianEra.AD;
        
        try { 
            chronology.date(era, 6, 1000, 1); // Invalid month: 1000
            fail("Should throw DateTimeException for invalid month");
        } catch(DateTimeException e) {
            assertTrue("Exception should mention invalid month", 
                      e.getMessage().contains("Invalid value for MonthOfYear"));
        }
    }
    
    @Test(timeout = 4000)
    public void testCreateDate_WithInvalidDay_ShouldThrowException() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        
        try { 
            chronology.date(1737, 1737, 1737); // Invalid month and day
            fail("Should throw DateTimeException for invalid date");
        } catch(DateTimeException e) {
            assertTrue("Exception should mention invalid month", 
                      e.getMessage().contains("Invalid value for MonthOfYear"));
        }
    }
    
    @Test(timeout = 4000)
    public void testCreateDate_WithInvalidSeptemberDate_ShouldThrowException() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        
        try { 
            chronology.date(9, 9, 31); // September 31st doesn't exist
            fail("Should throw DateTimeException for invalid September date");
        } catch(DateTimeException e) {
            assertTrue("Exception should mention invalid date", 
                      e.getMessage().contains("Invalid date 'SEPTEMBER 31'"));
        }
    }

    // ========== Era and Year Tests ==========
    
    @Test(timeout = 4000)
    public void testProlepticYear_WithJulianEraAD_ShouldReturnCorrectYear() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        JulianEra era = JulianEra.AD;
        
        int result = chronology.prolepticYear(era, 13);
        
        assertEquals("AD era should return the same year", 13, result);
    }
    
    @Test(timeout = 4000)
    public void testProlepticYear_WithJulianEraBC_ShouldReturnNegativeYear() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        JulianEra era = JulianEra.BC;
        
        int result = chronology.prolepticYear(era, 1778);
        
        assertEquals("BC era should return negative year", -1777, result);
    }
    
    @Test(timeout = 4000)
    public void testProlepticYear_WithNonJulianEra_ShouldThrowException() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        ThaiBuddhistEra era = ThaiBuddhistEra.BE;
        
        try { 
            chronology.prolepticYear(era, -535);
            fail("Should throw ClassCastException for non-Julian era");
        } catch(ClassCastException e) {
            assertTrue("Exception should mention era type", 
                      e.getMessage().contains("Era must be JulianEra"));
        }
    }

    // ========== Leap Year Tests ==========
    
    @Test(timeout = 4000)
    public void testIsLeapYear_WithNonLeapYear_ShouldReturnFalse() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        boolean isLeap = chronology.isLeapYear(371);
        
        assertFalse("Year 371 should not be a leap year", isLeap);
    }
    
    @Test(timeout = 4000)
    public void testIsLeapYear_WithCutoverYear_ShouldReturnTrue() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        boolean isLeap = chronology.isLeapYear(1752); // Cutover year
        
        assertTrue("Year 1752 (cutover year) should be a leap year", isLeap);
    }
    
    @Test(timeout = 4000)
    public void testIsLeapYear_WithModernLeapYear_ShouldReturnTrue() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        boolean isLeap = chronology.isLeapYear(3676);
        
        assertTrue("Year 3676 should be a leap year", isLeap);
    }

    // ========== Date from Day-of-Year Tests ==========
    
    @Test(timeout = 4000)
    public void testDateYearDay_WithValidParameters_ShouldSucceed() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        JulianEra era = JulianEra.BC;
        
        BritishCutoverDate date = chronology.dateYearDay(era, 5, 5);
        
        assertNotNull("Date should be created from year-day successfully", date);
    }
    
    @Test(timeout = 4000)
    public void testDateYearDay_WithValidYearAndDay_ShouldSucceed() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        BritishCutoverDate date = chronology.dateYearDay(103, 103);
        
        assertNotNull("Date should be created from proleptic year and day", date);
    }
    
    @Test(timeout = 4000)
    public void testDateYearDay_WithInvalidDayOfYear_ShouldThrowException() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        JulianEra era = JulianEra.BC;
        
        try { 
            chronology.dateYearDay(era, 1778, -5738); // Invalid day of year
            fail("Should throw DateTimeException for invalid day of year");
        } catch(DateTimeException e) {
            assertTrue("Exception should mention invalid day of year", 
                      e.getMessage().contains("Invalid value for DayOfYear"));
        }
    }
    
    @Test(timeout = 4000)
    public void testDateYearDay_WithDayExceedingYearLength_ShouldThrowException() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        try { 
            chronology.dateYearDay(874, 874); // Day 874 exceeds year length
            fail("Should throw DateTimeException for day exceeding year length");
        } catch(DateTimeException e) {
            assertTrue("Exception should mention invalid day of year", 
                      e.getMessage().contains("Invalid value for DayOfYear"));
        }
    }
    
    @Test(timeout = 4000)
    public void testDateYearDay_WithInvalidLeapYearDay_ShouldThrowException() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        
        try { 
            chronology.dateYearDay(366, 366); // Day 366 in non-leap year
            fail("Should throw DateTimeException for day 366 in non-leap year");
        } catch(DateTimeException e) {
            assertTrue("Exception should mention leap year issue", 
                      e.getMessage().contains("not a leap year"));
        }
    }

    // ========== Current Date Tests ==========
    
    @Test(timeout = 4000)
    public void testDateNow_WithDefaultZone_ShouldSucceed() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        BritishCutoverDate currentDate = chronology.dateNow();
        
        assertNotNull("Current date should be obtainable", currentDate);
    }
    
    @Test(timeout = 4000)
    public void testDateNow_WithSpecificZone_ShouldSucceed() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        ZoneId zone = ZoneId.systemDefault();
        
        BritishCutoverDate currentDate = chronology.dateNow(zone);
        
        assertNotNull("Current date with specific zone should be obtainable", currentDate);
    }
    
    @Test(timeout = 4000)
    public void testDateNow_WithClock_ShouldSucceed() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        Clock clock = MockClock.systemDefaultZone();
        
        BritishCutoverDate currentDate = chronology.dateNow(clock);
        
        assertNotNull("Current date with clock should be obtainable", currentDate);
    }

    // ========== Epoch Day Tests ==========
    
    @Test(timeout = 4000)
    public void testDateEpochDay_WithValidEpochDay_ShouldSucceed() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        BritishCutoverDate date = chronology.dateEpochDay(-1813L);
        
        assertNotNull("Date should be created from epoch day", date);
    }
    
    @Test(timeout = 4000)
    public void testDateEpochDay_WithExtremeEpochDay_ShouldThrowException() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        
        try { 
            chronology.dateEpochDay(-2135812540L); // Extremely negative epoch day
            fail("Should throw DateTimeException for extreme epoch day");
        } catch(DateTimeException e) {
            assertTrue("Exception should mention invalid year", 
                      e.getMessage().contains("Invalid value for Year"));
        }
    }

    // ========== Temporal Accessor Tests ==========
    
    @Test(timeout = 4000)
    public void testDate_FromTemporalAccessor_ShouldReturnSameInstance() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        BritishCutoverDate originalDate = chronology.dateNow();
        
        BritishCutoverDate convertedDate = chronology.date(originalDate);
        
        assertSame("Converting BritishCutoverDate should return same instance", 
                  originalDate, convertedDate);
    }
    
    @Test(timeout = 4000)
    public void testLocalDateTime_FromZonedDateTime_ShouldSucceed() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        ZonedDateTime zonedDateTime = MockZonedDateTime.now();
        
        ChronoLocalDateTime<BritishCutoverDate> localDateTime = 
            chronology.localDateTime(zonedDateTime);
        
        assertNotNull("Local date-time should be created from zoned date-time", localDateTime);
    }

    // ========== Zoned DateTime Tests ==========
    
    @Test(timeout = 4000)
    public void testZonedDateTime_FromInstant_ShouldSucceed() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        Instant instant = MockInstant.ofEpochSecond(1004L, -1721L);
        ZoneOffset zoneOffset = ZoneOffset.ofTotalSeconds(0);
        
        ChronoZonedDateTime<BritishCutoverDate> zonedDateTime = 
            chronology.zonedDateTime(instant, zoneOffset);
        
        assertNotNull("Zoned date-time should be created from instant", zonedDateTime);
    }
    
    @Test(timeout = 4000)
    public void testZonedDateTime_FromZonedDateTime_ShouldSucceed() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        ZonedDateTime zonedDateTime = MockZonedDateTime.now();
        
        ChronoZonedDateTime<BritishCutoverDate> result = 
            chronology.zonedDateTime(zonedDateTime);
        
        assertNotNull("Zoned date-time should be created from temporal accessor", result);
    }

    // ========== Field Resolution Tests ==========
    
    @Test(timeout = 4000)
    public void testResolveDate_WithEpochDay_ShouldSucceed() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        Map<TemporalField, Long> fieldValues = new HashMap<>();
        fieldValues.put(ChronoField.EPOCH_DAY, 763L);
        
        BritishCutoverDate resolvedDate = 
            chronology.resolveDate(fieldValues, ResolverStyle.STRICT);
        
        assertNotNull("Date should be resolved from epoch day field", resolvedDate);
    }
    
    @Test(timeout = 4000)
    public void testResolveDate_WithEmptyFields_ShouldReturnNull() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        Map<TemporalField, Long> emptyFields = new HashMap<>();
        
        BritishCutoverDate resolvedDate = 
            chronology.resolveDate(emptyFields, ResolverStyle.STRICT);
        
        assertNull("Empty field map should return null", resolvedDate);
    }
    
    @Test(timeout = 4000)
    public void testResolveDate_WithInvalidYearOfEra_ShouldThrowException() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        Map<TemporalField, Long> fieldValues = new HashMap<>();
        fieldValues.put(ChronoField.YEAR_OF_ERA, -4135L); // Invalid negative year of era
        
        try { 
            chronology.resolveDate(fieldValues, ResolverStyle.STRICT);
            fail("Should throw DateTimeException for invalid year of era");
        } catch(DateTimeException e) {
            assertTrue("Exception should mention invalid year of era", 
                      e.getMessage().contains("Invalid value for YearOfEra"));
        }
    }

    // ========== Value Range Tests ==========
    
    @Test(timeout = 4000)
    public void testRange_ForYearOfEra_ShouldReturnValidRange() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        
        ValueRange range = chronology.range(ChronoField.YEAR_OF_ERA);
        
        assertNotNull("Year of era range should be available", range);
    }
    
    @Test(timeout = 4000)
    public void testRange_ForYear_ShouldReturnValidRange() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        
        ValueRange range = chronology.range(ChronoField.YEAR);
        
        assertNotNull("Year range should be available", range);
    }
    
    @Test(timeout = 4000)
    public void testRange_ForProlepticMonth_ShouldReturnValidRange() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        
        ValueRange range = chronology.range(ChronoField.PROLEPTIC_MONTH);
        
        assertNotNull("Proleptic month range should be available", range);
    }
    
    @Test(timeout = 4000)
    public void testRange_ForDayOfYear_ShouldReturnValidRange() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        ValueRange range = chronology.range(ChronoField.DAY_OF_YEAR);
        
        assertNotNull("Day of year range should be available", range);
    }
    
    @Test(timeout = 4000)
    public void testRange_ForAlignedWeekOfYear_ShouldReturnValidRange() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        ValueRange range = chronology.range(ChronoField.ALIGNED_WEEK_OF_YEAR);
        
        assertNotNull("Aligned week of year range should be available", range);
    }
    
    @Test(timeout = 4000)
    public void testRange_ForAlignedWeekOfMonth_ShouldReturnValidRange() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        
        ValueRange range = chronology.range(ChronoField.ALIGNED_WEEK_OF_MONTH);
        
        assertNotNull("Aligned week of month range should be available", range);
    }

    // ========== Era Tests ==========
    
    @Test(timeout = 4000)
    public void testEraOf_WithValidEraValue_ShouldReturnCorrectEra() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        
        JulianEra era = chronology.eraOf(0);
        
        assertEquals("Era value 0 should return BC era", JulianEra.BC, era);
    }
    
    @Test(timeout = 4000)
    public void testEraOf_WithInvalidEraValue_ShouldThrowException() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        try { 
            chronology.eraOf(4); // Invalid era value
            fail("Should throw DateTimeException for invalid era value");
        } catch(DateTimeException e) {
            assertTrue("Exception should mention invalid era", 
                      e.getMessage().contains("Invalid era: 4"));
        }
    }
    
    @Test(timeout = 4000)
    public void testEras_ShouldReturnTwoEras() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        
        List<Era> eras = chronology.eras();
        
        assertEquals("Should have exactly 2 eras", 2, eras.size());
    }

    // ========== Chronology Properties Tests ==========
    
    @Test(timeout = 4000)
    public void testGetId_ShouldReturnCorrectId() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        
        String id = chronology.getId();
        
        assertEquals("Chronology ID should be 'BritishCutover'", "BritishCutover", id);
    }
    
    @Test(timeout = 4000)
    public void testGetCalendarType_ShouldReturnNull() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        
        String calendarType = chronology.getCalendarType();
        
        assertNull("Calendar type should be null", calendarType);
    }
    
    @Test(timeout = 4000)
    public void testGetCutover_ShouldReturnCutoverDate() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        
        LocalDate cutover = chronology.getCutover();
        
        assertNotNull("Cutover date should be available", cutover);
    }

    // ========== Period Operations Tests ==========
    
    @Test(timeout = 4000)
    public void testPeriodOperations_ShouldWorkCorrectly() {
        BritishCutoverChronology chronology = BritishCutoverChronology.INSTANCE;
        ChronoPeriod period = chronology.period(371, 371, 371);
        BritishCutoverDate originalDate = BritishCutoverDate.ofEpochDay(371);
        
        BritishCutoverDate modifiedDate = originalDate.minus(period);
        
        assertFalse("Date after period subtraction should be different", 
                   modifiedDate.equals(originalDate));
    }

    // ========== Error Handling Tests ==========
    
    @Test(timeout = 4000)
    public void testCreateDate_WithWrongEraType_ShouldThrowClassCastException() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        MinguoEra wrongEra = MinguoEra.BEFORE_ROC;
        
        try { 
            chronology.date(wrongEra, 293, 293, 293);
            fail("Should throw ClassCastException for wrong era type");
        } catch(ClassCastException e) {
            assertTrue("Exception should mention era type requirement", 
                      e.getMessage().contains("Era must be JulianEra"));
        }
    }
    
    @Test(timeout = 4000)
    public void testCreateDateYearDay_WithWrongEraType_ShouldThrowClassCastException() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        JapaneseEra wrongEra = JapaneseEra.SHOWA;
        
        try { 
            chronology.dateYearDay(wrongEra, -5181, -5181);
            fail("Should throw ClassCastException for wrong era type");
        } catch(ClassCastException e) {
            assertTrue("Exception should mention era type requirement", 
                      e.getMessage().contains("Era must be JulianEra"));
        }
    }

    // ========== Null Parameter Tests ==========
    
    @Test(timeout = 4000)
    public void testNullParameters_ShouldThrowNullPointerException() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        
        // Test various null parameter scenarios
        assertThrows("Null temporal accessor should throw NPE", 
                    NullPointerException.class, 
                    () -> chronology.date((TemporalAccessor) null));
        
        assertThrows("Null zone should throw NPE", 
                    NullPointerException.class, 
                    () -> chronology.dateNow((ZoneId) null));
        
        assertThrows("Null clock should throw NPE", 
                    NullPointerException.class, 
                    () -> chronology.dateNow((Clock) null));
        
        assertThrows("Null field map should throw NPE", 
                    NullPointerException.class, 
                    () -> chronology.resolveDate(null, ResolverStyle.SMART));
        
        assertThrows("Null chrono field should throw NPE", 
                    NullPointerException.class, 
                    () -> chronology.range(null));
    }

    // ========== Edge Case Tests ==========
    
    @Test(timeout = 4000)
    public void testExtremeClockOffsets_ShouldHandleGracefully() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        Clock baseClock = MockClock.systemDefaultZone();
        
        // Test with extreme duration that causes overflow
        Duration extremeDuration = ChronoUnit.FOREVER.getDuration();
        Clock extremeClock = MockClock.offset(baseClock, extremeDuration);
        
        assertThrows("Extreme clock offset should throw ArithmeticException", 
                    ArithmeticException.class, 
                    () -> chronology.dateNow(extremeClock));
        
        // Test with era duration that exceeds limits
        Duration eraDuration = ChronoUnit.ERAS.getDuration();
        Clock eraClock = MockClock.offset(baseClock, eraDuration);
        
        assertThrows("Era duration offset should throw DateTimeException", 
                    DateTimeException.class, 
                    () -> chronology.dateNow(eraClock));
    }
    
    @Test(timeout = 4000)
    public void testIncompatibleTemporalAccessors_ShouldThrowDateTimeException() {
        BritishCutoverChronology chronology = new BritishCutoverChronology();
        
        // Test with LocalDate (missing time information for ChronoLocalDateTime)
        assertThrows("LocalDate should not convert to ChronoLocalDateTime", 
                    DateTimeException.class, 
                    () -> chronology.localDateTime(chronology.CUTOVER));
        
        // Test with LocalDate (missing zone information for ChronoZonedDateTime)
        assertThrows("LocalDate should not convert to ChronoZonedDateTime", 
                    DateTimeException.class, 
                    () -> chronology.zonedDateTime(chronology.CUTOVER));
        
        // Test with Era (insufficient information for LocalDate)
        IsoEra incompatibleEra = IsoEra.CE;
        assertThrows("Era should not convert to LocalDate", 
                    DateTimeException.class, 
                    () -> chronology.date(incompatibleEra));
    }

    // Helper method for assertThrows (if not available in test framework)
    private void assertThrows(String message, Class<? extends Exception> expectedType, Runnable action) {
        try {
            action.run();
            fail(message + " - Expected exception was not thrown");
        } catch (Exception e) {
            if (!expectedType.isInstance(e)) {
                fail(message + " - Expected " + expectedType.getSimpleName() + 
                     " but got " + e.getClass().getSimpleName());
            }
        }
    }
}