package org.apache.commons.io.file.attribute;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.nio.file.attribute.FileTime;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Tests that converting an NTFS time to a FileTime and back results in the original value.
     * This test specifically uses a negative NTFS time, which represents a time before the
     * NTFS epoch (January 1, 1601).
     */
    @Test
    public void testNtfsTimeRoundtripConversionForNegativeValue() {
        // Arrange: Define an original NTFS time.
        // A negative value is a valid test case representing a time before the epoch.
        final long originalNtfsTime = -1401L;

        // Act: Convert the NTFS time to a FileTime and then convert it back.
        final FileTime intermediateFileTime = FileTimes.ntfsTimeToFileTime(originalNtfsTime);
        final long roundtripNtfsTime = FileTimes.toNtfsTime(intermediateFileTime);

        // Assert: The value after the roundtrip conversion should match the original.
        assertEquals(originalNtfsTime, roundtripNtfsTime);
    }
}