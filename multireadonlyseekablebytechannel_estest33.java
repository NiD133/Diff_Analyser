package org.apache.commons.compress.utils;

import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class MultiReadOnlySeekableByteChannel_ESTestTest33 extends MultiReadOnlySeekableByteChannel_ESTest_scaffolding {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    /**
     * Verifies that attempting to change the position of a closed
     * MultiReadOnlySeekableByteChannel throws a ClosedChannelException.
     */
    @Test
    public void positionShouldThrowExceptionWhenChannelIsClosed() throws IOException {
        // Arrange: Create a multi-channel from two temporary files.
        File file1 = tempFolder.newFile("test1.bin");
        File file2 = tempFolder.newFile("test2.bin");
        final SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forFiles(file1, file2);

        // Act: Close the channel before attempting to use it.
        channel.close();

        // Assert: Expect a ClosedChannelException when trying to set the position.
        assertThrows(ClosedChannelException.class, () -> channel.position(0L));
    }
}