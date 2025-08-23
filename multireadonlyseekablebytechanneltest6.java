package org.apache.commons.compress.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
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
 * Tests the behavior of {@link MultiReadOnlySeekableByteChannel}, focusing on the close operation.
 * <p>
 * Note: This class contains comprehensive helper methods (e.g., for verifying read operations)
 * that may be used by other tests in this suite. They are refactored here for overall clarity.
 * </p>
 */
public class MultiReadOnlySeekableByteChannelCloseTest {

    // region Helper methods for verifying channel read behavior (likely used by other tests)

    /**
     * Verifies that a channel behaves correctly by testing it with various data chunkings.
     * It splits the source data into multiple chunks, creates a MultiReadOnlySeekableByteChannel,
     * and then performs detailed read verification.
     *
     * @param expectedData The complete data the channel should represent.
     * @throws IOException on I/O error.
     */
    private void verifyChannelReadsCorrectlyWithVariousChunkSizes(final byte[] expectedData) throws IOException {
        for (int chunkSize = 1; chunkSize <= expectedData.length; chunkSize++) {
            // Sanity check: ensure the base SeekableInMemoryByteChannel works as expected.
            try (SeekableByteChannel singleChannel = createInMemoryChannel(expectedData)) {
                verifyChannelReadsCorrectlyWithVariousBufferSizes(expectedData, singleChannel);
            }
            // Main check: verify the MultiReadOnlySeekableByteChannel.
            final byte[][] chunks = splitIntoChunks(expectedData, chunkSize);
            try (SeekableByteChannel multiChannel = createMultiChannelFromByteArrays(chunks)) {
                verifyChannelReadsCorrectlyWithVariousBufferSizes(expectedData, multiChannel);
            }
        }
    }

    /**
     * Verifies a given channel by reading its entire content using various buffer sizes.
     *
     * @param expectedData The expected data from the channel.
     * @param channel      The channel to test.
     * @throws IOException on I/O error.
     */
    private void verifyChannelReadsCorrectlyWithVariousBufferSizes(final byte[] expectedData, final SeekableByteChannel channel) throws IOException {
        // Test with various read buffer sizes, including sizes smaller, equal, and larger than the content.
        for (int readBufferSize = 1; readBufferSize <= expectedData.length + 5; readBufferSize++) {
            verifyChannelStateAndContent(expectedData, channel, readBufferSize);
        }
    }

    /**
     * Performs detailed verification of a channel's state and content for a given read buffer size.
     * It checks channel properties (size, position) and reads the entire content, verifying each read operation.
     *
     * @param expectedData   The expected data from the channel.
     * @param channel        The channel to test.
     * @param readBufferSize The size of the buffer to use for each read operation.
     * @throws IOException on I/O error.
     */
    private void verifyChannelStateAndContent(final byte[] expectedData, final SeekableByteChannel channel, final int readBufferSize) throws IOException {
        final String context = "with readBufferSize=" + readBufferSize;

        assertTrue(channel.isOpen(), () -> "Channel should be open before reading. " + context);
        assertEquals(expectedData.length, channel.size(), () -> "Channel size should match expected data length. " + context);

        // Reset position and verify
        channel.position(0);
        assertEquals(0, channel.position(), () -> "Position should be 0 after reset. " + context);

        // Reading into a zero-sized buffer should do nothing and return 0.
        assertEquals(0, channel.read(ByteBuffer.allocate(0)), () -> "Read with zero-sized buffer should return 0. " + context);

        // Read the entire channel content and verify along the way
        final ByteBuffer fullContentBuffer = ByteBuffer.allocate(expectedData.length + 100); // Extra space for safety
        final ByteBuffer readBuffer = ByteBuffer.allocate(readBufferSize);

        int bytesRead;
        while ((bytesRead = channel.read(readBuffer)) != -1) {
            // The buffer's position should equal the number of bytes read.
            assertEquals(bytesRead, readBuffer.position(), () -> "Buffer position should match bytes read. " + context);

            // The read buffer should be full after a successful read,
            // unless it's the final read operation that hits EOF.
            if (fullContentBuffer.position() + bytesRead < expectedData.length) {
                assertEquals(0, readBuffer.remaining(), () -> "Read buffer should be full if not at EOF. " + context);
            }

            readBuffer.flip();
            fullContentBuffer.put(readBuffer);
            readBuffer.clear();
        }

        fullContentBuffer.flip();
        final byte[] actualBytes = new byte[fullContentBuffer.remaining()];
        fullContentBuffer.get(actualBytes);

        assertArrayEquals(expectedData, actualBytes, () -> "Final read content should match expected data. " + context);
    }

    /**
     * Verifies the behavior of an empty channel.
     *
     * @param channel The empty channel to test.
     * @throws IOException on I/O error.
     */
    private void verifyEmptyChannelBehavior(final SeekableByteChannel channel) throws IOException {
        final ByteBuffer buf = ByteBuffer.allocate(10);
        assertTrue(channel.isOpen());
        assertEquals(0, channel.size());
        assertEquals(0, channel.position());

        // Reading from an empty channel should immediately return -1 (EOF).
        assertEquals(-1, channel.read(buf));

        // Positioning beyond the end and reading should still result in EOF.
        channel.position(5);
        assertEquals(-1, channel.read(buf));

        channel.close();
        assertFalse(channel.isOpen());

        // Operations on a closed channel should throw ClosedChannelException.
        assertThrows(ClosedChannelException.class, () -> channel.read(buf), "read() should throw on a closed channel");
        assertThrows(ClosedChannelException.class, () -> channel.position(100), "position() should throw on a closed channel");
    }

    // endregion

    // region Factory and utility methods

    /**
     * Splits a byte array into a list of smaller byte arrays, each of a given maximum size.
     *
     * @param input     The byte array to split.
     * @param chunkSize The maximum size of each chunk.
     * @return An array of byte arrays representing the chunks.
     */
    private byte[][] splitIntoChunks(final byte[] input, final int chunkSize) {
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
     * Creates an empty {@link SeekableInMemoryByteChannel}.
     */
    private SeekableByteChannel createEmptyInMemoryChannel() {
        return new SeekableInMemoryByteChannel(ByteUtils.EMPTY_BYTE_ARRAY);
    }

    /**
     * Creates a {@link MultiReadOnlySeekableByteChannel} from an array of byte arrays.
     */
    private SeekableByteChannel createMultiChannelFromByteArrays(final byte[][] arr) {
        final SeekableByteChannel[] channels = new SeekableByteChannel[arr.length];
        for (int i = 0; i < channels.length; i++) {
            channels[i] = createInMemoryChannel(arr[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    /**
     * Creates a {@link SeekableInMemoryByteChannel} from a byte array.
     */
    private SeekableByteChannel createInMemoryChannel(final byte[] arr) {
        return new SeekableInMemoryByteChannel(arr);
    }

    /**
     * Creates a test channel composed of two empty sub-channels.
     */
    private SeekableByteChannel createChannelFromTwoEmptySources() {
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(createEmptyInMemoryChannel(), createEmptyInMemoryChannel());
    }

    // endregion

    /**
     * A test-specific stub that throws an exception on {@link #close()} to verify error handling.
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

    /**
     * Tests that closing a channel is an idempotent operation, as specified by the {@link java.io.Closeable} interface.
     * Calling close() more than once should have no effect after the first call.
     */
    @Test
    void closeIsIdempotent() throws Exception {
        // The channel under test is composed of two empty sub-channels.
        try (SeekableByteChannel channel = createChannelFromTwoEmptySources()) {
            // First close
            channel.close();
            assertFalse(channel.isOpen(), "Channel should be closed after the first call to close()");

            // Second close (should have no effect and not throw an exception)
            channel.close();
            assertFalse(channel.isOpen(), "Channel should remain closed after the second call to close()");
        }
    }
}