package org.apache.commons.compress.utils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.Collections;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 * This focuses on the non-standard behavior of the position() method on a closed channel.
 */
public class MultiReadOnlySeekableByteChannelTestTest19 {

    /**
     * The Javadoc for {@link MultiReadOnlySeekableByteChannel#position()} states that it
     * deliberately violates the {@link SeekableByteChannel} contract. Instead of throwing a
     * {@link ClosedChannelException} when called on a closed channel, it returns the
     * last known position. This test verifies this specific behavior.
     */
    @Test
    void positionOnClosedChannelReturnsLastPositionWithoutException() throws IOException {
        // Arrange
        final long expectedPosition = 5L;
        // Use a channel with content to allow setting a non-zero position.
        SeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(
            Collections.singletonList(new SeekableInMemoryByteChannel(new byte[10]))
        );

        // Set a position and verify it before closing.
        channel.position(expectedPosition);
        assertEquals(expectedPosition, channel.position(), "Position should be set correctly before close");

        // Act
        channel.close();
        assertFalse(channel.isOpen(), "Channel should be closed");

        // Assert
        // Verify that calling position() on a closed channel does not throw an exception
        // and returns the last known position.
        long positionAfterClose = assertDoesNotThrow(channel::position,
            "position() on a closed channel should not throw an exception.");

        assertEquals(expectedPosition, positionAfterClose,
            "Position after close should be the last known position.");
    }

    /**
     * Verifies that other methods, like read(), still throw a ClosedChannelException
     * on a closed channel as expected by the interface contract.
     */
    @Test
    void readOnClosedChannelThrowsException() throws IOException {
        // Arrange
        SeekableByteChannel channel = new MultiReadOnlySeekableByteChannel(
            Collections.singletonList(new SeekableInMemoryByteChannel(new byte[10]))
        );
        assertTrue(channel.isOpen());
        channel.close();
        assertFalse(channel.isOpen());

        // Act & Assert
        ByteBuffer buffer = ByteBuffer.allocate(1);
        assertThrows(ClosedChannelException.class, () -> channel.read(buffer),
            "read() on a closed channel should throw ClosedChannelException.");
    }
}