package org.apache.commons.compress.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;

/**
 * Contains tests for the {@link MultiReadOnlySeekableByteChannel} class.
 */
public class MultiReadOnlySeekableByteChannelTest {

    @Rule
    public final TemporaryFolder tempFolder = new TemporaryFolder();

    @Test(expected = ClosedChannelException.class)
    public void readFromClosedChannelShouldThrowException() throws IOException {
        // Arrange: Create a multi-file channel from two temporary files and then close it.
        final File file1 = tempFolder.newFile();
        final File file2 = tempFolder.newFile();
        final SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(file1, file2);
        channel.close();

        final ByteBuffer buffer = ByteBuffer.allocate(10);

        // Act: Attempt to read from the now-closed channel.
        // Assert: The @Test(expected) annotation asserts that a ClosedChannelException is thrown.
        channel.read(buffer);
    }
}