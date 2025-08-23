package org.apache.commons.compress.utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 * This test focuses on the open/closed state of the channel.
 */
public class MultiReadOnlySeekableByteChannelTest {

    // Rule to create temporary files and folders for testing, ensuring cleanup.
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    private File[] testFiles;

    @Before
    public void setUp() throws IOException {
        // Create two empty temporary files to be used by the channel.
        File file1 = temporaryFolder.newFile("test1.txt");
        File file2 = temporaryFolder.newFile("test2.txt");
        testFiles = new File[]{file1, file2};
    }

    /**
     * Verifies that the isOpen() method returns false after the channel has been explicitly closed.
     */
    @Test
    public void isOpenShouldReturnFalseAfterChannelIsClosed() throws IOException {
        // Arrange: Create a multi-file channel from a set of temporary files.
        // The channel is expected to be open immediately after creation.
        SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forFiles(testFiles);
        assertTrue("Channel should be open upon creation", multiChannel.isOpen());

        // Act: Close the channel.
        multiChannel.close();

        // Assert: Verify that the channel is now reported as closed.
        assertFalse("Channel should be closed after calling close()", multiChannel.isOpen());
    }
}