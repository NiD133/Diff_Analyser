package org.apache.commons.compress.utils;

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
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 *
 * <p>This test class contains a specific test case for behavior when seeking past the end of the channel,
 * as well as a comprehensive testing harness (the 'check' methods) for verifying the read and position
 * logic under various conditions (e.g., different underlying channel sizes and read buffer sizes).</p>
 */
// Renamed class for clarity and adherence to standard naming conventions.
public class MultiReadOnlySeekableByteChannelTest {

    /**
     * The {@link SeekableByteChannel} interface specifies that setting a position
     * greater than the current size is legal. A subsequent read should then
     * immediately return an end-of-file indication (-1).
     *
     * @see <a href="https://docs.oracle.com/javase/8/docs/api/java/nio/channels/SeekableByteChannel.html#position-long-">SeekableByteChannel#position(long)</a>
     */
    @Test
    void readingFromPositionAfterEndShouldReturnEof() throws IOException {
        // Arrange: Create a multi-channel from two empty sources, resulting in a total size of 0.
        // Inlining the channel creation makes the test setup self-contained and easier to understand.
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(
            new SeekableInMemoryByteChannel(), new SeekableInMemoryByteChannel())) {

            // Act: Set the position beyond the end of the channel.
            channel.position(2);
            assertEquals(2, channel.position(), "Position should be updatable even if it's beyond the channel's size.");

            final ByteBuffer readBuffer = ByteBuffer.allocate(5);
            final int bytesRead = channel.read(readBuffer);

            // Assert: A subsequent read must return -1 (EOF).
            assertEquals(-1, bytesRead, "Reading from a position after the end should return EOF.");
        }
    }

    // --- Comprehensive Test Harness Helpers ---
    // The following methods form a test harness for exhaustively checking channel behavior.
    // They are not used by the single test case above but are preserved and improved for completeness.

    /**
     * A comprehensive check that iterates through various chunk sizes for the underlying channels.
     *
     * @param expected The full byte array content expected from the channel.
     * @throws IOException On I/O error.
     */
    private void check(final byte[] expected) throws IOException {
        for (int channelChunkSize = 1; channelChunkSize <= expected.length; channelChunkSize++) {
            // Sanity check that the test logic works for a simple, single in-memory channel.
            try (SeekableByteChannel singleChannel = makeSingleChannel(expected)) {
                check(expected, singleChannel);
            }
            // Check against the MultiReadOnlySeekableByteChannel instance.
            try (SeekableByteChannel multiChannel = makeMultiChannel(groupBytes(expected, channelChunkSize))) {
                check(expected, multiChannel);
            }
        }
    }

    /**
     * A comprehensive check that iterates through various read buffer sizes.
     *
     * @param expected The full byte array content expected from the channel.
     * @param channel  The channel to test.
     * @throws IOException On I/O error.
     */
    private void check(final byte[] expected, final SeekableByteChannel channel) throws IOException {
        // Test with various buffer sizes, including smaller, equal, and larger than the content.
        for (int readBufferSize = 1; readBufferSize <= expected.length + 5; readBufferSize++) {
            check(expected, channel, readBufferSize);
        }
    }

    /**
     * Performs the core read and position assertions on a channel for a given read buffer size.
     *
     * @param expected       The full byte array content expected from the channel.
     * @param channel        The channel to test.
     * @param readBufferSize The size of the buffer to use for each read operation.
     * @throws IOException On I/O error.
     */
    private void check(final byte[] expected, final SeekableByteChannel channel, final int readBufferSize) throws IOException {
        final String context = "for readBufferSize " + readBufferSize;

        assertTrue(channel.isOpen(), () -> "Channel should be open before reading " + context);
        assertEquals(expected.length, channel.size(), () -> "Channel size should match expected data length " + context);

        // Reset position and verify
        channel.position(0);
        assertEquals(0, channel.position(), () -> "Position should be 0 after reset " + context);
        assertEquals(0, channel.read(ByteBuffer.allocate(0)), () -> "Reading into a zero-capacity buffer should read 0 bytes " + context);

        // Read the entire channel content
        final ByteBuffer resultBuffer = ByteBuffer.allocate(expected.length + 100); // Extra space for safety
        final ByteBuffer readBuffer = ByteBuffer.allocate(readBufferSize);

        while (channel.read(readBuffer) != -1) {
            // The buffer should be full after a read, unless it's the final read operation.
            final int remaining = readBuffer.remaining();
            if (resultBuffer.position() + readBuffer.position() < expected.length) {
                assertEquals(0, remaining, () -> "Read buffer should be full if not at EOF " + context);
            }

            readBuffer.flip();
            resultBuffer.put(readBuffer);
            readBuffer.clear();
        }

        // Verify the total content read
        resultBuffer.flip();
        final byte[] actual = new byte[resultBuffer.remaining()];
        resultBuffer.get(actual);
        assertArrayEquals(expected, actual, () -> "The fully read content should match the expected content " + context);
    }

    /**
     * Checks the behavior of an empty channel.
     */
    private void checkEmpty(final SeekableByteChannel channel) throws IOException {
        assertTrue(channel.isOpen());
        assertEquals(0, channel.size());
        assertEquals(0, channel.position());

        final ByteBuffer buf = ByteBuffer.allocate(10);
        assertEquals(-1, channel.read(buf), "Reading an empty channel should immediately return EOF.");

        // Position past the end and try to read again
        channel.position(5);
        assertEquals(-1, channel.read(buf), "Reading after positioning past the end of an empty channel should return EOF.");

        channel.close();
        assertFalse(channel.isOpen());

        // Verify operations on a closed channel throw exceptions
        assertThrows(ClosedChannelException.class, () -> channel.read(buf), "Reading from a closed channel should throw ClosedChannelException.");
        assertThrows(ClosedChannelException.class, () -> channel.position(100), "Positioning a closed channel should throw ClosedChannelException.");
    }

    // --- Test Data Factory Methods ---

    /**
     * Splits a byte array into a list of smaller byte arrays (chunks).
     *
     * @param input     The byte array to split.
     * @param chunkSize The size of each chunk.
     * @return An array of byte arrays representing the chunks.
     */
    private byte[][] groupBytes(final byte[] input, final int chunkSize) {
        final List<byte[]> groups = new ArrayList<>();
        int idx = 0;
        while (idx < input.length) {
            final int nextIdx = Math.min(idx + chunkSize, input.length);
            groups.add(Arrays.copyOfRange(input, idx, nextIdx));
            idx = nextIdx;
        }
        return groups.toArray(new byte[0][]);
    }

    /**
     * Creates a {@link MultiReadOnlySeekableByteChannel} from an array of byte arrays.
     */
    private SeekableByteChannel makeMultiChannel(final byte[][] arr) {
        final SeekableByteChannel[] channels = new SeekableByteChannel[arr.length];
        for (int i = 0; i < channels.length; i++) {
            channels[i] = makeSingleChannel(arr[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    /**
     * Creates a simple, single {@link SeekableInMemoryByteChannel} from a byte array.
     */
    private SeekableByteChannel makeSingleChannel(final byte[] arr) {
        return new SeekableInMemoryByteChannel(arr);
    }

    /**
     * A test-specific implementation of {@link SeekableByteChannel} that throws an exception on close.
     * Useful for testing that underlying resources are closed correctly even if one fails.
     */
    private static final class ThrowingSeekableByteChannel implements SeekableByteChannel {
        private boolean closed;

        @Override
        public void close() throws IOException {
            closed = true;
            throw new IOException("Simulated close failure");
        }

        @Override
        public boolean isOpen() {
            return !closed;
        }

        @Override
        public long position() {
            return 0;
        }

        @Override
        public SeekableByteChannel position(final long newPosition) {
            return this;
        }

        @Override
        public int read(final ByteBuffer dst) {
            return -1;
        }

        @Override
        public long size() {
            return 0;
        }

        @Override
        public SeekableByteChannel truncate(final long size) {
            throw new NonWritableChannelException();
        }

        @Override
        public int write(final ByteBuffer src) {
            throw new NonWritableChannelException();
        }
    }
}