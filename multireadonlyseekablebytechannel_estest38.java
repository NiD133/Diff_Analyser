package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.Collections;
import java.util.List;

/**
 * Unit tests for the {@link MultiReadOnlySeekableByteChannel} class.
 */
public class MultiReadOnlySeekableByteChannelTest {

    /**
     * Verifies that attempting to truncate a MultiReadOnlySeekableByteChannel
     * throws a NonWritableChannelException, enforcing its read-only contract.
     */
    @Test(expected = NonWritableChannelException.class)
    public void truncateShouldThrowExceptionForReadOnlyChannel() throws IOException {
        // Arrange: Create a MultiReadOnlySeekableByteChannel.
        // The list of underlying channels can be empty for this test's purpose.
        final List<SeekableByteChannel> emptyChannelList = Collections.emptyList();
        final SeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(emptyChannelList);

        // Act: Attempt to call the truncate method, which is a write operation.
        channel.truncate(0L);

        // Assert: The test expects a NonWritableChannelException, as declared in the @Test annotation.
    }
}