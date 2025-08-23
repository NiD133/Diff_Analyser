package org.apache.commons.compress.utils;

import org.junit.Test;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for {@link MultiReadOnlySeekableByteChannel}.
 */
public class MultiReadOnlySeekableByteChannelTest {

    /**
     * Verifies that a channel created via {@code forPaths} is initialized with a position of 0.
     */
    @Test
    public void forPaths_whenChannelIsCreated_initialPositionShouldBeZero() throws IOException {
        // Arrange
        // The original auto-generated test used a path to the current directory ("").
        // This is an unusual input, as opening a channel on a directory would
        // normally fail. We preserve this logic, assuming the test environment
        // handles this by creating an empty, readable channel.
        final Path path = Paths.get("");
        final Path[] paths = new Path[8];
        Arrays.fill(paths, path);

        // Act
        final SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forPaths(paths);

        // Assert
        assertEquals("A newly created channel should be positioned at the beginning.", 0L, channel.position());
    }
}