package org.apache.commons.compress.utils;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link SeekableInMemoryByteChannel} class.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that isOpen() returns false after the channel has been explicitly closed.
     */
    @Test
    public void isOpenShouldReturnFalseAfterChannelIsClosed() {
        // Arrange: Create a new channel, which should be open by default.
        final SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        assertTrue("A new channel should be open upon creation.", channel.isOpen());

        // Act: Close the channel.
        channel.close();

        // Assert: Verify the channel is now reported as closed.
        assertFalse("isOpen() should return false after close() has been called.", channel.isOpen());
    }
}