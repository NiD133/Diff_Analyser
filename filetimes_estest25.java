package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.nio.file.attribute.FileTime;
import java.time.DateTimeException;
import org.junit.Test;

/**
 * Tests edge cases for the {@link FileTimes} utility class.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#plusNanos(FileTime, long)} throws a DateTimeException
     * when the calculation results in an underflow of the underlying {@link java.time.Instant}.
     */
    @Test
    public void testPlusNanosThrowsExceptionOnInstantUnderflow() {
        // Arrange: Create a FileTime representing the earliest possible Unix time to set up
        // the underflow condition.
        final FileTime earliestUnixFileTime = FileTimes.fromUnixTime(Long.MIN_VALUE);

        // Act & Assert: Attempting to subtract a very large duration (by adding a large
        // negative number of nanoseconds) should cause the Instant to go below its minimum
        // supported value, triggering a DateTimeException.
        final DateTimeException exception = assertThrows(DateTimeException.class, () -> {
            FileTimes.plusNanos(earliestUnixFileTime, Long.MIN_VALUE);
        });

        // Verify the exception message to confirm the cause of the error.
        assertEquals("Instant exceeds minimum or maximum instant", exception.getMessage());
    }
}