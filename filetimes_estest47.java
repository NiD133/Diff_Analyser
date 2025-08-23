package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import org.junit.Test;

/**
 * Contains an improved test case for the {@link FileTimes} class.
 * The original test was auto-generated and lacked clarity.
 */
// The original test class name and inheritance are preserved to match the context.
public class FileTimes_ESTestTest47 extends FileTimes_ESTest_scaffolding {

    /**
     * The offset between the NTFS file time epoch (January 1, 1601) and the Unix epoch (January 1, 1970),
     * expressed in 100-nanosecond intervals. This constant represents the expected NTFS time
     * for a {@code FileTime} at the Unix epoch.
     */
    private static final long NTFS_EPOCH_OFFSET_HUNDREDS_OF_NANOS = 116444736000000000L;

    /**
     * Tests that {@link FileTimes#toNtfsTime(FileTime)} correctly converts a {@link FileTime}
     * representing the Unix epoch into its corresponding NTFS time.
     */
    @Test
    public void toNtfsTime_withUnixEpoch_shouldReturnCorrectOffset() {
        // Arrange: Create a FileTime representing the Unix epoch (1970-01-01T00:00:00Z).
        final FileTime unixEpoch = FileTime.from(Instant.EPOCH);

        // Act: Convert the epoch FileTime to its equivalent NTFS time.
        final long actualNtfsTime = FileTimes.toNtfsTime(unixEpoch);

        // Assert: The result should be the well-known offset between the NTFS and Unix epochs.
        assertEquals(NTFS_EPOCH_OFFSET_HUNDREDS_OF_NANOS, actualNtfsTime);
    }
}