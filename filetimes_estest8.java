package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;

import java.time.Instant;
import org.junit.Test;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    @Test
    public void toNtfsTimeShouldCorrectlyConvertPreEpochInstant() {
        // Arrange
        // An Instant representing a time just before the Unix epoch (1970-01-01T00:00:00Z).
        final long millisBeforeEpoch = -1978L;
        final Instant preEpochInstant = Instant.ofEpochMilli(millisBeforeEpoch);

        // The expected NTFS time is calculated by taking the NTFS time for the Unix epoch
        // and subtracting the duration before the epoch, converted to 100-nanosecond intervals.
        // Note: UNIX_TO_NTFS_OFFSET is negative, so we use its negation to get the epoch offset.
        final long unixEpochAsNtfs = -FileTimes.UNIX_TO_NTFS_OFFSET;
        final long offsetIn100Nanos = millisBeforeEpoch * FileTimes.HUNDRED_NANOS_PER_MILLISECOND;
        final long expectedNtfsTime = unixEpochAsNtfs + offsetIn100Nanos;

        // Act
        final long actualNtfsTime = FileTimes.toNtfsTime(preEpochInstant);

        // Assert
        assertEquals("The NTFS time for an instant before the Unix epoch should be calculated correctly.",
                     expectedNtfsTime, actualNtfsTime);
    }
}