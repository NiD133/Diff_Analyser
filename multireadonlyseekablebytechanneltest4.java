package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
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

    private static final byte[] DATA_10_BYTES = "0123456789".getBytes(UTF_8);
    private static final byte[] DATA_20_BYTES = "0123456789abcdefghij".getBytes(UTF_8);

    //<editor-fold desc="Test Cases">

    @Test
    @DisplayName("An empty channel should have size 0 and always return end-of-file")
    void emptyChannelShouldHaveSizeZeroAndReturnEndOfFile() throws IOException {
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels()) {
            assertTrue(channel.isOpen());
            assertEquals(0, channel.size());
            assertEquals(0, channel.position());
            assertEquals(-1, channel.read(ByteBuffer.allocate(10)), "Reading from an empty channel should return -1");
        }
    }

    @Test
    @DisplayName("A closed empty channel should throw ClosedChannelException on operations")
    void closedEmptyChannelShouldThrowException() throws IOException {
        SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels();
        channel.close();
        assertFalse(channel.isOpen());

        assertThrows(ClosedChannelException.class, () -> channel.read(ByteBuffer.allocate(10)));
        assertThrows(ClosedChannelException.class, () -> channel.position(10));
        assertThrows(ClosedChannelException.class, channel::size);
    }

    @Test
    @DisplayName("write() should throw NonWritableChannelException")
    void writeShouldThrowException() throws IOException {
        try (SeekableByteChannel channel = createChannel(DATA_10_BYTES)) {
            assertThrows(NonWritableChannelException.class, () -> channel.write(ByteBuffer.allocate(1)));
        }
    }

    @Test
    @DisplayName("truncate() should throw NonWritableChannelException")
    void truncateShouldThrowException() throws IOException {
        try (SeekableByteChannel channel = createChannel(DATA_10_BYTES)) {
            assertThrows(NonWritableChannelException.class, () -> channel.truncate(1));
        }
    }

    @Test
    @DisplayName("Positioning should work correctly across underlying channel boundaries")
    void positioningShouldWorkAcrossChannelBoundaries() throws IOException {
        // Create a channel from 4 segments of 5 bytes each. Total size = 20 bytes.
        byte[][] segments = groupBytes(DATA_20_BYTES, 5);
        try (SeekableByteChannel channel = createMultiChannel(segments)) {
            assertEquals(20, channel.size());

            // Position in the middle of the first channel
            channel.position(2);
            assertEquals(2, channel.position());
            assertEquals('2', readSingleByte(channel));

            // Position on a channel boundary
            channel.position(5);
            assertEquals(5, channel.position());
            assertEquals('5', readSingleByte(channel));

            // Position in the middle of a later channel
            channel.position(13);
            assertEquals(13, channel.position());
            assertEquals('d', readSingleByte(channel));

            // Position at the end
            channel.position(20);
            assertEquals(20, channel.position());
            assertEquals(-1, readSingleByte(channel), "Reading at EOF should return -1");

            // Position beyond the end should throw an exception
            assertThrows(IOException.class, () -> channel.position(21), "Positioning beyond size should fail");
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("readScenarios")
    @DisplayName("Reading sequentially should yield all bytes")
    void readingSequentiallyShouldYieldAllBytes(final String description, final byte[] expectedData, final int channelChunkSize, final int readBufferSize)
        throws IOException {
        byte[][] segments = groupBytes(expectedData, channelChunkSize);
        try (SeekableByteChannel channel = createMultiChannel(segments)) {
            // Assert initial state
            assertEquals(expectedData.length, channel.size());
            assertEquals(0, channel.position());

            // Read all bytes and assert content
            final byte[] actualData = readAllBytesFromChannel(channel, readBufferSize);
            assertArrayEquals(expectedData, actualData);

            // Assert final state
            assertEquals(expectedData.length, channel.position(), "Position should be at the end after reading");
            assertEquals(-1, channel.read(ByteBuffer.allocate(1)), "Further reads should return -1");
        }
    }

    //</editor-fold>

    //<editor-fold desc="Test Parameter Providers and Helpers">

    /**
     * Provides scenarios for the parameterized read test, covering various combinations of
     * total data size, underlying channel chunk sizes, and read buffer sizes.
     */
    static Stream<Arguments> readScenarios() {
        final List<byte[]> testData = List.of(
            ByteUtils.EMPTY_BYTE_ARRAY,
            "a".getBytes(UTF_8),
            "0123456789".getBytes(UTF_8) // 10 bytes
        );
        final List<Integer> chunkSizes = List.of(1, 3, 10, 12);
        final List<Integer> bufferSizes = List.of(1, 5, 10, 15);

        final List<Arguments> arguments = new ArrayList<>();
        for (final byte[] data : testData) {
            for (final int chunkSize : chunkSizes) {
                for (final int bufferSize : bufferSizes) {
                    final String desc = String.format("dataSize=%d, chunkSize=%d, bufferSize=%d",
                        data.length, chunkSize, bufferSize);
                    arguments.add(Arguments.of(desc, data, chunkSize, bufferSize));
                }
            }
        }
        return arguments.stream();
    }

    /**
     * Splits a byte array into a list of smaller byte arrays, each of a given maximum size.
     */
    private static byte[][] groupBytes(final byte[] input, final int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("chunkSize must be positive");
        }
        final List<byte[]> groups = new ArrayList<>();
        int offset = 0;
        while (offset < input.length) {
            final int length = Math.min(chunkSize, input.length - offset);
            groups.add(Arrays.copyOfRange(input, offset, offset + length));
            offset += length;
        }
        return groups.toArray(new byte[0][]);
    }

    /**
     * Creates a {@link MultiReadOnlySeekableByteChannel} from an array of byte arrays.
     */
    private static SeekableByteChannel createMultiChannel(final byte[][] segments) {
        final SeekableByteChannel[] channels = new SeekableByteChannel[segments.length];
        for (int i = 0; i < segments.length; i++) {
            channels[i] = new SeekableInMemoryByteChannel(segments[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    /**
     * Creates a single-segment {@link MultiReadOnlySeekableByteChannel}.
     */
    private static SeekableByteChannel createChannel(final byte[] data) {
        return new SeekableInMemoryByteChannel(data);
    }

    /**
     * Reads all bytes from a channel into a byte array.
     */
    private static byte[] readAllBytesFromChannel(final SeekableByteChannel channel, final int bufferSize) throws IOException {
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
     * Reads a single byte from the channel's current position.
     */
    private static int readSingleByte(final SeekableByteChannel channel) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(1);
        final int bytesRead = channel.read(buffer);
        if (bytesRead == -1) {
            return -1;
        }
        return buffer.get(0);
    }

    //</editor-fold>
}