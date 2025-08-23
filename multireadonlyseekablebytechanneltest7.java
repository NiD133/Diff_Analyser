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
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MultiReadOnlySeekableByteChannel}.
 * This class focuses on read operations, edge cases like empty channels, and exception handling.
 */
class MultiReadOnlySeekableByteChannelTest {

    // =================================================================================
    // Test Cases
    // =================================================================================

    /**
     * Verifies that data can be read correctly from a channel composed of multiple
     * underlying channels. This test covers reading across the boundaries of the
     * sub-channels.
     */
    @Test
    void shouldReadAcrossMultipleChannels() throws IOException {
        final byte[] testData = "abcdefghijklmnopqrstuvwxyz".getBytes(UTF_8);
        // This helper method exhaustively tests various sub-channel sizes and read buffer sizes.
        verifyReadBehaviorWithVariousChunkAndBufferSizes(testData);
    }

    /**
     * Verifies that a MultiReadOnlySeekableByteChannel created from empty sources
     * behaves correctly as an empty channel.
     */
    @Test
    void shouldBehaveAsEmptyForEmptySources() throws IOException {
        // Test with a multi-channel composed of two empty sub-channels
        try (SeekableByteChannel channel = createMultiChannel(createEmptyInMemoryChannel(), createEmptyInMemoryChannel())) {
            verifyEmptyChannelBehavior(channel);
        }
    }

    /**
     * Tests that if a sub-channel throws an IOException on close(), the exception
     * is propagated, but only after attempting to close all other sub-channels.
     */
    @Test
    void closeShouldPropagateExceptionAfterClosingAllSubChannels() {
        // Arrange: Create two sub-channels that will throw an exception on close().
        final ThrowingSeekableByteChannel channel1 = new ThrowingSeekableByteChannel();
        final ThrowingSeekableByteChannel channel2 = new ThrowingSeekableByteChannel();
        final SeekableByteChannel multiChannel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channel1, channel2);

        // Act & Assert: Closing the multi-channel should throw the expected IOException.
        assertThrows(IOException.class, multiChannel::close, "Expected an IOException to be thrown on close.");

        // Assert: Verify that close() was attempted on both sub-channels.
        assertFalse(channel1.isOpen(), "The first sub-channel should have been closed.");
        assertFalse(channel2.isOpen(), "The second sub-channel should have been closed.");
    }

    // =================================================================================
    // Verification Helper Methods
    // =================================================================================

    /**
     * Verifies the read behavior of a channel by splitting the expected data into
     * various chunk sizes, creating a MultiReadOnlySeekableByteChannel from them,
     * and then testing with various read buffer sizes.
     *
     * @param expectedData The complete data that the channel should represent.
     */
    private void verifyReadBehaviorWithVariousChunkAndBufferSizes(final byte[] expectedData) throws IOException {
        // Iterate through all possible ways to chunk the source data
        for (int chunkSize = 1; chunkSize <= expectedData.length; chunkSize++) {
            final byte[][] chunks = splitIntoChunks(expectedData, chunkSize);

            // Sanity check: ensure the test setup works with a single, non-multi channel
            try (SeekableByteChannel singleChannel = createInMemoryChannel(expectedData)) {
                verifyReadBehaviorWithVariousBufferSizes(expectedData, singleChannel);
            }

            // Main check: verify the MultiReadOnlySeekableByteChannel
            try (SeekableByteChannel multiChannel = createMultiChannel(chunks)) {
                verifyReadBehaviorWithVariousBufferSizes(expectedData, multiChannel);
            }
        }
    }

    /**
     * Verifies the read behavior of a given channel against expected data,
     * using a range of different read buffer sizes.
     *
     * @param expectedData The data the channel is expected to contain.
     * @param channel      The channel to test.
     */
    private void verifyReadBehaviorWithVariousBufferSizes(final byte[] expectedData, final SeekableByteChannel channel) throws IOException {
        // Iterate through various read buffer sizes to test different read scenarios
        for (int readBufferSize = 1; readBufferSize <= expectedData.length + 5; readBufferSize++) {
            verifyChannelRead(expectedData, channel, readBufferSize);
        }
    }

    /**
     * Performs a detailed verification of a channel's state and read content.
     *
     * @param expectedData   The data the channel is expected to contain.
     * @param channel        The channel to test.
     * @param readBufferSize The size of the ByteBuffer to use for each read operation.
     */
    private void verifyChannelRead(final byte[] expectedData, final SeekableByteChannel channel, final int readBufferSize) throws IOException {
        final String context = "for readBufferSize=" + readBufferSize;

        // 1. Check initial state and metadata
        assertTrue(channel.isOpen(), context);
        assertEquals(expectedData.length, channel.size(), "Channel size should match expected data length " + context);
        channel.position(0);
        assertEquals(0, channel.position(), "Position should be 0 after reset " + context);
        assertEquals(0, channel.read(ByteBuffer.allocate(0)), "Read with zero-capacity buffer should return 0 " + context);

        // 2. Read all content from the channel
        final ByteBuffer resultBuffer = ByteBuffer.allocate(expectedData.length + 100); // Extra space for safety
        final ByteBuffer readBuffer = ByteBuffer.allocate(readBufferSize);
        int bytesRead;
        while ((bytesRead = channel.read(readBuffer)) != -1) {
            // Before flipping, check if the previous read filled the buffer.
            // This should be true for all reads except the last one.
            if (resultBuffer.position() < expectedData.length) {
                assertEquals(0, readBuffer.remaining(), "Read buffer should be full until the end of the channel is reached " + context);
            }

            readBuffer.flip();
            resultBuffer.put(readBuffer);
            readBuffer.clear();

            // After a read, the buffer's position should equal the number of bytes read.
            if (bytesRead == -1) {
                assertEquals(0, readBuffer.position(), "Buffer position should be 0 after EOF " + context);
            } else {
                assertEquals(bytesRead, readBuffer.position(), "Buffer position should match bytes read " + context);
            }
        }

        // 3. Verify the content read matches the expected data
        resultBuffer.flip();
        final byte[] actualReadBytes = new byte[resultBuffer.remaining()];
        resultBuffer.get(actualReadBytes);
        assertArrayEquals(expectedData, actualReadBytes, "The fully read content should match the expected data " + context);
    }

    /**
     * Verifies that a channel conforms to the behavior of an empty channel.
     */
    private void verifyEmptyChannelBehavior(final SeekableByteChannel channel) throws IOException {
        final ByteBuffer buf = ByteBuffer.allocate(10);

        assertTrue(channel.isOpen());
        assertEquals(0, channel.size(), "Size of an empty channel should be 0.");
        assertEquals(0, channel.position(), "Position of an empty channel should be 0.");

        // Reading from an empty channel should immediately return -1 (EOF).
        assertEquals(-1, channel.read(buf), "Read on an empty channel should return -1.");

        // Seeking past the end and then reading should also result in EOF.
        channel.position(5);
        assertEquals(-1, channel.read(buf), "Read after seeking past the end should return -1.");

        channel.close();
        assertFalse(channel.isOpen());

        // Operations on a closed channel should throw ClosedChannelException.
        assertThrows(ClosedChannelException.class, () -> channel.read(buf), "Read on a closed channel should throw exception.");
        assertThrows(ClosedChannelException.class, () -> channel.position(100), "Position on a closed channel should throw exception.");
    }

    // =================================================================================
    // Factory and Utility Methods
    // =================================================================================

    /**
     * Splits a byte array into a list of smaller byte arrays (chunks).
     */
    private byte[][] splitIntoChunks(final byte[] input, final int chunkSize) {
        final List<byte[]> groups = new ArrayList<>();
        int idx = 0;
        while (idx < input.length) {
            final int end = Math.min(idx + chunkSize, input.length);
            groups.add(Arrays.copyOfRange(input, idx, end));
            idx += chunkSize;
        }
        return groups.toArray(new byte[0][]);
    }

    /**
     * Creates a {@link MultiReadOnlySeekableByteChannel} from an array of byte arrays.
     */
    private SeekableByteChannel createMultiChannel(final byte[]... chunks) {
        final SeekableByteChannel[] channels = new SeekableByteChannel[chunks.length];
        for (int i = 0; i < chunks.length; i++) {
            channels[i] = createInMemoryChannel(chunks[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    /**
     * Creates a {@link MultiReadOnlySeekableByteChannel} from an array of channels.
     */
    private SeekableByteChannel createMultiChannel(final SeekableByteChannel... channels) {
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    /**
     * Creates a simple, in-memory channel for testing purposes.
     */
    private SeekableByteChannel createInMemoryChannel(final byte[] data) {
        return new SeekableInMemoryByteChannel(data);
    }

    /**
     * Creates an empty in-memory channel.
     */
    private SeekableByteChannel createEmptyInMemoryChannel() {
        return new SeekableInMemoryByteChannel(ByteUtils.EMPTY_BYTE_ARRAY);
    }

    // =================================================================================
    // Test Doubles
    // =================================================================================

    /**
     * A test-specific implementation of {@link SeekableByteChannel} that always
     * throws an {@link IOException} when {@code close()} is called.
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
            return -1; // Behaves like an empty channel
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