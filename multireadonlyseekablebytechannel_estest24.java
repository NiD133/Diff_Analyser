package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Unit tests for the {@link MultiReadOnlySeekableByteChannel} class.
 */
public class MultiReadOnlySeekableByteChannelTest {

    /**
     * Tests that {@link MultiReadOnlySeekableByteChannel#forPaths(Path...)} throws a
     * NullPointerException if the input array contains a null element. The underlying
     * {@code java.nio.file.Files.newByteChannel} is expected to throw this exception.
     */
    @Test(expected = NullPointerException.class)
    public void forPathsThrowsNullPointerExceptionWhenArrayContainsNull() throws IOException {
        // Arrange: Create an array of Path objects with a null element.
        // A single-element array containing null is sufficient to demonstrate the behavior.
        final Path[] pathsWithNull = { null };

        // Act: This call is expected to throw a NullPointerException.
        MultiReadOnlySeekableByteChannel.forPaths(pathsWithNull);

        // Assert: The test passes if a NullPointerException is thrown,
        // as specified by the 'expected' attribute of the @Test annotation.
    }
}