package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * Tests for the {@link FileTimes} utility class, focusing on time conversions.
 */
public class FileTimesTest {

    /**
     * Tests that converting a {@link FileTime} to a {@link Date} and then to an
     * NTFS time representation yields the correct numerical value.
     */
    @Test
    public void testToNtfsTimeFromDate() {
        // Arrange: Define a test time slightly after the Unix epoch.
        final long millisSinceEpoch = 120L;
        final FileTime fileTime = FileTime.fromMillis(millisSinceEpoch);

        // The expected NTFS time is the sum of two components, both measured in
        // 100-nanosecond intervals:
        // 1. The fixed offset from the NTFS epoch (1601-01-01) to the Unix epoch (1970-01-01).
        final long ntfsEpochToUnixEpochOffset = 116444736000000000L;
        // 2. The number of 100-nanosecond intervals represented by our test milliseconds.
        final long intervalsPerMillisecond = TimeUnit.MILLISECONDS.toNanos(1) / 100;
        final long expectedNtfsTime = ntfsEpochToUnixEpochOffset + (millisSinceEpoch * intervalsPerMillisecond);

        // Act: Convert the FileTime to a Date, and then convert that Date to an NTFS time.
        final Date date = FileTimes.toDate(fileTime);
        final long actualNtfsTime = FileTimes.toNtfsTime(date);

        // Assert: Verify that the intermediate and final values are correct.
        assertNotNull("The intermediate Date object should not be null", date);
        assertEquals("The final NTFS time should be calculated correctly", expectedNtfsTime, actualNtfsTime);
    }
}