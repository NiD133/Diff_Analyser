package org.apache.commons.compress.utils;

import org.junit.Test;
import org.evosuite.runtime.mock.java.io.MockFile;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;

/**
 * This test class is a placeholder to contain the improved test case.
 * The original test was part of an auto-generated suite.
 */
public class MultiReadOnlySeekableByteChannel_ESTestTest8 extends MultiReadOnlySeekableByteChannel_ESTest_scaffolding {

    /**
     * Tests that attempting to read from a MultiReadOnlySeekableByteChannel
     * created from directories (instead of regular files) throws an IOException.
     * The underlying FileChannel cannot be read from a directory.
     */
    @Test(timeout = 4000, expected = IOException.class)
    public void readFromChannelOfDirectoriesShouldThrowIOException() throws IOException {
        // Arrange: Create a channel from one or more directories.
        // MockFile("") represents the current working directory.
        File directory = new MockFile("");
        File[] directories = {directory, directory};

        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(directories);
        ByteBuffer buffer = ByteBuffer.allocate(16);

        // Act & Assert: Attempting to read from the channel should throw an IOException
        // because the underlying resources are directories, not readable files.
        channel.read(buffer);
    }
}