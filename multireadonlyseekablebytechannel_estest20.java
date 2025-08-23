package org.apache.commons.compress.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for {@link MultiReadOnlySeekableByteChannel}.
 */
public class MultiReadOnlySeekableByteChannelTest {

    // The TemporaryFolder rule ensures that files created are deleted after the test runs.
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Tests that setting the position beyond the end of the channel is a valid
     * operation and correctly updates the channel's position. This is consistent
     * with the contract of {@link SeekableByteChannel#position(long)}.
     */
    @Test
    public void settingPositionBeyondChannelSizeShouldUpdatePosition() throws IOException {
        // Arrange
        // Create a multi-channel from two empty files. The resulting channel will have a size of 0.
        File emptyFile1 = tempFolder.newFile("emptyFile1.bin");
        File emptyFile2 = tempFolder.newFile("emptyFile2.bin");

        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(emptyFile1, emptyFile2)) {
            // Sanity check to confirm our setup is correct.
            assertEquals("Channel size should be zero for two empty files.", 0L, channel.size());

            final long positionBeyondSize = 500L;

            // Act
            // Set the position to a value greater than the channel's current size.
            // This is allowed and should not throw an exception. A subsequent read
            // from this position would simply return an end-of-file indication (-1).
            channel.position(positionBeyondSize);

            // Assert
            // Verify that the channel's position is updated to the new value.
            assertEquals("The position should be updatable to a value beyond the channel's size.",
                    positionBeyondSize, channel.position());
        }
    }
}