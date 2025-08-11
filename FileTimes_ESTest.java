package org.apache.commons.io.file.attribute;

import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.Date;

import org.junit.Test;

/**
 * Readable, deterministic unit tests for FileTimes.
 *
 * Intent:
 * - Avoid magic numbers by deriving expectations from FileTimes' own constants.
 * - Prefer fixed instants over "now()" to keep tests deterministic.
 * - Group tests by behavior and document the why, not just the what.
 */
public class FileTimesTest {

    // Helpers to express intent and avoid magic numbers.

    // 100-nanosecond units per second. HUNDRED_NANOS_PER_SECOND is private in FileTimes,
    // but we can derive it from the public-per-package millisecond constant.
    private static final long HNS_PER_SECOND = FileTimes.HUNDRED_NANOS_PER_MILLISECOND * 1000L;

    // The Unix epoch in NTFS seconds offset form (negative).
    private static final long UNIX_EPOCH_AS_NTFS_SECONDS_OFFSET = FileTimes.UNIX_TO_NTFS_OFFSET / HNS_PER_SECOND;

    // Simple fixed instants to keep tests deterministic.
    private static final FileTime FT_EPOCH = FileTimes.EPOCH; // 1970-01-01T00:00:00Z
    private static final FileTime FT_1S = FileTimes.fromUnixTime(1L); // 1970-01-01T00:00:01Z

    // ----------------------------
    // isUnixTime: seconds overload
    // ----------------------------

    @Test
    public void isUnixTime_seconds_within_32bit_bounds_returns_true() {
        assertTrue(FileTimes.isUnixTime(Integer.MAX_VALUE));
        assertTrue(FileTimes.isUnixTime(Integer.MIN_VALUE));
    }

    @Test
    public void isUnixTime_seconds_far_out_of_bounds_returns_false() {
        assertFalse(FileTimes.isUnixTime(Long.MIN_VALUE));
        assertFalse(FileTimes.isUnixTime(Long.MAX_VALUE - 17)); // arbitrary large out-of-range
    }

    // --------------------------------
    // isUnixTime: FileTime overload
    // --------------------------------

    @Test
    public void isUnixTime_fileTime_null_is_true() {
        assertTrue(FileTimes.isUnixTime((FileTime) null));
    }

    @Test
    public void isUnixTime_fileTime_epoch_is_true() {
        assertTrue(FileTimes.isUnixTime(FT_EPOCH));
    }

    @Test
    public void isUnixTime_fileTime_far_before_epoch_is_false() {
        // NTFS 0 is 1601-01-01, well before Unix epoch and outside 32-bit Unix seconds bounds.
        FileTime beforeEpoch = FileTimes.ntfsTimeToFileTime(0L);
        assertFalse(FileTimes.isUnixTime(beforeEpoch));
    }

    // ------------------------------
    // fromUnixTime / toUnixTime
    // ------------------------------

    @Test
    public void toUnixTime_epoch_is_zero() {
        assertEquals(0L, FileTimes.toUnixTime(FT_EPOCH));
    }

    @Test
    public void toUnixTime_null_returns_zero() {
        assertEquals(0L, FileTimes.toUnixTime((FileTime) null));
    }

    @Test
    public void fromUnixTime_round_trips_through_toUnixTime() {
        assertEquals(-1L, FileTimes.toUnixTime(FileTimes.fromUnixTime(-1L)));
        assertEquals(0L, FileTimes.toUnixTime(FileTimes.fromUnixTime(0L)));
        assertEquals(1L, FileTimes.toUnixTime(FileTimes.fromUnixTime(1L)));
        assertEquals(Integer.MAX_VALUE, FileTimes.toUnixTime(FileTimes.fromUnixTime(Integer.MAX_VALUE)));
    }

    @Test
    public void ntfs_zero_converts_to_expected_unix_seconds_offset() {
        FileTime ft = FileTimes.ntfsTimeToFileTime(0L);
        assertEquals(UNIX_EPOCH_AS_NTFS_SECONDS_OFFSET, FileTimes.toUnixTime(ft));
    }

    // ------------------------------
    // NTFS <-> Date/Instant/FileTime
    // ------------------------------

    @Test
    public void ntfs_zero_round_trips_through_date_and_instant() {
        Date date = FileTimes.ntfsTimeToDate(0L);
        assertEquals(0L, FileTimes.toNtfsTime(date));

        Instant instant = FileTimes.ntfsTimeToInstant(0L);
        assertEquals(0L, FileTimes.toNtfsTime(instant));
    }

    @Test
    public void ntfs_minus_one_round_trips_through_instant() {
        Instant instant = FileTimes.ntfsTimeToInstant(-1L);
        assertEquals(-1L, FileTimes.toNtfsTime(instant));
    }

    @Test
    public void ntfs_bigdecimal_fraction_throws_arithmetic_exception() {
        assertThrows(ArithmeticException.class, () -> FileTimes.ntfsTimeToInstant(new BigDecimal("1.5")));
        assertThrows(ArithmeticException.class, () -> FileTimes.ntfsTimeToDate(new BigDecimal("-1104.7219")));
    }

    @Test
    public void ntfs_bigdecimal_null_arguments_throw_npe() {
        assertThrows(NullPointerException.class, () -> FileTimes.ntfsTimeToInstant((BigDecimal) null));
        assertThrows(NullPointerException.class, () -> FileTimes.ntfsTimeToDate((BigDecimal) null));
    }

    // ------------------------------
    // Java millis <-> NTFS time
    // ------------------------------

    @Test
    public void javaMillis_epoch_to_ntfs_is_negated_offset() {
        // At Unix epoch (javaMillis = 0), NTFS time is the negated offset.
        long expected = -FileTimes.UNIX_TO_NTFS_OFFSET;
        assertEquals(expected, FileTimes.toNtfsTime(0L));
    }

    @Test
    public void javaMillis_offset_back_to_ntfs_zero() {
        // Java millis exactly at the NTFS epoch (1601-01-01).
        assertEquals(0L, FileTimes.toNtfsTime(-11644473600000L));
    }

    @Test
    public void toNtfsTime_clamps_at_long_bounds() {
        assertEquals(Long.MAX_VALUE, FileTimes.toNtfsTime(Long.MAX_VALUE));
        assertEquals(Long.MIN_VALUE, FileTimes.toNtfsTime(Long.MIN_VALUE));
    }

    @Test
    public void toNtfsTime_with_date_example() {
        // FileTime.fromMillis(120) -> Date -> NTFS. Derived expectation:
        // ntfs = javaMillis * HNS_PER_MILLISECOND - UNIX_TO_NTFS_OFFSET
        long javaMillis = 120L;
        long expectedNtfs = javaMillis * FileTimes.HUNDRED_NANOS_PER_MILLISECOND - FileTimes.UNIX_TO_NTFS_OFFSET;

        Date date = FileTimes.toDate(FileTime.fromMillis(javaMillis));
        assertNotNull(date);
        assertEquals(expectedNtfs, FileTimes.toNtfsTime(date));
    }

    // ------------------------------
    // Null handling for Date/FileTime conversions
    // ------------------------------

    @Test
    public void toFileTime_null_date_returns_null() {
        assertNull(FileTimes.toFileTime((Date) null));
    }

    @Test
    public void toDate_null_fileTime_returns_null() {
        assertNull(FileTimes.toDate((FileTime) null));
    }

    @Test
    public void toNtfsTime_null_inputs_throw_npe() {
        assertThrows(NullPointerException.class, () -> FileTimes.toNtfsTime((Date) null));
        assertThrows(NullPointerException.class, () -> FileTimes.toNtfsTime((FileTime) null));
        assertThrows(NullPointerException.class, () -> FileTimes.toNtfsTime((Instant) null));
    }

    // ------------------------------
    // Arithmetic helpers (plus/minus)
    // ------------------------------

    @Test
    public void plus_minus_zero_are_idempotent() {
        assertEquals(FT_EPOCH, FileTimes.plusNanos(FT_EPOCH, 0L));
        assertEquals(FT_EPOCH, FileTimes.minusMillis(FT_EPOCH, 0L));
        assertEquals(FT_EPOCH, FileTimes.minusSeconds(FT_EPOCH, 0L));
    }

    @Test
    public void plus_and_minus_change_time_when_non_zero() {
        assertEquals(FT_1S, FileTimes.plusSeconds(FT_EPOCH, 1L));
        assertNotEquals(FT_EPOCH, FileTimes.plusMillis(FT_EPOCH, 1L));
        assertNotEquals(FT_EPOCH, FileTimes.minusNanos(FT_EPOCH, 1L));
    }

    @Test
    public void plusSeconds_overflow_in_millis_conversion_throws_arithmetic_exception() {
        // Very large secondsToAdd will overflow when converted to milliseconds internally.
        assertThrows(ArithmeticException.class, () -> FileTimes.plusSeconds(FT_EPOCH, Long.MAX_VALUE));
    }

    @Test
    public void plusSeconds_past_instant_max_throws_datetime_exception() {
        FileTime nearMax = FileTime.from(Instant.MAX);
        assertThrows(DateTimeException.class, () -> FileTimes.plusSeconds(nearMax, 1L));
    }

    @Test
    public void minusSeconds_past_instant_min_throws_datetime_exception() {
        FileTime nearMin = FileTime.from(Instant.MIN);
        assertThrows(DateTimeException.class, () -> FileTimes.minusSeconds(nearMin, 1L));
    }

    @Test
    public void plus_minus_null_arguments_throw_npe() {
        assertThrows(NullPointerException.class, () -> FileTimes.plusMillis(null, 1L));
        assertThrows(NullPointerException.class, () -> FileTimes.plusNanos(null, 1L));
        assertThrows(NullPointerException.class, () -> FileTimes.plusSeconds(null, 1L));

        assertThrows(NullPointerException.class, () -> FileTimes.minusMillis(null, 1L));
        assertThrows(NullPointerException.class, () -> FileTimes.minusNanos(null, 1L));
        assertThrows(NullPointerException.class, () -> FileTimes.minusSeconds(null, 1L));
    }

    // ------------------------------
    // setLastModifiedTime
    // ------------------------------

    @Test
    public void setLastModifiedTime_null_path_throws_npe() {
        assertThrows(NullPointerException.class, () -> FileTimes.setLastModifiedTime((Path) null));
    }
}