package org.apache.commons.io.file.attribute;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.file.attribute.FileTime;
import java.time.DateTimeException;
import java.time.Instant;
import org.junit.Test;

/**
 * Contains tests for the {@link FileTimes} utility class, focusing on edge cases.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#minusNanos(FileTime, long)} throws a DateTimeException
     * when subtracting a negative value from a FileTime representing the maximum possible
     * instant, which causes a time overflow.
     */
    @Test
    public void minusNanosWithNegativeValueShouldThrowExceptionOnOverflow() {
        // Arrange: Create a FileTime at the maximum representable instant.
        // Subtracting a negative number of nanoseconds is equivalent to adding,
        // which should cause an overflow when performed on the maximum time value.
        final FileTime maxFileTime = FileTime.from(Instant.MAX);
        final long negativeNanosToSubtract = -1174L;

        // Act & Assert: The operation should overflow and throw a DateTimeException.
        try {
            FileTimes.minusNanos(maxFileTime, negativeNanosToSubtract);
            fail("Expected a DateTimeException to be thrown due to instant overflow, but it was not.");
        } catch (final DateTimeException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals("Instant exceeds minimum or maximum instant", e.getMessage());
        }
    }
}