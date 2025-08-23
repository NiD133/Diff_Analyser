package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.NonWritableChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 */
class MultiReadOnlySeekableByteChannelTest {

    @Test
    @DisplayName("An empty multi-channel should behave like a single empty channel")
    void emptyChannelShouldBehaveCorrectly() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels()) {
            final ByteBuffer buf = ByteBuffer.allocate(10);

            // Act & Assert
            assertAll("Empty Channel Behavior",
                () -> assertTrue(channel.isOpen(), "Channel should be open initially"),
                () -> assertEquals(0, channel.size(), "Size should be 0"),
                () -> assertEquals(0, channel.position(), "Position should be 0"),
                () -> assertEquals(-1, channel.read(buf), "Read should return -1 for EOF"),
                () -> {
                    // Attempting to read past the end should still return -1
                    channel.position(5);
                    assertEquals(-1, channel.read(buf), "Read after positioning past the end should also return -1");
                }
            );

            // Assert behavior after close
            channel.close();
            assertFalse(channel.isOpen(), "Channel should be closed");
            assertThrows(ClosedChannelException.class, () -> channel.read(buf), "Read on closed channel should throw exception");
            assertThrows(ClosedChannelException.class, () -> channel.position(100), "Position on closed channel should throw exception");
        }
    }

    @ParameterizedTest(name = "[{index}] data size={0} bytes, chunk size={1}, buffer size={2}")
    @MethodSource("readConfigurations")
    @DisplayName("Read should return correct data for various channel and buffer configurations")
    void readShouldReturnCorrectDataForVariousConfigurations(final byte[] expectedData, final int channelChunkSize, final int readBufferSize) throws IOException {
        // Arrange
        final byte[][] chunks = splitIntoChunks(expectedData, channelChunkSize);
        try (SeekableByteChannel channel = createMultiChannel(chunks)) {
            assertEquals(expectedData.length, channel.size());
            final ByteBuffer resultBuffer = ByteBuffer.allocate(expectedData.length);
            final ByteBuffer readBuffer = ByteBuffer.allocate(readBufferSize);

            // Act: Read the entire channel content
            while (channel.read(readBuffer) != -1) {
                readBuffer.flip();
                resultBuffer.put(readBuffer);
                readBuffer.clear();
            }
            final byte[] actualData = resultBuffer.array();

            // Assert
            assertArrayEquals(expectedData, actualData,
                () -> String.format("Data mismatch for chunk size %d and read buffer size %d", channelChunkSize, readBufferSize));
        }
    }

    @Test
    @DisplayName("Positioning to a negative offset should throw IllegalArgumentException")
    void positionToNegativeOffsetShouldThrowException() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels()) {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> channel.position(-1));
        }
    }

    @Test
    @DisplayName("Reading into a zero-capacity buffer should return 0 and not advance position")
    void readWithZeroCapacityBufferShouldReturnZero() throws IOException {
        // Arrange
        byte[] data = "test".getBytes(UTF_8);
        try (SeekableByteChannel channel = createMultiChannel(new byte[][]{data})) {
            // Act
            int bytesRead = channel.read(ByteBuffer.allocate(0));

            // Assert
            assertEquals(0, bytesRead, "Should read 0 bytes");
            assertEquals(0, channel.position(), "Position should not advance");
        }
    }

    @Test
    @DisplayName("Write operation should throw NonWritableChannelException")
    void writeShouldThrowException() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels()) {
            assertThrows(NonWritableChannelException.class, () -> channel.write(ByteBuffer.allocate(1)));
        }
    }

    @Test
    @DisplayName("Truncate operation should throw NonWritableChannelException")
    void truncateShouldThrowException() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels()) {
            assertThrows(NonWritableChannelException.class, () -> channel.truncate(1));
        }
    }

    @Test
    @DisplayName("Close should propagate exceptions from underlying channels")
    void closeShouldPropagateExceptions() {
        // Arrange
        final ThrowingSeekableByteChannel throwingChannel = new ThrowingSeekableByteChannel();
        final SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(throwingChannel);

        // Act & Assert
        IOException e = assertThrows(IOException.class, multiChannel::close, "IOException from underlying channel should be propagated");
        assertEquals("Simulated I/O Error on close", e.getMessage());
    }

    // =================================================================
    // Test Data Providers and Helper Methods
    // =================================================================

    /**
     * Provides a stream of arguments for the parameterized read test.
     * Each argument consists of: { full data byte array, chunk size for splitting, read buffer size }
     */
    private static Stream<Arguments> readConfigurations() {
        final byte[] data = "0123456789ABCDEF".getBytes(UTF_8); // 16 bytes
        return Stream.of(
            // --- Test with a single underlying channel ---
            Arguments.of(data, data.length, 5),              // Read buffer smaller than data
            Arguments.of(data, data.length, data.length),    // Read buffer same size as data
            Arguments.of(data, data.length, data.length + 5),// Read buffer larger than data

            // --- Test with multiple, evenly-sized underlying channels ---
            Arguments.of(data, 8, 3),  // 2 channels of 8 bytes, read buffer smaller than chunk
            Arguments.of(data, 4, 4),  // 4 channels of 4 bytes, read buffer same size as chunk
            Arguments.of(data, 2, 10), // 8 channels of 2 bytes, read buffer spans multiple chunks

            // --- Test with multiple, unevenly-sized underlying channels ---
            Arguments.of(data, 7, 3),  // Chunks: 7, 7, 2. Read buffer smaller than chunk
            Arguments.of(data, 7, 7),  // Read buffer same size as chunk
            Arguments.of(data, 7, 9),  // Read buffer spans chunk boundary
            Arguments.of(data, 7, 20)  // Read buffer larger than total data
        );
    }

    /**
     * Creates a {@link MultiReadOnlySeekableByteChannel} from an array of byte arrays.
     */
    private static SeekableByteChannel createMultiChannel(final byte[][] chunks) {
        final SeekableByteChannel[] channels = new SeekableByteChannel[chunks.length];
        for (int i = 0; i < chunks.length; i++) {
            channels[i] = new SeekableInMemoryByteChannel(chunks[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    /**
     * Splits a byte array into a list of smaller byte arrays of a given chunk size.
     */
    private static byte[][] splitIntoChunks(final byte[] input, final int chunkSize) {
        final List<byte[]> groups = new ArrayList<>();
        for (int idx = 0; idx < input.length; idx += chunkSize) {
            final int end = Math.min(input.length, idx + chunkSize);
            groups.add(Arrays.copyOfRange(input, idx, end));
        }
        return groups.toArray(new byte[0][]);
    }

    /**
     * A mock SeekableByteChannel that throws an exception on close() to test error propagation.
     */
    private static class ThrowingSeekableByteChannel implements SeekableByteChannel {
        private boolean open = true;

        @Override
        public void close() throws IOException {
            open = false;
            throw new IOException("Simulated I/O Error on close");
        }

        @Override
        public boolean isOpen() {
            return open;
        }

        @Override
        public long position() { return 0; }

        @Override
        public SeekableByteChannel position(long newPosition) { return this; }

        @Override
        public int read(ByteBuffer dst) { return -1; }

        @Override
        public long size() { return 0; }

        @Override
        public SeekableByteChannel truncate(long size) { return this; }

        @Override
        public int write(ByteBuffer src) { return 0; }
    }
}