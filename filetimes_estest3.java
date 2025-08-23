package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import org.junit.Test;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#toUnixTime(FileTime)} correctly converts a
     * {@link FileTime} object to its equivalent Unix timestamp in seconds.
     */
    @Test
    public void testToUnixTimeConvertsFileTimeFromKnownInstant() {
        // Arrange: Create a FileTime from a known Instant.
        // The original test relied on a mocked clock returning this specific time.
        // Using a constant makes the test deterministic and its logic clear.
        final long expectedSecondsSinceEpoch = 1392409281L; // Represents 2014-02-14T20:21:21Z
        final Instant instant = Instant.ofEpochSecond(expectedSecondsSinceEpoch);
        final FileTime fileTime = FileTime.from(instant);

        // Act: Convert the FileTime back to a Unix timestamp.
        final long actualSecondsSinceEpoch = FileTimes.toUnixTime(fileTime);

        // Assert: The converted timestamp should match the original value.
        assertEquals(expectedSecondsSinceEpoch, actualSecondsSinceEpoch);
    }
}