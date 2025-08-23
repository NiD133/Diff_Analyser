package org.apache.commons.compress.utils;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
public class SeekableInMemoryByteChannelTest {

    /**
     * Verifies that attempting to set a negative position on the channel
     * throws an IOException with a specific, informative message.
     */
    @Test
    public void shouldThrowIOExceptionWhenSettingNegativePosition() {
        // Arrange: Create a channel. The initial content is not relevant for this test.
        SeekableInMemoryByteChannel channel = new SeekableInMemoryByteChannel();
        final long negativePosition = -1L;

        try {
            // Act: Attempt to set the position to a negative value.
            channel.position(negativePosition);
            fail("Expected an IOException to be thrown for a negative position, but none was.");
        } catch (final IOException e) {
            // Assert: Verify that the correct exception was thrown with the expected message.
            final String expectedMessage = "Position must be in range [0.." + Integer.MAX_VALUE + "]";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}