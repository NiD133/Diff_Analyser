package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link SeekableInMemoryByteChannel} class.
 * This class focuses on verifying the behavior of the channel's open state.
 */
public class SeekableInMemoryByteChannel_ESTestTest20 {

    /**
     * Verifies that a newly created SeekableInMemoryByteChannel is in an open state by default.
     */
    @Test
    public void newChannelShouldBeOpen() {
        // Arrange: Create a new instance of the channel. The initial capacity is not
        // relevant to this test, so a small value is used.
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel(1);

        // Act: The action is implicitly the channel's state after creation.
        // We will directly assert on the result of isOpen().

        // Assert: The channel should report that it is open.
        assertTrue("A new channel should be open immediately after creation.", channel.isOpen());
    }
}