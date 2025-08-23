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
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 */
class MultiReadOnlySeekableByteChannelTest {

    private static byte[] testData;
    private static int testDataLength;

    @BeforeAll
    static void setupClass() {
        testData = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".getBytes(UTF_8);
        testDataLength = testData.length;
    }

    // Parameter source for the comprehensive multi-channel read test
    static Stream<Arguments> multiChannelReadParameters() {
        return IntStream.rangeClosed(1, testDataLength) // channelSize
            .boxed()
            .flatMap(channelSize ->
                IntStream.rangeClosed(1, testDataLength + 5) // readBufferSize
                    .mapToObj(readBufferSize -> Arguments.of(channelSize, readBufferSize))
            );
    }

    // Parameter source for the single-channel sanity check
    static Stream<Integer> singleChannelReadParameters() {
        return IntStream.rangeClosed(1, testDataLength + 5).boxed();
    }

    @ParameterizedTest(name = "channelSize={0}, readBufferSize={1}")
    @MethodSource("multiChannelReadParameters")
    @DisplayName("Comprehensive read from multi-channel should succeed")
    void comprehensiveReadFromMultiChannel(int channelSize, int readBufferSize) throws IOException {
        final byte[][] segments = splitIntoChunks(testData, channelSize);
        try (SeekableByteChannel multiChannel = createMultiChannel(segments)) {
            assertChannelBehavesCorrectly(multiChannel, testData, readBufferSize);
        }
    }

    @ParameterizedTest(name = "readBufferSize={0}")
    @MethodSource("singleChannelReadParameters")
    @DisplayName("Sanity check: Comprehensive read from single in-memory channel should succeed")
    void comprehensiveReadFromSingleChannel(int readBufferSize) throws IOException {
        try (SeekableByteChannel singleChannel = new SeekableInMemoryByteChannel(testData)) {
            assertChannelBehavesCorrectly(singleChannel, testData, readBufferSize);
        }
    }

    @Test
    @DisplayName("Channel composed of empty segments should behave as an empty channel")
    void emptyChannelBehavior() throws IOException {
        // A multi-channel composed of two empty channels
        try (SeekableByteChannel multiEmpty = createMultiChannel(new byte[0][], new byte[0][])) {
            assertEmptyChannel(multiEmpty);
        }
    }

    @Test
    @DisplayName("Closing channel should propagate exceptions from underlying channels")
    void closePropagatesUnderlyingExceptions() {
        final ThrowingSeekableByteChannel throwingChannel = new ThrowingSeekableByteChannel();
        try (MultiReadOnlySeekableByteChannel multiChannel = new MultiReadOnlySeekableByteChannel(
            Collections.singletonList(throwingChannel))) {

            final IOException e = assertThrows(IOException.class, multiChannel::close,
                "Expected an IOException from the underlying channel's close() method.");
            assertEquals("Simulated I/O Error on close", e.getMessage());
            assertFalse(multiChannel.isOpen(), "Channel should be marked as closed even if close() throws.");
            assertTrue(throwingChannel.isClosed(), "Underlying channel's close() should have been called.");
        }
    }

    /**
     * Asserts that a channel behaves correctly when it is expected to be empty.
     */
    private void assertEmptyChannel(final SeekableByteChannel channel) throws IOException {
        final ByteBuffer buf = ByteBuffer.allocate(10);
        assertTrue(channel.isOpen(), "Channel should be open initially.");
        assertEquals(0, channel.size(), "Channel size should be 0.");
        assertEquals(0, channel.position(), "Initial position should be 0.");
        assertEquals(-1, channel.read(buf), "Read on empty channel should return -1.");

        // Position can be set beyond size, but read should still return -1
        channel.position(5);
        assertEquals(-1, channel.read(buf), "Read after positioning should still return -1.");

        channel.close();
        assertFalse(channel.isOpen(), "Channel should be closed.");

        assertThrows(ClosedChannelException.class, () -> channel.read(buf), "Read on closed channel should throw.");
        assertThrows(ClosedChannelException.class, () -> channel.position(100), "Positioning a closed channel should throw.");
    }

    /**
     * A comprehensive set of assertions for a channel's read behavior.
     *
     * @param channel        The channel to test.
     * @param expectedData   The complete byte array expected to be read from the channel.
     * @param readBufferSize The size of the buffer to use for each read call.
     */
    private void assertChannelBehavesCorrectly(final SeekableByteChannel channel, final byte[] expectedData, final int readBufferSize) throws IOException {
        final String context = "with readBufferSize " + readBufferSize;
        assertTrue(channel.isOpen(), "Channel should be open before reading " + context);
        assertEquals(expectedData.length, channel.size(), "Channel size should match expected data length " + context);

        // Test reading from the start
        channel.position(0);
        assertEquals(0, channel.position(), "Position should be 0 after reset " + context);
        assertEquals(0, channel.read(ByteBuffer.allocate(0)), "Read with zero-capacity buffer should return 0 " + context);

        // Read the entire channel content
        final ByteBuffer resultBuffer = ByteBuffer.allocate(expectedData.length + 100); // Extra space for safety
        final ByteBuffer readChunkBuffer = ByteBuffer.allocate(readBufferSize);

        int bytesRead;
        while ((bytesRead = channel.read(readChunkBuffer)) != -1) {
            final int remainingBeforeFlip = readChunkBuffer.remaining();
            readChunkBuffer.flip();
            resultBuffer.put(readChunkBuffer);
            readChunkBuffer.clear();

            // If this isn't the last read, the buffer should have been fully populated.
            if (resultBuffer.position() < expectedData.length) {
                assertEquals(0, remainingBeforeFlip, "Intermediate read buffer should be full " + context);
            }

            if (bytesRead == -1) { // Should not happen inside loop, but for logical completeness
                assertEquals(0, readChunkBuffer.position(), "Buffer position should be 0 after EOF " + context);
            } else {
                assertEquals(bytesRead, readChunkBuffer.position(), "Buffer position should equal bytes read " + context);
            }
        }

        resultBuffer.flip();
        final byte[] actualData = new byte[resultBuffer.remaining()];
        resultBuffer.get(actualData);

        assertArrayEquals(expectedData, actualData, "Read content should match expected data " + context);
    }

    // --- Test Helper Methods ---

    private byte[][] splitIntoChunks(final byte[] input, final int chunkSize) {
        final List<byte[]> groups = new ArrayList<>();
        for (int i = 0; i < input.length; i += chunkSize) {
            final int end = Math.min(input.length, i + chunkSize);
            groups.add(Arrays.copyOfRange(input, i, end));
        }
        return groups.toArray(new byte[0][]);
    }

    private SeekableByteChannel createMultiChannel(final byte[]... segments) {
        final SeekableByteChannel[] channels = new SeekableByteChannel[segments.length];
        for (int i = 0; i < segments.length; i++) {
            channels[i] = new SeekableInMemoryByteChannel(segments[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    /**
     * A mock SeekableByteChannel that throws an exception on close.
     */
    private static final class ThrowingSeekableByteChannel implements SeekableByteChannel {
        private boolean closed;

        @Override
        public void close() throws IOException {
            closed = true;
            throw new IOException("Simulated I/O Error on close");
        }

        @Override
        public boolean isOpen() {
            return !closed;
        }

        public boolean isClosed() {
            return closed;
        }

        // --- Unused Mock Methods ---
        @Override public long position() { return 0; }
        @Override public SeekableByteChannel position(final long newPosition) { return this; }
        @Override public int read(final ByteBuffer dst) { return -1; }
        @Override public long size() { return 0; }
        @Override public SeekableByteChannel truncate(final long size) { return this; }
        @Override public int write(final ByteBuffer src) { return 0; }
    }
}