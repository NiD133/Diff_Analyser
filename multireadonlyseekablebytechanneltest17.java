package org.apache.commons.compress.utils;

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
 */
class MultiReadOnlySeekableByteChannelTest {

    /**
     * The primary test harness. It verifies the behavior of a channel by splitting the
     * expected content into various chunk sizes and creating a MultiReadOnlySeekableByteChannel
     * from them. It also sanity-checks the baseline SeekableInMemoryByteChannel.
     *
     * @param expected The full byte array the channel should represent.
     * @throws IOException On I/O errors.
     */
    private void verifyReadBehaviorAcrossChunkSizes(final byte[] expected) throws IOException {
        for (int chunkSize = 1; chunkSize <= expected.length; chunkSize++) {
            // Sanity check that all operations work for a single, simple channel
            try (SeekableByteChannel singleChannel = new SeekableInMemoryByteChannel(expected)) {
                verifyReadBehaviorAcrossBufferSizes(expected, singleChannel);
            }
            // Check against our MultiReadOnlySeekableByteChannel instance
            try (SeekableByteChannel multiChannel = createMultiChannel(splitIntoChunks(expected, chunkSize))) {
                verifyReadBehaviorAcrossBufferSizes(expected, multiChannel);
            }
        }
    }

    /**
     * Tests a given channel's read behavior against a variety of read buffer sizes.
     *
     * @param expected The full byte array the channel should represent.
     * @param channel The channel to test.
     * @throws IOException On I/O errors.
     */
    private void verifyReadBehaviorAcrossBufferSizes(final byte[] expected, final SeekableByteChannel channel) throws IOException {
        // Test with various buffer sizes, including smaller, equal, and larger than the content.
        for (int readBufferSize = 1; readBufferSize <= expected.length + 5; readBufferSize++) {
            assertFullReadYieldsExpectedContent(expected, channel, readBufferSize);
        }
    }

    /**
     * Asserts that a channel's content, when fully read, matches the expected byte array.
     * It performs checks on the channel's state before and during the read.
     *
     * @param expected       The expected byte array.
     * @param channel        The channel to read from.
     * @param readBufferSize The size of the buffer to use for each read operation.
     * @throws IOException On I/O errors.
     */
    private void assertFullReadYieldsExpectedContent(final byte[] expected, final SeekableByteChannel channel, final int readBufferSize) throws IOException {
        // 1. Pre-read state checks
        assertTrue(channel.isOpen(), () -> "Channel should be open before reading. Buffer size: " + readBufferSize);
        assertEquals(expected.length, channel.size(), () -> "Channel size should match expected data length. Buffer size: " + readBufferSize);
        channel.position(0);
        assertEquals(0, channel.position(), () -> "Position should be 0 after reset. Buffer size: " + readBufferSize);
        assertEquals(0, channel.read(ByteBuffer.allocate(0)), () -> "Reading into an empty buffer should return 0. Buffer size: " + readBufferSize);

        // 2. Read all content with intermediate checks
        final byte[] actual = readAllBytesWithIntegrityChecks(channel, readBufferSize, expected.length);

        // 3. Final content verification
        assertArrayEquals(expected, actual, () -> "Channel content should match expected data. Buffer size: " + readBufferSize);
    }

    /**
     * Reads all bytes from a channel, performing checks during the read process to ensure
     * buffer states are consistent with the read() contract.
     */
    private byte[] readAllBytesWithIntegrityChecks(final SeekableByteChannel channel, final int readBufferSize, final int expectedSize) throws IOException {
        // Allocate a buffer large enough for the full result, with a small margin.
        final ByteBuffer resultBuffer = ByteBuffer.allocate(expectedSize + 100);
        final ByteBuffer chunkBuffer = ByteBuffer.allocate(readBufferSize);

        int bytesRead;
        while ((bytesRead = channel.read(chunkBuffer)) != -1) {
            final int remainingInChunk = chunkBuffer.remaining();

            chunkBuffer.flip();
            resultBuffer.put(chunkBuffer);

            // After a successful read, the buffer's position should equal the number of bytes read.
            assertEquals(bytesRead, chunkBuffer.position(), () -> "Buffer position should equal bytes read. Buffer size: " + readBufferSize);

            // If we haven't read all expected content yet, the read buffer should have been completely filled.
            if (resultBuffer.position() < expectedSize) {
                assertEquals(0, remainingInChunk, () -> "Intermediate read should fill the buffer completely. Buffer size: " + readBufferSize);
            }
            chunkBuffer.clear();
        }

        // After the final read returns -1, the chunk buffer's position should be 0, as it wasn't written to.
        assertEquals(0, chunkBuffer.position(), () -> "Buffer position should be 0 after EOF. Buffer size: " + readBufferSize);

        resultBuffer.flip();
        final byte[] byteArray = new byte[resultBuffer.remaining()];
        resultBuffer.get(byteArray);
        return byteArray;
    }


    /**
     * Asserts that a channel exhibits all the expected behaviors of an empty channel.
     *
     * @param channel The channel to check.
     * @throws IOException on I/O error.
     */
    private void assertBehavesAsEmptyChannel(final SeekableByteChannel channel) throws IOException {
        final ByteBuffer buffer = ByteBuffer.allocate(10);

        // An empty channel should be open, have size 0, and be at position 0
        assertTrue(channel.isOpen(), "Channel should be open initially");
        assertEquals(0, channel.size(), "Size of an empty channel should be 0");
        assertEquals(0, channel.position(), "Position of an empty channel should be 0");

        // Reading from an empty channel should immediately return -1 (EOF)
        assertEquals(-1, channel.read(buffer), "Reading an empty channel should return -1 for EOF");

        // Positioning beyond the end of an empty channel is allowed, but reading should still return EOF
        channel.position(5);
        assertEquals(-1, channel.read(buffer), "Reading after positioning past the end should still return -1");

        // After closing, the channel should report as closed and operations should fail
        channel.close();
        assertFalse(channel.isOpen(), "Channel should be closed after calling close()");

        assertThrows(ClosedChannelException.class, () -> channel.read(buffer), "Reading from a closed channel should throw ClosedChannelException");
        assertThrows(ClosedChannelException.class, () -> channel.position(100), "Positioning a closed channel should throw ClosedChannelException");
    }

    /**
     * Splits a byte array into a list of smaller byte arrays.
     *
     * @param input     The byte array to split.
     * @param chunkSize The size of each chunk. The last chunk may be smaller.
     * @return An array of byte arrays representing the chunks.
     */
    private byte[][] splitIntoChunks(final byte[] input, final int chunkSize) {
        final List<byte[]> groups = new ArrayList<>();
        int idx = 0;
        while (idx < input.length) {
            final int end = Math.min(input.length, idx + chunkSize);
            groups.add(Arrays.copyOfRange(input, idx, end));
            idx += chunkSize;
        }
        return groups.toArray(new byte[0][]);
    }

    private SeekableByteChannel createEmptyChannel() {
        return new SeekableInMemoryByteChannel(ByteUtils.EMPTY_BYTE_ARRAY);
    }

    private SeekableByteChannel createMultiChannel(final byte[][] chunks) {
        final SeekableByteChannel[] channels = new SeekableByteChannel[chunks.length];
        for (int i = 0; i < channels.length; i++) {
            channels[i] = new SeekableInMemoryByteChannel(chunks[i]);
        }
        return MultiReadOnlySeekableByteChannel.forSeekableByteChannels(channels);
    }

    /**
     * A test utility class that throws an exception on close.
     */
    private static final class ThrowingSeekableByteChannel implements SeekableByteChannel {
        private boolean closed;

        @Override
        public void close() throws IOException {
            closed = true;
            throw new IOException("simulated close exception");
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
            return this;
        }

        @Override
        public int write(final ByteBuffer src) {
            return 0;
        }
    }

    @Test
    void concatenatingTwoEmptyChannelsShouldResultInAnEmptyChannel() throws IOException {
        // Arrange
        try (SeekableByteChannel channel = MultiReadOnlySeekableByteChannel.forSeekableByteChannels(
                createEmptyChannel(), createEmptyChannel())) {
            // Act & Assert
            assertBehavesAsEmptyChannel(channel);
        }
    }
}