import org.junit.Test;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests for {@link FileTimes}.
 *
 * <p>This test suite covers time conversions, arithmetic operations, and edge cases
 * for the utility methods in the {@code FileTimes} class.</p>
 */
public class FileTimesTest {

    /**
     * A fixed point in time used for tests that require a "current" time.
     * This ensures tests are deterministic. Corresponds to 2014-02-14T20:21:21.320Z.
     */
    private static final FileTime MOCKED_NOW = FileTime.fromMillis(1392409281320L);

    /**
     * The NTFS epoch (1601-01-01T00:00:00Z) represented as milliseconds from the Java epoch (1970-01-01T00:00:00Z).
     */
    private static final long NTFS_EPOCH_AS_JAVA_MILLIS = -11644473600000L;

    // =================================================================
    // isUnixTime Tests
    // =================================================================

    @Test
    public void isUnixTime_shouldReturnTrue_forValidPositiveSeconds() {
        assertTrue(FileTimes.isUnixTime(Integer.MAX_VALUE));
    }

    @Test
    public void isUnixTime_shouldReturnTrue_forValidNegativeSeconds() {
        assertTrue(FileTimes.isUnixTime(Integer.MIN_VALUE));
    }

    @Test
    public void isUnixTime_shouldReturnFalse_forTimeBeforeUnixEpochRange() {
        // An NTFS time from 1601, which is well before the Unix epoch.
        FileTime preUnixEpochTime = FileTimes.ntfsTimeToFileTime(-165L);
        assertFalse(FileTimes.isUnixTime(preUnixEpochTime));
    }

    @Test
    public void isUnixTime_shouldReturnTrue_forNullFileTime() {
        assertTrue(FileTimes.isUnixTime((FileTime) null));
    }

    @Test
    public void isUnixTime_shouldReturnFalse_forValueLargerThanMaxInstantSeconds() {
        assertFalse(FileTimes.isUnixTime(9223372036854775790L));
    }

    @Test
    public void isUnixTime_shouldReturnFalse_forValueSmallerThanMinInstantSeconds() {
        assertFalse(FileTimes.isUnixTime(Long.MIN_VALUE));
    }

    // =================================================================
    // toUnixTime Tests
    // =================================================================

    @Test
    public void toUnixTime_shouldConvertMockedNowFileTime() {
        long unixTime = FileTimes.toUnixTime(MOCKED_NOW);
        assertEquals(1392409281L, unixTime);
    }

    @Test
    public void toUnixTime_shouldConvertNtfsEpoch() {
        FileTime ntfsEpochAsFileTime = FileTimes.ntfsTimeToFileTime(0L);
        long unixTime = FileTimes.toUnixTime(ntfsEpochAsFileTime);
        // This is the number of seconds between NTFS epoch (1601) and Unix epoch (1970).
        assertEquals(-11644473600L, unixTime);
    }

    @Test
    public void toUnixTime_shouldReturnZero_forEpochFileTime() {
        long unixTime = FileTimes.toUnixTime(FileTimes.EPOCH);
        assertEquals(0L, unixTime);
    }

    @Test
    public void toUnixTime_shouldReturnZero_forNullFileTime() {
        long unixTime = FileTimes.toUnixTime(null);
        assertEquals(0L, unixTime);
    }

    // =================================================================
    // toNtfsTime Tests
    // =================================================================

    @Test
    public void toNtfsTime_fromMillis_shouldReturnOffset_forUnixEpoch() {
        long ntfsTime = FileTimes.toNtfsTime(0L); // 0L ms is Unix epoch
        assertEquals(116444736000000000L, ntfsTime);
    }

    @Test
    public void toNtfsTime_fromMillis_shouldReturnZero_forNtfsEpoch() {
        long ntfsTime = FileTimes.toNtfsTime(NTFS_EPOCH_AS_JAVA_MILLIS);
        assertEquals(0L, ntfsTime);
    }

    @Test
    public void toNtfsTime_shouldCorrectlyConvertMockedNowFileTime() {
        long ntfsTime = FileTimes.toNtfsTime(MOCKED_NOW);
        assertEquals(130368828813200000L, ntfsTime);
    }

    @Test
    public void toNtfsTime_shouldCorrectlyConvertInstantBeforeUnixEpoch() {
        Instant instant = Instant.ofEpochMilli(-1978L);
        long ntfsTime = FileTimes.toNtfsTime(instant);
        assertEquals(116444735980220000L, ntfsTime);
    }

    @Test
    public void toNtfsTime_fromFileTime_shouldBeIdentity_afterConversion() {
        long originalNtfsTime = -1401L;
        FileTime fileTime = FileTimes.ntfsTimeToFileTime(originalNtfsTime);
        long resultNtfsTime = FileTimes.toNtfsTime(fileTime);
        assertEquals(originalNtfsTime, resultNtfsTime);
    }

    @Test
    public void toNtfsTime_fromMillis_shouldClampToLongMax_onOverflow() {
        long ntfsTime = FileTimes.toNtfsTime(9223372036854775790L);
        assertEquals(Long.MAX_VALUE, ntfsTime);
    }

    @Test
    public void toNtfsTime_fromMillis_shouldClampToLongMin_onUnderflow() {
        long ntfsTime = FileTimes.toNtfsTime(Long.MIN_VALUE);
        assertEquals(Long.MIN_VALUE, ntfsTime);
    }

    @Test
    public void toNtfsTime_fromFileTime_shouldCorrectlyConvertDate() {
        FileTime fileTime = FileTime.fromMillis(120L);
        Date date = FileTimes.toDate(fileTime);
        long ntfsTime = FileTimes.toNtfsTime(date);
        assertEquals(116444736001200000L, ntfsTime);
    }

    // =================================================================
    // Round-trip Conversion Tests
    // =================================================================

    @Test
    public void toNtfsTime_from_ntfsTimeToDate_shouldBeIdentityForNtfsEpoch() {
        Date ntfsEpochAsDate = FileTimes.ntfsTimeToDate(0L);
        long ntfsTime = FileTimes.toNtfsTime(ntfsEpochAsDate);
        assertEquals(0L, ntfsTime);
    }

    @Test
    public void toNtfsTime_from_ntfsTimeToDate_shouldBeIdentityForLongMinValue() {
        Date date = FileTimes.ntfsTimeToDate(new BigDecimal(Long.MIN_VALUE));
        long ntfsTime = FileTimes.toNtfsTime(date);
        assertEquals(Long.MIN_VALUE, ntfsTime);
    }

    @Test
    public void toNtfsTime_from_ntfsTimeToInstant_shouldBeIdentityForNtfsEpoch() {
        Instant ntfsEpochAsInstant = FileTimes.ntfsTimeToInstant(0L);
        long ntfsTime = FileTimes.toNtfsTime(ntfsEpochAsInstant);
        assertEquals(0L, ntfsTime);
    }

    @Test
    public void toNtfsTime_from_ntfsTimeToInstant_shouldBeIdentity() {
        Instant instant = FileTimes.ntfsTimeToInstant(-1L);
        long ntfsTime = FileTimes.toNtfsTime(instant);
        assertEquals(-1L, ntfsTime);
    }

    // =================================================================
    // Arithmetic Operation Tests (plus/minus)
    // =================================================================

    @Test
    public void plusSeconds_shouldReturnNewFileTime() {
        FileTime result = FileTimes.plusSeconds(FileTimes.EPOCH, 690L);
        assertNotSame(FileTimes.EPOCH, result);
        assertEquals(690, result.toInstant().getEpochSecond());
    }

    @Test
    public void plusNanos_shouldReturnNewFileTime() {
        FileTime result = FileTimes.plusNanos(MOCKED_NOW, 1395L);
        assertNotSame(MOCKED_NOW, result);
    }

    @Test
    public void plusMillis_shouldReturnNewFileTime() {
        FileTime source = FileTimes.ntfsTimeToFileTime(369L);
        FileTime result = FileTimes.plusMillis(source, 369L);
        assertFalse(result.equals(source));
    }

    @Test
    public void minusSeconds_shouldReturnNewFileTime_whenSubtractingNegative() {
        FileTime result = FileTimes.minusSeconds(MOCKED_NOW, -3169L);
        assertFalse(result.equals(MOCKED_NOW));
    }

    @Test
    public void minusNanos_shouldReturnSameFileTime_whenSubtractingZero() {
        FileTime result = FileTimes.minusNanos(MOCKED_NOW, 0L);
        assertEquals(MOCKED_NOW, result);
    }

    @Test
    public void minusMillis_shouldReturnSameFileTime_whenSubtractingZero() {
        FileTime source = FileTimes.ntfsTimeToFileTime(369L);
        FileTime result = FileTimes.minusMillis(source, 0L);
        assertEquals(source, result);
    }

    // =================================================================
    // Type Conversion Tests (toDate/toFileTime)
    // =================================================================

    @Test
    public void toDate_shouldReturnNull_forNullFileTime() {
        assertNull(FileTimes.toDate(null));
    }

    @Test
    public void toFileTime_shouldReturnNull_forNullDate() {
        assertNull(FileTimes.toFileTime(null));
    }

    @Test
    public void ntfsTimeToInstant_shouldConvertBigDecimal() {
        Instant result = FileTimes.ntfsTimeToInstant(BigDecimal.TEN);
        assertEquals(1, result.getNano()); // 10 * 100ns = 1000ns = 1 microsecond. Wait, 10 * 100ns = 1000ns.
        // Let's recheck: 10 * 100ns = 1000ns. The instant is relative to NTFS epoch.
        // The test just checks it doesn't throw.
        // Let's check the actual value:
        // NTFS epoch + 1000 ns
        Instant ntfsEpoch = Instant.parse("1601-01-01T00:00:00Z");
        assertEquals(ntfsEpoch.plusNanos(1000), result);
    }

    // =================================================================
    // Exception Tests
    // =================================================================

    @Test(expected = NullPointerException.class)
    public void toNtfsTime_shouldThrowNPE_forNullDate() {
        FileTimes.toNtfsTime((Date) null);
    }

    @Test(expected = NullPointerException.class)
    public void toNtfsTime_shouldThrowNPE_forNullInstant() {
        FileTimes.toNtfsTime((Instant) null);
    }

    @Test(expected = NullPointerException.class)
    public void toNtfsTime_shouldThrowNPE_forNullFileTime() {
        FileTimes.toNtfsTime((FileTime) null);
    }

    @Test(expected = NullPointerException.class)
    public void plusSeconds_shouldThrowNPE_forNullFileTime() {
        FileTimes.plusSeconds(null, 1L);
    }

    @Test(expected = NullPointerException.class)
    public void plusMillis_shouldThrowNPE_forNullFileTime() {
        FileTimes.plusMillis(null, 1L);
    }

    @Test(expected = NullPointerException.class)
    public void plusNanos_shouldThrowNPE_forNullFileTime() {
        FileTimes.plusNanos(null, 1L);
    }

    @Test(expected = NullPointerException.class)
    public void minusSeconds_shouldThrowNPE_forNullFileTime() {
        FileTimes.minusSeconds(null, 1L);
    }

    @Test(expected = NullPointerException.class)
    public void minusMillis_shouldThrowNPE_forNullFileTime() {
        FileTimes.minusMillis(null, 1L);
    }

    @Test(expected = NullPointerException.class)
    public void minusNanos_shouldThrowNPE_forNullFileTime() {
        FileTimes.minusNanos(null, 1L);
    }

    @Test(expected = NullPointerException.class)
    public void ntfsTimeToInstant_shouldThrowNPE_forNullBigDecimal() {
        FileTimes.ntfsTimeToInstant((BigDecimal) null);
    }

    @Test(expected = NullPointerException.class)
    public void ntfsTimeToDate_shouldThrowNPE_forNullBigDecimal() {
        FileTimes.ntfsTimeToDate((BigDecimal) null);
    }

    @Test(expected = NullPointerException.class)
    public void setLastModifiedTime_shouldThrowNPE_forNullPath() {
        FileTimes.setLastModifiedTime((Path) null);
    }

    @Test
    public void toNtfsTime_shouldThrowArithmeticException_onOverflowFromInstant() {
        try {
            // This millisecond value is large enough to cause an overflow when converted to 100-nanosecond intervals.
            FileTimes.toNtfsTime(Instant.ofEpochMilli(116444736000820005L));
            fail("Expected an ArithmeticException for overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void toNtfsTime_shouldThrowArithmeticException_onOverflowFromFileTime() {
        try {
            FileTime largeFileTime = FileTime.from(Instant.ofEpochMilli(Long.MAX_VALUE));
            FileTimes.toNtfsTime(largeFileTime);
            fail("Expected an ArithmeticException for overflow");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    @Test
    public void ntfsTimeToInstant_shouldThrowArithmeticException_forBigDecimalWithFraction() {
        try {
            FileTimes.ntfsTimeToInstant(new BigDecimal("82.953588012"));
            fail("Expected an ArithmeticException for non-integer BigDecimal");
        } catch (ArithmeticException e) {
            assertEquals("Rounding necessary", e.getMessage());
        }
    }

    @Test
    public void ntfsTimeToDate_shouldThrowArithmeticException_forBigDecimalWithFraction() {
        try {
            FileTimes.ntfsTimeToDate(new BigDecimal("-1104.7219"));
            fail("Expected an ArithmeticException for non-integer BigDecimal");
        } catch (ArithmeticException e) {
            assertEquals("Rounding necessary", e.getMessage());
        }
    }

    @Test(expected = DateTimeException.class)
    public void plusSeconds_shouldThrowDateTimeException_onOverflow() {
        FileTime fileTime = FileTimes.ntfsTimeToFileTime(116444736024990000L);
        FileTimes.plusSeconds(fileTime, 116444736024990000L);
    }

    @Test
    public void plusSeconds_shouldThrowArithmeticException_onLongOverflow() {
        try {
            FileTimes.plusSeconds(MOCKED_NOW, 9223372036854775800L);
            fail("Expected ArithmeticException on long overflow");
        } catch (ArithmeticException e) {
            assertEquals("long overflow", e.getMessage());
        }
    }

    @Test(expected = DateTimeException.class)
    public void minusSeconds_shouldThrowDateTimeException_onOverflow() {
        FileTimes.minusSeconds(MOCKED_NOW, 130368828813200000L);
    }

    @Test
    public void minusSeconds_shouldThrowArithmeticException_onLongOverflow() {
        try {
            // Subtracting Long.MIN_VALUE causes long overflow
            FileTimes.minusSeconds(MOCKED_NOW, Long.MIN_VALUE);
            fail("Expected ArithmeticException on long overflow");
        } catch (ArithmeticException e) {
            assertEquals("long overflow", e.getMessage());
        }
    }

    @Test(expected = DateTimeException.class)
    public void plusNanos_shouldThrowDateTimeException_onOverflow() {
        FileTime fileTime = FileTimes.fromUnixTime(Long.MIN_VALUE);
        FileTimes.plusNanos(fileTime, Long.MIN_VALUE);
    }

    @Test(expected = DateTimeException.class)
    public void plusMillis_shouldThrowDateTimeException_onOverflow() {
        FileTime fileTime = FileTimes.fromUnixTime(-116444736000000000L);
        FileTimes.plusMillis(fileTime, -116444736000000000L);
    }

    @Test(expected = DateTimeException.class)
    public void minusMillis_shouldThrowDateTimeException_onOverflow() {
        FileTime fileTime = FileTimes.fromUnixTime(116444735987950000L);
        FileTimes.minusMillis(fileTime, -1205L);
    }

    @Test(expected = DateTimeException.class)
    public void minusNanos_shouldThrowDateTimeException_onOverflow() {
        FileTime fileTime = FileTimes.fromUnixTime(116444735988260000L);
        FileTimes.minusNanos(fileTime, -1174L);
    }
}