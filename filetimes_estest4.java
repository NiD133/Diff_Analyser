package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;

import java.nio.file.attribute.FileTime;
import org.junit.Test;

/**
 * Tests for {@link FileTimes} focusing on time conversions.
 */
// The original class name and inheritance are preserved for context.
// In a typical project, this might be named FileTimesTest.
public class FileTimes_ESTestTest4 extends FileTimes_ESTest_scaffolding {

    /**
     * Tests that the conversion from the NTFS epoch time to a Unix timestamp is correct.
     *
     * <p>The NTFS epoch (represented by 0L) corresponds to 1601-01-01 00:00:00 UTC. The Unix epoch
     * is 1970-01-01 00:00:00 UTC. This test verifies that the calculated Unix timestamp
     * for the NTFS epoch is the correct negative offset in seconds.</p>
     */
    @Test
    public void toUnixTime_shouldReturnCorrectNegativeTimestamp_whenConvertingNtfsEpoch() {
        // Arrange
        // The NTFS epoch is represented by 0 in 100-nanosecond intervals.
        final long ntfsEpochTime = 0L;

        // The expected Unix time is the number of seconds between the Unix epoch (1970)
        // and the NTFS epoch (1601). This value can be calculated from the offset constant
        // in the FileTimes class, which is defined in 100-nanosecond intervals.
        final long hundredNanosPerSecond = 10_000_000L;
        final long expectedUnixTimestamp = FileTimes.UNIX_TO_NTFS_OFFSET / hundredNanosPerSecond;
        // The expected value is -11644473600L.

        // Act
        // 1. Convert the NTFS epoch time to a FileTime object.
        final FileTime ntfsEpochAsFileTime = FileTimes.ntfsTimeToFileTime(ntfsEpochTime);
        // 2. Convert the resulting FileTime to a Unix timestamp (in seconds).
        final long actualUnixTimestamp = FileTimes.toUnixTime(ntfsEpochAsFileTime);

        // Assert
        assertEquals("The Unix timestamp for the NTFS epoch should be the correct negative offset.",
            expectedUnixTimestamp, actualUnixTimestamp);
    }
}