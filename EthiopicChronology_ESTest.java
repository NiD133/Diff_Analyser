package org.joda.time.chrono;

import org.junit.Test;
import static org.junit.Assert.*;
import org.joda.time.Chronology;
import org.joda.time.DateTimeZone;
import org.joda.time.chrono.AssembledChronology;
import org.joda.time.chrono.EthiopicChronology;
import org.joda.time.chrono.LenientChronology;

/**
 * Test suite for EthiopicChronology functionality.
 * Tests cover instance creation, leap day calculations, year boundaries,
 * and chronology operations.
 */
public class EthiopicChronologyTest {

    // Test constants for better readability
    private static final long NEGATIVE_TIMESTAMP_1794 = -56623968000000L;
    private static final long MIN_LONG_VALUE = -9223372036854775808L;
    private static final long LEAP_DAY_TIMESTAMP_2079 = 3461702400000L;
    private static final long NON_LEAP_DAY_TIMESTAMP_1970 = 1209600011L;
    private static final int LARGE_YEAR = 15271875;
    private static final int ETHIOPIC_YEAR_ONE = 1;
    private static final int INVALID_MIN_DAYS_NEGATIVE = -4898;
    private static final int INVALID_MIN_DAYS_LARGE = 1767;

    // ========== Instance Creation Tests ==========

    @Test
    public void shouldCreateInstanceWithUTCTimeZone() {
        EthiopicChronology chronology = EthiopicChronology.getInstanceUTC();
        
        assertNotNull("UTC instance should not be null", chronology);
        assertEquals("EE constant should equal 1", 1, EthiopicChronology.EE);
    }

    @Test
    public void shouldCreateInstanceWithDefaultTimeZone() {
        EthiopicChronology chronology = EthiopicChronology.getInstance();
        
        assertNotNull("Default instance should not be null", chronology);
    }

    @Test
    public void shouldCreateInstanceWithCustomTimeZone() {
        DateTimeZone customZone = DateTimeZone.forOffsetMillis(-1962);
        
        EthiopicChronology chronology = EthiopicChronology.getInstance(customZone, 1);
        
        assertNotNull("Custom zone instance should not be null", chronology);
    }

    @Test
    public void shouldCreateInstanceWithNullTimeZone() {
        EthiopicChronology chronology = EthiopicChronology.getInstance((DateTimeZone) null);
        
        assertNotNull("Null zone instance should not be null", chronology);
    }

    // ========== Instance Creation Validation Tests ==========

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectNegativeMinDaysInFirstWeek() {
        DateTimeZone zone = DateTimeZone.forOffsetMillis(1900);
        
        EthiopicChronology.getInstance(zone, INVALID_MIN_DAYS_NEGATIVE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectTooLargeMinDaysInFirstWeek() {
        EthiopicChronology.getInstance((DateTimeZone) null, INVALID_MIN_DAYS_LARGE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectInvalidMinDaysInConstructor() {
        new EthiopicChronology((Chronology) null, (Object) null, -159);
    }

    // ========== Leap Day Calculation Tests ==========

    @Test
    public void shouldIdentifyNonLeapDay() {
        EthiopicChronology chronology = EthiopicChronology.getInstance((DateTimeZone) null);
        
        boolean isLeapDay = chronology.isLeapDay(NEGATIVE_TIMESTAMP_1794);
        
        assertFalse("Should not be a leap day", isLeapDay);
    }

    @Test
    public void shouldIdentifyLeapDay() {
        DateTimeZone zone = DateTimeZone.forOffsetMillis(-833);
        EthiopicChronology chronology = EthiopicChronology.getInstance(zone);
        
        boolean isLeapDay = chronology.isLeapDay(LEAP_DAY_TIMESTAMP_2079);
        
        assertTrue("Should be a leap day", isLeapDay);
    }

    @Test
    public void shouldIdentifyNonLeapDayInRecentDate() {
        DateTimeZone zone = DateTimeZone.forOffsetMillis(-2263);
        EthiopicChronology chronology = EthiopicChronology.getInstance(zone);
        
        boolean isLeapDay = chronology.isLeapDay(NON_LEAP_DAY_TIMESTAMP_1970);
        
        assertFalse("Should not be a leap day", isLeapDay);
    }

    @Test
    public void shouldHandleNegativeTimestampForLeapDay() {
        DateTimeZone zone = DateTimeZone.forOffsetMillis(-1962);
        EthiopicChronology chronology = EthiopicChronology.getInstance(zone);
        
        boolean isLeapDay = chronology.isLeapDay(-2899);
        
        assertFalse("Should not be a leap day for negative timestamp", isLeapDay);
    }

    // ========== Leap Day Boundary Tests ==========

    @Test(expected = IllegalArgumentException.class)
    public void shouldRejectTimestampBelowSupportedMinimum() {
        EthiopicChronology chronology = EthiopicChronology.getInstanceUTC();
        
        chronology.isLeapDay(MIN_LONG_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void shouldHandleTimeZoneOffsetOverflow() {
        DateTimeZone zone = DateTimeZone.forOffsetMillis(-4246);
        EthiopicChronology chronology = EthiopicChronology.getInstance(zone);
        
        chronology.isLeapDay(MIN_LONG_VALUE);
    }

    // ========== Year Boundary Tests ==========

    @Test
    public void shouldReturnCorrectMaxYear() {
        EthiopicChronology chronology = EthiopicChronology.getInstanceUTC();
        
        int maxYear = chronology.getMaxYear();
        
        assertEquals("Max year should be 292272984", 292272984, maxYear);
    }

    @Test
    public void shouldReturnCorrectMinYear() {
        DateTimeZone zone = DateTimeZone.forOffsetMillis(-1962);
        EthiopicChronology chronology = EthiopicChronology.getInstance(zone);
        
        int minYear = chronology.getMinYear();
        
        assertEquals("Min year should be -292269337", -292269337, minYear);
    }

    // ========== Year Calculation Tests ==========

    @Test
    public void shouldCalculateFirstDayOfLargeYear() {
        EthiopicChronology chronology = EthiopicChronology.getInstanceUTC();
        
        long firstDay = chronology.calculateFirstDayOfYearMillis(LARGE_YEAR);
        
        assertEquals("First day calculation for large year", 481881796790400000L, firstDay);
    }

    @Test
    public void shouldCalculateFirstDayOfYearOne() {
        EthiopicChronology chronology = EthiopicChronology.getInstance();
        
        long firstDay = chronology.calculateFirstDayOfYearMillis(ETHIOPIC_YEAR_ONE);
        
        assertEquals("First day of Ethiopic year 1", -61894108800000L, firstDay);
    }

    @Test
    public void shouldCalculateFirstDayOfMaxYear() {
        EthiopicChronology chronology = EthiopicChronology.getInstanceUTC();
        
        long firstDay = chronology.calculateFirstDayOfYearMillis(292272984);
        
        assertEquals("First day of max year", 9223371994233600000L, firstDay);
    }

    // ========== Chronology Operations Tests ==========

    @Test
    public void shouldReturnApproximateMillisAtEpoch() {
        EthiopicChronology chronology = EthiopicChronology.getInstance();
        
        long approxMillis = chronology.getApproxMillisAtEpochDividedByTwo();
        
        assertEquals("Approximate millis at epoch divided by two", 30962844000000L, approxMillis);
    }

    @Test
    public void shouldAssembleChronologyFields() {
        EthiopicChronology chronology = EthiopicChronology.getInstanceUTC();
        AssembledChronology.Fields fields = new AssembledChronology.Fields();
        
        chronology.assemble(fields);
        
        // Verify assembly completed without exception
        assertEquals("EE constant should remain 1", 1, EthiopicChronology.EE);
    }

    // ========== Time Zone Conversion Tests ==========

    @Test
    public void shouldReturnSameInstanceForSameZone() {
        EthiopicChronology chronology = EthiopicChronology.getInstance((DateTimeZone) null);
        
        Chronology result = chronology.withZone((DateTimeZone) null);
        
        assertSame("Should return same instance for same zone", chronology, result);
    }

    @Test
    public void shouldConvertToUTCZone() {
        EthiopicChronology chronology = EthiopicChronology.getInstance();
        DateTimeZone utcZone = DateTimeZone.UTC;
        
        EthiopicChronology utcChronology = (EthiopicChronology) chronology.withZone(utcZone);
        
        assertNotNull("UTC conversion should not be null", utcChronology);
    }

    @Test
    public void shouldConvertToUTCFromCustomZone() {
        DateTimeZone customZone = DateTimeZone.forOffsetMillis(-1962);
        EthiopicChronology chronology = EthiopicChronology.getInstance(customZone);
        
        Chronology utcChronology = chronology.withUTC();
        
        assertNotSame("UTC chronology should be different instance", utcChronology, chronology);
    }

    // ========== Constructor Tests ==========

    @Test
    public void shouldCreateChronologyWithLenientBase() {
        DateTimeZone zone = DateTimeZone.forOffsetMillis(-1962);
        EthiopicChronology baseChronology = EthiopicChronology.getInstance(zone);
        LenientChronology lenientBase = LenientChronology.getInstance(baseChronology);
        Object param = new Object();
        
        EthiopicChronology newChronology = new EthiopicChronology(lenientBase, param, 1);
        
        assertNotEquals("New chronology should be different from base", 
                       newChronology, baseChronology);
    }
}