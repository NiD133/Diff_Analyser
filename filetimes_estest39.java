package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#toNtfsTime(long)} correctly converts the
     * Unix epoch (0 milliseconds) to its equivalent NTFS time.
     * <p>
     * The NTFS time is the number of 100-nanosecond intervals since January 1, 1601.
     * The Unix epoch is January 1, 1970. The result of converting 0L (the Unix epoch)
     * should therefore be the constant offset between these two points in time.
     * </p>
     */
    @Test
    public void toNtfsTime_convertsUnixEpochMillisToNtfsOffset() {
        // Arrange: The input is the Unix epoch in milliseconds.
        final long unixEpochMillis = 0L;

        // The expected NTFS time for the Unix epoch is the constant offset
        // between the NTFS epoch (1601) and the Unix epoch (1970).
        // The constant in FileTimes is defined as a negative value, so we negate it
        // to get the positive offset value.
        final long expectedNtfsTimeForEpoch = -FileTimes.UNIX_TO_NTFS_OFFSET;

        // Act: Convert the Java time to NTFS time.
        final long actualNtfsTime = FileTimes.toNtfsTime(unixEpochMillis);

        // Assert: The result should match the known offset.
        assertEquals(expectedNtfsTimeForEpoch, actualNtfsTime);
    }
}