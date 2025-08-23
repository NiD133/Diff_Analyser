package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * The NTFS epoch (1601-01-01T00:00:00Z) represented as milliseconds from the
     * Java/Unix epoch (1970-01-01T00:00:00Z).
     */
    private static final long NTFS_EPOCH_AS_JAVA_MILLIS = -11644473600000L;

    @Test
    public void toNtfsTime_withNtfsEpochInMillis_shouldReturnZero() {
        // The method under test converts a Java time (milliseconds since the Unix epoch)
        // to an NTFS time (100-nanosecond intervals since the NTFS epoch).
        // When the input is the NTFS epoch itself, the result should be 0.
        final long actualNtfsTime = FileTimes.toNtfsTime(NTFS_EPOCH_AS_JAVA_MILLIS);

        assertEquals("The NTFS time for the NTFS epoch should be 0", 0L, actualNtfsTime);
    }
}