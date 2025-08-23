package org.apache.commons.compress.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 */
public class MultiReadOnlySeekableByteChannelTest {

    @Rule
    public final TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Tests that setting the position to a value greater than the channel's total size
     * is a valid operation and correctly updates the current position. According to the
     * SeekableByteChannel contract, this is legal and should not change the channel's size.
     */
    @Test
    public void testPositionCanBeSetBeyondChannelSize() throws IOException {
        // Arrange: Create a multi-channel from two empty files.
        // The total size of the combined channel will be 0.
        File part1 = tempFolder.newFile("part1.txt");
        File part2 = tempFolder.newFile("part2.txt");
        File[] files = {part1, part2};

        long positionBeyondSize = 1024L;

        // Act & Assert
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(files)) {
            // Set the position to a value larger than the channel's size (0).
            channel.position(positionBeyondSize);

            // Verify that the position was updated correctly.
            long newPosition = channel.position();
            assertEquals("Position should be updatable to a value greater than the channel's size.",
                positionBeyondSize, newPosition);
        }
    }
}