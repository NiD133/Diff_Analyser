package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SeekableInMemoryByteChannel}.
 */
class SeekableInMemoryByteChannelTest {

    private static final byte[] TEST_DATA = "Some data".getBytes(UTF_8);
    private SeekableInMemoryByteChannel channel;

    @BeforeEach
    void setUp() {
        channel = new SeekableInMemoryByteChannel();
    }

    @Test
    void write_whenPositionIsAfterEnd_growsChannelAndFillsGapWithNullBytes() throws IOException {
        // Arrange
        final int gapSize = 2;
        final byte[] expectedData = new byte[gapSize + TEST_DATA.length];
        System.arraycopy(TEST_DATA, 0, expectedData, gapSize, TEST_DATA.length);

        // Act
        channel.position(gapSize);
        final int bytesWritten = channel.write(ByteBuffer.wrap(TEST_DATA));

        // Assert
        assertEquals(TEST_DATA.length, bytesWritten, "Should write all bytes from the buffer.");
        assertEquals(expectedData.length, channel.size(), "Channel size should include the gap and the written data.");
        assertEquals(expectedData.length, channel.position(), "Position should be at the end of the newly written data.");

        // Verify the entire content, including the gap
        final byte[] actualData = new byte[expectedData.length];
        channel.position(0);
        channel.read(ByteBuffer.wrap(actualData));
        assertArrayEquals(expectedData, actualData, "The gap should be filled with null bytes.");
    }

    @Test
    void close_whenCalledMultipleTimes_hasNoEffectAfterFirstCall() throws IOException {
        // Arrange
        // The channel is created in setUp() and closed by try-with-resources.
        // We call close() explicitly to test idempotency.
        
        // Act
        channel.close();
        assertFalse(channel.isOpen(), "Channel should be closed after the first call.");

        // Act again
        channel.close();
        assertFalse(channel.isOpen(), "Channel should remain closed on subsequent calls.");
    }

    @Test
    void read_whenChannelIsClosed_throwsClosedChannelException() throws IOException {
        // Arrange
        channel.write(ByteBuffer.wrap(TEST_DATA));
        channel.close();

        // Act & Assert
        assertThrows(ClosedChannelException.class, () -> {
            channel.read(ByteBuffer.allocate(10));
        });
    }

    @Test
    void write_whenChannelIsClosed_throwsClosedChannelException() throws IOException {
        // Arrange
        channel.close();

        // Act & Assert
        assertThrows(ClosedChannelException.class, () -> {
            channel.write(ByteBuffer.wrap(TEST_DATA));
        });
    }

    @Test
    void position_whenChannelIsClosed_returnsPositionAtTimeOfClosing() throws IOException {
        // This test verifies behavior that deviates from the SeekableByteChannel interface,
        // as documented in SeekableInMemoryByteChannel.
        
        // Arrange
        final long expectedPosition = 5;
        channel.write(ByteBuffer.wrap(TEST_DATA));
        channel.position(expectedPosition);
        
        // Act
        channel.close();

        // Assert
        assertEquals(expectedPosition, channel.position());
    }

    @Test
    void size_whenChannelIsClosed_returnsSizeAtTimeOfClosing() throws IOException {
        // This test verifies behavior that deviates from the SeekableByteChannel interface,
        // as documented in SeekableInMemoryByteChannel.

        // Arrange
        channel.write(ByteBuffer.wrap(TEST_DATA));
        
        // Act
        channel.close();

        // Assert
        assertEquals(TEST_DATA.length, channel.size());
    }
}