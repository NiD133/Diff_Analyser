package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;

import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;
import org.junit.Test;

/**
 * Tests for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that subtracting zero milliseconds from a FileTime results in an equal FileTime.
     */
    @Test
    public void minusMillisWithZeroShouldReturnEqualFileTime() {
        // Arrange: Create a starting FileTime instance.
        // Using a clear, arbitrary value like "one day after epoch" avoids magic numbers.
        final FileTime originalFileTime = FileTime.from(1, TimeUnit.DAYS);
        final long millisToSubtract = 0L;

        // Act: Call the method under test.
        final FileTime resultFileTime = FileTimes.minusMillis(originalFileTime, millisToSubtract);

        // Assert: The resulting FileTime should be identical to the original.
        assertEquals(originalFileTime, resultFileTime);
    }
}