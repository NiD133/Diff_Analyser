package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import org.junit.Test;

/**
 * Tests for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#plusSeconds(FileTime, long)} returns a new FileTime
     * instance with the correctly advanced time.
     */
    @Test
    public void plusSecondsReturnsNewInstanceWithCorrectValue() {
        // Arrange: Define the base time, the duration to add, and the expected result.
        final FileTime baseTime = FileTimes.EPOCH; // 1970-01-01T00:00:00Z
        final long secondsToAdd = 690L; // 11 minutes and 30 seconds
        final FileTime expectedTime = FileTime.from(Instant.ofEpochSecond(secondsToAdd));

        // Act: Add the specified number of seconds to the base time.
        final FileTime actualTime = FileTimes.plusSeconds(baseTime, secondsToAdd);

        // Assert: Verify the result has the correct time value and is a new instance.
        assertEquals("The resulting FileTime should be advanced by the correct number of seconds.",
            expectedTime, actualTime);
        assertNotSame("The method should return a new FileTime instance, not modify the original.",
            baseTime, actualTime);
    }
}