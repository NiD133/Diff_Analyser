/*
 * Test suite for GregorianChronology class
 * Tests the Gregorian calendar system implementation including leap years,
 * date calculations, and chronology instance creation.
 */

package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.TimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.joda.time.chrono.AssembledChronology;
import org.joda.time.chrono.GregorianChronology;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class GregorianChronology_ESTest extends GregorianChronology_ESTest_scaffolding {

    // ========== Date Calculation Tests ==========
    
    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYear_Year20() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        
        long firstDayMillis = chronology.calculateFirstDayOfYearMillis(20);
        
        assertEquals("First day of year 20 CE should be -61536067200000L milliseconds from epoch", 
                     -61536067200000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYear_EpochYear1970() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstance();
        
        long firstDayMillis = chronology.calculateFirstDayOfYearMillis(1970);
        
        assertEquals("First day of epoch year 1970 should be 0 milliseconds", 
                     0L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYear_Year0() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstance();
        
        long firstDayMillis = chronology.calculateFirstDayOfYearMillis(0);
        
        assertEquals("First day of year 0 should be -62167219200000L milliseconds from epoch", 
                     -62167219200000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYear_FutureYear3000() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstance();
        
        long firstDayMillis = chronology.calculateFirstDayOfYearMillis(3000);
        
        assertEquals("First day of year 3000 should be 32503680000000L milliseconds from epoch", 
                     32503680000000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYear_MinimumSupportedYear() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        
        long firstDayMillis = chronology.calculateFirstDayOfYearMillis(-292275054);
        
        assertEquals("First day of minimum supported year should be -9223372017043200000L", 
                     -9223372017043200000L, firstDayMillis);
    }

    // ========== Leap Year Tests ==========
    
    @Test(timeout = 4000)
    public void testIsLeapYear_Year0_ShouldBeLeap() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstance(DateTimeZone.getDefault());
        
        boolean isLeap = chronology.isLeapYear(0);
        
        assertTrue("Year 0 should be a leap year in Gregorian calendar", isLeap);
    }

    @Test(timeout = 4000)
    public void testIsLeapYear_Year900BCE_ShouldNotBeLeap() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstance();
        
        boolean isLeap = chronology.isLeapYear(-900);
        
        assertFalse("Year 900 BCE should not be a leap year", isLeap);
    }

    // ========== Chronology Assembly Tests ==========
    
    @Test(timeout = 4000)
    public void testAssemble_WithValidFields_ShouldSucceed() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstance();
        AssembledChronology.Fields fields = new AssembledChronology.Fields();
        
        // Should not throw exception
        chronology.assemble(fields);
    }

    @Test(timeout = 4000)
    public void testAssemble_WithNullFields_ShouldThrowNullPointerException() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstance();
        
        try {
            chronology.assemble(null);
            fail("Expected NullPointerException when assembling with null fields");
        } catch(NullPointerException e) {
            verifyException("org.joda.time.chrono.BasicChronology", e);
        }
    }

    // ========== Average Time Calculations Tests ==========
    
    @Test(timeout = 4000)
    public void testGetAverageMillisPerYearDividedByTwo() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstance();
        
        long avgMillisPerYearDivTwo = chronology.getAverageMillisPerYearDividedByTwo();
        
        assertEquals("Average milliseconds per year divided by two should be 15778476000L", 
                     15778476000L, avgMillisPerYearDivTwo);
    }

    @Test(timeout = 4000)
    public void testGetApproxMillisAtEpochDividedByTwo() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstance();
        
        long approxMillisAtEpochDivTwo = chronology.getApproxMillisAtEpochDividedByTwo();
        
        assertEquals("Approximate milliseconds at epoch divided by two should be 31083597720000L", 
                     31083597720000L, approxMillisAtEpochDivTwo);
    }

    @Test(timeout = 4000)
    public void testGetAverageMillisPerMonth() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        
        long avgMillisPerMonth = chronology.getAverageMillisPerMonth();
        
        assertEquals("Average milliseconds per month should be 2629746000L", 
                     2629746000L, avgMillisPerMonth);
    }

    @Test(timeout = 4000)
    public void testGetAverageMillisPerYear() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstance();
        
        long avgMillisPerYear = chronology.getAverageMillisPerYear();
        
        assertEquals("Average milliseconds per year should be 31556952000L", 
                     31556952000L, avgMillisPerYear);
    }

    // ========== Year Range Tests ==========
    
    @Test(timeout = 4000)
    public void testGetMaxYear() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstance();
        
        int maxYear = chronology.getMaxYear();
        
        assertEquals("Maximum supported year should be 292278993", 292278993, maxYear);
    }

    @Test(timeout = 4000)
    public void testGetMinYear() throws Throwable {
        TimeZone timeZone = TimeZone.getTimeZone("tySc3*yvDsM"); // Invalid timezone defaults to GMT
        DateTimeZone dateTimeZone = DateTimeZone.forTimeZone(timeZone);
        GregorianChronology chronology = GregorianChronology.getInstance(dateTimeZone, 1);
        
        int minYear = chronology.getMinYear();
        
        assertEquals("Minimum supported year should be -292275054", -292275054, minYear);
    }

    // ========== Instance Creation Tests ==========
    
    @Test(timeout = 4000)
    public void testGetInstance_WithInvalidMinDaysInFirstWeek_ShouldThrowException() throws Throwable {
        DateTimeZone dateTimeZone = DateTimeZone.forOffsetMillis(531);
        
        try {
            GregorianChronology.getInstance(dateTimeZone, 531);
            fail("Expected IllegalArgumentException for invalid minDaysInFirstWeek");
        } catch(IllegalArgumentException e) {
            assertEquals("Invalid min days in first week: 531", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetInstance_WithNullZoneAndInvalidMinDays_ShouldThrowException() throws Throwable {
        try {
            GregorianChronology.getInstance(null, -3203);
            fail("Expected IllegalArgumentException for negative minDaysInFirstWeek");
        } catch(IllegalArgumentException e) {
            assertEquals("Invalid min days in first week: -3203", e.getMessage());
        }
    }

    // ========== Time Zone Conversion Tests ==========
    
    @Test(timeout = 4000)
    public void testWithUTC_ShouldReturnDifferentInstance() throws Throwable {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        GregorianChronology chronology = GregorianChronology.getInstance(defaultZone, 7);
        
        Chronology utcChronology = chronology.withUTC();
        
        assertNotSame("withUTC() should return different instance when original is not UTC", 
                      utcChronology, chronology);
    }

    @Test(timeout = 4000)
    public void testWithZone_WithUTCZone_ShouldReturnSameInstance() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstance();
        DateTimeZone utcZone = DateTimeZone.UTC;
        
        Chronology result = chronology.withZone(utcZone);
        
        assertSame("withZone(UTC) should return same instance when already UTC", 
                   chronology, result);
    }

    @Test(timeout = 4000)
    public void testWithZone_WithNullZone_ShouldReturnSameInstance() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        
        Chronology result = chronology.withZone(null);
        
        assertSame("withZone(null) should return same instance for UTC chronology", 
                   chronology, result);
    }

    // ========== Error Handling Tests ==========
    
    @Test(timeout = 4000)
    public void testGetDaysInYearMonth_WithInvalidValues_ShouldThrowException() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        
        try {
            chronology.getDaysInYearMonth(-2764, -2764);
            fail("Expected ArrayIndexOutOfBoundsException for invalid year/month values");
        } catch(ArrayIndexOutOfBoundsException e) {
            assertEquals("-2765", e.getMessage());
            verifyException("org.joda.time.chrono.BasicGJChronology", e);
        }
    }

    // ========== Integration Tests ==========
    
    @Test(timeout = 4000)
    public void testLocalDateTimeToDateTime_WithTimezoneOffset() throws Throwable {
        GregorianChronology chronology = GregorianChronology.getInstanceUTC();
        LocalDateTime localDateTime = new LocalDateTime(199L, chronology);
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(-3631);
        
        DateTime dateTime = localDateTime.toDateTime(offsetZone);
        
        assertEquals("DateTime conversion should account for timezone offset", 
                     3830L, dateTime.getMillis());
    }
}