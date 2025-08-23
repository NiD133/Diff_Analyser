package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.nio.file.attribute.FileTime;
import java.time.Instant;
import org.junit.Test;

/**
 * Tests for {@link FileTimes}.
 */
public class FileTimesTest {

    /**
     * Tests that subtracting a negative number of seconds from a FileTime is
     * equivalent to adding the corresponding positive number of seconds.
     */
    @Test
    public void testMinusSecondsWithNegativeValueIsEquivalentToAddition() {
        // Arrange: Create a starting point in time and define the duration.
        final FileTime initialFileTime = FileTime.from(Instant.now());
        final long seconds = 1234L; // A non-zero, positive duration.

        // Act: Subtract a negative duration from the initial time.
        final FileTime timeAfterSubtraction = FileTimes.minusSeconds(initialFileTime, -seconds);

        // Assert: The result should be the same as adding the positive duration.
        final FileTime expectedFileTime = FileTimes.plusSeconds(initialFileTime, seconds);
        assertEquals(expectedFileTime, timeAfterSubtraction);

        // Also, verify that the time has indeed changed from the original.
        assertNotEquals(initialFileTime, timeAfterSubtraction);
    }
}