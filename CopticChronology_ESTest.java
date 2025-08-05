/*
 * Test suite for CopticChronology - Tests the Coptic calendar system implementation
 * 
 * The Coptic calendar is an ancient Egyptian calendar system where:
 * - Year 1 began on August 29, 284 CE (Julian calendar)
 * - Each year has 12 months of 30 days plus a 13th month of 5-6 days
 * - Leap years occur every 4 years (similar to Julian calendar)
 */

package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.AssembledChronology;
import org.joda.time.chrono.CopticChronology;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class CopticChronology_ESTest extends CopticChronology_ESTest_scaffolding {

    // Constants for better readability
    private static final long MINIMUM_SUPPORTED_INSTANT = -9223372036854775768L;
    private static final int MAXIMUM_SUPPORTED_YEAR = 292272708;
    private static final int MINIMUM_SUPPORTED_YEAR = -292269337;
    private static final int VALID_MIN_DAYS_IN_FIRST_WEEK = 1;
    private static final int INVALID_MIN_DAYS_IN_FIRST_WEEK = 634;
    private static final int INVALID_NEGATIVE_MIN_DAYS = -1686;
    private static final long EXPECTED_EPOCH_MILLIS_DIVIDED_BY_TWO = 26607895200000L;

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYear_MaximumYear() throws Throwable {
        // Test calculation of first day of year for the maximum supported year
        CopticChronology chronology = CopticChronology.getInstanceUTC();
        
        long firstDayMillis = chronology.calculateFirstDayOfYearMillis(MAXIMUM_SUPPORTED_YEAR);
        
        assertEquals("First day of maximum year should be calculated correctly", 
                     9223371994233600000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testAssemble_WithDefaultTimeZone() throws Throwable {
        // Test that chronology can be assembled with fields in default timezone
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        CopticChronology chronology = CopticChronology.getInstance(defaultZone);
        AssembledChronology.Fields fields = new AssembledChronology.Fields();
        
        chronology.assemble(fields);
        
        // Verify the AM era constant is correctly set
        assertEquals("AM era constant should be 1", 1, CopticChronology.AM);
    }

    @Test(timeout = 4000)
    public void testGetApproxMillisAtEpochDividedByTwo() throws Throwable {
        // Test retrieval of approximate milliseconds at epoch divided by two
        CopticChronology chronology = CopticChronology.getInstance();
        
        long epochMillis = chronology.getApproxMillisAtEpochDividedByTwo();
        
        assertEquals("Epoch millis divided by two should match expected value", 
                     EXPECTED_EPOCH_MILLIS_DIVIDED_BY_TWO, epochMillis);
    }

    @Test(timeout = 4000)
    public void testIsLeapDay_ThrowsExceptionForUnsupportedMinimumInstant() throws Throwable {
        // Test that checking leap day below minimum supported instant throws exception
        CopticChronology chronology = CopticChronology.getInstance();
        
        try {
            chronology.isLeapDay(MINIMUM_SUPPORTED_INSTANT);
            fail("Should throw IllegalArgumentException for instant below minimum supported");
        } catch(IllegalArgumentException e) {
            assertTrue("Exception message should mention minimum supported date",
                      e.getMessage().contains("below the supported minimum"));
            verifyException("org.joda.time.chrono.LimitChronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_ThrowsExceptionForInvalidMinDaysInFirstWeek() throws Throwable {
        // Test that constructor throws exception for invalid minDaysInFirstWeek parameter
        CopticChronology baseChronology = CopticChronology.getInstanceUTC();
        
        try {
            new CopticChronology(baseChronology, baseChronology, INVALID_MIN_DAYS_IN_FIRST_WEEK);
            fail("Should throw IllegalArgumentException for invalid min days in first week");
        } catch(IllegalArgumentException e) {
            assertTrue("Exception message should mention invalid min days",
                      e.getMessage().contains("Invalid min days in first week"));
            verifyException("org.joda.time.chrono.BasicChronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructor_ValidMinDaysInFirstWeek() throws Throwable {
        // Test that constructor works with valid minDaysInFirstWeek parameter
        CopticChronology baseChronology = CopticChronology.getInstance();
        
        CopticChronology newChronology = new CopticChronology(baseChronology, baseChronology, 
                                                             VALID_MIN_DAYS_IN_FIRST_WEEK);
        
        assertNotEquals("New chronology should be different from base chronology", 
                       baseChronology, newChronology);
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYear_MiddleYear() throws Throwable {
        // Test calculation of first day of year for a middle-range year
        CopticChronology chronology = CopticChronology.getInstanceUTC();
        int testYear = 3571;
        
        long firstDayMillis = chronology.calculateFirstDayOfYearMillis(testYear);
        
        assertEquals("First day of year 3571 should be calculated correctly", 
                     59476377600000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testCalculateFirstDayOfYear_FirstYear() throws Throwable {
        // Test calculation of first day of the first Coptic year
        CopticChronology chronology = CopticChronology.getInstanceUTC();
        int firstCopticYear = 1;
        
        long firstDayMillis = chronology.calculateFirstDayOfYearMillis(firstCopticYear);
        
        assertEquals("First day of Coptic year 1 should be calculated correctly", 
                     -53184211200000L, firstDayMillis);
    }

    @Test(timeout = 4000)
    public void testGetInstance_ThrowsExceptionForNegativeMinDays() throws Throwable {
        // Test that getInstance throws exception for negative minDaysInFirstWeek
        DateTimeZone customZone = DateTimeZone.forOffsetMillis(INVALID_NEGATIVE_MIN_DAYS);
        
        try {
            CopticChronology.getInstance(customZone, INVALID_NEGATIVE_MIN_DAYS);
            fail("Should throw IllegalArgumentException for negative min days in first week");
        } catch(IllegalArgumentException e) {
            assertTrue("Exception message should mention invalid min days",
                      e.getMessage().contains("Invalid min days in first week"));
            verifyException("org.joda.time.chrono.CopticChronology", e);
        }
    }

    @Test(timeout = 4000)
    public void testGetInstance_WithNullTimeZoneAndValidMinDays() throws Throwable {
        // Test getInstance with null timezone and valid minDaysInFirstWeek
        CopticChronology chronology = CopticChronology.getInstance(null, VALID_MIN_DAYS_IN_FIRST_WEEK);
        
        assertEquals("AM era constant should be correctly set", 1, CopticChronology.AM);
    }

    @Test(timeout = 4000)
    public void testConstructor_WithNullBaseChronology() throws Throwable {
        // Test constructor with null base chronology
        CopticChronology paramChronology = CopticChronology.getInstance();
        
        CopticChronology newChronology = new CopticChronology(null, paramChronology, 
                                                             VALID_MIN_DAYS_IN_FIRST_WEEK);
        
        assertNotEquals("New chronology should be different from param chronology", 
                       paramChronology, newChronology);
    }

    @Test(timeout = 4000)
    public void testGetMinYear() throws Throwable {
        // Test retrieval of minimum supported year
        CopticChronology chronology = CopticChronology.getInstanceUTC();
        
        int minYear = chronology.getMinYear();
        
        assertEquals("Minimum year should match expected value", 
                     MINIMUM_SUPPORTED_YEAR, minYear);
    }

    @Test(timeout = 4000)
    public void testWithUTC_FromCustomTimeZone() throws Throwable {
        // Test conversion to UTC from custom timezone
        DateTimeZone customZone = DateTimeZone.forOffsetMillis(-1686);
        CopticChronology chronologyWithCustomZone = CopticChronology.getInstance(customZone, 7);
        
        CopticChronology utcChronology = (CopticChronology) chronologyWithCustomZone.withUTC();
        
        assertEquals("AM era constant should be preserved", 1, CopticChronology.AM);
    }

    @Test(timeout = 4000)
    public void testIsLeapDay_RegularDate() throws Throwable {
        // Test leap day check for a regular date (not a leap day)
        CopticChronology chronology = CopticChronology.getInstance();
        long regularDateMillis = 16965676800000L;
        
        boolean isLeapDay = chronology.isLeapDay(regularDateMillis);
        
        assertFalse("Regular date should not be identified as leap day", isLeapDay);
    }

    @Test(timeout = 4000)
    public void testIsLeapDay_MaxYearDate() throws Throwable {
        // Test leap day check for date in maximum supported year
        CopticChronology chronology = CopticChronology.getInstance();
        long maxYearDateMillis = 9223371994233600000L;
        
        boolean isLeapDay = chronology.isLeapDay(maxYearDateMillis);
        
        assertFalse("Date in max year should not be identified as leap day", isLeapDay);
    }

    @Test(timeout = 4000)
    public void testGetMaxYear() throws Throwable {
        // Test retrieval of maximum supported year
        CopticChronology chronology = CopticChronology.getInstance();
        
        int maxYear = chronology.getMaxYear();
        
        assertEquals("Maximum year should match expected value", 
                     MAXIMUM_SUPPORTED_YEAR, maxYear);
    }

    @Test(timeout = 4000)
    public void testWithZone_SameZone() throws Throwable {
        // Test that withZone returns same instance when zone is unchanged
        DateTimeZone customZone = DateTimeZone.forOffsetMillis(-1686);
        CopticChronology chronology = CopticChronology.getInstance(customZone, 7);
        
        Chronology sameZoneChronology = chronology.withZone(customZone);
        
        assertSame("Should return same instance when zone is unchanged", 
                   chronology, sameZoneChronology);
    }

    @Test(timeout = 4000)
    public void testIsLeapDay_EarlyDate() throws Throwable {
        // Test leap day check for very early date
        CopticChronology chronology = CopticChronology.getInstance();
        long earlyDateMillis = 1L;
        
        boolean isLeapDay = chronology.isLeapDay(earlyDateMillis);
        
        assertFalse("Early date should not be identified as leap day", isLeapDay);
    }

    @Test(timeout = 4000)
    public void testWithZone_NullZoneOnUTCInstance() throws Throwable {
        // Test withZone with null parameter on UTC instance
        CopticChronology utcChronology = CopticChronology.getInstanceUTC();
        
        Chronology resultChronology = utcChronology.withZone(null);
        
        assertSame("Should return same UTC instance when zone is null", 
                   utcChronology, resultChronology);
    }
}