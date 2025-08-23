package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import org.junit.Test;

/**
 * Unit tests for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#plusMillis(FileTime, long)} returns a new, later FileTime
     * when a positive duration is added.
     */
    @Test
    public void testPlusMillisWithPositiveDurationReturnsLaterFileTime() {
        // Arrange: Set up a known starting point and a duration to add.
        final FileTime originalFileTime = FileTime.from(Instant.EPOCH);
        final long millisToAdd = 1000L;

        // Act: Call the method under test.
        final FileTime newFileTime = FileTimes.plusMillis(originalFileTime, millisToAdd);

        // Assert: Verify the result is a new, distinct, and later time.
        assertNotEquals("The returned FileTime should be a new instance.", originalFileTime, newFileTime);
        assertTrue("The new FileTime should be later than the original.", newFileTime.compareTo(originalFileTime) > 0);
    }
}