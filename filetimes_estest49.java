package org.apache.commons.io.file.attribute;

import org.junit.Test;

import java.nio.file.attribute.FileTime;
import java.time.Instant;

/**
 * Tests for the {@link FileTimes} class, focusing on arithmetic operations and edge cases.
 */
public class FileTimesTest {

    /**
     * Tests that {@link FileTimes#plusSeconds(FileTime, long)} throws an
     * {@link ArithmeticException} when the addition results in a value
     * that overflows the capacity of a {@code long} for epoch-seconds.
     */
    @Test(expected = ArithmeticException.class)
    public void plusSecondsShouldThrowArithmeticExceptionOnOverflow() {
        // Arrange: Create a starting FileTime. Any positive time combined with
        // Long.MAX_VALUE will cause an overflow. Using a fixed instant like
        // one second past the epoch makes the test deterministic.
        final FileTime startTime = FileTime.from(Instant.ofEpochSecond(1));
        final long secondsToAdd = Long.MAX_VALUE;

        // Act: Attempt to add a number of seconds that will cause the underlying
        // epoch-second calculation to overflow. The method is expected to throw.
        FileTimes.plusSeconds(startTime, secondsToAdd);

        // Assert: The 'expected' attribute of the @Test annotation handles the
        // verification that an ArithmeticException is thrown.
    }
}