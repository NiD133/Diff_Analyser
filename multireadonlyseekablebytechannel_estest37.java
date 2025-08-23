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
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Verifies that a newly created MultiReadOnlySeekableByteChannel
     * has an initial position of 0.
     */
    @Test
    public void initialPositionShouldBeZero() throws IOException {
        // Arrange: Create a set of empty files to be concatenated.
        // The content of the files is irrelevant for this test.
        File file1 = tempFolder.newFile("test1.txt");
        File file2 = tempFolder.newFile("test2.txt");
        File[] inputFiles = {file1, file2};

        // Act: Create the multi-channel and get its initial position.
        // A try-with-resources statement ensures the channel is closed automatically.
        try (SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forFiles(inputFiles)) {
            long initialPosition = multiChannel.position();

            // Assert: The initial position should be zero.
            assertEquals("A new channel's initial position should be 0.", 0L, initialPosition);
        }
    }
}