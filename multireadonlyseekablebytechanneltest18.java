package org.apache.commons.compress.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for reading from a {@link MultiReadOnlySeekableByteChannel}.
 */
class MultiReadOnlySeekableByteChannelReadTest {

    private static final byte[] TEST_DATA = "abcdefghijklmnopqrstuvwxyz".getBytes(StandardCharsets.UTF_8);

    /**
     * Provides arguments for the parameterized read test.
     * Each argument set includes a chunk size to split the source data into multiple channels,
     * and a read buffer size to test various reading scenarios.
     *
     * @return A stream of arguments: (chunkSize, readBufferSize, description).
     */
    static Stream<Arguments> readParameters() {
        return Stream.of(
            Arguments.of(3, 5, "Chunk size smaller than read buffer"),
            Arguments.of(5, 3, "Chunk size larger than read buffer"),
            Arguments.of(1, 1, "Single-byte chunks and reads"),
            Arguments.of(TEST_DATA.length, 30, "Single source channel, buffer larger than data"),
            Arguments.of(TEST_DATA.length, TEST_DATA.length, "Single source channel, buffer equals data size"),
            Arguments.of(5, 5, "Chunk size equals read buffer size")
        );
    }

    @ParameterizedTest(name = "[{index}] {2}")
    @MethodSource("readParameters")
    @DisplayName("Should read all bytes sequentially from concatenated channels")
    void read_returnsAllBytes_whenReadSequentially(int chunkSize, int readBufferSize, String description) throws IOException {
        // Arrange
        final byte[][] chunks =-splitIntoChunks(TEST_DATA, chunkSize);
        try (SeekableByteChannel multiChannel = createMultiChannel(chunks)) {

            // Assert initial state
            assertEquals(TEST_DATA.length, multiChannel.size(), "Initial size should match total data length");
            assertEquals(0, multiChannel.position(), "Initial position should be zero");

            // Act
            final byte[] actualBytes = readAllBytesFromChannel(multiChannel, readBufferSize);

            // Assert
            assertArrayEquals(TEST_DATA, actualBytes, "The fully read data should match the original data");
            assertEquals(TEST_DATA.length, multiChannel.position(), "Final position should be at the end of the channel");
        }
    }

    @Nested
    @DisplayName("when channel is empty")
    class EmptyChannelTests {

        @Test
        void emptyChannel_hasZeroSizeAndPosition() throws IOException {
            // Arrange
            try (SeekableByteChannel emptyChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels()) {
                // Act & Assert
                assertEquals(0, emptyChannel.size());
                assertEquals(0, emptyChannel.position());
            }
        }

        @Test
        void readFromEmptyChannel_returnsEOF() throws IOException {
            // Arrange
            try (SeekableByteChannel emptyChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels()) {
                final ByteBuffer buffer = ByteBuffer.allocate(10);

                // Act
                final int bytesRead = emptyChannel.read(buffer);

                // Assert
                assertEquals(-1, bytesRead, "Reading from an empty channel should immediately return -1 (EOF)");
            }
        }

        @Test
        void accessingClosedEmptyChannel_throwsException() throws IOException {
            // Arrange
            final SeekableByteChannel emptyChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels();
            final ByteBuffer buffer = ByteBuffer.allocate(10);
            emptyChannel.close(); // Close the channel before acting

            // Act & Assert
            assertFalse(emptyChannel.isOpen());
            assertThrows(ClosedChannelException.class, () -> emptyChannel.read(buffer),
                "Reading from a closed channel should throw ClosedChannelException");
            assertThrows(ClosedChannelException.class, () -> emptyChannel.position(1),
                "Positioning a closed channel should throw ClosedChannelException");
        }
    }

    @Test
    @DisplayName("Helper method 'splitIntoChunks' should split byte array correctly")
    void splitIntoChunks_splitsByteArray_correctly() {
        // Arrange
        final byte[] data = {1, 2, 3, 4, 5, 6, 7};

        // Act & Assert
        assertArrayEquals(new byte[][]{{1, 2, 3}, {4, 5, 6}, {7}},
            splitIntoChunks(data, 3), "Split with a remainder");

        assertArrayEquals(new byte[][]{{1, 2, 3}, {4, 5, 6}},
            splitIntoChunks(new byte[]{1, 2, 3, 4, 5, 6}, 3), "Split with no remainder");
    }

    // --- Test Helper Methods ---

    /**
     * Creates a {@link MultiReadOnlySeekableByteChannel} from an array of byte arrays.
     * Each inner byte array is wrapped in its own {@link SeekableInMemoryByteChannel}.
     */
    private SeekableByteChannel createMultiChannel(final byte[][] chunks) {
        final SeekableByteChannel[] channels = new SeekableByteChannel[chunks.length];
        for (int i = 0; i < chunks.length; i++) {
            channels[i] = new SeekableInMemoryByteChannel(chunks[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    /**
     * Reads all bytes from a channel into a byte array.
     */
    private byte[] readAllBytesFromChannel(SeekableByteChannel channel, int bufferSize) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteBuffer buffer = ByteBuffer.allocate(bufferSize);

        while (channel.read(buffer) != -1) {
            buffer.flip();
            outputStream.write(buffer.array(), buffer.arrayOffset() + buffer.position(), buffer.remaining());
            buffer.clear();
        }
        return outputStream.toByteArray();
    }

    /**
     * Splits a byte array into smaller chunks of a specified size.
     */
    private byte[][] splitIntoChunks(final byte[] input, final int chunkSize) {
        final List<byte[]> groups = new ArrayList<>();
        for (int i = 0; i < input.length; i += chunkSize) {
            final int end = Math.min(input.length, i + chunkSize);
            groups.add(Arrays.copyOfRange(input, i, end));
        }
        return groups.toArray(new byte[0][]);
    }
}