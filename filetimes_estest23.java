package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.file.attribute.FileTime;
import java.time.DateTimeException;
import java.time.Instant;
import org.junit.Test;

/**
 * Contains tests for the {@link FileTimes} class.
 * This class has been refactored for clarity from an auto-generated test suite.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#plusSeconds(FileTime, long)} throws a {@link DateTimeException}
     * when the resulting time would exceed the maximum value representable by {@link Instant}.
     */
    @Test(timeout = 4000)
    public void plusSecondsThrowsExceptionWhenResultExceedsMaxInstant() {
        // Arrange: Set up the initial conditions for the test.
        // We use the epoch as a starting point and add a number of seconds so large
        // that it is guaranteed to cause an overflow.
        final FileTime startFileTime = FileTime.from(Instant.EPOCH);
        final long secondsToAdd = Long.MAX_VALUE;

        // Act & Assert: Execute the method and verify the outcome.
        try {
            FileTimes.plusSeconds(startFileTime, secondsToAdd);
            fail("Expected a DateTimeException because the resulting Instant would be out of bounds.");
        } catch (final DateTimeException e) {
            // The exception is expected. We can optionally verify its message
            // to confirm it was thrown for the correct reason. This message
            // originates from java.time.Instant.plusSeconds().
            assertEquals("Instant exceeds minimum or maximum instant", e.getMessage());
        }
    }
}