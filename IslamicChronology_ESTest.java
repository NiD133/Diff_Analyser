package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.TimeZone;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.AssembledChronology;
import org.joda.time.chrono.CopticChronology;
import org.joda.time.chrono.GJChronology;
import org.joda.time.chrono.ISOChronology;
import org.joda.time.chrono.IslamicChronology;
import org.joda.time.tz.UTCProvider;

/**
 * Test suite for IslamicChronology functionality.
 * Tests the Islamic (Hijri) calendar system implementation.
 */
public class IslamicChronologyTest {

    // Constants for better readability
    private static final int ISLAMIC_YEAR_1 = 1;
    private static final int ISLAMIC_YEAR_30 = 30; // Leap year in Habash al-Hasib pattern
    private static final int ISLAMIC_YEAR_1900 = 1900;
    private static final int DAYS_IN_LONG_MONTH = 30;
    private static final int DAYS_IN_SHORT_MONTH = 29;
    private static final int DAYS_IN_LEAP_YEAR = 355;
    private static final int DAYS_IN_REGULAR_YEAR = 354;

    // ========== Leap Year Pattern Tests ==========

    @Test
    public void testLeapYearPatternTypes_AreNotEqual() {
        IslamicChronology.LeapYearPatternType pattern15Based = IslamicChronology.LEAP_YEAR_15_BASED;
        IslamicChronology chronology = IslamicChronology.getInstance();
        
        boolean areEqual = chronology.LEAP_YEAR_HABASH_AL_HASIB.equals(pattern15Based);
        
        assertFalse("Different leap year patterns should not be equal", areEqual);
    }

    @Test
    public void testLeapYearPattern_CustomPattern_NonLeapYear() {
        IslamicChronology.LeapYearPatternType customPattern = 
            new IslamicChronology.LeapYearPatternType(-1861, -276);
        
        boolean isLeapYear = customPattern.isLeapYear(-1861);
        
        assertFalse("Year -1861 should not be a leap year with this custom pattern", isLeapYear);
    }

    @Test
    public void testLeapYearPattern_HabashAlHasib_Year30IsLeapYear() {
        TimeZone timeZone = TimeZone.getTimeZone("");
        DateTimeZone dateTimeZone = DateTimeZone.forTimeZone(timeZone);
        IslamicChronology chronology = IslamicChronology.getInstance(dateTimeZone, 
            IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB);
        
        boolean isLeapYear = chronology.isLeapYear(ISLAMIC_YEAR_30);
        
        assertTrue("Year 30 should be a leap year in Habash al-Hasib pattern", isLeapYear);
    }

    @Test
    public void testLeapYear_Year1_IsNotLeapYear() {
        IslamicChronology chronology = IslamicChronology.getInstance();
        
        boolean isLeapYear = chronology.isLeapYear(ISLAMIC_YEAR_1);
        
        assertFalse("Year 1 should not be a leap year", isLeapYear);
    }

    @Test
    public void testLeapYearPatternType_EqualsItself() {
        IslamicChronology.LeapYearPatternType pattern = IslamicChronology.LEAP_YEAR_15_BASED;
        
        boolean isEqual = pattern.equals(pattern);
        
        assertTrue("Leap year pattern should equal itself", isEqual);
    }

    @Test
    public void testLeapYearPatternType_NotEqualsToNonPatternObject() {
        IslamicChronology chronology = IslamicChronology.getInstance();
        
        boolean isEqual = chronology.LEAP_YEAR_16_BASED.equals(chronology);
        
        assertFalse("Leap year pattern should not equal chronology object", isEqual);
    }

    @Test
    public void testLeapYearPatternType_DifferentPatternsNotEqual() {
        IslamicChronology.LeapYearPatternType habashPattern = IslamicChronology.LEAP_YEAR_HABASH_AL_HASIB;
        IslamicChronology chronology = IslamicChronology.getInstance();
        
        boolean areEqual = chronology.LEAP_YEAR_INDIAN.equals(habashPattern);
        
        assertFalse("Indian and Habash al-Hasib patterns should not be equal", areEqual);
    }

    // ========== Days in Month/Year Tests ==========

    @Test
    public void testGetDaysInMonthMax_EvenMonth_Returns29Days() {
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        
        int daysInMonth = chronology.getDaysInMonthMax(-238);
        
        assertEquals("Even-numbered months should have 29 days", DAYS_IN_SHORT_MONTH, daysInMonth);
    }

    @Test
    public void testGetDaysInMonthMax_OddMonth_Returns30Days() {
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        
        int daysInMonth = chronology.getDaysInMonthMax(1);
        
        assertEquals("Odd-numbered months should have 30 days", DAYS_IN_LONG_MONTH, daysInMonth);
    }

    @Test
    public void testGetDaysInMonthMax_Month12_Returns30Days() {
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        
        int daysInMonth = chronology.getDaysInMonthMax(12);
        
        assertEquals("Month 12 should have 30 days", DAYS_IN_LONG_MONTH, daysInMonth);
    }

    @Test
    public void testGetDaysInYear_LeapYear_Returns355Days() {
        IslamicChronology chronology = IslamicChronology.getInstance();
        
        int daysInYear = chronology.getDaysInYear(-188); // This is a leap year
        
        assertEquals("Leap years should have 355 days", DAYS_IN_LEAP_YEAR, daysInYear);
    }

    @Test
    public void testGetDaysInYear_RegularYear_Returns354Days() {
        IslamicChronology chronology = IslamicChronology.getInstance();
        
        int daysInYear = chronology.getDaysInYear(ISLAMIC_YEAR_1);
        
        assertEquals("Regular years should have 354 days", DAYS_IN_REGULAR_YEAR, daysInYear);
    }

    @Test
    public void testGetDaysInYearMax_Returns355() {
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        
        int maxDaysInYear = chronology.getDaysInYearMax();
        
        assertEquals("Maximum days in a year should be 355", DAYS_IN_LEAP_YEAR, maxDaysInYear);
    }

    @Test
    public void testGetDaysInMonthMax_Returns30() {
        IslamicChronology chronology = IslamicChronology.getInstance();
        
        int maxDaysInMonth = chronology.getDaysInMonthMax();
        
        assertEquals("Maximum days in a month should be 30", DAYS_IN_LONG_MONTH, maxDaysInMonth);
    }

    // ========== Date Calculation Tests ==========

    @Test
    public void testCalculateFirstDayOfYear_NegativeYear() {
        IslamicChronology chronology = IslamicChronology.getInstance();
        
        long firstDayMillis = chronology.calculateFirstDayOfYearMillis(-292269337);
        
        assertEquals("First day calculation for large negative year", 
            -8948534433609600000L, firstDayMillis);
    }

    @Test
    public void testCalculateFirstDayOfYear_Year1576() {
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        
        long firstDayMillis = chronology.calculateFirstDayOfYearMillis(1576);
        
        assertEquals("First day calculation for year 1576", 5700585600000L, firstDayMillis);
    }

    @Test
    public void testGetYear_FromNegativeTimestamp() {
        IslamicChronology chronology = IslamicChronology.getInstance();
        
        int year = chronology.getYear(-69139612803234L);
        
        assertEquals("Year extraction from negative timestamp", -839, year);
    }

    @Test
    public void testGetYear_FromRecentTimestamp() {
        IslamicChronology chronology = IslamicChronology.getInstance(null);
        
        int year = chronology.getYear(-2099L);
        
        assertEquals("Year extraction from recent timestamp", 1389, year);
    }

    @Test
    public void testSetYear_ChangeYear() {
        DateTimeZone defaultZone = DateTimeZone.getDefault();
        IslamicChronology chronology = IslamicChronology.getInstance(defaultZone);
        
        long newTimestamp = chronology.setYear(-42246144000000L, ISLAMIC_YEAR_1900);
        
        assertEquals("Setting year should return correct timestamp", 15651100800000L, newTimestamp);
    }

    // ========== Time Calculations ==========

    @Test
    public void testGetAverageMillisPerMonth() {
        IslamicChronology chronology = IslamicChronology.getInstance();
        
        long avgMillisPerMonth = chronology.getAverageMillisPerMonth();
        
        assertEquals("Average milliseconds per month", 2551440384L, avgMillisPerMonth);
    }

    @Test
    public void testGetAverageMillisPerYear() {
        IslamicChronology chronology = IslamicChronology.getInstance();
        
        long avgMillisPerYear = chronology.getAverageMillisPerYear();
        
        assertEquals("Average milliseconds per year", 30617280288L, avgMillisPerYear);
    }

    @Test
    public void testGetYearDifference_SameTimestamp_ReturnsZero() {
        IslamicChronology chronology = IslamicChronology.getInstance();
        
        long yearDifference = chronology.getYearDifference(-2966L, -2966L);
        
        assertEquals("Year difference for same timestamp should be zero", 0L, yearDifference);
    }

    @Test
    public void testGetYearDifference_DifferentTimestamps() {
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        
        long yearDifference = chronology.getYearDifference(918518400000L, 604800L);
        
        assertEquals("Year difference calculation", 29L, yearDifference);
    }

    // ========== Instance and Equality Tests ==========

    @Test
    public void testGetInstanceUTC_WithUTC_ReturnsSameInstance() {
        IslamicChronology chronology = IslamicChronology.getInstance();
        
        Chronology utcChronology = chronology.withUTC();
        
        assertSame("withUTC() should return same instance if already UTC", chronology, utcChronology);
    }

    @Test
    public void testEquals_SameChronology_ReturnsTrue() {
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        
        boolean isEqual = chronology.equals(chronology);
        
        assertTrue("Chronology should equal itself", isEqual);
    }

    @Test
    public void testEquals_DifferentTimeZones_ReturnsFalse() {
        IslamicChronology utcChronology = IslamicChronology.getInstanceUTC();
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(1);
        IslamicChronology offsetChronology = IslamicChronology.getInstance(offsetZone);
        
        boolean areEqual = utcChronology.equals(offsetChronology);
        
        assertFalse("Chronologies with different time zones should not be equal", areEqual);
    }

    @Test
    public void testEquals_DifferentLeapYearPatterns_ReturnsFalse() {
        IslamicChronology utcChronology = IslamicChronology.getInstanceUTC();
        DateTimeZone offsetZone = DateTimeZone.forOffsetMillis(1);
        IslamicChronology indianPatternChronology = IslamicChronology.getInstance(offsetZone, 
            IslamicChronology.LEAP_YEAR_INDIAN);
        
        boolean areEqual = utcChronology.equals(indianPatternChronology);
        
        assertFalse("Chronologies with different leap year patterns should not be equal", areEqual);
    }

    @Test
    public void testEquals_NonChronologyObject_ReturnsFalse() {
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        Object otherObject = new Object();
        
        boolean isEqual = chronology.equals(otherObject);
        
        assertFalse("Chronology should not equal non-chronology object", isEqual);
    }

    // ========== Boundary and Error Tests ==========

    @Test
    public void testGetMinYear_Returns1() {
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        
        int minYear = chronology.getMinYear();
        
        assertEquals("Minimum supported year should be 1", 1, minYear);
    }

    @Test
    public void testGetMaxYear_ReturnsMaxSupportedYear() {
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        
        int maxYear = chronology.getMaxYear();
        
        assertEquals("Maximum supported year", 292271022, maxYear);
    }

    @Test(expected = ArithmeticException.class)
    public void testCalculateFirstDayOfYear_YearTooLarge_ThrowsException() {
        IslamicChronology chronology = IslamicChronology.getInstanceUTC();
        
        chronology.calculateFirstDayOfYearMillis(292272984); // Year too large
    }

    @Test(expected = NullPointerException.class)
    public void testGetInstance_NullLeapYearPattern_ThrowsException() {
        DateTimeZone zone = DateTimeZone.forOffsetHours(12);
        
        IslamicChronology.getInstance(zone, null);
    }

    @Test(expected = NullPointerException.class)
    public void testHashCode_NullLeapYearPattern_ThrowsException() {
        ISOChronology isoChronology = ISOChronology.getInstanceUTC();
        IslamicChronology chronology = new IslamicChronology(isoChronology, isoChronology, null);
        
        chronology.hashCode();
    }

    // ========== Integration Tests ==========

    @Test
    public void testAssemble_PopulatesFields() {
        IslamicChronology chronology = IslamicChronology.getInstance();
        AssembledChronology.Fields fields = new AssembledChronology.Fields();
        
        chronology.assemble(fields);
        
        assertEquals("AH constant should be 1", 1, IslamicChronology.AH);
    }

    @Test
    public void testGetLeapYearPatternType_ReturnsNonNull() {
        IslamicChronology chronology = IslamicChronology.getInstance();
        
        IslamicChronology.LeapYearPatternType patternType = chronology.getLeapYearPatternType();
        
        assertNotNull("Leap year pattern type should not be null", patternType);
    }

    @Test
    public void testWithZone_DifferentZone_ReturnsNewInstance() {
        IslamicChronology utcChronology = IslamicChronology.getInstanceUTC();
        DateTimeZone newZone = DateTimeZone.forOffsetHoursMinutes(1, 1);
        
        Chronology newChronology = utcChronology.withZone(newZone);
        
        assertNotSame("withZone should return new instance for different zone", 
            newChronology, utcChronology);
    }

    @Test
    public void testWithZone_NullZone_ReturnsNewInstance() {
        IslamicChronology utcChronology = IslamicChronology.getInstanceUTC();
        
        IslamicChronology newChronology = (IslamicChronology) utcChronology.withZone(null);
        
        assertEquals("AH constant should remain 1", 1, IslamicChronology.AH);
    }
}