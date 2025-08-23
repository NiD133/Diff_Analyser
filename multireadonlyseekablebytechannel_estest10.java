package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 */
public class MultiReadOnlySeekableByteChannelTest {

    /**
     * Tests that calling the two-argument position() method with negative values
     * throws an IllegalArgumentException.
     *
     * The method signature is position(long channelNumber, long relativeOffset).
     * The underlying implementation is expected to reject negative position offsets.
     */
    @Test
    public void positionWithNegativeArgumentsShouldThrowIllegalArgumentException() throws IOException {
        // Arrange: Create a channel with an empty list of backing channels.
        // The validation should occur before any channels are accessed.
        List<SeekableByteChannel> emptyChannelList = Collections.emptyList();
        MultiReadOnlySeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(emptyChannelList);

        // Act & Assert: Attempt to set a negative position and verify the exception.
        try {
            channel.position(-1L, -1L);
            fail("Expected an IllegalArgumentException to be thrown for a negative position.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception message is as expected.
            assertEquals("Negative position: -1", e.getMessage());
        }
    }
}