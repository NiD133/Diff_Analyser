package org.apache.commons.compress.utils;

import static java.nio.charset.StandardCharsets.UTF_8;
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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 */
@DisplayName("MultiReadOnlySeekableByteChannel")
class MultiReadOnlySeekableByteChannelTest {

    @Test
    @DisplayName("constructor should throw NullPointerException for a null channel list")
    void constructorThrowsOnNullList() {
        assertThrows(NullPointerException.class, () -> new MultiReadOnlySeekableByteChannel(null));
    }

    @Nested
    @DisplayName("when channel is empty")
    class EmptyChannelTests {

        @Test
        @DisplayName("should handle being constructed with no channels")
        void testEmptyChannelFromNoArgs() throws IOException {
            try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels()) {
                assertEmptyChannelBehavior(channel);
            }
        }

        @Test
        @DisplayName("should handle being constructed with multiple empty channels")
        void testEmptyChannelFromMultipleEmptyChannels() throws IOException {
            try (SeekableByteChannel empty1 = new SeekableInMemoryByteChannel();
                 SeekableByteChannel empty2 = new SeekableInMemoryByteChannel();
                 SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(empty1, empty2)) {
                assertEmptyChannelBehavior(channel);
            }
        }

        private void assertEmptyChannelBehavior(final SeekableByteChannel channel) throws IOException {
            final ByteBuffer buf = ByteBuffer.allocate(10);
            assertTrue(channel.isOpen());
            assertEquals(0, channel.size());
            assertEquals(0, channel.position());
            assertEquals(-1, channel.read(buf), "Read on empty channel should return -1");

            // Seeking past the end is allowed, but subsequent reads should still return -1
            channel.position(5);
            assertEquals(-1, channel.read(buf));

            channel.close();
            assertFalse(channel.isOpen());

            // Operations on a closed channel should fail
            assertThrows(ClosedChannelException.class, () -> channel.read(buf));
            assertThrows(ClosedChannelException.class, () -> channel.position(100));
        }
    }

    @Nested
    @DisplayName("read-only properties")
    class ReadOnlyTests {
        private SeekableByteChannel channel;

        @BeforeEach
        void setUp() {
            channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(new SeekableInMemoryByteChannel(new byte[10]));
        }

        @AfterEach
        void tearDown() throws IOException {
            channel.close();
        }

        @Test
        @DisplayName("write() should throw NonWritableChannelException")
        void writeShouldThrow() {
            assertThrows(NonWritableChannelException.class, () -> channel.write(ByteBuffer.allocate(1)));
        }

        @Test
        @DisplayName("truncate() should throw NonWritableChannelException")
        void truncateShouldThrow() {
            assertThrows(NonWritableChannelException.class, () -> channel.truncate(1L));
        }
    }

    @Nested
    @DisplayName("when reading data")
    class ReadTests {

        static Stream<Arguments> readScenarios() {
            final byte[] data = "0123456789ABCDEF".getBytes(UTF_8); // 16 bytes
            return Stream.of(
                // Buffer size < chunk size
                Arguments.of(data, 5, 3),
                // Buffer size == chunk size
                Arguments.of(data, 5, 5),
                // Buffer size > chunk size
                Arguments.of(data, 3, 5),
                // Read all in one go
                Arguments.of(data, 3, 20),
                // Uneven final chunk
                Arguments.of(data, 7, 4),
                // Single-byte chunks and buffers (worst case)
                Arguments.of(data, 1, 1)
            );
        }

        @ParameterizedTest(name = "data size={0}, chunk size={1}, buffer size={2}")
        @MethodSource("readScenarios")
        @DisplayName("should read contents correctly across various chunk and buffer sizes")
        void readsContentCorrectly(byte[] testData, int channelChunkSize, int readBufferSize) throws IOException {
            List<SeekableByteChannel> channels = createChannelsFromData(testData, channelChunkSize);
            try (SeekableByteChannel multiChannel = new MultiReadOnlySeekableByteChannel(channels)) {
                assertChannelContentMatches(multiChannel, testData, readBufferSize);
            }
        }

        private void assertChannelContentMatches(SeekableByteChannel channel, byte[] expectedData, int readBufferSize) throws IOException {
            // Check initial state
            assertTrue(channel.isOpen(), "Channel should be open");
            assertEquals(expectedData.length, channel.size(), "Channel size should match expected data length");
            channel.position(0);
            assertEquals(0, channel.position(), "Position should be 0 after reset");

            // Read all bytes and verify content
            byte[] actualData = readAllBytesFromChannel(channel, readBufferSize, expectedData.length);
            assertArrayEquals(expectedData, actualData, "Read content should match expected data");
        }

        private byte[] readAllBytesFromChannel(SeekableByteChannel channel, int readBufferSize, int totalSize) throws IOException {
            final ByteBuffer resultBuffer = ByteBuffer.allocate(totalSize + 1); // +1 to detect overflow
            final ByteBuffer readBuffer = ByteBuffer.allocate(readBufferSize);

            while (channel.read(readBuffer) != -1) {
                readBuffer.flip();
                resultBuffer.put(readBuffer);
                readBuffer.clear();
            }

            resultBuffer.flip();
            final byte[] arr = new byte[resultBuffer.remaining()];
            resultBuffer.get(arr);
            return arr;
        }
    }

    @Nested
    @DisplayName("when seeking")
    class PositionTests {
        private static final byte[] CONTENT = "0123456789ABCDEFGHIJKLMNOPQRST".getBytes(UTF_8); // 30 bytes
        private SeekableByteChannel channel;

        @BeforeEach
        void setUp() {
            // Split content into 3 channels of 10 bytes each
            List<SeekableByteChannel> channels = createChannelsFromData(CONTENT, 10);
            channel = new MultiReadOnlySeekableByteChannel(channels);
        }

        @AfterEach
        void tearDown() throws IOException {
            channel.close();
        }

        @Test
        @DisplayName("should seek to start")
        void seekToStart() throws IOException {
            channel.position(0);
            assertEquals(0, channel.position());
            assertEquals('0', readSingleByte());
        }

        @Test
        @DisplayName("should seek within the first sub-channel")
        void seekWithinFirstChannel() throws IOException {
            channel.position(5);
            assertEquals(5, channel.position());
            assertEquals('5', readSingleByte());
        }

        @Test
        @DisplayName("should seek to a sub-channel boundary")
        void seekToChannelBoundary() throws IOException {
            channel.position(10);
            assertEquals(10, channel.position());
            assertEquals('A', readSingleByte());
        }

        @Test
        @DisplayName("should seek within a later sub-channel")
        void seekWithinSecondChannel() throws IOException {
            channel.position(15);
            assertEquals(15, channel.position());
            assertEquals('F', readSingleByte());
        }

        @Test
        @DisplayName("should seek to the end")
        void seekToEnd() throws IOException {
            channel.position(CONTENT.length);
            assertEquals(CONTENT.length, channel.position());
            assertEquals(-1, readSingleByte());
        }

        @Test
        @DisplayName("should handle seeking past the end")
        void seekPastEnd() throws IOException {
            channel.position(CONTENT.length + 5);
            assertEquals(CONTENT.length + 5, channel.position());
            assertEquals(-1, readSingleByte());
        }

        @Test
        @DisplayName("should throw an exception when seeking to a negative position")
        void seekToNegativePosition() {
            assertThrows(IOException.class, () -> channel.position(-1));
        }

        private int readSingleByte() throws IOException {
            ByteBuffer buffer = ByteBuffer.allocate(1);
            int bytesRead = channel.read(buffer);
            if (bytesRead == -1) {
                return -1;
            }
            return buffer.get(0);
        }
    }

    // --- Helper Methods ---

    /**
     * Splits a byte array into multiple chunks and wraps each in a {@link SeekableInMemoryByteChannel}.
     */
    private static List<SeekableByteChannel> createChannelsFromData(final byte[] input, final int chunkSize) {
        final List<SeekableByteChannel> channels = new ArrayList<>();
        if (input.length == 0) {
            return channels;
        }
        int idx = 0;
        for (; idx + chunkSize <= input.length; idx += chunkSize) {
            channels.add(new SeekableInMemoryByteChannel(Arrays.copyOfRange(input, idx, idx + chunkSize)));
        }
        if (idx < input.length) {
            channels.add(new SeekableInMemoryByteChannel(Arrays.copyOfRange(input, idx, input.length)));
        }
        return channels;
    }
}