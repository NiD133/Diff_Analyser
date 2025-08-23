package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.NonWritableChannelException;
import java.util.Collections;

/**
 * Unit tests for the {@link MultiReadOnlySeekableByteChannel} class.
 */
public class MultiReadOnlySeekableByteChannelTest {

    /**
     * Verifies that attempting to write to a MultiReadOnlySeekableByteChannel
     * throws a NonWritableChannelException, enforcing its read-only contract.
     */
    @Test(expected = NonWritableChannelException.class)
    public void writeShouldThrowNonWritableChannelException() throws IOException {
        // Arrange: Create a read-only channel. For this test, it can be empty.
        MultiReadOnlySeekableByteChannel readOnlyChannel =
            new MultiReadOnlySeekableByteChannel(Collections.emptyList());
        ByteBuffer dummyBuffer = ByteBuffer.allocate(1);

        // Act & Assert: Attempting to write should throw the expected exception.
        readOnlyChannel.write(dummyBuffer);
    }
}