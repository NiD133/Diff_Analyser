package org.apache.commons.compress.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;

/**
 * Contains tests for {@link MultiReadOnlySeekableByteChannel}.
 */
public class MultiReadOnlySeekableByteChannelTest {

    // Rule to create temporary files and folders, ensuring cleanup after the test.
    @Rule
    public final TemporaryFolder temporaryFolder = new TemporaryFolder();

    /**
     * Verifies that attempting to set the position on a closed channel
     * throws a ClosedChannelException.
     */
    @Test(expected = ClosedChannelException.class)
    public void shouldThrowExceptionWhenPositioningAClosedChannel() throws IOException {
        // Arrange: Create a multi-file channel from two temporary files.
        File file1 = temporaryFolder.newFile();
        File file2 = temporaryFolder.newFile();
        File[] files = {file1, file2};

        // The method under test, position(long, long), is specific to the implementation class,
        // so a cast is necessary.
        final MultiReadOnlySeekableByteChannel channel =
            (MultiReadOnlySeekableByteChannel) MultiReadOnlySeekableByteChannel.forFiles(files);

        // Close the channel to set up the test condition.
        channel.close();

        // Act & Assert: Attempting to set the position should throw the expected exception.
        channel.position(0L, 0L);
    }
}