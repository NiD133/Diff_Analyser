package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 */
class MultiReadOnlySeekableByteChannelTest {

    // --- Test Helper Methods ---

    /**
     * Creates a {@link MultiReadOnlySeekableByteChannel} from a single byte array,
     * split into chunks of the given size.
     *
     * @param data      The source data.
     * @param chunkSize The size of each underlying channel.
     * @return A multi-channel instance for testing.
     */
    private MultiReadOnlySeekableByteChannel createMultiChannel(final byte[] data, final int chunkSize) {
        final List<byte[]> chunks = new ArrayList<>();
        for (int i = 0; i < data.length; i += chunkSize) {
            chunks.add(Arrays.copyOfRange(data, i, Math.min(data.length, i + chunkSize)));
        }

        final SeekableByteChannel[] channels = chunks.stream()
            .map(SeekableInMemoryByteChannel::new)
            .toArray(SeekableByteChannel[]::new);

        return new MultiReadOnlySeekableByteChannel(Arrays.asList(channels));
    }

    /**
     * Reads all bytes from a channel using a specific buffer size.
     *
     * @param channel        The channel to read from.
     * @param readBufferSize The size of the buffer for each read operation.
     * @return The full content of the channel as a byte array.
     * @throws IOException if an I/O error occurs.
     */
    private byte[] readAllBytes(final SeekableByteChannel channel, final int readBufferSize) throws IOException {
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteBuffer buffer = ByteBuffer.allocate(readBufferSize);
        while (channel.read(buffer) != -1) {
            buffer.flip();
            final byte[] chunk = new byte[buffer.remaining()];
            buffer.get(chunk);
            outputStream.write(chunk);
            buffer.clear();
        }
        return outputStream.toByteArray();
    }

    // --- Static Factory Method Tests ---

    @Test
    @DisplayName("forFiles should throw NullPointerException when given a null array")
    void forFiles_shouldThrowException_forNullArgument() {
        assertThrows(NullPointerException.class, () -> MultiReadOnlySeekableByteChannel.forFiles((File[]) null));
    }

    // --- Behavior Tests ---

    @Nested
    @DisplayName("When channel has content")
    class ChannelWithContentTests {

        private static final byte[] TEST_DATA = "ApacheCommonsCompress".getBytes(UTF_8);

        static Stream<Arguments> provideChannelConfigurations() {
            final List<Arguments> arguments = new ArrayList<>();
            // Test with various ways of chunking the source data
            for (int chunkSize = 1; chunkSize <= TEST_DATA.length; chunkSize++) {
                // Test with various read buffer sizes to check different read patterns
                for (int readBufferSize = 1; readBufferSize <= TEST_DATA.length + 5; readBufferSize++) {
                    arguments.add(Arguments.of(chunkSize, readBufferSize));
                }
            }
            return arguments.stream();
        }

        @ParameterizedTest(name = "chunkSize={0}, readBufferSize={1}")
        @MethodSource("provideChannelConfigurations")
        @DisplayName("read() should return the full, correct content")
        void read_shouldReturnConcatenatedContent(final int chunkSize, final int readBufferSize) throws IOException {
            try (SeekableByteChannel channel = createMultiChannel(TEST_DATA, chunkSize)) {
                final byte[] actualData = readAllBytes(channel, readBufferSize);
                assertArrayEquals(TEST_DATA, actualData, "Content should match expected data");
            }
        }

        @Test
        @DisplayName("size() should return the sum of the sizes of the individual channels")
        void size_shouldReturnSumOfIndividualChannelSizes() throws IOException {
            final byte[] data1 = "Part1".getBytes(UTF_8);
            final byte[] data2 = "AndPart2".getBytes(UTF_8);
            final SeekableByteChannel[] channels = {
                new SeekableInMemoryByteChannel(data1),
                new SeekableInMemoryByteChannel(data2)
            };

            try (SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels)) {
                assertEquals(data1.length + data2.length, multiChannel.size());
            }
        }

        @Test
        @DisplayName("position() should be set and read correctly, including across boundaries")
        void position_shouldBeHandledCorrectly() throws IOException {
            final byte[] data = new byte[20]; // e.g., two 10-byte channels
            try (SeekableByteChannel channel = createMultiChannel(data, 10)) {
                // Position within the first channel
                channel.position(5);
                assertEquals(5, channel.position());

                // Position exactly at the boundary
                channel.position(10);
                assertEquals(10, channel.position());

                // Position within the second channel
                channel.position(15);
                assertEquals(15, channel.position());

                // Read a byte and check if position advances
                channel.read(ByteBuffer.allocate(1));
                assertEquals(16, channel.position());
            }
        }

        @Test
        @DisplayName("position() should throw IOException when set beyond channel size")
        void position_shouldThrowException_whenSetBeyondSize() throws IOException {
            try (SeekableByteChannel channel = createMultiChannel(new byte[10], 5)) {
                assertThrows(IOException.class, () -> channel.position(11));
            }
        }
    }

    @Nested
    @DisplayName("When channel is empty")
    class EmptyChannelTests {

        @Test
        @DisplayName("An empty channel should have size 0 and read -1")
        void emptyChannel_shouldBehaveAsExpected() throws IOException {
            // Test with a channel composed of multiple empty sub-channels
            final SeekableByteChannel[] emptyChannels = {
                new SeekableInMemoryByteChannel(),
                new SeekableInMemoryByteChannel()
            };

            try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(emptyChannels)) {
                assertTrue(channel.isOpen());
                assertEquals(0, channel.size());
                assertEquals(0, channel.position());
                assertEquals(-1, channel.read(ByteBuffer.allocate(10)));

                // Positioning in an empty channel should not fail but subsequent reads should
                channel.position(5);
                assertEquals(5, channel.position());
                assertEquals(-1, channel.read(ByteBuffer.allocate(10)));
            }
        }

        @Test
        @DisplayName("A closed empty channel should throw ClosedChannelException on operations")
        void closedEmptyChannel_shouldThrowException() throws IOException {
            final SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels();
            channel.close();

            assertFalse(channel.isOpen());
            assertThrows(ClosedChannelException.class, () -> channel.read(ByteBuffer.allocate(10)), "read");
            assertThrows(ClosedChannelException.class, () -> channel.position(1), "position");
            assertThrows(ClosedChannelException.class, channel::size, "size");
        }
    }

    @Nested
    @DisplayName("As a read-only channel")
    class ReadOnlyTests {

        @Test
        @DisplayName("write() should throw NonWritableChannelException")
        void write_shouldThrowException() throws IOException {
            try (SeekableByteChannel channel = createMultiChannel(new byte[1], 1)) {
                final ByteBuffer buffer = ByteBuffer.wrap(new byte[]{1});
                assertThrows(NonWritableChannelException.class, () -> channel.write(buffer));
            }
        }

        @Test
        @DisplayName("truncate() should throw NonWritableChannelException")
        void truncate_shouldThrowException() throws IOException {
            try (SeekableByteChannel channel = createMultiChannel(new byte[1], 1)) {
                assertThrows(NonWritableChannelException.class, () -> channel.truncate(0));
            }
        }
    }
}