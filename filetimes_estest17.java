package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertFalse;

import java.nio.file.attribute.FileTime;
import org.junit.Test;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#isUnixTime(FileTime)} returns false for a FileTime
     * representing a time far in the past, before the NTFS epoch (1601).
     *
     * <p>Such a time is valid as a {@code FileTime}, but its equivalent value in seconds
     * since the Unix epoch (1970) falls outside the standard representable range.
     * </p>
     */
    @Test
    public void isUnixTimeShouldReturnFalseForTimeBeforeNtfsEpoch() {
        // Arrange: An NTFS time is the number of 100-nanosecond intervals since 1601-01-01.
        // A negative value represents a time before this epoch.
        final long negativeNtfsTime = -165L;
        final FileTime timeBefore1601 = FileTimes.ntfsTimeToFileTime(negativeNtfsTime);

        // Act: Check if this historical time is a valid Unix time.
        final boolean isUnixTime = FileTimes.isUnixTime(timeBefore1601);

        // Assert: The result should be false, as the time is out of the standard Unix time range.
        assertFalse("A time before 1601 should not be considered a valid Unix time.", isUnixTime);
    }
}