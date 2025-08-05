package org.apache.commons.io.file.attribute;

import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.nio.file.attribute.FileTime;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.Date;
import org.apache.commons.io.file.attribute.FileTimes;

/**
 * Test suite for FileTimes utility class.
 * Tests time conversions between Unix time, NTFS time, FileTime, Date, and Instant.
 */
public class FileTimesTest {

    // Constants for test readability
    private static final long UNIX_EPOCH_SECONDS = 0L;
    private static final long UNIX_TIME_MAX = 2147483647L; // 2038-01-19 03:14:07 UTC
    private static final long UNIX_TIME_MIN = -2147483648L; // 1901-12-13 20:45:52 UTC
    private static final long NTFS_EPOCH = 0L; // 1601-01-01 00:00:00 UTC
    private static final long NTFS_TO_UNIX_OFFSET_SECONDS = -11644473600L;

    // Unix Time Validation Tests
    
    @Test
    public void testIsUnixTime_WithValidMaxUnixTime_ReturnsTrue() {
        boolean result = FileTimes.isUnixTime(UNIX_TIME_MAX);
        assertTrue("Maximum Unix time should be valid", result);
    }

    @Test
    public void testIsUnixTime_WithValidMinUnixTime_ReturnsTrue() {
        boolean result = FileTimes.isUnixTime(UNIX_TIME_MIN);
        assertTrue("Minimum Unix time should be valid", result);
    }

    @Test
    public void testIsUnixTime_WithExcessivelyLargeTime_ReturnsFalse() {
        long excessiveTime = 9223372036854775790L;
        boolean result = FileTimes.isUnixTime(excessiveTime);
        assertFalse("Excessively large time should not be valid Unix time", result);
    }

    @Test
    public void testIsUnixTime_WithExcessivelySmallTime_ReturnsFalse() {
        long excessiveTime = -9223372036854775808L;
        boolean result = FileTimes.isUnixTime(excessiveTime);
        assertFalse("Excessively small time should not be valid Unix time", result);
    }

    @Test
    public void testIsUnixTime_WithNullFileTime_ReturnsTrue() {
        boolean result = FileTimes.isUnixTime((FileTime) null);
        assertTrue("Null FileTime should be considered valid", result);
    }

    @Test
    public void testIsUnixTime_WithFileTimeOutOfRange_ReturnsFalse() {
        FileTime fileTime = FileTimes.ntfsTimeToFileTime(-165L);
        boolean result = FileTimes.isUnixTime(fileTime);
        assertFalse("FileTime before Unix epoch should not be valid Unix time", result);
    }

    // Unix Time Conversion Tests

    @Test
    public void testToUnixTime_WithEpochFileTime_ReturnsZero() {
        FileTime epochTime = FileTimes.EPOCH;
        long result = FileTimes.toUnixTime(epochTime);
        assertEquals("Unix epoch should convert to 0", UNIX_EPOCH_SECONDS, result);
    }

    @Test
    public void testToUnixTime_WithNullFileTime_ReturnsZero() {
        long result = FileTimes.toUnixTime(null);
        assertEquals("Null FileTime should return 0", 0L, result);
    }

    @Test
    public void testToUnixTime_WithNtfsEpochFileTime_ReturnsNtfsOffset() {
        FileTime ntfsEpoch = FileTimes.ntfsTimeToFileTime(NTFS_EPOCH);
        long result = FileTimes.toUnixTime(ntfsEpoch);
        assertEquals("NTFS epoch should convert to offset seconds", 
                    NTFS_TO_UNIX_OFFSET_SECONDS, result);
    }

    // NTFS Time Conversion Tests

    @Test
    public void testToNtfsTime_WithUnixEpochMillis_ReturnsExpectedValue() {
        long unixEpochMillis = 0L;
        long expectedNtfsTime = 116444736000000000L;
        long result = FileTimes.toNtfsTime(unixEpochMillis);
        assertEquals("Unix epoch should convert to expected NTFS time", 
                    expectedNtfsTime, result);
    }

    @Test
    public void testNtfsTimeRoundTrip_WithZeroValue_MaintainsValue() {
        Date originalDate = FileTimes.ntfsTimeToDate(NTFS_EPOCH);
        long result = FileTimes.toNtfsTime(originalDate);
        assertEquals("NTFS time round trip should maintain value", NTFS_EPOCH, result);
    }

    @Test
    public void testNtfsTimeRoundTrip_WithNegativeValue_MaintainsValue() {
        long originalNtfsTime = -1401L;
        FileTime fileTime = FileTimes.ntfsTimeToFileTime(originalNtfsTime);
        long result = FileTimes.toNtfsTime(fileTime);
        assertEquals("Negative NTFS time round trip should maintain value", 
                    originalNtfsTime, result);
    }

    @Test
    public void testToNtfsTime_WithLargePositiveTime_ClampsToMaxValue() {
        long largeTime = 9223372036854775790L;
        long result = FileTimes.toNtfsTime(largeTime);
        assertEquals("Large time should be clamped to max value", 
                    Long.MAX_VALUE, result);
    }

    @Test
    public void testToNtfsTime_WithLargeNegativeTime_ClampsToMinValue() {
        long largeNegativeTime = -9223372036854775808L;
        long result = FileTimes.toNtfsTime(largeNegativeTime);
        assertEquals("Large negative time should be clamped to min value", 
                    Long.MIN_VALUE, result);
    }

    // Date/FileTime Conversion Tests

    @Test
    public void testToFileTime_WithNullDate_ReturnsNull() {
        FileTime result = FileTimes.toFileTime(null);
        assertNull("Null date should return null FileTime", result);
    }

    @Test
    public void testToDate_WithNullFileTime_ReturnsNull() {
        Date result = FileTimes.toDate(null);
        assertNull("Null FileTime should return null Date", result);
    }

    @Test
    public void testDateFileTimeRoundTrip_MaintainsValue() {
        FileTime originalFileTime = FileTime.fromMillis(120L);
        Date date = FileTimes.toDate(originalFileTime);
        assertNotNull("Converted date should not be null", date);
        
        long ntfsTime = FileTimes.toNtfsTime(date);
        assertEquals("Round trip conversion should maintain NTFS time", 
                    116444736001200000L, ntfsTime);
    }

    // Time Arithmetic Tests

    @Test
    public void testPlusSeconds_WithValidInput_ReturnsModifiedTime() {
        FileTime originalTime = FileTimes.EPOCH;
        long secondsToAdd = 690L;
        
        FileTime result = FileTimes.plusSeconds(originalTime, secondsToAdd);
        
        assertNotSame("Result should be different instance", originalTime, result);
    }

    @Test
    public void testPlusNanos_WithValidInput_ReturnsModifiedTime() {
        FileTime originalTime = FileTimes.now();
        long nanosToAdd = 1395L;
        
        FileTime result = FileTimes.plusNanos(originalTime, nanosToAdd);
        
        assertNotSame("Result should be different instance", originalTime, result);
    }

    @Test
    public void testMinusSeconds_WithNegativeValue_AddsTime() {
        FileTime originalTime = FileTimes.now();
        long negativeSeconds = -3169L;
        
        FileTime result = FileTimes.minusSeconds(originalTime, negativeSeconds);
        
        assertFalse("Result should be different from original", 
                   result.equals(originalTime));
    }

    @Test
    public void testMinusNanos_WithZero_ReturnsSameTime() {
        FileTime originalTime = FileTimes.now();
        
        FileTime result = FileTimes.minusNanos(originalTime, 0L);
        
        assertTrue("Subtracting zero should return equivalent time", 
                  result.equals(originalTime));
    }

    @Test
    public void testMinusMillis_WithZero_ReturnsSameTime() {
        FileTime originalTime = FileTimes.ntfsTimeToFileTime(369L);
        
        FileTime result = FileTimes.minusMillis(originalTime, 0L);
        
        assertTrue("Subtracting zero milliseconds should return same time", 
                  result.equals(originalTime));
    }

    @Test
    public void testPlusMillis_WithValidInput_ReturnsModifiedTime() {
        FileTime originalTime = FileTimes.ntfsTimeToFileTime(369L);
        
        FileTime result = FileTimes.plusMillis(originalTime, 369L);
        
        assertFalse("Adding milliseconds should change the time", 
                   result.equals(originalTime));
    }

    // BigDecimal Conversion Tests

    @Test
    public void testNtfsTimeToInstant_WithBigDecimalTen_ReturnsValidInstant() {
        BigDecimal ntfsTime = BigDecimal.TEN;
        
        Instant result = FileTimes.ntfsTimeToInstant(ntfsTime);
        
        assertNotNull("Should convert BigDecimal to valid Instant", result);
    }

    @Test
    public void testNtfsTimeToDate_WithLargeNegativeBigDecimal_ReturnsValidDate() {
        BigDecimal largeNegative = new BigDecimal(Long.MIN_VALUE);
        
        Date result = FileTimes.ntfsTimeToDate(largeNegative);
        
        assertNotNull("Should convert large negative BigDecimal to Date", result);
        assertEquals("Should produce expected date string", 
                    "Thu Nov 14 21:11:54 GMT 27628", result.toString());
    }

    // Error Condition Tests

    @Test(expected = NullPointerException.class)
    public void testToNtfsTime_WithNullDate_ThrowsNullPointerException() {
        FileTimes.toNtfsTime((Date) null);
    }

    @Test(expected = NullPointerException.class)
    public void testToNtfsTime_WithNullInstant_ThrowsNullPointerException() {
        FileTimes.toNtfsTime((Instant) null);
    }

    @Test(expected = NullPointerException.class)
    public void testToNtfsTime_WithNullFileTime_ThrowsNullPointerException() {
        FileTimes.toNtfsTime((FileTime) null);
    }

    @Test(expected = NullPointerException.class)
    public void testPlusSeconds_WithNullFileTime_ThrowsNullPointerException() {
        FileTimes.plusSeconds(null, 848L);
    }

    @Test(expected = NullPointerException.class)
    public void testPlusNanos_WithNullFileTime_ThrowsNullPointerException() {
        FileTimes.plusNanos(null, -1240L);
    }

    @Test(expected = NullPointerException.class)
    public void testPlusMillis_WithNullFileTime_ThrowsNullPointerException() {
        FileTimes.plusMillis(null, 24L);
    }

    @Test(expected = NullPointerException.class)
    public void testMinusSeconds_WithNullFileTime_ThrowsNullPointerException() {
        FileTimes.minusSeconds(null, 0L);
    }

    @Test(expected = NullPointerException.class)
    public void testMinusNanos_WithNullFileTime_ThrowsNullPointerException() {
        FileTimes.minusNanos(null, 0L);
    }

    @Test(expected = NullPointerException.class)
    public void testMinusMillis_WithNullFileTime_ThrowsNullPointerException() {
        FileTimes.minusMillis(null, 2332L);
    }

    @Test(expected = ArithmeticException.class)
    public void testToNtfsTime_WithOverflowInstant_ThrowsArithmeticException() {
        Instant overflowInstant = Instant.ofEpochMilli(116444736000820005L);
        FileTimes.toNtfsTime(overflowInstant);
    }

    @Test(expected = ArithmeticException.class)
    public void testToNtfsTime_WithMaxInstant_ThrowsArithmeticException() {
        Instant maxInstant = Instant.ofEpochMilli(Long.MAX_VALUE);
        FileTime fileTime = FileTime.from(maxInstant);
        FileTimes.toNtfsTime(fileTime);
    }

    @Test(expected = DateTimeException.class)
    public void testPlusSeconds_WithOverflow_ThrowsDateTimeException() {
        FileTime largeTime = FileTimes.ntfsTimeToFileTime(116444736024990000L);
        FileTimes.plusSeconds(largeTime, 116444736024990000L);
    }

    @Test(expected = DateTimeException.class)
    public void testPlusNanos_WithUnderflow_ThrowsDateTimeException() {
        FileTime minTime = FileTimes.fromUnixTime(Long.MIN_VALUE);
        FileTimes.plusNanos(minTime, Long.MIN_VALUE);
    }

    @Test(expected = ArithmeticException.class)
    public void testPlusSeconds_WithLongOverflow_ThrowsArithmeticException() {
        FileTime currentTime = FileTimes.now();
        FileTimes.plusSeconds(currentTime, 9223372036854775800L);
    }

    @Test(expected = ArithmeticException.class)
    public void testMinusSeconds_WithLongOverflow_ThrowsArithmeticException() {
        FileTime currentTime = FileTimes.now();
        FileTimes.minusSeconds(currentTime, Long.MIN_VALUE);
    }

    @Test(expected = NullPointerException.class)
    public void testNtfsTimeToInstant_WithNullBigDecimal_ThrowsNullPointerException() {
        FileTimes.ntfsTimeToInstant((BigDecimal) null);
    }

    @Test(expected = ArithmeticException.class)
    public void testNtfsTimeToInstant_WithNonIntegerBigDecimal_ThrowsArithmeticException() {
        BigDecimal nonInteger = new BigDecimal(82.953588012);
        FileTimes.ntfsTimeToInstant(nonInteger);
    }

    @Test(expected = NullPointerException.class)
    public void testNtfsTimeToDate_WithNullBigDecimal_ThrowsNullPointerException() {
        FileTimes.ntfsTimeToDate((BigDecimal) null);
    }

    @Test(expected = ArithmeticException.class)
    public void testNtfsTimeToDate_WithNonIntegerBigDecimal_ThrowsArithmeticException() {
        BigDecimal nonInteger = BigDecimal.valueOf(-1104.7219);
        FileTimes.ntfsTimeToDate(nonInteger);
    }

    @Test(expected = NullPointerException.class)
    public void testSetLastModifiedTime_WithNullPath_ThrowsNullPointerException() {
        FileTimes.setLastModifiedTime(null);
    }
}