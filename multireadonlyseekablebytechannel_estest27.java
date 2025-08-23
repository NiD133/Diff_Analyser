package org.apache.commons.compress.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Contains tests for the {@link MultiReadOnlySeekableByteChannel} class.
 */
public class MultiReadOnlySeekableByteChannelTest {

    // Rule to create and manage temporary files, ensuring they are cleaned up after tests.
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Verifies that invoking size() on a channel that has already been closed
     * throws a ClosedChannelException.
     */
    @Test
    public void sizeShouldThrowExceptionWhenCalledOnClosedChannel() throws IOException {
        // Arrange: Create a multi-file channel from two temporary files with content.
        File file1 = tempFolder.newFile("test1.txt");
        Files.write(file1.toPath(), "content".getBytes(StandardCharsets.UTF_8));

        File file2 = tempFolder.newFile("test2.txt");
        Files.write(file2.toPath(), "more content".getBytes(StandardCharsets.UTF_8));

        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(file1, file2);
        assertNotNull("Channel should be created successfully", channel);

        // Act: Close the channel before attempting to access its properties.
        channel.close();

        // Assert: Expect a ClosedChannelException when trying to get the size.
        try {
            channel.size();
            fail("Expected a ClosedChannelException to be thrown after the channel was closed.");
        } catch (final ClosedChannelException e) {
            // This is the expected behavior. The test passes.
        }
    }
}