package org.apache.commons.io.file.attribute;

import org.junit.Test;
import static org.junit.Assert.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.Date;
import org.apache.commons.io.file.attribute.FileTimes;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.mock.java.time.MockInstant;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class FileTimes_ESTest extends FileTimes_ESTest_scaffolding {

    // Test Unix time boundaries
    @Test(timeout = 4000)
    public void testIsUnixTimeWithMaxInt() {
        assertTrue(FileTimes.isUnixTime(2147483647L));
    }

    @Test(timeout = 4000)
    public void testIsUnixTimeWithMinInt() {
        assertTrue(FileTimes.isUnixTime(-2147483648L));
    }

    // Test conversion from FileTime to Unix time
    @Test(timeout = 4000)
    public void testNowToUnixTime() {
        FileTime fileTime = FileTimes.now();
        long unixTime = FileTimes.toUnixTime(fileTime);
        assertEquals(1392409281L, unixTime);
    }

    // Test NTFS time conversion
    @Test(timeout = 4000)
    public void testNtfsTimeToFileTimeAndToUnixTime() {
        FileTime fileTime = FileTimes.ntfsTimeToFileTime(0L);
        long unixTime = FileTimes.toUnixTime(fileTime);
        assertEquals(-11644473600L, unixTime);
    }

    @Test(timeout = 4000)
    public void testNtfsTimeToDateAndBack() {
        Date date = FileTimes.ntfsTimeToDate(0L);
        long ntfsTime = FileTimes.toNtfsTime(date);
        assertEquals(0L, ntfsTime);
    }

    @Test(timeout = 4000)
    public void testBigDecimalNtfsTimeToDateAndBack() {
        BigDecimal bigDecimal = new BigDecimal(-9223372036854775808L);
        Date date = FileTimes.ntfsTimeToDate(bigDecimal);
        long ntfsTime = FileTimes.toNtfsTime(date);
        assertEquals(-9223372036854775808L, ntfsTime);
        assertEquals("Thu Nov 14 21:11:54 GMT 27628", date.toString());
    }

    // Test conversion from NTFS time to Instant
    @Test(timeout = 4000)
    public void testNtfsTimeToInstantAndBack() {
        Instant instant = FileTimes.ntfsTimeToInstant(0L);
        long ntfsTime = FileTimes.toNtfsTime(instant);
        assertEquals(0L, ntfsTime);
    }

    @Test(timeout = 4000)
    public void testMockInstantToNtfsTime() {
        Instant instant = MockInstant.ofEpochMilli(-1978L);
        long ntfsTime = FileTimes.toNtfsTime(instant);
        assertEquals(116444735980220000L, ntfsTime);
    }

    // Test BigDecimal conversion to Date and FileTime
    @Test(timeout = 4000)
    public void testBigDecimalZeroToDateAndFileTime() {
        BigDecimal bigDecimal = new BigDecimal(0.0);
        Date date = FileTimes.ntfsTimeToDate(bigDecimal);
        FileTime fileTime = FileTimes.toFileTime(date);
        long ntfsTime = FileTimes.toNtfsTime(fileTime);
        assertEquals(0L, ntfsTime);
    }

    // Test NTFS time conversion with negative values
    @Test(timeout = 4000)
    public void testNegativeNtfsTimeToFileTimeAndBack() {
        FileTime fileTime = FileTimes.ntfsTimeToFileTime(-1401L);
        long ntfsTime = FileTimes.toNtfsTime(fileTime);
        assertEquals(-1401L, ntfsTime);
    }

    @Test(timeout = 4000)
    public void testNegativeUnixTimeToNtfsTime() {
        long ntfsTime = FileTimes.toNtfsTime(-11644473600000L);
        assertEquals(0L, ntfsTime);
    }

    // Test FileTime arithmetic operations
    @Test(timeout = 4000)
    public void testPlusSeconds() {
        FileTime fileTime = FileTimes.EPOCH;
        FileTime newFileTime = FileTimes.plusSeconds(fileTime, 690L);
        assertNotSame(fileTime, newFileTime);
    }

    @Test(timeout = 4000)
    public void testPlusNanos() {
        FileTime fileTime = FileTimes.now();
        FileTime newFileTime = FileTimes.plusNanos(fileTime, 1395L);
        assertNotSame(fileTime, newFileTime);
    }

    @Test(timeout = 4000)
    public void testMinusSeconds() {
        FileTime fileTime = FileTimes.now();
        FileTime newFileTime = FileTimes.minusSeconds(fileTime, -3169L);
        assertFalse(newFileTime.equals(fileTime));
    }

    @Test(timeout = 4000)
    public void testMinusNanos() {
        FileTime fileTime = FileTimes.now();
        FileTime newFileTime = FileTimes.minusNanos(fileTime, 0L);
        assertTrue(newFileTime.equals(fileTime));
    }

    // Test exception handling
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testToNtfsTimeWithNullDate() {
        FileTimes.toNtfsTime((Date) null);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testToNtfsTimeWithNullInstant() {
        FileTimes.toNtfsTime((Instant) null);
    }

    @Test(timeout = 4000, expected = ArithmeticException.class)
    public void testToNtfsTimeWithOverflowInstant() {
        Instant instant = MockInstant.ofEpochMilli(116444736000820005L);
        FileTimes.toNtfsTime(instant);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testToNtfsTimeWithNullFileTime() {
        FileTimes.toNtfsTime((FileTime) null);
    }

    @Test(timeout = 4000, expected = ArithmeticException.class)
    public void testToNtfsTimeWithOverflowFileTime() {
        Instant instant = MockInstant.ofEpochMilli(9223372036854775807L);
        FileTime fileTime = FileTime.from(instant);
        FileTimes.toNtfsTime(fileTime);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testPlusSecondsWithOverflow() {
        FileTime fileTime = FileTimes.ntfsTimeToFileTime(116444736024990000L);
        FileTimes.plusSeconds(fileTime, 116444736024990000L);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testPlusSecondsWithNullFileTime() {
        FileTimes.plusSeconds((FileTime) null, 848L);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testPlusNanosWithOverflow() {
        FileTime fileTime = FileTimes.fromUnixTime(-9223372036854775808L);
        FileTimes.plusNanos(fileTime, -9223372036854775808L);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testPlusMillisWithOverflow() {
        FileTime fileTime = FileTimes.fromUnixTime(-116444736000000000L);
        FileTimes.plusMillis(fileTime, -116444736000000000L);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testPlusMillisWithNullFileTime() {
        FileTimes.plusMillis((FileTime) null, 24L);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testNtfsTimeToInstantWithNullBigDecimal() {
        FileTimes.ntfsTimeToInstant((BigDecimal) null);
    }

    @Test(timeout = 4000, expected = ArithmeticException.class)
    public void testNtfsTimeToInstantWithRounding() {
        BigDecimal bigDecimal = new BigDecimal(82.953588012);
        FileTimes.ntfsTimeToInstant(bigDecimal);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testNtfsTimeToDateWithNullBigDecimal() {
        FileTimes.ntfsTimeToDate((BigDecimal) null);
    }

    @Test(timeout = 4000, expected = ArithmeticException.class)
    public void testNtfsTimeToDateWithRounding() {
        BigDecimal bigDecimal = BigDecimal.valueOf(-1104.7219);
        FileTimes.ntfsTimeToDate(bigDecimal);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testMinusSecondsWithOverflow() {
        FileTime fileTime = FileTimes.now();
        FileTimes.minusSeconds(fileTime, 130368828813200000L);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testMinusSecondsWithNullFileTime() {
        FileTimes.minusSeconds((FileTime) null, 0L);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testMinusNanosWithNullFileTime() {
        FileTimes.minusNanos((FileTime) null, 0L);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testMinusMillisWithOverflow() {
        FileTime fileTime = FileTimes.fromUnixTime(116444735987950000L);
        FileTimes.minusMillis(fileTime, -1205L);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testMinusMillisWithNullFileTime() {
        FileTimes.minusMillis((FileTime) null, 2332L);
    }

    // Test conversion from EPOCH to Unix time
    @Test(timeout = 4000)
    public void testEpochToUnixTime() {
        FileTime fileTime = FileTimes.EPOCH;
        long unixTime = FileTimes.toUnixTime(fileTime);
        assertEquals(0L, unixTime);
    }

    // Test conversion from null FileTime to Unix time
    @Test(timeout = 4000)
    public void testNullFileTimeToUnixTime() {
        long unixTime = FileTimes.toUnixTime((FileTime) null);
        assertEquals(0L, unixTime);
    }

    // Test conversion from Java time to NTFS time
    @Test(timeout = 4000)
    public void testJavaTimeToNtfsTime() {
        long ntfsTime = FileTimes.toNtfsTime(0L);
        assertEquals(116444736000000000L, ntfsTime);
    }

    // Test isUnixTime with null FileTime
    @Test(timeout = 4000)
    public void testIsUnixTimeWithNullFileTime() {
        assertTrue(FileTimes.isUnixTime((FileTime) null));
    }

    // Test conversion from negative Unix time to NTFS time
    @Test(timeout = 4000)
    public void testNegativeUnixTimeToNtfsTime() {
        long ntfsTime = FileTimes.toNtfsTime(-9223372036854775808L);
        assertEquals(-9223372036854775808L, ntfsTime);
    }

    // Test conversion from large Unix time to NTFS time
    @Test(timeout = 4000)
    public void testLargeUnixTimeToNtfsTime() {
        long ntfsTime = FileTimes.toNtfsTime(9223372036854775790L);
        assertEquals(9223372036854775807L, ntfsTime);
    }

    // Test conversion from null Date to FileTime
    @Test(timeout = 4000)
    public void testNullDateToFileTime() {
        FileTime fileTime = FileTimes.toFileTime((Date) null);
        assertNull(fileTime);
    }

    // Test conversion from null FileTime to Date
    @Test(timeout = 4000)
    public void testNullFileTimeToDate() {
        Date date = FileTimes.toDate((FileTime) null);
        assertNull(date);
    }

    // Test isUnixTime with large Unix time
    @Test(timeout = 4000)
    public void testIsUnixTimeWithLargeUnixTime() {
        assertFalse(FileTimes.isUnixTime(9223372036854775790L));
    }

    // Test isUnixTime with negative large Unix time
    @Test(timeout = 4000)
    public void testIsUnixTimeWithNegativeLargeUnixTime() {
        assertFalse(FileTimes.isUnixTime(-9223372036854775808L));
    }

    // Test conversion from current FileTime to NTFS time
    @Test(timeout = 4000)
    public void testNowToNtfsTime() {
        FileTime fileTime = FileTimes.now();
        long ntfsTime = FileTimes.toNtfsTime(fileTime);
        assertEquals(130368828813200000L, ntfsTime);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testPlusNanosWithNullFileTime() {
        FileTimes.plusNanos((FileTime) null, -1240L);
    }

    @Test(timeout = 4000, expected = ArithmeticException.class)
    public void testPlusSecondsWithLongOverflow() {
        FileTime fileTime = FileTimes.now();
        FileTimes.plusSeconds(fileTime, 9223372036854775800L);
    }

    @Test(timeout = 4000)
    public void testMinusMillisWithNoChange() {
        FileTime fileTime = FileTimes.ntfsTimeToFileTime(369L);
        FileTime newFileTime = FileTimes.minusMillis(fileTime, 0L);
        assertTrue(newFileTime.equals(fileTime));
    }

    // Test NTFS time to Instant with negative value
    @Test(timeout = 4000)
    public void testNegativeNtfsTimeToInstantAndBack() {
        Instant instant = FileTimes.ntfsTimeToInstant(-1L);
        long ntfsTime = FileTimes.toNtfsTime(instant);
        assertEquals(-1L, ntfsTime);
    }

    @Test(timeout = 4000, expected = NullPointerException.class)
    public void testSetLastModifiedTimeWithNullPath() {
        FileTimes.setLastModifiedTime((Path) null);
    }

    @Test(timeout = 4000, expected = DateTimeException.class)
    public void testMinusNanosWithOverflow() {
        FileTime fileTime = FileTimes.fromUnixTime(116444735988260000L);
        FileTimes.minusNanos(fileTime, -1174L);
    }

    @Test(timeout = 4000, expected = ArithmeticException.class)
    public void testMinusSecondsWithLongOverflow() {
        FileTime fileTime = FileTimes.now();
        FileTimes.minusSeconds(fileTime, -9223372036854775808L);
    }

    @Test(timeout = 4000)
    public void testFileTimeToDateAndBack() {
        FileTime fileTime = FileTime.fromMillis(120L);
        Date date = FileTimes.toDate(fileTime);
        assertNotNull(date);

        long ntfsTime = FileTimes.toNtfsTime(date);
        assertEquals(116444736001200000L, ntfsTime);
    }

    @Test(timeout = 4000)
    public void testPlusMillisWithChange() {
        FileTime fileTime = FileTimes.ntfsTimeToFileTime(369L);
        FileTime newFileTime = FileTimes.plusMillis(fileTime, 369L);
        assertFalse(newFileTime.equals(fileTime));
    }
}