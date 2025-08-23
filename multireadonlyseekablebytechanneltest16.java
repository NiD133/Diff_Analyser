package org.apache.commons.compress.utils;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import org.junit.jupiter.api.Test;

/**
 * Tests for the position-related behavior of {@link MultiReadOnlySeekableByteChannel}.
 *
 * Note: The original class name 'MultiReadOnlySeekableByteChannelTestTest16'
 * was preserved. A more descriptive name would be e.g., 'MultiReadOnlySeekableByteChannelPositionTest'.
 */
public class MultiReadOnlySeekableByteChannelTestTest16 {

    /**
     * Tests that attempting to set a negative position throws an IllegalArgumentException.
     * The SeekableByteChannel contract specifies that a negative position is invalid.
     */
    @Test
    void positionShouldThrowIllegalArgumentExceptionForNegativeValue() throws IOException {
        // Arrange: Create a test channel instance.
        // The content is irrelevant, so using empty sources is a simple way to get an instance.
        try (SeekableByteChannel channel = createTestChannel()) {
            // Act & Assert: Verify that setting the position to -1 throws the expected exception.
            assertThrows(IllegalArgumentException.class, () -> channel.position(-1L));
        }
    }

    /**
     * Creates a MultiReadOnlySeekableByteChannel for testing purposes.
     *
     * @return A new instance of MultiReadOnlySeekableByteChannel.
     */
    private SeekableByteChannel createTestChannel() {
        // This test requires any valid instance of the class under test.
        // A channel composed of two empty in-memory channels is a simple and lightweight choice.
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(
            new SeekableInMemoryByteChannel(),
            new SeekableInMemoryByteChannel()
        );
    }
}